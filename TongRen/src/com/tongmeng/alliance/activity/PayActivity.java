package com.tongmeng.alliance.activity;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.tencent.mm.sdk.modelpay.PayReq;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tongmeng.alliance.dao.PayForm;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.MD5;
import com.tongmeng.alliance.util.PayUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.KeelLog;

public class PayActivity extends JBaseActivity implements OnClickListener{

	TextView nameText, phpneText, titleText, priceText, numText, timeText,
			allpriceText;
	ImageView zfbBox, wxBox;
	Button payBtn;
	String time, index = "zfb", address;
	public static String activityId;

	PayForm payform;
//	PayReq req;
//	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	Map<String, String> resultunifiedorder = new HashMap<String, String>();
	StringBuffer sb;
	String APP_ID, MCH_ID, API_KEY;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				initValue();
				break;
			case 1:
				Toast.makeText(PayActivity.this, "获取支付表单信息失败，请重试", 0).show();
				break;
			case 2:// 获取配置信息成功
				if(APP_ID != null && !"".equals(APP_ID) && MCH_ID!= null && !"".equals(MCH_ID) && API_KEY!=null && !"".equals(API_KEY)){
					getFormNo();
				}
				break;
			case 3:// 获取配置信息失败
				Toast.makeText(PayActivity.this, msg.obj+"", 0).show();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_action_pay);
		initTitle();

		activityId = getIntent().getStringExtra("activityId");
		initView();
//		req = new PayReq();
		sb = new StringBuffer();
		// 获取订单信息

		getConfigInfo("weixinAppAppid");
		getConfigInfo("weixinAppMchId");
		getConfigInfo("weixinAppApiKey");
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "支付界面",
				false, null, false, true);
	}
	public void getFormNo() {
		new Thread() {
			public void run() {

				String typeUrl = "http://192.168.101.140:8801/deal/getPayForm.json";
				String idParam = "{\"activityId\":\"" + activityId + "\"}";
				Log.e("", "idParam::" + idParam);
				String result = HttpRequestUtil.sendPost(typeUrl, idParam,
						PayActivity.this);
				Log.e("", "result::" + result);
				payform = Utils.getPayForm(result);
				Log.e("", "payform::" + payform);
				if (payform != null) {
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(1);
				}
			};
		}.start();
	}

	public void initView() {

		nameText = (TextView) findViewById(R.id.activity_action_pay_nameText);
		phpneText = (TextView) findViewById(R.id.activity_action_pay_phoneText);
		titleText = (TextView) findViewById(R.id.activity_action_pay_titleText);
		priceText = (TextView) findViewById(R.id.activity_action_pay_priceText);
		numText = (TextView) findViewById(R.id.activity_action_pay_NoText);
		timeText = (TextView) findViewById(R.id.activity_action_pay_timeText);
		allpriceText = (TextView) findViewById(R.id.activity_action_pay_totalpriceText);

		zfbBox = (ImageView) findViewById(R.id.activity_action_pay_zfbCheck);
		wxBox = (ImageView) findViewById(R.id.activity_action_pay_wxCheck);
		zfbBox.setOnClickListener(this);
		wxBox.setOnClickListener(this);

		payBtn = (Button) findViewById(R.id.activity_action_pay_payBtn);

		payBtn.setOnClickListener(this);
	}

	// 给控件设值
		public void initValue() {
			titleText.setText(payform.getTitle());
			nameText.setText(payform.getName());
			timeText.setText(payform.getStartTime());
			priceText.setText(payform.getFee());
			allpriceText.setText(payform.getFee());
			// nameText.setText(userName);
			phpneText.setText(payform.getMobile());

		}
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_action_pay_zfbCheck:
			if (index.equals("wx")) {
				zfbBox.setBackgroundResource(R.drawable.icon_checked1);
				wxBox.setBackgroundResource(R.drawable.icon_unchecked2);
				index = "zfb";
			}
			break;

		case R.id.activity_action_pay_wxCheck:
			if (index.equals("zfb")) {
				wxBox.setBackgroundResource(R.drawable.icon_checked1);
				zfbBox.setBackgroundResource(R.drawable.icon_unchecked2);
				index = "wx";
			}
			break;

		case R.id.activity_action_pay_payBtn:
			GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
			getPrepayId.execute();

			break;
		default:
			break;
		}
	}
	
	/**
	 * 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", "----" + packageSign);
		return packageSign;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes());
		Log.e("orion", "----" + appSign);
		return appSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", "----" + sb.toString());
		return sb.toString();
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PayActivity.this,
					getString(R.string.app_tip),
					getString(R.string.getting_prepayid));
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			Log.e("", "---prepay_id----" + result.get("prepay_id"));
			sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");

			resultunifiedorder = result;
			genPayReq();
			sendPayReq();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion", "--entity--" + entity);

			byte[] buf = PayUtil.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", "--content--" + content);
			Map<String, String> xml = decodeXml(content);

			return xml;
		}
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", "----" + e.toString());
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", APP_ID));
			packageParams.add(new BasicNameValuePair("body", "APP pay test"));
			packageParams
					.add(new BasicNameValuePair("mch_id", MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url",
					"http://192.168.101.140:8801/deal/afterPaied.json"));
			packageParams.add(new BasicNameValuePair("out_trade_no",
					genOutTradNo()));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", (int) (Float
					.parseFloat(payform.getFee()) * 100) + ""));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return xmlstring;

		} catch (Exception e) {
			return null;
		}

	}

	private void genPayReq() {

//		req.appId = APP_ID;
//		req.partnerId = MCH_ID;
//		req.prepayId = resultunifiedorder.get("prepay_id");
//		req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
//		req.nonceStr = genNonceStr();
//		req.timeStamp = String.valueOf(genTimeStamp());
//
//		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//		signParams.add(new BasicNameValuePair("appid", req.appId));
//		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//		signParams.add(new BasicNameValuePair("package", req.packageValue));
//		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
//		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
//		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//
//		req.sign = genAppSign(signParams);
//		sb.append("sign\n" + req.sign + "\n\n");
//		Log.e("orion", "--signParams--" + signParams.toString());
	}

	private void sendPayReq() {

//		msgApi.registerApp(APP_ID);
//		msgApi.sendReq(req);
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
						Constant.getCinfigPath, param, PayActivity.this);
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
							KeelLog.e(TAG, "temp::"+temp);
							if (temp == null || "".equals(temp)) {
								Message msg = new Message();
								msg.what = 3;
								msg.obj = "获取配置文件出错";
								handler.sendMessage(msg);
							} else {
								KeelLog.e(TAG, "key::"+key);
								KeelLog.e(TAG, "value"+temp);
								if(key.equals("weixinAppAppid")){
									APP_ID = temp;
								}else if(key.equals("weixinAppMchId")){
									MCH_ID = temp;
								}else if(key.equals("weixinAppApiKey")){
									API_KEY = temp;
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
}
