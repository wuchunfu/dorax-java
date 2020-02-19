package org.dorax.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author wuchunfu
 * @date 2020-02-08
 */
public class GzipUtils {
    /**
     * Compresses a byte array by applying GZIP compression
     *
     * @param buffer byte array buffer
     * @return byte array
     * @throws IOException IOException
     */
    public static byte[] compress(byte[] buffer) throws IOException {
        ByteArrayOutputStream gzipByteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream;
        gzipOutputStream = new GZIPOutputStream(gzipByteArrayOutputStream);
        gzipOutputStream.write(buffer, 0, buffer.length);
        gzipOutputStream.close();
        return gzipByteArrayOutputStream.toByteArray();
    }

    /**
     * Uncompresses a byte array by applying GZIP uncompression
     *
     * @param bais ByteArrayInputStream
     * @return byte array
     * @throws IOException IOException
     */
    public static byte[] uncompress(ByteArrayInputStream bais) throws IOException {
        GZIPInputStream gzipInputStream = new GZIPInputStream(bais);
        byte[] buffer = new byte[1024];
        int length;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((length = gzipInputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        gzipInputStream.close();
        baos.close();
        return baos.toByteArray();
    }

    /**
     * Determines if a byte array is compressed. The java.util.zip GZip
     * implementaiton does not expose the GZip header so it is difficult to determine
     * if a string is compressed.
     *
     * @param bytes an array of bytes
     * @return true if the array is compressed or false otherwise
     */
    public static boolean isCompressed(byte[] bytes) {
        if ((bytes == null) || (bytes.length < 2)) {
            return false;
        } else {
            return ((bytes[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (bytes[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8)));
        }
    }
}
