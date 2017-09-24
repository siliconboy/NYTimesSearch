package com.codepath.nytimessearch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yingbwan on 9/23/2017.
 */

public class MyUtils {

    public static String convertMDYToYMD(String mdy){
        SimpleDateFormat src = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dst = new SimpleDateFormat("yyyyMMdd");
        Date tmDate=null;
        try {
            tmDate = src.parse(mdy);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tmDate==null?null:dst.format(tmDate);
    }
}
