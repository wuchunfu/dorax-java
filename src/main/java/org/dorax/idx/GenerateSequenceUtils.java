package org.dorax.idx;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 根据时间生成唯一序列ID
 * 时间精确到秒，ID最大值为99999且循环使用
 *
 * @author wuchunfu
 * @date 2020-02-05
 */
public class GenerateSequenceUtils {
    private static final FieldPosition HELPER_POSITION = new FieldPosition(0);

    /**
     * 时间：精确到秒
     */
    private final static Format DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private final static NumberFormat NUMBER_FORMAT = new DecimalFormat("00000");

    private static int seq = 0;

    private static final int MAX = 99999;

    public static synchronized String generateSequenceNo() {
        Calendar rightNow = Calendar.getInstance();
        StringBuffer sb = new StringBuffer();
        DATE_FORMAT.format(rightNow.getTime(), sb, HELPER_POSITION);
        NUMBER_FORMAT.format(seq, sb, HELPER_POSITION);
        if (seq == MAX) {
            seq = 0;
        } else {
            seq++;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(generateSequenceNo());
    }
}
