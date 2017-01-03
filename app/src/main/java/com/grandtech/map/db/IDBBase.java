package com.grandtech.map.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;


/**
 * Created by zy on 2016/11/18.
 */

public interface IDBBase<T> {

    /**
     * 获取gdb数据库链接
     */
    public SQLiteDatabase getGdbConnection(String path);
    /**
     * 获得数据库连接
     * @return
     */
    public SQLiteDatabase getWritableConnection(Context context, String path);
    /**
     * 获得数据库连接
     * @return
     */
    public SQLiteDatabase getReadableConnection(Context context,String path);

    /**
     * 关闭数据库链接和游标
     * @param connection
     * @param cursor
     */
    public void closeAll(SQLiteDatabase connection, Cursor cursor);

    /**
     * 保存
     * @param t
     * @return
     */
    public  boolean  save(T t);
    /**
     * 更新历史记录
     * @param t
     * @return
     */
    public  boolean  update(T t);
    /**
     * 删除历史记录
     * @param t
     * @return
     */
    public  boolean  delete(T t);

    /**
     * 获取一个实体
     * @param t
     * @return
     */
    public T getT(T t);
    /**
     * 根据历史记录id删除历史记录
     * @param id
     * @return
     */
    public boolean  deleteRecordById(String id);
    /**
     * 根据历史记录id查询历史记录
     * @param id
     * @return
     */
    public T getRecordById(String id);
    /**
     * 查询所有的历史记录
     * @return
     */
    public List<T> getAllRecords();
    /**
     * 更具关键字查找
     * */
    public List<T> getTopRecords(String keyword);
    /**
     * 获得前几条历史记录
     * */
    public List<T> getTopRecords(String keyword,int num);
    /**
     * 获得前几条历史记录
     * */
    public List<T> getTopRecords(int num);
    /**
     * 根据name搜索
     * @param name
     * @return
     */
    public T getItemByName(String name);

    public T cursor2Bean(Cursor cursor);
}
