package com.face.permission.common.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sunyu on 2018/4/20
 */
public class RandomUtil {
    //除I，O，B
    public final static String[] stringChars = new String[] { "A", "C", "D", "E", "F", "G", "H",

            "J", "K", "L","M", "N", "P", "Q","R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    public final static String[] chars = new String[] { "0", "1", "2", "3", "4", "5",

            "6", "7", "8", "9", "A", "C", "D", "E", "F", "G", "H","I",

            "J", "K", "L","M", "N","O", "P", "Q","R", "S", "T",

            "U", "V", "W", "X", "Y", "Z" };

    public static String getNumberCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    public static String getNumberThreeCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100));
    }

    public static String getWordAndNumberCode() {
        return (char) (new Random().nextInt(26) + 65) + String.valueOf((int) ((Math.random() * 9 + 1) * 10000));
    }

    public static Integer getRandom(Integer len){
        return new Random().nextInt(len);
    }


    public static Integer getRandom(Integer min,Integer max){
        return new Random().nextInt(max - min + 1) + min;
    }
//
//    public static void main(String[] args) {
//        for(int i=0;i<100;i++){
//            System.out.println(getRandom(16));
//        }
//    }

    //返回length长度的随机字符串
    public static String getStringRandom(int length){
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<length;i++){
            int index = random.nextInt(22);
            sb.append(stringChars[index]);
        }
        return sb.toString();
    }
    //返回length长度的随机数字字符串
    public static String getIntRandom(int length){
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<length;i++){
            int number = random.nextInt(10);
            sb.append(number);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<ItemSKURequest> itemSKURequests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemSKURequest iss = new ItemSKURequest();
            iss.setMarketPrice(Long.valueOf(getNumberThreeCode()));
            iss.setSalePrice(Long.valueOf(getNumberThreeCode()));
            itemSKURequests.add(iss);
        }
        Long salePrice = itemSKURequests.stream().map(ItemSKURequest::getSalePrice).min(Long::compare).get();

        ItemSKURequest itemSKURequest =itemSKURequests.parallelStream()
                .collect(Collectors.minBy(Comparator.comparing(ItemSKURequest::getSalePrice))).get();
        System.out.println(itemSKURequest.toString());
    }


    private static class ItemSKURequest {

        /**
         * sku id
         */
        private Long id;


        /**
         * 商品标题
         */
        private String itemTitle;


        /**
         * 商品属性值id组装(表t_item_prop_option的id拼接)，1_2_3，id从小到大
         */
        private Map<String,String> propertys;

        /**
         * sku名称
         */
        private String skuName;

        /**
         * 采购价（单位分）
         */
        private Long costPrice;

        /**
         * 市场价（单位分）
         */
        private Long marketPrice;

        /**
         * 销售价（单位分）
         */
        private Long salePrice;


        /**
         * 供应商sku编号
         */
        private String supplierSkuNo;

        /**
         * sku默认图片url（如sku无自己的图片，则是商品图片）
         */
        private String picUrl;

        /**
         * 库存数
         */
        private Integer stock;

        /**
         * 国际条码
         */
        private String barcode;

        /**
         * 供应商自定义商品编号
         */
        private String supplierItemNo;

        private Integer txPercentage;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getItemTitle() {
            return itemTitle;
        }

        public void setItemTitle(String itemTitle) {
            this.itemTitle = itemTitle;
        }

        public Map<String, String> getPropertys() {
            return propertys;
        }

        public void setPropertys(Map<String, String> propertys) {
            this.propertys = propertys;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public Long getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(Long costPrice) {
            this.costPrice = costPrice;
        }

        public Long getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(Long marketPrice) {
            this.marketPrice = marketPrice;
        }

        public Long getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(Long salePrice) {
            this.salePrice = salePrice;
        }

        public String getSupplierSkuNo() {
            return supplierSkuNo;
        }

        public void setSupplierSkuNo(String supplierSkuNo) {
            this.supplierSkuNo = supplierSkuNo;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public Integer getStock() {
            return stock;
        }

        public void setStock(Integer stock) {
            this.stock = stock;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getSupplierItemNo() {
            return supplierItemNo;
        }

        public void setSupplierItemNo(String supplierItemNo) {
            this.supplierItemNo = supplierItemNo;
        }

        public Integer getTxPercentage() {
            return txPercentage;
        }

        public void setTxPercentage(Integer txPercentage) {
            this.txPercentage = txPercentage;
        }

        @Override
        public String toString() {
            return "ItemSKURequest{" +
                    "id=" + id +
                    ", itemTitle='" + itemTitle + '\'' +
                    ", propertys=" + propertys +
                    ", skuName='" + skuName + '\'' +
                    ", costPrice=" + costPrice +
                    ", marketPrice=" + marketPrice +
                    ", salePrice=" + salePrice +
                    ", supplierSkuNo='" + supplierSkuNo + '\'' +
                    ", picUrl='" + picUrl + '\'' +
                    ", stock=" + stock +
                    ", barcode='" + barcode + '\'' +
                    ", supplierItemNo='" + supplierItemNo + '\'' +
                    ", txPercentage=" + txPercentage +
                    '}';
        }
    }


}
