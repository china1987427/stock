package com.china.stock.common.tool.base;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class JsonUtil {

	private static ObjectMapper objectMapper = null;

	static {
		objectMapper = new ObjectMapper();
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属�?
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 设置序列化是默认值和null不进行序列化�?
		// objectMapper.setSerializationInclusion(Include.NON_NULL);
		// objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objectMapper.getSerializerProvider().setNullValueSerializer(
				new JsonSerializer<Object>() {

					@Override
					public void serialize(Object obj, JsonGenerator jg,
							SerializerProvider sp) throws IOException,
							JsonProcessingException {
						jg.writeString("");
					}
				});
	}

	/**
	 * 直接序列化不过滤字段
	 * 
	 * @param value
	 *            待序列化的实体对�?
	 * @return
	 */
	public static String jsonSerialization(Object value) {

		try {
			return objectMapper.writeValueAsString(value);
		} catch (Exception e) {
			throw new RuntimeException("解析对象错误");
		}
	}

	/**
	 * 设置过滤字段的序列化
	 * 
	 * @param value
	 *            待序列化的实体对�?
	 * @param filterName
	 *            过滤器名
	 * @param properties
	 *            过滤的实体属�?
	 * @return
	 */
	public static String jsonSerialization(Object value, String filterName,
			String... properties) {

		try {
			return filter(filterName, properties).writeValueAsString(value);
		} catch (Exception e) {
			throw new RuntimeException("解析对象错误");
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T jsonDeserialization(String json, Class<?> clazz) {
		try {
			return (T) objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException("反序列化对象错误");
		}
	}

	private static ObjectMapper filter(String filterName, String... properties) {

		FilterProvider filterProvider = new SimpleFilterProvider().addFilter(
				filterName,
				SimpleBeanPropertyFilter.serializeAllExcept(properties));
		objectMapper.setFilters(filterProvider);

		return objectMapper;
	}

	/**
	 * 复杂类型json反序列化 ,比如List,Map
	 * 
	 * @param json
	 * @param collectionClass
	 * @param elementClasses
	 * @return
	 */
	public static <T> T jsonDeserialization(String json,
			Class<?> collectionClass, Class<?>... elementClasses) {
		try {
			JavaType javaType = getJavaType(collectionClass, elementClasses);
			return objectMapper.readValue(json, javaType);
		} catch (Exception e) {
			throw new RuntimeException("反序列化对象错误");
		}
	}

	private static JavaType getJavaType(Class<?> collectionClass,
			Class<?>... elementClasses) {
		return objectMapper.getTypeFactory().constructParametricType(
				collectionClass, elementClasses);
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ParseException {
		/*
		 * String privateRoleList = "1,2,3"; List<String> tempList =
		 * Arrays.asList(privateRoleList.split(",")); List<String> menuList =
		 * new ArrayList<String>(tempList);
		 * System.out.println(menuList.remove("0"));
		 */

		String jsonString = "{\"content\":{\"items\":[{\"MS_ID\":320,\"buy\":[{\"GGPS_Price\":699,\"GGPS_SKUID\":5,\"GGPS_Stock\":100,\"GG_ID\":367,\"GoodsNum\":10,\"OT_ID\":77537,\"OT_IsOld\":\"\",\"OT_Price\":699,\"OT_Status\":0}]},{\"MS_ID\":322,\"buy\":[{\"GGPS_Price\":450,\"GGPS_SKUID\":6,\"GGPS_Stock\":100,\"GG_ID\":368,\"GoodsNum\":5,\"OT_ID\":77538,\"OT_IsOld\":\"\",\"OT_Price\":450,\"OT_Status\":0}]}],\"total\":2},\"msg\":\"执行成功\",\"resultCode\":\"000000\"}";
		Map<String, Object> trolleyMap = (Map<String, Object>) JSONObject
				.parse(jsonString);

		/*
		 * Map<String, Object> userMap = JSON.parseObject(jsonString, new
		 * TypeReference<Map<String, Object>>() {});
		 * System.out.println(userMap.get("content")); System.out.println(((Map)
		 * userMap.get("content")).get("items"));
		 */

		//System.out.println(trolleyMap.get("content"));


		Map<String, Object> contentMap = ((Map<String, Object>) trolleyMap.get("content"));
		
		float goodsNumShop = 0f;// 店铺商品数量
		float priceShop = 0f;// 店铺商品价格
		float goodsNumTotal = 0f;// 总商品数�?
		float priceTotal = 0f;// 总商品价�?
		
		List<Map<String, Object>> itemMaps = (List<Map<String, Object>>) ((Map<String, Object>) trolleyMap
				.get("content")).get("items");
		for (Map<String, Object> map : itemMaps) {
			System.out.println("map:" + map.get("MS_ID"));
			System.out.println("buy:" + map.get("buy"));
			goodsNumShop = 0f;// 店铺商品数量
			priceShop = 0f;// 店铺商品价格
			List<Map<String, Object>> buyMaps = (List<Map<String, Object>>) map
					.get("buy");
			for (Map<String, Object> buymap : buyMaps) {
				goodsNumShop += Float.parseFloat(buymap.get("GoodsNum")
						.toString());
				priceShop += goodsNumShop * Float.parseFloat(buymap.get("GGPS_Price")
						.toString());
			}
			map.put("goodsNumShop", goodsNumShop);// 计算店铺内商品数�?
			map.put("priceShop", priceShop);// 店铺商品价格
			
			goodsNumTotal+=goodsNumShop;
			priceTotal+=priceShop;
		}

		contentMap.put("goodsNumTotal", goodsNumTotal);
		contentMap.put("priceTotal", priceTotal);
		
		System.out.println("cotnet:" + JSONObject.toJSONString(trolleyMap));
		
		/*
		 * for (Object o : trolleyMap.entrySet()) { Map.Entry<String,String>
		 * entry = (Map.Entry<String,String>)o;
		 * System.out.println(entry.getKey()
		 * +"--->"+entry.getValue().toString()); }
		 */

		/*
		 * Map<String, Object> map = ListMapUtil.getEmptyMap(); JSONObject json
		 * =JSONObject.parseObject(jsonString); Set<String> i = json.keySet();
		 * while (i.) { String key = (String) i.next(); String value =
		 * json.getString(key); if (value.indexOf("{") == 0) {
		 * map.put(key.trim(), getJosn(value)); } else if (value.indexOf("[") ==
		 * 0) { map.put(key.trim(), getList(value)); } else {
		 * map.put(key.trim(), value.trim()); } }
		 * System.out.println(trolleyMap.get("content"));
		 */

		/*
		 * Map<String,Object> paramMap = new HashMap<String,Object>();
		 * paramMap.put("dArg1", 1); paramMap.put("dArg2", "007");
		 * paramMap.put("dArg3", "高富�?); List<Map<String,Object>> list = new
		 * ArrayList<Map<String,Object>>(); list.add(paramMap); String json =
		 * jsonSerialization(list); List<Map<String,Object>> list2 =
		 * jsonDeserialization(json, ArrayList.class, Demo.class);
		 * System.out.println(list2.size());
		 */
	}
}
