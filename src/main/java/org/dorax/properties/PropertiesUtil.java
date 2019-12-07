package org.dorax.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public static void main(String[] args) {
        System.out.println(readProperty("config.properties", "username"));
        Properties properties = getProperties("config.properties");
        System.out.println(properties.getProperty("username"));
    }
}
