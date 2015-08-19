package com.tongdao.sdk.beans;


public class TdErrorBean {

    /**
     * 返回的错误码
     */
    private int errorCode;

    /**
     * 返回的错误信息
     */
    private String errorMsg;

    /**
     * 获得错误码
     *
     * @return int 返回错误码
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * 获得错误信息
     *
     * @return String 错误信息
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 同道内部调用,不建议使用
     *
     * @param errorCode
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 同道内部调用,不建议使用
     *
     * @param errorCode
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
