package com.tr.ui.mystart;


import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.mystart.frg.FrgMyRequirement;
import com.utils.common.EUtil;

/**
 * @Filename MyStartActivity.java
 * @Author xuxinjian
 * @Date 2014-4-17
 * @description 首页-我的页面
 */
public class MyStartActivity extends JBaseFragmentActivity {

	private ViewPager mPager;// 页卡内容
	private View cursor;// 动画图片
	private TextView txtRequirement, txtAffairs/*, txtKnowLedge*/;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	public  ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mystart);
        // 初始化viewpage&fragment相关
    	if (findViewById(R.id.mystartVPager) != null) {
			// 如果我们从先前的状态恢复，那么我们不需要做任何事，直接返回
			if (savedInstanceState != null) {
				return;
			}
		}
    	
		//创建fragment相关
    	String tempuserID =  App.getApp().getAppData().getUser().mID+"";
    	FrgMyRequirement myRequirement = new FrgMyRequirement();
    	myRequirement.setType(FrgMyRequirement.TYPE_REQUIREMENT,tempuserID);
    	
    	FrgMyRequirement myAffairs = new FrgMyRequirement();
    	myAffairs.setType(FrgMyRequirement.TYPE_AFFAIR,tempuserID);
    	
//		FrgMyRequirement myKnowledge = new FrgMyRequirement();
//		myKnowledge.setType(FrgMyRequirement.TYPE_KNOWLEDGE, tempuserID);
    	
		mFragments.add(myRequirement);
		mFragments.add(myAffairs);
//		mFragments.add(myKnowledge);
		InitImageView();
		InitTextView();
		InitViewPager();
	}
	
	/**
	 * 将never部分，放到actionbar的三个点里面去
	 */
	private void getOverflowMenu() {  
	     try {  
	        ViewConfiguration config = ViewConfiguration.get(this);  
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");  
	        if(menuKeyField != null) {  
	            menuKeyField.setAccessible(true);  
	            menuKeyField.setBoolean(config, false);  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	}  
	
	public void initJabActionBar(){
		    //将下拉列表添加到actionbar中
		    ActionBar actionbar = getActionBar();  
		    actionbar.setDisplayHomeAsUpEnabled(true);
		    actionbar.setDisplayShowTitleEnabled(true);
		    actionbar.setTitle("我的");
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
	 * 一下部分为fragment相关部分---begin02
	 * 初始化动画
	 */
	private void InitImageView() {
		cursor =  findViewById(R.id.cursor);
		bmpW = getWindowManager().getDefaultDisplay().getWidth() / 2;
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cursor.getLayoutParams();
		layoutParams.width = bmpW;
		cursor.setLayoutParams(layoutParams);
	}

	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		txtRequirement = (TextView) findViewById(R.id.txtMystartRequirement);
		txtAffairs = (TextView) findViewById(R.id.txtMyAffairs);
//		txtKnowLedge = (TextView) findViewById(R.id.txtMystartKnowledge);

		txtRequirement.setOnClickListener(new MyOnClickListener(0));
		txtAffairs.setOnClickListener(new MyOnClickListener(1));
//		txtKnowLedge.setOnClickListener(new MyOnClickListener(2));
	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.mystartVPager);
		mPager.setAdapter(new HomeFrgPagerAdapter(
				getSupportFragmentManager(), mFragments));
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
		int one = offset * 1 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 1;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			int blackColor = App.getApp().getResources().getColor(R.color.black);
			int selectColor = App.getApp().getResources().getColor(R.color.home_dt_orange_font_comment_title);
			
			txtRequirement.setTextColor(blackColor);
//			txtKnowLedge.setTextColor(blackColor);
			txtAffairs.setTextColor(blackColor);
			switch (arg0) {// arg0为当前页
			case 0:
				txtRequirement.setTextColor(selectColor);
				if (currIndex == 1) { // currIndex为之前页
					animation = new TranslateAnimation(one, 0, 0, 0);
				}
//				else if (currIndex == 2) {
//					animation = new TranslateAnimation(two, 0, 0, 0);
//				}
				break;
			case 1:
				txtAffairs.setTextColor(selectColor);
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} 
//				else if (currIndex == 2) {
//					animation = new TranslateAnimation(two, one, 0, 0);
//				}
				break;
			/*case 2:
				txtKnowLedge.setTextColor(selectColor);
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;*/
			}
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

		//这段不注释，fragment来回滑动几次就不显示了
//		@Override
//		public Object instantiateItem(ViewGroup container, int position) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.homeVPager, mFragments.get(position)).commit();
//			
//			Fragment f = mFragments.get(position);
//			if(f == null){
//				showToast("getItem = null");
//			}
//			
//			return mFragments.get(position); 
//
//		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment f = mFragments.get(arg0);
			if(f == null){
				EUtil.showToast("getItem = null");
			}
			return mFragments.get(arg0);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	}
	
}
