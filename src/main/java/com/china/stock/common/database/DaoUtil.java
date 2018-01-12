package com.china.stock.common.database;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeanUtils;

import com.china.stock.common.tool.base.LogerUtil;





abstract public class DaoUtil {
	/**
	 * 读取排除模型属性的配置文件
	 */
	public static Properties extProperty = new Properties();
	static {
		InputStream inputStream=null;
		try {
			inputStream = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("extEntyProperty.properties");
			extProperty.load(inputStream);
		} catch (IOException e) {
			LogerUtil.writeError("读取extEntyProperty.properties出错"
					+ e.getMessage());
		}
		finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				inputStream=null;
			}
		}
	}

	/**
	 * 
	 * @param entityClass
	 *            模型的class
	 * @param res
	 *            数据哭的返回集
	 * @return 返回填充模型
	 */
	public static <T> T fillModel(Class<T> entityClass, ResultSet res) {
		T instance=null;
		try {
			instance = entityClass.newInstance();
			ResultSetMetaData metaData = res.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				Object object = res.getObject(columnName);
				PropertyDescriptor descriptor = BeanUtils
						.getPropertyDescriptor(entityClass, columnName);
				if (descriptor != null) {
					descriptor.getWriteMethod().invoke(instance, object);
				}
			}
		} catch (Exception e) {
			LogerUtil.writeError("填充数据模型错误"+e.getMessage());
		}
		return instance;

	}

	/**
	 * 填充obj
	 * 
	 * @param res
	 * @return
	 */
	public static Object[] fillOBJ(ResultSet res) {
		List<Object> objs = new ArrayList<Object>();
		try {
			ResultSetMetaData metaData = res.getMetaData();
			int columnCount = res.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				objs.add(res.getObject(columnName));
			}
		} catch (SQLException e) {
			LogerUtil.writeError(e.getMessage());
			return null;
		}
		return objs.toArray();
	}

	/**
	 * 填充map
	 * 
	 * @param res
	 * @return
	 */
	public static Map<String, Object> fillMap(ResultSet res) {
		try {
			Map<String, Object> re = new HashMap<String, Object>();
			ResultSetMetaData metaData = res.getMetaData();
			int columnCount = res.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				re.put(columnName, res.getObject(columnName));
			}
			return re;
		} catch (SQLException e) {
			LogerUtil.writeError(e.getMessage());
			return null;
		}
	}
}
