package com.grandtech.map.gis.maptools;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.MapView;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;


/**
 * Created by zy on 2016/11/7.
 * 清空当前编辑操作
 */

public class ClearHandleTool extends DrawCore {

    private int mode = ITool.BTN_CLICK;
    private Context context;
    private MapView mapView;

    /**
     * 规定其子类必须实现的构造方法
     *
     * @param context
     */
    public ClearHandleTool(Context context) {
        super(context);
        onCreate(context);
    }

    @Override
    public void onCreate(Context context) {
        this.context = context;
    }

    @Override
    public void onReady() {
        mapView = mapContext.getMapView();
        featureLayer = mapContext.getFeatureLayer();
    }

    @Override
    public void onClick() {
        onReady();
        //Toast.makeText(context,"清空编辑",Toast.LENGTH_SHORT).show();
        if (featureLayer != null) {
            featureLayer.clearSelection();
        }
        if (sketchLayer != null) {
            sketchLayer.removeAll();
        }
        if (graphicsLayerEditing != null) {
            graphicsLayerEditing.removeAll();
        }
        vertexPoints.clear();
        midPoints.clear();
        midPointSelected = false;
        vertexSelected = false;
        insertingIndex = 0;
        editingStates.clear();
    }

    @Override
    public int triggerMode() {
        return mode;
    }
}
