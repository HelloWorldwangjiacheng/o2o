package com.imooc.o2o.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class ImageUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();

    public static String generateThumbnail(CommonsMultipartFile thumbnail, String targetAddress){
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail);
        makeDirPath(targetAddress);
        String relativeAddress = targetAddress + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath()+relativeAddress);
        try {
            Thumbnails.of(thumbnail.getInputStream())
                    .size(200,200)
//                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
                    .outputQuality(0.8f)
                    .toFile(dest);
        } catch (Exception ioe){
            ioe.printStackTrace();
            throw new RuntimeException("创建缩略图失败："+ioe.getMessage());
        }
        return relativeAddress;
    }

//    public static String generateThumbnail(File thumbnail, String targetAddress){
//        String realFileName = getRandomFileName();
//        String extension = getFileExtension(thumbnail);
//        makeDirPath(targetAddress);
//        String relativeAddress = targetAddress + realFileName + extension;
//
//        File dest = new File(PathUtil.getImgBasePath() + relativeAddress);
//        try {
//            Thumbnails.of(thumbnail)
//                    .size(200,200)
//                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
//                    .outputQuality(0.8f)
//                    .toFile(dest);
//        } catch (Exception ioe){
//            ioe.printStackTrace();
//        }
//        return relativeAddress;
//    }

//    public static String generateThumbnail(InputStream thumbnail, String fileName, String targetAddress){
//        String realFileName = getRandomFileName();
//        String extension = getFileExtension(fileName);
//        makeDirPath(targetAddress);
//        String relativeAddress = targetAddress + realFileName + extension;
//
//        File dest = new File(PathUtil.getImgBasePath() + relativeAddress);
//        try {
//            Thumbnails.of(thumbnail)
//                    .size(200,200)
//                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
//                    .outputQuality(0.8f)
//                    .toFile(dest);
//        } catch (Exception ioe){
//            ioe.printStackTrace();
//        }
//        return relativeAddress;
//    }

    /**
     * 创建目标路径所涉及到的目录，即/home/work
     * @param targetAddress
     */
    private static void makeDirPath(String targetAddress) {
        String realFileParentPath = PathUtil.getImgBasePath()+targetAddress;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件流的拓展名
//     * @param cFile
     * @return
     */
    private static String getFileExtension(CommonsMultipartFile cFile) {
        String originalFilename = cFile.getOriginalFilename();
//        String originalFilename = cFile.getName();
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

//    private static String getFileExtension(String fileName) {
//        return fileName.substring(fileName.lastIndexOf("."));
//    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     * @return
     */
    public static String getRandomFileName() {
        //获取随机的五位数
        int randomNum = r.nextInt(89999)+10000; //randomNum在10000和99999之间取值刚好是5位数
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr+randomNum;
    }


    public static void main(String[] args) throws IOException {
        Thumbnails.of(new File("C:\\Users\\w1586\\Desktop\\heihei.jpg"))
                .size(200,200)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+"\\watermark.jpg")),0.25f)
                .outputQuality(0.8f)
                .toFile("C:\\Users\\w1586\\Desktop\\hei.jpg");
    }
}
