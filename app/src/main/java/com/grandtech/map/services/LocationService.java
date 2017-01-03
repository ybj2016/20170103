package com.grandtech.map.services;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.core.geometry.Point;
import com.grandtech.map.activity.TrackActivity;
import com.grandtech.map.dao.TrackDao;
import com.grandtech.map.entity.Track;
import com.grandtech.map.utils.commons.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by zy on 2016/11/25.
 */

public class LocationService extends BaseFrontService {

    public static final String TAG = "LocationService";
    private static final int interval = 5000;
    private TextView postionView;
    private LocationManager locationManager;
    private String locationProvider;
    MyLocationListener myLocationListener;

    /**
     * 服务创建，只会执行一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "创建了服务A");
    }

    /**
     * 服务每次启动都会执行
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "开启了服务A");
        initLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 停止服务时执行
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(myLocationListener);
        Intent intent = new Intent();
        Log.d(TAG, "关闭了服务A");
        intent.setAction(TrackActivity.BR_INTENT);
        intent.putExtra("locServiceRun", false);//发送给LocationManager
        sendBroadcast(intent);//发送广播
    }


    private void initLocation() {
        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if(providers.contains(LocationManager.GPS_PROVIDER)){
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else{
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return ;
        }
        //获取Location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location!=null){
            //不为空,显示地理位置经纬度
             myLocationListener = new MyLocationListener();
            myLocationListener.onLocationChanged(location);

        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, myLocationListener);
    }
    /**
     * 显示地理位置经度和纬度信息
     * @param location
     */
    private void showLocation(Location location){
        //第一次保存
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    public class MyLocationListener implements LocationListener {
        private LocationManager locationManager;
        private Track track;
        private int count = 0;
        private TrackDao trackDao;
        private Context context;
        private Intent intent;
        private String provider;
        private MyLocationListener() {
            context = LocationService.this;
            trackDao = new TrackDao(context);
            track = new Track();
            track.setcCount(0);
            track.setcTrackName("轨迹");
            track.setcDesc("测试");
            track.setcStartTime(DateUtil.dateTimeToStr(new Date()));
            intent = new Intent();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            if(2.2636<location.getLatitude()&&location.getLatitude()<53.5228&&73.6170<location.getLongitude()&&location.getLongitude()<135.1790){
                if (count == 0) {
                    track.setcCoordinates(String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()));
                    track.setcEndTime(DateUtil.dateTimeToStr(new Date()));
                    track.setcCount(count + 1);
                    track.setcId(trackDao.saveTrack(track));//首次保存
                }
                count++;
                //没1个点存一次 节约开支
                if (count % 1 == 0) {
                    String cCoordinates = trackDao.getRecordById(track.getcId()).getcCoordinates();
                    track.setcCoordinates(cCoordinates+";"+String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()));
                    track.setcEndTime(DateUtil.dateTimeToStr(new Date()));
                    track.setcCount(count + 1);
                    trackDao.update(track);
                }
            }else{
                return;
            }
            Point locPoint = new Point(location.getLongitude(), location.getLatitude());
            intent.setAction(TrackActivity.BR_INTENT);
           // intent.setAction(MapActivity.BR_INTENT);
            intent.putExtra("locServiceRun", true);//发送给LocationManager
            intent.putExtra("locPointCount", count);//发送给TrackActivity
            intent.putExtra("locPoint", locPoint);//发送给LocationManager
            sendBroadcast(intent);//发送广播
            //Toast.makeText(context,"发送广播",500).show();

        }
    }
}
