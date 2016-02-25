package com.tr.ui.connections.revision20150122;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tr.R;
import com.tr.model.obj.Connections;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

/**
 * 推荐联系人
 * @author gushi
 *
 */
public class PushPeople2Activity extends JBaseFragmentActivity {

	/** 标签组  **/
	private RadioGroup tabRadioGroup;
	/** 邀请 标签   **/
	private RadioButton inviterRadioButton;
	/** 加好友 标签   **/
	private RadioButton addFriendRadioButton;
	/** 主ViewPager */
	private ViewPager mainViewPager;
	
	/** fragment 列表 */
	private List<PushPeopleFragment> pushPeopleFragmentList;
	
	private PushPeopleAdapter pushPeopleAdapter;
	
	/** 完成 MenuItem */
	private MenuItem finishMenuItem; 
	
	private Context context;
	private ArrayList<RadioButton> radioButtonList;
	private ArrayList<Connections> mOnLineConnectionsList;
	private ArrayList<Connections> mOffLineConnectionsList;
	
	
	public Context getContext() {
		return context;
	}
	
	@Override
	public void initJabActionBar() {
//		jabGetActionBar().setTitle("手机通讯录");
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "手机通讯录", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		
		Intent intent = getIntent();
		ArrayList<Connections> onLineConnectionsList = (ArrayList<Connections>) intent.getSerializableExtra("listOnLine");
		if(onLineConnectionsList != null){
			mOnLineConnectionsList = onLineConnectionsList;
		}
		ArrayList<Connections> offLineConnectionsList = (ArrayList<Connections>) intent.getSerializableExtra("listOffLine");
		if(offLineConnectionsList != null){
			mOffLineConnectionsList = offLineConnectionsList;
		}
		
		setContentView(R.layout.activity_push_people2);
		
		tabRadioGroup = (RadioGroup) findViewById(R.id.tabRadioGroup);
		inviterRadioButton = (RadioButton) findViewById(R.id.inviterRadioButton);
		addFriendRadioButton = (RadioButton) findViewById(R.id.addFriendRadioButton);
		
		radioButtonList = new ArrayList<RadioButton>();
		radioButtonList.add(inviterRadioButton);
		radioButtonList.add(addFriendRadioButton);
		
		
		mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
		
		pushPeopleFragmentList = new ArrayList<PushPeopleFragment>();
		pushPeopleFragmentList.add(new PushPeopleFragment(1, mOffLineConnectionsList));
		pushPeopleFragmentList.add(new PushPeopleFragment(2, mOnLineConnectionsList));
		
		pushPeopleAdapter = new PushPeopleAdapter(getSupportFragmentManager(), pushPeopleFragmentList);
		mainViewPager.setAdapter(pushPeopleAdapter);
		mainViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				radioButtonList.get(arg0).setChecked(true);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			} 
		});
		
		mainViewPager.setCurrentItem(0);
		tabRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mainViewPager.setCurrentItem(radioButtonList.indexOf(findViewById(checkedId)));
			}
		});
		
	}
	
	
	
	
	class PushPeopleAdapter extends FragmentPagerAdapter {
		
		private List<PushPeopleFragment> mPushPeopleFragmentList ;

		public PushPeopleAdapter(FragmentManager fm, List<PushPeopleFragment> pushPeopleFragmentList) {
			super(fm);
			mPushPeopleFragmentList = pushPeopleFragmentList;
		}
		
		@Override
		public int getCount() {
			return mPushPeopleFragmentList.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			return mPushPeopleFragmentList.get(arg0);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object){
			super.destroyItem(container, position, object);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		(finishMenuItem = menu.add(0, Menu.NONE, 0, "完成")).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// 这里要写跳转到框架 然后结束自己.
		if(finishMenuItem == item){
			ENavigate.startMainActivity((Activity)getContext());
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	

}
