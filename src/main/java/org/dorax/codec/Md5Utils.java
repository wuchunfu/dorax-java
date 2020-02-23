package org.dorax.codec;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密类（封装jdk自带的md5加密方法）
 *
 * @author wuchunfu
 * @date 2019-12-25
 */
public class Md5Utils {

    /**
     * 获取加密字符串
     *
     * @param source 明文字符串
     * @return 密文字符串
     */
    public static String encrypt(String source) {
        return encodeMd5(source.getBytes());
    }

    /**
     * 获取加密字符串
     *
     * @param source 明文字节
     * @return 密文字符串
     */
    private static String encodeMd5(byte[] source) {
        try {
            return encodeHex(MessageDigest.getInstance("MD5").digest(source));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 获取加密字符串
     *
     * @param bytes 明文哈希
     * @return 密文字符串
     */
    private static String encodeHex(byte[] bytes) {
        StringBuilder buffer = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            if (((int) aByte & 0xff) < 0x10) {
                buffer.append("0");
            }
            buffer.append(Long.toString((int) aByte & 0xff, 16));
        }
        return buffer.toString();
    }

    /**
     * 获取加密字符串
     *
     * @param value  明文字符串
     * @param encode 字符编码
     * @return 秘闻字符串
     */
    public static String getMd5(String value, String encode) {
        String result = "";
        try {
            result = getMd5(value.getBytes(encode));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final int HEX_VALUE_COUNT = 16;

    /**
     * 获取加密字符串
     *
     * @param bytes 明文字节
     * @return 秘闻字符串
     */
    public static String getMd5(byte[] bytes) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] str = new char[16 * 2];
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] tmp = md.digest();
            int k = 0;
            for (int i = 0; i < HEX_VALUE_COUNT; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(str);
    }

    public static void main(String[] args) {
        System.out.println(encrypt("123456"));
    }
}
