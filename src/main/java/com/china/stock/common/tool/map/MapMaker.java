package com.china.stock.common.tool.map;

import java.util.Map;

public class MapMaker 
{ 
	/**
	 * 初始化map对象
	 */
	public static Map<String,Object> makeEmpty()
	{
		return new MapCreator<String,Object>().getMap();
	}
	
	public static Map<String,String> makeEmptyStrStr()
	{
		return new MapCreator<String,String>().getMap();
	}
	
	/**
	 * 将对象转换成map
	 * @param pramArray 对象信息
	 * @return
	 */
	public static Map<String,Object> makeStrObj(Object... pramArray)
	{
		MapCreator<String,Object> mapCreator = new MapCreator<String,Object>();
		for(int i=0;i<pramArray.length-1;i=i+2)
		{
			mapCreator.add(pramArray[i].toString(), pramArray[i+1]);
		}
		return mapCreator.getMap();
	}
	
	/**
	 * 将String对象转换成map
	 * @param pramArray 对象信息
	 * @return
	 */
	public static Map<String,String> makeStrStr(String... pramArray)
	{
		MapCreator<String,String> mapCreator = new MapCreator<String,String>();
		for(int i=0;i<pramArray.length-1;i=i+2)
		{
			mapCreator.add(pramArray[i].toString(), pramArray[i+1]);
		}
		return mapCreator.getMap();
	}
}
