package com.face.permission.service.impl.user;

import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.common.constants.enums.role.RoleEnum;
import com.face.permission.common.constants.enums.user.UserEnums;
import com.face.permission.common.utils.AssertUtil;
import com.face.permission.common.utils.SnowFlakeGenerator;
import com.face.permission.mapper.dao.PAccountMapper;
import com.face.permission.mapper.dao.PUserMapper;
import com.face.permission.mapper.domain.PAccountDO;
import com.face.permission.mapper.domain.PUserDO;
import com.face.permission.service.interfaces.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.face.permission.common.constants.enums.user.UserErrorEnum.*;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 15:21
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    PUserMapper userMapper;

    @Autowired
    PAccountMapper accountMapper;

    @Override
//    @Trans
    public TokenDTO selfRegister(UserRequest request) {
        //1参数校验
//        request.
        //2用户输入入库

        //3用户登陆密码入库

        //4生成token返回


        return null;
    }

    @Override
    public TokenDTO cmsRegister(UserRequest request) {


        //1参数校验

        //2.请求用户是否有注册其它用户的权限判断
        Integer[] roles = request.getRoles();

        //2用户输入入库

        //3用户登陆密码入库

        //4生成token返回


        return null;
    }


    /**
     * cms后台注册
     */
    class SelfUserRegister extends RegisterTemplate {

        @Override
        void checkParam(UserRequest request) {

        }

        @Override
        void dataStorage(UserRequest request) {

        }

        @Override
        String createToken(UserRequest request) {
            return null;
        }
    }

    class CmsUserRegister extends RegisterTemplate {

        @Override
        void checkParam(UserRequest request) {
            //1.请求是否来自cms后台
            AssertUtil.isTrue(request.getFromWay() == UserEnums.UserFromWay.FROM_CMS.getCode(),
                    FROM_WAY_ILLAGEL.getCode(), FROM_WAY_ILLAGEL.getMsg());
            //2.操作注册的用户是否有权限
            Integer[] roles = request.getRoles();
            AssertUtil.isTrue(Arrays.stream(roles).anyMatch(role -> role == RoleEnum.ROOT_ROLE.getCode())
                        , NO_ROLE.getCode(), NO_ROLE.getMsg());
        }

        @Override
        void dataStorage(UserRequest request) {
            //自定义用户ID-雪花算法
            String uId = SnowFlakeGenerator.getSnowId().toString();

            //1.用户基本信息入库
            PUserDO userDO = new PUserDO();
            userDO.setuId(uId);
            userDO.setNickName(request.getNickName());
            userDO.setMobilePhone(request.getMobilePhone());
            userDO.setEmail(request.getEmail());
            userDO.setHeadPic(request.getHeadPic());
            userDO.setSex(request.getSex());
            userDO.setStatus(UserEnums.UserStatus.AVAILABLE.getCode());
            userDO.setCreateTime(LocalDateTime.now());
            userDO.setUpdateTime(LocalDateTime.now());
            AssertUtil.isTrue(userMapper.insertSelective(userDO) > 0, USER_INFO_STORAGE_ERROR.getMsg());
            //2.用户账号信息入库
            PAccountDO accountDO = new PAccountDO();
            accountDO.setuId(uId);
            accountDO.setLoginName(request.getLoginName());
            AssertUtil.isTrue(accountMapper.insertSelective(accountDO) > 0, USER_ACCCOUNT_STORAGE_ERROR.getMsg());


        }

        @Override
        String createToken(UserRequest request) {
            return null;
        }
    }
}
