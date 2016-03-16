package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class AddResponse implements Serializable{

    @XStreamAlias("code")
    private int code;   //状态码

    @XStreamAlias("msg")
    private String msg;//返回状态信息

    @XStreamAlias("moment_id")
    private int moment_id; //回复者id

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public int getMoment_id() {
        return moment_id;
    }

    public void setMoment_id(int moment_id) {
        this.moment_id = moment_id;
    }

    @Override
    public String toString() {
        return "AddResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", moment_id=" + moment_id +
                '}';
    }
}


