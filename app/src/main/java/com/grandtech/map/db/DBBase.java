package com.grandtech.map.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grandtech.map.entity.MobileUser;
import com.grandtech.map.utils.commons.CursorHelper;

import java.util.List;

/**
 * Created by zy on 2016/11/23.
 */

public class DBBase<T> implements IDBBase<T> {

    protected SQLiteDatabase connection = null;
    protected Cursor cursor = null;
    protected Context context;


    public DBBase(Context context) {
        this.context = context;
    }

    @Override
    public SQLiteDatabase getGdbConnection(String path) {
        return SQLiteConnectionFactory.getGdbConnection(path);
    }

    @Override
    public SQLiteDatabase getWritableConnection(Context context, String path) {
        return SQLiteConnectionFactory.getWritableConnection(context, path);
    }

    @Override
    public SQLiteDatabase getReadableConnection(Context context, String path) {
        return SQLiteConnectionFactory.getReadableConnection(context, path);
    }

    @Override
    public void closeAll(SQLiteDatabase connection, Cursor cursor) {
        SQLiteConnectionFactory.closeAll(connection, cursor);
    }

    @Override
    public boolean save(T t) {
        return false;
    }

    @Override
    public boolean update(T t) {
        return false;
    }

    @Override
    public boolean delete(T t) {
        return false;
    }

    @Override
    public T getT(T t) {
        return null;
    }

    protected T cursor2Entity(Cursor cursor,Class clazz){
        return (T) CursorHelper.cursor2Entity(cursor,clazz);
    }

    protected List<T> cursor2List(Cursor cursor,Class clazz){
        return (List<T>) CursorHelper.cursor2Entity(cursor,clazz);
    }

    @Override
    public boolean deleteRecordById(String id) {
        return false;
    }

    @Override
    public T getRecordById(String id) {
        return null;
    }

    @Override
    public List<T> getAllRecords() {
        return null;
    }

    @Override
    public List<T> getTopRecords(String keyword) {
        return null;
    }

    @Override
    public List<T> getTopRecords(String keyword, int num) {
        return null;
    }

    @Override
    public List<T> getTopRecords(int num) {
        return null;
    }

    @Override
    public T getItemByName(String name) {
        return null;
    }

    @Override
    public T cursor2Bean(Cursor cursor) {
        return null;
    }
}
