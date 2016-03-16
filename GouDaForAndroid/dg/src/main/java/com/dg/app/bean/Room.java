package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czh on 2015/9/23.
 */
@SuppressWarnings("serial")
@XStreamAlias("rooms")
public class Room {
    @XStreamAlias("roomID")
    int roomID;
    @XStreamAlias("photos")
    List<String> photos=new ArrayList<>();
    @XStreamAlias("name")
    String name;
    @XStreamAlias("gender")
    int gender;
    @XStreamAlias("cuteValues")
    int cuteValues;
    @XStreamAlias("fansCount")
    int fansCount;
    @XStreamAlias("tags")
    List<String> tags=new ArrayList<>();
    @XStreamAlias("ownerSays")
    List<Say>   ownerSays=new ArrayList<>();

    public int getRoomID() {
        return roomID;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public int getCuteValues() {
        return cuteValues;
    }

    public int getFansCount() {
        return fansCount;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<Say> getOwnerSays() {
        return ownerSays;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setCuteValues(int cuteValues) {
        this.cuteValues = cuteValues;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setOwnerSays(List<Say> ownerSays) {
        this.ownerSays = ownerSays;
    }
}
