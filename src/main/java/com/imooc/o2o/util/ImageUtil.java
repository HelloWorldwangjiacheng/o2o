package com.imooc.o2o.util;

import ch.qos.logback.core.util.FileUtil;
import com.imooc.o2o.dto.ImageHolder;
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

    public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
        //获取不重复的随机名
        String realFileName = getRandomFileName();
        //获取文件扩展名如png、jpg等
        String extension = getFileExtension(thumbnail.getImageName());
        //如果目标路径不存在，则自动创建
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(337, 640)
                    .outputQuality(0.9f)
                    .toFile(dest);
        } catch (IOException e) {
            throw new RuntimeException("创建缩略图失败：" + e.toString());
        }
        return relativeAddr;
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

    public static String generateThumbnail(ImageHolder thumbnail, String targetAddress){
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddress);
        String relativeAddress = targetAddress + realFileName + extension;

        File dest = new File(PathUtil.getImgBasePath() + relativeAddress);
        try {
            Thumbnails.of(thumbnail.getImage())
                    .size(200,200)
//                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"\\watermark.jpg")),0.25f)
                    .outputQuality(0.8f)
                    .toFile(dest);
        } catch (Exception ioe){
            ioe.printStackTrace();
        }
        return relativeAddress;
    }

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
//    private static String getFileExtension(CommonsMultipartFile cFile) {
//        String originalFilename = cFile.getOriginalFilename();
////        String originalFilename = cFile.getName();
//        return originalFilename.substring(originalFilename.lastIndexOf("."));
//    }

    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

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

    /**
     * storePath是文件的路径还是目录的路径，
     * 如果storePath是文件路径，则删除该文件
     * 如果storePath是目录路径则删除该目录下的所有文件
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
        if (fileOrPath.exists()){
            if (fileOrPath.isDirectory()){
                File files[] = fileOrPath.listFiles();
                for (int i=0; i<files.length;i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }


    public static void main(String[] args) throws IOException {
        Thumbnails.of(new File("C:\\Users\\w1586\\Desktop\\heihei.jpg"))
                .size(200,200)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+"\\watermark.jpg")),0.25f)
                .outputQuality(0.8f)
                .toFile("C:\\Users\\w1586\\Desktop\\hei.jpg");
    }


}
