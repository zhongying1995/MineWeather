package com.zhongying.mineweather.areadata;

import android.text.TextUtils;

import com.zhongying.mineweather.areadata.base.AreasData;
import com.zhongying.mineweather.areadata.base.Callback;
import com.zhongying.mineweather.db.County;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class CountiesData extends AreasData {

    private int cityId;

    private Callback callback;

    /*
        构造函数
     */
    public CountiesData(Callback callback){
        super(callback);
        this.callback = callback;

    }

    private void setCityId(int cityId){
        this.cityId = cityId;
    }

    @Override
    public void setId(int id) {
        setCityId(id);
    }

    @Override
    protected List getDataListFromLite(Class<?> clzz) {
        if(!clzz.equals(County.class)){
            return null;
        }
        List<County> list = DataSupport.where("cityId = ?",String.valueOf(cityId)).find(County.class);

        return list!=null?list:null;
    }

    @Override
    protected boolean saveDataIntoLite(String response) {
        if(response==null|| TextUtils.isEmpty(response)){
            return false;
        }

        try {
            JSONArray allCounties = new JSONArray(response);
            for(int i=0;i<allCounties.length();i++){
                JSONObject county = allCounties.getJSONObject(i);
                County c = new County();
                c.setCountyName(county.getString("name"));
                c.setCityId(county.getInt("id"));
                c.setWeatherId(county.getInt("weather_id"));
                c.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }
}
