package com.shj00007.utility.listviewfromnet;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 
 * @author Emil Sj��lander
 * 
 * 
 *         Copyright 2012 Emil Sj��lander
 * 
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 * 
 */
public class StickyListHeadersListView extends ListView implements
		OnScrollListener {

	private static final boolean DEBUG = false;
	private static final String TAG = "StickyListHeadersListView";
	private static final String HEADER_HEIGHT = "headerHeight";
	private static final String SUPER_INSTANCE_STATE = "superInstanceState";

	private OnScrollListener scrollListener;
	private boolean areHeadersSticky = true;
	private int headerBottomPosition;
	private int headerHeight = -1;
	private View header;
	private int dividerHeight;
	private Drawable divider;
	private boolean clippingToPadding;
	private boolean clipToPaddingHasBeenSet;
	private Long oldHeaderId = null;
	private boolean headerHasChanged = true;
	private boolean setupDone = false;
	private final Rect clippingRect = new Rect();

	private DataSetObserver dataSetChangedObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			reset();
		}

		@Override
		public void onInvalidated() {
			reset();
		}
	};

	public StickyListHeadersListView(Context context) {
		this(context, null);
	}

	public StickyListHeadersListView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.listViewStyle);
	}

	public StickyListHeadersListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		setupDone = true;
		super.setOnScrollListener(this);
		setDivider(getDivider());
		setDividerHeight(getDividerHeight());
		// null out divider, dividers are handled by adapter so they look good
		// with headers
		super.setDivider(null);
		super.setDividerHeight(0);
		setVerticalFadingEdgeEnabled(false);
	}

	private void reset() {
		headerBottomPosition = 0;
		headerHeight = -1;
		header = null;
		oldHeaderId = null;
		headerHasChanged = true;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		headerHeight = ((Bundle) state).getInt(HEADER_HEIGHT);
		headerHasChanged = true;
		super.onRestoreInstanceState(((Bundle) state)
				.getParcelable(SUPER_INSTANCE_STATE));
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Bundle instanceState = new Bundle();
		instanceState.putInt(HEADER_HEIGHT, headerHeight);
		instanceState.putParcelable(SUPER_INSTANCE_STATE,
				super.onSaveInstanceState());
		return instanceState;
	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		view = ((WrapperView) view).getItem();
		return super.performItemClick(view, position, id);
	}

	/**
	 * can only be set to false if headers are sticky, not compatible with
	 * fading edges
	 */
	@Override
	public void setVerticalFadingEdgeEnabled(boolean verticalFadingEdgeEnabled) {
		if (areHeadersSticky) {
			super.setVerticalFadingEdgeEnabled(false);
		} else {
			super.setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled);
		}
	}

	@Override
	public void setDivider(Drawable divider) {
		if (setupDone) {
			this.divider = divider;
			if (getAdapter() != null) {
				((StickyListHeadersAdapter) getAdapter()).setDivider(divider);
			}
		} else {
			super.setDivider(divider);
		}
	}

	@Override
	public void setDividerHeight(int height) {
		if (setupDone) {
			dividerHeight = height;
			if (getAdapter() != null) {
				((StickyListHeadersAdapter) getAdapter())
						.setDividerHeight(height);
			}
		} else {
			super.setDividerHeight(height);
		}
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		scrollListener = l;
	}

	public void setAreHeadersSticky(boolean areHeadersSticky) {
		if (areHeadersSticky) {
			super.setVerticalFadingEdgeEnabled(false);
		}
		this.areHeadersSticky = areHeadersSticky;
	}

	public boolean areHeadersSticky() {
		return areHeadersSticky;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (!clipToPaddingHasBeenSet) {
			clippingToPadding = true;
		}
		if (!(adapter instanceof StickyListHeadersAdapter))
			throw new IllegalArgumentException(
					"Adapter must be a subclass of StickyListHeadersAdapter");
		StickyListHeadersAdapter stickyAdapter = (StickyListHeadersAdapter) adapter;
		stickyAdapter.setDivider(divider);
		stickyAdapter.setDividerHeight(dividerHeight);
		reset();
		super.setAdapter(stickyAdapter);
		stickyAdapter.registerDataSetObserver(dataSetChangedObserver);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			scrollChanged(getFirstVisiblePosition());
		}
		super.dispatchDraw(canvas);
		if (header != null && areHeadersSticky) {
			if (headerHasChanged) {
				int widthMeasureSpec = MeasureSpec.makeMeasureSpec(getWidth(),
						MeasureSpec.EXACTLY);
				int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
						MeasureSpec.UNSPECIFIED);
				header.measure(widthMeasureSpec, heightMeasureSpec);
				header.layout(getLeft() + getPaddingLeft(), 0, getRight()
						- getPaddingRight(), headerHeight);
				headerHasChanged = false;
			}
			int top = headerBottomPosition - headerHeight;
			clippingRect.left = getPaddingLeft();
			clippingRect.right = getWidth() - getPaddingRight();
			clippingRect.bottom = top + headerHeight;
			if (clippingToPadding) {
				clippingRect.top = getPaddingTop();
			} else {
				clippingRect.top = 0;
			}

			canvas.save();
			canvas.clipRect(clippingRect);
			canvas.translate(getPaddingLeft(), top);
			header.draw(canvas);
			canvas.restore();
		}
	}

	@Override
	public void setClipToPadding(boolean clipToPadding) {
		super.setClipToPadding(clipToPadding);
		clippingToPadding = clipToPadding;
		clipToPaddingHasBeenSet = true;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (scrollListener != null) {
			scrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			scrollChanged(firstVisibleItem);
		}
	}

	private void scrollChanged(int firstVisibleItem) {
		if (getAdapter() == null || getAdapter().getCount() == 0)
			return;

		firstVisibleItem = getFixedFirstVisibleItem(firstVisibleItem);
		if (areHeadersSticky) {
			final int childCount = getChildCount();
			if (childCount != 0) {

				WrapperView viewToWatch = (WrapperView) super.getChildAt(0);
				for (int i = 1; i < childCount; i++) {
					WrapperView child = (WrapperView) super.getChildAt(i);

					int firstChildDistance;
					if (clippingToPadding) {
						firstChildDistance = Math
								.abs((viewToWatch.getTop() - getPaddingTop()));
					} else {
						firstChildDistance = Math.abs(viewToWatch.getTop());
					}

					int secondChildDistance;
					if (clippingToPadding) {
						secondChildDistance = Math
								.abs((child.getTop() - getPaddingTop())
										- headerHeight);
					} else {
						secondChildDistance = Math.abs(child.getTop()
								- headerHeight);
					}

					if (!viewToWatch.hasHeader()
							|| (child.hasHeader() && secondChildDistance < firstChildDistance)) {
						viewToWatch = child;
					}
				}

				if (viewToWatch.hasHeader()) {

					if (headerHeight < 0)
						headerHeight = viewToWatch.getHeader().getHeight();

					if (firstVisibleItem == 0
							&& super.getChildAt(0).getTop() > 0
							&& !clippingToPadding) {
						headerBottomPosition = 0;
					} else {
						if (clippingToPadding) {
							headerBottomPosition = Math.min(
									viewToWatch.getTop(), headerHeight
											+ getPaddingTop());
							headerBottomPosition = headerBottomPosition < getPaddingTop() ? headerHeight
									+ getPaddingTop()
									: headerBottomPosition;
						} else {
							headerBottomPosition = Math.min(
									viewToWatch.getTop(), headerHeight);
							headerBottomPosition = headerBottomPosition < 0 ? headerHeight
									: headerBottomPosition;
						}
					}
				} else {
					headerBottomPosition = headerHeight;
					if (clippingToPadding) {
						headerBottomPosition += getPaddingTop();
					}
				}
			}

			long currentHeaderId = ((StickyListHeadersAdapter) getAdapter())
					.getHeaderId(firstVisibleItem);

			if (oldHeaderId == null || oldHeaderId != currentHeaderId) {
				headerHasChanged = true;
				header = ((StickyListHeadersAdapter) getAdapter())
						.getHeaderView(firstVisibleItem, header);
				header.setLayoutParams(new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, headerHeight));
			}
			oldHeaderId = currentHeaderId;
			int top = clippingToPadding ? getPaddingTop() : 0;
			for (int i = 0; i < childCount; i++) {
				WrapperView child = (WrapperView) super.getChildAt(i);
				if (child.hasHeader()) {
					View childHeader = child.getHeader();
					if (child.getTop() < top) {
						childHeader.setVisibility(View.INVISIBLE);
					} else {
						childHeader.setVisibility(View.VISIBLE);
					}
				}
			}
		}
	}

	private int getFixedFirstVisibleItem(int firstVisibleItem) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

			for (int i = 0; i < getChildCount(); i++) {
				if (getChildAt(i).getBottom() >= 0) {
					firstVisibleItem += i;
					break;
				}
			}

			// work around to fix bug with firstVisibleItem being to high
			// because listview does not take clipToPadding=false into account
			if (!clippingToPadding && getPaddingTop() > 0) {
				if (super.getChildAt(0).getTop() > 0) {
					if (firstVisibleItem > 0) {
						firstVisibleItem -= 1;
					}
				}
			}
		}

		if (DEBUG)
			Log.d(TAG, String.valueOf(getChildAt(0).getBottom()));
		return firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == 2) {
			action_up = true;
		}

		if (scrollState == 0 && action_up) {
			this.scrollBy(x, -y);
		}
		if (scrollListener != null) {
			scrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void setSelectionFromTop(int position, int y) {
		if (areHeadersSticky && header != null)
			y = y + header.getHeight();
		super.setSelectionFromTop(position, y);
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		// TODO Auto-generated method stub
		// Log.i("test", "scrollx=" + scrollX + ",scrolly=" + scrollY);
		// Log.i("test", "clampedX=" + clampedX + ",clampedY=" + clampedY);
		// if(clampedY){
		// this.scrollBy(0, -3);
		// }else{
		// this.scrollBy(0, 2);
		// }
		y = scrollY;
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}

	private int delY;
	private boolean action_up;

	private int x;
	private int y;

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// TODO Auto-generated method stub
		Log.i("test", scrollY + "");
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX, 200, isTouchEvent);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		// Log.i("test", "onTouchEvent");
		// switch (ev.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// action_up = false;
		// break;
		// case MotionEvent.ACTION_MOVE:
		// delY = (int) ev.getY() / 2;
		// break;
		// case MotionEvent.ACTION_UP:
		// action_up = true;
		// break;
		// default:
		// break;
		// }
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			action_up = false;
			break;

		case MotionEvent.ACTION_UP:
			Log.i("test", "dispatchTouchEvent");
			action_up = true;
			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

}
