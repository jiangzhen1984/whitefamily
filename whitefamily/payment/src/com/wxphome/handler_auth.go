
package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"com/glwapi"
)


func auth() {
	g := glwapi.D()
	if g.IsInit() == false {
		LE("glwapi doesn't inital yet")
		return
	}
	token_url := g.TokenURL()
	r, err :=  http.Get(token_url)	
	if err == nil {
		defer r.Body.Close()
		rdata, _ := ioutil.ReadAll(r.Body)
		_, e := g.HandleTokenAuthResp([]byte(rdata))
		if e != nil {
			LE("Handle auth response error:%s", e)
		} else {
			LI("Get auth token successfully: %s\n", g)
		}
	} else {
		LE("Request auth failed error:%s   url:%s\n", err, token_url)
	}
}

func auth_handler(resp http.ResponseWriter, req *http.Request) {
	g := glwapi.D()
	token_url := g.TokenURL()
	r, err :=  http.Get(token_url)	
	if err == nil {
		defer r.Body.Close()
		rdata, _ := ioutil.ReadAll(r.Body)
		g.HandleTokenAuthResp([]byte(rdata))
		fmt.Fprintf(resp, g.String())
	} else {
		log.Fatal("====")
		fmt.Fprintf(resp, " Token get error: %s", err)
	}
}
