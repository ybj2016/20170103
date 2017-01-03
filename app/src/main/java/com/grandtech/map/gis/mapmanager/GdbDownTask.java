package com.grandtech.map.gis.mapmanager;

import com.esri.core.ags.FeatureServiceInfo;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.tasks.geodatabase.GenerateGeodatabaseParameters;
import com.esri.core.tasks.geodatabase.GeodatabaseStatusCallback;
import com.esri.core.tasks.geodatabase.GeodatabaseStatusInfo;
import com.esri.core.tasks.geodatabase.GeodatabaseSyncTask;
import com.grandtech.map.utils.commons.PathConfig;
import com.grandtech.map.utils.enmus.EnumGDBTask;

/**
 * Created by zy on 2016/12/22.
 */

public class GdbDownTask implements ITask {
    /**
     * 下载数据地址
     */
    private final static String FEATURE_SERVICE_URL = PathConfig.FEATURE_SERVICE_URL;
    /**
     * 要保存的数据名称
     */
    private String gdbFileName;
    /**
     * gdb下载任务
     */
    private GeodatabaseSyncTask gdbTask;
    /**
     * 数据下载范围
     */
    private Polygon extend;
    /**
     * 进度回调接口
     */
    private ITaskCallBack iTaskCallBack;


    public GdbDownTask(String gdbFileName, Polygon extend, ITaskCallBack iTaskCallBack) {
        this.gdbFileName = gdbFileName;
        this.extend = extend;
        this.iTaskCallBack = iTaskCallBack;
        gdbTask = new GeodatabaseSyncTask(FEATURE_SERVICE_URL, null);
    }

    @Override
    public void processing() {
        if (iTaskCallBack != null) {
            iTaskCallBack.before(EnumGDBTask.DOWN_BEFORE);
        }
        //检查当前数据是否满足要求
        gdbTask.fetchFeatureServiceInfo(new CallbackListener<FeatureServiceInfo>() {
            @Override
            public void onError(Throwable e) { //如果不满足则抛出异常
                if (iTaskCallBack != null) {
                    iTaskCallBack.error(EnumGDBTask.DOWN_ERROR);
                }
            }

            @Override
            public void onCallback(FeatureServiceInfo fsInfo) {
                if (fsInfo.isSyncEnabled()) {//检查服务功能是否满足要求
                    if (iTaskCallBack != null) {
                        iTaskCallBack.processing(EnumGDBTask.DOWN_PROCESSING);
                    }
                    SpatialReference spatialReference = SpatialReference.create(PathConfig.SRID_TEXT);
                    GenerateGeodatabaseParameters params = new GenerateGeodatabaseParameters(fsInfo, extend, spatialReference, null, true);
                    params.setOutSpatialRef(spatialReference);
                    CallbackListener<String> gdbResponseCallback = new CallbackListener<String>() { //下载结果的回调函数

                        @Override
                        public void onCallback(String path) {
                            if (iTaskCallBack != null) {
                                iTaskCallBack.success(EnumGDBTask.DOWN_SUCCESS);//下载完成返回路径
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (iTaskCallBack != null) {
                                iTaskCallBack.error(EnumGDBTask.DOWN_ERROR);
                            }
                        }
                    };
                    GeodatabaseStatusCallback statusCallback = new GeodatabaseStatusCallback() {

                        @Override
                        public void statusUpdated(GeodatabaseStatusInfo status) {
                            if (!status.isDownloading()) {
                                if (iTaskCallBack != null) {
                                    iTaskCallBack.processing(EnumGDBTask.DOWN_PROCESSING);
                                }
                            }
                        }
                    };
                    gdbTask.generateGeodatabase(params, gdbFileName, false, statusCallback, gdbResponseCallback);//执行下载
                    if (iTaskCallBack != null) {
                        iTaskCallBack.processing(EnumGDBTask.DOWN_PROCESSING);
                    }
                } else {
                    if (iTaskCallBack != null) {
                        iTaskCallBack.error(EnumGDBTask.DOWN_ERROR);
                    }
                }
            }
        });
    }
}
