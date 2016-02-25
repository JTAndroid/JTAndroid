package com.tr.ui.communities.frag;

import java.util.HashMap;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.communities.model.CreatePrecondition;
import com.tr.ui.home.HomePageActivity;
import com.tr.ui.relationship.SIMContactActivity;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class FragOnCreatCommunity extends JBaseFragment implements OnClickListener, IBindData {
	@ViewInject(R.id.tag_open_address_book)
	private TextView tag_open_address_book;// 开启手机通讯录
	@ViewInject(R.id.item_open_address_book)
	private RelativeLayout item_open_address_book;// 开启手机通讯录item

	@ViewInject(R.id.tag_full_three_days)
	private TextView tag_full_three_days;// 注册满3天
	@ViewInject(R.id.item_full_three_days)
	private RelativeLayout item_full_three_days;// 注册满3天item
	@ViewInject(R.id.tag_data_not_enough_iv)
	private ImageView tag_data_not_enough_iv;
	@ViewInject(R.id.tag_full_three_days_iv)
	private ImageView tag_full_three_days_iv;
	@ViewInject(R.id.tag_open_address_book_iv)
	private ImageView tag_open_address_book_iv;

	@ViewInject(R.id.tag_data_not_enough)
	private TextView tag_data_not_enough;// 资料不完善
	@ViewInject(R.id.item_data_not_enough)
	private RelativeLayout item_data_not_enough;// 资料不完善item

	@ViewInject(R.id.text_oncreat)
	private TextView text_oncreat;// 开始 创建
	
	@ViewInject(R.id.data_layout)
	private LinearLayout data_layout;// 数据布局容器
	
	private CreatePrecondition createPrecondition;
	private String noticeOpen;
	private String noticeReg;
	private String noticeUserInfo;
	private boolean READ_CONTACTS = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getBundle();
		super.onCreate(savedInstanceState);
	}

	/**
	 * 获取传过来的数据
	 */
	private void getBundle() {
		// TODO 获取传过来的数据

	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_oncreat_community, null);
		ViewUtils.inject(this, view); // 注入view和事件..
		// initView();
		return view;
	}

	private void checkPermisiion() {
		// TODO Auto-generated method stub
		try {
			Cursor cur = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
			READ_CONTACTS = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			// showToast("请开启");
		}

	}

	@Override
	public void onResume() {
		getData();
		super.onResume();
	}

	private void getData() {
		showLoadingDialog();
		CommunityReqUtil.doGetPrecondition(getActivity(), Long.parseLong(String.valueOf(App.getApp().getUserID())), 1, this, null);
	}

	private void initView() {
		item_open_address_book.setOnClickListener(this);// 开始手机通讯录

		item_data_not_enough.setOnClickListener(this);// 点击完善个人主页页面

		text_oncreat.setOnClickListener(this);// 开始创建
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_open_address_book:// 开始手机通讯录
			ENavigate.startSIMContactActivity(getActivity(), SIMContactActivity.TYPE_INVITE);
			break;
		case R.id.item_data_not_enough:// 点击完善个人主页页面
			startActivity(new Intent(getActivity(), HomePageActivity.class));
			break;
		case R.id.text_oncreat:// 开始创建
			if (null != createPrecondition) {
				if (READ_CONTACTS && createPrecondition.getRegFlag() && createPrecondition.getUserInfoFlag()) {
					FragStartCreatCommunity fragment = new FragStartCreatCommunity();
					Bundle bundle = new Bundle();
					fragment.setArguments(bundle);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_oncreat_community, fragment, "startCreate").commitAllowingStateLoss();
				} else {
					String toast = "";
					if (!TextUtils.isEmpty(noticeOpen))
						toast = noticeOpen;
					else if (!TextUtils.isEmpty(noticeReg))
						toast = noticeReg;
					else if (!TextUtils.isEmpty(noticeUserInfo))
						toast = noticeUserInfo;
					showToast(toast);
				}
			}

			break;

		default:
			break;
		}
	}

	private void reFreshUI() {
		if (READ_CONTACTS) {// 满足条件时隐藏
			tag_open_address_book.setText("开启手机通讯录");
//			tag_open_address_book.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_circle_check_pressed), null, null, null);
			tag_open_address_book_iv.setImageResource(R.drawable.icon_circle_check_pressed);
			tag_open_address_book.setTextColor(getResources().getColor(R.color.text_flow_content));
			noticeOpen = "";
		} else {
//			tag_open_address_book.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.chat_re), null, null, null);
			tag_open_address_book.setTextColor(getResources().getColor(R.color.btn_click));
			tag_open_address_book_iv.setImageResource(R.drawable.chat_re);
			noticeOpen = "开启手机通讯录";
		}
		if (null != createPrecondition.getRegFlag() && createPrecondition.getRegFlag()) {
			tag_full_three_days.setText("注册时间满3天");
//			tag_full_three_days.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_circle_check_pressed), null, null, null);
			tag_full_three_days_iv.setImageResource(R.drawable.icon_circle_check_pressed);
			tag_full_three_days.setTextColor(getResources().getColor(R.color.text_flow_content));
			noticeReg = "";
		} else {
//			tag_full_three_days.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.chat_re), null, null, null);
			tag_full_three_days.setTextColor(getResources().getColor(R.color.btn_click));
			tag_full_three_days_iv.setImageResource(R.drawable.chat_re);
			tag_full_three_days.setText("注册时间不满3天");
			noticeReg = "注册时间不满3天";
		}
		if (null != createPrecondition.getUserInfoFlag() && createPrecondition.getUserInfoFlag()) {
			tag_data_not_enough.setText("个人资料完整");
//			tag_data_not_enough.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_circle_check_pressed), null, null, null);
			tag_data_not_enough_iv.setImageResource(R.drawable.icon_circle_check_pressed);
			tag_data_not_enough.setTextColor(getResources().getColor(R.color.text_flow_content));
			noticeUserInfo = "";
		} else {
//			tag_data_not_enough.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.chat_re), null, null, null);
			tag_data_not_enough_iv.setImageResource(R.drawable.chat_re);
			tag_data_not_enough.setTextColor(getResources().getColor(R.color.btn_click));
			item_data_not_enough.setVisibility(View.VISIBLE);// 满足条件时隐藏
			tag_data_not_enough.setText("个人资料度不够");
			noticeUserInfo = "个人资料度不够";
		}

		if (READ_CONTACTS && createPrecondition.getRegFlag() && createPrecondition.getUserInfoFlag()) {
			text_oncreat.setBackgroundResource(R.drawable.sign_in);
//			text_oncreat.setBackgroundColor(R.color.button_orange_bg);
		} 
//		else {
//			text_oncreat.setBackgroundResource(R.drawable.sign_in_normal);
//			text_oncreat.setBackgroundColor(R.color.button_gray_bg);
//		}
	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case EAPIConsts.CommunityReqType.TYPE_GET_PRECONDITION:
			if (null != object) {
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				createPrecondition = (CreatePrecondition) dataMap.get("result");
				checkPermisiion();
				reFreshUI();
				initView();
				data_layout.setVisibility(View.VISIBLE);
			}else
				data_layout.setVisibility(View.GONE);
				
			dismissLoadingDialog();
			break;

		default:
			break;
		}
	}
}
