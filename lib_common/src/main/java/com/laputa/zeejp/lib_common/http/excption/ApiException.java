package com.laputa.zeejp.lib_common.http.excption;

/**
 * 网络请求出错
 */
public class ApiException extends Exception {

    private int code;

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
