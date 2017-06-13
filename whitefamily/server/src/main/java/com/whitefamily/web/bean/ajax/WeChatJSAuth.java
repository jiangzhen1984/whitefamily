package com.whitefamily.web.bean.ajax;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.json.JSONTokener;

@AjaxMapping(name ="jsauth", uri="/jsauth.ajaxj", param="", uriMapping=true)
public class WeChatJSAuth extends AjaxDispatcherJson {

	@Override
	public JSONObject onGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject json = new JSONObject();
		json.put("ret", -1);
		return json;
	}

	@Override
	public JSONObject onPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		InputStream in = null;
		CloseableHttpResponse resp = null;
		JSONObject json = new JSONObject();
		String appid = null, ts = null, nonce = null, s = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet("http://localhost:8083/js/auth");
			resp = httpclient.execute(httpGet);
			HttpEntity entity1 = resp.getEntity();
			in = entity1.getContent();
			byte[] buf = new byte[2048];
			int n = in.read(buf);
			if (n >= 2048) {
				//FIXME should continue to read
			} else {
				String str = new String(buf, 0, n);
				JSONObject object = (JSONObject) new JSONTokener(str).nextValue();
				if (object.getInt("ret") == 0) {
					appid = object.getString("appid");
					ts = object.getString("ts");
					nonce = object.getString("nonce");
					s = object.getString("s");
					
				}
				
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (resp != null) {
				resp.close();
			}
		}
		 
		json.put("ret", 0);
		json.put("appid", appid);
		json.put("timestamp", ts);
		json.put("nonce", nonce);
		json.put("sign", s);
		
		return json;
	}

}
