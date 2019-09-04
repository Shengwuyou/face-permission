package com.face.permission.mapper.domain;


import lombok.Data;

import java.time.LocalDateTime;

@Data
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
    private Integer grade;
    /**
     * 账户类型(0游客/1普通/2VIP/3管理员/4root)
     */
    private Integer type;
    /**
     * 二进制设置权限00000
     * 第1位 faceMsg浏览信息权限
     * 第2位 查看信息发布人信息（关注）
     * 第3位 与信息发布人发送消息（以上为普通用户，以下为管理员用户）
     */
    private String roles;
    /**
     * 状态(0未知/1有效 /2无效)
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String extJson;

}