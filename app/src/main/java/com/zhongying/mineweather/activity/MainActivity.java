package com.zhongying.mineweather.activity;

import android.content.Intent;
import android.os.Bundle;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.activity.base.BaseActivity;
import com.zhongying.mineweather.db.SharedPreferencesManager;

/**
 * @class: 第一次进入软件时的页面
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String weatherText = SharedPreferencesManager.getInstance().getString("weather");

        //若不是第一次启动，则直接前往天气界面
        if(weatherText !=null){
            Intent it = new Intent(this,WeatherActivity.class);
            startActivity(it);
            finish();
        }
    }
}
