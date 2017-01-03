package com.grandtech.map.dao;

import android.content.Context;

import com.grandtech.map.db.DBBase;
import com.grandtech.map.db.DBHelper;
import com.grandtech.map.entity.MobileUser;
import com.grandtech.map.utils.commons.CursorHelper;
import com.grandtech.map.utils.commons.DateUtil;
import com.grandtech.map.utils.commons.MD5AndDESUtil;

import java.util.List;

/**
 * Created by zy on 2016/12/22.
 */

public class MobileUserDao extends DBBase<MobileUser> {

    private final String path = DBHelper.DB_PATH + DBHelper.DB_NAME;

    public MobileUserDao(Context context) {
        super(context);
    }

    @Override
    public boolean save(MobileUser mobileUser) {
        boolean flag = false;
        try {
            //获得数据库连接
            connection = this.getWritableConnection(context, path);
            connection.beginTransaction();//开启事物
            StringBuffer sb = new StringBuffer("insert into ");
            sb.append(MobileUser.TB_MOBILE_USER);
            sb.append(" (");
            sb.append(MobileUser.C_PhoneNum);
            sb.append(",");
            sb.append(MobileUser.C_Pwd);
            sb.append(",");
            sb.append(MobileUser.C_Name);
            sb.append(",");
            sb.append(MobileUser.C_DateTime);
            sb.append(",");
            sb.append(MobileUser.C_State);
            sb.append(")values(");
            sb.append(mobileUser.getPhoneNum());
            sb.append(",'");
            sb.append(mobileUser.getPwd());
            sb.append("','");
            sb.append(mobileUser.getName());
            sb.append("','");
            sb.append(DateUtil.dateTimeToStr(mobileUser.getDateTime()));
            sb.append("','");
            sb.append(mobileUser.getState());
            sb.append("')");
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

    public boolean saveOrUpdate(MobileUser mobileUser) {
        boolean flag = false;
        try {
            if (checkUser(mobileUser)) {
                flag = update(mobileUser);
            } else {
                flag = save(mobileUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean update(MobileUser mobileUser) {
        boolean flag = false;
        try {
            //获得数据库连接
            connection = this.getWritableConnection(context, path);
            connection.beginTransaction();//开启事物
            StringBuffer sb = new StringBuffer("update ");
            sb.append(MobileUser.TB_MOBILE_USER);
            sb.append(" set ");
            sb.append(MobileUser.C_Pwd);
            sb.append(" = '");
            sb.append(mobileUser.getPwd());
            sb.append("',");
            sb.append(MobileUser.C_Name);
            sb.append(" = '");
            sb.append(mobileUser.getName());
            sb.append("',");
            sb.append(MobileUser.C_DateTime);
            sb.append(" = '");
            sb.append(DateUtil.dateTimeToStr(mobileUser.getDateTime()));
            sb.append("',");
            sb.append(MobileUser.C_State);
            sb.append(" = ");
            sb.append(mobileUser.getState());
            sb.append(" where ");
            sb.append(MobileUser.C_PhoneNum);
            sb.append(" = '");
            sb.append(mobileUser.getPhoneNum());
            sb.append("'");
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
    public boolean delete(MobileUser mobileUser) {
        return super.delete(mobileUser);
    }

    /**
     * 检查用户
     *
     * @param mobileUser
     * @return
     */
    public boolean checkUser(MobileUser mobileUser) {
        boolean flag = false;
        try {
            //获得数据库连接
            connection = this.getReadableConnection(context, path);
            connection.beginTransaction();//开启事物
            StringBuffer sb = new StringBuffer("select count(" + MobileUser.C_PhoneNum + ")");
            sb.append(" from ");
            sb.append(MobileUser.TB_MOBILE_USER);
            sb.append(" where ");
            sb.append(MobileUser.C_PhoneNum);
            sb.append(" = '");
            sb.append(mobileUser.getPhoneNum());
            sb.append("' and ");
            sb.append(MobileUser.C_Pwd);
            sb.append(" = '");
            sb.append(mobileUser.getPwd());
            sb.append("'");
            cursor = connection.rawQuery(sb.toString(), null);
            cursor.moveToFirst();
            connection.setTransactionSuccessful();
            int i = (int) cursor.getLong(0);
            if (i > 0) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            connection.endTransaction();
            closeAll(connection, cursor);
        }
        return flag;
    }

    public MobileUser getByPhoneAndPwd(MobileUser mobileUser) {
        MobileUser user = null;
        try {
            //获得数据库连接
            connection = this.getReadableConnection(context, path);
            connection.beginTransaction();//开启事物
            StringBuffer sb = new StringBuffer("select *");
            sb.append(" from ");
            sb.append(MobileUser.TB_MOBILE_USER);
            sb.append(" where ");
            sb.append(MobileUser.C_PhoneNum);
            sb.append(" = '");
            sb.append(mobileUser.getPhoneNum());
            sb.append("' and ");
            sb.append(MobileUser.C_Pwd);
            sb.append(" = '");
            sb.append(MD5AndDESUtil.md5Encrypt(mobileUser.getPwd()));
            sb.append("'");
            cursor = connection.rawQuery(sb.toString(), null);
            cursor.moveToFirst();
            connection.setTransactionSuccessful();
            user = (MobileUser) CursorHelper.cursor2Json2Entity(cursor, MobileUser.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            connection.endTransaction();
            closeAll(connection, cursor);
        }
        return user;
    }
}
