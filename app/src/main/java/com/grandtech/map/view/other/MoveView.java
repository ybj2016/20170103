package com.grandtech.map.view.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.grandtech.map.utils.commons.MeasureScreen;

public class MoveView extends View  implements View.OnClickListener {

	private static final int WIDTH = 100;

	private static final int NORMAL_CLICK_EVENT=500;//区分点击事件和长按事件的阀值
	private static final int MOVE_DIS=20;//拖动阀值

	//获取当前设备的宽高（包括横竖屏幕切换）
	private int w;//1080
	private int h;//1920
	private int h_s=120;//上下要留出的范围

	private Rect rect = new Rect(0, 240, WIDTH, WIDTH + 240);//绘制矩形的区域
	private int deltaX,deltaY;//点击位置和图形边界的偏移量
	private static Paint paint = new Paint();//画笔

	private int count=0;

	public MoveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		w = MeasureScreen.getScreenWidth(context);
		h = MeasureScreen.getScreenHeight(context);
		paint = new Paint();
	}


	@Override
	public void onClick(View v) {
		count++;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		paint.setColor(Color.WHITE);//矩形填充色
		canvas.drawRect(rect, paint);//画矩形
		paint.setColor(Color.YELLOW);//化文字
		paint.setTextSize(30);
		String text = String.valueOf(count);
		//paint.getTextBounds(text, 0, text.length(), rect);
		float textWidth = rect.width();
		float textHeight = rect.height();
		canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2+ textHeight / 2, paint);
	}

	@Override
	public boolean onTouchEvent (MotionEvent event) {
		int x = (int) event.getX();//手指的x坐标
		int y = (int) event.getY();//手指的y坐标
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(!rect.contains(x, y)) {
					return false;//没有在矩形上点击，不处理触摸消息
				}
				deltaX = x - rect.left;//手指在view上的位置x
				deltaY = y - rect.top;
				break;
			case MotionEvent.ACTION_MOVE:

			case MotionEvent.ACTION_UP:
				Rect old = new Rect(rect);
				//定义矩形在上下左右位置的移动范围 防治超出边界（ok 有点小bug）
				if(x - deltaX<0){//左边界
					if(y - deltaY<h_s){//上边界
						rect.top = h_s;
						rect.left = 0;
						rect.right = rect.left + WIDTH;
						rect.bottom = rect.top + WIDTH;
					}else if(y + WIDTH - deltaY>h-h_s) {//下边界    不能通过 rect.top + WIDTH>**来判断  否则有点小问题
						rect.bottom = h-h_s;
						rect.left = 0;
						rect.top = rect.bottom-WIDTH;
						rect.right = rect.left + WIDTH;
					}else {//正常
						rect.left = 0;
						rect.top = y - deltaY;
						rect.bottom = rect.top + WIDTH;
						rect.right = rect.left + WIDTH;
					}
				}else if (x + WIDTH-deltaX>w) {//右边界
					if(y - deltaY<h_s){//上边界
						rect.top = h_s;
						rect.bottom = rect.top + WIDTH;
						rect.left = w-WIDTH;
						rect.right = w;
					}else if(y + WIDTH - deltaY>h-h_s) {//下边界
						rect.bottom = h-h_s;
						rect.top = rect.bottom-WIDTH;
						rect.left = w-WIDTH;
						rect.right = w;
					}else {//正常
						rect.top = y - deltaY;
						rect.bottom = rect.top + WIDTH;
						rect.left = w-WIDTH;
						rect.right = w;
					}
				}else {//左右边界正常
					if(y - deltaY<h_s){//上边界
						rect.top = h_s;
						rect.left = x - deltaX;
						rect.right = rect.left + WIDTH;
						rect.bottom = rect.top + WIDTH;
					}else if(y + WIDTH - deltaY>h-h_s) {//下边界
						rect.bottom = h-h_s;
						rect.top = rect.bottom-WIDTH;
						rect.right = rect.left + WIDTH;
						rect.left = x - deltaX;
					}else {//正常
						rect.left = x - deltaX;
						rect.top = y - deltaY;
						rect.right = rect.left + WIDTH;
						rect.bottom = rect.top + WIDTH;
					}
				}
				old.union(rect);//要刷新的区域，求新矩形区域与旧矩形区域的并集
				invalidate(old);//出于效率考虑，设定脏区域，只进行局部刷新，不是刷新整个view
				break;
		}
		return true;//处理了触摸消息，消息不再传递
	}
}
