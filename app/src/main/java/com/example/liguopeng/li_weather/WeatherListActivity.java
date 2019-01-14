package com.example.liguopeng.li_weather;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.List;

import static com.example.liguopeng.li_weather.AddressResolutionUtil.addressResolution;

public class WeatherListActivity extends SingleFragmentActivity implements WeatherListFragment.Callbacks{

//在这里/在SingleFragmentActivity中/在weatherlistfragment中直接对主页面的上部分进行操作？

//    public LocationClient mLocationClient = null;
  //  public BDAbstractLocationListener myListener = new MyLocationListener();

    private   BDAbstractLocationListener myListener = new WeatherLocation.MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherLocation.startLocate(this,myListener);
        //下面的代码放在这里会导致拿不到城市
//        FlickWeather.mCity=mAddress;
//        Log.d("获得的城市",mAddress+"  "+ FlickWeather.mCity);
    }

    @Override
    protected Fragment createFragment() {
        return new WeatherListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onWeatherSelected(Weather weather) {
        if(findViewById(R.id.detail_fragment_container)==null){
            Intent intent =WeatherActivity.newIntent(this,weather.getId());
            startActivity(intent);
        }
        else {
            Fragment newDetail=WeatherFragment.newInstance(weather.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container,newDetail)
                    .commit();
        }
    }


}
