package com.grandtech.map.entity;

import com.esri.core.geometry.Point;
import com.grandtech.map.utils.commons.List2StrFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2016/11/24.
 * gps坐标点（wgs84）
 */

public class Track implements Serializable {

    public final static String TB_GPS="tb_gps";//表名
    public final static String C_ID = "cId"; //id
    public final static String C_TRACK_NAME="cTrackName";//轨迹名称
    public final static String C_START_TIME="cStartTime";//开始时间
    public final static String C_COORDINATES  = "cCoordinates";//坐标串
    public final static String C_COUNT="cCount";//坐标点数
    public final static String C_END_TIME="cEndTime";//结束时间
    public final static String C_DESC="cDesc";//描述

    private Integer cId;
    private String cTrackName;
    private String cCoordinates;
    private Integer cCount;
    private String cStartTime;
    private String cEndTime;
    private String cDesc;


    private List<Gps> gpsList=new List2StrFormat<Gps>(";");

    public void addGps(Gps gps){
        gpsList.add(gps);
    }

    public List<Gps> getGpsList(){
        return gpsList;
    }

    public List<Point> getPointList(){
        List<Point> listPoint = new ArrayList<>();
        if(gpsList!=null&&gpsList.size()>0){
            Point point;
            for(Gps gps : gpsList){
                point=new Point(gps.getWgLon(),gps.getWgLat());
                listPoint.add(point);
            }
            return listPoint;
        }else if(cCoordinates!=null&&(gpsList==null||gpsList.size()==0)){
            String[] coordinates = cCoordinates.split(";");
            Point point;
            for (int i = 0; i < coordinates.length; i++) {
                point = new Point();
                String[] pointArray = coordinates[i].split(",");
                point.setXY((double)Float.parseFloat(pointArray[1]),(double) Float.parseFloat(pointArray[0]));
                listPoint.add(point);
            }
            return listPoint;
        }
        return null;
    }

    public void removeGpsList(){
        gpsList.clear();
    }

    public Gps getInitializeGps(Double x, Double y){
        return new Gps(x,y);
    }


    public  void setcId(Integer cId){
        this.cId=cId;
    }

    public Integer getcId(){
        return cId;
    }

    public String getcTrackName() {
        return cTrackName;
    }

    public void setcTrackName(String cTrackName) {
        this.cTrackName = cTrackName;
    }

    public String getcCoordinates() {
        return cCoordinates;
    }

    public void setcCoordinates(String cCoordinates) {
        this.cCoordinates = cCoordinates;
    }

    public Integer getcCount() {
        return cCount;
    }

    public void setcCount(Integer cCount) {
        this.cCount = cCount;
    }

    public String getcEndTime() {
        return cEndTime;
    }

    public void setcEndTime(String cEndTime) {
        this.cEndTime = cEndTime;
    }

    public String getcDesc() {
        return cDesc;
    }

    public void setcDesc(String cDesc) {
        this.cDesc = cDesc;
    }

    public String getcStartTime() {
        return cStartTime;
    }

    public void setcStartTime(String cStartTime) {
        this.cStartTime = cStartTime;
    }

}
