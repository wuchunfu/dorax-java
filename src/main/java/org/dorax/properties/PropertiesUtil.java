package org.dorax.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Properties;

/**
 * 配置文件工具类
 *
 * @author wuchunfu
 * @date 2019-12-06
 */
public class PropertiesUtil {

    public PropertiesUtil() {

    }

    public static String readProperty(String propertiesName, String key) {
        String value = "";
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesName);
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static Properties getProperties(String propertiesName) {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesName);
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    public static void writeProperty(String propertiesName, String key, String value) {
        InputStream is = null;
        OutputStream os = null;
        Properties prop = new Properties();
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesName);
            prop.load(is);
            os = new FileOutputStream(Objects.requireNonNull(PropertiesUtil.class.getClassLoader().getResource(propertiesName)).getFile());
            prop.setProperty(key, value);
            prop.store(os, key);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeProperties(String realPath, String key, String value) {
        Properties prop = new Properties();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = new FileInputStream(realPath);
            prop.load(is);
            prop.setProperty(key, value);
            // 文件输出流
            fos = new FileOutputStream(realPath);
            // 将Properties集合保存到流中
            prop.store(fos, "update for " + key);
            is.close();
            fos.close();//关闭流
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readValue(String realPath, String key) {
        Properties prop = new Properties();
        InputStream is = null;
        String value = "";
        try {
            is = new FileInputStream(realPath);
            prop.load(is);
            value = prop.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static String read(String fileName, String key) {
        try {
            Properties props = new Properties();
            String basePath = Objects.requireNonNull(PropertiesUtil.class.getClassLoader().getResource("/")).getPath();
            File config = new File(basePath + fileName);
            InputStream in = new FileInputStream(config);
            props.load(in);
            return (String) props.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 取出Property，但以System的Property优先,取不到返回空字符串.
     *
     * @param propertiesName 配置文件名称
     * @param key            key 值
     * @return 返回 string 类型的值
     */
    private String getValue(String propertiesName, String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }
        Properties properties = getProperties(propertiesName);
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return "";
    }

    /**
     * 取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.
     *
     * @param propertiesName 配置文件名称
     * @param key            key 值
     * @param defaultValue   默认值
     * @return 返回 string 类型的值
     */
    public String getProperty(String propertiesName, String key, String defaultValue) {
        String value = getValue(propertiesName, key);
        return value != null ? value : defaultValue;
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     *
     * @param propertiesName 配置文件名称
     * @param key            key 值
     * @return 返回 int 类型的值
     */
    public Integer getInteger(String propertiesName, String key) {
        String value = getValue(propertiesName, key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Integer.valueOf(value);
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     *
     * @param propertiesName 配置文件名称
     * @param key            key 值
     * @param defaultValue   默认值
     * @return 返回 int 类型的值
     */
    public Integer getInteger(String propertiesName, String key, Integer defaultValue) {
        String value = getValue(propertiesName, key);
        return value != null ? Integer.valueOf(value) : defaultValue;
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     *
     * @param propertiesName 配置文件名称
     * @param key            key 值
     * @return 返回 double 类型的值
     */
    public Double getDouble(String propertiesName, String key) {
        String value = getValue(propertiesName, key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Double.valueOf(value);
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     *
     * @param propertiesName 配置文件名称
     * @param key            key 值
     * @param defaultValue   默认值
     * @return 返回 double 类型的值
     */
    public Double getDouble(String propertiesName, String key, Integer defaultValue) {
        String value = getValue(propertiesName, key);
        return value != null ? Double.parseDouble(value) : defaultValue;
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null抛出异常,如果内容不是true/false则返回false.
     *
     * @param propertiesName 配置文件名称
     * @param key            key 值
     * @return 如果内容为真则返回 true，否则返回 false
     */
    public Boolean getBoolean(String propertiesName, String key) {
        String value = getValue(propertiesName, key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Boolean.valueOf(value);
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null则返回Default值,如果内容不为true/false则返回false.
     *
     * @param propertiesName 配置文件名称
     * @param key            key 值
     * @param defaultValue   默认值
     * @return 如果内容为真则返回 true，否则返回 false
     */
    public Boolean getBoolean(String propertiesName, String key, boolean defaultValue) {
        String value = getValue(propertiesName, key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public static void main(String[] args) {
        System.out.println(readProperty("config.properties", "username"));
        Properties properties = getProperties("config.properties");
        System.out.println(properties.getProperty("username"));
    }
}
