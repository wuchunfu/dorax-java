package org.dorax.lang;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * BigDecimal工具类
 *
 * @author wuchunfu
 * @date 2019-12-08
 */
public class NumberUtils {

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, 10);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 格式化双精度，保留两个小数
     *
     * @return 格式化后的数据
     */
    public static String formatDouble(double b) {
        BigDecimal bg = BigDecimal.valueOf(b);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 百分比计算
     *
     * @return 格式化后的数据
     */
    public static String formatScale(double one, long total) {
        BigDecimal bg = new BigDecimal(one * 100 / total);
        return bg.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 格式化数值类型
     *
     * @param data    数据
     * @param pattern 格式
     * @return 格式化后的数据
     */
    public static String formatNumber(Object data, String pattern) {
        DecimalFormat df;
        if (pattern == null) {
            df = new DecimalFormat();
        } else {
            df = new DecimalFormat(pattern);
        }
        return df.format(data);
    }

    /**
     * 获取object的值
     *
     * @param object object 对象
     * @return 值
     */
    public static Long getLong(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof BigDecimal) {
            return ((BigDecimal) object).longValue();
        }
        if (object instanceof BigInteger) {
            return ((BigInteger) object).longValue();
        }
        if (object instanceof Long) {
            return ((Long) object);
        }
        return null;
    }

    /**
     * 保留指定位数的小数(少的位数不补零)
     *
     * @param value 双精度值
     * @param n     小数点位数
     * @return 保留指定位数的小数的值
     */
    public static String keepRandomPoint(Double value, int n) {
        if (value == null) {
            value = 0.00;
        }
        return new BigDecimal(value).setScale(n, RoundingMode.HALF_UP).toString();
    }

    /**
     * 浮点保留两位小数(少的位数不补零)
     *
     * @param value 双精度值
     * @return 保留两位小数的值
     */
    public static String keep2Point(double value) {
        return keepRandomPoint(value, 2);
    }

    /**
     * 浮点保留1位小数(少的位数不补零)
     *
     * @param value 双精度值
     * @return 保留一位小数的值
     */
    public static String keep1Point(double value) {
        return keepRandomPoint(value, 1);
    }

    /**
     * 浮点保留任意位小数(少位补零)
     *
     * @param value 双精度值
     * @param n     小数点位数
     * @return 保留任意位小数的值
     */
    public static String keepRandomPointZero(double value, int n) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(Double.valueOf(keepRandomPoint(value, n)));
    }

    /**
     * 浮点保留两位小数(少位补零)
     *
     * @param value 双精度值
     * @return 保留两位小数的值
     */
    public static String keep2PointZero(double value) {
        return keepRandomPointZero(value, 2);
    }

    /**
     * 获取任意小数点位的百分比表示
     *
     * @param value 双精度值
     * @param n     小数点位数
     * @return 获取任意小数点位的百分比表示的值
     */
    public static String percentRandomPoint(double value, int n) {
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setGroupingUsed(false);
        percent.setMaximumFractionDigits(n);
        return percent.format(value);
    }

    /**
     * 百分比保留两位小数
     *
     * @param value 双精度值
     * @return 百分比保留两位小数的值
     */
    public static String percent2Point(double value) {
        return percentRandomPoint(value, 2);
    }

    /**
     * 获取格式化经纬度后的小数(保留3位)
     *
     * @param value 双精度值
     * @return 获取格式化经纬度后的小数(保留3位)的值
     */
    public static String latLngPoint(double value) {
        return keepRandomPoint(value, 3);
    }
}
