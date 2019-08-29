package com.face.permission.service.impl.user;

import com.alibaba.fastjson.JSON;
import com.face.permission.api.model.request.user.UserInfo;
import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.common.constants.RedisKeyCosntant;
import com.face.permission.common.constants.enums.user.UserEnums;
import com.face.permission.common.exceptions.FaceServiceException;
import com.face.permission.common.utils.*;
import com.face.permission.mapper.dao.PAccountMapper;
import com.face.permission.mapper.dao.PUserMapper;
import com.face.permission.mapper.domain.PAccountDO;
import com.face.permission.mapper.domain.PUserDO;
import com.face.permission.mapper.dto.request.UserLoginDTO;
import com.face.permission.mapper.query.user.UserQuery;
import com.face.permission.mapper.vo.user.UserInfoVo;
import com.face.permission.service.interfaces.user.IUserService;
import com.face.permission.service.template.RedisSelfCacheManager;
import com.face.permission.service.template.RegisterTemplate;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

import static com.face.permission.common.constants.RedisKeyCosntant.REGISTER_LOCK_KEY;
import static com.face.permission.common.constants.enums.SystemErrorEnum.ASSERT_ERROR_CODE;
import static com.face.permission.common.utils.JwtUtils.DEFAULT_TOKEN_EXPIRE_TIME;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 15:21
 */
@Service
public class UserServiceImpl implements IUserService {

    private Logger logger = LoggerUtil.COMMON_DEFAULT;

    @Autowired
    PUserMapper userMapper;

    @Autowired
    PAccountMapper accountMapper;

    @Autowired
    RegisterTemplate cmsUserRegister;

    @Autowired
    RedisSelfCacheManager redisSelfCacheManager;

    @Override
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
        String token = cmsUserRegister.register(request);
        return new TokenDTO().setToken(token);
    }

    @Override
    public TokenDTO login(UserLoginDTO request) {
        checkLoginName(request);

        PUserDO userDO = null;
        String uId = null;
        if (request.getType() != UserEnums.LoginTypeEnum.LOGIN_NAME.getCode()){
            userDO = userMapper.selectByLoginType(request);
            AssertUtil.isTrue( userDO != null , "账号不存在");
            uId = userDO.getuId();
            String userInfoKey = RedisKeyCosntant.USER_INFO_KEY + userDO.getuId();
            redisSelfCacheManager.set(userInfoKey, JSON.toJSONString(userDO), 60 * 3);
        }

        PAccountDO accountDO = getIfAbsentAccount(uId, request.getLoginName(), request.getPassword());

        if (userDO == null){
            userDO = userMapper.selectByUID(accountDO.getUId());
            AssertUtil.isTrue( userDO != null , "登陆信息异常");
            String userInfoKey = RedisKeyCosntant.USER_INFO_KEY + userDO.getuId();
            redisSelfCacheManager.set(userInfoKey, JSON.toJSONString(userDO), 60 * 3);
        }
        //将String数组转成Integer数组
        Integer[] roles = (Integer[]) ConvertUtils.convert(accountDO.getRoles().split(","), Integer.class);
        String token = createToken(userDO.getuId(), roles, userDO.getNickName(), request.getFromWay(), request.getPlatform());
        return new TokenDTO().setToken(token).setNickName(userDO.getNickName());
    }

    @Override
    public boolean update(UserRequest request) {
        return false;
    }


    @Override
    public Integer getTotal(UserQuery query) {
        return userMapper.selectUserTotal(query);
    }

    @Override
    public List<UserInfoVo> getList(UserQuery query) {
        return userMapper.selectUsers(query);

    }

    private String createToken(String uId, Integer[] roles, String nickName, Integer fromWay, String platForm ){
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(uId);
        userInfo.setRoles(roles);
        userInfo.setNickName(nickName);
        userInfo.setFromWay(fromWay);
        userInfo.setPlatform(platForm);
        try {
            return JwtUtils.createJWT(1, JwtUtils.objectToMap(userInfo), DEFAULT_TOKEN_EXPIRE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FaceServiceException(ASSERT_ERROR_CODE.getCode(), "登陆异常");
        }
    }

    /**
     * 检查登陆账号 是loginName / mobile / email
     * @param request
     */
    private void checkLoginName(UserLoginDTO request){
        if (request.getType() == UserEnums.LoginTypeEnum.LOGIN_NAME.getCode()  ){
            return;
        } else if (Pattern.matches("^(?!.*[\\\\W])(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,16}$", request.getLoginName())){
            request.setType(UserEnums.LoginTypeEnum.LOGIN_NAME.getCode());
        }else if (ValidatorUtil.isMobile(request.getLoginName())){
            request.setMobilePhone(request.getLoginName());
            request.setType(UserEnums.LoginTypeEnum.MOBILE.getCode());
        }else if (ValidatorUtil.isEmail(request.getLoginName())){
            request.setEmail(request.getLoginName());
            request.setType(UserEnums.LoginTypeEnum.EMAIL.getCode());
        }else {
            AssertUtil.error("用户账号格式异常");
        }
    }

    private PAccountDO getIfAbsentAccount(String uId, String loginName, String password){
        AssertUtil.isTrue(!StringUtils.isAllBlank(uId, loginName), "用户信息缺失");
        PAccountDO accountDO = null;
        String psd = MD5.getMD5(password);
        if (StringUtils.isNotBlank(uId)){
            accountDO = redisSelfCacheManager.get(RedisKeyCosntant.USER_ACCOUNT_KEY + uId, PAccountDO.class);

        }
        if (accountDO == null){
            accountDO = accountMapper.selectByUserId(uId, loginName, psd);
        }
        AssertUtil.isTrue( accountDO != null , "账户不存在");
        redisSelfCacheManager.set(RedisKeyCosntant.USER_ACCOUNT_KEY + accountDO.getUId(), JSON.toJSONString(accountDO), 60 * 3);
        return accountDO;
    }
}
