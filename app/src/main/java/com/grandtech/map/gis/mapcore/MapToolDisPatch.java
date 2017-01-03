package com.grandtech.map.gis.mapcore;

/**
 * Created by zy on 2016/12/6.
 */

public class MapToolDisPatch {

    private static MapToolDisPatch instance = null;

    private MapToolDisPatch(){};

    private synchronized static void syncInit(){
        if(instance==null){
            instance=new MapToolDisPatch();
        }
    }

    public static MapToolDisPatch getInstance(){
        if(instance==null){
            syncInit();
        }
        return instance;
    }
}
