package org.dorax.properties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 资源文件读取工具
 *
 * @author wuchunfu
 * @date 2019-12-27
 */
public class PropertiesFileUtils {

    /**
     * 当打开多个资源文件时，缓存资源文件
     */
    private static Map<String, PropertiesFileUtils> configMap = new HashMap<>();
    /**
     * 打开文件时间，判断超时使用
     */
    private Date loadTime;
    /**
     * 资源文件
     */
    private ResourceBundle resourceBundle;
    /**
     * 默认资源文件名称
     */
    private static final String NAME = "config";
    /**
     * 缓存时间
     */
    private static final Integer TIME_OUT = 60 * 1000;

    /**
     * 私有构造方法，创建单例
     *
     * @param name 配置文件名称
     */
    private PropertiesFileUtils(String name) {
        this.loadTime = new Date();
        this.resourceBundle = ResourceBundle.getBundle(name);
    }

    /**
     * 获取实例
     *
     * @return 实例对象
     */
    public static synchronized PropertiesFileUtils getInstance() {
        return getInstance(NAME);
    }

    /**
     * 获取实例
     *
     * @param name 配置文件名称
     * @return 实例对象
     */
    public static synchronized PropertiesFileUtils getInstance(String name) {
        PropertiesFileUtils conf = configMap.get(name);
        if (null == conf) {
            conf = new PropertiesFileUtils(name);
            configMap.put(name, conf);
        }
        // 判断是否打开的资源文件是否超时1分钟
        if ((System.currentTimeMillis() - conf.getLoadTime().getTime()) > TIME_OUT) {
            conf = new PropertiesFileUtils(name);
            configMap.put(name, conf);
        }
        return conf;
    }

    /**
     * 根据key读取value
     *
     * @param key 键
     * @return string 类型的值
     */
    public String getString(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return "";
        }
    }

    /**
     * 根据key读取value(整型)
     *
     * @param key 键
     * @return int 类型的值
     */
    public Integer getInt(String key) {
        try {
            String value = resourceBundle.getString(key);
            return Integer.parseInt(value);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * 根据key读取value(布尔)
     *
     * @param key 键
     * @return 布尔 类型的值
     */
    public boolean getBool(String key) {
        try {
            String value = resourceBundle.getString(key);
            return "true".equals(value);
        } catch (MissingResourceException e) {
            return false;
        }
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public Date getLoadTime() {
        return loadTime;
    }
}
