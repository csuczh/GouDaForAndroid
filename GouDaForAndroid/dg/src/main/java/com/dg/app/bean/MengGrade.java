package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianxiao on 2015/10/15.
 */
@SuppressWarnings("serial")
@XStreamAlias("grade")
public class MengGrade extends Entity implements Serializable{

    @XStreamAlias("mnt")
    private String mnt; //狗圈发布新的状态（纯文字）

    @XStreamAlias("mni")
    private String mni; //狗圈发布新的状态（图文）

    @XStreamAlias("ml")
    private String ml; //狗圈点赞

    @XStreamAlias("mld")
    private String mld; //狗圈被点赞

    @XStreamAlias("mr")
    private String mr; //狗圈评论

    @XStreamAlias("mrd")
    private String mrd; //狗圈被评论

    @XStreamAlias("ms")
    private String ms; //狗圈分享

    @XStreamAlias("mf")
    private String mf; //狗圈关注

    @XStreamAlias("zl")
    private String zl; //狗知点赞

    @XStreamAlias("zs")
    private String zs; //狗知分享

    @XStreamAlias("isd")
    private String isd; //狗搭发送邀请

    @XStreamAlias("ir")
    private String ir; //狗搭接受邀请

    @XStreamAlias("ia")
    private String ia; //狗搭同意接受的邀请

    @XStreamAlias("ish")
    private String ish; //狗搭打招呼

    @XStreamAlias("ol")
    private String ol; //登陆

    @XStreamAlias("oat")
    private String oat; //添加标签

    @XStreamAlias("ov")
    private String ov; //他人浏览我的主页

    @XStreamAlias("os")
    private String os; //分享到其他社交平台

    public String getMnt() {
        return mnt;
    }

    public void setMnt(String mnt) {
        this.mnt = mnt;
    }

    public String getMni() {
        return mni;
    }

    public void setMni(String mni) {
        this.mni = mni;
    }

    public String getMl() {
        return ml;
    }

    public void setMl(String ml) {
        this.ml = ml;
    }

    public String getMld() {
        return mld;
    }

    public void setMld(String mld) {
        this.mld = mld;
    }

    public String getMr() {
        return mr;
    }

    public void setMr(String mr) {
        this.mr = mr;
    }

    public String getMrd() {
        return mrd;
    }

    public void setMrd(String mrd) {
        this.mrd = mrd;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public String getMf() {
        return mf;
    }

    public void setMf(String mf) {
        this.mf = mf;
    }

    public String getZl() {
        return zl;
    }

    public void setZl(String zl) {
        this.zl = zl;
    }

    public String getZs() {
        return zs;
    }

    public void setZs(String zs) {
        this.zs = zs;
    }

    public String getIsd() {
        return isd;
    }

    public void setIsd(String isd) {
        this.isd = isd;
    }

    public String getIr() {
        return ir;
    }

    public void setIr(String ir) {
        this.ir = ir;
    }

    public String getIa() {
        return ia;
    }

    public void setIa(String ia) {
        this.ia = ia;
    }

    public String getIsh() {
        return ish;
    }

    public void setIsh(String ish) {
        this.ish = ish;
    }

    public String getOl() {
        return ol;
    }

    public void setOl(String ol) {
        this.ol = ol;
    }

    public String getOat() {
        return oat;
    }

    public void setOat(String oat) {
        this.oat = oat;
    }

    public String getOv() {
        return ov;
    }

    public void setOv(String ov) {
        this.ov = ov;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }


    @Override
    public String toString() {
        return "MengGrade{" +
                "mnt='" + mnt + '\'' +
                ", mni='" + mni + '\'' +
                ", ml='" + ml + '\'' +
                ", mld='" + mld + '\'' +
                ", mr='" + mr + '\'' +
                ", mrd='" + mrd + '\'' +
                ", ms='" + ms + '\'' +
                ", mf='" + mf + '\'' +
                ", zl='" + zl + '\'' +
                ", zs='" + zs + '\'' +
                ", isd='" + isd + '\'' +
                ", ir='" + ir + '\'' +
                ", ia='" + ia + '\'' +
                ", ish='" + ish + '\'' +
                ", ol='" + ol + '\'' +
                ", oat='" + oat + '\'' +
                ", ov='" + ov + '\'' +
                ", os='" + os + '\'' +
                '}';
    }

}
