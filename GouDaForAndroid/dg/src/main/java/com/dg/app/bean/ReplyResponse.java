package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class ReplyResponse implements Serializable{

    @XStreamAlias("code")
    private int code;   //状态码

    @XStreamAlias("msg")
    private String msg;//返回状态信息

    @XStreamAlias("reply_id")
    private int reply_id; //回复者id

    @XStreamAlias("time")
    private String time;//回复时间

    @Override
    public boolean equals(Object o) {
        if (o instanceof Tweet) {
            return ((ReplyResponse) o).reply_id == this.reply_id;
        } else {
            return false;
        }
    }

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

    public int getReply_id() {
        return reply_id;
    }

    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
