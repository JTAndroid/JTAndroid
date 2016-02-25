package com.tr.ui.search;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.frg.FrgFlow;
import com.tr.ui.organization.widgets.ShowOrganizationFirstFaileLoginAlertDialog;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableFragmentListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableListener;
import com.utils.common.EUtil;

/**
 * 搜索页面，首页-点击搜索进去
 * 
 * @ClassName: SearchActivity.java
 * @Description: TODO(用一句话描述该文件做什么)
 * @author xuxinjian
 * @version V1.0
 * @Date 2014-4-30 上午9:02:28
 */
public class SearchActivity extends JBaseFragmentActivity implements
		ScrollableFragmentListener {

	private ViewPager mPager;// 页卡内容
	private View cursor;// 动画图片
	private TextView txtRequirementOut, txtRequirementIn, txtMember,txtSearchOrgAndCustomer,
			txtSearchKnowledge, txtSearchMetting, txtSearchDynamic,txtSearchDemand;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private int bmfrpW;// 好友、组织图片宽度
	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	public EditText mInputText;
	public TextView searchButton;
	private TextView[] textList;
	
	
	private final int TYPE_CONTACTS = 1;
	private final int TYPE_ORGANDCUSTOMER = 2;
	private final int TYPE_KNOWLEDGE = 3;
	private final int TYPE_COMMUNICATION = 4;
	private final int TYPE_DEMAND = 5;
	private int firstView;
	private int secondView;
	private int thirdView;
	private int fourthView;
	private int fifthView;
	private RelativeLayout.LayoutParams layoutParams;
	private ImageView home_search_iv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		// 初始化viewpage&fragment相关
		if (findViewById(R.id.searchVPager) != null) {
			// 如果我们从先前的状态恢复，那么我们不需要做任何事，直接返回
			if (savedInstanceState != null) {
				return;
			}
		}
		// 动态
		FrgFlow frgFlow = new FrgFlow();
		frgFlow.setParent(SearchActivity.this);
		// 创建fragment相关
		FrgSearch myRequirement = new FrgSearch();
		myRequirement.setType(FrgSearch.TYPE_REQUIREMENT_OUT);

		// FrgSearch frgMember = new FrgSearch();
		// frgMember.setType(FrgSearch.TYPE_REQUIREMENT_IN);

		// FrgSearch frgFriend = new FrgSearch();
		// frgFriend.setType(FrgSearch.TYPE_FRIEND);
		/*用户人脉*/
		FrgSearch frgMember = new FrgSearch();
		frgMember.setType(FrgSearch.TYPE_MEMBER);

		/*组织客户*/
		FrgSearch frgOrgAndCustomer = new FrgSearch();
		frgOrgAndCustomer.setType(FrgSearch.TYPE_ORGANDCUSTOMER);
		
		// 添加知识搜索
		FrgSearch frgKnowledge = new FrgSearch();
		frgKnowledge.setType(FrgSearch.TYPE_KNOWLEDGE);

		// 添加会议搜索
		FrgSearch frgMetting = new FrgSearch();
		frgMetting.setType(FrgSearch.TYPE_METTING);
		
		// 添加需求
		FrgDemandSearch frgDemand = new FrgDemandSearch();
		
		mFragments.add(frgMember);
		mFragments.add(frgOrgAndCustomer);
		mFragments.add(frgKnowledge);
		mFragments.add(frgMetting);
		mFragments.add(frgDemand);
		InitTextView();
		InitImageView();
		InitViewPager();
		
		int type = getIntent().getIntExtra("type", 0);
		//判断要跳到哪个fregment
		switch (type) {
		case TYPE_CONTACTS:
			mPager.setCurrentItem(0);
			break;
		case TYPE_ORGANDCUSTOMER:
			mPager.setCurrentItem(1);
			break;
		case TYPE_KNOWLEDGE:
			mPager.setCurrentItem(2);
			break;
		case TYPE_COMMUNICATION:
			mPager.setCurrentItem(3);
			break;
		case TYPE_DEMAND:
			mPager.setCurrentItem(4);
			break;
		default:
			break;
		}
		if(type == 0){
			String fromActivityName = getIntent().getStringExtra(ENavConsts.EFromActivityName);
			if (fromActivityName.equals("MeetingHomeActivity")) {// 会议直接跳转到搜索会议tab
				currIndex = 2;
				mPager.setCurrentItem(currIndex);
			} else {
				textList[0].setTextColor(App.getApp().getResources().getColor(R.color.home_dt_orange_font_comment_title));
			}
		}
	}

	/**
	 * 将never部分，放到actionbar的三个点里面去
	 */
	private void getOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initJabActionBar() {
		// 将下拉列表添加到actionbar中
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowTitleEnabled(false);

		actionbar.setDisplayShowCustomEnabled(true);
		 actionbar.setCustomView(R.layout.search_actionbar_edit);

		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.search_actionbar_edit, null);
		searchButton = (TextView) v.findViewById(R.id.home_search_tv);
		mInputText = (EditText) v.findViewById(R.id.home_search_edit);
		home_search_iv = (ImageView) v.findViewById(R.id.home_search_iv);
		home_search_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mInputText.setText("");
				home_search_iv.setVisibility(View.GONE);
			}
		});
		mInputText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				home_search_iv.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
				String mCurKeyword = mInputText.getText().toString();
				if((mFragments.get(currIndex)) instanceof FrgSearch){
					FrgSearch nowFrg = (FrgSearch) mFragments.get(currIndex);
					nowFrg.mCurKeyword = mCurKeyword;
					nowFrg.startGetData(0,true);
				}else if((mFragments.get(currIndex)) instanceof FrgDemandSearch){
					FrgDemandSearch nowFrg = (FrgDemandSearch) mFragments.get(currIndex);
					nowFrg.mCurKeyword = mCurKeyword;
					nowFrg.startGetData(0,true);
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		actionbar.setCustomView(v);
	}

	/**
	 * actionbar 中菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getSupportMenuInflater().inflate(R.menu.home_search, menu);
		// return super.onCreateOptionsMenu(menu);

		// getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		// com.actionbarsherlock.view.MenuItem menuItem =
		// menu.findItem(R.id.home_ab_menu_share_item);
		// mShareActionProvider = (ShareActionProvider)
		// menuItem.getActionProvider();
		// Intent shareIntent=getShareIntent();
		// mShareActionProvider.setShareIntent(shareIntent);
		return true;
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 一下部分为fragment相关部分---begin02 初始化动画
	 */
	private void InitImageView() {
		cursor = findViewById(R.id.cursor);
		//权重一共分了5.4份，所以除以5.4
		bmpW = (int) (getWindowManager().getDefaultDisplay().getWidth()/ 5.4);
		bmfrpW = (int) (bmpW * 1.2);
		layoutParams = (RelativeLayout.LayoutParams) cursor.getLayoutParams();
		layoutParams.width = bmfrpW;
		cursor.setLayoutParams(layoutParams);
		textList[0].setTextColor(App.getApp().getResources().getColor(R.color.home_dt_orange_font_comment_title));
	}

	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		txtRequirementOut = (TextView) findViewById(R.id.txtsearchRequirementOut);
		txtRequirementIn = (TextView) findViewById(R.id.txtsearchRequirementIn);
		txtMember = (TextView) findViewById(R.id.txtSearchMember);
		txtSearchOrgAndCustomer = (TextView) findViewById(R.id.txtSearchOrgAndCustomer);
		txtSearchKnowledge = (TextView) findViewById(R.id.txtSearchKnowledge);
		txtSearchMetting = (TextView) findViewById(R.id.txtSearchMetting);
		txtSearchDynamic = (TextView) findViewById(R.id.txtSearchDynamic);
		txtSearchDemand = (TextView) findViewById(R.id.txtSearchDemand);
		
		textList = new TextView[] { txtMember,txtSearchOrgAndCustomer, txtSearchKnowledge,
				txtSearchMetting,txtSearchDemand };

		for (int i = 0; i < textList.length; i++) {
			textList[i].setOnClickListener(new MyOnClickListener(i));
		}

		// txtRequirementOut.setOnClickListener(new MyOnClickListener(0));
		// txtRequirementIn.setOnClickListener(new MyOnClickListener(1));
		// txtMember.setOnClickListener(new MyOnClickListener(2));
		// txtSearchKnowledge.setOnClickListener(new MyOnClickListener(3));

	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.searchVPager);
		mPager.setAdapter(new HomeFrgPagerAdapter(getSupportFragmentManager(),mFragments));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

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
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		// int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			int blackColor = App.getApp().getResources().getColor(R.color.black);
			int selectColor = App.getApp().getResources().getColor(R.color.home_dt_orange_font_comment_title);
			firstView = txtMember.getMeasuredWidth();
			secondView = txtSearchOrgAndCustomer.getMeasuredWidth();
			thirdView = txtSearchKnowledge.getMeasuredWidth();
			fourthView = txtSearchMetting.getMeasuredWidth();
			fifthView = txtSearchDemand.getMeasuredWidth();
			layoutParams = (RelativeLayout.LayoutParams) cursor.getLayoutParams();
			switch (arg0) {
			case 0:
				layoutParams.width = firstView;
				break;
			case 1:
				layoutParams.width = secondView;
				break;
			case 2:
				layoutParams.width = thirdView;
				break;
			case 3:
				layoutParams.width = fourthView;
				break;
			case 4:
				layoutParams.width = fifthView;
				break;

			default:
				break;
			}
			cursor.setLayoutParams(layoutParams);

			// txtRequirementOut.setTextColor(blackColor);
			// txtRequirementIn.setTextColor(blackColor);
			// txtMember.setTextColor(blackColor);
			// txtSearchKnowledge.setTextColor(blackColor);
			/*
			 * switch (arg0) {// arg0为当前页 case 0:
			 * txtRequirementOut.setTextColor(selectColor); if (currIndex == 1)
			 * { // currIndex为之前页 animation = new TranslateAnimation(one, 0, 0,
			 * 0); } else if (currIndex == 2) { animation = new
			 * TranslateAnimation(two, 0, 0, 0); } break; case 1:
			 * txtRequirementIn.setTextColor(selectColor); if (currIndex == 0) {
			 * animation = new TranslateAnimation(offset, one, 0, 0); } else if
			 * (currIndex == 2) { animation = new TranslateAnimation(two, one,
			 * 0, 0); } break; case 2: txtMember.setTextColor(selectColor); if
			 * (currIndex == 0) { animation = new TranslateAnimation(offset,
			 * two, 0, 0); } else if (currIndex == 1) { animation = new
			 * TranslateAnimation(one, two, 0, 0); } break; }
			 */
			/*
			 * switch (arg0) {// arg0为当前页 case 0:
			 * txtRequirementOut.setTextColor(selectColor); break; case 1:
			 * txtRequirementIn.setTextColor(selectColor); break; case 2:
			 * txtMember.setTextColor(selectColor); break; case 3:
			 * txtSearchKnowledge.setTextColor(selectColor); break; }
			 */

			// 抽算法
			// for(TextView tv : textList ){
			// tv.setTextColor(blackColor);
			// }

			// 上一标签字变黑
			textList[currIndex].setTextColor(blackColor);
			// 当前标签字高亮
			textList[arg0].setTextColor(selectColor);
			/*
			 * currIndex : 上一标签页游标位置 Shang : 活动前位置 Zhe : 活动后位置
			 */
			
			int Shang = 0;
			int Zhe = 0;
			//上一个位置是0时
			if (currIndex == 0) {
				Shang = 0;
				if (arg0 == 0) {
					Zhe = 0;
				}
				if (arg0 == 1) {
					Zhe = firstView+2;
				}
			}
			//上一个位置是1时
			if (currIndex == 1) {
				Shang = firstView + 2;
				if (arg0 == 0) {
					Zhe = 0;
				}
				if (arg0 == 2) {
					Zhe = firstView + secondView+3;
				}
			}
			//上一个位置是2时
			if (currIndex == 2) {
				Shang = firstView + secondView + 3;
				if (arg0 == 1) {
					Zhe = firstView;
				}
				if (arg0 == 3) {
					Zhe = firstView + secondView + thirdView+4;
				}
			}
			//上一个位置是3时
			if (currIndex == 3) {
				Shang = firstView + secondView + thirdView + 4;
				if (arg0 == 2) {
					Zhe = firstView + secondView;
				}
				if (arg0 == 4) {
					Zhe = firstView + secondView + thirdView + fourthView+5;
				}
			}
			//上一个位置是4时
			if (currIndex == 4) {
				Shang = firstView + secondView + thirdView + fourthView + 6;
				if (arg0 == 3) {
					Zhe = firstView + secondView + thirdView;
				}
				if (arg0 == 5) {
					Zhe = firstView + secondView + thirdView + fourthView + fifthView+5;
				}
			}
			
//			animation = new TranslateAnimation(one * currIndex, one * arg0, 0, 0);
			animation = new TranslateAnimation(Shang, Zhe, 0, 0);

			currIndex = arg0;
			mPager.setCurrentItem(currIndex);
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(100);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	public class HomeFrgPagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

		public HomeFrgPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public HomeFrgPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> fragment) {
			super(fm);
			mFragments = fragment;
		}

		// 这段不注释，fragment来回滑动几次就不显示了
		// @Override
		// public Object instantiateItem(ViewGroup container, int position) {
		// getSupportFragmentManager().beginTransaction()
		// .add(R.id.homeVPager, mFragments.get(position)).commit();
		//
		// Fragment f = mFragments.get(position);
		// if(f == null){
		// showToast("getItem = null");
		// }
		//
		// return mFragments.get(position);
		//
		// }

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment f = mFragments.get(arg0);
			if (f == null) {
				EUtil.showToast("getItem = null");
			}
			return mFragments.get(arg0);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	}

	@Override
	public void onFragmentAttached(ScrollableListener fragment, int position) {

	}

	@Override
	public void onFragmentDetached(ScrollableListener fragment, int position) {

	}

}
