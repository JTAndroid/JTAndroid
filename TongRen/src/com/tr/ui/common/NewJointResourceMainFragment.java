package com.tr.ui.common;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.tr.R;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.ResourceBase;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.NewJointResourceFragment.ResourceType_new;
import com.tr.ui.demand.NeedDetailsActivity;
import com.tr.ui.knowledge.KnowledgeOfDetailActivity;
import com.tr.ui.widgets.LazyViewPager;
import com.tr.ui.widgets.LazyViewPager.OnPageChangeListener;
import com.utils.common.EConsts;
import com.utils.log.ToastUtil;

public class NewJointResourceMainFragment extends JBaseFragment {
	
	private RadioGroup tabRgp;
	private LazyViewPager resourceVp;
	
	private ResourceAdapter mAdapter;
	private List<NewJointResourceFragment> mListFragment = new ArrayList<NewJointResourceFragment>();// 页面列表
	
	private ResourceType_new mTarResType; // 被对接的资源类型
	private int currentPage;//要显示的对接内容1  知识 2  需求 3  组织  4  人脉
	private String userid;
	private NewJointResourceFragment currentFrg;
	private View view;
	private ImageView joinResourceBackIv;
	private JointResourceActivity formResourceActivity;
	private KnowledgeOfDetailActivity formKnowledgeActivity;
	private LinearLayout joinResourceBackLL;
	private int type;
	private int Knowledge = 2;
	private int Need =  3;
	
	public NewJointResourceMainFragment (){
		
	}
	public NewJointResourceMainFragment ( ResourceType_new mTarResType, ResourceBase mTarRes){
		this.mTarResType = mTarResType;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view = inflater.inflate(R.layout.activity_joint_resource, container, false);
		
		return view;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initControls();
	}
	private void initVars(){
		currentFrg = new NewJointResourceFragment();
		// 我的资源
		NewJointResourceFragment myResFrg = new NewJointResourceFragment();
		Bundle myResBdl = new Bundle();
		myResBdl.putSerializable(EConsts.Key.JOINT_RESOURCE_SOURCE, ResourceSource.My);
		myResBdl.putSerializable(EConsts.Key.TARGET_RESOURCE_TYPE, mTarResType);
		myResBdl.putSerializable(EConsts.Key.ID, userid);
		myResBdl.putInt("currentPage", currentPage);
		myResFrg.setArguments(myResBdl);
		mListFragment.add(myResFrg);
		// 好友资源
		NewJointResourceFragment friendResFrg = new NewJointResourceFragment();
		Bundle friResBdl = new Bundle();
		friResBdl.putSerializable(EConsts.Key.JOINT_RESOURCE_SOURCE, ResourceSource.Friend);
		friResBdl.putSerializable(EConsts.Key.TARGET_RESOURCE_TYPE, mTarResType);
		friResBdl.putSerializable(EConsts.Key.ID, userid);
		friendResFrg.setArguments(friResBdl);
		mListFragment.add(friendResFrg);
		// 金桐脑资源
		NewJointResourceFragment platformResFrg = new NewJointResourceFragment();
		Bundle plaResBdl = new Bundle();
		plaResBdl.putSerializable(EConsts.Key.JOINT_RESOURCE_SOURCE, ResourceSource.Platform);
		plaResBdl.putSerializable(EConsts.Key.TARGET_RESOURCE_TYPE, mTarResType);
		plaResBdl.putSerializable(EConsts.Key.ID, userid);
		platformResFrg.setArguments(plaResBdl);
		mListFragment.add(platformResFrg);
		
		
	}
	private void initControls(){
		
		resourceVp = (LazyViewPager) view.findViewById(R.id.resourceVp);
		mAdapter = new ResourceAdapter(getChildFragmentManager(),mListFragment);
		resourceVp.setAdapter(mAdapter);
		joinResourceBackIv = (ImageView) view.findViewById(R.id.joinResourceBackIv);
		joinResourceBackIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (type==Knowledge) {
					formKnowledgeActivity.knowDetailContentVp.setCurrentItem(0);
				}else{
					getActivity().finish();	
				}
				
			}
		});
		resourceVp.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageSelected(int arg0) {
				
				switch(arg0){
				case 0: // 我的资源
					tabRgp.check(R.id.myResRbtn);
					break;
				case 1: // 好友资源
					tabRgp.check(R.id.friendResRbtn);
					break;
				case 2: // 金桐脑资源
					tabRgp.check(R.id.platformResRbtn);
					break;
				}
			}
		});
		tabRgp = (RadioGroup) view.findViewById(R.id.tabRgp);

		// 设置当前选中项
		resourceVp.setCurrentItem(currentPage);
		switch(currentPage){
		case 0:
			tabRgp.check(R.id.myResRbtn);
			break;
		case 1:
			tabRgp.check(R.id.friendResRbtn);
			break;
		case 2:
			tabRgp.check(R.id.platformResRbtn);
			break;
		}
		
		tabRgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.myResRbtn:
					resourceVp.setCurrentItem(0);
					break;
				case R.id.friendResRbtn:
					resourceVp.setCurrentItem(1);
					break;
				case R.id.platformResRbtn:
					resourceVp.setCurrentItem(2);
					break;
				}
			}
		});
	}

	
	class ResourceAdapter extends FragmentPagerAdapter{

		private List<NewJointResourceFragment> mListFragment;
		public ResourceAdapter(FragmentManager fm) {
			super(fm);
		}

		public ResourceAdapter(FragmentManager fm,
				List<NewJointResourceFragment> mListFragment) {
			super(fm);
			this.mListFragment = mListFragment;
		}

		public void setmListFragment(List<NewJointResourceFragment> mListFragment) {
			this.mListFragment = mListFragment;
		}

		@Override
		public int getCount() {
			return mListFragment.size();
		}

		/*
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		*/
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			super.instantiateItem(container, position);
			currentFrg = mListFragment.get(position);
			return currentFrg;
		}
		@Override
		public Fragment getItem(int arg0) {
			currentFrg = mListFragment.get(arg0);
			return currentFrg;
		}
		@Override
		public int getItemPosition(Object object) {
		    return ResourceAdapter.POSITION_NONE;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object){
			super.destroyItem(container, position, object);
		}
	}
	
	// 资源来源
	public enum ResourceSource {
		My, Friend, Platform
	}

	public void setJointResourceResourceBase(ResourceType_new mTarResType, int currentPage, String userid) {
		this.mTarResType = mTarResType;
		this.currentPage = currentPage;
		this.userid = userid;
		initVars();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		currentFrg.onActivityResult(requestCode, resultCode, data);
	}
}
