package com.example.liguopeng.li_weather.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.liguopeng.li_weather.databases.WeatherDbSchema.SettingTable;
import com.example.liguopeng.li_weather.databases.WeatherDbSchema.WeatherTable;
import java.nio.file.WatchEvent;

public class WeatherBaseHelper extends SQLiteOpenHelper{

    private  static final int VERSION=1;
    private  static final String DATABASENAME="weatherBase.db";

    public WeatherBaseHelper(Context context){
        super(context,DATABASENAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ SettingTable.NAME+"("+
        SettingTable.Cols.CITY+","+
        SettingTable.Cols.UNTI+","+
        SettingTable.Cols.NOTIFICATION+")");

        db.execSQL("create table "+ WeatherTable.NAME+"("+
                WeatherTable.Cols.DATE+","+
                WeatherTable.Cols.INITDATE+","+
                WeatherTable.Cols.ID+","+
                WeatherTable.Cols.LAT+","+
                WeatherTable.Cols.LON+","+
                WeatherTable.Cols.UPDATETIME+","+
                WeatherTable.Cols.LOCATION+","+
                WeatherTable.Cols.TEP_MIN+","+
                WeatherTable.Cols.TEP_MAX+","+
                WeatherTable.Cols.HUM+","+
                WeatherTable.Cols.PRES+","+
                WeatherTable.Cols.WIND_SPD+","+
                WeatherTable.Cols.WIND_DIR+ ","+
                WeatherTable.Cols.DAY+","+
                WeatherTable.Cols.IMAGETEXTD+","+
                WeatherTable.Cols.IMAGETEXTN+","+
                WeatherTable.Cols.SRC_D+","+
                WeatherTable.Cols.SRC_N+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
