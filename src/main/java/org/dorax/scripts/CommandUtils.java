package org.dorax.scripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 命令行工具类
 *
 * @author wuchunfu
 * @date 2019-12-13
 */
public class CommandUtils {

    /**
     * 执行命令
     *
     * @param command 命令字符串
     * @return 执行结果
     * @throws IOException io 异常
     */
    public static String execute(String command) throws IOException {
        return execute(command, "UTF-8");
    }

    /**
     * 执行命令
     *
     * @param command     命令字符串
     * @param charsetName 字符集
     * @return 自行结果
     * @throws IOException io 异常
     */
    public static String execute(String command, String charsetName) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        // 记录dos命令的返回信息
        StringBuilder stringBuilder = new StringBuilder();
        // 获取返回信息的流
        InputStream in = process.getInputStream();
        Reader reader = new InputStreamReader(in, charsetName);
        BufferedReader bReader = new BufferedReader(reader);
        String res = bReader.readLine();
        while (res != null) {
            stringBuilder.append(res);
            stringBuilder.append("\n");
            res = bReader.readLine();
        }
        bReader.close();
        reader.close();
        return stringBuilder.toString();
    }
}
