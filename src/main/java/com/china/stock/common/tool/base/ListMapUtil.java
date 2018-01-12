package com.china.stock.common.tool.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListMapUtil
{

	public static List<Map<String,Object>> getEmptyListMap()
	{
		return new ArrayList<Map<String,Object>>();
	}
	
	public static Map<String,Object> getEmptyMap()
	{
		return new HashMap<String,Object>();
	}
	
	public static Map<String,String> getEmptyMapString()
	{
		return new HashMap<String,String>();
	}
	
	public static List<Map<String,Object>> minGroupBy(List<Map<String,Object>> sourceListMap,String minKey,String groupByKey)
	{
		List<Map<String,Object>> distinctListMap = distinct(sourceListMap,groupByKey);
		List<Map<String,Object>> newListMap = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map:distinctListMap)
		{
			newListMap.add(min(select(sourceListMap,groupByKey,map.get(groupByKey)),minKey));
		}
		return newListMap;
	}
	
	public static Map<String,Object> minWhr(List<Map<String,Object>> sourceListMap,String minKey,String whrKey,Object whrValue)
	{
		Map<String,Object> rltMap = null;
		for(Map<String,Object> map:sourceListMap)
		{
			if(map.get(whrKey).toString().equals(whrValue.toString()))
			{
				if(null == rltMap)
					rltMap = map;
				else
				{
					if (Double.parseDouble(rltMap.get(minKey).toString())>Double.parseDouble(map.get(minKey).toString()))
					{
						rltMap = map;
					}
				}
			}
		}
		return rltMap;
	}
	
	public static Map<String,Object> maxWhr(List<Map<String,Object>> sourceListMap,String minKey,String whrKey,Object whrValue)
	{
		Map<String,Object> rltMap = null;
		for(Map<String,Object> map:sourceListMap)
		{
			if(map.get(whrKey).toString().equals(whrValue.toString()))
			{
				if(null == rltMap)
					rltMap = map;
				else
				{
					if (Double.parseDouble(rltMap.get(minKey).toString())<Double.parseDouble(map.get(minKey).toString()))
					{
						rltMap = map;
					}
				}
			}
		}
		return rltMap;
	}
	
	public static Map<String,Object> min(List<Map<String,Object>> sourceListMap,String key)
	{
		Map<String,Object> rltMap = null;
		for(Map<String,Object> map:sourceListMap)
		{
			if(null == rltMap)
				rltMap = map;
			else
			{
				if (Double.parseDouble(rltMap.get(key).toString())>Double.parseDouble(map.get(key).toString()))
				{
					rltMap = map;
				}
			}
		}
		return rltMap;
	}

	public static List<Map<String,Object>> select(List<Map<String,Object>> sourceListMap,String key,Object value)
	{
		List<Map<String,Object>> newListMap = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map:sourceListMap)
		{
			if(map.get(key).toString().equals(value.toString()))
				newListMap.add(map);
		}
		return newListMap;
	}
	
	public static List<Map<String,Object>> select(List<Map<String,Object>> sourceListMap,String key,Object value,String removeKeys)
	{
		List<Map<String,Object>> newListMap = new ArrayList<Map<String,Object>>();
		Map<String,Object> mapTemp = null;
		for(Map<String,Object> map:sourceListMap)
		{
			if(map.get(key).toString().equals(value.toString())){
				mapTemp = copyMap(map);
				for(String keys : removeKeys.split(",")) {					
					mapTemp.remove(keys);
				}
				newListMap.add(mapTemp);
			}				
		}
		return newListMap;
	}
	
	public static <K,V> List<Map<K,V>> selectLike(List<Map<K,V>> sourceListMap,K key,V value)
	{
		List<Map<K,V>> newListMap = new ArrayList<Map<K,V>>();
		for(Map<K,V> map:sourceListMap)
		{
			if(map.get(key).toString().contains(value.toString()))
				newListMap.add(map);
		}
		return newListMap;
	}
	
	public static List<Map<String,Object>> update(List<Map<String,Object>> sourceListMap,String updateKey,Object updateValue,String whrKey,Object whrValue)
	{
		for(Map<String,Object> map:sourceListMap)
		{
			if(map.get(whrKey).toString().equals(whrValue.toString()))
				map.put(updateKey,updateValue);
		}
		
		return sourceListMap;
	}
	
	public static List<Map<String,Object>> distinct(List<Map<String,Object>> sourceListMap,String key)
	{
		List<Map<String,Object>> newListMap = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map:sourceListMap)
		{
			if (!exists(newListMap,key,map.get(key)))
				newListMap.add(map);
		}
		return newListMap;
	}
	
	public static List<Map<String,Object>> distinct(List<Map<String,Object>> sourceListMap,String key,String removeKeys)
	{
		List<Map<String,Object>> newListMap = new ArrayList<Map<String,Object>>();
		Map<String,Object> mapTemp = null;
		for(Map<String,Object> map:sourceListMap)
		{
			if (!exists(newListMap,key,map.get(key))){
				mapTemp = copyMap(map);
				for(String keys : removeKeys.split(",")) {
					mapTemp.remove(keys);
				}
				newListMap.add(mapTemp);
			}				
		}
		return newListMap;
	}
	
	public static List<String> distinctKey(List<Map<String,Object>> sourceListMap,String key)
	{
		List<String> newList = new ArrayList<String>();
		for(Map<String,Object> map:sourceListMap)
				newList.add(map.get(key).toString());
		
		return newList;
	}
	
	public static Map<String,Object> copyMap(Map<String,Object> inMap)
	{
		Map<String,Object> outMap = new HashMap<String,Object>();
		for(Map.Entry<String, Object> entry : inMap.entrySet())
    	{   
			outMap.put(entry.getKey(), entry.getValue());
    	}
		
		return outMap;
	}
	
	public static List<Map<String,Object>> copyListMap(List<Map<String,Object>> inListMap)
	{
		List<Map<String,Object>> outListMap = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> inMap:inListMap)
		{
			outListMap.add(copyMap(inMap));
		}
		return outListMap;
	}
	
	public static boolean exists(List<Map<String,Object>> sourceListMap,String key,Object value)
	{
		for(Map<String,Object> map:sourceListMap)
		{
			//System.out.println(map.get(key).toString()+";"+value.toString());
			if(map.get(key).toString().equals(value.toString()))
			{
				return true;
			}
		}
		return false;
	}
	public static boolean existsNew(List<Map<String,Object>> sourceListMap,String key)
	{
		for(Map<String,Object> map:sourceListMap)
		{
			//System.out.println(map.get(key).toString()+";"+value.toString());
			if(map.get(key).toString()!=null&&""!=map.get(key).toString())
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean exists(List<Map<String,Object>> sourceListMap,String key,Object value,String key1,Object value1)
	{
		for(Map<String,Object> map:sourceListMap)
		{
			//System.out.println(map.get(key).toString()+";"+value.toString());
			if(map.get(key).toString().equals(value.toString())&&map.get(key1).toString().equals(value1.toString()))
			{
				return true;
			}
		}
		return false;
	}
	
	public static void clear(List<Map<String,Object>> listMap)
	{
		for(Map<String,Object> map:listMap)
		{
			map.clear();
		}
		listMap.clear();
	}
	
	public static void mapSelectCut(Map<String,Object> map,String keyLst)
	{
		keyLst = ","+keyLst+",";
		for(Map.Entry<String, Object> entry : map.entrySet())
    	{   
    		String curKey = entry.getKey();
			if(!keyLst.contains(","+entry.getKey()+","))
				map.remove(curKey);
    	}   
	}
	
	public static void listMapSelectCut(List<Map<String,Object>> listMap,String keyLst)
	{
		for(Map<String,Object> map:listMap)
			mapSelectCut(map,keyLst);
	}
	
	public static List<Map<String,Object>> selectCopy(List<Map<String,Object>> sourceListMap,String key,Object value)
	{
		List<Map<String,Object>> newListMap = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map:sourceListMap)
		{
			if(map.get(key).toString().equals(value.toString()))
				newListMap.add(copyMap(map));
		}
		return newListMap;
	}
	
	public static List<Map<String,Object>> InnerJoin(List<Map<String,Object>> listMapLeft,List<Map<String,Object>> listMapRight,String key)
	{
		List<Map<String,Object>> listMapOut = getEmptyListMap();
		for(Map<String,Object> map:listMapLeft)
		{
			for(Map<String,Object> mapTmp:selectCopy(listMapRight,key,map.get(key)))
			{
				listMapOut.add(copyMap(mapTmp));
			}
		}
		
		return listMapOut;
	}
	public static List<Map<String,String>> select2String(List<Map<String,String>> sourceListMap,String key,Object value){
		List<Map<String,String>> newListMap = new ArrayList<Map<String,String>>();
		for(Map<String,String> map:sourceListMap)
		{
			if(map.get(key).toString().equals(value.toString()))
				newListMap.add(map);
		}
		return newListMap;
	}
	public static Map<String,String> selectMap(List<Map<String,String>> sourceListMap,String key,Object value,String key2,Object value2){
		for(Map<String,String> map:sourceListMap)
		{
			if(map.get(key).toString().equals(value.toString())&&!map.get(key2).toString().equals(value2.toString()))
				return map;
		}
		return new HashMap<String,String>();
	}
	public static Map<String,Object> selectMapNew(List<Map<String,Object>> sourceListMap,String key,Object value,String key2,Object value2){
		for(Map<String,Object> map:sourceListMap)
		{
			if(map.get(key).toString().equals(value.toString())&&!map.get(key2).toString().equals(value2.toString()))
				return map;
		}
		return new HashMap<String,Object>();
	}
	public static boolean exists(List<Map<String,String>> sourceListMap,String key){
		for(Map<String,String> map:sourceListMap)
		{
			if(String.valueOf(map.get(key))!=""&&String.valueOf(map.get(key))!=null)
			{
				return true;
			}
		}
		return false;
	}
}
