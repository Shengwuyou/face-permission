package com.face.permission.service.interfaces.roles;

import com.face.permission.mapper.domain.PRoleDo;

import java.util.List;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-08 09:39
 */
public interface IRoleService {

    /**
     * 根据类型获取所有权限
     * @return
     */
    List<PRoleDo> getAllRoles();

    /**
     * 根据code获取权限
     * @param code
     * @return
     */
    PRoleDo getRoleByCode(Integer code);

    /**
     * 获取所有允许系统授权给非root账户的所有权限
     * @return
     */
    List<PRoleDo> getAllCanSetRoles();
    /**
     * 根据类型获取权限
     * @param type
     * @return
     */
    List<PRoleDo> getRolesByType(Integer type);
}
