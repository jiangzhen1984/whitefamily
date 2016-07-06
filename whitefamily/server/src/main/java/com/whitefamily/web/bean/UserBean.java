package com.whitefamily.web.bean;

import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.whitefamily.service.IUserService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFUser;


@ManagedBean(name = "userBean", eager = false)
@SessionScoped
public class UserBean {
	
	IUserService userService;
	
	private String userName;
	private String password;
	
	private boolean isLogined;
	
	
	private String errMsg;
	
	WFUser user;
	

	public UserBean() {
		super();
		userService = ServiceFactory.getUserService();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLogined() {
		return isLogined;
	}

	public void setLogined(boolean isLogined) {
		this.isLogined = isLogined;
	}
	
	
	
	
	public WFUser getUser() {
		return user;
	}

	public void setUser(WFUser user) {
		this.user = user;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String login() {
		errMsg = null;
		
		if (userName == null || userName.isEmpty()) {
			errMsg = "请输入用户名";
			return "loginfailed";
		}
		
		if (password == null || password.isEmpty()) {
			errMsg = "请输入密码";
			return "loginfailed";
		}
		
		user = userService.login(userName, password);
		if (user == null) {
			errMsg = "登陆失败， 用户名或密码错误";
			return "loginfailed";
		}
		
		isLogined = true;
		
		String agent = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("user-agent");
		if (agent == null || agent.isEmpty()) {
			return "dashboard";
		}
		boolean client = Pattern.matches("(micromessenger/)", agent);
		if (client) {
			return "mobiledashboard";
		} else {
			return "dashboard";
		}
	}
	
	public String logout() {
		user = null;
		isLogined = false;
		return "logout";
	}
	

}
