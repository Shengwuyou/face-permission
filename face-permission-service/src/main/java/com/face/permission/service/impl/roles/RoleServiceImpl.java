package com.face.permission.service.impl.roles;

import com.alibaba.fastjson.JSON;
import com.face.permission.mapper.dao.PRoleMapper;
import com.face.permission.mapper.dao.PUserMapper;
import com.face.permission.mapper.domain.PRoleDo;
import com.face.permission.service.interfaces.roles.IRoleService;
import com.face.permission.service.template.RedisSelfCacheManager;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.face.permission.common.constants.RedisKeyCosntant.*;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-05 17:34
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    PUserMapper userMapper;

    @Autowired
    PRoleMapper roleMapper;

    @Autowired
    RedisSelfCacheManager redisSelfCacheManager;

    @Override
    public List<PRoleDo> getAllRoles() {
        String roleTypeKey = ROLE_ALL_KEY ;
        List<PRoleDo> roleDos = redisSelfCacheManager.getList(roleTypeKey, PRoleDo.class);
        if (roleDos == null){
            roleDos = roleMapper.selectAll();
            if (CollectionUtils.isNotEmpty(roleDos)) {
                redisSelfCacheManager.set(roleTypeKey, JSON.toJSONString(roleDos), 30 * 60);
            }
        }
        return roleDos;
    }

    @Override
    public PRoleDo getRoleByCode(Integer code) {
        return null;
    }

    @Override
    public List<PRoleDo> getAllCanSetRoles() {
        String roleTypeKey = CAN_SET_ROLE_ALL_KEY;
        List<PRoleDo> roleDos = redisSelfCacheManager.getList(roleTypeKey, PRoleDo.class);
        if (roleDos == null){
            roleDos = roleMapper.selectNotType(0);
            if (CollectionUtils.isNotEmpty(roleDos)) {
                redisSelfCacheManager.set(roleTypeKey, JSON.toJSONString(roleDos), 30 * 60);
            }
        }
        return roleDos;
    }

    @Override
    public List<PRoleDo> getRolesByType(Integer type) {
        String roleTypeKey = ROLE_TYPE_KEY + type;
        List<PRoleDo> roleDos = redisSelfCacheManager.getList(roleTypeKey, PRoleDo.class);
        if (roleDos == null){
            roleDos = roleMapper.selectByType(type);
            if (CollectionUtils.isNotEmpty(roleDos)) {
                redisSelfCacheManager.set(roleTypeKey, JSON.toJSONString(roleDos), 30 * 60);
            }
        }
        return roleDos;
    }
}
