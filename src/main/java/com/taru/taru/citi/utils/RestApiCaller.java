package com.taru.taru.citi.utils;


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

    public String getTestData() {
        String response = "";
        try {
            HttpURLConnection connection = APICreator.createShiranAPITest(APICreator.GET_METHOD);
            connection.connect();
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                response = readResponse(connection);
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return response;
    }

    public String login() {
        String token = "";
        try {
            HttpURLConnection connection = APICreator.createConnection(APICreator.POST_METHOD);
            fillRequest(connection);
            connection.connect();
            int status = connection.getResponseCode();
            JSONObject response = null;
            if (status == HttpURLConnection.HTTP_OK) {
                response = readJSONObject(connection);
                if(response != null) {
                    token = response.get(TOKEN).toString();
                }
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }catch (JSONException jsonEx) {
            jsonEx.printStackTrace();
        }
        return token;
    }

    public JSONArray getAccounts(String token) {
        JSONArray accounts = null;
        try {

            HttpURLConnection connection = APICreator.createAccountsConnection(APICreator.GET_METHOD, token);
            connection.connect();
            int status = connection.getResponseCode();
            JSONArray response = null;
            if (status == HttpURLConnection.HTTP_OK) {
                accounts = readJSONArray(connection);
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return accounts;
    }

    public String getTransactions(String token, String accountID) {
        String transcations = "";
        try {

            HttpURLConnection connection = APICreator.createTransactionsConnection(APICreator.GET_METHOD, token, accountID);
            connection.connect();
            int status = connection.getResponseCode();
            JSONArray response = null;
            if (status == HttpURLConnection.HTTP_OK) {
                response = readJSONArray(connection);
                if(response != null) {
                    transcations = response.toString();
                }
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return transcations;
    }

    private void fillRequest(HttpURLConnection connection) {
        BufferedWriter out;
        try {
            String jsonStr = APICreator.getUserJsonStrRes();
            OutputStream outputStream = connection.getOutputStream();
            out = new BufferedWriter(new OutputStreamWriter(outputStream, UTF_8));
            out.write(jsonStr);
            out.flush();
            out.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
