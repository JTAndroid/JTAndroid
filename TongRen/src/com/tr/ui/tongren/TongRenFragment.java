package com.tr.ui.tongren;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.home.MainActivity;
import com.tr.ui.home.frg.FrgFlow.FlowSelectType;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.adapter.TongRenFrgPagerAdapter;
import com.tr.ui.widgets.CustomViewPager;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.tr.ui.work.WorkMainFragment;
import com.utils.common.EUtil;
import com.utils.display.DisplayUtil;

public class TongRenFragment extends JBaseFragment implements 
		OnPageChangeListener {
	private CustomViewPager tongRenVPager;
	private ArrayList<Fragment> tongRenfragments = new ArrayList<Fragment>();
	private View affRemind;
	private ImageView bottom_aff_red_dot;
	/** 当前桐人位于第几页 */
	private CurrentTongRenFrgTitle currentTongRenFrgTitle = CurrentTongRenFrgTitle.first;
	private int selectColor = App.getApp().getResources()
			.getColor(R.color.actionbar_title_textcolor);
	private MenuItem calendar;
	private MenuItem list;
	private WorkMainFragment workMainFragment;
	private int flag = 0;
	private MenuItem create;
	private MenuItem msg;
	private MenuItem more;
	private int mTpye;
	public final static int REQ_ORG = 200;
	public final static int REQ_PROJECT = 1000;
	private OrganizationFragment organizationFragment;
	private ProjectFragment projectFragment;
	private TitlePopup titlePopup;
	private View affair_top_line;
	/** first：桐人第一页（事务）；second：桐人第二页（项目）；third：桐人第三页（组织） */
	public enum CurrentTongRenFrgTitle {
		first, second, third
	}

	public TongRenFragment() {
		super();
	}

	/** MainActivityt 传过来的事务提醒的控件 */
	public TongRenFragment(View view) {
		super();
		this.affRemind = view;

		bottom_aff_red_dot = (ImageView) affRemind
				.findViewById(R.id.bottom_aff_red_dot);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View tongren = inflater.inflate(R.layout.fragment_tongren, null);
		setHasOptionsMenu(true);

		return tongren;
	}

	public CurrentTongRenFrgTitle getCurrentTongRenFrgTitle() {
		return currentTongRenFrgTitle;
	}

	public void setCurrentTongRenFrgTitle(
			CurrentTongRenFrgTitle currentTongRenFrgTitle) {
		this.currentTongRenFrgTitle = currentTongRenFrgTitle;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		affair_top_line = view.findViewById(R.id.affair_top_line);
		tongRenVPager = (CustomViewPager) view.findViewById(R.id.tongRenVPager);
		workMainFragment = new WorkMainFragment(affRemind);
		tongRenfragments.add(workMainFragment);
		projectFragment = new ProjectFragment();
		tongRenfragments.add(projectFragment);
		organizationFragment = new OrganizationFragment();
		tongRenfragments.add(organizationFragment);
		TongRenFrgPagerAdapter tongRenFrgPagerAdapter = new TongRenFrgPagerAdapter(
				getChildFragmentManager(), tongRenfragments);
		tongRenVPager.setAdapter(tongRenFrgPagerAdapter);
		tongRenVPager.setOffscreenPageLimit(0);
		tongRenVPager.setOnPageChangeListener(this);
		tongRenVPager.setCurrentItem(0);
		
		titlePopup = new TitlePopup(getActivity(), LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopup.addAction(new ActionItem(getActivity(), "全部消息已读"));
		titlePopup.setItemOnClickListener(onPopupItemClick);
	}
	private OnPopuItemOnClickListener onPopupItemClick = new OnPopuItemOnClickListener(){

		@Override
		public void onItemClick(ActionItem item, int position) {
			// TODO Auto-generated method stub
			switch (position) {
			case 0://全部消息已读
				Toast.makeText(getActivity(), "全部消息已读", Toast.LENGTH_SHORT).show();
//				affRemind.setVisibility(View.GONE);
				workMainFragment.setAllRedGone();
				break;

			default:
				break;
			}
		}
		
	};
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.findItem(R.id.home_new_menu_more).setVisible(false);
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		inflater.inflate(R.menu.tongren, menu);
		create = menu.findItem(R.id.aff_create);
		calendar = menu.findItem(R.id.affairs_new_menu_calendar);
		list = menu.findItem(R.id.affairs_new_menu_list);
		msg = menu.findItem(R.id.tongren_new_menu_msg);
		more = menu.findItem(R.id.affair_menu_more);
	}

	// **
	// 设置菜单的显示与否
	//
	@Override
	public void onPrepareOptionsMenu(Menu menu) {

		if (mTpye == 1||mTpye==2) {
			create.setVisible(false);
			calendar.setVisible(false);
			more.setVisible(false);
			list.setVisible(false);
			msg.setVisible(true);
		} else {
			if (flag % 2 == 0) {
				more.setVisible(true);
				calendar.setVisible(false);
				list.setVisible(true);
				msg.setVisible(false);
				create.setVisible(false);
			} else {
				more.setVisible(false);
				calendar.setVisible(true);
				list.setVisible(false);
				msg.setVisible(false);
				create.setVisible(false);
			}
			
		}
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// 列表样式按钮
		case R.id.affairs_new_menu_calendar:
		case R.id.affairs_new_menu_list:
			getActivity().getWindow().invalidatePanelMenu(
					Window.FEATURE_OPTIONS_PANEL);
			workMainFragment.setShowType(1);
			flag++;

			break;
		case R.id.aff_create:
			ENavigate.startNewAffarActivity(getActivity());
			break;
		case R.id.tongren_new_menu_msg:
			Intent intent = new Intent(getActivity(), TongRenMessageActivity.class);
			if (mTpye==0) {
				getActivity().startActivity(intent);
			}else if(mTpye == 1){
				getActivity().startActivityForResult(intent, TongRenFragment.REQ_PROJECT);
			}else if(mTpye == 2){
				getActivity().startActivityForResult(intent, TongRenFragment.REQ_ORG);
			}
			
			break;
		case R.id.affair_menu_more://事务actionBar更多按钮
			titlePopup.show(affair_top_line);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mainpage:
				HomeCommonUtils
						.startTranslateAnimation(CurrentTongRenFrgTitle.first);
				changeFlowType(HomeCommonUtils.mainpageTv,
						CurrentTongRenFrgTitle.first);
				tongRenVPager.setCurrentItem(0);
				create.setVisible(false);

				break;
			case R.id.flowpage:
				HomeCommonUtils
						.startTranslateAnimation(CurrentTongRenFrgTitle.second);
				changeFlowType(HomeCommonUtils.flowpageTv,
						CurrentTongRenFrgTitle.second);
				tongRenVPager.setCurrentItem(1);
				break;
			case R.id.gintongpage:
				HomeCommonUtils
						.startTranslateAnimation(CurrentTongRenFrgTitle.third);
				changeFlowType(HomeCommonUtils.gintongpageTv,
						CurrentTongRenFrgTitle.third);
				tongRenVPager.setCurrentItem(2);

				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 选择当前为（first事务，second项目，third组织）
	 * 
	 * @param textView
	 *            切换到相应页面的顶部TextView控件
	 * @param currentFlowFrgTitle
	 *            记录当前页类型（first，second，third）
	 * @param view
	 *            文字控件下面的横线
	 * @param flowSelectType
	 *            改变相应的动态页面的类型
	 */
	private void changeFlowType(TextView textView,
			CurrentTongRenFrgTitle CurrentTongRenFrgTitle) {
		HomeCommonUtils.resetTextSize();
		textView.setTextSize(DisplayUtil.sp2px(16, 1));
		this.currentTongRenFrgTitle = CurrentTongRenFrgTitle;
		textView.setTextColor(selectColor);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			mTpye = 0;
			currentTongRenFrgTitle = CurrentTongRenFrgTitle.first;
			HomeCommonUtils
					.startTranslateAnimation(CurrentTongRenFrgTitle.first);
			changeFlowType(HomeCommonUtils.mainpageTv,
					CurrentTongRenFrgTitle.first);
			HomeCommonUtils.initHorizontalCustomActionBar(getActivity(),
					getActivity().getActionBar(), onClickListener, currentTongRenFrgTitle);
			break;
		case 1:
			mTpye = 1;
			currentTongRenFrgTitle = CurrentTongRenFrgTitle.second;
			HomeCommonUtils
					.startTranslateAnimation(CurrentTongRenFrgTitle.second);
			changeFlowType(HomeCommonUtils.flowpageTv,
					CurrentTongRenFrgTitle.second);
			HomeCommonUtils.initHorizontalCustomActionBar(getActivity(),
					getActivity().getActionBar(), onClickListener, currentTongRenFrgTitle);
			break;
		case 2:
			mTpye = 2;
			currentTongRenFrgTitle = CurrentTongRenFrgTitle.third;
			HomeCommonUtils
					.startTranslateAnimation(CurrentTongRenFrgTitle.third);
			changeFlowType(HomeCommonUtils.gintongpageTv,
					CurrentTongRenFrgTitle.third);
			HomeCommonUtils.initHorizontalCustomActionBar(getActivity(),
					getActivity().getActionBar(), onClickListener, currentTongRenFrgTitle);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TongRenFragment.REQ_ORG) {
			organizationFragment
					.onActivityResult(requestCode, resultCode, data);
		} else if (requestCode == TongRenFragment.REQ_PROJECT) {
			projectFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
}
