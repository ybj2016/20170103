package com.grandtech.map.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;

import com.grandtech.map.R;
import com.grandtech.map.gis.mapcore.MapContext;
import com.grandtech.map.services.LocationService;
import com.grandtech.map.utils.commons.AppHelper;
import com.grandtech.map.utils.commons.SharedPreferencesHelper;

public class SettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    public final static String LAYER_OPACITY = "layerOpacity";
    private TableRow row1;
    private TableRow rowTrackHistory;
    private Switch switchLoc;
    private SeekBar sbLayoutTransparency;
    private MapContext mapContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        initialize();
        registerEvent();
        initClass();
    }

    //寻找控件
    @Override
    protected void initialize() {
        row1 = (TableRow) findViewById(R.id.row1);
        rowTrackHistory = (TableRow) findViewById(R.id.rowTrackHistory);
        switchLoc = (Switch) findViewById(R.id.switchLoc);
        sbLayoutTransparency= (SeekBar) findViewById(R.id.sbLayoutTransparency);
    }

    //注册事件
    @Override
    protected void registerEvent() {
        row1.setOnClickListener(this);
        rowTrackHistory.setOnClickListener(this);
        switchLoc.setOnCheckedChangeListener(this);
        sbLayoutTransparency.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void initClass() {
        if (AppHelper.isProcessRunning(this, "com.grandtech.map.services.LocationServiceAps")) {//如果服务正在运行
            switchLoc.setChecked(true);
        }
        Object layerOpacity = SharedPreferencesHelper.readObject(this,LAYER_OPACITY);
        if(layerOpacity!=null) {
            sbLayoutTransparency.setProgress((int)(new Float(layerOpacity.toString())*100));
        }else {
            sbLayoutTransparency.setProgress(sbLayoutTransparency.getMax());
        }
        mapContext = MapContext.getInstance(null);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.row1:
                intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.rowTrackHistory:
                intent = new Intent(this, TrackActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
       Intent locationBD = new Intent(this, LocationService.class);
        if (b) {
            if (AppHelper.isLocationOpen(this)) {//判断服务是否正在运行
                locationBD.putExtra("isStartOutSide",true);//服务是否由外部启动
                startService(locationBD);
            } else {
                Toast.makeText(this, "请连接网络或者打开GPS", Toast.LENGTH_LONG).show();
                switchLoc.setChecked(false);
            }
        } else {
            stopService(locationBD);
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDestroy() {
        float layerOpacity = (float)sbLayoutTransparency.getProgress()/sbLayoutTransparency.getMax();
        mapContext.getFeatureLayer().setOpacity(layerOpacity);
        SharedPreferencesHelper.saveOrUpdateObject(this,LAYER_OPACITY,layerOpacity);
        super.onDestroy();
    }
}
