package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 宠物狗实体列表类
 * 		
 * @author czh
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class MomentDetailResponse extends Entity{
	
	public final static String PREF_GOUQUAN_MOMENTS_LIST = "gouquan_moment_detail.pref";

	@XStreamAlias("code")
	private int code;
	
	@XStreamAlias("msg")
	private String msg;
	
	@XStreamAlias("moment")
	private Moment moment;

	public static String getPrefReadedNewsList() {
		return PREF_GOUQUAN_MOMENTS_LIST;
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

	public Moment getMoment() {
		return moment;
	}

	public void setMoment(Moment moment) {
		this.moment = moment;
	}

	@Override
	public String toString() {
		return "MomentDetailResponse{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", moment=" + moment +
				'}';
	}
}
