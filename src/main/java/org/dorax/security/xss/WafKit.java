package org.dorax.security.xss;

import java.util.regex.Pattern;

/**
 * Web防火墙工具类
 *
 * @author wuchunfu
 * @date 2019-12-25
 */
public class WafKit {

    private final static Pattern SCRIPT_TWO_PATTERN = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
    private final static Pattern SCRIPT_ONE_START_PATTERN = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private final static Pattern SCRIPT_ONE_END_PATTERN = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
    private final static Pattern EVAL_PATTERN = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private final static Pattern EXPRESSION_PATTERN = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private final static Pattern JAVASCRIPT_PATTERN = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
    private final static Pattern VBSCRIPT_PATTERN = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
    private final static Pattern ONLOAD_PATTERN = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    /**
     * 过滤XSS脚本内容
     *
     * @param value 待处理内容
     * @return 处理后的结果
     */
    public static String stripXss(String value) {
        String rlt = null;
        if (null != value) {
            // Avoid null characters
            rlt = value.replaceAll("", "");

            // Avoid anything between script tags
            Pattern scriptPattern = SCRIPT_TWO_PATTERN;
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Remove any lonesome <script ...> tag
            scriptPattern = SCRIPT_ONE_START_PATTERN;
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Remove any lonesome </script> tag
            scriptPattern = SCRIPT_ONE_END_PATTERN;
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Avoid eval(...) expressions
            scriptPattern = EVAL_PATTERN;
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Avoid expression(...) expressions
            scriptPattern = EXPRESSION_PATTERN;
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Avoid javascript:... expressions
            scriptPattern = JAVASCRIPT_PATTERN;
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Avoid vbscript:... expressions
            scriptPattern = VBSCRIPT_PATTERN;
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            // Avoid onload= expressions
            scriptPattern = ONLOAD_PATTERN;
            rlt = scriptPattern.matcher(rlt).replaceAll("");
        }
        return rlt;
    }

    /**
     * 过滤SQL注入内容
     *
     * @param value 待处理内容
     * @return 处理后的结果
     */
    public static String stripSqlInjection(String value) {
        return (null == value) ? null : value.replaceAll("('.+--)|(--)|(%7C)", "");
    }

    /**
     * 过滤SQL/XSS注入内容
     *
     * @param value 待处理内容
     * @return 处理后的结果
     */
    public static String stripSqlXss(String value) {
        return stripXss(stripSqlInjection(value));
    }
}
