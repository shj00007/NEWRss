package com.shj00007.rsshandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RSSHandlerReader {

	public String RSSFEEDOFCHOICE[] = { "http://www.cnbeta.com/backend.php",
			"http://feed.appinn.com/", "http://www.weiphone.com/rss.xml",
			"http://rss.cnn.com/rss/cnn_topstories.rss",
			"http://sports-ak.espn.go.com/espn/rss/news",
			"http://feed.feedsky.com/my1510" };
	private InputStream[] input = null;
	private List<ArrayList<RSSFeed>> rssFeedList = null;
	private ArrayList<RSSFeed> rssfeedarraylist1 = null;
	private ArrayList<RSSFeed> rssfeedarraylist2 = null;
	private ArrayList<RSSFeed> rssfeedarraylist3 = null;

	// public final String RSSFEEDOFCHOICE = "http://feed.appinn.com/";
	// public final String RSSFEEDOFCHOICE = "http://www.weiphone.com/rss.xml";
	// public final String RSSFEEDOFCHOICE =
	// "http://rss.cnn.com/rss/cnn_topstories.rss";
	// public final String RSSFEEDOFCHOICE =
	// "http://sports-ak.espn.go.com/espn/rss/news";
	// public final String RSSFEEDOFCHOICE = "http://feed.feedsky.com/my1510";

	public RSSHandlerReader(InputStream[] input) {
		this.input = input;
		
	}

	public final String tag = "RSSReader";
	public RSSFeed[] rssBean = null;

	private RSSFeed getFeed(InputStream urlToRssFeed) {
		try {
			// URL url = new URL(urlToRssFeed);

			// create the factory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// create a parser
			SAXParser parser = factory.newSAXParser();

			// create the reader (scanner)
			XMLReader xmlreader = parser.getXMLReader();
			// instantiate our handler
			RSSHandler theRssHandler = new RSSHandler();
			// assign our handler
			xmlreader.setContentHandler(theRssHandler);
			// get our data via the url class
			InputSource is = new InputSource(urlToRssFeed);

			// perform the synchronous parse
			xmlreader.parse(is);
			// get the results - should be a fully populated RSSFeed instance,
			// or null on error
			return theRssHandler.getFeed();
		} catch (Exception ee) {
			// if we have a problem, simply return null

			return null;
		}
	}

	public List<ArrayList<RSSFeed>> getRSSBean() {
		rssBean = new RSSFeed[input.length];
		rssFeedList = new ArrayList<ArrayList<RSSFeed>>();

		rssfeedarraylist1 = new ArrayList<RSSFeed>();
		rssfeedarraylist2 = new ArrayList<RSSFeed>();
		rssfeedarraylist3 = new ArrayList<RSSFeed>();
		for (int i = 0; i < rssBean.length; i++) {
			rssBean[i] = getFeed(input[i]);

			if (i <= 1) {
				rssBean[i].setCategory("info");
				rssfeedarraylist1.add(rssBean[i]);
			} else if (i > 1 && i <= 3) {
				rssBean[i].setCategory("news");
				rssfeedarraylist2.add(rssBean[i]);
			} else {
				rssBean[i].setCategory("sundry");
				rssfeedarraylist3.add(rssBean[i]);
			}
		}
		rssFeedList.add(rssfeedarraylist1);
		rssFeedList.add(rssfeedarraylist2);
		rssFeedList.add(rssfeedarraylist3);
		return rssFeedList;
	}
}
