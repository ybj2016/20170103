package com.grandtech.map.gis.mapmanager;

import com.grandtech.map.utils.enmus.EnumGDBTask;

/**
 * gdb数据上传下载状态
 * Created by zy on 2016/12/22.
 */

public interface ITaskCallBack {
    public void before(EnumGDBTask enumGDBTask);

    public void processing(EnumGDBTask enumGDBTask);

    public void success(EnumGDBTask enumGDBTask);

    public void error(EnumGDBTask enumGDBTask);
}
