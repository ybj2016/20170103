package com.grandtech.map.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.grandtech.map.R;
import com.grandtech.map.dao.GdbDao;
import com.grandtech.map.gis.mapcore.MapContext;

/**
 * Created by jsb03 on 2016/12/30.
 */

public class PlotStatisticsActivity extends Activity {

    private TextView dkcountTextView ;//显示地块数TextView
    private TextView dkbjTextView ;//显示地块已标记数TextView
    private GdbDao gdbDao;
    private MapContext mapContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_statistics);
        init();


    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    dkbjTextView.setText(msg.arg1+"");
                    dkcountTextView.setText(msg.arg2+"");
                    break;
            }
        }
    };

    //初始化
    private void init(){
        dkcountTextView = (TextView) findViewById(R.id.dkcountTextView);
        dkbjTextView = (TextView) findViewById(R.id.dkbjTextView);
        gdbDao=new GdbDao(this);
        mapContext = MapContext.getInstance(null);
        dkcountTextView.setText("正在统计...");
        dkbjTextView.setText("正在统计...");
        gdbDao.getEditedZZMC(mapContext.getFeatureLayer(), new GdbDao.ICountCallBack() {
            @Override
            public void countCallBack(Integer count,Integer countAll) {
                handler.obtainMessage(0,count,countAll,null).sendToTarget();
            }
        });
    }
}
