
package main


import (
	"net/http"
	"log"
)



func res_redirect(w http.ResponseWriter, r * http.Request) {
	var uri = r.RequestURI
	name := uri[4:]
	http.ServeFile(w, r, "./web/"+name)
}

func res_redirect1(w http.ResponseWriter, r * http.Request) {
	var uri = r.RequestURI
	name := uri[4:]
	log.Printf("11%s  ===%s \n", name, uri)
	http.ServeFile(w, r, "./web/"+name)
}
