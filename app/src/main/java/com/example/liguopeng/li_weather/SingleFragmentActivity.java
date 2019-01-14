package com.example.liguopeng.li_weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;


public abstract class SingleFragmentActivity extends AppCompatActivity {
    public static TextView mMainDayDate;
    // private TextView mMainDayDate;
    public static TextView mMainHighTem;
    public static TextView mMainLowTem;
    public static ImageView mMainWeatherImage_d;
    public static ImageView mMainWeatherImage_n;
    public static TextView mMainWeatherText;
    public static TextView mMainCurrentCity;

    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_fragment);
        setContentView(getLayoutResId());

        mMainDayDate = (TextView) findViewById(R.id.mian_day_and_date);

        mMainHighTem = (TextView) findViewById(R.id.mian_high_tem);
        mMainLowTem = (TextView) findViewById(R.id.mian_low_tem);
        mMainWeatherImage_d = (ImageView) findViewById(R.id.main_weather_image_d);
        mMainWeatherImage_n=(ImageView)findViewById(R.id.main_weather_image_n);
        mMainWeatherText = (TextView) findViewById(R.id.mian_weather_text);
        mMainCurrentCity = (TextView) findViewById(R.id.current_city);



        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
    }
    }



}
