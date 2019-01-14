package com.example.liguopeng.li_weather.app;

import android.app.Application;

import com.example.liguopeng.li_weather.utils.ScreenUtils;
import com.example.liguopeng.li_weather.utils.ToastUtils;


/**
 * Created by fySpring
 * Date : 2017/10/9
 * To do :
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        ToastUtils.init(this);
        ScreenUtils.init(this);
    }
}
