package com.china.stock.common.database;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("all")
public interface IBaseDao<T> {

	/**
	 * 添加
	 * @param t
	 */
	public abstract void add(T t, String autoIncrementKeyName);
	/**
	 * 添加
	 * @param t
	 */
	public abstract Number add4autoID(T t, String autoIncrementKeyName);

	/**
	 * 更新
	 * @param t
	 */
	public abstract void update(T t, String primaryKeyName);

	/**
	 * 根据id删除
	 * @param id
	 * @param entityClass
	 */
	public abstract void delete(Serializable id, Class entityClass,
			String primaryKeyName);

	/**
	 * 通过Id的到
	 * @param id
	 * @param entityClass
	 */
	public abstract T get(Serializable id, Class entityClass,
			String primaryKeyName);

	/**
	 * 通过Id的到,但是屏蔽了异常
	 * @param id
	 * @param entityClass
	 */
	public abstract T getNOException(Serializable id, Class entityClass,
			String primaryKeyName);

	/**
	 * 得到所有
	 * @param entityClass
	 * @return
	 */
	public abstract List<T> getNOException4List(Serializable id, Class entityClass,
			String primaryKeyName);
	
	/**
	 * 得到所有
	 * @param entityClass
	 * @return
	 */
	public abstract List<T> getAll(Class entityClass);

	/**
	 * 通用方法，通过自定义sql来获取所需数据
	 * @param sql
	 *            ：用自定义数据
	 * @param paeams
	 *            查询所需
	 * @param rowMapper
	 *            处理返回结果是图的一行数据的接口
	 * @return 返回的是一个list
	 */
	public abstract List findBySql(String sql, Object[] paeams,
			RowMapper rowMapper);
	/**
	 * 通用方法，通过自定义sql来获取所需数据,有参数查询时，Map<String, ?>paramMap
	 * @param sql
	 * @param paramMap
	 * @return返回的是一个map
	 */
	List<Map<String, Object>> findBySql(String sql,Map<String, ?>paramMap);
	/**
	 * 通用方法，通过自定义sql来获取所需数据 ,有参数查询时，传值为Object...args
	 * @param sql
	 * @param args
	 * @return 返回的是一个map
	 */
	List<Map<String, Object>> findBySql(String sql,Object...args);
	/**
	 * 执行SQL
	 */
	void execute(String sql);
	void execute(String sql,Object...args);
	void execute(String sql, Map<String, ?> paramMap);
	}