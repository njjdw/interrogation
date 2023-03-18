package com.interrogation.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * 生成四位和六位的随机数字
 */
public class RandomUtils {

    private static Random random = new Random();

    /**
     * 生成随机数
     *
     * @param bits 生成随机数的位数
     * @return
     */
    public static String getDigitBits(int bits) {
        StringBuilder bitsCode = new StringBuilder();
        for (int i = 0; i < bits; i++) {
            bitsCode.append((int) (random.nextInt(9)));//输入参数，数值介于0-9
        }
        return bitsCode.toString();
    }

    public static String getMixBits(int bits) {
        //bitsCode作用域不应在类上，其生命周期不会随方法调用而结束
        StringBuilder bitsCode = new StringBuilder();
        int codeType;
        for (int i = 0; i < bits; i++) {
            codeType = 1+random.nextInt(3);
            if (codeType == 1) {
                bitsCode.append(random.nextInt(9));
            } else if (codeType == 2) {
                bitsCode.append((char) (65 + random.nextInt(25)));
            } else {
                bitsCode.append((char) (97 + random.nextInt(25)));
            }
        }
        return bitsCode.toString();
    }

//    public static String getColorCode() {
//        String r, g, b;
//        Random random = new Random();
//        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
//        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
//        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
//
//        r = r.length() == 1 ? "0" + r : r;
//        g = g.length() == 1 ? "0" + g : g;
//        b = b.length() == 1 ? "0" + b : b;
//
//        return "0x" + r + g + b;
//    }
}