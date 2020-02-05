package org.dorax.lang;

import org.dorax.codec.EncodeUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author wuchunfu
 * @date 2019-12-09
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 下划线转驼峰
     *
     * @param str 要转换的字符串
     * @return 转换后的值
     */
    public static String lineToHump(String str) {
        if (null == str || "".equals(str)) {
            return str;
        }
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        str = sb.toString();
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    /**
     * 驼峰转下划线,效率比上面高
     *
     * @param str 要转换的字符串
     * @return 转换后的值
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线(简单写法，效率低于{@link #humpToLine(String)})
     *
     * @param str 要转换的字符串
     * @return 转换后的值
     */
    public static String humpToLine2(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    /**
     * 首字母转小写
     *
     * @param str 要转换的字符串
     * @return 转换后的值
     */
    public static String toLowerCaseFirstOne(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return Character.toLowerCase(str.charAt(0)) + str.substring(1);
        }
    }

    /**
     * 首字母转大写
     *
     * @param str 要转换的字符串
     * @return 转换后的值
     */
    public static String toUpperCaseFirstOne(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }
    }

    /**
     * Object 转 String
     *
     * @param object object 对象
     * @return string 值
     */
    public static String getString(Object object) {
        return getString(object, "");
    }

    /**
     * Object 转 String
     *
     * @param object       object 对象
     * @param defaultValue 默认值
     * @return string 值
     */
    public static String getString(Object object, String defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return object.toString();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Object 转 Integer
     *
     * @param object object 对象
     * @return int 值
     */
    public static int getInt(Object object) {
        return getInt(object, -1);
    }

    /**
     * Object 转 Integer
     *
     * @param object       object 对象
     * @param defaultValue 默认值
     * @return int 值
     */
    public static int getInt(Object object, Integer defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(object.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Object 转 Boolean
     *
     * @param object object 对象
     * @return 布尔值
     */
    public static boolean getBoolean(Object object) {
        return getBoolean(object, false);
    }

    /**
     * Object 转 Boolean
     *
     * @param object       object 对象
     * @param defaultValue 默认值
     * @return 布尔对象
     */
    public static boolean getBoolean(Object object, Boolean defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(object.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为字节数组
     *
     * @param str 字符串
     * @return 字节数组
     */
    public static byte[] getBytes(String str) {
        if (str != null) {
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 转换为字节数组
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String toString(byte[] bytes) {
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 去除左右空格（包含中文空格）
     *
     * @param str 字符串
     * @return 修剪后的字符串
     */
    public static String strTrim(final String str) {
        return str == null ? null : str.replaceAll("^[\\s|　| ]*|[\\s|　| ]*$", "");
    }

    /**
     * 替换掉HTML标签方法
     *
     * @param html html 字符串
     * @return 替换后的字符串
     */
    public static String stripHtml(String html) {
        if (isBlank(html)) {
            return "";
        }
        // html.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "");
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        return m.replaceAll("");
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param html html 字符串
     * @return 替换后的字符串
     */
    public static String toMobileHtml(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }

    /**
     * 对txt进行HTML编码，并将\n转换为&gt;br/&lt;、\t转换为&nbsp; &nbsp;
     *
     * @param txt txt 字符串
     * @return 替换后的字符串
     */
    public static String toHtml(String txt) {
        if (txt == null) {
            return "";
        }
        return replace(replace(EncodeUtils.encodeHtml(trim(txt)), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
    }

    /**
     * 缩略字符串（不区分中英文字符）
     *
     * @param str    目标字符串
     * @param length 截取长度
     * @return 缩略字符串
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int currentLength = 0;
        for (char c : stripHtml(EncodeUtils.decodeHtml(str)).toCharArray()) {
            currentLength += String.valueOf(c).getBytes(StandardCharsets.UTF_8).length;
            if (currentLength <= length - 3) {
                sb.append(c);
            } else {
                sb.append("...");
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 缩略字符串替换Html正则表达式预编译
     */
    private static Pattern p1 = Pattern.compile("<([a-zA-Z]+)[^<>]*>");

    /**
     * 缩略字符串（适应于与HTML标签的）
     *
     * @param param  目标字符串
     * @param length 截取长度
     * @return 缩略字符串
     */
    public static String htmlAbbr(String param, int length) {
        if (param == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        int n = 0;
        char temp;
        // 是不是HTML代码
        boolean isCode = false;
        // 是不是HTML特殊字符,如&nbsp;
        boolean isHtml = false;
        for (int i = 0; i < param.length(); i++) {
            temp = param.charAt(i);
            if (temp == '<') {
                isCode = true;
            } else if (temp == '&') {
                isHtml = true;
            } else if (temp == '>' && isCode) {
                n = n - 1;
                isCode = false;
            } else if (temp == ';' && isHtml) {
                isHtml = false;
            }
            if (!isCode && !isHtml) {
                n += String.valueOf(temp).getBytes(StandardCharsets.UTF_8).length;
            }
            if (n <= length - 3) {
                result.append(temp);
            } else {
                result.append("...");
                break;
            }
        }
        // 取出截取字符串中的HTML标记
        String tempResult = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
        // 去掉不需要结素标记的HTML标记
        tempResult = tempResult.replaceAll("</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|"
                + "HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|"
                + "basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|"
                + "option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>", "");
        // 去掉成对的HTML标记
        tempResult = tempResult.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
        // 用正则表达式取出标记
        Matcher m = p1.matcher(tempResult);
        List<String> endHtml = new ArrayList<>();
        while (m.find()) {
            endHtml.add(m.group(1));
        }
        // 补全不成对的HTML标记
        for (int i = endHtml.size() - 1; i >= 0; i--) {
            result.append("</");
            result.append(endHtml.get(i));
            result.append(">");
        }
        return result.toString();
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return 转换后的字符串
     */
    public static String cap(String str) {
        return capitalize(str);
    }

    /**
     * 首字母小写
     *
     * @param str 字符串
     * @return 转换后的字符串
     */
    public static String uncap(String str) {
        return uncapitalize(str);
    }

    /**
     * 驼峰命名法工具
     *
     * @return camelCase(" hello_world ") == "helloWorld"
     * capCamelCase("hello_world") == "HelloWorld"
     * uncamelCase("helloWorld") = "hello_world"
     */
    public static String camelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == SEPARATOR) {
                // 不允许第二个字符是大写
                upperCase = i != 1;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return camelCase(" hello_world ") == "helloWorld"
     * capCamelCase("hello_world") == "HelloWorld"
     * uncamelCase("helloWorld") = "hello_world"
     */
    public static String capCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = camelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return camelCase(" hello_world ") == "helloWorld"
     * capCamelCase("hello_world") == "HelloWorld"
     * uncamelCase("helloWorld") = "hello_world"
     */
    public static String uncamelCase(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean nextUpperCase = true;
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     *
     * @param objectString 对象串
     *                     例如：row.user.id
     *                     返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString) {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] vals = split(objectString, ".");
        for (String s : vals) {
            val.append(".").append(s);
            result.append("!").append(val.substring(1)).append("?'':");
        }
        result.append(val.substring(1));
        return result.toString();
    }

    /**
     * 获取随机字符串
     *
     * @param count 字符串个数
     * @return 随机字符串
     */
    public static String getRandomStr(int count) {
        char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);
            s.append(r);
        }
        return s.toString();
    }

    /**
     * 获取随机数字
     *
     * @param count 字符串个数
     * @return 随机字符串
     */
    public static String getRandomNum(int count) {
        char[] codeSeq = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);
            s.append(r);
        }
        return s.toString();
    }

    /**
     * 获取树节点名字
     *
     * @param isShowCode 是否显示编码 true or 1：显示在左侧：(code)name 2：显示在右侧：name(code) false or null：不显示编码：name
     * @param code       编码
     * @param name       名称
     * @return 数据节点名称
     */
    public static String getTreeNodeName(String isShowCode, String code, String name) {
        if ("true".equals(isShowCode) || "1".equals(isShowCode)) {
            return "(" + code + ") " + StringUtils.replace(name, " ", "");
        } else if ("2".equals(isShowCode)) {
            return StringUtils.replace(name, " ", "") + " (" + code + ")";
        } else {
            return StringUtils.replace(name, " ", "");
        }
    }

    /**
     * 将字符串重复N次，null、""不在循环次数里面 <br>
     * 当value == null || value == "" return value;<br>
     * 当count <= 1 返回  value
     *
     * @param value 需要循环的字符串
     * @param count 循环的次数
     * @return 重复N次的字符串
     */
    public static String repeatString(String value, int count) {
        if (value == null || "".equals(value) || count <= 1) {
            return value;
        }
        int length = value.length();
        // 长度为1，存在字符
        if (length == 1) {
            return repeatChar(value.charAt(0), count);
        }
        int outputLength = length * count;
        switch (length) {
            case 1:
                return repeatChar(value.charAt(0), count);
            case 2:
                char ch0 = value.charAt(0);
                char ch1 = value.charAt(1);
                char[] output2 = new char[outputLength];
                for (int i = count * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default:
                StringBuilder buf = new StringBuilder(outputLength);
                for (int i = 0; i < count; i++) {
                    buf.append(value);
                }
                return buf.toString();
        }
    }

    /**
     * 将某个字符重复N次
     *
     * @param ch    需要循环的字符
     * @param count 循环的次数
     * @return 重复N次的字符串
     */
    public static String repeatChar(char ch, int count) {
        char[] buf = new char[count];
        for (int i = count - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    /**
     * 判断字符串是否全部都为小写
     *
     * @param value 待判断的字符串
     * @return 是否全部为小写
     */
    public static boolean isAllLowerCase(String value) {
        if (value == null || "".equals(value)) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isLowerCase(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否全部大写
     *
     * @param value 待判断的字符串
     * @return 是否全部为大写
     */
    public static boolean isAllUpperCase(String value) {
        if (value == null || "".equals(value)) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isUpperCase(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 反转字符串
     *
     * @param value 待反转的字符串
     * @return 反转后的字符串
     */
    public static String reverse(String value) {
        if (value == null) {
            return null;
        }
        return new StringBuffer(value).reverse().toString();
    }

    /**
     * 截取字符串，支持中英文混乱，其中中文当做两位处理
     *
     * @param resourceString 待截取的字符串
     * @param length         截取长度
     * @return 截取后的字符串
     */
    public static String subString(String resourceString, int length) {
        String resultString = "";
        if (resourceString == null || "".equals(resourceString) || length < 1) {
            return resourceString;
        }
        if (resourceString.length() < length) {
            return resourceString;
        }
        char[] chr = resourceString.toCharArray();
        int strNum = 0;
        int strGBKNum = 0;
        boolean isHaveDot = false;
        for (int i = 0; i < resourceString.length(); i++) {
            // 0xa1汉字最小位开始
            if (chr[i] >= 0xa1) {
                strNum = strNum + 2;
                strGBKNum++;
            } else {
                strNum++;
            }
            if (strNum == length || strNum == length + 1) {
                if (i + 1 < resourceString.length()) {
                    isHaveDot = true;
                }
                break;
            }
        }
        resultString = resourceString.substring(0, strNum - strGBKNum);
        if (isHaveDot) {
            resultString = resultString + "...";
        }
        return resultString;
    }

    /**
     * 截取 HTML 字符串
     *
     * @param htmlString 待截取的 html 字符串
     * @param length     截取长度
     * @return 截取后的字符串
     */
    public static String subHTMLString(String htmlString, int length) {
        return subString(delHTMLTag(htmlString), length);
    }

    /**
     * 过滤html标签，包括script、style、html、空格、回车标签
     *
     * @param htmlStr 待过滤的字符串
     * @return 过滤后的字符串
     */
    public static String delHTMLTag(String htmlStr) {
        // 定义script的正则表达式
        String regexScript = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        // 定义style的正则表达式
        String regexStyle = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        // 定义HTML标签的正则表达式
        String regexHtml = "<[^>]+>";
        // 定义空格回车换行符
        String regexSpace = "\\s*|\t|\r|\n";

        Pattern pScript = Pattern.compile(regexScript, Pattern.CASE_INSENSITIVE);
        Matcher mScript = pScript.matcher(htmlStr);
        // 过滤script标签
        htmlStr = mScript.replaceAll("");

        Pattern pStyle = Pattern.compile(regexStyle, Pattern.CASE_INSENSITIVE);
        Matcher mStyle = pStyle.matcher(htmlStr);
        // 过滤style标签
        htmlStr = mStyle.replaceAll("");

        Pattern pHtml = Pattern.compile(regexHtml, Pattern.CASE_INSENSITIVE);
        Matcher mHtml = pHtml.matcher(htmlStr);
        // 过滤html标签
        htmlStr = mHtml.replaceAll("");

        Pattern pSpace = Pattern.compile(regexSpace, Pattern.CASE_INSENSITIVE);
        Matcher mSpace = pSpace.matcher(htmlStr);
        // 过滤空格回车标签
        htmlStr = mSpace.replaceAll("");

        // 返回文本字符串
        return htmlStr.trim();
    }
}
