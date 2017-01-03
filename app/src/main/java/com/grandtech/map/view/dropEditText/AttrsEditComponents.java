package com.grandtech.map.view.dropEditText;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.grandtech.map.R;
import com.grandtech.map.adapter.BAdapter;
import com.grandtech.map.adapter.DropETAdapter;
import com.grandtech.map.dao.DkTypeDao;
import com.grandtech.map.entity.DkType;

import java.util.List;

/**
 * Created by zy on 2016/11/17.
 */

public class AttrsEditComponents implements TextView.OnEditorActionListener, DropEditText.OnDropItemCallBack {
    private Context context;
    private View view;
    private DropEditText dropEditText;
    private DkTypeDao dkTypeDao;
    private IEditAttr iEditAttr;

    public AttrsEditComponents(Context context,IEditAttr iEditAttr){
        this.context=context;
        this.iEditAttr=iEditAttr;
        initialize();
    }

    public View getAttrsEditComponentsView(){
        view.setVisibility(View.GONE);
        return view;
    }

    public void initialize(){
        dkTypeDao=new DkTypeDao(context);
        view = LayoutInflater.from(context).inflate(R.layout.activity_map_drap_edit_view, null);
        dropEditText= (DropEditText) view.findViewById(R.id.dropEditTextAttrs);
        dropEditText.setOnEditorActionListener(this);
        dropEditText.setOnDropItemClick(this);
        List<DkType> mList = dkTypeDao.getAllRecords();//获取用户输入的历史记录
        DropETAdapter dropETAdapter=new DropETAdapter(context,mList);
        dropEditText.setAdapter(dropETAdapter);
    }
    //点击回撤按钮执行保存到gdb数据库和db数据库
    private void save2db(){
        DkType dkType=new DkType();
        dkType.setcName(dropEditText.getText());
        int count = dkTypeDao.getCountByName(dkType);
        if(count==0){
            dkTypeDao.save(dkType);//执行保存
            ((BAdapter)dropEditText.getAdapter()).appendBefore(dkType);
        }
        if(iEditAttr!=null){
            iEditAttr.editAttr(dropEditText.getText());
        }
    }

    @Override
    public void onDropItemClick(AdapterView<?> parent, View view, int position, long id) {
        dropEditText.getEditText().setText(((DkType)parent.getAdapter().getItem(position)).getcName());
        if(iEditAttr!=null){
            iEditAttr.editAttr(dropEditText.getText());
        }
    }

    @Override
    public void onDropItemLongClickDelete(AdapterView<?> parent,int position) {
        DkType dkType=new DkType();
        dkType.setcName(((DkType)parent.getAdapter().getItem(position)).getcName());
        dkTypeDao.delete(dkType);//先删除相同的
        ((BAdapter)dropEditText.getAdapter()).removeByPosition(position);//
    }

    @Override
    public boolean onEditorAction(TextView v, int keyCode, KeyEvent event){
        boolean isOK = true;
        switch (keyCode) {
            case EditorInfo.IME_ACTION_NONE:
                Toast.makeText(context, "点击-->NONE", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_GO:
                Toast.makeText(context, "点击-->GO", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                Toast.makeText(context, "点击-->SEARCH", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_SEND:
                Toast.makeText(context, "点击-->SEND", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_NEXT:
                Toast.makeText(context, "点击-->NEXT", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_DONE:
                //Toast.makeText(context, "点击-->回车", Toast.LENGTH_SHORT).show();
                save2db();
                break;
            default:
                isOK = false;
                break;
        }
        return false;
    }

    public interface IEditAttr{
        public void editAttr(String name);
    }
}
