package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by lenovo on 2015/11/20.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class Em_User_List extends Entity implements ListEntity<Em_Users>{
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;

    @XStreamAlias("users")
    private List<Em_Users> list;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public List<Em_Users> getList() {
        return list;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setList(List<Em_Users> list) {
        this.list = list;
    }
}
