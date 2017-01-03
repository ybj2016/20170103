package com.grandtech.map.gis.mapevent;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;


/**
 * Created by zy on 2016/11/2.
 */

public class MapStatusChangedListener implements OnStatusChangedListener {

    private MapView mapView;
    public MapStatusChangedListener(MapView mapView){
        this.mapView=mapView;
    }

    @Override
    public void onStatusChanged(Object source, STATUS status) {
        if (STATUS.INITIALIZED == status) {

            if (source instanceof MapView) {

            }
        }
        if (STATUS.LAYER_LOADED == status) {
            if (source instanceof ArcGISFeatureLayer) {
                //gdbManager.showProgress(this, false);

            }
        }
    }
}
