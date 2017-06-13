

package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"io/ioutil"
	"strings"
	"com/glwapi"
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
			return
		}

		LI(xml)

		xmlreader := strings.NewReader(xml)
		r, e2 := http.Post(glwapi.PAYMENT_URL_CO, "text/xml", xmlreader)
		if e2 != nil {
			data, _ :=json.Marshal(&OrderPaymentResp{Error : -6})
			fmt.Fprintf(w, string(data))
			return
		}

		defer r.Body.Close()
		body , _ := ioutil.ReadAll(r.Body)
		if e2 != nil {
			data, _ :=json.Marshal(&OrderPaymentResp{Error : -7})
			fmt.Fprintf(w, string(data))
			return
		}

		e4 := glwapi.DecodeCreateOrderResp(o, body)
		if e4 != nil {
			data, _ :=json.Marshal(&OrderPaymentResp{Error : -8})
			fmt.Fprintf(w, string(data))
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

func  order_query_handler(w http.ResponseWriter, r *http.Request) {
}
