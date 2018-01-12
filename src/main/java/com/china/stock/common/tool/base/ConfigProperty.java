package com.china.stock.common.tool.base;

import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigProperty {
	
	public static String getProperty(String entry) {
		return confProps.getProperty(entry);
	}

	private final static Properties confProps = new Properties();

	static {
		Logger log = Logger.getLogger(ConfigProperty.class);
		try {
			confProps.loadFromXML(ConfigProperty.class.getResourceAsStream("/sysconfig.xml"));
		}
		catch (Exception ex) {
			log.warn("Load Edrive Config file failed.", ex);
		}
	}
}
