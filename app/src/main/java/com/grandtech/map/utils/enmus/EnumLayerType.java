package com.grandtech.map.utils.enmus;

/**
 * Created by kuchanly on 2016-10-18.
 */

public enum EnumLayerType {




    ArcGISDynamicMapServiceLayer {
        @Override
        public int getValue() {
            return 1;
        }
        @Override
        public String toString() {
            return "ArcGISDynamicMapServiceLayer";
        }
        @Override
        public String getDesc() {
            return "ArcGISDynamicMapServiceLayer动态服务";
        }
    },
    ArcGISFeatureLayer {
        @Override
        public int getValue() {
            return 2;
        }
        @Override
        public String toString() {
            return "ArcGISFeatureLayer";
        }
        @Override
        public String getDesc() {
            return "ArcGISFeatureLayer图层";
        }
    },
    ArcGISImageServiceLayer {
        @Override
        public int getValue() {
            return 3;
        }
        @Override
        public String toString() {
            return "ArcGISImageServiceLayer";
        }
        @Override
        public String getDesc() {
            return "ArcGISImageServiceLayer";
        }
    },
    ArcGISLocalTiledLayer {
        @Override
        public int getValue() {
            return 4;
        }
        @Override
        public String toString() {
            return "ArcGISLocalTiledLayer";
        }
        @Override
        public String getDesc() {
            return "ArcGISLocalTiledLayer";
        }
    },
    ArcGISTiledMapServiceLayer {
        @Override
        public int getValue() {
            return 5;
        }
        @Override
        public String toString() {
            return "ArcGISTiledMapServiceLayer";
        }
        @Override
        public String getDesc() {
            return "ArcGISTiledMapServiceLayer";
        }
    },
    BingMapsLayer {
        @Override
        public int getValue() {
            return 6;
        }
        @Override
        public String toString() {
            return "BingMapsLayer";
        }
        @Override
        public String getDesc() {
            return "BingMapsLayer";
        }
    },
    KMLLayer {
        @Override
        public int getValue() {
            return 7;
        }
        @Override
        public String toString() {
            return "KMLLayer";
        }
        @Override
        public String getDesc() {
            return "KMLLayer";
        }
    },
    MessageGroupLayer {
        @Override
        public int getValue() {
            return 8;
        }
        @Override
        public String toString() {
            return "MessageGroupLayer";
        }
        @Override
        public String getDesc() {
            return "MessageGroupLayer";
        }
    },
    OpenStreetMapLayer {
        @Override
        public int getValue() {
            return 9;
        }
        @Override
        public String toString() {
            return "OpenStreetMapLayer";
        }
        @Override
        public String getDesc() {
            return "OpenStreetMapLayer";
        }
    },
    TiledServiceLayer {
        @Override
        public int getValue() {
            return 10;
        }
        @Override
        public String toString() {
            return "TiledServiceLayer";
        }
        @Override
        public String getDesc() {
            return "TiledServiceLayer";
        }
    },
    WMSLayer {
        @Override
        public int getValue() {
            return 11;
        }
        @Override
        public String toString() {
            return "WMSLayer";
        }
        @Override
        public String getDesc() {
            return "WMSLayer";
        }
    },
    WMTSLayer {
        @Override
        public int getValue() {
            return 12;
        }
        @Override
        public String toString() {
            return "WMTSLayer";
        }
        @Override
        public String getDesc() {
            return "WMTSLayer";
        }
    };




    /**
     * @return 对应整数值
     */
    public abstract int getValue();

    public abstract String toString();

    /**
     * @return 获取枚举描述
     */
    public abstract String getDesc();
    /**
     * 根据枚举整型表达式获取枚举对象
     */
    public static EnumHttpState getHttp(int value) {
        EnumHttpState type = null;
        for (int i = 0; i < EnumHttpState.values().length; i++) {
            if (EnumHttpState.values()[i].getValue()==value) {
                type = EnumHttpState.values()[i];
                break;
            }
        }
        return type;
    }
    /**
     * 根据枚举描名称获取枚举对象
     */
    public static EnumHttpState getHttpByName(String name) {
        EnumHttpState type = null;
        for (int i = 0; i < EnumHttpState.values().length; i++) {
            if (EnumHttpState.values()[i].toString().equalsIgnoreCase(name.trim())) {
                type = EnumHttpState.values()[i];
                break;
            }
        }
        return type;
    }

    /**
     * 根据枚举描述信息表达式获取枚举对象
     */
    public static EnumHttpState getHttpByDesc(String desc) {
        EnumHttpState type = null;
        for (int i = 0; i < EnumHttpState.values().length; i++) {
            if (EnumHttpState.values()[i].getDesc().equalsIgnoreCase(desc.trim())) {
                type = EnumHttpState.values()[i];
                break;
            }
        }
        return type;
    }

}
