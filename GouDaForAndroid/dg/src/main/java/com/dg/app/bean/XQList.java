package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/10/25.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class XQList extends  Entity{
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;

    @XStreamAlias("date")
    private XQ list ;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }



    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public XQ getList() {
        return list;
    }

    public void setList(XQ list) {
        this.list = list;
    }
}
