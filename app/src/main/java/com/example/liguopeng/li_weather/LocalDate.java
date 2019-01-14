package com.example.liguopeng.li_weather;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LocalDate {
    private static int year,day,month;
    private static String Smonth,Sday;
    private static String mLocalDate;
    protected static String getLocalDate(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        if (String.valueOf(month).length()==1){
            Smonth="0"+String.valueOf(month);
        }
        else {
            Smonth=String.valueOf(month);
        }
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if (String.valueOf(day).length()==1){
            Sday="0"+String.valueOf(day);
        }
        else {
            Sday=String.valueOf(day);
        }
        mLocalDate= String.valueOf(year)+"-"+Smonth+"-"+Sday;
       Log.d("ddd",mLocalDate);

    return mLocalDate;

    }



    public static String daysBetween(String startDay, String endDay) {
        String hintDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(startDay);
            date2 = sdf.parse(endDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        if(between_days==0){
            hintDate="今天";
        }
        else if(between_days==1){
            hintDate="明天";
        }
        else if(between_days==2){
            hintDate="后天";
        }
        else {
            hintDate=" ";
        }
        return hintDate;
    }



    public static String getWeekByDateStr(String strDate,String t_strDate)
    {
        int year = Integer.parseInt(strDate.substring(0, 4));
        int month = Integer.parseInt(strDate.substring(5, 7));
        int day = Integer.parseInt(strDate.substring(8, 10));
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(java.util.Calendar.YEAR, year);
        c.set(java.util.Calendar.MONTH, month - 1);
        c.set(java.util.Calendar.DAY_OF_MONTH, day);
        String week = "";
        int weekIndex = c.get(java.util.Calendar.DAY_OF_WEEK);//传入日期的weekIndex

        switch (weekIndex)
        {
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
        }
        /*
         * 还要判断一下是今天，明天，后天
         * */
        if(LocalDate.daysBetween(t_strDate,strDate).equals(" ")){
            return week;
        }
        else{
            week=LocalDate.daysBetween(t_strDate,strDate);
        }
        return week;
    }

}
