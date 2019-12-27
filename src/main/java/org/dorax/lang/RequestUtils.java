package org.dorax.lang;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * request工具类
 *
 * @author wuchunfu
 * @date 2019-12-27
 */
public class RequestUtils {

    /**
     * 移除request指定参数
     *
     * @param request   请求对象
     * @param paramName 请求参数名
     * @return 移除后的值
     */
    public String removeParam(HttpServletRequest request, String paramName) {
        String queryString = "";
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.equals(paramName)) {
                continue;
            }
            if ("".equals(queryString)) {
                queryString = key + "=" + request.getParameter(key);
            } else {
                queryString += "&" + key + "=" + request.getParameter(key);
            }
        }
        return queryString;
    }

    /**
     * 获取请求basePath
     *
     * @param request 请求对象
     * @return 请求basePath
     */
    public static String getBasePath(HttpServletRequest request) {
        StringBuilder basePath = new StringBuilder();
        String scheme = request.getScheme();
        String domain = request.getServerName();
        int port = request.getServerPort();
        basePath.append(scheme);
        basePath.append("://");
        basePath.append(domain);
        if ("http".equalsIgnoreCase(scheme) && 80 != port) {
            basePath.append(":").append(port);
        } else if ("https".equalsIgnoreCase(scheme) && port != 443) {
            basePath.append(":").append(port);
        }
        return basePath.toString();
    }

    /**
     * 获取ip工具类，除了getRemoteAddr，其他ip均可伪造
     *
     * @param request 请求对象
     * @return ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        // 网宿cdn的真实ip
        String ip = request.getHeader("Cdn-Src-Ip");
        if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
            // 蓝讯cdn的真实ip
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
            // 获取代理ip
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
            // 获取代理ip
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // 获取代理ip
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // 获取真实ip
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 请求中参数转Map<String, String>,for支付宝异步回调,平时建议直接使用request.getParameterMap(),返回Map<String, String[]>
     *
     * @param request 请求对象
     * @return 转换后的请求参数
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            result.put(parameterName, request.getParameter(parameterName));
        }
        return result;
    }
}
