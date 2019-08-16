package com.face.permission.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.face.permission.common.utils.AssertUtil;
import com.face.permission.common.utils.LoggerUtil;
import com.face.permission.server.config.ThreadLocalUser;
import com.face.permission.server.config.annoations.LoginIntercept;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;

import static com.face.permission.common.constants.enums.SystemErrorEnum.UNKONW_HTTP_REQUEST;
import static com.face.permission.server.config.ThreadLocalUser.checkToken;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-01 14:31
 */
public class LoginInterceptorHandler implements HandlerInterceptor {
    private Logger logger = LoggerUtil.HTTP_REMOTE;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.记录请求日志
        logRemote(request);
        //2.未知请求，直接拦截
        if(handler == null){
            return false;
        }
        AssertUtil.isTrue(handler instanceof HandlerMethod, UNKONW_HTTP_REQUEST.getCode(), UNKONW_HTTP_REQUEST.getMsg());

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method =  handlerMethod.getMethod();
        //检查请求是否登陆----规则见JWT
        String token = request.getHeader("token");
        //2.检查是否存在登陆拦截
        LoginIntercept loginIntercept = method.getAnnotation(LoginIntercept.class);
        if (loginIntercept != null && loginIntercept.require()){
            checkToken(token);
        }
        //3.拦截器前置判断通过，进入
        return true;
    }

    /**
     * 只有preHandle返回true才需要执行本方法
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
    }

    /**
     * 只有preHandle返回true才需要执行本方法
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("本次请求结束：开始清除当前请求的userInfo");
        ThreadLocalUser.cleanThreadLocal();
    }

    /**
     * 类型：remote - 日志记录请求的接口/参数/地址
     * @param request
     */
    private void logRemote(HttpServletRequest request){
        JSONObject req = new JSONObject();
        req.put("MethodType", request.getMethod());
        req.put("请求接口", request.getRequestURI());
        req.put("请求参数", request.getParameterMap());
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            StringBuffer content = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
            req.put("请求体", content.toString());
        } catch (IOException e) {
            logger.info("请求参数处理异常，请求接口：" + request.getMethod());
        } finally {
            try {
                request.getReader().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info(req.toJSONString());
    }


}
