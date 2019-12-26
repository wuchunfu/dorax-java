package org.dorax.lang;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 参数校验工具类
 *
 * @author wuchunfu
 * @date 2019-12-26
 */
public class ValidateUtils {

    /**
     * 对象是否不为空
     *
     * @param o 对象
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 对象是否为空
     *
     * @param o 对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            return "".equals(o.toString().trim());
        } else if (o instanceof List) {
            return ((List) o).size() == 0;
        } else if (o instanceof Map) {
            return ((Map) o).size() == 0;
        } else if (o instanceof Set) {
            return ((Set) o).size() == 0;
        } else if (o instanceof Object[]) {
            return ((Object[]) o).length == 0;
        } else if (o instanceof int[]) {
            return ((int[]) o).length == 0;
        } else if (o instanceof long[]) {
            return ((long[]) o).length == 0;
        }
        return false;
    }

    /**
     * 对象组中是否存在空对象
     *
     * @param os 对象数组
     * @return 是否存在空对象
     */
    public static boolean isOneEmpty(Object... os) {
        for (Object o : os) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象组中是否全是空对象
     *
     * @param os 对象数组
     * @return 是否存在空对象
     */
    public static boolean isAllEmpty(Object... os) {
        for (Object o : os) {
            if (!isEmpty(o)) {
                return false;
            }
        }
        return true;
    }
}
