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
public class FansList extends Entity implements ListEntity<Fan> {
	
	public final static String PREF_GOUQUAN_MOMENTS_LIST = "me_fans.pref";

	@XStreamAlias("code")
	private int code;
	
	@XStreamAlias("msg")
	private String msg;
	
	@XStreamAlias("fansList")
	private List<Fan> list = new ArrayList<Fan>();

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

	@Override
	public List<Fan> getList() {
		return list;
	}

	public void setList(List<Fan> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "FansList{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", list=" + list +
				'}';
	}
}
