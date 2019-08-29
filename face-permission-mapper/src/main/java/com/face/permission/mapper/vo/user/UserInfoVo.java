package com.face.permission.mapper.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-26 20:29
 */
public class UserInfoVo {

    @ApiModelProperty("uId")
    private String uId;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("手机号")
    private String mobilePhone;

    @ApiModelProperty("用户状态 0未知 1男 2女")
    private Integer sex;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("用户状态 0未知 1有效 2无效")
    private Integer status;

    @ApiModelProperty("登陆账号")
    private String loginName;

    @ApiModelProperty("账号等级")
    private Integer grade;

    @ApiModelProperty("账号类型: (0游客/1普通/2VIP/3管理员/4root)")
    private Integer type;

    @ApiModelProperty("账号创建时间")
    private LocalDateTime createTime;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
