package com.grandtech.map.adapter;

/**
 * Created by zy on 2016/11/18.
 * adapter基类
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public abstract class BAdapter<T> extends BaseAdapter {

    protected List<T> list;
    private LayoutInflater inflater;
    private Context context;

    public BAdapter(Context context) {
        init(context, new ArrayList<T>());
    }

    public BAdapter(Context context, List<T> list) {
        init(context, list);
    }

    private void init(Context context, List<T> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.context=context;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        if (null != list) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 后面追加
     * @param t
     */
    public void appendLast(T t){
        if (null != t) {
            this.list.add(t);
            notifyDataSetChanged();
        }
    }

    /**
     * 前面追加
     * @param t
     */
    public void appendBefore(T t){
        if (null != t) {
            this.list.add(0,t);
            notifyDataSetChanged();
        }
    }

    /**
     * 根据位置移除
     * @param position
     */
    public void removeByPosition(int position){
        if (null != list) {
            this.list.remove(position);
            notifyDataSetChanged();
        }
    }
    /**
     * 根据Item对象移除
     * @param o
     */
    public void removeByObject(Object o){
        if (null != list) {
            this.list.remove(o);
            notifyDataSetChanged();
        }
    }

    //判断adapterView的最后一项是否完全显示
    public boolean isLastItemVisible(AdapterView adapterView) {
        if (null == this || this.isEmpty()) {
            return true;
        }
        final int lastItemPosition = this.getCount() - 1;
        final int lastVisiblePosition = adapterView.getLastVisiblePosition();
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - adapterView.getFirstVisiblePosition();
            final int childCount = adapterView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = adapterView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= adapterView.getBottom();
            }
        }
        return false;
    }

    //判断adapterView的指定项是否显示  完全不完全不管
    public boolean isPositionItemVisible(AdapterView adapterView,int position) {
        if (null == this || this.isEmpty()) {
            return true;
        }
        final int lastVisiblePosition = adapterView.getLastVisiblePosition();
        final int firstVisiblePosition = adapterView.getFirstVisiblePosition();
        if(getCount()==1) {
            if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
                return true;
            }
        }else {
            if (position >= firstVisiblePosition && position < lastVisiblePosition) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflate(parent,getContentView());
        }
        onInitView(convertView, position, parent);
        return convertView;
    }

    /** 加载布局 */
    private View inflate(ViewGroup parent,int layoutResID) {
        View view = inflater.inflate(layoutResID, null);
        return view;
    }

    public abstract int getContentView();

    public abstract void onInitView(View view, int position,ViewGroup parent);

    /**
     *
     * @param view
     *            converView
     * @param id
     *            控件的id
     * @return 返回<T extends View>
     */
    @SuppressWarnings("unchecked")
    protected <E extends View> E get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (null == viewHolder) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (null == childView) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);

        }
        return (E) childView;
    }

}
