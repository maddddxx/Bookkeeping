package com.hui.tally.utils;

import java.math.BigDecimal;

public class FloatUtils {

    public static float div(float v1,float v2){
        float v3 = v1/v2;
        BigDecimal b1 = new BigDecimal(v3);
        float val = b1.setScale(4, 4).floatValue();
        return val;
    }

    public static String ratioToPercent(float val){
        float v = val*100;
        BigDecimal b1 = new BigDecimal(v);
        float v1 = b1.setScale(2, 4).floatValue();
        String per = v1+"%";
        return per;
    }
}
