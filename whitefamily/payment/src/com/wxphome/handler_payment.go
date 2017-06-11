
package main

import (
	"fmt"
	"net/http"
	"com/glwapi"
)


func wechat_paymentresult_handler(w http.ResponseWriter, r * http.Request) {
	var buf []byte= make([]byte, 2048, 4096)
	n, _ := r.Body.Read(buf)
	LI("===>%s====len:%d", string(buf),n)

	order := glwapi.WeChatOrder{}
	er := glwapi.DecodePaymentResult(&order, buf)
	LI("%s", order)
	if er != nil {
		data, _ :=  glwapi.EncodePaymentResultRespXml(glwapi.PAYMENT_R_FAIL, "fail")
		LI(data)
		fmt.Fprintf(w, data)
	} else {
		data, e1 :=  glwapi.EncodePaymentResultRespXml(glwapi.PAYMENT_R_SUCCESS, "ok")
		fmt.Fprintf(w, data)
		if e1 != nil {
			LI(e1.Error())
		}
		LI(data)
	}
}
