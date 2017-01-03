package com.grandtech.map.gis.maputil;

import android.content.Context;
import android.util.Log;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.RasterLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.raster.FileRasterSource;
import com.grandtech.map.activity.SettingActivity;
import com.grandtech.map.entity.LayerInfo;
import com.grandtech.map.utils.commons.FileHelper;
import com.grandtech.map.utils.commons.PathConfig;
import com.grandtech.map.utils.commons.SharedPreferencesHelper;
import com.grandtech.map.utils.enmus.EnumLayerType;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by kuchanly on 2016-10-18.
 * 针对地图对象的功能封装，增加图层等信息；
 */

public class LayerUtil {
    private static final String TAG = "LayerUtil";
    private static final String BASE_IMAGE_URL = "http://gykj123.cn:2098/arcgis/rest/services/HKSSZ/SSYX/MapServer";
    private static final String BASE_LOCALPATH = "http://gykj123.cn:2098/arcgis/rest/services/HKSSZ/SSYX/MapServer";

    /*
    * 向地图对象里面增加图层对象。
    * */
    public static void addLayer(MapView map, Layer layer) {

        try {
            if (map != null && layer != null)
                map.addLayer(layer);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    /*
       * 向地图对象里面增加图层对象,根据图层地址或类别进行增加。
       * */
    public static void addLayer(MapView map, String url, EnumLayerType layerType) {
        try {
            Layer layer = null;
            switch (layerType.getValue()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    layer = new ArcGISTiledMapServiceLayer(LayerUtil.BASE_IMAGE_URL);
                default:

            }
            LayerUtil.addLayer(map, layer);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    /*
  * 向地图对象里面增加背景影像图。
  * */
    public static void addBaseLayer(MapView map) {
        try {
            Layer layer = new ArcGISTiledMapServiceLayer(LayerUtil.BASE_IMAGE_URL);
            LayerUtil.addLayer(map, layer);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }

    }

    /*
      * 向地图对象里面增加背景影像图。
      * */

    /**
     * 读取Geodatabase中离线地图信息
     *
     * @param geodatabsePath 离线Geodatabase文件路径
     */
    public static void addLocalGdbFeatureLayer(String geodatabsePath, MapView map) {
        Geodatabase localGdb = null;
        try {
            localGdb = new Geodatabase(geodatabsePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // layerInfoList = new ArrayList<>();
        // 添加FeatureLayer到MapView中
        if (localGdb != null) {
            int flag = 0;
            for (GeodatabaseFeatureTable gdbFeatureTable : localGdb.getGeodatabaseTables()) {
                if (gdbFeatureTable.hasGeometry() && flag == 1) {
                    FeatureLayer layer = new FeatureLayer(gdbFeatureTable);
                    layer.setEnableLabels(true);
                    map.addLayer(layer);
                }
                flag++;
            }
        }
    }

    public static FeatureLayer getFeatureLayerForGdb(String gdbPath,int index) {
        if (FileHelper.isExist(gdbPath)) {
            Geodatabase localGdb = null;
            FeatureLayer singleLayer = null;
            FeatureLayer layer = null;
            try {
                localGdb = new Geodatabase(gdbPath);//程序崩掉是这里原因
                if (localGdb != null) {
                    int flag = 0;
                    for (GeodatabaseFeatureTable gdbFeatureTable : localGdb.getGeodatabaseTables()) {
                        if (gdbFeatureTable.hasGeometry() && flag == index) {
                            layer = new FeatureLayer(gdbFeatureTable);
                            layer.setEnableLabels(true);
                            singleLayer = layer;
                            break;
                        }
                        flag++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return singleLayer;
        }
        return null;
    }

    /**
     * 读取Geodatabase中离线地图信息
     *
     * @param gdbPath 离线Geodatabase文件路径
     */
    public static FeatureLayer addGdbFeatureLayer(Context context, String gdbPath, MapView map) {
        if (FileHelper.isExist(gdbPath)) {
            Geodatabase localGdb = null;
            FeatureLayer singleLayer = null;
            FeatureLayer layer = null;
            try {
                localGdb = new Geodatabase(gdbPath);//程序崩掉是这里原因
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 添加FeatureLayer到MapView中
            if (localGdb != null) {
                int flag = 0;
                for (GeodatabaseFeatureTable gdbFeatureTable : localGdb.getGeodatabaseTables()) {
                    if (gdbFeatureTable.hasGeometry()) {
                        layer = new FeatureLayer(gdbFeatureTable);
                        layer.setEnableLabels(true);
                        if(layer.getName().equals("ZZDK")) {
                            singleLayer = layer;
                            Object layerOpacity = SharedPreferencesHelper.readObject(context, SettingActivity.LAYER_OPACITY);
                            if(layerOpacity!=null) {
                                layer.setOpacity(new Float(layerOpacity.toString()));
                            }
                        }
                        map.addLayer(layer);
                    }
                    flag++;
                }
            }
            return singleLayer;
        } else {
            return null;
        }
    }

    public static void addLocalGdbFeatureLayer(String geodatabsePath, MapView mapView, List<LayerInfo> layersInfo) {

        for (Layer layer : mapView.getLayers()) {//先移除当前mapview上的要素图层或瓦片图层
            if (layer instanceof ArcGISFeatureLayer || layer instanceof ArcGISTiledMapServiceLayer || layer instanceof FeatureLayer)
                mapView.removeLayer(layer);
        }
        Geodatabase localGdb = null;
        try {
            localGdb = new Geodatabase(geodatabsePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 添加FeatureLayer到MapView中
        if (localGdb != null) {
            int k = 0;
            for (GeodatabaseFeatureTable gdbFeatureTable : localGdb.getGeodatabaseTables()) {
                if (layersInfo != null) {
                    if (gdbFeatureTable.hasGeometry() && layersInfo.get(k).getLayerPath().equals(gdbFeatureTable.getGeodatabase().getPath()) && layersInfo.get(k).getLayerSelected() == LayerInfo.LAYER_SELECTED) {
                        FeatureLayer layer = new FeatureLayer(gdbFeatureTable);
                        layer.setEnableLabels(true);
                        mapView.addLayer(layer);
                    }
                    k++;
                } else {
                    System.out.println("初始化图层数据失败！！");
                }
            }
        }
    }



     /*
      * 向地图对象里面增加背景影像图。
      * */

    /**
     * 读取rasterPath中离线地图信息
     *
     * @param rasterPath 离线raster文件路径
     */
    public static void addLocalImagesLayer(String rasterPath, MapView map, boolean asOperationalLayer) {
        if (FileHelper.isExist(rasterPath)) {
            RasterLayer rasterLayer = null;
            try {
                FileRasterSource rasterSource = new FileRasterSource(rasterPath);
                if (asOperationalLayer) {
                    rasterSource.project(map.getSpatialReference());
                }
                rasterLayer = new RasterLayer(rasterSource);
                map.addLayer(rasterLayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
