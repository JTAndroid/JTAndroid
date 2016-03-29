package com.tongmeng.alliance.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tr.R;
import com.tr.model.obj.JTFile;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.KeelLog;

public class MySendDetailActivity extends JBaseActivity implements
		OnClickListener {

	TextView mysendetail_theme, mysenddetail_time_tv, mysenddetail_place_tv;
	// 参会人员列表 编辑活动 分享
	RelativeLayout mysenddetail_attendpeople, mysenddetail_editactivity,
			mysenddetail_myshare, mysenddetail_code;
	String id;
	Button mysendcancle_activity;
	LinearLayout mysend_detail_linearlayout;
	View layout;
	Dialog dialog;
	Button cancleBtn;
	String image, theme;
	ImageView mysendetail_image;
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.default_picture_liebiao) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.default_picture_liebiao) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.default_picture_liebiao) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.build();

	Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.arg1) {

			case 1:
				Toast.makeText(getApplicationContext(), "取消成功", 1).show();
				finish();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "取消失败", 1).show();

				break;
			default:
				break;
			}

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mysenddetail_myshare:

			JTFile jtFile = new JTFile();
			// jtFile.mFileName = App.getNick()+"分享了[会议]";
			jtFile.fileName = theme;
			jtFile.mUrl = image;
			jtFile.mSuffixName = theme;
			jtFile.mType = JTFile.TYPE_CONFERENCE;
			jtFile.mModuleType = 1;// 会议类型
			jtFile.mTaskId = id;
			jtFile.reserved1 = theme;
			// 弹出分享对话框
			FrameWorkUtils.showSharePopupWindow2(MySendDetailActivity.this,
					jtFile);
			break;

		case R.id.mysenddetail_editactivity:
			Intent intentdetailedit = new Intent(MySendDetailActivity.this,
					ReleaseActivity.class);
			intentdetailedit.putExtra("activityId", id);
			// Toast.makeText(getApplicationContext(), "点击活动id"+id, 1).show();

			// intentdetail.putExtra("id", Integer.parseInt(id));
			startActivity(intentdetailedit);
			break;
		case R.id.mysenddetail_attendpeople:
			Intent intent = new Intent(MySendDetailActivity.this,
					AttendPeopleInfoActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);
			break;
		case R.id.mysendcancle_activity:
			DeleteMyCreateActivity(id);
			break;
		case R.id.mysenddetail_code:
			Intent codeIntent = new Intent(MySendDetailActivity.this,
					QRSignCodeActivity.class);
			codeIntent.putExtra("activityId", id);
			startActivity(codeIntent);
			break;
		default:
			break;
		}
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.my_send_detail);
		initTitle();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mysendetail_image = (ImageView) findViewById(R.id.mysendetail_image);
		image = getIntent().getExtras().getString("imagepic");
		theme = getIntent().getExtras().getString("theme");
		String time = getIntent().getExtras().getString("time");
		String place = getIntent().getExtras().getString("place");
		id = getIntent().getExtras().getString("id");
		KeelLog.e("", "id::" + id);
		id = getIntent().getStringExtra("id");
		KeelLog.e("", "id::" + id);
		// Toast.makeText(getApplicationContext(), "点击活动id"+id, 1).show();
		mysendetail_theme = (TextView) findViewById(R.id.mysendetail_theme);
		mysenddetail_time_tv = (TextView) findViewById(R.id.mysenddetail_time_tv);
		mysenddetail_place_tv = (TextView) findViewById(R.id.mysenddetail_place_tv);
		mysenddetail_attendpeople = (RelativeLayout) findViewById(R.id.mysenddetail_attendpeople);
		mysenddetail_editactivity = (RelativeLayout) findViewById(R.id.mysenddetail_editactivity);
		mysenddetail_myshare = (RelativeLayout) findViewById(R.id.mysenddetail_myshare);
		mysenddetail_code = (RelativeLayout) findViewById(R.id.mysenddetail_code);
		mysenddetail_editactivity.setOnClickListener(this);
		mysenddetail_myshare.setOnClickListener(this);
		mysenddetail_code.setOnClickListener(this);
		mysendcancle_activity = (Button) findViewById(R.id.mysendcancle_activity);
		mysendetail_theme.setText(theme);
		mysenddetail_time_tv.setText(time);
		mysenddetail_place_tv.setText(place);
		mysendcancle_activity.setOnClickListener(this);
		mysenddetail_attendpeople.setOnClickListener(this);
		mysend_detail_linearlayout = (LinearLayout) findViewById(R.id.mysend_detail_linearlayout);
		mysend_detail_linearlayout.setOnClickListener(this);

		ImageLoader.getInstance().displayImage(image, mysendetail_image,
				options);
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "活动管理",
				false, null, false, true);
	}

	protected void DeleteMyCreateActivity(final String id) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				System.out.print("AccountUserAdapter设置默认账户状态id" + id);
				String params = "{\"id\":\"" + id + "\"}";
				String path = "http://www.etongmeng.com/server/activity/delete.json";
				String result = HttpRequestUtil.sendPost(path, params,
						MySendDetailActivity.this);
				try {
					JSONObject rev = new JSONObject(result);

					JSONObject responseData = rev.getJSONObject("responseData");
					String succeed = responseData.getString("succeed");
					JSONObject notification = rev.getJSONObject("notification");
					String notifyCode = notification.getString("notifyCode");
					String notifyInfo = notification.getString("notifyInfo");
					if (succeed.equals("true")) {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 1;

							msgHandler.sendMessage(msg);
						}
					} else if (succeed.equals("false")) {
						if (msgHandler != null) {
							Message msg = msgHandler.obtainMessage();
							msg.arg1 = 2;
							// msg.obj = message;
							msgHandler.sendMessage(msg);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			};
		}.start();
	}
}
