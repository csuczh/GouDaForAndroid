package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class FosterDogInvitation extends Entity implements Serializable,Comparable<FosterDogInvitation>{

    @XStreamAlias("foster_id")
    private int foster_id;   //信息id

    @XStreamAlias("invite_explain")
    private String invite_explain;//邀请信息

    @XStreamAlias("publish_time")
    private String publish_time;//邀请时间

    @XStreamAlias("start_time")
    private String start_time;//开始时间

    @XStreamAlias("off_time")
    private String off_time;//结束时间


    public int getFoster_id() {
        return foster_id;
    }

    public void setFoster_id(int foster_id) {
        this.foster_id = foster_id;
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getOff_time() {
        return off_time;
    }

    public void setOff_time(String off_time) {
        this.off_time = off_time;
    }

    @Override
    public String toString() {
        return "FosterDogInvitation{" +
                "foster_id=" + foster_id +
                ", invite_explain='" + invite_explain + '\'' +
                ", publish_time='" + publish_time + '\'' +
                ", start_time='" + start_time + '\'' +
                ", off_time='" + off_time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FosterDogInvitation) {
            return ((FosterDogInvitation) o).foster_id == this.foster_id;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(FosterDogInvitation another) {
        return another.foster_id-this.foster_id;
    }
}
