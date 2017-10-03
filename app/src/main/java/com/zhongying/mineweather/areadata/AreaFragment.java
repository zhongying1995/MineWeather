package com.zhongying.mineweather.areadata;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.areadata.base.AreasData;
import com.zhongying.mineweather.areadata.base.Callback;
import com.zhongying.mineweather.db.City;
import com.zhongying.mineweather.db.County;
import com.zhongying.mineweather.db.Province;
import com.zhongying.mineweather.fragment.base.BaseFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @class: 城市选择界面的Fragment
 *      由于没办法选取出一种能够解决问题的方法，故放弃该设计模式
 * Created by Administrator on 2017/9/14.
 */

public class AreaFragment extends BaseFragment implements AdapterView.OnItemClickListener,View.OnClickListener {

    private String TAG = "ChooseAreaFragment";

    private Context mContext ;

    private final static int LEVEL_PROVINCE = 0;
    private final static int LEVEL_CITY = 1;
    private final static int LEVEL_COUNTY = 2;
    private int mCurrentLevel;

    private ProgressDialog mDialog;//显示加载中的Dialog

    private TextView title_tv;//显示当前的标题

    private ImageView back_iv;//非省级下时的后退按钮

    private ListView area_lv;//展示内容的ListView

    private ArrayAdapter<String> mAdapter;

    private List<String> mDataList = new ArrayList<>();

    //省级列表
    private List<Province> mProvinceList;
    private AreasData mProvincesData;
    private Province mCurrentProvince;
    private int mCurrentProvinceId;

    //市级列表
    private List<City> mCityList;
    private AreasData mCitiesData;
    private City mCurrentCity;
    private int mCurrentCityId;
    //县级列表
    private List<County> mCountyList;
    private AreasData mCountiesData;
    private County mCurrentCounty;
    private int mCurrentCountyId;

    //基础URl
    private String mBaseAdress = "http://guolin.tech/api/china";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_area_layout,container,false);
        initView( view);

        mAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,mDataList);
        area_lv.setAdapter(mAdapter);
        notifyDataListToProvince();
        mCurrentLevel = LEVEL_PROVINCE;
        return view;
    }

    private void initView(View view){
        title_tv = (TextView) view.findViewById(R.id.area_title_tv);
        back_iv = (ImageView) view.findViewById(R.id.area_back_btn);
        area_lv = (ListView) view.findViewById(R.id.chosen_city_lv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        area_lv.setOnItemClickListener(this);
        back_iv.setOnClickListener(this);
        back_iv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.w(TAG,"position:"+position+"\nid:"+id);
        switch (mCurrentLevel){
            case LEVEL_PROVINCE:
                mCurrentProvince = mProvinceList.get(position);
                mCurrentProvinceId = mCurrentProvince.getProvinceId();

                Log.w(TAG,"mCurrentProvinceId:"+mCurrentProvinceId);

                String address = mBaseAdress + "/"+mCurrentProvinceId;
                Log.w(TAG,"address: "+address);
                notifyDataListToCity(mCurrentProvinceId,address);
                back_iv.setVisibility(View.VISIBLE);
                mCurrentLevel = LEVEL_CITY;
                break;
            case LEVEL_CITY:
                mCurrentCity =  mCityList.get(position);
                mCurrentCityId = mCurrentCity.getCityId();

                Log.w(TAG,"mCurrentCityId:"+mCurrentCityId);

                String address1 = mBaseAdress +"/"+mCurrentProvinceId+"/"+mCurrentCityId;
                Log.w(TAG,"address: "+address1);

                notifyDataListToCounty(mCurrentCityId,address1);
                mCurrentLevel = LEVEL_COUNTY;
                break;
            case LEVEL_COUNTY:

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (mCurrentLevel){
            case LEVEL_CITY:
                back_iv.setVisibility(View.INVISIBLE);
                notifyDataListToProvince();
                mCurrentLevel = LEVEL_PROVINCE;
                break;
            case LEVEL_COUNTY:
                String address = mBaseAdress + "/"+mCurrentProvinceId;
                notifyDataListToCity(mCurrentProvinceId,address);
                mCurrentLevel = LEVEL_CITY;
                break;
        }
    }


    //刷新当前的数据为省级数据
    private void notifyDataListToProvince(){
        Log.i(TAG,"notifyDataListToProvince()");
        mDataList.clear();
        mProvinceList = getProvinceList();
        for (Province province: mProvinceList) {
            mDataList.add(province.getProvinceName());
            Log.i(TAG,province.getProvinceName());
        }
        if(mDataList.size()<=0){
            Log.i(TAG,"notifyDataListToProvince() --  size<=0");
        }
        mAdapter.notifyDataSetChanged();
        area_lv.setSelection(0);
    }

    //刷新当前的数据为市级数据
    private void notifyDataListToCity(int provinceId,String address){
        Log.i(TAG,"notifyDataListToCity()");
        mDataList.clear();
        mCityList = getCityList(provinceId,address);
        for (City city: mCityList) {
            mDataList.add(city.getCityName());
            Log.i(TAG,city.getCityName());
        }
        mAdapter.notifyDataSetChanged();
        area_lv.setSelection(0);
    }

    //刷新当前的数据为县级数据
    private void notifyDataListToCounty(int cityId,String address){
        Log.i(TAG,"notifyDataListToCounty()");
        mDataList.clear();
        mCountyList = getCountyList(cityId,address);
        for(County county:mCountyList){
            mDataList.add(county.getCountyName());
            Log.i(TAG,county.getCountyName());
        }
        mAdapter.notifyDataSetChanged();
        area_lv.setSelection(0);
    }

    private List<Province> getProvinceList(){
        Log.i(TAG,"getProvinceList()");
        if(mProvincesData == null){
            mProvincesData = new ProvincesData(new Callback() {
                @Override
                public void onFailure(Call call) {

                }

                @Override
                public void onSucceed(Call call, Response response) throws IOException {

                }

                @Override
                public void onPreResponse() {

                }

                @Override
                public void onPostResponse() {

                }
            });
        }
        mProvinceList = mProvincesData.getDataList(Province.class,mBaseAdress);
        if(mProvincesData == null){
            Log.i(TAG,"getProvinceList()  --  mProvincesData == null");
        }
        if(mProvinceList.size()<=0){
            Log.i(TAG,"getProvinceList()  --  size<=0");
        }
        return mProvinceList;
    }

    private List<City> getCityList(int provinceId,String address){
        Log.i(TAG,"getCityList()");
        if(mCitiesData == null){
            mCitiesData = new CitiesData(new Callback() {
                @Override
                public void onFailure(Call call) {

                }

                @Override
                public void onSucceed(Call call, Response response) throws IOException {

                }

                @Override
                public void onPreResponse() {

                }

                @Override
                public void onPostResponse() {

                }
            });
        }
        mCitiesData.setId(provinceId);
        mCityList = mCitiesData.getDataList(City.class,address);

        return mCityList;
    }

    private List<County> getCountyList(int cityId,String address){
        Log.i(TAG,"getCountyList()");
        if(mCountiesData == null){
            mCountiesData = new CountiesData(new Callback() {
                @Override
                public void onFailure(Call call) {

                }

                @Override
                public void onSucceed(Call call, Response response) throws IOException {

                }

                @Override
                public void onPreResponse() {

                }

                @Override
                public void onPostResponse() {

                }
            });
        }
        mCountiesData.setId(cityId);
        Log.i(TAG,"getCityList() -- "+cityId);
        mCountyList = mCountiesData.getDataList(County.class,address);

        return mCountyList;
    }

}
