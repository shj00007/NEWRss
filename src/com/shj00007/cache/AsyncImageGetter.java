package com.shj00007.cache;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.shj00007.cache.model.URLDrawable;

public class AsyncImageGetter extends AsyncTask<String, Void, Drawable> {

	URLDrawable mURLDrawable = null;
	OnViewChangeListener mOnViewChangeListener = null;

	public interface OnViewChangeListener {
		public void onInvalidateView(Drawable pDrawable);
	}

	public void setOnViewChangeListener(URLDrawable pUrlDrawable,
			OnViewChangeListener pOnViewChangeListener) {
		this.mURLDrawable = pUrlDrawable;
		this.mOnViewChangeListener = pOnViewChangeListener;
	}

	@Override
	protected void onPostExecute(Drawable result) {
		// TODO Auto-generated method stub
		if (result != null) {

			mURLDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(),
					0 + result.getIntrinsicHeight());
			mURLDrawable.drawable = result;
			mOnViewChangeListener.onInvalidateView(result);

		}
	}

	@Override
	protected Drawable doInBackground(String... params) {
		// TODO Auto-generated method stub
		Drawable _URLDrawable = null;

		String source = params[0];

		_URLDrawable = new BitmapDrawable(ImageCache.getInstance().getImage(
				source));

		_URLDrawable.setBounds(0, 0, _URLDrawable.getIntrinsicWidth(),
				_URLDrawable.getIntrinsicHeight());

		return _URLDrawable;
	}

}
