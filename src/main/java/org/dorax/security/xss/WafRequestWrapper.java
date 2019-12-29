package org.dorax.security.xss;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Request请求过滤包装
 *
 * @author wuchunfu
 * @date 2019-12-25
 */
public class WafRequestWrapper extends HttpServletRequestWrapper {

    private boolean filterXss;
    private boolean filterSql;

    public WafRequestWrapper(HttpServletRequest request, boolean filterXss, boolean filterSql) {
        super(request);
        this.filterXss = filterXss;
        this.filterSql = filterSql;
    }

    public WafRequestWrapper(HttpServletRequest request) {
        this(request, true, true);
    }

    /**
     * 数组参数过滤
     *
     * @param parameter 过滤参数
     * @return 参数值列表
     */
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = filterParamString(values[i]);
        }
        return encodedValues;
    }

    /**
     * 获取参数集合
     *
     * @return 参数结果集合
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> primary = super.getParameterMap();
        Map<String, String[]> result = new HashMap<>(primary.size());
        for (Map.Entry<String, String[]> entry : primary.entrySet()) {
            result.put(entry.getKey(), filterEntryString(entry.getValue()));
        }
        return result;
    }

    /**
     * 过滤字符串参数集合
     *
     * @param rawValue 参数集合
     * @return 过滤后的结果
     */
    protected String[] filterEntryString(String[] rawValue) {
        for (int i = 0; i < rawValue.length; i++) {
            rawValue[i] = filterParamString(rawValue[i]);
        }
        return rawValue;
    }

    /**
     * 参数过滤
     *
     * @param parameter 过滤参数
     * @return 过滤后的结果
     */
    @Override
    public String getParameter(String parameter) {
        return filterParamString(super.getParameter(parameter));
    }

    /**
     * 请求头过滤
     *
     * @param name 过滤内容
     * @return 过滤后的结果
     */
    @Override
    public String getHeader(String name) {
        return filterParamString(super.getHeader(name));
    }

    /**
     * Cookie内容过滤
     *
     * @return cookie 内容集合
     */
    @Override
    public Cookie[] getCookies() {
        Cookie[] existingCookies = super.getCookies();
        if (existingCookies != null) {
            for (Cookie cookie : existingCookies) {
                cookie.setValue(filterParamString(cookie.getValue()));
            }
        }
        return existingCookies;
    }

    /**
     * 过滤字符串内容
     *
     * @param rawValue 待处理内容
     * @return 过滤后的结果
     */
    protected String filterParamString(String rawValue) {
        if (null == rawValue) {
            return null;
        }
        String tmpStr = rawValue;
        if (this.filterXss) {
            tmpStr = WafKit.stripXss(rawValue);
        }
        if (this.filterSql) {
            tmpStr = WafKit.stripSqlInjection(tmpStr);
        }
        return tmpStr;
    }
}
