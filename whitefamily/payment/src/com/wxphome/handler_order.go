

package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"io/ioutil"
	"strings"
	"github.com/glwapi/com/glwapi"
)


type OrderPaymentFormData struct {
	OID		string	`json:"order_id"`
	OD		string	`json:"order_desc"`
	BU		string	`json:"back_url"`
	Fee		string	`json:"fee"`
	BD		string	`json:"back_data"`
}


type OrderPaymentResp struct {
	Error	int	`json:"error"`
	
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
	if err := json.Unmarshal(r.Body, &fd); err == nil {
		if fd.OID == "" || fd.OD == "" || fd.BU == "" || fd.Fee == "" || fd.BD == "" {
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

		//TODO get user openid
		o, e := g.WeChat.CreateOrder1(nil, fd.OID, fd.BD, fd.BD, "", "127.0.0.1", "", fee)
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
		body , e3 := ioutil.ReadAll(r.Body)
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
			
		data, _ :=json.Marshal(&OrderPaymentResp{Error : -0})
		fmt.Fprintf(w, string(data))
	}
		
}
