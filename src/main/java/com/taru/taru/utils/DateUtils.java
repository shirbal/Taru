package com.taru.taru.utils;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by Shiran Maor on 9/26/2015.
 */
public class DateUtils {

    public static int getNextMonth() {
        int currentMonth = getCurrentMonth() + 2;
        int nextMonth = (currentMonth == 13) ? 1 : currentMonth;
        return nextMonth;
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static int getTargetYear(int targetMonth) {
        int currentYearTemp = getCurrentYear();
        return (targetMonth == 1) ? currentYearTemp + 1 : currentYearTemp;
    }

    public static int getCurrentYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return year;
    }

    public static String createDateFromDay(Integer day) {
        int month = getNextMonth();
        int year = getTargetYear(month);
        StringBuilder str = new StringBuilder();
        str.append(month);
        str.append("/");
        str.append(day);
        str.append("/");
        str.append(year);
        return str.toString();
    }

    public static String createDateAsString(int year, int month, int day) {
        return month + "/" + day + "/" + year;
    }

    public static String getDateForPresentation(int year, int month, int day) {
        return month + "." + day + "." + year;
    }

    public static java.util.Date toDate(String dateStr) {
        String[] split = dateStr.split("/");
        return new Date(Integer.valueOf(split[2])-1900,Integer.valueOf(split[0])-1,Integer.valueOf(split[1]));

    }

    public static String dateToString(java.util.Date key) {
       int month = key.getMonth() + 1;
        int year = key.getYear() + 1900;
        int day = key.getDate();
        return month + "/" + day + "/" + year;

    }

    public static int getDay(String date) {
        String[] split = date.split("/");
        return Integer.valueOf(split[1]);
    }

    public static int getYear(String date) {
        String[] split = date.split("/");
        return Integer.valueOf(split[2]);
    }

    public static int getMonth(String date) {
        String[] split = date.split("/");
        return Integer.valueOf(split[0]);
    }

    public static int numberOfDays(String date) {
        String[] split = date.split("/");
        int month = Integer.valueOf(split[0]);
        int year = Integer.valueOf(split[2]);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,month-1);
        cal.set(Calendar.YEAR, year);
        int result = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return result;


    }

    public static String getDateFromRep(String date) {
        return date.replace(".","/");
    }
}
