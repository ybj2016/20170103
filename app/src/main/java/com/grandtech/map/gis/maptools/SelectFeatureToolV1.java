package com.grandtech.map.gis.maptools;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.esri.core.map.Feature;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2016/11/9.
 * 批量修改多个要素的一个属性
 */

public class SelectFeatureToolV1 extends DrawCore {

    private MapView mapView;
    private Context context;
    private FeatureLayer featureLayer;//当前编辑的要素图层
    private List<Long> geometryIds;//记录当前选中的要素
    private ICallBack iCallBack;
    private boolean isOpened = false;//编辑框是否开启

    /**
     * 规定其子类必须实现的构造方法
     *
     * @param context
     */
    public SelectFeatureToolV1(Context context) {
        super(context);
        onCreate(context);
        onReady();
    }

    @Override
    public void onCreate(Context context) {
        this.mapView = mapContext.getMapView();
        this.context = context;
    }

    @Override
    public void onReady() {
        featureLayer = mapContext.getFeatureLayer();
        geometryIds = new ArrayList<>();
    }

    @Override
    public boolean onSingleTap(MotionEvent event) {
        long start = System.currentTimeMillis();
        if (true && featureLayer != null) {//首次选中一个图形 准备执行编辑（选中一个图形，并使之高亮）
            long[] featureIds = featureLayer.getFeatureIDs(event.getX(), event.getY(), 25);
            if (featureIds.length > 0) {
                long featureId = featureIds[0];
                if (geometryIds.contains(featureId)) {//移除选中
                    geometryIds.remove(featureId);//添加到数组里
                    featureLayer.unselectFeature(featureId);
                } else {//新增选中
                    geometryIds.add(featureId);//添加到数组里
                    featureLayer.selectFeature(featureId);
                    featureLayer.setSelectionColor(Color.GREEN);
                    //=====单选代码===如果注释掉则选中功能就支持多选=====
                    for (int i = 0; i < geometryIds.size(); i++) {
                        long oldFeatureId = geometryIds.get(i);
                        if (oldFeatureId!=featureId){
                            geometryIds.remove(oldFeatureId);
                            featureLayer.unselectFeature(oldFeatureId);
                        }
                    }
                    //=====单选代码===如果注释掉则选中功能就支持多选=====
                }
            }
        }
        if (geometryIds.size() > 0) {
            if (iCallBack != null) {
                iCallBack.startEdit(featureLayer.getSelectedFeatures());
            }
        } else {
            if (iCallBack != null) {
                iCallBack.endEdit();
            }
        }
        long end = System.currentTimeMillis();
        //Toast.makeText(context,(end-start)+"",Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public int triggerMode() {
        return ITool.MAP_CLICK;
    }


    public void setICallBack(ICallBack iCallBack) {
        this.iCallBack = iCallBack;
    }

    public interface ICallBack {
        public void startEdit(List<Feature> features);//开始编辑

        public void endEdit();//结束编辑
    }
}
