package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 登录  响应Bean
 * Created by xianxiao on 2015/10/29.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class LoginResponse implements Serializable{

    @XStreamAlias("code")
    private int code;   //响应码

    @XStreamAlias("msg")
    private String msg; //响应信息

    @XStreamAlias("userid")
    private int userid;   //用户id

    @XStreamAlias("cityid")
    private int cityid;   //用户id

    @XStreamAlias("nickname")
    private String nickname;   //用户昵称

    @XStreamAlias("sex")
    private int sex;   //用户id

    @XStreamAlias("userlogo")
    private String userlogo;   //用户头像

    @XStreamAlias("dog_sex")
    private int dog_sex;   //狗狗性别

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
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

    public String getUserlogo() {
        return userlogo;
    }

    public void setUserlogo(String userlogo) {
        this.userlogo = userlogo;
    }

    public int getDog_sex() {
        return dog_sex;
    }

    public void setDog_sex(int dog_sex) {
        this.dog_sex = dog_sex;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", userid=" + userid +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", userlogo='" + userlogo + '\'' +
                ", dog_sex=" + dog_sex +
                '}';
    }
}
