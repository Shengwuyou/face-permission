package com.face.permission.common.responses;

import io.jsonwebtoken.Claims;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-01 19:30
 */
public class JwtCheckResult {
    /**
     *  token校验结果 true / false
     */
    private boolean check;
    /**
     * 结果描述
     */
    private String msg;
    /**
     * 结果返回body
     */
    private Claims claims;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户权限集合
     */
    private Integer[] roles;

    public JwtCheckResult() {
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getUserId() {
        userId = claims.get("userId", String.class);
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer[] getRoles() {
        roles = claims.get("roles", Integer[].class);
        return roles;
    }

    public void setRoles(Integer[] roles) {
        this.roles = roles;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }
}
