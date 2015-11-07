package com.taru.taru.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.widget.TextView;

import com.taru.taru.R;
import com.taru.taru.vdesmet.lib.calendar.CalendarView;
import com.taru.taru.views.DailyTransactionViewModel;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Shiran Maor on 11/6/15.
 */
public class CalendarHelper {

    public static void fillCalendarWithData(Context con, CalendarView singleMonth,
                                            Map<String, Pair<Double,List<DailyTransactionViewModel>>> daysToTransactions) {
        Set<Map.Entry<String, Pair<Double, List<DailyTransactionViewModel>>>> entries = daysToTransactions.entrySet();
        Iterator<Map.Entry<String, Pair<Double, List<DailyTransactionViewModel>>>> iterator = entries.iterator();
        while(iterator.hasNext()) {

            Map.Entry<String, Pair<Double, List<DailyTransactionViewModel>>> next = iterator.next();
            String date = next.getKey();
            double amount = next.getValue().first;

            final Calendar firstValidDay = Calendar.getInstance();
            firstValidDay.set(Calendar.YEAR,2015);
            firstValidDay.set(Calendar.MONTH,8);

            int day = DateUtils.getDay(date);
            firstValidDay.set(Calendar.DATE, day);
            firstValidDay.set(Calendar.HOUR_OF_DAY, 0);
            firstValidDay.set(Calendar.MINUTE, 0);
            firstValidDay.set(Calendar.SECOND, 0);
            firstValidDay.set(Calendar.MILLISECOND, 0);
            long timeInMillis = firstValidDay.getTimeInMillis();
            TextView textViewForDate = singleMonth.getTextViewForDate(timeInMillis);

            if(textViewForDate!=null) {
                if (amount < 0) {
                    textViewForDate.setTextColor(con.getResources().getColor(R.color.red));
                } else {
                    textViewForDate.setTextColor(con.getResources().getColor(R.color.green));
                }
                amount = (amount < 0) ? amount*(-1.0) : amount;
                setPrice(textViewForDate,amount);
                //textViewForDate.setText(String.valueOf(amount));
                textViewForDate.setTextSize(9);

            }

            TextView dateNumber = singleMonth.getDateNumber(timeInMillis);
            if(dateNumber != null) {
                dateNumber.setTypeface(dateNumber.getTypeface(), Typeface.BOLD);
            }
        }

    }

    private static void setPrice(TextView text, Double amount) {
        int i = amount.intValue();
        text.setText("$"+i);
    }



}
