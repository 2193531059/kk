package com.administrator.seawindow.utils;

public class EventBusEvent {
    public static final String RFRESH_SEA_HOTNEWS = "RFRESH_SEA_HOTNEWS";

    private String mEvent;
    private Object mExtra;
    private Object mArg;

    public Object getmArg() {
        return mArg;
    }

    public String getEvent() {
        return mEvent;
    }

    public Object getExtra() {
        return mExtra;
    }

    public EventBusEvent(String event, Object extra) {
        mEvent = event;
        mExtra = extra;
    }

    public EventBusEvent(String event, Object extra, Object arg) {
        mEvent = event;
        mExtra = extra;
        mArg = arg;
    }

    public EventBusEvent(String event) {
        this(event, null);
    }
}
