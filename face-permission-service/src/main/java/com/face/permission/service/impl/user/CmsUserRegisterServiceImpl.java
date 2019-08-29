package com.face.permission.service.impl.user;

import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.common.constants.enums.role.RoleEnum;
import com.face.permission.common.constants.enums.user.UserEnums;
import com.face.permission.common.utils.AssertUtil;
import com.face.permission.common.utils.JwtUtils;
import com.face.permission.common.utils.MD5;
import com.face.permission.common.utils.SnowFlakeGenerator;
import com.face.permission.mapper.dao.PAccountMapper;
import com.face.permission.mapper.dao.PUserMapper;
import com.face.permission.mapper.domain.PAccountDO;
import com.face.permission.mapper.domain.PUserDO;
import com.face.permission.service.template.RegisterTemplate;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.face.permission.common.constants.enums.user.UserErrorEnum.*;
import static com.face.permission.common.constants.enums.user.UserErrorEnum.USER_ACCCOUNT_STORAGE_ERROR;
import static com.face.permission.common.utils.JwtUtils.DEFAULT_TOKEN_EXPIRE_TIME;

/**
 * @Description cms后台注册
 * @Author xuyizhong
 * @Date 2019-08-16 17:48
 */
@Service
public class CmsUserRegisterServiceImpl extends RegisterTemplate {

    @Autowired
    PUserMapper userMapper;

    @Autowired
    PAccountMapper accountMapper;


    @Override
    public void checkParam(UserRequest request) {
        //1.请求是否来自cms后台
        AssertUtil.isTrue(request.getFromWay() == UserEnums.UserFromWay.FROM_CMS.getCode(),
                FROM_WAY_ILLAGEL.getCode(), FROM_WAY_ILLAGEL.getMsg());
        //2.操作注册的用户是否有权限
        Integer[] roles = request.getRoles();
        AssertUtil.isTrue(Arrays.stream(roles).anyMatch(role -> role == RoleEnum.ROOT_ROLE.getCode())
                , NO_ROLE.getCode(), NO_ROLE.getMsg());

        //3.检查注册用户的手机号/邮箱/loginName 是否已经注册
        List<PUserDO> userDOS = userMapper.selectRegisterUsers(request.getMobilePhone(), request.getEmail());
        if (userDOS.size() > 0){
            PUserDO userDO = userDOS.get(0);
            AssertUtil.isTrue(!Objects.equals(userDO.getMobilePhone(), request.getMobilePhone()), "手机号已经被注册！");
            AssertUtil.isTrue(userDO.getEmail() != null && !Objects.equals(userDO.getEmail(), request.getEmail()), "邮箱已经被注册！");
        }
        PAccountDO accountDO = accountMapper.selectByUserId(null,request.getLoginName(), null);
        AssertUtil.isNull(accountDO, "登陆账号已经被注册!");
    }

    @Override
    public void dataStorage(UserRequest request) {
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
        accountDO.setUId(uId);
        accountDO.setLoginName(request.getLoginName());
        accountDO.setPassword(MD5.getMD5(request.getPassword())); //MD5加密存储
        accountDO.setGrade(0);
        accountDO.setType(request.getType());
        accountDO.setStatus(1);
        accountDO.setRoles(ConvertUtils.convert(request.getRole()));
        accountDO.setCreateTime(LocalDateTime.now());
        accountDO.setUpdateTime(LocalDateTime.now());
        AssertUtil.isTrue(accountMapper.insertSelective(accountDO) > 0, USER_ACCCOUNT_STORAGE_ERROR.getMsg());
        request.setUid(uId);

    }

    @Override
    public String createToken(UserRequest request) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", request.getUid());
        claims.put("roles", request.getRole());


        String token = JwtUtils.createJWT(1, claims, DEFAULT_TOKEN_EXPIRE_TIME); //版本：1 ，用户信息 ， 现在是5分钟 /超时时间15分钟 = 900000
        //存入redis ，保证每次不仅要有效还要有版本号的控制

        return token;
    }
}
