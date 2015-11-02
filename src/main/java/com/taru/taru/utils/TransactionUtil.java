package com.taru.taru.utils;

import android.util.Pair;

import com.taru.taru.models.Transaction;
import com.taru.taru.models.enums.TransactionType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
                                                  Map<String,List<Transaction>> daysToTransactions) {
        for(Transaction transaction : data) {
            String date = transaction.getDate();
            List<Transaction> transactions = daysToTransactions.get(date);
            if(transaction == null) {
                transactions = new ArrayList<>();
            }
            transactions.add(transaction);
            daysToTransactions.put(date,transactions);
        }
    }
}
