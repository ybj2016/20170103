package com.grandtech.map.utils.commons;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grandtech.map.entity.MobileUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 游标转实体
 * Created by zy on 2016/12/23.
 */

public final class CursorHelper {

    private CursorHelper() {
    }

    /**
     * 游标转json
     *
     * @param cursor
     * @return
     */
    public static JSONArray cursor2Json(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        String key = cursor.getColumnName(i);
                        if (cursor.getString(i) != null) {
                            Log.d("TAG_NAME", cursor.getString(i));
                            rowObject.put(key, cursor.getString(i));
                        } else {
                            rowObject.put(key, "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("TAG_NAME", resultSet.toString());
        return resultSet;
    }

    /**
     * 借助第三方转化
     *
     * @param cursor
     * @param clazz
     * @return
     */
    public static Object cursor2Json2Entity(Cursor cursor, Class clazz) {
        Object object = null;
        String jsonStr = null;
        try {
            jsonStr = cursor2Json(cursor).get(0).toString();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            object = gson.fromJson(jsonStr, clazz);//转json
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }


    //=========================以下是抄的=============================

    /**
     * cursor转实体
     *
     * @param c
     * @param clazz
     * @return
     */
    public static Object cursor2Entity(Cursor c, Class clazz) {
        return cursor2VO(c, clazz);
    }

    /**
     * cursor转实体
     *
     * @param c
     * @param clazz
     * @return
     */
    public static Object cursor2List(Cursor c, Class clazz) {
        return cursor2VOList(c, clazz);
    }

    /**
     * 通过SQL语句获得对应的VO。注意：Cursor的字段名或者别名一定要和VO的成员名一样
     *
     * @param db
     * @param sql
     * @param clazz vo class
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object sql2VO(SQLiteDatabase db, String sql, Class clazz) {
        Cursor c = db.rawQuery(sql, null);
        return cursor2VO(c, clazz);
    }

    /**
     * 通过SQL语句获得对应的VO。注意：Cursor的字段名或者别名一定要和VO的成员名一样
     *
     * @param db
     * @param sql
     * @param selectionArgs
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object sql2VO(SQLiteDatabase db, String sql,
                                String[] selectionArgs, Class clazz) {
        Cursor c = db.rawQuery(sql, selectionArgs);
        return cursor2VO(c, clazz);
    }

    /**
     * 通过SQL语句获得对应的VO的List。注意：Cursor的字段名或者别名一定要和VO的成员名一样
     *
     * @param db
     * @param sql
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List sql2VOList(SQLiteDatabase db, String sql, Class clazz) {

        Cursor c = db.rawQuery(sql, null);

        return cursor2VOList(c, clazz);
    }

    /**
     * 通过SQL语句获得对应的VO的List。注意：Cursor的字段名或者别名一定要和VO的成员名一样
     *
     * @param db
     * @param sql
     * @param selectionArgs
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List sql2VOList(SQLiteDatabase db, String sql,
                                  String[] selectionArgs, Class clazz) {
        Cursor c = db.rawQuery(sql, selectionArgs);
        return cursor2VOList(c, clazz);
    }

    /**
     * 通过Cursor转换成对应的VO。注意：Cursor里的字段名（可用别名）必须要和VO的属性名一致
     *
     * @param c
     * @param clazz
     * @return
     */
    @SuppressWarnings({"rawtypes", "unused"})
    private static Object cursor2VO(Cursor c, Class clazz) {
        if (c == null) {
            return null;
        }
        Object obj;
        int i = 1;
        try {
            c.moveToNext();
            obj = setValues2Fields(c, clazz);

            return obj;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("ERROR @：cursor2VO");
            return null;
        } finally {
            c.close();
        }
    }

    /**
     * 通过Cursor转换成对应的VO集合。注意：Cursor里的字段名（可用别名）必须要和VO的属性名一致
     *
     * @param c
     * @param clazz
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List cursor2VOList(Cursor c, Class clazz) {
        if (c == null) {
            return null;
        }
        List list = new LinkedList();
        Object obj;
        try {
            while (c.moveToNext()) {
                obj = setValues2Fields(c, clazz);

                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR @：cursor2VOList");
            return null;
        } finally {
            c.close();
        }
    }

    /**
     * 把值设置进类属性里
     *
     * @param c
     * @param clazz
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private static Object setValues2Fields(Cursor c, Class clazz) throws Exception {

        String[] columnNames = c.getColumnNames();// 字段数组
        //init a instance from the VO`s class
        Object obj = clazz.newInstance();
        //return a field array from obj`s ALL(include private exclude inherite(from father)) field
        Field[] fields = clazz.getDeclaredFields();

        for (Field _field : fields) {
            //field`s type
            Class<? extends Object> typeClass = _field.getType();// 属性类型
            for (int j = 0; j < columnNames.length; j++) {
                String columnName = columnNames[j];
                typeClass = getBasicClass(typeClass);
                //if typeclass is basic class ,package.if not,no change
                boolean isBasicType = isBasicType(typeClass);
                if (isBasicType) {
                    String attr = _field.getName();
                    if (columnName.equalsIgnoreCase(attr)) {// 是基本类型(我的字段有前缀c)
                        String _str = c.getString(c.getColumnIndex(columnName));
                        if (_str == null) {
                            break;
                        }
                        _str = _str == null ? "" : _str;
                        //if value is null,make it to ""
                        //use the constructor to init a attribute instance by the value
                        Constructor<? extends Object> cons = typeClass.getConstructor(String.class);
                        Object attribute = cons.newInstance(_str);
                        _field.setAccessible(true);
                        //give the obj the attr
                        _field.set(obj, attribute);
                        break;
                    }
                } else {
                    Object obj2 = setValues2Fields(c, typeClass);// 递归
                    _field.set(obj, obj2);
                    break;
                }

            }
        }
        return obj;
    }

    /**
     * 判断是不是基本类型
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static boolean isBasicType(Class typeClass) {
        if (typeClass.equals(Integer.class) || typeClass.equals(Long.class)
                || typeClass.equals(Float.class)
                || typeClass.equals(Double.class)
                || typeClass.equals(Boolean.class)
                || typeClass.equals(Byte.class)
                || typeClass.equals(Short.class)
                || typeClass.equals(String.class)) {

            return true;

        } else {
            return false;
        }
    }

    /**
     * 获得包装类
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("all")
    public static Class<? extends Object> getBasicClass(Class typeClass) {
        Class _class = basicMap.get(typeClass);
        if (_class == null)
            _class = typeClass;
        return _class;
    }

    @SuppressWarnings("rawtypes")
    private static Map<Class, Class> basicMap = new HashMap<Class, Class>();

    static {
        basicMap.put(int.class, Integer.class);
        basicMap.put(long.class, Long.class);
        basicMap.put(float.class, Float.class);
        basicMap.put(double.class, Double.class);
        basicMap.put(boolean.class, Boolean.class);
        basicMap.put(byte.class, Byte.class);
        basicMap.put(short.class, Short.class);
    }
}
