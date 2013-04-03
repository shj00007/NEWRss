package com.shj00007.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fragmenttest1.R;
import com.shj00007.bean.ModelRssItem;
import com.shj00007.business.BusinessRss;

public class MidListViewAdapter extends BaseAdapter {

	Context context = null;
	MidViewHolder viewHolder = null;
	private BusinessRss mBusinessRss = null;
	private ArrayList<ModelRssItem> mModelRssItems = null;
	private String mRssName = "";

	public MidListViewAdapter(Context context, BusinessRss pBusinessRss,
			String pRssName) {

		this.context = context;
		this.mBusinessRss = pBusinessRss;
		this.mRssName = pRssName;
		this.mModelRssItems = mBusinessRss.getModelRssItem(pRssName);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mModelRssItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub

		return mModelRssItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LinearLayout.inflate(context, R.layout.mid_listview,
					null);
			parent = (ViewGroup) LinearLayout.inflate(context,
					R.layout.mid_layout, null);
			viewHolder = new MidViewHolder();
			viewHolder.midname = (TextView) convertView
					.findViewById(R.id.midname);
			viewHolder.middate = (TextView) convertView
					.findViewById(R.id.middate);
			viewHolder.midtitle = (TextView) convertView
					.findViewById(R.id.midtitle);
			viewHolder.midtext = (TextView) convertView
					.findViewById(R.id.midtext);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (MidViewHolder) convertView.getTag();
		}
		viewHolder.midname.setText(mRssName);
		viewHolder.middate.setText(mModelRssItems.get(position).getPubdate());
		viewHolder.midtitle
				.setText(mModelRssItems.get(position).getNewstitle());

		if (mModelRssItems.get(position).isIsread()) {
			viewHolder.midtitle
					.setTextAppearance(context, R.style.styleHasRead);
		} else {
			viewHolder.midtitle.setTextAppearance(context, R.style.customstyle);
		}

		viewHolder.midtext.setText(mModelRssItems.get(position).getCategory());
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		this.mModelRssItems = mBusinessRss.getModelRssItem(mRssName);
		super.notifyDataSetChanged();
	}

	static class MidViewHolder {
		TextView midname = null;
		TextView middate = null;
		TextView midtitle = null;
		TextView midtext = null;
	}

}
