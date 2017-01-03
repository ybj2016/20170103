package com.grandtech.map.gis.mapcore;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.grandtech.map.gis.maptools.Operation;

/**
 * Created by kuchanly on 2016-11-2.
 * 地图操作相关工具接口
 */

public interface ITool {

    public final static int BTN_CLICK = 1; //由按钮点击触发的工具
    public final static int MAP_CLICK = 2; //由地图触发的工具

    /**
     *传入MapView实例化后对象
     * @param context
     */
    public void  onCreate(Context context);

    /**
     * 销毁
     */
    public void onDestroy();

    /**
     * 重置工具参数
     */
    public void onReady();

    /**
     * 按钮触发
     */
    public void  onClick();

    /**
     *
     * @param v
     * @param event
     * @return
     */
    public boolean onTouch(View v, MotionEvent event);

    /**
     *
     * @param up
     * @return
     */
    public boolean onDoubleTapDragUp(MotionEvent up);

    /**
     *
     * @param from
     * @param to
     * @return
     */
    public boolean onDoubleTapDrag(MotionEvent from, MotionEvent to);

    /**
     *
     * @param from
     * @param to
     * @param velocityX
     * @param velocityY
     * @return
     */
    public boolean onFling(MotionEvent from, MotionEvent to, float velocityX, float velocityY);



    /**
     * 地图单击触发
     * @param event
     */
    public boolean  onSingleTap(MotionEvent event);

    /**
     * 长按抬起
     * @param point
     * @return
     */
    public boolean onLongPressUp(MotionEvent point);

    /**
     * 长按
     * @param point
     */
    public void onLongPress(MotionEvent point);

    /**
     * 多点手势
     * @param event
     */
    public void onMultiPointersSingleTap(MotionEvent event);

    /**
     * 地图双击触发
     * @param event
     */
    public boolean onDoubleTap(MotionEvent event);

    /**
     *
     * @param event
     * @return
     */
    public boolean onPinchPointersUp(MotionEvent event);

    /**
     *
     * @param event
     * @return
     */
    public boolean onPinchPointersMove(MotionEvent event);

    /**
     *
     * @param from
     * @param to
     * @return
     */
    public boolean onDragPointerUp(MotionEvent from, MotionEvent to);

    /**
     *
     * @param from
     * @param to
     * @return
     */
    public boolean onDragPointerMove(MotionEvent from, MotionEvent to);


    /**
     * 开始执行持久化操作
     * @param o
     */
    public void beforeOperates(Object o);

    /**
     * 执行持久化操作中
     * @param o
     */
    public void operating(Object o);
    /**
     * 结束执行持久化操作
     * @param o
     */
    public void afterOperates(Object o);

    /**
     * 当前按钮的触发方式
     * @return
     */
    public int triggerMode();



}
