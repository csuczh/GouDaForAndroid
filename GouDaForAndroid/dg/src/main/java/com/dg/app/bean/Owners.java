package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import com.dg.app.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
@XStreamAlias("item")
public class Owners extends Entity {
	@XStreamAlias("user_id")
	private int user_id;

	@XStreamAlias("user_logo")
	private String user_logo;
	@XStreamAlias("nickname")
	private String nickname;
	
	@XStreamAlias("user_age")
	private int user_age;
	
	@XStreamAlias("user_sex")
	private int user_sex;
	
	@XStreamAlias("pos_lng")
	private double pos_lng;
	
	@XStreamAlias("pos_lat")
	private double pos_lat;
	
	@XStreamAlias("career")
	private String career;

	@XStreamAlias("dog_sex")
	private int dog_sex;

	@XStreamAlias("login_time")
	private String login_time;

	@XStreamAlias("easemob_id")
	private String easemob_id;
	public int getUser_id() {
		return user_id;
	}

	public String getNickname() {
		return nickname;
	}

	public int getUser_age() {
		return user_age;
	}

	public int getUser_sex() {
		return user_sex;
	}

	public double getPos_lng() {
		return pos_lng;
	}

	public double getPos_lat() {
		return pos_lat;
	}

	public String getCareer() {
		return career;
	}

	public int getDog_sex() {
		return dog_sex;
	}

	public String getLogin_time() {
		return login_time;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setUser_age(int user_age) {
		this.user_age = user_age;
	}

	public void setUser_sex(int user_sex) {
		this.user_sex = user_sex;
	}

	public void setPos_lng(double pos_lng) {
		this.pos_lng = pos_lng;
	}

	public void setPos_lat(double pos_lat) {
		this.pos_lat = pos_lat;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public void setDog_sex(int dog_sex) {
		this.dog_sex = dog_sex;
	}

	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}

	public void setUser_logo(String user_logo) {
		this.user_logo = user_logo;
	}

	public String getUser_logo() {
		return user_logo;
	}
	public void setEasemob_id(String easemob_id) {
		this.easemob_id = easemob_id;
	}

	public String getEasemob_id() {
		return easemob_id;
	}
}
