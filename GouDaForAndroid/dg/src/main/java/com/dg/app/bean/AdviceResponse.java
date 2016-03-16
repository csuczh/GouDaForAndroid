package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 赞某动态/删除动态评论/删除动态  响应Bean
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class AdviceResponse implements Serializable{

    @XStreamAlias("code")
    private int code;   //状态码

    @XStreamAlias("msg")
    private String msg;//返回状态信息

    @XStreamAlias("advice_id")
    private int advice_id;

    public boolean OK() {
        return code == 0;
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

    public int getAdvice_id() {
        return advice_id;
    }

    public void setAdvice_id(int advice_id) {
        this.advice_id = advice_id;
    }

    @Override
    public String toString() {
        return "AdviceResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", advice_id=" + advice_id +
                '}';
    }
}


