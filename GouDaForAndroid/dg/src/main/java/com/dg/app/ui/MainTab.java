package com.dg.app.ui;

import com.dg.app.R;

import com.dg.app.fragment.DetailDogKnowFragment;
import com.dg.app.fragment.MeFragment;
import com.dg.app.fragment.MomentsFragment;
import com.dg.app.viewpagerfragment.GouDaMapViewFragment;


public enum MainTab {

	NEWS(0, R.string.main_tab_name_gouda, R.drawable.tab_icon_gouda,
			GouDaMapViewFragment.class),

	TWEET(1, R.string.main_tab_name_gouquan, R.drawable.tab_icon_gouquan,
			MomentsFragment.class),

//	QUICK(2, R.string.main_tab_name_quick, R.drawable.tab_icon_gouda,
//			null),

	EXPLORE(2, R.string.main_tab_name_gouzhi, R.drawable.tab_icon_gouzhi,
			DetailDogKnowFragment.class),

	ME(3, R.string.main_tab_name_my, R.drawable.tab_icon_me,
			MeFragment.class);


	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
		this.idx = idx;
		this.resName = resName;
		this.resIcon = resIcon;
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getResIcon() {
		return resIcon;
	}

	public void setResIcon(int resIcon) {
		this.resIcon = resIcon;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}
}
