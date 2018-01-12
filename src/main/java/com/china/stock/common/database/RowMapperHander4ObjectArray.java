package com.china.stock.common.database;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("all")
public class RowMapperHander4ObjectArray implements RowMapper<Object[]>{

	@Override
	public Object[] mapRow(ResultSet res, int arg1) throws SQLException {
		return DaoUtil.fillOBJ(res);
	}


}
