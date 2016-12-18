package com.whitefamily.service;

import java.lang.reflect.Proxy;

import org.hibernate.SessionFactory;

public class ServiceFactory {
	
	private static ICategoryService icsReal;
	
	private static IGoodsService igsReal;
	
	private static IShopService issReal;
	
	private static IUserService iusReal;
	
	private static ISupplierService isupsReal;
	
	private static IInventoryService  iisReal;
	
	
	public static ICategoryService ics;
	
	public static IGoodsService igs;
	
	public static IShopService iss;
	
	public static IInventoryService  iis;
	
	public static IUserService ius;
	
	public static ISupplierService isups;
	
	public static SessionFactory sessionFactory;
	
	
	static {
		
		getRealUserService();
		getRealGoodsService();
		getRealShopService();
		getRealCategoryService();
		getRealSupplierService();
		getRealInventoryService();
	}
	
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
			if (igs == null) {
				getGoodsService();
			}
			((ShopService)getRealShopService()).setGoodsService(getRealGoodsService());
			if (ius == null) {
				getUserService();
			}
			((ShopService)getRealShopService()).setUserService(getRealUserService());
			((ShopService)getRealShopService()).setSupplierService(getRealSupplierService());
			((ShopService)getRealShopService()).setInventoryService(getRealInventoryService());
			iss = (IShopService)getProxy(IShopService.class.getClassLoader(), IShopService.class, getRealShopService());
		}
		return iss;
	}
	
	
	public static IInventoryService  getInventoryService() {
		if (iis == null) {
			((InventoryService)getRealInventoryService()).setGoodsService(getRealGoodsService());
			((InventoryService)getRealInventoryService()).setSupplierService(getRealSupplierService());
			((InventoryService)getRealInventoryService()).setUserService(getRealUserService());
			iis = (IInventoryService)getProxy(IInventoryService.class.getClassLoader(), IInventoryService.class, getRealInventoryService());
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
			if (iis == null) {
				getInventoryService();
			}
			((SupplierService)getRealSupplierService()).setShopService(getRealShopService());
			isups = (ISupplierService)getProxy(ISupplierService.class.getClassLoader(), ISupplierService.class, getRealSupplierService());
		}
		return isups;
	}
	
	public static IInventoryService  getRealInventoryService() {
		if (iisReal == null) {
			iisReal =  new InventoryService();
		}
		return iisReal;
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
