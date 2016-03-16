package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by lenovo on 2015/10/26.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class KindsList extends Entity implements ListEntity<Kind>{
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;

    @XStreamAlias("category")
    private List<Kind> list;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public List<Kind> getList() {
        return list;
    }
}
