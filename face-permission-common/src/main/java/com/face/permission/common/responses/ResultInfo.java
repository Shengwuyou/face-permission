package com.face.permission.common.responses;

import com.face.permission.common.constants.ReturnConstant;

import java.io.Serializable;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-08 17:47
 */
public class ResultInfo<T> implements Serializable {
    /**
     * 响应码
     */
    private Long code;
    /**
     * 响应描述
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    public static ResultInfo<?> success(Object data){
        ResultInfo resultInfo = new ResultInfo(data);
        return resultInfo;
    }

    public static ResultInfo<?> error(Long code, String message, Object data){
        ResultInfo resultInfo = new ResultInfo(code, message, data);
        return resultInfo;
    }

    public static ResultInfo<?> error(Long code, String message){
        ResultInfo resultInfo = new ResultInfo(code, message);
        return resultInfo;
    }

    public static ResultInfo<?> success(Long code, String message){
        ResultInfo resultInfo = new ResultInfo(code, message);
        return resultInfo;
    }

    /**
     * 请求成功，不含data的构造函数
     */
    public ResultInfo() {
        this.code = ReturnConstant.SUCCESS_CODE;
        this.message = ReturnConstant.SUCCESS_MSG;
        this.data = null;
    }

    /**
     * 请求成功，带data的构造函数
     * @param data
     */
    public ResultInfo(T data) {
        this.code = ReturnConstant.SUCCESS_CODE;
        this.message = ReturnConstant.SUCCESS_MSG;
        this.data = data;
    }

    /**
     * 需要设置code和 msg的构造函数，一般为异常返回
     * @param code
     * @param message
     */
    public ResultInfo(Long code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    /**
     * code，message，data都需要自定义的返回设置
     * @param code
     * @param message
     * @param data
     */
    public ResultInfo(Long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
