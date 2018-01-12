package com.china.stock.common.database;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("all")
public class RowMapperHander4Map  implements RowMapper<Map<String, Object>>{
	@Override
	public Map<String, Object> mapRow(ResultSet res, int arg1)
			throws SQLException {
		return DaoUtil.fillMap(res);
	}



}
