package com.grandtech.map.gis.mapmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.grandtech.map.R;
import com.grandtech.map.dao.JzzbDao;
import com.grandtech.map.entity.Jzzb;
import com.grandtech.map.gis.mapcore.DrawCore;
import com.grandtech.map.utils.commons.DialogHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.widget.Toast.makeText;


/**
 * Created by fs on 2016/12/28.
 */

public class LocationManagerV1 extends DrawCore {

    private Context context;
    private MapView mapView;

    private MyLocationListener myLocationListener = new MyLocationListener();//定位监听
    private android.location.LocationManager locationManager1;//地理位置管理器
    private String locationProvider = null;//判断定位方式
    private Point pt1 = null;//定位点
    private Point pt2 = new Point();//转换点
    private MarkerSymbol markerSymbol;//点样式
    private MarkerSymbol markerSymbol1;//点样式
    private Drawable image;//图片
    private PictureMarkerSymbol symbol;
    private AlertDialog.Builder dialog;//弹窗
    private GraphicsLayer graphicsLayer1 = new GraphicsLayer();//动态图层
    private Timer timer;//延时
    private Double cX;//x坐标差值
    private Double cY;//y坐标差值
    private Point point;//长按时屏幕坐标转换的投影坐标
    private String sql;//修改或插入sql
    private JzzbDao jzzbDao = new JzzbDao(context);
    private Jzzb jzzb;
    private Boolean bl;

    /**
     * 规定其子类必须实现的构造方法
     *
     * @param context
     */
    public LocationManagerV1(Context context) {
        super(context);
        this.context = context;
        this.mapView = mapContext.getMapView();
        onCreate(context);
    }

    @Override
    public void onCreate(Context context) {

        onReady();
    }

    @Override
    public void onReady() {

    }

    public void onDestroy() {

    }
    //定位 fs
    public void loctSart() {
        //获取地理位置管理器
        locationManager1 = (android.location.LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager1.getProviders(true);
        if (providers.contains(android.location.LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = android.location.LocationManager.GPS_PROVIDER;
        } else if (providers.contains(android.location.LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = android.location.LocationManager.NETWORK_PROVIDER;
        } else {
            return ;
        }
        image = context.getResources().getDrawable(R.mipmap.location);
        symbol = new PictureMarkerSymbol(image);
        //markerSymbol = new SimpleMarkerSymbol(rgb(30, 144, 255), 18, SimpleMarkerSymbol.STYLE.CIRCLE);
        //markerSymbol1 = new SimpleMarkerSymbol(Color.WHITE, 22, SimpleMarkerSymbol.STYLE.CIRCLE);
        locationManager1.requestLocationUpdates(locationProvider, 1000, 1, myLocationListener);
        timer = new Timer();
        locationManager1.addGpsStatusListener(statusListener);//设置卫星监听
        jzzb = jzzbDao.getT(null);
        //jzzb.setCoordinates();
        if (jzzb == null) {
            bl=false;
            cX = 0.0;
            cY = 0.0;
        } else {
            bl=true;
            String coordinate = jzzb.getCoordinates();
            String[] coordinates = coordinate.split(",");
            cX = Double.parseDouble(coordinates[0]);
            cY = Double.parseDouble(coordinates[1]);
        }
        mapView.addLayer(graphicsLayer1);
    }

    //卫星监听 fs
    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
            GpsStatus status = locationManager1.getGpsStatus(null); // 取当前状态
            updateGpsStatus(event, status);
        }
    };

    //查询卫星数量 fs
    private void updateGpsStatus(int event, GpsStatus status) {
        if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            //numSatelliteList.clear();
            int count = 0;
            while (it.hasNext() && count <= maxSatellites) {
                GpsSatellite s = it.next();
                if (s.getSnr() != 0)//只有信躁比不为0的时候才算搜到了星
                {
                    // numSatelliteList.add(s);
                    count++;
                }
                //Toast.makeText(mContext, "当前星颗数="+count, Toast.LENGTH_SHORT).show();
            }
            if (count < 4) {
                final Toast tst2 = makeText(context, "当前gps信号差！", Toast.LENGTH_SHORT);
                tst2.show();
                timer.schedule(new TimerTask() {
                    public void run() {
                        tst2.cancel();
                    }
                }, 2000);

            }
        }
    }

    //定位监听 fs
    public class MyLocationListener implements LocationListener {
        private MyLocationListener() {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    //Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    //Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    // Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            graphicsLayer1.removeAll();
        }

        @Override
        public void onLocationChanged(Location location) {
            if (2.2636 < location.getLatitude() && location.getLatitude() < 53.5228 && 73.6170 < location.getLongitude() && location.getLongitude() < 135.1790) {
                if (pt1 != null) {
                    graphicsLayer1.removeAll();
                }
                if (location != null) {
                    pt1 = (Point) GeometryEngine.project(location.getLongitude(), location.getLatitude(), mapView.getSpatialReference());
                    pt2.setXY(pt1.getX() + cX, pt1.getY() + cY);
                    Graphic gp = new Graphic(pt2, symbol);
                   // Graphic gp1 = new Graphic(pt2, markerSymbol1);
                    //graphicsLayer1.addGraphic(gp1);
                    graphicsLayer1.addGraphic(gp);
                    //mapView..addLayer(graphicsLayer1);
                }
            } else {
                return;
            }
        }
    }

    //设置基本点 fs
    public void setupLoc() {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("提示")
                .setContentText("选择地图上你当前的真实位置长按!")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mapView.setOnLongPressListener(new OnLongPressListener() {
                            @Override
                            public boolean onLongPress(float x, float y) {
                                point = mapView.toMapPoint(x, y);
                                String a = null;
                                if ( pt1 == null) {
                                    Toast tst = makeText(context, "当前gps信号弱无法设置基准定位！", Toast.LENGTH_SHORT);
                                    tst.show();
                                    return false;
                                }
                                a = String.valueOf(point.getX() - pt1.getX()) + "," + String.valueOf(point.getY() - pt1.getY());
                                jzzb = new Jzzb();
                                jzzb.setCoordinates(a);
                                getAlert();
                                return true;
                            }
                        });
                    }
                }).show();
    }
    //重置定位
    public void resetLoc() {
        if (jzzbDao.delete(null)) {
            cX = 0.0;
            cY = 0.0;
            Toast a = Toast.makeText(context, "重置成功！", Toast.LENGTH_SHORT);
            a.show();
            bl=false;
        }

    }

    private void getAlert() {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("提示")
                .setContentText("设置成功，是否保存！")
                .setCancelText("取消")
                .setConfirmText("保存")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        if (bl) {
                            jzzbDao.update(jzzb);
                        } else {
                            jzzbDao.save(jzzb);
                            bl=true;
                        }
                        cX = point.getX() - pt1.getX();
                        cY = point.getY() - pt1.getY();
                        mapView.setOnLongPressListener(null);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }

    @Override
    public int triggerMode() {
        return 0;
    }

}

