package com.face.permission.service.impl.user;

import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.common.constants.enums.user.UserEnums;
import com.face.permission.common.utils.AssertUtil;
import com.face.permission.service.template.RegisterTemplate;
import org.springframework.stereotype.Service;

import static com.face.permission.common.constants.enums.user.UserErrorEnum.*;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-16 17:51
 */

@Service(value = "selfRegister")
public class SelfUserRegisterServiceImpl extends RegisterTemplate {

    @Override
    public void checkParam(UserRequest request) {
        //1.请求是否来自client 客户端
        AssertUtil.isTrue(request.getFromWay() == UserEnums.UserFromWay.FROM_CLIENT.getCode(),
                FROM_WAY_ILLAGEL.getCode(), FROM_WAY_ILLAGEL.getMsg());
        checkBaseParam(request);
    }
}
