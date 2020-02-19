package com.imooc.o2o.util;

public class PathUtil {
    private static String seperator = System.getProperty("file.separator");

    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")){
            basePath = "C:/Users/w1586/Desktop/images/";
        } else{
            basePath="/home/wjc/image/";
        }
        basePath = basePath.replace("/",seperator);
        return basePath;
    }

    public static String getShopImagePath(Long shopId){
        String imagePath = "upload/item/shop/"+shopId+"/";
        return imagePath.replace("/",seperator);
    }
}
