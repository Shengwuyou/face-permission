package com.face.permission.common.constants.enums.user;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 17:40
 */
public enum UserErrorEnum {
    PARAM_ILLAGEL(10001L, "Caused by:请求参数异常"),
    FROM_WAY_ILLAGEL(10002L, "请求的来源异常，请检查接口");


    private Long code;
    private String msg;

    UserErrorEnum(Long code, String msg) {
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
