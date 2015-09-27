package com.taru.taru.models;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shiran Maor on 9/26/2015.
 */
public class TransactionsModel {

    private String _category;
    private List<Pair<Integer,Double>> _transactions;

    public TransactionsModel(String category) {
        this._category = category;
        this._transactions = new ArrayList<>();
    }

    public String getCategory() {
        return _category;
    }

    public List<Pair<Integer,Double>> getTransactions() {
        return this._transactions;
    }

    public void add(double[][] amounts, double[][] days) {
        int lastColumn = amounts[0].length - 1;
        for(int i = 0; i < amounts.length; i++) {
            int day = (int)days[i][lastColumn];
            double amount = amounts[i][lastColumn];
            add(day,amount);
        }
    }

    private void add(int day, double amount) {
        _transactions.add(new Pair<Integer, Double>(day,amount));
    }
}
