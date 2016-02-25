package com.tr.ui.home;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.FragmentTabAdapter.OnRgsExtraCheckedChangedListener;
import com.tr.ui.home.frg.ContactsMainFragment;
import com.tr.ui.home.frg.FrgSociality;
import com.tr.ui.home.frg.GintongMainFragment;
import com.tr.ui.widgets.SlidingMenu;
/**
 * 新版首页（仿QQ效果）
 * @author zhongshan
 *
 */
public class MainDrawerActivity extends JBaseFragmentActivity implements OnRgsExtraCheckedChangedListener, OnClickListener{
	private SlidingMenu mMenu;
	private RelativeLayout mainContentLayout;
	private FrameLayout mainHeaderLayout;
	private RadioButton gintongMainRb;
	private RadioButton messageMainRb;
	private RadioButton contactsMainRb;
	private GintongMainFragment gintongMainFragment;
	private FrgSociality frgSociality;
	private ContactsMainFragment contactsMainFragment;
	private RadioButton[] mTabs;
	private List<Fragment> list;
	private ImageView myProFileIv;
	private TextView mainTitleTv;
	private TextView rightMenuTv;
	private ImageView searchIv;
	private ImageView moreIv;

	@Override
	public void initJabActionBar() {
		getActionBar().hide();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer_main);
		initView();
		setListener();
		initControl();
	}
	
	private void initView() {
		mMenu = (SlidingMenu) findViewById(R.id.id_menu);
		mainContentLayout = (RelativeLayout) findViewById(R.id.mainContentLayout);
		mainHeaderLayout = (FrameLayout) findViewById(R.id.mainHeaderLayout);
		myProFileIv = (ImageView) mainHeaderLayout.findViewById(R.id.myProFileIv);
		mainTitleTv = (TextView) mainHeaderLayout.findViewById(R.id.mainTitleTv);
		rightMenuTv = (TextView) mainHeaderLayout.findViewById(R.id.rightMenuTv);
		searchIv = (ImageView) mainHeaderLayout.findViewById(R.id.searchIv);
		moreIv = (ImageView) mainHeaderLayout.findViewById(R.id.moreIv);
		gintongMainRb = (RadioButton) mainContentLayout.findViewById(R.id.gintongMainRb);
		messageMainRb = (RadioButton) mainContentLayout.findViewById(R.id.messageMainRb);
		contactsMainRb = (RadioButton) mainContentLayout.findViewById(R.id.contactsMainRb);
		mTabs = new RadioButton[3];
		mTabs[0] = gintongMainRb;
		mTabs[1] = messageMainRb;
		mTabs[2] = contactsMainRb;
		// 把第一个tab设为选中状态
		mTabs[0].setChecked(true);
	}

	private void setListener() {
		myProFileIv.setOnClickListener(this);
		rightMenuTv.setOnClickListener(this);
		searchIv.setOnClickListener(this);
		moreIv.setOnClickListener(this);
	}
	private void initControl() {
		initFragment();
	}
	
	private void initFragment() {
		gintongMainFragment = new GintongMainFragment();
		frgSociality = new FrgSociality(); 
		contactsMainFragment = new ContactsMainFragment();
		list = new ArrayList<Fragment>();
		list.add(gintongMainFragment);
		list.add(frgSociality);
		list.add(contactsMainFragment);
		FragmentTabAdapter fragmentTabAdapter = new FragmentTabAdapter(this, list, R.id.mainContentFl, mTabs);
		fragmentTabAdapter.setOnRgsExtraCheckedChangedListener(this);
	}
	@Override
	public void OnRgsExtraCheckedChanged(Button[] btns, int checkedId, int index) {
		switch (index) {
		case 0:
			mainTitleTv.setText("");
			rightMenuTv.setVisibility(View.GONE);
			searchIv.setVisibility(View.VISIBLE);
			moreIv.setVisibility(View.VISIBLE);
			break;
		case 1:
			mainTitleTv.setText("消息");
			rightMenuTv.setVisibility(View.VISIBLE);
			rightMenuTv.setText("发起畅聊");
			searchIv.setVisibility(View.GONE);
			moreIv.setVisibility(View.GONE);
			break;
		case 2:
			mainTitleTv.setText("通讯录");
			rightMenuTv.setVisibility(View.VISIBLE);
			rightMenuTv.setText("添加朋友");
			searchIv.setVisibility(View.GONE);
			moreIv.setVisibility(View.GONE);
			break;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myProFileIv:
			mMenu.toggle();
			break;
		case R.id.rightMenuTv:
			
			break;
		case R.id.searchIv:
			
			break;
		case R.id.moreIv:
			
			break;
		}
	}
}
