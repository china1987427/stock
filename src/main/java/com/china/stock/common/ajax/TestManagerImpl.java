package com.china.stock.common.ajax;



public class TestManagerImpl implements TestManager {
	private static String a = "aaa";
	public void init() {
		System.out.println("-----------------");
	}

	@Override
	public String getStock(String stockCode) throws Exception {
		System.out.println("==========");
		return "ok";
	}

	
}
