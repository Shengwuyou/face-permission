package com.face.permission.service.impl;

import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.common.constants.enums.user.UserEnums;
import com.face.permission.common.utils.AssertUtil;
import com.face.permission.service.interfaces.IUserService;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 15:21
 */
@Service
public class UserServiceImpl implements IUserService {

    @Override
    public TokenDTO selfRegister(UserRequest request) {
        //1参数校验
        request.
        //2用户输入入库

        //3用户登陆密码入库

        //4生成token返回


        return null;
    }

    @Override
    public TokenDTO cmsRegister(UserRequest request) {
        //1参数校验
        //1.请求是否来自cms后台
        AssertUtil.isTrue(request.getFromWay() == UserEnums.UserFromWay.FROM_CMS.getCode(), );
        //2.请求用户是否有注册其它用户的权限判断
        Integer[] roles = request.getRoles();

        //2用户输入入库

        //3用户登陆密码入库

        //4生成token返回


        return null;
    }

}
