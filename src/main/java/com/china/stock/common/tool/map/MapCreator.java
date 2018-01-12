package com.china.stock.common.tool.map;

import java.util.HashMap;
import java.util.Map;

public class MapCreator<K,V>
{
	private Map<K,V> map = new HashMap<K,V>();

	public MapCreator<K,V> MapCreater()
	{
		return this;
	}
	
	public MapCreator<K,V> add(K key,V val)
	{
		this.map.put(key, val);
		return this;
	}
	
	public Map<K,V> getMap()
	{
		return this.map;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
		//MapCreater mm = new MapCreater("key1",(Object)12);
		
		System.out.println((new MapCreator<String,Object>()
		                    .add("key1",123)
		                    .add("key2", "abc")
		                    .add("key3", "bcd")
		                    .getMap()));

	}

}
