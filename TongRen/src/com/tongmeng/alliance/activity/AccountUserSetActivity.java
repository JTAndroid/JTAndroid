package com.tongmeng.alliance.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tongmeng.alliance.dao.AccountUser;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AccountUserSetActivity extends JBaseActivity {

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;

	ListView list_account_user;
	List<AccountUser> list_user;
	int mposition;
	AccountUser accountUser;
	TextView noaccountuser;
	AccountUserAdapter accountUserAdapter;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.arg1) {

			case 2:
				accountUserAdapter = new AccountUserAdapter(
						AccountUserSetActivity.this, list_user);
				list_account_user.setAdapter(accountUserAdapter);
				list_account_user
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								mposition = position;
								accountUserAdapter.setChoic_position(position);
								accountUserAdapter.notifyDataSetChanged();
							}
						});
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
		setContentView(R.layout.account_user_set);
		list_account_user = (ListView) findViewById(R.id.list_account_user);
		noaccountuser = (TextView) findViewById(R.id.noaccountuser);

		getAccountUser();
	}

	private void getAccountUser() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				System.out.println("执行的代码");
				String result = HttpRequestUtil.sendPost(
						Constant.getAccounPath, null,
						AccountUserSetActivity.this);
				list_user = new ArrayList<AccountUser>();
				try {
					JSONObject rev = new JSONObject(result);
					JSONObject responseData = rev.getJSONObject("responseData");
					JSONArray account = responseData
							.getJSONArray("accountList");
					System.out.println("开始解析输出account" + account);
					int id = 0;
					int type = 0;
					String name = null;
					String act = null;
					int isDefault = 0;
					for (int i = 0, n = account.length(); i < n; i++) {
						JSONObject obj = account.getJSONObject(i);
						id = obj.getInt("id");
						type = obj.getInt("type");
						name = obj.getString("name");
						act = obj.getString("account");
						isDefault = obj.getInt("isDefault");
						accountUser = new AccountUser();
						accountUser.setId(id);
						accountUser.setType(type);
						accountUser.setName(name);
						accountUser.setAccount(act);
						accountUser.setIsDefault(String.valueOf(isDefault));
						list_user.add(accountUser);
					}

					JSONObject notification = rev.getJSONObject("notification");
					String notifyCode = notification.getString("notifyCode");
					String notifyInfo = notification.getString("notifyInfo");
					if (handler != null) {
						Message msg = handler.obtainMessage();
						msg.arg1 = 2;
						handler.sendMessage(msg);
					}

				} catch (JSONException e) {
					e.printStackTrace();
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
		myTitle.setText("收款账户设置");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("");
		create_Tv.setBackgroundResource(R.drawable.ic_actionbar_more);
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AccountUserSetActivity.this,
						AccountUserDetailActivity.class);
				startActivity(intent);
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}

	public class AccountUserAdapter extends BaseAdapter {
		Context context;
		List<AccountUser> list;
		private LayoutInflater mInflater;
		ViewHolder holder;
		private int choic_position;
		Dialog dialog;
		View layout;
		String accountDefalt;
		String notifyInfodelete;
		String notifyInfoisDefalut;

		public AccountUserAdapter(Context context, List<AccountUser> list) {
			this.context = context;
			this.list = list;
			this.mInflater = LayoutInflater.from(context);
			dialog = new Dialog(context, R.style.MyDialogStyle);
		}

		public int getChoic_position() {
			return choic_position;
		}

		public void setChoic_position(int choic_position) {
			this.choic_position = choic_position;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			holder = new ViewHolder();

			if (convertView == null) {

				convertView = mInflater.inflate(R.layout.account_user_item,
						null);
				holder.accountUserName = (TextView) convertView
						.findViewById(R.id.account_user_item_name);
				holder.accountUserNum = (TextView) convertView
						.findViewById(R.id.account_user_item_num);
				holder.accountSetDefault = (TextView) convertView
						.findViewById(R.id.account_user_setdefault);
				holder.accountImage = (ImageView) convertView
						.findViewById(R.id.account_user_set_image);
				holder.account_user_set_linearlayout = (LinearLayout) convertView
						.findViewById(R.id.account_user_set_linearlayout);
				holder.accountImage = (ImageView) convertView
						.findViewById(R.id.account_user_set_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.accountUserName.setText(list.get(position).getName());
			holder.accountUserNum.setText(list.get(position).getAccount());
			if (String.valueOf(list.get(position).getType()).equals("2")) {

				holder.accountImage
						.setImageResource(R.drawable.icon_alipaypayment);
			} else if (String.valueOf(list.get(position).getType()).equals("1")) {
				holder.accountImage
						.setImageResource(R.drawable.icon_wechatpayment);
			}

			if (list.get(position).getIsDefault().equals("1")) {
				accountDefalt = "默认账号";
				holder.accountSetDefault.setText(accountDefalt); // 1是默认账号，0不是默认账号
				holder.accountSetDefault
						.setBackgroundResource(R.drawable.btn_gray);
			} else if (list.get(position).getIsDefault().equals("0")) {
				accountDefalt = "设为默认";
				holder.accountSetDefault.setText(accountDefalt);
				holder.accountSetDefault
						.setBackgroundResource(R.drawable.btn_blue);
			}
			holder.account_user_set_linearlayout
					.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							LayoutInflater inflater = ((Activity) context)
									.getLayoutInflater();
							View layout = inflater
									.inflate(
											R.layout.dialog_judgment,
											(ViewGroup) ((Activity) context)
													.findViewById(R.id.set_dialog_judgment));
							dialog.setContentView(layout);

							WindowManager m = ((Activity) context)
									.getWindowManager();
							Display d = m.getDefaultDisplay();
							Window dialogWindow = dialog.getWindow();
							android.view.WindowManager.LayoutParams p = dialogWindow
									.getAttributes();
							p.width = (int) (d.getWidth());
							p.height = (int) (1 * d.getHeight() / 4);
							dialogWindow.setGravity(Gravity.CENTER);
							dialog.getWindow().setAttributes(p);
							dialog.show();
							Button cancle = (Button) layout
									.findViewById(R.id.cancle);
							Button summint = (Button) layout
									.findViewById(R.id.summint);
							TextView title_set = (TextView) layout
									.findViewById(R.id.title_set);
							title_set.setText("删除账户");
							cancle.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
							summint.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									String idDelete = String.valueOf(list.get(
											position).getId());
									setDelete(Constant.deleteAccountPath,
											idDelete);

								}
							});
							return false;
						}
					});

			holder.accountSetDefault.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (holder.accountSetDefault.getText().toString()
							.equals("默认账号")) {
						Toast.makeText(context, "当前账号已经是默认账号", 1).show();
					}

					else if (holder.accountSetDefault.getText().toString()
							.equals("设为默认")) {
						String idDefault = String.valueOf(list.get(
								choic_position).getId());
						GetSucceed(Constant.setDefaltAccountPath, idDefault);
					}

				}
			});
			return convertView;
		}

		protected void setDelete(final String path, final String id) {
			// TODO Auto-generated method stub
			new Thread() {
				public void run() {
					System.out.print("AccountUserAdapter设置默认账户状态id" + id);
					String params = "{\"id\":\"" + id + "\"}";
					String result = HttpRequestUtil.sendPost(path, params,
							context);
					System.out.print("AccountUserAdapter设置默认账户状态result"
							+ result);
					try {
						JSONObject rev = new JSONObject(result);
						System.out.print("AccountUserAdapter设置默认账户状态rev" + rev);

						JSONObject responseData = rev
								.getJSONObject("responseData");
						String succeed = responseData.getString("succeed");
						System.out.print("AccountUserAdapter设置默认账户状态succeed"
								+ succeed);

						JSONObject notification = rev
								.getJSONObject("notification");
						String notifyCode = notification
								.getString("notifyCode");
						notifyInfodelete = notification.getString("notifyInfo");
						// 删除账号成功
						if (notifyCode.equals("0001")) {
							if (msgHandler != null) {
								Message msg = msgHandler.obtainMessage();
								msg.arg1 = 5;
								msgHandler.sendMessage(msg);
							}
						}
						// 删除账号失败
						else {
							if (msgHandler != null) {
								Message msg = msgHandler.obtainMessage();
								msg.arg1 = 6;
								msgHandler.sendMessage(msg);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				};
			}.start();
		}

		protected void GetSucceed(final String path, final String id) {
			// TODO Auto-generated method stub
			new Thread() {
				public void run() {
					String param = "{\"id\":\"" + id + "\"}";
					KeelLog.e("AccountUserDetailActivity", "param::" + param);
					String result = HttpRequestUtil.sendPost(path, param,
							context);
					KeelLog.e("AccountUserDetailActivity", "result::" + result);
					try {
						JSONObject rev = new JSONObject(result);
						System.out.print("AccountUserAdapter设置默认账户状态rev" + rev);

						JSONObject responseData = rev
								.getJSONObject("responseData");
						String succeed = responseData.getString("succeed");
						System.out.print("AccountUserAdapter设置默认账户状态succeed"
								+ succeed);
						JSONObject notification = rev
								.getJSONObject("notification");
						String notifyCode = notification
								.getString("notifyCode");
						notifyInfoisDefalut = notification
								.getString("notifyInfo");
						// 设置默认账号成功
						if (notifyCode.equals("0001")) {
							if (msgHandler != null) {
								Message msg = msgHandler.obtainMessage();
								msg.arg1 = 3;

								msgHandler.sendMessage(msg);
							}
						}
						// 设置默认账号失败
						else {
							if (msgHandler != null) {
								Message msg = msgHandler.obtainMessage();
								msg.arg1 = 4;
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

		Handler msgHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.arg1) {

				case 3:
					Toast.makeText(context, "点击了设置默认账户", 1).show();
					holder.accountSetDefault.setText("默认账户");
					holder.accountSetDefault
							.setBackgroundResource(R.drawable.btn_gray);
					AccountUserAdapter.this.notifyDataSetChanged();

					Intent intentset = new Intent(context,
							AccountUserSetActivity.class);
					context.startActivity(intentset);
					break;
				case 4:
					Toast.makeText(context, notifyInfoisDefalut, 1).show();
					break;
				case 5:
					Toast.makeText(context, "删除成功", 1).show();
					dialog.dismiss();
					list.remove(choic_position);
					Intent intentdelete = new Intent(context,
							AccountUserSetActivity.class);
					context.startActivity(intentdelete);
					 notifyDataSetChanged();
					break;
				case 6:
					Toast.makeText(context, notifyInfodelete, 1).show();

					break;
				default:
					break;
				}

			}
		};

		class ViewHolder {
			ImageView accountImage;
			TextView accountUserName, accountUserNum;
			TextView accountSetDefault;
			LinearLayout account_user_set_linearlayout;
		}
	}
}
