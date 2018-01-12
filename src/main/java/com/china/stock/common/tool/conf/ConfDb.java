package com.china.stock.common.tool.conf;

import java.io.IOException;

public class ConfDb
{
	public static ConfIns confIns = new ConfIns();
	public static String getValue(String key) throws IOException
	{
		return confIns.getValue(key,"jdbc.properties");
	}
	

}
