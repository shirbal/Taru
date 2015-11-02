package com.taru.taru.views;


import com.taru.taru.models.enums.TransactionType;

/**
 * Created by Shiran Maor on 9/26/2015.
 */
public class TransactionViewModel {

    private String _category;
    private String _date;
    private double _amount;
    private TransactionType _type;

    public TransactionViewModel(String category, String date, double amount, TransactionType type) {
        _category = category;
        _date = date;
        _amount = amount;
        _type = type;
    }

    public String getCategory() {
        return _category;
    }

    public String getDate() {
        return _date;
    }

    public double getAmount() {
        return _amount;
    }

    public TransactionType getType() {
        return _type;
    }

}
