package com.grandtech.map.utils.commons;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zy on 2016/11/15.
 * android简单事件模拟类
 */

public class EventHelper {

    /**
     * 为view模拟一次触摸事件
     * @param view
     */
    public static void simulateTouch(View view){
        final long downTime = SystemClock.uptimeMillis();
        float x=  view.getWidth();
        float y=  view.getHeight();//模拟事件在控件上的点击坐标
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        final MotionEvent upEvent = MotionEvent.obtain(downTime, SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }
}
