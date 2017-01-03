package com.grandtech.map.utils.enmus;

/**
 * Created by zy on 2016/10/17.
 */

/**
 * Http状态枚举
 */
public enum EnumHttpState {

    SC_CONTINUE {
        @Override
        public int getValue() {
            return 100;
        }
        @Override
        public String toString() {
            return "Continue";
        }
        @Override
        public String getDesc() {
            return "继续";
        }
    },

    SC_SWITCHING_PROTOCOLS {
        @Override
        public int getValue() {
            return 101;
        }
        @Override
        public String toString() {
            return "SC_SWITCHING_PROTOCOLS";
        }
        @Override
        public String getDesc() {
            return "转换协议";
        }
    },

    SC_OK {
        @Override
        public int getValue() {
            return 200;
        }
        @Override
        public String toString() {
            return "SC_OK";
        }
        @Override
        public String getDesc() {
            return "正常";
        }

    },

    SC_CREATED {
        @Override
        public int getValue() {
            return 201;
        }
        @Override
        public String toString() {
            return "SC_CREATED";
        }

        @Override
        public String getDesc() {
            return "已创建";
        }
    },

    SC_ACCEPTED {
        @Override
        public int getValue() {
            return 202;
        }
        @Override
        public String toString() {
            return "SC_ACCEPTED";
        }

        @Override
        public String getDesc() {
            return "接受";
        }
    },

    SC_NON_AUTHORITATIVE_INFORMATION{
        @Override
        public int getValue() {
            return 203;
        }
        @Override
        public String toString() {
            return "SC_NON_AUTHORITATIVE_INFORMATION";
        }

        @Override
        public String getDesc() {
            return "非官方信息";
        }
    },

    SC_NO_CONTENT{
        @Override
        public int getValue() {
            return 204;
        }
        @Override
        public String toString() {
            return "SC_NO_CONTENT";
        }

        @Override
        public String getDesc() {
            return "无内容";
        }
    },

    SC_RESET_CONTENT{
        @Override
        public int getValue() {
            return 205;
        }
        @Override
        public String toString() {
            return "SC_RESET_CONTENT";
        }

        @Override
        public String getDesc() {
            return "重置内容";
        }
    },

    SC_PARTIAL_CONTENT{
        @Override
        public int getValue() {
            return 206;
        }
        @Override
        public String toString() {
            return "SC_PARTIAL_CONTENT";
        }

        @Override
        public String getDesc() {
            return "局部内容";
        }
    },

    SC_MULTIPLE_CHOICES{
        @Override
        public int getValue() {
            return 300;
        }
        @Override
        public String toString() {
            return "SC_MULTIPLE_CHOICES";
        }

        @Override
        public String getDesc() {
            return "多重选择";
        }
    },

    SC_MOVED_PERMANENTLY{
        @Override
        public int getValue() {
            return 301;
        }
        @Override
        public String toString() {
            return "SC_MOVED_PERMANENTLY";
        }

        @Override
        public String getDesc() {
            return "所请求的文档在别的地方";
        }
    },
    SC_MOVED_TEMPORARILY{
        @Override
        public int getValue() {
            return 302;
        }
        @Override
        public String toString() {
            return "SC_MOVED_TEMPORARILY";
        }

        @Override
        public String getDesc() {
            return "找到";
        }
    },
    SC_SEE_OTHER{
        @Override
        public int getValue() {
            return 303;
        }
        @Override
        public String toString() {
            return "SC_SEE_OTHER";
        }

        @Override
        public String getDesc() {
            return "参见其他信息";
        }
    },
    SC_NOT_MODIFIED{
        @Override
        public int getValue() {
            return 304;
        }
        @Override
        public String toString() {
            return "SC_NOT_MODIFIED";
        }

        @Override
        public String getDesc() {
            return "修正";
        }
    },
    SC_USE_PROXY{
        @Override
        public int getValue() {
            return 305;
        }
        @Override
        public String toString() {
            return "SC_USE_PROXY";
        }

        @Override
        public String getDesc() {
            return "使用代理";
        }
    },
    SC_TEMPORARY_REDIRECT{
        @Override
        public int getValue() {
            return 307;
        }
        @Override
        public String toString() {
            return "SC_TEMPORARY_REDIRECT";
        }

        @Override
        public String getDesc() {
            return "临时重定向";
        }
    },
    SC_BAD_REQUEST{
        @Override
        public int getValue() {
            return 400;
        }
        @Override
        public String toString() {
            return "SC_BAD_REQUEST";
        }

        @Override
        public String getDesc() {
            return "错误请求";
        }
    },
    SC_UNAUTHORIZED{
        @Override
        public int getValue() {
            return 401;
        }
        @Override
        public String toString() {
            return "SC_UNAUTHORIZED";
        }

        @Override
        public String getDesc() {
            return "未授权";
        }
    },
    SC_FORBIDDEN{
        @Override
        public int getValue() {
            return 403;
        }
        @Override
        public String toString() {
            return "SC_FORBIDDEN";
        }

        @Override
        public String getDesc() {
            return "禁止";
        }
    },
    SC_NOT_FOUND{
        @Override
        public int getValue() {
            return 404;
        }
        @Override
        public String toString() {
            return "SC_NOT_FOUND";
        }

        @Override
        public String getDesc() {
            return "未找到";
        }
    },

    SC_METHOD_NOT_ALLOWED{
        @Override
        public int getValue() {
            return 405;
        }
        @Override
        public String toString() {
            return "SC_METHOD_NOT_ALLOWED";
        }

        @Override
        public String getDesc() {
            return "方法未允许";
        }
    },

    SC_NOT_ACCEPTABLE{
        @Override
        public int getValue() {
            return 406;
        }
        @Override
        public String toString() {
            return "SC_NOT_ACCEPTABLE";
        }

        @Override
        public String getDesc() {
            return "无法访问";
        }
    },
    SC_PROXY_AUTHENTICATION_REQUIRED{
        @Override
        public int getValue() {
            return 407;
        }
        @Override
        public String toString() {
            return "SC_PROXY_AUTHENTICATION_REQUIRED";
        }

        @Override
        public String getDesc() {
            return "代理服务器认证要求";
        }
    },
    SC_REQUEST_TIMEOUT{
        @Override
        public int getValue() {
            return 408;
        }
        @Override
        public String toString() {
            return "SC_REQUEST_TIMEOUT";
        }

        @Override
        public String getDesc() {
            return "请求超时";
        }
    },
    SC_CONFLICT{
        @Override
        public int getValue() {
            return 409;
        }
        @Override
        public String toString() {
            return "SC_CONFLICT";
        }

        @Override
        public String getDesc() {
            return "冲突";
        }
    },
    SC_GONE{
        @Override
        public int getValue() {
            return 410;
        }
        @Override
        public String toString() {
            return "SC_GONE";
        }

        @Override
        public String getDesc() {
            return "已经不存在";
        }
    },
    SC_LENGTH_REQUIRED{
        @Override
        public int getValue() {
            return 411;
        }
        @Override
        public String toString() {
            return "SC_LENGTH_REQUIRED";
        }

        @Override
        public String getDesc() {
            return "需要数据长度";
        }
    },
    SC_PRECONDITION_FAILED{
        @Override
        public int getValue() {
            return 412;
        }
        @Override
        public String toString() {
            return "SC_PRECONDITION_FAILED";
        }

        @Override
        public String getDesc() {
            return "先决条件错误";
        }
    },
    SC_REQUEST_ENTITY_TOO_LARGE{
        @Override
        public int getValue() {
            return 413;
        }
        @Override
        public String toString() {
            return "SC_REQUEST_ENTITY_TOO_LARGE";
        }

        @Override
        public String getDesc() {
            return "请求实体过大";
        }
    },
    SC_REQUEST_URI_TOO_LONG{
        @Override
        public int getValue() {
            return 414;
        }
        @Override
        public String toString() {
            return "SC_REQUEST_URI_TOO_LONG";
        }

        @Override
        public String getDesc() {
            return "请求URI过长";
        }
    },
    SC_UNSUPPORTED_MEDIA_TYPE{
        @Override
        public int getValue() {
            return 415;
        }
        @Override
        public String toString() {
            return "SC_UNSUPPORTED_MEDIA_TYPE";
        }

        @Override
        public String getDesc() {
            return "不支持的媒体格式";
        }
    },
    SC_REQUESTED_RANGE_NOT_SATISFIABLE{
        @Override
        public int getValue() {
            return 416;
        }
        @Override
        public String toString() {
            return "SC_REQUESTED_RANGE_NOT_SATISFIABLE";
        }

        @Override
        public String getDesc() {
            return "请求范围无法满足";
        }
    },
    SC_EXPECTATION_FAILED{
        @Override
        public int getValue() {
           return 417;
        }
        @Override
        public String toString() {
           return "SC_EXPECTATION_FAILED";
        }

        @Override
        public String getDesc() {
           return "期望失败";
        }
    },
    SC_INTERNAL_SERVER_ERROR{
        @Override
        public int getValue() {
            return 500;
        }
        @Override
        public String toString() {
            return "SC_INTERNAL_SERVER_ERROR";
        }

        @Override
        public String getDesc() {
            return "内部服务器错误";
        }
    },
    SC_NOT_IMPLEMENTED{
        @Override
        public int getValue() {
            return 501;
        }
        @Override
        public String toString() {
            return "SC_NOT_IMPLEMENTED";
        }

        @Override
        public String getDesc() {
            return "未实现";
        }
    },
    SC_BAD_GATEWAY{
        @Override
        public int getValue() {
            return 502;
        }
        @Override
        public String toString() {
            return "SC_BAD_GATEWAY";
        }
        @Override
        public String getDesc() {
            return "错误的网关";
        }
    },
    SC_SERVICE_UNAVAILABLE{
        @Override
        public int getValue() {
            return 503;
        }
        @Override
        public String toString() {
            return "SC_SERVICE_UNAVAILABLE";
        }
        @Override
        public String getDesc() {
            return "服务无法获得";
        }
    },
    SC_GATEWAY_TIMEOUT{
        @Override
        public int getValue() {
            return 504;
        }
        @Override
        public String toString() {
            return "SC_GATEWAY_TIMEOUT";
        }
        @Override
        public String getDesc() {
            return "网关超时";
        }
    },
    SC_HTTP_VERSION_NOT_SUPPORTED{
        @Override
        public int getValue() {
            return 505;
        }
        @Override
        public String toString() {
            return "SC_HTTP_VERSION_NOT_SUPPORTED";
        }
        @Override
        public String getDesc() {
            return "不支持的 HTTP 版本";
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
