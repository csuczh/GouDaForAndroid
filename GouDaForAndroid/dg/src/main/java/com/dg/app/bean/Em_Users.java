package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/11/20.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class Em_Users extends Entity{
    @XStreamAlias("user_id")
    private int user_id;
    @XStreamAlias("nickname")
    private String nickname;
    @XStreamAlias("user_sex")
    private int user_sex;
    @XStreamAlias("user_logo")
    private String user_logo;
    @XStreamAlias("pos_lng")
    private double pos_lng;
    @XStreamAlias("pos_lat")
    private double pos_lat;
    @XStreamAlias("easemob_id")
    private String easemob_id;

    public int getUser_id() {
        return user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getUser_sex() {
        return user_sex;
    }

    public String getUser_logo() {
        return user_logo;
    }

    public double getPos_lng() {
        return pos_lng;
    }

    public double getPos_lat() {
        return pos_lat;
    }

    public String getEasemob_id() {
        return easemob_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public void setUser_logo(String user_logo) {
        this.user_logo = user_logo;
    }

    public void setPos_lng(double pos_lng) {
        this.pos_lng = pos_lng;
    }

    public void setPos_lat(double pos_lat) {
        this.pos_lat = pos_lat;
    }

    public void setEasemob_id(String easemob_id) {
        this.easemob_id = easemob_id;
    }
}
