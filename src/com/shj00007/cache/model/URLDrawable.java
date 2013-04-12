package com.shj00007.cache.model;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class URLDrawable extends BitmapDrawable {

	public Drawable drawable;

	@Override
	public void draw(Canvas canvas) {
		
		if (drawable != null) {
			drawable.draw(canvas);
		}
	}
}
