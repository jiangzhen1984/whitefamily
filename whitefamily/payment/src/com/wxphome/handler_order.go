

package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"io/ioutil"
	"strings"
	"com/glwapi"
	"html/template"
	"log"
)


type OrderPaymentFormData struct {
	OID		string	`json:"order_id"`
	OD		string	`json:"order_desc"`
	BU		string	`json:"back_url"`
	Fee		string	`json:"fee"`
	BD		string	`json:"back_data"`
	UID		string	`json:"open_id"`
	CIP		string	`json:"ip"`
}


type OrderPaymentResp struct {
	Error	int	`json:"error"`
	Msg	string	`json:"emsg"`
	OID	string	`json:"oid"`
	WxOId	string	`json:"wxid"`
	AppIdP	string	`json:"appid"`
	JSTS	string	`json:"jsts"`
	JSST	string	`json:"jsst"`
	JSN	string	`json:"jsn"`
	JSS	string	`json:"jss"`
}


type PayTpl struct {
	Error	int
	OrderNo	string
	AppId	string
	JSS	string
	JSN	string
	JSTS	string
	PayTS	string
	PayST	string
	PayN	string
	PayS	string
	BackU	string
	BackD	string
	SN	string
}


type UserOrder struct {
	BackU	string
	BackD	string
	NotiU	string
	Fee	string
	OrderSn	string
	OrderNo	string
	OrderD	string
	Ip	string
	OpenId	string

}




var transUserholder map[string]*UserOrder = make(map[string]*UserOrder)
var transholder map[string]*PayTpl = make(map[string]*PayTpl)


func  order_create_handler(w http.ResponseWriter, r *http.Request) {
	if r.Method != "POST" {
		data, _ :=json.Marshal(&OrderPaymentResp{Error : -2})
		fmt.Fprintf(w, string(data))
		return
	}

	g := glwapi.D()
	if g.WeChat.IsTokened() == false {
		data, _ :=json.Marshal(&OrderPaymentResp{Error : -3})
		fmt.Fprintf(w, string(data))
		return
	}
	var fd = &OrderPaymentFormData{}
	if err := json.NewDecoder(r.Body).Decode(&fd); err == nil {
		if fd.OID == "" || fd.OD == "" || fd.BU == "" || fd.Fee == "" || fd.BD == "" || fd.CIP =="" {
			data, _ :=json.Marshal(&OrderPaymentResp{Error : -2})
			fmt.Fprintf(w, string(data))
			return
		}

		fee, _ := strconv.Atoi(fd.Fee)
		if fee <= 0  {
			data, _ :=json.Marshal(&OrderPaymentResp{Error : -3})
			fmt.Fprintf(w, string(data))
			return
		}

		if fd.UID == "" {
			//TODO to get user open id
		}
		fd.UID = "oL2LKvlLxlkRtgSwqImr1IL1vkPc"
		o, e := g.WeChat.CreateOrder1(&glwapi.WeChatUser{OpenId : fd.UID}, fd.OID, fd.BD, fd.BD, "", fd.CIP, "http://wechat.wxphome.cn/wechat", fee)
		if e != nil {
			data, _ :=json.Marshal(&OrderPaymentResp{Error : -4})
			fmt.Fprintf(w, string(data))
			return
		}

		xml , e1:= glwapi.EncodeCreateOrderXml(o)
		if e1 != nil {
			data, _ :=json.Marshal(&OrderPaymentResp{Error : -5})
			fmt.Fprintf(w, string(data))
			log.Printf("%s\n", e1)
			return
		}

		LI(xml)

		xmlreader := strings.NewReader(xml)
		r, e2 := http.Post(glwapi.PAYMENT_URL_CO, "text/xml", xmlreader)
		if e2 != nil {
			data, _ :=json.Marshal(&OrderPaymentResp{Error : -6})
			fmt.Fprintf(w, string(data))
			log.Printf("%s\n", e2)
			return
		}

		defer r.Body.Close()
		body , _ := ioutil.ReadAll(r.Body)
		if e2 != nil {
			data, _ :=json.Marshal(&OrderPaymentResp{Error : -7})
			fmt.Fprintf(w, string(data))
			log.Printf("%s\n", e2)
			return
		}

		e4 := glwapi.DecodeCreateOrderResp(o, body)
		if e4 != nil {
			data, _ :=json.Marshal(&OrderPaymentResp{Error : -8})
			fmt.Fprintf(w, string(data))
			log.Printf("%s\n", e4)
			return
		}
		opr := &OrderPaymentResp{Error : 0, OID: o.OrderNo, WxOId : o.PrepayId}
		jss := o.JsSign()
		opr.JSN = jss.N
		opr.JSS = jss.S
		opr.JSST = jss.ST
		opr.JSTS = jss.TS
		data, _ :=json.Marshal(opr)
		fmt.Fprintf(w, string(data))
	} else {
		data, _ :=json.Marshal(&OrderPaymentResp{Error : -9, Msg : err.Error()})
		fmt.Fprintf(w, string(data))
	}

}


func do_js_auth(tpl * PayTpl, u string) {
	g := glwapi.D()
        if g.WeChat.IsGotJSToken() == false {
                js_ticket_url, _ := g.GetJSTicketURL()
                resp, e := http.Get(js_ticket_url)
                if e != nil {
                        //FIXME should retry
			LE("%s", e)
                        return
                }

                defer resp.Body.Close()
                js_ticket_resp, _ := ioutil.ReadAll(resp.Body)
                _, e = g.HandleTicketResp(js_ticket_resp)
                if e != nil {
			LE("%s", e)
                        //FIXME should retry
                        return
                }
        }

        js, e := g.GetJSAuth(g.WeChat.JSToken, u)
        if e != nil {
		LE("%s", e)
                return
        }

        js.N()
        js.T()
        js.S()
        LI(js.String())
	tpl.JSS = js.Sign
	tpl.JSN = js.Nonce
	tpl.JSTS = js.Ts

}


func order_pay_handler(w http.ResponseWriter, r *http.Request) {
	//TODO get user openid
	//testOpenId := "oL2LKvlLxlkRtgSwqImr1IL1vkPc"

	//TODO create order and get js auth
	g := glwapi.D()
	if g.WeChat.IsTokened() == false {
		render_error(w, -3, "")
		return
	}

	var sn string = r.FormValue("state")
	var uo * UserOrder
	var ok bool
	if sn == "" {
		uo = new (UserOrder)
		uo.OrderNo = r.FormValue("order_no")
		uo.Fee     = r.FormValue("order_fee")
		uo.OrderD  = r.FormValue("order_desc")
		uo.BackD   = r.FormValue("back_data")
		uo.BackU   = r.FormValue("back_url")
		uo.Ip      = strings.Split(r.RemoteAddr, ":")[0]
		uo.OrderSn = glwapi.R(32)
		transUserholder[uo.OrderSn] = uo
		handler_user_order_token(w, r, uo)
		return;
	} else {
		uo, ok = transUserholder[sn]
		if !ok {
			LE("State code error")
			return
		}
	}

	fee,_ := strconv.Atoi(uo.Fee)
	o, e := g.WeChat.CreateOrder1(&glwapi.WeChatUser{OpenId : uo.OpenId}, uo.OrderNo, uo.OrderD, uo.OrderD, uo.OrderSn,  uo.Ip, "http://payment.wxphome.cn/wechat/paymentresult", fee)
	if e != nil {
		LE("%s", e)
		render_error(w, -4, uo.BackU)
		return
	}

	xml , e1:= glwapi.EncodeCreateOrderXml(o)
	if e1 != nil {
		LE("%s", e1)
		render_error(w, -5, uo.BackU)
		return
	}

	LI(xml)

	xmlreader := strings.NewReader(xml)
	r1, e2 := http.Post(glwapi.PAYMENT_URL_CO, "text/xml", xmlreader)
	if e2 != nil {
		LE("%s", e2)
		render_error(w, -6, uo.BackU)
		return
	}

	defer r1.Body.Close()
	body , _ := ioutil.ReadAll(r1.Body)
	e4 := glwapi.DecodeCreateOrderResp(o, body)
	if e4 != nil {
		LE("%s", e4)
		render_error(w, -8, uo.BackU)
		return
	}

	jss := o.JsSign()

	pt := PayTpl{}

	LI("http://" + r.Host+r.URL.String())
	do_js_auth(&pt, "http://" + r.Host+r.URL.String())
	pt.OrderNo = jss.P
	pt.AppId = jss.AppId
	pt.PayS = jss.S
	pt.PayN = jss.N
	pt.PayTS = jss.TS
	pt.PayST = jss.ST

	pt.SN = uo.OrderSn
	pt.BackD = uo.BackD
	pt.BackU = uo.BackU
	transholder[pt.SN] = &pt

	t, _ := template.ParseFiles("web/payment.html")
	t.Execute(w, &pt)
}



func order_query_handler(w http.ResponseWriter, r * http.Request) {
	g := glwapi.D()
	order := &glwapi.WeChatOrder{WeChat : g.WeChat}
	order.OrderNo = r.FormValue("orderno")
	data, e:= glwapi.EncodeQueryOrderXml(order)
	LV(VDebug,"%s\n", string(data))
	if e != nil {
		LE("%s\n", e)
		return
	}

	xmlreader := strings.NewReader(data)
	r1, e2 := http.Post(glwapi.PAYMENT_URL_QO, "text/xml", xmlreader)
	if e2 != nil {
		LE("%s\n", e2)
		return
	}

	defer r1.Body.Close()
	body , _ := ioutil.ReadAll(r1.Body)
	LI("%s\n", string(body))
	e4 := glwapi.DecodeQueryOrderResp(order, body)
	if e4 != nil {
		LE("%s", e4)
		return
	}
	LI("%s", order)

}



func render_error(w http.ResponseWriter, errcode int, u string) {
	pt := PayTpl{Error: errcode, BackU: u }
	LI("===> code : %d   url:%s\n", errcode, u)
	et, _ := template.ParseFiles("web/payment_error.html")
	et.Execute(w, &pt)
}
