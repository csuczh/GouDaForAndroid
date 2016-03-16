package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class   Moment extends Entity implements Serializable, Comparable<Moment>{

    @XStreamAlias("moment_id")
    private int moment_id;  //状态id

    @XStreamAlias("content")
    private String content; //状态正文

    @XStreamAlias("publisher_id")
    private int publisher_id;  //发布者id

    @XStreamAlias("publisher_name")
    private String publisher_name; //发布者昵称

    @XStreamAlias("time")
    private String time; //发布时间

    @XStreamAlias("userlogo")
    private String userlogo; //发布者头像
    @XStreamAlias("easemob_id")
    private String easemob_id;

    public String getEasemob_id() {
        return easemob_id;
    }

    public void setEasemob_id(String easemob_id) {
        this.easemob_id = easemob_id;
    }
    //TODO  有待修改
//    @XStreamAlias("images")
//    private List<String> images = new ArrayList(); //发布的图片
//    public List<String> getImages() {
//        return images;
//    }
//
//    public void setImages(List<String> images) {
//        this.images = images;
//    }

    @XStreamAlias("images")
    private String images; //发布图片

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @XStreamAlias("likes_num")
    private int likes_num;  //喜欢数

    @XStreamAlias("reply_num")
    private int reply_num;  //回复数

    @XStreamAlias("liked")
    private int liked ; //是否喜欢

    public int getMoment_id() {
        return moment_id;
    }

    public void setMoment_id(int moment_id) {
        this.moment_id = moment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserlogo() {
        return userlogo;
    }

    public void setUserlogo(String userlogo) {
        this.userlogo = userlogo;
    }

    public int getLikes_num() {
        return likes_num;
    }

    public void setLikes_num(int likes_num) {
        this.likes_num = likes_num;
    }

    public int getReply_num() {
        return reply_num;
    }

    public void setReply_num(int reply_num) {
        this.reply_num = reply_num;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    @Override
    public String toString() {
        return "Moment{" +
                "moment_id=" + moment_id +
                ", content='" + content + '\'' +
                ", publisher_id=" + publisher_id +
                ", publisher_name='" + publisher_name + '\'' +
                ", time='" + time + '\'' +
                ", userlogo='" + userlogo + '\'' +
                ", images='" + images + '\'' +
                ", likes_num=" + likes_num +
                ", reply_num=" + reply_num +
                ", liked=" + liked +
                '}';
    }

    @Override
    public int compareTo(Moment another) {
        return another.moment_id-this.moment_id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Moment) {
            return ((Moment) o).moment_id == this.moment_id;
        } else {
            return false;
        }
    }

    public List<String> getSmallImg(){
        List<String> small_imgs = new ArrayList<>();

        String[] imgs = images.split(",");

        for(String s:imgs){
            s = "small_"+s;
            small_imgs.add(s);
        }
        return small_imgs;
    }

    public List<String> getMiddleImg(){
        List<String> mid_imgs = new ArrayList<>();

        String[] imgs = images.split(",");

        for(String s:imgs){
            s = "middle_"+s;
            mid_imgs.add(s);
        }
        return mid_imgs;
    }


}
