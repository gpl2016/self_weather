package com.example.liguopeng.li_weather;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlickWeather {
    private static final String TAG="FlickWeather";
    private static final String API_KEY="afc160b7251c448884403a92071c53e1";
    public static String mCity="长沙";
    public static int mCityChanged=0;
    private String longitude;
    private String latitude;
    private String location;//返回的城市名称
    private String updatetime;//数据更新时间
    /*
     *
     * */
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }



    public List<Weather> fetchItems() throws IOException ,JSONException{
        List<Weather> items=new ArrayList<>();
        try {
            String url = Uri.parse("https://free-api.heweather.com/s6/weather/forecast?")
                    .buildUpon()
                    .appendQueryParameter("location", mCity)
                    .appendQueryParameter("key", API_KEY)
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.d(TAG, "received" + jsonString);
            Log.d(TAG, "received" + "被调用");
            //Gson gson=new Gson();
            JSONObject jsonBody=new JSONObject(jsonString);
            parseItems(items,jsonBody);
        }catch (IOException ioe){
            Log.e(TAG,"failed");
        }
        return items;
    }

    private void parseItems(List<Weather>items,JSONObject jsonBody) throws IOException,JSONException{
        JSONArray weatherJSONArray=jsonBody.getJSONArray("HeWeather6");
        JSONObject weatherJsonObject=weatherJSONArray.getJSONObject(0);
        JSONObject weatherJsonObjectBasic=weatherJsonObject.getJSONObject("basic");//这个是要获取经纬度的哦
        JSONObject weatherJsonObjectUpdate=weatherJsonObject.getJSONObject("update");
        longitude=weatherJsonObjectBasic.getString("lon");
        latitude=weatherJsonObjectBasic.getString("lat");
        location=weatherJsonObjectBasic.getString("location");
        updatetime=weatherJsonObjectUpdate.getString("loc");


        //状态码
        JSONArray weatherJsonArraydaily_forecast=weatherJsonObject.getJSONArray("daily_forecast");//最后包含天气信息的数组
        for(int i=0;i<weatherJsonArraydaily_forecast.length();i++){
            JSONObject weatherJsonRealObject=weatherJsonArraydaily_forecast.getJSONObject(i);
            Weather item=new Weather();
            Gson gson =new Gson();
            item = gson.fromJson(String.valueOf(weatherJsonRealObject), Weather.class);
            item.setLat(latitude);
            item.setLon(longitude);
            item.setLoc(location);
            item.setUpdate(updatetime);
            // Log.d("解析出的数据",item.pres);
            Log.d(TAG,item.getLoc());
            Log.d(TAG,item.getUpdate());
            items.add(item);//忘记了这一行，导致很长时间找不到bug，在adapter那里size是0，所以一直是空白
        }

    }
}





