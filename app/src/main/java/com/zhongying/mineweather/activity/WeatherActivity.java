package com.zhongying.mineweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhongying.mineweather.R;
import com.zhongying.mineweather.activity.base.BaseActivity;
import com.zhongying.mineweather.constant.Constant;
import com.zhongying.mineweather.db.SharedPreferencesManager;
import com.zhongying.mineweather.okhttp.HttpCallback;
import com.zhongying.mineweather.okhttp.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @class: 管理天气的页面,天气界面由该layout下的fragmen展示
 * Created by Administrator on 2017/9/26.
 */

public class WeatherActivity extends BaseActivity {

    private ImageView mBackgroundPicture_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_layout);

        initView();
        initBgPicture();
    }

    private void initView(){
        mBackgroundPicture_iv = (ImageView) findViewById(R.id.background_picture_iv);
    }

    private void initBgPicture(){
        String picture = SharedPreferencesManager.getInstance()
                .getString(Constant.SHARED_KEY_BACKGROUND);

        if(picture ==null){
            //无背景图，请求网络访问
            loadBgPicture();
            return;
        }
        //直接本地获取
        showBgPicture(picture);
    }
    
    /**
     * 从网络获取 微软必应的每日一图
     */
    private void loadBgPicture(){
        String url = Constant.BACKGROUND_PICTURE_URL;
        HttpUtil.requestOkHttpUrl(url, new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,
                                "获取最新背景图失败.....",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String picture = response.body().string();
                SharedPreferencesManager.getInstance().putString(Constant.SHARED_KEY_BACKGROUND,picture);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showBgPicture(picture);
                    }
                });
            }
        });
    }

    private void showBgPicture(String picture){
        Glide.with(WeatherActivity.this).load(picture).into(mBackgroundPicture_iv);
    }

}
