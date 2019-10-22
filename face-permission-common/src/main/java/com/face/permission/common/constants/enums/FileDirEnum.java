package com.face.permission.common.constants.enums;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-10-21 10:57
 */
public enum FileDirEnum {
    USER_HEAD(1, "用户头像路径","user/%s/head/"),
    USER_COVER(1, "用户封面路径","user/%s/cover/"),
    USER_PICTUREAS(1, "用户图片库路径","user/%s/pictures/"),
    USER_NEWS(1, "用户发布的动态图片路径","user/%s/head/");

    private Integer code;
    private String msg;
    private String fileDir;

    FileDirEnum(Integer code, String msg, String fileDir) {
        this.code = code;
        this.msg = msg;
        this.fileDir = fileDir;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }
}
