package com.grandtech.map.gis.maptools;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.LineSymbol;
import com.grandtech.map.gis.mapcore.DrawSymbol;
import com.grandtech.map.gis.maputil.EditingStates;
import com.grandtech.map.gis.mapcore.CutPolygon;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by zy on 2016/11/7.
 * 面分割
 */

public class AreaCutTool extends DrawCore {

    private Context context;
    private MapView mapView;
    private long[] editFeatureIds;
    private FeatureLayer featureLayer;//当前编辑的要素图层
    private LineSymbol lineSymbol;//绘制线的样式
    private ICallBack iCallBack;

    /**
     * 规定其子类必须实现的构造方法
     *
     * @param context
     */
    public AreaCutTool(Context context) {
        super(context);
        onCreate(context);
        onReady();
    }

    @Override
    public void onCreate(Context context) {
        this.context = context;
        this.lineSymbol = DrawSymbol.mLineSymbol;//绘制线的样式
    }

    @Override
    public void onReady() {
        mapView= mapContext.getMapView();
        featureLayer = mapContext.getFeatureLayer();
        polyline = new Polyline();
        drawGraphic = new Graphic(polyline, lineSymbol);
        graphicID = graphicsLayerEditing.addGraphic(drawGraphic);
    }

    @Override
    public boolean onSingleTap(MotionEvent event) {
        Point point = mapView.toMapPoint(event.getX(), event.getY());
        //首次判断是否为移动节点
        if (midPointSelected || vertexSelected) {
            movePoint(point);
        } else {
            int idx1 = getSelectedIndex(event.getX(), event.getY(), midPoints, mapView);//从中间点集合获取索引
            if (idx1 != -1) {//-1表示选中了中点
                midPointSelected = true;
                insertingIndex = idx1;
            } else {//如果选中了节点
                int idx2 = getSelectedIndex(event.getX(), event.getY(), vertexPoints, mapView);//从节点集合获取索引
                if (idx2 != -1) {//-1表示选中了节点
                    vertexSelected = true;
                    insertingIndex = idx2;
                } else {//用户没有执行移动节点操作
                    editingStates.add(new EditingStates(vertexPoints, midPointSelected, vertexSelected, insertingIndex));
                    vertexPoints.add(point);
                }
            }
        }
        refreshMap();
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        ArrayList<Polygon> ps;
        if (featureLayer == null) return true;
        if (iCallBack != null) {
            int count = featureLayer.getSelectedFeatures().size();
            if (count > 1) {
                iCallBack.cutFail(new Throwable("只能同时分割一个要素！"));
                return true;//结束
            }
            boolean isContinue = iCallBack.beforeCut(count);
            if (!isContinue) {
                return true;//结束
            }
        }
        return true;
    }

    @Override
    protected void refreshMap() {
        super.refreshMap();
        drawPolyline();//绘制线
        drawVertices(); //绘制要素节点
        drawPolylineMidPoints();
    }

    @Override
    public int triggerMode() {
        return ITool.MAP_CLICK;
    }


    @Override
    public void beforeOperates(Object o) {

    }

    @Override
    public void operating(Object o) {
        //onReady();//为下一次分割做好准备
        try {
            ArrayList<Polygon> ps;
            Feature feature = (featureLayer.getSelectedFeatures()).get(0);
            polygon = (Polygon) feature.getGeometry();

            ps = CutPolygon.Cut(polygon, polyline);
            //先删掉原有的feature
            Map<String, Object> attributes = feature.getAttributes();//删除前获取属性
            GeodatabaseFeature geodatabaseFeature = (GeodatabaseFeature) ((FeatureLayer) mapView.getLayerByID(featureLayer.getID())).getFeature(feature.getId());
            if (geodatabaseFeature != null) {
                geodatabaseFeature.getTable().deleteFeature(geodatabaseFeature.getId());
            }
            //保存为Feature
            for (int i = 0; i < ps.size(); i++) {
                GeodatabaseFeatureTable geoFeatTable = (GeodatabaseFeatureTable) featureLayer.getFeatureTable();
                GeodatabaseFeature geoFeature = new GeodatabaseFeature(attributes, ps.get(i), geoFeatTable);
                geoFeatTable.addFeature(geoFeature);
            }
            clear();//清空geometry
            if (iCallBack != null) {
                iCallBack.cutSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (iCallBack != null) {
                iCallBack.cutFail(e);
            }
        }
    }

    @Override
    public void afterOperates(Object o) {

    }

    public void setICallBack(ICallBack iCallBack) {
        this.iCallBack = iCallBack;
    }

    public interface ICallBack {
        public boolean beforeCut(int count);

        public void cutFail(Throwable e);

        public void cutSuccess();
    }
}
