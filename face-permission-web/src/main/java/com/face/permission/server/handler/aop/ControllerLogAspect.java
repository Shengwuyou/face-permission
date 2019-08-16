package com.face.permission.server.handler.aop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.face.permission.common.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-02 19:19
 */

@Aspect
public class ControllerLogAspect {

    private Logger logger = LoggerUtil.HTTP_REMOTE;

    @Pointcut("execution(public * com.face.permission.server.web.controller..*.*(..))")
    public void logPoint(){}

    @Before("logPoint()")
    public void doBefore(JoinPoint joinPoint){
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容

        logger.info("请求方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

        // 获取参数, 只取自定义的参数, 自带的HttpServletRequest, HttpServletResponse不管
        if (joinPoint.getArgs().length > 0) {
            for (Object o : joinPoint.getArgs()) {
                if (o instanceof HttpServletRequest || o instanceof HttpServletResponse) {
                    continue;
                }
                logger.info("请求参数 : " + JSON.toJSONString(o));
            }
        }
    }

//    private void logRemote(HttpServletRequest request){
//        JSONObject req = new JSONObject();
//        req.put("MethodType", request.getMethod());
//        req.put("请求接口", request.getRequestURI());
//        req.put("请求参数", request.getParameterMap());
//        BufferedReader reader = null;
//        try {
//            reader = request.getReader();
//            StringBuffer content = new StringBuffer();
//            String line;
//            while ((line = reader.readLine()) != null){
//                content.append(line);
//            }
//            req.put("请求体", content.toString());
//        } catch (IOException e) {
//            logger.info("请求参数处理异常，请求接口：" + request.getMethod());
//        } finally {
//            try {
//                //这里不能关闭流，不然后面读取不到了,并且还需要重置，解决httpServletRequest只能读取一次的问题
//                request.getReader().reset();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        logger.info(req.toJSONString());
//    }

    @AfterReturning
    public void afterReturn(){

    }
}
