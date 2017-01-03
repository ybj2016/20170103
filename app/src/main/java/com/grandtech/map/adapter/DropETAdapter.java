package com.grandtech.map.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandtech.map.R;
import com.grandtech.map.entity.DkType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2016/11/18.
 */

public class DropETAdapter extends BAdapter<DkType> {


    public DropETAdapter(Context context) {
        super(context);
    }

    public DropETAdapter(Context context, List<DkType> list) {
        super(context, list);
    }

    @Override
    public int getContentView() {
        return R.layout.drop_edit_text_pop_view_item;
    }

    @Override
    public void onInitView(View view, int position,ViewGroup parent) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(parent.getWidth(), RelativeLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);//无法解决item 填充listView的补救措施
        TextView tv = get(view, R.id.tvListItem);
        tv.setText(getItem(position).getcName());
    }
}
