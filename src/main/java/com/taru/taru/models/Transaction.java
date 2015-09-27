package com.taru.taru.models;

import com.taru.taru.utils.Formatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Created by Shiran Maor on 9/23/2015.
 */
public class Transaction {

    public static final String ID = "id";
    public static final String DATE_POSTED = "date_posted";
    public static final String TRANSACTION_AMOUNT = "transaction_amount";
    public static final String DESCRIPTION = "description";

    private String _id;
    private Timestamp  _date;
    private double _amount;
    private String _description;

    public Transaction(){}

    public Transaction(JSONObject jsonObject) {
        readPropertiesFromJSon(jsonObject);
    }

    public String getId() {
        return _id;
    }

    public Timestamp getDate() {
        return _date;
    }

    public double getAmount() {
        return _amount;
    }

    public String getDescripion() {
        return _description;
    }

    private void readPropertiesFromJSon(JSONObject jsonObject) {
        try {
            this._id = jsonObject.get(ID).toString();
            String datePostedStr = jsonObject.get(DATE_POSTED).toString();
            this._date = Formatter.createTimestampFromStr(datePostedStr);
            String amountStr = jsonObject.get(TRANSACTION_AMOUNT).toString();
            this._amount = Formatter.getAmountAsDouble(amountStr);
            String descriptionStr = jsonObject.get(DESCRIPTION).toString();
            this._description = Formatter.cleanDescriptionStr(descriptionStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
