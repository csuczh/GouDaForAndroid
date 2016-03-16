package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * 遛狗请求列表类
 * 		
 * @author czh
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class DateDogInvitationsList extends Entity implements ListEntity<DateDogInvitation>{
	
	public final static String PREF_ME_INVITATIONS_LIST = "me_xiangqing_invitations.pref";

	@XStreamAlias("code")
	private int code;

	@XStreamAlias("msg")
	private String msg;

	@XStreamAlias("dates")
	private List<DateDogInvitation> list = new ArrayList<DateDogInvitation>();

	public static String getPrefReadedNewsList() {
		return PREF_ME_INVITATIONS_LIST;
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
	public List<DateDogInvitation> getList() {
		return list;
	}

	public void setList(List<DateDogInvitation> list) {
		this.list = list;
	}
}
