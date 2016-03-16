package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 登录用户实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressWarnings("serial")
@XStreamAlias("user")
public class DGRegistDog extends Entity {

    @XStreamAlias("user_id")
    private int user_id;

    @XStreamAlias("dog_logo")
    private String dog_logo;

    @XStreamAlias("dog_nickname")
    private String dog_nickname;

    @XStreamAlias("dog_variety")
    private String dog_variety;

    @XStreamAlias("dog_sex")
    private int dog_sex;

    @XStreamAlias("dog_age")
    private int dog_age;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDog_logo() {
        return dog_logo;
    }

    public void setDog_logo(String dog_logo) {
        this.dog_logo = dog_logo;
    }

    public String getDog_nickname() {
        return dog_nickname;
    }

    public void setDog_nickname(String dog_nickname) {
        this.dog_nickname = dog_nickname;
    }

    public String getDog_variety() {
        return dog_variety;
    }

    public void setDog_variety(String dog_variety) {
        this.dog_variety = dog_variety;
    }

    public int getDog_sex() {
        return dog_sex;
    }

    public void setDog_sex(int dog_sex) {
        this.dog_sex = dog_sex;
    }

    public int getDog_age() {
        return dog_age;
    }

    public void setDog_age(int dog_age) {
        this.dog_age = dog_age;
    }
}
