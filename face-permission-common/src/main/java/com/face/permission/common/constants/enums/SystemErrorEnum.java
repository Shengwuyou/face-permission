package com.face.permission.common.constants.enums;

import com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-01 17:31
 */
public enum SystemErrorEnum {

    ASSERT_ERROR_CODE(210L, "断言异常"),

    UNKONW_HTTP_REQUEST(211L, "请求异常");



    private Long code;
    private String msg;

    SystemErrorEnum(Long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

