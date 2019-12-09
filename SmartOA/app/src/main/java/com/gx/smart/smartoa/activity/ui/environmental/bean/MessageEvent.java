package com.gx.smart.smartoa.activity.ui.environmental.bean;

public class MessageEvent {
    public int action;

    public MessageEvent(int action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "[action:" + action + "]";
    }
}