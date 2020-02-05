package org.dorax.lang;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金钱处理工具类
 *
 * @author wuchunfu
 * @date 2020-02-05
 */
public class MoneyUtils {

    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    /**
     * 汉语中货币单位大写
     */
    private static final String[] CN_UPPER_MONEY_UNIT = {"分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟"};
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 零元整
     */
    private static final String CN_ZERO_FULL = "零元整";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;

    /**
     * 人民币转换为大写,格式为：x万x千x百x十x元x角x分
     *
     * @param numberOfMoney 传入的金额
     * @return 格式化后的字符串
     */
    public static String number2CnMoney(String numberOfMoney) {
        return number2CnMoney(new BigDecimal(numberOfMoney));
    }

    /**
     * 人民币转换为大写,格式为：x万x千x百x十x元x角x分
     *
     * @param numberOfMoney 传入的金额
     * @return 格式化后的字符串
     */
    public static String number2CnMoney(BigDecimal numberOfMoney) {
        StringBuilder sb = new StringBuilder();
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZERO_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONEY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONEY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONEY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONEY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONEY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }

    /**
     * 将人民币转换为会计格式金额(xxxx,xxxx,xxxx.xx),保留两位小数
     *
     * @param money 待转换的金额
     * @return 转换后的字符串
     */
    public static String accountantMoney(BigDecimal money) {
        return accountantMoney(money, 2, 1);
    }

    /**
     * 格式化金额，显示为xxx万元，xxx百万,xxx亿
     *
     * @param money   待处理的金额
     * @param scale   小数点后保留的位数
     * @param divisor 格式化值（10:十元、100:百元,1000千元，10000万元......）
     * @return 格式化后的字符串
     */
    public static String getFormatMoney(BigDecimal money, int scale, double divisor) {
        return formatMoney(money, scale, divisor) + getCellFormat(divisor);
    }

    /**
     * 获取会计格式的人民币(格式为:xxxx,xxxx,xxxx.xx)
     *
     * @param money   待处理的金额
     * @param scale   小数点后保留的位数
     * @param divisor 格式化值（10:十元、100:百元,1000千元，10000万元......）
     * @return 转换后的字符串
     */
    public static String getAccountantMoney(BigDecimal money, int scale, double divisor) {
        return accountantMoney(money, scale, divisor) + getCellFormat(divisor);
    }

    /**
     * 将人民币转换为会计格式金额(xxxx,xxxx,xxxx.xx)
     *
     * @param money   待处理的金额
     * @param scale   小数点后保留的位数
     * @param divisor 格式化值
     * @return 格式化后的字符串
     */
    private static String accountantMoney(BigDecimal money, int scale, double divisor) {
        String disposeMoneyStr = formatMoney(money, scale, divisor);
        // 小数点处理
        int dotPosition = disposeMoneyStr.indexOf(".");
        // 小数点之前的字符串
        String exceptDotMoney;
        // 小数点之后的字符串
        String dotMoney = null;
        if (dotPosition > 0) {
            exceptDotMoney = disposeMoneyStr.substring(0, dotPosition);
            dotMoney = disposeMoneyStr.substring(dotPosition);
        } else {
            exceptDotMoney = disposeMoneyStr;
        }
        // 负数处理
        int negativePosition = exceptDotMoney.indexOf("-");
        if (negativePosition == 0) {
            exceptDotMoney = exceptDotMoney.substring(1);
        }
        StringBuilder reverseExceptDotMoney = new StringBuilder(exceptDotMoney);
        // 字符串倒转
        reverseExceptDotMoney.reverse();
        char[] moneyChar = reverseExceptDotMoney.toString().toCharArray();
        // 返回值
        StringBuilder returnMoney = new StringBuilder();
        for (int i = 0; i < moneyChar.length; i++) {
            if (i != 0 && i % 3 == 0) {
                // 每隔3位加','
                returnMoney.append(",");
            }
            returnMoney.append(moneyChar[i]);
        }
        // 字符串倒转
        returnMoney.reverse();
        if (dotPosition > 0) {
            returnMoney.append(dotMoney);
        }
        if (negativePosition == 0) {
            return "-" + returnMoney.toString();
        } else {
            return returnMoney.toString();
        }
    }

    /**
     * 格式化金额，显示为xxx万元，xxx百万,xxx亿
     *
     * @param money   待处理的金额
     * @param scale   小数点后保留的位数
     * @param divisor 格式化值
     * @return 格式化后的字符串
     */
    private static String formatMoney(BigDecimal money, int scale, double divisor) {
        if (divisor == 0) {
            return "0.00";
        }
        if (scale < 0) {
            return "0.00";
        }
        BigDecimal divisorBd = new BigDecimal(divisor);
        return money.divide(divisorBd, scale, RoundingMode.HALF_UP).toString();
    }

    /**
     * 格式化值
     *
     * @param divisor 格式化值
     * @return 格式化后的值
     */
    private static String getCellFormat(double divisor) {
        String str = String.valueOf(divisor);
        int len = str.substring(0, str.indexOf(".")).length();
        String cell = "";
        switch (len) {
            case 1:
                cell = "元";
                break;
            case 2:
                cell = "十元";
                break;
            case 3:
                cell = "百元";
                break;
            case 4:
                cell = "千元";
                break;
            case 5:
                cell = "万元";
                break;
            case 6:
                cell = "十万元";
                break;
            case 7:
                cell = "百万元";
                break;
            case 8:
                cell = "千万元";
                break;
            case 9:
                cell = "亿元";
                break;
            case 10:
                cell = "十亿元";
                break;
            default:
                break;
        }
        return cell;
    }
}
