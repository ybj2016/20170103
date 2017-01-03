package com.grandtech.map.gis.maptools;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.table.TableException;
import com.grandtech.map.gis.mapcore.DrawConstant;
import com.grandtech.map.gis.mapcore.DrawSymbol;
import com.grandtech.map.gis.maputil.EditingStates;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;

/**
 * Created by zy on 2016/11/7.
 * 图形编辑工具
 */

public class EditGraphicTool extends DrawCore{

    private MapView mapView;
    private Context context;
    private Layer layer;
    private GeodatabaseFeature geodatabaseFeature;
    private GeodatabaseFeature gdbFeatureSelected;//当前操作的GeodatabaseFeature
    private FeatureLayer featureLayer;//当前编辑的要素图层
    private LineSymbol lineSymbol;
    private boolean isEdit = false;//是否正在处于编辑状态


    public EditGraphicTool(Context context) {
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
        isEdit = false;
        featureLayer= mapContext.getFeatureLayer();
        handleType=mapContext.getHandleType();
        lineSymbol = DrawSymbol.mLineSymbol;
        polyline = new Polyline();
        drawGraphic = new Graphic(this.polyline, this.lineSymbol);
        graphicID = this.graphicsLayerEditing.addGraphic(drawGraphic);
    }

    @Override
    public boolean onSingleTap(MotionEvent event) {
        if(!isEdit) {//首次选中一个图形 准备执行编辑（选中一个图形，并使之高亮）
            final long[] featureIds = ((FeatureLayer) mapView.getLayerByID(featureLayer.getID())).getFeatureIDs(event.getX(), event.getY(), 25);
            if (featureIds.length > 0) {
                featureEditId = featureIds[0];
                gdbFeatureSelected = (GeodatabaseFeature) ((FeatureLayer) mapView.getLayerByID(featureLayer.getID())).getFeature(featureEditId);
                isEdit = true;//正处于编辑状态
                if (gdbFeatureSelected.getGeometry().getType().equals(Geometry.Type.POLYLINE)) {
                    handleType= DrawConstant.POLYLINE;
                    Polyline polyline = (Polyline) gdbFeatureSelected.getGeometry();
                    for (int i = 0; i < polyline.getPointCount(); i++) {
                        vertexPoints.add(polyline.getPoint(i));
                    }
                    refreshMap();
                    editingStates.add(new EditingStates(vertexPoints, midPointSelected, vertexSelected, insertingIndex));
                } else if (gdbFeatureSelected.getGeometry().getType().equals(Geometry.Type.POLYGON)) {
                    handleType=DrawConstant.POLYGON;
                    Polygon polygon = (Polygon) gdbFeatureSelected.getGeometry();
                    for (int i = 0; i < polygon.getPointCount(); i++) {
                        vertexPoints.add(polygon.getPoint(i));
                    }
                    refreshMap();
                    editingStates.add(new EditingStates(vertexPoints, midPointSelected, vertexSelected, insertingIndex));
                }
            }
        }else {//执行选中后的高亮操作
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
                        //editingStates.add(new EditingStates(vertexPoints, midPointSelected, vertexSelected, insertingIndex));
                        //vertexPoints.add(point);
                    }
                }
            }
            refreshMap();
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        //保存
        if(editingStates.size()>1) {
            try {
                ((GeodatabaseFeatureTable) featureLayer.getFeatureTable()).updateFeature(featureEditId, drawGraphic.getGeometry());
            } catch (TableException e) {
                e.printStackTrace();
            }
        }
        //清空临时图形
        clear();
        onReady();//为下一次执行做好准备
        return true;//事件不交给mapView处理
    }
    @Override
    protected void refreshMap() {
        super.refreshMap();
        if(handleType==DrawConstant.POLYLINE) {
            drawPolyline();//绘制线
            drawVertices(); //绘制要素节点
            drawPolylineMidPoints();
        }else if(handleType==DrawConstant.POLYGON){
            drawPolygon();
            drawVertices(); //绘制要素节点
            drawPolygonMidPoints();
        }
        mapContext.setFeatureEditId(featureEditId);
        mapContext.setFeatureLayer(featureLayer);
        mapContext.setDrawGraphic(drawGraphic);
    }

    @Override
    public int triggerMode() {
        return ITool.MAP_CLICK;
    }
}
