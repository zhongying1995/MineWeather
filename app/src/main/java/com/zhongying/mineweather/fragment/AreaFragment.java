package com.zhongying.mineweather.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhongying.mineweather.R;
import com.zhongying.mineweather.activity.MainActivity;
import com.zhongying.mineweather.activity.WeatherActivity;
import com.zhongying.mineweather.constant.Constant;
import com.zhongying.mineweather.db.City;
import com.zhongying.mineweather.db.County;
import com.zhongying.mineweather.db.Province;
import com.zhongying.mineweather.db.SharedPreferencesManager;
import com.zhongying.mineweather.fragment.base.BaseFragment;
import com.zhongying.mineweather.okhttp.HttpCallback;
import com.zhongying.mineweather.okhttp.HttpUtil;
import com.zhongying.mineweather.response.area.ResolveArea;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @class: 按照书上的方式写吧，别搞什么设计模式了，那只会玩死自己
 * Created by Administrator on 2017/9/17.
 */

public class AreaFragment extends BaseFragment implements View.OnClickListener,ListView.OnItemClickListener {

    private String TAG = "AreaFragment";

    //背景图片
    private ImageView mBgPicture_iv;

    private TextView title_tv;

    private ImageView back_iv;

    private ListView area_lv;

    private ProgressDialog dialog;

    private final static int LEVEL_PROVINCE = 0;

    private final static int LEVEL_CITY = 1;

    private final static int LEVEL_COUNTY = 2;

    private int mCurrentLevel = 0;

    private Activity mActivity;

    private ArrayAdapter<String > adapter;

    private List<String> mDataList = new ArrayList<>();

    //当前点击的省、市的 Id
    private Province mCurrentProvince;
    private City mCurrentCity;

    //当前使用的省、市的 List
    private List<Province> mChosenProvinceList;
    private List<City> mChosenCityList;
    private List<County> mChosenCountyList;

    //请求网络数据的基础链接
    private final static String BASE_URL = "http://guolin.tech/api/china";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mActivity == null){
            this.mActivity = getActivity();
        }

        View view = inflater.inflate( R.layout.fragment_choose_area_layout,container,false);
        initView(view);
        initAdapter();
        /*暂时不使用背景图
            initBgPicture();
        */
        return view;
    }

    //初始化控件
    private void initView(View view){
        mBgPicture_iv = (ImageView) view.findViewById(R.id.background_picture_iv);

        this.title_tv = (TextView) view.findViewById(R.id.area_title_tv);
        this.back_iv = (ImageView) view.findViewById(R.id.area_back_btn);
        this.back_iv.setOnClickListener(this);
        back_iv.setVisibility(View.INVISIBLE);
        this.area_lv = (ListView) view.findViewById(R.id.chosen_city_lv);
    }

    //初始化listview的adapter
    private void initAdapter(){
        adapter = new ArrayAdapter<String >(mActivity,android.R.layout.simple_list_item_1,mDataList);
        area_lv.setAdapter(adapter);
        area_lv.setOnItemClickListener(this);
        queryProvincesData();
    }

    //初始化背景图片
    private void initBgPicture(){

        String picture = SharedPreferencesManager
                .getInstance().getString(Constant.SHARED_KEY_BACKGROUND);
        if(picture == null){
            requestBgPituce();
            return;
        }
        showBgPicture(picture);
    }

    @Override
    public void onClick(View v) {
        switch (mCurrentLevel){
            case LEVEL_COUNTY:
                queryCitysData();
                break;
            case LEVEL_CITY:
                back_iv.setVisibility(View.INVISIBLE);
                queryProvincesData();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (mCurrentLevel){
            case LEVEL_PROVINCE:
                back_iv.setVisibility(View.VISIBLE);
                mCurrentProvince = mChosenProvinceList.get(position);
                queryCitysData();
                break;
            case LEVEL_CITY:
                mCurrentCity = mChosenCityList.get(position);
                queryCountyData();
                break;
            case LEVEL_COUNTY:
                String weatherId = mChosenCountyList.get(position).getWeatherId();

                //保存当前的weatherId
                SharedPreferencesManager.getInstance().putString(Constant.SHARED_KEY_WEATHERID,weatherId);

                if(mActivity instanceof MainActivity){
                    Intent it = new Intent(mActivity, WeatherActivity.class);
                    it.putExtra("weather_id",weatherId);
                    startActivity(it);
                    mActivity.finish();
                }else if(mActivity instanceof WeatherActivity){
                    WeatherActivity activity = (WeatherActivity) mActivity;
                    activity.closeDrawer();
                    activity.openRefresh();
                    activity.requestWeather(weatherId);
                }

                break;
        }
    }

    

    //从本地litepal库获取省级的数据
    private void queryProvincesData(){
        mChosenProvinceList =DataSupport.findAll(Province.class);
        //先尝试本地请求
        if(mChosenProvinceList.size()>0){
            mDataList.clear();
            for (Province province:mChosenProvinceList){
                mDataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            area_lv.setSelection(0);
            mCurrentLevel = LEVEL_PROVINCE;
        }else {
            //本地请求失败，进行网络请求
            queryDataFormServer(BASE_URL,LEVEL_PROVINCE);
        }
    }

    //从本地litepal库获取市级的数据
    private void queryCitysData(){
        mChosenCityList = DataSupport.
                where("provinceid = ?",String.valueOf(mCurrentProvince.getProvinceId())).find(City.class);
        if(mChosenCityList.size()>0){
            notifyTitle(mCurrentProvince.getProvinceName());
            mDataList.clear();
            for(City city : mChosenCityList){
                mDataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            area_lv.setSelection(0);
            mCurrentLevel = LEVEL_CITY;
        }else {
            //请求网络访问
            String address = BASE_URL +"/"+ mCurrentProvince.getProvinceId();
            queryDataFormServer(address,LEVEL_CITY);
        }

    }

    //从本地litepal库获取县级的数据
    private void queryCountyData(){
        mChosenCountyList = DataSupport.
                where("cityId = ?",String .valueOf(mCurrentCity.getCityId())).find(County.class);

        if(mChosenCountyList.size()>0){
            notifyTitle(mCurrentCity.getCityName());
            mDataList.clear();
            for (County county: mChosenCountyList){
                mDataList.add(county.getCountyName());

            }
            adapter.notifyDataSetChanged();
            area_lv.setSelection(0);
            mCurrentLevel = LEVEL_COUNTY;
        }else {
            String address = BASE_URL + "/" +
                    mCurrentProvince.getProvinceId()+"/"+mCurrentCity.getCityId();

            queryDataFormServer(address,LEVEL_COUNTY);
        }
    }

    //向网络请求数据
    private void queryDataFormServer(String address, final int requestType){
        showDialog();
        HttpUtil.requestOkHttpUrl(address, new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeDialog();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                boolean isSaved = false;
                //调用函数解析函数，并把数据保存到本地的数据库
                String text = response.body().string();
                switch (requestType){
                    case LEVEL_PROVINCE:
                        isSaved = ResolveArea.
                                handleProvinceResponse(text);
                        break;
                    case LEVEL_CITY:
                        isSaved = ResolveArea.
                                handleCityResponse(text, mCurrentProvince.getProvinceId());
                        break;
                    case LEVEL_COUNTY:
                        isSaved = ResolveArea.handleCountyResponse(text,mCurrentCity.getCityId());
                        break;
                }

                //数据正常保存，回到UI线程，请求重新从本地获取数据
                if(isSaved){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (requestType){
                                case LEVEL_PROVINCE:
                                    queryProvincesData();
                                    break;
                                case LEVEL_CITY:
                                    queryCitysData();
                                    break;
                                case LEVEL_COUNTY:
                                    queryCountyData();
                                    break;
                            }
                            closeDialog();
                        }
                    });
                }
            }
        });

    }

    //展示一个等待窗口
    private void showDialog(){
        if(dialog == null){
            dialog = new ProgressDialog(mActivity);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading...");
        }
        dialog.show();
    }

    //关闭等待的窗口
    private void closeDialog(){
        if(dialog !=null){
            dialog.dismiss();
        }

    }

    private void notifyTitle(String title){
        title_tv.setText(title);
    }


    //向网络请求背景图
    private void requestBgPituce(){
        String url = Constant.BACKGROUND_PICTURE_URL;
        HttpUtil.requestOkHttpUrl(url, new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity,
                                "请求背景图失败....",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String picture = response.body().string();
                SharedPreferencesManager .getInstance()
                        .putString(Constant.SHARED_KEY_BACKGROUND,picture);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showBgPicture(picture);
                    }
                });
            }
        });
    }

    //设置背景图
    private void showBgPicture(String picture){
        Glide.with(mActivity).load(picture).into(mBgPicture_iv);
    }


}
