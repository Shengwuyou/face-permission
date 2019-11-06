package com.face.permission.server.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.face.permission.common.annoations.LoginIntercept;
import com.face.permission.common.constants.enums.FileDirEnum;
import com.face.permission.common.model.request.user.ThreadLocalUser;
import com.face.permission.common.model.request.user.UserInfo;
import com.face.permission.common.model.request.user.UserRequest;
import com.face.permission.common.responses.ResultInfo;
import com.face.permission.common.utils.AssertUtil;
import com.face.permission.common.utils.ImageUtil;
import com.face.permission.service.interfaces.user.IUserService;
import com.face.permission.service.template.OssFaceClientManager;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-10-22 15:13
 */

@Api(tags = "工具管理类")
@RestController
public class UtilController {


    private String PIC_OSS_NGINX_IP = "http://121.40.124.14:8090/%s";

    @Autowired
    private OssFaceClientManager ossFaceClientManager;

    @Autowired
    private IUserService userService;


}
