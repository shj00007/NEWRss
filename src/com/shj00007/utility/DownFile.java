package com.shj00007.utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.TextView;

import com.shj00007.database.DBHelper;

public class DownFile implements ImageGetter {

	public final String IAMGE_FLODER_PATH = "/data/data/com.shj00007/pic";
	TextView rightText = null;
	String str = null;

	public DownFile(TextView prightText, String pStr) {
		rightText = prightText;
		this.str = pStr;
	}

	/**
	 * 下载网页上图片
	 * 
	 * @param httpUrl
	 * @param urlPath
	 */
	public static void getHtmlPicture(String httpUrl, String urlPath) {
		URL url;
		BufferedInputStream in;
		FileOutputStream file;
		try {
			// System.out.println("取网络图片");
			String fileName = urlPath.substring(urlPath.lastIndexOf("/"));
			String filePath = "/data/data/com.example.fragmenttest1/pic" // 设置图片下载的根目录
					+ urlPath.substring(0, urlPath.lastIndexOf("/"));
			System.out.println(filePath);
			url = new URL(httpUrl + urlPath);

			// 如果该目录不存在,则创建之
			File uploadFilePath = new File(filePath);
			if (uploadFilePath.exists() == false) {
				uploadFilePath.mkdirs();
			}

			in = new BufferedInputStream(url.openStream());
			file = new FileOutputStream(new File(filePath + fileName));

			int t;
			while ((t = in.read()) != -1) {
				file.write(t);
			}
			file.close();
			in.close();
			System.out.println("图片获取成功");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得网页html代码
	 * 
	 * @param httpUrl
	 * @return
	 * @throws IOException
	 */
	public static String getHtmlCode(String httpUrl) throws IOException {
		String content = "";
		URL uu = new URL(httpUrl); // 创建URL类对象
		BufferedReader ii = new BufferedReader(new InputStreamReader(
				uu.openStream())); // //使用openStream得到一输入流并由此构造一个BufferedReader对象
		String input;
		while ((input = ii.readLine()) != null) { // 建立读取循环，并判断是否有读取值
			content += input;
		}
		ii.close();
		return content;
	}

	/**
	 * 下载网页代码及图片并替换图片链接地址
	 * 
	 * @param url
	 * @throws IOException
	 */
	public static String get(String url) throws IOException {
		String searchImgReg = "(?x)(src|SRC|background|BACKGROUND)=('|\")(http://.*?/)(.*?.(jpg|JPG|png|PNG|gif|GIF))('|\")";

		// String content = getHtmlCode(url);

		String content = url;

		System.out.println(content);
		// 下载图片
		Pattern pattern = Pattern.compile(searchImgReg);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			getHtmlPicture(matcher.group(3), matcher.group(4));
		}
		// 修改图片链接地址
		pattern = Pattern.compile(searchImgReg);
		matcher = pattern.matcher(content);
		StringBuffer replaceStr = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(replaceStr, matcher.group(1)
					+ "='/web_upload/" + matcher.group(4) + "'");// 逐个动态替换图片链接地址
		}
		matcher.appendTail(replaceStr);// 添加尾部
		System.out.println(replaceStr.toString());
		return replaceStr.toString();
	}

	public static void saveDescription(String pText, String pFileName) {
		FileOutputStream _OutputStream = null;
		try {
			_OutputStream = new FileOutputStream(DBHelper.TEXTFILE_PAT + "/"
					+ pFileName);
			byte[] bbuf = new byte[32];
			_OutputStream.write(pText.getBytes());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != _OutputStream) {
				try {
					_OutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Drawable getDrawable(final String source) {
		InputStream is = null;

		Drawable d = null;
		final String iamgeMD5 = MD5Change.getMD5(source);
		File imageFolder = null;

		imageFolder = new File(IAMGE_FLODER_PATH);
		if (!imageFolder.isDirectory()) {
			imageFolder.mkdirs();
		}
		imageFolder = new File(IAMGE_FLODER_PATH + "/" + iamgeMD5);
		if (!imageFolder.exists()) {
			try {
				imageFolder.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					InputStream is = null;
					FileOutputStream _FileOutputStream = null;
					File _Image = null;
					try {

						is = (InputStream) new URL(source).getContent();
						_Image = new File(IAMGE_FLODER_PATH + "/" + iamgeMD5);
						_FileOutputStream = new FileOutputStream(_Image);
						byte[] buffer = new byte[1024];
						int hasread = 0;
						while ((hasread = is.read(buffer, 0, 1024)) > 0) {
							_FileOutputStream.write(buffer, 0, hasread);
						}
						_FileOutputStream.flush();
						Handler handler = new Handler(Looper.getMainLooper()) {
							public void handleMessage(Message msg) {

								rightText.setText(Html.fromHtml(str,
										new DownFile(rightText, str), null));

							};
						};
						handler.sendEmptyMessageDelayed(0, 500);

					} catch (Exception e) {
						_Image.delete();
						Log.i("test", "d=null");
						e.printStackTrace();
					} finally {
						try {

							if (_FileOutputStream != null) {

								_FileOutputStream.close();

							}
							if (is != null) {
								is.close();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();

			return null;
		} else {

			try {
				is = new FileInputStream(imageFolder);
				d = Drawable.createFromStream(is, "src");
				if (d == null) {
					return null;
				}
				d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
				return d;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}