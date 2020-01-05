package org.dorax.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件下载及压缩
 *
 * @author wuchunfu
 * @date 2020-01-04
 */
public class FileZipAndDownload {

    private static Logger log = LoggerFactory.getLogger(FileZipAndDownload.class);

    /**
     * 下载及压缩所有请求的文件
     *
     * @param urlList  url 路径
     * @param filePath 文件路径
     * @param outFile  输出路径
     */
    public void downloadAndZipFile(List<String> urlList, String filePath, File outFile) {
        File path = new File(filePath);
        if (path.exists()) {
            deleteAllFilesOfDir(path);
        }
        if (!path.exists()) {
            path.mkdirs();
        }
        String absolutePath = path.getAbsolutePath() + File.separator;
        downloadFiles(urlList, absolutePath);
        File[] files = new File(absolutePath).listFiles();
        if (files != null) {
            zipFiles(files, outFile);
        }
    }

    /**
     * 下载所有请求的文件
     *
     * @param urlList  url 列表
     * @param filePath 文件路径
     */
    public static void downloadFiles(List<String> urlList, String filePath) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        for (String urlStr : urlList) {
            try {
                URL url = new URL(urlStr);
                File file = new File(urlStr);
                String fileName = URLDecoder.decode(file.getName(), "UTF-8");
                File targetFilePath = new File(filePath + fileName);
                targetFilePath.setWritable(true, false);
                DataInputStream dis = new DataInputStream(url.openStream());
                bis = new BufferedInputStream(dis);
                FileOutputStream fos = new FileOutputStream(targetFilePath);
                bos = new BufferedOutputStream(fos);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                    bos.flush();
                }

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件夹下所有文件
     *
     * @param filePath 文件路径
     */
    public static void deleteAllFilesOfDir(File filePath) {
        if (null != filePath) {
            if (!filePath.exists()) {
                return;
            }
            if (filePath.isFile()) {
                boolean result = filePath.delete();
                int tryCount = 0;
                while (!result && tryCount++ < 10) {
                    System.gc();
                    result = filePath.delete();
                }
            }
            File[] files = filePath.listFiles();
            if (null != files) {
                for (File file : files) {
                    deleteAllFilesOfDir(file);
                }
            }
            filePath.delete();
        }
    }

    /**
     * 压缩文件夹下面所有文件
     *
     * @param inFiles 输入路径
     * @param outFile 输出路径
     */
    public static void zipFiles(File[] inFiles, File outFile) {
        BufferedInputStream bis = null;
        ZipOutputStream zos = null;
        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            zos = new ZipOutputStream(bos, StandardCharsets.UTF_8);
            for (File inFile : inFiles) {
                FileInputStream fis = new FileInputStream(inFile);
                bis = new BufferedInputStream(fis);
                ZipEntry ze = new ZipEntry(inFile.getName());
                zos.putNextEntry(ze);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bis.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                    zos.flush();
                }
                zos.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
