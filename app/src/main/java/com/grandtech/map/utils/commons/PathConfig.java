package com.grandtech.map.utils.commons;

import android.os.Environment;

/**
 * Created by zy on 2016/10/18.
 */

public final class PathConfig {

    private PathConfig(){};

    //======================================================ArcgisService服务==================================================================
    //远程基础地图服务地址
    public static final String BASE_MAP_SERVICE_URL = "http://192.168.1.150:6080/arcgis/rest/services/SSZNYDSJ_V5/MapServer";
    //内网 测试
    //public static final String FEATURE_SERVICE_URL = "http://192.168.1.160:6080/arcgis/rest/services/GYNYDSJ/FeatureServer";
    //外网 试用
    public static final String FEATURE_SERVICE_URL = "http://gykj123.cn:2098/arcgis/rest/services/GYNYDSJ/FeatureServer";
    //要素的图层
    public static final int[] FEATURE_SERVICE_LAYER_IDS = {0, 1};

    //本地路径地址
    public static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath().toString();

    public static String E_MAP = "/eMap";

    //存放文件的根目录
    public static String ROOT_FOLDER = ROOT_PATH + E_MAP;
    //存放gdb数据的文件夹
    public static String ROOT_FOLDER_GDB = ROOT_FOLDER + "/gdb";
    //存放卫星影像的文件夹
    public static String ROOT_FOLDER_IMG = ROOT_FOLDER + "/img";
    //存放sqLite数据的文件夹
    public static String ROOT_FOLDER_SQLITE = ROOT_FOLDER + "/sqlite";


    public static final String DATAPATH_GEODATABASE = ROOT_FOLDER_GDB + "/GYNYDSJYX.geodatabase";


    public static final String DATAPATH_RASTERPATH = ROOT_FOLDER_IMG + "/GYNYDSJYX.jp2";

    //投影的空间参考
    public static final int SRID = 4225;

    public static final String SRID_TEXT = "PROJCS[\"CGCS2000_3_Degree_GK_Zone_39\",GEOGCS[\"GCS_China_Geodetic_Coordinate_System_2000\",DATUM[\"D_China_2000\",SPHEROID[\"CGCS2000\",6378137.0,298.257222101]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",39500000.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",117.0],PARAMETER[\"Scale_Factor\",1.0],PARAMETER[\"Latitude_Of_Origin\",0.0],UNIT[\"Meter\",1.0],AUTHORITY[\"EPSG\",4527]]";
    //要操作gdb的表名
    public static final String TBNAME = "ZZDK";

    //要批量修改的字段名称
    public static final String ZZMC = "ZZMC";

    public static final String DCR = "DCR";

    public static final String DCSJ = "DCSJ";

    //===================================================================自己的服务地址===================================================================
    //public static final String MOBILE_SERVICE_BASE = "http://192.168.11.101:8080/EstateServiceSys/";

    //===================================================================公网的服务地址===================================================================
    public static final String MOBILE_SERVICE_BASE = "http://gykj123.cn:8588/EstateServiceSys/";
    //新用户注册地址
    public static final String MOBILE_SERVICE_USER_REGISTER = MOBILE_SERVICE_BASE + "MobileController/userRegister";
    //
    public static final String MOBILE_SERVICE_USER_LOGIN = MOBILE_SERVICE_BASE + "MobileController/userLogin";
}
