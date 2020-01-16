package org.dorax.utils;

import static org.dorax.utils.OperatingSystem.OS.MAC;
import static org.dorax.utils.OperatingSystem.OS.UNIX;
import static org.dorax.utils.OperatingSystem.OS.WINDOWS;

/**
 * 操作系统判断
 *
 * @author wcf
 * @date 2019-12-10
 */
public class OperatingSystem {

    public enum OS {
        /**
         * windows os
         */
        WINDOWS,
        /**
         * mac os
         */
        MAC,
        /**
         * unix os
         */
        UNIX
    }

    private static final OS OS = get(System.getProperty("os.name", "").toLowerCase());

    public static OS get() {
        return OS;
    }

    private static OS get(String osName) {
        if (isWindows(osName)) {
            return WINDOWS;
        } else if (isMac(osName)) {
            return MAC;
        } else if (isUnix(osName)) {
            return UNIX;
        }
        return null;
    }

    public static boolean isWindows(String osName) {
        return (osName.contains("win"));
    }

    public static boolean isMac(String osName) {
        return (osName.contains("mac"));
    }

    public static boolean isUnix(String osName) {
        return (osName.contains("nix") || osName.contains("nux") || osName.contains("aix"));
    }

    public static void main(String[] args) {
        System.out.println(get());
    }
}
