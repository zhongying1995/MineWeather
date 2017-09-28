package com.zhongying.mineweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.activity.base.BaseActivity;

/**
 * @class: 管理天气的页面,天气界面由该layout下的fragmen展示
 * Created by Administrator on 2017/9/26.
 */

public class WeatherActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_layout);

    }



}
