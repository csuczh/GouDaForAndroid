package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 *  响应Bean
 * Created by xianxiao on 2015/10/29.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class ReplyListResponse implements Serializable{

    @XStreamAlias("code")
    private int code;   //响应码

    @XStreamAlias("msg")
    private String msg; //响应信息

    @XStreamAlias("replies")
    private Replys replies;  //返回评论列表信息息

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

    public Replys getReplies() {
        return replies;
    }

    public void setReplies(Replys replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "ReplyListResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", replies=" + replies +
                '}';
    }
}
