package com.laoz.lucene.files.domain;

/**
 * @author: laoz
 * @Time: 2022/7/22  11:36
 * @description: 响应信息主体
 */

import com.laoz.lucene.files.constants.Constants;

import java.io.Serializable;

/**
 * 响应信息主体
 */
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = Constants.SUCCESS;

    /**
     * 失败
     */
    public static final int FAIL = Constants.FAIL;

    private int code;

    private String msg;

    protected T data;

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }
    public static <T> R<T> ok( String msg) {
        return restResult(null, SUCCESS, msg);
    }

    public static <T> R<T> fail() {
        return restResult(null, FAIL, null);
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL, null);
    }

    public static <T> R<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }
    public static <T> R<T> fail(T data,int code, String msg) {
        return restResult(data, code, msg);
    }

    public static <T> R<T> buildByBoolean(boolean trueOrFalse) {
        if (trueOrFalse){
            return ok();
        }
        return fail();
    }

    public static <T> R<T> buildByBoolean(boolean trueOrFalse, String msg) {
        if (trueOrFalse){
            return ok();
        }
        return fail(msg);
    }

    public static <T> R<T> buildByBoolean(boolean trueOrFalse,String successMsg,String msg) {
        if (trueOrFalse){
            return ok(successMsg);
        }
        return fail(msg);
    }


    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess(){
        if (R.SUCCESS == this.code) {
            return true;
        }
        return false;
    }
}