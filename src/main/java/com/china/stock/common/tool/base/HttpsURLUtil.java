package com.china.stock.common.tool.base;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.springframework.http.converter.json.GsonBuilderUtils;

import com.google.gson.JsonObject;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

@SuppressWarnings("all")
public class HttpsURLUtil {
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			String protocol = url.getProtocol();
			URLConnection con = url.openConnection();
			if("http".equals(protocol)){
//				HttpURLConnection con1 = (HttpURLConnection) url.openConnection();
//				// 设置请求方式（GET/POST）
				((HttpURLConnection) con).setRequestMethod(requestMethod);
			}else {
				
				((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
					
				});
				// 设置请求方式（GET/POST）
				((HttpURLConnection) con).setRequestMethod(requestMethod);
			}
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);

			if ("GET".equalsIgnoreCase(requestMethod))
				con.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = con.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = con.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			if("http".equals(protocol)){
				((HttpURLConnection) con).disconnect();
			}else {
				
				((HttpsURLConnection) con).disconnect();
			}
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			LogerUtil.log.error("server connection timed out.");
		} catch (Exception e) {
			LogerUtil.log.error("https request error:{}", e);
		}
		return jsonObject;
	}

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @return 返回一个流
	 */
	public static InputStream httpRequest(String requestUrl,
			String requestMethod) {
		try {
			URL url = new URL(requestUrl);
			String protocol = url.getProtocol();
			URLConnection con = url.openConnection();
			if("http".equals(protocol)){
//				HttpURLConnection con1 = (HttpURLConnection) url.openConnection();
//				// 设置请求方式（GET/POST）
				((HttpURLConnection) con).setRequestMethod(requestMethod);
			}else {
				
				((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
					
				});
				// 设置请求方式（GET/POST）
				((HttpURLConnection) con).setRequestMethod(requestMethod);
			}
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			// 设置请求方式（GET/POST）
			con.connect();
			return con.getInputStream();
		} catch (ConnectException ce) {
			LogerUtil.log.error("Weixin server connection timed out."+ce.getMessage());
			return null;
		} catch (Exception e) {
			LogerUtil.log.error("https request error:{}", e);
			return null;
		}
	}

	/**
	 * 模拟from表单提交文件
	 * 
	 * @param requestUrl
	 * @param requestMethod
	 * @param file
	 * @return String
	 */
	public static JSONObject formUpload(String urlStr,
			Map<String, String> textMap, File file) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------GENIUSABCD123456"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			// 设置请求头信息
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			String filename = file.getName();
			String contentType = new MimetypesFileTypeMap()
					.getContentType(file);
			if (filename.endsWith(".jpg")) {
				contentType = "image/jpg";
			}
			if (contentType == null || contentType.equals("")) {
				contentType = "application/octet-stream";
			}

			StringBuffer strBuf = new StringBuffer();
			strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
			strBuf.append("Content-Disposition: form-data; name=\""
					+ "uploadfile" + "\"; filename=\"" + filename + "\"\r\n");
			strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

			out.write(strBuf.toString().getBytes());

			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf1 = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf1.append(line).append("\n");
			}
			res = strBuf1.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			LogerUtil.writeError("模拟from表单上传文件" + e.getMessage());
			throw new RuntimeException("模拟from表单上传文件" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return JSONObject.fromObject(res);
	}

	private static String readBack(HttpURLConnection con) throws IOException {
		String result = null;
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			LogerUtil.writeError("发送POST请求出现异常！" + e);
			e.printStackTrace();
			throw new IOException("数据读取异常");
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return result;
	}
}
