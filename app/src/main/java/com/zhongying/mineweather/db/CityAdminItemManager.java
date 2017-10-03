package com.zhongying.mineweather.db;

import android.text.TextUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @class: 封装了对CityAdminItem的操作
 * Created by Administrator on 2017/9/30.
 */

public class CityAdminItemManager {

    private CityAdminItemManager mInstance;

    private CityAdminItemManager(){

    }

    //通过单例方式来获取唯一的 管理者
    public static CityAdminItemManager getInstance(){
        return Holder.manager;
    }

    private static class Holder{
        private static CityAdminItemManager manager =
                new CityAdminItemManager();
    }

    /**
        插入一个新的城市item
        @param city： 插入的目标item
        @return true表示成功插入
     */
    public boolean insert(CityAdminItem city){
        if(city == null){
            return false;
        }
        city.save();
        return true;
    }

    /**
     * 删除指定weatherId的城市item
     * @param cityWeatherId 目标城市item的weatherId
     * @return true表示成功删除
     */
    public boolean delete(String cityWeatherId){
        if(cityWeatherId == null || TextUtils.isEmpty(cityWeatherId)){
            return false;
        }
        DataSupport.deleteAll(CityAdminItem.class,"cityWeatherId = ?",cityWeatherId);

        return true;
    }

    /**
     * 获取所有的收藏的城市item
     * @return 所有城市item的 list表
     */
    public List<CityAdminItem> findAll(){
        return DataSupport.findAll(CityAdminItem.class);
    }

    /**
     * 获取指定 weatherId 的城市item
     * @param cityWeatherId 目标城市item 的weatherId
     * @return
     */
    public CityAdminItem find(String cityWeatherId){
        if(cityWeatherId == null||TextUtils.isEmpty(cityWeatherId)){
            return null;
        }
        List<CityAdminItem> list = DataSupport.
                where("cityWeatherId = ?",cityWeatherId).find(CityAdminItem.class);
        if(list.size() == 0){
            return null;
        }
        return list.get(0);
    }

    /**
     * 指定weatherId的城市item是否存在
     * @param cityWeatherId
     * @return true存在，false不存在
     */
    public boolean isHadByWeatherId(String cityWeatherId){
        CityAdminItem item = find(cityWeatherId);
        if(item == null){
            return false;
        }
        return true;
    }

    /**
     * 更新城市item的数据
     * @param city
     */
    public void update(CityAdminItem city){
        if(city == null){
            return;
        }
        city.save();
    }

}
