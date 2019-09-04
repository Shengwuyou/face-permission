package com.face.permission.server.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.face.permission.common.constants.ReturnConstant;
import com.face.permission.common.constants.enums.SystemErrorEnum;
import com.face.permission.common.constants.enums.user.UserErrorEnum;
import com.face.permission.common.exceptions.FaceServiceException;
import com.face.permission.common.responses.ResultInfo;
import com.face.permission.common.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;
import java.util.List;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 17:35
 */
@RestControllerAdvice
public class PermissionExceptionHandler {
    Logger logger = LoggerUtil.COMMON_DEFAULT;


    //===================【请求参数异常处理器】========================
    @ExceptionHandler(value = {UnexpectedTypeException.class})
    public ResultInfo<?> validException(UnexpectedTypeException e) {
        String msg = String.format(UserErrorEnum.PARAM_ILLAGEL.getMsg(), e.getMessage());
        logger.error(msg, e);
        return ResultInfo.error(UserErrorEnum.PARAM_ILLAGEL.getCode(), msg);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResultInfo<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BeanPropertyBindingResult bindingResult = (BeanPropertyBindingResult) e.getBindingResult();
        List<FieldError> errors2 = bindingResult.getFieldErrors();
        JSONArray arr = new JSONArray();
        errors2.forEach(error -> {
            JSONObject param = new JSONObject();
            param.put("validFiled", error.getField());
            param.put("validMessage", error.getDefaultMessage());
            arr.add(param);
        });
        logger.error(arr.toJSONString(), e);
        return ResultInfo.error(UserErrorEnum.PARAM_ILLAGEL.getCode(), arr.getJSONObject(0).getString("validMessage"));
    }



    //===================【断言异常处理器】========================
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResultInfo<?> illegalArgumentException(IllegalArgumentException e) {
        logger.error(e.getMessage(), e);
        return ResultInfo.error(SystemErrorEnum.ASSERT_ERROR_CODE.getCode(), e.getMessage());
    }

    //===================【断言异常处理器】========================
    @ExceptionHandler(value = {FaceServiceException.class})
    public ResultInfo<?> faceServiceException(FaceServiceException e) {
        logger.error(e);
        return ResultInfo.error(ReturnConstant.UNKNOW_ERROR, e.getMessage());
    }


    //===================【未知异常处理器】========================
    @ExceptionHandler(value = {Exception.class})
    public ResultInfo<?> Exception(Exception e) {
        logger.error(e);
        return ResultInfo.error(ReturnConstant.UNKNOW_ERROR, e.getMessage());
    }
}
