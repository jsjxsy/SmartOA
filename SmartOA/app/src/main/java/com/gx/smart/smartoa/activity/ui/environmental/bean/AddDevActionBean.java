package com.gx.smart.smartoa.activity.ui.environmental.bean;

import java.io.Serializable;

/**
 * Author: 13750831423
 * Time: 2018/9/17  20:23
 * Email: guolejun1990@163.com
 * Web: www.dd121.com
 * Description:
 */
public class AddDevActionBean implements Serializable {
    private boolean isSelected;
    private String devName;
    private boolean devOpen;

    private String val;
    private String channel;
    private String model;
    private String category;
    private int uuid;
    private int linkId;

    @Override
    public String toString() {
        return "isSelected:" + isSelected + ",devName:"
                + devName + ",devOpen:" + devOpen + ",val:"
                + val + ",channel:" + channel + ",model:"
                + model + ",category:" + category + ",uuid:"
                + uuid + ",linkId:" + linkId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddDevActionBean that = (AddDevActionBean) o;
        return uuid == that.uuid;
    }

    @Override
    public int hashCode() {
        return model.hashCode() + devName.hashCode() + uuid;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public boolean isDevOpen() {
        return devOpen;
    }

    public void setDevOpen(boolean devOpen) {
        this.devOpen = devOpen;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }
}
