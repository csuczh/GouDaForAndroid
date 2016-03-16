package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/10/27.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class ResponseResult extends  Entity {
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
