package com.china.stock.test;

import com.china.stock.common.tool.base.EncryptUtil;

public class Test5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String a = EncryptUtil.md5Digest("1987427", 32);
			System.out.println(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
