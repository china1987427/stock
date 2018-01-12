package com.china.stock.common.database.util;


import java.util.*;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;


public class StockHibernateTemplate extends HibernateTemplate {

	public FlipInfo getFlipInfo() {
		return flipInfo;
	}

	public void setFlipInfo(FlipInfo flipInfo) {
		this.flipInfo = flipInfo;
		setMaxResults(flipInfo.getSize());
	}

	public StockHibernateTemplate() {
		flipInfo = null;
	}

	public StockHibernateTemplate(SessionFactory sessionFactory) {
		super(sessionFactory);
		flipInfo = null;
	}

	protected void prepareCriteria(Criteria criteria) {
		if (flipInfo != null) {
			if (flipInfo.isNeedTotal()) {
				flipInfo.setTotal(((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue());
				criteria.setProjection(null);
			}
			if (flipInfo.getStartAt().intValue() > 0)
				criteria.setFirstResult(flipInfo.getStartAt().intValue());
			String sortField = flipInfo.getSortField();
			String sortOrder = flipInfo.getSortOrder();
			if (sortField != null && !"".equals(sortField)) {
				if (sortOrder == null)
					sortOrder = "asc";
				if ("asc".equalsIgnoreCase(sortOrder))
					criteria.addOrder(Order.asc(sortField));
				else if ("desc".equalsIgnoreCase(sortOrder))
					criteria.addOrder(Order.desc(sortField));
			}
		}
		super.prepareCriteria(criteria);
	}

	protected void prepareQuery(final Query queryObject, final Object params) {
		if (flipInfo != null) {
			if (flipInfo.isNeedTotal()) {
				List objects = (List) executeWithNativeSession(new HibernateCallback() {

					public List doInHibernate(Session session) throws HibernateException {
						String countSQL = StockHibernateTemplate.countSQL(queryObject.getQueryString().trim());
						Query queryObj = session.createQuery(countSQL);
						if (params != null)
							if (params instanceof Map) {
								Map paramsMap = (Map) params;
								String key;
								for (Iterator ite = paramsMap.keySet().iterator(); ite.hasNext(); applyNamedParameterToQuery(queryObj, key,
										paramsMap.get(key)))
									key = (String) ite.next();

							} else {
								queryObj.setProperties(params);
							}
						return queryObj.list();
					}

				});
				flipInfo.setTotal(objects != null && !objects.isEmpty() ? ((Number) objects.get(0)).intValue() : 0);
				if (flipInfo.isNeedTotal() && flipInfo.getTotal() == 0)
					flipInfo.setPage((new Integer(0)).intValue());
				else if (flipInfo.getPage() < 1)
					flipInfo.setPage((new Integer(1)).intValue());
				else if (flipInfo.getPage() > flipInfo.getPages().intValue())
					flipInfo.setPage(flipInfo.getPages().intValue());
			}
			if (flipInfo.getStartAt().intValue() > 0)
				queryObject.setFirstResult(flipInfo.getStartAt().intValue());
		}
		super.prepareQuery(queryObject);
	}

	public List findByNamedQueryAndNamedParam(String queryName, Map params) throws DataAccessException {
		return findBy(queryName, params, true);
	}

	public List findByNamedParam(String queryString, Map params) throws DataAccessException {
		return findBy(queryString, params, false);
	}

	public List findByValueBean(String queryString, Object valueBean) throws DataAccessException {
		return findByValueBean(queryString, valueBean, false);
	}

	public List findByNamedQueryAndValueBean(String queryName, Object valueBean) throws DataAccessException {
		return findByValueBean(queryName, valueBean, true);
	}

	private List findBy(final String query, final Map params, final boolean namedQuery) throws DataAccessException {
		return (List) executeWithNativeSession(new HibernateCallback() {

			public List doInHibernate(Session session) throws HibernateException {
				Query queryObject;
				if (namedQuery)
					queryObject = session.getNamedQuery(query);
				else
					queryObject = session.createQuery(query);
				if (flipInfo != null) {
					String qstring = queryObject.getQueryString();
					String sortField = flipInfo.getSortField();
					String sortOrder = flipInfo.getSortOrder();
					if (sortField != null && !"".equals(sortField)) {
						StringBuilder sqlb = new StringBuilder(qstring);
						int oidx = qstring.toLowerCase().indexOf("order by");
						if (oidx != -1)
							sqlb.delete(oidx, sqlb.length());
						sqlb.append(" order by ");
						sqlb.append(sortField);
						if (sortOrder != null)
							sqlb.append(" ").append(sortOrder);
						qstring = sqlb.toString();
						queryObject = session.createQuery(qstring);
					}
				}
				if (params != null) {
					String key;
					for (Iterator ite = params.keySet().iterator(); ite.hasNext(); applyNamedParameterToQuery(queryObject, key, params.get(key)))
						key = (String) ite.next();

				}
				prepareQuery(queryObject, params);
				return queryObject.list();
			}
		});
	}

	private List findByValueBean(final String query, final Object valueBean, final boolean namedQuery) throws DataAccessException {
		return (List) executeWithNativeSession(new HibernateCallback() {

			public List doInHibernate(Session session) throws HibernateException {
				Query queryObject;
				if (namedQuery)
					queryObject = session.getNamedQuery(query);
				else
					queryObject = session.createQuery(query);
				queryObject.setProperties(valueBean);
				prepareQuery(queryObject, valueBean);
				return queryObject.list();
			}

		});
	}

	public static String countSQL(String hqlStr) {
		String tempCountSql = hqlStr.toLowerCase();
		StringBuilder countSqlBuf = new StringBuilder();
		boolean isDistinct = false;
		String countPropertiesName = "*";
		int selectIndex = tempCountSql.indexOf("select");
		int fromIndex = tempCountSql.indexOf("from ");
		if (fromIndex != -1) {
			if (selectIndex != -1) {
				String tmpStr = tempCountSql.substring(0, fromIndex);
				int distinctIndex = tmpStr.indexOf("distinct");
				if (distinctIndex != -1) {
					isDistinct = true;
					distinctIndex += 9;
					tmpStr = tmpStr.substring(distinctIndex);
					int dotIndex = tmpStr.indexOf(',');
					int kIndex = tmpStr.indexOf(')');
					if (dotIndex != -1 && dotIndex > kIndex)
						countPropertiesName = hqlStr.substring(distinctIndex, distinctIndex + dotIndex);
					else
						countPropertiesName = hqlStr.substring(distinctIndex, fromIndex - 1);
				}
			}
			int orderByIndex = tempCountSql.indexOf("order by");
			countSqlBuf.append("select count(").append(isDistinct ? "distinct " : "");
			countSqlBuf.append(countPropertiesName).append(") ");
			if (orderByIndex > 0)
				countSqlBuf.append(hqlStr.substring(fromIndex, orderByIndex));
			else
				countSqlBuf.append(hqlStr.substring(fromIndex));
		}
		return countSqlBuf.toString();
	}

	public int bulkUpdate(final String hql, final Map nameParameters) {
		return ((Integer) executeWithNativeSession(new HibernateCallback() {

			public Integer doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(hql);
				if (nameParameters != null) {
					Set entries = nameParameters.entrySet();
					for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
						java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
						String name = (String) entry.getKey();
						Object value = entry.getValue();
						if (value instanceof Collection)
							queryObject.setParameterList(name, (Collection) value);
						else if (value instanceof Object[])
							queryObject.setParameterList((String) entry.getKey(), (Object[]) value);
						else
							queryObject.setParameter((String) entry.getKey(), value);
					}

				}
				return Integer.valueOf(queryObject.executeUpdate());
			}
		})).intValue();
	}

	private FlipInfo flipInfo;

}
