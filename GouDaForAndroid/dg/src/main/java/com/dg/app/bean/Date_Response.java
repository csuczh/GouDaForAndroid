package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/11/30.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class Date_Response {
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;
    @XStreamAlias("date_id")
    private int date_id;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public int getDate_id() {
        return date_id;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setDate_id(int date_id) {
        this.date_id = date_id;
    }
}
