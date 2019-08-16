package com.face.permission.common.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;

public class SnowFlakeGenerator {

    public static class Factory {
        /**
         * 每一部分占用位数的默认值
         */
        private final static int DEFAULT_MACHINE_BIT_NUM = 5;   //机器标识占用的位数

        //idc数据中心呢就是放服务器的地方，每个地方都有这种数据中心拉 你留意的话 像长沙麓谷这边有一个电信机房啊！！ 这就是数据中心啊！！
        private final static int DEFAULT_IDC_BIT_NUM = 5;//数据中心占用的位数

        private int machineBitNum;
        private int idcBitNum;

        public Factory() {
            this.idcBitNum = DEFAULT_IDC_BIT_NUM;
            this.machineBitNum = DEFAULT_MACHINE_BIT_NUM;
        }

        public Factory(int machineBitNum, int idcBitNum) {
            this.idcBitNum = idcBitNum;
            this.machineBitNum = machineBitNum;
        }

        public SnowFlakeGenerator create(long idcId, long machineId) {
            return new SnowFlakeGenerator(this.idcBitNum, this.machineBitNum, idcId, machineId);
        }
    }

    /**
     * 起始的时间戳
     * 作者写代码时的时间戳
     */
    private final static long START_STAMP = 1508143349995L;

    /**
     * 可分配的位数
     */
    private final static int REMAIN_BIT_NUM = 22;

    /**
     * idc编号
     */
    private long idcId;

    /**
     * 机器编号
     */
    private long machineId;

    /**
     * 当前序列号
     */
    private long sequence = 0L;

    /**
     * 上次最新时间戳
     */
    private long lastStamp = -1L;

    /**
     * idc偏移量：一次计算出，避免重复计算
     */
    private int idcBitLeftOffset;

    /**
     * 机器id偏移量：一次计算出，避免重复计算
     */
    private int machineBitLeftOffset;

    /**
     * 时间戳偏移量：一次计算出，避免重复计算
     */
    private int timestampBitLeftOffset;

    /**
     * 最大序列值：一次计算出，避免重复计算
     */
    private int maxSequenceValue;

    private SnowFlakeGenerator(int idcBitNum, int machineBitNum, long idcId, long machineId) {
        int sequenceBitNum = REMAIN_BIT_NUM - idcBitNum - machineBitNum;

        if (idcBitNum <= 0 || machineBitNum <= 0 || sequenceBitNum <= 0) {
            throw new IllegalArgumentException("error bit number");
        }

        this.maxSequenceValue = ~(-1 << sequenceBitNum);

        machineBitLeftOffset = sequenceBitNum;
        idcBitLeftOffset = idcBitNum + sequenceBitNum;
        timestampBitLeftOffset = idcBitNum + machineBitNum + sequenceBitNum;

        this.idcId = idcId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     */
    public synchronized long nextId() {
        long currentStamp = getTimeMill();
        if (currentStamp < lastStamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastStamp - currentStamp));
        }

        //新的毫秒，序列从0开始，否则序列自增
        if (currentStamp == lastStamp) {
            sequence = (sequence + 1) & this.maxSequenceValue;
            if (sequence == 0L) {
                //Twitter源代码中的逻辑是循环，直到下一个毫秒
                lastStamp = tilNextMillis();
//                throw new IllegalStateException("sequence over flow");
            }
        } else {
            sequence = 0L;
        }

        lastStamp = currentStamp;

        return (currentStamp - START_STAMP) << timestampBitLeftOffset | idcId << idcBitLeftOffset | machineId << machineBitLeftOffset | sequence;
    }

    private long getTimeMill() {
        return System.currentTimeMillis();
    }

    private long tilNextMillis() {
        long timestamp = getTimeMill();
        while (timestamp <= lastStamp) {
            timestamp = getTimeMill();
        }
        return timestamp;
    }

    private static Factory factory = new Factory();

    private static SnowFlakeGenerator snowFlakeGenerator;

    private static Long getWorkId(){
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for(int b : ints){
                sums += b;
            }
            return (long)(sums % 32);
        } catch (UnknownHostException e) {
            // 如果获取失败，则使用随机数备用
            return RandomUtils.nextLong(0,31);
        }
    }

    private static Long getDataCenterId(){
        int[] ints = StringUtils.toCodePoints(SystemUtils.getHostName());
        int sums = 0;
        for (int i: ints) {
            sums += i;
        }
        return (long)(sums % 32);
    }


    public static Long getSnowId(){
        if (snowFlakeGenerator == null){
            snowFlakeGenerator = factory.create(getDataCenterId(),getWorkId());
        }
        return snowFlakeGenerator.nextId();
    }


    //本文针对上面两个问题进行解决，笔者的解决方案是
    // workId使用服务器hostName生成
    // dataCenterId使用IP生成，这样可以最大限度防止10位机器码重复，但是由于两个ID都不能超过32，只能取余数，还是难免产生重复，但是实际使用中，hostName和IP的配置一般连续或相近，只要不是刚好相隔32位，就不会有问题，况且，hostName和IP同时相隔32的情况更加是几乎不可能的事，平时做的分布式部署，一般也不会超过10台容器。
    //---------------------
    //作者：阿凯--Nonkey
    //来源：CSDN
    //原文：https://blog.csdn.net/u012488504/article/details/82194495
    //版权声明：本文为博主原创文章，转载请附上博文链接！
    public static void main(String[] args) {
        Factory factory = new Factory();
        SnowFlakeGenerator snowFlakeGenerator = factory.create(1,1);
        System.out.println(snowFlakeGenerator.nextId());
    }

}