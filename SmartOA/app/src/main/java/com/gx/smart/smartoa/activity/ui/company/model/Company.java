package com.gx.smart.smartoa.activity.ui.company.model;

import java.io.Serializable;

/**
 * @author xiaosy
 * @create 2019-12-12
 * @Describe
 **/
public class Company implements Serializable {

    /**
     * createTime : 1575351507000
     * id : 1112
     * level : 1
     * modifyTime : 1575351514000
     * name : 30楼广信-公司
     * number : 30
     * subsidiaryId : 1
     * tenantNo : 2
     * type : 1
     */

    private long createTime;
    private int id;
    private int level;
    private long modifyTime;
    private String name;
    private String number;
    private String subsidiaryId;
    private int tenantNo;
    private int type;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSubsidiaryId() {
        return subsidiaryId;
    }

    public void setSubsidiaryId(String subsidiaryId) {
        this.subsidiaryId = subsidiaryId;
    }

    public int getTenantNo() {
        return tenantNo;
    }

    public void setTenantNo(int tenantNo) {
        this.tenantNo = tenantNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
