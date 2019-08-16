package com.face.permission.common.constants.enums.user;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-09 11:29
 */
public class UserEnums {

    public enum UserFromWay{
        FROM_CMS(0, "cms后台"),
        FROM_CLIENT(1, "用户端")
        ;

        UserFromWay(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        private Integer code;
        private String msg;

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum UserStatus{
        UNKONW(0, "未知状态"),
        AVAILABLE(1, "有效"),
        UNAVAILABLE(2, "无效"),
        ;

        UserStatus(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        private Integer code;
        private String msg;

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
