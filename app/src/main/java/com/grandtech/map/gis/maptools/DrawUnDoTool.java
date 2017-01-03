package com.grandtech.map.gis.maptools;

import android.content.Context;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.grandtech.map.gis.mapcore.DrawConstant;
import com.grandtech.map.gis.maputil.EditingStates;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;

/**
 * Created by zy on 2016/11/3.
 * 回撤工具
 */

public class DrawUnDoTool extends DrawCore{

    private MapView mapView;
    private Context context;

    public DrawUnDoTool(Context context){
        super(context);
        onCreate(context);
    }

    @Override
    public void onCreate(Context context) {
        this.mapView= (MapView) mapView;
        this.context=context;
    }

    @Override
    public void onReady() {
        featureLayer= mapContext.getFeatureLayer();
        graphicID= mapContext.getGraphicID();//获取全局id
    }

    @Override
    public void onClick() {
        onReady();
        if(vertexPoints.size()>=1) {//删除时至少保留一个节点
            if(editingStates.size()>=1){
                if (editingStates.size() == 0) {
                    midPointSelected = false;
                    vertexSelected = false;
                    insertingIndex = 0;
                } else {
                    vertexPoints.clear();//清空节点要素
                    EditingStates state = editingStates.get(editingStates.size() - 1);
                    vertexPoints.addAll(state.points);
                    midPointSelected = state.midPointSelected;
                    vertexSelected = state.vertexSelected;
                    insertingIndex = state.insertingIndex;
                    refreshMap();
                    editingStates.remove(editingStates.size() - 1);//临时数组
                    Toast.makeText(mapView.getContext(), "撤销操作", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(mapView.getContext(), "当前状态无法撤销", Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(context,"回撤",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void refreshMap() {
        handleType=mapContext.getHandleType();//必须放到父类方法之前
        super.refreshMap();
        if(handleType== DrawConstant.POLYGON){
            drawPolygon();//绘制面
            drawVertices(); //绘制要素节点
            drawPolygonMidPoints();
        }else if(handleType==DrawConstant.POLYLINE){
            drawPolyline();//绘制线
            drawVertices(); //绘制要素节点
            drawPolylineMidPoints();
        }
    }

    @Override
    public int triggerMode() {
        return ITool.BTN_CLICK;
    }
}
