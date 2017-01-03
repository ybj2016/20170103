package com.grandtech.map.gis.maputil;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.FeatureType;
import com.esri.core.map.Field;
import com.esri.core.table.FeatureTable;
import com.grandtech.map.utils.commons.MapHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2016/10/28.
 */

public class AttributeUtil {

    private AttributeUtil(){

    }

    public  static GeodatabaseFeature editFeature;

    public static Field[] getFields(FeatureResult featureResult){
        try {
            if(featureResult!=null) {
                return (Field[]) featureResult.getFields().toArray();
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 要素图层不支持获取FeatureType[]
     * @param featureResult
     * @return
     */
    public static FeatureType[] getFeatureTypes(FeatureResult featureResult){
        return null;
    }

    public static String getTypeIdFiledName(FeatureResult featureResult){

        return featureResult.getObjectIdFieldName();
    }

    public static GeodatabaseFeature process(FeatureResult featureResult){
        for(int i=0;i<featureResult.getFields().size();i++){
            System.out.println(featureResult.getFields().get(i).getFieldType());
        }
        try {
            if(featureResult!=null) {
                Iterator<Object> iterator = featureResult.iterator();
                GeodatabaseFeature geodatabaseFeature = null;
                while (iterator.hasNext()) {
                    geodatabaseFeature = (GeodatabaseFeature) iterator.next();
                    editFeature=geodatabaseFeature;
                }

                System.out.println(geodatabaseFeature.toString());
                return geodatabaseFeature;
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<AttributeInfo> getAttributes(FeatureResult featureResult){
        if(featureResult!=null) {
            Iterator<Object> iterator = featureResult.iterator();
            GeodatabaseFeature geodatabaseFeature = null;
            while (iterator.hasNext()) {
                geodatabaseFeature = (GeodatabaseFeature) iterator.next();
                editFeature=geodatabaseFeature;
            }
            Map<String,Object> map = geodatabaseFeature.getAttributes();
            List<AttributeInfo> attributes=new ArrayList<>();
            for (String key : map.keySet()) {
                AttributeInfo attribute=new AttributeInfo(key,map.get(key));
                attributes.add(attribute);
            }
            return attributes;
        }else {
            return null;
        }
    }

    public static boolean batchEditGdbTable(FeatureLayer featureLayer, Map<String, Object> map){
        boolean flag=false;
        FeatureTable featureTable = featureLayer.getFeatureTable();

        long ids=featureTable.getNumberOfFeatures();


        GeodatabaseFeature gdbF= (GeodatabaseFeature) featureLayer.getFeature(0);



        GeodatabaseFeatureTable gdbFTB= (GeodatabaseFeatureTable) gdbF.getTable();
        Map<String, Object> attrMap = null;
        try {
            for(long id=0;id<ids;id++) {
                attrMap=gdbF.getAttributes();
                MapHelper.merge2Map(attrMap,map);
                gdbFTB.updateFeature(id, attrMap);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>:"+id);
            }
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
            flag=false;
        }
        return flag;
    }

}
