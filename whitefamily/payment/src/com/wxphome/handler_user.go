
package main

import (
	"log"
	"fmt"
	"com/glwapi"
	"net/http"
)


func handler_uer_token(w http.ResponseWriter, r *http.Request) {
	g := glwapi.D()
	if g.IsInit() == false {
		fmt.Fprintf(w, "glwapi not inital")
	}
	code_url, _, e := g.CodeURL("http://test.com", "state_code")
	if e != nil {
		//redirect
		log.Fatal("%s", e)
	} else {
		log.Printf("%s\n", code_url)
	}
	
}
