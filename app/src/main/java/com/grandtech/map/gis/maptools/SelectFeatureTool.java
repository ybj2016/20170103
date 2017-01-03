package com.grandtech.map.gis.maptools;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2016/11/9.
 */

public class SelectFeatureTool extends DrawCore{

    private MapView mapView;
    private Context context;
    private FeatureLayer featureLayer;//当前编辑的要素图层
    private List<Long> geometryIds=new ArrayList<>();//记录当前选中的要素

    /**
     * 规定其子类必须实现的构造方法
     *
     * @param context
     */
    public SelectFeatureTool(Context context) {
        super(context);
        onCreate(context);
        onReady();
    }

    @Override
    public void onCreate(Context context) {
        this.mapView = mapContext.getMapView();
        this.context = context;
    }
    /**
     * 设置当前要查询的 featureLayer
     * @param featureLayer
     */
    public void setFeatureLayer(FeatureLayer featureLayer){
        this.featureLayer=featureLayer;
    }

    @Override
    public void onReady() {
        featureLayer= mapContext.getFeatureLayer();
    }

    @Override
    public boolean onSingleTap(MotionEvent event) {
        if(true) {//首次选中一个图形 准备执行编辑（选中一个图形，并使之高亮）
            final long[] featureIds = ((FeatureLayer) mapView.getLayerByID(featureLayer.getID())).getFeatureIDs(event.getX(), event.getY(), 25);
            if (featureIds.length > 0) {
                long featureId=featureIds[0];
                if(geometryIds.contains(featureId)){//新增选中
                    geometryIds.remove(featureIds[0]);//添加到数组里
                    featureLayer.unselectFeature(featureIds[0]);
                }else {//新增移除
                    geometryIds.add(featureIds[0]);//添加到数组里
                    featureLayer.selectFeature(featureIds[0]);
                    featureLayer.setSelectionColor(Color.GREEN);
                }
            }
        }
        return false;
    }

    @Override
    public int triggerMode() {
        return ITool.MAP_CLICK;
    }
}
