package com.taru.taru.citi.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Shiran Maor on 9/22/2015.
 */
public class APICreator {

    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String CLIENT_ID_HEADER = "client_id=";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final String CLIENT_ID = "a18d5c67-fdb8-4ebe-9184-ac5c14e142c1";
    private static final String HOST = "https://citimobilechallenge.anypresenceapp.com/retailbanking/v1";
    private static final String LOGIN = "/login";
    private static final String ACCOUNTS = "/accounts";
    private static final String TRANSACTIONS = "/accounts/{account_id}/transactions";
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCOUNT_ID = "{account_id}";

    private static String  _jsonStrRes;

    static  {
        try {
            JSONObject object = new JSONObject();
            object.accumulate(USERNAME, "impatiencesnuffle");
            object.accumulate(PASSWORD, "mooBi8jais");
            _jsonStrRes = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static HttpURLConnection createShiranAPITest(String method) {
        HttpURLConnection connection = null;
        try {
            URL url = createShiranApi();
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
           // connection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);

            //connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setChunkedStreamingMode(0);
        } catch (IOException e) {
            e.printStackTrace();
            if(connection != null) {
                connection.disconnect();
            }
        }

        return connection;
    }

    public static HttpURLConnection createConnection(String method) {
        HttpURLConnection connection = null;
        try {
            URL url = createLogin();
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setChunkedStreamingMode(0);
        } catch (IOException e) {
            e.printStackTrace();
            if(connection != null) {
                connection.disconnect();
            }
        }

        return connection;
    }

    public static HttpURLConnection createAccountsConnection(String method, String token) {
        HttpURLConnection connection = null;
        try {
            URL url = createAccounts();
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
            String jsonStr = APICreator.getAuthorizationHeader(token);
            connection.setRequestProperty(AUTHORIZATION, jsonStr);
            connection.setDoInput(true);
        } catch (IOException e) {
            e.printStackTrace();
            if(connection != null) {
                connection.disconnect();
            }
        }

        return connection;
    }

    public static HttpURLConnection createTransactionsConnection(String method, String token, String accountID) {
        HttpURLConnection connection = null;
        try {
            URL url = createTransactions(accountID);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
            String jsonStr = APICreator.getAuthorizationHeader(token);
            connection.setRequestProperty(AUTHORIZATION, jsonStr);
            connection.setDoInput(true);
        } catch (IOException e) {
            e.printStackTrace();
            if(connection != null) {
                connection.disconnect();
            }
        }

        return connection;
    }

    public static String getUserJsonStrRes() {
        return _jsonStrRes;
    }

    public static String getAuthorizationHeader(String token) {
        return "Bearer " + token;
    }

    private static void appendClientID(StringBuilder str) {
        str.append("?");
        str.append(CLIENT_ID_HEADER);
        str.append(CLIENT_ID);
    }

    @Nullable
    private static URL createBasicURL(String urlAsString) {
        URL result = null;
        try {
            result = new URL(urlAsString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @NonNull
    private static String createBasicUrlAsString(String host, String action,boolean needClientID) {
        StringBuilder str = new StringBuilder();
        str.append(host);
        str.append(action);
        if (needClientID) {
            appendClientID(str);
        }
        return str.toString();
    }

    private static URL createLogin() {
        String basicUrlAsString = createBasicUrlAsString(HOST,LOGIN,true);
        return createBasicURL(basicUrlAsString);
    }

    private static URL createAccounts() {
        String basicUrlAsString = createBasicUrlAsString(HOST,ACCOUNTS,true);
        return createBasicURL(basicUrlAsString);
    }

    private static URL createShiranApi() {
        String basicUrlAsString = createBasicUrlAsString("http://192.168.1.106:8888","/greeting",false);
        return createBasicURL(basicUrlAsString);
    }

    private static URL createTransactions(String accountID) {
        String basicUrlAsString = createBasicUrlAsString(HOST,TRANSACTIONS,true);
        basicUrlAsString = basicUrlAsString.replace(ACCOUNT_ID,accountID);
        return createBasicURL(basicUrlAsString);
    }

}
