package com.face.permission.common.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 登陆拦截注解
 * @Author xuyizhong
 * @Date 2019-08-01 17:08
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginIntercept {

    //是否需要登陆，默认为需要
    boolean require() default true;

}
