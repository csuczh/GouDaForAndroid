package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/10/26.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class Question extends Entity {
    @XStreamAlias("question_id")
    private int question_id;
    @XStreamAlias("title")
    private String title;
    @XStreamAlias("content")
    private String content;
    @XStreamAlias("title_image")
    private String title_image;
    @XStreamAlias("publisher_id")
    private int publisher_id;
    @XStreamAlias("publisher_logo")
    private String publisher_logo;
    @XStreamAlias("publisher_name")
    private String publisher_name;
    @XStreamAlias("likes_num")
    private int likes_num;
    @XStreamAlias("hot")
    private int hot;

    public int getHot() {
        return hot;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTitle_image() {
        return title_image;
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

    public int getLikes_num() {
        return likes_num;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle_image(String title_image) {
        this.title_image = title_image;
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

    public void setLikes_num(int likes_num) {
        this.likes_num = likes_num;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }
}
