package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by czh on 2015/9/23.
 */
@SuppressWarnings("serial")
@XStreamAlias("says")
public class Say {
    @XStreamAlias("year")
    String year;
    @XStreamAlias("month")
    String month;
    @XStreamAlias("sayUrl")
    String sayUrl;
    @XStreamAlias("content")
    String content;

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getSayUrl() {
        return sayUrl;
    }

    public String getContent() {
        return content;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setSayUrl(String sayUrl) {
        this.sayUrl = sayUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
