package com.china.stock.common.tool.base;

import java.io.File;


public class FileUtil {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		//�?��目录
		File uploadDir = new File("d:\\test");
		if(!uploadDir.isDirectory()){
			System.out.println("上传目录不存在�?");
			return;
		}
		//�?��目录写权�?
		if(!uploadDir.canWrite()){
			System.out.println("上传目录没有写权限�?");
			return;
		}

		File saveDirFile = new File("d:\\test\\xw");
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}

	}
	
	
	/**
	 * @param args
	 */
	/*public static void main(String[] args) throws Exception
	{
		Map<String, String> params = new HashMap<String,String>();
		params.put("appkey", "65734592");
		params.put("code", "0dee197dfdf84d8a97daca3d527223ab");
		params.put("sign", "F4CA3FBE76A450C626BCD360525293E9");
		//计算sign
		String sign=SignUtil.getSign(MapMaker.makeStrStr("code","0dee197dfdf84d8a97daca3d527223ab"),"65734592",Conf.getValue("secret"));

	    System.out.println(sign);
		Response rsp=Jsoup.connect("http://192.168.20.51:8085/singleLogin/authtoken")
				.data(params)
				.timeout(9000)
				.execute();
		System.out.println("out:"+rsp.body());

	}*/
	
}
