package com.grandtech.map.utils.commons;

import android.content.Context;
import android.util.DisplayMetrics;

import com.grandtech.map.R;


/**
 * @author yft
 * 测量屏幕尺寸
 */
public class MeasureScreen {

    /**
     * 获取屏幕宽度的分辨率
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        DisplayMetrics dm=context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
    /**
     * 获取屏幕高度的分辨率
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        DisplayMetrics dm=context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 通过反射获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context){
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
            statusBarHeight = 0;
        }
        return statusBarHeight;
    }

    /**
     * 获取app显示高度
     * @return
     */
    public static int getAppDisplayHeight(Context context){
         return getScreenHeight(context)-getStatusBarHeight(context);
    }

    /**
     * title栏高度
     * @param context
     * @return
     */
    public static int getTitleBarHeight(Context context){
        return (int)context.getResources().getDimension(R.dimen.app_title_height);
    }

    /**
     * 工具栏高度
     * @param context
     * @return
     */
    public static int getToolBarHeight(Context context){
        return (int)context.getResources().getDimension(R.dimen.app_foot_height);
    }

    public static int getFirstLevelMenuWidth(Context context){
        return (int)context.getResources().getDimension(R.dimen.app_first_level_width);
    }

    /**
     * 地图显示高度
     * @param context
     * @return
     */
    public static int getMapViewHeight(Context context){
        return getAppDisplayHeight(context)-getTitleBarHeight(context)-getToolBarHeight(context);
    }

    /**
     * dip转px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    /**
     * px转dip
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
