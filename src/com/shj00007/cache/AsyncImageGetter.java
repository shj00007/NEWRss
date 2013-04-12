package com.shj00007.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.shj00007.cache.model.URLDrawable;
import com.shj00007.utility.DownFile;
import com.shj00007.utility.MD5Change;

public class AsyncImageGetter extends AsyncTask<String, Void, Drawable> {

	URLDrawable mURLDrawable = null;
	View mView = null;
	OnViewChangeListener mOnViewChangeListener = null;

	public AsyncImageGetter(URLDrawable pDrawable, View pView) {
		this.mURLDrawable = pDrawable;
		this.mView = pView;
	}

	public interface OnViewChangeListener {
		public void onInvalidateView();
	}

	public void setOnViewChangeListener(
			OnViewChangeListener pOnViewChangeListener) {
		this.mOnViewChangeListener = pOnViewChangeListener;
	}

	@Override
	protected void onPostExecute(Drawable result) {
		// TODO Auto-generated method stub
		if (result != null) {

			mURLDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(),
					0 + result.getIntrinsicHeight());
			mURLDrawable.drawable = result;
			mOnViewChangeListener.onInvalidateView();
			// DownFile.this.mRightText.setHeight((DownFile.this.mRightText.getHeight()
			// + result.getIntrinsicHeight()));
			// DownFile.this.mRightText.setEllipsize(null);

		} else {
			Log.i("test", "图片读取异常");
		}
	}

	@Override
	protected Drawable doInBackground(String... params) {
		// TODO Auto-generated method stub
		FileInputStream _FileInputStream = null;
		FileOutputStream _FileOutputStream = null;
		File _ImageFile = null;
		Drawable _URLDrawable = null;
		InputStream _InputStream = null;

		String source = params[0];

		String iamgeMD5 = MD5Change.getMD5(source);
		_ImageFile = new File(DownFile.IMAGE_FLODER_PATH + "/" + iamgeMD5);
		if (_ImageFile.exists()) {
			try {
				Log.i("test", "读取图片+" + DownFile.IMAGE_FLODER_PATH + "/"
						+ iamgeMD5);
				_InputStream = new FileInputStream(_ImageFile);
				_URLDrawable = Drawable.createFromStream(_InputStream, "src");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				_ImageFile.delete();
				e.printStackTrace();
				return null;
			} finally {
				if (_FileInputStream != null) {
					try {
						_FileInputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (_InputStream != null) {
					try {
						_InputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			try {
				Log.i("test", "file不存在");
				_ImageFile.createNewFile();
				_FileOutputStream = new FileOutputStream(_ImageFile);
				// DefaultHttpClient httpClient = new DefaultHttpClient();
				// HttpGet request = new HttpGet(source);
				// HttpResponse response = httpClient.execute(request);
				_InputStream = (InputStream) new URL(source).getContent();
				_URLDrawable = Drawable.createFromStream(_InputStream, "src");
				Bitmap bitmap = ((BitmapDrawable) _URLDrawable).getBitmap();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100,
						_FileOutputStream);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("test", "图片读取异常");
				_ImageFile.delete();
				e.printStackTrace();
				return null;
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

		}

		_URLDrawable.setBounds(0, 0, _URLDrawable.getIntrinsicWidth(),
				_URLDrawable.getIntrinsicHeight());

		return _URLDrawable;
	}

}
