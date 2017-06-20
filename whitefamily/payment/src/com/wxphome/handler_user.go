
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

func handler_user_order_token(w http.ResponseWriter, r *http.Request, uo * UserOrder) bool {
	g := glwapi.D()
	code_url, _, e := g.CodeURL("http://payment.wxphome.cn/user/auth", uo.OrderSn)
	if e != nil {
		return false
	} else {
		log.Printf("user auth code url ===>%s\n", code_url)
		http.Redirect(w, r, code_url, http.StatusSeeOther)
		return true
	}
}



func wechat_user_auth_handler(w http.ResponseWriter, r *http.Request) {
	g := glwapi.D()
	LI("======%s\n", r.URL)
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
		uo, ok := transUserholder[st_code]
		if ok {
			LI("Get user OpenId:(%s)", user.OpenId)
			uo.OpenId = user.OpenId
			http.Redirect(w, r, "http://payment.wxphome.cn/order/pay?state="+st_code, http.StatusSeeOther)
		} else {
			LE("Doesn't find Sn:%s", st_code)
		}
	} else {
		LE("User is nil and no error")
	}
}
