package com.face.permission.server.config;

import com.face.permission.common.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @Description 重置request的 stream流，使用后解决只能读取一次的问题 -- 配合ServletRequestStreamFilter使用
 * @Author xuyizhong
 * @Date 2019-08-07 11:02
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private Logger logger = LoggerUtil.HTTP_REMOTE;

    private final byte[] body;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        String sessionStream = getBodyString(request);
        body = sessionStream.getBytes(Charset.forName("UTF-8"));

    }

    public String getBodyString(HttpServletRequest request){
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            StringBuffer content = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
            return content.toString();
        } catch (IOException e) {
            logger.info("请求参数处理异常，请求接口：" + request.getMethod());
        } finally {
            try {
                request.getReader().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }
}
