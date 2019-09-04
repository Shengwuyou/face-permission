package com.face.permission.common.constants.enums.role;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-31 18:06
 */
public enum RoleTypeEnum {
    UNKONW(0, "未知操作"),
    ADD(1, "新增"),
    DELETE(2, "删除"),
    UPDATE(3, "更新"),
    QUERY(4, "查询")
    ;

    /**
     *
     */
    private Integer code;
    /**
     * 描述
     */
    private String msg;

    RoleTypeEnum(Integer code, String msg) {
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
