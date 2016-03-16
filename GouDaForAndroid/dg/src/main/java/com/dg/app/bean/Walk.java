package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/10/25.
 */
@SuppressWarnings("serial")
@XStreamAlias("walk")
public class Walk extends Entity{
    @XStreamAlias("walk_id")
    private int walk_id;
    @XStreamAlias("invite_explain")
    private String explain;
    @XStreamAlias("publisher_id")
    private int publisher_id;
    @XStreamAlias("user_sex")
    private int user_sex;
    @XStreamAlias("dog_sex")
    private int dog_sex;
    @XStreamAlias("publisher_name")
    private String publisher_name;
    @XStreamAlias("dog_nickname")
    private String dog_nickname;
    @XStreamAlias("time")
    private String time;
    @XStreamAlias("user_logo")
    private String user_logo;
    @XStreamAlias("dog_logo")
    private String dog_log;
    @XStreamAlias("lng")
    private double lng;
    @XStreamAlias("lat")
    private double lat;
    @XStreamAlias("deadline")
    private String deadline;
    @XStreamAlias("easemob_id")
    private String easemob_id;
    @XStreamAlias("is_accepted")
    private int is_accepted;
    @XStreamAlias("is_agreed")
    private int is_agreed;

    public int getWalk_id() {
        return walk_id;
    }

    public String getExplain() {
        return explain;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public int getUser_sex() {
        return user_sex;
    }

    public int getDog_sex() {
        return dog_sex;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public String getTime() {
        return time;
    }

    public String getUser_logo() {
        return user_logo;
    }

    public String getDog_log() {
        return dog_log;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public String getDeadline() {
        return deadline;
    }

    public int getIs_accepted() {
        return is_accepted;
    }

    public int getIs_agreed() {
        return is_agreed;
    }

    public void setWalk_id(int walk_id) {
        this.walk_id = walk_id;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public void setDog_sex(int dog_sex) {
        this.dog_sex = dog_sex;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUser_logo(String user_logo) {
        this.user_logo = user_logo;
    }

    public void setDog_log(String dog_log) {
        this.dog_log = dog_log;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setIs_accepted(int is_accepted) {
        this.is_accepted = is_accepted;
    }

    public void setIs_agreed(int is_agreed) {
        this.is_agreed = is_agreed;
    }

    public String getDog_nickname() {
        return dog_nickname;
    }

    public void setDog_nickname(String dog_nickname) {
        this.dog_nickname = dog_nickname;
    }

    public String getEasemob_id() {
        return easemob_id;
    }

    public void setEasemob_id(String easemob_id) {
        this.easemob_id = easemob_id;
    }
}
