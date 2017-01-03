package com.grandtech.map.view.checkimageview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.grandtech.map.R;
import com.grandtech.map.utils.commons.ViewsTreeHelper;

import java.util.List;

/**
 * Created by zy on 2016/11/15.
 */

public class CheckImageView extends ImageView {

    private int count = 0;//点击的次数
    public boolean isDown = false;//当前按钮的状态
    private String tag=null;//如果等于null其就是一个普通的 imageview
    public Drawable srcDrawable;//弹起颜色
    public Drawable src_cDrawable;//按下颜色
    private CheckImageView currentCheckImageView=null;//记录当前当前按钮的对象
    private boolean editStats=false;
    private boolean INSCROLLVIEW_FALSE=true;//嵌套进scrollview时置为false 有bug  暂时关闭某些 功能  以便以后完善
    private OnCheckedListener onCheckedListener;
    private View rootView;
    private Context context;
    private boolean lockable = false;
    private View currentView;

    public CheckImageView(Context context) {
        super(context);
    }

    public CheckImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CheckImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(final Context context,AttributeSet attrs){
        this.context=context;
        tag= (String) this.getTag();
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.checkBtnSrc);
        if (typedArray != null) {
            srcDrawable=typedArray.getDrawable(R.styleable.checkBtnSrc_src);
            src_cDrawable=typedArray.getDrawable(R.styleable.checkBtnSrc_src_c);
            lockable=typedArray.getBoolean(R.styleable.checkBtnSrc_lock_able,false);
            typedArray.recycle();
        }
        this.setBackground(srcDrawable);
        if(tag!=null&&tag.contains("[")&&tag.contains("]")) {
            this.setClickable(true);//必须设置 否则监听不到 手指移动和抬起事件
        }else {
            this.setClickable(true);//必须设置 否则监听不到 手指移动和抬起事件 //this.setClickable(false);
        }

        //绘制完成后回调
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView=((ViewGroup)((Activity)context).findViewById(android.R.id.content)).getChildAt(0);//获取ViewTree的根节点
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x=0;
        int y=0;
        int view_w=this.getWidth();
        int view_y=this.getHeight();
        if(lockable) {//锁定按钮
            if (event.getAction() == MotionEvent.ACTION_DOWN && src_cDrawable != null && INSCROLLVIEW_FALSE) {
                if (!isDown) {
                    this.setBackground(src_cDrawable);//弹起图片
                } else {
                    this.setBackground(srcDrawable);//按下图片
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP && src_cDrawable != null) {//对按钮状态的监听
                x = (int) event.getX();
                y = (int) event.getY();
                if (x <= view_w && x >= 0 && y <= view_y && y >= 0 && tag != null) {//判断手指离开时是否在当前点击的view上
                    count++;
                    if (count % 2 == 0) {
                        if (onCheckedListener != null) {//手指离开时监听事件
                            onCheckedListener.unChecked(this,this);
                        }
                        //设置样式
                        isDown = false;
                        //System.out.println("弹起");
                        this.setBackground(srcDrawable);//弹起图片
                        this.setCurrentCheckImage(null);
                        //设置功能
                        if (tag.startsWith("[") && tag.endsWith("]")) {//重置其他按钮组
                            String contextTag = tag.substring(1, tag.lastIndexOf(']'));
                            setDisableControl((ViewGroup) rootView, contextTag, false);
                            otherCheckButtonReset((ViewGroup) rootView, contextTag);
                        } else {//设置其他按钮组不可编辑
                            otherCheckButtonReset((ViewGroup) rootView, tag);
                        }
                    } else {
                        //设置样式
                        isDown = true;
                        this.setBackground(src_cDrawable);//按下图片
                        this.setCurrentCheckImage(this);
                        //System.out.println("按下");
                        //设置功能
                        if (tag.startsWith("[") && tag.endsWith("]")) {//设置其他按钮组可编辑
                            String contextTag = tag.substring(1, tag.lastIndexOf(']'));
                            setDisableControl((ViewGroup) rootView, contextTag, true);
                        } else {
                            currentView=this;
                            otherCheckButtonReset((ViewGroup) rootView, this.tag);
                        }
                        if (onCheckedListener != null) {//手指离开时监听事件
                            onCheckedListener.checked(this);
                        }
                    }
                } else {//若手指离开则回复原样
                    if (INSCROLLVIEW_FALSE) {
                        if (!isDown) {
                            this.setBackground(srcDrawable);//弹起图片
                        } else {
                            this.setBackground(src_cDrawable);//按下图片
                        }
                    }
                }
            }
        }else {//非锁定按钮
            if (event.getAction() == MotionEvent.ACTION_DOWN && src_cDrawable != null && INSCROLLVIEW_FALSE) {
                if (!isDown) {
                    this.setBackground(src_cDrawable);//弹起图片
                } else {
                    this.setBackground(srcDrawable);//按下图片
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP && src_cDrawable != null) {//对按钮状态的监听
                if (INSCROLLVIEW_FALSE) {
                    if (!isDown) {
                        this.setBackground(srcDrawable);//弹起图片
                    } else {
                        this.setBackground(src_cDrawable);//按下图片
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     *
     * @param currrentView
     * @param lastView
     */
    public void otherReSet(View currrentView,View lastView){
        ((CheckImageView)lastView).reSet();
        if(onCheckedListener!=null) {//手指离开时监听事件
            onCheckedListener.unChecked(currrentView,lastView);
        }
    }

    /**
     * 设置其他控件是否用
     * @param curView
     * @param tag
     * @param disable
     */
    public void setDisableControl(ViewGroup curView,String tag,boolean disable){
        List<View> views = ViewsTreeHelper.find(curView,tag);
        for (int i=0;i<views.size();i++){
            View view=views.get(i);
            Object viewTag=view.getTag();
            if (viewTag!=null&&viewTag.equals(tag)) {
                ((CheckImageView) view).setClickable(disable);
            }
        }
    }

    /**
     * 重置其他按钮（互斥）
     * @param curView
     * @param tag
     */
    private void otherCheckButtonReset(ViewGroup curView,String tag) {
        List<View> views = ViewsTreeHelper.find(curView,tag);
        for (int i=0;i<views.size();i++){
            View view=views.get(i);
            Object viewTag=view.getTag();
            if(viewTag!=null&&viewTag.equals(tag) && ((CheckImageView) view) != this&&((CheckImageView) view).getCurrentCheckImage()!=null){
                ((CheckImageView) view).otherReSet(currentView,view);
            }
        }
    }

    /**
     * 重置样式
     */
    public void reSet(){
        this.count=0;
        this.setBackground(srcDrawable);
        this.setCurrentCheckImage(null);
    }

    public void setCurrentCheckImage(CheckImageView currentCheckImageView){
        this.currentCheckImageView=currentCheckImageView;
    }

    public CheckImageView getCurrentCheckImage(){
        return this.currentCheckImageView;
    }

    //事件监听
    public interface OnCheckedListener{
        /**
         * 当前的view
         * @param view
         */
        public void checked(View view);

        /**
         * 当前的view和上一个view
         * @param cuView
         * @param lastView
         */
        public void unChecked(View cuView,View lastView);
    }

    public void setOnCheckedListener(OnCheckedListener onCheckedListener){
        this.onCheckedListener=onCheckedListener;
    }

}
