package com.face.permission.common.constants.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-09 11:29
 */

public class UserEnums {

    public enum UserFromWay{
        FROM_CMS(0, "cms后台"),
        FROM_CLIENT(1, "用户端");

        private Integer code;
        private String msg;

        UserFromWay(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }



    public enum UserStatus{
        UNKONW(0, "未知状态"),
        AVAILABLE(1, "有效"),
        UNAVAILABLE(2, "无效");

        private Integer code;
        private String msg;

        UserStatus(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }
    }


    public enum LoginTypeEnum{
        LOGIN_NAME(0, "用户名登陆"),
        MOBILE(1, "手机号登陆"),
        EMAIL(2, "邮箱登陆");

        private Integer code;
        private String msg;

        LoginTypeEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
