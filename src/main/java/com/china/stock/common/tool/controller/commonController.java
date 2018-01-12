package com.china.stock.common.tool.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.china.stock.common.tool.base.TFSUtil;
import com.china.stock.common.tool.conf.Conf;



/**
 * 共用控制器
 * 
 * @ClassName: ComController
 * @Description: 测试
 * @author tianzy
 * @date 2015年4月24日 上午10:33:50
 */
@Controller
@RequestMapping(value = "/common")
public class commonController extends BaseController {

	/**
	 * @upfile 就是上面提到的upfile，要对应一致
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadImg")
	public String uploadImg(MultipartHttpServletRequest request) throws Exception {
		String strJson = "'{'\"name\":\"{0}\",\"originalName\":\"{1}\", \"size\":\"{2}\", \"state\":\"{3}\", \"type\":\"{4}\", \"url\":\"{5}\"'}'";

		MultipartFile myfile = request.getFile("upfile");
		// 上传文件的原名(即上传前的文件名字)
		String originalFilename = null;
		FileOutputStream out = null;
		// 如果只是上传一个文件,则只需要MultipartFile类型接收文件即可,而且无需显式指定@RequestParam注解
		// 如果想上传多个文件,那么这里就要用MultipartFile[]类型来接收文件,并且要指定@RequestParam注解
		// 上传多个文件时,前台表单中的所有<input
		// type="file"/>的name都应该是myfiles,否则参数里的myfiles无法获取到所有上传的文件
		if (myfile.isEmpty()) {
			strJson = MessageFormat.format(strJson, "", "", "", "FALSE","","");
		} else {
			try {
				 
				originalFilename = myfile.getOriginalFilename();
				out = new FileOutputStream(myfile.getName()); 
				String extentString = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
				
				//服务器端扩展名判断start,需测试
				HashMap<String, String> extMap = new HashMap<String, String>();
				extMap.put("image", "gif,jpg,jpeg,png");
				
				//检查扩展名
			 	if(!Arrays.<String>asList(extMap.get("image").split(",")).contains(extentString)){
			 		strJson = MessageFormat.format(strJson, "1", "上传文件扩展名是不支持的扩展名。\n只支持" + extMap.get("image") + "格式。", "", "");
					WriterJson(strJson);
					return null;
				} 
			 	//服务器端扩展名判断end
				
				String tfsname = TFSUtil.getTfsManager().saveFile(myfile.getBytes(), null, extentString);

				if (org.springframework.util.StringUtils.isEmpty(tfsname)) {
					strJson = MessageFormat.format(strJson, "", "", "","FALSE","","");
				} else {
					strJson = MessageFormat.format(strJson, tfsname+"."+extentString, myfile.getName(),myfile.getBytes(),"SUCCESS","."+extentString, tfsname+"."+extentString);

					out.write(myfile.getBytes());
					out.close();
				}
			} catch (Exception e) {
				System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
				e.printStackTrace();

				strJson = MessageFormat.format(strJson, "1", "文件上传失败。", "", "", "","FALSE","","");
			} finally {
				out.close();
			}			
		}
		return strJson;
	}

	/**
	 * 文件上传到本地文件
	 * 
	 * @param uname
	 *            文件名
	 * @param myfiles
	 *            文件信息
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxUploadLocalNoName")
	public String ajaxUploadLocalNoName(MultipartHttpServletRequest request) throws Exception {
		String strJson = "'{'\"resultCode\":\"{0}\",\"resultInfo\":\"{1}\",\"fileName\":\"{2}\", \"path\":\"{3}\"'}'";

		String valueString = request.getParameter("id");
		MultipartFile myfile = request.getFile(valueString);

		// 上传文件的原名(即上传前的文件名字)
		String originalFilename = null;
		// 如果只是上传一个文件,则只需要MultipartFile类型接收文件即可,而且无需显式指定@RequestParam注解
		// 如果想上传多个文件,那么这里就要用MultipartFile[]类型来接收文件,并且要指定@RequestParam注解
		// 上传多个文件时,前台表单中的所有<input
		// type="file"/>的name都应该是myfiles,否则参数里的myfiles无法获取到所有上传的文件
		if (myfile.isEmpty()) {
			strJson = MessageFormat.format(strJson, "1", "请选择文件后上传。", "", "");
			WriterJson(strJson);
			return null;
		} else {
			try {
				originalFilename = myfile.getOriginalFilename();
				String extentString = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
				String tfsname = TFSUtil.getTfsManager().saveFile(myfile.getBytes(), null, extentString);

				if (org.springframework.util.StringUtils.isEmpty(tfsname)) {
					strJson = MessageFormat.format(strJson, "1", "文件上传失败,tfs返回空。", "", "");
					WriterJson(strJson);
				} else {
					strJson = MessageFormat.format(strJson, "0", "文件上传成功。", tfsname + "." + extentString, Conf.getValue("tfsurl"));
					WriterJson(strJson);
				}
				return null;
			} catch (Exception e) {
				System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
				e.printStackTrace();

				strJson = MessageFormat.format(strJson, "1", "文件上传失败。", "", "");
				WriterJson(strJson);

				return null;
			} finally {
				// tfsManager.destroy();
			}
		}
	}

	public String fileName(String source) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssms");
		String date = format.format(new Date());
		int ran = (int) ((Math.random() * 9 + 1) * 10000);
		return source + "_" + date + ran;
	}

	/**
	 * 文件上传到本地文件
	 * 
	 * @param uname
	 *            文件名
	 * @param myfiles
	 *            文件信息
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxUploadLocal")
	public String ajaxUploadLocal(@RequestParam("uname") String uname, @RequestParam MultipartFile[] myfiles) throws Exception {
		String strJson = "'{'\"resultCode\":\"{0}\",\"resultInfo\":\"{1}\", \"path\":\"{2}\"'}'";

		// 可以在上传文件的同时接收其它参数
		System.out.println("收到用户[" + uname + "]的文件上传请求");
		// 这里实现文件上传操作用的是commons.io.FileUtils类,它会自动判断/upload是否存在,不存在会自动创建
		String realPath = getRequest().getSession().getServletContext().getRealPath("/uploadxw");

		// 上传文件的原名(即上传前的文件名字)
		String originalFilename = null;
		// 如果只是上传一个文件,则只需要MultipartFile类型接收文件即可,而且无需显式指定@RequestParam注解
		// 如果想上传多个文件,那么这里就要用MultipartFile[]类型来接收文件,并且要指定@RequestParam注解
		// 上传多个文件时,前台表单中的所有<input
		// type="file"/>的name都应该是myfiles,否则参数里的myfiles无法获取到所有上传的文件
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				strJson = MessageFormat.format(strJson, "1", "请选择文件后上传。", "");
				WriterJson(strJson);
				return null;
			} else {
				originalFilename = myfile.getOriginalFilename();

				try {
					FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, originalFilename));
				} catch (IOException e) {
					System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
					e.printStackTrace();

					strJson = MessageFormat.format(strJson, "1", "文件上传失败，请重试。", "");
					WriterJson(strJson);

					return null;
				}
			}
		}

		strJson = MessageFormat.format(strJson, "0", "文件上传成功。", getRequest().getContextPath() + "/upload/" + originalFilename);
		WriterJson(strJson);

		return null;
	}

	/**
	 * 文件上传到tfs
	 * 
	 * @param uname
	 *            文件名
	 * @param myfiles
	 *            文件信息
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxUploadTfs")
	public String ajaxUploadTfs(@RequestParam("uname") String uname, @RequestParam MultipartFile[] myfiles) throws Exception {
		String strJson = "'{'\"resultCode\":\"{0}\",\"resultInfo\":\"{1}\", \"path\":\"{2}\"'}'";
		
		// 上传文件的原名(即上传前的文件名字)
		String originalFilename = null;

		if (myfiles.length <= 0) {
			strJson = MessageFormat.format(strJson, "1", "请选择文件后上传。", "");
			WriterJson(strJson);
		}
		
		MultipartFile myfile = myfiles[0];
		if (myfile.isEmpty()) {
			strJson = MessageFormat.format(strJson, "1", "请选择文件后上传。", "");
			WriterJson(strJson);
			return null;
		} else {
			try {
				originalFilename = myfile.getOriginalFilename();
				String extentString = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
				
				//服务器端扩展名判断start,需测试
				HashMap<String, String> extMap = new HashMap<String, String>();
				extMap.put("image", "gif,jpg,jpeg,png");
				
				//检查扩展名
			 	if(!Arrays.<String>asList(extMap.get("image").split(",")).contains(extentString)){
			 		strJson = MessageFormat.format(strJson, "1", "上传文件扩展名是不支持的扩展名。\n只支持" + extMap.get("image") + "格式。", "", "");
					WriterJson(strJson);
					return null;
				} 
			 	//服务器端扩展名判断end
				
				String tfsname = TFSUtil.getTfsManager().saveFile(myfile.getBytes(), null, extentString);

				if (org.springframework.util.StringUtils.isEmpty(tfsname)) {
					strJson = MessageFormat.format(strJson, "1", "文件上传失败。", "");
					WriterJson(strJson);
				} else {
					strJson = MessageFormat.format(strJson, "0", "文件上传成功。", tfsname + "." + extentString);
					WriterJson(strJson);
				}
				return null;
			} catch (Exception e) {
				System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
				e.printStackTrace();

				strJson = MessageFormat.format(strJson, "1", "文件上传失败。", "");
				WriterJson(strJson);

				return null;
			} finally {
				// tfsManager.destroy();
			}
		}
	}

	public static void main(String[] args) throws Exception {

	}
}
