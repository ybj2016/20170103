package com.grandtech.map.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.grandtech.map.R;
import com.grandtech.map.adapter.TrackAdapter;
import com.grandtech.map.dao.TrackDao;
import com.grandtech.map.entity.Track;

import java.io.Serializable;
import java.util.List;

public class TrackActivity extends BaseActivity implements TrackAdapter.ItemUiUpdate, AbsListView.OnScrollListener {

    public final static String BR_INTENT = "com.grandtech.map.activity.TrackActivity";
    private ListView lvTrackHistory;
    private TrackAdapter trackAdapter;
    private TrackDao trackDao;
    private List<Track> tracks;
    private LocBroadcastReceiver locBroadcastReceiver;
    private View itemView;
    private boolean isPositionItemVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        initialize();
        initClass();
        registerEvent();
        registerBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(locBroadcastReceiver);
        //给MapActivity发送广播
        List<Track> tracks = trackAdapter.getSelectTrack();
        Intent intent = new Intent();
        intent.setAction(MapActivity.BR_INTENT);
        intent.putExtra("tracks", (Serializable) tracks);
        sendBroadcast(intent);
    }

    //初始化控件
    @Override
    protected void initialize() {
        lvTrackHistory = (ListView) findViewById(R.id.lvTrackHistory);
    }

    //注册事件
    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {
        trackDao = new TrackDao(this);
        tracks = trackDao.getAllRecords();
        trackAdapter = new TrackAdapter(this, tracks, this);
        lvTrackHistory.setAdapter(trackAdapter);
        lvTrackHistory.setOnScrollListener(this);
    }

    public void registerBroadcastReceiver() {
        locBroadcastReceiver = new LocBroadcastReceiver();
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BR_INTENT);
        registerReceiver(locBroadcastReceiver, myIntentFilter);//注册广播
    }

    @Override
    public void update(View view) {
        itemView = view;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int count = msg.what;
            if (itemView != null&&isPositionItemVisible) {
                ((TextView) itemView).setText(count + "");
            }
        }
    };

    //listView滚动监听
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        System.out.println("onScrollStateChanged");
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        isPositionItemVisible = trackAdapter.isPositionItemVisible(lvTrackHistory, 0);
        System.out.println(isPositionItemVisible);
    }

    class LocBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int count = intent.getIntExtra("locPointCount", 0);
            handler.obtainMessage(count).sendToTarget();
        }
    }

    ;
}
