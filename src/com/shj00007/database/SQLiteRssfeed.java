package com.shj00007.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.shj00007.bean.ModelRssfeed;
import com.shj00007.database.base.SQLiteBase;

public class SQLiteRssfeed extends SQLiteBase {

	public SQLiteRssfeed(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}

	public void addFeed(String pRssName, String pCategory, int pUnread,
			String pFeedLink) {
		mDatabase
				.execSQL("INSERT INTO rssfeed_list(rssname,category,unread,feedlink) VALUES('"
						+ pRssName
						+ "','"
						+ pCategory
						+ "','"
						+ pUnread
						+ "','"
						+pFeedLink
						+"');");
						

	}

	public ArrayList<String> getRssCategoryList() {
		ArrayList<String> _CategoryList = new ArrayList<String>();
		Cursor _Cursor = mDatabase.rawQuery(
				"Select distinct category From rssfeed_list;", null);
		while (_Cursor.moveToNext()) {
			_CategoryList.add(getCursorString(_Cursor, "category"));
		}
		_Cursor.close();
		return _CategoryList;
	}

	public ArrayList<ModelRssfeed> getRssfeedList() {
		ArrayList<ModelRssfeed> _ModelRssfeedList = new ArrayList<ModelRssfeed>();
		ModelRssfeed _ModelRssfeed = null;
		Cursor _Cursor = mDatabase
				.rawQuery("Select * From rssfeed_list;", null);
		while (_Cursor.moveToNext()) {
			_ModelRssfeed = new ModelRssfeed();
			_ModelRssfeed.setTablename("table"
					+ getCursorString(_Cursor, "_id"));
			_ModelRssfeed.setCategory(getCursorString(_Cursor, "category"));
			_ModelRssfeed.setRssname(getCursorString(_Cursor, "rssname"));
			_ModelRssfeed.setUnreadcount(getCursorInt(_Cursor, "unread"));
			_ModelRssfeedList.add(_ModelRssfeed);
		}
		_Cursor.close();

		return _ModelRssfeedList;
	}

	public void deleteRssFeed(String pRssFeedName) {
		mDatabase.execSQL("delete from rssfeed_list where rssfeed_name='"
				+ pRssFeedName + "';");
	}

	public void updateRssFeedCount(String pRssFeedName, int pUnreadCount) {
		mDatabase.execSQL("update rssfeed_list set unread=" + pUnreadCount
				+ " where rssname='" + pRssFeedName + "'");
	}

	public void setRssHasRead(String pRssName) {
		// TODO Auto-generated method stub
		mDatabase.execSQL("update rssfeed_list set unread=0 where rssname='"+pRssName+"'");
	}

}
