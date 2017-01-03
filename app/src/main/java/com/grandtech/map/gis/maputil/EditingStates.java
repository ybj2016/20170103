package com.grandtech.map.gis.maputil;

import com.esri.core.geometry.Point;

import java.util.ArrayList;

/**
 * Created by zy on 2016/10/26.
 */

/**
 * 点的容器
 */
public class EditingStates {
    public ArrayList<Point> points = new ArrayList<Point>();
    public boolean midPointSelected = false;
    public boolean vertexSelected = false;
    public int insertingIndex;

    public EditingStates(ArrayList<Point> points, boolean midPointSelected, boolean vertexSelected, int insertingIndex) {
        this.points.addAll(points);
        this.midPointSelected = midPointSelected;
        this.vertexSelected = vertexSelected;
        this.insertingIndex = insertingIndex;
    }
}
