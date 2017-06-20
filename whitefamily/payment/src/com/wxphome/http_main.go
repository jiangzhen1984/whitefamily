

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
		//InsecureSkipVerify: true,
	}

	//set handler
	http.HandleFunc("/user/auth", wechat_user_auth_handler)
	http.HandleFunc("/auth", auth_handler)
	http.HandleFunc("/order", order_create_handler)
	http.HandleFunc("/order/pay", order_pay_handler)
	http.HandleFunc("/order/create", order_create_handler)
	http.HandleFunc("/order/query", order_query_handler)
	http.HandleFunc("/wechat", wechat_user_auth_handler)
	http.HandleFunc("/wechat/paymentresult", wechat_paymentresult_handler)
	http.HandleFunc("/js/auth", js_auth_handler)
	http.HandleFunc("/res/", res_redirect)
	http.HandleFunc("*.png", res_redirect1)


	//log.Fatal(ser.ListenAndServeTLS("bin/apache.crt", "bin/apache.key"))
	log.Fatal(ser.ListenAndServe())
}
