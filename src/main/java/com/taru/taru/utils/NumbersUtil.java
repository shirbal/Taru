package com.taru.taru.utils;

import java.text.DecimalFormat;

/**
 * Created by shiranmaor on 11/1/15.
 */
public class NumbersUtil {

    public static double round(double num, int digits) {
        digits = 0;

        StringBuilder str = new StringBuilder("#");
        if(digits > 0) {
            str.append(".");
        }
        while(digits > 0) {
            str.append("#");
            digits--;
        }
        DecimalFormat df = new DecimalFormat(str.toString());
        double res = num;
        try {
            res = Double.valueOf(df.format(num));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;



    }
}
