package com.taru.taru.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shiran Maor on 9/23/2015.
 */
public class Transaction {

    private String _type;
    private String  _date;
    private double _amount;
    private String _category;

    public Transaction(){}

    public Transaction(JSONObject jsonObject) {
        readPropertiesFromJSon(jsonObject);
    }

    public String get_type() {
        return _type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public String getDate() {
        return _date;
    }

    public void setDate(String date) {
        this._date = _date;
    }

    public double getAmount() {
        return _amount;
    }

    public void setAmount(double amount) {
        this._amount = amount;
    }

    public String getCategory() {
        return _category;
    }

    public void setCategory(String category) {
        this._category = category;
    }

    private void readPropertiesFromJSon(JSONObject jsonObject) {
        try {
//            "_type": "OUT",
//                    "month": "4/2015",
//                    "amount": 131.7,
//                    "category": "Beauty",
//                    "transactionDate": {
//                "date": "4/6/2015",
//                        "month": 4,
//                        "year": 2015,
//                        "dateRepresentation": "4/2015"
//            }
            String type = jsonObject.get("type").toString();
            this._type = type;
            String amount = jsonObject.get("amount").toString();
            _amount = Double.parseDouble(amount);
            String category = jsonObject.get("category").toString();
            _category = category;
            String transactionDateStr = jsonObject.get("transactionDate").toString();
            String date = parseDate(transactionDateStr);
            _date = date;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String parseDate(String transactionDateStr) {
        String result = "";
        try {
            JSONObject dateJson = new JSONObject(transactionDateStr);
            String dateStr = dateJson.get("date").toString();
            result = dateStr;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
