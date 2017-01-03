package com.grandtech.map.view.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * 带网格线的GridView
 * @author yft
 *
 */

public class LineGridView extends GridView {

	public LineGridView(Context context) {
		super(context);
	}

	public LineGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LineGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if(getChildAt(0)!=null){
			View view=getChildAt(0);//获取第一个item
			int column=getWidth()/view.getWidth();//计算GridView的列数
			int childCount=getChildCount();//获取item的数量
			Paint paint=new Paint();//画笔
			paint.setStyle(Paint.Style.STROKE);//画实线
			paint.setColor(getContext().getResources().getColor(android.R.color.black));//设置画笔的颜色
			for(int i=0;i<childCount;i++){
				View mView=getChildAt(i);//获取第i个item
				if((i+1)%column==0){ //最后列的item 只绘制底边线
					canvas.drawLine(mView.getLeft(), mView.getBottom(), mView.getRight(), mView.getBottom(), paint);
				}else if((i+1)>(childCount-(childCount%column))){//绘制竖线
					canvas.drawLine(mView.getRight(), mView.getTop(), mView.getRight(), mView.getBottom(), paint);
				}else{
					canvas.drawLine(mView.getRight(), mView.getTop(), mView.getRight(), mView.getBottom(), paint);
					canvas.drawLine(mView.getLeft(), mView.getBottom(), mView.getRight(), mView.getBottom(), paint);
				}
			}

			if(childCount%column!=0){//缺少的 补齐  以保证美观
				for(int i=0;i<(column-childCount%column);i++){
					View lastView=getChildAt(childCount-1);//获取GridView中最后一个item
					//补齐缺失item的竖线
					canvas.drawLine(lastView.getRight()+lastView.getWidth()*i, lastView.getTop(), lastView.getRight()+lastView.getWidth()*i, lastView.getBottom(), paint);
				}
			}
		}
	}


}
