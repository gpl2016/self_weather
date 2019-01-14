package com.example.liguopeng.li_weather;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.baidu.mapapi.model.LatLng;

import java.io.File;

public class MapUtil {

    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名




    public static boolean isBaiduMapInstalled(){
        return isInstallPackage(PN_BAIDU_MAP);
    }



    private static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }



    /**
     * 高德、腾讯转百度
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    private static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }



    /**
     * 打开百度地图导航功能(默认坐标点是高德地图，需要转换)
     * @param context
     * @param slat 起点纬度
     * @param slon 起点经度
     * @param sname 起点名称 可不填（0,0，null）
     * @param dlat 终点纬度
     * @param dlon 终点经度
     * @param dname 终点名称 必填
     */
    public static void openBaiDuNavi(Context context,double slat, double slon, String sname, double dlat, double dlon, String dname){
        String uriString = null;
        double destination[] = gaoDeToBaidu(dlat, dlon);
        dlat = destination[0];
        dlon = destination[1];

       // StringBuilder builder = new StringBuilder("baidumap://map/marker");
        StringBuilder builder = new StringBuilder("baidumap://map/geocoder");
        builder.append("?location=")
                .append(dlat)
                .append(",")
                .append(dlon);
        uriString = builder.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_BAIDU_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);//隐式intent
    }

    }