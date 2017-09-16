package com.zhongying.mineweather.fragment;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.areadata.CitiesData;
import com.zhongying.mineweather.areadata.ProvincesData;
import com.zhongying.mineweather.areadata.base.AreasData;
import com.zhongying.mineweather.db.City;
import com.zhongying.mineweather.db.County;
import com.zhongying.mineweather.db.Province;
import com.zhongying.mineweather.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @class: 城市选择界面的Fragment
 * Created by Administrator on 2017/9/14.
 */

public class AreaFragment extends BaseFragment implements AdapterView.OnItemClickListener {

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
    private ProvincesData mProvincesData;
    //市级列表
    private List<City> mCityList;
    private AreasData mCitiesData;
    //县级列表
    private List<County> mCountyList;
    private AreasData mCountiesData;

    //
    private String mBaseAdress = "http://guolin.tech/api/china";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_choose_area_layout,container,false);
        initView( view);

        mAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,mDataList);
        area_lv.setAdapter(mAdapter);
        notifyDataListToProvince();
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void notifyDataListToProvince(){
        mDataList.clear();
        for (Province province: getProvinceList()) {
            mDataList.add(province.getProvinceName());
        }
        mAdapter.notifyDataSetChanged();
        mCurrentLevel = LEVEL_PROVINCE;
    }

    private void notifyDataListToCity(){
        mDataList.clear();
        for (City city: getCityList()) {
            mDataList.add(city.getCityName());
        }
        mAdapter.notifyDataSetChanged();
        mCurrentLevel = LEVEL_CITY;
    }

    private void notifyDataListToCounty(){
        mDataList.clear();
        for(County county:getCountyList()){
            mDataList.add(county.getCountyName());
        }
        mAdapter.notifyDataSetChanged();
        mCurrentLevel = LEVEL_COUNTY;
    }

    private List<Province> getProvinceList(){
        if(mProvincesData == null){
            mProvincesData = new ProvincesData();
        }
        mProvinceList = mProvincesData.getDataList(Province.class,mBaseAdress,mProvincesData.callback);

        return mProvinceList;
    }

    private List<City> getCityList(){
        if(mCitiesData == null){
            mCitiesData = new CitiesData(0);
        }
       // mCityList = mCitiesData.getDataList(City.class,mBaseAdress,mCitiesData.callback);

        return mCityList;
    }

    private List<County> getCountyList(){
        if(mCountiesData == null){
            mCountiesData = new CitiesData(0);
        }
       // mCountyList = mCountiesData.getDataList(County.class,mBaseAdress,mCountiesData.callback);

        return mCountyList;
    }
}
