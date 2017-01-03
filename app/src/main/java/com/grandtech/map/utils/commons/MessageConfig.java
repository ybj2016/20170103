package com.grandtech.map.utils.commons;

/**
 * Created by zy on 2016/11/17.
 */

public class MessageConfig {

    //下载gdb数据库
    public final static int DOWN_GDB_BEFORE=2001;
    public final static int DOWN_GDB_PROCESSING=2002;
    public final static int DOWN_GDB_SUCCESS=2003;
    public final static int DOWN_GDB_ERROR=2004;

    //上传gdb数据库
    public final static int SYNC_GDB_HANDLER=3000;
    public final static int SYNC_GDB_BEFORE=3001;
    public final static int SYNC_GDB_PROCESSING=3002;
    public final static int SYNC_GDB_SUCCESS=3003;
    public final static int SYNC_GDB_ERROR=3004;

    //查询结果回调监听
    public final static int QUERY_SUCCESS=4001;
    public final static int QUERY_ERROR=4002;

    private MessageConfig(){

    }
}
