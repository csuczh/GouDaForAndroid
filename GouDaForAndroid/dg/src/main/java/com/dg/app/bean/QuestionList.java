package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/10/26.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class QuestionList extends Entity implements ListEntity<Question> {
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;

    @XStreamAlias("questions")
    private List<Question> list=new ArrayList<Question>();

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public List<Question> getList() {
        return list;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setList(List<Question> list) {
        this.list = list;
    }
}
