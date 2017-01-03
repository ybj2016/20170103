package com.grandtech.map.utils.commons;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Created by zy on 2016/10/17.
 */

public class XmlHelper {

    public static Object inputStreamToEntity(Object o, InputStream inputStream) throws Exception {

        XmlPullParser xmlPullParser= Xml.newPullParser();
        xmlPullParser.setInput(inputStream,"UTF-8");
        int type=xmlPullParser.getEventType();
        Class clazz = Class.forName(o.getClass().getName());//根据类名反射获取
        Object obj=clazz.newInstance();//实例化实体类
        Field[] fields = clazz.getDeclaredFields();
        fields[0].getType().getName();//获取类中的自动名称
        while (type!=XmlPullParser.END_DOCUMENT){
            switch (type){
                case XmlPullParser.START_TAG:
                    for(Field f : fields) {
                        if(f.getName().equals(xmlPullParser.getName())){
                            f.set(obj,xmlPullParser.getName());
                        }
                    }
                    break;
            }
            type=xmlPullParser.next();
        }
        return obj;
    }
}
