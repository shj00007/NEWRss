package com.shj00007.utility;

import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shj00007.cache.AsyncImageGetter;
import com.shj00007.cache.AsyncImageGetter.OnViewChangeListener;
import com.shj00007.cache.model.URLDrawable;
import com.shj00007.database.DBHelper;

public class DownFile implements ImageGetter {

	public interface OnImageChangeListener {
		public void onInvalidateView(View v);

	}

	public final static String IMAGE_FLODER_PATH = "/data/data/com.shj00007/pic";
	TextView mRightText;

	public DownFile(TextView pRightText) {
		mRightText = pRightText;
	}

	@Override
	public Drawable getDrawable(String source) {
		// TODO Auto-generated method stub
		URLDrawable _URLDrawable = new URLDrawable();
		AsyncImageGetter mAsyncTask = new AsyncImageGetter();
		mAsyncTask.setOnViewChangeListener(_URLDrawable,
				new OnViewChangeListener() {

					@Override
					public void onInvalidateView(Drawable pDrawable) {
						
						mRightText.setText(DownFile.this.mRightText.getText());
					}
				});
		mAsyncTask.execute(source);

		return _URLDrawable;
	}

	public static void saveDescription(String pText, String pFileName) {
		FileOutputStream _OutputStream = null;
		try {
			_OutputStream = new FileOutputStream(DBHelper.TEXTFILE_PAT + "/"
					+ pFileName);
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

}