package com.face.permission.common.exceptions;

import com.face.permission.common.responses.ResultInfo;

import java.io.Serializable;

/**
 * @Description 系统处理过之后抛出的异常
 * @Author xuyizhong
 * @Date 2019-07-08 17:39
 */
public class FaceServiceException extends RuntimeException implements Serializable {

    private ResultInfo<Object> result;

    public FaceServiceException(Long code, String message) {
        this.result = new ResultInfo<>(code,message);
    }

    public FaceServiceException(ResultInfo<Object> result) {
        this.result = result;
    }

    public FaceServiceException(String message, ResultInfo<Object> result) {
        super(message);
        this.result = result;
    }

    public FaceServiceException(String message, Throwable cause, ResultInfo<Object> result) {
        super(message, cause);
        this.result = result;
    }

    public FaceServiceException(Throwable cause, ResultInfo<Object> result) {
        super(cause);
        this.result = result;
    }

    public FaceServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ResultInfo<Object> result) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.result = result;
    }
}
