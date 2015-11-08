package com.taru.taru.citi.utils;


import android.content.Context;
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

    public List<Transaction> getData(Context con) {
        List<Transaction> result = null;
        String res = "";
        try {
            InputStream open = con.getAssets().open("input.txt");
            res = readFromInputStream(res,open);
            result = parseResponse(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    return result;
    }


    public List<Transaction> getData(UrlCreator url, String method) {

        List<Transaction> res = null;
        try {
            HttpURLConnection connection = APICreator.createConnection(method, url);
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

    private List<Transaction> parseResponse(String readResponse) {
        List<Transaction> result = new ArrayList<>();
        try
        {
            JSONArray transactions = new JSONArray(readResponse);
            int length = transactions.length();
            for(int i = 0; i < length; i++) {
                Object transactionObject = transactions.get(i);
                JSONObject transactionJson = new JSONObject(transactionObject.toString());
                Transaction transaction = readTransaction(transactionJson);
                result.add(transaction);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
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
            result = readFromInputStream(result, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String readFromInputStream(String result, InputStream inputStream) {
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
