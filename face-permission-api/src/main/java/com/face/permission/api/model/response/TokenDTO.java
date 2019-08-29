package com.face.permission.api.model.response;
/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 15:41
 */
public class TokenDTO {

    /**
     * 请求令牌
     */
    private String token;
    /**
     * 用户昵称
     */
    private String nickName;

    public String getToken() {
        return token;
    }

    public TokenDTO setToken(String token) {
        this.token = token;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public TokenDTO setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }
}
