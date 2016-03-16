package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
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
public class TagsList implements Serializable {
	
	public final static String PREF_SETTING_TAGS_LIST = "user_tagslist.pref";

	@XStreamAlias("code")
	private int code;
	
	@XStreamAlias("msg")
	private String msg;

	@XStreamAlias("tags")
	private List<Tag> tags = new ArrayList<>();

	public static String getPrefReadedNewsList() {
		return PREF_SETTING_TAGS_LIST;
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

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "TagsList [code=" + code + ", msg=" + msg + ", tags=" + tags
				+ "]";
	}

}
