package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class Fan extends Entity implements Serializable, Comparable<Fan>{

    @XStreamAlias("user_id")
    private int user_id; //登陆用户id

    @XStreamAlias("nickname")
    private String nickname; //登陆用户昵称

    @XStreamAlias("user_age")
    private int user_age; //登陆用户年龄

    @XStreamAlias("user_sex")
    private int user_sex; //登陆用户性别0男1女

    @XStreamAlias("user_logo")
    private String user_logo; //登陆用户的缩略图地址

    @XStreamAlias("career")
    private String career; //人的职业

    @XStreamAlias("dog_sex")
    private int dog_sex; //发布狗狗性别0公1母

    @XStreamAlias("city_name")
    private String city_name; //用户所在城市名


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

    public int getUser_age() {
        return user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public int getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_logo() {
        return user_logo;
    }

    public void setUser_logo(String user_logo) {
        this.user_logo = user_logo;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public int getDog_sex() {
        return dog_sex;
    }

    public void setDog_sex(int dog_sex) {
        this.dog_sex = dog_sex;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    @Override
    public String toString() {
        return "Fan{" +
                "user_id=" + user_id +
                ", nickname='" + nickname + '\'' +
                ", user_age=" + user_age +
                ", user_sex=" + user_sex +
                ", user_logo='" + user_logo + '\'' +
                ", career='" + career + '\'' +
                ", dog_sex=" + dog_sex +
                ", city_name=" + city_name +
                '}';
    }

    @Override
    public int compareTo(Fan another) {
        return another.user_id-this.user_id;
    }
}
