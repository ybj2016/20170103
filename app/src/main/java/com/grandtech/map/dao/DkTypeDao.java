package com.grandtech.map.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grandtech.map.db.DBBase;
import com.grandtech.map.db.DBHelper;
import com.grandtech.map.db.IDBBase;
import com.grandtech.map.db.SQLiteConnectionFactory;
import com.grandtech.map.entity.DkType;
import com.grandtech.map.utils.commons.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zy on 2016/11/18.
 *
 */

public class DkTypeDao extends DBBase<DkType> {

    private final String path = DBHelper.DB_PATH + DBHelper.DB_NAME;

    public DkTypeDao(Context context) {
        super(context);
    }

    public int getCountByName(DkType dkType) {
        int flag = 0;
        SQLiteDatabase connection = null;
        Cursor cursor;
        try {
            //获得数据库连接
            connection = this.getReadableConnection(context, path);
            StringBuffer sql = new StringBuffer("select " + DkType.C_ID + " from ");
            sql.append(DkType.TB_DK_TYPE);
            sql.append(" where ");
            sql.append(DkType.C_NAME);
            sql.append(" = '");
            sql.append(dkType.getcName());
            sql.append("'");
            cursor = connection.rawQuery(sql.toString(), null);
            flag = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(connection, null);
        }
        return flag;
    }

    @Override
    public boolean save(DkType dkType) {
        boolean flag = false;
        SQLiteDatabase connection = null;
        try {
            //获得数据库连接
            connection = this.getWritableConnection(context, path);
            ContentValues values = new ContentValues();
            values.put(DkType.C_NAME, dkType.getcName());
            values.put(DkType.C_CHOICECOUNT, 1);
            values.put(DkType.C_DATETIME, String.valueOf(new Date()));
            connection.insert(DkType.TB_DK_TYPE, null, values);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(connection, null);
        }
        return flag;
    }

    @Override
    public boolean update(DkType dkType) {
        return false;
    }

    @Override
    public boolean delete(DkType dkType) {
        boolean flag = false;
        SQLiteDatabase connection = null;
        try {
            //获得数据库连接
            connection = this.getWritableConnection(context, path);
            connection.delete(DkType.TB_DK_TYPE, DkType.C_NAME + "= '" + dkType.getcName() + "'", null);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(connection, null);
        }
        return flag;
    }

    @Override
    public boolean deleteRecordById(String id) {
        boolean flag = false;
        SQLiteDatabase connection = null;
        try {
            //获得数据库连接
            connection = getWritableConnection(context, path);
            connection.delete(DkType.TB_DK_TYPE, DkType.C_ID + "=" + id, null);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(connection, null);
        }
        return flag;
    }

    @Override
    public DkType getRecordById(String id) {
        return null;
    }

    @Override
    public List<DkType> getAllRecords() {
        List<DkType> list = new ArrayList<DkType>();
        SQLiteDatabase connection = null;
        Cursor cursor = null;
        try {
            connection = this.getReadableConnection(context, path);
            StringBuffer sql = new StringBuffer("select * from ");
            sql.append(DkType.TB_DK_TYPE);
            sql.append(" order by ");
            sql.append(DkType.C_ID);
            sql.append(" desc");
            cursor = connection.rawQuery(sql.toString(), null);
            if (cursor != null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    DkType dkType = cursor2Bean(cursor);
                    if (dkType != null) {
                        list.add(dkType);
                    }
                    ;
                }
                ;
            }
            ;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(connection, cursor);
        }
        return list;
    }

    @Override
    public List<DkType> getTopRecords(String keyword) {
        return null;
    }

    @Override
    public List<DkType> getTopRecords(String keyword, int num) {
        return null;
    }

    @Override
    public List<DkType> getTopRecords(int num) {
        return null;
    }

    @Override
    public DkType getItemByName(String name) {
        return null;
    }

    @Override
    public DkType cursor2Bean(Cursor cursor) {
        DkType dkType = null;
        if (cursor != null) {
            dkType = new DkType();
            dkType.setcId(cursor.getInt(cursor.getColumnIndex(DkType.C_ID)));
            dkType.setcName(cursor.getString(cursor.getColumnIndex(DkType.C_NAME)));
            dkType.setcChoiceCount(cursor.getInt(cursor.getColumnIndex(DkType.C_CHOICECOUNT)));
            dkType.setcDateTime(DateUtil.strToDate(cursor.getString(cursor.getColumnIndex(DkType.C_DATETIME))));
            dkType.setcDesc(cursor.getString(cursor.getColumnIndex(DkType.C_DESC)));
        }
        return dkType;
    }

    ;
}
