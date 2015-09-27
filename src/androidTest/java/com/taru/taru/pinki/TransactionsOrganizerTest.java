package com.taru.taru.pinki;


import junit.framework.TestCase;

import java.util.Date;

/**
 * Created by Shiran Maor on 9/26/2015.
 */
public class TransactionsOrganizerTest extends TestCase {
    TransactionsOrganizer org = new TransactionsOrganizer();

    public void testCalcAverageAmounts() throws Exception {
        double[][] arr = {
                          {10,20,30,0},
                          {50,30,10,0} ,
                          {100,20,30,0},
                          {200,0,0,0}
                        };
        org.calcAverageAmounts(arr,4);
    }

    public void testFromStringToDate() throws Exception{
        String date = "2015-07-21";
        Date dateFromStr = org.createDateFromStr(date);
        assertEquals(21,dateFromStr.getDate());
        assertEquals(2015, dateFromStr.getYear());
    }

    public void testDateAverage() throws Exception {
        Date[][] arr = {
                {new Date(2015,04,01),new Date(2015,05,02),new Date(2015,06,05),null},
                {new Date(2015,04,06),new Date(2015,05,07),new Date(2015,06,10),null} ,
                {new Date(2015,04,14),new Date(2015,05,14),new Date(2015,06,19),null},
                {new Date(2015,04,22),new Date(2015,05,20),null,null}
        };

        org.calcAverageDates(arr,4);



    }
}