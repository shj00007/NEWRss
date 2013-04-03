package com.shj00007.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.shj00007.bean.ModelRssItem;
import com.shj00007.bean.ModelRssfeed;
import com.shj00007.database.DBHelper;
import com.shj00007.database.SQLiteRssItem;
import com.shj00007.database.SQLiteRssfeed;
import com.shj00007.rss.RssParser;
import com.shj00007.utility.DateTools;
import com.shj00007.utility.DownFile;
import com.shj00007.utility.MD5Change;

public class BusinessRss {
	private RssParser mParser = null;
	private SQLiteRssfeed mSQLiteRssfeed = null;
	private SQLiteRssItem mSQLiteRssItem = null;
	private ModelRssfeed mModelRssfeed = null;
	private ModelRssItem mModelRssItem = null;

	public BusinessRss(Context pContext) {

		mSQLiteRssfeed = new SQLiteRssfeed(pContext);
		mSQLiteRssItem = new SQLiteRssItem(pContext);

	}

	public void updateRss() {

		String _LocalLastNews = "";
		String _DownloadLatNews = "";
		int index = 0;

		_DownloadLatNews = mParser.getFeed().getItem().get(index).getTitle();
		_LocalLastNews = mSQLiteRssItem.getLastNews(mParser.getFeed()
				.getTitle());

		while (!_DownloadLatNews.equals(_LocalLastNews)
				&& index < mParser.getFeed().getItem().size() - 1) {
			index++;
			_DownloadLatNews = mParser.getFeed().getItem().get(index)
					.getTitle();
		}

		for (int i = index - 1; i >= 0; i--) {

			String pDescriptionMD5 = MD5Change.getMD5(mParser.getFeed()
					.getTitle()
					+ mParser.getFeed().getItem().get(i).getTitle()
					+ mParser.getFeed().getItem().get(i).getPubDate());
			DownFile.saveDescription(mParser.getFeed().getItem().get(i)
					.getDescription(), pDescriptionMD5);

			String _Date = DateTools.getDate(mParser.getFeed().getItem().get(i)
					.getPubDate());
			mSQLiteRssItem.addNews(mParser.getFeed().getTitle(), mParser
					.getFeed().getItem().get(i).getTitle(), _Date, mParser
					.getFeed().getItem().get(i).getCategory(), mParser
					.getFeed().getItem().get(i).getLink(), pDescriptionMD5);
		}
	}

	public void downloadRSS(String pUrl) {
		mParser = new RssParser(pUrl);
		mParser.parse();
	}

	public void addRssFeed(String pCategory) {

		mSQLiteRssfeed.addFeed(mParser.getFeed().getTitle(), pCategory, mParser
				.getFeed().getItem().size() - 1);
		mSQLiteRssItem.createTable(mParser.getFeed().getTitle());

	}

	public HashMap<String, ArrayList<ModelRssfeed>> getModelRssfeeds() {
		HashMap<String, ArrayList<ModelRssfeed>> list = new HashMap<String, ArrayList<ModelRssfeed>>();
		ArrayList<String> _RssCategoryList = getRssCategoryList();
		ArrayList<ModelRssfeed> _RssfeedList = mSQLiteRssfeed.getRssfeedList();
		int categorycount = _RssCategoryList.size();
		for (int i = 0; i < categorycount; i++) {
			ArrayList<ModelRssfeed> _RssCategoryfeeds = new ArrayList<ModelRssfeed>();

			int rssfeedcategorycount = _RssfeedList.size();
			for (int j = 0; j < rssfeedcategorycount; j++) {
				if (_RssCategoryList.get(i).equals(
						_RssfeedList.get(j).getCategory())) {
					_RssCategoryfeeds.add(_RssfeedList.get(j));
				}
			}
			list.put(_RssCategoryList.get(i), _RssCategoryfeeds);
		}

		return list;
	}

	public ArrayList<ModelRssItem> getModelRssItem(String pRssFeedName) {
		return mSQLiteRssItem.getRssfeedList(pRssFeedName);
	}

	public ArrayList<String> getRssCategoryList() {

		return mSQLiteRssfeed.getRssCategoryList();
	}

	public String getDescription(String pRssName, String pItemName) {
		String description = "";
		String _DescriptionMD5 = mSQLiteRssItem.getDescriptionMD5(pRssName,
				pItemName);
		FileInputStream _InputStream = null;
		try {
			_InputStream = new FileInputStream(DBHelper.TEXTFILE_PAT + "/"
					+ _DescriptionMD5);
			byte[] bytes = new byte[1024];
			int hasRead = 0;
			while ((hasRead = _InputStream.read(bytes)) > 0) {
				description += new String(bytes, 0, hasRead);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (_InputStream != null) {
				try {
					_InputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return description;
	}

	public void setHasRead(String pRssName, String pItemName) {
		mSQLiteRssItem.setHasRead(pRssName, pItemName);
	}

}
