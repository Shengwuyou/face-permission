package com.face.permission.common.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * 图片压缩工具类
 *
 * @author lnj
 * createTime 2018-10-19 15:31
 **/
public class ImageUtil {

    // 图片默认缩放比率
    private static final double DEFAULT_SCALE = 0.8d;

    // 缩略图后缀
    private static final String SUFFIX = "-thumbnail";


    /**
     * * 将图片文件输出到指定的路径，并可设定压缩质量
     *
     * @param outImgPath
     * @param newImg
     * @param per
     * @author cevencheng
     */
    private static void outImage(String outImgPath, BufferedImage newImg, float per) {
        // 判断输出的文件夹路径是否存在，不存在则创建
        File file = new File(outImgPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // 输出到文件流
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outImgPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(newImg);
            // 压缩质量
            jep.setQuality(per, true);
            encoder.encode(newImg, jep);
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 图片剪切工具方法
     *
     * @param srcfile 源图片
     * @param outfile 剪切之后的图片
     * @param x 剪切顶点 X 坐标
     * @param y 剪切顶点 Y 坐标
     * @param width 剪切区域宽度
     * @param height 剪切区域高度
     *
     * @throws IOException
     * @author cevencheng
     */
    public static void cut(File srcfile, File outfile, int x, int y, int width, int height) throws IOException {
        FileInputStream is = null;
        ImageInputStream iis = null;
        try {
            // 读取图片文件
            is = new FileInputStream(srcfile);

            /*
             * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 声称能够解码指定格式。
             * 参数：formatName - 包含非正式格式名称 .（例如 "jpeg" 或 "tiff"）等 。
             */
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("jpg");
            ImageReader reader = it.next();
            // 获取图片流
            iis = ImageIO.createImageInputStream(is);

            /*
             * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索’。
             * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
             */
            reader.setInput(iis, true);

            /*
             * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
             * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 将从其 ImageReader 实现的
             * getDefaultReadParam 方法中返回 ImageReadParam 的实例。
             */
            ImageReadParam param = reader.getDefaultReadParam();

            /*
             * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
             * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
             */
            Rectangle rect = new Rectangle(x, y, width, height);

            // 提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);

            /*
             * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
             * BufferedImage 返回。
             */
            BufferedImage bi = reader.read(0, param);

            // 保存新图片
            ImageIO.write(bi, "jpg", outfile);
        } finally {
            if (is != null) {
                is.close();
            }
            if (iis != null) {
                iis.close();
            }
        }
    }

    public static InputStream resize(MultipartFile multipartFile, int maxLength){
        try {
            FileInputStream in = (FileInputStream) multipartFile.getInputStream();
            return resize(ImageIO.read(in), maxLength);
        } catch (IOException e) {
            System.out.println("读取图片文件出错！" + e.getMessage());
        }
        return null;
    }

    /**
     * <b>
     * 指定长或者宽的最大值来压缩图片
     * 	推荐使用此方法
     * </b>
     * @param maxLength
     *            :长或者宽的最大值
     * @author cevencheng
     */
    public static InputStream resize(BufferedImage src, int maxLength){
        int old_w = src.getWidth();
        // 得到源图宽
        int old_h = src.getHeight();
        // 得到源图长
        int new_w = 0;
        // 新图的宽
        int new_h = 0;
        // 新图的长
        BufferedImage tempImg = new BufferedImage(old_w, old_h,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = tempImg.createGraphics();
        g.setColor(Color.white);
        // 从原图上取颜色绘制新图
        g.fillRect(0, 0, old_w, old_h);
        g.drawImage(src, 0, 0, old_w, old_h, Color.white, null);
        g.dispose();
        // 根据图片尺寸压缩比得到新图的尺寸
        if (old_w > old_h) {
            // 图片要缩放的比例
            new_w = maxLength;
            new_h = (int) Math.round(old_h * ((float) maxLength / old_w));
        } else {
            new_w = (int) Math.round(old_w * ((float) maxLength / old_h));
            new_h = maxLength;
        }
        BufferedImage newImg = new BufferedImage(new_w, new_h,
                BufferedImage.TYPE_INT_RGB);
        newImg.getGraphics().drawImage(
                tempImg.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0,
                0, null);
        // 调用方法输出图片文件
        return bufferedImageToInputStream(newImg);
    }

    /**
     * 将BufferedImage转换为InputStream
     * @param image
     * @return
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {

        }
        return null;
    }



    /**
     * 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return 后缀
     */
    public String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase("jpeg") ||
                FilenameExtension.equalsIgnoreCase("jpg") ||
                FilenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase("pptx") ||
                FilenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase("docx") ||
                FilenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }


    /**
     * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片，这是一种非常简单的方式。
     * @param inputStream
     * @return
     */
    public static boolean isImage(InputStream inputStream) {
        if ( inputStream == null) {
            return false;
        }
        Image img = null;
        try {
            img = ImageIO.read(inputStream);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            img = null;
        }
    }
}
