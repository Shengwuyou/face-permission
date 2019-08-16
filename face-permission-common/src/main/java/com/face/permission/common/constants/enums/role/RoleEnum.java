package com.face.permission.common.constants.enums.role;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-16 10:36
 */
public enum RoleEnum {
    ROOT_ROLE(9, "管理员权限");

    /**
     * 权限code
     */
    private Integer code;
    /**
     * 权限描述
     */
    private String msg;

    RoleEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
