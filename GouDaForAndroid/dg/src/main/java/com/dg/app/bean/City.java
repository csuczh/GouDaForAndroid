package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/10/25.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class City extends Entity  {
    @XStreamAlias("cityid")
    int cityid;
    @XStreamAlias("name")
    String name;
    @XStreamAlias("province")
    String province;

    public int getCityid() {
        return cityid;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
