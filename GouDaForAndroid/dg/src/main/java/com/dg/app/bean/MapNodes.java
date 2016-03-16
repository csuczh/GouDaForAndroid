package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import com.dg.app.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 狗搭模块，遛狗 相亲 等
 * @author 程志豪
 * @created 2015年9月8日
 *
 */

public class MapNodes extends Entity {
	
	public final static int GODATYPE_ALL = 0;//0 全部信息
	public final static int GODATYPE_LIUGOU = 1;//1 遛狗
	public final static int GODATYPE_XIANGQIN = 2;//2 相亲
	public final static int GODATYPE_JIYANG = 3;//3 寄养
	

	private double longitude;
	

	private double  latitude;
	

	private String dogpicture;
	

	private String poplepicture;

	private  String message;

	private int sex;
	public MapNodes(double longitude, double latitude, String dogpicture, String poplepicture, String message) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.dogpicture = dogpicture;
		this.poplepicture = poplepicture;
		this.message = message;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setDogpicture(String dogpicture) {
		this.dogpicture = dogpicture;
	}

	public void setPoplepicture(String poplepicture) {
		this.poplepicture = poplepicture;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public String getDogpicture() {
		return dogpicture;
	}

	public String getPoplepicture() {
		return poplepicture;
	}

	public String getMessage() {
		return message;
	}
}
