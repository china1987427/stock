package com.china.stock.common.tool.base;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taobao.common.tfs.DefaultTfsManager;
import com.taobao.common.tfs.TfsManager;

public class TFSUtil {
	/**
	 * 
	 * @Description: 获取文件服务TfsManager对象
	 * @return
	 * @throws Exception
	 */
	static TfsManager tfsManager = null;

	@SuppressWarnings("resource")
	public static TfsManager getTfsManager() {
		if (tfsManager != null) {
			return tfsManager;
		} else {
			try {
				tfsManager = new DefaultTfsManager();
				ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("config/tfs.xml");
				tfsManager = (DefaultTfsManager) ctx.getBean("tfsManager");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tfsManager;
	}

	@SuppressWarnings("unused")
	private static void tfsManagerDestroy() {
		if (tfsManager != null) {
			try {
				tfsManager.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
