package com.face.permission.service.template;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.face.permission.common.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-10-18 17:00
 */

@Component
public class OssFaceClientManager {

    private static final Logger logger = LoggerUtil.COMMON_DEFAULT;

    @Autowired
    private String bucketName;

    @Autowired
    private OSSClient ossClient;

    /**
     * 上传 文件 到oss
     * @param filePath
     * @param file
     * @return
     */
    public String uploadFile(String filePath, File file){
        Optional<PutObjectResult> rt = null;
        try {
            rt = Optional.ofNullable(ossClient.putObject(bucketName, filePath, file));
        } catch (OSSException e) {
            logger.error("上传文件失败", e);
        } catch (ClientException e) {
            logger.error("客户端连接失败", e);
        } finally {

        }
        return rt.get().getETag();
    }

    /**
     * 上传 文件流 到oss
     * @param filePath
     * @param inputStream
     * @return
     */
    public String uploadFile(String filePath, InputStream inputStream){
        Optional<PutObjectResult> rt = null;
        try {
            rt = Optional.ofNullable(ossClient.putObject(bucketName, filePath, inputStream));
        } catch (OSSException e) {
            logger.error("上传文件流失败", e);
        } catch (ClientException e) {
            logger.error("客户端连接失败", e);
        } finally {

        }
        return rt.get().getETag();
    }

    /**
     * 上传 文件流 到oss
     * @param filePath
     * @param inputStream
     * @return
     */
    public String uploadFile(String filePath, InputStream inputStream, String originalFileName){
        Optional<PutObjectResult> rt = null;
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType("image/jpeg");
            objectMetadata.setContentDisposition("inline;filename=" + originalFileName);

            rt = Optional.ofNullable(ossClient.putObject(bucketName, filePath, inputStream, objectMetadata));
        } catch (OSSException e) {
            logger.error("上传文件流失败", e);
        } catch (ClientException e) {
            logger.error("客户端连接失败", e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return rt.get().getETag();
    }



}
