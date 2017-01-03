package com.grandtech.map.gis.mapevent;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.MapView;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.ToolPoolFactory;


/**
 * Created by zy on 2016/11/2.
 */

public class OnSingleTapListener implements MapTouchListener.IOnSingleTap {

    private Context context;
    private MapView mapView;

    public OnSingleTapListener(Context context, MapView mapView) {
        this.context=context;
        this.mapView=mapView;
    }

    @Override
    public boolean onSingleTap(MotionEvent point) {
        ITool iTool= ToolPoolFactory.getCurrentMapTriggerTool();
        if(iTool!=null) {
            return iTool.onSingleTap(point);
        }
        return false;
    }

}
