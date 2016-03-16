package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class Collection extends Entity implements Serializable, Comparable<Collection>{

    @XStreamAlias("question_id")
    private int question_id;   //狗知问题id

    @XStreamAlias("title")
    private String title ;//标题

    @XStreamAlias("publisher_id")
    private int publisher_id; //发布人用户id

    @XStreamAlias("publisher_logo")
    private String publisher_logo;//发布人头像

    @XStreamAlias("publisher_name")
    private String publisher_name;//发布人昵称

    @XStreamAlias("publish_time")
    private String publish_time;//发布时间

    @XStreamAlias("likes_num")
    private int likes_num;//赞数

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getPublisher_logo() {
        return publisher_logo;
    }

    public void setPublisher_logo(String publisher_logo) {
        this.publisher_logo = publisher_logo;
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

    public int getLikes_num() {
        return likes_num;
    }

    public void setLikes_num(int likes_num) {
        this.likes_num = likes_num;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "question_id=" + question_id +
                ", title='" + title + '\'' +
                ", publisher_id=" + publisher_id +
                ", publisher_logo='" + publisher_logo + '\'' +
                ", publisher_name='" + publisher_name + '\'' +
                ", publish_time='" + publish_time + '\'' +
                ", likes_num=" + likes_num +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Collection) {
            return ((Collection) o).question_id == this.question_id;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Collection another) {
        return another.question_id-this.question_id;
    }
}
