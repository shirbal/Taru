package com.taru.taru.models;

import com.taru.taru.utils.KeysGenerators;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Shiran Maor on 9/23/2015.
 */
public class TestDataCreator {

    private JSONArray _transactions = new JSONArray();

    private static TestDataCreator _instance = new TestDataCreator();

    private TestDataCreator() {
        createTestData();
    }

    public static TestDataCreator getInstance() {
        return _instance;
    }

    public JSONArray getData() {
        return _transactions;
    }

    private void createTestData() {
        createPaycheckData();
        createCellularData();
        createDaycareData();
        createRentData();
        createCarInsuranceData();
        createHealthInsuranceData();
        createGrociriesData();


    }

    private void createGrociriesData() {
        // Groceries
        add("2015-04-03-07:27:52", "Groceries", 60);
        add("2015-04-08-07:27:52", "Groceries", 100);
        add("2015-04-14-07:27:52", "Groceries", 80);
        add("2015-04-16-07:27:52", "Groceries", 35);
        add("2015-04-21-07:27:52", "Groceries", 45);
        add("2015-04-26-07:27:52", "Groceries", 63);
        add("2015-04-30-07:27:52", "Groceries", 99);

        add("2015-05-05-07:27:52", "Groceries", 90);
        add("2015-05-07-07:27:52", "Groceries", 20);
        add("2015-05-08-07:27:52", "Groceries", 40);
        add("2015-05-10-07:27:52", "Groceries", 120);
        add("2015-05-17-07:27:52", "Groceries", 84);
        add("2015-05-21-07:27:52", "Groceries", 16);
        add("2015-05-23-07:27:52", "Groceries", 50);
        add("2015-05-27-07:27:52", "Groceries", 50);
        add("2015-05-30-07:27:52", "Groceries", 50);

        add("2015-06-02-07:27:52", "Groceries", 140);
        add("2015-06-09-07:27:52", "Groceries", 73);
        add("2015-06-14-07:27:52", "Groceries", 100);
        add("2015-06-20-07:27:52", "Groceries", 33);
        add("2015-06-22-07:27:52", "Groceries", 58);
        add("2015-06-25-07:27:52", "Groceries", 88);
        add("2015-06-30-07:27:52", "Groceries", 37);
        add("2015-06-31-07:27:52", "Groceries", 70);
    }

    private void createHealthInsuranceData() {
        // Health Insurance
        add("2015-04-10-10:00:00", "Health Insurance", 95);
        add("2015-05-10-10:00:00", "Health Insurance", 95);
        add("2015-06-10-10:00:00", "Health Insurance", 95);
    }

    private void createCarInsuranceData() {
        // Car Insurance
        add("2015-04-21-10:00:00", "Car Insurance", 100);
        add("2015-05-21-10:00:00", "Car Insurance", 100);
        add("2015-06-21-10:00:00", "Car Insurance", 100);
    }

    private void createRentData() {
        // Rent
        add("2015-04-08-10:00:00", "Home", 1800);
        add("2015-05-08-10:00:00", "Home", 1800);
        add("2015-06-08-10:00:00", "Home", 1800);
    }

    private void createDaycareData() {
        // Day Care
        add("2015-04-01-10:00:00", "School", 1560);
        add("2015-05-01-10:00:00", "School", 1560);
        add("2015-06-01-10:00:00", "School", 150);
    }

    private void createCellularData() {
        // Cellular
        add("2015-04-10-10:00:00", "Utilities", 120);
        add("2015-05-10-10:00:00", "Utilities", 120);
        add("2015-06-10-10:00:00", "Utilities", 120);
    }

    private void createPaycheckData() {
        // Paycheck
        add("2015-04-01-10:00:00", "Paycheck", 10000);
        add("2015-05-01-10:00:00", "Paycheck", 10000);
        add("2015-06-01-10:00:00", "Paycheck", 10000);
    }

    private void add(String dateAndTime, String description, double amount) {
        JSONObject obj = new JSONObject();
        String id = KeysGenerators.generateID();
        try {
            obj.put(Transaction.ID, id);
            obj.put(Transaction.DATE_POSTED, dateAndTime);
            obj.put(Transaction.DESCRIPTION, description);
            obj.put(Transaction.TRANSACTION_AMOUNT, amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        _transactions.put(obj);

    }
}
