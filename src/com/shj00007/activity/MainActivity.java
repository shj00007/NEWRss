package com.shj00007.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.fragmenttest1.R;
import com.shj00007.adapter.ExpandableAdapter;
import com.shj00007.adapter.MidListViewAdapter;
import com.shj00007.business.BusinessRss;
import com.shj00007.utility.DownFile;

public class MainActivity extends Activity implements OnGestureListener,
		OnTouchListener {

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
	private ImageView ivAddRss = null;
	private ProgressDialog m_ProgressDialog;

	private ListView mMidListView = null;

	private TextView tvrighttext = null;
	private ScrollView svrightscroll = null;
	private String _Description = null;

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

	}

	public void initView() {
		ivAddRss = (ImageView) findViewById(R.id.ivAddRss);
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListleft);
		mMidListView = (ListView) findViewById(R.id.mid_listview);
		tvrighttext = (TextView) findViewById(R.id.tvright_text_up);
		svrightscroll = (ScrollView) findViewById(R.id.svrightscrool);
	}

	public void setListener() {
		mExpandableListView.setOnTouchListener(this);
		mMidListView.setOnTouchListener(this);
		tvrighttext.setOnTouchListener(this);
		svrightscroll.setOnTouchListener(this);
		ivAddRss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final EditText et = new EditText(MainActivity.this);
				et.setText("http://cn.engadget.com/rss.xml");
				ShowProgressDialog("!!!", "loading");
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("请输入")
						.setView(et)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

										mBusinessRss.downloadRSS(et.getText()
												.toString());
										mBusinessRss.addRssFeed("新闻");
										mBusinessRss.updateRss();
										bindData();

									}
								}).setNegativeButton("取消", null).show();

				DismissProgressDialog();

			}
		});

		mMidListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String _RssName = ((TextView) arg1.findViewById(R.id.midname))
						.getText().toString();
				String _ItemTitle = ((TextView) arg1
						.findViewById(R.id.midtitle)).getText().toString();
				_Description = mBusinessRss
						.getDescription(_RssName, _ItemTitle);
				tvrighttext.setText(Html.fromHtml(_Description, new DownFile(
						tvrighttext, _Description), null));
				mBusinessRss.setHasRead(_RssName, _ItemTitle);
				// ((TextView) arg1.findViewById(R.id.midtitle))
				// .setTextAppearance(MainActivity.this,
				// R.style.styleHasRead);
				mExpandableAdapter.notifyDataSetChanged();

				((MidListViewAdapter) arg0.getAdapter()).notifyDataSetChanged();
			}
		});
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
				mMidListView);
		mExpandableListView.setAdapter(mExpandableAdapter);
	}

	public void setLayoutSize() {
		DisplayMetrics dm = getResources().getDisplayMetrics();

		leftView = (LinearLayout) findViewById(R.id.left_view);
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
		if (e1.getX() - e2.getX() > 120 && ishomeopen) {
			layout.startAnimation(open_layout_anim);
			ishomeopen = false;
			return true;
		} else if (e1.getX() - e2.getX() < -80 && e1.getX() < 50 && !ishomeopen) {
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

}
