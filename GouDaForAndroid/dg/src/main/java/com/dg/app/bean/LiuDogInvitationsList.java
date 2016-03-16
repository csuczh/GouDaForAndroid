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
public class LiuDogInvitationsList extends Entity implements ListEntity<LiuGouInvitation> {
	
	public final static String PREF_ME_INVITATIONS_LIST = "me_liugou_invitations.pref";

	@XStreamAlias("code")
	private int code;
	
	@XStreamAlias("msg")
	private String msg;
	
	@XStreamAlias("walks")
	private List<LiuGouInvitation> list = new ArrayList<LiuGouInvitation>();

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
	public List<LiuGouInvitation> getList() {
		return list;
	}

	public void setList(List<LiuGouInvitation> list) {
		this.list = list;
	}
}
