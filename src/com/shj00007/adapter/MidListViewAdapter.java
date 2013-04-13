package com.shj00007.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shj00007.R;
import com.shj00007.bean.ModelRssItem;
import com.shj00007.business.BusinessRss;
import com.shj00007.utility.listviewfromnet.StickyListHeadersBaseAdapter;

public class MidListViewAdapter extends StickyListHeadersBaseAdapter  {

	Context context = null;
	MidViewHolder viewHolder = null;
	private BusinessRss mBusinessRss = null;
	private ArrayList<ModelRssItem> mModelRssItems = null;
	private String mRssName = "";
	private boolean mOnlyViewUnRead = false;
	
	public MidListViewAdapter(Context context, BusinessRss pBusinessRss,
			String pRssName) {
		super(context);
		this.context = context;
		this.mBusinessRss = pBusinessRss;
		this.mRssName = pRssName;
		this.mModelRssItems = mBusinessRss.getModelRssItem(pRssName,
				mOnlyViewUnRead);
	}

	public boolean getOnlyViewUnRead() {
		return this.mOnlyViewUnRead;
	}

	public void setOnlyViewUnRead(boolean pOnlyViewUnRead) {
		this.mOnlyViewUnRead = pOnlyViewUnRead;
	}

	public String getRssName() {
		return mRssName;
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

	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		this.mModelRssItems = mBusinessRss.getModelRssItem(mRssName,
				mOnlyViewUnRead);
		super.notifyDataSetChanged();
	}

	static class MidViewHolder {
		TextView midname = null;
		TextView middate = null;
		TextView midtitle = null;
		TextView midtext = null;
	}

	@Override
	public View getHeaderView(int position, View convertView) {
		// TODO Auto-generated method stub
		HeaderViewHolder holder;

		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = LinearLayout.inflate(context, R.layout.header, null);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		// set header text as first char in name
		holder.text.setText(mModelRssItems.get(position).getPubdate());

		return convertView;
	}

	class HeaderViewHolder {
		TextView text;
	}

	@Override
	public long getHeaderId(int position) {
		// TODO Auto-generated method stub
		return mModelRssItems.get(position).getPubdate().subSequence(0, 13)
				.charAt(12);
	}

	@Override
	protected View getView(int position, View convertView) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LinearLayout.inflate(context, R.layout.mid_listview,
					null);
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

}
