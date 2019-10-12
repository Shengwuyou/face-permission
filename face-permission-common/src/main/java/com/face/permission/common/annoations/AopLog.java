package com.face.permission.common.annoations;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-10-08 14:12
 */
public @interface AopLog {

    //请求拦截，方法类型
    MethodTypeEnum mehtodType() default MethodTypeEnum.UNKONW;

    //方法作用
    String mehtodName() default "";

    enum MethodTypeEnum{
        ADD("add"),
        UPDATE("update"),
        DELETE("delete"),
        SELECT("select"),
        UNKONW("unKnow"),
        ;

        MethodTypeEnum(String name) {
            this.name = name;
        }

        private String name;
    }

}
