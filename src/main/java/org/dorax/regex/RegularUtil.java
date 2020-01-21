package org.dorax.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 后端正则表达式验证
 *
 * @author wuchunfu
 * @date 2019-12-07
 */
public class RegularUtil {

    /**
     * 判断是否是邮箱
     *
     * @param str 待验证的字符串
     * @return 如果是符合的字符串, 返回 true ,否则为 false
     */
    public static boolean isEmail(String str) {
        String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        return match(regex, str);
    }

    /**
     * 判断是否是IP地址
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isIp(String str) {
//        String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
//        String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
        String regex = "((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))";
        return match(regex, str);
    }

    /**
     * 判断是否是 HTTP(s) URL
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isUrl(String str) {
        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        return match(regex, str);
    }

    /**
     * 判断是否是 FTP 网址
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isFTP(String str) {
        String regex = "ftp\\:\\/\\/[^:]*:@([^\\/]*)";
        return match(regex, str);
    }

    /**
     * 判断是否是电话号码
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isTelephone(String str) {
//        String regex = "^(\\d{3,4}-)?\\d{6,8}$";
        String regex = "^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$";
        return match(regex, str);
    }

    /**
     * 判断是否是手机号码
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isMobliePhone(String str) {
        String regex = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
        return match(regex, str);
    }

    /**
     * 验证输入手机号码
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true ,否则为 false
     */
    public static boolean isHandset(String str) {
        String regex = "^[1]+[3,5]+\\d{9}$";
        return match(regex, str);
    }

    /**
     * 验证输入密码条件(字符与数据同时出现)
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true ,否则为 false
     */
    public static boolean isPassword(String str) {
        String regex = "[A-Za-z]+[0-9]";
        return match(regex, str);
    }

    /**
     * 验证输入密码长度 (6-18位)
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isPasswordLength(String str) {
        System.out.println(str.length());
        return str.length() > 5 && str.length() < 19;
    }

    /**
     * 判断是否是邮政编码
     * (?!n) : 匹配任何其后没有紧接指定字符串 n 的字符串。反向预查
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isPostalCode(String str) {
        String regex = "[1-9]\\d{5}(?!\\d)";
        return match(regex, str);
    }

    /**
     * 验证输入身份证号
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isIdCard(String str) {
        String regex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
        return match(regex, str);
    }

    /**
     * 判断是否是外籍分配身份证号码
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isFidCard(String str) {
        String regex = "^F\\S{16}F$";
        return match(regex, str);
    }

    /**
     * 验证输入两位小数
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isDecimal(String str) {
        String regex = "^[0-9]+(.[0-9]{2})?$";
        return match(regex, str);
    }

    /**
     * 验证输入一年的12个月
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isMonth(String str) {
        String regex = "^(0?[[1-9]|1[0-2])$";
        return match(regex, str);
    }

    /**
     * 验证输入一个月的31天
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isDay(String str) {
        String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
        return match(regex, str);
    }

    /**
     * 验证日期 YYYY-MM-DD
     *
     * @param str 待验证的字符串
     * @return 如果是符合网址格式的字符串, 返回 true,否则为 false
     */
    public static boolean isDate(String str) {
        // 严格验证时间格式的(匹配[2002-01-31], [1997-04-30],
        // [2004-01-01])不匹配([2002-01-32], [2003-02-29], [04-01-01])
        // String regex =
        // "^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((01,3-9])|(1[0-2]))-(29|30)))))$";
        // 没加时间验证的 YYYY-MM-DD
        String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$";
        return match(regex, str);
    }

    /**
     * 验证日期时间 YYYY-MM-DD HH:mm:SS
     *
     * @param str 待验证的字符串
     * @return 如果是符合网址格式的字符串, 返回 true,否则为 false
     */
    public static boolean isDateTime(String str) {
        // 加了时间验证的 YYYY-MM-DD 00:00:00
        String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
        return match(regex, str);
    }

    /**
     * 判断是否是数字
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isNumber(String str) {
        String regex = "^[0-9]*$";
        return match(regex, str);
    }

    /**
     * 验证非零的正整数
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isIntNumber(String str) {
        String regex = "^\\+?[1-9][0-9]*$";
        return match(regex, str);
    }

    /**
     * 判断是否是大写字母
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isUpChar(String str) {
        String regex = "^[A-Z]+$";
        return match(regex, str);
    }

    /**
     * 判断是否是小写字母
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isLowChar(String str) {
        String regex = "^[a-z]+$";
        return match(regex, str);
    }

    /**
     * 判断是否是字母
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isLetter(String str) {
        String regex = "^[A-Za-z]+$";
        return match(regex, str);
    }

    /**
     * 判断是否是汉字
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isChinese(String str) {
        String regex = "^[\u4e00-\u9fa5]+$";
        return match(regex, str);
    }

    /**
     * 判断是否是自定义表达式
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isEspressione(String str) {
        String regex = "^\\#\\{.*?}$";
        return match(regex, str);
    }

    /**
     * 判断是否是整数
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isNegativeInteger(String str) {
        String regex = "^-[1-9]\\d*$";
        return match(regex, str);
    }

    /**
     * 判断是否是正整数
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isPositiveInteger(String str) {
        String regex = "^[1-9]\\d*$";
        return match(regex, str);
    }

    /**
     * 判断是否是负数
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isNegativeNumber(String str) {
        String regex = "^-[1-9]\\d*|0$";
        return match(regex, str);
    }

    /**
     * 判断是否是正数
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isPositiveNumber(String str) {
        String regex = "^[1-9]\\d*|0$";
        return match(regex, str);
    }

    /**
     * 判断是否是数字
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isNum(String str) {
        String regex = "^([+-]?)\\d*\\.?\\d+$";
        return match(regex, str);
    }

    /**
     * 正则表达式校验最多两位小数的实数
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isNumAndTwoDecimals(String str) {
        String regex = "^(([0-9]*)|(([0]\\.\\d{0,2}|[0-9]*\\.\\d{0,2})))$";
        return match(regex, str);

    }

    /**
     * 判断是否是整数(负数,零,正数)
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isInteger(String str) {
        String regex = "^-?([0-9]\\d*)$";
        return match(regex, str);
    }

    /**
     * 判断是不是整数和小数
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isIntOrFloat(String str) {
        String regex = "[+]?\\d+(\\.\\d+)?$";
        return match(regex, str);
    }

    /**
     * 判断是否是正浮点数类型
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isPositiveFloat(String str) {
        String regex = "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$";
        return match(regex, str);
    }

    /**
     * 判断是否是负浮点数类型
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isNegativeFloat(String str) {
        String regex = "^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$";
        return match(regex, str);
    }

    /**
     * 判断是否是浮点数类型
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isFloat(String str) {
        String regex = "^-?([1-9]\\d*|0(?!\\.0+$))\\.\\d+?$";
        return match(regex, str);
    }

    /**
     * 判断是否是非负浮点数类型（正浮点数大于等于10的数字且包含0）
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isNonNegativeFloat(String str) {
        String regex = "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$";
        return match(regex, str);
    }

    /**
     * 判断是否是非正浮点数类型（正浮点数大于等于-10的数字且包含0）
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isNonPositiveFloat(String str) {
        String regex = "^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$";
        return match(regex, str);
    }

    /**
     * 判断是否是非正浮点数类型（正浮点数大于等于-10的数字且包含0）
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isACSII(String str) {
        String regex = "^[\\x00-\\xFF]+$";
        return match(regex, str);
    }

    /**
     * 判断是否是是色值
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isColor(String str) {
        String regex = "^#[a-fA-F0-9]{6}$";
        return match(regex, str);
    }

    /**
     * 判断是否是QQ
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isQQ(String str) {
        String regex = "^[1-9]*[1-9][0-9]*$";
        return match(regex, str);
    }

    /**
     * 判断是否是图片, 支持 jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isPicture(String str) {
        String regex = "(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$";
        return match(regex, regex);
    }

    /**
     * 判断是否是压缩文件, 支持 rar|zip|7zip|7z|tgz|gz
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isCompressFile(String str) {
        String regex = "(.*)\\.(rar|zip|7zip|7z|tgz|gz)$";
        return match(regex, str);
    }

    /**
     * 判断是否是字母和数字混合(只有字母或数字也可以)
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isNumAndLetter(String str) {
        String regex = "^[\\d|A-Za-z]+$";
        return match(regex, str);
    }

    /**
     * 判断是否是汉字、字母、数字的混合
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 true,否则为 false
     */
    public static boolean isChineseAzNum(String str) {
        String regex = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";
        return match(regex, str);
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex 的正则表达式格式,返回 true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static void main(String[] args) {
        String mobliePhone = "15138456324";
        String chinese = "中国";
        boolean mobliePhoneFlag = RegularUtil.isMobliePhone(mobliePhone);
        boolean chineseFlag = RegularUtil.isChinese(chinese);
        System.out.println(mobliePhoneFlag);
        System.out.println(chineseFlag);
    }
}
