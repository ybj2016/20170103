package com.grandtech.map.entity;


import java.io.Serializable;

/**
 * Created by zy on 2016/11/22.
 * 用户的账户信息
 */

public class Account  implements Serializable {

    private String collectName;

    public String getCollectName() {
        return collectName;
    }

    public void setCollectName(String collectName) {
        this.collectName = collectName;
    }

}
