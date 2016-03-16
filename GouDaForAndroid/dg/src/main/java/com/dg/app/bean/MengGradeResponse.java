package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * 宠物狗实体列表类
 * 		
 * @author czh
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class MengGradeResponse extends Entity{
	
	public final static String PREF_GOUQUAN_MOMENTS_LIST = "mengyaobang_mission.pref";

	@XStreamAlias("code")
	private int code;
	
	@XStreamAlias("msg")
	private String msg;
	
	@XStreamAlias("grade")
	private MengGrade mengGrade;

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

	public MengGrade getMengGrade() {
		return mengGrade;
	}

	public void setMengGrade(MengGrade mengGrade) {
		this.mengGrade = mengGrade;
	}

	@Override
	public String toString() {
		return "MengGradeResponse{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", mengGrade=" + mengGrade +
				'}';
	}
}
