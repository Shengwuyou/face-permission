package com.face.permission.service.impl.user;

import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.service.template.RegisterTemplate;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-16 17:51
 */

public class SelfUserRegisterServiceImpl extends RegisterTemplate {

    @Override
    public void checkParam(UserRequest request) {

    }

    @Override
    public void dataStorage(UserRequest request) {

    }

    @Override
    public String createToken(UserRequest request) {
        return null;
    }
}
