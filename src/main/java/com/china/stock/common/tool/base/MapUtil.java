package com.china.stock.common.tool.base;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.china.stock.common.tool.conf.Conf;



public class MapUtil {
	protected static Logger log = Logger.getLogger(MapUtil.class);
	
	/*
	 * 默认地球半径
	 */
	private static double EARTH_RADIUS = 6371;

	/**
	 * 计算经纬度点对应正方形4个点的坐标
	 * 
	 * @param longitude 经度
	 * @param latitude  纬度
	 * @param distance 该点所在圆的半径，该圆与此正方形内切,单位千米
	 * @return
	 */
	public static Map<String, double[]> returnLLSquarePoint(double longitude, double latitude, double distance) {
		Map<String, double[]> squareMap = new HashMap<String, double[]>();
		// 计算经度弧度,从弧度转换为角度
		double dLongitude = 2 * (Math.asin(Math.sin(distance / (2 * EARTH_RADIUS)) / Math.cos(Math.toRadians(latitude))));
		dLongitude = Math.toDegrees(dLongitude);
		// 计算纬度角度
		double dLatitude = distance / EARTH_RADIUS;
		dLatitude = Math.toDegrees(dLatitude);
		// 正方形
		double[] leftTopPoint = { latitude + dLatitude, longitude - dLongitude };
		double[] rightTopPoint = { latitude + dLatitude, longitude + dLongitude };
		double[] leftBottomPoint = { latitude - dLatitude, longitude - dLongitude };
		double[] rightBottomPoint = { latitude - dLatitude, longitude + dLongitude };
		squareMap.put("leftTopPoint", leftTopPoint);
		squareMap.put("rightTopPoint", rightTopPoint);
		squareMap.put("leftBottomPoint", leftBottomPoint);
		squareMap.put("rightBottomPoint", rightBottomPoint);
		return squareMap;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下
	 * 
	 * @param lon1
	 *            第一点的经度
	 * @param lat1
	 *            第一点的纬度
	 * @param lon2
	 *            第二点的经度
	 * @param lat3
	 *            第二点的纬度
	 * @return 返回的距离，单位m
	 * */
	public static double GetDistance(double lon1, double lat1, double lon2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lon1) - rad(lon2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000 * 1000) / 10000;
		return s;
	}

	/**
	 * 获取指定IP对应的经纬度（为空返回当前机器经纬度）
	 * 
	 * @param ip
	 * @return
	 * @throws IOException 
	 */
	public static String[] getIPXY(String ip) throws IOException {

		String ak = Conf.getValue("mapkey");
		String[] ips = new String[]{"",""};
		if (ip.isEmpty()) {
			ip = "";//InetAddress.getLocalHost().getHostAddress();
		}

		try {
			JSONObject json=JSON.parseObject(Jsoup.connect("http://api.map.baidu.com/location/ip?ak=" + ak + "&ip=" + ip + "&coor=bd09ll")			
					.ignoreContentType(true)
					.timeout(4000)
					.execute().body());
			if(json.get("status").toString().equals("0")){
				JSONObject content = JSON.parseObject(json.get("content").toString());
				ips[0]= JSON.parseObject(content.get("point").toString()).get("x").toString();
				ips[1]= JSON.parseObject(content.get("point").toString()).get("y").toString();
			}

			if(ips[0].isEmpty()){
				ips[0]=Conf.getValue("defaultLng");
				ips[1]=Conf.getValue("defaultLat");
			}			

		}  catch (IOException e) {
			log.error("mapUtil-getIPXY异常:"+e.getMessage());
			ips[0]=Conf.getValue("defaultLng");
			ips[1]=Conf.getValue("defaultLat");
		}

		return ips;
	}

	public static void main(String[] args) throws Exception {
		String[] ss = getIPXY("");
		System.out.println(ss[0]);
		System.out.println(ss[1]);
		//returnLLSquarePoint(104.14847, 30.634901, 5);
		// System.out.println(GetDistance(104.14847,30.634901,104.013437,30.706141));
	}
}
