package com.example.liguopeng.li_weather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liguopeng.li_weather.activities.MainActivity;
import com.example.liguopeng.li_weather.databases.WeatherBaseHelper;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.liguopeng.li_weather.WeatherListFragment.mbinding;
import static com.example.liguopeng.li_weather.WeatherListFragment.mstopbinding;

public class WeatherSetting extends AppCompatActivity {
    private Button mButton;
    private TextView mLocation;
    public static TextView mCurrentLocation;
    private TextView mUnti;
    private TextView mCurrentUnti;
    private TextView mNotification;
    public static boolean NOTIFICATION=false;
    private TextView mCurrentNotification;
    private LinearLayout mUntiLayout;
    private LinearLayout mLocationLayout;
    private CheckBox mCheckBox;
    public static WeatherNotificationService.MyBinder myBinder;
    private Context mContext;
    private  SQLiteDatabase mDatabase;
    public static boolean mIschecked;
    /*指定当前activity要那个service干什么*/
    public static ServiceConnection notificationConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("MyService","startnotificati");
            myBinder=(WeatherNotificationService.MyBinder)service;
            myBinder.startNotification();
            mbinding=true;
        }
/*
* 调用解绑后不会调用这个函数的
* */
        @Override
        public void onServiceDisconnected(ComponentName name) {
//            Log.d("Myservice","stopnotificati");
//            myBinder.stopNotification();
        }
    };
        private ServiceConnection stopNotificationConnection =new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder=(WeatherNotificationService.MyBinder)service;
                myBinder.stopNotification();
                mstopbinding=true;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        mUntiLayout=(LinearLayout)findViewById(R.id.unti_layout);
        mUntiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UntiChoice();
            }
        });
        mLocationLayout=(LinearLayout)findViewById(R.id.location_layout);
        mLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationChoice();
            }
        });

        mContext=this;
        mLocation=(TextView)findViewById(R.id.setting_location);
        mCurrentLocation=(TextView)findViewById(R.id.setting_current_location);
        mCurrentLocation.setText(FlickWeather.mCity);
        mUnti=(TextView)findViewById(R.id.setting_unti);
        mCurrentUnti=(TextView)findViewById(R.id.setting_current_unti);
        if (Weather.UNTI==true){
            mCurrentUnti.setText("华氏度℉");
        }

        mNotification=(TextView)findViewById(R.id.setting_notification);
        mCurrentNotification=(TextView)findViewById(R.id.setting__current_notification);
        mCheckBox=(CheckBox)findViewById(R.id.setting_checkbox) ;
        mCheckBox.setChecked(mIschecked);
        if(mIschecked)
            mCurrentNotification.setText("已经开启");
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    NOTIFICATION=true;
                    Log.d("MyServiceis",String.valueOf(NOTIFICATION));
                    WeatherLab.get(mContext);
                    mCurrentNotification.setText("已经开启");
                    Intent startIntent=new Intent(WeatherSetting.this,WeatherNotificationService.class);
                    startService(startIntent);//先启动服务
                    Log.d("MyService","msopbinding"+String.valueOf(mbinding));
                    if(mstopbinding){//总是出现，这里是true，但是报错是没有注册的情况，干脆再绑定一次，然后再解绑
                        Intent bindIntent_S = new Intent(WeatherSetting.this, WeatherNotificationService.class);
                        bindService(bindIntent_S, stopNotificationConnection, BIND_AUTO_CREATE);
                        unbindService(stopNotificationConnection);
                        mstopbinding=false;
                    }
                    Log.d("MyService","这里");
                    Log.d("MyService","mbinding"+String.valueOf(mbinding));
                     if(mbinding){
                         Intent bindIntent1 = new Intent(WeatherSetting.this, WeatherNotificationService.class);
                         bindService(bindIntent1, notificationConnection, BIND_AUTO_CREATE);
                     unbindService(notificationConnection);
                       mbinding=false;
                    }
                   // Intent bindIntent0 = new Intent(WeatherSetting.this, WeatherNotificationService.class);
                    //bindService(bindIntent0, stopNotificationConnection, BIND_AUTO_CREATE);//绑定stop
                    //unbindService(stopNotificationConnection);
                    Intent bindIntent1 = new Intent(WeatherSetting.this, WeatherNotificationService.class);
                    bindService(bindIntent1, notificationConnection, BIND_AUTO_CREATE);
//这里传入BIND_AUTO_CREATE表示在Activity和Service建立关联后自动创建Service，这会使得MyService中的onCreate()方法得到执行，但onStartCommand()方法不会执行。
                }
                else{
                    NOTIFICATION=false;
                    Log.d("MyService",String.valueOf(NOTIFICATION));
                    isChecked=false;
                    WeatherLab.get(mContext);
                    Intent bindIntent2 = new Intent(WeatherSetting.this, WeatherNotificationService.class);
                    bindService(bindIntent2, stopNotificationConnection, BIND_AUTO_CREATE);
                    Intent stopIntent=new Intent(WeatherSetting.this,WeatherNotificationService.class);
                    stopService(stopIntent);
                    mCurrentNotification.setText("已经关闭");
                }
            }
        });




    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(notificationConnection);
    }

    private void UntiChoice(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("温度单位");
        //    指定下拉列表的显示数据
        final String[] untis = {"摄氏度℃", "华氏度℉"};
        //    设置一个下拉的列表选择项
        builder.setItems(untis, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            { mCurrentUnti.setText(untis[which]);//textview
                //Log.d("选择的",String.valueOf(which));
                if (which==1){
                    Weather.UNTI=true;
                    WeatherLab.get(mContext);//改完单位之后，会更新数据库，在更新数据库之前，会把各种单位图片都重新初始化一遍。
                }
                else{
                    Weather.UNTI=false;
                    WeatherLab.get(mContext);
                }

            }
        });
        //builder.show();不用这个方法而采用下面的方法去掉背景多出来的框框
        AlertDialog r_dialog = builder.create();
        r_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        r_dialog.show();
    }

    private void LocationChoice(){
        Intent intent=new Intent(WeatherSetting.this,MainActivity.class);
        startActivity(intent);

    }


    @Override
    protected void onResume() {
        super.onResume();
        WeatherLab.get(this);
//        mCurrentLocation.setText(FlickWeather.mCity);
        Log.d("设置页面","被调用"+FlickWeather.mCity);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<Weather>> {
        @Override
        protected List<Weather> doInBackground(Void... params) {
            List<Weather> items1 = new ArrayList<>();
            try {
                items1 = new FlickWeather().fetchItems();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return items1;
        }

        @Override
        protected void onPostExecute(List<Weather> items) {
            WeatherListFragment.mItems = items;
        }
    }
}
