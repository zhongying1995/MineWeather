package com.zhongying.mineweather.areadata;

import android.text.TextUtils;
import android.util.Log;

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
    private String TAG = "ChooseAreaFragment";

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
        if(list.size()<=0){
            Log.i(TAG,"County - getDataListFromLite() -- size<=0");
        }
        return list!=null?list:null;
    }

    @Override
    protected boolean saveDataIntoLite(String response) {
        Log.i(TAG,"saveDataIntoLite(String response)");
        if(response==null|| TextUtils.isEmpty(response)){
            Log.i(TAG,"false");
            return false;
        }
        try {
            JSONArray allCounties = new JSONArray(response);
            for(int i=0;i<allCounties.length();i++){
                JSONObject county = allCounties.getJSONObject(i);
                County c = new County();
                c.setCountyName(county.getString("name"));
                c.setCityId(cityId);
                c.setWeatherId(county.getString("weather_id"));
                Log.i(TAG,"cityId: "+cityId);
                c.save();
            }
            if(allCounties.length()==0){
                Log.i(TAG,"allCounties.length()==0");
            }else {
                Log.i(TAG,"allCounties.length()!=0");
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG,"JSONException ！！！");
            return false;
        }

    }
}
