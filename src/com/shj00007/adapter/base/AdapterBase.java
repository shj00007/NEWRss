package com.shj00007.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class AdapterBase extends BaseAdapter {

	private Context mContext = null;
	private LayoutInflater mLayoutInflater = null;

	public AdapterBase(Context pContext) {
		this.mContext = pContext;
		this.mLayoutInflater = LayoutInflater.from(mContext);
	}

	public LayoutInflater getLayoutInflater() {
		return mLayoutInflater;
	}

}
