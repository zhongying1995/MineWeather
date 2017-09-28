package com.zhongying.mineweather.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
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
import com.zhongying.mineweather.constant.Constant;
import com.zhongying.mineweather.db.SharedPreferencesManager;
import com.zhongying.mineweather.fragment.base.BaseFragment;
import com.zhongying.mineweather.gson.DailyForecast;
import com.zhongying.mineweather.gson.HeWeather;
import com.zhongying.mineweather.gson.Wind;
import com.zhongying.mineweather.okhttp.HttpCallback;
import com.zhongying.mineweather.okhttp.HttpUtil;
import com.zhongying.mineweather.service.AutoUpdateService;
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

    private Activity mActivity;

    //标题栏
    private ImageView mChooseArea_iv;

    private ImageView mPlusArea_iv,mSetting_iv,mShare_iv;

    //天气界面的第一层父容器
    private LinearLayout mWeatherControl_linear;

    //主天气部分
    private TextView mCountyName_tv;
    private ImageView mIsCollected_iv;
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = getActivity();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mActivity == null){
            mActivity = getActivity();
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
        String weatherText = SharedPreferencesManager
                .getInstance().getString(Constant.SHARED_KEY_WEATHER_JSON);
        if(weatherText == null || TextUtils.isEmpty(weatherText)){
            //没用从本地prefer获取天气weatherJson，从网络请求
            mWeatherId = mActivity.getIntent().getStringExtra("weather_id");
            mWeatherControl_linear.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }else {
            HeWeather weather = Utilies.handleWeatherResponse(weatherText);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.to_choose_area_iv:

                break;
            case R.id.plus_area_iv:
                Log.i(TAG,"onClick()");
                if(isHadLongClick){
                //收起两个ImageView
                    closeWindow();
                    break;
                }

                break;
            case R.id.setting_iv:

                break;
            case R.id.share_iv:
                Log.i(TAG,"share_iv 被点击!");
                break;
            case R.id.is_collect_iv:

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

        String url = Utilies.getWeatherUrl(weatherId);

        HttpUtil.requestOkHttpUrl(url, new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity,"请求天气信息失败....",
                                Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
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
                            showWeatherInfo(weather);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }else {
                            Toast.makeText(mActivity,"请求天气信息失败....",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    //对界面的控制设置天气数据
    private void showWeatherInfo(HeWeather weather){
        if(weather == null || !"ok".equals(weather.status)){
            Toast.makeText(mActivity,"请求天气数据失败...",Toast.LENGTH_SHORT).show();
            return;
        }
        //主界面
        mCountyName_tv.setText(weather.basic.city);
        //mIsCollected_iv.setImageResource(); 收藏选项
        mNowTemperature_tv.setText(weather.now.temperation);
        mNowCondIcon_iv.setImageResource(getWeatherIconId(weather.now.condition.code));
        mNowCondTxt_tv.setText(weather.now.condition.info);
        mAirQuality_tv.setText(weather.aqi.city.quality);
        mMinTemp_tv.setText(weather.dailyForecastList.get(0).temp.minTemp);
        mMaxTemp_tv.setText(weather.dailyForecastList.get(0).temp.maxTemp);
        //未来一小时
        mNextHourTemp_tv.setText(weather.hourlyForecastList.get(0).temperature);
        mNextHourCode_iv.setImageResource(getWeatherIconId(weather.hourlyForecastList.get(0).cond.code));
        mNextHourTxt_tv.setText(weather.hourlyForecastList.get(0).cond.info);
        mNextHourWin_tv.setText(dealWinInfo(weather.hourlyForecastList.get(0).wind));
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


}
