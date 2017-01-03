package com.grandtech.map.dao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.query.QueryParameters;
import com.grandtech.map.activity.LoginActivity;
import com.grandtech.map.db.DBBase;
import com.grandtech.map.entity.Gdb;
import com.grandtech.map.entity.MobileUser;
import com.grandtech.map.utils.commons.DateUtil;
import com.grandtech.map.utils.commons.PathConfig;
import com.grandtech.map.utils.commons.SharedPreferencesHelper;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by zy on 2016/11/23.
 * gdb数据库操作 尝试
 */

public class GdbDao extends DBBase<Gdb> {

    private final static String path = PathConfig.DATAPATH_GEODATABASE;
    //private final static String TB_NAME = "SSZDKC_evw";
    private final static String TB_NAME = "ZZDK_evw";
    private final static String DCR = PathConfig.DCR;
    private final static String DCSJ = PathConfig.DCSJ;
    private final static String ZZMC = PathConfig.ZZMC;

    public GdbDao(Context context) {
        super(context);
    }

   /* public int getAllCount(int falg) {
        SQLiteDatabase connection = null;
        Cursor cursor = null;
        int flag = 0;
        StringBuffer sql = null;
        try {
            //获得数据库连接
            connection = this.getGdbConnection(path);
            if (falg == 0) {
                sql = new StringBuffer("select count(OBJECTID) from " + TB_NAME);
            } else if (falg == 1) {
                sql = new StringBuffer("select count(OBJECTID) from " + TB_NAME + " where ZZMC IS NOT NULL and trim(ZZMC)<>''");//使用trim()函数
            }
            cursor = connection.rawQuery(sql.toString(), null);
            cursor.moveToFirst();
            flag = (int) cursor.getLong(0);
        } catch (Exception e) {
            e.printStackTrace();
            flag = 0;
        } finally {
            closeAll(connection, cursor);
        }
        return flag;
    }*/

    public boolean batchEdit(Map<String, Object> map) {
        SQLiteDatabase connection = null;
        boolean flag = false;
        try {
            //获得数据库连接
            connection = this.getGdbConnection(path);
            connection.beginTransaction();//开启事物
            StringBuffer stringBuffer = new StringBuffer("update ");
            stringBuffer.append(TB_NAME);
            stringBuffer.append(" set ");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                StringBuffer sb = new StringBuffer();
                sb.append(entry.getKey());
                sb.append(" = ");
                if (entry.getKey().equals("DCSJ")) {
                    if (entry.getValue() == null) {
                        sb.append(" NULL ");
                    } else {
                        sb.append(entry.getValue());
                    }
                } else {
                    if (entry.getValue() == null) {
                        sb.append(" NULL ");
                    } else {
                        sb.append("'");
                        sb.append(entry.getValue().toString());
                        sb.append("'");
                    }
                }
                sb.append(" where ");
                sb.append(entry.getKey());
                sb.append(" is NULL or trim(");
                sb.append(entry.getKey());
                sb.append(") = ''");
                String sql = stringBuffer.toString() + sb.toString();
                connection.execSQL(sql);
            }
            connection.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            connection.endTransaction();
            closeAll(connection, null);
        }
        return flag;
    }


    public interface ICallBack {
        public void isEditComplete(boolean isEditComplete);
        public void handlerCount(Integer count);
    }


    public interface ICountCallBack {
        public void countCallBack(Integer count,Integer countAll);
    }

    /**
     * 批量修改属性
     */
    public void batchEdit(FeatureLayer featureLayer, final ICallBack iCallBack) {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setWhere("1=1");
        featureLayer.getFeatureTable().queryFeatures(queryParameters, new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult featureResult) {
                try {
                    GeodatabaseFeatureTable gftable = null;
                    Map<String, Object> atrrMap = null;
                    MobileUser mobileUser = (MobileUser) SharedPreferencesHelper.readObject(context, LoginActivity.USER_ACCOUNT);
                    String collectName = null;
                    if (mobileUser != null) {
                        collectName = mobileUser.getName();
                    }
                    Iterator<Object> iterator = featureResult.iterator();
                    Feature feature;
                    Map<String, Object> map;
                    Object o;
                    GeodatabaseFeature geoFeature;
                    int i = 0;
                    long time = DateUtil.getUTCTimeStr();
                    while (iterator.hasNext()) {
                        feature = (Feature) iterator.next();
                        map = feature.getAttributes();
                        o = map.get(DCR);
                        if (collectName != null) {
                            if (o == null || o.toString().trim().equals("")) {
                                geoFeature = (GeodatabaseFeature) feature;
                                if (gftable == null) {
                                    gftable = (GeodatabaseFeatureTable) geoFeature.getTable();
                                }
                                atrrMap = geoFeature.getAttributes();
                                atrrMap.put(DCR, collectName);
                                atrrMap.put(DCSJ, time);
                                gftable.updateFeature(feature.getId(), atrrMap);
                                iCallBack.handlerCount(++i);
                            }
                        }
                    }
                    iCallBack.isEditComplete(true);
                } catch (Exception e) {
                    iCallBack.isEditComplete(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                iCallBack.isEditComplete(false);
            }
        });
    }


    public void getEditedZZMC(FeatureLayer featureLayer, final ICountCallBack iCountCallBack) {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setWhere("1=1");
        featureLayer.getFeatureTable().queryFeatures(queryParameters, new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult featureResult) {
                Integer count = 0;
                Integer countAll = 0;
                try {
                    Iterator<Object> iterator = featureResult.iterator();
                    Feature feature;
                    Map<String, Object> map;
                    Object o;
                    GeodatabaseFeature geoFeature;
                    int i = 0;
                    long time = DateUtil.getUTCTimeStr();
                    while (iterator.hasNext()) {
                        feature = (Feature) iterator.next();
                        map = feature.getAttributes();
                        o = map.get(ZZMC);
                        countAll++;
                        if (o != null && !o.toString().trim().equals("")) {
                            count++;
                        }
                    }
                    iCountCallBack.countCallBack(count,countAll);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }
}
