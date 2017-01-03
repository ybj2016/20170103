package com.grandtech.map.utils.enmus;

/**
 * 用户注册回调结果
 *
 * @author zy
 */
public enum EnumUserRegisterCallBack {
    // 正常
    SUCCESS {
        @Override
        public int getValue() {
            return 0;
        }

        @Override
        public String toString() {
            return "SUCCESS";
        }

        @Override
        public String getDesc() {
            return "成功";
        }
    },
    // 已经存在
    ISEXIST {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String toString() {
            return "ISEXIST";
        }

        @Override
        public String getDesc() {
            return "账号已存在";
        }
    },
    // 服务端错误
    SURVER_ERROR {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String toString() {
            return "SURVER_ERROR";
        }

        @Override
        public String getDesc() {
            return "服务端错误";
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
    public static EnumUserRegisterCallBack getEnumByCode(int code) {
        EnumUserRegisterCallBack state = null;

        for (int i = 0; i < EnumUserRegisterCallBack.values().length; i++) {
            if (EnumUserRegisterCallBack.values()[i].getValue() == code) {
                state = EnumUserRegisterCallBack.values()[i];
                break;
            }
        }
        return state;
    }

    /**
     * 根据枚举描名称获取枚举对象
     */
    public static EnumUserRegisterCallBack getEnumByName(String name) {
        EnumUserRegisterCallBack state = null;
        for (int i = 0; i < EnumUserRegisterCallBack.values().length; i++) {
            if (EnumUserRegisterCallBack.values()[i].toString().equalsIgnoreCase(name.trim())) {
                state = EnumUserRegisterCallBack.values()[i];
                break;
            }
        }
        return state;
    }

    /**
     * 根据枚举描述信息表达式获取枚举对象
     */
    public static EnumUserRegisterCallBack getEnumByDesc(String desc) {
        EnumUserRegisterCallBack state = null;
        for (int i = 0; i < EnumUserRegisterCallBack.values().length; i++) {
            if (EnumUserRegisterCallBack.values()[i].getDesc().equalsIgnoreCase(desc.trim())) {
                state = EnumUserRegisterCallBack.values()[i];
                break;
            }
        }
        return state;
    }

}
