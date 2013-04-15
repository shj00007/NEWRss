package com.shj00007.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shj00007.database.base.SQLiteBase;

public class SQLiteRssStarr extends SQLiteBase {

	public SQLiteRssStarr(Context pContext,SQLiteDatabase pDatabase) {
		super(pContext,pDatabase);
		// TODO Auto-generated constructor stub
	}

	public void addStarred(String pRssName, String pItemName, String pPubdate) {
		mDatabase
				.execSQL("INSERT INTO starred_list(rssname,title,pubdate) values('"
						+ pRssName
						+ "','"
						+ pItemName
						+ "','"
						+ pPubdate
						+ "')");
	}

	public Cursor getStarredCursor() {
		// TODO Auto-generated method stub
		Cursor _Cursor = mDatabase.rawQuery("select * from starred_list", null);
		return _Cursor;
	}

	public boolean isItemStarred(String pItemName) {

		Cursor _Cursor = mDatabase.rawQuery(
				"select * from starred_list where title='" + pItemName + "'",
				null);
		if (_Cursor.moveToNext()) {
			_Cursor.close();
			return true;
		} else {
			_Cursor.close();
			return false;
		}
	}

	public void deleteItemStarred(String pItemName) {
		mDatabase.execSQL("delete from starred_list where title='" + pItemName
				+ "'");
	}
}
