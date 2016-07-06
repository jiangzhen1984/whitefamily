package com.whitefamily.web.bean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import com.whitefamily.po.customer.AccountType;
import com.whitefamily.po.customer.Role;
import com.whitefamily.service.IShopService;
import com.whitefamily.service.IUserService;
import com.whitefamily.service.Result;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFManager;
import com.whitefamily.service.vo.WFShop;
import com.whitefamily.service.vo.WFUser;

@ManagedBean(name = "userManagementBean", eager = false)
@SessionScoped
public class UserManagementBean {

	private IUserService userService;

	private IShopService shopService;

	private String name;
	
	private String userName;

	private String password;
	
	private String passwordConfirm;

	private Role role;

	private long shopId;

	private String shopName;

	private String errMsg;
	
	private long userId;

	public UserManagementBean() {
		super();
		userService = ServiceFactory.getUserService();
		shopService = ServiceFactory.getShopService();
	}

	public List<WFUser> getUserList() {
		return userService.queryUser(0, 50);
	}

	public SelectItem[] getUserAccountType() {
		SelectItem[] items = new SelectItem[Role.values().length];
		int i = 0;
		for (Role g : Role.values()) {
			items[i++] = new SelectItem(g, g.getValue());
		}
		return items;
	}
	
	public SelectItem[] getUserRole() {
		SelectItem[] items = new SelectItem[Role.values().length];
		int i = 0;
		for (Role g : Role.values()) {
			items[i++] = new SelectItem(g, g.getValue());
		}
		return items;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	

	public IShopService getShopService() {
		return shopService;
	}

	public void setShopService(IShopService shopService) {
		this.shopService = shopService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	
	
	

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String gotoCreateUser() {
		reset();
		return "gotocreateuser";
	}
	

	public String gotoUpdateUser() {
		WFUser user = userService.getUser(userId);
		this.name = user.getName();
		this.userName = user.getUsername();
		this.password = user.getPassword();
		this.passwordConfirm = password;
		this.role = user.getRole();
		if (user.getRole() == Role.MANAGER) {
			this.shopId = ((WFManager)user).getShop().getId();
			this.shopName = ((WFManager)user).getShop().getName();
		}
		return "gotoupdateuser";
	}
	

	public String createOrUpdateUser() {
		errMsg = null;
		Result res = null;
		
		if (name == null || name.isEmpty()) {
			errMsg = "请输入雇员姓名";
			return "failed";
		}
		
		if (userName == null || userName.isEmpty()) {
			errMsg = "请输入雇员系统登陆名";
			return "failed";
		}
		
		// means add user
		if (userId <= 0 || (userId > 0 && (password != null && passwordConfirm != null))) {
			if (password == null || password.isEmpty()) {
				errMsg = "请输入雇员系统登陆密码";
				return "failed";
			}
			
			if (passwordConfirm == null || passwordConfirm.isEmpty()) {
				errMsg = "请输入雇员系统登陆确认密码";
				return "failed";
			}
			
			if (!password.equals(passwordConfirm)) {
				errMsg = "两次输入密码不一致";
				return "failed";
			}
		
		}
		
		
		
		WFUser user = null;
		if (userId > 0) {
			user = userService.getUser(userId);
		} else {
			if (role == Role.MANAGER) {
				if (shopId <= 0) {
					errMsg = "请输入所属店铺";
					return "failed";
				}
				WFShop wfshop = shopService.getShop(shopId);
				if (wfshop == null) {
					errMsg = "请输入所属店铺";
					return "failed";
				}
				WFManager m = new WFManager();
				m.setShop(wfshop);
				user = m;
				
			} else {
				user = new WFUser();
			}
		}
		
		user.setName(name);
		user.setUsername(userName);
		user.setPassword(password);
		user.setRole(role);
		user.setAccountType(AccountType.NORMAL);
		if (userId > 0) {
			res = userService.updateUser(user);
		} else {
			res = userService.createUser(user);
		}

		if (res != Result.SUCCESS) {
			errMsg = res == Result.ERR_USER_ALREADY_EXIST ? "用户已经存在" : "创建用户失败";
			return "failed";
		}
		reset();
		return "success";
	}

	private void reset() {
		name = null;
		userName = null;
		password = null;
		shopId = 0;
		shopName = null;
		errMsg = null;
		userId = 0;
	}
}
