package org.dorax.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 文件操作
 *
 * @author wuchunfu
 * @date 2019-12-07
 */
public class FileUtils {

    /**
     * 验证字符串是否为正确路径名的正则
     */
    public static String matches = "[A-Za-z]:\\\\[^:?\"><*]*";
    public final static String CRLF = System.getProperty("line.separator");

    /**
     * 将文本文件中的内容读入到buffer中
     *
     * @param buffer   buffer
     * @param filePath 文件路径
     */
    public static void readToBuffer(StringBuffer buffer, String filePath) {
        InputStream is;
        try {
            is = new FileInputStream(filePath);
            // 用来保存每行读取的内容
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            // 读取第一行
            line = reader.readLine();
            // 如果 line 为空说明读完了
            while (line != null) {
                // 将读到的内容添加到 buffer 中
                buffer.append(line);
                // 添加换行符
                buffer.append(CRLF);
                // 读取下一行
                line = reader.readLine();
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取文本文件内容
     *
     * @param filePath 文件所在路径
     * @return 文本内容
     */
    public static String readText(String filePath) {
        StringBuffer sb = new StringBuffer();
        FileUtils.readToBuffer(sb, filePath);
        return sb.toString();
    }

    /**
     * 将文件内容写入文件
     *
     * @param filePath 文件路径
     * @param content  文件内容
     * @return 如果文件创建成功并成功写入则返回 true, 否则返回 false
     */
    public static boolean writeText(String filePath, String content) {
        File file = new File(filePath);
        try (FileOutputStream fop = new FileOutputStream(file)) {
            // if file doesn't exists, then create it
            if (!file.exists()) {
                return file.createNewFile();
            }
            // get the content in bytes
            byte[] contentInBytes = content.getBytes(StandardCharsets.UTF_8);
            fop.write(contentInBytes);
            fop.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取文件前缀
     *
     * @param file 文件
     * @return 返回文件前缀字符串
     */
    public static String getPrefix(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".") + 1;
        if (0 == index) {
            return "";
        } else {
            return fileName.substring(index);
        }
    }


    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 如何删除成功则返回 true, 否则返回 false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return 如果删除成功则返回 true, 否则返回 false
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 子文件的个数
     *
     * @param filePath 文件路径
     * @return 返回文件的个数
     */
    public static int countFiles(String filePath) {
        File folder = new File(filePath);
        if (folder.isDirectory()) {
            int num = 0;
            File[] flist = folder.listFiles();
            if (flist != null) {
                for (File file : flist) {
                    if (file.isFile()) {
                        num += 1;
                    }
                }
            }
            return num;
        } else {
            return 0;
        }
    }
}
