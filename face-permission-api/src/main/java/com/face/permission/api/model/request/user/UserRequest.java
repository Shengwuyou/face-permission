package com.face.permission.api.model.request.user;


import com.face.permission.api.model.request.valids.groups.CreateGroup;
import com.face.permission.api.model.request.valids.groups.RetrieveGroup;
import com.face.permission.api.model.request.valids.groups.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 15:43
 */
@Data
public class UserRequest extends UserInfo {
    /**
     * 发起注册请求的用户
     */
    private String parentUserId;

    /**
     * 用户ID
     */
    @NotBlank(message = "userID 不能为空", groups = UpdateGroup.class)
    private String userId;

    /**
     * 昵称
     */
    @NotBlank(message = "nickName 不能为空", groups = CreateGroup.class)
    @Pattern(regexp = ".{1,10}", message = "昵称长度范围1～10", groups = {CreateGroup.class, UpdateGroup.class})
    private String nickName;
    /**
     * 手机号
     */
    @NotBlank(message = "mobilePhone 不能为空", groups = CreateGroup.class)
    @Pattern(regexp = "^1[0-9]{10}", message = "手机号格式不正确", groups = {CreateGroup.class, RetrieveGroup.class, UpdateGroup.class})
    private String mobilePhone;
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式非法", groups = {CreateGroup.class, RetrieveGroup.class, UpdateGroup.class})
    private String email;
    /**
     * 头像图片url
     */
    private String headPic;
    /**
     * 性别
     */
//    @Pattern(regexp = "[0-2]+", message = "性别范围0，1，2", groups = {CreateGroup.class, UpdateGroup.class})
    private Integer sex;

    /**
     * 状态(0未知/1有效 /2无效)
     */
//    @Pattern(regexp = "[0-2]+", message = "状态0，1，2", groups = {CreateGroup.class, UpdateGroup.class})
    private Integer status;

    /**
     * 登录名 手机号/邮箱/自定义 （默认使用手机号）
     */
    @Pattern(regexp = "^(?!.*[\\\\W])(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,16}$", message = "登录名需要字母数字组合 8～16位", groups = {CreateGroup.class, RetrieveGroup.class, UpdateGroup.class})
    private String loginName;

    /**
     * 密码
     */
    @NotBlank(message = "password 不能为空", groups = {CreateGroup.class, RetrieveGroup.class})
    @Pattern(regexp = "^(?!.*[\\\\W])(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,16}$", message = "密码必须使用大小写字母和数字组合  8～16位", groups = {CreateGroup.class, RetrieveGroup.class, UpdateGroup.class})
    private String password;

    /**
     * 账号等级( 0~N)
     */
    private Integer grade;

    /**
     * 账户类型(0游客/1普通/2VIP/3管理员/4root)
     */
//    @NotNull(message = "type 不能为空", groups = CreateGroup.class)
//    @Pattern(regexp = "[0-4]+", message = "账户类型(0游客/1普通/2VIP/3管理员/4root)", groups = CreateGroup.class)
    private Integer type;

    /**
     * 二进制设置权限00000（暂时使用数字简单代替）
     * 第1位 faceMsg浏览信息权限
     * 第2位 查看信息发布人信息（关注）
     * 第3位 与信息发布人发送消息（以上为普通用户，以下为管理员用户）
     * 第4位 登陆用户后台 查询所有普通用户
     * 第5位 登陆用户后台 操作普通用户
     * 第6位 登陆用户后台 操作所有用户
     */
    private Integer[] role;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
