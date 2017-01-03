package com.grandtech.map.utils.enmus;

/**
 * Created by zy on 2016/12/22.
 */

public enum EnumLoginState {
    //在线登录
    ONLINE {
        @Override
        public int getValue() {
            return 0;
        }

        @Override
        public String toString() {
            return "ONLINE";
        }
        @Override
        public String getDesc() {
            return "在线登录";
        }
    },
    //离线登录
    OUTLINE {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String toString() {
            return "OUTLINE";
        }
        @Override
        public String getDesc() {
            return "离线登录";
        }
    },//未登录
    OFF {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String toString() {
            return "OFF";
        }
        @Override
        public String getDesc() {
            return "未登录";
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
    public static EnumLoginState getEnumByCode(int code) {
        EnumLoginState state = null;

        for (int i = 0; i < EnumLoginState.values().length; i++) {
            if (EnumLoginState.values()[i].getValue()==code) {
                state = EnumLoginState.values()[i];
                break;
            }
        }
        return state;
    }
    /**
     * 根据枚举描名称获取枚举对象
     */
    public static EnumLoginState getEnumByName(String name) {
        EnumLoginState state = null;
        for (int i = 0; i < EnumLoginState.values().length; i++) {
            if (EnumLoginState.values()[i].toString().equalsIgnoreCase(name.trim())) {
                state = EnumLoginState.values()[i];
                break;
            }
        }
        return state;
    }

    /**
     * 根据枚举描述信息表达式获取枚举对象
     */
    public static EnumLoginState getEnumByDesc(String desc) {
        EnumLoginState state = null;
        for (int i = 0; i < EnumLoginState.values().length; i++) {
            if (EnumLoginState.values()[i].getDesc().equalsIgnoreCase(desc.trim())) {
                state = EnumLoginState.values()[i];
                break;
            }
        }
        return state;
    }

}
