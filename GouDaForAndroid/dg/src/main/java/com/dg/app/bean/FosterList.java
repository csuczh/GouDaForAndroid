package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/10/25.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class FosterList extends Entity{
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;

    @XStreamAlias("foster")
    private Foster list ;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Foster getList() {
        return list;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setList(Foster list) {
        this.list = list;
    }
}
