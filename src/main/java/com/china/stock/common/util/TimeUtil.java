package com.china.stock.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtil {
	public static void main(String[] args) {
		List<String> list = TimeUtil.getTimeArray();
		System.out.println(list);
	}

	public static Object sdfDateTime(String sdfParam, String datetime, String str, Date d) {
		Object date = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(sdfParam);
			if ("str".equals(str)) {
				date = sdf.format(d);
			} else if ("date".equals(str)) {
				date = sdf.parse(datetime);
			} else if ("week".equals(str)) {
				if ("".equals(datetime) && d != null) {
					date = sdf.format(d);
				} else if (!"".equals(datetime) && d == null) {
					sdf = new SimpleDateFormat("yyyy-MM-dd");
					date = sdf.format(sdf.parse(datetime));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Calendar setCal(int hour, int minute, int second) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		return c;
	}

	public static List<String> getTimeArray() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c1 = setCal(9, 29, 0);
		Calendar c2 = setCal(11, 30, 0);
		Calendar c3 = setCal(13, 0, 0);
		Calendar c4 = setCal(15, 0, 0);
		long time = c1.getTimeInMillis();
		List<String> list = new ArrayList<String>();
		while (true) { // 循环条件中直接为TRUE
			time += 60000;
			if (time <= c2.getTimeInMillis() || time >= c3.getTimeInMillis()) {
				list.add(sdf.format(new Date(time)));
			}
			if (time >= c4.getTimeInMillis()) { // 直到符合条件后跳出本循环 否则一直循环下去
				break;
			}
		}
		return list;
	}

	public static boolean compareDate(Object date1, Object date2) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (date1 instanceof Date && date2 instanceof Date) {
				if (((Date) date1).getTime() > ((Date) date2).getTime()) {
					return true;
				} else if (((Date) date1).getTime() < ((Date) date2).getTime()) {
					return false;
				}
			} else if (date1 instanceof String && date2 instanceof String) {
				Date d1 = sdf.parse((String) date1);
				Date d2 = sdf.parse((String) date2);
				if (d1.getTime() > d2.getTime()) {
					return true;
				} else if (d1.getTime() < d2.getTime()) {
					return false;
				}
			} else if (date1 instanceof Date && date2 instanceof String) {
				Date d2 = sdf.parse((String) date2);
				if (((Date) date1).getTime() > d2.getTime()) {
					return true;
				} else if (((Date) date1).getTime() < d2.getTime()) {
					return false;
				}
			} else if (date1 instanceof String && date2 instanceof Date) {
				Date d1 = sdf.parse((String) date1);
				if (d1.getTime() > ((Date) date2).getTime()) {
					return true;
				} else if (d1.getTime() < ((Date) date2).getTime()) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static long getCal(Date date, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - num); // 获取1年的数据
		return cal.getTimeInMillis();
	}
}
