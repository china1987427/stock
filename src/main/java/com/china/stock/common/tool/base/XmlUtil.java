package com.china.stock.common.tool.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * <p>xml文件操作的常用方�?/p>
 * 
 * @author zwh
 * @version 1.0.0
 */
public class XmlUtil {

	/**
	 * 获得src下的文件名为fileName的文�?
	 */
	public static String getFilePathByName(String fileName) {
		// return fileName;
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource(fileName);
		//System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
		String urlString=url.toString().substring(6);
		String name = System.getProperties().getProperty("os.name");
		if("linux".equalsIgnoreCase(name)){
			urlString=File.separator+urlString;
		}
		return urlString;

	}

	/**
	 * 判断XML文件是否存在.
	 * 
	 * @param fileName
	 * @return
	 */
	private static boolean fileExist(String fileName) {
		File objFile = new File(fileName);
		if (objFile.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据路径获取Document
	 * 
	 * @author x.z.f
	 * @param path
	 * @return Document
	 * @throws Exception
	 */
	public static Document getDocumentByPath(String path) throws Exception {
		Document doc = null;
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			return null;
		}

		if (fileExist(path)) {
			File fileUrl = new File(path);
			SAXReader reader = new SAXReader();
			try {
				doc = reader.read(fileUrl);
			} catch (DocumentException e) {
				doc = null;
			}
		} else {
			throw new Exception("没找到文�?"+path);
		}
		return doc;
	}

	/**
	 * 获得任务类型list或�?服务器地�?ist 数据List<Map<String,String>>
	 * 
	 * @author x.z.f
	 * @param filename
	 *            文件名字
	 * @param xPath
	 *            任务类型的xPath:task/taskType/type
	 *            服务器地�?Path:task/taskService/service
	 * @param defaultKey
	 *            默认key
	 * @param defaultValue
	 *            默认value
	 * @throws Exception
	 * @return List<Map<String,String>> 任务类型Map数据
	 */
	@SuppressWarnings("rawtypes")
	public static List<Map<String, String>> getTaskTypeMap(String filename,
			String xPath, String defaultKey, String defaultValue)
			throws Exception {
		String path = getFilePathByName(filename);
		
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		if (!StrUtils.isEmpty(defaultValue)) {
			map = new HashMap<String, String>();
			map.put("key", defaultKey);
			map.put("value", defaultValue);
			listMap.add(map);
		}

		/*System.out.println(path);
		String url=path.replaceAll("\\\\",File.separator);
		System.out.println(File.separator+"------"+url);*/

		Document doc = getDocumentByPath(path);
		List typeList = doc.selectNodes(xPath);

		for (int i = 0; i < typeList.size(); i++) {
			map = new HashMap<String, String>();
			Element element = (Element) typeList.get(i);
			map.put("key", element.attributeValue("id"));
			map.put("value", element.attributeValue("name"));
			listMap.add(map);
		}

		return listMap;
	}
	/**
	 * 获得任务类型list或�?服务器地�?ist 数据List<Map<String,String>>
	 * 
	 * @author 杨昊�?
	 * @param filename
	 *            文件名字
	 * @param xPath
	 *            任务类型的xPath:task/taskType/type
	 *            服务器地�?Path:task/taskService/service
	 * @param defaultKey
	 *            默认key
	 * @param defaultValue
	 *            默认value
	 * @throws Exception
	 * @return List<Map<String,String>> 任务类型Map数据
	 */
	@SuppressWarnings("rawtypes")
	public static List<Map<String, String>> getKeyValueMap(String filename,
			String xPath, String defaultKey, String defaultValue)
			throws Exception {
		String path = getFilePathByName(filename);
		
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		if (StrUtils.isEmpty(defaultValue)) {
			defaultKey="key";
		}
		if (StrUtils.isEmpty(defaultValue)) {
			defaultValue="value";
		}
		Document doc = getDocumentByPath(path);
		List typeList = doc.selectNodes(xPath);

		for (int i = 0; i < typeList.size(); i++) {
			map = new HashMap<String, String>();
			Element element = (Element) typeList.get(i);
			map.put("key", element.attributeValue(defaultKey));
			map.put("value", element.attributeValue(defaultValue));
			listMap.add(map);
		}

		return listMap;
	}

	/**
	 * 根据task/taskService/service (或�?：task/taskType/type)节点的id属�?获取对应的服务器�?
	 * 
	 * @param filename
	 *            : task.xml
	 * @param xpth
	 *            :task/taskService/service[@id=
	 *            '192.1.1.8'](或�?：task/taskType/type[@id='1'])
	 * @param attrName
	 *            "serviceName"(或�?：className)
	 * @return 服务器名
	 */
	public static String getNodeAttrbute(String filename, String xpth,
			String attrName) {

		String path = getFilePathByName(filename);
		Document doc = null;
		Element element = null;
		try {
			doc = getDocumentByPath(path);
			element = (Element) doc.selectSingleNode(xpth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return element.attributeValue(attrName);
	}
	
	public static String getTextNodeValue(String filename, String xpth) {

		String path = getFilePathByName(filename);
		Document doc = null;
		Element element = null;
		try {
			doc = getDocumentByPath(path);
			element = (Element) doc.selectSingleNode(xpth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return element.getText();
	}
	
	
	public static void updateAttributeText(String filename,String xPath,String attributeName,String attributeValue){	
		
		String path = getFilePathByName(filename);		
		Document doc = null;
		try {
			doc = getDocumentByPath(path);
			
			if(doc != null){				
				
				Node node = doc.selectSingleNode(xPath);
				Element brandElement=(Element) node;
		        brandElement.addAttribute(attributeName, attributeValue);	
		        
		         XMLWriter xmlWriter = new XMLWriter( new FileOutputStream(path));
				 xmlWriter.write(doc); 
				 xmlWriter.close();
				 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
				
	}
	/**
	 * 将xml格式的字符串转换为xml对象，并根据xPath获得相应的文本�?
	 * @param xml 待转换的xml字符�?
	 * @param xPath xPath
	 * @return 返回�?��Map对象其中如果节点存在则key="result"与xPath对应的文本�?，否则返回接口调用异常信息�?
	 */
	
	public static Map<String,String> getTextNode(String xml,String xPath){
		Map<String,String> resultMap = new HashMap<String,String>();
		try {
			Document doc = DocumentHelper.parseText(xml);
			Node node = doc.selectSingleNode(xPath);
			if(node!=null){
				resultMap.put("result", node.getText());
			}else{
				resultMap.put("result", "调用接口异常");
			}
			
		} catch (DocumentException e) {
			resultMap.put("result", "调用接口异常");
		}
		
		return resultMap;
	} 

	public static void main(String args[]) throws Exception {
		// List list = root.selectNodes("book[@url='dom4j.com']");
		/*URL url = Thread.currentThread().getContextClassLoader()
				.getResource("urlConfiguration.xml");*/
		//String fullFileName = url.toString();
		// getTaskTypeMap("D:/java_soft_setup/apache-tomcat-6.0.35/webapps/ANAP/WEB-INF/classes/task.xml","task/taskType/type");
		//System.err.println(fullFileName);
		//String info = getNodeAttrbute("config/urlConfiguration.xml", "Urls/Url[@key='registUser']", "value");
		//String xml = "<?xml version=\"1.0\"?><error>UserAlreadyExistsException</error>";
		//Map<String ,String> map = getTextNode(xml,"result|error");
//String queryString = XmlUtil.getNodeAttrbute("config/urlConfiguration.xml", "Urls/Url[@key='updateUser']", "params");
		
		//queryString = MessageFormat.format(queryString, "1",null,"2","");
		System.out.println(getTaskTypeMap("config/areaConf.xml","/Root/Provience[@id='"+2+"']/City","",""));
	}
}
