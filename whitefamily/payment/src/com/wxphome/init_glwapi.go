
package main

import (
	"log"
	"com/glwapi"
	"encoding/json"
	"io/ioutil"
)

type Config struct {
	Appid   string  `json:"appid"`
	Secret  string `json:"secret"`
	MchId  string  `json:"mchid"`
	MchApiKey  string `json:"apikey"`
}

func InitGlwapi() {
	var data = Config{}
	bs, e := ioutil.ReadFile("./bin/config.json")
	if e != nil {
		log.Fatal(e)
	}
	json.Unmarshal(bs, &data)
	log.Printf("%s", data)
	g := glwapi.D()
	ret := g.Init(data.Appid, data.Secret, data.MchId, data.MchApiKey)
	if ret != true {
		log.Fatal(" initalize glwapi failed")
	}
}


func InitToken() {

}
