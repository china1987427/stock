package com.china.stock.common.secret;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * SecretUtils {3DES���ܽ��ܵĹ����� }
 * @author William
 * @date 2013-04-19
 */
public class DESedeUtil {
 
    //��������㷨����DES��DESede(��3DES)��Blowfish
    private static final String Algorithm = "DESede";    
    private static final String PASSWORD_CRYPT_KEY = "2012PinganVitality075522628888ForShenZhenBelter075561869839";
    
    
    /**
     * ���ܷ���
     * @param src Դ��ݵ��ֽ�����
     * @return 
     */
    public static byte[] encryptMode(byte[] src) {
        try {
             SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);    //�����Կ
             Cipher c1 = Cipher.getInstance(Algorithm);    //ʵ�������/���ܵ�Cipher������
             c1.init(Cipher.ENCRYPT_MODE, deskey);    //��ʼ��Ϊ����ģʽ
             return c1.doFinal(src);
         } catch (java.security.NoSuchAlgorithmException e1) {
             e1.printStackTrace();
         } catch (javax.crypto.NoSuchPaddingException e2) {
             e2.printStackTrace();
         } catch (java.lang.Exception e3) {
             e3.printStackTrace();
         }
         return null;
     }
    
    
    /**
     * ���ܺ���
     * @param src ���ĵ��ֽ�����
     * @return
     */
    public static byte[] decryptMode(byte[] src) {      
        try {
            SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);    //��ʼ��Ϊ����ģʽ
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
     }
    
    
    /*
     * ����ַ������Կ�ֽ����� 
     * @param keyStr ��Կ�ַ�
     * @return 
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException{
        byte[] key = new byte[24];    //����һ��24λ���ֽ����飬Ĭ�����涼��0
        byte[] temp = keyStr.getBytes("UTF-8");    //���ַ�ת���ֽ�����
        
        /*
         * ִ�����鿽��
         * System.arraycopy(Դ���飬��Դ�������￪ʼ������Ŀ�����飬��������λ)
         */
        if(key.length > temp.length){
            //���temp����24λ���򿽱�temp����������ȵ����ݵ�key������
            System.arraycopy(temp, 0, key, 0, temp.length);
        }else{
            //���temp����24λ���򿽱�temp����24�����ȵ����ݵ�key������
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    } 
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub

		//String msg = "3DES���ܽ��ܰ���";
		String msg = "3306_20140812";
        System.out.println("����ǰ��" + msg);
        
        
        String data = Base64Util.encryptBASE64(msg.getBytes());
		System.out.println("����ǰ��" + data);
		
        
        //����
        byte[] secretArr = DESedeUtil.encryptMode(data.getBytes());    
        System.out.println("���ܺ�" + new String(secretArr));
        
        //����
        byte[] myMsgArr = DESedeUtil.decryptMode(secretArr);  
        System.out.println("���ܺ�" + new String(myMsgArr));
        
        byte[] byteArray = Base64Util.decryptBASE64(data);
		System.out.println("���ܺ�" + new String(byteArray));
        
        //System.out.println("2012PinganVitality075522628888ForShenZhenBelter075561869839".length());
	}

}
