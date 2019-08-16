package com.face.permission.service.impl.user;

import com.face.permission.api.model.request.user.UserRequest;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-16 09:56
 */
public abstract class RegisterTemplate {

    public String register(UserRequest request){
        String key = "u_register_"+ "mobile/XXXX";
        //lock

        //1.检查参数 checkParam
        checkParam(request);
        //2.信息入库
        dataStorage(request);
        //3.token创建，redis缓存
        String token = createToken(request);
        //finally   unlock
        return token;
    }

    /**
     * 参数校验
     * @return
     */
    abstract void checkParam(UserRequest request);

    /**
     * 信息入库
     */
    abstract void dataStorage(UserRequest request);

    /**
     * 分配请求认证令牌
     * @return
     */
    abstract String createToken(UserRequest request);

}
