package org.dorax.network;

import com.google.common.base.Joiner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 敏感信息过滤工具类.
 *
 * @author wuchunfu
 * @date 2020-01-08
 */
public class SensitiveInfoUtils {

    private static final String FAKE_IP_SAMPLE = "ip";

    /**
     * 屏蔽替换IP地址敏感信息.
     *
     * @param target 待替换敏感信息的字符串列表
     * @return 替换敏感信息后的字符串列表
     */
    public static List<String> filterSensitiveIps(final List<String> target) {
        final Map<String, String> fakeIpMap = new HashMap<>();
        final AtomicInteger step = new AtomicInteger();
        return target.stream().map(input -> {
            Matcher matcher = Pattern.compile(RealIpUtils.IP_REGEX).matcher(input);
            String result = input;
            while (matcher.find()) {
                String realIp = matcher.group();
                String fakeIp;
                if (fakeIpMap.containsKey(realIp)) {
                    fakeIp = fakeIpMap.get(realIp);
                } else {
                    fakeIp = Joiner.on("").join(FAKE_IP_SAMPLE, step.incrementAndGet());
                    fakeIpMap.put(realIp, fakeIp);
                }
                result = result.replace(realIp, fakeIp);
            }
            return result;
        }).collect(Collectors.toList());
    }
}
