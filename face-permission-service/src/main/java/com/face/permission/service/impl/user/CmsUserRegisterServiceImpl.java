package com.face.permission.service.impl.user;

import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.common.constants.enums.role.RoleEnum;
import com.face.permission.common.constants.enums.user.UserEnums;
import com.face.permission.common.utils.AssertUtil;
import com.face.permission.service.template.RegisterTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.face.permission.common.constants.enums.user.UserErrorEnum.*;

/**
 * @Description cms后台注册
 * @Author xuyizhong
 * @Date 2019-08-16 17:48
 */
@Service(value = "cmsRegister")
public class CmsUserRegisterServiceImpl extends RegisterTemplate {
    @Override
    public void checkParam(UserRequest request) {
        //1.请求是否来自cms后台
        AssertUtil.isTrue(request.getFromWay() == UserEnums.UserFromWay.FROM_CMS.getCode(),
                FROM_WAY_ILLAGEL.getCode(), FROM_WAY_ILLAGEL.getMsg());
        //2.操作注册的用户是否有权限
        Integer[] roles = request.getRoles();
        AssertUtil.isTrue(Arrays.stream(roles).anyMatch(role -> role == RoleEnum.ROOT_ROLE.getCode())
                , NO_ROLE.getCode(), NO_ROLE.getMsg());
        checkBaseParam(request);
    }
}
