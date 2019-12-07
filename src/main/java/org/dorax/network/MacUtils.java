package org.dorax.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * MAC地址工具
 *
 * @author wuchunfu
 * @date 2019-12-07
 */
public class MacUtils {

    /**
     * 获取当前操作系统名称. return 操作系统名称 例如:windows,Linux,Unix等.
     */
    public static String getOsName() {
        return System.getProperty("os.name").toLowerCase();
    }

    /**
     * 获取Unix网卡的mac地址.
     *
     * @return mac地址
     */
    public static String getUnixMacAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process;
        try {
            // Unix下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
            process = Runtime.getRuntime().exec("ifconfig eth0");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int index;
            while ((line = bufferedReader.readLine()) != null) {
                // 寻找标示字符串[hwaddr]
                index = line.toLowerCase().indexOf("hwaddr");
                // 找到了
                if (index != -1) {
                    // 取出mac地址并去除2边空格
                    mac = line.substring(index + "hwaddr".length() + 1).trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return mac;
    }

    /**
     * 获取Linux网卡的mac地址.
     *
     * @return mac地址
     */
    public static String getLinuxMacAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process;
        try {
            // linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
            process = Runtime.getRuntime().exec("ifconfig eth0");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int index;
            while ((line = bufferedReader.readLine()) != null) {
                index = line.toLowerCase().indexOf("硬件地址");
                // 找到了
                if (index != -1) {
                    // 取出mac地址并去除2边空格
                    mac = line.substring(index + 4).trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        // 取不到，试下Unix取发
        if (mac == null) {
            return getUnixMacAddress();
        }
        return mac;
    }

    /**
     * 获取widnows网卡的mac地址.
     *
     * @return mac地址
     */
    public static String getWindowsMacAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process;
        try {
            // windows下的命令，显示信息中包含有mac地址信息
            process = Runtime.getRuntime().exec("ipconfig /all");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int index;
            while ((line = bufferedReader.readLine()) != null) {
                // 寻找标示字符串[physical address 或  物理地址]
                if (line.split("-").length == 6) {
                    index = line.indexOf(":");
                    if (index != -1) {
                        // 取出mac地址并去除2边空格
                        mac = line.substring(index + 1).trim();
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return mac;
    }

    public static String getMac() {
        String os = getOsName();
        String mac;
        if (os.startsWith("windows")) {
            mac = getWindowsMacAddress();
        } else if (os.startsWith("linux")) {
            mac = getLinuxMacAddress();
        } else {
            mac = getUnixMacAddress();
        }
        return mac == null ? "" : mac;
    }

    /**
     * 测试用的main方法.
     *
     * @param args 运行参数.
     */
    public static void main(String[] args) {
        String os = getOsName();
        System.out.println("os: " + os);
        if (os.startsWith("windows")) {
            String mac = getWindowsMacAddress();
            System.out.println("windows mac: " + mac);
        } else if (os.startsWith("linux")) {
            String mac = getLinuxMacAddress();
            System.out.println("linux mac: " + mac);
        } else {
            String mac = getUnixMacAddress();
            System.out.println("unix mac: " + mac);
        }
    }
}
