package com.zhongying.mineweather.utily;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zhongying.mineweather.R;
import com.zhongying.mineweather.constant.Constant;
import com.zhongying.mineweather.gson.HeWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @class: 工具类
 * Created by Administrator on 2017/9/25.
 */

public class Utilies {

    private static Map<String,Integer> mIcons;

    /**
     * 构造函数，初始化某些数据
     */
    public Utilies(){

    }

    /**
     * @function: 解析返回的天气数据
     * @param response 返回的reponse的string
     * @return 返回天气类 HeWeather
     */
    public static HeWeather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            Log.w("importent",weatherContent);
            return new Gson().fromJson(weatherContent,HeWeather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @function: 组合成一个合法的url请求
     * @param： 对应的weatherId
     * @return： 正确的url地址
     * https://free-api.heweather.com/v5/weather?city=yourcity&key=yourkey
     */
    public static String getWeatherUrl(String weatherId){
        return "https://free-api.heweather.com/v5/weather?city="
                +weatherId+"&key="+ Constant.KEY_FOR_HEWEATHER;
    }


    /**
     * @function: 获取天气图标对应的图标id
     * @param code 天气图标的 代码  eg： 100
     * @return 返回该图标的id
     */
    public static int getWeatherIconId(String code){
        if(code==null || TextUtils.isEmpty(code)){
            return -1;
        }
        if(mIcons == null || mIcons.size()==0){
            mIcons = new HashMap<>();
            mIcons.clear();
            initWeatherIconsId();
        }
        return mIcons.get(code);
    }

    private static void initWeatherIconsId(){
        mIcons.put("100", R.mipmap.icon100);
        mIcons.put("101", R.mipmap.icon101);
        mIcons.put("102", R.mipmap.icon102);
        mIcons.put("103", R.mipmap.icon103);
        mIcons.put("104", R.mipmap.icon104);
        mIcons.put("200", R.mipmap.icon200);
        mIcons.put("201", R.mipmap.icon201);
        mIcons.put("202", R.mipmap.icon202);
        mIcons.put("203", R.mipmap.icon203);
        mIcons.put("204", R.mipmap.icon204);
        mIcons.put("205", R.mipmap.icon205);
        mIcons.put("206", R.mipmap.icon206);
        mIcons.put("207", R.mipmap.icon207);
        mIcons.put("208", R.mipmap.icon208);
        mIcons.put("209", R.mipmap.icon209);
        mIcons.put("210", R.mipmap.icon210);
        mIcons.put("211", R.mipmap.icon211);
        mIcons.put("212", R.mipmap.icon212);
        mIcons.put("213", R.mipmap.icon213);
        mIcons.put("300", R.mipmap.icon300);
        mIcons.put("301", R.mipmap.icon301);
        mIcons.put("302", R.mipmap.icon302);
        mIcons.put("303", R.mipmap.icon303);
        mIcons.put("304", R.mipmap.icon304);
        mIcons.put("305", R.mipmap.icon305);
        mIcons.put("306", R.mipmap.icon306);
        mIcons.put("307", R.mipmap.icon307);
        mIcons.put("308", R.mipmap.icon308);
        mIcons.put("309", R.mipmap.icon309);
        mIcons.put("310", R.mipmap.icon310);
        mIcons.put("311", R.mipmap.icon311);
        mIcons.put("312", R.mipmap.icon312);
        mIcons.put("313", R.mipmap.icon313);
        mIcons.put("400", R.mipmap.icon400);
        mIcons.put("401", R.mipmap.icon401);
        mIcons.put("402", R.mipmap.icon402);
        mIcons.put("403", R.mipmap.icon403);
        mIcons.put("404", R.mipmap.icon404);
        mIcons.put("405", R.mipmap.icon405);
        mIcons.put("406", R.mipmap.icon406);
        mIcons.put("407", R.mipmap.icon407);
        mIcons.put("500", R.mipmap.icon500);
        mIcons.put("501", R.mipmap.icon501);
        mIcons.put("502", R.mipmap.icon502);
        mIcons.put("503", R.mipmap.icon503);
        mIcons.put("504", R.mipmap.icon504);
        mIcons.put("507", R.mipmap.icon507);
        mIcons.put("508", R.mipmap.icon508);
        mIcons.put("900", R.mipmap.icon900);
        mIcons.put("901", R.mipmap.icon901);
        mIcons.put("999", R.mipmap.icon999);
    }

}
