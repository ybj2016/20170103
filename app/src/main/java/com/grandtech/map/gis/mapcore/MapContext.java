package com.grandtech.map.gis.mapcore;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.grandtech.map.entity.Track;
import com.grandtech.map.gis.maputil.EditingStates;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2016/11/11.
 */

public class MapContext {


    private static MapContext instance = null;

    private MapView mapView;
    //当前属性的编辑的要素图层
    private FeatureLayer featureLayer;
    //当前绘制要素graphicID
    private int graphicID;
    //当前绘制要素
    private Graphic drawGraphic;
    //需要持久化到FeatureLayer里的线数据
    private Polyline polyline;
    //需要持久化到FeatureLayer里的面数据
    private Polygon polygon;
    //绘制要素图层
    private GraphicsLayer graphicsLayerEditing = new GraphicsLayer();
    //当前绘制的草图图层
    private GraphicsLayer sketchLayer = new GraphicsLayer();
    //定位图层
    private GraphicsLayer locationLayer = new GraphicsLayer();
    //轨迹图层
    private GraphicsLayer trackLayer = new GraphicsLayer();
    //当前编辑要素的id
    private long featureEditId = -1;
    //节点
    private ArrayList<Point> vertexPoints = new ArrayList();
    //中间点
    private ArrayList<Point> midPoints = new ArrayList<Point>();
    //存储编辑状态的信息 主要用于回撤操作
    private ArrayList<EditingStates> editingStates = new ArrayList<EditingStates>();
    //线段中间点
    private boolean midPointSelected = false;
    //顶点是否选择
    private boolean vertexSelected = false;
    //插入位置信息
    private int insertingIndex = -1;
    //当前操作的 要素类型 点、线、面
    private int handleType = -1;
    //轨迹列表
    private List<Integer> checkItems;

    private MapContext(MapView mapView) {
        if (mapView != null) {
            this.mapView = mapView;
        }
        if (mapView != null) {
            mapView.addLayer(graphicsLayerEditing);
            mapView.addLayer(sketchLayer);
            mapView.addLayer(locationLayer);
            mapView.addLayer(trackLayer);
        }
    }

    private static synchronized void syncInit(MapView mapView) {
        if (instance == null) {
            instance = new MapContext(mapView);
        }
    }

    public static MapContext getInstance(MapView mapView) {
        if (instance == null) {
            syncInit(mapView);
        }
        return instance;
    }

    public synchronized int getHandleType() {
        return handleType;
    }

    public synchronized void setHandleType(int handleType) {
        this.handleType = handleType;
    }

    public synchronized MapView getMapView() {
        return mapView;
    }

    public synchronized void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public synchronized FeatureLayer getFeatureLayer() {
        return featureLayer;
    }

    public synchronized void setFeatureLayer(FeatureLayer featureLayer) {
        this.featureLayer = featureLayer;
    }

    public synchronized int getGraphicID() {
        return graphicID;
    }

    public synchronized void setGraphicID(int graphicID) {
        this.graphicID = graphicID;
    }

    public synchronized Graphic getDrawGraphic() {
        return drawGraphic;
    }

    public synchronized void setDrawGraphic(Graphic drawGraphic) {
        this.drawGraphic = drawGraphic;
    }

    public synchronized Polyline getPolyline() {
        return polyline;
    }

    public synchronized void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public synchronized Polygon getPolygon() {
        return polygon;
    }

    public synchronized void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public synchronized GraphicsLayer getGraphicsLayerEditing() {
        return graphicsLayerEditing;
    }

    public synchronized void setGraphicsLayerEditing(GraphicsLayer graphicsLayerEditing) {
        this.graphicsLayerEditing = graphicsLayerEditing;
    }

    public synchronized GraphicsLayer getSketchLayer() {
        return sketchLayer;
    }

    public synchronized void setSketchLayer(GraphicsLayer sketchLayer) {
        this.sketchLayer = sketchLayer;
    }

    public synchronized GraphicsLayer getLocationLayer() {
        return locationLayer;
    }

    public synchronized void setLocationLayer(GraphicsLayer locationLayer) {
        this.locationLayer = locationLayer;
    }

    public synchronized GraphicsLayer getTrackLayer() {
        return trackLayer;
    }

    public synchronized void setTrackLayer(GraphicsLayer trackLayer) {
        this.trackLayer = trackLayer;
    }

    public synchronized long getFeatureEditId() {
        return featureEditId;
    }

    public synchronized void setFeatureEditId(long featureEditId) {
        this.featureEditId = featureEditId;
    }

    public synchronized ArrayList<Point> getVertexPoints() {
        return vertexPoints;
    }

    public synchronized void setVertexPoints(ArrayList<Point> vertexPoints) {
        this.vertexPoints = vertexPoints;
    }

    public synchronized ArrayList<Point> getMidPoints() {
        return midPoints;
    }

    public synchronized void setMidPoints(ArrayList<Point> midPoints) {
        this.midPoints = midPoints;
    }

    public synchronized ArrayList<EditingStates> getEditingStates() {
        return editingStates;
    }

    public synchronized void setEditingStates(ArrayList<EditingStates> editingStates) {
        this.editingStates = editingStates;
    }

    public synchronized boolean isMidPointSelected() {
        return midPointSelected;
    }

    public synchronized void setMidPointSelected(boolean midPointSelected) {
        this.midPointSelected = midPointSelected;
    }

    public synchronized boolean isVertexSelected() {
        return vertexSelected;
    }

    public synchronized void setVertexSelected(boolean vertexSelected) {
        this.vertexSelected = vertexSelected;
    }

    public synchronized int getInsertingIndex() {
        return insertingIndex;
    }

    public synchronized void setInsertingIndex(int insertingIndex) {
        this.insertingIndex = insertingIndex;
    }


    public synchronized List<Integer> getCheckItems() {
        return checkItems;
    }

    public synchronized void setCheckItems(List<Integer> checkItems) {
        this.checkItems = checkItems;
    }

    public void recycle() {
        if(checkItems!=null){
            checkItems.clear();
        }
        if(mapView!=null){
            mapView.recycle();
        }
        instance = null;
    }
}
