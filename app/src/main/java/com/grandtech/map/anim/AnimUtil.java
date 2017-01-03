package com.grandtech.map.anim;

import android.app.Activity;

import com.grandtech.map.R;

public class AnimUtil {
	
	/**
	 * 进入跳转效果
	 * @param activity
	 */
	public static void changePageIn(Activity activity){
		activity.overridePendingTransition(R.anim.in_zoom, R.anim.out_zoom);
	}
	
	/**
	 * 退出跳转效果
	 * @param activity
	 */
	public static void changePageOut(Activity activity){
	activity.overridePendingTransition(R.anim.out_zoom, R.anim.in_zoom);
	}
}


/*
 * 缩放入（iPhone）：overridePendingTransition(R.anim.in_zoom, R.anim.out_zoom);
 * 缩放出（iPhone）：overridePendingTransition(R.anim.out_zoom, R.anim.in_zoom);
 * 左进：overridePendingTransition(R.anim.in_left, R.anim.out_right);
 * 左出：overridePendingTransition(R.anim.out_left, R.anim.in_right);
 * 右进：overridePendingTransition(R.anim.in_right, R.anim.out_left);
 * 右出：overridePendingTransition(R.anim.out_right, R.anim.in_left);
 * */
