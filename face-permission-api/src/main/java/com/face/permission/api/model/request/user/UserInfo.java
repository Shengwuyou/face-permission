package com.face.permission.api.model.request.user;


import com.face.permission.api.model.request.valids.groups.CreateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-01 14:11
 */

@Data
public class UserInfo {

    /**
     * 操作人id
     */
    private String uid;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 操作人权限集合
     */
    private Integer[] roles;

    /**
     * 注册平台 系统/配置/环境 （IOS/Android/PC）
     */
    private String platform;
    /**
     * 请求来自：0权限后台  1客户端
     */
    private Integer fromWay;
}
