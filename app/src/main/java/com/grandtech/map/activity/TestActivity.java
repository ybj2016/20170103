package com.grandtech.map.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.grandtech.map.R;

import java.util.List;

/**
 * Created by zy on 2016/12/2.
 */

public class TestActivity extends Activity {

    Button GPS_btn;
    MapView mMapView ;
    ArcGISTiledMapServiceLayer tileLayer;
    Point point;
    Point wgspoint;
    GraphicsLayer gLayerPos;
    PictureMarkerSymbol locationSymbol;
    LocationManager locMag;
    Location loc ;
    Point mapPoint;
    TextView txtview;
    ZoomControls zoomCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mMapView = (MapView)findViewById(R.id.map);
        tileLayer = new ArcGISTiledMapServiceLayer(
                "http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer");
        mMapView.addLayer(tileLayer);
        zoomCtrl=(ZoomControls)findViewById(R.id.zoomCtrl);
        zoomCtrl.setOnZoomInClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mMapView.zoomin();
            }
        });
        zoomCtrl.setOnZoomOutClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                mMapView.zoomout();
            }
        });
        gLayerPos = new GraphicsLayer();
        mMapView.addLayer(gLayerPos);
        locationSymbol =  new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.tips_error));
        locMag = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        GPS_btn=(Button)findViewById(R.id.GPS_btn);
        GPS_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final List<String> providers=locMag.getProviders(true);
                for(String provider:providers)
                {
                    loc = locMag.getLastKnownLocation(provider);
                    LocationListener locationListener = new LocationListener(){
                        public void onLocationChanged(Location location) {
                            markLocation(location);
                        }
                        public void onProviderDisabled(String arg0)
                        {
                        }
                        public void onProviderEnabled(String arg0)
                        {
                        }
                        public void onStatusChanged(String arg0, int arg1, Bundle arg2)
                        {
                        }
                    };
                    locMag.requestLocationUpdates(provider, 2000, 10, locationListener);
                    if(loc!=null)
                    {
                        markLocation(loc);
                    }
                }
            }
        });
    }
    private void markLocation(Location location)
    {
        gLayerPos.removeAll();
        double locx = location.getLongitude();//经度
        double locy = location.getLatitude();//纬度
        wgspoint = new Point(locx, locy);
        //定位到所在位置
        SpatialReference spatialReference = mMapView.getSpatialReference();
        mapPoint = (Point) GeometryEngine.project(wgspoint, SpatialReference.create(4326),spatialReference);
        Graphic graphic = new Graphic(mapPoint,locationSymbol);
        gLayerPos.addGraphic(graphic);
        mMapView.centerAt(mapPoint, true);
        mMapView.setScale(100);
        mMapView.setMaxResolution(300);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }
    @Override   protected void onResume() {
        super.onResume();
        mMapView.unpause();
    }

}
