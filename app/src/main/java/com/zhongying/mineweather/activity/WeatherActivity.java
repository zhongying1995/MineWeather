package com.zhongying.mineweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.activity.base.BaseActivity;
import com.zhongying.mineweather.fragment.AreaFragment;
import com.zhongying.mineweather.fragment.WeatherFragment;

/**
 * @class: 管理天气的页面,天气界面由该layout下的fragmen展示
 * Created by Administrator on 2017/9/26.
 */

public class WeatherActivity extends BaseActivity {

    private DrawerLayout mDrawerlayout;

    private WeatherFragment mWeatherFragment;

    private AreaFragment mCityFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_layout);

        initView();

    }

    private void initView(){
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mWeatherFragment = (WeatherFragment) getSupportFragmentManager()
                .findFragmentById(R.id.weather_fragment);
        mCityFragment = (AreaFragment) getSupportFragmentManager()
                .findFragmentById(R.id.choose_area_fragment);


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
