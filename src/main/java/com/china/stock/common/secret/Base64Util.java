package com.china.stock.common.secret;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/** * BASE64���ܽ��� */
public class Base64Util {
	/** * BASE64���� * @param key * @return * @throws Exception */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/** * BASE64���� * @param key * @return * @throws Exception */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	public static void main(String[] args) throws Exception {
		String data = Base64Util.encryptBASE64("7fanke77".getBytes());
		System.out.println("����ǰ��" + data);
		byte[] byteArray = Base64Util.decryptBASE64(data);
		System.out.println("���ܺ�" + new String(byteArray));
	}
}
