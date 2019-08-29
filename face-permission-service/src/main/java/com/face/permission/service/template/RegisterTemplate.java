package com.face.permission.service.template;

import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.common.exceptions.FaceServiceException;
import com.face.permission.common.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import static com.face.permission.common.constants.RedisKeyCosntant.REGISTER_LOCK_KEY;
import static com.face.permission.common.constants.enums.SystemErrorEnum.ASSERT_ERROR_CODE;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-16 09:56
 */
public abstract class RegisterTemplate {


    @Autowired
    RedisSelfCacheManager redisSelfCacheManager;

    public String register(UserRequest request){
        String key = REGISTER_LOCK_KEY+ request.getMobilePhone();
        if (redisSelfCacheManager.lock(key,  3L)){
            try {
                //1.检查参数 checkParam
                checkParam(request);
                //2.信息入库
                dataStorage(request);
                //3.token创建，redis缓存
                String token = createToken(request);
                return token;
            } finally {
                redisSelfCacheManager.unLock(key);
            }
        }else{
            throw new FaceServiceException(ASSERT_ERROR_CODE.getCode(), "系统繁忙，请稍后重试");
        }
    }

    /**
     * 参数校验
     * @return
     */
    public abstract void checkParam(UserRequest request);

    /**
     * 信息入库
     */
    public abstract void dataStorage(UserRequest request);

    /**
     * 分配请求认证令牌
     * @return
     */
    public abstract String createToken(UserRequest request);

}
