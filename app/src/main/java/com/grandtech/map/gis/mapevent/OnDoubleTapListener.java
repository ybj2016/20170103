package com.grandtech.map.gis.mapevent;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.MapView;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.ToolPoolFactory;


/**
 * Created by zy on 2016/11/9.
 */

public class OnDoubleTapListener implements MapTouchListener.IOnDoubleTap {

    private Context context;
    private MapView mapView;

    public OnDoubleTapListener(Context context, MapView mapView) {
        this.context = context;
        this.mapView = mapView;
    }

    @Override
    public boolean onDoubleTap(MotionEvent point) {
        ITool iTool= ToolPoolFactory.getCurrentMapTriggerTool();
        if(iTool!=null) {
            return iTool.onDoubleTap(point);
        }
        return false;
    }
}
