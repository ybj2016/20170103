package com.grandtech.map.gis.maputil;

import android.util.Log;

import com.esri.android.map.MapView;

/**
 * Created by kuchanly on 2016-10-19.
 * 地图初始化及地图操作相关
 */

public class MapUtil {

    private static final String TAG = "MapUtil";

    /**
     * 初始化地图对象相关参数
     */
    public static void init(MapView map) {
        try {
            map.setMinScale(5000000);//设置当前地图最小比例尺
            map.setMaxScale(100);//设置当前地图对象最大比例尺
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }


}
