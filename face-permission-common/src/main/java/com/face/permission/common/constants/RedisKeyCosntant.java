package com.face.permission.common.constants;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-20 15:31
 */
public interface RedisKeyCosntant {

    /**
     * user_info_%s (自定义userId)-用户信息缓存
     */
    String USER_INFO_KEY = "user_info_";

    /**
     * user_account_%s (自定义userId)-用户账号缓存
     */
    String USER_ACCOUNT_KEY = "user_account_";

    /**
     * 用户注册锁，防止重复注册，以手机号为重复拦截条件
     */
    String REGISTER_LOCK_KEY = "u_register_";



    /**
     * 用户注册锁，防止重复注册，以手机号为重复拦截条件,有 0 和 1，数据库中当某一个为空之后，表示数据发生变更，那就去取另外一个
     */
    String RECOMMEND_FRIENDS = "recommend_friends_";








    /**
     *
     */
    String ROLE_TYPE_KEY = "u_role_type_";

    /**
     *
     */
    String ROLE_ALL_KEY = "u_role_all";

    /**
     *
     */
    String CAN_SET_ROLE_ALL_KEY = "u_set_role_all";
}
