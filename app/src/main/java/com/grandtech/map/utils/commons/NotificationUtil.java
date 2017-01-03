package com.grandtech.map.utils.commons;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.grandtech.map.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by zy on 2016/11/17.
 */

public class NotificationUtil {

    private Context context;
    //获取状态通知栏管理
    private NotificationManager notificationManager;
    //实例化通知栏构造器
    private NotificationCompat.Builder notificationBuilder;
    //显示的图标
    private int iconId;
    //通知ID
    private int notificationId;
    public NotificationUtil(Context context,int iconId){
        this.context=context;
        this.notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        this.notificationBuilder = new NotificationCompat.Builder(context);
        this.iconId=iconId;
        initNotify();
    }

    private void initNotify() {
        notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setContentIntent(getDefalutIntent(0))
                // .setNumber(number)//显示数量
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                //.setDefaults(Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
                // requires VIBRATE permission
                .setSmallIcon(iconId);
    }

    private PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
    }

    /**
     *
     * @param notifyId  通知id
     * @param title     通知标题
     * @param context   通知内容
     * @param ticker    变化的值
     */
    public void showProgressNotify(int notifyId,String title,String context,String ticker) {
        notificationBuilder.setContentTitle(title).setContentText(context).setTicker(ticker);// 通知首次出现在通知栏，带上升动画效果的
        notificationBuilder.setProgress(0, 0, true); //不确定进度的
        notificationManager.notify(notifyId, notificationBuilder.build());
    }

    private void setNotificationId(int notificationId){
        this.notificationId=notificationId;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public NotificationCompat.Builder getBuilder() {
        return notificationBuilder;
    }
}
