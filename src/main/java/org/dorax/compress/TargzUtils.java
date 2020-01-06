package org.dorax.compress;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author wuchunfu
 * @date 2020-01-06
 */
public class TargzUtils {

    /**
     * 解压.tar文件
     *
     * @param srcFile  压缩文件
     * @param destPath 解压目录
     * @throws Exception Exception
     */
    public static void unTarFile(File srcFile, String destPath) throws Exception {
        try (TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(srcFile))) {
            deArchive(new File(destPath), tarArchiveInputStream);
        }
    }

    /**
     * 解压tar.gz
     *
     * @param gzFile  压缩文件
     * @param descDir 解压目录
     * @throws Exception Exception
     */
    public static void unTarGzFile(File gzFile, String descDir) throws Exception {
        GZIPInputStream inputStream = new GZIPInputStream((new FileInputStream(gzFile)));
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        deArchive(new File(descDir), tarArchiveInputStream);
    }

    /**
     * 解压缩
     *
     * @param destFile              目标目录
     * @param tarArchiveInputStream 压缩文件输入
     * @throws Exception Exception
     */
    private static void deArchive(File destFile, TarArchiveInputStream tarArchiveInputStream) throws Exception {
        TarArchiveEntry entry;
        while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
            // 文件
            String dir = destFile.getPath() + File.separator + entry.getName();
            File dirFile = new File(dir);
            // 文件检查
            fileProber(dirFile);
            if (entry.isDirectory()) {
                dirFile.mkdirs();
            } else {
                deArchiveFile(dirFile, tarArchiveInputStream);
            }
        }
    }

    /**
     * 解压缩文件
     *
     * @param destFile              目标目录
     * @param tarArchiveInputStream 压缩文件输入
     * @throws Exception Exception
     */
    private static void deArchiveFile(File destFile, TarArchiveInputStream tarArchiveInputStream) throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
        int count;
        byte[] data = new byte[1024];
        while ((count = tarArchiveInputStream.read(data, 0, 1024)) != -1) {
            bos.write(data, 0, count);
        }
        bos.close();
    }

    /**
     * 文件检查
     *
     * @param dirFile 文件
     */
    public static void fileProber(File dirFile) {
        File parentFile = dirFile.getParentFile();
        if (!parentFile.exists()) {
            // 递归寻找上级目录
            fileProber(parentFile);
            parentFile.mkdir();
        }
    }
}
