package com.taru.taru.views;

import com.taru.taru.models.enums.TransactionType;

/**
 * Created by Shiran Maor on 11/3/15.
 */
public class DailyTransactionViewModel {


    private String _category;

    private double _amount;
    private TransactionType _type;

    public DailyTransactionViewModel(String category, double amount, TransactionType type) {
        _category = category;

        _amount = amount;
        _type = type;
    }

    public String getCategory() {
        return _category;
    }


    public double getAmount() {
        return _amount;
    }

    public TransactionType getType() {
        return _type;
    }

}
