package com.tr.ui.relationship;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.db.ConnectionsCacheData;
import com.tr.model.obj.Connections;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.conference.square.SquareActivity;
import com.tr.ui.home.InviteFriendByQRCodeActivity;
import com.tr.ui.home.MyQRCodeActivity;
import com.tr.ui.home.frg.FrgConnections;
import com.tr.ui.widgets.EProgressDialog;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.zxing.android.CaptureActivity;

/**
 *  联系人 activity
 *  组织好友 和 个人好友
 * @author gushi
 *  */
public class MyFriendAllActivity extends JBaseFragmentActivity {
	public static final String TAG = "MyFriendAllActivity";
	
	/** 界面加载进度 */
	protected EProgressDialog mProgressDialog;
	
	private int newGroupCount = 0;
	private int newConnectionsCount = 0;
	
	private FrgConnections frgConnections;
	/** 传进过来的数据 */
	private Intent mIntent; 
	
	private int fromType;
	private FragmentManager fragmentManager ;
	
	/** 完成 MenuItem */
	private MenuItem finishMenuItem; 
	private MenuItem RichScan;
	private MenuItem InviteFriends;
	private MenuItem CreateConnections;
	private Context context;

	private TextView titleTv;

	private ImageView titleIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String MSG = "onCreate()";
		context = this;
		
		mIntent = getIntent();
		fromType = mIntent.getIntExtra(EConsts.Key.TYPE, ConnectionsCacheData.FILTER_FRIEND_ALL);
		setContentView(R.layout.activity_my_friend_all);
		initJabActionBar();
		
		fragmentManager = getSupportFragmentManager();
		frgConnections = new FrgConnections();
		Bundle bundle = new Bundle();
		bundle.putInt(EConsts.Key.TYPE, fromType);
//		bundle.putInt(EConsts.Key.SUM, sum);
		frgConnections.setArguments(bundle);
		fragmentManager.beginTransaction().replace(R.id.rootLl, frgConnections).commit();
	}

	
	public void initJabActionBar() {
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		if(ConnectionsCacheData.FILTER_FRIEND_ALL == fromType ){
			actionbar.setTitle("通讯录");
		}
		else if(ConnectionsCacheData.FILTER_PEOPLE_ALL == fromType ){
			actionbar.setTitle("好友/人脉 ");
			actionbar.setDisplayShowCustomEnabled(true);
			actionbar.setDisplayShowHomeEnabled(false);
			actionbar.setDisplayShowTitleEnabled(true);
			final View mCustomView = getLayoutInflater().inflate(
					R.layout.demand_actionbar_title, null);
			

			actionbar.setCustomView(mCustomView, new ActionBar.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
					.getLayoutParams();
			mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
					| Gravity.CENTER_HORIZONTAL;
			actionbar.setCustomView(mCustomView, mP);
			actionbar.setTitle(" ");
			
		}
	}
	
	
	public Context getContext() {
		return context;
	}


	public int getNewGroupCount() {
		return newGroupCount;
	}


	public void setNewGroupCount(int newGroupCount) {
		this.newGroupCount = newGroupCount;
	}


	public int getNewConnectionsCount() {
		return newConnectionsCount;
	}


	public void setNewConnectionsCount(int newConnectionsCount) {
		this.newConnectionsCount = newConnectionsCount;
	}



	IBindData iBindData = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			// 获得新的群数
			if (tag == EAPIConsts.IMReqType.IM_GET_NEW_GROUP_COUNT) {
				if (object != null) {
					newGroupCount = (Integer) object;
					int size = newGroupCount + newConnectionsCount;
					if (newGroupCount == 0) {
						if (frgConnections.cnsSizeTagGroup != null) {
							frgConnections.cnsSizeTagGroup.setVisibility(View.INVISIBLE);
						}
					}
					else {
						if (frgConnections.cnsSizeTagGroup != null) {
							frgConnections.cnsSizeTagGroup.setText("" + newGroupCount); 
							frgConnections.cnsSizeTagGroup.setVisibility(View.VISIBLE);
						}
					}
				}
			}
			
			// 获得新的关系数
			else if (tag == EAPIConsts.concReqType.im_getNewConnectionsCount) {
				if (object != null) {
					newConnectionsCount = (Integer) object;
					// newConnectionsCount=89;
					int size = newGroupCount + newConnectionsCount;
					if (newConnectionsCount == 0) {
						if (frgConnections.cnsSizeTagNew != null) {
							frgConnections.cnsSizeTagNew.setVisibility(View.INVISIBLE);
						}
					} else {
						if (frgConnections.cnsSizeTagGroup != null) {
							frgConnections.cnsSizeTagNew.setText("" + newConnectionsCount);
							frgConnections.cnsSizeTagNew.setVisibility(View.VISIBLE);
						}
					}
				}
			}
			
		}
	};

	
	public static byte ITEM_NEWConnections = 0;
	public static byte ITEM_GROUP = 1;

	private android.app.ActionBar actionbar;

	
	public void changeCnsSizeTag(int item_type) {
		if (item_type == ITEM_GROUP) {
			newGroupCount = 0;
			if (frgConnections != null && frgConnections.cnsSizeTagGroup != null) {
				frgConnections.cnsSizeTagGroup.setVisibility(View.INVISIBLE);
			}
		} else {
			newConnectionsCount = 0;
			if (frgConnections != null && frgConnections.cnsSizeTagNew != null) {
				frgConnections.cnsSizeTagNew.setVisibility(View.INVISIBLE);
			}
		}
		int size = newGroupCount + newConnectionsCount;
	}
	
	/**
	 * 刷新界面
	 * @param connections
	 */
	public void updateUI(Connections connections){
		String MSG = "updateUI()";
		Log.i(TAG, MSG);
//		frgConnections.updateUI(connections);
		frgConnections.reFreshData();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(ConnectionsCacheData.FILTER_PEOPLE_ALL == fromType ){
			getMenuInflater().inflate(R.menu.activity_new_main, menu);
			menu.findItem(R.id.home_new_menu_more).setIcon(R.drawable.ic_action_overflow);
			menu.findItem(R.id.home_new_menu_search).setVisible(false);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(finishMenuItem == item){
			ENavigate.startSIMContactActivity(getContext(), SIMContactActivity./*TYPE_INVITE*/TYPE_ADD_CONTACTS);
		}
		if (item.getItemId() ==  R.id.home_new_menu_more) { // 扫一扫
			final PopupWindow popupWindow = new PopupWindow(MyFriendAllActivity.this);
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			ColorDrawable dw = new ColorDrawable(0000000);
            popupWindow.setBackgroundDrawable(dw);
			popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
			View convertView = View.inflate(MyFriendAllActivity.this, R.layout.main_menu_more_popupwindow, null);
			LinearLayout home_scan = (LinearLayout) convertView.findViewById(R.id.home_scan);
			LinearLayout invite_friends = (LinearLayout) convertView.findViewById(R.id.invite_friends);
			LinearLayout onekeyback = (LinearLayout) convertView.findViewById(R.id.onekeyback);
			
			convertView.findViewById(R.id.scan_Iv).setVisibility(View.GONE);
			convertView.findViewById(R.id.invite_friends_Iv).setVisibility(View.GONE);
			convertView.findViewById(R.id.onekeyback_Iv).setVisibility(View.GONE);
			
			TextView onekeyback_Tv = (TextView) convertView.findViewById(R.id.onekeyback_Tv);
			onekeyback_Tv.setText("创建人脉");
			home_scan.setOnClickListener(new View.OnClickListener() {// 扫一扫

				@Override
				public void onClick(View v) {
					startActivityForResult(new Intent(MyFriendAllActivity.this, CaptureActivity.class), 1000);
					popupWindow.dismiss();
				}
			});
			invite_friends.setOnClickListener(new View.OnClickListener() {// 邀请好友

				@Override
				public void onClick(View v) {
					ENavigate.startMeetingInviteFriendsActivity(MyFriendAllActivity.this);
					popupWindow.dismiss();
				}
			});
			onekeyback.setOnClickListener(new View.OnClickListener() {// 创建人脉

				@Override
				public void onClick(View v) {
					ENavigate.startNewConnectionsActivity(MyFriendAllActivity.this, 1, null, 9);
					popupWindow.dismiss();
				}
			});
			popupWindow.setContentView(convertView);
			
			Rect frame = new Rect();
			getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;
			
			popupWindow.showAtLocation(actionbar.getCustomView(), Gravity.TOP|Gravity.RIGHT, 0, actionbar.getHeight()+statusBarHeight);
//			popupWindow.showAsDropDown(actionbar.getCustomView(),(int)Utils.convertDpToPixel(260),0);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 1000) {
			if (resultCode == Activity.RESULT_OK) {
				String meetingId = intent.getStringExtra("result");
				if (null == meetingId) {
					Toast.makeText(MyFriendAllActivity.this, "无效的二维码", Toast.LENGTH_SHORT).show();
					return;
				} else if (meetingId.isEmpty()) {
					Toast.makeText(MyFriendAllActivity.this, "无效的二维码", Toast.LENGTH_SHORT).show();
					return;
				}
				// TODO 这里用二维码访问网页 好有人脉
				else if (meetingId.contains("/invitation/")) {
					String substr = meetingId.substring(0, meetingId.length() - 1);
					substr = substr.substring(substr.lastIndexOf("/") + 1, substr.length());
					ENavigate.startInviteFriendByQRCodeActivity(MyFriendAllActivity.this, substr, InviteFriendByQRCodeActivity.PeopleFriend);
					return;
				}
				// 组织
				else if (meetingId.contains("customerId")) {
					String substr = meetingId.substring(meetingId.lastIndexOf("=") + 1, meetingId.length());
					if (StringUtils.isEmpty(substr) || StringUtils.isEmpty(App.getUserID())) {
						Toast.makeText(MyFriendAllActivity.this, "无效的二维码", 0).show();
						return;
					}
					ENavigate.startInviteFriendByQRCodeActivity(MyFriendAllActivity.this, substr, InviteFriendByQRCodeActivity.OrgFriend);
					return;
				//社群
				}else if (meetingId.contains("communityId")) {
					String communityId = meetingId.substring(meetingId.indexOf("=")+1, meetingId.indexOf("&"));
					if (StringUtils.isEmpty(communityId) || StringUtils.isEmpty(App.getUserID())) {
						Toast.makeText(MyFriendAllActivity.this, "无效的二维码", 0).show();
						return;
					}
					ENavigate.startCommunityDetailsActivity(MyFriendAllActivity.this,Long.parseLong(communityId),false);
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
						Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent1);
						return;
					}
					Intent intent2 = new Intent(MyFriendAllActivity.this, SquareActivity.class);
					intent.putExtra("meeting_id", Long.valueOf(meetingId));
					startActivity(intent2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if(100 == requestCode ){
			if(intent!=null){
				boolean IsChange = intent.getBooleanExtra("IsChange", false);
				if (IsChange) {
					updateUI(null);
				}
			}
		}else if(requestCode == 9){
			if(resultCode == RESULT_OK){
				
			}else if (resultCode == ENavConsts.ActivityReqCode.REQUEST_CODE_UPDATE_FRIDENDS) {
				frgConnections.startGetData();
			}
		}
	}
	
}
