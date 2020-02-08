package org.dorax.io;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 验证字符串是否为正确路径名的正则
     */
    public static String matches = "[A-Za-z]:\\\\[^:?\"><*]*";
    public final static String CRLF = System.getProperty("line.separator");

    /**
     * 图片类型
     */
    private static List<String> imageList = Arrays.asList(
            ".bmp", ".jpg", ".jpeg", ".png", ".tiff",
            ".gif", ".pcx", ".tga", ".exif", ".fpx",
            ".svg", ".psd", ".cdr", ".pcd", ".dxf",
            ".ufo", ".eps", ".ai", ".raw", ".wmf"
    );

    /**
     * 视频类型
     */
    private static List<String> videoList = Arrays.asList(
            ".mp4", ".avi", ".mov", ".wmv", ".asf",
            ".navi", ".3gp", ".mkv", ".f4v", ".rmvb",
            ".webm"
    );

    /**
     * 音乐类型
     */
    private static List<String> musicList = Arrays.asList(
            ".mp3", ".wma", ".wav", ".mod", ".ra",
            ".cd", ".md", ".asf", ".aac", ".vqf",
            ".ape", ".mid", ".ogg", ".m4a", ".vqf"
    );

    /**
     * 文档类型
     */
    private static List<String> documentList = Arrays.asList(
            ".doc", ".docx", ".pdf", ".docm", ".dot",
            ".dotx", ".dotm", ".odt", ".fodt", ".rtf",
            ".txt", ".html", ".htm", ".jsp", ".mht",
            ".djvu", ".fb2", ".epub", ".xps", ".wpd"
    );

    /**
     * Spreadsheet 文件类型
     */
    private static List<String> spreadsheetList = Arrays.asList(
            ".xls", ".xlsx", ".xlsm", ".xlt", ".xltx",
            ".xltm", ".ods", ".fods", ".csv"
    );

    /**
     * Presentation 文件类型
     */
    private static List<String> presentationList = Arrays.asList(
            ".pps", ".ppsx", ".ppsm", ".ppt", ".pptx",
            ".pptm", ".pot", ".potx", ".potm", ".odp",
            ".fodp"
    );

    public enum FileType {
        /**
         * 1. Text
         * 2. Spreadsheet
         * 3. Presentation
         * 4. Image
         * 5. Video
         * 6. Music
         */
        Text,
        Spreadsheet,
        Presentation,
        Image,
        Video,
        Music
    }

    public static FileType getFileType(String fileName) {
        String ext = getExtension(fileName).toLowerCase();
        if (documentList.contains(ext)) {
            return FileType.Text;
        }
        if (spreadsheetList.contains(ext)) {
            return FileType.Spreadsheet;
        }
        if (presentationList.contains(ext)) {
            return FileType.Presentation;
        }
        if (imageList.contains(ext)) {
            return FileType.Image;
        }
        if (videoList.contains(ext)) {
            return FileType.Video;
        }
        if (musicList.contains(ext)) {
            return FileType.Music;
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

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 是否创建成功
     */
    public static boolean createFile(final Path path) {
        try {
            final Path parent = path.getParent();
            if (parent == null) {
                log.warn("Failed to create file as the parent was null. path: {}", path);
                return false;
            }
            Files.createDirectories(parent);
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            return true;
        } catch (final Exception e) {
            log.warn("createFile failed, path: {}", path, e);
            return false;
        }
    }

    /**
     * 从输入流中读取内容
     *
     * @param is 输入流对象
     * @return String 内容
     * @throws Exception Exception
     */
    public String readFromIS(InputStream is) throws Exception {
        try {
            StringBuilder strRtn = new StringBuilder();
            int length = is.available();
            byte[] buf = new byte[length];
            while ((is.read(buf, 0, length)) != -1) {
                strRtn.append(new String(buf, 0, length, StandardCharsets.UTF_8));
            }
            return strRtn.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            is.close();
        }
    }

    /**
     * 输入流转换为文件
     *
     * @param ins  输入流对象
     * @param file 文件
     * @return 文件
     */
    public static File inputStreamToFile(@NonNull InputStream ins, File file) {
        try (OutputStream os = new FileOutputStream(file)) {
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("IO异常", e);
        } finally {
            try {
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 拷贝文件
     *
     * @param formFile   被拷贝文件
     * @param toPathFile 拷贝文件的路径
     */
    public static void copyFile(File formFile, File toPathFile) {
        try (FileInputStream fi = new FileInputStream(formFile);
             FileOutputStream fo = new FileOutputStream(toPathFile);
             FileChannel in = fi.getChannel();
             FileChannel out = fo.getChannel()
        ) {
            // 连接两个通道，并且从in通道读取，然后写入out通道
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符串转文件
     *
     * @param fileContent
     * @param path
     * @throws IOException
     */
    public static void strToFile(String fileContent, String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        try (
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        ) {
            osw.write(fileContent);
        }
    }

    /**
     * inputStream 转 outputStream
     *
     * @param in inputStream
     * @return ByteArrayOutputStream
     * @throws Exception Exception
     */
    public static ByteArrayOutputStream inputStreamToOutputStream(InputStream in) throws Exception {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream;
    }

    /**
     * outputStream 转 inputStream
     *
     * @param out outputStream
     * @return ByteArrayInputStream
     */
    public static ByteArrayInputStream outputStreamToInputStream(OutputStream out) {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * inputStream 转 String
     *
     * @param in inputStream
     * @return String
     * @throws Exception Exception
     */
    public static String inputStreamToString(InputStream in) throws Exception {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream.toString();
    }

    /**
     * outputStream 转 String
     *
     * @param out outputStream
     * @return String
     */
    public static String outputStreamToString(OutputStream out) {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream.toString();
    }

    /**
     * String 转 inputStream
     *
     * @param str String
     * @return ByteArrayInputStream
     */
    public static ByteArrayInputStream stringToInputStream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    /**
     * String 转 outputStream
     *
     * @param str String
     * @return ByteArrayOutputStream
     * @throws Exception Exception
     */
    public static ByteArrayOutputStream parseOutputStream(String str) throws Exception {
        return inputStreamToOutputStream(stringToInputStream(str));
    }

    /**
     * 清空文件夹
     *
     * @param folderPath 文件家路径
     */
    public static void clearFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }
        String[] filePaths = folder.list();
        if (filePaths == null || filePaths.length == 0) {
            return;
        }
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isDirectory()) {
                    clearFolder(filePath);
                } else {
                    file.delete();
                }
            }
        }
    }

    /**
     * 清空文件和文件目录
     *
     * @param file 文件
     * @throws Exception Exception
     */
    public static void clean(File file) throws Exception {
        String[] cs = file.list();
        if (null == cs || cs.length <= 0) {
            log.info("delFile:[ {} ]", file);
            boolean isDelete = file.delete();
            if (!isDelete) {
                log.error("delFile:[ {} 文件删除失败！" + " ]", file.getName());
                throw new Exception(file.getName() + "文件删除失败！");
            }
        } else {
            for (String cn : cs) {
                String cp = file.getPath() + File.separator + cn;
                File f2 = new File(cp);
                if (f2.exists() && f2.isFile()) {
                    log.info("delFile:[ {} ]", f2);
                    boolean isDelete = f2.delete();
                    if (!isDelete) {
                        log.error("delFile:[ {} 文件删除失败！" + " ]", f2.getName());
                        throw new Exception(f2.getName() + "文件删除失败！");
                    }
                } else if (f2.exists() && f2.isDirectory()) {
                    clean(f2);
                }
            }
            log.info("delFile:[ {} ]", file);
            boolean isDelete = file.delete();
            if (!isDelete) {
                log.error("delFile:[ {} 文件删除失败！" + " ]", file.getName());
                throw new Exception(file.getName() + "文件删除失败！");
            }
        }
    }

    /**
     * 获得类的基路径，打成jar包也可以正确获得路径
     *
     * @return 文件路径
     */
    public static String getBasePath() {
        String filePath = FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (filePath.endsWith(".jar")) {
            filePath = filePath.substring(0, filePath.lastIndexOf("/"));
            try {
                // 解决路径中有空格%20的问题
                filePath = URLDecoder.decode(filePath, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        File file = new File(filePath);
        filePath = file.getAbsolutePath();
        return filePath;
    }

    /**
     * 获得类的基路径，打成jar包也可以正确获得路径
     *
     * @return 文件路径
     */
    public static String getClassPath() throws URISyntaxException {
        Path path = Paths.get(Objects.requireNonNull(FileUtils.class.getClassLoader().getResource("/")).toURI());
        return path.toAbsolutePath().toString();
    }

    /**
     * i/o进行读取文件
     *
     * @return fileContent读出的内容
     */
    public static String readFile(String filePath) {
        StringBuilder fileContent = new StringBuilder();
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                try (InputStreamReader read = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                     BufferedReader reader = new BufferedReader(read)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileContent.append(line).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }

    /**
     * i/o写入文件
     *
     * @param content   写入文件内容
     * @param writePath 要写入的文件名路径
     */
    public static void writeFile(String content, String writePath) {
        try {
            File file = new File(writePath);
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
                 BufferedWriter reader = new BufferedWriter(osw)) {
                reader.write(content);
                osw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * i/o写入文件
     *
     * @param content 写入文件内容
     * @param path    要写入的文件
     */
    public static void writeFile(String content, Path path) {
        try {
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(path.toFile()), StandardCharsets.UTF_8);
                 BufferedWriter reader = new BufferedWriter(osw)) {
                reader.write(content);
                osw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹
     *
     * @param path 　路径
     */
    public static void createFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 删除指定文件夹 如果文件夹里面存在文件夹就进行递归，删除规则是从里面开始先删除
     *
     * @param folderPath 文件夹路径
     */
    public static void delFolders(String folderPath) {
        // 删除完里面所有内容
        File file = new File(folderPath);
        // 如果路径本身就是一个文件就直接删除
        if (!file.isDirectory()) {
            file.delete();
            return;
        }
        // 检查文件夹里面是否存在文件夹
        File[] tempList = file.listFiles();
        if (tempList != null && tempList.length > 0) {
            for (File tmpFile : tempList) {
                if (tmpFile.isDirectory()) {
                    // 递归删除
                    delFolders(tmpFile.getPath());
                } else {
                    tmpFile.delete();
                }
            }
        } else {
            file.delete();
        }
        delFolders(file.getPath());
    }

    /**
     * 删除文件
     *
     * @param path 文件路径
     */
    public static void delFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 读文件到字节数组中
     *
     * @param filePath 文件路径
     */
    public static byte[] fileToByte(String filePath) {
        FileInputStream is = null;
        try {
            File file = new File(filePath);
            byte[] dist = null;
            if (file.exists()) {
                is = new FileInputStream(file);
                dist = new byte[is.available()];
                is.read(dist);
            }
            return dist;
        } catch (Exception e) {
            log.error("IO异常", e);
            return new byte[0];
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return 后缀名
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return 前缀名
     */
    public static String getFilePrefix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
