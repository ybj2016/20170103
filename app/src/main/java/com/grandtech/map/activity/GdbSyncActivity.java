package com.grandtech.map.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.tasks.geodatabase.SyncGeodatabaseParameters;
import com.grandtech.map.R;
import com.grandtech.map.dao.GdbDao;
import com.grandtech.map.entity.MobileUser;
import com.grandtech.map.gis.mapcore.MapContext;
import com.grandtech.map.gis.mapmanager.GdbSyncTask;
import com.grandtech.map.gis.mapmanager.ITaskCallBack;
import com.grandtech.map.utils.commons.AnimationHelper;
import com.grandtech.map.utils.commons.MessageConfig;
import com.grandtech.map.utils.commons.PathConfig;
import com.grandtech.map.utils.commons.SharedPreferencesHelper;
import com.grandtech.map.utils.enmus.EnumGDBTask;
import com.grandtech.map.utils.okhttp.OkHttpUtils;
import com.grandtech.map.utils.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 同步gdb数据填写有关信息
 */
public class GdbSyncActivity extends BaseActivity implements View.OnClickListener {

    public static Handler handler;
    private String localGdbPath = PathConfig.DATAPATH_GEODATABASE;
    private Button btnSync;
    private EditText etCollectTown;
    private MobileUser mobileUser;
    private GdbDao gdbDao;
    private MapContext mapContext;
    private FeatureLayer featureLayer;
    private Boolean isSync = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdb_sync);
        initialize();
        registerEvent();
        initClass();
    }

    @Override
    protected void initialize() {
        btnSync = (Button) findViewById(R.id.btnSync);
        etCollectTown = (EditText) findViewById(R.id.etCollectTown);
    }

    @Override
    protected void registerEvent() {
        btnSync.setOnClickListener(this);
    }

    @Override
    protected void initClass() {
        mobileUser = (MobileUser) SharedPreferencesHelper.readObject(this, LoginActivity.USER_ACCOUNT);
        gdbDao = new GdbDao(this);
        mapContext = MapContext.getInstance(null);
        featureLayer = mapContext.getFeatureLayer();
    }

    @Override
    public void onClick(View view) {
        if(isSync){
            Toast.makeText(this,"正在同步，请稍候再试！",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()) {
            case R.id.btnSync:
                try {
                    if (isNetWork()) {
                        validate();
                        beforeSync();
                        isSync = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 验证数据
     */
    private void validate() {
        if (etCollectTown.getText().toString().equals("")) {
            AnimationHelper.shakeAnimation(etCollectTown);
            Toast.makeText(this, "调查省市不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobileUser == null) {
            Toast.makeText(this, "未能找到当前登录的账户信息，请重新登录！", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * gdb同步前
     *
     * @throws Exception
     */
    public void beforeSync() throws Exception {
        String url = PathConfig.MOBILE_SERVICE_BASE + "MobileController/beforeSync";
        Geodatabase gdb = new Geodatabase(localGdbPath);// 创建一个离线地理数据库
        SyncGeodatabaseParameters syncParams = gdb.getSyncParameters();// 获取离线地理数据库同步参数
        String uuid = syncParams.generateRequestParams().get("replicaID").replace("{", "").replace("}", "");//获取副本id
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);//副本id
        params.put("phoneNum", mobileUser.getPhoneNum());//手机号
        params.put("townName", etCollectTown.getText().toString());//调查乡镇名称
        OkHttpUtils.post().url(url).params(params).build().execute(new Callback<Object>() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {//这是异步线程
                Boolean b = Boolean.valueOf(response.body().string());
                return b;//返回值到ui线程
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(GdbSyncActivity.this,"服务获取用户信息出错！",Toast.LENGTH_SHORT).show();
                isSync = false;
            }

            @Override
            public void onResponse(Object response, int id) { //这里是ui线程
                if ((Boolean) response) {
                    asyncSync();//成功则开始同步
                }
            }
        });
    }

    public void afterSync(Integer callBack) throws Exception {
        String url = PathConfig.MOBILE_SERVICE_BASE + "MobileController/afterSync";
        Geodatabase gdb = new Geodatabase(localGdbPath);// 创建一个离线地理数据库
        SyncGeodatabaseParameters syncParams = gdb.getSyncParameters();// 获取离线地理数据库同步参数
        String uuid = syncParams.generateRequestParams().get("replicaID").replace("{", "").replace("}", "");//获取副本id
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);//副本id
        params.put("callback", callBack.toString());//同步结果回调
        OkHttpUtils.post().url(url).params(params).build().execute(new Callback<Object>() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {//这是异步线程
                String string = response.body().string();
                return string;//返回值到ui线程
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                System.out.println(e.toString());
            }

            @Override
            public void onResponse(Object response, int id) { //这里是ui线程
                Toast.makeText(GdbSyncActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void asyncSync() {
        handler.obtainMessage(0, EnumGDBTask.SYNC_BEFORE.getValue(), 0, EnumGDBTask.SYNC_BEFORE).sendToTarget();
        gdbDao.batchEdit(featureLayer, new GdbDao.ICallBack() {//批量写入信息
            @Override
            public void isEditComplete(boolean isEditComplete) {
                if (isEditComplete) {
                    syncGdb();//写完后同步数据
                }
            }

            @Override
            public void handlerCount(Integer count) {
                handler.obtainMessage(MessageConfig.SYNC_GDB_HANDLER, EnumGDBTask.SYNC_BEFORE.getValue(), count, EnumGDBTask.SYNC_BEFORE).sendToTarget();
            }
        });
    }

    private void syncGdb() {
        GdbSyncTask gdbSyncTask = new GdbSyncTask(PathConfig.DATAPATH_GEODATABASE, new ITaskCallBack() {
            @Override
            public void before(EnumGDBTask enumGDBTask) {
                handler.obtainMessage(0, enumGDBTask.getValue(), 0, enumGDBTask).sendToTarget();
                isSync = true;
            }

            @Override
            public void processing(EnumGDBTask enumGDBTask) {
                handler.obtainMessage(0, enumGDBTask.getValue(), 0, enumGDBTask).sendToTarget();
                isSync = true;
            }

            @Override
            public void success(EnumGDBTask enumGDBTask) {
                handler.obtainMessage(0, enumGDBTask.getValue(), 0, enumGDBTask).sendToTarget();
                isSync = false;
                try {
                    afterSync(enumGDBTask.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(EnumGDBTask enumGDBTask) {
                handler.obtainMessage(0, enumGDBTask.getValue(), 0, enumGDBTask).sendToTarget();
                isSync = false;
                try {
                    afterSync(enumGDBTask.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        gdbSyncTask.processing();
    }
}
