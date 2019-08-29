package com.face.permission.mapper.dto.request;

import lombok.Data;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-19 22:58
 */
@Data
public class UserLoginDTO {
    /**
     * 发起注册请求的用户
     */
    private String parentUserId;

    /**
     * 登录名 手机号/邮箱/自定义 （默认使用手机号）
     * loginName（字母数字下划线）
     */
    private String loginName;

    /**
     * 手机号 11位数字
     */
    private String mobilePhone;
    /**
     * 邮箱
     */
    private String email;


    /**
     * 密码
     */
    private String password;

    /**
     * 登陆类型 0 : loginName 1 : mobile 2 : email
     */
    private Integer type;


    /**
     * 注册平台 系统/配置/环境 （IOS/Android/PC）
     */
    private String platform;
    /**
     * 请求来自：0权限后台  1客户端
     */
    private Integer fromWay;
}
