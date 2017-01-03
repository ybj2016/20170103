package com.grandtech.map.gis.maptools;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.Feature;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2016/11/9.
 */

public class DeleteFeatureTool extends DrawCore {

    private int mode = ITool.BTN_CLICK;
    private MapView mapView;
    private Context context;
    private FeatureLayer featureLayer;
    private List<Feature> features;
    private GeodatabaseFeature geodatabaseFeature;
    private ICallBack iCallBack;

    /**
     * 规定其子类必须实现的构造方法
     *
     * @param context
     */
    public DeleteFeatureTool(Context context) {
        super(context);
        onCreate(context);
    }

    @Override
    public void onCreate(Context context) {
        this.context = context;
    }

    @Override
    public void onReady() {
        mapView=mapContext.getMapView();
        featureLayer = mapContext.getFeatureLayer();
        features = new ArrayList<>();
    }

    @Override
    public void onClick() {
        onReady();
        if (featureLayer == null) return;
        //执行删除
        features = featureLayer.getSelectedFeatures();
        if (iCallBack != null) {//删除前回调
            boolean isContinue = iCallBack.beforeDelete(features.size());
            if (!isContinue) {
                return;
            }
        }
    }

    @Override
    public int triggerMode() {
        return mode;
    }

    //删除
    @Override
    public void operating(Object o) {
        boolean isError = false;
        for (int i = 0; i < features.size(); i++) {
            geodatabaseFeature = (GeodatabaseFeature) ((FeatureLayer) mapView.getLayerByID(featureLayer.getID())).getFeature(features.get(i).getId());
            if (geodatabaseFeature != null) {
                try {
                    geodatabaseFeature.getTable().deleteFeature(geodatabaseFeature.getId());
                } catch (Exception e) {
                    isError = true;
                    if (iCallBack != null) {//删除前回调
                        iCallBack.deleteFail(e);
                    }
                    break;
                }
            }
        }
        if (!isError) {
            if (iCallBack != null) {//删除前回调
                iCallBack.deleteSuccess();
            }
        }
    }

    public void setICallBack(ICallBack iCallBack) {
        this.iCallBack = iCallBack;
    }

    public interface ICallBack {
        //删除前
        public boolean beforeDelete(int count);

        //删除出现异常
        public void deleteFail(Exception e);

        //删除后
        public void deleteSuccess();

    }
}
