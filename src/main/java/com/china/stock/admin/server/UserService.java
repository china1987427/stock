package com.china.stock.admin.server;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.china.stock.admin.dao.UserDao;

@Service(value = "/userService")
public class UserService {
	private static Logger log = Logger.getLogger(UserService.class);
	@Autowired
	private UserDao userDao;

	public Map<String, Object> login(String username, String password) {
		return userDao.verify(username, "username");
	}

	public String verify(String value, String type) {
		String result = "";
		if ("1".equals(type)) {
			Map<String, Object> u = userDao.verify(value, "username");
			if (CollectionUtils.isEmpty(u)) {
				result = "false";
			} else {
				result = "true";
			}
		} else if ("2".equals(type)) {
			Map<String, Object> u = userDao.verify(value, "mobile");
			if (CollectionUtils.isEmpty(u)) {
				result = "false";
			} else {
				result = "true";
			}
		}
		return result;
	}

	public String registering(String username, String password, String mobile, String email, String province, String city, String county, String street) {
		String result = "";
		int regResult = userDao.registering(username, password, mobile, email, province, city, county, street);
		if (regResult == 1) {
			result = "true";
		} else {
			result = "false";
		}
		return result;
	}

	public List<Map<String, Object>> getArea(int id, int fatherId, int level, String aName) {
		return userDao.getAddr(id, fatherId, level, aName);
	}

	/**
	 * 获取用户信息
	 * 
	 */
	public Map<String, Object> getUserInfo(String username) {
		return userDao.getUserInfo(username);
	}

	public void saveRegion(List<Map<String, Object>> list) {
		userDao.saveRegion(list);
	}

	public List<Map<String, Object>> getProvince() {
		return userDao.getProvince();
	}

	public List<Map<String, Object>> getParentProvince(String regionCode) {
		return userDao.getParentProvince(regionCode);
	}

	public List<Map<String, Object>> getCity(String regionCode) {
		return userDao.getCity(regionCode);
	}
}
