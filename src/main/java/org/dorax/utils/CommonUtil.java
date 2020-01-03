package org.dorax.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wcf
 * @date 2019-12-10
 */
public class CommonUtil {

    public static String urlPage(String urlStr) {
        String page = null;
        String[] arrSplit = urlStr.split("[?]");
        if ((urlStr.length()) > 0 && (arrSplit.length >= 1) && (arrSplit[0] != null)) {
            page = arrSplit[0];
        }
        return page;
    }


    private static String truncateUrlPage(String urlStr) {
        String strAllParam = null;
        String[] arrSplit = urlStr.split("[?]");
        if ((urlStr.length() > 1) && (arrSplit.length) > 1 && (arrSplit[1] != null)) {
            strAllParam = arrSplit[1];
        }
        return strAllParam;
    }

    public static Map<String, String> urlRequest(String urlStr) {
        Map<String, String> mapRequest = new HashMap<>();
        String strUrlParam = truncateUrlPage(urlStr);
        if (strUrlParam == null) {
            return mapRequest;
        }
        String[] arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = strSplit.split("[=]");
            if (arrSplitEqual.length > 1) {
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (!"".equals(arrSplitEqual[0])) {
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    public static List<String> getAllKey(Map<String, String> map) {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    public static String assembleUrl(String url, Map<String, String> params) {
        if (!url.endsWith("?")) {
            url += "?";
        }
        StringBuilder urlBuilder = new StringBuilder(url);
        for (String key : params.keySet()) {
            try {
                if (params.get(key) == null || params.get(key).length() == 0) {
                    urlBuilder.append(key).append("=").append(params.get(key)).append("&");
                } else {
                    urlBuilder.append(key).append("=").append(URLEncoder.encode(params.get(key), "UTF-8")).append("&");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        url = urlBuilder.toString();
        return url;
    }

    public static void main(String[] args) {
        String url = "https://www.test.com?username=aa&password=123456";
        System.out.println(urlPage(url));
        System.out.println(truncateUrlPage(url));
        System.out.println(urlRequest(url));
        Map<String, String> map = new HashMap<>(2);
        map.put("aa", "11");
        map.put("bb", "22");
        System.out.println(getAllKey(map));
    }
}
