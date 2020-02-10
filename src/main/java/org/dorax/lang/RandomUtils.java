package org.dorax.lang;

import java.util.Random;

/**
 * 随机数工具类
 *
 * @author wuchunfu
 * @date 2020-02-05
 */
public class RandomUtils {

    private static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTER_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER_CHAR = "0123456789";

    /**
     * 获取定长的随机数，包含大小写、数字
     *
     * @param length 随机数长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 获取定长的随机数，包含大小写字母
     *
     * @param length 随机数长度
     * @return 随机字符串
     */
    public static String generateMixString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(LETTER_CHAR.charAt(random.nextInt(LETTER_CHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 获取定长的随机数，只包含小写字母
     *
     * @param length 随机数长度
     * @return 随机字符串
     */
    public static String generateLowerString(int length) {
        return generateMixString(length).toLowerCase();
    }

    /**
     * 获取定长的随机数，只包含大写字母
     *
     * @param length 随机数长度
     * @return 随机字符串
     */
    public static String generateUpperString(int length) {
        return generateMixString(length).toUpperCase();
    }

    /**
     * 获取定长的随机数，只包含数字
     *
     * @param length 随机数长度
     * @return 随机字符串
     */
    public static String generateNumberString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBER_CHAR.charAt(random.nextInt(NUMBER_CHAR.length())));
        }
        return sb.toString();
    }

    protected static final Random RANDOM = new Random();

    public static Random getRandom() {
        return RANDOM;
    }

    public static String randomString() {
        return java.util.UUID.randomUUID().toString();
    }

    public static char randomChar() {
        return RandomUtils.randomString().charAt(0);
    }

    public static long randomLong() {
        return RandomUtils.RANDOM.nextLong();
    }

    public static long randomPositiveLong() {
        return Math.abs(RandomUtils.randomLong());
    }

    public static int randomInt() {
        return RandomUtils.RANDOM.nextInt();
    }

    public static int randomPositiveInt() {
        return Math.abs(RandomUtils.randomInt());
    }

    public static int randomInterval(final int min, final int max) {
        return min + RANDOM.nextInt(max - min);
    }

    public static int randomMax(final int max) {
        int value = randomPositiveInt() % max;

        if (value == 0) {
            value = max;
        }

        return value;
    }

    public static int randomPort() {
        return RandomUtils.RANDOM.nextInt(65536);
    }

    public static short randomShort() {
        return (short) RandomUtils.RANDOM.nextInt(Short.MAX_VALUE);
    }

    public static byte randomByte() {
        return Integer.valueOf(RandomUtils.RANDOM.nextInt()).byteValue();
    }

    public static boolean randomBoolean() {
        return RandomUtils.RANDOM.nextBoolean();
    }

    public static byte[] randomBytes() {
        return RandomUtils.randomString().getBytes();
    }

    public static byte[] randomBytes(final int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = RandomUtils.randomByte();
        }
        return bytes;
    }

    public static double randomDouble() {
        return RandomUtils.RANDOM.nextDouble();
    }

    public static float randomFloat() {
        return RandomUtils.RANDOM.nextFloat();
    }
}
