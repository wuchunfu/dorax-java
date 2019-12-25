package org.dorax.codec;

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

    public static void main(String[] args) {
        System.out.println(encrypt("123456"));
    }
}
