package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class UploadLogoResponse implements Serializable{

    @XStreamAlias("code")
    private int code;   //状态码

    @XStreamAlias("msg")
    private String msg;//错误或成功信息

    @XStreamAlias("url")
    private String url ; //图片地址(上传成功则有此字段)

    @XStreamAlias("error")
    private String error ; //文件上传错误信息(上次失败则有此字段)

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}


