package com.grandtech.map.gis.mapmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.MotionEvent;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.grandtech.map.R;
import com.grandtech.map.activity.TrackActivity;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.utils.commons.SharedPreferencesHelper;


/**
 * Created by zy on 2016/12/2.
 */

public class LocationManager extends DrawCore {

    private Context context;
    private MapView mapView;
    private PictureMarkerSymbol locationSymbol;
    private Point mapPoint;
    private SpatialReference mapSpatialReference;
    private SpatialReference pointSpatialReference;
    private Graphic locGraphic;
    private int locGraphicId;
    private LocationBroadcastReceiver locationBroadcastReceiver;
    private boolean isFirstDraw = true;

    /**
     * 规定其子类必须实现的构造方法
     *
     * @param context
     */
    public LocationManager(Context context) {
        super(context);
        this.context = context;
        this.mapView = mapContext.getMapView();
        onCreate(context);
    }


    @Override
    public void onCreate(Context context) {
        locationSymbol = new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.location));
        locationSymbol.setOffsetY(30);
        locationBroadcastReceiver = new LocationBroadcastReceiver();
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(TrackActivity.BR_INTENT);
        context.registerReceiver(locationBroadcastReceiver, myIntentFilter);//注册广播
        onReady();
    }

    @Override
    public void onReady() {
        isFirstDraw = true;
        locationLayer.removeAll();//清空之前的
        pointSpatialReference = SpatialReference.create(SpatialReference.WKID_WGS84);
    }

    public void onDestroy() {
        locationLayer.removeAll();
        context.unregisterReceiver(locationBroadcastReceiver);
    }

    /**
     * 将轨迹绘制到地图上
     * @param gpsPoint
     */
    private void markLocation(Point gpsPoint) {
        if (isFirstDraw) {
            mapSpatialReference = mapView.getSpatialReference(); //不能放的太前，因为地图加载需要时间，可能取得空值
            if (mapSpatialReference != null) {
                SharedPreferencesHelper.saveOrUpdateObject(context, "mapSpatialReference", mapSpatialReference);//空间参考对象的持久化
            }else {
                mapSpatialReference = (SpatialReference) SharedPreferencesHelper.readObject(context, "mapSpatialReference");
            }
        }
        if (mapSpatialReference != null) {
            mapPoint = (Point) GeometryEngine.project(gpsPoint, pointSpatialReference, mapSpatialReference);//球面坐标转平面坐标
            locGraphic = new Graphic(mapPoint, locationSymbol);
            locationLayer.removeAll();
            locGraphicId = locationLayer.addGraphic(locGraphic); //定位到所在位置
            isFirstDraw = false;
        }
    }

    @Override
    public int triggerMode() {
        return 0;
    }

    /**
     * 广播接收器
     */
    private class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra("locServiceRun", false)) {
                point = (Point) intent.getSerializableExtra("locPoint");
                markLocation(point);
            } else {
                onReady();//为下一次定位做好准备
            }
        }
    }
}
