package com.face.permission.service.template;

import com.alibaba.fastjson.JSON;
import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.common.constants.enums.user.UserEnums;
import com.face.permission.common.exceptions.FaceServiceException;
import com.face.permission.common.utils.AssertUtil;
import com.face.permission.common.utils.JwtUtils;
import com.face.permission.common.utils.MD5;
import com.face.permission.common.utils.SnowFlakeGenerator;
import com.face.permission.mapper.dao.PAccountMapper;
import com.face.permission.mapper.dao.PUserMapper;
import com.face.permission.mapper.domain.PAccountDO;
import com.face.permission.mapper.domain.PUserDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.face.permission.common.constants.RedisKeyCosntant.REGISTER_LOCK_KEY;
import static com.face.permission.common.constants.enums.SystemErrorEnum.ASSERT_ERROR_CODE;
import static com.face.permission.common.constants.enums.user.UserErrorEnum.USER_ACCCOUNT_STORAGE_ERROR;
import static com.face.permission.common.constants.enums.user.UserErrorEnum.USER_INFO_STORAGE_ERROR;
import static com.face.permission.common.utils.JwtUtils.DEFAULT_TOKEN_EXPIRE_TIME;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-16 09:56
 */
public abstract class RegisterTemplate {

    @Autowired
    PUserMapper userMapper;

    @Autowired
    PAccountMapper accountMapper;

    @Autowired
    RedisSelfCacheManager redisSelfCacheManager;

    public TokenDTO register(UserRequest request) {
        // 锁的规则是以手机号为唯一的表示
        String key = REGISTER_LOCK_KEY + request.getMobilePhone();
        if (redisSelfCacheManager.lock(key, 3L)) {
            try {
                //1.检查参数 checkParam
                checkParam(request);
                //2.信息入库
                dataStorage(request);
                //3.token创建，redis缓存
                String token = createToken(request);
                TokenDTO tokenDTO = new TokenDTO();
                tokenDTO.setToken(token).setNickName(request.getNickName()).setuId(request.getUid());
                return tokenDTO;
            } finally {
                redisSelfCacheManager.unLock(key);
            }
        } else {
            throw new FaceServiceException(ASSERT_ERROR_CODE.getCode(), "系统繁忙，请稍后重试");
        }
    }

    /**
     * 参数校验
     *
     * @return
     */
    public abstract void checkParam(UserRequest request);

    /**
     * 公共参数校验方法
     *
     * @param request
     */
    public void checkBaseParam(UserRequest request) {
        //3.检查注册用户的手机号/邮箱/loginName 是否已经注册
        List<PUserDO> userDOS = userMapper.selectRegisterUsers(request.getMobilePhone(), request.getEmail());
        if (userDOS.size() > 0) {
            PUserDO userDO = userDOS.get(0);
            AssertUtil.isTrue(!Objects.equals(userDO.getMobilePhone(), request.getMobilePhone()), "手机号已经被注册！");
            AssertUtil.isTrue(userDO.getEmail() != null && !Objects.equals(userDO.getEmail(), request.getEmail()), "邮箱已经被注册！");
        }
        PAccountDO accountDO = accountMapper.selectByUserId(null, request.getLoginName(), null);
        AssertUtil.isNull(accountDO, "登陆账号已经被注册!");
    }

    /**
     * 信息入库
     */
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
        accountDO.setStatus(UserEnums.UserStatus.AVAILABLE.getCode());
        accountDO.setRoles(JSON.toJSONString(request.getRole()));
        accountDO.setCreateTime(LocalDateTime.now());
        accountDO.setUpdateTime(LocalDateTime.now());
        AssertUtil.isTrue(accountMapper.insertSelective(accountDO) > 0, USER_ACCCOUNT_STORAGE_ERROR.getMsg());
        request.setUid(uId);
    }

    /**
     * 分配请求认证令牌
     *
     * @return
     */
    public String createToken(UserRequest request) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", request.getUid());
        claims.put("roles", request.getRole());


        String token = JwtUtils.createJWT(1, claims, DEFAULT_TOKEN_EXPIRE_TIME); //版本：1 ，用户信息 ， 现在是5分钟 /超时时间15分钟 = 900000
        //存入redis ，保证每次不仅要有效还要有版本号的控制

        return token;
    }

}
