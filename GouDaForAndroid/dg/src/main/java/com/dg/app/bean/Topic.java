package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/10/27.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class Topic extends  Entity{
    @XStreamAlias("question_id")
    private int question_id;
    @XStreamAlias("title")
    private String title;
    @XStreamAlias("likes_num")
    private int likes_num;
    @XStreamAlias("publisher_id")
    private int publisher_id;
    @XStreamAlias("publisher_logo")
    private String publisher_logo;
    @XStreamAlias("publisher_name")
    private String publisher_name;

    public int getQuestion_id() {
        return question_id;
    }

    public String getTitle() {
        return title;
    }

    public int getLikes_num() {
        return likes_num;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public String getPublisher_logo() {
        return publisher_logo;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLikes_num(int likes_num) {
        this.likes_num = likes_num;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public void setPublisher_logo(String publisher_logo) {
        this.publisher_logo = publisher_logo;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }
}
