package com.tongmeng.alliance.wxapi;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import com.tongmeng.alliance.activity.PayActivity;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.KeelLog;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WXPayEntryActivity extends JBaseActivity
// implements IWXAPIEventHandler
{
	private static final String TAG = "WXPayEntryActivity::";
	// 界面
	LinearLayout successLayout;
	TextView faileView;
//	public IWXAPI api;
	String APP_ID;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				if (faileView.getVisibility() == View.VISIBLE) {
					faileView.setVisibility(View.GONE);
				}
				if (successLayout.getVisibility() == View.GONE) {
					successLayout.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				sendToServer();
				break;
			case 2:
				if (faileView.getVisibility() == View.GONE) {
					faileView.setVisibility(View.VISIBLE);
				}
				if (successLayout.getVisibility() == View.VISIBLE) {
					successLayout.setVisibility(View.GONE);
				}
				break;

			case 3:
				if (APP_ID != null && !"".equals(APP_ID)) {
					initValue();
				}
				break;
			case 4:
				break;
			default:
				break;
			}
		};
	};

	public void initValue(){
//		api = WXAPIFactory.createWXAPI(this, APP_ID);
//        api.handleIntent(getIntent(), this);
		initView();	
	}
	
	public void initView() {
		successLayout = (LinearLayout) findViewById(R.id.pay_result_success);
		faileView = (TextView) findViewById(R.id.pay_result_faile);
	}
	
	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.pay_result);
		initTitle();
		getConfigInfo("weixinAppAppid");
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "支付结果",
				false, null, false, true);
	}

	/**
	 * 
	 * @param key
	 *            要获取字段在服务器中的名字
	 * @param filed
	 *            本界面记录字段的变量
	 */
	public void getConfigInfo(final String key) {
		new Thread() {
			@SuppressWarnings("unused")
			public void run() {
				String param = "{\"key\":\"" + key + "\"}";
				KeelLog.e(TAG, "getConfigInfo  param::" + param);
				String result = HttpRequestUtil.sendPost(
						Constant.getCinfigPath, param, WXPayEntryActivity.this);
				KeelLog.e(TAG, "getConfigInfo  result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				KeelLog.e(TAG, "getConfigInfo  dao::" + dao.toString());
				if (dao == null) {
					Message msg = new Message();
					msg.what = 3;
					msg.obj = "获取配置文件出错";
					handler.sendMessage(msg);
				} else {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() == null
								|| "".equals(dao.getResponseData())
								|| "null".equals(dao.getResponseData())) {
							Message msg = new Message();
							msg.what = 3;
							msg.obj = "获取配置文件出错";
							handler.sendMessage(msg);

						} else {

							String temp = getConfigValue(dao.getResponseData());
							KeelLog.e(TAG, "temp::" + temp);
							if (temp == null || "".equals(temp)) {
								Message msg = new Message();
								msg.what = 3;
								msg.obj = "获取配置文件出错";
								handler.sendMessage(msg);
							} else {
								KeelLog.e(TAG, "key::" + key);
								KeelLog.e(TAG, "value" + temp);
								if (key.equals("weixinAppAppid")) {
									APP_ID = temp;
								} else if (key.equals("weixinAppMchId")) {
								} else if (key.equals("weixinAppApiKey")) {
								}
								handler.sendEmptyMessage(2);
							}
						}
					} else {
						Message msg = new Message();
						msg.what = 3;
						msg.obj = "获取配置文件出错,出错原因:" + dao.getNotifyInfo();
						handler.sendMessage(msg);
					}
				}
			};
		}.start();
	}

	// 解析服务器配置数据
	public String getConfigValue(String responseData) {
		try {
			JSONObject job = new JSONObject(responseData);
			return job.getString("value");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendToServer(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "");
		map.put("activityId", PayActivity.activityId);
		String url = "http://192.168.101.140:8801/deal/afterPaied.json";
		String param = Utils.simpleMapToJsonStr(map);
		Log.e("", "param:" + param);
		String result = HttpRequestUtil.sendPost(url, param,
				WXPayEntryActivity.this);
		boolean result1 = getApplyResult(result);
		Log.e("", "result1::"+result1);
		if (result1) {
			handler.sendEmptyMessage(0);
		} else {
			handler.sendEmptyMessage(1);
		}
	}
	
	/**
	 * 获取报名结果
	 * 
	 * @param result
	 * @return
	 */
	public static boolean getApplyResult(String result) {
		Log.e("", "报名结果返回：" + getContent(result));
		try {
			JSONObject obj = new JSONObject(getContent(result));
			Log.e("", "报名结果：" + obj.getBoolean("succeed"));
			return obj.getBoolean("succeed");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 调用结果成功则获取返回数据中 responseData的数据，失败返回提示信息
	 * 
	 * @param result
	 * @return
	 */
	public static String getContent(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String notification = obj.getString("notification");
			if (TextUtils.equals("0001",
					new JSONObject(notification).getString("notifyCode"))) {
				return obj.getString("responseData");
			} else {
				return new JSONObject(notification).getString("notifyInfo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
//	@Override
//	public void onReq(BaseReq req) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onResp(BaseResp resp) {
//		// TODO Auto-generated method stub
//		
////		Toast.makeText(getApplicationContext(), "支付获取返回值::"+resp.errCode, 1).show();
//		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
//		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			new Thread() {
//				public void run() {
//					sendToServer();
//				};
//			}.start();
//		}else{
//			handler.sendEmptyMessage(2);
//		}
//	}
}
