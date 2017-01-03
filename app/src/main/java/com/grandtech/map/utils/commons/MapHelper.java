package com.grandtech.map.utils.commons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2016/11/23.
 * Map<K-V>工具类
 */

public class MapHelper {

    private MapHelper(){}

    /**
     * 合并两个Map
     * @return
     */
    public static Map<String,Object>  merge2Map(Map<String,Object> map1,Map<String,Object> map2){
        Map<String, Object>  map =new HashMap<String, Object>();
        map.putAll(map1);
        map.putAll(map2);
        return map;
    }

    /**
     * Map的拆分
     * @param map
     * @param s
     * @return
     */
    public List<Map<String,Object>> splitMap(Map<String,Object> map, String[] s){
        List<Map<String,Object>> list=null;
        int flag=0;
        for(int i=0;i<=s.length;i++){
            Map<String,Object> temp = new HashMap<String, Object>();
            for(Map.Entry<String, Object> entry:map.entrySet()){

              /*  for(int j=flag;j<map.size();j++){
                    Map<String, Object> entry=map.entrySet().toArray()[j];
                }*/

                temp.put(entry.getKey(),entry.getValue());
                flag++;
                if(entry.getKey().equals(s[i])){
                    break;
                }

                System.out.println(entry.getKey()+"--->"+entry.getValue());
            }
            list.add(temp);
        }
        return  null;
    }

}
