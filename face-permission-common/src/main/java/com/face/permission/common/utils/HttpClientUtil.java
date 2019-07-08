package com.face.permission.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * http请求发送
 *
 * @Author huangbugan
 * @CreateDate 2015年8月26日
 */
public class HttpClientUtil {
    /**
     * https协议的请求客户端
     */
    private static CloseableHttpClient httpsClient = null;

    /**
     * http协议的请求客户端
     */
    private static CloseableHttpClient httpClient = null;

    private static final Logger log = LoggerUtil.COMMON_DEFAULT;

    private static final Lock LOCKHTTPSCLIENT = new ReentrantLock();

    private static final Lock LOCKHTTPCLIENT = new ReentrantLock();

    static {
        // 初始化客户端
        getHttpClient();
        getHttpsClient();
    }

    /**
     * 获取https的请求客户端、多服务不做任务验证
     *
     * @return
     * @Author huangbugan
     * @CreateDate 2015年8月26日
     */
    private static CloseableHttpClient getHttpsClient() {
        if (httpsClient == null) {
            try {
                LOCKHTTPSCLIENT.lock();
                if (httpsClient == null) {
                    SSLContext sslContext = new SSLContextBuilder()
                            .loadTrustMaterial(null, new TrustStrategy() {
                                // 信任所有
                                @Override
                                public boolean isTrusted(X509Certificate[] chain,
                                                         String authType) throws CertificateException {
                                    return true;
                                }
                            }).build();
                    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                        @Override
                        public boolean verify(String arg0, SSLSession arg1) {
                            // 对服务器不做验证
                            return true;
                        }
                    };
                    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                            hostnameVerifier);
                    RequestConfig.Builder builder = RequestConfig.custom();
                    // 单位毫秒
                    builder.setConnectTimeout(5000);
                    builder.setSocketTimeout(5000);
                    httpsClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
                }
            } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
                LoggerUtil.error(log, e);
            } finally {
                LOCKHTTPSCLIENT.unlock();
            }
        }
        return httpsClient;
    }

    /**
     * 获取http的客户端
     *
     * @return
     * @Author huangbugan
     * @CreateDate 2015年8月26日
     */
    private static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            try {
                LOCKHTTPCLIENT.lock();
                if (httpClient == null) {
                    RequestConfig.Builder builder = RequestConfig.custom();
                    // 单位毫秒 默认3分钟
                    builder.setConnectTimeout(300000);
                    builder.setSocketTimeout(300000);
                    HttpClientBuilder clientBuilder = HttpClients.custom()
                            .setDefaultRequestConfig(builder.build());
                    httpClient = clientBuilder.build();
                }
            } finally {
                LOCKHTTPCLIENT.unlock();
            }
        }
        return httpClient;
    }

    public static HttpResponse executePost(final List<NameValuePair> nameValuePairs, final String url) {
        return executePost(nameValuePairs, null, url, "UTF-8");
    }

    /**
     * 执行post请求
     *
     * @param nameValuePairs 请求参数、不能为空
     * @param url            请求url 不能为空
     * @param charSet        请求字符编码 不能为空
     * @return 返回HttpResponseDTO {request :true表示http请求成功、false 表示http请求失败。reponseCode：表示请求响应的http代码如200，reponseResult：响应数据}
     * @Author huangbugan
     * @CreateDate 2015年8月26日
     */
    public static HttpResponse executePost(final List<NameValuePair> nameValuePairs,
                                           List<Header> headers, final String url,
                                           final String charSet) {
        Assert.notNull(nameValuePairs, "nameValuePairs must not null");
        Assert.notNull(url, "url must not null");
        Assert.notNull(charSet, "charSet must not null");
        HttpResponse result = new HttpResponse();
        CloseableHttpResponse response = null;
        try {
            HttpPost postMethod = new HttpPost(url);

            StringBuffer stringBuffer = new StringBuffer();
            for (NameValuePair nameValuePair : nameValuePairs) {
                stringBuffer.append(nameValuePair.getName()).append("=")
                        .append(URLEncoder.encode(nameValuePair.getValue(), "utf-8"));
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs,
                    charSet);
            // urlEncodedFormEntity.setContentEncoding(charSet);
            postMethod.setEntity(urlEncodedFormEntity);
            // postMethod.setHeader(new BasicHeader("Content-Type", "application/json"));
            if (!CollectionUtils.isEmpty(headers)) {
                for (Header header : headers) {
                    postMethod.addHeader(header);
                }
            }
            // 根据url 区分请求协议
            if (StringUtils.startsWithIgnoreCase(url, "https://")) {
                response = getHttpsClient().execute(postMethod);
            } else {
                response = getHttpClient().execute(postMethod);
            }
            result = executeResponse(response, charSet);
        } catch (Exception e) {
            LoggerUtil.error(log, e);
            // TODO 去掉
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LoggerUtil.error(log, e);
                }
            }
        }
        return result;
    }

    public static HttpResponse executeGet(String url) {
        return executeGet(null, url, "UTF-8");
    }

    public static HttpResponse executeGet(List<Header> headers, final String url,
                                          final String charSet) {
        Assert.notNull(url, "url must not null");
        Assert.notNull(charSet, "charSet must not null");
        HttpResponse result = new HttpResponse();
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            if (!CollectionUtils.isEmpty(headers)) {
                for (Header header : headers) {
                    httpGet.addHeader(header);
                }
            }
            // 根据url 区分请求协议
            if (StringUtils.startsWithIgnoreCase(url, "https://")) {
                response = getHttpsClient().execute(httpGet);
            } else {
                response = getHttpClient().execute(httpGet);
            }
            result = executeResponse(response, charSet);
        } catch (Exception e) {
            LoggerUtil.error(log, e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LoggerUtil.error(log, e);
                }
            }
        }
        return result;
    }

    public static HttpResponse executePut(List<NameValuePair> nameValuePairs, List<Header> headers,
                                          final String url, final String charSet) {
        Assert.notNull(url, "url must not null");
        Assert.notNull(charSet, "charSet must not null");
        HttpResponse result = new HttpResponse();
        CloseableHttpResponse response = null;
        try {
            HttpPut httpPut = new HttpPut(url);
            if (!CollectionUtils.isEmpty(headers)) {
                for (Header header : headers) {
                    httpPut.addHeader(header);
                }
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs,
                    charSet);
            httpPut.setEntity(urlEncodedFormEntity);
            // 根据url 区分请求协议
            if (StringUtils.startsWithIgnoreCase(url, "https://")) {
                response = getHttpsClient().execute(httpPut);
            } else {
                response = getHttpClient().execute(httpPut);
            }
            result = executeResponse(response, charSet);
        } catch (Exception e) {
            LoggerUtil.error(log, e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LoggerUtil.error(log, e);
                }
            }
        }
        return result;
    }

    /**
     * 执行post请求(直接提交字符串数据)
     *
     * @param content 请求数据
     * @param url     请求url 不能为空
     * @param charSet 请求字符编码 不能为空
     * @return 返回HttpResponseDTO {request :true表示http请求成功、false 表示http请求失败。reponseCode：表示请求响应的http代码如200，reponseResult：响应数据}
     * @Author zhanghaifeng
     * @CreateDate 2015年12月25日
     */
    public static HttpResponse executePost(final String content, List<Header> headers,
                                           final String url, final String charSet) {
        Assert.notNull(url, "url must not null");
        Assert.notNull(charSet, "charSet must not null");
        HttpResponse result = new HttpResponse();
        CloseableHttpResponse response = null;
        try {
            HttpPost postMethod = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(content, charSet);
            stringEntity.setContentType("application/json");
            postMethod.setEntity(stringEntity);
            if (!CollectionUtils.isEmpty(headers)) {
                for (Header header : headers) {
                    postMethod.addHeader(header);
                }
            }
            // 根据url 区分请求协议
            if (StringUtils.startsWithIgnoreCase(url, "https://")) {
                response = getHttpsClient().execute(postMethod);
            } else {
                response = getHttpClient().execute(postMethod);
            }
            result = executeResponse(response, charSet);
        } catch (Exception e) {
            LoggerUtil.error(log, e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LoggerUtil.error(log, e);
                }
            }
        }
        return result;
    }

    public static HttpResponse postHttpsRestfulAPI(String url, String json, final String charSet) {
        Assert.notNull(url, "url must not null");
        Assert.notNull(charSet, "charSet must not null");
        HttpResponse result = new HttpResponse();
        CloseableHttpResponse response = null;
        try {
            HttpPost postMethod = new HttpPost(url);

            postMethod.addHeader("Content-Type", "application/json");
            HttpEntity item = new ByteArrayEntity(json.getBytes());
            postMethod.setEntity(item);
            // 根据url 区分请求协议
            if (StringUtils.startsWithIgnoreCase(url, "https://")) {
                response = getHttpsClient().execute(postMethod);
            } else {
                response = getHttpClient().execute(postMethod);
            }
            result = executeResponse(response, charSet);
        } catch (Exception e) {
            LoggerUtil.error(log, e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LoggerUtil.error(log, e);
                }
            }
        }
        return result;

    }

    public static void main(String[] args) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("data_json",
                "D6D1JG6/J9iirb/zLKyHedpDA/JELSHOc7Bd/v2jQcgI0tnTi/BXorKnOyGQfrVESAcbmd82zu97rEqehK281J2sL3M9P9oqltSCfJWmtr1AF+2lEyk0i6vxkx2Arx5L205W//SPlJ2L/vW7L7wdbThY09zQu2hhRuYDzDFIQOg8GSjkGlDw/9glSdPGo+mh/U6WzfcWJI89P4O0SjvKKtR5nNtpkEAl8glEox8A66TzjkaBiBHd8VmbOHL1ACpglukpIEUvc8BcjZ2jI3z+H+ADhg9PZQvfxTDRdXtyxVTIxQo8IWaBPkonYIX4G6jjBg2QLDYRHLKDIWQ4w/KRTg=="));
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("api-ver", "V1"));
        headers.add(new BasicHeader("common-json",
                "{'sign':'27D250E64AD1BA9DAF0B86DD494F372E','p':'iphone','timestamp':'1503913352034','c_id':'ios','d_ml':'iPhone 6','encrypt_model':'rsa','token':'e5c9fe3f85e8631072b3ec4444a44a24','ver':'1.9.0','d_id':'F0B3AD2C-1D92-4BFB-984E-814ED23D6123','lat':'30.302024','lng':'120.161359','d_sys_ver':'10.2','d_bd':'apple'}"));
        System.out.println(JSON.toJSONString(executePost(nameValuePairs, headers,
                "http://api.wind.vpdai.com/wind-api/api/mobile/servicePwd/auth", "utf-8")));

    }

    private static HttpResponse executeResponse(CloseableHttpResponse response, String charSet) {
        HttpResponse result = new HttpResponse();
        result.setReponseCode(response.getStatusLine().getStatusCode());
        HttpEntity responseBody = response.getEntity();
        try {
            result.setReponseResult(EntityUtils.toString(responseBody, charSet));
            EntityUtils.consume(responseBody);
            result.setRequest(true);
        } catch (ParseException | IOException e) {
            LoggerUtil.error(log, e);
        }
        return result;
    }

    public static class HttpResponse {

        /**
         * 请求是否成功
         */
        private boolean request = false;

        /**
         * 响应结果
         */
        private String reponseResult;

        /**
         * http响应代码
         */
        private int reponseCode;

        public boolean isRequest() {
            return request;
        }

        public void setRequest(boolean request) {
            this.request = request;
        }

        public String getReponseResult() {
            return reponseResult;
        }

        public void setReponseResult(String reponseResult) {
            this.reponseResult = reponseResult;
        }

        public int getReponseCode() {
            return reponseCode;
        }

        public void setReponseCode(int reponseCode) {
            this.reponseCode = reponseCode;
        }

    }

}
