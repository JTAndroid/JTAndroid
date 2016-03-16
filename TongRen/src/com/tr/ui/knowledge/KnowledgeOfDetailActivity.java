package com.tr.ui.knowledge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.im.MJTPushMessage;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.knowledge.KnowledgeComment;
import com.tr.model.knowledge.KnowledgeStatics;
import com.tr.model.obj.ResourceBase;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.JointResourceMainFragment;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.demand.MyView.MyViewPager;
import com.tr.ui.home.frg.JoinFrg_chart;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.knowledge.swipeback.SwipeBackActivity;
import com.tr.ui.knowledge.utils.ActivityCollector;
import com.utils.common.Constants;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.IBindData;

/**
 * @ClassName KnowledgeOfDetailActivity.java
 * @Description 知识详情页面
 * @Author CJJ
 * @Version v 24
 * @Created 2014-10-30
 */
public class KnowledgeOfDetailActivity extends SwipeBackActivity implements OnTouchListener,OnGestureListener,
		IBindData {

	private final String TAG = getClass().getSimpleName();
	/** 控件 */
	public MyViewPager knowDetailContentVp;
	/** 变量 */
	private ArrayList<JBaseFragment> mKnowledgeBaseFragments;
	private KnowledgeDetailFragment mKnowledgeDetailFragment; // 知识详情页面
//	private KnowledegeCommentDetailsFragment mKnowledegeCommentDetailsFragment;// 对知识或评论发表评论页面
	private List<String> mListTitles = new ArrayList<String>();
	private MyViewPagerAdapter myViewPagerAdapter;
	private ArrayList<KnowledgeComment> newKnowCommentsList;
	private String formActivity;
	private long mKnowledgeId;
	private int mKnowledgeType;
	private Knowledge2 knowledge2;
	private String  requestActivity;
	private boolean isShowSave;
//	private JointResourceMainFragment mJointResource;
	private JoinFrg_chart frgChart;
	private boolean isFirstIn = true;
	private JBaseFragment jBaseFragment;
	@Override
	public void initJabActionBar() {
		getActionBar().hide();
		/*jabGetActionBar().setTitle("知识详情");*/
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setSwipeEnable(true);
		setContentView(R.layout.activity_knowledge_details);
		initComponent();
		ActivityCollector.addActivity(this);
	}

	private void initComponent() {
		knowDetailContentVp = (MyViewPager) findViewById(R.id.knowDetailContentVp);
		mKnowledgeBaseFragments = new ArrayList<JBaseFragment>();
		
//		mKnowledegeCommentDetailsFragment = new KnowledegeCommentDetailsFragment(
//				knowDetailContentVp,KnowledgeOfDetailActivity.this);//左滑改为对接
		
		mKnowledgeDetailFragment = new KnowledgeDetailFragment(
				knowDetailContentVp,KnowledgeOfDetailActivity.this);
		
//		mJointResource = new JointResourceMainFragment();
		onReservedInterface();
		Bundle bundle = new Bundle();
		bundle.putString(EConsts.Key.ID, mKnowledgeId+"");
		bundle.putInt(EConsts.Key.TYPE, 1);

		frgChart = new JoinFrg_chart();
		frgChart.setArguments(bundle);
		// 拿到跳转的数据(接口)
		initData(mKnowledgeId, mKnowledgeType);
		mListTitles.add("知识详情");
		mListTitles.add("资源对接");
		mKnowledgeBaseFragments.add(mKnowledgeDetailFragment);
		mKnowledgeBaseFragments.add(frgChart);
//		mKnowledgeBaseFragments.add(mJointResource);
		myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
		knowDetailContentVp.setAdapter(myViewPagerAdapter);
		knowDetailContentVp
				.setOnPageChangeListener(new MyOnPageChangeListener());
		knowDetailContentVp.setOnTouchListener(this);
	}
	/**
	 * 获取知识详情对象
	 */
	private void initData(long mKnowledgeId, int mKnowledgeType) {
		KnowledgeReqUtil.doGetKnoDetails(this, this, mKnowledgeId,
				mKnowledgeType, null);
	}
	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		

		@Override
		public void onPageSelected(int index) {
			jBaseFragment = mKnowledgeBaseFragments.get(index);
			switch (index) {
			case 0:
				getActionBar().hide();
				mKnowledgeDetailFragment.setCommentIcon();
				if(knowledge2 == null){
//					initData(mKnowledgeId,mKnowledgeType);
//					mJointResource.setJointResourceResourceBase(ResourceType.Knowledge,knowledge2.toKnowledgeMini2());
				}else{
//					initData(knowledge2.getId(),knowledge2.getType());
//					mJointResource.setJointResourceResourceBase(ResourceType.Knowledge,knowledge2.toKnowledgeMini2());
				}
				
//				mKnowledegeCommentDetailsFragment.popupWindowDismiss();
				/*getActionBar().hide();*/
				break;
			case 1:
				/*getActionBar().show();*/
//				KnowledgeOfDetailActivity.this.showLoadingDialog();
//				initCommentData();//更新评论和收藏数据数据
//				mKnowledgeDetailFragment.stopMultiMedia();
				if (isFirstIn) {
					isFirstIn = false;
					frgChart.getData();
				}
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * 初始化数据
	 */
	private void initCommentData() {
		KnowledgeReqUtil.doGetKnowledgeComment(KnowledgeOfDetailActivity.this,
				this, mKnowledgeId, 0L, 0, 20, null);
	}

	/**
	 * 处理跳转数据
	 */
	private void onReservedInterface() {
		Intent intent = getIntent();
		
		formActivity = intent.getStringExtra(EConsts.Key.FROM_ACTIVITY);
		mKnowledgeId = getIntent().getLongExtra(
				EConsts.Key.KNOWLEDGE_DETAIL_ID, 1L);
		mKnowledgeType = getIntent().getIntExtra(
				EConsts.Key.KNOWLEDGE_DETAIL_TYPE, 0);
		isShowSave = getIntent().getBooleanExtra(ENavConsts.KEY_FRG_FLOW_FORWARDING_KNOWLEDGE_TYPE,true);
		knowledge2 = (Knowledge2) getIntent().getSerializableExtra(
				EConsts.Key.KNOWLEDGE2);
		requestActivity = (String) getIntent().getStringExtra("requestActivity");
		
		
		Bundle knowDetailFrgBundle = new Bundle();
		
		
		knowDetailFrgBundle.putString(EConsts.Key.FROM_ACTIVITY,
				formActivity);
		knowDetailFrgBundle.putLong(EConsts.Key.KNOWLEDGE_DETAIL_ID,
				mKnowledgeId);
		knowDetailFrgBundle.putInt(EConsts.Key.KNOWLEDGE_DETAIL_TYPE,
				mKnowledgeType);
		knowDetailFrgBundle.putBoolean(ENavConsts.KEY_FRG_FLOW_FORWARDING_KNOWLEDGE_TYPE,
				isShowSave);
		knowDetailFrgBundle.putSerializable(EConsts.Key.KNOWLEDGE2, knowledge2);
		mKnowledgeDetailFragment.setArguments(knowDetailFrgBundle);
		if(knowledge2 == null){
			this.mKnowledgeId = mKnowledgeId;
		}else{
			this.mKnowledgeId = knowledge2.getId();
		}
	}

	class MyViewPagerAdapter extends FragmentPagerAdapter {

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return mKnowledgeBaseFragments.get(arg0);
		}

		@Override
		public int getCount() {
			return mKnowledgeBaseFragments.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}
	}

	/**
	 * 处理viewpager中item的返回事件
	 */
	public void onBackPressedFragment() {
		if (mKnowledgeBaseFragments.size() > 1) {
//			mKnowledegeCommentDetailsFragment = (KnowledegeCommentDetailsFragment) mKnowledgeBaseFragments
//					.get(knowDetailContentVp.getCurrentItem());// 获取回复评论frg对象
//			mKnowledegeCommentDetailsFragment.onBackPressed();
		}
	}

	@Override
	public void onBackPressed() {
//		if (myViewPagerAdapter != null
//				&& knowDetailContentVp.getCurrentItem() > 0
//				&& knowDetailContentVp.getCurrentItem() != 0) {
//			onBackPressedFragment();
////			initCommentData();//更新评论和收藏数据数据
//				 if(mJointResource.equals(jBaseFragment)) {  
//					 knowDetailContentVp.setCurrentItem(0);
//				    }  else{
//				    	super.onBackPressed();
//				    }
//				
//			return;
//		}
		
		if(requestActivity != null){
			if(requestActivity.equals("MyKnowledgeActivity")){
				Intent intent = new Intent();
				intent.putExtra(EConsts.Key.KNOWLEDGE_MINI2, knowledge2.toKnowledgeMini2());
				intent.putExtra(EConsts.Key.REQUEST_CODE, MyKnowledgeActivity.REQUESTCODE_CREATE_KNOWLEDGE_ACTIVITY);
				setResult(Activity.RESULT_OK, intent);
			}
		}
		
		super.onBackPressed();
	}

	@Override
	public void bindData(int tag, Object object) {
		int total = 0;
		if (KnoReqType.GetKnowledgeComment == tag) {// 获取知识评论
			if (object != null) {
				if (newKnowCommentsList != null) {
					newKnowCommentsList.clear();// 如果当前刷新中，则清除之前数据
				}
				Map<String, Object> hm = (Map<String, Object>) object;
				newKnowCommentsList = (ArrayList<KnowledgeComment>) hm
						.get("listKnowledgeComment");
				total = (Integer) hm.get("total");
				mKnowledgeDetailFragment.setNewKnowCommentsList(newKnowCommentsList, total, mKnowledgeId);
			}
//			mKnowledegeCommentDetailsFragment.setNewKnowCommentsList(newKnowCommentsList,total,mKnowledgeId);
			KnowledgeOfDetailActivity.this.dismissLoadingDialog();
		}else if (tag == EAPIConsts.KnoReqType.GetKnoDetails) {
			// 获取知识详情列表
			Map<String, Object> dataHm = (Map<String, Object>) object;
			if(dataHm == null){
			  return;
			}
			knowledge2 = (Knowledge2) dataHm.get("knowledge2");
//			mJointResource.setJointResourceResourceBase(ResourceType.Knowledge,knowledge2.toKnowledgeMini2());
			mKnowledgeDetailFragment.setmKnowledge2(knowledge2);
			KnowledgeOfDetailActivity.this.showLoadingDialog();
			mKnowledgeDetailFragment.initKnowledgeDetailsData();
//			updateKnowledgeDetailUI();
			initCommentData();
		}
	}

	private void updateKnowledgeDetailUI() {
		 
		KnowledgeStatics mKnowStatics =knowledge2.getKnowledgeStatics();
		/*知识评论数*/
		if (null != mKnowStatics && mKnowStatics.getCommentcount() > 0) {
			mKnowledgeDetailFragment.setCommentNum(mKnowStatics.getCommentcount() + "");
		} else {
			mKnowledgeDetailFragment.setCommentNum("");
		}
		/*知识收藏数*/
		if (null != mKnowStatics && mKnowStatics.getCollectioncount() > 0) {
			mKnowledgeDetailFragment.setCollectionNum(mKnowStatics.getCollectioncount() + "");
		} else {
			mKnowledgeDetailFragment.setCollectionNum("");
		}
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == android.R.id.home){
			if(knowDetailContentVp.getCurrentItem() ==1 ){
				knowDetailContentVp.setCurrentItem(0);
			}
		}
		return true;
	}
	
	@Override  
    public boolean onTouch(View v, MotionEvent event) {  
        return false;
    }  

	@Override
	public boolean onDown(MotionEvent e) {
		String MSG = "onDown()";
		Log.i(TAG, MSG);
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		String MSG = "onFling()";

//		if (knowDetailContentVp.getCurrentItem() == 0  && (e2.getX() - e1.getX() > 200)) {
//			// 显示上一个页面
////			finish();
//			Toast.makeText(getApplicationContext(), "显示上一个页面", 0).show();
//			return false;
//		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		String MSG = "onLongPress()";
		Log.i(TAG, MSG);
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		String MSG = "onScroll()";
		Log.i(TAG, MSG);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		String MSG = "onShowPress()";
		Log.i(TAG, MSG);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		String MSG = "onSingleTapUp()";
		Log.i(TAG, MSG);
		return false;
	}

}
