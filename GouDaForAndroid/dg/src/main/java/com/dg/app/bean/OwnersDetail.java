package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * 主人详情界面
 * @author czh
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("dg")
public class OwnersDetail extends Entity {
	
	@XStreamAlias("owners")
	private Owners owners;

	public Owners getOwners() {
		return owners;
	}

	public void setOwners(Owners owners) {
		this.owners = owners;
	}
}
