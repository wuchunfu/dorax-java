package org.dorax.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

/**
 * 对象操作工具类
 *
 * @author wuchunfu
 * @date 2019-12-13
 */
public class ObjectUtils {

    /**
     * 注解到对象复制，只复制能匹配上的方法。
     *
     * @param annotation annotation 对象
     * @param object     object 对象
     */
    public static void annotationToObject(Object annotation, Object object) {
        if (annotation != null) {
            Class<?> annotationClass = annotation.getClass();
            if (null == object) {
                return;
            }
            Class<?> objectClass = object.getClass();
            for (Method m : objectClass.getMethods()) {
                if (StringUtils.startsWith(m.getName(), "set")) {
                    try {
                        String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
                        Object obj = annotationClass.getMethod(s).invoke(annotation);
                        if (obj != null && !"".equals(obj.toString())) {
                            m.invoke(object, obj);
                        }
                    } catch (Exception e) {
                        // 忽略所有设置失败方法
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 序列化对象
     *
     * @param object object 对象
     * @return 字节数组
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos;
        ByteArrayOutputStream baos;
        try {
            if (object != null) {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                return baos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化对象
     *
     * @param bytes 字节数组
     * @return object 对象
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais;
        try {
            if (bytes != null && bytes.length > 0) {
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
