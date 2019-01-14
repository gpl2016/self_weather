package com.example.liguopeng.li_weather;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.example.liguopeng.li_weather.databases.WeatherDbSchema;
import com.example.liguopeng.li_weather.databases.WeatherDbSchema.WeatherTable;

public class
WeatherCursorWrapper extends CursorWrapper {

    WeatherCursorWrapper(Cursor cursor){
        super(cursor);
    }


    public Weather getWeather(){

        /*
         * 数据库存的都是修饰过的,所以要向数据库中存未修饰过的数据，这样改会比较简单
         * */
        String id=getString(getColumnIndex(WeatherTable.Cols.ID));
        String tmp_min=getString(getColumnIndex(WeatherTable.Cols.TEP_MIN));
        Log.d("Local获取的",tmp_min);
        String tmp_max=getString(getColumnIndex(WeatherTable.Cols.TEP_MAX));
        String hum=getString(getColumnIndex(WeatherTable.Cols.HUM));
        String pres=getString(getColumnIndex(WeatherTable.Cols.PRES));
        String wind_spd=getString(getColumnIndex(WeatherTable.Cols.WIND_SPD));
        String wind_dir=getString(getColumnIndex(WeatherTable.Cols.WIND_DIR));
        String date=getString(getColumnIndex(WeatherTable.Cols.DATE));
        String cond_txt_d=getString(getColumnIndex(WeatherTable.Cols.IMAGETEXTD));
        String cond_txt_n=getString(getColumnIndex(WeatherTable.Cols.IMAGETEXTN));
        String cond_code_d=getString(getColumnIndex(WeatherTable.Cols.SRC_D));
        String cond_code_n=getString(getColumnIndex(WeatherTable.Cols.SRC_N));
        String update=getString(getColumnIndex(WeatherTable.Cols.UPDATETIME));
        String loc=getString(getColumnIndex(WeatherTable.Cols.LOCATION));
        String lat=getString(getColumnIndex(WeatherTable.Cols.LAT));
        String lon=getString(getColumnIndex(WeatherTable.Cols.LON));

        Weather weather=new Weather();
        weather.setdate(date);//
        weather.setId(id);
        Log.d("Local获取的1",tmp_min);
        weather.setLowTem(tmp_min);
        weather.setTopTem(tmp_max);
        weather.setHumidity(hum);
        weather.setPressure(pres);
        weather.setWind("风速："+wind_spd+"km/h"+" "+wind_dir);
        weather.setDate(date);
        weather.setDay(LocalDate.getWeekByDateStr(date,LocalDate.getLocalDate()));
        weather.setCond_txt_d(cond_txt_d);
        weather.setCond_txt_n(cond_txt_n);
        weather.setImageText(cond_txt_d,cond_txt_n);
        weather.setImageSrc_d(cond_code_d);
        weather.setImageSrc_n(cond_code_n);
        weather.setUpdatetime(update);
        weather.setLocation(loc);
        weather.setLat(lat);
        weather.setLon(lon);

        return weather;
    }



}
