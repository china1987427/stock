package com.china.stock.common.tool.conf;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.china.stock.common.tool.base.ObjUtil;








public class ConfIns
{
	public String getValue(String key) throws IOException
	{
		return getValue(key,null);
	}
	public String getValue(String key,String conf) throws IOException 
	{
		Resource resource = new ClassPathResource(ObjUtil.ifNull(conf, "conf.properties"));
		Properties props = PropertiesLoaderUtils.loadProperties(resource);
		
		return props.getProperty(key);
	}
}
