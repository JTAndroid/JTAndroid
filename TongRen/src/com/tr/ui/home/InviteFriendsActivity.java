package com.tr.ui.home;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.conference.square.SquareActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.knowledge.KnowledgeSquareActivity;
import com.tr.ui.relationship.MyFriendAllActivity;
import com.tr.ui.relationship.SIMContactActivity;
import com.utils.string.StringUtils;
import com.zxing.android.CaptureActivity;

public class InviteFriendsActivity extends JBaseActivity implements OnClickListener,PlatformActionListener {

	@ViewInject(R.id.phone_friend)
	private RelativeLayout phone_friend;
	@ViewInject(R.id.wechat_friend)
	private RelativeLayout wechat_friend;
	@ViewInject(R.id.qq_friend)
	private RelativeLayout qq_friend;
	@ViewInject(R.id.sao_yi_sao)
	private RelativeLayout sao_yi_sao;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(InviteFriendsActivity.this, "分享失败", 0).show();
		};
	};
	private String meetingId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_friends);
		ShareSDK.initSDK(this);
		ViewUtils.inject(this);
		setListener();
	}

	private void setListener() {
		phone_friend.setOnClickListener(this);
		wechat_friend.setOnClickListener(this);
		qq_friend.setOnClickListener(this);
		sao_yi_sao.setOnClickListener(this);
	}

	@Override
	public void initJabActionBar() {
//		jabGetActionBar().setTitle("邀请朋友");
//		mActionBar.setDisplayShowCustomEnabled(true);
//		mActionBar.setDisplayShowHomeEnabled(true);
//		mActionBar.setDisplayShowTitleEnabled(true);
//		View mCustomView = getLayoutInflater().inflate(R.layout.org_firstpage_actionbar_title, null);
//		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
//		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
//
//		mActionBar.setCustomView(mCustomView, mP);
//		TextView myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
//		myTitle.setText("邀请好友");
//		TextView create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
//		create_Tv.setVisibility(View.GONE);
//		ImageView search = (ImageView) mCustomView.findViewById(R.id.titleIv);
//		search.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ENavigate.startSearchActivity(InviteFriendsActivity.this,1);
//			}
//		});
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "邀请好友", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		dismissLoadingDialog();
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_more).setVisible(false);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			case R.id.home_new_menu_search:// 全局搜索
			ENavigate.startSearchActivity(this, 1);
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 联系人
		case R.id.phone_friend:
			ENavigate.startSIMContactActivity(InviteFriendsActivity.this, SIMContactActivity.TYPE_INVITE);
			break;
		// 微信
		case R.id.wechat_friend:
			ShareParams sp = new ShareParams();
			sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性

			sp.setTitle(ShareConfig.share_title);
			sp.setUrl(ShareConfig.share_title_url);
			sp.setText(ShareConfig.share_text);
			sp.setImageUrl(ShareConfig.image_url);
			Platform circle = ShareSDK.getPlatform(InviteFriendsActivity.this, Wechat.NAME);
			circle.setPlatformActionListener(InviteFriendsActivity.this); // 设置分享事件回调
			// 执行图文分享
			circle.share(sp);
			break;
		// qq
		case R.id.qq_friend:
			showLoadingDialog("");
			ShareUtils.share_QQFriend(InviteFriendsActivity.this, new PlatformActionListener() {
				@Override
				public void onError(Platform arg0, int arg1, Throwable arg2) {
					dismissLoadingDialog();
					handler.sendEmptyMessage(0);
				}

				@Override
				public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
					dismissLoadingDialog();
				}

				@Override
				public void onCancel(Platform arg0, int arg1) {
					dismissLoadingDialog();
				}
			}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
			break;
		case R.id.sao_yi_sao:
			startActivityForResult(new Intent(this, CaptureActivity.class), 1000);
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data!=null) {
			if (requestCode == 1000) {
				if (resultCode == Activity.RESULT_OK) {
					meetingId = data.getStringExtra("result");
					if (null == meetingId) {
						Toast.makeText(this, "无效的二维码", Toast.LENGTH_SHORT).show();
						return;
					} else if (meetingId.isEmpty()) {
						Toast.makeText(this, "无效的二维码", Toast.LENGTH_SHORT).show();
						return;
					}
					// TODO 这里用二维码访问网页 好有人脉
					else if (meetingId.contains("/invitation/")) {
						String substr = meetingId.substring(0, meetingId.length() - 1);
						substr = substr.substring(substr.lastIndexOf("/") + 1, substr.length());
						ENavigate.startInviteFriendByQRCodeActivity(InviteFriendsActivity.this, substr, InviteFriendByQRCodeActivity.PeopleFriend);
						return;
					}
					// 组织
					else if (meetingId.contains("customerId")) {
						String substr = meetingId.substring(meetingId.lastIndexOf("=") + 1, meetingId.length());
						if (StringUtils.isEmpty(substr) || StringUtils.isEmpty(App.getUserID())) {
							Toast.makeText(InviteFriendsActivity.this, "无效的二维码", 0).show();
							return;
						}
						ENavigate.startInviteFriendByQRCodeActivity(InviteFriendsActivity.this, substr, InviteFriendByQRCodeActivity.OrgFriend);
						return;

					}else if (meetingId.contains("communityId")) {
						String communityId = meetingId.substring(meetingId.indexOf("=")+1, meetingId.indexOf("&"));
						if (StringUtils.isEmpty(communityId) || StringUtils.isEmpty(App.getUserID())) {
							Toast.makeText(InviteFriendsActivity.this, "无效的二维码", 0).show();
							return;
						}
						ENavigate.startCommunityDetailsActivity(InviteFriendsActivity.this,Long.parseLong(communityId), false);
						return;

					}
					try {
						// 启动
						long a = 0;
						a = Long.valueOf(meetingId).longValue();
						if (0 == a) {
							// Toast.makeText(this, "无效的二维码",
							// Toast.LENGTH_SHORT)
							// .show();
							Uri uri = Uri.parse(meetingId);
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent);
							return;
						}
						Intent intent = new Intent(InviteFriendsActivity.this, SquareActivity.class);
						intent.putExtra("meeting_id", Long.valueOf(meetingId));
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	@Override
	public void onCancel(Platform arg0, int arg1) {
		dismissLoadingDialog();
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		dismissLoadingDialog();
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		dismissLoadingDialog();
		handler.sendEmptyMessage(0);
	}

}
