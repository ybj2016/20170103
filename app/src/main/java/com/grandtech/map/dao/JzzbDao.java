package com.grandtech.map.dao;


import android.content.Context;
import android.database.Cursor;

import com.grandtech.map.db.DBBase;
import com.grandtech.map.db.DBHelper;
import com.grandtech.map.entity.Jzzb;
import com.grandtech.map.utils.commons.DateUtil;

import java.util.Date;

/**
 * Created by fs on 2016/12/28.
 */

public class JzzbDao extends DBBase<Jzzb> {

    private final String path = DBHelper.DB_PATH + DBHelper.DB_NAME;

    public JzzbDao(Context context) {
        super(context);
    }

    @Override
    public boolean delete(Jzzb jzzb) {
        boolean flag = false;
        try {
            //获得数据库连接
            connection = this.getWritableConnection(context, path);
            connection.beginTransaction();//开启事物
            String sql = "delete from " + Jzzb.TB_JZZB;
            connection.execSQL(sql);
            connection.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            connection.endTransaction();
            closeAll(connection, cursor);
        }
        return flag;
    }

    @Override
    public boolean update(Jzzb jzzb) {
        boolean flag = false;
        try {
            //获得数据库连接
            connection = this.getWritableConnection(context, path);
            connection.beginTransaction();//开启事物
            String sql = "update " + Jzzb.TB_JZZB + " set " + Jzzb.C_VERSION + "=" + Jzzb.C_VERSION + "+1," + Jzzb.C_COORDINATES + "='" + jzzb.getCoordinates() + "'," + Jzzb.C_MODIFY_TIME + "='" + DateUtil.dateTimeToStr(new Date()) + "'";
            connection.execSQL(sql);
            connection.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            connection.endTransaction();
            closeAll(connection, cursor);
        }
        return flag;
    }

    @Override
    public boolean save(Jzzb jzzb) {
        boolean flag = false;
        try {
            //获得数据库连接
            connection = this.getWritableConnection(context, path);
            connection.beginTransaction();//开启事物
            String sql = "insert into " + Jzzb.TB_JZZB + "(" + Jzzb.C_COORDINATES + "," + Jzzb.C_VERSION + "," + Jzzb.C_CREATE_TIME + "," + Jzzb.C_MODIFY_TIME + ") values('" + jzzb.getCoordinates() + "', 1 ,'" + DateUtil.dateTimeToStr(new Date()) + "','" + DateUtil.dateTimeToStr(new Date()) + "')";
            connection.execSQL(sql);
            connection.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            connection.endTransaction();
            closeAll(connection, cursor);
        }
        return flag;
    }

    @Override
    public Jzzb getT(Jzzb jzzb) {
        Cursor cursor = null;
        Jzzb jzzb1 = null;
        try {
            //获得数据库连接
            connection = this.getWritableConnection(context, path);
            connection.beginTransaction();//开启事物
            cursor = connection.rawQuery("select " + Jzzb.C_COORDINATES + " from " + Jzzb.TB_JZZB, null);
            connection.setTransactionSuccessful();
            jzzb1 = cursor2Entity(cursor, Jzzb.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.endTransaction();
            closeAll(connection, cursor);
        }
        return jzzb1;
    }
}
