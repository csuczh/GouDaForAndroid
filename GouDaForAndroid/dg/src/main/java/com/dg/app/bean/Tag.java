package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

import java.io.Serializable;

@XStreamAlias("item")
public class Tag implements Serializable{
	
	@XStreamAlias("tag_id")
	private String tag_id;

	@XStreamAlias("name")
	private String name;

	public String getTag_id() {
		return tag_id;
	}

	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Tag{" +
				"tag_id='" + tag_id + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
