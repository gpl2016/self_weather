package com.example.liguopeng.li_weather;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.example.liguopeng.li_weather.databases.WeatherBaseHelper;

import java.lang.reflect.Field;

import static com.example.liguopeng.li_weather.WeatherSetting.NOTIFICATION;
/*
*
* 如果同一个城市的uodatetime发生了改变，发出通知？
*
*
* */

/*
*进入主界面，如果通知是开启的，直接启动通知服务，定期1分钟1通知？可以设置一个频率选择。
* 开启与否是通过设置页面的那个checkbox决定的
* service里面不能有耗时操作，所以天气预报的那个定时，是需要开启一个子线程的
* */
public class WeatherNotificationService extends Service {

    private Weather mWeather;
 //   private String Todaydate=LocalDate.getLocalDate();
    public static String Todayid;
    public static final String TAG = "MyService";
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private  String today;


    private MyBinder mBinder=new MyBinder();
    /*
    * onCreate()方法只会在Service第一次被创建的时候调用，如果当前Service已经被创建过了，
    * 不管怎样调用startService()方法，onCreate()方法都不会再执行。但是会执行onStartCommand
    * */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
/*
*
* 任何一个Service在整个应用程序范围内都是通用的，
 * 即MyService不仅可以和MainActivity建立关联，还可以和任何一个Activity建立关联，
 * 而且在建立关联时它们都可以获取到相同的MyBinder实例。
 * 一个Service必须要在既没有和任何Activity关联又处理停止状态的时候才会被销毁。所以要先解绑，再销毁
 * */

    /*
     * 既然在Service里也要创建一个子线程，那为什么不直接在Activity里创建呢？
     * 这是因为Activity很难对Thread进行控制，当Activity被销毁之后，就没有任何其它的办法可以再重新获取到之前创建的子线程的实例。
     * 而且在一个Activity中创建的子线程，另一个Activity无法对其进行操作。
     * 但是Service就不同了，所有的Activity都可以与Service进行关联，
     * 然后可以很方便地操作其中的方法，即使Activity被销毁了，之后只要重新与Service建立关联，
     * 就又能够获取到原有的Service中Binder的实例。因此，使用Service来处理后台任务，
     * Activity就可以放心地finish，完全不需要担心无法对后台任务进行控制的情况。
     *现在我们可以在Activity中根据具体的场景来调用MyBinder中的任何public方法，
     * 即实现了Activity指挥Service干什么Service就去干什么的功能。
* */
class MyBinder extends Binder{
private Handler mHandler;
private Runnable runnable;
private Thread notificationThread;

private  boolean noti=false;
        public void startNotification(){

            noti=true;
            mHandler=new Handler();
            mHandler.post(runnable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "weather";
                String channelName = "定时天气通知";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                createNotificationChannel(channelId, channelName, importance);

                channelId = "new'_weather";
                channelName = "新天气通知";
                importance = NotificationManager.IMPORTANCE_HIGH;
                createNotificationChannel(channelId, channelName, importance);
                Log.d(TAG,"if exe");
            }




            notificationThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    if(noti==false||NOTIFICATION==false){
                        Log.d(TAG,"unti=false exe");
                    }
                    else{
                        sendChatMsg();
                        Log.d(TAG,"run exe");
                        mHandler.postDelayed(this,5000);

                    }

                }

            });

            notificationThread.start();
//                    =new Runnable() {
//                @Override
//                public void run() {
//                    sendChatMsg();
//                    mHandler.postDelayed(this,2000);
//                    Log.d(TAG,"run exe");
//                }
//            };
            //sendChatMsg();

            Log.d(TAG,"startNotification exe");
        }

        public void stopNotification(){
            noti=false;
            notificationThread.interrupt();
            notificationThread=null;
            Log.d(TAG,"stopNotification exe");
            mHandler.removeCallbacks(notificationThread);

        }

        private void startWeather(){
            Log.d(TAG,"startWeather exe");
        }
    }




    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    public void sendChatMsg() {
    /*
    * 更改城市之后，Todayid没有更新，应该在设置完城市之后，就更改todayid，或者回到setting页面页面更改todayid
    * 这个改成数据库之后就比较好写了，直接where date=今日时间，或者直接取第一条数据就可以
    * */
        mContext=this.getApplicationContext();
        mDatabase=new WeatherBaseHelper(mContext).getWritableDatabase();
        today=LocalDate.getLocalDate();
        Log.d(TAG,today);
        Cursor cursor=mDatabase.rawQuery("select id from weathers where initdate=?",new String[]{today});
        while (cursor.moveToNext()){
            Todayid =cursor.getString(0);
            Log.d(TAG,Todayid);
        }
        mDatabase.close();

        mWeather = WeatherLab.get(this).getWeather(Todayid);
        int id;
        id=getResource("a"+String.valueOf(mWeather.getCond_code_d()));
        Log.d("id",String.valueOf(id));
        Intent intent =new Intent(this,WeatherListActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "weather")
                .setContentTitle(mWeather.getDay()+mWeather.getDate())
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher2)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), id))
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("如下是"+mWeather.getLocation()+"地区"+mWeather.getDay()+"的天气情况:"+mWeather.getImageText()+","+"最高温度"+mWeather.getTopTem()
                        +"最低温度"+mWeather.getLowTem()))
                .build();
        manager.notify(1, notification);
    }

    public static int  getResource(String imageName){

        Class drawable = R.drawable.class;
        try {
            Field field = drawable.getField(imageName);
            int resId = field.getInt(imageName);
            return resId;
        } catch (NoSuchFieldException e) {//如果没有在"mipmap"下找到imageName,将会返回0
            return 0;
        } catch (IllegalAccessException e) {
            return 0;
        }

    }


}