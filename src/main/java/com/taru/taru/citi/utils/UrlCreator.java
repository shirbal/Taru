package com.taru.taru.citi.utils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by shiranmaor on 10/15/15.
 */
public class UrlCreator {

    private static final String LOCAL_HOST = "http://192.168.1.106";
    private static final String DEFAULT_PORT = ":8080";

    public static final String ALL_MONTHS = "/months";
    public static final String TRANSACTINOS = "/projected";
    public static final String TRANSACTION_BY_CATEGORY = "/categ/{catgoryName}";

    private StringBuilder urlStr = new StringBuilder();

    public UrlCreator() {
        init();
    }


    private void init() {
        String host = getHost();
        String port = getPort();
        urlStr.append(host);
        urlStr.append(port);
    }

    public URL createURL() {
        URL result = null;
        try {
            result = new URL(urlStr.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void append(String action) {
        urlStr.append(action);
    }


    private String getHost() {
        return LOCAL_HOST;
    }

    private String getPort() {
        return DEFAULT_PORT;
    }


}
