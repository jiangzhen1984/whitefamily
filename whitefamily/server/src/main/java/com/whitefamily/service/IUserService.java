package com.whitefamily.service;

import java.util.List;

import com.whitefamily.service.vo.WFUser;

public interface IUserService {
	
	public WFUser getUser(long id);
	
	public WFUser getUser(String username);

	public WFUser login(String username, String password);


	public Result createUser(WFUser user);
	
	public Result updateUser(WFUser user);
	
	public Result deleteUser(long id);
	
	
	public List<WFUser> queryUser(int start, int count);
	

}
