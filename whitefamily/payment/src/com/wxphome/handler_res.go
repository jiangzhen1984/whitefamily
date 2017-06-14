
package main


import (
	"net/http"
	"log"
)



func res_redirect(w http.ResponseWriter, r * http.Request) {
	var uri = r.RequestURI
	name := uri[4:]
	log.Printf("%s  ===%s \n", name, uri)
	http.ServeFile(w, r, "/home/CORPUSERS/28851274/github/whitefamily/whitefamily/payment/web/"+name)
}
