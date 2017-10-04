package com.zhongying.mineweather.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.activity.WeatherActivity;
import com.zhongying.mineweather.constant.Constant;
import com.zhongying.mineweather.db.CityAdminItem;
import com.zhongying.mineweather.db.CityAdminItemManager;
import com.zhongying.mineweather.fragment.base.BaseFragment;
import com.zhongying.mineweather.gson.DailyForecast;
import com.zhongying.mineweather.gson.HeWeather;
import com.zhongying.mineweather.gson.Wind;
import com.zhongying.mineweather.okhttp.HttpCallback;
import com.zhongying.mineweather.okhttp.HttpUtil;
import com.zhongying.mineweather.service.AutoUpdateService;
import com.zhongying.mineweather.utily.SharedPreferencesManager;
import com.zhongying.mineweather.utily.Utilies;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @class: 控制主要天气页面的展示
 * Created by Administrator on 2017/9/19.
 */

public class WeatherFragment extends BaseFragment implements View.OnClickListener,View.OnLongClickListener
            ,SwipeRefreshLayout.OnRefreshListener{

    private String TAG = "WeatherFragment";

    private WeatherActivity mActivity;

    //标题栏
    private ImageView mChooseArea_iv;

    private ImageView mPlusArea_iv,mSetting_iv,mShare_iv;

    //天气界面的第一层父容器
    private LinearLayout mWeatherControl_linear;

    //主天气部分
    private TextView mCountyName_tv;
    private ImageView mIsCollected_iv;//收藏城市图标
    private TextView mNowTemperature_tv;
    private ImageView mNowCondIcon_iv;
    private TextView mNowCondTxt_tv;
    private TextView mAirQuality_tv;
    private TextView mMinTemp_tv;
    private TextView mMaxTemp_tv;

    //未来一小时
    private TextView mNextHourTemp_tv;
    private ImageView mNextHourCode_iv;
    private TextView mNextHourTxt_tv;
    private TextView mNextHourWin_tv;

    //3天
    private LinearLayout mDays_linearLayout;

    //建议
    private TextView mSuggTravel_tv;

    //更新
    private TextView mUpdateTime_tv;

    boolean isHadLongClick = false;

    //下拉刷新
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //当前的城市id
    private String mWeatherId;
    public String getWeatherId() {
        return mWeatherId;
    }


    //城市是否被收藏,true：被收藏
    private boolean mIsCollected;

    //当前天气对应的数据实体类
    private HeWeather mHeWeather;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (WeatherActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,"生命周期：onCreateView()");
        if(mActivity == null){
            mActivity = (WeatherActivity) getActivity();
        }
        View view = inflater.inflate(R.layout.fragment_weather_layout,container,false);
        initView(view);
        initData();

        return view;
    }


    private void initView(View view){
        //伪标题栏
        mChooseArea_iv = (ImageView) view.findViewById(R.id.to_choose_area_iv);
        mChooseArea_iv.setOnClickListener(this);
        mPlusArea_iv = (ImageView) view.findViewById(R.id.plus_area_iv);
        mPlusArea_iv.setOnClickListener(this);
        mPlusArea_iv.setOnLongClickListener(this);
        mSetting_iv = (ImageView) view.findViewById(R.id.setting_iv);
        mSetting_iv.setOnClickListener(this);
        mShare_iv = (ImageView) view.findViewById(R.id.share_iv);
        mShare_iv.setOnClickListener(this);
        //界面控制LinearLayout
        mWeatherControl_linear = (LinearLayout) view.findViewById(R.id.weather_control_linear);
        //主天气介绍
        mCountyName_tv = (TextView) view.findViewById(R.id.county_name_tv);
        mIsCollected_iv = (ImageView) view.findViewById(R.id.is_collect_iv);
        mIsCollected_iv.setOnClickListener(this);
        mNowTemperature_tv = (TextView) view.findViewById(R.id.now_temp_tv);
        mNowCondIcon_iv = (ImageView) view.findViewById(R.id.now_cond_icon_iv);
        mNowCondTxt_tv = (TextView) view.findViewById(R.id.now_cond_txt_tv);
        mAirQuality_tv = (TextView) view.findViewById(R.id.air_quality_tv);
        mMinTemp_tv = (TextView) view.findViewById(R.id.min_temp_tv);
        mMaxTemp_tv = (TextView) view.findViewById(R.id.max_temp_tv);
        //未来一小时
        mNextHourTemp_tv = (TextView) view.findViewById(R.id.next_hour_temp_tv);
        mNextHourCode_iv = (ImageView) view.findViewById(R.id.next_hour_code_iv);
        mNextHourTxt_tv = (TextView) view.findViewById(R.id.next_hour_txt_tv);
        mNextHourWin_tv = (TextView) view.findViewById(R.id.next_hour_win_tv);
        //3天
        mDays_linearLayout= (LinearLayout) view.findViewById(R.id.days_linear);
        //建议、更新
        mSuggTravel_tv = (TextView) view.findViewById(R.id.sugg_travel_tv);
        mUpdateTime_tv = (TextView) view.findViewById(R.id.update_time_tv);

        //下拉刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    private void initData(){
        //初始化天气数据
        Log.i(TAG,"initData()>>>>>>>>>>>");

        String weatherText = SharedPreferencesManager
                .getInstance().getString(Constant.SHARED_KEY_WEATHER_JSON);
        //没有从本地prefer获取天气weatherJson，从网络请求
        if(weatherText == null || TextUtils.isEmpty(weatherText)){
            Log.i(TAG,"没有数据缓存！！");
            Intent it = mActivity.getIntent();
            if("MainActivity".equals(it.getStringExtra("from"))){
                mWeatherId = it.getStringExtra("weather_id");
                Log.i(TAG,"initData() --- mWeatherId:"+mWeatherId);
                mWeatherControl_linear.setVisibility(View.INVISIBLE);
                requestWeather(mWeatherId);
            }
        }else {
            Log.i(TAG,"有数据缓存！！");
            mHeWeather = Utilies.handleWeatherResponse(weatherText);
            if(mHeWeather == null){
                return;
            }
            mWeatherId = mHeWeather.basic.weatherId;
            showWeatherInfo(mHeWeather);
        }
        Log.i(TAG,"<<<<<<<<<<<<<initData()");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.to_choose_area_iv:
                mActivity.openDrawer();
                break;
            case R.id.plus_area_iv:
                //根据情况来确定是跳转到管理城市页面还是收起imageView

                //收起两个ImageView
                if(isHadLongClick){
                    closeWindow();
                    break;
                }

                //跳转到管理城市的界面
                mActivity.startAdminCountyActivity();
                break;
            case R.id.setting_iv:
                //跳转到设置界面
                break;
            case R.id.share_iv:
                //分享功能
                break;
            case R.id.is_collect_iv:
                //收藏城市
                reverseCollectCity();

                break;
        }
    }

    private void openWindow(){
        mPlusArea_iv.setImageResource(R.mipmap.plus);
        isHadLongClick = false;

        ObjectAnimator animatorPlus = ObjectAnimator.
                ofFloat(mSetting_iv,"translationY",0,250f);
        ObjectAnimator animatorShare = ObjectAnimator.
                ofFloat(mShare_iv,"translationY",0,500f);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animatorPlus,animatorShare);
        set.setInterpolator(new BounceInterpolator());
        set.setDuration(120);
        set.start();
    }

    private void closeWindow(){
        mPlusArea_iv.setImageResource(R.mipmap.plus);
        isHadLongClick = false;

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorPlus;
        ObjectAnimator animatorShare;
        animatorPlus = ObjectAnimator.
                ofFloat(mSetting_iv,"translationY",250f,0);
        animatorShare = ObjectAnimator.
                ofFloat(mShare_iv,"translationY",500f,0);
        set.playTogether(animatorPlus,animatorShare);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSetting_iv.setVisibility(View.INVISIBLE);
                mShare_iv.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.plus_area_iv:
                Log.i(TAG,"onLongClick()");

                if(isHadLongClick){
                //收起两个imageView
                    closeWindow();
                }else {
                //展开两个imageView
                    openWindow();
                }

                break;
        }
        return true;
    }

    //请求网络数据
    public void requestWeather(final String weatherId){
        Log.i(TAG,"requestWeather()>>>>>>>>>>>>");

        if(weatherId == null){
            Log.i(TAG,"weatherId == null");
            return;
        }
        String url = Utilies.getWeatherUrl(weatherId);
        if(mWeatherId!=null && !weatherId.equals(mWeatherId)){
            mWeatherId = weatherId;
        }

        HttpUtil.requestOkHttpUrl(url, new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity,"网络请求失败....",
                                Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                        mActivity.setRequestCollectCity(false);
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String text = response.body().string();

                final HeWeather weather = Utilies.handleWeatherResponse(text);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //保存网络下载的信息
                        if(Utilies.isWeatherResponseAvailable(weather)){
                            SharedPreferencesManager.getInstance()
                                    .putString(Constant.SHARED_KEY_WEATHER_JSON,text);
                            mHeWeather = weather;//保存一个实体类对象

                            //收藏该城市
                            if(mActivity.getRequestCollectCity()){
                                collectCity();
                            }
                            mActivity.setRequestCollectCity(false);

                            showWeatherInfo(weather);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }else {
                            Toast.makeText(mActivity,"请求的天气信息有问题....",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        Log.i(TAG,"<<<<<<<<<<<<requestWeather()");
    }

    //对界面的控制设置天气数据
    private void showWeatherInfo(HeWeather weather){
        if(weather == null || !"ok".equals(weather.status)){
            Toast.makeText(mActivity,"设置天气数据失败...",Toast.LENGTH_SHORT).show();
            return;
        }
        //主界面
        mCountyName_tv.setText(weather.basic.cityName);

        mNowTemperature_tv.setText(weather.now.temperature);
        mNowCondIcon_iv.setImageResource(getWeatherIconId(weather.now.condition.code));
        mNowCondTxt_tv.setText(weather.now.condition.info);
        mAirQuality_tv.setText(weather.aqi.city.quality);
        mMinTemp_tv.setText(weather.dailyForecastList.get(0).temp.minTemp);
        mMaxTemp_tv.setText(weather.dailyForecastList.get(0).temp.maxTemp);
        //未来一小时
        //有时候会抽风，不返回数据
        if(weather.hourlyForecastList.size()!=0){
            mNextHourTemp_tv.setText(weather.hourlyForecastList.get(0).temperature);
            mNextHourCode_iv.setImageResource(getWeatherIconId(weather.hourlyForecastList.get(0).cond.code));
            mNextHourTxt_tv.setText(weather.hourlyForecastList.get(0).cond.info);
            mNextHourWin_tv.setText(dealWinInfo(weather.hourlyForecastList.get(0).wind));
        }

        //3天
        mDays_linearLayout.removeAllViews();
        for(DailyForecast forecast:weather.dailyForecastList){
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_days_layou,mDays_linearLayout,false);
            ImageView condIcon = (ImageView) view.findViewById(R.id.days_cond_iv);
            TextView win_tv = (TextView) view.findViewById(R.id.days_win_tv);
            TextView sunrise = (TextView) view.findViewById(R.id.days_sunrise_tv);
            TextView sundown = (TextView) view.findViewById(R.id.days_sundown_tv);
            condIcon.setImageResource(getWeatherIconId(forecast.condition.code));
            win_tv.setText(dealWinInfo(forecast.wind));
            sunrise.setText(forecast.astronomy.sunrise);
            sundown.setText(forecast.astronomy.sundown);

            mDays_linearLayout.addView(view);
        }
        //建议、更新
        mSuggTravel_tv.setText(weather.suggestion.travel.info);
        mUpdateTime_tv.setText(weather.basic.update.updateTime);

        mWeatherControl_linear.setVisibility(View.VISIBLE);

        //收藏图标的显示
        showCollectCityIcon();

        //自动更新
        Intent it = new Intent(mActivity, AutoUpdateService.class);
        mActivity.startService(it);
    }

    //组合风的天气信息
    private String dealWinInfo(Wind wind){
        String s = wind.windForce;
        return s.substring(0,s.length()-1)+wind.direction;
    }

    //设置天气对应的图标,直接本地查询，不再使用网络方式
    private int getWeatherIconId(String code){
        int id = Utilies.getWeatherIconId(code);
        if(id == -1){
            Utilies.getWeatherIconId("100");
        }
        return id;
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        requestWeather(mWeatherId);
    }

    //打开下拉刷新
    public void openRefresh(){
        mSwipeRefreshLayout.setRefreshing(true);
    }
    //关闭下拉刷新
    public void closeRefresh(){
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //显示收藏城市图标
    private void showCollectCityIcon(){
        Log.i(TAG,"showCollectCityIcon()>>>>>>>>>>>>>>");
        Log.i(TAG,"showCollectCityIcon()----mWeatherId:"+mWeatherId);

        if(CityAdminItemManager.getInstance().isHadByWeatherId(mWeatherId)){
            //当前城市已被收藏
            Log.i(TAG,"当前城市已被收藏");
            mIsCollected_iv.setImageResource(R.mipmap.collect);
            mIsCollected = true;
        }else {
            Log.i(TAG,"当前城市未被收藏");
            mIsCollected_iv.setImageResource(R.mipmap.uncollect);
            mIsCollected = false;
        }
        Log.i(TAG,"<<<<<<<<<<<<<showCollectCityIcon()");
    }

    //反转当前收藏城市的操作，收藏改为不收藏，不收藏改为收藏
    public void reverseCollectCity(){
        Log.i(TAG,"reverseCollectCity()>>>>>>>>>>>>>>");
        if(mIsCollected){
            //取消收藏
            Log.i(TAG,"已经收藏当前的城市，请求取消收藏");
            uncollectCity();
        }else {
            //请求收藏
            Log.i(TAG,"未收藏当前的城市，请求收藏");
            collectCity();
        }
        changeCollectCityIcon(mIsCollected);
        Log.i(TAG,"<<<<<<<<<<<<<<reverseCollectCity()");
    }

    //更改收藏城市图标的显示
    private void changeCollectCityIcon(boolean collect){
        if(collect){
            mIsCollected_iv.setImageResource(R.mipmap.collect);
        }else {
            mIsCollected_iv.setImageResource(R.mipmap.uncollect);
        }
    }

    //取消收藏城市
    private void uncollectCity(){
        if(!CityAdminItemManager.getInstance().isHadByWeatherId(mWeatherId)){
            return;
        }
        mIsCollected = false;
        CityAdminItemManager.getInstance().delete(mWeatherId);
    }
    //收藏城市
    private void collectCity(){
        if(CityAdminItemManager.getInstance().isHadByWeatherId(mWeatherId)){
            return;
        }
        mIsCollected = true;
        CityAdminItem item = new CityAdminItem();
        item.setCityName(mHeWeather.basic.cityName);
        item.setAirQuality(mHeWeather.aqi.city.quality);
        item.setCityWeatherId(mWeatherId);
        item.setIconCode(mHeWeather.now.condition.code);
        item.setTemperature(mHeWeather.now.temperature);
        item.setMinTemperature(mHeWeather.dailyForecastList.get(0).temp.minTemp);
        item.setMaxTemperature(mHeWeather.dailyForecastList.get(0).temp.maxTemp);
        item.setWindDir(mHeWeather.dailyForecastList.get(0).wind.direction);
        item.setWindForce(mHeWeather.dailyForecastList.get(0).wind.windForce);

        CityAdminItemManager.getInstance().insert(item);
    }

}
