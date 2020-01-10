package org.dorax.idx;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * UUID 工具类
 *
 * @author wuchunfu
 * @date 2020-01-10
 */
public class UUIDUtils {

    private UUIDUtils() {
    }

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public static String generateUuid(String hint) {
        UUID uuid = UUID.nameUUIDFromBytes(hint.getBytes(StandardCharsets.UTF_8));
        return uuid.toString();
    }

    public static boolean isValidUuid(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
