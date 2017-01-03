package com.grandtech.map.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.grandtech.map.entity.DkType;
import com.grandtech.map.entity.Jzzb;
import com.grandtech.map.entity.MobileUser;
import com.grandtech.map.entity.Track;
import com.grandtech.map.utils.commons.PathConfig;

/**
 * Created by zy on 2016/11/18.
 * 数据库操作类
 */

public class DBHelper extends SDSQLiteOpenHelper {
    public final static String DB_PATH = PathConfig.ROOT_FOLDER_SQLITE + "/";
    public final static String DB_NAME = "db_gykj.db";//国源科技数据库
    private final static int VERSION = 4;//暂时规定，有几个建表语句数据库版本号就更新为几
    private SQLiteDatabase db;
    private boolean defaultCreate = false;
    //以下是table的生成脚本
    //用户账号
    private final static String CREATE_TB_USER_ACCOUNT_TBL = "create table " + MobileUser.TB_MOBILE_USER + "(" + MobileUser.C_PhoneNum + " char(11) primary key, " + MobileUser.C_Pwd + " varchar(100), " + MobileUser.C_Name + " varchar(5), " + MobileUser.C_DateTime + " timestamp, " + MobileUser.C_State + " integer)";
    //地块类型
    private final static String CREATE_TB_DK_TYPE_TBL = "create table " + DkType.TB_DK_TYPE + "(" + DkType.C_ID + " integer primary key autoincrement, " + DkType.C_NAME + " text, " + DkType.C_DATETIME + " timestamp, " + DkType.C_CHOICECOUNT + " integer, " + DkType.C_DESC + " text)";
    //gps坐标
    private final static String CREATE_TB_GPS_TBL = "create table " + Track.TB_GPS + "(" + Track.C_ID + " integer primary key, " + Track.C_TRACK_NAME + " varchar(50), " + Track.C_COORDINATES + " text, " + Track.C_START_TIME + " timestamp, " + Track.C_END_TIME + " timestamp, " + Track.C_COUNT + " integer, " + Track.C_DESC + " varchar(50))";
    //基准坐标 fs
    private final static String CREATE_TB_JZZB_TBL = "create table " + Jzzb.TB_JZZB + " (" + Jzzb.C_ID + " integer primary key," + Jzzb.C_VERSION + " integer, " + Jzzb.C_COORDINATES + " text, " + Jzzb.C_CREATE_TIME + " timestamp," + Jzzb.C_MODIFY_TIME + " timestamp)";

    //SQLiteOpenHelper子类必须要的一个构造函数
    public DBHelper(Context context, String path, String name, CursorFactory factory, int version, boolean defaultCreate) {
        //必须通过super 调用父类的构造函数
        super(context, path, name, factory, version, defaultCreate);
    }

    //数据库的构造函数，传递三个参数的
    public DBHelper(Context context, String path, String name, int version, boolean defaultCreate) {
        this(context, path, name, null, version, defaultCreate);
    }

    //数据库的构造函数，传递一个参数的， 数据库名字和版本号都写死了
    public DBHelper(Context context, boolean defaultCreate) {
        this(context, DB_PATH, DB_NAME, null, VERSION, defaultCreate);
    }

    //数据库的构造函数，传递一个参数的， 数据库名字和版本号都写死了
    public DBHelper(Context context, boolean defaultCreate, int newVersion, int oldVersion) {
        this(context, DB_PATH, DB_NAME, null, VERSION, defaultCreate);
    }

    // 回调函数，第一次创建时才会调用此函数，创建一个数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        System.out.println("Create Database");
        db.execSQL(CREATE_TB_DK_TYPE_TBL);
        db.execSQL(CREATE_TB_GPS_TBL);
        db.execSQL(CREATE_TB_USER_ACCOUNT_TBL);
        db.execSQL(CREATE_TB_JZZB_TBL);
    }

    //数据升级(需要修改数据库版本号)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("update Database");
        db.execSQL(CREATE_TB_USER_ACCOUNT_TBL);
        db.execSQL(CREATE_TB_JZZB_TBL);
    }

}