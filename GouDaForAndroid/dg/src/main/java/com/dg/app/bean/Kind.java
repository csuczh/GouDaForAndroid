package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lenovo on 2015/10/26.
 */
@SuppressWarnings("serial")
@XStreamAlias("item")
public class Kind extends Entity {
    @XStreamAlias("category_id")
     private int category_id;
    @XStreamAlias("name")
     private String name;
    @XStreamAlias("short")
    private String abbr;

    public int getCategory_id() {
        return category_id;
    }

    public String getName() {
        return name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }
}
