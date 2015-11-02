package com.taru.taru.models.enums;

/**
 * Created by shiranmaor on 11/1/15.
 */
public enum TransactionType {

    OUT,IN;

    public static TransactionType parse(String str) {
        TransactionType res = OUT;
        if(str.equals("IN")) {
          res = IN;
        }
        return res;
    }


}
