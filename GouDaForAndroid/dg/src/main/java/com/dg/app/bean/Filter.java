package com.dg.app.bean;

/**
 * Created by lenovo on 2015/10/20.
 */
public class Filter {
    int type;

    boolean onoff;
    int peopleSex;
    int  dogSex;

    String province;
    String  city;
    String dist;

    String ageMax;
    String ageMin;

    String lgt;
    String lat;


    public void setType(int type) {
        this.type = type;
    }


    public int getType() {
        return type;
    }

    public boolean isOnoff() {
        return onoff;
    }

    public int getPeopleSex() {
        return peopleSex;
    }

    public int getDogSex() {
        return dogSex;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDist() {
        return dist;
    }

    public String getAgeMax() {
        return ageMax;
    }

    public String getAgeMin() {
        return ageMin;
    }

    public String getLgt() {
        return lgt;
    }

    public String getLat() {
        return lat;
    }

    public void setOnoff(boolean onoff) {
        this.onoff = onoff;
    }

    public void setPeopleSex(int peopleSex) {
        this.peopleSex = peopleSex;
    }

    public void setDogSex(int dogSex) {
        this.dogSex = dogSex;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public void setAgeMax(String ageMax) {
        this.ageMax = ageMax;
    }

    public void setAgeMin(String ageMin) {
        this.ageMin = ageMin;
    }

    public void setLgt(String lgt) {
        this.lgt = lgt;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
