package com.whitefamily.web.bean.ajax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;


public abstract class AjaxDispatcherJson extends AjaxDispatcher  implements JSONRequestRender {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxDispatcherJson() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject data = onGetRequest(request, response);
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		if (data == null) {
			data = new JSONObject();
			data.put("ret", -1);
		} 
		out.print(data.toString());
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject data = onPostRequest(request, response);
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		if (data == null) {
			data = new JSONObject();
			data.put("ret", -1);
		} 
		out.print(data.toString());
		out.flush();
	}

}
