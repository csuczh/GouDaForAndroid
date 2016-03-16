package com.dg.app.bean;

/**
 * Created by czh on 2015/9/20.
 */
public class DialogEntity {
    //关于狗的照片，性别，昵称
    private String dogPictureUrl;
    private int dogSex;
    private String dogNickName;
    //关于人的照片，性别，昵称
    private   String peoplePictureUrl;
    private   int peopleSex;
    private String peopleNickName;
    //消息内容
    private String messageContent;
    //开始时间和截至时间
    private String start_end_time;
    //判断是否是其他人
    private int isOther;
    //距离
    private String distance;
    //发表的日期
    private String postTime;

    public DialogEntity(String dogPictureUrl, int dogSex, String dogNickName, String peoplePictureUrl, int peopleSex, String peopleNickName, String messageContent, String start_end_time, int isOther, String distance, String postTime) {
        this.dogPictureUrl = dogPictureUrl;
        this.dogSex = dogSex;
        this.dogNickName = dogNickName;
        this.peoplePictureUrl = peoplePictureUrl;
        this.peopleSex = peopleSex;
        this.peopleNickName = peopleNickName;
        this.messageContent = messageContent;
        this.start_end_time = start_end_time;
        this.isOther = isOther;
        this.distance = distance;
        this.postTime = postTime;
    }

    public String getDogPictureUrl() {
        return dogPictureUrl;
    }

    public int getDogSex() {
        return dogSex;
    }

    public String getDogNickName() {
        return dogNickName;
    }

    public String getPeoplePictureUrl() {
        return peoplePictureUrl;
    }

    public int getPeopleSex() {
        return peopleSex;
    }

    public String getPeopleNickName() {
        return peopleNickName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getStart_end_time() {
        return start_end_time;
    }

    public int getIsOther() {
        return isOther;
    }

    public String getDistance() {
        return distance;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setDogPictureUrl(String dogPictureUrl) {
        this.dogPictureUrl = dogPictureUrl;
    }

    public void setDogSex(int dogSex) {
        this.dogSex = dogSex;
    }

    public void setDogNickName(String dogNickName) {
        this.dogNickName = dogNickName;
    }

    public void setPeoplePictureUrl(String peoplePictureUrl) {
        this.peoplePictureUrl = peoplePictureUrl;
    }

    public void setPeopleSex(int peopleSex) {
        this.peopleSex = peopleSex;
    }

    public void setPeopleNickName(String peopleNickName) {
        this.peopleNickName = peopleNickName;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setStart_end_time(String start_end_time) {
        this.start_end_time = start_end_time;
    }

    public void setIsOther(int isOther) {
        this.isOther = isOther;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
}
