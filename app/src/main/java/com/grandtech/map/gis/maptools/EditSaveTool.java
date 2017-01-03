package com.grandtech.map.gis.maptools;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.table.TableException;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;


/**
 * Created by zy on 2016/11/8.
 */

public class EditSaveTool extends DrawCore{

    private Context context;
    private MapView mapView;
    private FeatureLayer featureLayer;
    /**
     * 规定其子类必须实现的构造方法
     *
     * @param context
     */
    public EditSaveTool(Context context) {
        super(context);
        onCreate(context);
    }

    @Override
    public void onCreate(Context context) {
        this.mapView= mapContext.getMapView();
        this.context=context;
    }

    @Override
    public void onReady() {//从全局变量里获取数据
        editingStates= mapContext.getEditingStates();
        featureLayer=mapContext.getFeatureLayer();
        featureEditId=mapContext.getFeatureEditId();
        drawGraphic=mapContext.getDrawGraphic();
    }

    @Override
    public void onClick() {
        //初始化参数
        onReady();
        //保存编辑
        if(editingStates.size()>1) {
            try {
                ((GeodatabaseFeatureTable) featureLayer.getFeatureTable()).updateFeature(featureEditId, drawGraphic.getGeometry());
            } catch (TableException e) {
                e.printStackTrace();
            }
        }
        //清空临时图形
        clear();
    }

    @Override
    public int triggerMode() {
        return ITool.BTN_CLICK;
    }
}
