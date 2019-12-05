package org.dorax.enums;

/**
 * @author wcf
 * @date 2019-12-10
 */
public enum StatusCode {

    /**
     * 1 success
     * 2 failed
     */
    SUCCESS(0, "success"),
    FAILED(1, "failed");

    /**
     * status code
     */
    private int code;
    /**
     * status description message
     */
    private String msg;

    /**
     * status code constructor
     *
     * @param code The code
     * @param msg  The massage
     */
    StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * Get code
     *
     * @return status code
     */
    public int getCode() {
        return code;
    }

    /**
     * Set code
     *
     * @param code The code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Get massage
     *
     * @return massage string
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Set massage
     *
     * @param msg The massage
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * toString
     *
     * @return string
     */
    @Override
    public String toString() {
        return "StatusCode{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
