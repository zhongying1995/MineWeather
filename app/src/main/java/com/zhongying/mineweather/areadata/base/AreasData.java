package com.zhongying.mineweather.areadata.base;

import android.text.TextUtils;

import com.zhongying.mineweather.okhttp.HttpUtil;

import java.util.List;



/**
 * @class: 控制所有地区级别行为的父类
 * Created by Administrator on 2017/9/15.
 */

public abstract class AreasData {

    private Callback callback;

    /**
     * 构造函数
     */
    public AreasData(Callback callback){
        this.callback = callback;
    }

    public abstract void setId(int id);

    /**
     * @function: 用固定的流程来控制返回当前级别的数据
     *          从本地或者服务器
     * @return 返回所需要的当前级别的数据
     */
    public final List getDataList(Class<?> clzz,String adress){
        if(clzz==null||adress==null|| TextUtils.isEmpty(adress)||callback==null){
            return null;
        }
        //尝试从本地获取数据
        List list = getDataListFromLite(clzz);

        //先向网络发起请求，请求成功后，解析数据并保存到本地，再从本地获取数据
        if(list == null || list.size()<=0){
            callback.onPreResponse();
            queryFromServer(adress);
            saveDataIntoLite(callback.getResopnseText());
            callback.onPostResponse();
            list = getDataListFromLite(clzz);
        }

        return list!=null?list:null;
    }

    /**
     * @function: 从本地获取当前级别的数据
     * @return
     */
    protected abstract List getDataListFromLite(Class<?> clzz);

    /**
     * @function: 保存网络查询的数据到本地,需要在 onSucceed方法中调用
     * @return true：成功保存
     */
    protected abstract boolean saveDataIntoLite(String response);

    /**
     * @function： 向网络提前数据请求，并返回数据后，调用保存数据的方法
     * @param address
     */
    private void queryFromServer(String address){
        HttpUtil.requestOkHttpUrl(address,callback);
    }

}
