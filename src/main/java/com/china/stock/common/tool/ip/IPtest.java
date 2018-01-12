package com.china.stock.common.tool.ip;

import junit.framework.TestCase;

public class IPtest extends TestCase {
	public static void main(String args[]) {
		IPtest ipTest = new IPtest();
		String path = ipTest.getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath();
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(1, path.indexOf("/classes/"));
		}
		path+="/qqwry";
		// 指定纯真数据库的文件名，所在文件夹
		IPSeeker ip = new IPSeeker("QQWry.Dat", path);
		// 测试IP 58.20.43.13
		System.out.println(path);
		System.out.println(ip.getIPLocation("182.149.194.0").getCountry());
		System.out.println(ip.getIPLocation("182.149.194.0").getArea());
	}
}
