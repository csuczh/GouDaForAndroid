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
@XStreamAlias("gouda")
public class DGUser extends Entity{

    @XStreamAlias("code")
    private int code; //错误码 0为成功

    @XStreamAlias("msg")
    private String msg;//错误或成功信息

    @XStreamAlias("userid")
    private int userid;//登陆用户id

    @XStreamAlias("cityid")
    private int cityid;//用户所在城市id

    @XStreamAlias("nickname")
    private String nickname;//用户昵称

    @XStreamAlias("sex")
    private int sex;//用户性别 性别0男 1女

    @XStreamAlias("userlogo")
    private String userlogo;//用户头像url

    @XStreamAlias("age")
    private int age;//用户年龄

    @XStreamAlias("career")
    private String career;//职业

    @XStreamAlias("easemob_id")
    private String easemob_id;//用户环信账号

    @XStreamAlias("dog_sex")
    private int dog_sex;//狗的性别0公 1母(如果没有狗则没有此字段)

    @XStreamAlias("fans_num")
    private int fans_num;

    @XStreamAlias("follow_num")
    private int follow_num;

    @XStreamAlias("have_dog")
    private int have_dog;


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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getEasemob_id() {
        return easemob_id;
    }

    public void setEasemob_id(String easemob_id) {
        this.easemob_id = easemob_id;
    }

    public int getFans_num() {
        return fans_num;
    }

    public void setFans_num(int fans_num) {
        this.fans_num = fans_num;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public int getHave_dog() {
        return have_dog;
    }

    public void setHave_dog(int have_dog) {
        this.have_dog = have_dog;
    }

    @Override
    public String toString() {
        return "DGUser{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", userid=" + userid +
                ", cityid=" + cityid +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", userlogo='" + userlogo + '\'' +
                ", age=" + age +
                ", career='" + career + '\'' +
                ", easemob_id='" + easemob_id + '\'' +
                ", dog_sex=" + dog_sex +
                ", fans_num=" + fans_num +
                ", follow_num=" + follow_num +
                ", have_dog=" + have_dog +
                '}';
    }
}
