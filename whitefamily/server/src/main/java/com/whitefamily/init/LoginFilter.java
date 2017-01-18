package com.whitefamily.init;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hsr =(HttpServletRequest) request;
		HttpServletResponse hres =(HttpServletResponse) response;
		HttpSession sess = hsr.getSession(false);
		if (sess == null) {
			hres.sendRedirect(hsr.getContextPath()+"/login.xhtml");
			return;
		} else {
			LoginChecker checker = (LoginChecker)sess.getAttribute("userBean");
			if (checker == null || !checker.isLogined()) {
				hres.sendRedirect(hsr.getContextPath()+"/login.xhtml");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
