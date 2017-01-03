package com.grandtech.map.gis.maptools;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.MarkerSymbol;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.DrawSymbol;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2016/11/4.
 * 画点工具
 */

public class DrawPointTool extends DrawCore {

    private MapView mapView;
    private Context context;
    private MarkerSymbol markerSymbol;
    private List<PointContainer> pointContainers = new ArrayList<PointContainer>();

    public DrawPointTool(Context context) {
        super(context);
        onCreate(context);
        onReady();
    }

    @Override
    public void onCreate(Context context) {
        this.mapView = mapContext.getMapView();
        this.context = context;
        markerSymbol = DrawSymbol.markerSymbol;//绘制面的样式
    }

    @Override
    public void onReady() {
        featureLayer = mapContext.getFeatureLayer();
    }

    @Override
    public boolean onSingleTap(MotionEvent event) {
        point = new Point();
        Point mapPoint = mapView.toMapPoint(event.getX(), event.getY());
        drawGraphic = new Graphic(point, markerSymbol);
        graphicID = this.graphicsLayerEditing.addGraphic(drawGraphic);
        point.setXY(mapPoint.getX(), mapPoint.getY());
        int idx1 = getSelectedIndex(event.getX(), event.getY(), vertexPoints, mapView);//从临时集合里去除离手指最近的点
        if (vertexSelected) {
            PointContainer pc = pointContainers.get(insertingIndex);
            moveOldPoint(pc.getGraphicID(), pc.getPoint());
            vertexSelected = false;
        } else {
            if (idx1 != -1) {
                insertingIndex = idx1;
                vertexSelected = true;
            } else {
                vertexSelected = false;
                vertexPoints.add(point);//添加到临时集合里
                drawNewPoint(graphicID, point);
                pointContainers.add(new PointContainer(graphicID, point));
            }
        }

        return false;
    }

    @Override
    public int triggerMode() {
        return ITool.MAP_CLICK;
    }

    /**
     * 绘制一个点到绘制图层
     */
    private void drawNewPoint(int graphicID, Point point) {
        this.drawGraphic = new Graphic(point, DrawSymbol.markerSymbol);
        this.graphicsLayerEditing.updateGraphic(graphicID, drawGraphic);
    }

    /**
     * 移动一个旧点到新的位置
     */
    private void moveOldPoint(int graphicID, Point point) {
        this.drawGraphic = new Graphic(point, DrawSymbol.markerSymbol);
        this.graphicsLayerEditing.updateGraphic(graphicID, drawGraphic);
    }

    /**
     * 一个点容器
     */
    private class PointContainer {
        private int graphicID;
        private Point point;

        public PointContainer(int graphicID, Point point) {
            this.graphicID = graphicID;
            this.point = point;
        }

        public int getGraphicID() {
            return graphicID;
        }

        public void setGraphicID(int graphicID) {
            this.graphicID = graphicID;
        }

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }
    }
}
