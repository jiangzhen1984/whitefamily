package com.whitefamily.web.bean.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public interface JSONRequestRender {
	
	JSONObject onGetRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	JSONObject onPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;


}
