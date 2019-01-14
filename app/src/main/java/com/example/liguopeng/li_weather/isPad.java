package com.example.liguopeng.li_weather;

import android.content.Context;
import android.content.res.Configuration;

public class isPad {
    public static boolean isPada(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}

