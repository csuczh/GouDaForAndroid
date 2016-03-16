package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("reply")
public class Reply implements Serializable, Comparable<Reply>{

    @XStreamAlias("reply_id")
    private int reply_id;   //回复信息id

    @XStreamAlias("content")
    private String content;//回复内容

    @XStreamAlias("sender_id")
    private int sender_id; //发送者id

    @XStreamAlias("sender_name")
    private String sender_name;//发送者昵称

    @XStreamAlias("sender_logo")
    private String sender_logo;//发送者头像

    @XStreamAlias("replied_id")
    private String replied_id; //被回复状态的id

    @XStreamAlias("replied_name")
    private String replied_name;//被回复状态的用户名

    @XStreamAlias("time")
    private String time;//回复时间

    public String getSender_logo() {
        return sender_logo;
    }

    public void setSender_logo(String sender_logo) {
        this.sender_logo = sender_logo;
    }

    public int getReply_id() {
        return reply_id;
    }

    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReplied_id() {
        return replied_id;
    }

    public void setReplied_id(String replied_id) {
        this.replied_id = replied_id;
    }

    public String getReplied_name() {
        return replied_name;
    }

    public void setReplied_name(String replied_name) {
        this.replied_name = replied_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "reply_id=" + reply_id +
                ", content='" + content + '\'' +
                ", sender_id='" + sender_id + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", sender_logo='" + sender_logo + '\'' +
                ", replied_id=" + replied_id +
                ", replied_name='" + replied_name + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Reply) {
            return ((Reply) o).reply_id == this.reply_id;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Reply another) {
        return another.reply_id-this.reply_id;
    }
}
