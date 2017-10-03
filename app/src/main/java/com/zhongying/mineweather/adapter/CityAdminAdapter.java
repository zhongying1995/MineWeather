package com.zhongying.mineweather.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.db.CityAdminItem;
import com.zhongying.mineweather.utily.Utilies;

import java.util.List;

/**
 * @class: 管理收藏城市界面下的ListView的 adapter
 * Created by Administrator on 2017/9/30.
 */

public class CityAdminAdapter extends BaseAdapter {

    private final static String TAG = "CityAdminAdapter";

    private Context mContext;

    private List<CityAdminItem> mCityList;

    private IDeleteCityListener mDeleteCityListener;

    //item样式的最大种类
    private static final int VIEW_TYPE_COUNT = 2;

    //通用item样式
    private static final int NORMAL_TYPE_ITEM = 0;
    //添加item样式
    private static final int ADD_TYPE_ITEM = 1;

    public CityAdminAdapter(Context context,List<CityAdminItem> cityList,IDeleteCityListener deleteCityListener){
        this.mContext = context;
        this.mCityList = cityList;
        this.mDeleteCityListener = deleteCityListener;
    }


    @Override
    public int getCount() {
        return mCityList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        Log.i(TAG,"getItem()>>>>>>>>>>");
        Log.i(TAG,"position:"+position);
        Log.i(TAG,"<<<<<<<<<<getItem()");
        return mCityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG,"getItem()>>>>>>>>>>");
        Log.i(TAG,"getItemId:"+position);
        Log.i(TAG,"<<<<<<<<<<getItem()");
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position==mCityList.size()?ADD_TYPE_ITEM:NORMAL_TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG,"getView()>>>>>>>>>>>>>>>");
        ViewHolder holder;
        Log.i(TAG,"position:"+position);
        int type = getItemViewType(position);
        switch (type){
            case ADD_TYPE_ITEM:
                if(convertView == null){
                    convertView = LayoutInflater.from(mContext)
                            .inflate(R.layout.item_add_city_layout,parent,false);
                    holder = new ViewHolder();
                    holder.addItem_iv =
                            (ImageView) convertView.findViewById(R.id.add_county_iv);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if(holder == null){
                    Log.i(TAG,"holder == null");
                }
                if(holder.addItem_iv == null){
                    Log.i(TAG,"holder.addItem_iv == null");
                }
//            holder.addItem_iv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //点击添加城市按钮时的回调
//                }
//            });
            break;
            case NORMAL_TYPE_ITEM:
                if(convertView == null){
                    convertView = LayoutInflater.from(mContext)
                            .inflate(R.layout.item_admin_city_layout,parent,false);
                    holder = new ViewHolder();
                    holder.removeItem_iv =
                            (ImageView) convertView.findViewById(R.id.remove_item_iv);
                    holder.cityTemperature_rl =
                            (RelativeLayout) convertView.findViewById(R.id.city_temp_relative );
                    holder.currentCounty_tv =
                            (TextView) convertView.findViewById(R.id.current_county_tv);
                    holder.weatherIcon_iv =
                            (ImageView) convertView.findViewById(R.id.weather_icon_iv);
                    holder.currentTemperature =
                            (TextView) convertView.findViewById(R.id.current_temperature);
                    holder.minAndMaxTemperature =
                            (TextView) convertView.findViewById(R.id.max_min_temp_tv);
                    holder.detailTemp_tv =
                            (TextView) convertView.findViewById(R.id.wind_detail_tv);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final CityAdminItem item = mCityList.get(position);

                holder.removeItem_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //使用回调方式处理删除城市item
                        mDeleteCityListener.deleteCity(item.getCityWeatherId());
                    }
                });
                holder.cityTemperature_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //使用回调方式，跳转当前的界面去主天气界面，并且切换城市
                        mDeleteCityListener.exchangeCityWeather(item.getCityWeatherId());
                    }
                });

                holder.currentCounty_tv.setText(item.getCityName());
                int id = Utilies.getWeatherIconId(item.getIconCode());
                holder.weatherIcon_iv.setImageResource(id);
                holder.currentTemperature.setText(item.getTemperature());
                holder.minAndMaxTemperature.setText
                        (assembleMinMaxTemp(item.getMinTemperature(),item.getMaxTemperature()));
                holder.detailTemp_tv.setText
                        (assembleWindDetail(item.getAirQuality(),item.getWindDir(),item.getWindForce()));

                break;
        }

        Log.i(TAG,"<<<<<<<<<<<<<<<getView()");
        return convertView;
    }

    private class ViewHolder{
        ImageView removeItem_iv;
        RelativeLayout cityTemperature_rl;
        TextView currentCounty_tv;
        ImageView weatherIcon_iv;
        TextView currentTemperature, minAndMaxTemperature;
        TextView detailTemp_tv;

        ImageView addItem_iv;
    }

    private String assembleMinMaxTemp(String minTemp,String maxTemp){
        //35°/ 34°
        return minTemp+"°/ "+maxTemp+"°";
    }

    private String assembleWindDetail(String airQuality, String windDir, String windForce){
        //空气良|微西南风
        return  "空气"+airQuality+"|"+windForce.substring(0,windForce.length()-1)+windDir;
    }

}
