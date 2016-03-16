package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class Message extends Entity implements Serializable, Comparable<Message>{

    @XStreamAlias("message_id")
    private int message_id;   //信息id

    @XStreamAlias("publisher_id")
    private int publisher_id; //发送者id

    @XStreamAlias("publisher_name")
    private String publisher_name;//发送者名字

    @XStreamAlias("publisher_logo")
    private String publisher_logo;//发送者头像

    @XStreamAlias("publish_time")
    private String publish_time;//发送时间

    @XStreamAlias("moment_id")
    private int moment_id; //被回复状态的id

    @XStreamAlias("content")
    private String content;//发送者名字

    @XStreamAlias("image")
    private String image;//

    @XStreamAlias("origin")
    private String origin;//被回复状态内容

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getPublisher_logo() {
        return publisher_logo;
    }

    public void setPublisher_logo(String publisher_logo) {
        this.publisher_logo = publisher_logo;
    }

    public int getMoment_id() {
        return moment_id;
    }

    public void setMoment_id(int moment_id) {
        this.moment_id = moment_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_id=" + message_id +
                ", publisher_id=" + publisher_id +
                ", publisher_name='" + publisher_name + '\'' +
                ", publisher_logo='" + publisher_logo + '\'' +
                ", publish_time='" + publish_time + '\'' +
                ", moment_id=" + moment_id +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", origin='" + origin + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Message) {
            return ((Message) o).message_id == this.message_id;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Message another) {
        return another.message_id-this.message_id;
    }
}
