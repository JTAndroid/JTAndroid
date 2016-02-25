package com.tr.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.home.MCheckFriend;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
//import com.baidu.navisdk.util.common.StringUtils;

/**
 * @author 孙嘉楠
 * @description 通过二维码调用本页面
 */
public class InviteFriendByQRCodeActivity extends JBaseActivity implements OnClickListener, IBindData {

	@ViewInject(R.id.avatar)
	private ImageView avatar;
	@ViewInject(R.id.my_name)
	private TextView myName;
	@ViewInject(R.id.my_counter)
	private TextView myCounter;
	private Button status;
	private int flag = 0;
	private MCheckFriend mCheckFriend;
	private String friendId;
	private String OrgId;
	private LinearLayout myBaseInfoRl;

	/** 0：人脉好友 1. 组织客户 */
	private int type;
	private String addfriendId;

	public static int PeopleFriend = 0;
	public static int OrgFriend = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_friend);
		ViewUtils.inject(this);
		initControl();
		setListener();
		friendId = getIntent().getStringExtra("friendId");
		type = getIntent().getIntExtra("type", 0);
		OrgId = getIntent().getStringExtra("OrgId");// 只有组织才用到组织id
		if (!TextUtils.isEmpty(OrgId)) {
			addfriendId = OrgId;
		}
		if (!TextUtils.isEmpty(friendId)) {
			addfriendId = friendId;
		}
		CommonReqUtil.doGetCheckFriend(this, this, addfriendId, type, null);
	}

	private void setListener() {
		status.setOnClickListener(this);
		myBaseInfoRl.setOnClickListener(this);
	}

	private void initControl() {
		status = (Button) findViewById(R.id.add_friend_status);
		myBaseInfoRl = (LinearLayout) findViewById(R.id.my_base_info);
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "详细资料", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_friend_status:
			// 加好友
			if (flag == 0) {
				// Toast.makeText(InviteFriendByQRCodeActivity.this, "加好友",
				// 0).show();
				if (type == OrgFriend) {
					UserReqUtil.doAddFriend(InviteFriendByQRCodeActivity.this, this, UserReqUtil.getDoAddFriendParams(type, mCheckFriend.getId() + ""), null);
				} else {
					UserReqUtil.doAddFriend(InviteFriendByQRCodeActivity.this, this, UserReqUtil.getDoAddFriendParams(type, friendId), null);

				}
			}
			// 等待确认
			if (flag == 1) {
				// Toast.makeText(InviteFriendByQRCodeActivity.this,
				// "加好友",0).show();
			}
			// TA的主页
			if (flag == 2 || flag == 3) {
				if (OrgId == null) {
					ENavigate.startRelationHomeActivity(InviteFriendByQRCodeActivity.this, friendId, true, ENavConsts.type_details_member);
				} else {
					ENavigate.startOrgMyHomePageActivity(InviteFriendByQRCodeActivity.this, Long.valueOf(OrgId), Long.valueOf(friendId), true,
							ENavConsts.type_details_org);
				}
			}
			break;
		case R.id.my_base_info:
			if (mCheckFriend == null || !mCheckFriend.isSuccess()) {
				return;
			}

			if (OrgId == null) {
				// 组织客户
				if (mCheckFriend.isVirtual()) {
					ENavigate.startOrgMyHomePageActivityByOrgId(InviteFriendByQRCodeActivity.this, mCheckFriend.getCustomerId());
				}
				// 人脉好友
				else {
					ENavigate.startRelationHomeActivity(InviteFriendByQRCodeActivity.this, friendId ,true, ENavConsts.type_details_member);
				}
			} else {
				ENavigate.startOrgMyHomePageActivity(InviteFriendByQRCodeActivity.this, Long.valueOf(OrgId), 0L, true,
						ENavConsts.type_details_org);
			}
			break;
		}
	}

	/**
	 * @param flag
	 *            0:加好友 1：等待确认 2：TA的主页
	 */
	private void changeStatus(int flag) {
		this.flag = flag;
		// 加好友
		if (flag == 0) {
			// 仅好友可见
			if (mCheckFriend.getHomePageVisible() == 1) {
				myBaseInfoRl.setClickable(false);
			}
			status.setClickable(true);
			status.setBackgroundResource(R.drawable.sign_in);
			status.setText("加为好友");
		}
		// 等待确认
		if (flag == 1) {
			if (mCheckFriend.getHomePageVisible() == 1) {
				myBaseInfoRl.setClickable(false);
			}
			status.setClickable(false);
			status.setBackgroundResource(R.drawable.sign_in_normal);
			status.setText("等待确认");
		}
		// TA的主页
		if (flag == 2) {
			status.setClickable(true);
			status.setBackgroundResource(R.drawable.sign_in);
			status.setText("TA的主页");
		}
		if (flag == 3) {
			status.setClickable(true);
			status.setBackgroundResource(R.drawable.sign_in);
			status.setText("我的主页");
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.CommonReqType.CHECK_FRIEND) {
			if (object != null) {
				mCheckFriend = (MCheckFriend) object;
				if (mCheckFriend.isSuccess()) {
					init(mCheckFriend);
				} else {
					status.setVisibility(View.GONE);
					Toast.makeText(InviteFriendByQRCodeActivity.this, "你没有查看权限，不能加对方为好友", 0).show();
				}
				initData(mCheckFriend);
			} else {
				status.setVisibility(View.GONE);
			}
		}
		if (tag == EAPIConsts.ReqType.ADD_FRIEND && object != null) {
			DataBox result = (DataBox) object;
			// 改变状态
			changeStatus(1);
			Toast.makeText(InviteFriendByQRCodeActivity.this, "添加成功，请等待通过审核", 0).show();
		}
	}

	private void init(MCheckFriend mCheckFriend) {
		if (friendId != null && friendId.equals(App.getUserID())) {
			changeStatus(3);
		} else {
			changeStatus(mCheckFriend.getStatus());
		}
	}

	// 初始化数据
	private void initData(MCheckFriend mCheckFriend) {
		// 组织客户
		if (mCheckFriend.isVirtual()) {
			Util.initAvatarImage(getApplicationContext(), avatar, "", mCheckFriend.getPicPath(), 0, 2);
		}
		// 人脉
		else {
			Util.initAvatarImage(getApplicationContext(), avatar, "", mCheckFriend.getPicPath(), 0, 1);
		}
		if (!StringUtils.isEmpty(mCheckFriend.getName())) {
			myName.setText(mCheckFriend.getName());
		}
		if (!StringUtils.isEmpty(mCheckFriend.getCompany())) {
			myCounter.setVisibility(View.VISIBLE);
			myCounter.setText(mCheckFriend.getCompany());
		} else if (!StringUtils.isEmpty(mCheckFriend.getCareer())) {
			myCounter.setVisibility(View.VISIBLE);
			myCounter.setText(" " + mCheckFriend.getCareer());
		}else {
			myCounter.setVisibility(View.GONE);
		}
	}

}
