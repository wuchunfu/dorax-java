package org.dorax.io;

import java.text.DecimalFormat;

/**
 * 文件大小转换工具类
 *
 * @author wuchunfu
 * @date 2019-12-24
 */
public class FileSizeHelper {

    public static long ONE_KB = 1024;
    public static long ONE_MB = ONE_KB * 1024;
    public static long ONE_GB = ONE_MB * 1024;
    public static long ONE_TB = ONE_GB * (long) 1024;
    public static long ONE_PB = ONE_TB * (long) 1024;

    /**
     * 获取格式化后的字符串
     *
     * @param fileSize 文件字节大小
     * @return 格式化后的字符串
     */
    public static String getFormatFileSize(Long fileSize) {
        if (fileSize == null) {
            return null;
        }
        return getFormatFileSize(fileSize.longValue());
    }

    /**
     * 获取格式化后的字符串
     *
     * @param fileSize 文件字节大小
     * @return 格式化后的字符串
     */
    public static String getFormatFileSize(long fileSize) {
        if (fileSize < 0) {
            return String.valueOf(fileSize);
        }
        String result = getFormatFileSize(fileSize, ONE_PB, "PB");
        if (result != null) {
            return result;
        }
        result = getFormatFileSize(fileSize, ONE_TB, "TB");
        if (result != null) {
            return result;
        }
        result = getFormatFileSize(fileSize, ONE_GB, "GB");
        if (result != null) {
            return result;
        }
        result = getFormatFileSize(fileSize, ONE_MB, "MB");
        if (result != null) {
            return result;
        }
        result = getFormatFileSize(fileSize, ONE_KB, "KB");
        if (result != null) {
            return result;
        }
        return fileSize + "B";
    }

    /**
     * 获取格式化后的字符串
     *
     * @param fileSize 文件字节大小
     * @param unit     字节单位
     * @param unitName 字节单位名称
     * @return 格式化后的字符串
     */
    private static String getFormatFileSize(long fileSize, long unit, String unitName) {
        if (fileSize == 0) {
            return "0";
        }
        if (fileSize / unit >= 1) {
            double value = fileSize / (double) unit;
            DecimalFormat df = new DecimalFormat("######.##" + unitName);
            return df.format(value);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getFormatFileSize(999999999999999999L));
    }
}
