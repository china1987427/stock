package com.china.stock.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
	public static void main(String[] args) {
		String a = StringUtil.getStrByLT("yyyy-MM-dd", "2015-10-22", 60);
		System.out.println(a);
	}

	public static String getWeek(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String week = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
			week = sdf.format(format.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return week;
	}

	public static String getStrDate(String str, Object date) {
		SimpleDateFormat sdf = new SimpleDateFormat(str);
		String strDate = sdf.format(date);
		return strDate;
	}

	public static Date getDate(String str, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(str);
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	public static String getStrByLT(String str, String date, int num) {
		SimpleDateFormat sdf = new SimpleDateFormat(str);
		String strDate = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			long l = format.parse(date).getTime() + num * 24 * 60 * 60 * 1000;
			strDate = sdf.format(new Date(l));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return strDate;
	}

	public static boolean isTimeDiffer(Date date) {
		boolean result = false;
		long differ = new Date().getTime() - date.getTime();
		if (differ < 180000) {
			result = true;
		}
		return result;
	}

	public static String replace(String original, String oldString, String newString, int counts) {
		if (original == null || oldString == null || newString == null)
			return "";
		if (counts < 0)
			throw new IllegalArgumentException("parameter counts can not be negative");
		StringBuilder sb = new StringBuilder();
		int end = original.indexOf(oldString);
		int start = 0;
		int stringSize = oldString.length();
		for (int currentCount = 0; end != -1; currentCount++) {
			if (counts != 0 && currentCount >= counts)
				break;
			sb.append(original.substring(start, end));
			sb.append(newString);
			start = end + stringSize;
			end = original.indexOf(oldString, start);
		}

		end = original.length();
		sb.append(original.substring(start, end));
		return sb.toString();
	}
}
