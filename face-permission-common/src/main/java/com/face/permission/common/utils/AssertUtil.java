package com.face.permission.common.utils;

import com.face.permission.common.exceptions.FaceServiceException;

import static com.face.permission.common.constants.enums.SystemErrorEnum.ASSERT_ERROR_CODE;

/**
 * @Description  模仿的是 org.springframework.util.Assert
 * @Author xuyizhong
 * @Date 2019-07-08 19:34
 */
public class AssertUtil {

    public static void error( Long code, String message){
        throw new FaceServiceException(code, message);
    }

    public static void state(boolean expression, Long code, String message){
        if (!expression){
            throw new FaceServiceException(code, message);
        }
    }

    public static void isTrue(boolean expression, Long code, String message){
        if (!expression){
            throw new FaceServiceException(code, message);
        }
    }

    public static void isNull(Object object, Long code, String message){
        if (object != null){
            throw new FaceServiceException(code, message);
        }
    }

    public static void notNull(Object object, Long code, String message){
        if (object == null){
            throw new FaceServiceException(code, message);
        }
    }


    public static void error( String message){
        throw new FaceServiceException(ASSERT_ERROR_CODE.getCode(), message);
    }

    public static void state(boolean expression, String message){
        if (!expression){
            throw new FaceServiceException(ASSERT_ERROR_CODE.getCode(), message);
        }
    }

    public static void isTrue(boolean expression, String message){
        if (!expression){
            throw new FaceServiceException(ASSERT_ERROR_CODE.getCode(), message);
        }
    }

    public static void isNull(Object object, String message){
        if (object != null){
            throw new FaceServiceException(ASSERT_ERROR_CODE.getCode(), message);
        }
    }

    public static void notNull(Object object, String message){
        if (object == null){
            throw new FaceServiceException(ASSERT_ERROR_CODE.getCode(), message);
        }
    }
}
