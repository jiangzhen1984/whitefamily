package com.whitefamily.service;

import java.lang.reflect.Proxy;

import org.hibernate.SessionFactory;

public class ServiceFactory {
	
	private static ICategoryService icsReal;
	
	private static IGoodsService igsReal;
	
	private static IShopService issReal;
	
	private static IUserService iusReal;
	
	private static ISupplierService isupsReal;
	
	
	public static ICategoryService ics;
	
	public static IGoodsService igs;
	
	public static IShopService iss;
	
	public static IInventoryService  iis;
	
	public static IUserService ius;
	
	public static ISupplierService isups;
	
	public static SessionFactory sessionFactory;
	
	public static ICategoryService  getCategoryService() {
		if (ics == null) {
			ics = (ICategoryService)getProxy(ICategoryService.class.getClassLoader(), ICategoryService.class, getRealCategoryService());
		}
		return ics;
	}

	
	
	public static IGoodsService  getGoodsService() {
		if (igs == null) {
			((GoodsService)getRealGoodsService()).setCateService(getRealCategoryService());
			igs = (IGoodsService)getProxy(IGoodsService.class.getClassLoader(), IGoodsService.class, getRealGoodsService());
		}
		return igs;
	}
	
	
	public static IShopService  getShopService() {
		if (iss == null) {
			((ShopService)getRealShopService()).setGoodsService(getRealGoodsService());
			((ShopService)getRealShopService()).setUserService(getRealUserService());
			((ShopService)getRealShopService()).setSupplierService(getRealSupplierService());
			iss = (IShopService)getProxy(IShopService.class.getClassLoader(), IShopService.class, getRealShopService());
		}
		return iss;
	}
	
	
	public static IInventoryService  getInventoryService() {
		if (iis == null) {
			IInventoryService  service = new InventoryService();
			((InventoryService)service).setGoodsService(getRealGoodsService());
			((InventoryService)service).setSupplierService(getRealSupplierService());
			iis = (IInventoryService)getProxy(IInventoryService.class.getClassLoader(), IInventoryService.class, service);
		}
		return iis;
	}
	
	
	public static IUserService  getUserService() {
		if (ius == null) {
			((UserService)getRealUserService()).setShopService(getRealShopService());
			ius = (IUserService)getProxy(IUserService.class.getClassLoader(), IUserService.class, getRealUserService());
		}
		return ius;
	}
	
	
	public static ISupplierService  getSupplierService() {
		if (isups == null) {
			isups = (ISupplierService)getProxy(ISupplierService.class.getClassLoader(), ISupplierService.class, getRealSupplierService());
		}
		return isups;
	}
	
	
	private static synchronized IUserService getRealUserService() {
		if (iusReal == null) {
			iusReal = new UserService();
		}
		return iusReal;
	}
	
	
	private static synchronized IGoodsService getRealGoodsService() {
		if (igsReal == null) {
			igsReal = new GoodsService();
		}
		return igsReal;
	}
	
	
	
	private static synchronized IShopService getRealShopService() {
		if (issReal == null) {
			issReal = new ShopService();
		}
		return issReal;
	}
	
	private static synchronized ICategoryService getRealCategoryService() {
		if (icsReal == null) {
			icsReal = new CategoryService();
		}
		return icsReal;
	}
	
	
	private static synchronized ISupplierService getRealSupplierService() {
		if (isupsReal == null) {
			isupsReal = new SupplierService();
		}
		return isupsReal;
	}
	
	private static Object getProxy(ClassLoader loder, Class cls, Object obj) {
		return Proxy.newProxyInstance(loder, 
			     new Class[]{cls}, 
			     new ServiceProxy(obj));
	}
	
	
}
