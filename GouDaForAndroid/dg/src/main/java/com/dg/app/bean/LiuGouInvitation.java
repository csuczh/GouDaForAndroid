package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class LiuGouInvitation extends Entity implements Serializable, Comparable<LiuGouInvitation>{

    @XStreamAlias("walk_id")
    private int walk_id;   //信息id

    @XStreamAlias("invite_explain")
    private String invite_explain;//邀请信息

    @XStreamAlias("publish_time")
    private String publish_time;//邀请时间

    public int getWalk_id() {
        return walk_id;
    }

    public void setWalk_id(int walk_id) {
        this.walk_id = walk_id;
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

    @Override
    public String toString() {
        return "LiuGouInvitation{" +
                "walk_id=" + walk_id +
                ", invite_explain='" + invite_explain + '\'' +
                ", publish_time='" + publish_time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LiuGouInvitation) {
            return ((LiuGouInvitation) o).walk_id == this.walk_id;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(LiuGouInvitation another) {
        return another.walk_id-this.walk_id;
    }
}
