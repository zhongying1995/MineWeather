package com.zhongying.mineweather.areadata;

import android.text.TextUtils;

import com.zhongying.mineweather.areadata.base.AreasData;
import com.zhongying.mineweather.areadata.base.Callback;
import com.zhongying.mineweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class ProvincesData extends AreasData {

    private Callback callback;

    /**
     * 构造函数
     */
    public ProvincesData(Callback callback){
        super(callback);
        this.callback = callback;
    }

    @Override
    public void setId(int id) {

    }

    /**
     * @function: 从本地数据库，获取省级的DataList
     * @param clzz
     * @return
     */
    @Override
    protected List getDataListFromLite(Class<?> clzz) {
        if(!clzz.equals(Province.class)){
            return null;
        }
        List<Province> provinces = DataSupport.findAll(Province.class);
        return provinces!=null?provinces:null;
    }

    /**
     * @function: 解析和处理省级数据
     * @param response 返回的数据
     * @return  true：成功返回
     */
    @Override
    protected boolean saveDataIntoLite(String response) {
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


}
