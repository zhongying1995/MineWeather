package com.zhongying.mineweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.activity.base.BaseActivity;
import com.zhongying.mineweather.fragment.ChooseAreaFragment;
import com.zhongying.mineweather.fragment.WeatherFragment;

/**
 * @class: 管理天气的页面,天气界面由该layout下的fragmen展示
 * Created by Administrator on 2017/9/26.
 */

public class WeatherActivity extends BaseActivity {
    private String TAG = "WeatherActivity";

    private DrawerLayout mDrawerlayout;

    private WeatherFragment mWeatherFragment;

    private ChooseAreaFragment mCityFragment;

    //当前界面上展示的城市的weatherId
    private String mCurrentWeatherId;

    //在选择城市界面点击城市后，请求收藏城市
    private boolean requestCollectCity;

    public boolean getRequestCollectCity() {
        return requestCollectCity;
    }
    public void setRequestCollectCity(boolean requestCollectCity) {
        this.requestCollectCity = requestCollectCity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG,"onCreate()>>>>>>>>>>>>>>>>");
        setContentView(R.layout.activity_weather_layout);
        initView();
        Log.w(TAG,"<<<<<<<<<<<<<<<<<onCreate()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume()>>>>>>>>>>>>>>>");
        mCurrentWeatherId = mWeatherFragment.getWeatherId();
        Log.i(TAG,"onResume() --- mCurrentWeatherId:"+mCurrentWeatherId);
        Log.i(TAG,"<<<<<<<<<<<<<<<onResume()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult()>>>>>>>>>>>>>>>>");
        switch (requestCode){
            case AdminCountyActivity.REQUEST_CODE_FOR_WEATHER_ID:
                if(resultCode == RESULT_OK){
                    Log.i(TAG,"resultCode == RESULT_OK");
                    String target = data.getStringExtra("target");
                    Log.i(TAG,"target:"+target);
                    if("exchange".equals(target)){
                        Log.i(TAG,"\"exchange\".equals(target)");
                        String weatherId = data.getStringExtra("weather_id");
                        requestWeather(weatherId);

                    }else if("delete".equals(target)){
                        Log.i(TAG,"\"delete\".equals(target)");
                        boolean deleteCity = data.getBooleanExtra("delete_city",false);
                        if(deleteCity){
                            //当前的城市被用户在管理界面取消收藏
                            Log.i(TAG,"reverseCollectCity()");
                            mWeatherFragment.reverseCollectCity();
                        }
                    }else if("add".equals(target)){
                        Log.i(TAG,"\"add\".equals(target)");
                        setRequestCollectCity(true);
                        openDrawer();
                    }

                }
                break;
            default:
                Log.i(TAG,"default:");

                break;
        }

        Log.i(TAG,"<<<<<<<<<<<<<<<<<<onActivityResult()");
    }

    public void startAdminCountyActivity(){
        Log.i(TAG,"startAdminCountyActivity()>>>>>>>>>>>>>>>>>");
        Log.i(TAG,"mCurrentWeatherId:"+mCurrentWeatherId);
        AdminCountyActivity.startAdminCountyActivity(WeatherActivity.this,mCurrentWeatherId);
        Log.i(TAG,"<<<<<<<<<<<<<<<<<startAdminCountyActivity()");
    }

    private void initView(){
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mWeatherFragment = (WeatherFragment) getSupportFragmentManager()
                .findFragmentById(R.id.weather_fragment);
        mCityFragment = (ChooseAreaFragment) getSupportFragmentManager()
                .findFragmentById(R.id.choose_area_fragment);
    }

    @Override
    public void onBackPressed() {
        if(!mDrawerlayout.isDrawerOpen(GravityCompat.START)){
            super.onBackPressed();
        }else {
            if(!mCityFragment.onBackPressed()){
                closeDrawer();
            }
        }

    }

    //打开侧拉
    public void openDrawer(){
        mDrawerlayout.openDrawer(GravityCompat.START);
    }
    //关闭侧拉
    public void closeDrawer(){
        mDrawerlayout.closeDrawers();
    }

    //打开下拉刷新
    public void openRefresh(){
        mWeatherFragment.openRefresh();
    }

    //关闭下拉刷新
    public void closeRefresh(){
        mWeatherFragment.closeRefresh();
    }

    public void requestWeather(String weatherId){
        mWeatherFragment.requestWeather(weatherId);
    }



}
