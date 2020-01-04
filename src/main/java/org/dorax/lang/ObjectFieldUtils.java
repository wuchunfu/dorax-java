package org.dorax.lang;

import java.lang.reflect.Field;

/**
 * 通过反射设置和获取字段的工具类
 *
 * @author wuchunfu
 * @date 2020-01-04
 */
public class ObjectFieldUtils {
    /**
     * 获取对象属性赋值
     *
     * @param dObject   object 对象
     * @param fieldName 字段别名
     * @return object 对象
     */
    public static Object getFieldValue(Object dObject, String fieldName) {
        Object result = null;
        try {
            // 获取对象的属性域
            Field fu = dObject.getClass().getDeclaredField(fieldName);
            try {
                // 设置对象属性域的访问属性
                fu.setAccessible(true);
                // 获取对象属性域的属性值
                result = fu.get(dObject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 给对象属性赋值
     *
     * @param dObject   object 对象
     * @param fieldName 字段别名
     * @param val       值
     * @return object 对象
     */
    public static Object setFieldValue(Object dObject, String fieldName, Object val) {
        Object result = null;
        try {
            // 获取对象的属性域
            Field fu = dObject.getClass().getDeclaredField(fieldName);
            try {
                // 设置对象属性域的访问属性
                fu.setAccessible(true);
                // 设置对象属性域的属性值
                fu.set(dObject, val);
                // 获取对象属性域的属性值
                result = fu.get(dObject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return result;
    }
}
