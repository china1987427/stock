package com.china.stock.common.database.util;

import java.sql.Connection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class StockHibernateDaoSupport extends HibernateDaoSupport {
	public final  StockHibernateTemplate getHibernateTpl() {
		setHibernateTemplate(createHibernateTemplate(getSessionFactory()));
		return (StockHibernateTemplate) getHibernateTemplate();
	}

	protected HibernateTemplate createHibernateTemplate(SessionFactory sessionFactory) {
		return new StockHibernateTemplate(sessionFactory);
	}
	
	public final Connection currentConnection() {
		return currentSession().connection();
	}

	public final Session currentSession() {
		return super.getSession();
	}

	public final Session currentSession(boolean allowCreate) {
		return super.getSession(allowCreate);
	}
}
