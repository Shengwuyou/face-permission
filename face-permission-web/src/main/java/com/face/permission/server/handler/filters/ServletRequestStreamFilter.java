package com.face.permission.server.handler.filters;

import com.face.permission.common.utils.LoggerUtil;
import com.face.permission.server.config.BodyReaderHttpServletRequestWrapper;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description 重置request的 stream流，使用后解决只能读取一次的问题 -- 配合 BodyReaderHttpServletRequestWrapper使用
 * @Author xuyizhong
 * @Date 2019-08-07 11:00
 */
@WebFilter(filterName = "servletRequestStreamFilter", urlPatterns = "/*")
public class ServletRequestStreamFilter implements Filter {
    private Logger logger = LoggerUtil.HTTP_REMOTE;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("初始化ServletRequestStreamFilter过滤器");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("执行ServletRequestStreamFilter过滤器");

        // 防止流读取一次后就没有了, 所以需要将流继续写出去
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);

        filterChain.doFilter(requestWrapper, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("销毁ServletRequestStreamFilter过滤器");

    }
}
