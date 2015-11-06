package com.taru.taru.utils;

import android.util.Pair;

import com.taru.taru.models.Transaction;
import com.taru.taru.models.enums.TransactionType;
import com.taru.taru.views.DailyTransactionViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by shiranmaor on 11/1/15.
 */
public class TransactionUtil {

    public static Pair<Double,Double> calcTotal(List<Transaction> data) {
        Pair<Double,Double> result = null;
        double totalExpenses = 0;
        double totalIncome = 0;
        Iterator<Transaction> iterator = data.iterator();
        while(iterator.hasNext()) {
            Transaction next = iterator.next();
            if(next.getType() == TransactionType.OUT) {
                totalExpenses += next.getAmount();
            } else {
                totalIncome += next.getAmount();
            }
        }

        result = new Pair<>(totalIncome,totalExpenses);

        return result;

    }

    /**
     *
     * @param data
     */
    public static void updateDaysToTransactionMap(List<Transaction> data,
                                                  Map<String,Pair<Double,List<DailyTransactionViewModel>>> daysToTransactions,
                                                  Map<String,Double> balances) {
        for(Transaction transaction : data) {
            String date = transaction.getDate();
            Pair<Double,List<DailyTransactionViewModel>> transactionsAndAmount = daysToTransactions.get(date);
            updateMapWithNewModel(daysToTransactions, transaction, date, transactionsAndAmount);
        }
        updateBalances(daysToTransactions, balances);

    }

    private static void updateMapWithNewModel(Map<String, Pair<Double, List<DailyTransactionViewModel>>> daysToTransactions,
                                              Transaction transaction,
                                              String date, Pair<Double,
                                                List<DailyTransactionViewModel>> transactionsAndAmount) {
        List<DailyTransactionViewModel> list;
        Double total;
        if(transactionsAndAmount == null) {
            total = 0.0;
            list = new ArrayList<>();
        } else {
            total = transactionsAndAmount.first;
            list = transactionsAndAmount.second;
        }

        DailyTransactionViewModel newModel = transactionToDailyModel(transaction);
        list.add(newModel);
        if(transaction.getType() == TransactionType.OUT) {
            total -= transaction.getAmount();
        } else {
            total += transaction.getAmount();
        }

        total = NumbersUtil.round(total, 2);
        daysToTransactions.put(date,new Pair<>(total,list));
    }

    private static void updateBalances(Map<String, Pair<Double, List<DailyTransactionViewModel>>> daysToTransactions, Map<String, Double> balances) {
        Map<Date,Double> dates = new TreeMap<>();
        Set<Map.Entry<String, Pair<Double, List<DailyTransactionViewModel>>>> entries =
                daysToTransactions.entrySet();
        Iterator<Map.Entry<String, Pair<Double, List<DailyTransactionViewModel>>>> iterator = entries.iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, Pair<Double, List<DailyTransactionViewModel>>> next = iterator.next();
            String dateStr = next.getKey();
            Date date = DateUtils.toDate(dateStr);
            Pair<Double, List<DailyTransactionViewModel>> value = next.getValue();
            Double totalForThisDay = value.first;
            dates.put(date,totalForThisDay);
        }
        fillMissingDatesWithBalance(dates);
        updateBalancesMap(balances, dates);

    }

    private static void fillMissingDatesWithBalance(Map<Date, Double> dates) {
        int numberOfDates = 30;
        double total = 0;
        for(int i = 1; i <= numberOfDates; i++) {
            String dateStr = DateUtils.createDateAsString(2015, 9, i);
            Date date = DateUtils.toDate(dateStr);

            Double currentTotal = dates.get(date);
            if(currentTotal == null) {
                dates.put(date,total);
            } else {
                total = currentTotal;
            }
        }
    }

    private static void updateBalancesMap(Map<String, Double> balances, Map<Date, Double> dates) {
        Set<Map.Entry<Date, Double>> entries = dates.entrySet();
        Iterator<Map.Entry<Date, Double>> iterator = entries.iterator();
        double total = 0;
        while(iterator.hasNext()) {
            Map.Entry<Date, Double> next = iterator.next();
            Date key = next.getKey();
            String dateStr = DateUtils.dateToString(key);
            total+=next.getValue();
            total = NumbersUtil.round(total,0);
            balances.put(dateStr,total);
        }
    }

    private static DailyTransactionViewModel transactionToDailyModel(Transaction transaction) {
        TransactionType type = transaction.getType();
        String category = transaction.getCategory();
        double amount = NumbersUtil.round(transaction.getAmount(),2);
        DailyTransactionViewModel newModel = new DailyTransactionViewModel(category,amount,type);
        return newModel;
    }
}
