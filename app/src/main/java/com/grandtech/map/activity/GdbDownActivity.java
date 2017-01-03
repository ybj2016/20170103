package com.grandtech.map.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.grandtech.map.R;
import com.grandtech.map.gis.mapmanager.GdbDownTask;
import com.grandtech.map.gis.mapmanager.ITaskCallBack;
import com.grandtech.map.utils.commons.FileHelper;
import com.grandtech.map.utils.commons.PathConfig;
import com.grandtech.map.utils.enmus.EnumGDBTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * gdb数据下载管理activity
 */
public class GdbDownActivity extends BaseActivity implements View.OnClickListener {

    private Button btndown;
    private EditText etpoint1_x, etpoint1_y, etpoint3_x, etpoint3_y;
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdb);
        initialize();
        registerEvent();
        initClass();
    }

    @Override
    protected void initialize() {
        btndown = (Button) findViewById(R.id.btnload);
        etpoint1_x = (EditText) findViewById(R.id.etpoint1_x);
        etpoint1_y = (EditText) findViewById(R.id.etpoint1_y);
        etpoint3_x = (EditText) findViewById(R.id.etpoint3_x);
        etpoint3_y = (EditText) findViewById(R.id.etpoint3_y);
    }

    @Override
    protected void registerEvent() {
        btndown.setOnClickListener(this);
    }

    @Override
    protected void initClass() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnload:
                if (isNetWork()) {//判断网络是否存在
                    if (FileHelper.isExist(PathConfig.DATAPATH_GEODATABASE)) {
                        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("确认下载?")
                                .setContentText("该操作会覆盖原文件，请先确保文件已同步成功!")
                                .setCancelText("取消")
                                .setConfirmText("是的,删除并下载!")
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
                                        FileHelper.deleteSameNameFile(PathConfig.DATAPATH_GEODATABASE);//先删除文件
                                        sweetAlertDialog.dismiss();
                                        downGdb();
                                    }
                                }).show();
                    } else {
                        downGdb();
                    }
                }
                break;
        }
    }


    //下载数据
    private void downGdb() {
        Point point1 = new Point(Float.parseFloat(etpoint1_x.getText().toString()), Float.parseFloat(etpoint1_y.getText().toString()));
        Point point3 = new Point(Float.parseFloat(etpoint3_x.getText().toString()), Float.parseFloat(etpoint3_y.getText().toString()));
        Point point2 = new Point(point3.getX(), point1.getY());
        Point point4 = new Point(point1.getX(), point3.getY());
        MultiPath multipath = new Polygon();
        multipath.startPath(point1);
        multipath.lineTo(point2);
        multipath.lineTo(point3);
        multipath.lineTo(point4);

        GdbDownTask gdbDownTask = new GdbDownTask(PathConfig.DATAPATH_GEODATABASE, (Polygon) multipath, new ITaskCallBack() {
            @Override
            public void before(EnumGDBTask enumGDBTask) {
                handler.obtainMessage(0, enumGDBTask.getValue(), 0, enumGDBTask).sendToTarget();
                System.out.println("开始下载");
            }

            @Override
            public void processing(EnumGDBTask enumGDBTask) {
                handler.obtainMessage(0, enumGDBTask.getValue(), 0, enumGDBTask).sendToTarget();
                System.out.println("下载中");
            }

            @Override
            public void success(EnumGDBTask enumGDBTask) {
                handler.obtainMessage(0, enumGDBTask.getValue(), 0, enumGDBTask).sendToTarget();
                System.out.println("下载成功");
            }

            @Override
            public void error(EnumGDBTask enumGDBTask) {
                handler.obtainMessage(0, enumGDBTask.getValue(), 0, enumGDBTask).sendToTarget();
                System.out.println("下载失败");
            }
        });
        gdbDownTask.processing();
    }
}
