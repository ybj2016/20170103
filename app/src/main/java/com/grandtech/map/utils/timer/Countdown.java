package com.grandtech.map.utils.timer;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zy on 2016/10/18.
 * 倒计时类
 */

public class Countdown {
    private Timer timer;
    private TimerTask task;
    private Handler handler;
    private int length;//计时长度
    private int delay = 1000;//执行延迟
    private int interval=1000;//执行间隔

    public Countdown(Handler handler,int length,int delay,int interval){
        this.handler=handler;
        this.length = length;
        this.delay=delay;
        this.interval=interval;
        countTime();
    }

    private void countTime(){
        timer=new Timer();
        task=new TimerTask() {
            @Override
            public void run() {
                length--;
                Message message=new Message();
                message.arg1=length;
                handler.sendMessage(message);
                if(length==0){
                    if(timer!=null) {
                        timer.cancel();
                        timer = null;
                    }
                    if(this!=null){
                        this.cancel();
                        task=null;
                    }
                }
            }
        };
        timer.schedule(task,delay,interval);
    }
}
