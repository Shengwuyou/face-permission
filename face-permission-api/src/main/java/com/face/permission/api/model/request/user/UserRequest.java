package com.face.permission.api.model.request.user;


import com.face.permission.api.model.request.valids.groups.CreateGroup;
import com.face.permission.api.model.request.valids.groups.RetrieveGroup;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 15:43
 */
public class UserRequest extends UserInfo{
    /**
     * 发起注册请求的用户
     */
    private String parentUserId;
    /**
     * 昵称
     */
    @NotBlank(message = "nickName 不能为空" , groups = CreateGroup.class)
    @Pattern(regexp = ".{8,20}", message = "昵称长度范围8～20" , groups = CreateGroup.class)
    private String nickName;
    /**
     * 手机号
     */
    @NotBlank(message = "mobilePhone 不能为空" , groups = CreateGroup.class)
    @Pattern(regexp = "^1[0-9]{10}", message = "手机号格式不正确" , groups = CreateGroup.class)
    private String mobilePhone;
    /**
     * 邮箱
     */
//    @Email(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱校验失败")
    @Email(message = "邮箱格式非法" , groups = CreateGroup.class)
    private String email;
    /**
     * 头像图片url
     */
    private String headPic;
    /**
     * 性别
     */
    @Pattern(regexp = "[0-2]+", message = "性别范围0，1，2" , groups = CreateGroup.class)
    private String sex;

    /**
     * 状态(0未知/1有效 /2无效)
     */
    @Pattern(regexp = "[0-2]+", message = "状态0，1，2" , groups = CreateGroup.class)
    private String status;

    /**
     * 登录名 手机号/邮箱/自定义 （默认使用手机号）
     */
    @NotBlank(message = "loginName 不能为空", groups = RetrieveGroup.class)
    @Pattern(regexp = "^(?!.*[\\\\W])(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,16}$", message = "登录名需要字母数字组合 8～16位", groups = CreateGroup.class)
    private String loginName;

    /**
     * 密码
     */
    @NotBlank(message = "password 不能为空", groups = {CreateGroup.class, RetrieveGroup.class})
    @Pattern(regexp = "^(?!.*[\\\\W])(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,16}$", message = "密码必须使用大小写字母和数字组合  8～16位", groups =  {CreateGroup.class, RetrieveGroup.class})
    private String password;
    /**
     * 账号等级( 0~N)
     */
    private String grade;
    /**
     * 账户类型(0游客/1普通/2VIP/3管理员/4root)
     */
    @NotNull(message = "type 不能为空", groups = CreateGroup.class)
    @Pattern(regexp = "[0-4]+", message = "账户类型(0游客/1普通/2VIP/3管理员/4root)", groups = CreateGroup.class)
    private String type;

    /**
     * 二进制设置权限00000（暂时使用数字简单代替）
     * 第1位 faceMsg浏览信息权限
     * 第2位 查看信息发布人信息（关注）
     * 第3位 与信息发布人发送消息（以上为普通用户，以下为管理员用户）
     * 第4位 登陆用户后台 查询所有普通用户
     * 第5位 登陆用户后台 操作普通用户
     * 第6位 登陆用户后台 操作所有用户
     */
    private String role;

    public String getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(String parentUserId) {
        this.parentUserId = parentUserId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
