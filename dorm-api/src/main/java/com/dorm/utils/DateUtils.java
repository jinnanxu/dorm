package com.dorm.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static SimpleDateFormat sdfStandard = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdfCn = new SimpleDateFormat("yyyy年MM月dd日");
    private static SimpleDateFormat sdfCnYM = new SimpleDateFormat("yyyy年MM月");

    private static SimpleDateFormat dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatCnDate(Date date){
        if(date!=null){
            return sdfCn.format(date);
        }else{
            return "";
        }
    }

    public static String formatCnYMDate(Date date){
        if(date!=null){
            return sdfCnYM.format(date);
        }else{
            return "";
        }
    }

    public static String formatStandard(Date date){
        if(date!=null){
            return sdfStandard.format(date);
        }else{
            return "";
        }
    }

    public static Date parseStandardDate(String dateStr){
        if(StringUtils.isNotBlank(dateStr)){
            try {
                return sdfStandard.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return new Date();
        }
    }

    public static String formatDateAndTime(Date date){
        if(date!=null){
            return dateAndTime.format(date);
        }else{
            return "";
        }
    }

    public static Date addDays(Date date, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
