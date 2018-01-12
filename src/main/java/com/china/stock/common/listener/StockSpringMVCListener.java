package com.china.stock.common.listener;

/**
 * 监听器 ServletContextListener：用于监听WEB 应用启动和销毁的事件，监听器类需要实现
 */
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class StockSpringMVCListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
	}

	
}
