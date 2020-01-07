package org.dorax.io;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    private static List<String> ExtsDocument = Arrays.asList(
            ".doc", ".docx", ".docm",
            ".dot", ".dotx", ".dotm",
            ".odt", ".fodt", ".rtf", ".txt",
            ".html", ".htm", ".mht",
            ".pdf", ".djvu", ".fb2", ".epub", ".xps");

    private static List<String> ExtsSpreadsheet = Arrays.asList(
            ".xls", ".xlsx", ".xlsm",
            ".xlt", ".xltx", ".xltm",
            ".ods", ".fods", ".csv");

    private static List<String> ExtsPresentation = Arrays.asList(
            ".pps", ".ppsx", ".ppsm",
            ".ppt", ".pptx", ".pptm",
            ".pot", ".potx", ".potm",
            ".odp", ".fodp");

    public enum FileType {
        /**
         * 1. text
         * 2. Spreadsheet
         * 3. Presentation
         */
        Text,
        Spreadsheet,
        Presentation
    }

    public static FileType getFileType(String fileName) {
        String ext = getExtension(fileName).toLowerCase();
        if (ExtsDocument.contains(ext)) {
            return FileType.Text;
        }
        if (ExtsSpreadsheet.contains(ext)) {
            return FileType.Spreadsheet;
        }
        if (ExtsPresentation.contains(ext)) {
            return FileType.Presentation;
        }
        return FileType.Text;
    }

    /**
     * 获取扩展名
     *
     * @param fileName 文件名称
     * @return 扩展名
     */
    public static String getExtension(String fileName) {
        if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, ".")) {
            return StringUtils.EMPTY;
        }
        String ext = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, "."));
        return StringUtils.trimToEmpty(ext);
    }

    /**
     * 获取文件名
     *
     * @param header header
     * @return 文件名称
     */
    public static String getFileName(String header) {
        String[] tempArr1 = header.split(";");
        String[] tempArr2 = tempArr1[2].split("=");
        // 获取文件名，兼容各种浏览器的写法
        return tempArr2[1].substring(tempArr2[1].lastIndexOf("\\") + 1).replaceAll("\"", "");
    }

    /**
     * 获取路径权限
     *
     * @param path 路径
     * @return 权限代码
     * @throws IOException IOException
     */
    public static String getPermissions(Path path) throws IOException {
        PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(path, PosixFileAttributeView.class);
        PosixFileAttributes readAttributes = fileAttributeView.readAttributes();
        Set<PosixFilePermission> permissions = readAttributes.permissions();
        return PosixFilePermissions.toString(permissions);
    }

    /**
     * 设置文件权限
     *
     * @param file      文件
     * @param permsCode 权限代码
     * @param recursive 是否是递归的
     * @return 权限代码
     * @throws IOException IOException
     */
    public static String setPermissions(File file, String permsCode, boolean recursive) throws IOException {
        PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(file.toPath(), PosixFileAttributeView.class);
        fileAttributeView.setPermissions(PosixFilePermissions.fromString(permsCode));
        if (file.isDirectory() && recursive && file.listFiles() != null) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                setPermissions(f, permsCode, true);
            }
        }
        return permsCode;
    }

    /**
     * 创建文件夹
     *
     * @param fileFolderName 文件夹名称
     */
    public static void mkFolder(String fileFolderName) {
        File file = new File(fileFolderName);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 创建文件
     *
     * @param fileName 文件名称
     * @return 文件
     */
    public static File mkFile(String fileName) {
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

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
