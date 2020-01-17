package org.dorax.utils;

import java.util.Arrays;

/**
 * Row Key 工具类
 *
 * @author wuchunfu
 * @date 2020-01-14
 */
public class RowKeyUtils {
    private static final byte[] EMPTY_KEY = new byte[0];

    /**
     * When scanning for a prefix the scan should stop immediately after the the last row that has the
     * specified prefix. This method calculates the closest next rowKey immediately following the
     * given rowKeyPrefix.
     *
     * <p><b>IMPORTANT: This converts a rowKey<u>Prefix</u> into a rowKey</b>.
     *
     * <p>If the prefix is an 'ASCII' string put into a byte[] then this is easy because you can
     * simply increment the last byte of the array. But if your application uses real binary rowids
     * you may run into the scenario that your prefix is something like: &nbsp;&nbsp;&nbsp;<b>{ 0x12,
     * 0x23, 0xFF, 0xFF }</b><br>
     * Then this stopRow needs to be fed into the actual scan<br>
     * &nbsp;&nbsp;&nbsp;<b>{ 0x12, 0x24 }</b> (Notice that it is shorter now)<br>
     * This method calculates the correct stop row value for this usecase.
     *
     * @param rowKeyPrefix the rowKey<u>Prefix</u>.
     * @return the closest next rowKey immediately following the given rowKeyPrefix.
     */
    public static byte[] calculateTheClosestNextRowKeyForPrefix(byte[] rowKeyPrefix) {
        // Essentially we are treating it like an 'unsigned very very long' and doing +1 manually.
        // Search for the place where the trailing 0xFFs start
        int offset = rowKeyPrefix.length;
        while (offset > 0) {
            if (rowKeyPrefix[offset - 1] != (byte) 0xFF) {
                break;
            }
            offset--;
        }
        if (offset == 0) {
            // We got an 0xFFFF... (only FFs) stopRow value which is
            // the last possible prefix before the end of the table.
            // So set it to stop at the 'end of the table'
            return EMPTY_KEY;
        }
        // Copy the right length of the original
        byte[] newStopRow = Arrays.copyOfRange(rowKeyPrefix, 0, offset);
        // And increment the last one
        newStopRow[newStopRow.length - 1]++;
        return newStopRow;
    }
}
