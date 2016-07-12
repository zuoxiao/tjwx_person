package com.example.tjwx_person.utils;




import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;




/** 
 * DESede Coder<br/> 
 * secret key length:   112/168 bit, default:   168 bit<br/> 
 * mode:    ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/> 
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/ 
 *  
 */  
public class TripleDES {  

    /** 
     * 密钥算法 
     */  
    private static final String KEY_ALGORITHM = "DESede";  

    private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";  
  private static byte[] key = "jsdoif23SDF324jlkdsfpowe".getBytes();
//  private static byte[] key = "channelsoft_chinamobile_ibm".getBytes();

    /** 
     * 转换密钥 
     *  
     * @param key   二进制密钥 
     * @return Key  密钥 
     * @throws Exception 
     */  
    private static Key toKey(byte[] key) throws Exception{  
        //实例化DES密钥规则  
        DESedeKeySpec dks = new DESedeKeySpec(key);  
        //实例化密钥工厂  
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);  
        //生成密钥  
        SecretKey  secretKey = skf.generateSecret(dks);  
        return secretKey;  
    }  

    /** 
     * 加密 
     *  
     * @param data  待加密数据 
     * @param key   密钥 
     * @return byte[]   加密数据 
     * @throws Exception 
     */  
    private static byte[] encrypt(byte[] data,Key key) throws Exception{  
        return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  

    /** 
     * 加密 
     *  
     * @param data  待加密数据 
     * @param key   密钥 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   加密数据 
     * @throws Exception 
     */  
    private static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{  
        //实例化  
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);  
        //使用密钥初始化，设置为加密模式  
        cipher.init(Cipher.ENCRYPT_MODE, key);  
        //执行操作  
        return cipher.doFinal(data);  
    }  

    /** 
     * 解密 
     *  
     * @param data  待解密数据 
     * @param key   密钥 
     * @return byte[]   解密数据 
     * @throws Exception 
     */  
    private static byte[] decrypt(byte[] data,Key key) throws Exception{  
        return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  

    /** 
     * 解密 
     *  
     * @param data  待解密数据 
     * @param key   密钥 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   解密数据 
     * @throws Exception 
     */  
    private static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{  
        //实例化  
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);  
        //使用密钥初始化，设置为解密模式  
        cipher.init(Cipher.DECRYPT_MODE, key);  
        //执行操作  
        return cipher.doFinal(data);  
    }  

    /**
     * TripleDES加密后Base64转码
     * @param planText 需要加密的字符串
     * @return TripleDES且Base64后字符串
     * */
    public static String encryptAndBase64(String planText){
        return encryptAndBase64(planText,null);
    }
    
    public static String encryptAndBase64(String planText,byte[]keyIn){
 
        if(planText==null)
            return null;
        try {
            Key k ;
            if(keyIn==null || keyIn.length==0){
                k = toKey(key);
            }else{
                k = toKey(keyIn);
            }
            byte[] encryptData = encrypt(planText.getBytes("UTF-8"),k);
            return Base64.encodeBytes(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
    }
    
    /**
     * Base64解码后TripleDES解密
     * @param encryptStr 需要解密的字符串
     * @return 解密后的字符串
     * */
    public static String decryptAndBase64(String encryptStr){
        return decryptAndBase64(encryptStr, null);
    }
    
    public static String decryptAndBase64(String encryptStr,byte[]keyIn){
        if(encryptStr==null)
            return null;
        try{
            Key k ;
            if(keyIn==null || keyIn.length==0){
                k = toKey(key);
            }else{
                k = toKey(keyIn);
            }
            
            byte[] decryptData = decrypt(Base64.decode(encryptStr.getBytes("UTF-8")), k);
            return new String(decryptData);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    //去掉手机号前的5个0
    public static String filterDecryptAndBase64(String encryptStr,byte[]keyIn){
        String eData = TripleDES.decryptAndBase64(encryptStr,keyIn);
        if(eData!=null && eData.length()==16){
            return eData.substring(5,eData.length());
        }
        return eData;
    }
    
    
    public static void main(String[] args){
        String eData = TripleDES.encryptAndBase64("15818682658");
//       du.encryptData("0000013260389647");
        System.out.println(eData);
        System.out.println(eData.length());
        try
        {
            System.out.println(URLEncoder.encode(eData, "utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //http://211.136.93.44/vgopread/readWapIndex.do?u=bxkFvrMzE7x945GUBUambw%3D%3D&c=6
        String uData = TripleDES.filterDecryptAndBase64("0Zp7QDfpdIr0k56OU/zC/w==",null);
//      System.out.println(TripleDES.decryptAndBase64("bxkFvrMzE7x945GUBUambw=="));
        System.out.println(uData);
    }


}  
