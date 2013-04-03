package com.shj00007.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fragmenttest1.R;
import com.shj00007.bean.ModelRssfeed;
import com.shj00007.business.BusinessRss;

public class ExpandableAdapter extends BaseExpandableListAdapter implements
		OnScrollListener {

	private Context mContext = null;
	private ArrayList<String> mRssCategoryList = null;
	private GroupViewHolder mGroupViewHolder = null;
	private ChildViewHolder mChildViewHolder = null;
	private HashMap<String, ArrayList<ModelRssfeed>> mModelRssfeeds = null;
	private ArrayList<ModelRssfeed> mChildModelRssfeed = null;
	private LayoutInflater mLayoutInflater = null;
	private BusinessRss mBusinessRss = null;

	private ListView mMidListView = null;
	private MidListViewAdapter mMidListViewAdapter = null;

	public ExpandableAdapter(Context pContext, BusinessRss pBusinessRss,
			ListView pMidListView) {
		this.mContext = pContext;
		this.mBusinessRss = pBusinessRss;
		this.mRssCategoryList = pBusinessRss.getRssCategoryList();
		this.mModelRssfeeds = pBusinessRss.getModelRssfeeds();
		this.mLayoutInflater = LayoutInflater.from(mContext);

		this.mMidListView = pMidListView;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		ArrayList<ModelRssfeed> _ModelRssfeed = getModelRssfeed(arg0);
		return _ModelRssfeed.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		if (null == arg3) {
			arg3 = mLayoutInflater.inflate(R.layout.left_child_layout, null);
			mChildViewHolder = new ChildViewHolder();
			mChildViewHolder.tvleftchildtitle = (TextView) arg3
					.findViewById(R.id.tvleftchildtitle);
			mChildViewHolder.tvleftchildcount = (TextView) arg3
					.findViewById(R.id.tvleftchildcount);
			arg3.setTag(mChildViewHolder);
		} else {
			mChildViewHolder = (ChildViewHolder) arg3.getTag();
		}
		ArrayList<ModelRssfeed> _ModelRssfeed = getModelRssfeed(arg0);
		mChildViewHolder.tvleftchildtitle.setText(_ModelRssfeed.get(arg1)
				.getRssname());
		if (_ModelRssfeed.get(arg1).getUnreadcount() > 0) {

			mChildViewHolder.tvleftchildcount.setText(_ModelRssfeed.get(arg1)
					.getUnreadcount() + "");
		} else {
			mChildViewHolder.tvleftchildcount.setVisibility(View.GONE);
		}
		return arg3;
	}

	public ArrayList<ModelRssfeed> getModelRssfeed(int arg0) {
		String _Category = (String) getGroup(arg0);
		mChildModelRssfeed = mModelRssfeeds.get(_Category);
		return mChildModelRssfeed;
	}

	@Override
	public int getChildrenCount(int arg0) {
		String _Category = (String) getGroup(arg0);
		mChildModelRssfeed = mModelRssfeeds.get(_Category);
		return mChildModelRssfeed.size();
	}

	@Override
	public Object getGroup(int arg0) {
		return mRssCategoryList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return mRssCategoryList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		if (null == arg2) {
			arg2 = mLayoutInflater.inflate(R.layout.left_group_layout, null);
			mGroupViewHolder = new GroupViewHolder();
			mGroupViewHolder.tvrss_category = (TextView) arg2
					.findViewById(R.id.tvrss_category);
			arg2.setTag(mGroupViewHolder);
		} else {
			mGroupViewHolder = (GroupViewHolder) arg2.getTag();
		}
		mGroupViewHolder.tvrss_category.setText(mRssCategoryList.get(arg0));
		return arg2;
	}

	static class GroupViewHolder {
		private TextView tvrss_category = null;
		private TextView righttext = null;
		private ImageView arrow = null;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		this.mRssCategoryList = mBusinessRss.getRssCategoryList();
		this.mModelRssfeeds = mBusinessRss.getModelRssfeeds();
		super.notifyDataSetChanged();
	}

	static class ChildViewHolder {
		private TextView tvleftchildtitle = null;
		private TextView tvleftchildcount = null;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<ModelRssfeed> _ModelRssfeed = getModelRssfeed(groupPosition);
		String _RssName = _ModelRssfeed.get(childPosition).getRssname();

		mMidListViewAdapter = new MidListViewAdapter(mContext, mBusinessRss,
				_RssName);
		mMidListView.setAdapter(mMidListViewAdapter);

		return false;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	/*
	 * @Override public boolean hasStableIds() { // TODO Auto-generated method
	 * stub return false; }
	 * 
	 * @Override public boolean isChildSelectable(int groupPosition, int
	 * childPosition) { // TODO Auto-generated method stub return false; }
	 * 
	 * @Override public void onScroll(AbsListView view, int firstVisibleItem,
	 * int visibleItemCount, int totalItemCount) { // TODO Auto-generated method
	 * stub }
	 * 
	 * @Override public void onScrollStateChanged(AbsListView view, int
	 * scrollState) { // TODO Auto-generated method stub
	 * 
	 * }
	 */

}
