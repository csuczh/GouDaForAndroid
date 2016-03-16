package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * 宠物狗实体列表类
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class RankingList extends Entity implements ListEntity<Ranking> {
	
	public final static String PREF_GOUQUAN_MOMENTS_LIST = "rongyaobang_ranking.pref";

	@XStreamAlias("code")
	private int code;
	
	@XStreamAlias("msg")
	private String msg;
	
	@XStreamAlias("ranking")
	private List<Ranking> list = new ArrayList<Ranking>();

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
	public List<Ranking> getList() {
		return list;
	}

	public void setList(List<Ranking> list) {
		this.list = list;
	}
}
