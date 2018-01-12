package com.china.stock.common.tool.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * 编码/解码算法工具�?
 */
public class CodecUtil {
	/**
	 * 使用MD5算法计算Hash
	 * @param source 源字符串
	 * @param charset 源字符串字符�?
	 * @return MD5 Hash
	 */
	public static
	String md5Hash(String source, String charset) {
		if (StringUtils.isEmpty(source) || StringUtils.isBlank(charset)) {
			return "";
		}
		try {
			return ByteFormat.toHex(md5Hash(source.getBytes(charset)));
		}
		catch (UnsupportedEncodingException ex) {
			log.warn("md5Hash([" + source + "], [" + charset + "])", ex);
			return "";
		}
	}
	
	/**
	 * 使用MD5算法计算Hash
	 * @param source 源数�?
	 * @return MD5 Hash
	 */
	public static
	byte[] md5Hash(byte[] source) {
		return digest(source, ALGORITHM_MD5);
	}

	/**
	 * 使用MD5算法计算文件Hash
	 * @param sourceFile 要计算的文件
	 * @return MD5 Hash
	 */
	public static
	String md5Hash(File sourceFile) {
		return ByteFormat.toHex(fileDigest(sourceFile, ALGORITHM_MD5));
	}
	
	/**
	 * 使用SHA1算法计算Hash
	 * @param source 源字符串
	 * @param charset 源字符串字符�?
	 * @return SHA1 Hash
	 */
	public static
	String sha1Hash(String source, String charset) {
		if (StringUtils.isEmpty(source) || StringUtils.isBlank(charset)) {
			return "";
		}
		try {
			return ByteFormat.toHex(sha1Hash(source.getBytes(charset)));
		}
		catch (UnsupportedEncodingException ex) {
			log.warn("sha1Hash([" + source + "], [" + charset + "])", ex);
			return "";
		}
	}

	/**
	 * 使用SHA1算法计算Hash
	 * @param source 源数�?
	 * @return SHA1 Hash
	 */
	public static
	byte[] sha1Hash(byte[] source) {
		return digest(source, ALGORITHM_SHA1);
	}

	/**
	 * 使用HMAC-SHA1算法计算签名
	 * @param source 源字符串
	 * @param key 使用的key字符�?
	 * @param charset 源字符串和key字符串的字符�?
	 * @return HMAC-SHA1 签名
	 */
	public static
	String hmacsha1(String source, String key, String charset) {
		if (StringUtils.isEmpty(source) || StringUtils.isEmpty(key) || StringUtils.isBlank(charset)) {
			return "";
		}
		try {
			return ByteFormat.toHex(
					hmacsha1(source.getBytes(charset), key.getBytes(charset)));
		}
		catch (UnsupportedEncodingException ex) {
			log.warn("hmacsha1Hash([" + source + "], [" + charset + "])", ex);
			return "";
		}
	}
	
    public static String hmacsha1(String data, String key) {
    	System.out.println("hmacsha1 data[" + data + "]");
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance(ALGORITHM_HMACSHA1);
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), ALGORITHM_HMACSHA1);
            mac.init(spec);
            byteHMAC = mac.doFinal(data.getBytes());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException ignore) {
            // should never happen
        }
        
        return ByteFormat.toHex(byteHMAC);
    }
	
	/**
	 * 使用HMAC-SHA1算法计算签名
	 * @param source 源数�?
	 * @param key 使用的key
	 * @return HMAC-SHA1 签名
	 */
	public static
	byte[] hmacsha1(byte[] source, byte[] key) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key, ALGORITHM_HMACSHA1);
			Mac mac = Mac.getInstance(ALGORITHM_HMACSHA1);
			mac.init(signingKey);
			return mac.doFinal(source);
		}
		catch (NoSuchAlgorithmException ex) {
			log.warn("digest() - No such algorithm[" + ALGORITHM_HMACSHA1 + "]!", ex);
			return new byte[0];
		}
		catch (InvalidKeyException ex) {
			log.warn("digest() - InvalidKey!", ex);
			return new byte[0];
		}

	}
	
	
	/**
	 * 使用SHA1算法计算文件Hash
	 * @param sourceFile 要计算的文件
	 * @return SHA1 Hash
	 */
	public static
	String sha1Hash(File sourceFile) {
		return ByteFormat.toHex(fileDigest(sourceFile, ALGORITHM_SHA1));
	}
	
	/**
	 * BASE64 encode
	 * @param source 源字符串
	 * @param charset 源字符串字符�?
	 * @return Base64编码结果
	 */
	public static
	String base64Encode(String source, String charset) {
		if (StringUtils.isEmpty(source) || StringUtils.isBlank(charset)) {
			return "";
		}
		try {
			return base64Encode(source.getBytes(charset));
		}
		catch (UnsupportedEncodingException ex) {
			log.warn("base64Encode([" + source + "], [" + charset + "])", ex);
			return "";
		}
	}

	/**
	 * BASE64 encode
	 * @param data 要编码的数据
	 * @return Base64编码结果
	 */
	public static
	String base64Encode(byte[] data) {
		if (data == null) {
			return "";
		}
		
		return CodecUtil.base64Encode(data) ;
	}

	/**
	 * url encode
	 * @param s 要编码的字符�?
	 * @param charset 字符�?
	 * @return 编码后的字符�?
	 */
	public static
	String urlEncode(String s, String charset) {
		if (StringUtils.isEmpty(s) || StringUtils.isBlank(charset)) {
			return "";
		}
		
		try {
			return URLEncoder.encode(s, charset);
		}
		catch (UnsupportedEncodingException ex) {
			log.warn("urlEncode([" + s + "], [" + charset + "])", ex);
		}
		return "";
	}
	
	/**
	 * url decode
	 * @param s 要解码的字符�?
	 * @param charset 字符�?
	 * @return 解码后的字符�?
	 */
	public static
	String urlDecode(String s, String charset) {
		if (StringUtils.isEmpty(s) || StringUtils.isBlank(charset)) {
			return "";
		}
		
		try {
			return URLDecoder.decode(s, charset);
		}
		catch (UnsupportedEncodingException ex) {
			log.warn("urlDecode([" + s + "], [" + charset + "])", ex);
		}
		return "";
	}

	/**
	 * 使用指定算法计算摘要
	 * @param source 源数�?
	 * @param algorithm 摘要算法
	 * @return 摘要结果
	 */
	private static
	byte[] digest(byte[] source, String algorithm) {
		if (source == null) {
			return new byte[0];
		}
		
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(source);
			return md.digest();
		}
		catch (NoSuchAlgorithmException ex) {
			log.warn("digest() - No Such Algorithm[" + algorithm + "]", ex);
			return new byte[0];
		}
	}

	/**
	 * 使用指定算法计算摘要
	 * @param source 源数�?
	 * @param algorithm 摘要算法
	 * @return 摘要结果
	 */
	private static
	byte[] fileDigest(File file, String algorithm) {
		log.debug("fileDigest() - file[" + file + "], algorithm[" + algorithm + "]");

		if (file == null || file.length() == 0) {
			log.warn("fileDigest() - file[" + file + "] is null or zero length, return byte[0]!");
			return new byte[0];
		}

		long maxBufferSize = 1024 * 1024 * 256;
		try {
			FileInputStream in = new FileInputStream(file);
			FileChannel ch = in.getChannel();
			long fileLen = file.length();
			int digestUpdateCount = fileLen % maxBufferSize == 0 ? 
					(int)(fileLen / maxBufferSize) :
					(int)(fileLen / maxBufferSize) + 1;

			MessageDigest md = MessageDigest.getInstance(algorithm);
			
			log.debug("fileDigest() - file.length=" + fileLen +
					  ", digestUpdateCount=" + digestUpdateCount);

			long position = 0;
			for (int i = 0; i < digestUpdateCount; i++) {
				long size = file.length() - position;
				if (size > maxBufferSize) {
					size = maxBufferSize;
				}
				log.debug("fileDigest() - map buffer[" + i + "] position=" +
						  position + ", size=" + size);
				md.update(ch.map(FileChannel.MapMode.READ_ONLY, position, size));
				position += size;
			}

			byte[] digest = md.digest();

			if (log.isDebugEnabled()) {
				log.debug("fileDigest() - file[" + file + "], algorithm[" + algorithm +
						  "], digest[" + ByteFormat.toHex(digest) + "]");
			}
			in.close();
			return digest;
		}
		catch (NoSuchAlgorithmException ex) {
			log.warn("digest() - No such algorithm[" + algorithm + "]!", ex);
			return new byte[0];
		}
		catch (FileNotFoundException ex) {
			log.warn("digest() - File[" + file.getAbsolutePath() + "] not found!", ex);
			return new byte[0];
		}
		catch (IOException ex) {
			log.warn("digest() - File[" + file.getAbsolutePath() + "] read error!", ex);
			return new byte[0];
		}
	}
	
	/**
	 * 
	* @Description: seq生成
	* @return yyyyMMddhhmmss+6位随机数
	 */
	public static String codeGenerate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddhhmmss");
		String date=format.format(new Date());
		int ran=(int)((Math.random()*9+1)*100000);
		
		return date+ran;
	}
	
	/**
	 * 调试对象
	 */
	private static Logger log = Logger.getLogger(CodecUtil.class);

	/**
	 * MD5算法名称
	 */
	private final static String ALGORITHM_MD5 = "MD5";

	/**
	 * SHA1算法名称
	 */
	private final static String ALGORITHM_SHA1 = "SHA1";

	/**
	 * HAMC-SHA1算法名称
	 */
	private final static String ALGORITHM_HMACSHA1 = "HmacSHA1";
	
	public static void main(String[] args) throws Exception {
		int ran=(int)((Math.random()*9+1)*100000000);
		System.out.println(ran);
	}
}
