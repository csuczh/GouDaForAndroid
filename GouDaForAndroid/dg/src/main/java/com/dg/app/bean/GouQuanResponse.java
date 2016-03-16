package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 同城/热门/关注/我的/详细  响应Bean
 * Created by xianxiao on 2015/10/29.
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class GouQuanResponse implements Serializable{

    @XStreamAlias("code")
    private int code;   //响应码

    @XStreamAlias("msg")
    private String msg; //响应信息

    @XStreamAlias("moments")
    private List<Moment> moments = new ArrayList<>();  //返回状态信息

    @Override
    public String toString() {
        return "GouQuanResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", moments=" + moments +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Moment> getMoments() {
        return moments;
    }

    public void setMoments(List<Moment> moments) {
        this.moments = moments;
    }
}
