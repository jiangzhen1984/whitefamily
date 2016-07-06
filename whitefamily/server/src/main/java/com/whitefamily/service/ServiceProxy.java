package com.whitefamily.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class ServiceProxy implements InvocationHandler {
	
	private static SessionFactory sessionFactory;
	
	private Object real;
	
	private Method openSessionMtd;
	private Method closeSessionMtd;
	
	static {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("/hibernate.cfg.xml");

			// apply configuration property settings to
			// StandardServiceRegistryBuilder
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();

			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	

	public ServiceProxy(Object real) {
		super();
		this.real = real;
		try {
			Method m = real.getClass().getMethod("setSessionFactory", new Class[]{SessionFactory.class});
			m.invoke(real, new Object[]{sessionFactory});
			openSessionMtd = real.getClass().getMethod("openSession", new Class[]{});
			closeSessionMtd = real.getClass().getMethod("closeSession", new Class[]{});
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(" can not inject sessionfactory ");
		} 
	}



	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		openSessionMtd.invoke(real, new Object[]{});
		try {
			Object obj = method.invoke(real, args);
			return obj;
		} finally {
			closeSessionMtd.invoke(real, new Object[]{});
		}
	}

}
