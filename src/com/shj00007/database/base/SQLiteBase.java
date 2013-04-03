package com.shj00007.database.base;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shj00007.database.DBHelper;

public class SQLiteBase {
	public DBHelper mDbHelper = null;
	public SQLiteDatabase mDatabase = null;
	
	public SQLiteBase(Context pContext) {
		mDbHelper = new DBHelper(pContext);
		mDatabase = mDbHelper.getWritableDatabase();
		
	}

	public String getCursorString(Cursor _Cursor, String value) {
		return _Cursor.getString(_Cursor.getColumnIndex(value));
	}

	public int getCursorInt(Cursor _Cursor, String value) {
		return _Cursor.getInt(_Cursor.getColumnIndex(value));
	}

	public boolean getBooleanResult(int zeroOrone) {
		if (zeroOrone == 0) {
			return false;
		} else {
			return true;
		}
	}

}
