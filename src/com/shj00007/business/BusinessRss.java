package com.shj00007.business;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

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

	public BusinessRss(Context pContext) {

		mSQLiteRssfeed = new SQLiteRssfeed(pContext);
		mSQLiteRssItem = new SQLiteRssItem(pContext);

	}

	public void updateRss() {

		String _LocalLastNews = "";
		String _DownloadLastNews = "";
		int index = 0;

		_DownloadLastNews = mParser.getFeed().getItem().get(index).getTitle();
		_LocalLastNews = mSQLiteRssItem.getLastNews(mParser.getFeed()
				.getTitle());

		while (!_DownloadLastNews.equals(_LocalLastNews)
				&& index < mParser.getFeed().getItem().size() - 1) {
			index++;
			_DownloadLastNews = mParser.getFeed().getItem().get(index)
					.getTitle();
		}
		String _RssName = mParser.getFeed().getTitle();
		for (int i = index - 1; i >= 0; i--) {

			String pDescriptionMD5 = MD5Change.getMD5(_RssName
					+ mParser.getFeed().getItem().get(i).getTitle()
					+ mParser.getFeed().getItem().get(i).getPubDate());
			DownFile.saveDescription(mParser.getFeed().getItem().get(i)
					.getDescription(), pDescriptionMD5);

			String _Date = DateTools.getDate(mParser.getFeed().getItem().get(i)
					.getPubDate());
			mSQLiteRssItem.addNews(_RssName, mParser.getFeed().getItem().get(i)
					.getTitle(), _Date, mParser.getFeed().getItem().get(i)
					.getCategory(), mParser.getFeed().getItem().get(i)
					.getLink(), pDescriptionMD5);
		}
		mSQLiteRssfeed.updateRssFeedCount(_RssName,
				mSQLiteRssItem.getUnreadCount(_RssName));

	}

	public void downloadRSS(String pUrl) {
		mParser = new RssParser(pUrl);
		mParser.parse();
	}

	public void addRssFeed(String pCategory, String pFeedLink) {

		mSQLiteRssfeed.addFeed(mParser.getFeed().getTitle(), pCategory, mParser
				.getFeed().getItem().size() - 1, pFeedLink);
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

	public ArrayList<ModelRssItem> getModelRssItem(String pRssFeedName,
			boolean pOnlyViewUnread) {
		return mSQLiteRssItem.getRssItemList(pRssFeedName, pOnlyViewUnread);
	}

	public ArrayList<String> getRssCategoryList() {

		return mSQLiteRssfeed.getRssCategoryList();
	}

	public String getDescription(String pRssName, String pItemName) {
		String description = "";
		String _DescriptionMD5 = mSQLiteRssItem.getDescriptionMD5(pRssName,
				pItemName);
		FileReader _FileReader = null;
		try {
			_FileReader = new FileReader(DBHelper.TEXTFILE_PAT + "/"
					+ _DescriptionMD5);
			char[] buffer = new char[1024];
			int hasRead = 0;
			while ((hasRead = _FileReader.read(buffer)) != -1) {
				description += new String(buffer, 0, hasRead);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (_FileReader != null) {
				try {
					_FileReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return description;
	}

	public boolean isRead(String pRssName, String pItemName) {
		return mSQLiteRssItem.getIsRead(pRssName, pItemName);
	}

	public void setHasRead(String pRssName, String pItemName) {
		mSQLiteRssItem.setHasRead(pRssName, pItemName);
	}

	public void setRssIsread(String pRssName) {
		mSQLiteRssfeed.setRssHasRead(pRssName);
		mSQLiteRssItem.setAllHasRead(pRssName);
	}

	public boolean isRssFeedExist(String pLink) {
		return mSQLiteRssfeed.isRssExist(pLink);
	}
}
