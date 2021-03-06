
package main

import (
	"com/glwapi"
	"net/http"
	"encoding/json"
	"fmt"
	"io/ioutil"
)


type JsResp struct {
	Err	int	`json:"err"`
	AppId	string	`json:"appid"`
	N	string	`json:"nonce"`
	S	string	`json:"sign"`
	T	string	`json:"timestamp"`
	Tic	string	`json:"tick"`
}

func js_auth_handler(w http.ResponseWriter,  r * http.Request) {
	g := glwapi.D()
	if g.IsInit() == false {
		LE(" Doesn't init yet ")
		return
	}
	if g.WeChat.IsGotJSToken() == false {
		js_ticket_url, _ := g.GetJSTicketURL()
		resp, e := http.Get(js_ticket_url)
		if e != nil {
			//FIXME should retry
			bs, _ := json.Marshal(&JsResp{Err : -1})
			fmt.Fprintf(w, string(bs))
			return
		}

		defer resp.Body.Close()
		js_ticket_resp, _ := ioutil.ReadAll(resp.Body)
		_, e = g.HandleTicketResp(js_ticket_resp)
		if e != nil {
			//FIXME should retry
			bs, _ := json.Marshal(&JsResp{Err : -2})
			fmt.Fprintf(w, string(bs))
			return
		}
	}

	u := r.FormValue("url")
	js, e := g.GetJSAuth(g.WeChat.JSToken,	u)
	if e != nil {
		bs, _ := json.Marshal(&JsResp{Err : -3})
		fmt.Fprintf(w, string(bs))
		return
	}

	js.N()
	js.T()
	js.S()
	LI(js.String())
	bs, _ := json.Marshal(&JsResp{Err : 0, AppId: g.WeChat.AppId, N : js.Nonce, S: js.Sign, T: js.Ts, Tic : js.Ticket})
	fmt.Fprintf(w, string(bs))

}
