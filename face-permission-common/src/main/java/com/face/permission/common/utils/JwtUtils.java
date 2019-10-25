package com.face.permission.common.utils;

import com.face.permission.common.responses.JwtCheckResult;
import io.jsonwebtoken.*;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-01 19:23
 */
public class JwtUtils {
    private static String JWT_SECERT = "YiZhongXu";

    private static String ISSUER = "root";

    private static String JWT_ERRCODE_FAIL = "签名校验失败!";

    private static String JWT_ERRCODE_EXPIRE = "token过期！";

    private static String JWT_UNKONW_ERROR = "其它错误！";

    public static String TOKEN_ILLEGAL_MSG = "token异常";
    /**
     * 5分钟过期
     */
    public static Long DEFAULT_TOKEN_EXPIRE_TIME = 120 * 60 * 1000L ;

    /**
     * 签发JWT    ----- 针对无法进行手动失效token的问题，通过版本控制
     * （分布式系统在redis中存储一个短期token[userId，jti：版本号]）
     * @param versionId   编号-版本控制
     * @param claims map 目标用户信息数据
     * @param ttlMillis 多长时间过期
     *
     *
     *     iss (issuer)：签发人
     *     exp (expiration time)：过期时间
     *     sub (subject)：主题
     *     aud (audience)：受众
     *     nbf (Not Before)：生效时间
     *     iat (Issued At)：签发时间
     *     jti (JWT ID)：编号-版本控制
     *
     *
     * @return  String
     *
     */
    public static String createJWT(Integer versionId, Map<String, Object> claims, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(versionId.toString())
                .setClaims(claims)
                .setIssuer(ISSUER)     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey); // 签名算法以及密匙
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate); // 过期时间
        }
        return builder.compact();
    }
    /**
     * 验证JWT
     * @param jwtStr
     * @return
     */
    public static JwtCheckResult validateJWT(String jwtStr) {
        JwtCheckResult checkResult = new JwtCheckResult();
        Claims claims = null;
        try {
            claims = parseJWT(jwtStr);
            //1.查询redis 查询token的版本
            //claims.getId()
            //2.token超时前3分钟重新生成
            checkResult.setCheck(true);
            checkResult.setClaims(claims);
        } catch (ExpiredJwtException e) {
            checkResult.setMsg(JWT_ERRCODE_EXPIRE);
            checkResult.setCheck(false);
        } catch (SignatureException e) {
            checkResult.setMsg(JWT_ERRCODE_FAIL);
            checkResult.setCheck(false);
        } catch (Exception e) {
            checkResult.setMsg(JWT_UNKONW_ERROR);
            checkResult.setCheck(false);
        }
        return checkResult;
    }
    //加解密的钥匙，服务端保存
    private static SecretKey generalKey() {
        byte[] encodedKey = Base64.decode(JWT_SECERT);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     *
     * 解析JWT字符串
     * @param jwt
     * @return
     * @throws Exception
     */
    private static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                setter.invoke(obj, map.get(property.getName()));
            }
        }

        return obj;
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null)
            return null;

        Map<String, Object> map = new HashMap<>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter!=null ? getter.invoke(obj) : null;
            map.put(key, value);
        }

        return map;
    }

}
