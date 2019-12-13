package org.dorax.idx;

import org.apache.commons.lang3.StringUtils;
import org.dorax.codec.EncodeUtils;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 *
 * @author wuchunfu
 * @date 2019-12-09
 */
public class IdGenerate {
    private static SecureRandom random = new SecureRandom();

    /**
     * 生成UUID, 中间无-分割.
     */
    public static String uuid() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * 基于Base62编码的SecureRandom随机生成bytes.
     */
    public static String randomBase62(int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return EncodeUtils.encodeBase62(randomBytes);
    }

    /**
     * 使用SecureRandom随机生成指定范围的Integer.
     */
    public static int randomInt(int min, int max) {
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 获取新代码编号
     */
    public static String nextCode(String code) {
        if (code != null) {
            String str = code.trim();
            int lastNotNumIndex = -1;
            int len = str.length() - 1;
            for (int i = len; i >= 0; i--) {
                if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                    lastNotNumIndex = i;
                } else {
                    break;
                }
            }
            String prefix = str;
            String prevNum = "000";
            if (lastNotNumIndex != -1) {
                prefix = str.substring(0, lastNotNumIndex);
                prevNum = str.substring(lastNotNumIndex);
            }
            String nextNum = String.valueOf(Long.parseLong(prevNum) + 1);
            str = prefix + StringUtils.leftPad(nextNum, prevNum.length(), "0");
            return str;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(nextCode("8") + " = 9");
        System.out.println(nextCode("09") + " = 10");
        System.out.println(nextCode("009") + " = 010");
        System.out.println(nextCode("T09") + " = T10");
        System.out.println(nextCode("TG09") + " = TG10");
        System.out.println(nextCode("TG0101") + " = TG0102");
        System.out.println(nextCode("TG0109") + " = TG0110");
        System.out.println(nextCode("TG02T03") + " = TG02T04");
        System.out.println(nextCode("TG02T099") + " = TG02T100");
        System.out.println(nextCode("TG02T100") + " = TG02T101");
        System.out.println(nextCode("TG02T10A") + " = TG02T10A001");
        System.out.println(nextCode("1123117153417957377") + " = 1123117153417957379");
        System.out.println(nextCode("0040009") + " = 0040010");
        System.out.println(uuid());
        // 数值型ID重复验证测试
        Set<String> set = new HashSet<>();
        try {
            for (int i = 0; i < 100; i++) {
                String id = String.valueOf(uuid());
                if (set.contains(id)) {
                    throw new Exception(id + " exists");
                }
                set.add(id);
                System.out.println(id);
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
