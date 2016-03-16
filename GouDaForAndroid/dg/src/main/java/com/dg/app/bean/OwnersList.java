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
public class OwnersList extends Entity implements ListEntity<Owners> {
	
	public final static String PREF_READED_NEWS_LIST = "readed_owners_list.pref";

	
	@XStreamAlias("code")
	private int code;
	
	@XStreamAlias("msg")
	private String msg;
	
	@XStreamAlias("users")
	private List<Owners> list = new ArrayList<Owners>();

	public static String getPrefReadedNewsList() {
		return PREF_READED_NEWS_LIST;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setList(List<Owners> list) {
		this.list = list;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public List<Owners> getList() {
		return list;
	}
}
