package org.dorax.utils;

import com.google.common.base.Splitter;

import java.util.List;

/**
 * @author wcf
 * @date 2019-12-10
 */
public class VersionUtil {

    private final static Splitter SNAPSHOT_SPLITTER = Splitter.on("-").trimResults();
    private final static Splitter POINT_SPLITTER = Splitter.on(".").trimResults();
    private final static int VERSION_LENGTH = 3;

    /**
     * source < target false
     * source >= target true
     *
     * @param source source file path
     * @param target target file path
     * @return true or false
     */
    public static boolean greaterEqualThanVersion(String source, String target) {
        source = SNAPSHOT_SPLITTER.split(source).iterator().next();
        target = SNAPSHOT_SPLITTER.split(target).iterator().next();
        List<String> sourceList = POINT_SPLITTER.splitToList(source);
        List<String> targetList = POINT_SPLITTER.splitToList(target);

        if (sourceList.size() != VERSION_LENGTH || targetList.size() != VERSION_LENGTH) {
            return false;
        }
        int result = Integer.parseInt(sourceList.get(0)) - Integer.parseInt(targetList.get(0));
        if (result < 0) {
            return false;
        } else if (result > 0) {
            return true;
        }
        result = Integer.parseInt(sourceList.get(1)) - Integer.parseInt(targetList.get(1));
        if (result < 0) {
            return false;
        } else if (result > 0) {
            return true;
        }
        result = Integer.parseInt(sourceList.get(2)) - Integer.parseInt(targetList.get(2));
        return result >= 0;
    }
}
