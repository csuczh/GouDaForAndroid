package com.dg.app.bean;


import java.util.ArrayList;
import java.util.List;

/**
 *狗搭列表实体类
 * 		
 * @author czh
 * @created 2015年9月8日
 */
public class MapList extends Entity implements ListEntity<MapNodes> {
	


	public final static int CATALOG_ALL = 1;//所有的狗搭的模块
	public final static int CATALOG_LIUGOU = 2;//遛狗菜单
	public final static int CATALOG_XIANGQIN= 3;//相亲菜单
	
	public final static int CATALOG_JIYANG = 4;//寄养菜单

	

	private int catalog;
	

	private int pageSize;
	

	private int GouDaCount;
	

	private List<MapNodes> list = new ArrayList<MapNodes>();

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getGouDaCount() {
		return GouDaCount;
	}

	public void setGouDaCount(int gouDaCount) {
		GouDaCount = gouDaCount;
	}

	public List<MapNodes> getList() {
		return list;
	}

	public void setList(List<MapNodes> list) {
		this.list = list;
	}
}
