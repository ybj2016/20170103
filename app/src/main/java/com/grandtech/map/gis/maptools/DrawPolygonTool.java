package com.grandtech.map.gis.maptools;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.table.TableException;
import com.grandtech.map.gis.mapcore.DrawSymbol;
import com.grandtech.map.gis.maputil.EditingStates;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;


/**
 * Created by kuchanly on 2016-11-2.
 * 画面工具
 */

public class DrawPolygonTool extends DrawCore{

    private MapView mapView;
    private Context context;
    private FillSymbol fillSymbol;

    public DrawPolygonTool(Context context){
        super(context);
        onCreate(context);
        onReady();
    }

    @Override
    public void onCreate(Context context){
        this.mapView = mapContext.getMapView();
        this.context=context;
        //绘制面的样式
        this.fillSymbol = DrawSymbol.mFillSymbol;
        this.fillSymbol.setAlpha(90);
    }

    @Override
    public void onReady() {
        featureLayer= mapContext.getFeatureLayer();
        polygon = new Polygon();
        drawGraphic = new Graphic(polygon,fillSymbol);
        graphicID = graphicsLayerEditing.addGraphic(drawGraphic);
    }

    @Override
    public boolean onSingleTap(MotionEvent event) {
        Point point = mapView.toMapPoint(event.getX(), event.getY());
        System.out.println(point.getX()+"||"+point.getY());
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
        //保存
        if(featureLayer==null)return true;
        if(editingStates.size()>1) {
            try {
                GeodatabaseFeatureTable geoFeatTable=(GeodatabaseFeatureTable) featureLayer.getFeatureTable();
                GeodatabaseFeature geoFeature=new GeodatabaseFeature(null,drawGraphic.getGeometry(),geoFeatTable);
                geoFeatTable.addFeature(geoFeature);
            } catch (TableException e) {
                Toast.makeText(context,"不能在范围之外绘制",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        //清空临时图形
        clear();
        onReady();//为下一次执行做好准备
        return true;//事件不交给mapView处理
    }

    public int triggerMode() {
        return ITool.MAP_CLICK;
    }

    @Override
    protected void refreshMap() {
        super.refreshMap();
        drawPolygon();//绘制面
        drawVertices(); //绘制要素节点
        drawPolygonMidPoints();
    }
}
