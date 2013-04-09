package com.shj00007.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class RssParser extends DefaultHandler {
	private String urlString;
	private RssFeed rssFeed;
	private StringBuilder text;
	private Item item;
	private boolean imgStatus;

	public RssParser(String url) {
		this.urlString = url;
		this.text = new StringBuilder();
	}

	public RssParser() {
		this.text = new StringBuilder();
	}

	public boolean parse() {
		InputStream urlInputStream = null;
		SAXParserFactory spf = null;
		SAXParser sp = null;

		try {
			URL url = new URL(this.urlString);
			// _setProxy(); // Set the proxy if needed
			urlInputStream = url.openConnection().getInputStream();
			spf = SAXParserFactory.newInstance();
			if (spf != null) {
				sp = spf.newSAXParser();
				sp.parse(urlInputStream, this);
			}
			return true;
		}
		/*
		 * Exceptions need to be handled MalformedURLException
		 * ParserConfigurationException IOException SAXException
		 */

		catch (Exception e) {
			System.out.println("Exception: " + e);
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (urlInputStream != null)
					urlInputStream.close();
			} catch (Exception e) {
			}
		}
	}

	public RssFeed getFeed() {
		return (this.rssFeed);
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {
		if (qName.equalsIgnoreCase("channel"))
			this.rssFeed = new RssFeed();
		else if (qName.equalsIgnoreCase("item") && (this.rssFeed != null)) {
			this.item = new Item();
			this.rssFeed.addItem(this.item);
		} else if (qName.equalsIgnoreCase("image") && (this.rssFeed != null))
			this.imgStatus = true;
	}

	public void endElement(String uri, String localName, String qName) {
		if (this.rssFeed == null)
			return;

		if (qName.equalsIgnoreCase("item"))
			this.item = null;

		else if (qName.equalsIgnoreCase("image"))
			this.imgStatus = false;

		else if (qName.equalsIgnoreCase("title")) {
			if (this.item != null)
				this.item.title = this.text.toString().trim();
			else if (this.imgStatus)
				this.rssFeed.imageTitle = this.text.toString().trim();
			else
				this.rssFeed.title = this.text.toString().trim();
		}

		else if (qName.equalsIgnoreCase("link")) {
			if (this.item != null)
				this.item.link = this.text.toString().trim();
			else if (this.imgStatus)
				this.rssFeed.imageLink = this.text.toString().trim();
			else
				this.rssFeed.link = this.text.toString().trim();
		}

		else if (qName.equalsIgnoreCase("description")) {
			if (this.item != null)
				this.item.description = this.text.toString().trim();
			else
				this.rssFeed.description = this.text.toString().trim();
		}

		else if (qName.equalsIgnoreCase("url") && this.imgStatus)
			this.rssFeed.imageUrl = this.text.toString().trim();

		else if (qName.equalsIgnoreCase("language"))
			this.rssFeed.language = this.text.toString().trim();

		else if (qName.equalsIgnoreCase("generator"))
			this.rssFeed.generator = this.text.toString().trim();

		else if (qName.equalsIgnoreCase("copyright"))
			this.rssFeed.copyright = this.text.toString().trim();

		else if (qName.equalsIgnoreCase("pubDate") && (this.item != null))
			this.item.pubDate = this.text.toString().trim();

		else if (qName.equalsIgnoreCase("category") && (this.item != null)) {
			this.rssFeed.addItem(this.text.toString().trim(), this.item);
			this.item.category = this.text.toString().trim();
		}
		this.text.setLength(0);
	}

	public void characters(char[] ch, int start, int length) {
		this.text.append(ch, start, length);
	}

	public static void _setProxy() throws IOException {
		Properties sysProperties = System.getProperties();
		sysProperties.put("proxyHost", "<Proxy IP Address>");
		sysProperties.put("proxyPort", "<Proxy Port Number>");
		System.setProperties(sysProperties);
	}

	public static class RssFeed {
		private String title;
		private String description;
		private String link;
		private String language;
		private String generator;
		private String copyright;
		private String imageUrl;
		private String imageTitle;
		private String imageLink;

		private ArrayList<Item> items;
		private HashMap<String, ArrayList<Item>> category;

		public void addItem(Item item) {
			if (this.items == null)
				this.items = new ArrayList<Item>();
			this.items.add(item);
		}

		public void addItem(String category, Item item) {
			if (this.category == null)
				this.category = new HashMap<String, ArrayList<Item>>();
			if (!this.category.containsKey(category))
				this.category.put(category, new ArrayList<Item>());
			this.category.get(category).add(item);
		}

		public ArrayList<Item> getItem() {
			if (null == items) {
				return null;
			} else {
				return items;
			}
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public String getGenerator() {
			return generator;
		}

		public void setGenerator(String generator) {
			this.generator = generator;
		}

		public String getCopyright() {
			return copyright;
		}

		public void setCopyright(String copyright) {
			this.copyright = copyright;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getImageTitle() {
			return imageTitle;
		}

		public void setImageTitle(String imageTitle) {
			this.imageTitle = imageTitle;
		}

		public String getImageLink() {
			return imageLink;
		}

		public void setImageLink(String imageLink) {
			this.imageLink = imageLink;
		}
	}

	public static class Item {
		private String title;
		private String description;
		private String link;
		private String pubDate;
		private String category;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public String getPubDate() {
			return pubDate;
		}

		public void setPubDate(String pubDate) {
			this.pubDate = pubDate;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

	}

}
