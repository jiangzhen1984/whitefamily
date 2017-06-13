package com.whitefamily.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.whitefamily.po.customer.AccountType;
import com.whitefamily.po.customer.Role;
import com.whitefamily.po.customer.User;
import com.whitefamily.service.vo.Franchisee;
import com.whitefamily.service.vo.WFManager;
import com.whitefamily.service.vo.WFShop;
import com.whitefamily.service.vo.WFUser;
import com.whitefamily.service.vo.WFVegetableSupplier;


public class UserService extends BaseService implements IUserService {

	private IShopService shopService;

	public IShopService getShopService() {
		return shopService;
	}

	public void setShopService(IShopService shopService) {
		this.shopService = shopService;
	}

	public WFUser getUser(long id) {
		Session sess = getSession();
		User u = (User)sess.get(User.class, id);
		WFUser wfu = populateUser(u);
		return wfu;
	}
	
	public WFUser getUser(long id, Session sess) {
		User u = (User)sess.get(User.class, id);
		WFUser wfu = populateUser(u);
		return wfu;
	}

	public WFUser getUser(String username) {
		Session sess = getSession();
		Query query = sess
				.createQuery(" from User where username = ?  ");
		query.setString(0, username);
		List<User> list = query.list();
		if (list.size() > 0) {
			User u = list.iterator().next();
			sess.close();
			return populateUser(u);
		}
		sess.close();
		return null;
	}
	
	
	public WFUser getUser(String username, Session sess) {
		Query query = sess
				.createQuery(" from User where username = ?  ");
		query.setString(0, username);
		List<User> list = query.list();
		if (list.size() > 0) {
			User u = list.iterator().next();
			return populateUser(u);
		}
		return null;
	}

	@Override
	public WFUser login(String username, String password) {
		Session sess = getSession();
		Query query = sess
				.createQuery(" from User where username = ? and password = ? ");
		query.setString(0, username);
		query.setString(1, password);
		List<User> list = query.list();
		if (list.size() > 0) {
			User u = list.iterator().next();
			//let proxy close session
			return populateUser(u);
		}
		sess.close();
		return null;
	}
	
	
	private WFUser populateUser(User u) {
		WFUser wfu = null;
		if (u.getRole() == Role.MANAGER) {
			WFManager m = new WFManager();
			m.setId(u.getId());
			m.setAccountType(AccountType.NORMAL);
			m.setName(u.getName());
			m.setRole(Role.MANAGER);
			if (u.getShopId() > 0) {
				WFShop shop =shopService.getShop(u.getShopId());
				shop.setId(u.getShopId());
				m.setShop(shop);
			}
			wfu = m;
		} else if (u.getRole() == Role.VEGETABLE_SUPPLIER){
			wfu = new WFVegetableSupplier();
			wfu.setId(u.getId());
		} else if (u.getRole() == Role.FRANCHISEE) {
			Franchisee m = new Franchisee();
			m.setId(u.getId());
			m.setAccountType(AccountType.NORMAL);
			m.setName(u.getName());
			m.setRole(Role.FRANCHISEE);
			if (u.getShopId() > 0) {
				WFShop shop =shopService.getShop(u.getShopId());
				shop.setId(u.getShopId());
				m.setShop(shop);
			}
			wfu = m;
		} else {
			wfu = new WFUser();
			wfu.setId(u.getId());
		}
		wfu.setId(u.getId());
		wfu.setRole(u.getRole());
		wfu.setAccountType(u.getAccountType());
		wfu.setUsername(u.getUsername());
		wfu.setName(u.getName());
		return wfu;
	}


	
	public Result createUser(WFUser user) {
		if (user == null) {
			throw new NullPointerException(" user is null ");
		}
		
		if (user.getRole() == Role.MANAGER && ((WFManager)user).getShop() == null) {
			return Result.ERR_SHOP_NOT_EXIST;
		}
		
		Session sess = getSession();
		beginTransaction(sess);
		WFUser wfu = getUser(user.getUsername(), sess);
		if (wfu != null) {
			this.rollbackTrans();
			return Result.ERR_USER_ALREADY_EXIST;
		}
		User u = new User();
		u.setUsername(user.getUsername());
		u.setPassword(user.getPassword());
		u.setAccountType(user.getAccountType());
		u.setRole(user.getRole());
		u.setName(user.getName());
		u.setShopId(user.getShopId());
		u.setShopName(user.getShopName());
		u.setShopAddress(user.getShopAddress());
		sess.save(u);
		sess.flush();
		commitTrans();
		
		user.setId(u.getId());
		return Result.SUCCESS;
		
	}
	
	public Result updateUser(WFUser user) {
		if (user == null) {
			throw new NullPointerException(" user is null ");
		}
		
		if (user.getRole() == Role.MANAGER && ((WFManager)user).getShop() == null) {
			return Result.ERR_SHOP_NOT_EXIST;
		}
		
		Session sess = getSession();
		beginTransaction(sess);
		WFUser wfu = getUser(user.getUsername(), sess);
		if (wfu != null) {
			return Result.ERR_USER_ALREADY_EXIST;
		}
		User u = this.getUser(user.getId());
		u.setUsername(user.getUsername());
		u.setPassword(user.getPassword());
		u.setAccountType(user.getAccountType());
		u.setRole(user.getRole());
		u.setName(user.getName());
		
		Query q = sess.createQuery(" delete from ManagerShop where manager.id= ?" );
		q.setLong(0, user.getId());
		q.executeUpdate();
		
		sess.update(u);
		commitTrans();
		
		return Result.SUCCESS;
	}
	
	
	
	public Result deleteUser(long id) {
		Session sess = getSession();
		User u = (User)sess.get(User.class, id);
		beginTransaction(sess);
		sess.delete(u);
		commitTrans();
		 
		return Result.SUCCESS;
	}
	
	public List<WFUser> queryUser(int start, int count) {
		Session sess = getSession();
		Query query = sess.createQuery(" from User order by id ");
		query.setFirstResult(start);
		query.setMaxResults(count);
		List<User> list = query.list();
		List<WFUser> wfList = new ArrayList<WFUser>(list.size());
		for (User u : list) {
			WFUser wfu = populateUser(u);
			wfList.add(wfu);
		}
		sess.close();
		return wfList;
		
		
	}

}
