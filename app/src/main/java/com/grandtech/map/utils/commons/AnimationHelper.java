package com.grandtech.map.utils.commons;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by zy on 2016/11/16.
 * 心跳view
 */



public final class AnimationHelper {
    private static final float ZOOM_MAX = 1.3f;
    private static final float ZOOM_MIN = 1.0f;

    private AnimationHelper(){}

    /**
     * 左右晃动动画
     * @param v
     */
    public static void shakeAnimation(View v){
        TranslateAnimation animation = new TranslateAnimation(0, -10, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(10);
        animation.setRepeatCount(5);
        animation.setRepeatMode(Animation.REVERSE);
        v.startAnimation(animation);
    }

    /**
     *
     * @param view
     *            需要实现心跳的View
     */
    public static void playHeartbeatAnimation(final View view) {

        /**
         * 放大动画
         */
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(ZOOM_MIN, ZOOM_MAX, ZOOM_MIN, ZOOM_MAX, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.8f));

        animationSet.setDuration(500);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                /**
                 * 缩小动画
                 */
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(new ScaleAnimation(ZOOM_MAX, ZOOM_MIN, ZOOM_MAX, ZOOM_MIN,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
                animationSet.addAnimation(new AlphaAnimation(0.8f, 1.0f));
                animationSet.setDuration(600);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setFillAfter(false);
                // 实现心跳的View
                view.startAnimation(animationSet);
            }
        });
        // 实现心跳的View
        view.startAnimation(animationSet);

    }
}
