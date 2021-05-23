package com.bobo.bobo_res.utils;

import java.math.BigDecimal;

/**
 * Created by 公众号：IT波 on 2021/5/22 Copyright © Leon. All rights reserved.
 * Functions: 解决两个double相加小数位非常长的问题
 */
public class PreciseAdditionAndSubtraction {

    /**
     * @param v1 被加数
     * @param v2 加数
     * @param scale 保留几位小数*/
    public static double add(Double v1, Double v2, int scale){
        BigDecimal b1 = new BigDecimal(Double.toString(v1 == null? 0.0 : v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2 == null? 0.0 : v2));
        return round(b1.add(b2).doubleValue(), scale);
    }

    //3个数相加
    public static double add(Double v1, Double v2, Double v3, int scale)  {
        BigDecimal b1 = new BigDecimal(Double.toString(v1 == null? 0.0 : v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2 == null? 0.0 : v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3 == null? 0.0 : v3));
        BigDecimal b = b1.add(b2);
        return round(b.add(b3).doubleValue(), scale);
    }

    /**
     * @param v1 被减数
     * @param v2 减数
     */
    public static double sub(double v1, double v2, int scale)  {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return round(b1.subtract(b2).doubleValue(), scale);
    }

    /**
     * @param v1 被乘数
     * @param v2 乘数
     */
    public static double mul(double v1, double v2, int scale)  {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return round(b1.multiply(b2).doubleValue(), scale);
    }

    /**
     * @param v1 被除数
     * @param v2 除数
     */
    public static double div(double v1, double v2, int scale)  {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue(); //ROUND_HALF_UP:四舍五入
    }

    /**
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     */
    public static double round(double v, int scale)  {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
