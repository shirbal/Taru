package com.taru.taru.citi.utils;


import android.util.JsonReader;

import com.taru.taru.models.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by smaor on 9/22/2015.
 */
public class RestApiCaller {

    private static final String UTF_8 = "utf-8";
    private static final String TOKEN = "token";
    private static RestApiCaller _caller = new RestApiCaller();

    private RestApiCaller(){}

    public static RestApiCaller getInstance() {
        return _caller;
    }

    public Map<String, List<Transaction>> getData(UrlCreator url, String method) {

        Map<String, List<Transaction>> res = null;
        try {
            HttpURLConnection connection = APICreator.createConnection(method,url);
            connection.connect();
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                String readResponse = readResponse(connection);

                res = parseResponse(readResponse);
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return res;
    }

    private Map<String, List<Transaction>> parseResponse(String readResponse) {
        Map<String, List<Transaction>> result = new HashMap<>();
        try
        {

            JSONObject categoriesObject = new JSONObject(readResponse);
            Iterator<String> keys = categoriesObject.keys();
            while(keys.hasNext()) {
                String categoryName = keys.next();
                List<Transaction> transactions = new ArrayList<>();

                Object categoryTransactionObject = categoriesObject.get(categoryName);
                JSONArray transactionsArray = new JSONArray(categoryTransactionObject.toString());
                int length = transactionsArray.length();
                for(int i = 0; i < length; i++) {
                    Object transactionObject = transactionsArray.get(i);
                    JSONObject transactionJson = new JSONObject(transactionObject.toString());
                    Transaction transaction = readTransaction(transactionJson);
                    transactions.add(transaction);
                }
                result.put(categoryName,transactions);


            }

        } catch (Exception ex) {
            ex.printStackTrace();;
        }
        return result;
    }

    private Transaction readTransaction(JSONObject transactinoJson) {
        return new Transaction(transactinoJson);
    }

    private String readResponse(HttpURLConnection connection) {
        String result = "";
        try {
            InputStream inputStream = connection.getInputStream();
            if (inputStream != null) {
                BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine = "";
                StringBuilder res = new StringBuilder("");
                try {
                    while ((inputLine = buff.readLine()) != null) {
                        res.append(inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result = res.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private JSONObject readJSONObject(HttpURLConnection connection) {
        String response = readResponse(connection);
        return getJSONObjectFromString(response);
    }

    private JSONArray readJSONArray(HttpURLConnection connection) {
        String response = readResponse(connection);
        return getJSONArrayFromString(response);
    }

    private JSONObject getJSONObjectFromString(String str) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private JSONArray getJSONArrayFromString(String str) {



        JSONArray obj = null;
        try {
            obj = new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
