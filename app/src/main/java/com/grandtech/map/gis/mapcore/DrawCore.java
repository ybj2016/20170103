package com.grandtech.map.gis.mapcore;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.grandtech.map.activity.AboutActivity;
import com.grandtech.map.gis.maputil.EditingStates;

import java.util.ArrayList;

/**
 * Created by zy on 2016/11/3.
 * 地图工具基类
 */

public abstract class DrawCore extends DrawCommon implements ITool {


    /**
     * 规定其子类必须实现的构造方法
     * @param context
     */
    public DrawCore(Context context){
        super(context);
    }

    /**
     * 绘制多段线
     */
    protected void drawPolyline(){
        handleType=DrawConstant.POLYLINE;
        MultiPath multipath;
        if (vertexPoints.size() >= 1) {
            //利用节点信息创建MultiPath信息
            multipath = new Polyline();
            multipath.startPath(vertexPoints.get(0));
            for (int i = 1; i < vertexPoints.size(); i++) {
                multipath.lineTo(vertexPoints.get(i));
            }
            //创建多段线或者面
            drawGraphic= new Graphic(multipath, DrawSymbol.mLineSymbol);
            polyline = (Polyline) multipath;//保存线数据到全局变量
            graphicsLayerEditing.updateGraphic(graphicID, this.drawGraphic);
        }
    }

    /**
     * 绘制多边形
     */
    protected void drawPolygon() {
        handleType=DrawConstant.POLYGON;
        MultiPath multipath;
        if (vertexPoints.size() >= 1) {
            //利用节点信息创建MultiPath信息
            multipath = new Polygon();
            multipath.startPath(vertexPoints.get(0));
            for (int i = 1; i < vertexPoints.size(); i++) {
                multipath.lineTo(vertexPoints.get(i));
            }
            //保存面数据到全局变量
            polygon = (Polygon) multipath;
            //绘制到临时图层
            drawGraphic = new Graphic(multipath,DrawSymbol.mFillSymbol);
            graphicsLayerEditing.updateGraphic(graphicID, this.drawGraphic);//更新到临时图层
        }
    }

    /**
     * 绘制要素的节点信息在mPoints中.
     */
    protected void drawVertices() {
        int index = 0;
        SimpleMarkerSymbol symbol;
        for (Point pt : vertexPoints) {
            if (vertexSelected && index == insertingIndex) {
                symbol = DrawSymbol.mRedMarkerSymbol;
            } else if (index == vertexPoints.size() - 1 && !midPointSelected && !vertexSelected) {
                symbol = DrawSymbol.mRedMarkerSymbol;
            } else {
                symbol = DrawSymbol.mBlackMarkerSymbol;
            }
            Graphic graphic = new Graphic(pt, symbol);
            sketchLayer.addGraphic(graphic);//添加节点信息到临时图层
            index++;
        }
    }

    /**
     * 绘线的中点
     */
    protected void drawPolylineMidPoints() {
        int index;
        Graphic graphic;
        midPoints.clear();
        if (vertexPoints.size() > 1) {
            for (int i = 1; i < vertexPoints.size(); i++) {
                Point p1 = vertexPoints.get(i - 1);
                Point p2 = vertexPoints.get(i);
                midPoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
            }
            index = 0;
            for (Point pt : midPoints) {
                if (midPointSelected && insertingIndex == index) {
                    graphic = new Graphic(pt, DrawSymbol.mRedMarkerSymbol);
                } else {
                    graphic = new Graphic(pt, DrawSymbol.mGreenMarkerSymbol);
                }
                this.sketchLayer.addGraphic(graphic);
                index++;
            }
        }
    }

    /**
     * 绘制节点的中心点
     */
    protected void drawPolygonMidPoints() {
        int index;
        Graphic graphic;
        midPoints.clear();
        if (vertexPoints.size() > 1) {
            for (int i = 1; i < vertexPoints.size(); i++) {
                Point p1 = vertexPoints.get(i - 1);
                Point p2 = vertexPoints.get(i);
                midPoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
            }
            if (vertexPoints.size() > 2) {
                Point p1 = vertexPoints.get(0);
                Point p2 = vertexPoints.get(vertexPoints.size() - 1);
                midPoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
            }
            index = 0;
            for (Point pt : midPoints) {
                if (midPointSelected && insertingIndex == index) {
                    graphic = new Graphic(pt, DrawSymbol.mRedMarkerSymbol);
                } else {
                    graphic = new Graphic(pt, DrawSymbol.mGreenMarkerSymbol);
                }
                this.sketchLayer.addGraphic(graphic);
                index++;
            }
        }
    }

    /**
     * 移动当前选择点
     */
    protected void movePoint(Point point) {
        editingStates.add(new EditingStates(vertexPoints, midPointSelected, vertexSelected, insertingIndex));//只存储上一个点
        if (midPointSelected) {//把中点移动到新位置
            vertexPoints.add(insertingIndex + 1, point);
        } else {//把顶点移动到新位置
            ArrayList<Point> temp = new ArrayList<>();
            for (int i = 0; i < vertexPoints.size(); i++) {
                if (i == insertingIndex) {
                    temp.add(point);
                } else {
                    temp.add(vertexPoints.get(i));
                }
            }
            vertexPoints.clear();
            vertexPoints.addAll(temp);
        }
        midPointSelected = false;//移动之后重置
        vertexSelected = false;//移动之后重置
        for (int i=0;i<editingStates.size();i++){
            System.out.println(editingStates.get(i).points.size());
        }
    }

    /**
     * 判断当前是点击地图事件 还是编辑绘制的点的事件
     * @param x
     * @param y
     * @param points
     * @param map
     * @return
     */
    protected int getSelectedIndex(double x, double y, ArrayList<Point> points, MapView map) {
        final int TOLERANCE = 40; // 距离容差  40个像素

        if (points == null || points.size() == 0) {
            return -1;
        }
        int index = -1;
        double distSQ_Small = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            Point p = map.toScreenPoint(points.get(i));
            double diffx = p.getX() - x;
            double diffy = p.getY() - y;
            double distSQ = diffx * diffx + diffy * diffy;
            if (distSQ < distSQ_Small) {
                index = i;
                distSQ_Small = distSQ;//取出最小的一个
            }
        }
        if (distSQ_Small < (TOLERANCE * TOLERANCE)) {//判断距离是否早 允许范围内
            return index;
        }
        return -1;
    }


    /**
     * 清空地图
     */
    protected void clear(){
        if(featureLayer!=null){
            featureLayer.clearSelection();
        }
        if (sketchLayer != null) {
            sketchLayer.removeAll();
        }
        if (graphicsLayerEditing != null) {
            graphicsLayerEditing.removeAll();
        }
        vertexPoints.clear();
        midPoints.clear();
        midPointSelected = false;
        vertexSelected = false;
        insertingIndex = 0;
        editingStates.clear();
    }

    protected void refreshMap(){
        saveToGlobalParam();
        if (sketchLayer != null) {
            sketchLayer.removeAll();
        }
    }

    @Override
    public abstract void onCreate(Context context);

    @Override
    public abstract void onReady();

    @Override
    public void onDestroy(){
        
    }

    @Override
    public abstract int triggerMode();

    @Override
    public void onClick() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onDoubleTapDragUp(MotionEvent up) {
        return false;
    }

    @Override
    public boolean onDoubleTapDrag(MotionEvent from, MotionEvent to) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent from, MotionEvent to, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTap(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onLongPressUp(MotionEvent point) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent point) {

    }

    @Override
    public void onMultiPointersSingleTap(MotionEvent event) {

    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onPinchPointersUp(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onPinchPointersMove(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
        return false;
    }

    @Override
    public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
        return false;
    }

    @Override
    public void beforeOperates(Object o) {

    }

    @Override
    public void operating(Object o) {

    }

    @Override
    public void afterOperates(Object o) {

    }
}
