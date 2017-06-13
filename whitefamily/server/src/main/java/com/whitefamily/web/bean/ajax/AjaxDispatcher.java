package com.whitefamily.web.bean.ajax;

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


/**
 * Servlet implementation class AjaxDispatcher
 */

@WebServlet(asyncSupported = true, urlPatterns = { "*.ajaxj" }, loadOnStartup=2)
public class AjaxDispatcher extends HttpServlet {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	private static final long serialVersionUID = 1L;
	
	
	private Map<String, Mapping> mapping = new HashMap<String, Mapping>();
	
	static class Mapping {
		String name;
		String action;
		HttpServlet  service;
		@Override
		public String toString() {
			return "Mapping [name=" + name + ", action=" + action + ", service=" + service + "]";
		}
		
		
	}
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxDispatcher() {
        super();
    }

	@Override
	public void destroy() {
		super.destroy();
		mapping.clear();
		logger.info("====clear ajax service ");
		
	}

	@Override
	public void init() throws ServletException {
		super.init();
		
		
		Class base = AjaxDispatcher.class;
    	Class ano = AjaxMapping.class;
    	Package p = base.getPackage();
        ClassLoader cl = base.getClassLoader();
        List<Class> list = new ArrayList<Class>();
        try {
			Enumeration<URL> rs = cl.getResources("com/whitefamily/web/bean/ajax");
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
        		logger.warn("Skip class " + c.getName());
        		continue;
        	}
        	
        	AjaxMapping  am = (AjaxMapping)an;
			Mapping m = new Mapping();
			try {
				m.service = (HttpServlet)c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			m.name = am.name();
			m.action = am.uri();
			logger.info("===initilaized ajax service mapping===>"+m.toString());
			mapping.put(m.action, m);
        }
	}

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		String uri = arg0.getRequestURI();
		int idx = uri.lastIndexOf('/');
		if (idx != -1) {
			uri = uri.substring(idx);
		}
		Mapping m = mapping.get(uri);
		if (m == null) {
			logger.error("No such mapping:" + uri);
			return;
		}
		if (m != null) {
			String method = arg0.getMethod();
			if (method.equals(METHOD_GET)) {
				((AjaxDispatcher)m.service).doGet(arg0, arg1);
			} else if (method.equals(METHOD_POST)) {
				((AjaxDispatcher)m.service).doPost(arg0, arg1);
			} else {
				throw e;
			}
		}
	}
	
	 private static ServletException e = new ServletException("Not support this method mapping");
	 private static final String METHOD_GET = "GET";
	 private static final String METHOD_POST = "POST";
	

	

}
