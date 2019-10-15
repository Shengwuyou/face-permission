package com.face.permission.common.model.request.user;

import com.alibaba.fastjson.JSONObject;
import com.face.permission.common.constants.ReturnConstant;
import com.face.permission.common.responses.JwtCheckResult;
import com.face.permission.common.utils.AssertUtil;
import com.face.permission.common.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static com.face.permission.common.utils.JwtUtils.TOKEN_ILLEGAL_MSG;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-02 13:54
 */
public class ThreadLocalUser {
    //ThreadLocal 会在内部为每一个线程开辟一块空间，每一个线程只操作内部的东西
    private static ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>();

    /**
     * 本次拦截 threadLocal中保存user信息
     * @param userInfo
     */
    public static void setUserInfo(UserInfo userInfo){
        if (userInfoThreadLocal == null){
            userInfoThreadLocal = new ThreadLocal<>();
        }
        userInfoThreadLocal.set(userInfo);
    }

    /**
     * 获取用户信息
     * @return
     */
    public static UserInfo getUserInfo(){
        if (userInfoThreadLocal == null){
            userInfoThreadLocal = new ThreadLocal<>();
        }
        return userInfoThreadLocal.get();
    }

    public static void cleanThreadLocal(){
        userInfoThreadLocal = null;
    }


    /**
     * 校验token参数是否正常
     * @param token
     * @return
     */
    public static UserInfo checkToken(String token){
        AssertUtil.isTrue(StringUtils.isNotBlank(token), ReturnConstant.TOKEN_ILLEGAL, TOKEN_ILLEGAL_MSG);
        JwtCheckResult rt = JwtUtils.validateJWT(token);
        //TODO 这儿需要加入 用户token唯一性校验 和 版本失效控制校验
        AssertUtil.isTrue(rt.isCheck(), ReturnConstant.TOKEN_ILLEGAL, rt.getMsg());
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(rt.getUserId());
        userInfo.setRoles(rt.getRoles());
        userInfo.setNickName_(rt.getNickName());
        userInfo.setFromWay(rt.getFromWay());
        userInfo.setPlatform(rt.getPlatForm());
        ThreadLocalUser.setUserInfo(userInfo);
        return userInfo;
    }

    public static void checkTrace(String trace){
        if (trace == null){
            return;
        }
        JSONObject traceJs = JSONObject.parseObject(trace);

        //TODO 这儿需要加入 用户token唯一性校验 和 版本失效控制校验, 实现单点登录
        UserInfo userInfo = getUserInfo();
        AssertUtil.isTrue(Objects.equals(traceJs.getString("platform"), userInfo.getPlatform()), "请求平台发生变更，请重新获取token");
        AssertUtil.isTrue(userInfo.getFromWay() == traceJs.getInteger("fromWay"), "请求来源发生变更，请重新获取token");
        ThreadLocalUser.setUserInfo(userInfo);
    }
}
