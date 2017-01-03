package com.grandtech.map.utils.commons;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by zy on 2016/11/21.
 */

public class RandomHelper {

    private static final String INT = "0123456789";
    private static final String STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random random = new Random();
    private static final SecureRandom secureRandom = new SecureRandom();

    private RandomHelper(){
    }

    public static enum RndType {
        INT,
        STRING,
        ALL
    }

    public static String randomStr(int length) {
        return random(length, RndType.STRING);
    }

    public static String randomInt(int length) {
        return random(length, RndType.INT);
    }

    public static String randomAll(int length) {
        return random(length, RndType.ALL);
    }

    private static String random(int length, RndType rndType) {
        StringBuilder s = new StringBuilder();
        char num = 0;
        for (int i = 0; i < length; i++) {
            if (rndType.equals(RndType.INT))
                num =INT.charAt(random.nextInt(INT.length()));//产生数字0-9的随机数
            else if (rndType.equals(RndType.STRING))
                num =STR.charAt(random.nextInt(STR.length()));//产生字母a-z的随机数
            else {
                num = ALL.charAt(random.nextInt(ALL.length()));
            }
            s.append(num);
        }
        return s.toString();
    }

    public static int randomIntOnly(int seed){
        return secureRandom.nextInt(seed);
    }

}