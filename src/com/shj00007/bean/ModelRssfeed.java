package com.shj00007.bean;

public class ModelRssfeed {
	private String tablename = "";
	private String rssname = "";
	private String category = "";
	private int unreadcount = 0;

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getRssname() {
		return rssname;
	}

	public void setRssname(String rssname) {
		this.rssname = rssname;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getUnreadcount() {
		return unreadcount;
	}

	public void setUnreadcount(int unreadcount) {
		this.unreadcount = unreadcount;
	}

}
