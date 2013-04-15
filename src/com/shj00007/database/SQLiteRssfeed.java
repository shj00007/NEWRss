package com.shj00007.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shj00007.bean.ModelRssfeed;
import com.shj00007.database.base.SQLiteBase;

public class SQLiteRssfeed extends SQLiteBase {

	public SQLiteRssfeed(Context pContext,SQLiteDatabase pDatabase) {
		super(pContext,pDatabase);
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
						+ "','" + pFeedLink + "');");

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
				.rawQuery("SELECT * FROM rssfeed_list;", null);
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
		mDatabase.execSQL("DELETE FROM rssfeed_list WHERE rssfeed_name='"
				+ pRssFeedName + "';");
	}

	public void updateRssFeedCount(String pRssFeedName, int pUnreadCount) {
		mDatabase.execSQL("UPDATE rssfeed_list SET unread=" + pUnreadCount
				+ " WHERE rssname='" + pRssFeedName + "'");
	}

	public void setRssHasRead(String pRssName) {
		// TODO Auto-generated method stub
		mDatabase.execSQL("UPDATE rssfeed_list SET unread=0 WHERE rssname='"
				+ pRssName + "'");
	}

	public boolean isRssExist(String pLink) {
		Cursor _Cursor = mDatabase.rawQuery(
				"SELECT * FROM rssfeed_list WHERE feedlink='" + pLink + "'",
				null);
		boolean _isExist = _Cursor.moveToNext();
		_Cursor.close();
		if (_isExist) {
			Log.i("test", "plink!=null");
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<String> getFeedLinkList() {
		ArrayList<String> _FeedLinkList=new ArrayList<String>();
		Cursor _Cursor=mDatabase.rawQuery("select feedlink from rssfeed_list;", null);
		while(_Cursor.moveToNext()){
			_FeedLinkList.add(getCursorString(_Cursor, "feedlink"));
		}
		_Cursor.close();
		return _FeedLinkList;
	}

}
