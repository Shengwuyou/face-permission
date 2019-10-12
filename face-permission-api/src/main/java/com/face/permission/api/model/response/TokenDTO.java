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
     * 用户id
     */
    private String uId;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像url
     */
    private String headPic;

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

    public String getuId() {
        return uId;
    }

    public TokenDTO setuId(String uId) {
        this.uId = uId;
        return this;
    }

    public String getHeadPic() {
        return headPic;
    }

    public TokenDTO setHeadPic(String headPic) {
        this.headPic = headPic;
        return this;
    }
}
