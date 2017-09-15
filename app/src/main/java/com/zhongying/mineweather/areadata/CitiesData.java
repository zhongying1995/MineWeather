package com.zhongying.mineweather.areadata;

import android.text.TextUtils;

import com.zhongying.mineweather.areadata.base.AreasData;
import com.zhongying.mineweather.db.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class CitiesData extends AreasData {

    int provinceId;

    Callback callback = new Callback() {
        @Override
        public void preCallback() {

        }

        @Override
        public void postCallback() {

        }

        @Override
        public void onFailure() {

        }

        @Override
        public void onSucceed() {

        }
    };

    //需要把当前省份的Id传进来
    public CitiesData(int provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    protected List getDataListFromLite(Class<?> clzz) {
        if(!clzz.equals(City.class)){
            return null;
        }
        List<City> cities = DataSupport.where("provinceId = ?",String.valueOf(provinceId)).find(City.class);
        return cities!=null?cities:null;
    }

    @Override
    protected boolean saveDataIntoLite(String response) {
        if(response==null|| TextUtils.isEmpty(response)){
            return false;
        }

        try {
            JSONArray cities = new JSONArray(response);
            for(int i=0;i<cities.length();i++){
                JSONObject city = cities.getJSONObject(i);
                City c = new City();
                c.setCityId(city.getInt("id"));
                c.setCityName(city.getString("name"));
                c.setProvinceId(provinceId);
                c.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }
}
