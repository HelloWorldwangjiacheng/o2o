package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution shopExecution = new ShopExecution();
        if (shopList!=null || shopList.size()!=0){
            shopExecution.setShopList(shopList);
            shopExecution.setCount(count);
        }else{
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return shopExecution;
    }

    @Override
    public Shop getByShopId(Long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail)
            throws ShopOperationException {
        //1.判断是否需要处理图片
        if (shop == null || shop.getShopId() == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else {
            // 判断是否需要处理图片
            try{
                if (thumbnail.getImage() != null &&
                        thumbnail.getImageName() != null &&
                        !"".equals(thumbnail.getImageName()))
                {
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() != null){
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
//                    addShopImg(shop, shopImgInputStream, fileName);
                    addShopImg(shop, thumbnail);
                }
                //2.更新店铺信息
                shop.setLastEditTime(new Date());
                int effectNum = shopDao.updateShop(shop);
                if (effectNum<=0){
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                }else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS,shop);
                }
            }catch (Exception e){
                throw new ShopOperationException("modifyShop error:"+e.getMessage());
            }

        }

    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点： 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     * 4.因为使用事务那在抛出异常必须使用RuntimeException
     */
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
        //传入的参数是否合法,空值判断
        if (shop == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            //给店铺信息赋初值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectedShopNum = shopDao.insertShop(shop);
            //判断插入是否有效
            if (effectedShopNum <= 0){
                throw new ShopOperationException("店铺创建失败");
            }else {
                if (thumbnail.getImage() !=null){
                    //存储图片
                    try {
//                        addShopImg(shop, shopImgInputStream, fileName);
                        addShopImg(shop, thumbnail);
                    }catch (Exception ex){
                        throw new ShopOperationException("addShopImg error:"+ ex.getMessage());
                    }
                    //更新店铺的图片地址
                    effectedShopNum = shopDao.updateShop(shop);
                    if (effectedShopNum<=0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }

        }catch (Exception e){
            throw new ShopOperationException("addShop error:"+e.getMessage());
        }
        //check待审核
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    /**
     * 添加商店图片
     * @param shop
//     * @param shopImg
     */
//    private void addShopImg(Shop shop, File shopImg) {
//        //获取shop图片目录的相对值路径
//        String dest = PathUtil.getShopImagePath(shop.getShopId());
//        FileItem fileItem = createFileItem(shopImg, "shopImgName");
//        CommonsMultipartFile commonsMultipartFile = new CommonsMultipartFile(fileItem);
//        String shopImgAddress = ImageUtil.generateThumbnail(commonsMultipartFile,dest);
//        shop.setShopImg(shopImgAddress);
//    }

//    private void addShopImg(Shop shop, CommonsMultipartFile shopImg) {
//        //获取shop图片目录的相对值路径
//        String dest = PathUtil.getShopImagePath(shop.getShopId());
//        String shopImgAddress = ImageUtil.generateThumbnail(shopImg,dest);
//        shop.setShopImg(shopImgAddress);
//    }

    private void addShopImg(Shop shop, ImageHolder thumbnail) {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
//        String shopImgAddress = ImageUtil.generateThumbnail(thumbnail.getImage(), thumbnail.getImageName(), dest);
        String shopImgAddress = ImageUtil.generateThumbnail(thumbnail, dest);
        shop.setShopImg(shopImgAddress);
    }

    /**
     * 将File文件格式转化为FileItem格式
     * @param file
     * @param fieldName
     * @return
     */
    private FileItem createFileItem(File file, String fieldName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }


}
