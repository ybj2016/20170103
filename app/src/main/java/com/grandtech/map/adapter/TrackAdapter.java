package com.grandtech.map.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.grandtech.map.R;
import com.grandtech.map.dao.TrackDao;
import com.grandtech.map.entity.Track;
import com.grandtech.map.gis.mapcore.MapContext;
import com.grandtech.map.utils.commons.AppHelper;
import com.grandtech.map.utils.commons.DialogHelper;
import com.grandtech.map.utils.commons.VibratorHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2016/11/29.
 */

public class TrackAdapter extends BAdapter<Track> {

    private List<Integer> checkItems;//选中的项
    private Context context;
    private TrackDao trackDao;
    private ItemUiUpdate itemUiUpdate;
    private MapContext mapContext;

    public TrackAdapter(Context context, List list, ItemUiUpdate itemUiUpdate) {
        super(context, list);
        this.context = context;
        this.itemUiUpdate = itemUiUpdate;
        trackDao = new TrackDao(context);
        mapContext = MapContext.getInstance(null);
        checkItems = mapContext.getCheckItems();
        if(checkItems==null){
            checkItems = new ArrayList<>();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.track_list_item;
    }

    @Override
    public void onInitView(View view, final int position, ViewGroup parent) {
        final CheckBox cbTrack = get(view, R.id.cbTrack);
        TextView tvLocating = get(view, R.id.tvLocating);
        TextView tvTrackName = get(view, R.id.tvTrackName);
        TextView tvTrackCount = get(view, R.id.tvTrackCount);
        TextView tvTrackStart = get(view, R.id.tvTrackStart);
        TextView tvTrackEnd = get(view, R.id.tvTrackEnd);
        final Track track = getItem(position);
        tvTrackName.setText(track.getcTrackName());
        tvTrackCount.setText(Integer.toString(track.getcCount()));
        tvTrackStart.setText(track.getcStartTime());
        tvTrackEnd.setText(track.getcEndTime());
        if (checkItems.contains(position)) {
            cbTrack.setChecked(true);//会触发 setOnCheckedChangeListener事件所有不要用 setOnCheckedChangeListener去监听 否则无法区分是点击触发还是 代码触发
        } else {
            cbTrack.setChecked(false);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbTrack.isChecked()) {
                    cbTrack.setChecked(true);
                    checkItems.add(new Integer(position));//注意不能是基本类型
                } else {
                    cbTrack.setChecked(false);
                    checkItems.remove(new Integer(position));
                }
            }
        });
        if (AppHelper.isProcessRunning(context, "com.grandtech.map.services.LocationServiceAps") && position == 0) {
            tvLocating.setVisibility(View.VISIBLE);
            tvTrackCount.setText("");
            view.setOnLongClickListener(null);//取消监听
            if (itemUiUpdate != null) {
                itemUiUpdate.update(tvTrackCount);
            }
        } else {
            tvLocating.setVisibility(View.GONE);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    VibratorHelper.Vibrate(context, 150);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("确认删除这" + checkItems.size() + "个选中项？");
                    builder.setTitle("提示");
                    DialogHelper.dialog(builder, "确定", "取消", new DialogHelper.ICallBack() {
                        @Override
                        public void dialogOk() {
                            List<Track> tracks = getSelectTrack();
                            trackDao.batchDelete(tracks);//先从数据库里移除
                            for (int i = 0; i < tracks.size(); i++) {//再从adapter缓存里清除
                                Track t = tracks.get(i);
                                removeByObject(t);//从adapter里移除
                            }
                            checkItems.clear();
                        }

                        @Override
                        public void dialogCancel() {

                        }
                    });
                    return true;//拦截点击事件
                }
            });
        }
    }

    /**
     * 获取选中的轨迹列表
     *
     * @return
     */
    public List<Track> getSelectTrack() {
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < checkItems.size(); i++) {
            int k = checkItems.get(i);
            Track track = list.get(k);
            tracks.add(track);
        }
        mapContext.setCheckItems(checkItems);//存入全局变量
        return tracks;
    }

    /**
     * 动态更新某个ui
     */
    public interface ItemUiUpdate {
        public void update(View view);
    }
}
