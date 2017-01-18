#!/usr/bin/env python
import httplib, urllib
import requests


if __name__=="__main__":
        params = urllib.urlencode({"companyName": "18602283656", "password" : "123456", "loginName":""})
	cookie={"JSESSIONID" :"F4C03539B8DFB7526765881D91FFC04E"}
	headers = {
                "Host" : "store.youmeishi.cn",
                "Accept-Encoding":"gzip, deflate",
                "X-Requested-With":"XMLHttpRequest",
                "Accept" : "*/*",
                "User-Agent" :"Mozilla/5.0",
                "X-Requested-With" :"XMLHttpRequest",
                "Referer" :"http://store.youmeishi.cn/Identity/MainPage.do",
                "Content-Type" : "application/x-www-form-urlencoded; charset=UTF-8"}
	#conn = httplib.HTTPConnection("localhost:8080")
       	#conn.request("POST", "WhiteFamily/shop/dashboard.xhtml", params, headers)
        #response = conn.getresponse()
#	response = requests.post(url="http://wf.wxphome.cn/WhiteFamily/shop/dashboard.xhtml", headers=headers, json=params, cookies=cookie)
	response = requests.post(url="http://store.youmeishi.cn/Identity/LoginNew.do", headers=headers, json=params, cookies=cookie)
#	response = requests.post(url="http://store.youmeishi.cn", headers=headers, json=params, cookies=cookie)
#	response = requests.post(url="http://store.youmeishi.cn/Identity/MainPage.do", headers=headers, json=params, cookies=cookie)
        print response.status_code, response.reason
	data = response.content
        print response.cookies
	print data
