package com.tr.ui.home;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.db.ConnectionsDBManager;
import com.tr.model.obj.Connections;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.frg.HomePageFrag;
import com.tr.ui.home.frg.JoinFrg_chart;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.widgets.CustomViewPager;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.utils.common.ContactsDetailsPageUtils;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * @ClassName: HomePageActivity
 * @Description: 个人/他人主页界面
 * @author cui
 * @date 2015-11-30 上午10:16:51
 * 
 */
public class HomePageActivity extends JBaseFragmentActivity {
	/** 用户id */
	private String userId;
	/** 用来区分当前人的类型 */
	private int type = 0;
	/** 线上：用户or线下：人脉 */
	private boolean isonline;
	private TitlePopup titlePopup;
	// 角色 com.utils.common.GlobalVariable.HomePageInformation
	private String roleString;
	private View top_line;

	/** ViewPager容器 */
	private CustomViewPager mPager;
	/** ViewPager容器中的Fragment */
	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	private HomeFrgPagerAdapter homeFrgPagerAdapter;

	private JoinFrg_chart frgChart;
	private boolean isFirstIn = true;
	public boolean is_good_friend = false;
	public PeopleDetails peopleDetails = new PeopleDetails();
	public ContactsDetailsPageUtils mContactsDetailsPageUtils = new ContactsDetailsPageUtils();
	@Override
	public void initJabActionBar() {
		getBundle();
		setContentView(R.layout.activity_home_page);
		// ViewUtils.inject(this); // 注入view和事件..
		initView();
		InitViewPager();
		initPopWindow();
	}

	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		initContactsDetailsPageUtils();
	}

	/**
	 * 初始化更多弹出框
	 */
	private void initContactsDetailsPageUtils() {
		mContactsDetailsPageUtils.mConnext = HomePageActivity.this;
		mContactsDetailsPageUtils.mActivity = HomePageActivity.this;
		mContactsDetailsPageUtils.userId = userId;
		mContactsDetailsPageUtils.titlePopup = titlePopup;
		mContactsDetailsPageUtils.mIBindData = mIBindData;
		mContactsDetailsPageUtils.type = type;
		mContactsDetailsPageUtils.peopleDetails = peopleDetails;
		mContactsDetailsPageUtils.is_good_friend = is_good_friend;
		mContactsDetailsPageUtils.showDetailsPageUtil();
	}


	private void getBundle() {
		// TODO Auto-generated method stub
		userId = getIntent().getStringExtra(EConsts.Key.ID);
		is_good_friend = getIntent().getBooleanExtra(
				EConsts.Key.IS_GOOD_FRIEND, true);
		roleString = getIntent().getStringExtra(EConsts.Key.HOME_PAGE_ROLE);


		type = getIntent().getIntExtra(ENavConsts.EFromActivityType,
				ENavConsts.type_details_other);
		isonline = getIntent().getBooleanExtra(EConsts.Key.isOnline, false); // online
		if (StringUtils.isEmpty(userId)) {
			userId = App.getUserID();
			type = 1;//用户
		}
		if (App.getUserID().equals(userId)) {// 用户自己
			HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(),
					"我的主页", false, null, false, true);
		} else if (is_good_friend && type == 1) {//好友用户
			HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(),
					"我的好友", false, null, false, true);
		}else if (is_good_friend && type == 2) {//好友人脉
			HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(),
					"我的人脉", false, null, false, true);
		} else {
			HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(),
					"人脉详情", false, null, false, true);
		}
	}

	private void initView() {
		top_line = findViewById(R.id.view_top_line);
		Bundle bundle = new Bundle();
		bundle.putString(EConsts.Key.ID, userId);

		frgChart = new JoinFrg_chart();
		frgChart.setArguments(bundle);

		HomePageFrag homePageFrag = new HomePageFrag(HomePageActivity.this);
		bundle.putInt(ENavConsts.EFromActivityType, type);
		bundle.putBoolean(EConsts.Key.isOnline, isonline);
		bundle.putString(EConsts.Key.HOME_PAGE_ROLE, roleString);
		homePageFrag.setData(bundle);

		mFragments.add(homePageFrag);
		mFragments.add(frgChart);
	}

	/** 初始化ViewPager */
	private void InitViewPager() {
		mPager = (CustomViewPager) findViewById(R.id.homeVPager);
		// mPager.setPagingEnabled(false);// 禁止viewpager滑动
		mPager.setOffscreenPageLimit(2); // 状态表示
		homeFrgPagerAdapter = new HomeFrgPagerAdapter(
				getSupportFragmentManager(), mFragments);
		mPager.setAdapter(homeFrgPagerAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/** 页卡切换监听 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
			if (index == 1) {
				if (isFirstIn) {
					isFirstIn = false;
					frgChart.getData();
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_more).setIcon(
				R.drawable.ic_action_overflow);
		// if(!App.getUserID().equals(userId)){//他人主页
		menu.findItem(R.id.home_new_menu_more).setVisible(true);
		// }
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.home_new_menu_more:
			titlePopup.show(top_line);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (null != intent) {
			HomePageFrag homePageFrag = (HomePageFrag) getSupportFragmentManager()
					.getFragments().get(0);
			homePageFrag.handerRequsetCode(requestCode, intent);
		}
		super.onActivityResult(requestCode, resultCode, intent);
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

		public ArrayList<Fragment> getmFragments() {
			return mFragments;
		}

		public void setmFragments(ArrayList<Fragment> mFragments) {
			this.mFragments = mFragments;
		}

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

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}

	}

	IBindData mIBindData = new IBindData() {

		@Override
		public void bindData(int tag, Object object) {
			// 隐藏加载框
			dismissLoadingDialog();
			if (tag == EAPIConsts.concReqType.im_deleteFriend) { // 删除好友
				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						ConnectionsDBManager.getInstance(HomePageActivity.this)
								.delete(userId, Connections.type_persion, true);
						showToast("删除成功");
						Intent intent = new Intent();
						intent.putExtra("IsChange", true);
						setResult(RESULT_OK, intent);
						finish();
					}
				} else {
					showToast("删除失败");
				}
			}
			if(tag == PeopleRequestType.COLLECT_PEOPLE){//收藏人脉
				Boolean isSuccess = (Boolean) object;

				if (isSuccess != null) {

					if (isSuccess) {
						Toast.makeText(HomePageActivity.this, "收藏成功", 1)
								.show();
						mContactsDetailsPageUtils.is_collect_people = true;
						initContactsDetailsPageUtils();
					}
				}
			}
			if(tag == PeopleRequestType.CANCEL_COLLECT){//取消收藏人脉
				Boolean Success = (Boolean) object;
				if (Success != null) {

					if (Success) {
						Toast.makeText(HomePageActivity.this, "取消收藏成功", 1)
								.show();
						mContactsDetailsPageUtils.is_collect_people = false;
						initContactsDetailsPageUtils();
					}
				}
			}
		}
	};
	
	public void setCurrentPage(int index){
		mPager.setCurrentItem(index);
	}

}
