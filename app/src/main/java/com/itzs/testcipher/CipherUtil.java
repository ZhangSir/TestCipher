package com.itzs.testcipher;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 使用系统Javax包中Cipher类进行加密解密的工具（这里使用AES对称加密方式）
 * Created by zhangshuo on 2016/6/28.
 */
public class CipherUtil {

    private static byte[] getRawKey(byte[] seed) throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG"); // 获得一个随机数，传入的参数为默认方式。
        sr.setSeed(seed);  // 设置一个种子，这个种子一般是用户设定的密码。也可以是其它某个固定的字符串
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");  // 获得一个key生成器（AES加密模式）
        keyGen.init(128, sr);      // 设置密匙长度128位
        SecretKey key = keyGen.generateKey();  // 获得密匙
        byte[] raw = key.getEncoded();   // 返回密匙的byte数组供加解密使用
        return raw;
    }

    private static byte[] encry(byte[] raw, byte[] input) throws Exception {  // 加密
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES"); // 根据上一步生成的密匙指定一个密匙（密匙二次加密？）
        Cipher cipher = Cipher.getInstance("AES");  // 获得Cypher实例对象
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);  // 初始化模式为加密模式，并指定密匙
        byte[] encode = cipher.doFinal(input);  // 执行加密操作。 input为需要加密的byte数组
        return encode;                         // 返回加密后的密文（byte数组)
    }

    private static byte[] decry(byte[] raw, byte[] encode) throws Exception{ // 解密
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);   // 解密的的方法差不多，只是这里的模式不一样
        byte[] decode = cipher.doFinal(encode);  // 加解密都通过doFinal方法来执行最终的实际操作
        return decode;
    }

    public static String decryptString(String seed, byte[] encode) throws Exception{
        byte[] raw = getRawKey(seed.getBytes());
        byte[] decode = decry(raw, encode);
        return new String(decode);
    }

    public static byte[] encryptString(String seed, String input) throws Exception{
        byte[] raw = getRawKey(seed.getBytes());
        byte[] encode = encry(raw, input.getBytes());
// return new String(encode);  // 加密后的byte数组转换成字符串时将出现问题。
        return encode;
    }

    private static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
