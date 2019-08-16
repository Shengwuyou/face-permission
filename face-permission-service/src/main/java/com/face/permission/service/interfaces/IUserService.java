package com.face.permission.service.interfaces;

import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.api.model.response.TokenDTO;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 15:21
 */
public interface IUserService {

    /**
     * face 用户自定义注册
     * @param request
     * @return
     */
    TokenDTO selfRegister(UserRequest request);

    /**
     * 后台用户导入注册
     * @param request
     * @return
     */
    TokenDTO cmsRegister(UserRequest request);
}
