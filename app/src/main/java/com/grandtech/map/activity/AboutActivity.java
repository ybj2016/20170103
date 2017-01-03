package com.grandtech.map.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandtech.map.R;
import com.grandtech.map.utils.commons.AppHelper;

public class AboutActivity extends BaseActivity {

    private TextView tvVision;
    private ImageView imgaboutback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //初始化控件
        initialize();
        initClass();
    }

    @Override
    protected void initialize() {
        tvVision = (TextView) findViewById(R.id.tvVision);
    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {
        tvVision.setText("软件版本号:   \t"+AppHelper.getVersionName(this));
    }
}
