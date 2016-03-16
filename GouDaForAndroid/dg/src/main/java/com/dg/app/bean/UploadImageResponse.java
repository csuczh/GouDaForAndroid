package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class UploadImageResponse implements Serializable{

    @XStreamAlias("code")
    private int code;   //状态码

    @XStreamAlias("msg")
    private String msg;//返回状态信息

    @XStreamAlias("url")
    private String url ; //回复者id

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


