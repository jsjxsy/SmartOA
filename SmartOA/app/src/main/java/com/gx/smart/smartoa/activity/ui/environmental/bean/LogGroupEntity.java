package com.gx.smart.smartoa.activity.ui.environmental.bean;

import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.OptionLogDto;

import java.util.ArrayList;

public class LogGroupEntity {
    private String header;
    private String footer;
    private ArrayList<OptionLogDto> children;

    public LogGroupEntity(String header, String footer,
                          ArrayList<OptionLogDto> children) {
        this.header = header;
        this.footer = footer;
        this.children = children;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public ArrayList<OptionLogDto> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<OptionLogDto> children) {
        this.children = children;
    }
}
