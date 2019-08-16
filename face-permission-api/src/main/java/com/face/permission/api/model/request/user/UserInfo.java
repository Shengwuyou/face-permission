package com.face.permission.api.model.request.user;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-01 14:11
 */
public class UserInfo {
    /**
     * 注册平台 系统/配置/环境 （IOS/Android/PC）
     */
    private String platform;
    /**
     * 操作人id
     */
    private String uid;
    /**
     * 操作人权限集合
     */
    private Integer[] roles;
    /**
     * 请求来自：0权限后台  1客户端
     */
    private Integer fromWay;


    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer[] getRoles() {
        return roles;
    }

    public void setRoles(Integer[] roles) {
        this.roles = roles;
    }

    public Integer getFromWay() {
        return fromWay;
    }

    public void setFromWay(Integer fromWay) {
        this.fromWay = fromWay;
    }
}
