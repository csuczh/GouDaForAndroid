package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class RegistResponse implements Serializable{

    @XStreamAlias("code")
    private int code;   //状态码

    @XStreamAlias("msg")
    private String msg;//返回状态信息

    @XStreamAlias("userid")
    private int userid; //新注册用户id

    @XStreamAlias("easemob_id")
    private String easemob_id;//用户环信账号

    @XStreamAlias("cityid")
    private int cityid; //用户城市id

    @XStreamAlias("nickname")
    private String nickname;//用户昵称

    @XStreamAlias("sex")
    private int sex; //性别0男 1女

    @XStreamAlias("userlogo")
    private String userlogo ;//用户头像url

    @XStreamAlias("age")
    private int age ; //年龄

    @XStreamAlias("career")
    private String career ;//职业

    @XStreamAlias("dog_sex")
    private int dog_sex  ; //狗的性别0公 1母(如果没有狗则没有此字段)

    @XStreamAlias("fans_num")
    private int fans_num ; //粉丝数

    @XStreamAlias("follow_num")
    private int follow_num ; //关注数

    @XStreamAlias("grade")
    private int grade ; //积分

    @XStreamAlias("gradeTag")
    private String gradeTag ;//积分称号

    @XStreamAlias("have_dog")
    private int have_dog ; //用户是否有狗0为无1为有

    @Override
    public boolean equals(Object o) {
        if (o instanceof Tweet) {
            return ((RegistResponse) o).userid == this.userid;
        } else {
            return false;
        }
    }

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

    public String getEasemob_id() {
        return easemob_id;
    }

    public void setEasemob_id(String easemob_id) {
        this.easemob_id = easemob_id;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getGradeTag() {
        return gradeTag;
    }

    public void setGradeTag(String gradeTag) {
        this.gradeTag = gradeTag;
    }

    public int getHave_dog() {
        return have_dog;
    }

    public void setHave_dog(int have_dog) {
        this.have_dog = have_dog;
    }

    @Override
    public String toString() {
        return "RegistResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", userid=" + userid +
                ", easemob_id='" + easemob_id + '\'' +
                ", cityid=" + cityid +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", userlogo='" + userlogo + '\'' +
                ", age=" + age +
                ", career='" + career + '\'' +
                ", dog_sex=" + dog_sex +
                ", fans_num=" + fans_num +
                ", follow_num=" + follow_num +
                ", grade=" + grade +
                ", gradeTag='" + gradeTag + '\'' +
                ", have_dog=" + have_dog +
                '}';
    }
}
