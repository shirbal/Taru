package com.taru.taru.models;

import com.taru.taru.models.enums.TransactionType;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shiran Maor on 9/23/2015.
 */
public class Transaction {

    private static final String AMOUNT = "amount";
    private static final String CATEGORY = "category";
    private static final String TRANSACTION_DATE = "transactionDate";
    private static final String TRANSACTION_TYPE = "transactionType";
    private static final String DATE = "date";

    private TransactionType _type;
    private String  _date;
    private double _amount;
    private String _category;


    public Transaction(JSONObject jsonObject) {
        readPropertiesFromJSon(jsonObject);
    }

    public Transaction(String category, String date, Double amount, TransactionType type) {
        _category = category;
        _date = date;
        _amount = amount;
        _type = type;
    }

    public TransactionType getType() {
        return _type;
    }


    public String getDate() {
        return _date;
    }


    public double getAmount() {
        return _amount;
    }


    public String getCategory() {
        return _category;
    }


    private void readPropertiesFromJSon(JSONObject jsonObject) {
        try {

            String amount = jsonObject.get(AMOUNT).toString();
            _amount = Double.parseDouble(amount);
            String category = jsonObject.get(CATEGORY).toString();
            _category = category;
            String transactionDateStr = jsonObject.get(TRANSACTION_DATE).toString();
            String date = parseDate(transactionDateStr);
            _date = date;

            String transactionCategoryStr = jsonObject.get(TRANSACTION_TYPE).toString();
            _type = TransactionType.parse(transactionCategoryStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String parseDate(String transactionDateStr) {
        String result = "";
        try {
            JSONObject dateJson = new JSONObject(transactionDateStr);
            String dateStr = dateJson.get(DATE).toString();
            result = dateStr;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
