package com.grandtech.map.entity;

/**
 * Created by fs on 2016/12/28.
 * jzzb数据库实体(坐标纠正)
 */

public class Jzzb {

    public final static String TB_JZZB = "tb_jzzb";//表名
    public final static String C_ID = "cId"; //id
    public final static String C_VERSION = "cVersion";//修改次数
    public final static String C_CREATE_TIME = "cCreateTime";//创建时间
    public final static String C_COORDINATES = "cCoordinates";//坐标
    public final static String C_MODIFY_TIME = "cModifyTime";//结束时间
    /**
     * 主键
     */
    private Integer cId;

    /**
     * 版本号
     */
    private String cVersion;
    /**
     * 创建时间
     */
    private String cCreateTime;
    /**
     * 坐标基准点
     */
    private String cCoordinates;
    /**
     *修改时间
     */
    private String cModifyTime;

    public Integer getId() {
        return cId;
    }

    public void setId(Integer id) {
        this.cId = id;
    }

    public String getVersion() {
        return cVersion;
    }

    public void setVersion(String version) {
        this.cVersion = version;
    }

    public String getCreateTime() {
        return cCreateTime;
    }

    public void setCreateTime(String createTime) {
        this.cCreateTime = createTime;
    }

    public String getCoordinates() {
        return cCoordinates;
    }

    public void setCoordinates(String coordinates) {
        this.cCoordinates = coordinates;
    }

    public String getModifyTime() {
        return cModifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.cModifyTime = modifyTime;
    }
}
