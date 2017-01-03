package com.grandtech.map.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * 手机用户  登录时采用手机号和密码(本地验证)
 * Created by zy on 2016/12/22.
 */

public class MobileUser implements Serializable {

    public final static String TB_MOBILE_USER="tb_MobileUser";//表名
    public final static String C_PhoneNum = "phoneNum"; //电话号码
    public final static String C_Name="name";//真实姓名
    public final static String C_Pwd="pwd";//密码
    public final static String C_DateTime  = "dateTime";//登录日期
    public final static String C_State="state";//账号状态

    //主键
    private String phoneNum;
    //用户名（采集名称）
    private String name;
    //密码
    private String pwd;
    //登录时间
    private Date dateTime;
    //账号状态
    private Integer state;
    //登录状态
    private Integer isOnLine;

    public MobileUser(){}

    public MobileUser(Integer state, Date dateTime, String pwd, String phoneNum, String name) {
        this.state = state;
        this.dateTime = dateTime;
        this.pwd = pwd;
        this.phoneNum = phoneNum;
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getIsOnLine() {
        return isOnLine;
    }

    public void setIsOnLine(Integer isOnLine) {
        this.isOnLine = isOnLine;
    }
}