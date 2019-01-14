package com.example.liguopeng.li_weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.UUID;


public class WeatherActivity extends SingleFragmentActivity2 {

    private static final String EXTRA_WEATHER_ID =
            "com.example.liguopeng.li_weather.weather_id";

    public static Intent newIntent(Context packageContext, String weatherid) {
        Intent intent = new Intent(packageContext, WeatherActivity.class);
        intent.putExtra(EXTRA_WEATHER_ID, weatherid);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String weatherId = (String)getIntent()
                .getStringExtra(EXTRA_WEATHER_ID);
        return WeatherFragment.newInstance(weatherId);
    }



}
