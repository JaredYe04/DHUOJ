package client.util;


import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Decrypt {
   public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
		
	    /**
	     * DES算法，加密
	     *
	     * @param data 待加密字符串
	     * @param key  加密私钥，长度不能够小于8位
	     * @return 加密后的字节数组，一般结合Base64编码使用
	     * @throws InvalidAlgorithmParameterException 
	     * @throws Exception 
	     */
	    public static byte[] encrypt(String key,String data) {
	    	if(data == null)
	    		return new byte[0];
	    	try{
		    	DESKeySpec dks = new DESKeySpec(key.getBytes());	    	
		    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		        //key的长度不能够小于8位字节
		        Key secretKey = keyFactory.generateSecret(dks);
		        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		        IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
		        AlgorithmParameterSpec paramSpec = iv;
		        cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);           
		        byte[] bytes = cipher.doFinal(data.getBytes());            
		        return bytes;
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		return new byte[0];
	    	}
	    }
	
	    /**
	     * DES算法，解密
	     *
	     * @param data 待解密字符串
	     * @param key  解密私钥，长度不能够小于8位
	     * @return 解密后的字节数组
	     * @throws Exception 异常
	     */
	    public static String decrypt(String key,byte[] data) {
	    	if(data.length==0)
	    		return "<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"no\"?><root><respMsg>用户名或密码错误</respMsg></root>";
	        try {
		    	DESKeySpec dks = new DESKeySpec(key.getBytes());
		    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	            //key的长度不能够小于8位字节
	            Key secretKey = keyFactory.generateSecret(dks);
	            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
	            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
	            AlgorithmParameterSpec paramSpec = iv;
	            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
	            return new String(cipher.doFinal(data));
	        } catch (Exception e){
	    		e.printStackTrace();
	    		return "<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"no\"?><root><respMsg>用户名或密码错误</respMsg></root>";
	        }
	    }
}
