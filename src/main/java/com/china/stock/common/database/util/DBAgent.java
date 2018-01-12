package com.china.stock.common.database.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


public final class DBAgent {

	public static Object get(Class entityClass, Serializable id) {
		return currentHibernateDaoSupport().getHibernateTpl().get(entityClass, id);
	}

	public static List loadAll(Class entityClass) {
		return loadAll(entityClass, null);
	}

	public static List loadAll(Class entityClass, FlipInfo fi) {
		StockHibernateTemplate ht = currentHibernateDaoSupport().getHibernateTpl();
		if (fi != null)
			ht.setFlipInfo(fi);
		List result = ht.loadAll(entityClass);
		if (fi != null)
			fi.setData(result);
		return result;
	}

	public static Object save(Object entity) {
		return currentHibernateDaoSupport().getHibernateTpl().save(entity);
	}

	public static List saveAll(List entities) {
		List results = new ArrayList();
		for (int i = 0; i < entities.size(); i++) {
			results.add(save(entities.get(i)));
			if ((i + 1) % 1000 == 0 && i != 0) {
				currentHibernateDaoSupport().getHibernateTpl().flush();
				currentHibernateDaoSupport().getHibernateTpl().clear();
			}
		}

		return results;
	}

	public static List saveAllForceFlush(List entities) {
		List results = new ArrayList();
		for (int i = 0; i < entities.size(); i++) {
			results.add(save(entities.get(i)));
			if ((i + 1) % 1000 == 0 && i != 0 || i == entities.size() - 1) {
				currentHibernateDaoSupport().getHibernateTpl().flush();
				currentHibernateDaoSupport().getHibernateTpl().clear();
			}
		}

		return results;
	}

	public static void update(Object entity) {
		StockHibernateTemplate tpl = currentHibernateDaoSupport().getHibernateTpl();
		tpl.merge(entity);
	}

	public static void updateNoMerge(Object entity) {
		StockHibernateTemplate tpl = currentHibernateDaoSupport().getHibernateTpl();
		tpl.update(entity);
	}

	public static void updateAll(List entities) {
		for (int i = 0; i < entities.size(); i++) {
			update(entities.get(i));
			if (i % 1000 == 0 && i != 0) {
				currentHibernateDaoSupport().getHibernateTpl().flush();
				currentHibernateDaoSupport().getHibernateTpl().clear();
			}
		}

	}

	public static void delete(Object entity) {
		currentHibernateDaoSupport().getHibernateTpl().delete(entity);
	}

	public static void deleteAll(List entities) {
		currentHibernateDaoSupport().getHibernateTpl().deleteAll(entities);
	}

	public static void saveOrUpdate(Object entity) {
		currentHibernateDaoSupport().getHibernateTpl().saveOrUpdate(entity);
	}

	public static void evict(Object entity) {
		currentHibernateDaoSupport().getHibernateTpl().evict(entity);
	}

	public static List findByCriteria(DetachedCriteria criteria) {
		return currentHibernateDaoSupport().getHibernateTpl().findByCriteria(criteria);
	}

	public static List findByCriteria(DetachedCriteria criteria, FlipInfo fi) {
		StockHibernateTemplate ht = currentHibernateDaoSupport().getHibernateTpl();
		ht.setFlipInfo(fi);
		List l = ht.findByCriteria(criteria);
		fi.setData(l);
		return l;
	}

	public static List find(String queryString) {
		return find(queryString, null);
	}

	public static List find(String queryString, Map params) {
		return find(queryString, params, null);
	}

	public static List find(String queryString, Map params, FlipInfo fi) {
		return findBy(queryString, params, fi, false);
	}

	public static List findByValueBean(String queryString, Object valueBean) {
		return findBy(queryString, valueBean, null, false);
	}

	public static List findByValueBean(String queryString, Object valueBean, FlipInfo fi) {
		return findBy(queryString, valueBean, fi, false);
	}

	public static List findByNamedQuery(String queryName) {
		return findByNamedQuery(queryName, null);
	}

	public static List findByNamedQuery(String queryName, Map params) {
		return findByNamedQuery(queryName, params, null);
	}

	public static List findByNamedQuery(String queryName, Map params, FlipInfo fi) {
		return findBy(queryName, params, fi, true);
	}

	public static List findByNamedQueryAndValueBean(String queryName, Object valueBean) {
		return findBy(queryName, valueBean, null, true);
	}

	public static List findByNamedQueryAndValueBean(String queryName, Object valueBean, FlipInfo fi) {
		return findBy(queryName, valueBean, fi, true);
	}

	public static  int bulkUpdate(String hql, Object values[]) {
		StockHibernateTemplate ht = currentHibernateDaoSupport().getHibernateTpl();
		return ht.bulkUpdate(hql, values);
	}

	public static int bulkUpdate(String hql, Map nameParameters) {
		StockHibernateTemplate ht = currentHibernateDaoSupport().getHibernateTpl();
		return ht.bulkUpdate(hql, nameParameters);
	}

	public static boolean exists(String hql) {
		return exists(hql, null);
	}

	public static boolean exists(String hql, Map params) {
		FlipInfo fi = new FlipInfo(1, 1);
		fi.setNeedTotal(false);
		List result = find(hql, params, fi);
		return result.size() != 0;
	}

	public static int count(String hql) {
		return count(hql, null);
	}

	
	public static int count(String hql, Map params) {
		String countHql = StockHibernateTemplate.countSQL(hql);
		List objects = find(countHql, params);
		return objects != null && !objects.isEmpty() ? ((Number) objects.get(0)).intValue() : 0;
	}

	public static List memoryPaging(List dataList, FlipInfo fi) {
		int size = fi.getSize();
		int page = fi.getPage();
		int start = fi.getStartAt().intValue();
		List datas;
		for (datas = dataList; start > datas.size(); start = fi.getStartAt().intValue())
			fi.setPage(--page);

		fi.setTotal(datas.size());
		int end = start + size <= fi.getTotal() ? start + size : fi.getTotal();
		List resultList = new ArrayList();
		for (; start < end; start++)
			resultList.add(datas.get(start));

		fi.setData(resultList);
		return fi.getData();
	}

	private static List findBy(String query, Object params, FlipInfo fi, boolean namedQuery) {
		StockHibernateTemplate ht = currentHibernateDaoSupport().getHibernateTpl();
		String qstring = query;
		if (fi != null)
			ht.setFlipInfo(fi);
		List result = new ArrayList();
		if (params == null || (params instanceof Map)) {
			if (namedQuery)
				result = ht.findByNamedQueryAndNamedParam(qstring, (Map) params);
			else
				result = ht.findByNamedParam(qstring, (Map) params);
		} else if (namedQuery)
			result = ht.findByNamedQueryAndValueBean(qstring, params);
		else
			result = ht.findByValueBean(qstring, params);
		if (fi != null)
			fi.setData(result);
		return result;
	}

	public void commit() {
		StockHibernateTemplate tpl = currentHibernateDaoSupport().getHibernateTpl();
		tpl.flush();
		tpl.clear();
	}

	private static StockHibernateDaoSupport currentHibernateDaoSupport() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		return (StockHibernateDaoSupport) wac.getBean("hibernateDaoSupport");
	}
}
