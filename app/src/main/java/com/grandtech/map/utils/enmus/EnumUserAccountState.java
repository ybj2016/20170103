package com.grandtech.map.utils.enmus;


/**
 * 用户账号状态
 * @author zy
 *
 */
public enum EnumUserAccountState {
	//正常
	NORMAL {
		@Override
        public int getValue() {
            return 0;
        }

        @Override
        public String toString() {
            return "NORMAL";
        }
        @Override
        public String getDesc() {
            return "正常";
        }
    },
	//异常
	UNUSUAL {
		@Override
        public int getValue() {
            return 1;
        }

        @Override
        public String toString() {
            return "UNUSUAL";
        }
        @Override
        public String getDesc() {
            return "异常";
        }
    },
	//错误
	ERROR {
		@Override
        public int getValue() {
            return 2;
        }

        @Override
        public String toString() {
            return "ERROR";
        }
        @Override
        public String getDesc() {
            return "不可用";
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
    public static EnumUserAccountState getEnumByCode(int code) {
    	EnumUserAccountState state = null;

        for (int i = 0; i < EnumUserAccountState.values().length; i++) {
            if (EnumUserAccountState.values()[i].getValue()==code) {
            	state = EnumUserAccountState.values()[i];
                break;
            }
        }
        return state;
    }
    /**
     * 根据枚举描名称获取枚举对象
     */
    public static EnumUserAccountState getEnumByName(String name) {
    	EnumUserAccountState state = null;
        for (int i = 0; i < EnumUserAccountState.values().length; i++) {
            if (EnumUserAccountState.values()[i].toString().equalsIgnoreCase(name.trim())) {
            	state = EnumUserAccountState.values()[i];
                break;
            }
        }
        return state;
    }
    
    /**
     * 根据枚举描述信息表达式获取枚举对象
     */
    public static EnumUserAccountState getEnumByDesc(String desc) {
    	EnumUserAccountState state = null;
        for (int i = 0; i < EnumUserAccountState.values().length; i++) {
            if (EnumUserAccountState.values()[i].getDesc().equalsIgnoreCase(desc.trim())) {
            	state = EnumUserAccountState.values()[i];
                break;
            }
        }
        return state;
    }

}

