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
public class CollectionsList extends Entity implements ListEntity<Collection> {
	
	private final static String PREF_DOGKNOW_COLLECTIONS_LIST = "wode_collections.pref";

	@XStreamAlias("code")
	private int code;
	
	@XStreamAlias("msg")
	private String msg;
	
	@XStreamAlias("questions")
	private List<Collection> list = new ArrayList<Collection>();

	public static String getPrefReadedNewsList() {
		return PREF_DOGKNOW_COLLECTIONS_LIST;
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
	public List<Collection> getList() {
		return list;
	}

	public void setList(List<Collection> list) {
		this.list = list;
	}
}
