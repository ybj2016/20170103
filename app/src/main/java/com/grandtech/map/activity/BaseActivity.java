package com.grandtech.map.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;

import com.esri.core.map.LayerInfo;
import com.grandtech.map.utils.commons.DialogHelper;
import com.grandtech.map.utils.commons.FileHelper;
import com.grandtech.map.utils.commons.NetHelper;
import com.grandtech.map.utils.commons.PathConfig;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by zy on 2016/10/27.
 */

public abstract class BaseActivity extends Activity {


    protected static ICallBack activityICallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (!isAppOnForeground()) {
            //App 进入后台
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected String getClassName() {
        return getClass().getSimpleName();
    }

    protected abstract void initialize();

    protected abstract void registerEvent();

    protected abstract void initClass();

    /**
     * 网络请求
     */
    protected boolean isNetWork(){
        boolean isNetWork= NetHelper.hasInternet(this);
        if(!isNetWork){
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setConfirmText("确定")
                    .setTitleText("网络断开...")
                    .setContentText("请先连接网络!")
                    .show();
        }
        return isNetWork;
    }

    /**
     * activity间的回调接口
     */
    public interface ICallBack {
        public void activityCallBack(String tag, Object o);
    }

    public static void setICallBack(ICallBack iCallBack) {
        activityICallBack = iCallBack;
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
