package com.china.stock.common.tool.page;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;


/**
 * 分页�?
 * @author zwh
 * @version 1.0.0
 */
public class Pagination extends SimplePage implements java.io.Serializable,
		Paginable {
	private static final long serialVersionUID = 1L;

	private NamedParameterJdbcDaoSupport jdbcTemplate;

	public Pagination() {

	}

	public Pagination(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}


	@SuppressWarnings("rawtypes")
	public Pagination(int pageNo, int pageSize, int totalCount, List list) {
		super(pageNo, pageSize, totalCount);
		this.list = list;
	}

	/**
	 * * 每页显示10条记录的构�?函数,使用该函数必须先给Pagination设置pageNo，jTemplate初�? �?? * @param sql
	 * oracle语句 �??
	 */
	public Pagination(String sql) {
		if (jdbcTemplate == null) {
			throw new IllegalArgumentException(
					"Pagination.jdbcTemplate is null,please initial it first. ");
		} else if (sql.equals("")) {
			throw new IllegalArgumentException(
					"Pagination.sql is empty,please initial it first. ");
		}
		new Pagination(sql, pageNo, DEF_COUNT, jdbcTemplate);
	}

	/**
	 * 分页构�?函数 
	 * 
	 * @param sql
	 *            根据传入的sql语句得到�?��基本分页信息 
	 * @param currentPage
	 *            当前�?
	 * @paramnumPerPage 每页记录�?
	 * @param jdbcTemplate
	 *            JdbcTemplate实例
	 */
	public Pagination(String sql, int pageNo, int pageSize,
			NamedParameterJdbcDaoSupport jdbcTemplate) {
		if (jdbcTemplate == null) {
			throw new IllegalArgumentException(
					"Pagination.jTemplate is null,please initial it first. ");
		} else if (sql == null || sql.equals("")) {
			throw new IllegalArgumentException(
					"Pagination.sql is empty,please initial it first. ");
		}
		this.setJdbcTemplate(jdbcTemplate);// 计算总记录数
		StringBuffer totalSQL = new StringBuffer(" SELECT count(*) FROM ( ");
		totalSQL.append(sql);
		totalSQL.append(" ) totalTable ");
		// 总记录数
		this.totalCount = getJdbcTemplate().getJdbcTemplate().queryForObject(totalSQL.toString(), Integer.class);

		if (pageSize <= 0) {
			this.pageSize = DEF_COUNT;
		} else {
			this.pageSize = pageSize;
		}
		if (pageNo <= 0) {
			this.pageNo = 1;
		} else {
			this.pageNo = pageNo;
		}
		if ((this.pageNo - 1) * this.pageSize >= totalCount) {
			this.pageNo = totalCount / pageSize;
		}
		if(pageNo>0){
			this.startIndex = (pageNo - 1) * pageSize;
		}
		if (totalCount < pageSize) {
			this.lastIndex = totalCount;
		} else if ((totalCount % pageSize == 0)
				|| (totalCount % pageSize != 0 && pageNo < this.getTotalPage())) {
			this.lastIndex = pageNo * pageSize;
		} else if (totalCount % pageSize != 0 && pageNo == this.getTotalPage()) {// �?���?��
			this.lastIndex = totalCount;
		}
		StringBuffer paginationSQL = new StringBuffer(sql);
		paginationSQL.append(" limit ");
		paginationSQL.append(this.startIndex );
		paginationSQL.append(" , " );
		paginationSQL.append(this.lastIndex);
		this.list = getJdbcTemplate().getJdbcTemplate().queryForList(paginationSQL.toString());
	}
	
	/**
	 *  分页构�?函数 
	 * @param sql sql语句
	 * @param pageNo 当前页码
	 * @param pageSize 每页记录�?
	 * @param jdbcTemplate JdbcTemplate实例
	 */
	public Pagination( int pageNo, int pageSize,String sql,
			NamedParameterJdbcDaoSupport jdbcTemplate,Map<String,Object> paramMap) {
		if (jdbcTemplate == null) {
			throw new IllegalArgumentException(
					"Pagination.jTemplate is null,please initial it first. ");
		} else if (sql == null || sql.equals("")) {
			throw new IllegalArgumentException(
					"Pagination.sql is empty,please initial it first. ");
		}
		this.setJdbcTemplate(jdbcTemplate);// 计算总记录数
		StringBuffer totalSQL = new StringBuffer(" SELECT count(*) FROM ( ");
		totalSQL.append(sql);
		totalSQL.append(" ) totalTable ");
		// 总记录数
		this.totalCount = getJdbcTemplate().getNamedParameterJdbcTemplate().queryForObject(totalSQL.toString(), paramMap, Integer.class);

		if (pageSize <= 0) {
			this.pageSize = DEF_COUNT;
		} else {
			this.pageSize = pageSize;
		}
		if (pageNo <= 0) {
			this.pageNo = 1;
		} else {
			this.pageNo = pageNo;
		}
		if ((this.pageNo - 1) * this.pageSize >= totalCount) {
			this.pageNo = totalCount / pageSize;
		}
		this.startIndex = (pageNo - 1) * pageSize;
		if (totalCount < pageSize) {
			this.lastIndex = totalCount;
		} else if ((totalCount % pageSize == 0)
				|| (totalCount % pageSize != 0 && pageNo < this.getTotalPage())) {
			this.lastIndex = pageNo * pageSize;
		} else if (totalCount % pageSize != 0 && pageNo == this.getTotalPage()) {// �?���?��
			this.lastIndex = totalCount;
		}
		StringBuffer paginationSQL = new StringBuffer(sql);
		paginationSQL.append(" limit ");
		paginationSQL.append(this.startIndex );
		paginationSQL.append(" , " );
		paginationSQL.append(this.lastIndex);
		this.list = jdbcTemplate.getNamedParameterJdbcTemplate().queryForList(paginationSQL.toString(), paramMap);
	}

	public int getFirstResult() {
		return (pageNo - 1) * pageSize;
	}

	/**
	 * 当前页的数据
	 */


	@SuppressWarnings("rawtypes")
	private List list;


	@SuppressWarnings("rawtypes")
	public List getList() {
		return list;
	}


	@SuppressWarnings("rawtypes")
	public void setList(List list) {
		this.list = list;
	}

	public NamedParameterJdbcDaoSupport getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(NamedParameterJdbcDaoSupport jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
