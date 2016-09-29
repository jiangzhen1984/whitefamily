package com.whitefamily.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

public class BaseService {

	private static ThreadLocal<Session> sessionLocal = new ThreadLocal<Session>();
	protected SessionFactory sessionFactory = null;
	private Logger  logger = LoggerFactory.logger(getClass());


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Session getSession() {
		return openSession();
	}

	public Session openSession() {
		return openSession(false);
	}
	
	public Session openSession(boolean flag) {
		Session sess = sessionLocal.get();
		if (sess == null || (flag && !sess.isOpen())) {
			sess = getSessionFactory().openSession();
			sessionLocal.set(sess);
		}
		logger.info("===> session open "+ sess);
		return sess;
	}

	protected Transaction beginTransaction(Session session) {
		return session.beginTransaction();
	}

	public void closeSession() {
		Session sess = sessionLocal.get();
		if (sess != null) {
			if (sess.isOpen()) {
				sess.close();
			}
			sessionLocal.remove();
		}
		logger.info("===> session closed "+ sess);
	}

}
