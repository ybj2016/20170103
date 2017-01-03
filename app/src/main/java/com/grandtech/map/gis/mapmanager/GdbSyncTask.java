package com.grandtech.map.gis.mapmanager;

import com.esri.core.ags.FeatureServiceInfo;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTableEditErrors;
import com.esri.core.map.CallbackListener;
import com.esri.core.tasks.geodatabase.GeodatabaseStatusCallback;
import com.esri.core.tasks.geodatabase.GeodatabaseStatusInfo;
import com.esri.core.tasks.geodatabase.GeodatabaseSyncTask;
import com.esri.core.tasks.geodatabase.SyncGeodatabaseParameters;
import com.grandtech.map.utils.commons.PathConfig;
import com.grandtech.map.utils.enmus.EnumGDBTask;

import java.util.Map;

/**
 * gdb同步任务
 * Created by zy on 2016/12/22.
 */
public class GdbSyncTask implements ITask {

    /**
     * 服务地址
     */
    private static final String FEATURE_SERVICE_URL = PathConfig.FEATURE_SERVICE_URL;
    /**
     * 同步任务
     */
    private GeodatabaseSyncTask gdbSyncTask;
    /**
     * 同步到服务器上的本地文件路径
     */
    private String localGdbPath;
    /**
     * 回调接口
     */
    private ITaskCallBack iCallBack;

    public GdbSyncTask(String localGdbPath, ITaskCallBack iCallBack) {
        this.iCallBack = iCallBack;
        this.localGdbPath = localGdbPath;
        gdbSyncTask = new GeodatabaseSyncTask(FEATURE_SERVICE_URL, null);
    }

    @Override
    public void processing() {
        if (iCallBack != null) {
            iCallBack.before(EnumGDBTask.SYNC_BEFORE);
        }
        gdbSyncTask.fetchFeatureServiceInfo(new CallbackListener<FeatureServiceInfo>() {

            @Override
            public void onError(Throwable throwable) {
                if (iCallBack != null) {
                    iCallBack.error(EnumGDBTask.SYNC_ERROR);
                }
            }

            @Override
            public void onCallback(FeatureServiceInfo fsInfo) {
                if (fsInfo == null) {
                    if (iCallBack != null) {
                        iCallBack.error(EnumGDBTask.SYNC_ERROR);
                    }
                    return;
                }
                if (fsInfo.isSyncEnabled()) {
                    try {
                        Geodatabase gdb = new Geodatabase(localGdbPath);// 创建一个离线地理数据库
                        SyncGeodatabaseParameters syncParams = gdb.getSyncParameters(); // 获取离线地理数据库同步参数
                        CallbackListener<Map<Integer, GeodatabaseFeatureTableEditErrors>> syncResponseCallback = new CallbackListener<Map<Integer, GeodatabaseFeatureTableEditErrors>>() {

                            @Override
                            public void onCallback(Map<Integer, GeodatabaseFeatureTableEditErrors> obj) {
                                if (obj != null) {
                                    if (obj.size() > 0) {
                                        if (iCallBack != null) {
                                            iCallBack.error(EnumGDBTask.SYNC_ERROR);
                                        }
                                    } else {
                                        if (iCallBack != null) {
                                            iCallBack.success(EnumGDBTask.SYNC_SUCCESS);
                                        }
                                    }
                                } else {
                                    if (iCallBack != null) {
                                        iCallBack.success(EnumGDBTask.SYNC_SUCCESS);
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (iCallBack != null) {
                                    iCallBack.error(EnumGDBTask.SYNC_ERROR);
                                }
                            }
                        };

                        GeodatabaseStatusCallback statusCallback = new GeodatabaseStatusCallback() {

                            @Override
                            public void statusUpdated(GeodatabaseStatusInfo status) {
                                System.out.println(">>>>>>>>>>>." + status.getStatus().toString());
                                if (iCallBack != null) {
                                    iCallBack.processing(EnumGDBTask.SYNC_PROCESSING);
                                }
                            }
                        };
                        // 执行同步
                        gdbSyncTask.syncGeodatabase(syncParams, gdb, statusCallback, syncResponseCallback);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (iCallBack != null) {
                            iCallBack.error(EnumGDBTask.SYNC_ERROR);
                        }
                    }
                }
            }
        });
    }
}
