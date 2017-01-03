package com.grandtech.map.gis.mapcore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zy on 2016-11-2.
 * 地图工具生产工厂和地图事件分发
 */

public class ToolPoolFactory {


    private static List<ITool> mapTools=new ArrayList<ITool>();

    public static int currentToolIndex_Map = -1;
    public static int currentToolIndex_Btn = -1;

    /**
     * 初始化工具集合
     */
    private static void  InitTools(){

    }

    public static void clearTools(){
        currentToolIndex_Map = -1;
        currentToolIndex_Btn = -1;
        mapTools.clear();
    }

    /**
     * 获取当前由map点击事件触发的工具
     * @return
     */
    public static ITool getCurrentMapTriggerTool(){
        ITool currentITool=null;
        try {
            if (mapTools.size() > currentToolIndex_Map&&mapTools.get(currentToolIndex_Map).triggerMode()==ITool.MAP_CLICK) {
                currentITool = mapTools.get(currentToolIndex_Map);
            }
        }catch (Exception e){
            currentITool=null;
        }
        return currentITool;
    }

    /**
     * 获取当前由按钮点击事件触发的工具
     * @return
     */
    public static ITool getCurrentBtnTriggerTool(){
        ITool currentITool=null;
        try {
            if (mapTools.size() > currentToolIndex_Btn&&mapTools.get(currentToolIndex_Btn).triggerMode()==ITool.BTN_CLICK) {
                currentITool = mapTools.get(currentToolIndex_Btn);
            }
        }catch (Exception e){
            currentITool=null;
        }
        return currentITool;
    }

    /**
     * 获取当前使用的工具
     * @return
     */
    public static ITool getCurrentTool(){
        ITool currentITool=null;
        try {
            if (mapTools.size() > currentToolIndex_Map) {
                currentITool = mapTools.get(currentToolIndex_Map);
            }
        }catch (Exception e){
            currentITool=null;
        }
        return currentITool;
    }

    public static void addTool(ITool iTool){
        if(!mapTools.contains(iTool)) {
            mapTools.add(iTool);
        }
    }

    public static void removeTool(ITool iTool){
        if(mapTools.contains(iTool)) {
            mapTools.remove(iTool);
        }
    }
}
