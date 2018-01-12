package com.china.stock.common.tool.base;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>字符串操作的常用方法总结</p>
 * 
 * @author zwh
 * @version 1.0.0
 */
public class StrUtils {

	public static boolean isEmpty(String _str) {
		if (_str==null || _str.equals("")) {
			return true;
		}
		return false;
	}

	public static String null2String(Object s) {
		return s == null ? "" : s.toString();
	}

	public static Integer null2Int(Object s) {
		Integer v = 0;
		if (s != null) {
			try {
				v = Integer.parseInt(s.toString());
			} catch (Exception e) {
			}
		}
		return v;
	}

	public static Long null2Long(Object s) {
		long v = 0;
		if (s != null) {
			try {
				v = Long.parseLong(s.toString());
			} catch (Exception e) {
			}
		}
		return v;
	}

	public static String encodeURL(String url) {
		if (url != null && url.length() > 0) {
			try {
				return URLEncoder.encode(url, "utf-8");
			} catch (UnsupportedEncodingException ex) {
				return url;
			}
		}
		return url;
	}

	public static String decodeURL(String url) {
		if (url != null && url.length() > 0) {
			try {
				url = new String(url.getBytes("ISO-8859-1"), "utf-8");
				return URLDecoder.decode(url, "utf-8");
			} catch (UnsupportedEncodingException ex) {
				return url;
			}
		}
		return url;
	}

	public static final String replaceAll(String src, String fnd, String rep) {
		if (src == null || src.equals("")) {
			return "";
		}

		String dst = src;

		int idx = dst.indexOf(fnd);

		while (idx >= 0) {
			dst = dst.substring(0, idx) + rep
					+ dst.substring(idx + fnd.length(), dst.length());
			idx = dst.indexOf(fnd, idx + rep.length());
		}

		return dst;
	}

	/**
	 * 有特殊符号的字符串转为数组：
	 * 
	 * @param src
	 *            ,regex
	 * @return string[]
	 */
	public static String[] strToArray(String src, String regex) {
		if (src == null || src.length() == 0)
			return new String[0];

		String[] result = src.split(regex);
		for (int i = 0; i < result.length; i++) {
			if ("null".equals(result[i]))
				result[i] = null;
		}
		return result;
	}

	/**
	 * 是否有中文字�?
	 * 
	 * @param s
	 * @return
	 */
	public static boolean hasCn(String s) {
		if (s == null) {
			return false;
		}
		return countCn(s) > s.length();
	}

	/**
	 * 获得字符。符合中文习惯�?
	 * 
	 * @param s
	 * @param length
	 * @return
	 */
	public static String getCn(String s, int len) {
		if (s == null) {
			return s;
		}
		int sl = s.length();
		if (sl <= len) {
			return s;
		}
		// 留出�?��位置用于�?
		len -= 1;
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		while (count < maxCount && i < sl) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
			i++;
		}
		if (count > maxCount) {
			i--;
		}
		return s.substring(0, i) + "…";
	}

	/**
	 * 计算GBK编码的字符串的字节数
	 * 
	 * @param s
	 * @return
	 */
	public static int countCn(String s) {
		if (s == null) {
			return 0;
		}
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		return count;
	}

	/**
	 * 文本转html
	 * 
	 * @param txt
	 * @return
	 */
	public static String txt2htm(String txt) {
		if (txt.equals("")) {
			return txt;
		}
		StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
		char c;
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			switch (c) {
			case '&':
				sb.append("&amp;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case ' ':
				sb.append("&nbsp;");
				break;
			case '\n':
				sb.append("<br/>");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * html转文�?
	 * 
	 * @param htm
	 * @return
	 */
	public static String htm2txt(String htm) {
		if (htm == null) {
			return htm;
		}
		htm = htm.replace("&amp;", "&");
		htm = htm.replace("&lt;", "<");
		htm = htm.replace("&gt;", ">");
		htm = htm.replace("&quot;", "\"");
		htm = htm.replace("&nbsp;", " ");
		htm = htm.replace("<br/>", "\n");
		return htm;
	}

	/**
	 * 替换字符�?
	 * 
	 * @param sb
	 * @param what
	 * @param with
	 * @return
	 */
	public static StringBuilder replace(StringBuilder sb, String what,
			String with) {
		int pos = sb.indexOf(what);
		while (pos > -1) {
			sb.replace(pos, pos + what.length(), with);
			pos = sb.indexOf(what);
		}
		return sb;
	}

	/**
	 * 替换字符�?
	 * 
	 * @param s
	 * @param what
	 * @param with
	 * @return
	 */
	public static String replace(String s, String what, String with) {
		return replace(new StringBuilder(s), what, with).toString();
	}

	/**
	 * 全角-->半角
	 * 
	 * @param qjStr
	 * @return
	 */
	public String Q2B(String qjStr) {
		String outStr = "";
		String Tstr = "";
		byte[] b = null;
		for (int i = 0; i < qjStr.length(); i++) {
			try {
				Tstr = qjStr.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (b[3] == -1) {
				b[2] = (byte) (b[2] + 32);
				b[3] = 0;
				try {
					outStr = outStr + new String(b, "unicode");
				} catch (java.io.UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else
				outStr = outStr + Tstr;
		}
		return outStr;
	}

	public static final char[] N62_CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };
	public static final char[] N36_CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };

	private static StringBuilder longToNBuf(long l, char[] chars) {
		int upgrade = chars.length;
		StringBuilder result = new StringBuilder();
		int last;
		while (l > 0) {
			last = (int) (l % upgrade);
			result.append(chars[last]);
			l /= upgrade;
		}
		return result;
	}

	/**
	 * 长整数转换成N62
	 * 
	 * @param l
	 * @return
	 */
	public static String longToN62(long l) {
		return longToNBuf(l, N62_CHARS).reverse().toString();
	}

	public static String longToN36(long l) {
		return longToNBuf(l, N36_CHARS).reverse().toString();
	}

	/**
	 * 长整数转换成N62
	 * 
	 * @param l
	 * @param length
	 *            如N62不足length长度，则补足0�?
	 * @return
	 */
	public static String longToN62(long l, int length) {
		StringBuilder sb = longToNBuf(l, N62_CHARS);
		for (int i = sb.length(); i < length; i++) {
			sb.append('0');
		}
		return sb.reverse().toString();
	}

	public static String longToN36(long l, int length) {
		StringBuilder sb = longToNBuf(l, N36_CHARS);
		for (int i = sb.length(); i < length; i++) {
			sb.append('0');
		}
		return sb.reverse().toString();
	}

	/**
	 * N62转换成整�?
	 * 
	 * @param n62
	 * @return
	 */
	public static long n62ToLong(String n62) {
		return nToLong(n62, N62_CHARS);
	}

	public static long n36ToLong(String n36) {
		return nToLong(n36, N36_CHARS);
	}

	private static long nToLong(String s, char[] chars) {
		char[] nc = s.toCharArray();
		long result = 0;
		long pow = 1;
		for (int i = nc.length - 1; i >= 0; i--, pow *= chars.length) {
			int n = findNIndex(nc[i], chars);
			result += n * pow;
		}
		return result;
	}

	private static int findNIndex(char c, char[] chars) {
		for (int i = 0; i < chars.length; i++) {
			if (c == chars[i]) {
				return i;
			}
		}
		throw new RuntimeException("N62(N36)非法字符" + c);
	}

	public static String getTreeDepth(int nSpaceCount) throws Exception {
		String str = "";
		for (int i = 0; i < nSpaceCount; ++i) {
			str = str + "<q style='padding:0 10px'>";
		}
		return str;
	}

	public static String getNbspDepth(int nSpaceCount) {
		String str = "";
		for (int i = 0; i < nSpaceCount; ++i) {
			str = str + " ";
		}
		return str;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String gbk2utf8(String str) {
		try {
			if (str != null) {
				return new String(str.getBytes("gbk"), "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成 catch �?
			e.printStackTrace();
		}
		return null;
	}

	public static String iso2gbk(String str) {
		try {
			if (str != null) {
				return new String(str.getBytes("ISO8859-1"), "gbk");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成 catch �?
			e.printStackTrace();
		}
		return null;
	}

	public static String utf82gbk(String str) {
		try {
			if (str != null) {
				return new String(str.getBytes("utf-8"), "gbk");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成 catch �?
			e.printStackTrace();
		}
		return null;
	}

	public static String getPercent(BigDecimal a, BigDecimal b) {
		return a.multiply(new BigDecimal(100))
				.divide(b, 2, BigDecimal.ROUND_HALF_UP).toString()
				+ "%";
	}

	/**
	 * 得到�?��uuid
	 * 
	 * @return
	 */
	public static Serializable createId() {
		return UUID.randomUUID().toString();
	}

	public static String createStringId() {
		return (String) createId();
	}

	/**
	 * 获取double数据的最小�?
	 * @param bd
	 * @return
	 */
	public static double getMin(double[] bd) {
		double num =0;
		for (int i = 0; i < bd.length; i++) {
			num = bd[i]<num ? bd[i] : num;
		}
		return num;
	}

	public static void main(String[] args) {
	}
	/**
	 * 对Map利用分隔符进行连接
	 * @param separate 分隔符
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String join(Map<String,?> obj,String separate)throws Exception{
		String param = "";
		if(obj.size()!=0){
			for(Entry entry : obj.entrySet()){
				param += entry.getKey()+"="+URLEncoder.encode(entry.getValue().toString(), "utf-8")+separate;
			}
			param = param.substring(0,param.length()-1);
		}
		return param;
	}
}