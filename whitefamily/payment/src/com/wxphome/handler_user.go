
package main

import (
	"log"
	"fmt"
	"com/glwapi"
	"net/http"
	"io/ioutil"
)


func handler_uer_token(w http.ResponseWriter, r *http.Request) {
	g := glwapi.D()
	if g.IsInit() == false {
		fmt.Fprintf(w, "glwapi not inital")
	}
	code_url, _, e := g.CodeURL("http://wechat.wxphome.cn/wechat", "state_code")
	if e != nil {
		//redirect
		log.Fatal("%s", e)
	} else {
		log.Printf("%s\n", code_url)
		http.Redirect(w, r, code_url, http.StatusSeeOther)
	}

}


func wechat_user_auth_handler(w http.ResponseWriter, r *http.Request) {
	g := glwapi.D()
	code, st_code, e := g.HandleCodeURLResponse(r)
	if e!= nil {
		fmt.Fprintf(w, "%s", e)
		return
	}
	LI(st_code)
	token_url, _ := g.UserTokenURL(code)
	LI(token_url)
	resp, e := http.Get(token_url)
	if e != nil {
		LE(e.Error())
		return
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		LE(err.Error())
		return
	}
	user, ue := g.HandleUserTokenURLResponse(body)
	if ue != nil {
		LI(ue.Error())
	}
	if user != nil {
		LI(user.OpenId)
	} else {
		LE("User is nil and no error")
	}
}
