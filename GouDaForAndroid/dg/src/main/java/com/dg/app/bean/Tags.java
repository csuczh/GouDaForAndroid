package com.dg.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Created by xianxiao on 2015/10/15.
 */

public class Tags implements Serializable{
	
	@XStreamImplicit(itemFieldName="item")
    private List<Tag> tags; //状态正文

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tag) {
		this.tags = tag;
	}

	@Override
	public String toString() {
		return "Tag [tags=" + tags + "]";
	}

	
}
