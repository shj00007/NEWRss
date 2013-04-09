package com.shj00007.database;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "rssinfo.db";

	public final static String IMAGEFILE_PATH = "/data/data/com.shj00007/image";
	public final static String TEXTFILE_PAT = "/data/data/com.shj00007/text";
	private File mFile = null;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS rssfeed_list(_id INTEGER PRIMARY KEY AUTOINCREMENT, rssname VARCHAR, category VARCHAR, unread INTEGER,feedlink INTEGER)");
		db.execSQL("CREATE TABLE IF NOT EXISTS starred_list(_id INTEGER PRIMARY KEY AUTOINCREMENT, rssname VARCHAR, title VARCHAR,pubdate DATETIME)");
		
		mFile = new File(IMAGEFILE_PATH);
		mFile.mkdirs();
		mFile = new File(TEXTFILE_PAT);
		mFile.mkdirs();

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
