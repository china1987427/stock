package com.china.stock.admin.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.china.stock.common.database.DBJdbc;

@Repository
public class UserDao extends DBJdbc {
	public Map<String, Object> verify(String value, String type) {
		String sql = "";
		if ("username".equals(type)) {
			sql = "select * from account where user_name=?";
		} else if ("mobile".equals(type)) {
			sql = "select * from account where mobile=?";
		}
		try {
			return getStockJdbc().getJdbcTemplate().queryForMap(sql, value);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int registering(String username, String password, String mobile, String email, String province, String city, String county, String street) {
		String sql = "insert into account(user_id,user_name,password,mobile,email,province,city,county,street,reg_date,status) "
				+ "values(:userId,:userName,:password,:mobile,:email,:province,:city,:county,:street,now(),:status)";
		Map<String, Object> map = new HashMap<String, Object>();
		Random rand = new Random();
		int userId = rand.nextInt();
		map.put("userId", userId);
		map.put("userName", username);
		map.put("password", password);
		map.put("mobile", mobile);
		map.put("email", email);
		map.put("province", province);
		map.put("city", city);
		map.put("county", county);
		map.put("street", street);
		map.put("status", "1");
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public List<Map<String, Object>> getAddr(int id, int fatherId, int level, String regionName) {
		StringBuilder sb = new StringBuilder();
		String sql = "select * from region where 1=1";
		Map<String, Object> map = new HashMap<String, Object>();
		sb.append(sql);
		if (id > 0) {
			sb.append(" and id=:id ");
			map.put("id", id);
		}
		if (fatherId > 0) {
			sb.append(" and fid=:fatherId ");
			map.put("fatherId", fatherId);
		}
		if (level > 0) {
			sb.append(" and level=:level ");
			map.put("level", level);
		}
		if (!"".equals(regionName) && regionName != null) {
			sb.append(" and region_name=:regionName ");
			map.put("regionName", regionName);
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sb.toString(), map);
	}

	public Map<String, Object> getUserInfo(String username) {
		String sql = "select user_id,user_name,password,mobile,email,province,city,county,street,reg_date,status,ismanager from account where user_name=?";
		return getStockJdbc().getJdbcTemplate().queryForMap(sql, username);
	}

	public void saveRegion(final List<Map<String, Object>> list) {
		System.out.println(list);
		String sql = "insert into region(region_code,region_name,fid,level) values(?,?,?,?)";
		getStockJdbc().getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

			public int getBatchSize() {
				return list.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Map<String, Object> map = list.get(i);
				ps.setString(1, String.valueOf(map.get("regionCode")));
				ps.setString(2, String.valueOf(map.get("regionName")));
				ps.setString(3, String.valueOf(map.get("fid")));
				ps.setString(4, String.valueOf(map.get("level")));
			}
		});

	}

	public List<Map<String, Object>> getProvince() {
		String sql = "select * from region where level = 1";
		return getStockJdbc().getJdbcTemplate().queryForList(sql);
	}

	public List<Map<String, Object>> getParentProvince(String regionCode) {
		String sql = "select * from region where region_code like ?";
		return getStockJdbc().getJdbcTemplate().queryForList(sql, regionCode + "%");
	}

	public List<Map<String, Object>> getCity(String regionCode) {
		String sql = "select * from region where region_code like ? and level=2";
		return getStockJdbc().getJdbcTemplate().queryForList(sql, regionCode + "%00");
	}
}
