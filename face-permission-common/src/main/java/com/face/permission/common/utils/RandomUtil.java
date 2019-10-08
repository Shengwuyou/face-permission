package com.face.permission.common.utils;

import java.util.Random;

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

    public static void main(String[] args) {
        for(int i=0;i<100;i++){
            System.out.println(getRandom(16));
        }
    }

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
}
