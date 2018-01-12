package com.china.stock.common.database;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.china.stock.common.tool.base.ExProperty;
import com.china.stock.common.tool.base.LogerUtil;
import com.china.stock.common.tool.base.Primarykey;




@Repository
@SuppressWarnings("all")
public class IBaseDaoImpl<T> implements IBaseDao<T> {
	@Autowired
	private NamedParameterJdbcDaoSupport actJdbc;

	/*
	 * 获取jdbc模板
	 */
	private JdbcTemplate jdbcTemplate() {
		return actJdbc.getJdbcTemplate();
	}

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		return actJdbc.getNamedParameterJdbcTemplate();
	}
	@Override
	public void add(T t, String autoIncrementKeyName) {
		String insertSql = autoCreateInsertSql(t,
				autoIncrementKeyName);
		LogerUtil.writeInfo(insertSql);
		jdbcTemplate().update(insertSql,
				new BeanPreparedStatement4Insert(t, autoIncrementKeyName));
	}

	public Number add4autoID(T t, String autoIncrementKeyName) {
		String sql = autoCreateInsertSql(t, autoIncrementKeyName);
		LogerUtil.writeInfo(sql);
		KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		jdbcTemplate().update(
				new BeanPreparedStatement4Insert4autoID(t,
						autoIncrementKeyName, sql), generatedKeyHolder);
		Number key = generatedKeyHolder.getKey();
		return key;
	}

	@Override
	public void update(T t, String primaryKeyName) {
		try {
			PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(
					t.getClass(), primaryKeyName);
			Method readMethod = descriptor.getReadMethod();
			Serializable invoke = (Serializable) readMethod.invoke(t, null);
			T t2 = getNOException(invoke, t.getClass(), primaryKeyName);
			if (t2 != null) {
				String updateSql = autoCreateUpdateSql(t,
						primaryKeyName);
				LogerUtil.writeInfo(updateSql);
				BeanUtils.copyProperties(t, t2);
				jdbcTemplate().update(updateSql,
						new BeanPreparedStatement4Update(t2, primaryKeyName));
			}
		} catch (Exception e) {
			LogerUtil.writeError("dao数据更新失败" + e.getMessage());
		}
	}

	@Override
	public void delete(Serializable id, Class entityClass, String primaryKeyName) {
		String deleteSql = autoCreateDeleteSql(entityClass, primaryKeyName);
		LogerUtil.writeInfo(deleteSql);
		jdbcTemplate().update(deleteSql, id);

	}

	@Override
	public T get(Serializable id, final Class entityClass, String primaryKeyName) {
		String selectSql = "select * from "
				+ entityClass.getSimpleName().toLowerCase() + " where "
				+ primaryKeyName + "=?";
		LogerUtil.writeInfo(selectSql);
		Object[] params = { id };
		T object = jdbcTemplate().queryForObject(selectSql, params,
				new RowMapperHander4Obj<T>(entityClass));
		return object;
	}

	@Override
	public T getNOException(Serializable id, final Class entityClass,
			String primaryKeyName) {
		T object = null;
		try {
			object = get(id, entityClass, primaryKeyName);
		} catch (DataAccessException e) {
			LogerUtil.log.error("get出错" + e.getMessage());
		}
		return object;
	}

	@Override
	public List<T> getNOException4List(Serializable id, Class entityClass,
			String primaryKeyName) {

		List<T> list = null;
		try {
			String selectSql = "select * from "
					+ entityClass.getSimpleName().toLowerCase() + " where "
					+ primaryKeyName + "=?";
			list = (List<T>) findBySql(selectSql, id);
		} catch (DataAccessException e) {
			LogerUtil.log.error("get出错" + e.getMessage());
		}
		return list;
	}

	@Override
	public List<T> getAll(final Class entityClass) {
		try {
			String selectSql = "select * from "
					+ entityClass.getSimpleName().toLowerCase();
			LogerUtil.writeInfo(selectSql);
			List query = jdbcTemplate().query(selectSql,
					new RowMapperHander4Obj<T>(entityClass));
			return query;
		} catch (DataAccessException e) {
			LogerUtil.log.error("get出错" + e.getMessage());
			return null;
		}

	}

	@Override
	public List findBySql(String sql, Object[] paeams, RowMapper rowMapper) {
		return jdbcTemplate().query(sql, paeams, rowMapper);
	}

	@Override
	public List<Map<String, Object>> findBySql(String sql,
			Map<String, ?> paramMap) {
		return namedParameterJdbcTemplate().queryForList(sql, paramMap);
	}

	@Override
	public List<Map<String, Object>> findBySql(String sql, Object... args) {
		return jdbcTemplate().queryForList(sql, args);
	}

	@Override
	public void execute(String sql) {
		jdbcTemplate().execute(sql);
	}

	@Override
	public void execute(String sql, Object... args) {
		jdbcTemplate().update(sql, args);
	}

	@Override
	public void execute(String sql, Map<String, ?> paramMap) {
		namedParameterJdbcTemplate().update(sql, paramMap);
	}

	// *******************************************下面是dao所依赖的方法****************************************************
	/**
	 * 自动生成delete语句
	 * 
	 * @param entityClass
	 * @return
	 */
	private String autoCreateDeleteSql(Class entityClass, String primaryKeyName) {
		return "DELETE FROM " + entityClass.getSimpleName() + " where "
				+ primaryKeyName + "=?";
	}

	/**
	 * 自动拼装UpdateSQL
	 * @param clazz
	 * @return
	 */
	private String autoCreateUpdateSql(T t, String primaryKeyName) {
		StringBuilder updateSql = new StringBuilder("UPDATE ").append(
				t.getClass().getSimpleName().toLowerCase()).append(" SET ");
		List<String> classNames = getClassNames(t, primaryKeyName);
		for (String name : classNames) {
			updateSql.append(name + "=?,");
		}
		String subUpdateSql = updateSql.substring(0, updateSql.length() - 1);
		return subUpdateSql + " where " + primaryKeyName + "=?";
	}

	/**
	 * 自动拼装insertSQL
	 * @param entityClass
	 * @return
	 */
	private String autoCreateInsertSql(T t,
			String autoIncrementKeyName) {
		StringBuilder sql = new StringBuilder("INSERT INTO ").append(
				t.getClass().getSimpleName().toLowerCase()).append(" (");
		StringBuilder valueSql = new StringBuilder(" VALUES ").append(" (");
		List<String> classNames = getClassNames(t,
				autoIncrementKeyName);
		for (String name : classNames) {
			sql.append(name + ",");
			valueSql.append("?,");
		}
		String subsql = sql.substring(0, sql.length() - 1);
		String subvalueSql = valueSql.substring(0, valueSql.length() - 1);
		return subsql + ")" + subvalueSql + ")";

	}

	/**
	 * 返回实体类的所有属性
	 * 
	 * @param entityClass
	 * @return
	 */
	private List<String> getClassNames(T t, String primaryKeyName) {
		List<String> classNames = new ArrayList<String>();
		try {
			Class<? extends Object> entityClass = t.getClass();
			BeanInfo beanInfo = Introspector.getBeanInfo(entityClass);
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				String name = propertyDescriptor.getName();
				Method readMethod = propertyDescriptor.getReadMethod();
				Object readValue = readMethod.invoke(t, null);
				Primarykey primarykey = readMethod.getAnnotation(Primarykey.class);
				ExProperty exProperty = readMethod.getAnnotation(ExProperty.class);
				if ("class".equals(name) || name.equals(primaryKeyName)||primarykey!=null||exProperty!=null||readValue==null) {
					continue;
				}
				String property = DaoUtil.extProperty.getProperty(entityClass.getName());
				if (StringUtils.isNotBlank(property)
						&& Arrays.asList(property.split(",")).contains(name)) {
					continue;
				}
				classNames.add(name);
			}
		} catch (Exception e) {
			LogerUtil.writeError("获取对象的需要插入属性出错" + e.getMessage());
		}
		return classNames;
	}

	/**
	 * BeanPreparedStatement4Insert
	 * 
	 * @author scnbhzw
	 */
	class BeanPreparedStatement4Insert implements PreparedStatementSetter {
		private T t;
		private String autoIncrementKeyName;

		public BeanPreparedStatement4Insert(T t, String autoIncrementKeyName) {
			super();
			this.t = t;
			this.autoIncrementKeyName = autoIncrementKeyName;
		}

		@Override
		public void setValues(PreparedStatement pre) throws SQLException {
			try {
				ParameterMetaData metaData = pre.getParameterMetaData();
				int columnCount = metaData.getParameterCount();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = getClassNames(t,
							autoIncrementKeyName).get(i - 1);
					PropertyDescriptor descriptor = BeanUtils
							.getPropertyDescriptor(t.getClass(), columnName);
					Method readMethod = descriptor.getReadMethod();
					Object invoke = readMethod.invoke(t);
					pre.setObject(i, invoke);
				}
			} catch (Exception e) {
				LogerUtil.writeError("BeanPreparedStatement4Insert"
						+ e.getMessage());
			}
		}
	}

	/**
	 * BeanPreparedStatement4Insert
	 * 
	 * @author scnbhzw
	 */
	class BeanPreparedStatement4Insert4autoID implements
			PreparedStatementCreator {
		private T t;
		private String autoIncrementKeyName;
		private String sql;

		@Override
		public PreparedStatement createPreparedStatement(Connection con)
				throws SQLException {
			PreparedStatement prepareStatement = con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			try {
				ParameterMetaData metaData = prepareStatement
						.getParameterMetaData();
				int columnCount = metaData.getParameterCount();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = getClassNames(t,
							autoIncrementKeyName).get(i - 1);
					PropertyDescriptor descriptor = BeanUtils
							.getPropertyDescriptor(t.getClass(), columnName);
					Method readMethod = descriptor.getReadMethod();
					Object invoke = readMethod.invoke(t);
					prepareStatement.setObject(i, invoke);
				}
			} catch (Exception e) {
				LogerUtil.writeError("BeanPreparedStatement4Insert"
						+ e.getMessage());
			} finally {
			}
			return prepareStatement;
		}

		public BeanPreparedStatement4Insert4autoID(T t,
				String autoIncrementKeyName, String sql) {
			super();
			this.t = t;
			this.autoIncrementKeyName = autoIncrementKeyName;
			this.sql = sql;
		}
	}

	/**
	 * BeanPreparedStatement4Update
	 * 
	 * @author scnbhzw
	 */
	class BeanPreparedStatement4Update implements PreparedStatementSetter {
		private T t;
		private String primaryKeyName;

		public BeanPreparedStatement4Update(T t, String primaryKeyName) {
			super();
			this.t = t;
			this.primaryKeyName = primaryKeyName;
		}

		@Override
		public void setValues(PreparedStatement pre) throws SQLException {
			try {
				ParameterMetaData metaData = pre.getParameterMetaData();
				int columnCount = metaData.getParameterCount();
				for (int i = 1; i <= columnCount; i++) {
					if (i != columnCount) {
						String columnName = getClassNames(t,
								primaryKeyName).get(i - 1);
						PropertyDescriptor descriptor = BeanUtils
								.getPropertyDescriptor(t.getClass(), columnName);
						Method readMethod = descriptor.getReadMethod();
						Object invoke = readMethod.invoke(t);
						pre.setObject(i, invoke);
						continue;
					}
					PropertyDescriptor descriptor = BeanUtils
							.getPropertyDescriptor(t.getClass(), primaryKeyName);
					Method readMethod = descriptor.getReadMethod();
					Object invoke = readMethod.invoke(t);
					pre.setObject(i, invoke);
				}
			} catch (Exception e) {
				LogerUtil.writeError("BeanPreparedStatement4Update"
						+ e.getMessage());
			}
		}
	}

}
