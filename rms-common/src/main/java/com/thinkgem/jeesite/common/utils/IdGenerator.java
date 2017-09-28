package com.thinkgem.jeesite.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IdGenerator {

    private long workerId;   //用ip地址最后几个字节标示
    private long datacenterId = 0L; //可配置在properties中,启动时加载,此处默认先写成0
    private long sequence = 0L;
    private long workerIdBits = 8L; //节点ID长度
    private long sequenceBits = 12L; //序列号12位
    private long workerIdShift = sequenceBits; //机器节点左移12位
    private long datacenterIdShift = sequenceBits + workerIdBits; //数据中心节点左移14位
    private long sequenceMask = -1L ^ (-1L << sequenceBits); //4095
    private long lastTimestamp = -1L;

    public IdGenerator(){
        workerId = 0x000000FF & getLastIP();
    }


    public synchronized String nextId() {
        long timestamp = timeGen(); //获取当前毫秒数
        //如果服务器时间有问题(时钟后退) 报错。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            //sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & sequenceMask;
            //判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp); //自旋等待到下一毫秒
            }
        } else {
            sequence = 0L; //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
        }
        lastTimestamp = timestamp;


        long suffix = (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;

        String datePrefix = DateFormatUtils.format(timestamp, "yyyyMMddHHMMssSSS");

        return datePrefix + suffix;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    private byte getLastIP(){
        byte lastip = 0;
        try{
            InetAddress ip = InetAddress.getLocalHost();
            byte[] ipByte = ip.getAddress();
            lastip = ipByte[ipByte.length - 1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return lastip;
    }
}