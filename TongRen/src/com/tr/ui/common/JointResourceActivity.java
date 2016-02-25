package com.tr.ui.common;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.tr.R;
import com.tr.model.obj.ResourceBase;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.common.JointResourceMainFragment.ResourceSource;
import com.tr.ui.common.NewJointResourceFragment.ResourceType_new;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.LazyViewPager;
import com.tr.ui.widgets.LazyViewPager.OnPageChangeListener;
import com.utils.common.EConsts;

/**
 * 对接资源
 * @author leon
 */
public class JointResourceActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	private RadioGroup tabRgp;
	private LazyViewPager resourceVp;
	
//	private ResourceAdapter mAdapter;
	private List<JointResourceFragment> mListFragment;
	
	public  ResourceType mTarResType; // 被对接的资源类型
	public  ResourceType_new newTarResType; // 被对接的资源类型
	public ResourceBase mTarRes; // 被对接的资源
	private JointResourceFragment currentFrg;
	public int currentPage;//要显示的frg
	public String userid;

	private JointResourceMainFragment jointResourceMainFragment;
	private NewJointResourceMainFragment newjointResourceMainFragment;
	@Override
	public void initJabActionBar() {
//		jabGetActionBar().setTitle("资源对接");
//		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "资源对接", false, null, false);
//		jabGetActionBar().setDisplayShowHomeEnabled(true);
		jabGetActionBar().hide();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_joint_resource_main);
		initVars();
//		initControls();
	}
	private void initVars(){
		mTarRes = (ResourceBase) getIntent().getSerializableExtra(EConsts.Key.TARGET_RESOURCE); // 目标资源
		currentPage = getIntent().getIntExtra("currentPage", 3);
		jointResourceMainFragment = new JointResourceMainFragment();
		newjointResourceMainFragment = new NewJointResourceMainFragment();
		
//		Bundle myResBdl = new Bundle();
//		myResBdl.putSerializable(EConsts.Key.TARGET_RESOURCE_TYPE, mTarResType);
//		myResBdl.putSerializable(EConsts.Key.TARGET_RESOURCE, mTarRes);
//		jointResourceMainFragment.setArguments(myResBdl);
		if(currentPage == 3){
			mTarResType = (ResourceType) getIntent().getSerializableExtra(EConsts.Key.TARGET_RESOURCE_TYPE); // 目标资源类型
			jointResourceMainFragment.setJointResourceResourceBase(mTarResType, mTarRes);
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_jointresource, jointResourceMainFragment).commit();
		}else{
			newTarResType = (ResourceType_new) getIntent().getSerializableExtra(EConsts.Key.TARGET_RESOURCE_TYPE); // 目标资源类型
			userid = getIntent().getStringExtra(EConsts.Key.ID);
			newjointResourceMainFragment.setJointResourceResourceBase(newTarResType, currentPage, userid);
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_jointresource, newjointResourceMainFragment).commit();
		}
		
		
//		mListFragment = new ArrayList<JointResourceFragment>(); // 页面列表
//		mTarResType = (ResourceType) getIntent().getSerializableExtra(EConsts.Key.TARGET_RESOURCE_TYPE); // 目标资源类型
//		mTarRes = (ResourceBase) getIntent().getSerializableExtra(EConsts.Key.TARGET_RESOURCE); // 目标资源
//		// Fragment参数
//		currentFrg = new JointResourceFragment();
//		// 我的资源
//		JointResourceFragment myResFrg = new JointResourceFragment();
//		Bundle myResBdl = new Bundle();
//		myResBdl.putSerializable(EConsts.Key.JOINT_RESOURCE_SOURCE, ResourceSource.My);
//		myResBdl.putSerializable(EConsts.Key.TARGET_RESOURCE_TYPE, mTarResType);
//		myResBdl.putSerializable(EConsts.Key.TARGET_RESOURCE, mTarRes);
//		myResFrg.setArguments(myResBdl);
//		mListFragment.add(myResFrg);
//		// 好友资源
//		JointResourceFragment friendResFrg = new JointResourceFragment();
//		Bundle friResBdl = new Bundle();
//		friResBdl.putSerializable(EConsts.Key.JOINT_RESOURCE_SOURCE, ResourceSource.Friend);
//		friResBdl.putSerializable(EConsts.Key.TARGET_RESOURCE_TYPE, mTarResType);
//		friResBdl.putSerializable(EConsts.Key.TARGET_RESOURCE, mTarRes);
//		friendResFrg.setArguments(friResBdl);
//		mListFragment.add(friendResFrg);
//		// 金桐脑资源
//		JointResourceFragment platformResFrg = new JointResourceFragment();
//		Bundle plaResBdl = new Bundle();
//		plaResBdl.putSerializable(EConsts.Key.JOINT_RESOURCE_SOURCE, ResourceSource.Platform);
//		plaResBdl.putSerializable(EConsts.Key.TARGET_RESOURCE_TYPE, mTarResType);
//		plaResBdl.putSerializable(EConsts.Key.TARGET_RESOURCE, mTarRes);
//		platformResFrg.setArguments(plaResBdl);
//		mListFragment.add(platformResFrg);
//		// 适配器
//		mAdapter = new ResourceAdapter(getSupportFragmentManager());
	}
//
//	private void initControls(){
//		
//		resourceVp = (LazyViewPager) findViewById(R.id.resourceVp);
//		resourceVp.setAdapter(mAdapter);
//		resourceVp.setOnPageChangeListener(new OnPageChangeListener(){
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				
//			}
//
//			@Override
//			public void onPageSelected(int arg0) {
//				
//				switch(arg0){
//				case 0: // 我的资源
//					tabRgp.check(R.id.myResRbtn);
//					break;
//				case 1: // 好友资源
//					tabRgp.check(R.id.friendResRbtn);
//					break;
//				case 2: // 金桐脑资源
//					tabRgp.check(R.id.platformResRbtn);
//					break;
//				}
//			}
//		});
//		// 设置当前选中项
//		resourceVp.setCurrentItem(0);
//		tabRgp = (RadioGroup) findViewById(R.id.tabRgp);
//		tabRgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				switch(checkedId){
//				case R.id.myResRbtn:
//					resourceVp.setCurrentItem(0);
//					break;
//				case R.id.friendResRbtn:
//					resourceVp.setCurrentItem(1);
//					break;
//				case R.id.platformResRbtn:
//					resourceVp.setCurrentItem(2);
//					break;
//				}
//			}
//		});
//	}
//
//	
//	class ResourceAdapter extends FragmentPagerAdapter{
//
//		public ResourceAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public int getCount() {
//			return mListFragment.size();
//		}
//
//		/*
//		@Override
//		public boolean isViewFromObject(View arg0, Object arg1) {
//			return arg0 == arg1;
//		}
//		*/
//
//		@Override
//		public Fragment getItem(int arg0) {
//			currentFrg = mListFragment.get(arg0);
//			return mListFragment.get(arg0);
//		}
//
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object){
//			super.destroyItem(container, position, object);
//		}
//	}
//	
//	// 资源来源
//	public enum ResourceSource {
//		My, Friend, Platform
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		jointResourceMainFragment.onActivityResult(requestCode, resultCode, intent); 
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
}
