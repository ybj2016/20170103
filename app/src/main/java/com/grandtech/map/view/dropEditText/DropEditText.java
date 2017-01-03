package com.grandtech.map.view.dropEditText;

import android.R.color;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.grandtech.map.R;
import com.grandtech.map.utils.commons.DialogHelper;
import com.grandtech.map.utils.commons.VibratorHelper;

public class DropEditText extends FrameLayout implements View.OnClickListener, OnItemClickListener, AdapterView.OnItemLongClickListener {
	private Context mContext;
	private EditText mEditText;  // 输入框
	private ImageView mDropImage; // 右边的图片按钮
	private PopupWindow mPopup; // 点击图片弹出popupwindow
	private float dropHeight;//下拉框的高度
	private WrapListView mPopView; // popupwindow的布局
	private TextView.OnEditorActionListener onEditorActionListener;
	private OnDropItemCallBack onDropItemCallBack;
	private int mDrawableLeft;
	private int mDropMode; // flow_parent or wrap_content
	private String mHit;
	
	public DropEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public DropEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs);
	}

	private void init(Context context,AttributeSet attrs){
		mContext=context;
		LayoutInflater.from(context).inflate(R.layout.drop_edit_text_layout, this);
		mPopView = (WrapListView) LayoutInflater.from(context).inflate(R.layout.drop_edit_text_pop_view, null);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DropEditText);
		if (ta != null) {
			mDrawableLeft = ta.getResourceId(R.styleable.DropEditText_drawableRight, R.drawable.drop);
			mDropMode = ta.getInt(R.styleable.DropEditText_dropMode, 0);
			mHit = ta.getString(R.styleable.DropEditText_hint);
			dropHeight = ta.getDimension(R.styleable.DropEditText_dropHeight, LinearLayout.LayoutParams.WRAP_CONTENT);
			ta.recycle();
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mEditText = (EditText) findViewById(R.id.dropview_edit);
		mDropImage = (ImageView) findViewById(R.id.dropview_image);
		
		mEditText.setSelectAllOnFocus(true);
		mDropImage.setImageResource(mDrawableLeft);
	
		if(!TextUtils.isEmpty(mHit)) {
			mEditText.setHint(mHit);
		}
		mDropImage.setOnClickListener(this);
		mPopView.setOnItemClickListener(this);
		mPopView.setOnItemLongClickListener(this);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		// 如果布局发生改
		// 并且dropMode是flower_parent
		// 则设置ListView的宽度
		if(changed && 0 == mDropMode) {
			mPopView.setListWidth(getMeasuredWidth());
		}
	}
	
	/**
	 * 设置Adapter
	 * @param adapter ListView的Adapter
	 */
	private BaseAdapter adapter;
	public void setAdapter(BaseAdapter adapter) {
		this.adapter=adapter;
		mPopView.setAdapter(adapter);
		mPopup = new PopupWindow(mPopView, LinearLayout.LayoutParams.WRAP_CONTENT, (int) dropHeight);
		mPopup.setBackgroundDrawable(new ColorDrawable(color.transparent));
		mPopup.setFocusable(true); // 让popwin获取焦点
	}

	public BaseAdapter getAdapter(){
		return adapter;
	}

	public void setOnEditorActionListener(TextView.OnEditorActionListener onEditorActionListener){
		this.onEditorActionListener=onEditorActionListener;
		mEditText.setOnEditorActionListener(onEditorActionListener);//对输入键盘的一些监听
	}
	
	/**
	 * 获取输入框内的内容
	 * @return String content
	 */
	public String getText() {
		return mEditText.getText().toString();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.dropview_image) {
			if(mPopup.isShowing()) {
				mPopup.dismiss();
				return;
			}
			
			mPopup.showAsDropDown(this, 0, 0);
		}
	}

	public EditText getEditText(){
		return mEditText;
	}

	public void setOnDropItemClick(OnDropItemCallBack onDropItemCallBack){
		this.onDropItemCallBack=onDropItemCallBack;
	}
	public interface OnDropItemCallBack{
		public void onDropItemClick(AdapterView<?> parent, View view, int position,long id);
		public void onDropItemLongClickDelete(AdapterView<?> parent,int position);
	}

	private boolean isItemLongClick=false;

	@Override
	public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
		isItemLongClick=true;
		VibratorHelper.Vibrate(mContext,100);//震动提示
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage("确认删除？");
		builder.setTitle("提示");
		DialogHelper.dialog(builder,"确定","取消",new DialogHelper.ICallBack() {
			@Override
			public void dialogOk() {
				onDropItemCallBack.onDropItemLongClickDelete(parent,position);
			}

			@Override
			public void dialogCancel() {

			}
		});
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(!isItemLongClick) {
			mPopup.dismiss();
			if (onDropItemCallBack != null) {
				onDropItemCallBack.onDropItemClick(parent, view, position, id);
			}
		}
		isItemLongClick=false;
	}
}
