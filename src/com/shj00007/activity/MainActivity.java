package com.shj00007.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.shj00007.R;
import com.shj00007.adapter.ExpandableAdapter;
import com.shj00007.adapter.MidListViewAdapter;
import com.shj00007.business.BusinessRss;
import com.shj00007.cache.AsyncImageGetter;
import com.shj00007.utility.DownFile;

public class MainActivity extends Activity implements OnGestureListener,
		OnTouchListener {
	AsyncImageGetter mAsyncImageGetter = null;
	private LinearLayout layout = null;
	private LinearLayout leftView = null;
	private LinearLayout midView = null;
	private LinearLayout rightView = null;
	private LinearLayout.LayoutParams params = null;
	private LayoutParams homeparams = null;
	private boolean ishomeopen = true;

	private GestureDetector gestureDetector = null;
	private Animation open_layout_anim = null;
	private Animation close_layout_anim = null;

	// new
	private BusinessRss mBusinessRss = null;
	private ExpandableListView mExpandableListView = null;
	private ExpandableAdapter mExpandableAdapter = null;

	private SimpleCursorAdapter mSimpleCursorAdapter = null;

	private ProgressDialog m_ProgressDialog;

	private ListView mMidListView = null;
	private ImageView mMidUnreadImage = null;
	private ImageView mRightUnreadImage = null;
	private TextView tvrighttext = null;
	private ScrollView svrightscroll = null;

	private ListView mMid_starr_listview = null;

	private ImageView mSetAllRead = null;
	private ImageView mUnread = null;
	private ImageView mAddFeed = null;
	private ImageView mViewStarr = null;
	private ImageView mSetStarr = null;
	private ImageView mUpdateRss = null;

	// private EditText etaddfeedname = null;
	// private AutoCompleteTextView etaddcategoryname = null;

	private Cursor _Cursor = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setLayoutSize();

		initTools();

		initView();

		setListener();

		bindData();

	}

	// new
	public void initTools() {
		mBusinessRss = new BusinessRss(this);
		mAsyncImageGetter = new AsyncImageGetter();
	}

	public void initView() {

		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListleft);
		mMidListView = (ListView) findViewById(R.id.mid_listview);
		tvrighttext = (TextView) findViewById(R.id.tvright_text_up);
		svrightscroll = (ScrollView) findViewById(R.id.svrightscrool);
		mMidUnreadImage = (ImageView) findViewById(R.id.ivmidunreadimage);
		mRightUnreadImage = (ImageView) findViewById(R.id.ivrightunreadimage);
		mSetAllRead = (ImageView) findViewById(R.id.ivsetallread);
		mUnread = (ImageView) findViewById(R.id.ivonlyunread);
		mAddFeed = (ImageView) findViewById(R.id.ivaddfeed);
		mViewStarr = (ImageView) findViewById(R.id.ivviewstarred);
		mSetStarr = (ImageView) findViewById(R.id.ivsetstarr);
		mMid_starr_listview = (ListView) findViewById(R.id.mid_starr_listview);
		mUpdateRss = (ImageView) findViewById(R.id.ivupdate);
		// etaddfeedname = (EditText) findViewById(R.id.etaddfeedname);
		// etaddcategoryname = (AutoCompleteTextView)
		// findViewById(R.id.etaddcategoryname);
	}

	public void setListener() {
		// mExpandableListView.setOnTouchListener(this);
		mMidListView.setOnTouchListener(this);
		tvrighttext.setOnTouchListener(this);
		svrightscroll.setOnTouchListener(this);
		mMid_starr_listview.setOnTouchListener(this);

		mSetStarr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "请选中Item", Toast.LENGTH_SHORT)
						.show();
			}
		});

		mExpandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				mMid_starr_listview.setVisibility(View.GONE);
				return false;
			}
		});

		mAddFeed.setOnClickListener(new OnClickListener() {
			private EditText etaddfeedname = null;
			private AutoCompleteTextView etaddcategoryname = null;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View _v = getLayoutInflater().inflate(R.layout.addfeeddialog,
						null);
				ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
						android.R.layout.simple_dropdown_item_1line,
						getResources().getStringArray(R.array.category));
				etaddfeedname = (EditText) _v.findViewById(R.id.etaddfeedname);
				etaddcategoryname = (AutoCompleteTextView) _v
						.findViewById(R.id.etaddcategory);
				etaddcategoryname.setAdapter(adapter);

				new AlertDialog.Builder(MainActivity.this)
						.setTitle("请输入")
						.setView(_v)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method
										// stub
										InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

										final String link = etaddfeedname
												.getText().toString();
										if (etaddfeedname.getText().toString()
												.equals("")) {

											imm.hideSoftInputFromWindow(
													etaddcategoryname
															.getWindowToken(),
													0);
											Toast.makeText(MainActivity.this,
													"请输入地址", Toast.LENGTH_SHORT)
													.show();
											return;
										}
										if (etaddcategoryname.getText()
												.toString().equals("")) {
											imm.hideSoftInputFromWindow(
													etaddcategoryname
															.getWindowToken(),
													0);
											Toast.makeText(MainActivity.this,
													"请输入类别", Toast.LENGTH_SHORT)
													.show();
											return;
										}
										final String category = etaddcategoryname
												.getText().toString();

										if (mBusinessRss.isRssFeedExist(link)) {
											Toast.makeText(MainActivity.this,
													"rss已存在",
													Toast.LENGTH_SHORT).show();
											return;
										}

										ShowProgressDialog("正在加载rss", "loading");

										new Thread(new Runnable() {

											@Override
											public void run() {

												if (mBusinessRss
														.downloadRSS(link)) {
													mBusinessRss.addRssFeed(
															category, link);
													mBusinessRss.updateRss();
													Handler myhandler = new Handler(
															Looper.getMainLooper()) {

														@Override
														public void handleMessage(
																Message msg) {
															bindData();
															DismissProgressDialog();
														}
													};
													myhandler.removeMessages(0);
													myhandler
															.sendEmptyMessage(0);
												} else {
													Handler handler = new Handler(
															Looper.getMainLooper()) {

														@Override
														public void handleMessage(
																Message msg) {
															DismissProgressDialog();
															Toast.makeText(
																	MainActivity.this,
																	"网络异常，请检查你的feed地址是否正确",
																	Toast.LENGTH_SHORT)
																	.show();
														}
													};
													handler.removeMessages(0);
													handler.sendEmptyMessage(0);

												}

											}
										}).start();

									}
								}).setNegativeButton("取消", null).show();

			}

		});

		mSetAllRead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String _RssName = "";
				try {
					_RssName = ((MidListViewAdapter) mMidListView.getAdapter())
							.getRssName();
				} catch (Exception e) {
					_RssName = null;
				}
				if (_RssName != null) {
					mBusinessRss.setRssIsread(_RssName);
					mExpandableAdapter.notifyDataSetChanged();
					((MidListViewAdapter) mMidListView.getAdapter())
							.notifyDataSetChanged();
				} else {
					Toast.makeText(MainActivity.this, "请先选中rss",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		mUnread.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mMidListView.getAdapter() == null) {
					Toast.makeText(MainActivity.this, "请选择rss",
							Toast.LENGTH_SHORT).show();
					return;
				}
				mMid_starr_listview.setVisibility(View.GONE);
				mMidListView.setVisibility(View.VISIBLE);
				MidListViewAdapter _Adapter = (MidListViewAdapter) mMidListView
						.getAdapter();
				if (_Adapter.getOnlyViewUnRead()) {
					_Adapter.setOnlyViewUnRead(false);
					Toast.makeText(MainActivity.this, "显示所有条目",
							Toast.LENGTH_SHORT).show();
				} else {
					_Adapter.setOnlyViewUnRead(true);
					Toast.makeText(MainActivity.this, "只显示未读条目",
							Toast.LENGTH_SHORT).show();
				}
				_Adapter.notifyDataSetChanged();
			}
		});

		mViewStarr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_Cursor = mBusinessRss.getStarredCursor();

				mSimpleCursorAdapter = new SimpleCursorAdapter(
						MainActivity.this,
						R.layout.mid_listview,
						_Cursor,
						new String[] { "rssname", "title", "pubdate" },
						new int[] { R.id.midname, R.id.midtitle, R.id.middate },
						CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
				mMid_starr_listview.setAdapter(mSimpleCursorAdapter);
				mMidUnreadImage.setVisibility(View.GONE);
				mMidListView.setVisibility(View.GONE);
				mMid_starr_listview.setVisibility(View.VISIBLE);
			}
		});

		mMid_starr_listview
				.setOnItemClickListener(new OnItemClickListenerImpl());
		mMidListView.setOnItemClickListener(new OnItemClickListenerImpl());

		mUpdateRss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowProgressDialog("正在更新rss", "loading");
				svrightscroll.scrollTo(0, 0);
				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							mBusinessRss.updateAllRss();
						} catch (Exception e) {
							e.printStackTrace();

							Handler myhandler = new Handler(Looper
									.getMainLooper()) {
								@Override
								public void handleMessage(Message msg) {
									// TODO Auto-generated method stub
									Toast.makeText(MainActivity.this, "网络异常",
											Toast.LENGTH_SHORT).show();
								}
							};
							myhandler.removeMessages(0);
							myhandler.sendEmptyMessage(0);
						}
						Handler myhandler = new Handler(Looper.getMainLooper()) {
							@Override
							public void handleMessage(Message msg) {
								// TODO Auto-generated method stub
								bindData();
								DismissProgressDialog();
							}
						};
						myhandler.removeMessages(0);
						myhandler.sendEmptyMessage(0);
					}
				}).start();

			}
		});
	}

	private class OnItemClickListenerImpl implements OnItemClickListener {

		String _RssName = "";
		String _ItemTitle = "";
		String _Pubdate = "";
		String _Description = null;

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			_RssName = ((TextView) arg1.findViewById(R.id.midname)).getText()
					.toString();
			_ItemTitle = ((TextView) arg1.findViewById(R.id.midtitle))
					.getText().toString();
			_Pubdate = ((TextView) arg1.findViewById(R.id.middate)).getText()
					.toString();

			_Description = "<h1>" + _ItemTitle + "</h1>\n\n<hr />"
					+ mBusinessRss.getDescription(_RssName, _ItemTitle);

			tvrighttext.setText(Html.fromHtml(_Description, new DownFile(
					tvrighttext), null));
			// tvrighttext.setText(_Description);
			mRightUnreadImage.setVisibility(View.GONE);
			svrightscroll.setVisibility(View.VISIBLE);
			if (!mBusinessRss.isRead(_RssName, _ItemTitle)) {

				mBusinessRss.setHasRead(_RssName, _ItemTitle);
				mExpandableAdapter.notifyDataSetChanged();
				((MidListViewAdapter) mMidListView.getAdapter())
						.notifyDataSetChanged();
			}

			mSetStarr.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mBusinessRss.isItemStarred(_ItemTitle)) {
						mBusinessRss.setItemUnstarr(_ItemTitle);
						Toast.makeText(MainActivity.this, "set unstarr",
								Toast.LENGTH_SHORT).show();
					} else {
						mBusinessRss.setItemStarr(_RssName, _ItemTitle,
								_Pubdate);
						Toast.makeText(MainActivity.this, "set starr",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}

	protected void ShowProgressDialog(String p_TitleResID, String p_MessageResID) {
		m_ProgressDialog = new ProgressDialog(this);
		m_ProgressDialog.setTitle(p_TitleResID);
		m_ProgressDialog.setMessage(p_MessageResID);
		m_ProgressDialog.show();
	}

	protected void DismissProgressDialog() {
		if (m_ProgressDialog != null) {
			m_ProgressDialog.dismiss();
		}
	}

	public void bindData() {
		mExpandableAdapter = new ExpandableAdapter(this, mBusinessRss,
				mMidListView, mMidUnreadImage);
		mExpandableListView.setAdapter(mExpandableAdapter);
	}

	public void setLayoutSize() {
		DisplayMetrics dm = getResources().getDisplayMetrics();

		leftView = (LinearLayout) findViewById(R.id.left_view);
		BitmapDrawable TileMe = new BitmapDrawable(getResources(),
				BitmapFactory.decodeResource(getResources(),
						R.drawable.left_background_repeat));
		// TileMe.setTileModeX(TileMode.REPEAT);
		TileMe.setTileModeY(TileMode.REPEAT);
		params = (LinearLayout.LayoutParams) leftView.getLayoutParams();
		params.width = (int) (dm.widthPixels * 0.3);
		leftView.setLayoutParams(params);

		midView = (LinearLayout) findViewById(R.id.mid_view);
		params = (LinearLayout.LayoutParams) midView.getLayoutParams();
		params.width = (int) (dm.widthPixels * 0.4);
		midView.setLayoutParams(params);
		midView.setOnTouchListener(this);

		rightView = (LinearLayout) findViewById(R.id.right_view);
		params = (LinearLayout.LayoutParams) rightView.getLayoutParams();
		params.width = (int) (dm.widthPixels * 0.6);
		rightView.setLayoutParams(params);
		rightView.setOnTouchListener(this);

		layout = (LinearLayout) findViewById(R.id.homelayout);
		gestureDetector = new GestureDetector(this, this);
		homeparams = (LayoutParams) layout.getLayoutParams();
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		if (e.getRawX() > 900 && ishomeopen) {
			setAnimation();
			layout.startAnimation(open_layout_anim);
			ishomeopen = false;
		}
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		setAnimation();
		if (e1.getRawX() - e2.getRawX() > 120 && ishomeopen) {
			layout.startAnimation(open_layout_anim);
			ishomeopen = false;
			return true;
		} else if (e1.getRawX() - e2.getRawX() < -80 && e1.getRawX() < 50
				&& !ishomeopen) {
			layout.startAnimation(close_layout_anim);
			ishomeopen = true;
			return true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return gestureDetector.onTouchEvent(event);
	}

	public void setAnimation() {
		open_layout_anim = AnimationUtils.loadAnimation(this,
				R.anim.open_layout);
		open_layout_anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				homeparams.leftMargin = -384;
				layout.setLayoutParams(homeparams);
				layout.clearAnimation();
			}
		});
		close_layout_anim = AnimationUtils.loadAnimation(this,
				R.anim.close_layout);
		close_layout_anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				homeparams.leftMargin = 0;
				layout.setLayoutParams(homeparams);
				layout.clearAnimation();
			}
		});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (_Cursor != null) {

			_Cursor.close();
		}
		super.onDestroy();
	}
}
