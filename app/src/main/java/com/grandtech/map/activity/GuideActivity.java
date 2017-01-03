package com.grandtech.map.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

import com.grandtech.map.R;
import com.grandtech.map.utils.timer.Countdown;


public class GuideActivity extends Activity {

    private Countdown countdown;
    private int max=100;
    private int length=2;
    private int delay=1000;
    private int interval=1000;
    private Intent intent;

    private ProgressBar progressBarGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* setTheme(R.style.Fullscreen);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        countdown=new Countdown(handler,length,delay,interval);//五秒后执行
        intent=new Intent(this,LoginActivity.class);
        //intent=new Intent(this,MapActivity.class);
        init();
    }



    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            progressBarGuide.setProgress(max-(max/length)*msg.arg1);
                if(msg.arg1==0){
                    finish();
                    startActivity(intent);
            }
        }
    };

    private void init(){
        progressBarGuide= (ProgressBar) findViewById(R.id.progressBarGuide);
        progressBarGuide.setMax(max);
        progressBarGuide.setVisibility(View.GONE);
    }
}
