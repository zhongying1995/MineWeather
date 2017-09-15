package com.zhongying.mineweather.response.area;

import android.text.TextUtils;

import com.zhongying.mineweather.db.City;
import com.zhongying.mineweather.db.County;
import com.zhongying.mineweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @class: 实现 解析服务器返回的数据，并保存于本地的数据库中
 * Created by Administrator on 2017/9/15.
 */

public class ResolveArea{
    /**
     * @function: 解析和处理省级数据
     * @param response 返回的数据
     * @return  true：成功返回
     */
    public static boolean handleProvinceResponse(String response){
        if(response==null || TextUtils.isEmpty(response)){
            return false;
        }

        try {
            JSONArray allProvinces = new JSONArray(response);
            for(int i=0;i<allProvinces.length();i++){
                JSONObject province = allProvinces.getJSONObject(i);
                Province p = new Province();
                p.setProvinceName(province.getString("name"));
                p.setProvinceId(province.getInt("id"));
                p.save();
            }
            return true;
        }catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }


    public static boolean handleCityResponse(String response,int provinceId){
        if(response==null || TextUtils.isEmpty(response)||provinceId<=0){
            return false;
        }

        try {
            JSONArray cities = new JSONArray(response);
            for(int i=0;i<cities.length();i++){
                JSONObject city = cities.getJSONObject(i);
                City c = new City();
                c.setProvinceId(provinceId);
                c.setCityName(city.getString("name"));
                c.setCityId(city.getInt("id"));
                c.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean handleCountyResponse(String response,int cityId){
        if(response ==null || TextUtils.isEmpty(response) || cityId<=34){
            return false;
        }

        try{
            JSONArray counties = new JSONArray(response);
            for(int i=0;i<counties.length();i++){
                JSONObject county = counties.getJSONObject(i);
                County c = new County();
                c.setCountyName(county.getString("name"));
                c.setWeatherId(county.getInt("id"));
                c.setCityId(cityId);
            }
            return true;
        }catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


    }

}
