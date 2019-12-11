package com.gx.smart.smartoa.activity.ui.environmental.websocket;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edgar on 2019/6/13.
 */
public class WsNotifyBean implements Parcelable {

    private String service;
    private String uuid;
    private String devName;
    private String category;
    private String model;
    private String val;
    private int channel;

    public static final Creator<WsNotifyBean> CREATOR = new Creator<WsNotifyBean>() {
        @Override
        public WsNotifyBean createFromParcel(Parcel in) {
            return new WsNotifyBean(in);
        }

        @Override
        public WsNotifyBean[] newArray(int size) {
            return new WsNotifyBean[size];
        }
    };

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(service);
        dest.writeString(uuid);
        dest.writeString(devName);
        dest.writeString(category);
        dest.writeString(model);
        dest.writeString(val);
        dest.writeInt(channel);
    }


    public WsNotifyBean(Parcel source) {
        service = source.readString();
        uuid=source.readString();
        devName = source.readString();
        category=source.readString();
        model = source.readString();
        val = source.readString();
        channel = source.readInt();
    }

    public WsNotifyBean() {
    }
}
