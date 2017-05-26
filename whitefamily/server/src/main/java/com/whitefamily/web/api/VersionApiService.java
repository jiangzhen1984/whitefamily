package com.whitefamily.web.api;

import org.json.JSONObject;

@ApiMapping(name ="version", method=Method.GET, action="/version")
public class VersionApiService implements ApiService {

	@Override
	public JSONObject onService(JSONObject data) {
		JSONObject obj = new JSONObject();
		obj.put("version", "0.1");
		return obj;
	}

}
