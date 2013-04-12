package com.shj00007.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.shj00007.cache.model.ImageModel;
import com.shj00007.utility.DownFile;
import com.shj00007.utility.MD5Change;

public class ImageCache {

	static private ImageCache cache;
	private Hashtable<String, ImageModelRef> imageModelRefs = null;
	private ReferenceQueue<ImageModel> q = null;

	private class ImageModelRef extends SoftReference<ImageModel> {

		private String _key = "";

		public ImageModelRef(ImageModel r, ReferenceQueue<ImageModel> q) {
			super(r, q);
			// TODO Auto-generated constructor stub
			_key = r.getImageName();
		}

	}

	private ImageCache() {
		imageModelRefs = new Hashtable<String, ImageModelRef>();
		q = new ReferenceQueue<ImageModel>();
	}

	public static ImageCache getInstance() {
		if (cache == null) {
			cache = new ImageCache();
		}
		return cache;
	}

	public Bitmap cacheImageModel(String pLink) {
		cleanCache();
		File _ImageFile = null;

		Bitmap _image = null;
		ImageModel _ImageModel = null;

		String iamgeMD5 = MD5Change.getMD5(pLink);
		_ImageFile = new File(DownFile.IMAGE_FLODER_PATH + "/" + iamgeMD5);

		if (_ImageFile.exists()) {
			_image = this.loadimageFromDisk(iamgeMD5, _ImageFile);
		} else {
			_image = this.loadImageFromNet(pLink, _ImageFile);
		}

		_ImageModel = new ImageModel(iamgeMD5, pLink, _image);

		ImageModelRef ref = new ImageModelRef(_ImageModel, q);
		imageModelRefs.put(_ImageModel.getImageName(), ref);

		return _image;

	}

	public Bitmap loadImageFromNet(String pLink, File _ImageFile) {
		InputStream _InputStream = null;
		Bitmap _image = null;
		FileOutputStream _FileOutputStream = null;
		try {
			_ImageFile.createNewFile();
			_FileOutputStream = new FileOutputStream(_ImageFile);
			// DefaultHttpClient httpClient = new DefaultHttpClient();
			// HttpGet request = new HttpGet(source);
			// HttpResponse response = httpClient.execute(request);
			_InputStream = (InputStream) new URL(pLink).getContent();
			_image = BitmapFactory.decodeStream(_InputStream);
			_image.compress(Bitmap.CompressFormat.PNG, 100, _FileOutputStream);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			_ImageFile.delete();
			e.printStackTrace();
		} finally {
			try {
				if (_FileOutputStream != null) {
					_FileOutputStream.close();
				}
				if (_InputStream != null) {
					_InputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return _image;
	}

	public Bitmap loadimageFromDisk(String pImageName, File _ImageFile) {
		FileInputStream _InputStream = null;
		Bitmap _image = null;
		try {
			_InputStream = new FileInputStream(_ImageFile);
			_image = BitmapFactory.decodeStream(_InputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			_ImageFile.delete();
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
		return _image;
	}

	public Bitmap getImage(String pLink) {
		Bitmap _image = null;
		String _key = MD5Change.getMD5(pLink);
		if (imageModelRefs.contains(_key)) {
			_image = imageModelRefs.get(_key).get().getImageBitmap();
		} else {
			_image = this.cacheImageModel(pLink);
		}

		return _image;
	}

	private void cleanCache() {
		// TODO Auto-generated method stub
		ImageModelRef _ImageModelRef = null;
		while ((_ImageModelRef = (ImageModelRef) q.poll()) != null) {
			imageModelRefs.remove(_ImageModelRef._key);
		}
	}

	public void clearCache() {
		cleanCache();
		imageModelRefs.clear();
		System.gc();
		System.runFinalization();
	}

}
