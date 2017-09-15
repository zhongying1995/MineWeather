package com.zhongying.mineweather.areadata.base;

import com.zhongying.mineweather.okhttp.HttpCallback;
import com.zhongying.mineweather.okhttp.HttpUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/15.
 */

public abstract class AreasData {

    public Callback mCallback ;

    /**
     * @function: 用固定的流程来控制返回当前级别的数据
     *          从本地或者服务器
     * @return 返回所需要的当前级别的数据
     */
    public final List getDataList(Class<?> clzz,String adress,Callback callback){

        //尝试从本地获取数据
        List list = getDataListFromLite(clzz);

        //先向网络发起请求，请求成功后，解析数据并保存到本地，再从本地获取数据
        if(list == null || list.size()<=0){
            queryFromServer(adress,callback);
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
     * @function: 保存网络查询的数据到本地
     * @return
     */
    protected abstract boolean saveDataIntoLite(String response);

    /**
     * @function： 向网络提前数据请求，并返回数据后，调用保存数据的方法
     * @param address
     */
    private void queryFromServer(String address,Callback callback){
        callback.preCallback();
        HttpUtil.requestOkHttpUrl(address, callback);
        callback.postCallback();
    }

    /**
     * 回调函数
     */
    public abstract class Callback extends HttpCallback{

        @Override
        public  void onFailure(Call call, IOException e){
            onFailure();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseText = response.body().string();
            if(!saveDataIntoLite(responseText)){
                onFailure();
            }else {
                onSucceed();
            }
        }

        public abstract void preCallback();

        public abstract void postCallback();

        public abstract void onFailure();

        public abstract void onSucceed();
    }
}
