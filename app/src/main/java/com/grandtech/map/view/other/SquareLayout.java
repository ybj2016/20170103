package com.grandtech.map.view.other;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 可以根据不同设备自适应宽高的布局  （宽度=高度）
 * @author yft
 *
 */
public class SquareLayout extends LinearLayout {

	public SquareLayout(Context context) {
		super(context);
	}

	public SquareLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/*public SquareLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}*/

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		//定义当前view的大小
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

		int childWidthSize=getMeasuredWidth();
		int childHeightSize=getMeasuredHeight();

		//定义高度和宽度一致
		heightMeasureSpec=widthMeasureSpec= MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
