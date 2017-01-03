package com.grandtech.map.gis.maputil;

/**
 * Created by kuchanly on 2016-10-27.
 */


        import java.util.Map;
        import java.util.concurrent.RejectedExecutionException;

        import android.util.Log;

        import com.esri.android.map.TiledServiceLayer;
        import com.esri.core.geometry.Envelope;
        import com.esri.core.geometry.Point;
        import com.esri.core.geometry.SpatialReference;

/**
 *Google地图加载
 */
public class GoogleMapLayer extends TiledServiceLayer {

    private int minLevel = 0;
    private int maxLevel = 19;
    private double[] scales = new double[] { 591657527.591555, 295828763.79577702, 147914381.89788899, 73957190.948944002, 36978595.474472001, 18489297.737236001, 9244648.8686180003, 4622324.4343090001, 2311162.217155, 1155581.108577, 577790.554289, 288895.277144, 144447.638572, 72223.819286, 36111.909643, 18055.954822, 9027.9774109999998, 4513.9887049999998, 2256.994353, 1128.4971760000001 }; private double[] resolutions = new double[] { 156543.03392800014, 78271.516963999937, 39135.758482000092, 19567.879240999919, 9783.9396204999593, 4891.9698102499797, 2445.9849051249898, 1222.9924525624949, 611.49622628138, 305.748113140558, 152.874056570411, 76.4370282850732, 38.2185141425366, 19.1092570712683, 9.55462853563415, 4.7773142679493699, 2.3886571339746849, 1.1943285668550503, 0.59716428355981721, 0.29858214164761665 }; private Point origin = new Point(-20037508.342787, 20037508.342787);

    private int dpi = 96;
    private int tileWidth = 256;
    private int tileHeight = 256;

    private int GoogleMapLayerType;

    public GoogleMapLayer(int layerType) {
        super(true);
        this.GoogleMapLayerType = layerType;
        this.init();
    }

    private void init() {
        try {
            getServiceExecutor().submit(new Runnable() {
                public void run() {
                    GoogleMapLayer.this.initLayer();
                }
            });
        } catch(RejectedExecutionException rejectedexecutionexception) {
            Log.e("Google Map Layer", "initialization of the layer failed.",
                    rejectedexecutionexception);
        }
    }

    @Override
    protected byte[] getTile(int level, int col, int row) throws Exception {
// TODO Auto-generated method stub

//	Log.v(GoogleMapLayer.class.getName(), "level:"+level+" col:"+col+" row:"+row);
        if (level > maxLevel || level < minLevel) {
            return new byte[0];
        }
        String s = "Galileo".substring(0, ((3 * col + row) % 8));
        String url = "";
        switch (GoogleMapLayerType) {
            case GoogleMapLayerTypes.IMAGE_GOOGLE_MAP:
                //http://mt2.google.cn/vt/lyrs=m@225000000&hl=zh-CN&gl=cn&x=420&y=193&z=9&s=Galil
                url = "http://mt" + (col % 4) + ".google.cn/vt/lyrs=s&hl=zh-CN&gl=cn&" + "x=" + col + "&" + "y=" + row + "&" + "z=" + level + "&" + "s=" + s;
                break;
            case GoogleMapLayerTypes.VECTOR_GOOGLE_MAP:
                url = "http://mt" + (col % 4) + ".google.cn/vt/lyrs=m@158000000&hl=zh-CN&gl=cn&" + "x=" + col + "&" + "y=" + row + "&" + "z=" + level + "&" + "s=" + s;
                break;
            case GoogleMapLayerTypes.TERRAIN_GOOGLE_MAP:
                url = "http://mt" + (col % 4) + ".google.cn/vt/lyrs=t@131,r@227000000&hl=zh-CN&gl=cn&" + "x=" + col + "&" + "y=" + row + "&" + "z=" + level + "&" + "s=" + s;
                break;
            case GoogleMapLayerTypes.ANNOTATION_GOOGLE_MAP:
                url = "http://mt" + (col % 4) + ".google.cn/vt/imgtp=png32&lyrs=h@169000000&hl=zh-CN&gl=cn&" + "x=" + col + "&" + "y=" + row + "&" + "z=" + level + "&" + "s=" + s;

//	 url = "http://mt"+ (col % 4) +".google.cn/vt/lyrs=m@256000000&hl=zh-CN&gl=CN&src=app&x=" + col + "&y=" + row + "&z=" + level + "&s=" + s;
                break;
        }
        Log.d(GoogleMapLayer.class.getName(), "url:"+url);
        Map<String, String> map = null;
        return com.esri.core.internal.io.handler.a.a(url, map);
    }

    protected void initLayer() {
        if (getID() == 0L) {
            nativeHandle = create();
            changeStatus(com.esri.android.map.event.OnStatusChangedListener.STATUS .fromInt(-1000));
        } else {
            this.setDefaultSpatialReference(SpatialReference.create(102113));
            this.setFullExtent(new Envelope(-22041257.773878, -32673939.6727517, 22041257.773878, 20851350.0432886));
            this.setTileInfo(new TileInfo(origin, scales, resolutions, scales.length, dpi, tileWidth, tileHeight));
            super.initLayer();
        }
    }

    public interface GoogleMapLayerTypes {

        /** * 谷歌矢量地图服务 ======市政图*/
        final int VECTOR_GOOGLE_MAP = 1;
        /** * 谷歌影像地图服务 ====== 卫星图 */
        final int IMAGE_GOOGLE_MAP = 2;
        /** * 谷歌地形地图服务=======地形图 */
        final int TERRAIN_GOOGLE_MAP = 3;
        /** * 谷歌道路等POI地图服务 ====纯道路图 */
        final int ANNOTATION_GOOGLE_MAP = 4;
    }
}
