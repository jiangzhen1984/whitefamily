package com.whitefamily.web.api;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

/**
 * Servlet implementation class ApiPortal
 */
@WebServlet(asyncSupported = true, urlPatterns = { ApiPortal.URI+"/*" }, loadOnStartup=2)
public class ApiPortal extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Log logger = LogFactory.getLog(ApiPortal.class);
	
	private Map<String, Mapping> mapping = new HashMap<String, Mapping>();
	
	private static WebServlet anno = (WebServlet)ApiPortal.class.getAnnotation(WebServlet.class);
	public static final String URI ="/api";
	
	static class Mapping {
		String name;
		String action;
		Method method;
		ApiService  service;
		@Override
		public String toString() {
			return "Mapping [name=" + name + ", action=" + action + ", method=" + method + ", service=" + service + "]";
		}
		
		
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiPortal() {
    
    }
    
    
    

	@Override
	public void destroy() {
		super.destroy();
		mapping.clear();
		logger.info("====clear api service ");
	}
 



	@Override
	public void init() throws ServletException {
		super.init();
		
		Class base = ApiService.class;
    	Class ano = ApiMapping.class;
    	Package p = base.getPackage();
        ClassLoader cl = base.getClassLoader();
        List<Class> list = new ArrayList<Class>();
        try {
			Enumeration<URL> rs = cl.getResources("com/whitefamily/web/api");
			while (rs.hasMoreElements())
			{
				URL url = rs.nextElement();
				File dir = new File(url.getFile());
				File[] files = dir.listFiles();

				for (File f : files) {
					logger.debug(f.getAbsolutePath());
					String className = f.getName().split("\\.")[0];
					list.add(Class.forName(p.getName()+"."+className, true, cl));
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        for (Class c : list) {
        	Annotation an = c.getDeclaredAnnotation(ano);
        	if (an == null) {
        		continue;
        	}
        	
        	ApiMapping  am = (ApiMapping)an;
        	
        	Class ints[] = c.getInterfaces();
    		for (Class i : ints) {
    			if (i.getName().equals(base.getName())) {
    				Mapping m = new Mapping();
    				try {
						m.service = (ApiService)c.newInstance();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
    				m.name = am.name();
    				m.action = am.action();
    				m.method = am.method();
    				logger.info("===initilaized api service mapping===>"+m.toString());
    				mapping.put(m.action, m);
    				break;
    			} 
    		}
        	
        }
	}




	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject result = new JSONObject();
		ApiService api = getService(request, response, Method.GET);
		if (api != null) {
			result.put("ret", 0);
			result.put("data", api.onService(null));
		} else {
			result.put("ret", -1);
		}
		
		response.getWriter().append(result.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject result = new JSONObject();
		ApiService api = getService(request, response, Method.POST);
		if (api != null) {
			result.put("ret", 0);
			result.put("data", api.onService(null));
		} else {
			result.put("ret", -1);
		}
		
		response.getWriter().append(result.toString());
	}
	
	private ApiService getService(HttpServletRequest request, HttpServletResponse response, Method m) {
		if (mapping == null || mapping.size() ==0) {
			return null;
		}
		
		String uri = request.getRequestURI();
		uri = uri.replaceFirst(request.getContextPath(), "");
		uri = uri.replaceFirst(URI, "");
		
		Mapping ser = mapping.get(uri);
		if (ser == null) {
			return null;
		} else if (ser.method == Method.ANY) {
			return ser.service;
		} else if (ser.method == m) {
			return ser.service;
		}  else {
			return null;
		}
			
	}

}
