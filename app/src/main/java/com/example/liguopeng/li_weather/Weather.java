package com.example.liguopeng.li_weather;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Weather {
    public static boolean UNTI=false;
    public String Celsius="℃";
    public String Fahrenheit="℉";
    private String code[]=new String[]{            "100",
            "101",
            "102",
            "103",
            "104",
            "200",
            "201",
            "202",
            "203",
            "204",
            "205",
            "206",
            "207",
            "208",
            "209",
            "210",
            "211",
            "212",
            "213",
            "300",
            "301",
            "302",
            "303",
            "304",
            "305",
            "306",
            "307",
            "308",
            "309",
            "310",
            "311",
            "312",
            "313",
            "314",
            "315",
            "316",
            "317",
            "318",
            "399",
            "400",
            "401",
            "402",
            "403",
            "404",
            "405",
            "406",
            "407",
            "408",
            "409",
            "410",
            "499",
            "500",
            "501",
            "502",
            "503",
            "504",
            "507",
            "508",
            "509",
            "510",
            "511",
            "512",
            "513",
            "514",
            "515",
            "900",
            "901",
            "999"
    };
    private String  lon;//经度
    private String  lat;//纬度
    private String loc;
    private String update;
    private String   cond_code_d   ;
    private String   cond_code_n ;
    private String   cond_txt_d   ;
    private String   cond_txt_n   ;
    private String   date   ;
    private String   hum   ;
    private String   pcpn   ;
    private String   pop   ;
    private String   pres   ;
    private String   tmp_max   ;
    private String   tmp_min   ;
    private String   uv_index   ;
    private String   vis   ;
    private String   wind_deg   ;
    private String   wind_dir   ;
    private String   wind_sc   ;
    private String  wind_spd;


    public String getCond_code_d() {
        return cond_code_d;
    }

    public void setCond_code_d(String cond_code_d) {
        this.cond_code_d = cond_code_d;
    }

    public String getCond_code_n() {
        return cond_code_n;
    }

    public void setCond_code_n(String cond_code_n) {
        this.cond_code_n = cond_code_n;
    }

    public String getCond_txt_d() {
        return cond_txt_d;
    }

    public void setCond_txt_d(String cond_txt_d) {
        this.cond_txt_d = cond_txt_d;
    }

    public String getCond_txt_n() {
        return cond_txt_n;
    }

    public void setCond_txt_n(String cond_txt_n) {
        this.cond_txt_n = cond_txt_n;
    }
    public String getdate(){return date;}
    public void setdate(String date) {
        this.date = date;
    }
    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public String getTmp_max() {
        return tmp_max;
    }

    public void setTmp_max(String tmp_max) {
        this.tmp_max = tmp_max;
    }

    public String getTmp_min() {
        return tmp_min;
    }

    public void setTmp_min(String tmp_min) {
        this.tmp_min = tmp_min;
    }

    public String getUv_index() {
        return uv_index;
    }

    public void setUv_index(String uv_index) {
        this.uv_index = uv_index;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public String getWind_deg() {
        return wind_deg;
    }

    public void setWind_deg(String wind_deg) {
        this.wind_deg = wind_deg;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }

    public String getWind_sc() {
        return wind_sc;
    }

    public void setWind_sc(String wind_sc) {
        this.wind_sc = wind_sc;
    }

    public String getWind_spd() {
        return wind_spd;
    }

    public void setWind_spd(String wind_spd) {
        this.wind_spd = wind_spd;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    private String mId;
    private String mTopTem;//
    private String mLowTem;//
    private String mHumidity;
    private String mPressure;
    private String mWind;
    private String mDate;//日期
    //    private String mDay=getWeekByDateStr(mDate);//今天，明天，周六？
    private String mDay;//今天，明天，周六？
    private String mImageText;//描述天气的情况，Clouds，Sunny.....
    private int mImageSrc_d;//图片的资源位置
    private int mImageSrc_n;
    private String mLocation;
    private String mUpdatetime;

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getUpdatetime() {
        return mUpdatetime;
    }

    public void setUpdatetime(String updatetime) {
        mUpdatetime = updatetime;
    }

    public String getTopTem() {
        return mTopTem;
    }

    public void setTopTem(String topTem) {
        if(UNTI){
            mTopTem=  String.format("%.1f",Double.valueOf(topTem)*9/5+32.0)+Fahrenheit;
        }
        else{
        mTopTem = topTem+Celsius;
        }
    }

    public String getLowTem() {
        return mLowTem;
    }

    public void setLowTem(String lowTem) {
        if(UNTI){
            mLowTem=  String.format("%.1f",Double.valueOf(lowTem)*9/5+32.0)+Fahrenheit;
        }
        else {
            mLowTem = lowTem + Celsius;
        }
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        mHumidity = "湿度："+humidity+"%";
    }

    public String getPressure() {
        return mPressure;
    }

    public void setPressure(String pressure) {
        mPressure = "大气压："+pressure+"hPa";
    }

    public String getWind() {
        return mWind;
    }

    public void setWind(String wind) {
        mWind = wind;
    }

    public String getDate() {
        return mDate;
    }
    public String getinitDate(){
        return date;
    }
    public void setDate(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        date=year+"年"+month+"月"+day+"日";
        mDate = date;
    }

    public String getImageText() {
        return mImageText;
    }

    public void setImageText(String cond_txt_d,String cond_txt_n ) {
        if (cond_txt_d.equals(cond_txt_n)){
            mImageText = cond_txt_d;
        }
        else{
            mImageText=cond_txt_d+"转"+cond_txt_n;
        }

    }



    public int getImageSrc_d() {
        return mImageSrc_d;
    }

    public void setImageSrc_d(String  imageSrc_d) {
        int index=0;
        for(int i=0;i<code.length;i++){
            if (code[i].equals(imageSrc_d)){
                index=i;
            }
        }
        mImageSrc_d = index;
    }

    public int getImageSrc_n() {
        return mImageSrc_n;
    }

    public void setImageSrc_n(String  imageSrc_n) {
        int index=0;
        for(int i=0;i<code.length;i++){
            if (code[i].equals(imageSrc_n)){
                index=i;
            }
        }
        mImageSrc_n = index;//起初图片一直显示不对，都是因为这里错误的写成了mImageSrc_d,因为是从上一个方法中直接复制过来的，所以以后还是尽量自己手打，又浪费了好多时间
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }


public Weather(){
        //Log.d("构造函数","构造函数");
}

    public void InitWeather(){//t用构造函数什么的都不好使，服了，只能这么初始化

      //  setId(cond_code_d+cond_code_n+cond_txt_d+cond_txt_n+date+hum+pcpn+pop+pres//+tmp_min+tmp_max+uv_index+vis+wind_deg+wind_dir+wind_sc+wind_spd+loc+update);

        setdate(date);
        setLat(lat);
        setLon(lon);
        setUpdatetime(update);
        setLocation(loc);
        setTmp_min(tmp_min);
        setTmp_max(tmp_max);
        setHum(hum);
        setPres(pres);
        setWind_spd(wind_spd);
        setWind_dir(wind_dir);
        setCond_txt_d(cond_txt_d);
        setCond_txt_n(cond_txt_n);
        setCond_code_d(cond_code_d);
        setCond_code_n(cond_code_n);

        setId(date+loc+update);
        setLowTem(tmp_min);
        setTopTem(tmp_max);
        setHumidity(hum);
        setPressure(pres);
        setWind("风速："+wind_spd+"km/h"+" "+wind_dir);
        Log.d("Local",date);
        setDate(date);
        setDay(LocalDate.getWeekByDateStr(date,LocalDate.getLocalDate()));
        setImageText(cond_txt_d,cond_txt_n);
        setImageSrc_d(cond_code_d);
        setImageSrc_n(cond_code_n);
        setUpdatetime(update);
        setLocation(loc);

    }
}