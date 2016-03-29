package com.tongmeng.alliance.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongmeng.alliance.dao.Apply;
import com.tongmeng.alliance.dao.AttendPeopleInfo;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.LoadUtil;
import com.tr.R;
import com.tr.model.obj.JTFile;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.FrameWorkUtils;
import com.utils.log.KeelLog;

public class AttendPeopleInfoActivity extends JBaseActivity {

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;
	AttendPeopleInfoAdapter attendpeopleAdapter;
	List<AttendPeopleInfo> list_attendpeopleinfo;
	ListView listview_attendpeopleinfo;
	JSONArray array_attenderList, array_applyList;
	String result;
	TextView attendnumber;
	int count;
	private String FILE = "saveSetting";
	SharedPreferences sp;
	Editor editor;
	
	Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.arg1) {
			case 1:

				attendnumber.setText(String.valueOf(count));
				Toast.makeText(AttendPeopleInfoActivity.this,
						"名单管理输出信息" + result, 1).show();

				attendpeopleAdapter = new AttendPeopleInfoAdapter(
						AttendPeopleInfoActivity.this, list_attendpeopleinfo);
				listview_attendpeopleinfo.setAdapter(attendpeopleAdapter);

				break;

			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendpeople_info_activity);
		initView();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		sp = this.getSharedPreferences(FILE, this.MODE_PRIVATE);
		editor = sp.edit();
		list_attendpeopleinfo = new ArrayList<AttendPeopleInfo>();
		// listview_custom = (ListView) findViewById(R.id.listview_custom);
		listview_attendpeopleinfo = (ListView) findViewById(R.id.listview_attendpeopleinfo);
//		hideallpeopleinfo = (ImageView) findViewById(R.id.hideallpeopleinfo);
		attendnumber = (TextView) findViewById(R.id.attendnumber);
		editor.putString("hidepeopleinfo", "1");
		editor.putString("hidepeopleinfoall", "1");
		editor.commit();
		String id = getIntent().getExtras().getString("id");
		Toast.makeText(this, id, 1).show();
		System.out.print("点击获取的当前页面的id" + id);
		
		
		EditActivityPeople(id);
	}

	protected void EditActivityPeople(final String id) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				System.out.print("AccountUserAdapter设置默认账户状态id" + id);
				try {
					String params = "{\"activityId\":\""+id+"\"}";
//					String attenderListpath = "http://192.168.101.140:8801/member/attenderList.json";
					// TODO Auto-generated method stub
					result = HttpRequestUtil.sendPost(Constant.attenderListpath, params,
							AttendPeopleInfoActivity.this);
					System.out.print("名单管理信息" + result);
					Log.e("", "名单管理界面result::"+result);

					JSONObject rev = new JSONObject(result);
					System.out.print("名单管理信息rev" + rev);
					JSONObject responseData = rev.getJSONObject("responseData");
					System.out.print("名单管理信息responseData" + responseData);
					JSONObject attenderList = responseData
							.getJSONObject("attenderList");
					count = attenderList.getInt("count");
					array_attenderList = attenderList
							.getJSONArray("attenderList");
					for (int i = 0, n = array_attenderList.length(); i < n; i++) {
						JSONObject obj = array_attenderList.getJSONObject(i);
						AttendPeopleInfo attendpeopleinfo = new AttendPeopleInfo();
						int id = obj.getInt("id");
						String name = obj.getString("name");
						String phone = obj.getString("phone");
						String pic = obj.getString("pic");
						attendpeopleinfo.setId(id);
						attendpeopleinfo.setName(name);
						attendpeopleinfo.setPhone(phone);
						attendpeopleinfo.setPic(pic);
						Apply apply;
						array_applyList = obj.getJSONArray("applyList");
						List<Apply> applyList = new ArrayList<Apply>();
						for (int j = 0, m = array_applyList.length(); j < m; j++) {
							apply = new Apply();
							JSONObject obj_apply = array_applyList
									.getJSONObject(j);
							String key = obj_apply.getString("key");
							String values = obj_apply.getString("value");
							apply.setKey(key);
							apply.setValues(values);
							applyList.add(apply);
						}
						attendpeopleinfo.setApplyList(applyList);
						list_attendpeopleinfo.add(attendpeopleinfo);
					}
					JSONObject notification = rev.getJSONObject("notification");
					String notifyCode = notification.getString("notifyCode");
					String notifyInfo = notification.getString("notifyInfo");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (msgHandler != null) {
					Message msg = msgHandler.obtainMessage();
					msg.arg1 = 1;

					msgHandler.sendMessage(msg);
				}

			};
		}.start();
	}


	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		KeelLog.e("ContactsMainPageActivity", "initJabActionBar");
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("邀请您报名");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("");
		create_Tv.setBackgroundResource(R.drawable.icon_in);
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		menu.findItem(R.id.home_new_menu_more)
				.setIcon(R.drawable.forward_share);
		return super.onCreateOptionsMenu(menu);
	}

	public class AttendPeopleInfoAdapter extends BaseAdapter {
		Context context;
		List<AttendPeopleInfo> list;
		private String FILE = "saveSetting";
		SharedPreferences sp;
		Editor editor;
		private LayoutInflater mInflater;
		ViewHolder holder;
		// Bitmap bitmap=null;

		public AttendPeopleInfoAdapter(Context context, List<AttendPeopleInfo> list) {
			this.context = context;
			this.list = list;
			this.mInflater = LayoutInflater.from(context);
			sp = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
			editor = sp.edit();

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			holder = new ViewHolder();
			if (convertView == null) {

				convertView = mInflater.inflate(R.layout.attendpeopleinfo, null);
				holder.name = (TextView) convertView
						.findViewById(R.id.attendpeopleinfo_name);
				holder.phone = (TextView) convertView
						.findViewById(R.id.attendpeopleinfo_phone);
				holder.header = (ImageView) convertView
						.findViewById(R.id.attendpeopleinfo_image);
				holder.listview_attendpeople_item = (LinearLayout) convertView
						.findViewById(R.id.listview_attendpeople_item);
				holder.shouqi = (ImageView) convertView.findViewById(R.id.shouqi);
				holder.attendpeopleinfo_call = (ImageView) convertView
						.findViewById(R.id.attendpeopleinfo_call);
				// 当前状态
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.attendpeopleinfo_call.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Toast.makeText(context, "点击了打电话", Toast.LENGTH_LONG).show();

					String strInput = holder.phone.getText().toString();
					try {
						if (isPhoneNumberValid(strInput) == true
								&& strInput != null) {

							// 穿件一个新的Intent 运行ACTION.CALL的常数与通过uri将字符串带入

							Intent myIntentDial = new Intent(
									"android.intent.action.CALL", Uri.parse("tel:"
											+ strInput));
							// 在StartActivity（）方法里面带入自定义的Intent对象以运行拨打电话的工作
							context.startActivity(myIntentDial);
							// ed_phone.setText("");

						} else {
							// ed_phone.setText("");
							Toast.makeText(context, "电话号码格式不符", Toast.LENGTH_LONG)
									.show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			AttendPeopleInfo info = list.get(position);
			Log.e("","第"+position+ "个item，条数：："+info.getApplyList().size());

			holder.name.setText(list.get(position).getName());
			holder.phone.setText(list.get(position).getPhone());
			
			for (int i = 0; i < info.getApplyList().size(); i++) {
				Apply apply = info.getApplyList().get(i);
				Log.e("", "第"+(i+1)+"个apply::"+apply.getKey()+"---"+apply.getValues());
				if (apply != null) {
					View view = mInflater.inflate(R.layout.attendpeopleinfoitem,
							null);
					TextView keyText = (TextView) view.findViewById(R.id.key);
					TextView valueText = (TextView) view.findViewById(R.id.values);
					keyText.setText(apply.getKey());
					valueText.setText(apply.getValues());
					holder.listview_attendpeople_item.addView(view);
				}
				
			}
			return convertView;
		}

		class ViewHolder {
			TextView name, phone, key, values;
			ImageView header;
			ImageView shouqi, attendpeopleinfo_call;
			LinearLayout listview_attendpeople_item;
		}

		// TODO Auto-generated method stub

		protected boolean isPhoneNumberValid(String phoneNumber) {
			boolean isValid = false;
			String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
			String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
			CharSequence inputStr = phoneNumber;
			// 创建Patten
			Pattern pattern = Pattern.compile(expression);
			// 将Patten以参数传入Matcher作 Regular expression
			Matcher matcher = pattern.matcher(inputStr);
			// 创建Patten2
			Pattern pattern2 = Pattern.compile(expression2);
			// 将Patten2以参数传入Matcher作 Regular expression
			Matcher matcher2 = pattern.matcher(inputStr);
			if (matcher.matches() || matcher2.matches()) {
				isValid = true;
			}
			// TODO Auto-generated method stub
			return isValid;
		}
	}

}
