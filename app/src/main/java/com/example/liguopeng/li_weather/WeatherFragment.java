package com.example.liguopeng.li_weather;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.UUID;

import static com.example.liguopeng.li_weather.WeatherListFragment.mPad;

//详细页的设计
public class WeatherFragment extends Fragment {

    private static final String ARG_WEATHER_ID = "weather_id";
      private TextView mDay;
      private TextView mDate;
      private TextView mHighTem;
      private TextView mLowTem;
      private ImageView mWeatherView_d;
      private ImageView mWeatherView_n;
      private TextView mImageText;
      private TextView mHumidity;
      private TextView mPressure;
      private TextView mWind;
      private Weather mWeather;
     // private LinearLayout LL1,LL2;

    /*
    * 每个fragment实例都可以附带一个Bundle对象.该bundle包含key-value
    * 要附加argument bundle给fragment，需要调用fragment.setArguments。
    * 而且必须在fragment创建后，添加给activity之前完成
    * */

    /*
    * newInstance的static方法可以完成上述要求，使用该方法，完成fragment实例及bundle对象的创建
    * 然后将argument放入bundle中，最后再附加给fragment
    *在托管的activity需要fragment实例时，转而调用newInstance，并不是调用构造函数。
    * */
    public static WeatherFragment newInstance(String weatherId) {
        Bundle args = new Bundle();
        args.putString(ARG_WEATHER_ID, weatherId);

        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        String weatherId = (String) getArguments().getString(ARG_WEATHER_ID);
       // Log.d("received细节视图",weatherId);
        mWeather = WeatherLab.get(getActivity()).getWeather(weatherId);
        Log.d("细节视图","onCreate调用");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("细节视图","onResume调用");
        //setAll();
    }

    @Override
    public void onStop() {
        super.onStop();
//       if(mPad){
//            LL1.removeAllViews();//移除所有视图
//       }
        Log.d("细节视图","onStop调用");
//        onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_weather,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_share_weather:
/*
* 接下来是点击share发生的事情.
* */
                Intent sendIntent =new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"如下是"+FlickWeather.mCity+"地区"+mWeather.getDay()+","+mWeather.getDate()+"的天气情况:"+mWeather.getImageText()+","+"最高温度"+mWeather.getTopTem()
                +"最低温度"+mWeather.getLowTem());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"您可以选择分享给如下应用"));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);
/*
* 声明组件，对组件进行操作
* */
        //LL1=(LinearLayout)v.findViewById(R.id.LL1);
       // LL2=(LinearLayout)v.findViewById(R.id.LL2);

        mDay=(TextView) v.findViewById(R.id.day);
        mDay.setText(mWeather.getDay());

        mDate=(TextView)v.findViewById(R.id.date);
        mDate.setText(mWeather.getDate());

        mHighTem=(TextView)v.findViewById(R.id.high_tem);
        mHighTem.setText(mWeather.getTopTem());

        mLowTem=(TextView)v.findViewById(R.id.low_tem);
        mLowTem.setText(mWeather.getLowTem());

        mWeatherView_d=(ImageView)v.findViewById(R.id.weather_image);
        //图片
        mWeatherView_d.getDrawable().setLevel(mWeather.getImageSrc_d());

        mWeatherView_n=(ImageView)v.findViewById(R.id.weather_image2);//晚上的图片
        mWeatherView_n.getDrawable().setLevel(mWeather.getImageSrc_n());

        mImageText=(TextView) v.findViewById(R.id.image_text);
        mImageText.setText(mWeather.getImageText());

        mHumidity=(TextView)v.findViewById(R.id.humidity);
        mHumidity.setText(mWeather.getHumidity());

        mPressure=(TextView)v.findViewById(R.id.pressure);
        mPressure.setText(mWeather.getPressure());

        mWind=(TextView)v.findViewById(R.id.wind);
        mWind.setText(mWeather.getWind());


        return v;
    }
    public void setAll(){

        mDay.setText(mWeather.getDay());
        mDate.setText(mWeather.getDate());
        mHighTem.setText(mWeather.getTopTem());
        mLowTem.setText(mWeather.getLowTem());
        //图片
        mWeatherView_d.getDrawable().setLevel(mWeather.getImageSrc_d());
        mWeatherView_n.getDrawable().setLevel(mWeather.getImageSrc_n());
        mImageText.setText(mWeather.getImageText());
        mHumidity.setText(mWeather.getHumidity());
        mPressure.setText(mWeather.getPressure());
        mWind.setText(mWeather.getWind());

    }
}
