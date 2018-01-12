package com.china.stock.common.database;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("all")
public class RowMapperHander4Obj<T> implements RowMapper<T>{
	private Class<T> clazz;
	public RowMapperHander4Obj(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public T mapRow(ResultSet resultSet, int row) throws SQLException {
		return DaoUtil.fillModel(clazz, resultSet);
	}


}
