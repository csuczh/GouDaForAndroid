package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/11/30.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class Walk_Response extends Entity{
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;
    @XStreamAlias("walk_id")
    private int walk_id;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public int getWalk_id() {
        return walk_id;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setWalk_id(int walk_id) {
        this.walk_id = walk_id;
    }

    @Override
    public String toString() {
        return "Walk_Response{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", walk_id=" + walk_id +
                '}';
    }
}
