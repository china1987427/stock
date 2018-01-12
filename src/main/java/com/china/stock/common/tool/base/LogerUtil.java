package com.china.stock.common.tool.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author scnbhzw 用于获取log对象
 */
abstract public class LogerUtil {
	public static Logger log;
	static {
		log = LoggerFactory.getLogger(LogerUtil.class);
	}

	public static void writeInfo(String message) {

		log.info(message);
	}

	public static void writeError(String message) {

		log.error(message);
	}

}
