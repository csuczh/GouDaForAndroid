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
public class Ranking extends Entity implements Serializable, Comparable<Ranking>{

    @XStreamAlias("user_id")
    private int user_id;  //用户id

    @XStreamAlias("nickname")
    private String nickname; //用户昵称

    @XStreamAlias("sex")
    private int sex;  //性别0男1女

    @XStreamAlias("logo")
    private String logo; //发布者昵称

    @XStreamAlias("grade")
    private int grade;  //用户当月积分

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public int compareTo(Ranking another) {
        return another.grade-this.grade;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Ranking) {
            return ((Ranking) o).user_id == this.user_id;
        } else {
            return false;
        }
    }

}
