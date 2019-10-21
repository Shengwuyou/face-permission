package com.face.permission.service.impl.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.common.constants.enums.role.RoleTypeEnum;
import com.face.permission.common.constants.enums.user.UserEnums;
import com.face.permission.common.exceptions.FaceServiceException;
import com.face.permission.common.model.request.user.ThreadLocalUser;
import com.face.permission.common.model.request.user.UserInfo;
import com.face.permission.common.model.request.user.UserRequest;
import com.face.permission.common.responses.PageQuery;
import com.face.permission.common.utils.*;
import com.face.permission.mapper.dao.PAccountMapper;
import com.face.permission.mapper.dao.PRoleMapper;
import com.face.permission.mapper.dao.PUserMapper;
import com.face.permission.mapper.domain.PAccountDO;
import com.face.permission.mapper.domain.PRoleDo;
import com.face.permission.mapper.domain.PUserDO;
import com.face.permission.mapper.dto.request.UserLoginDTO;
import com.face.permission.mapper.query.user.UserQuery;
import com.face.permission.mapper.vo.user.UserInfoVo;
import com.face.permission.service.interfaces.roles.IRoleService;
import com.face.permission.service.interfaces.user.IUserService;
import com.face.permission.service.template.RedisSelfCacheManager;
import com.face.permission.service.template.RegisterTemplate;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.face.permission.common.constants.RedisKeyCosntant.*;
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
    PRoleMapper roleMapper;

    @Autowired
    RegisterTemplate cmsRegister;

    @Autowired
    RegisterTemplate selfRegister;

    @Autowired
    RedisSelfCacheManager redisSelfCacheManager;

    @Autowired
    IRoleService roleService;


    @Override
    public TokenDTO selfRegister(UserRequest request) {
        return selfRegister.register(request);
    }

    @Override
    public TokenDTO cmsRegister(UserRequest request) {
        return cmsRegister.register(request);
    }

    @Override
    public UserInfoVo check(String userId, UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfoVo checkAccount(UserLoginDTO request) {
        UserEnums.LoginTypeEnum loginTypeEnum = request.checkLoginName();
        PUserDO userDO = null;
        if (loginTypeEnum != UserEnums.LoginTypeEnum.LOGIN_NAME){
            userDO = userMapper.selectByLoginType(request);
            AssertUtil.isTrue( userDO != null , "找不到对应用户信息");
            String userInfoKey = USER_INFO_KEY + userDO.getuId();
            redisSelfCacheManager.set(userInfoKey, JSON.toJSONString(userDO), 60 * 3);
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userDO, userInfoVo);
        return userInfoVo;
    }

    @Override
    public PUserDO getUser(String userId){
        String userInfoKey = USER_INFO_KEY + userId;
        PUserDO userDO = redisSelfCacheManager.get(userInfoKey, PUserDO.class);
        if (userDO == null){
            userDO = userMapper.selectByUID(userId);
        }
        return userDO;
    }

    @Override
    public void setUser(PUserDO userDO){
        if (userDO == null){
            return;
        }
        String userInfoKey = USER_INFO_KEY + userDO.getuId();
        redisSelfCacheManager.set(userInfoKey, JSON.toJSONString(userDO), 5*60);

    }

    @Override
    public PAccountDO getAccount(String userId){
        String accountKey = USER_ACCOUNT_KEY + userId;
        PAccountDO accountDO = redisSelfCacheManager.get( accountKey, PAccountDO.class);

        if (accountDO == null){
            accountDO = accountMapper.selectByUserId(userId, null, null);
        }
        return accountDO;
    }

    @Override
    public TokenDTO login(UserLoginDTO request) {
        UserEnums.LoginTypeEnum loginTypeEnum = request.checkLoginName();

        PUserDO userDO = null;
        String uId = null;
        if (loginTypeEnum != UserEnums.LoginTypeEnum.LOGIN_NAME){
            userDO = userMapper.selectByLoginType(request);
            AssertUtil.isTrue( userDO != null , "登陆信息异常");
            uId = userDO.getuId();
            String userInfoKey = USER_INFO_KEY + userDO.getuId();
            redisSelfCacheManager.set(userInfoKey, JSON.toJSONString(userDO), 60 * 3);
        }

        PAccountDO accountDO = getIfAbsentAccount(uId, request.getLoginName(), request.getPassword());

        if (userDO == null){
            userDO = userMapper.selectByUID(accountDO.getUId());
            AssertUtil.isTrue( userDO != null , "登陆信息异常");
            String userInfoKey = USER_INFO_KEY + userDO.getuId();
            redisSelfCacheManager.set(userInfoKey, JSON.toJSONString(userDO), 60 * 3);
        }
        //将String数组转成Integer数组
        Integer[] roles = (Integer[]) ConvertUtils.convert(accountDO.getRoles().split(","), Integer.class);
        String token = createToken(userDO.getuId(), roles, userDO.getNickName(), request.getFromWay(), request.getPlatform());
        return new TokenDTO().setToken(token).setNickName(userDO.getNickName());
    }

    /**
     * 检查修改权限并且更新数据
     * @param request
     */
    private void checkRolesAndUpdate(UserRequest request){
        request.setMobilePhone(null);
        request.setLoginName(null);
        Integer[] roles = request.getRoles();
        List<PRoleDo> allRoles = roleService.getRolesByType(RoleTypeEnum.UPDATE.getCode());
        List<PRoleDo> userRoles = allRoles.stream().filter(pRoleDo ->
                Arrays.stream(roles).anyMatch(role -> role == pRoleDo.getRoleCode())).collect(Collectors.toList());

        //整理user需要更新的信息
        PUserDO userDO = new PUserDO();
        PAccountDO accountDO = new PAccountDO();
        userDO.setuId(request.getUserId());
        accountDO.setUId(request.getUserId());
        //本人操作  or   root管理员操作
        if (Objects.equals(request.getUserId(), request.getUid()) || Arrays.stream(roles).anyMatch(role -> role == 1)){
            userDO.setNickName(request.getNickName());
            userDO.setEmail(request.getEmail());
            userDO.setHeadPic(request.getHeadPic());
            userDO.setSex(request.getSex());
            userDO.setStatus(request.getStatus());
            if (StringUtils.isNotBlank(request.getPassword())){
                accountDO.setPassword(MD5.getMD5(request.getPassword())); //MD5加密存储
            }
            accountDO.setStatus(request.getStatus());

            if (Arrays.stream(request.getRoles()).anyMatch(role -> role == 1)){ //root账号改天改地
                accountDO.setGrade(request.getGrade());
                accountDO.setType(request.getType());
                accountDO.setRoles(JSON.toJSONString(request.getRole()));
            }
        }else {
            //非用户本人
            userRoles.forEach(role ->{
                switch (role.getRoleName()){
                    case "nickName":  userDO.setNickName(request.getNickName()); break;
                    case "email":  userDO.setEmail(request.getEmail()); break;
                    case "headPic":  userDO.setHeadPic(request.getHeadPic()); break;
                    case "sex":  userDO.setSex(request.getSex()); break;

                    case "password":  accountDO.setPassword(MD5.getMD5(request.getPassword())); break;
                    case "grade":  accountDO.setGrade(request.getGrade()); break;
                    case "type":  accountDO.setType(request.getType()); break;
                    case "roles":  accountDO.setRoles(JSON.toJSONString(request.getRole())); break;
                    default: break;
                }
            } );
        }
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(userDO) > 0, "更新用户基本信息失败");
        AssertUtil.isTrue(accountMapper.updateByPrimaryKeySelective(accountDO) > 0, "更新用户账号信息失败");
    }

    @Override
    @Transactional
    public boolean update(UserRequest request) {
        checkRolesAndUpdate(request);
        List<String> keys = new ArrayList<>(2);
        keys.add(USER_INFO_KEY + request.getUserId());
        keys.add(USER_ACCOUNT_KEY + request.getUserId());
        redisSelfCacheManager.removeArr(keys);
        return true;
    }


    @Override
    public Integer getTotal(UserQuery query) {
        return userMapper.selectUserTotal(query);
    }

    @Override
    public List<UserInfoVo> getList(UserQuery query) {
        return userMapper.selectUsers(query);
    }

    @Override
    @Transactional
    public boolean delete(UserInfo userInfo, String userId) {
        //权限检查-是否是本人/是否是root
        AssertUtil.isTrue(Objects.equals(userInfo.getUid(), userId) || Objects.equals(userInfo.getUid(), "1"), "无权限注销");
        PUserDO userDO = getUser(userId);
        AssertUtil.notNull(userDO, "用户不存在uid"+ userId);
        AssertUtil.isTrue(userMapper.updateStatus(userId, 2) > 0, "用户注销失败");
        //用户失效依旧走缓存，减轻数据库压力
        userDO.setStatus(2);
        setUser(userDO);
        return true;
    }


    @Scheduled(cron = "0 0/1 * * * ?")
    public void refreshRecommendFriends() {
        //刷新好友推荐池，刷入一千条数据 并且组装中组装5组推荐好友
        PageQuery query = new PageQuery();
        query.setPage(0);
        query.setSize(600);
        List<UserInfoVo>  userInfoVos = userMapper.selectRecommendUsers(query);
        //缓存好数据-5分钟刷新一次
        redisSelfCacheManager.set(RECOMMEND_FRIENDS, JSONObject.toJSONString(userInfoVos), 900);
    }

    private String createToken(String uId, Integer[] roles, String nickName, Integer fromWay, String platForm ){
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(uId);
        userInfo.setRoles(roles);
        userInfo.setNickName_(nickName);
        userInfo.setFromWay(fromWay);
        userInfo.setPlatform(platForm);
        try {
            return JwtUtils.createJWT(1, JwtUtils.objectToMap(userInfo), DEFAULT_TOKEN_EXPIRE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FaceServiceException(ASSERT_ERROR_CODE.getCode(), "登陆异常");
        }
    }

    private PAccountDO getIfAbsentAccount(String uId, String loginName, String password){
        AssertUtil.isTrue(!StringUtils.isAllBlank(uId, loginName), "用户信息缺失");
        PAccountDO accountDO = null;
        String psd = MD5.getMD5(password);
        if (StringUtils.isNotBlank(uId)){
            accountDO = redisSelfCacheManager.get(USER_ACCOUNT_KEY + uId, PAccountDO.class);

        }
        if (accountDO == null){
            accountDO = accountMapper.selectByUserId(uId, loginName, psd);
        }
        AssertUtil.isTrue( accountDO != null , "账户不存在");
        redisSelfCacheManager.set(USER_ACCOUNT_KEY + accountDO.getUId(), JSON.toJSONString(accountDO), 60 * 3);
        return accountDO;
    }
}
