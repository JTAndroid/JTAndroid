package com.tongmeng.alliance.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongmeng.alliance.dao.ApplyForm;
import com.tongmeng.alliance.dao.ApplyLabel;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.dao.option;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.App;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.KeelLog;

/*报名界面
 * */
public class ApplyActivity extends JBaseActivity implements OnClickListener {

	TextView starttitletext, endtitletext, titletext, locationText, priceText;
	EditText askEdit;
	Button payBtn;
	LinearLayout otherLayout;

	List<EditText> list = new ArrayList<EditText>();

	String activityId, sexStr = null;
	ApplyForm applyForm = new ApplyForm();// 报名表单
	List<ApplyLabel> applyLabelList = new ArrayList<ApplyLabel>();// 报名表单中的报名信息
	ArrayList<option> optionList = new ArrayList<option>();// 报名信息中的单选框

	List<ImageView> checkList = new ArrayList<ImageView>();// 性别
	List<EditText> editList = new ArrayList<EditText>();// 如果有输入框，用此list保存
	List<String> nameList = new ArrayList<String>();// 报名表信息名称

	String phoneNumber = "";
	// 判断输入框是否都填写了
	boolean intentIndex = true;
	Message message;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				initData();
				break;
			case 1:
				Toast.makeText(ApplyActivity.this, msg.obj + "", 0).show();
				break;
			case 2:
				/**
				 * 报名成功，先判断手机号是否相同，然后判断是否付费
				 */
				if (phoneNumber != null && !"".equals(phoneNumber)) {
					if (phoneNumber.equals(App.getApp().getUser().mMobile)) {// 手机号相同，则继续判断是否免费活动
						activityIntent();
					} else {// 手机号不同，先进行验证
						Intent intent = new Intent(ApplyActivity.this,
								ApplyVerifyActivity.class);
						intent.putExtra("activityId", activityId);
						intent.putExtra("title", titletext.getText().toString());
						intent.putExtra("starttime", starttitletext.getText()
								.toString());
						intent.putExtra("endtime", endtitletext.getText()
								.toString());
						intent.putExtra("location", locationText.getText()
								.toString());
						intent.putExtra("price", priceText.getText().toString());
						startActivity(intent);
						finish();
					}
				} else {// 没有手机号输入框，报名成功后，判断是否付费活动，付费则跳转支付界面，免费则提示成功
					activityIntent();
				}
				break;
			case 3:
				Toast.makeText(ApplyActivity.this, msg.obj + "", 0).show();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_action_apply_payBtn:
			intentIndex = true;
			for (int i = editList.size() - 1; i >= 0; i--) {
				String textStr = editList.get(i).getText().toString();
				Log.e("", "textStr:" + textStr);
				Log.e("", "nameList.get(" + i + "):" + nameList.get(i));
				if (textStr == null || "".equals(textStr)
						|| textStr.equals(nameList.get(i))) {
					Toast.makeText(ApplyActivity.this,
							"请输入您的" + nameList.get(i) + "信息", 0).show();
					intentIndex = false;
				}
			}
			for (int i = 0; i < editList.size(); i++) {
				if (nameList.get(i).equals("手机")) {
					phoneNumber = editList.get(i).getText().toString();
					KeelLog.e("", "当前输入的phoneNumber：" + phoneNumber);
				}
			}

			if (intentIndex) {
				supply();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_action_apply);
		initTitle();
		initView();
		// 获取intent传递的数据
		getIntentParams();
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "活动报名",
				false, null, false, true);
	}

	// 获取控件
	private void initView() {

		titletext = (TextView) findViewById(R.id.activity_action_apply_titleText);
		starttitletext = (TextView) findViewById(R.id.activity_action_apply_action_startTimeText);
		endtitletext = (TextView) findViewById(R.id.activity_action_apply_action_endTimeText);
		locationText = (TextView) findViewById(R.id.activity_action_apply_actionLocationText);
		priceText = (TextView) findViewById(R.id.activity_action_apply_priceText);

		otherLayout = (LinearLayout) findViewById(R.id.activity_action_apply_otherLayout);

		askEdit = (EditText) findViewById(R.id.activity_action_apply_askEdit);
		payBtn = (Button) findViewById(R.id.activity_action_apply_payBtn);
		payBtn.setOnClickListener(this);
	}

	// 判断是否付费活动，免费直接提示成功，付费跳转支付
	public void activityIntent() {
		String fee = applyForm.getFee();
		if (fee != null && !"".equals(fee)) {
			if (fee.equals("免费")) {
				Toast.makeText(ApplyActivity.this, "恭喜您报名成功！祝您玩的愉快！", 0).show();
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			} else {
				float price = Float.parseFloat(fee);
				if (price == 0) {
					Toast.makeText(ApplyActivity.this, "恭喜您报名成功！祝您玩的愉快！", 0)
							.show();
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Intent intent = new Intent(ApplyActivity.this,
							PayActivity.class);
					intent.putExtra("activityId", activityId);
					startActivity(intent);
					finish();
				}
			}
		}
	}

	private void initData() {
		KeelLog.e("", "applyForm::" + applyForm);
		titletext.setText(applyForm.getTitle());
		starttitletext.setText(applyForm.getStartTime());
		endtitletext.setText(applyForm.getEndTime());
		locationText.setText(applyForm.getAddress());
		priceText.setText(applyForm.getFee() + "");

		applyLabelList = applyForm.getList();

		if (applyLabelList.size() != 0) {
			for (int i = 0; i < applyLabelList.size(); i++) {
				ApplyLabel appLayble = applyLabelList.get(i);
				Log.e("", "appLayble:" + appLayble);

				if (appLayble.getOption() != "null") {
					LinearLayout layout = (LinearLayout) LayoutInflater.from(
							ApplyActivity.this).inflate(
							R.layout.apply_checkbox, null);
					final ImageView manImg = (ImageView) layout
							.findViewById(R.id.apply_manImg);
					final ImageView womenImg = (ImageView) layout
							.findViewById(R.id.apply_womenImg);
					sexStr = "男";
					manImg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (sexStr.equals("女")) {
								manImg.setBackgroundResource(R.drawable.icon_checked1);
								womenImg.setBackgroundResource(R.drawable.icon_unchecked2);
							}
							sexStr = "男";
						}
					});
					womenImg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (sexStr.equals("男")) {
								manImg.setBackgroundResource(R.drawable.icon_unchecked2);
								womenImg.setBackgroundResource(R.drawable.icon_checked1);
							}
							sexStr = "女";
						}
					});
					checkList.add(manImg);
					checkList.add(womenImg);
					otherLayout.addView(layout);

				} else {
					LinearLayout layout = (LinearLayout) LayoutInflater.from(
							this).inflate(R.layout.apply_edittext, null);
					EditText text = (EditText) layout
							.findViewById(R.id.apply_edittext_infoName);
					text.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							appLayble.getLimit()) });
					text.setHint(appLayble.getName());
					nameList.add(appLayble.getName());
					otherLayout.addView(layout);
					editList.add(text);
				}
			}
		}
	}

	// 获取数据
	private void getIntentParams() {
		activityId = getIntent().getStringExtra("activityId");
		Log.e(TAG, "activityId:" + activityId);

		new Thread() {
			public void run() {
				String param = "{\"activityId\":\"" + activityId + "\"}";
				Log.e("", "param:" + param);
				String result = HttpRequestUtil
						.sendPost(Constant.searchApplyFormPath, param,
								ApplyActivity.this);
				Log.e("", "查询报名表单 result:" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao == null) {
					Message message = new Message();
					message.what = 1;
					message.obj = "查询报名表单信息失败，请重试！";
					handler.sendMessage(message);
				} else {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() == null
								|| "".equals(dao.getResponseData())) {
							Message message = new Message();
							message.what = 1;
							message.obj = "查询报名表单信息失败，请重试！";
							handler.sendMessage(message);
						} else {
							applyForm = getApplyForm(dao.getResponseData());
							if (applyForm == null) {
								Message message = new Message();
								message.what = 1;
								message.obj = "查询报名表单信息失败，请重试！";
								handler.sendMessage(message);
							} else {
								handler.sendEmptyMessage(0);
							}
						}
					} else {
						Message message = new Message();
						message.what = 1;
						message.obj = "查询报名表单信息失败，失败原因：" + dao.getNotifyInfo()
								+ "，请重试！";
						handler.sendMessage(message);
					}
				}

				// applyForm = Utils.getApplyForm(temResult);
				// if (applyForm != null) {
				// handler.sendEmptyMessage(0);
				// } else {
				// handler.sendEmptyMessage(1);
				// }

			};
		}.start();
	}

	public ApplyForm getApplyForm(String responseData) {
		ApplyForm form = new ApplyForm();
		try {
			JSONObject reObj = new JSONObject(responseData);
			String result = reObj.getString("applyForm");
			JSONObject job = new JSONObject(result);
			form.id = job.getInt("id");
			form.fee = job.getString("fee");
			form.startTime = job.getString("startTime");
			form.title = job.getString("title");
			form.address = job.getString("address");
			form.province = job.getString("province");
			form.endTime = job.getString("endTime");
			form.city = job.getString("city");
			String listStr = job.getString("applyLabelList");
			form.list = new ArrayList<ApplyLabel>();
			if (listStr != null && !"".equals(listStr)) {
				JSONArray array = new JSONArray(listStr);
				JSONObject jsonObj;
				for (int i = 0; i < array.length(); i++) {
					ApplyLabel label = new ApplyLabel();
					/**
					 * {"limit":10,"type":1,"optionList" :null,"name":"姓名"}
					 */
					jsonObj = (JSONObject) array.get(i);
					Log.e("Util>>>>>>>>>", "jsonObj::" + jsonObj.toString());

					label.limit = jsonObj.getInt("limit");
					label.type = jsonObj.getInt("type");
					label.name = jsonObj.getString("name");
					label.option = jsonObj.getString("optionList");

					form.list.add(label);
				}
			} else {
				form.list = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

		return form;
	}

	/**
	 * 报名方法
	 */
	public void supply() {
		if (intentIndex) {// 如果输入框都填写了内容
			// 调用报名接口
			new Thread() {
				public void run() {
					String param = getString(activityId, nameList, editList,
							sexStr);
					Log.e("", "报名param:" + param);
					String result = HttpRequestUtil.sendPost(
							Constant.applyPath, param, ApplyActivity.this);
					Log.e("", "报名回调结果：" + result);
					/**
					 * 报名成功： {"responseData":{"succeed":true},"notification":{
					 * "notifyCode":"0001","notifyInfo":"调用接口成功"}}
					 * 
					 * 报名失败：{"notification":{"notifCode": "0003","notifInfo":
					 * "请登录"}}
					 */
					if (result.contains("responseData")) {
						try {
							JSONObject job = new JSONObject(result);
							JSONObject notification = job
									.getJSONObject("notification");
							if (notification.getString("notifyCode").equals(
									"0001")) {
								String responseData = job
										.getString("responseData");
								if (responseData != null
										&& !"".equals(responseData)
										&& !"null".equals(responseData)) {
									JSONObject tempObj = new JSONObject(
											responseData);
									boolean isSuccess = tempObj
											.getBoolean("succeed");
									KeelLog.e(TAG, "isSuccess:" + isSuccess);
									if (isSuccess) {
										handler.sendEmptyMessage(2);
									} else {
										Message msg = new Message();
										msg.what = 3;
										msg.obj = "报名失败！";
										handler.sendMessage(msg);
									}
								} else {
									Message msg = new Message();
									msg.what = 3;
									msg.obj = "报名失败";
									handler.sendMessage(msg);
								}
							} else {
								Message msg = new Message();
								msg.what = 3;
								msg.obj = "报名失败，失败原因："
										+ notification.getString("notifyInfo");
								handler.sendMessage(msg);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					} else {
						try {
							JSONObject job = new JSONObject(result);
							JSONObject notification = job
									.getJSONObject("notification");
							if (!notification.getString("notifCode").equals(
									"0001")) {
								Message msg = new Message();
								msg.what = 3;
								msg.obj = "报名失败，失败原因："
										+ notification.getString("notifInfo")
										+ ",请重试！";
								handler.sendMessage(msg);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				};
			}.start();
		}
	}
	
	public static String getString(String activityId, List<String> nameList,
			List<EditText> valueList, String sex) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"activityId\":\"" + activityId + "\",\"applyList\":[");
		for (int i = 0; i < nameList.size(); i++) {
			sb.append("{\"key\":\"" + nameList.get(i) + "\",\"value\":\""
					+ valueList.get(i).getText().toString() + "\"},");
		}
		if (sex == null || sex.equals("")) {

		} else {
			sb.append("{\"key\":\"性别\",\"value\":\"" + sex + "\"},");
		}
		String s = sb.toString();
		return s.substring(0, s.lastIndexOf(",")) + "]}";
	}
}
