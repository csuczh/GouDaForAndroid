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
public class DGRegistUser extends Entity {

    @XStreamAlias("user_id")
    private int user_id;

    @XStreamAlias("easemob_id")
    private String easemob_id;

    @XStreamAlias("phone")
    private String phone;

    @XStreamAlias("password")
    private String password;

    @XStreamAlias("area_code")
    private int area_code;

    @XStreamAlias("nickname")
    private String nickname;

    @XStreamAlias("sex")
    private int sex;

    @XStreamAlias("age")
    private int age;

    @XStreamAlias("pos")
    private String pos;

    @XStreamAlias("code")
    private String code;

    @XStreamAlias("logo")
    private String logo;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEasemob_id() {
        return easemob_id;
    }

    public void setEasemob_id(String easemob_id) {
        this.easemob_id = easemob_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getArea_code() {
        return area_code;
    }

    public void setArea_code(int area_code) {
        this.area_code = area_code;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "DGRegistUser{" +
                "user_id=" + user_id +
                ", easemob_id=" + easemob_id +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", area_code=" + area_code +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", pos='" + pos + '\'' +
                ", code='" + code + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
