package com.tr.ui.conference.myhy.ui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.model.demand.NeedItemData;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.demand.MeNeedActivity;
/**
 * 新版 我的会议
 * @author zhongshan
 *
 */
public class MyMeetingActivity extends JBaseFragmentActivity {

	private View cursor;// 动画图片
	private int bmpW;// 动画图片宽度
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private TextView meetingTabTv, peopleTabTv,orgnazationTabTv, knowledgeTabTv, requirementTabTv, noteTabTv;
	
	private ViewPager viewPager;
	/**fragment集合*/
	public ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
	private MyMeetingViewPagerAdapter viewPagerAdapter;
	private View view;
	private TextView titleTv;
	private ImageView titleIv;
	private MenuItem findItem;
	private boolean isCreate;

	@Override
	public void initJabActionBar() {
		ActionBar actionBar = getActionBar();
		
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		final View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		actionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		actionBar.setCustomView(mCustomView, mP);
		actionBar.setTitle(" ");
		titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);
		titleTv.setText("我的活动");
		titleIv = (ImageView) mCustomView.findViewById(R.id.titleIv);
		titleIv.setVisibility(View.GONE);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_select_all_project, menu);
		findItem = menu.findItem(R.id.select_all);
		findItem.setTitle("创建");
		isCreate = true;
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.select_all:
			if (findItem.getTitle().length() > 0) {
				if (isCreate) {
					ENavigate.startInitiatorHYActivity(MyMeetingActivity.this);
				}
			}
			break;
		case android.R.id.home:
				finish();
				break;
		}

		return true;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_mymeting_layout);
		initView();
		initViewPager();
	}

	private void initView() {
		view = findViewById(R.id.view);
		viewPager = (ViewPager) findViewById(R.id.myMeetingViewPager);
		meetingTabTv = (TextView) findViewById(R.id.meetingTabTv);
		peopleTabTv = (TextView) findViewById(R.id.peopleTabTv);
		orgnazationTabTv = (TextView) findViewById(R.id.orgnazationTabTv);
		knowledgeTabTv = (TextView) findViewById(R.id.knowledgeTabTv);
		requirementTabTv = (TextView) findViewById(R.id.requirementTabTv);
		noteTabTv = (TextView) findViewById(R.id.noteTabTv);
		meetingTabTv.setOnClickListener(new MyOnClickListener(0));
		peopleTabTv.setOnClickListener(new MyOnClickListener(1));
		orgnazationTabTv.setOnClickListener(new MyOnClickListener(2));
		knowledgeTabTv.setOnClickListener(new MyOnClickListener(3));
		requirementTabTv.setOnClickListener(new MyOnClickListener(4));
		noteTabTv.setOnClickListener(new MyOnClickListener(5));
		cursor = findViewById(R.id.myConfCursor);
		bmpW = getWindowManager().getDefaultDisplay().getWidth() / 6;
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cursor.getLayoutParams();
		layoutParams.width = bmpW;
		cursor.setLayoutParams(layoutParams);
	}
	
	public View getDropDownView(){
		return view;
	}
	
	private void initViewPager() {
		
		MeetingListOfMeFrg meetingListOfMeFrg = new MeetingListOfMeFrg();
		MeetingPeopleFrg meetingPeopleFrg = new MeetingPeopleFrg();
		MeetingOrgFrg meetingOrgFrg = new MeetingOrgFrg();
		MeetingKnowledgeOrRequirementOrNoteFrg meetingknowledgeFrg = new MeetingKnowledgeOrRequirementOrNoteFrg(MeetingKnowledgeOrRequirementOrNoteFrg.KnowlwdgeOrRequireMentType.knowledge);
		MeetingKnowledgeOrRequirementOrNoteFrg mRequirementFrg = new MeetingKnowledgeOrRequirementOrNoteFrg(MeetingKnowledgeOrRequirementOrNoteFrg.KnowlwdgeOrRequireMentType.requirement);
		MeetingKnowledgeOrRequirementOrNoteFrg meetingNoteFrg = new MeetingKnowledgeOrRequirementOrNoteFrg(MeetingKnowledgeOrRequirementOrNoteFrg.KnowlwdgeOrRequireMentType.note);

		mFragmentList.add(meetingListOfMeFrg);
		mFragmentList.add(meetingPeopleFrg);
		mFragmentList.add(meetingOrgFrg);
		mFragmentList.add(meetingknowledgeFrg);
		mFragmentList.add(mRequirementFrg);
		mFragmentList.add(meetingNoteFrg);
		
		viewPager.setOffscreenPageLimit(6);
		viewPagerAdapter = new MyMeetingViewPagerAdapter(getSupportFragmentManager(),mFragmentList);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyHyOnPageChangeListener());
	}
	
	private class MyMeetingViewPagerAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
		public MyMeetingViewPagerAdapter(FragmentManager fm,ArrayList<Fragment> mFragmentList) {
			super(fm);
			this.mFragmentList = mFragmentList;
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}
		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}
	}
	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;
		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	};
	/**
	 * 底部滑动截监听
	 * @author zhongshan
	 */
	public class MyHyOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		int three = one * 3; //
		int four = one * 4;
		int five = one * 5;
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			int blackColor = App.getApp().getResources().getColor(R.color.black);
			int selectColor = App.getApp().getResources().getColor(R.color.home_dt_orange_font_comment_title);
			meetingTabTv.setTextColor(blackColor);
			peopleTabTv.setTextColor(blackColor);
			orgnazationTabTv.setTextColor(blackColor);
			knowledgeTabTv.setTextColor(blackColor);
			requirementTabTv.setTextColor(blackColor);
			noteTabTv.setTextColor(blackColor);
			switch (arg0) {
			case 0:
				meetingTabTv.setTextColor(selectColor);
				if (currIndex == 1) { // currIndex为之前页
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, 0, 0, 0);
				}else if (currIndex == 5) {
					animation = new TranslateAnimation(five, 0, 0, 0);
				}

				break;
			case 1:
				peopleTabTv.setTextColor(selectColor);
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, one, 0, 0);
				}else if (currIndex == 5) {
					animation = new TranslateAnimation(five, one, 0, 0);
				}
				break;
			case 2:
				orgnazationTabTv.setTextColor(selectColor);
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				} else if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, two, 0, 0);
				}else if (currIndex == 5) {
					animation = new TranslateAnimation(five, two, 0, 0);
				}

				break;
			case 3:
				knowledgeTabTv.setTextColor(selectColor);
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				} else if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, three, 0, 0);
				}else if (currIndex == 5) {
					animation = new TranslateAnimation(five, three, 0, 0);
				}
				
				break;
			case 4:
				requirementTabTv.setTextColor(selectColor);
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, four, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, four, 0, 0);
				} else if (currIndex == 0) {
					animation = new TranslateAnimation(offset, four, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, four, 0, 0);
				}else if (currIndex == 5) {
					animation = new TranslateAnimation(five, four, 0, 0);
				}
				break;

			case 5:
				noteTabTv.setTextColor(selectColor);
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, five, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, five, 0, 0);
				} else if (currIndex == 0) {
					animation = new TranslateAnimation(offset, five, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, five, 0, 0);
				}else if (currIndex == 4) {
					animation = new TranslateAnimation(four, five, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			viewPager.setCurrentItem(currIndex);
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(100);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}
	

}
