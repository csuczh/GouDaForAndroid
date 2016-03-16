package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/10/27.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class TopicList extends  Entity implements ListEntity<Topic>{
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;

    @XStreamAlias("questions")
    private List<Topic> list = new ArrayList<Topic>();

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<Topic> getList() {
        return list;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setList(List<Topic> list) {
        this.list = list;
    }
}
