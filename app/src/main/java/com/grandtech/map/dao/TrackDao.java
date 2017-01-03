package com.grandtech.map.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grandtech.map.db.DBBase;
import com.grandtech.map.db.DBHelper;
import com.grandtech.map.db.SQLiteConnectionFactory;
import com.grandtech.map.entity.DkType;
import com.grandtech.map.entity.Track;
import com.grandtech.map.utils.commons.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2016/11/24.
 */

public class TrackDao extends DBBase<Track> {

    private final String path = DBHelper.DB_PATH + DBHelper.DB_NAME;

    public TrackDao(Context context) {
        super(context);
    }

    /**
     * 从数据库里获取记录最大的id
     *
     * @return
     */
    public int getId() {
        int flag = -1;
        try {
            connection = this.getReadableConnection(context, path);
            StringBuffer sb = new StringBuffer("select max(" + Track.C_ID + ") from " + Track.TB_GPS + "");
            cursor = connection.rawQuery(sb.toString(), null);
            cursor.moveToFirst();
            flag = (int) cursor.getLong(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(connection, cursor);
        }
        return flag;
    }

    public int saveTrack(Track track) {
        int id = getId() + 1;
        try {
            //获得数据库连接
            connection = this.getWritableConnection(context, path);
            connection.beginTransaction();//开启事物
            StringBuffer sb = new StringBuffer("insert into ");
            sb.append(Track.TB_GPS);
            sb.append(" (");
            sb.append(Track.C_ID);
            sb.append(",");
            sb.append(Track.C_TRACK_NAME);
            sb.append(",");
            sb.append(Track.C_COORDINATES);
            sb.append(",");
            sb.append(Track.C_COUNT);
            sb.append(",");
            sb.append(Track.C_START_TIME);
            sb.append(",");
            sb.append(Track.C_END_TIME);
            sb.append(",");
            sb.append(Track.C_DESC);
            sb.append(")values(");
            sb.append(id);
            sb.append(",'");
            sb.append(track.getcTrackName());
            sb.append("','");
            sb.append(track.getcCoordinates());
            sb.append("',");
            sb.append(track.getcCount());
            sb.append(",'");
            sb.append(track.getcStartTime());
            sb.append("','");
            sb.append(track.getcEndTime());
            sb.append("','");
            sb.append(track.getcDesc());
            sb.append("')");
            connection.execSQL(sb.toString());
            connection.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            id = -1;
        } finally {
            connection.endTransaction();
            closeAll(connection, cursor);
        }
        return id;
    }

    @Override
    public boolean update(Track track) {
        boolean flag = false;
        try {
            //获得数据库连接
            connection = this.getWritableConnection(context, path);
            connection.beginTransaction();//开启事物
            StringBuffer sb = new StringBuffer("update ");
            sb.append(Track.TB_GPS);
            sb.append(" set ");
            sb.append(Track.C_COORDINATES);
            sb.append(" = '");
            sb.append(track.getcCoordinates());
            sb.append("',");
            sb.append(Track.C_COUNT);
            sb.append(" = ");
            sb.append(track.getcCount());
            sb.append(",");
            sb.append(Track.C_END_TIME);
            sb.append(" = '");
            sb.append(track.getcEndTime());
            sb.append("' where ");
            sb.append(Track.C_ID);
            sb.append(" = ");
            sb.append(track.getcId());
            connection.execSQL(sb.toString());
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
    public List<Track> getAllRecords() {
        List<Track> list = null;
        try {
            //获得数据库连接
            list = new ArrayList<Track>();
            connection = this.getReadableConnection(context, path);
            connection.beginTransaction();//开启事物
            StringBuffer sql = new StringBuffer("select * from ");
            sql.append(Track.TB_GPS);
            sql.append(" order by ");
            sql.append(Track.C_START_TIME);
            sql.append(" desc");
            cursor = connection.rawQuery(sql.toString(), null);
            if (cursor != null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Track track = cursor2Bean(cursor);
                    if (track != null) {
                        list.add(track);
                    }
                }
            }
            connection.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.endTransaction();
            closeAll(connection, cursor);
        }
        return list;
    }

    public Track getRecordById(Integer id) {
        Track track = null;
        try {
            connection = this.getReadableConnection(context, path);//获得数据库连接
            connection.beginTransaction();//开启事物
            StringBuffer sql = new StringBuffer("select * from ");
            sql.append(Track.TB_GPS);
            sql.append(" where ");
            sql.append(Track.C_ID);
            sql.append(" = ");
            sql.append(id);
            cursor = connection.rawQuery(sql.toString(), null);
            if (cursor != null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    track = cursor2Bean(cursor);
                }
            }
            connection.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.endTransaction();
            closeAll(connection, cursor);
        }
        return track;
    }

    @Override
    public Track getRecordById(String id) {
        return super.getRecordById(id);
    }

    @Override
    public boolean delete(Track track) {
        boolean flag = false;
        try {
            connection = this.getWritableConnection(context, path);
            connection.beginTransaction();//开启事物
            StringBuffer sb = new StringBuffer("delete from ");
            sb.append(Track.TB_GPS);
            sb.append(" where ");
            sb.append(Track.C_ID);
            sb.append(" = ");
            sb.append(track.getcId());
            connection.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            flag = false;
        } finally {
            connection.endTransaction();
            closeAll(connection, cursor);
        }
        return flag;
    }

    public boolean batchDelete(List<Track> tracks) {
        boolean flag = false;
        try {
            connection = this.getWritableConnection(context, path);
            connection.beginTransaction();//开启事物
            StringBuffer ids = new StringBuffer();
            for (int i = 0; i < tracks.size(); i++) {
                ids.append(tracks.get(i).getcId());
                if (i != tracks.size() - 1) {
                    ids.append(",");
                }
            }
            StringBuffer sql = new StringBuffer("delete from ");
            sql.append(Track.TB_GPS);
            sql.append(" where ");
            sql.append(Track.C_ID);
            sql.append(" in ( ");
            sql.append(ids);
            sql.append(" )");
            connection.execSQL(sql.toString());
            connection.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            flag = false;
        } finally {
            connection.endTransaction();
            closeAll(connection, cursor);
        }
        return flag;
    }

    @Override
    public Track cursor2Bean(Cursor cursor) {
        Track track = null;
        if (cursor != null) {
            track = new Track();
            track.setcId(cursor.getInt(cursor.getColumnIndex(Track.C_ID)));
            track.setcTrackName(cursor.getString(cursor.getColumnIndex(Track.C_TRACK_NAME)));
            track.setcStartTime(cursor.getString(cursor.getColumnIndex(Track.C_START_TIME)));
            track.setcEndTime(cursor.getString(cursor.getColumnIndex(Track.C_END_TIME)));
            track.setcCoordinates(cursor.getString(cursor.getColumnIndex(Track.C_COORDINATES)));
            track.setcCount(cursor.getInt(cursor.getColumnIndex(Track.C_COUNT)));
            track.setcDesc(cursor.getString(cursor.getColumnIndex(Track.C_DESC)));
        }
        return track;
    }
}
