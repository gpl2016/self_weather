package com.example.liguopeng.li_weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.liguopeng.li_weather.databases.WeatherBaseHelper;
import com.example.liguopeng.li_weather.databases.WeatherDbSchema;
import com.example.liguopeng.li_weather.databases.WeatherDbSchema.SettingTable;
import com.example.liguopeng.li_weather.databases.WeatherDbSchema.WeatherTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.liguopeng.li_weather.IsInternet.isNetworkAvalible;

public class WeatherLab {
    private static WeatherLab sWeatherLab;
        private Context mContext;
        private SQLiteDatabase mDatabase;
    private   List<Weather> mWeathers;

    public static WeatherLab get(Context context) {
            sWeatherLab = new WeatherLab(context);
        return sWeatherLab;
    }

    private WeatherLab(Context context) {
        mContext=context.getApplicationContext();
        mDatabase=new WeatherBaseHelper(mContext).getWritableDatabase();
        mWeathers = WeatherListFragment.mItems;
        Log.d("网络1",String.valueOf(mWeathers.size()));
        if(isNetworkAvalible(mContext)){
            Log.d("ccc","WeatherLab被调用0");
            Log.d("ccc", String.valueOf(mWeathers.size()+"0"));
            mDatabase.execSQL("delete from weathers");
            mDatabase.execSQL("delete from setting");

            for (Weather weather : mWeathers)
            {
            ContentValues values1=getContentValues1(weather);
            ContentValues values2=getContentValues2(weather);
            mDatabase.execSQL("delete from setting");
            mDatabase.insert(WeatherTable.NAME,null,values1);
            mDatabase.insert(SettingTable.NAME,null,values2);
            }
        }
             Log.d("ccc","WeatherLab被调用");
             Log.d("ccc", String.valueOf(mWeathers.size()));
    }

    private  static ContentValues getContentValues1(Weather weather){
        ContentValues values=new ContentValues();
        weather.InitWeather();
        values.put(WeatherTable.Cols.DATE,weather.getdate());
        values.put(WeatherTable.Cols.INITDATE,weather.getinitDate());
        values.put(WeatherTable.Cols.ID,weather.getId());
        values.put(WeatherTable.Cols.LAT,String.valueOf(weather.getLat()));
        values.put(WeatherTable.Cols.LON,String.valueOf(weather.getLon()));
        values.put(WeatherTable.Cols.UPDATETIME,weather.getUpdatetime());
        values.put(WeatherTable.Cols.LOCATION,weather.getLocation());
        values.put(WeatherTable.Cols.TEP_MIN,weather.getTmp_min());
        values.put(WeatherTable.Cols.TEP_MAX,weather.getTmp_max());
        values.put(WeatherTable.Cols.HUM,weather.getHum());
        values.put(WeatherTable.Cols.PRES,weather.getPres());
        values.put(WeatherTable.Cols.WIND_SPD,weather.getWind_spd());
        values.put(WeatherTable.Cols.WIND_DIR,weather.getWind_dir());
        values.put(WeatherTable.Cols.DAY,weather.getDay());
        values.put(WeatherTable.Cols.IMAGETEXTD,weather.getCond_txt_d());
        values.put(WeatherTable.Cols.IMAGETEXTN,weather.getCond_txt_n());
        values.put(WeatherTable.Cols.SRC_D,weather.getCond_code_d());
        values.put(WeatherTable.Cols.SRC_N,weather.getCond_code_n());
        return values;
    }


    private  static ContentValues getContentValues2(Weather weather){
        ContentValues values=new ContentValues();
        weather.InitWeather();
        values.put(SettingTable.Cols.CITY,weather.getLocation());
        values.put(SettingTable.Cols.UNTI,String.valueOf(Weather.UNTI));
        values.put(SettingTable.Cols.NOTIFICATION,String.valueOf(WeatherSetting.NOTIFICATION));
        return values;
    }

    public List<Weather> getWeathers() {
        return mWeathers;
    }

    public Weather getWeather(String id) {
        for (Weather weather : mWeathers) {
            if (weather.getId().equals(id)) {
                return weather;
            }
        }
        return null;
    }
}
