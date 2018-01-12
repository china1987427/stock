package com.china.stock.common.tool.base;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.china.stock.common.tool.entity.MyException;

public class ObjUtil {

	/**
	 * 统一判断http请求参数,判断是否为必填及参数转换错误情况
	 * 
	 * @param request
	 * @param fields
	 *            ,格式�?"String user*,Long pass",分别表示map中的key,类型,是否必填(*为必�?^
	 *            为如果传入参数则不能为空,其他为非必填)
	 * @return
	 */
	public static Map<String, Object> typeDetail(HttpServletRequest request, String fields)
			throws MyException {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 处理request中的参数
		boolean isRequst = false; // 是否必填
		int type = 0;

		if (StringUtils.isBlank(fields)) {
			return resultMap;
		}

		for (String fieldTemp : fields.split(",")) {
			String[] detailStrings = fieldTemp.trim().split("\\s+");

			if (detailStrings.length != 2) {
				throw new MyException(-2, "typeDetail参数传入个数不正�?");
			}
			if (org.springframework.util.StringUtils.isEmpty(detailStrings[1])) {
				throw new MyException(-2, "typeDetail参数传入Key为空!");
			}

			if (detailStrings[1].endsWith("*")) {
				isRequst = true;
				type = 1;
				detailStrings[1] = detailStrings[1].substring(0, detailStrings[1].length() - 1);
			} else if (detailStrings[1].endsWith("^")) {
				type = 2;
				isRequst = false;
				detailStrings[1] = detailStrings[1].substring(0, detailStrings[1].length() - 1);
			} else {
				type = 0;
				isRequst = false;
			}

			if (detailStrings[0].equals("String")) {
				resultMap.put(detailStrings[1], ObjUtil.toString(detailStrings[1],
						request.getParameter(detailStrings[1]), type, request.getParameterMap()));
			} else if (detailStrings[0].equals("Long") || detailStrings[0].equals("long")) {
				resultMap.put(detailStrings[1],
						ObjUtil.toLong(detailStrings[1], request.getParameter(detailStrings[1]), isRequst));
			} else if (detailStrings[0].equals("Integer") || detailStrings[0].equals("int")) {
				resultMap.put(detailStrings[1],
						ObjUtil.toInt(detailStrings[1], request.getParameter(detailStrings[1]), isRequst));
			} else if (detailStrings[0].equals("Float") || detailStrings[0].equals("float")) {
				resultMap.put(detailStrings[1],
						ObjUtil.toFloat(detailStrings[1], request.getParameter(detailStrings[1]), isRequst));
			} else {
				throw new MyException(-2, "typeDetail参数传入类型不正�?");
			}

		}
		return resultMap;
	}

	/**
	 * 统一判断http请求头参�?判断是否为必填及参数转换错误情况
	 * 
	 * @param request
	 * @param fields
	 *            ,格式�?"String user*,Long pass",分别表示map中的key,类型,是否必填(*为必�?^
	 *            为如果传入参数则不能为空,其他为非必填)
	 * @param isLogin
	 *            是否�?��获取用户信息,只有http的head中传入了token才能取到,用户信息存入map的key�?login"
	 * @return
	 */
	public static Map<String, Object> typeDetailHead(HttpServletRequest request, String fields)
			throws MyException {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 处理request中的参数
		boolean isRequst = false; // 是否必填
		int type = 0;

		if (StringUtils.isBlank(fields)) {
			return resultMap;
		}

		for (String fieldTemp : fields.split(",")) {
			String[] detailStrings = fieldTemp.trim().split("\\s+");

			if (detailStrings.length != 2) {
				throw new MyException(-2, "typeDetail参数传入个数不正�?");
			}
			if (org.springframework.util.StringUtils.isEmpty(detailStrings[1])) {
				throw new MyException(-2, "typeDetail参数传入Key为空!");
			}

			if (detailStrings[1].endsWith("*")) {
				isRequst = true;
				type = 1;
				detailStrings[1] = detailStrings[1].substring(0, detailStrings[1].length() - 1);
			} else if (detailStrings[1].endsWith("^")) {
				type = 2;
				isRequst = false;
				detailStrings[1] = detailStrings[1].substring(0, detailStrings[1].length() - 1);
			} else {
				type = 0;
				isRequst = false;
			}

			if (detailStrings[0].equals("String")) {
				resultMap.put(detailStrings[1],
						ObjUtil.toString(detailStrings[1], request.getHeader(detailStrings[1]), type, null));
			} else if (detailStrings[0].equals("Long") || detailStrings[0].equals("long")) {
				resultMap.put(detailStrings[1],
						ObjUtil.toLong(detailStrings[1], request.getHeader(detailStrings[1]), isRequst));
			} else if (detailStrings[0].equals("Integer") || detailStrings[0].equals("int")) {
				resultMap.put(detailStrings[1],
						ObjUtil.toInt(detailStrings[1], request.getHeader(detailStrings[1]), isRequst));
			} else if (detailStrings[0].equals("Float") || detailStrings[0].equals("float")) {
				resultMap.put(detailStrings[1],
						ObjUtil.toFloat(detailStrings[1], request.getHeader(detailStrings[1]), isRequst));
			} else {
				throw new MyException(-2, "typeDetail参数传入类型不正�?");
			}

		}
		return resultMap;
	}

	/**
	 * 将指定对象属性名称和属�?值转化为Map键�?�?
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap<String, Object> objectToMap(Object obj) throws Exception {
		if (obj == null) {
			throw new Exception("对象为空");
		}

		Class clazz = obj.getClass();
		HashMap map = new HashMap();
		getClass(clazz, map, obj);
		HashMap newMap = convertHashMap(map);
		return newMap;
	}

	/**
	 * 带入�?��对象，一个对象中的属性名称，取得该属性的�?
	 * 
	 * @param obj
	 * @param strProperty
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static String getPropertyValueFormObject(Object obj, String strProperty) throws Exception {
		// zzz:if (!StringUtils.isValidateString(strProperty)) {
		// return "";
		// }
		if (obj == null) {
			return "";
		}
		Class clazz = obj.getClass();
		HashMap map = new HashMap();
		getClass(clazz, map, obj);
		HashMap newMap = convertHashMap(map);

		if (newMap == null) {
			return "";
		} else {
			// zzz:Object objReturn =
			// newMap.get(StringUtils.validateString(strProperty));
			Object objReturn = newMap.get(strProperty);
			if (objReturn == null) {
				return "";
			} else {
				return objReturn.toString();
			}

		}

	}

	/**
		 *
		*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void getClass(Class clazz, HashMap map, Object obj) throws Exception {
		if (clazz.getSimpleName().equals("Object")) {
			return;
		}

		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length <= 0) {
			throw new Exception("当前对象中没有任何属性�?");
		}
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String name = fields[i].getName();
			Object value = fields[i].get(obj);
			map.put(name, value);

		}
		Class superClzz = clazz.getSuperclass();
		getClass(superClzz, map, obj);
	}

	/**
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private static HashMap convertHashMap(HashMap map) throws Exception {

		HashMap newMap = new HashMap();
		Set keys = map.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			Object key = it.next();
			convertToString(map.get(key), newMap, key);
		}

		return newMap;
	}

	/**
	 * 
	 * @param value
	 * @param newMap
	 * @param key
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void convertToString(Object value, HashMap newMap, Object key) {
		if (value != null) {
			Class clazz = value.getClass();
			if (isBaseType(clazz)) {
				newMap.put(key, value.toString());
			} else if (clazz == String.class) {
				newMap.put(key, value.toString());
			} else if (clazz == Date.class) {
				Date date = (Date) value;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				newMap.put(key, sdf.format(date));
			} else if (clazz == Timestamp.class) {
				Timestamp timestamp = (Timestamp) value;
				long times = timestamp.getTime();
				Date date = new Date(times);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				newMap.put(key, sdf.format(date));
			} else if (clazz == java.sql.Date.class) {
				java.sql.Date sqlDate = (java.sql.Date) value;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				newMap.put(key, sdf.format(sqlDate));
			} else {
				newMap.put(key, value);
			}
		} else {
			newMap.put(key, value);
		}

	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static boolean isBaseType(Class clazz) {

		if (clazz == Integer.class) {
			return true;
		}
		if (clazz == Long.class) {
			return true;
		}
		if (clazz == Double.class) {
			return true;
		}
		if (clazz == Byte.class) {
			return true;
		}
		if (clazz == Float.class) {
			return true;
		}
		if (clazz == Short.class) {
			return true;
		}
		if (clazz == Boolean.class) {
			return true;
		}
		return false;
	}

	public static <T> T ifNull(T parm, T out) {
		return null == parm ? out : parm;
	}

	public static <T> boolean ifNull(T parm) {
		return null == parm;
	}

	public static String ifBlank(String parm, String out) {
		return StringUtils.isBlank(parm) ? out : parm;
	}

	public static boolean isNumeric(String str) {
		if (StringUtils.isBlank(str))
			return false;

		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i)))
				return false;
		}

		return true;
	}

	public static String getStrLstFromJaByKey(JSONArray ja, String key) {
		String strList = "";
		for (int i = 0; i < ja.size(); i++) {
			if ("".equalsIgnoreCase(strList))
				strList += ObjUtil.ifNull(ja.getJSONObject(i).getString(key), "");
			else
				strList += "," + ObjUtil.ifNull(ja.getJSONObject(i).getString(key), "");
		}

		return strList;
	}

	public static long toLong(Object objVal, boolean chkNull) throws MyException {
		if (null == objVal) {
			if (chkNull) {
				throw new MyException(-2, "输入值错");
			} else {
				return -1L;
			}
		}

		try {
			return Long.valueOf(objVal.toString());
		} catch (Exception e) {
			if (chkNull) {
				throw new MyException(-2, "输入类型错误");
			} else {
				return -1L;
			}
		}
	}

	public static long toLong(String key, Object objVal, boolean chkNull) throws MyException {
		if (null == objVal) {
			if (chkNull) {
				throw new MyException(-2, key + "输入值错");
			} else {
				return -1L;
			}
		}

		try {
			return Long.valueOf(objVal.toString());
		} catch (Exception e) {
			throw new MyException(-2, "输入类型错误");
		}
	}

	public static long toLong(Object objVal) throws MyException {
		return toLong(objVal, true);
	}

	public static int toInt(Object objVal, boolean chkNull) throws MyException {
		if (null == objVal) {
			if (chkNull) {
				throw new MyException(-2, "输入值错");
			} else {
				return -1;
			}
		}

		try {
			return Integer.valueOf(objVal.toString());
		} catch (Exception e) {
			if (chkNull) {
				throw new MyException(-2, "输入类型错误");
			} else {
				return -1;
			}
		}
	}

	public static int toInt(String key, Object objVal, boolean chkNull) throws MyException {
		if (null == objVal || "" == objVal) {
			if (chkNull) {
				throw new MyException(-2, key + "输入值错");
			} else {
				return -1;
			}
		}

		try {
			return Integer.valueOf(objVal.toString());
		} catch (Exception e) {
			if (chkNull) {
				throw new MyException(-2, "输入类型错误");
			} else {
				return -1;
			}
		}
	}

	public static int toInt(Object objVal, int value) {
		if (null == objVal) {
			return value;
		}

		try {
			return Integer.valueOf(objVal.toString());
		} catch (Exception e) {
			return value;
		}
	}

	public static int toInt(Object objVal) throws MyException {
		return toInt(objVal, true);
	}

	public static double toDouble(Object objVal, boolean chkNull) throws MyException {
		if (null == objVal) {
			if (chkNull) {
				throw new MyException(-2, "输入值错");
			} else {
				return -1;
			}
		}

		try {
			return Double.valueOf(objVal.toString());
		} catch (Exception e) {
			if (chkNull) {
				throw new MyException(-2, "输入类型错误");
			} else {
				return -1;
			}
		}
	}

	public static double toDouble(Object objVal) throws MyException {
		return toDouble(objVal, true);
	}

	public static float toFloat(Object objVal, boolean chkNull) throws MyException {
		if (null == objVal) {
			if (chkNull) {
				throw new MyException(-2, "输入值错");
			} else {
				return -1;
			}
		}

		try {
			return Float.valueOf(objVal.toString());
		} catch (Exception e) {
			throw new MyException(-2, "输入类型错误");
		}
	}

	// type:0 非必�?1:必填 2:如果key不这空则必填
	public static float toFloat(String key, Object objVal, boolean chkNull) throws MyException {
		if (null == objVal) {
			if (chkNull) {
				throw new MyException(-2, key + "输入值错");
			} else {
				return -1;
			}
		}

		try {
			return Float.valueOf(objVal.toString());
		} catch (Exception e) {
			throw new MyException(-2, "输入类型错误");
		}
	}

	public static float toFloat(Object objVal) throws MyException {
		return toFloat(objVal, true);
	}

	public static String toString(Object objVal, boolean chkNull) throws MyException {
		if (null == objVal) {
			if (chkNull) {
				throw new MyException(-2, "输入值错");
			} else {
				return "";
			}
		}

		try {
			return objVal.toString();
		} catch (Exception e) {
			throw new MyException(-2, "输入类型错误");
		}
	}

	// type:0 非必�?1:必填 2:如果key不这空则必填
	public static String toString(String key, Object objVal, int type, Map<String, String[]> parasMap)
			throws MyException {
		if (type == 1) {
			if (org.springframework.util.StringUtils.isEmpty(objVal))
				throw new MyException(-2, key + "输入参数不能为空");
			return objVal.toString();
		} else if (type == 2) {
			if (parasMap.containsKey(key) && org.springframework.util.StringUtils.isEmpty(objVal))
				throw new MyException(-2, key + "输入参数不能为空");
			if (org.springframework.util.StringUtils.isEmpty(objVal)) {
				return "";
			} else {
				return objVal.toString();
			}
		} else {
			if (org.springframework.util.StringUtils.isEmpty(objVal)) {
				return "";
			} else {
				return objVal.toString();
			}
		}
	}

	public static String toString(Object objVal) throws MyException {
		return toString(objVal, true);
	}

	/**
	 * 参数工具
	 * 
	 * @Title paramsUtil
	 * @Description 把参数转换成string 避免�?null的情�?
	 * @author tianzy
	 * @param params
	 *            参数
	 * @return
	 */
	public static String paramsUtil(Object params) {
		if (org.springframework.util.StringUtils.isEmpty(params))
			return "";
		return params.toString();
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String args[]) throws Exception {
		// System.out.println("----"+toString("country","123",2));
		System.out.println("----" + toInt("country", "", true));
		// System.out.println("tt"+StringUtils.isBlank(null));
	}

}
