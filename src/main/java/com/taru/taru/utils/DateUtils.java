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
}
