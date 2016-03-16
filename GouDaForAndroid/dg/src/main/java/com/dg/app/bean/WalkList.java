package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/10/25.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class WalkList extends Entity  {
    @XStreamAlias("code")
    private int code;

    @XStreamAlias("msg")
    private String msg;

    @XStreamAlias("walk")
    private Walk list ;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }



    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Walk getList() {
        return list;
    }

    public void setList(Walk list) {
        this.list = list;
    }
}
