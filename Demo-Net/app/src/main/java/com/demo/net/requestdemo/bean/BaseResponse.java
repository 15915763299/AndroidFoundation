package com.demo.net.requestdemo.bean;

import java.io.Serializable;

/**
 * Created by zero on 2018/8/13.
 */

public class BaseResponse<T> implements Serializable {

    public static final int SUCCESS = 0;

    private int errorCode;
    private String errorMsg;
    private T data;

    public BaseResponse() {
        errorCode = SUCCESS;
        errorMsg = "成功";
    }

    public BaseResponse(int errorCode, String errorMsg, T data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
