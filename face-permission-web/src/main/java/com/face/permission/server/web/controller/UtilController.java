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

    /**
     * 上传和修改用户头像
     * @return
     */
    @PostMapping(value = "/upload/headImg")
    @LoginIntercept
    public ResultInfo<?> uploadUserHead(MultipartFile multipartFile) {
        JSONObject rt = new JSONObject();
        UserInfo userInfo = ThreadLocalUser.getUserInfo();
        try {
            AssertUtil.notNull(userInfo.getUid(), "用户信息异常");
            boolean isImage = ImageUtil.isImage(multipartFile.getInputStream());
            AssertUtil.isTrue(isImage, "格式异常，请上传图片文件");
            AssertUtil.isTrue(multipartFile.getSize() < 1024 * 1024, "文件不能大于1M");

            String fileDir = String.format(FileDirEnum.USER_HEAD.getFileDir(), userInfo.getUid());
            String fileName = fileDir + userInfo.getUid() + ".jpg";
            //压缩上传oss
            String ETag = ossFaceClientManager.uploadFile(fileName, ImageUtil.resize(multipartFile, 100));

            //文件已经上传成功 开始头像设置
            userService.updateHeadPic(userInfo.getUid(), String.format(PIC_OSS_NGINX_IP, fileName));

            rt.put("ossFileUrl", String.format(PIC_OSS_NGINX_IP, fileName));
            rt.put("ossETag", ETag);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultInfo.success(rt);
    }
}
