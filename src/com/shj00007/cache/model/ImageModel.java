package com.shj00007.cache.model;

import android.graphics.Bitmap;

public class ImageModel {
	private String ImageName;
	private String ImageLink;
	private Bitmap ImageBitmap;

	public String getImageName() {
		return ImageName;
	}

	public void setImageName(String imageName) {
		ImageName = imageName;
	}

	public Bitmap getImageBitmap() {
		return ImageBitmap;
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		ImageBitmap = imageBitmap;
	}
}
