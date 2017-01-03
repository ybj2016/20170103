package com.grandtech.map.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zy on 2016/11/18.
 * 地块类型实体类
 */

public class DkType implements Serializable {

    public final static String TB_DK_TYPE = "tb_dk_type";//数据库里的一张表
    public final static String C_ID="cId";
    public final static String C_NAME="cName";
    public final static String C_DATETIME="cDateTime";
    public final static String C_CHOICECOUNT="cChoiceCount";
    public final static String C_DESC="cDesc";

    private Integer cId;
    private String cName;
    private Date cDateTime;
    private Integer cChoiceCount;
    private String cDesc;

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String getcDesc() {
        return cDesc;
    }

    public void setcDesc(String cDesc) {
        this.cDesc = cDesc;
    }

    public Integer getcChoiceCount() {
        return cChoiceCount;
    }

    public void setcChoiceCount(Integer cChoiceCount) {
        this.cChoiceCount = cChoiceCount;
    }

    public Date getcDateTime() {
        return cDateTime;
    }

    public void setcDateTime(Date cDateTime) {
        this.cDateTime = cDateTime;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }
}
