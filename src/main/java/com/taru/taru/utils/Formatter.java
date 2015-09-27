package com.taru.taru.utils;

import java.sql.Timestamp;

/**
 * Created by Shiran Maor on 9/23/2015.
 */
public class Formatter {


    public static String getDateFromDateAndTime(String dateAndTime) {
        int index = dateAndTime.lastIndexOf("-");
        return dateAndTime.substring(0,index);
    }

    public static double getAmountAsDouble(String amountStr) {
        double v = Double.parseDouble(amountStr);
        return v;
    }

    public static String cleanDescriptionStr(String descriptionStr) {
        return descriptionStr;
    }

    public static Timestamp createTimestampFromStr(String datePostedStr) {
        int index = datePostedStr.lastIndexOf("-");
        StringBuilder str = new StringBuilder();
        Timestamp res = null;
        if(index != -1) {
            str.append( datePostedStr.substring(0,index - 1));
            str.append(" ");
            str.append(datePostedStr.substring(index + 1));
            res = Timestamp.valueOf(str.toString());
        }
        return res;
    }

    public static int getMonthFromDate(String date) {
        int index = date.indexOf("-");
        String monthStr = date.substring(index + 1, index + 3);
        int month = fromFullNumStrToInt(monthStr);
        return month;
    }

    private static int fromFullNumStrToInt(String monthStr) {
        int res = 0;
        if(monthStr.charAt(0) == '0') {
            res = Double.valueOf(Character.toString(monthStr.charAt(1))).intValue();
        } else {
            res = Double.valueOf(monthStr).intValue();
        }
        return res;
    }

    public static int getYearFromDateStr(String dateStr) {
        int index = dateStr.indexOf("-");
        String substring = dateStr.substring(0, index);
        Integer integer = Integer.valueOf(substring);
        return integer;
    }

    public static String manipulateDescription(String description) {
        return description;
    }

    public static int getDayFromDate(String dateStr) {
        int index = dateStr.lastIndexOf("-");
        String substring = dateStr.substring(index + 1);
        Integer integer = fromFullNumStrToInt(substring);
        return integer;
    }
}
