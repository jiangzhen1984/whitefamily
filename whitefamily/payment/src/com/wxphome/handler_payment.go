
package main

import (
	"fmt"
	"net/http"
	"com/glwapi"
)


func wechat_paymentresult_handler(w http.ResponseWriter, r * http.Request) {
	var buf []byte= make([]byte, 2048, 4096)
	r.Body.Read(buf)

	LI("===>\n%s\n", buf)
	order := glwapi.WeChatOrder{}
	er := glwapi.DecodePaymentResult(&order, buf)
	if er != nil {
		data, _ :=  glwapi.EncodePaymentResultRespXml(glwapi.PAYMENT_R_FAIL, "fail")
		fmt.Fprintf(w, data)
	} else {
		data, e1 :=  glwapi.EncodePaymentResultRespXml(glwapi.PAYMENT_R_SUCCESS, "ok")
		fmt.Fprintf(w, data)
		if e1 != nil {
			LI(e1.Error())
		}
		//TODO remove cache
		LI("====>order sn:%s   trans Id:%s\n", order.OrderNo, order.TransId)
		tp := transUserholder[order.Attach]
		if tp != nil {
			LI(" Post payment result %s\n", tp.NotiU)
			LI(tp.NotiU+"?back_data="+ tp.BackD+"&order_no="+tp.OrderNo+"&payment_result=0")
			http.Get(tp.NotiU+"?back_data="+ tp.BackD+"&order_no="+tp.OrderNo+"&payment_result=0")
		}
		delete(transUserholder, order.Attach)
	}
}
