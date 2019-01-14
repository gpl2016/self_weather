package com.example.liguopeng.li_weather;

import android.app.Activity;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liguopeng.li_weather.databases.WeatherBaseHelper;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import  android.content.ContextWrapper;
import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.liguopeng.li_weather.WeatherNotificationService.Todayid;
import static com.example.liguopeng.li_weather.WeatherSetting.NOTIFICATION;
import static com.example.liguopeng.li_weather.WeatherSetting.mIschecked;
import static com.example.liguopeng.li_weather.WeatherSetting.notificationConnection;

/*
* 第一页的设计
* 在weather手机程序中，应该是上面显示东西，下面显示列表
* */

/*
* 平板视图，希望在展示的时候直接在右面显示当天的天气情况
* 在改过城市之后，右侧也展示当天的天气情况
* 先是获取今天的日期，根据日期去数据库里面（你自己现在写的好像是Lab哦~~~bug会不会根据这个有关）取出来一个weather，从数据库中取出来，都是初始值，需要初始化，才能变成需要的。
* 再把这个weather给细节视图，启动细节视图，但是现在主要问题是，数据库总是空的，总是拿不到weather，跟踪一下mItems
* 还有一个问题是，获取本地数据，为什么会出来那么多条数据----》getLocalweather,总是在add，然后返回的，就多了？
*
* */
public class WeatherListFragment extends Fragment {
    private static final String TAG="WeatherListFragment";
    private RecyclerView mWeatherRecyclerView;
    public static List<Weather> mItems=new ArrayList<>();//这是剩余的
    private SQLiteDatabase mDatabase;
    public static boolean mPad=false;
    private Callbacks mCallbacks;//定义成员变量，存放实现callbacks接口的对象
    private double mlon;//经度
    private double mlat;//纬度
    private String address="天气位置";
    public  static  boolean  mbinding=false;//通知服务是否绑定
    public  static  boolean  mstopbinding=false;//停止通知服务是否绑定

/*
* 委托工作任务托管给activity，通常的做法是由fragment定义名为Callbacks的回调接口。
* 回调接口定义了fragment委托给托管activity处理的工作任务。任何打算托管目标fragment的activity都必须实现它
* */
    public interface Callbacks{
        void onWeatherSelected(Weather weather);
    }

/*
* onAttach()：执行该方法时，Fragment与Activity已经完成绑定，该方法有一个Activity类型的参数，代表绑定的Activity
* */
    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        mCallbacks=(Callbacks)activity;//将托管activity强制类型转换为Callbacks对象并赋值给Callbacks类型变量。在Detach中要赋值为null
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*获取上一次退出时的城市*/
        mDatabase=new WeatherBaseHelper(getActivity()).getWritableDatabase();
        String temp1;
        Cursor cursor1=mDatabase.rawQuery("select city from setting ",null);
        while (cursor1.moveToNext()){
            temp1=cursor1.getString(0);
            FlickWeather.mCity=temp1;
        }
        mDatabase.close();

        new FetchItemsTask().execute();//获取数据

        setHasOptionsMenu(true);//让FragmentManager知道WeatherListFragment需要接受选项菜单方法回调
        setRetainInstance(true);
        mPad=isPad.isPada(getActivity());//判断是否为平板
        if(!IsInternet.isNetworkAvalible(getActivity())) {
            Toast.makeText(getActivity(),"当前无网络连接，显示退出前天气情况",Toast.LENGTH_LONG).show();
            mItems=getLocalWeather.getLocalWeathers(getActivity());
            WeatherLab.get(this.getActivity());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_list, container, false);
        mPad=isPad.isPada(getActivity());
        mWeatherRecyclerView = (RecyclerView) view
                .findViewById(R.id.weather_recycler_view);
        mWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return view;

    }


    private void setupAdapter(){

        /*获取上一次退出时候的温度单位*/
        mDatabase=new WeatherBaseHelper(getActivity()).getWritableDatabase();
        String temp;
        Cursor cursor=mDatabase.rawQuery("select unti from setting ",null);
        while (cursor.moveToNext()){
            temp=cursor.getString(0);
            if(temp.equals("true")){
                Weather.UNTI=true;
            }
            else
                Weather.UNTI=false;
        }
        mDatabase.close();


        /*获取上一次退出时，是否设置通知,现在是能够获取到了，但是直接设置那面的状态是有bug的，如果是true，但是没有启动服务，再点一次就bug了*/
        mDatabase=new WeatherBaseHelper(getActivity()).getWritableDatabase();
        String temp2;
        Cursor cursor2=mDatabase.rawQuery("select notification from setting ",null);
        while (cursor2.moveToNext()){
            temp2=cursor2.getString(0);
            if(temp2.equals("true")){
                mIschecked=true;
            }
            else
                mIschecked=false;
        }
        mDatabase.close();


        if(isAdded()){//后面的判断是新增的1202
            if(!IsInternet.isNetworkAvalible(getActivity())){
                mItems=getLocalWeather.getLocalWeathers(getActivity());
                mWeatherRecyclerView.setAdapter(new WeatherAdapter(mItems));
                WeatherLab.get(getActivity());
            }
            else{
                WeatherLab.get(getActivity());
                mWeatherRecyclerView.setAdapter(new WeatherAdapter(mItems));
                Log.d("ccc", "setAdapter调用"+String.valueOf(mItems.size()));
            }
        }
        //平板情况下，右侧也显示
        if(mPad&&mItems.size()!=0){//加上这个数据库就被清除？或者说是拿不到数据？->是因为你太着急了，没等数据过来，你就想拿数据
            Weather initweather=getLocalWeather.getLocalWeather(getActivity());
            mCallbacks.onWeatherSelected(initweather);
        }

        if(mIschecked&&mItems.size()!=0){//加入之后，通知会变多，怎么区别是第一次进来，还是后台运行启动的->看服务是否被绑定过，如果已经绑定过了，就不要再绑定了。
              if(mbinding){
                  Log.d("MyService","mbinding这里+++成功了");
              }
              else {
            NOTIFICATION=true;
            Log.d("MyService","这里+++"+String.valueOf(NOTIFICATION));
            Intent startIntent=new Intent(getActivity(),WeatherNotificationService.class);
            getActivity().startService(startIntent);//先启动服务
            Intent bindIntent1 = new Intent(getActivity() ,WeatherNotificationService.class);
            getActivity().bindService(bindIntent1, notificationConnection, BIND_AUTO_CREATE);}
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setupAdapter();//作用是改变城市/设置之后，更新数据，很重要的。
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragemnt_weather_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_setting_weather:
                Intent intent=new Intent(getActivity(),WeatherSetting.class);
                startActivity(intent);
                return true;
            case R.id.menu_item_map_weather:
                if(MapUtil.isBaiduMapInstalled()){
                    MapUtil.openBaiDuNavi(getActivity(),0,0,null,mlat,mlon,address);
                }
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }

/*
* 列表部分
* RecyclerView的任务仅限于回收和定位屏幕上的TextView
* TextView能够显示数据离不开Adapter子类和ViewHolder子类
* ViewHolder子类作用：容纳View视图
* RecyclerView自身不会创建视图，它创建的是ViewHolder，ViewHolder引用这一个个itemView
* */
    private class WeatherHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageView mListImage_d;
        private ImageView mListImage_n;
        private TextView mListDay;
        private TextView mListImageText;
        private TextView mLsitHighTem;
        private TextView mLsitLowTem;
        private Weather mWeather;

        public WeatherHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mListImage_d=(ImageView)itemView.findViewById(R.id.list_item_weather_image);
            mListImage_n=(ImageView)itemView.findViewById(R.id.list_item_weather_image2);
            mListDay=(TextView)itemView.findViewById(R.id.list_item_day);
            mListImageText=(TextView)itemView.findViewById(R.id.list_item_image_text);
            mLsitHighTem=(TextView)itemView.findViewById(R.id.list_item_high_tem);
            mLsitLowTem=(TextView)itemView.findViewById(R.id.list_item_low_tem);
        }

        public void bindWeather(Weather weather)  {
            mWeather = weather;
            mlat=Double.parseDouble(weather.getLat());
            mlon=Double.parseDouble(weather.getLon());
            if (mWeather.getinitDate().equals(LocalDate.getLocalDate())){
                Todayid=mWeather.getId();
                if(!mPad){//如果是平板，就没有上面那部分了
                SingleFragmentActivity.mMainDayDate.setText(mWeather.getDay()+","+mWeather.getDate());
                SingleFragmentActivity.mMainHighTem.setText(mWeather.getTopTem());
                SingleFragmentActivity.mMainLowTem.setText(mWeather.getLowTem());
                SingleFragmentActivity.mMainWeatherImage_d.getDrawable().setLevel(mWeather.getImageSrc_d());//图片
                SingleFragmentActivity.mMainWeatherImage_n.getDrawable().setLevel(mWeather.getImageSrc_n());//图片
                SingleFragmentActivity.mMainWeatherText.setText(mWeather.getImageText());
                SingleFragmentActivity.mMainCurrentCity.setText(mWeather.getLocation());
                }

                mListImage_d.getDrawable().setLevel(mWeather.getImageSrc_d());//图片的层级，参考收藏的网页
                mListImage_n.getDrawable().setLevel(mWeather.getImageSrc_n());//图片的层级，参考收藏的网页
                mListDay.setText(mWeather.getDay());
                mListImageText.setText(mWeather.getImageText());
                mLsitHighTem.setText(mWeather.getTopTem());
                mLsitLowTem.setText(mWeather.getLowTem());
            }

            else{
                mListImage_d.getDrawable().setLevel(mWeather.getImageSrc_d());//图片的层级，参考收藏的网页
                mListImage_n.getDrawable().setLevel(mWeather.getImageSrc_n());//图片的层级，参考收藏的网页
                mListDay.setText(mWeather.getDay());
                mListImageText.setText(mWeather.getImageText());
                mLsitHighTem.setText(mWeather.getTopTem());
                mLsitLowTem.setText(mWeather.getLowTem());
            }

        }

        @Override
        public void onClick(View v) {
            mCallbacks.onWeatherSelected(mWeather);
        }
    }
/*
 * RecyclerView自身不会创建视图，它创建的是ViewHolder：这个实际任务是Adapter来完成的。
 * adapter是个控制器对象，从模型层获取数据，然后提供给RecyclerView显示
 * adapter负责：创建必要的ViewHolder、绑定ViewHolder至模型层数据
 * RecyclerView需要显示视图对象时，就会去找它的adapter
 * 首先调用adapter的getItemCount()方法，RecyclerView询问数组列表有多少个对象
 *2.RecyclerView调用adapter的createViewHolder方法创建ViewHolder以及ViewHolder要显示的视图。
 *3.RecyclerView会传入ViewHolder及其位置，调用onBindViewHolder方法。adapter会找到目标位置的数据并绑定到ViewHolder的视图上。绑定：使用模型数据填充视图。
 */


    private class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder> {

        private List< Weather> mWeathers;

        public  WeatherAdapter(List< Weather> weathers) {
            mWeathers = weathers;
        }

        @Override
        public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_weather, parent, false);
            return new WeatherHolder(view);
        }

        @Override
        public void onBindViewHolder(WeatherHolder holder, int position) {
            Weather weather = mWeathers.get(position);
            holder.bindWeather(weather);
        }

        @Override
        public int getItemCount() {
            return mWeathers.size();
        }


    }



    /*
     * FetchItemsTask是新加入的
     * */
    private class FetchItemsTask extends AsyncTask<Void,Void,List<Weather>>{
        @Override
        protected List<Weather> doInBackground(Void...params) {
            List<Weather> items1 =new ArrayList<>();
            try {
                items1= new FlickWeather().fetchItems();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return items1;
        }

        @Override
        protected void onPostExecute(List<Weather> items) {
            mItems=items;
            setupAdapter();
        }

    }
/*
* 解除与Activity的绑定。在onDestroy方法之后调用。
* */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(notificationConnection);//不加的话，service会泄露（leaked）

    }
}
