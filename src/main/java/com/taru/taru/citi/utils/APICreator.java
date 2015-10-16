package com.taru.taru.citi.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shiran Maor on 9/22/2015.
 */
public class APICreator {

    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";


    public static HttpURLConnection createConnection(String method, UrlCreator urlCreator) {
        HttpURLConnection connection = null;
        try {
            URL url = urlCreator.createURL();
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
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







}
