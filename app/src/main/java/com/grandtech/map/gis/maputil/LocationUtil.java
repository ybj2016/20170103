package com.grandtech.map.gis.maputil;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;


/**
 * Created by kuchanly on 2016-10-19.
 * 坐标定位通用类
 */

public class LocationUtil {
    public static final String TAG = "LocationUtil";

    /**
     * ESRI位置定位方法
     */
    public static void locationStart(MapView map) {
        try {

            LocationDisplayManager locationDisplayManager = map.getLocationDisplayManager();
            locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
            if (!locationDisplayManager.isStarted())
                locationDisplayManager.start();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public static boolean checkGps(Context paramContext) {
        LocationManager localLocationManager = (LocationManager) paramContext
                .getSystemService(Context.LOCATION_SERVICE);
        return localLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);//
    }

    public static boolean checkWifi(Context paramContext) {
        WifiManager localWifiManager = (WifiManager) paramContext
                .getSystemService(Context.WIFI_SERVICE);
        return localWifiManager.isWifiEnabled();
    }

    public static void setWifiEnable(Context paramContext, boolean paramBoolean) {
        WifiManager localWifiManager = (WifiManager) paramContext
                .getSystemService(Context.WIFI_SERVICE);
        localWifiManager.setWifiEnabled(paramBoolean);
    }

    public static boolean checkInternet(Context paramContext) {
        ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (localConnectivityManager == null) {
            return false;
        }
        NetworkInfo[] arrayOfNetworkInfo = localConnectivityManager
                .getAllNetworkInfo();
        if (arrayOfNetworkInfo != null) {
            for (int i = 0; i < arrayOfNetworkInfo.length; i++) {
                if (arrayOfNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isValidLocation(Location location) {
        return location != null && Math.abs(location.getLatitude()) <= 90
                && Math.abs(location.getLongitude()) <= 180;
    }

}
