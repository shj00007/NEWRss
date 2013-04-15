package com.shj00007.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shj00007.bean.ModelRssItem;
import com.shj00007.database.base.SQLiteBase;

public class SQLiteRssItem extends SQLiteBase {

	public SQLiteRssItem(Context pContext,SQLiteDatabase pDatabase) {
		super(pContext,pDatabase);
		// TODO Auto-generated constructor stub

	}

	public void createTable(String pRssFeedName) {
		String _TableName = getTableName(pRssFeedName);
		mDatabase
				.execSQL("CREATE TABLE IF NOT EXISTS "
						+ _TableName
						+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, pubdate DATETIME, category VARCHAR,image VARCHAR,descriptionMD5 VARCHAR,isread INTEGER,starred INTEGER,link VARCHAR)");
	}

	public void addNews(String pRssFeedName, String pTitle, String pPubdate,
			String pCategory, String pLink, String pDescriptionMD5) {
		String _TableName = getTableName(pRssFeedName);
		mDatabase
				.execSQL("INSERT INTO "
						+ _TableName
						+ "(title,pubdate,category,link,descriptionMD5,isread,starred) VALUES('"
						+ pTitle + "','" + pPubdate + "','" + pCategory + "','"
						+ pLink + "','" + pDescriptionMD5 + "',0,0);");
	}

	public ArrayList<ModelRssItem> getRssItemList(String pRssFeedName) {
		ArrayList<ModelRssItem> _ModelRssItems = new ArrayList<ModelRssItem>();
		ModelRssItem _ModelRssItem = null;
		String _TableName = getTableName(pRssFeedName);
		String _Sql = "SELECT title,pubdate,category,image,descriptionMD5,isread,starred FROM "
				+ _TableName + " ORDER BY _id DESC";
		Cursor _Cursor = mDatabase.rawQuery(_Sql, null);
		while (_Cursor.moveToNext()) {
			_ModelRssItem = new ModelRssItem();
			_ModelRssItem.setNewstitle(getCursorString(_Cursor, "title"));
			_ModelRssItem.setPubdate(getCursorString(_Cursor, "pubdate"));
			_ModelRssItem.setCategory(getCursorString(_Cursor, "category"));
			_ModelRssItem.setImage(getCursorString(_Cursor, "image"));
			_ModelRssItem.setDescriptionmd5(getCursorString(_Cursor,
					"descriptionMD5"));
			_ModelRssItem.setIsread(getBooleanResult(getCursorInt(_Cursor,
					"isread")));
			_ModelRssItem.setStarred(getBooleanResult(getCursorInt(_Cursor,
					"starred")));
			_ModelRssItems.add(_ModelRssItem);
		}
		_Cursor.close();
		return _ModelRssItems;
	}

	public ArrayList<ModelRssItem> getRssItemList(String pRssFeedName,
			boolean pOnlyViewUnread) {
		ArrayList<ModelRssItem> _ModelRssItems = new ArrayList<ModelRssItem>();
		ModelRssItem _ModelRssItem = null;
		String _TableName = getTableName(pRssFeedName);
		String _Sql = null;
		if (pOnlyViewUnread)
			_Sql = "SELECT title,pubdate,category,image,descriptionMD5,isread,starred FROM "
					+ _TableName + " WHERE ISREAD=0 ORDER BY _id DESC";
		else
			_Sql = "SELECT title,pubdate,category,image,descriptionMD5,isread,starred FROM "
					+ _TableName + " ORDER BY _id DESC";
		Cursor _Cursor = mDatabase.rawQuery(_Sql, null);
		while (_Cursor.moveToNext()) {
			_ModelRssItem = new ModelRssItem();
			_ModelRssItem.setNewstitle(getCursorString(_Cursor, "title"));
			_ModelRssItem.setPubdate(getCursorString(_Cursor, "pubdate"));
			_ModelRssItem.setCategory(getCursorString(_Cursor, "category"));
			_ModelRssItem.setImage(getCursorString(_Cursor, "image"));
			_ModelRssItem.setDescriptionmd5(getCursorString(_Cursor,
					"descriptionMD5"));
			_ModelRssItem.setIsread(getBooleanResult(getCursorInt(_Cursor,
					"isread")));
			_ModelRssItem.setStarred(getBooleanResult(getCursorInt(_Cursor,
					"starred")));
			_ModelRssItems.add(_ModelRssItem);
		}
		_Cursor.close();
		return _ModelRssItems;

	}

	public void deleteRssItemTable(String pRssFeedName) {
		String _TableName = getTableName(pRssFeedName);
		mDatabase.execSQL("DROP TABLE '" + _TableName + "';");
	}

	

	public String getLastNews(String pRssFeedName) {
		String news = "";
		String _TableName = getTableName(pRssFeedName);
		Cursor _Cursor = mDatabase.rawQuery("SELECT * FROM " + _TableName
				+ " ORDER BY _id DESC LIMIT 1", null);
		if (_Cursor.moveToNext()) {
			news = getCursorString(_Cursor, "title");
		}
		_Cursor.close();
		return news;
	}

	public String getDescriptionMD5(String pRssName, String pItemName) {
		String _tablename = getTableName(pRssName);
		String _descriptionMD5 = "";
		Cursor _Cursor = mDatabase.rawQuery("select descriptionMD5 from "
				+ _tablename + " where  title='" + pItemName + "'", null);
		if (_Cursor.moveToNext()) {
			_descriptionMD5 = getCursorString(_Cursor, "descriptionMD5");
		}
		_Cursor.close();
		return _descriptionMD5;
	}

	public void setHasRead(String pRssName, String pItemName) {
		ContentValues _RssItemIsRead = new ContentValues();
		_RssItemIsRead.put("isread", "1");

		Cursor _Cursor = mDatabase.rawQuery(
				"Select unread from rssfeed_list where rssname='" + pRssName
						+ "'", null);
		_Cursor.moveToNext();

		int unread = getCursorInt(_Cursor, "unread");
		if (unread > 0) {
			unread -= 1;
		}

		ContentValues _RssFeedListUnRead = new ContentValues();
		_RssFeedListUnRead.put("unread", unread);

		String _tablename = getTableName(pRssName);

		mDatabase.update(_tablename, _RssItemIsRead, "title=?",
				new String[] { pItemName });
		mDatabase.update("rssfeed_list", _RssFeedListUnRead, "rssname=?",
				new String[] { pRssName });
		_Cursor.close();
	}

	public boolean getIsRead(String pRssName, String pItemName) {
		String _tablename = getTableName(pRssName);
		Cursor _Cursor = mDatabase.rawQuery("Select isread from " + _tablename
				+ " where title='" + pItemName + "';", null);
		_Cursor.moveToNext();
		int isread = getCursorInt(_Cursor, "isread");
		_Cursor.close();
		if (isread == 1) {
			return true;
		} else {
			return false;
		}
	}

	public int getUnreadCount(String pRssName) {
		int _UnreadCount = 0;
		String _tablename = getTableName(pRssName);
		Cursor _Cursor = mDatabase.rawQuery("select count(*) from "
				+ _tablename + " where isread=0;", null);
		_Cursor.moveToNext();
		_UnreadCount = getCursorInt(_Cursor, "count(*)");
		return _UnreadCount;
	}

	public void setAllHasRead(String pRssName) {
		// TODO Auto-generated method stub
		String _tablename = getTableName(pRssName);
		mDatabase.execSQL("update " + _tablename
				+ " set isread=1 where isread=0;");
	}

	public void setStarred(String pRssName, String pItemName) {
		String _tablename = getTableName(pRssName);
		mDatabase.execSQL("update " + _tablename
				+ " set starred=1 where title='" + pItemName + "'");
	}
}
