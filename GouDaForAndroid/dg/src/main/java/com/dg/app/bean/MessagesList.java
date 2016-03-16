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
public class MessagesList extends Entity implements ListEntity<Message> {
	
	public final static String PREF_GOUQUAN_MOMENTS_LIST = "gouquan_messages.pref";

	@XStreamAlias("code")
	private int code;
	
	@XStreamAlias("msg")
	private String msg;
	
	@XStreamAlias("messages")
	private List<Message> list = new ArrayList<Message>();

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
	public List<Message> getList() {
		return list;
	}

	public void setList(List<Message> list) {
		this.list = list;
	}
}
