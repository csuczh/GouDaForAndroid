package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class DateDogInvitation extends Entity implements Serializable,Comparable<DateDogInvitation>{

    @XStreamAlias("date_id")
    private int date_id;   //信息id

    @XStreamAlias("invite_explain")
    private String invite_explain;//邀请信息

    @XStreamAlias("publish_time")
    private String publish_time;//邀请时间

    @XStreamAlias("dog_variety")
    private String dog_variety;

    public int getDate_id() {
        return date_id;
    }

    public void setDate_id(int date_id) {
        this.date_id = date_id;
    }

    public String getInvite_explain() {
        return invite_explain;
    }

    public void setInvite_explain(String invite_explain) {
        this.invite_explain = invite_explain;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getDog_variety() {
        return dog_variety;
    }

    public void setDog_variety(String dog_variety) {
        this.dog_variety = dog_variety;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DateDogInvitation) {
            return ((DateDogInvitation) o).date_id == this.date_id;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(DateDogInvitation another) {
        return another.date_id-this.date_id;
    }
}
