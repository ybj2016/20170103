package com.grandtech.map.utils.enmus;

/**
 * Created by zy on 2016/12/22.
 */

public enum EnumGDBTask {

    //******************************************下载****************************************
    //开始下载
    DOWN_BEFORE{
        @Override
        public int getValue() {
            return 2001;
        }

        @Override
        public String toString() {
            return "BEFORE";
        }

        @Override
        public String getDesc() {
            return "开始下载";
        }
    },
    //正在下载
    DOWN_PROCESSING {
        @Override
        public int getValue() {
            return 2002;
        }

        @Override
        public String toString() {
            return "PROCESSING";
        }

        @Override
        public String getDesc() {
            return "下载中...";
        }
    },
    //下载成功
    DOWN_SUCCESS {
        @Override
        public int getValue() {
            return 2003;
        }

        @Override
        public String toString() {
            return "SUCCESS";
        }

        @Override
        public String getDesc() {
            return "下载成功";
        }
    },
    //下载异常
    DOWN_ERROR {
        @Override
        public int getValue() {
            return 2004;
        }

        @Override
        public String toString() {
            return "ERROR";
        }

        @Override
        public String getDesc() {
            return "下载异常";
        }
    },

    //******************************************同步****************************************
    //开始同步
    SYNC_BEFORE{
        @Override
        public int getValue() {
            return 3001;
        }

        @Override
        public String toString() {
            return "BEFORE";
        }

        @Override
        public String getDesc() {
            return "开始同步";
        }
    },
    //正在同步
    SYNC_PROCESSING {
        @Override
        public int getValue() {
            return 3002;
        }

        @Override
        public String toString() {
            return "PROCESSING";
        }

        @Override
        public String getDesc() {
            return "同步中...";
        }
    },
    //同步成功
    SYNC_SUCCESS {
        @Override
        public int getValue() {
            return 3003;
        }

        @Override
        public String toString() {
            return "SUCCESS";
        }

        @Override
        public String getDesc() {
            return "同步成功";
        }
    },
    //同步异常
    SYNC_ERROR {
        @Override
        public int getValue() {
            return 3004;
        }

        @Override
        public String toString() {
            return "ERROR";
        }

        @Override
        public String getDesc() {
            return "同步异常";
        }
    };

    /**
     * @return 对应整数值
     */
    public abstract int getValue();

    public abstract String toString();

    public abstract String getDesc();

    /**
     * 根据枚举整型表达式获取枚举对象
     */
    public static EnumGDBTask getEnumByCode(int code) {
        EnumGDBTask state = null;

        for (int i = 0; i < EnumGDBTask.values().length; i++) {
            if (EnumGDBTask.values()[i].getValue() == code) {
                state = EnumGDBTask.values()[i];
                break;
            }
        }
        return state;
    }

    /**
     * 根据枚举描名称获取枚举对象
     */
    public static EnumGDBTask getEnumByName(String name) {
        EnumGDBTask state = null;
        for (int i = 0; i < EnumGDBTask.values().length; i++) {
            if (EnumGDBTask.values()[i].toString().equalsIgnoreCase(name.trim())) {
                state = EnumGDBTask.values()[i];
                break;
            }
        }
        return state;
    }

    /**
     * 根据枚举描述信息表达式获取枚举对象
     */
    public static EnumGDBTask getEnumByDesc(String desc) {
        EnumGDBTask state = null;
        for (int i = 0; i < EnumGDBTask.values().length; i++) {
            if (EnumGDBTask.values()[i].getDesc().equalsIgnoreCase(desc.trim())) {
                state = EnumGDBTask.values()[i];
                break;
            }
        }
        return state;
    }

}
