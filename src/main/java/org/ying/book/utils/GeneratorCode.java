package org.ying.book.utils;

import java.util.Random;

public class GeneratorCode {
    public static String generator(int length){
        Random yzm = new Random();                          //定义一个随机生成数技术，用来生成随机数
//2，用String常用API-charAit生成验证码
        String yzm1 = "1234567890abcdefghijklmnopqrstuvwxwzABCDEFGHIJKLMNOPQRSTUVWXYZ".toUpperCase();//定义一个String变量存放需要的数据，一共58位
        String yzm3 = "";//定义一个空的Atring变量用来接收生成的验证码
        for (int i = 0; i < length; i++) {
            int a = yzm.nextInt(58);//随机生成0-57之间的数，提供索引位置
            yzm3+=yzm1.charAt(a);//用get 和提供的索引找到相应位置的数据给变量
        }
        return yzm3;
    }
}
