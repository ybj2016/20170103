package com.grandtech.map.gis.mapevent;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;

/**
 * Created by zy on 2016/11/2.
 */

public class MapTouchListener extends MapOnTouchListener {

    private IOnTouch iOnTouch;
    private IOnDoubleTapDragUp iOnDoubleTapDragUp;
    private IOnDoubleTapDrag iOnDoubleTapDrag;
    private IOnFling iOnFling;
    private IOnSingleTap iOnSingleTap;
    private IOnLongPressUp iOnLongPressUp;
    private IOnLongPress iOnLongPress;
    private IOnMultiPointersSingleTap iOnMultiPointersSingleTap;
    private IOnPinchPointersUp iOnPinchPointersUp;
    private IOnPinchPointersMove iOnPinchPointersMove;
    private IOnPinchPointersDown iOnPinchPointersDown;
    private IOnDragPointerUp iOnDragPointerUp;
    private IOnDragPointerMove iOnDragPointerMove;
    private IOnDoubleTap iOnDoubleTap;

    public MapTouchListener(Context context, MapView view) {
        super(context, view);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(iOnTouch!=null){
            if(iOnTouch.onTouch(v,event)) {
                return true;
            }
        }
        return super.onTouch(v, event);
    }

    @Override
    public boolean onDoubleTapDragUp(MotionEvent up) {
        if(iOnDoubleTapDragUp!=null){
            if(iOnDoubleTapDragUp.onDoubleTapDragUp(up)) {
                return true;
            }
        }
        return super.onDoubleTapDragUp(up);
    }

    @Override
    public boolean onDoubleTapDrag(MotionEvent from, MotionEvent to) {
        if(iOnDoubleTapDrag!=null){
            if(iOnDoubleTapDrag.onDoubleTapDrag(from,to)) {
                return true;
            }
        }
        return super.onDoubleTapDrag(from, to);
    }

    @Override
    public boolean onFling(MotionEvent from, MotionEvent to, float velocityX, float velocityY) {
        if(iOnFling!=null){
            if(iOnFling.onFling(from,to,velocityX,velocityY)) {
                 return true;
            }
        }
        return super.onFling(from, to, velocityX, velocityY);
    }

    @Override
    public boolean onSingleTap(MotionEvent point) {
        if(iOnSingleTap!=null){
            if(iOnSingleTap.onSingleTap(point)) {
                return true;
            }
        }
        return super.onSingleTap(point);
    }

    @Override
    public boolean onLongPressUp(MotionEvent point) {
        if(iOnLongPressUp!=null){
            if(iOnLongPressUp.onLongPressUp(point)) {
                return true;
            }
        }
        return super.onLongPressUp(point);
    }

    @Override
    public void onLongPress(MotionEvent point) {
        if(iOnLongPress!=null){
            iOnLongPress.onLongPress(point);
        }
        super.onLongPress(point);
    }

    @Override
    public void onMultiPointersSingleTap(MotionEvent event) {
        if(iOnMultiPointersSingleTap!=null){
            iOnMultiPointersSingleTap.onMultiPointersSingleTap(event);
        }
        super.onMultiPointersSingleTap(event);
    }

    @Override
    public boolean onPinchPointersUp(MotionEvent event) {
        if(iOnPinchPointersUp!=null){
            if(iOnPinchPointersUp.onPinchPointersUp(event)) {
                return true;
            }
        }
        return super.onPinchPointersUp(event);
    }

    @Override
    public boolean onPinchPointersMove(MotionEvent event) {
        if(iOnPinchPointersMove!=null){
            if(iOnPinchPointersMove.onPinchPointersMove(event)) {
                return true;
            }
        }
        return super.onPinchPointersMove(event);
    }

    @Override
    public boolean onPinchPointersDown(MotionEvent event) {
        if(iOnPinchPointersDown!=null){
            if(iOnPinchPointersDown.onPinchPointersDown(event)) {
                return true;
            }
        }
        return super.onPinchPointersDown(event);
    }

    @Override
    public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
        if(iOnDragPointerUp!=null){
            if(iOnDragPointerUp.onDragPointerUp(from,to)) {
                return true;
            }
        }
        return super.onDragPointerUp(from, to);
    }

    @Override
    public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
        if(iOnDragPointerMove!=null){
            if(iOnDragPointerMove.onDragPointerMove(from,to)){
                return true;
            }
        }
        return super.onDragPointerMove(from, to);
    }

    @Override
    public boolean onDoubleTap(MotionEvent point) {
        if(iOnDoubleTap!=null) {
            if(iOnDoubleTap.onDoubleTap(point)){
                return true;//返回当前监听事件
            }
        }
        return super.onDoubleTap(point);//返回地图默认的监听事件
    }


    public interface IOnTouch{
        public boolean onTouch(View v, MotionEvent event);
    }

    public interface IOnDoubleTapDragUp{
        public boolean onDoubleTapDragUp(MotionEvent up);
    }

    public interface IOnDoubleTapDrag{
        public boolean onDoubleTapDrag(MotionEvent from, MotionEvent to);
    }

    public interface IOnFling{
        public boolean onFling(MotionEvent from, MotionEvent to, float velocityX, float velocityY);
    }

    public interface IOnSingleTap{
        public boolean onSingleTap(MotionEvent point);
    }

    public interface IOnLongPressUp{
        public boolean onLongPressUp(MotionEvent point);
    }

    public interface IOnLongPress {
        public void onLongPress(MotionEvent point);
    }

    public interface IOnMultiPointersSingleTap {
        public void onMultiPointersSingleTap(MotionEvent event);
    }

    public interface IOnPinchPointersUp{
        public boolean onPinchPointersUp(MotionEvent event);
    }

    public interface IOnPinchPointersMove{
        public boolean onPinchPointersMove(MotionEvent event);
    }

    public interface IOnPinchPointersDown {
        public boolean onPinchPointersDown(MotionEvent event);
    }

    public interface IOnDragPointerUp {
        public boolean onDragPointerUp(MotionEvent from, MotionEvent to);
    }

    public interface IOnDragPointerMove{
        public boolean onDragPointerMove(MotionEvent from, MotionEvent to);
    }

    public interface IOnDoubleTap{
        public boolean onDoubleTap(MotionEvent point);
    }

    public void setiOnTouch(IOnTouch iOnTouch) {
        this.iOnTouch = iOnTouch;
    }

    public void setiOnDoubleTapDragUp(IOnDoubleTapDragUp iOnDoubleTapDragUp) {
        this.iOnDoubleTapDragUp = iOnDoubleTapDragUp;
    }

    public void setiOnDoubleTapDrag(IOnDoubleTapDrag iOnDoubleTapDrag) {
        this.iOnDoubleTapDrag = iOnDoubleTapDrag;
    }

    public void setiOnFling(IOnFling iOnFling) {
        this.iOnFling = iOnFling;
    }

    public void setiOnSingleTap(IOnSingleTap iOnSingleTap) {
        this.iOnSingleTap = iOnSingleTap;
    }

    public void setiOnLongPressUp(IOnLongPressUp iOnLongPressUp) {
        this.iOnLongPressUp = iOnLongPressUp;
    }

    public void setiOnLongPress(IOnLongPress iOnLongPress) {
        this.iOnLongPress = iOnLongPress;
    }

    public void setiOnMultiPointersSingleTap(IOnMultiPointersSingleTap iOnMultiPointersSingleTap) {
        this.iOnMultiPointersSingleTap = iOnMultiPointersSingleTap;
    }

    public void setiOnPinchPointersUp(IOnPinchPointersUp iOnPinchPointersUp) {
        this.iOnPinchPointersUp = iOnPinchPointersUp;
    }

    public void setiOnPinchPointersMove(IOnPinchPointersMove iOnPinchPointersMove) {
        this.iOnPinchPointersMove = iOnPinchPointersMove;
    }

    public void setiOnPinchPointersDown(IOnPinchPointersDown iOnPinchPointersDown) {
        this.iOnPinchPointersDown = iOnPinchPointersDown;
    }

    public void setiOnDragPointerUp(IOnDragPointerUp iOnDragPointerUp) {
        this.iOnDragPointerUp = iOnDragPointerUp;
    }

    public void setiOnDragPointerMove(IOnDragPointerMove iOnDragPointerMove) {
        this.iOnDragPointerMove = iOnDragPointerMove;
    }

    public void setiOnDoubleTap(IOnDoubleTap iOnDoubleTap) {
        this.iOnDoubleTap = iOnDoubleTap;
    }

    public void stopListen(){
        if(iOnTouch!=null) {
            iOnTouch = null;
        }
        if(iOnDoubleTapDragUp!=null) {
            iOnDoubleTapDragUp = null;
        }
        if(iOnDoubleTapDrag!=null) {
            iOnDoubleTapDrag = null;
        }
        if(iOnFling!=null) {
            iOnFling = null;
        }
        if(iOnSingleTap!=null) {
            iOnSingleTap = null;
        }
        if(iOnLongPressUp!=null) {
            iOnLongPressUp = null;
        }
        if(iOnLongPress!=null) {
            iOnLongPress = null;
        }
        if(iOnMultiPointersSingleTap!=null) {
            iOnMultiPointersSingleTap = null;
        }
        if(iOnPinchPointersUp!=null) {
            iOnPinchPointersUp = null;
        }if(iOnPinchPointersMove!=null) {
            iOnPinchPointersMove = null;
        }
        if(iOnPinchPointersDown!=null) {
            iOnPinchPointersDown = null;
        }
        if(iOnDragPointerUp!=null) {
            iOnDragPointerUp = null;
        }
        if(iOnDragPointerMove!=null) {
            iOnDragPointerMove = null;
        }
        if(iOnDoubleTap!=null) {
            iOnDoubleTap = null;
        }
    }
}
