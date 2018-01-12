package com.china.stock.common.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * DAO实现基类。
 * 
 */
@Repository
public class DBJdbc{
	@Autowired
	private NamedParameterJdbcDaoSupport stockJdbc; //交易数据库	

	public NamedParameterJdbcDaoSupport getStockJdbc() {
		return stockJdbc;
	}

	public void setStockJdbc(NamedParameterJdbcDaoSupport stockJdbc) {
		this.stockJdbc = stockJdbc;
	}

	
}
