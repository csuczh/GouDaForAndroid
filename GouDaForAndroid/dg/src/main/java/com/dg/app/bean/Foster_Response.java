package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/11/30.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class Foster_Response {
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;
    @XStreamAlias("foster_id")
    private int foster_id;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setFoster_id(int foster_id) {
        this.foster_id = foster_id;
    }

    public String getMsg() {
        return msg;
    }

    public int getFoster_id() {
        return foster_id;
    }
}
