package com.grandtech.map.gis.mapmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.TextSymbol;
import com.grandtech.map.R;
import com.grandtech.map.activity.MapActivity;
import com.grandtech.map.entity.Gps;
import com.grandtech.map.entity.Track;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.DrawSymbol;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.ToolPoolFactory;
import com.grandtech.map.utils.commons.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2016/11/30.
 * 定位管理  负责将坐标点 绘制到地图临时层上
 */

public class TrackManager extends DrawCore {

    private MapView mapView;
    private Context context;
    private TrackBroadcastReceiver trackBroadcastReceiver;
    private List<Track> tracks;//存储了当前显示的轨迹
    private SpatialReference mapSpatialReference;
    private SpatialReference pointSpatialReference;
    private Graphic trackGraphic;
    private int trackGraphicId;
    private boolean isFirstDraw = true;
    private LineSymbol lineSymbol = new SimpleLineSymbol(Color.RED, 2);
    private PictureMarkerSymbol trackPointSymbol;
    private TextSymbol trackTextSymbol;

    /**
     * 规定其子类必须实现的构造方法
     *
     * @param context
     */
    public TrackManager(Context context) {
        super(context);
        onCreate(context);
    }

    @Override
    public void onCreate(Context context) {
        this.mapView = mapContext.getMapView();
        this.context = context;
        trackBroadcastReceiver = new TrackBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MapActivity.BR_INTENT);
        context.registerReceiver(trackBroadcastReceiver, intentFilter);//注册广播
        onReady();
    }

    @Override
    public void onReady() {
        isFirstDraw = true;
        trackLayer.removeAll();
        pointSpatialReference = SpatialReference.create(SpatialReference.WKID_WGS84);
        trackPointSymbol = new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.track_point));
    }

    private void markTrack(List<Track> tracks) {
        trackLayer.removeAll();
        if (isFirstDraw) {
            mapSpatialReference = mapView.getSpatialReference(); //不能放的太前，因为地图加载需要时间，可能取得空值
            if (mapSpatialReference != null) {
                SharedPreferencesHelper.saveOrUpdateObject(context, "mapSpatialReference", mapSpatialReference);//空间参考对象的持久化
            } else {
                mapSpatialReference = (SpatialReference) SharedPreferencesHelper.readObject(context, "mapSpatialReference");
            }
        }
        if (mapSpatialReference != null) {
            for (int i = 0; i < tracks.size(); i++) {
                List<Point> points = tracks.get(i).getPointList();
                String trackName = tracks.get(i).getcTrackName();
                String startDate = tracks.get(i).getcStartTime();
                //trackTextSymbol = new TextSymbol(15, trackName + "(" + startDate + ")", Color.RED);
                trackTextSymbol = new TextSymbol(15, trackName, Color.RED);
                trackTextSymbol.setFontFamily("DroidSansFallback.ttf");
                trackTextSymbol.setOffsetX(0);
                trackTextSymbol.setOffsetY(-15);
                //利用节点信息创建MultiPath信息
                MultiPath multipath = new Polyline();
                for (int j = 0; j < points.size(); j++) {
                    Point mapPoint = (Point) GeometryEngine.project(points.get(j), pointSpatialReference, mapSpatialReference);//球面坐标转平面坐标
                    trackLayer.addGraphic(new Graphic(mapPoint, trackPointSymbol));
                    if (j == 0) {
                        multipath.startPath(mapPoint);
                        trackLayer.addGraphic(new Graphic(mapPoint, trackTextSymbol));
                    } else {
                        multipath.lineTo(mapPoint);
                    }
                }
                //创建多段线
                trackGraphic = new Graphic(multipath, lineSymbol);
                trackGraphicId = trackLayer.addGraphic(trackGraphic);
            }
            isFirstDraw = false;
        }
    }


    /**
     * 重写地图监听事件
     * @param event
     * @return
     */
    @Override
    public boolean onSingleTap(MotionEvent event) {
        Point point = mapView.toMapPoint(event.getX(), event.getY());
        int[] graphicIDs = trackLayer.getGraphicIDs((float) point.getX(), (float) point.getY(), 25);
        if(graphicIDs!=null&&graphicIDs.length>0){
            Toast.makeText(context,"选中了",Toast.LENGTH_SHORT).show();
        }
        return super.onSingleTap(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        trackLayer.removeAll();
        context.unregisterReceiver(trackBroadcastReceiver);
    }

    @Override
    public int triggerMode() {
        return ITool.MAP_CLICK;
    }

    /**
     * 广播接收器
     */
    private class TrackBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isFirstDraw){
                ToolPoolFactory.currentToolIndex_Btn = -1;
                ToolPoolFactory.currentToolIndex_Map = 13;//设置当前地图监听事件
            }
            tracks = (List<Track>) intent.getSerializableExtra("tracks");
            markTrack(tracks);
        }
    }
}
