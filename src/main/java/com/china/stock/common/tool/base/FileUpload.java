package com.china.stock.common.tool.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class FileUpload {
	static Logger logger = Logger.getLogger(FileUpload.class);

	/**
	 * 异步文件上传
	 * 
	 * @param myFile
	 *            待上传的文件对象
	 * @param myFileFileName
	 *            待上传的文件�?
	 * @param folderName
	 *            文件上传的文件夹
	 * @return json信息{"resultCode:{0},"resultInfo":{1}} resultCode:0失败�?成功
	 */
	@SuppressWarnings("finally")
	public static JSONObject AjaxFileUpload(HttpServletRequest request,File myFile, String myFileName,
			String folderName, long timeMillis) {
		String strJson = "'{'\"resultCode\":\"{0}\",\"resultInfo\":\"{1}\", \"path\":\"{2}\"'}'";
		try {
			InputStream is = new FileInputStream(myFile);
			String uploadPath = request.getSession().getServletContext()
					.getRealPath("/" + folderName);

			File directory = new File(uploadPath);
			directory.mkdirs();
			String ext = myFileName.substring(myFileName
					.lastIndexOf("."));
			File toFile = new File(uploadPath, timeMillis + ext);
			OutputStream os = new FileOutputStream(toFile);
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			// System.out.println("上传文件新路径："+uploadPath+"/"+timeMillis+myFileFileName);
			strJson = MessageFormat.format(strJson, "1", "上传文件成功",
					folderName + "/" + timeMillis + ext);
			is.close();
			os.flush();
			os.close();

		} catch (Exception e) {
			logger.error(e.getMessage());
			strJson = MessageFormat.format(strJson, "0", "上传文件失败");
		} finally {
			return JSONObject.parseObject(strJson);
		}
	}
}
