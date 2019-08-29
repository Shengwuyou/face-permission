package com.face.permission.mapper.query.user;

import com.face.permission.common.responses.PageQuery;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-22 22:30
 */
public class UserQuery extends PageQuery {

    @ApiModelProperty("uId")
    private String uId;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("手机号")
    private String mobilePhone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("用户状态 0未知 1有效 2无效")
    private Integer status;

    @ApiModelProperty("登陆账号")
    private String loginName;

    @ApiModelProperty("账号类型: (0游客/1普通/2VIP/3管理员/4root)")
    private Integer type;

    @ApiModelProperty("账号创建开始时间 from")
    private LocalDateTime startTime;

    @ApiModelProperty("账号创建开始时间 to")
    private LocalDateTime endTime;

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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
