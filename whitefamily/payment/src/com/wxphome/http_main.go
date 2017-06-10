

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
		Addr	:	":8080",
	}

	//set handler
	http.HandleFunc("/auth", auth_handler)
	http.HandleFunc("/order", order_create_handler)
	http.HandleFunc("/order/create", order_create_handler)

	log.Fatal(ser.ListenAndServe())
}
