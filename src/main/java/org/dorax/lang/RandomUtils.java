package org.dorax.lang;

import java.util.Random;

/**
 * Random 工具类
 *
 * @author wuchunfu
 * @date 2020-01-11
 */
public class RandomUtils {

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
