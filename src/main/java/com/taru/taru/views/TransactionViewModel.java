package com.taru.taru.views;


/**
 * Created by Shiran Maor on 9/26/2015.
 */
public class TransactionViewModel {

    private String _category;
    private String _date;
    private double _amount;

    public TransactionViewModel(String category, String date, double amount) {
        _category = category;
        _date = date;
        _amount = amount;
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
}
