package com.zhongying.mineweather.activity;

import android.os.Bundle;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.activity.base.BaseActivity;

/**
 * @class: 最主要的天气展示页面
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
