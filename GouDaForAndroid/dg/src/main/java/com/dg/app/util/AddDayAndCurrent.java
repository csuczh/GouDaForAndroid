package com.dg.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lenovo on 2015/10/25.
 */
public class AddDayAndCurrent {
    public static String add(int day) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long current=System.currentTimeMillis();
        long add=current+day*24*60*60*1000;
        Date addDate=new Date(add);
        String date = sDateFormat.format(addDate);
        return  date;
    }
}
