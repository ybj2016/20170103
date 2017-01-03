package com.grandtech.map.gis.mapcore;

import android.content.Context;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.grandtech.map.gis.maputil.EditingStates;

import java.util.ArrayList;

/**
 * Created by zy on 2016/12/2.
 */

public class DrawCommon {

    //从全局变量里取值
    protected MapContext mapContext;
    //当前绘制要素graphicID
    protected int graphicID;
    //当前绘制要素
    protected Graphic drawGraphic;
    //需要持久化到FeatureLayer里的点数据
    protected Point point;
    //需要持久化到FeatureLayer里的线数据
    protected Polyline polyline;
    //需要持久化到FeatureLayer里的面数据
    protected Polygon polygon;
    //绘制要素图层
    protected GraphicsLayer graphicsLayerEditing;
    //零时图层，用于存储要素各个节点信息
    protected GraphicsLayer sketchLayer;
    //定位图层
    protected GraphicsLayer locationLayer;
    //轨迹图层
    protected GraphicsLayer trackLayer;
    //当前属性的编辑的要素图层
    protected FeatureLayer featureLayer;
    //当前编辑要素的id
    protected long featureEditId;
    //节点
    protected ArrayList<Point> vertexPoints;
    //中间点
    protected ArrayList<Point> midPoints;
    //编辑状态信息
    protected ArrayList<EditingStates> editingStates;
    //线段中间点
    protected boolean midPointSelected;
    //顶点是否选择
    protected boolean vertexSelected;
    //插入位置信息
    protected int insertingIndex;
    //当前操作的 要素类型 点、线、面
    protected int handleType;

    public DrawCommon(Context context) {
        initGlobalParam();
    }

    private void initGlobalParam(){
        //从全局变量里取值
        mapContext = MapContext.getInstance(null);
        //当前绘制要素graphicID
        graphicID = mapContext.getGraphicID();
        //当前绘制要素
        drawGraphic = mapContext.getDrawGraphic();
        //需要持久化到FeatureLayer里的线数据
        polyline = mapContext.getPolyline();
        //需要持久化到FeatureLayer里的面数据
        polygon = mapContext.getPolygon();
        //绘制要素图层
        graphicsLayerEditing = mapContext.getGraphicsLayerEditing();
        //零时图层，用于存储要素各个节点信息
        sketchLayer = mapContext.getSketchLayer();
        //定位图层
        locationLayer = mapContext.getLocationLayer();
        //轨迹图层
        trackLayer = mapContext.getTrackLayer();
        //当前属性的编辑的要素图层
        featureLayer = mapContext.getFeatureLayer();
        //当前编辑要素的id
        featureEditId = mapContext.getFeatureEditId();
        //节点
        vertexPoints = mapContext.getVertexPoints();
        //中间点
        midPoints = mapContext.getMidPoints();
        //编辑状态信息
        editingStates = mapContext.getEditingStates();
        //线段中间点
        midPointSelected = mapContext.isMidPointSelected();
        //顶点是否选择
        vertexSelected = mapContext.isVertexSelected();
        //插入位置信息
        insertingIndex = mapContext.getInsertingIndex();
        //当前操作的 要素类型 点、线、面
        handleType = mapContext.getHandleType();
    }

    /**
     * 变量存到全局变量中去
     */
    protected void saveToGlobalParam() {
        mapContext.setGraphicID(graphicID);
        mapContext.setDrawGraphic(drawGraphic);
        mapContext.setPolyline(polyline);
        mapContext.setPolygon(polygon);
        mapContext.setGraphicsLayerEditing(graphicsLayerEditing);
        mapContext.setSketchLayer(sketchLayer);
        mapContext.setLocationLayer(locationLayer);
        mapContext.setTrackLayer(trackLayer);
        mapContext.setFeatureLayer(featureLayer);
        mapContext.setVertexPoints(vertexPoints);
        mapContext.setMidPoints(midPoints);
        mapContext.setEditingStates(editingStates);
        mapContext.setMidPointSelected(midPointSelected);
        mapContext.setVertexSelected(vertexSelected);
        mapContext.setInsertingIndex(insertingIndex);
        mapContext.setHandleType(handleType);
        mapContext.setFeatureEditId(featureEditId);
    }

}
