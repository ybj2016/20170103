package com.grandtech.map.gis.maptools;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;


/**
 * Created by zy on 2016/11/7.
 * 属性编辑工具
 */

public class EditAttributeTool extends DrawCore{

    private MapView mapView;
    private Context context;
    private boolean isSearchData = false;
    private FeatureLayer featureLayer;//当前编辑的要素图层
    private CallbackListener<FeatureResult> resultCallbackListener;

    /**
     * 规定其子类必须实现的构造方法
     *
     * @param context
     */
    public EditAttributeTool(Context context) {
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
        featureLayer= mapContext.getFeatureLayer();
    }

    @Override
    public boolean onSingleTap(MotionEvent event) {
        if(!isSearchData) {//首次选中一个图形 准备执行编辑（选中一个图形，并使之高亮）
            isSearchData=true;
            Point point = mapView.toMapPoint(new Point(event.getX(), event.getY()));//屏幕坐标转化为地图坐标
            final long[] featureIds = ((FeatureLayer) mapView.getLayerByID(featureLayer.getID())).getFeatureIDs(event.getX(), event.getY(), 25);
            if (featureIds.length > 0) {
                QueryParameters query = new QueryParameters();
                query.setOutFields(new String[]{"*"});
                query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
                query.setGeometry(point);
                query.setInSpatialReference(mapView.getSpatialReference());
                featureLayer.setSelectionColorWidth(Color.GREEN);
                featureLayer.selectFeatures(query, FeatureLayer.SelectionMode.NEW, resultCallbackListener);
            }
        }
        return false;
    }

    @Override
    public int triggerMode() {
        return ITool.MAP_CLICK;
    }

    //设置回调监听函数
    public void setResultCallbackListener(CallbackListener<FeatureResult> resultCallbackListener){
        if(this.resultCallbackListener==null){
            this.resultCallbackListener=resultCallbackListener;
        }
    }

    public void setIsSearchData(boolean isSearchData){
        this.isSearchData=isSearchData;
    }
}
