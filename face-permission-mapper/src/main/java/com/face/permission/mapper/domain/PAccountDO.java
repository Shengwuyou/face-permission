package com.face.permission.mapper.domain;

import java.util.Date;

public class PAccountDO {
    private Long id;

    private String uId;
    /**
     * 登录名 手机号/邮箱/自定义 （默认使用手机号）
     */
    private String loginName;
    /**
     * 密码
     */
    private String password;
    /**
     * 账号等级( 0~N)
     */
    private Byte grade;
    /**
     * 账户类型(0游客/1普通/2VIP/3管理员/4root)
     */
    private Byte type;
    /**
     * 二进制设置权限00000
     * 第1位 faceMsg浏览信息权限
     * 第2位 查看信息发布人信息（关注）
     * 第3位 与信息发布人发送消息（以上为普通用户，以下为管理员用户）
     * 第4位 登陆用户后台 查询所有普通用户
     * 第5位 登陆用户后台 操作普通用户
     * 第6位 登陆用户后台 操作所有用户
     */
    private Byte role;
    /**
     * 状态(0未知/1有效 /2无效)
     */
    private Byte status;

    private Date createTime;

    private Date updateTime;

    private String extJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId == null ? null : uId.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Byte getGrade() {
        return grade;
    }

    public void setGrade(Byte grade) {
        this.grade = grade;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getRole() {
        return role;
    }

    public void setRole(Byte role) {
        this.role = role;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getExtJson() {
        return extJson;
    }

    public void setExtJson(String extJson) {
        this.extJson = extJson == null ? null : extJson.trim();
    }
}