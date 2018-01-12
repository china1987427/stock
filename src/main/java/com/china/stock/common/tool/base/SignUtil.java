package com.china.stock.common.tool.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SignUtil {
	/**
	 * 给TOP请求签名。
	 * 
	 * @param requestHolder 所有字符型的TOP请求参数
	 * @param secret 签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSign(Map<String, String> params ,String appkey, String secret) throws IOException 
	{

		params.put("appkey", appkey);
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);

		// 第二步：把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder(secret);
		for (String key : keys) {
			String value = String.valueOf(params.get(key));//params.get(key).toString();
			if (areNotEmpty(key, value)) {
				query.append(key).append(value);
			}
		}

		// 第三步：使用MD5加密
		byte[] bytes = encryptMD5(query.toString());

		// 第四步：把二进制转化为大写的十六进制
		return byte2hex(bytes);
	}
	/**
	 * 给TOP请求签名。
	 * 
	 * @param requestHolder 所有字符型的TOP请求参数
	 * @param secret 签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignO(Map<String, Object> params ,String appkey, String secret) throws IOException 
	{

		params.put("appkey", appkey);
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);

		// 第二步：把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder(secret);
		for (String key : keys) {
			String value = params.get(key).toString();
			if (areNotEmpty(key, value)) {
				query.append(key).append(value);
			}
		}

		// 第三步：使用MD5加密
		byte[] bytes = encryptMD5(query.toString());

		// 第四步：把二进制转化为大写的十六进制
		return byte2hex(bytes);
	}
	
	private static byte[] encryptMD5(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			bytes = md.digest(data.getBytes("UTF-8"));
		} catch (GeneralSecurityException gse) {
			String msg = getStringFromException(gse);
			throw new IOException(msg);
		}
		return bytes;
	}
	
	private static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}
	
	private static String getStringFromException(Throwable e) {
		String result = "";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bos);
		e.printStackTrace(ps);
		try {
			result = bos.toString("UTF-8");
		} catch (IOException ioe) {
		}
		return result;
	}
	
	/**
	 * 检查指定的字符串是否为空。
	 * <ul>
	 * <li>SysUtils.isEmpty(null) = true</li>
	 * <li>SysUtils.isEmpty("") = true</li>
	 * <li>SysUtils.isEmpty("   ") = true</li>
	 * <li>SysUtils.isEmpty("abc") = false</li>
	 * </ul>
	 * 
	 * @param value 待检查的字符串
	 * @return true/false
	 */
	private static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 检查指定的字符串列表是否不为空。
	 */
	private static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("method", "tyfo.user.login");
		params.put("source", "2");
		params.put("seq", "20141008105800000001");
		params.put("account", "id01");
		params.put("password", "123456");
		//System.out.println(getSign(params,"12345670","82db84b1a65b4d5dec34d3801bcdb1eb"));
		//System.out.println(getSign(MapMaker.makeStrStr("ordersn","OD201408031101467890","method","TradeAction.getOrderByShopSn"),"a9731a1740532c4f064db514b6a75b6c"));

	}

}
