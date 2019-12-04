package org.dorax.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 获取系统环境变量工具类
 *
 * @author wcf
 * @date 2019-12-10
 */
public class EnvUtil {
    private static final Logger logger = LoggerFactory.getLogger(EnvUtil.class);

    /**
     * 判断当前系统是否为 linux
     *
     * @return true linux, false windows
     */
    public static boolean isLinux() {
        return OperatingSystem.get() != OperatingSystem.OS.WINDOWS;
    }

    /**
     * 返回当前系统变量的函数 结果放至 Properties
     */
    public static Properties getEnv() {
        Properties prop = new Properties();
        try {
            Process p;
            if (isLinux()) {
                p = Runtime.getRuntime().exec("sh -c set");
            } else {
                // windows
                p = Runtime.getRuntime().exec("cmd /c set");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                int i = line.indexOf("=");
                if (i > -1) {
                    String key = line.substring(0, i);
                    String value = line.substring(i + 1);
                    prop.setProperty(key, value);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static void main(String[] args) {
        System.out.println(getEnv());
    }
}
