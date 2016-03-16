package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取 喜欢/回复  消息列表  响应Bean
 * Created by xianxiao on 2015/10/29.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class MessageResponse implements Serializable{

    @XStreamAlias("code")
    private int code;   //响应码

    @XStreamAlias("msg")
    private String msg; //响应信息

    @XStreamAlias("messages")
    private List<Message> messages = new ArrayList<>();  //返回状态信息

    @Override
    public String toString() {
        return "GouQuanResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", messages=" + messages +
                '}';
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
