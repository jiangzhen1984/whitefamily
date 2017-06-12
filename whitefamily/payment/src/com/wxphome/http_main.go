

package main

import (
	"log"
	"net/http"
)

func main() {
	//init glwapi
	InitGlwapi()

	go auth()

	//init server
	ser := &http.Server {
		Addr	:	":8083",
	}

	//set handler
	http.HandleFunc("/user/auth", handler_uer_token)
	http.HandleFunc("/auth", auth_handler)
	http.HandleFunc("/order", order_create_handler)
	http.HandleFunc("/order/create", order_create_handler)
	http.HandleFunc("/wechat", wechat_user_auth_handler)
	http.HandleFunc("/wechat/paymentresult", wechat_paymentresult_handler)
	http.HandleFunc("/js/auth", js_auth_handler)

	log.Fatal(ser.ListenAndServe())
}
