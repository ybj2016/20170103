package com.grandtech.map.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.grandtech.map.utils.commons.FileHelper;

/**
 * Created by zy on 2016/11/18.
 */
public final class SQLiteConnectionFactory {

	/**
	 * @param path
	 * @return
	 */
	public static SQLiteDatabase getGdbConnection(String path){
		final SQLiteDatabase connection=SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		return connection;
	}

	/**
	 *
	 * @param context
	 * @param path
     * @return
     */
	public static SQLiteDatabase getWritableConnection(Context context, String path){
		DBHelper dbHelper =new DBHelper(context,false);
		return dbHelper.getWritableDatabase();
	}

	/**
	 *
	 * @param context
	 * @param path
	 * @return
	 */
	public static SQLiteDatabase getReadableConnection(Context context, String path){
		DBHelper dbHelper =new DBHelper(context,false);
		return dbHelper.getReadableDatabase();
	}

	/**
	 * 关闭链接和游标
	 * @param connection
	 * @param cursor
     * @return
     */
	public static boolean closeAll(SQLiteDatabase connection, Cursor cursor){
		boolean flag=false;
		try{
			if(connection!=null){
				connection.close();
				connection=null;
			}
			if(cursor!=null){
				cursor.close();
				cursor=null;
			}

		}catch(Exception e){
			Log.e("close All:", e.toString());
		}
		return flag;
	}
}
