package com.tr.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.image.ImageLoader;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.conference.square.SquareActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.file.Screenshot;
import com.utils.string.StringUtils;
import com.zxing.android.CaptureActivity;

/**
 * 
 * @author sunjianan
 * 
 */
public class MyQRCodeActivity extends JBaseActivity {

	@ViewInject(R.id.iv_qrcode)
	private ImageView iv_qrcode;

	@ViewInject(R.id.rl_qrcode)
	private LinearLayout rl_qrcode;

	@ViewInject(R.id.iv_avatar)
	private ImageView iv_avatar;

	@ViewInject(R.id.tv_username)
	private TextView tv_username;
	
	@ViewInject(R.id.qrcodeTitleTv)
	private TextView qrcodeTitleTv;
	
	@ViewInject(R.id.qrcodeBottomTv)
	private TextView qrcodeBottomTv;
	@ViewInject(R.id.headerVi)
	private View headerVi;

	private String qRCodeStr = "QRCodeStr";

	private String qRName,communityId;

	private String userAvatar;

	private boolean isCommunity;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "二维码", false, null,true, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_qrcode);
		ViewUtils.inject(this);

		qRCodeStr = getIntent().getStringExtra(ENavConsts.QRCodeStr);
		qRName = getIntent().getStringExtra(ENavConsts.QRName);
		userAvatar = getIntent().getStringExtra(ENavConsts.userAvatar);
		isCommunity =  getIntent().getBooleanExtra("isCommunity", false);
		if (isCommunity) {
			communityId = getIntent().getStringExtra("communityId");
			qrcodeTitleTv.setText("群二维码");
			qrcodeBottomTv.setText("在金桐上扫一扫加群");
			HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "群名片", false, null,true, true);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		int imgLegth = tv_username.getWidth();
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgLegth, imgLegth);
		iv_qrcode.setLayoutParams(layoutParams);
		tv_username.setText(qRName);
		try {
			// TODO QRCodeStr
			Bitmap bitmap = FrameWorkUtils.createQRCode(qRCodeStr, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			iv_qrcode.setImageBitmap(bitmap);
			//有头像时设置头像，无头像时设置默认头像
			if (!StringUtils.isEmpty(userAvatar)) {
				if (isCommunity) {
					ImageLoader.load(iv_avatar, userAvatar, R.drawable.avatar_community);
				}else{
					ImageLoader.load(iv_avatar, userAvatar, R.drawable.head_pic_default);
				}
			} else {
				if (isCommunity) {
					iv_avatar.setImageResource(R.drawable.avatar_community);
				}else{
					iv_avatar.setImageResource(R.drawable.head_pic_default);
				}
			}
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.flow_qrcode, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.flow:
			final PopupWindow popupWindow = new PopupWindow(MyQRCodeActivity.this);
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			ColorDrawable dw = new ColorDrawable(0000000);
			popupWindow.setBackgroundDrawable(dw);
			popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
			View convertView = View.inflate(MyQRCodeActivity.this, R.layout.qrcode_menu_more_popupwindow, null);
			LinearLayout qrcode_scan = (LinearLayout) convertView.findViewById(R.id.qrcode_scan);
			LinearLayout qrcode_share = (LinearLayout) convertView.findViewById(R.id.qrcode_share);
			LinearLayout qrcode_save_image_ll = (LinearLayout) convertView.findViewById(R.id.qrcode_save_image_ll);
			popupWindow.setContentView(convertView);
			
			qrcode_scan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivityForResult(new Intent(MyQRCodeActivity.this, CaptureActivity.class), 1000);
					popupWindow.dismiss();
				}
			});
			/**
			 * 保存到手机
			 */
			qrcode_save_image_ll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					popupWindow.dismiss();
					Screenshot.GetandSaveCurrentImage(MyQRCodeActivity.this);
				}
			});
			
			qrcode_share.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FrameWorkUtils.showSharePopupWindowKnow(MyQRCodeActivity.this, qRCodeStr,qRName,userAvatar,communityId);
					popupWindow.dismiss();
				}
			});
			
			
			
			Rect frame = new Rect();
			getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;
			
			popupWindow.showAtLocation(getActionBar().getCustomView(), Gravity.TOP|Gravity.RIGHT, 0, getActionBar().getHeight()+statusBarHeight);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		return super.onMenuItemSelected(featureId, item);
	}
	
	private String meetingId;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
					ENavigate.startInviteFriendByQRCodeActivity(MyQRCodeActivity.this, substr, InviteFriendByQRCodeActivity.PeopleFriend);
					return;
				}
				// 组织
				else if (meetingId.contains("customerId")) {
					String substr = meetingId.substring(meetingId.lastIndexOf("=") + 1, meetingId.length());
					if (StringUtils.isEmpty(substr) || StringUtils.isEmpty(App.getUserID())) {
						Toast.makeText(MyQRCodeActivity.this, "无效的二维码", 0).show();
						return;
					}
					ENavigate.startInviteFriendByQRCodeActivity(MyQRCodeActivity.this, substr, InviteFriendByQRCodeActivity.OrgFriend);
					return;
				//社群
				}else if (meetingId.contains("communityId")) {
					String communityId = meetingId.substring(meetingId.indexOf("=")+1, meetingId.indexOf("&"));
					if (StringUtils.isEmpty(communityId) || StringUtils.isEmpty(App.getUserID())) {
						Toast.makeText(MyQRCodeActivity.this, "无效的二维码", 0).show();
						return;
					}
					ENavigate.startCommunityDetailsActivity(MyQRCodeActivity.this,Long.parseLong(communityId), false);
					return;

				}else if (meetingId.contains("http")){
					ENavigate.startNeturlKnowledgeActivity(MyQRCodeActivity.this, meetingId);
					return;
				}
				try {
					// 启动
					long a = 0;
					a = Long.valueOf(meetingId).longValue();
					if (0 == a) {
						Uri uri = Uri.parse(meetingId);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
						return;
					}
					Intent intent = new Intent(MyQRCodeActivity.this, SquareActivity.class);
					intent.putExtra("meeting_id", Long.valueOf(meetingId));
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
