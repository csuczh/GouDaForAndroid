package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianxiao on 2015/10/15.
 */
public class Replys implements Serializable{
	
	@XStreamImplicit(itemFieldName="item")
	private List<Reply> replys = new ArrayList<Reply>();

    public List<Reply> getReplys() {
		return replys;
	}

	public void setReplys(List<Reply> replys) {
		this.replys = replys;
	}
	
	@Override
	public String toString() {
		return "Replys [replys=" + replys + "]";
	}

}
