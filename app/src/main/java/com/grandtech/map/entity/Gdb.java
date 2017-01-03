package com.grandtech.map.entity;

import java.util.Date;

/**
 * Created by zy on 2016/11/23.
 * Gdb数据库的部分实体
 */

public class Gdb{
    private String collectName;
    private String dkType;
    private Date dateTime;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getDkType() {
        return dkType;
    }

    public void setDkType(String dkType) {
        this.dkType = dkType;
    }

    public String getCollectName() {
        return collectName;
    }

    public void setCollectName(String collectName) {
        this.collectName = collectName;
    }
}
