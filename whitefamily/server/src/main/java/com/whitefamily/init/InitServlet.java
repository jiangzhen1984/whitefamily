package com.whitefamily.init;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.whitefamily.ServerConstants;
import com.whitefamily.service.ServiceFactory;

public class InitServlet extends GenericServlet {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7914610720398902782L;
	
	

	@Override
	public void destroy() {
		super.destroy();
		if (ServiceFactory.sessionFactory != null) {
			ServiceFactory.sessionFactory.close();
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String name = config.getInitParameter("server_context_conf_name");
		if (name == null) {
			name = "server_context_conf.prop";
		}
		InputStream reader = this.getClass().getResourceAsStream(name);
		Properties prop = new Properties();
		try {
			prop.load(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ServerConstants.getInstance().init(prop);
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {

	}

}
