package com.china.stock.common.tool.conf;

import java.io.IOException;
import java.util.Random;


public class Conf
{
	public static ConfIns confIns = new ConfIns();
	public static String getValue(String key) throws IOException
	{
		return confIns.getValue(key);
	}
	
	public static String getValue(String key,String conf) throws IOException
	{
		return confIns.getValue(key,conf);
	}
	
	public static String getTfsValue() throws IOException{
		String[] array = getValue("tfsurls").split(",");
		Random random = new Random();
		return getValue((array[random.nextInt(array.length)]));
	}
	
}
