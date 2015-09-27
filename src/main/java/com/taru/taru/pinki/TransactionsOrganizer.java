package com.taru.taru.pinki;


import android.util.Pair;

import com.taru.taru.models.Transaction;
import com.taru.taru.models.TransactionsModel;
import com.taru.taru.utils.DateUtils;
import com.taru.taru.utils.Formatter;
import com.taru.taru.views.TransactionViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Shiran Maor on 9/23/2015.
 */
public class TransactionsOrganizer {

    private static final String DESCRIPTION = "Description";


    public TransactionViewModel[] create(JSONArray arr) {
        Map<String,List<JSONObject>> categorys = sortByCategory(arr);
        Map<String, Map<Integer,List<JSONObject>>> categoryByMonths = new Hashtable<>();
        Iterator<Map.Entry<String, List<JSONObject>>> iterator = categorys.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, List<JSONObject>> next = iterator.next();
            List<JSONObject> value = next.getValue();
            String category = next.getKey();

            Map<Integer, List<JSONObject>> integerListMap = devideToMonths(value, 12);
            categoryByMonths.put(category,integerListMap);
        }

        Iterator<Map.Entry<String, Map<Integer, List<JSONObject>>>> iterator1 = categoryByMonths.entrySet().iterator();
        int size = 0;
        List<TransactionsModel> models = new ArrayList<>();
        while(iterator1.hasNext()) {
            Map.Entry<String, Map<Integer, List<JSONObject>>> next = iterator1.next();
            Map<Integer, List<JSONObject>> value = next.getValue();
            TransactionsModel model = getTransactionsPrediction(value,next.getKey());
            size += model.getTransactions().size();
            models.add(model);
        }


        TransactionViewModel[] res = new TransactionViewModel[size];
        int i = 0;
        for(TransactionsModel model : models) {
            List<Pair<Integer, Double>> transactions = model.getTransactions();
            String category = model.getCategory();
            for(Pair<Integer,Double> pair : transactions) {
                String date = DateUtils.createDateFromDay(pair.first);
                double amount = pair.second;
                TransactionViewModel viewModel = new TransactionViewModel(category,date,amount);
                res[i] = viewModel;
                i++;
            }
        }

        return res;
    }

    public Map<String,List<JSONObject>> sortByCategory(JSONArray arr) {
        Map<String,List<JSONObject>> result = new Hashtable<>();
        try {
            for(int i = 0; i < arr.length(); i++) {
                JSONObject object = (JSONObject) arr.get(i);
                String description = object.get(Transaction.DESCRIPTION).toString();
                description = Formatter.manipulateDescription(description);
                List<JSONObject> currentList = result.get(description);
                if(currentList == null) {
                    currentList = new ArrayList<>();
                    result.put(description,currentList);
                }
                currentList.add(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<Integer,List<JSONObject>> devideToMonths(List<JSONObject> trans, int numOfMonths) {
        Map<Integer,List<JSONObject>> byMonth = new Hashtable<>();
        for(JSONObject obj : trans) {
            int month = getMonth(obj);
            List<JSONObject> currentMonth = byMonth.get(month);
            if(currentMonth == null) {
                currentMonth = new ArrayList<>();
                byMonth.put(month,currentMonth);
            }
            currentMonth.add(obj);
        }

        return byMonth;
    }

    public TransactionsModel getTransactionsPrediction(Map<Integer,List<JSONObject>> transByMonth, String category) {
        int max = getMaxOfTransactionPerMonth(transByMonth);
        int columns = transByMonth.size() + 1;
        double[][] amounts = new double[max][columns];
        double[][] days = new double[max][columns];
        fillAmountsAndDates(transByMonth, amounts, days);
        calcAverageAmounts(amounts, columns);
        calcAverageDates(days, columns);
        TransactionsModel transactions = new TransactionsModel(category);
        transactions.add(amounts,days);
        return transactions;
    }

    public void calcAverageDates(double[][] dates, int columns) {
        calcAverage(dates, columns);
    }

    public void calcAverageAmounts(double[][] amounts, int columns) {
        calcAverage(amounts, columns);
    }

    private void calcAverage(double[][] arr, int indexOfResults) {
        for(int i = 0; i < arr.length; i++) {
            int numOfMonths = 0;
            int sum = 0;
            for(int j = 0; j < indexOfResults - 1; j++) {
                if(arr[i][j] != 0) {
                    sum += arr[i][j];
                    numOfMonths++;
                }
            }
            arr[i][indexOfResults-1] = sum / numOfMonths;
        }
    }

    private void fillAmountsAndDates(Map<Integer, List<JSONObject>> transByMonth, double[][] amounts, double[][] dates) {
        int i = 0;
        int j = 0;
        Iterator<Map.Entry<Integer, List<JSONObject>>> iterator = transByMonth.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Integer, List<JSONObject>> next = iterator.next();
            double key = next.getKey();
            List<JSONObject> transactionForThisMonth = next.getValue();
            for(JSONObject tran : transactionForThisMonth) {
                try {
                    String amountStr = tran.get(Transaction.TRANSACTION_AMOUNT).toString();
                    String dateAndTimeStr = tran.get(Transaction.DATE_POSTED).toString();
                    double amountAsDouble = Formatter.getAmountAsDouble(amountStr);
                    String dateStr = Formatter.getDateFromDateAndTime(dateAndTimeStr);
                    int day = Formatter.getDayFromDate(dateStr);
                    amounts[i][j] = amountAsDouble;
                    dates[i][j] = day;
                    i++;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            i = 0;
            j++;

        }
    }

    private int getMaxOfTransactionPerMonth(Map<Integer, List<JSONObject>> transByMonth) {
        int max = 0;
        for(List<JSONObject> obj : transByMonth.values()) {
            if(obj.size() > max) {
                max = obj.size();
            }
        }
        return max;
    }

    private Integer getMonth(JSONObject obj) {
        Integer monthFromDate = null;
        try {
            String date = obj.get(Transaction.DATE_POSTED).toString();
            monthFromDate = Formatter.getMonthFromDate(date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(monthFromDate == null) {
            monthFromDate = DateUtils.getCurrentMonth();
        }
        return monthFromDate;
    }








}
