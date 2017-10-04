package com.zhongying.mineweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.activity.base.BaseActivity;
import com.zhongying.mineweather.adapter.CityAdminAdapter;
import com.zhongying.mineweather.adapter.IAdminCityItemListener;
import com.zhongying.mineweather.db.CityAdminItem;
import com.zhongying.mineweather.db.CityAdminItemManager;

import java.util.List;

/**
 * @class: 管理收藏城市的页面
 * Created by Administrator on 2017/9/20.
 */

public class AdminCountyActivity extends BaseActivity implements View.OnClickListener,
        IAdminCityItemListener {
    private String TAG = "AdminCountyActivity";

    public static final int REQUEST_CODE_FOR_WEATHER_ID = 1;

    private LinearLayout mCityAdminor_linear;
    private TextView mGoBack_tv;
    private ListView mCityList;
    private CityAdminAdapter mAdapter;
    List<CityAdminItem> mCityAdminItems;

    private boolean mHadDeletedCurrentCity = false;

    //天气主界面上，当前的城市的weatherId
    private String mCurrentWeatherId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_adminor_layout);

        initView();
        initData();
    }

    @Override
    public void onBackPressed() {
        setResultForDeleteCity();
        super.onBackPressed();
    }

    private void initView(){
        mCityAdminor_linear = (LinearLayout) findViewById(R.id.city_adminor_linear);
        mGoBack_tv = (TextView) findViewById(R.id.go_back_tv);
        mGoBack_tv.setOnClickListener(this);

        initListView();
    }

    //获取从主天气界面传进来的当前天气的weatherId
    private void initData(){
        Intent it = getIntent();
        mCurrentWeatherId = it.getStringExtra("weather_id");
    }

    //动态创建管理城市的ListView
    private void initListView(){
        //初始化adapter需展示的数据
        mCityAdminItems = CityAdminItemManager.getInstance().findAll();
        if(mCityAdminItems.size() == 0){
            return;
        }

        //有收藏的城市，请求创建ListView
        mCityList = new ListView(this);
        LinearLayout.LayoutParams lp = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mCityList.setLayoutParams(lp);
        mAdapter = new CityAdminAdapter(this,mCityAdminItems,this);
        mCityList.setAdapter(mAdapter);

        mCityAdminor_linear.addView(mCityList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_back_tv:
                setResultForDeleteCity();
                finish();
                break;
            case R.id.add_county_iv:
                //点击添加城市按钮

                break;
        }

    }

    //启动本Activity的方式
    public static void startAdminCountyActivity(Activity context,String weatherId){
        Intent it = new Intent(context,AdminCountyActivity.class);
        it.putExtra("weatherId",weatherId);
        context.startActivityForResult(it,REQUEST_CODE_FOR_WEATHER_ID);
    }

    //点击城市item的删除号的回调操作
    @Override
    public void deleteCity(String weatherId) {
        Log.i(TAG,"deleteCity()>>>>>>>>>>>>");
        CityAdminItemManager.getInstance().delete(weatherId);
        mCityAdminItems.clear();
        List<CityAdminItem> list = CityAdminItemManager.getInstance().findAll();
        if(list.size()!=0){
            for (CityAdminItem item: list){
                mCityAdminItems.add(item);
            }
        }
        Log.i(TAG,"mCityAdminItems.size():"+mCityAdminItems.size());
        mAdapter.notifyDataSetChanged();
        if(weatherId.equals(mCurrentWeatherId)){
            mHadDeletedCurrentCity = true;
            Log.i(TAG,"deleteCity() --- weatherId == mCurrentWeatherId");
            Log.i(TAG,"deleteCity() --- weatherId:"+weatherId);
            Log.i(TAG,"deleteCity() --- mCurrentWeatherId:"+mCurrentWeatherId);
        }
        Log.i(TAG,"<<<<<<<<<<<<<deleteCity()");
    }

    //用户点击城市块部分，跳转到被点击的城市，并在主天气界面展示
    @Override
    public void exchangeCityWeather(String weatherId) {
        Log.i(TAG,"exchangeCityWeather()>>>>>>>>>>>>>>");
        setResultForExchangeCity(weatherId);
        Log.i(TAG,"<<<<<<<<<<<<<<<<exchangeCityWeather()");
        finish();
    }

    //当用户点击加号时，跳转到选择城市列表，如果选择城市，则添加默认添加该城市
    @Override
    public void addCity() {
        setResultForAddCity();
        finish();
    }

    //当从管理城市界面点击切换城市到其他城市的回传
    private void setResultForExchangeCity(String weatherId){
        Intent it = new Intent();
        it.putExtra("target","exchange");
        it.putExtra("weather_id",weatherId);
        setResult(RESULT_OK,it);
    }

    //当从主天气界面跳转过来时，固定的回传
    private void setResultForDeleteCity(){
        Intent it =new Intent();
        it.putExtra("target","delete");
        it.putExtra("delete_city",mHadDeletedCurrentCity);
        setResult(RESULT_OK,it);
    }

    //当管理城市界面，点击加号，的固定回传
    private void setResultForAddCity(){
        Intent it =new Intent();
        it.putExtra("target","add");
        setResult(RESULT_OK,it);
    }

}
