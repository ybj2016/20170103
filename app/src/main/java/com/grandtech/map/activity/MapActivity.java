package com.grandtech.map.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.table.TableException;
import com.grandtech.map.R;
import com.grandtech.map.entity.MobileUser;
import com.grandtech.map.gis.mapcore.ITool;
import com.grandtech.map.gis.mapcore.MapContext;
import com.grandtech.map.gis.mapcore.ToolPoolFactory;
import com.grandtech.map.gis.mapevent.MapStatusChangedListener;
import com.grandtech.map.gis.mapevent.MapTouchListener;
import com.grandtech.map.gis.mapevent.OnDoubleTapListener;
import com.grandtech.map.gis.mapevent.OnSingleTapListener;
import com.grandtech.map.gis.mapmanager.LocationManager;
import com.grandtech.map.gis.mapmanager.LocationManagerV1;
import com.grandtech.map.gis.mapmanager.TrackManager;
import com.grandtech.map.gis.maptools.AreaCutTool;
import com.grandtech.map.gis.maptools.ClearHandleTool;
import com.grandtech.map.gis.maptools.DeleteFeatureTool;
import com.grandtech.map.gis.maptools.DrawPointTool;
import com.grandtech.map.gis.maptools.DrawPolygonTool;
import com.grandtech.map.gis.maptools.DrawPolylineTool;
import com.grandtech.map.gis.maptools.DrawUnDoTool;
import com.grandtech.map.gis.maptools.EditAttributeTool;
import com.grandtech.map.gis.maptools.EditGraphicTool;
import com.grandtech.map.gis.maptools.EditSaveTool;
import com.grandtech.map.gis.maptools.Operation;
import com.grandtech.map.gis.maptools.SelectFeatureTool;
import com.grandtech.map.gis.maptools.SelectFeatureToolV1;
import com.grandtech.map.gis.maputil.LayerUtil;
import com.grandtech.map.utils.commons.DateUtil;
import com.grandtech.map.utils.commons.DialogHelper;
import com.grandtech.map.utils.commons.EventHelper;
import com.grandtech.map.utils.commons.FileHelper;
import com.grandtech.map.utils.commons.MessageConfig;
import com.grandtech.map.utils.commons.NotificationUtil;
import com.grandtech.map.utils.commons.PathConfig;
import com.grandtech.map.utils.commons.SharedPreferencesHelper;
import com.grandtech.map.utils.commons.VibratorHelper;
import com.grandtech.map.utils.enmus.EnumGDBTask;
import com.grandtech.map.view.checkimageview.CheckImageView;
import com.grandtech.map.view.dropEditText.AttrsEditComponents;

import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by zy on 2016/11/2.
 */

public class MapActivity extends Activity implements CallbackListener<FeatureResult>, DeleteFeatureTool.ICallBack, SelectFeatureToolV1.ICallBack, View.OnClickListener, AreaCutTool.ICallBack, CheckImageView.OnCheckedListener, AttrsEditComponents.IEditAttr {

    //
    public static final String BR_INTENT = "com.grandtech.map.activity.MapActivity";
    //MapContext
    private MapContext mapContext;
    //地图事件处理类
    private MapTouchListener mapTouchListener;
    //地图状态监听类
    private MapStatusChangedListener mapStatusChangedListener;
    //绘制多边形
    private DrawPolygonTool drawPolygonTool;
    //绘制线工具
    private DrawPolylineTool drawPolylineTool;
    //绘制点工具
    private DrawPointTool drawPointTool;
    //回撤工具
    private DrawUnDoTool drawUnDoTool;
    //编辑工具
    private EditGraphicTool editGraphicTool;
    //编辑属性工具
    private EditAttributeTool editAttributeTool;
    //清空所有操作
    private ClearHandleTool clearHandleTool;
    //保存工具
    private EditSaveTool editSaveTool;
    //多选工具
    private SelectFeatureTool selectFeatureTool;
    //多项工具-专业定制
    private SelectFeatureToolV1 selectFeatureToolV1;
    //删除
    private DeleteFeatureTool deleteFeatureTool;
    //分割
    private AreaCutTool areaCutTool;
    //操作测试类
    private Operation operation;
    //地图轨迹类
    private TrackManager trackManager;

    private LocationManager locationManager;

    //批量修改属性控件
    private AttrsEditComponents attrsEditComponents;
    //通知栏工具类
    private NotificationUtil notificationUtil;
    //=================================================
    private ImageView imgzhinan;
    private CheckImageView ivSelect, ivAreaCut, ivDelete, ivDrawPolygon;
    private SensorManager manager;//获得管理
    private MapView mapView;
    private SensorListener listener = new SensorListener();
    private View view_compass;
    private View view_bottom;
    private TextView tv_scale;
    private LocationManagerV1 locationManagerV1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initialize();//初始化控件
        registerEvent();//注册点击事件
        initMap();//初始化地图
        initMapEvent();
        initTools();
        getscale();

        locationManagerV1.loctSart();
    }


    //获取比例尺
    private void getscale() {
        mapView.setOnZoomListener(new OnZoomListener() {

            @Override
            public void preAction(float arg0, float arg1, double arg2) {
            }

            @Override
            public void postAction(float arg0, float arg1, double arg2) {
                int i = (new Double(mapView.getScale())).intValue();//比例
                tv_scale.setText("1:" + i); //获取比例尺视图
            }
        });
    }

    //寻找控件
    private void initialize() {
        GdbDownActivity.handler = handler;
        GdbSyncActivity.handler = handler;
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);//获得管理
        notificationUtil = new NotificationUtil(this, R.drawable.gykj);
        attrsEditComponents = new AttrsEditComponents(this, this);
        view_bottom = LayoutInflater.from(this).inflate(R.layout.activity_map_bottom, null);
        view_compass = LayoutInflater.from(this).inflate(R.layout.activity_map_compass, null);
        mapView = (MapView) findViewById(R.id.mapView);
        imgzhinan = (ImageView) view_compass.findViewById(R.id.img_zhinan);
        ivSelect = (CheckImageView) view_bottom.findViewById(R.id.ivSelect);
        ivAreaCut = (CheckImageView) view_bottom.findViewById(R.id.ivAreaCut);
        ivDelete = (CheckImageView) view_bottom.findViewById(R.id.ivDelete);
        ivDrawPolygon = (CheckImageView) view_bottom.findViewById(R.id.ivDrawPolygon);
        tv_scale = (TextView) view_bottom.findViewById(R.id.tv_scale);
        mapView.addView(attrsEditComponents.getAttrsEditComponentsView());
        mapView.addView(view_compass);
        mapView.addView(view_bottom);
    }

    //初始化地图
    private void initMap() {
        LayerUtil.addLocalImagesLayer(PathConfig.DATAPATH_RASTERPATH, mapView, false);//加载地图
        FeatureLayer featureLayer = LayerUtil.addGdbFeatureLayer(this, PathConfig.DATAPATH_GEODATABASE, mapView);
        mapView.setMinScale(50000000);
        mapView.setMaxScale(100);
        //设置当前操作的图层
        mapContext = MapContext.getInstance(mapView);
        mapContext.setFeatureLayer(featureLayer);

    }

    private void initMapEvent() {
        mapTouchListener = new MapTouchListener(this, mapView);//地图监听
        mapTouchListener.setiOnSingleTap(new OnSingleTapListener(this, mapView));//设置地图点击事件
        mapTouchListener.setiOnDoubleTap(new OnDoubleTapListener(this, mapView));//设置地图双击事件
        mapStatusChangedListener = new MapStatusChangedListener(mapView);
        mapView.setOnTouchListener(mapTouchListener);
        mapView.setOnStatusChangedListener(mapStatusChangedListener);
    }

    //添加工具
    private void initTools() {
        drawPointTool = new DrawPointTool(this);
        drawPolygonTool = new DrawPolygonTool(this);
        drawPolylineTool = new DrawPolylineTool(this);
        drawUnDoTool = new DrawUnDoTool(this);
        editGraphicTool = new EditGraphicTool(this);
        editAttributeTool = new EditAttributeTool(this);
        editAttributeTool.setResultCallbackListener(this);//结果回调监听
        clearHandleTool = new ClearHandleTool(this);
        editSaveTool = new EditSaveTool(this);
        selectFeatureTool = new SelectFeatureTool(this);
        selectFeatureToolV1 = new SelectFeatureToolV1(this);
        deleteFeatureTool = new DeleteFeatureTool(this);
        areaCutTool = new AreaCutTool(this);
        operation = new Operation(this);
        trackManager = new TrackManager(this);
        locationManager = new LocationManager(this);
        locationManagerV1 = new LocationManagerV1(MapActivity.this);
        deleteFeatureTool.setICallBack(this);
        selectFeatureToolV1.setICallBack(this);
        areaCutTool.setICallBack(this);
        operation.setICallBack(this);
        ToolPoolFactory.addTool(drawPolygonTool);//0
        ToolPoolFactory.addTool(drawPolylineTool);//1
        ToolPoolFactory.addTool(drawUnDoTool);//2
        ToolPoolFactory.addTool(drawPointTool);//3
        ToolPoolFactory.addTool(editGraphicTool);//4
        ToolPoolFactory.addTool(editAttributeTool);//5
        ToolPoolFactory.addTool(clearHandleTool);//6
        ToolPoolFactory.addTool(editSaveTool);//7
        ToolPoolFactory.addTool(selectFeatureTool);//8
        ToolPoolFactory.addTool(selectFeatureToolV1);//9
        ToolPoolFactory.addTool(deleteFeatureTool);//10
        ToolPoolFactory.addTool(areaCutTool);//11
        ToolPoolFactory.addTool(operation);//12
        ToolPoolFactory.addTool(trackManager);//13
    }

    //注册点击事件
    private void registerEvent() {
        ivSelect.setOnCheckedListener(this);
        ivDrawPolygon.setOnCheckedListener(this);
        ivAreaCut.setOnCheckedListener(this);
        ivDelete.setOnClickListener(this);
        imgzhinan.setKeepScreenOn(true);//保持屏幕常亮
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = ((EnumGDBTask) msg.obj).getDesc();
            switch (msg.arg1) {
                case 0:
                    FeatureResult featureResult = (FeatureResult) msg.obj;
                    break;
                case MessageConfig.DOWN_GDB_BEFORE:
                    notificationUtil.showProgressNotify(0, "开始下载", "进度:", message);
                    break;
                case MessageConfig.DOWN_GDB_PROCESSING:
                    notificationUtil.getNotificationManager().notify(0, notificationUtil.getBuilder().setContentText(message).build());
                    break;
                case MessageConfig.DOWN_GDB_ERROR:
                    notificationUtil.getNotificationManager().notify(0, notificationUtil.getBuilder().setContentText(message).setProgress(100, 0, false).build());
                    break;
                case MessageConfig.DOWN_GDB_SUCCESS:
                    notificationUtil.getNotificationManager().notify(0, notificationUtil.getBuilder().setContentText(message).setProgress(100, 100, false).build());
                    VibratorHelper.Vibrate(MapActivity.this, 250);//震动提示
                    if (FileHelper.isExist(PathConfig.DATAPATH_GEODATABASE)) {
                        mapView.removeLayer(1);
                        FeatureLayer featureLayer = LayerUtil.addGdbFeatureLayer(MapActivity.this, PathConfig.DATAPATH_GEODATABASE, mapView);
                        mapContext.setMapView(mapView);
                        mapContext.setFeatureLayer(featureLayer);
                    }
                    break;
                //==============================================================================================
                case MessageConfig.SYNC_GDB_BEFORE:
                    if(msg.what!=MessageConfig.SYNC_GDB_HANDLER) {
                        notificationUtil.showProgressNotify(1, "开始同步", "进度:", message);
                    }else {
                        notificationUtil.getNotificationManager().notify(1, notificationUtil.getBuilder().setContentText("已批量写入" + msg.arg2 + "条数据！").build());
                    }
                    break;
                case MessageConfig.SYNC_GDB_PROCESSING:
                    notificationUtil.getNotificationManager().notify(1, notificationUtil.getBuilder().setContentText(message).build());
                    break;
                case MessageConfig.SYNC_GDB_ERROR:
                    notificationUtil.getNotificationManager().notify(1, notificationUtil.getBuilder().setContentText(message).setProgress(100, 0, false).build());
                    break;
                case MessageConfig.SYNC_GDB_SUCCESS:
                    notificationUtil.getNotificationManager().notify(1, notificationUtil.getBuilder().setContentText(message).setProgress(100, 100, false).build());
                    VibratorHelper.Vibrate(MapActivity.this, 250);//震动提示
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    @Override
    protected void onPause() {
        manager.unregisterListener(listener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapContext.recycle();//非常非常非常非常非常非常非常非常非常非常重要
        mapTouchListener.stopListen();
        locationManager.onDestroy();
        trackManager.onDestroy();
        ToolPoolFactory.clearTools();
        super.onDestroy();
        System.gc();
    }

    //加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //选定菜单子项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //判断各项ID
        Intent intent;
        switch (item.getItemId()) {
            case R.id.download:
                intent = new Intent(this, GdbDownActivity.class);
                startActivity(intent);
                break;
            case R.id.synchronization:
                intent = new Intent(this, GdbSyncActivity.class);
                startActivity(intent);
                break;
            case R.id.setup:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.setup_loc:
                locationManagerV1.setupLoc();
                break;
            case R.id.reset_loc:
                locationManagerV1.resetLoc();
                break;
            case R.id.PlotStatistics:
                intent = new Intent(this, PlotStatisticsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //查询结果的监听回调
    @Override
    public void onCallback(FeatureResult featureResult) {
        handler.obtainMessage(0, MessageConfig.QUERY_SUCCESS, 0, featureResult).sendToTarget();
        editAttributeTool.setIsSearchData(false);//设置结束搜索
    }

    @Override
    public void onError(Throwable throwable) {
        editAttributeTool.setIsSearchData(false);//设置结束搜索
        handler.obtainMessage(0, MessageConfig.QUERY_SUCCESS, 0, throwable).sendToTarget();
    }

    //删除操作回调监听
    @Override
    public boolean beforeDelete(int count) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("提示")
                .setContentText("确认删除这" + count + "吗？")
                .setCancelText("取消")
                .setConfirmText("删除")
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
                        ITool iToolBtn = ToolPoolFactory.getCurrentBtnTriggerTool();
                        if (iToolBtn != null) {
                            iToolBtn.operating(null);
                        }
                    }
                }).show();
        return true; //true表示继续执行删除；false表示取消执行删除
    }

    @Override
    public void deleteFail(Exception e) {
        System.out.println(e.toString());
    }

    @Override
    public void deleteSuccess() {
        System.out.println("删除成功");
    }

    //分割面操作回调监听
    @Override
    public boolean beforeCut(int count) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认分割这" + count + "个要素吗？");
        builder.setTitle("提示");
        DialogHelper.dialog(builder, "确认", "取消", new DialogHelper.ICallBack() {
            @Override
            public void dialogOk() {
                ITool iToolMap = ToolPoolFactory.getCurrentMapTriggerTool();//Unix时间戳
                if (iToolMap != null) {
                    iToolMap.operating(null);//执行分割
                    EventHelper.simulateTouch(ivSelect);//模拟一次触摸事件
                }
            }

            @Override
            public void dialogCancel() {
            }
        });
        return false; //true表示继续执行删除；false表示取消执行删除
    }

    @Override
    public void cutFail(Throwable e) {
        System.out.println("》》》》》》》》》》》》》" + e.getMessage());
    }

    @Override
    public void cutSuccess() {
        ToolPoolFactory.currentToolIndex_Map = 9;//选择工具置为选择
    }

    //批量编辑模块回调监听
    @Override
    public void editAttr(String name) {
        try {
            GeodatabaseFeatureTable gftable = null;
            Map<String, Object> atrrMap = null;
            if (features == null) return;
            MobileUser mobileUser = (MobileUser) SharedPreferencesHelper.readObject(this, LoginActivity.USER_ACCOUNT);
            String collectName = null;
            if (mobileUser != null) {
                collectName = mobileUser.getName();
            }
            for (int i = 0; i < features.size(); i++) {
                GeodatabaseFeature geoFeature = (GeodatabaseFeature) features.get(i);
                gftable = (GeodatabaseFeatureTable) geoFeature.getTable();
                atrrMap = geoFeature.getAttributes();
                atrrMap.put(PathConfig.ZZMC, name);//种植名称
                atrrMap.put(PathConfig.DCR, collectName);
                atrrMap.put(PathConfig.DCSJ, DateUtil.getUTCTimeStr());
                gftable.updateFeature(features.get(i).getId(), atrrMap);
            }
        } catch (TableException e) {
            e.printStackTrace();
        }
    }

    private List<Feature> features;

    @Override
    public void startEdit(List<Feature> features) {
        this.features = features;
        System.out.println("批量编辑开始");
    }

    @Override
    public void endEdit() {
        this.features = null;
        System.out.println("批量编辑结束");
    }


    //底部工具条事件监听
    @Override
    public void checked(View v) {
        switch (v.getId()) {
            case R.id.ivSelect:
                ToolPoolFactory.currentToolIndex_Btn = 6;//清空地图
                ToolPoolFactory.currentToolIndex_Map = 9;
                attrsEditComponents.getAttrsEditComponentsView().setVisibility(View.VISIBLE);
                break;
            case R.id.ivDrawPolygon:
                ToolPoolFactory.currentToolIndex_Btn = 6;//清空地图
                ToolPoolFactory.currentToolIndex_Map = 0;
                attrsEditComponents.getAttrsEditComponentsView().setVisibility(View.GONE);
                break;
            case R.id.ivAreaCut:
                ToolPoolFactory.currentToolIndex_Btn = -1;
                ToolPoolFactory.currentToolIndex_Map = 11;
                attrsEditComponents.getAttrsEditComponentsView().setVisibility(View.GONE);
                break;
            default:
                break;
        }
        addMapHandler();
    }

    @Override
    public void unChecked(View curView, View lastView) {
        //执行分割的功能
        if (curView.getId() == R.id.ivAreaCut && lastView.getId() == R.id.ivSelect) {
            ToolPoolFactory.currentToolIndex_Btn = -1;
            ToolPoolFactory.currentToolIndex_Map = -1;//地图工具清空
        } else if (curView.getId() == R.id.ivSelect && lastView.getId() == R.id.ivSelect) {//选择开始按下选择结束的情况
            attrsEditComponents.getAttrsEditComponentsView().setVisibility(View.GONE);
            ToolPoolFactory.currentToolIndex_Btn = 6;//清空地图
            ToolPoolFactory.currentToolIndex_Map = -1;//地图工具清空
        } else {
            ToolPoolFactory.currentToolIndex_Btn = 6;//清空地图
            ToolPoolFactory.currentToolIndex_Map = -1;//地图工具清空
        }
        addMapHandler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDelete:
                ToolPoolFactory.currentToolIndex_Btn = 10;
                addMapHandler();
                break;
            default:
                break;
        }
    }

    /**
     * 添加地图工具操作
     */
    private void addMapHandler() {
        ITool iToolBtn = ToolPoolFactory.getCurrentBtnTriggerTool();
        ITool iToolMap = ToolPoolFactory.getCurrentMapTriggerTool();
        if (iToolBtn != null) {
            iToolBtn.onReady();//按钮的功能准备
            iToolBtn.onClick();//按钮功能的触发
        }
        if (iToolMap != null) {
            iToolMap.onReady();//地图事件的功能准备
            //地图事件触发由点击地图产生
        }
    }

    //罗盘
    private final class SensorListener implements SensorEventListener {
        private float predegree = 0;

        public void onSensorChanged(SensorEvent event) {
            float degree = event.values[0];// 存放了方向值 90
            RotateAnimation animation = new RotateAnimation(predegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(100);
            imgzhinan.startAnimation(animation);
            predegree = -degree;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }
}
