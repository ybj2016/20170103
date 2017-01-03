package com.grandtech.map.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class LayerInfo implements Serializable {

    private static final long serialVersionUID = -6465237897027410019L;

    public final static int LAYER_UNSELECTED = 0;//当前图层被选中显示

    public final static int LAYER_SELECTED = 1;//当前图层未被选中显示

    public final static int LAYER_UNOPTIONED = 0;//当前图层未被操作

    public final static int LAYER_OPTIONED = 1;//当前图层正在被操作   //该属性所有图层唯一

    private int layerId;//图层ID

    private String layerName;//图层名称

    private int layerOrderId;//图层的排序顺序  rank

    private int layerSelected;//图层是否选中显示：0表示不显示，1表示显示

    private String layerPath;//图层地址

    private int layerOption = LAYER_UNOPTIONED;//图层是否操作：0表示不操作，1表示操作


    public LayerInfo(int layerId, String layerName, int layerOrderId, int layerSelected, String layerPath) {
        this.layerId = layerId;
        this.layerName = layerName;
        this.layerOrderId = layerOrderId;
        this.layerSelected = layerSelected;
        this.layerPath = layerPath;
    }

    public int getLayerId() {
        return layerId;
    }

    public void setLayerId(int layerId) {
        this.layerId = layerId;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public int getLayerOrderId() {
        return layerOrderId;
    }

    public void setLayerOrderId(int layerOrderId) {
        this.layerOrderId = layerOrderId;
    }

    public int getLayerSelected() {
        return layerSelected;
    }

    public void setLayerSelected(int layerSelected) {
        this.layerSelected = layerSelected;
    }

    public String getLayerPath() {
        return layerPath;
    }

    public void setLayerPath(String layerPath) {
        this.layerPath = layerPath;
    }

    public void setLayerOption(int layerOption) {
        this.layerOption = layerOption;
    }

    public int getLayerOption() {
        return layerOption;
    }
}