package com.grandtech.map.utils.okhttp.builder;


import com.grandtech.map.utils.okhttp.OkHttpUtils;
import com.grandtech.map.utils.okhttp.request.OtherRequest;
import com.grandtech.map.utils.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
