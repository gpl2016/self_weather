package com.example.liguopeng.li_weather;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.transition.ChangeImageTransform;
import android.util.Log;

import com.example.liguopeng.li_weather.databases.WeatherBaseHelper;
import com.example.liguopeng.li_weather.databases.WeatherDbSchema;
import com.example.liguopeng.li_weather.databases.WeatherDbSchema.WeatherTable;

import java.util.ArrayList;
import java.util.List;

public class getLocalWeather {
    private static List<Weather> mWeathers1=new ArrayList<>();
    private static Weather mWeather=new Weather();
    private static Context mContext;
    private static SQLiteDatabase mDatabase;

private static WeatherCursorWrapper queryWeathers(String whereClause, String[] whereArgs){
    Cursor cursor=mDatabase.query(
            WeatherTable.NAME,
            null,
            whereClause,
            whereArgs,
            null,
            null,
            null

    );
    Log.d("Local","2");
    return new WeatherCursorWrapper(cursor);
}

/*
* 要返回数据库中所有的信息
* 包括7天的天气信息
* //当前设置的情况
* */
    public static List<Weather> getLocalWeathers(Context context){
        Log.d("Local","1");
        mDatabase=new WeatherBaseHelper(context).getWritableDatabase();//要把取出来的，存到mWeathers里面。
        WeatherCursorWrapper cursor=queryWeathers(null,null);
        mWeathers1.clear();//加入了这个，查看本地记录应该不会出来那么多了吧->是的,bug没了
       try{
           cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            mWeathers1.add(cursor.getWeather());
            cursor.moveToNext();
        }}finally {
           cursor.close();
       }
        return mWeathers1;
    }

    /*
    * 返回第一个weather对象
    * */
    public static Weather getLocalWeather(Context context){
        mDatabase=new WeatherBaseHelper(context).getWritableDatabase();//要把取出来的，存到mWeathers里面。
        WeatherCursorWrapper cursor=queryWeathers(null,null);
        try{
            cursor.moveToFirst();
            mWeather=cursor.getWeather();
            cursor.moveToNext();
        }finally {
            cursor.close();
        }
        return mWeather;
    }


    public static String[] getLocalSetting(){

return null;

    }
}
