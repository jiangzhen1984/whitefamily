package com.whitefamily.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

public class BaseService {

	private static ThreadLocal<Session> sessionLocal = new ThreadLocal<Session>();
	private static ThreadLocal<Transaction> transactionLocal = new ThreadLocal<Transaction>();
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
			logger.info("===> session open "+ sess.hashCode());
		}
		return sess;
	}

	public Transaction beginTransaction(Session session) {
		Transaction trans = transactionLocal.get();
		if (trans != null) {
			logger.error("["+trans+"--" + trans.hashCode() +"] was not remove, set rollback");
			trans.rollback();
		}
		trans = session.beginTransaction();
		logger.info("[Thread:"+Thread.currentThread()+"]"+"["+trans+"--" + trans.hashCode() +"] created");
		transactionLocal.set(trans);
		return trans;
	}
	
	
	public void rollbackTrans() {
		Transaction trans = transactionLocal.get();
		trans.rollback();
		transactionLocal.remove();
		logger.info("[Thread:"+Thread.currentThread()+"]"+"["+trans+"--" + trans.hashCode() +"] rollback and removed");
	}
	
	public void commitTrans() {
		Transaction trans = transactionLocal.get();
		trans.commit();
		transactionLocal.remove();
		logger.info("[Thread:"+Thread.currentThread()+"]"+"["+trans+"--" + trans.hashCode() +"] commit and removed");
	}
	
	public boolean existTrans() {
		return transactionLocal.get() != null;
	}

	public void closeSession() {
		Session sess = sessionLocal.get();
		if (sess != null) {
			if (sess.isOpen()) {
				sess.close();
			}
			sessionLocal.remove();
			logger.info("===> session closed "+ sess.hashCode());
		}
	}

}
