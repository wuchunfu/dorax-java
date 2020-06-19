package org.dorax.utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 返回包装类
 *
 * @author wuchunfu
 * @date 2020-06-20
 */
public class ResultUtil<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    private T data;

    private String timestamp;

    public ResultUtil() {
    }

    public static <T> ResultUtil<T> success() {
        return success(null);
    }

    public static <T> ResultUtil<T> success(T data) {
        ResultUtil<T> result = new ResultUtil<>();
        result.setData(data);
        result.setCode(200);
        return result.putTimeStamp();
    }

    public static <T> ResultUtil<T> fail(String msg) {
        return fail(500, msg);
    }

    public static <T> ResultUtil<T> fail(int code, String msg) {
        ResultUtil<T> result = new ResultUtil<>();
        result.setCode(code);
        result.setMsg(msg);
        return result.putTimeStamp();
    }

    public static <T> ResultUtil<T> fail(int code, T data, String msg) {
        ResultUtil<T> tr = new ResultUtil<>();
        tr.setCode(code);
        tr.setData(data);
        tr.setMsg(msg);
        return tr.putTimeStamp();
    }

    private ResultUtil<T> putTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = dtf.format(now);
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String smg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ResultUtil{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}