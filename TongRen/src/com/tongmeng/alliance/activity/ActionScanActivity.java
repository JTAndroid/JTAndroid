package com.tongmeng.alliance.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tongmeng.alliance.zxinglib.ZXingScannerViewNew;
import com.tr.R;
import com.utils.log.KeelLog;

public class ActionScanActivity extends Activity implements
		ZXingScannerViewNew.ResultHandler, ZXingScannerViewNew.QrSize
		{
	public String TAG = "----ScanActivity----";

	ZXingScannerViewNew scanView;
	private TextView resultText;
	LinearLayout layout;
	private String activityId, index, role;

	// 头部
	// ImageView backImg, moreImg;
	// TextView titleText;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 获取签到码成功
				String str = msg.obj + "";
				if (str.equals("您已签到成功！")) {// 已经签到
					signSuccess();
				} else {// 获取签到码成功
					sign(str);
				}
				break;
			case 1:// 获取签到码失败
				resultText.setText(msg.obj + "");
				break;
			case 2:// 签到成功
				signSuccess();
				break;
			case 3:// 签到失败
				resultText.setText(msg.obj + "");
				break;
			case 4:// app扫描上传名单二维码成功
				Toast.makeText(ActionScanActivity.this, "app扫描上传名单二维码成功", 0).show();
				finish();
				break;
			case 5:// app扫描上传名单二维码失败
				resultText.setText(msg.obj + "");
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scanView = new ZXingScannerViewNew(this);
		scanView.setContentView(R.layout.logistics_scan_qr);
		scanView.setQrSize(this);
		setContentView(scanView);
		setupFormats();
		initUI();
	}

	private void initUI() {
		KeelLog.e(TAG, "initUI()");
		resultText = (TextView) findViewById(R.id.scan_resultText);

		layout = (LinearLayout) findViewById(R.id.scan_buttomLayout);
		// // 头部
		// backImg = (ImageView) findViewById(R.id.index_top_seartchImg);
		// KeelLog.e(TAG, "backImg:"+backImg);
		// moreImg = (ImageView) findViewById(R.id.index_top_moreImg);
		// titleText = (TextView) findViewById(R.id.index_top_nameText);
		// backImg.setBackgroundResource(R.drawable.icon_back);
		// backImg.setOnClickListener(this);
		// moreImg.setVisibility(View.VISIBLE);
		// titleText.setText("扫码");

		role = getIntent().getStringExtra("role");// role的值：扫一扫是“-1”；签到是“1”或“2”，1为成员，2为群主
		index = getIntent().getStringExtra("index");// index的值：扫一扫是“more”,签到界面是“sign”,上传名单是“load”
		activityId = getIntent().getStringExtra("activityId");
		if (!role.equals("-1")) {// 从签到界面跳转过来，传值antivityId，需要和扫码得到的activityId判断:
			activityId = getIntent().getStringExtra("activityId");
		} else if (role.equals("-1")) {// 主界面扫一扫跳转过来的，直接获取扫码得到的activityId

		}

	}

	@Override
	protected void onResume() {
		KeelLog.e(TAG, "onResume()");
		super.onResume();
		scanView.setResultHandler(this);
		scanView.startCamera(-1);
		scanView.setFlash(false);
		scanView.setAutoFocus(true);
	}

	/**
	 * 包含“signInCode://activityId=”字段，说明是活动签到二维码，截取“=”后字段，为活动id，然后获取签到码，再进行签到
	 * 
	 * @param rawResult
	 */
	@Override
	public void handleResult(Result rawResult) {
		KeelLog.e(TAG, "handleResult() rawResult::" + rawResult);
		// Toast.makeText(QrScanActivity.this, "当前返回数据是：" + rawResult,
		// 0).show();
		final String resultStr = rawResult + "";
		KeelLog.e(TAG, "handleResult() resultStr::" + resultStr);

		if (resultStr.contains("signInCode://activityId=")) {
			KeelLog.e(TAG, "contains   signInCode://activityId=  ");
			String tempActivityId = resultStr
					.substring(resultStr.indexOf("//activityId=") + 13,
							resultStr.length());
			KeelLog.e(TAG, "tempActivityId::" + tempActivityId);
			if (index.equals("sign")) {
				if (!activityId.equals(tempActivityId)) {
					// resultText.setText("扫码的活动不是当前活动");
					Toast.makeText(ActionScanActivity.this, "扫码的活动不是当前活动", 0).show();
					return;
				}
			}
			activityId = tempActivityId;
			getSignInCode(activityId);
		} else {

			KeelLog.e(TAG, "not  contains   signInCode://activityId=  ");
			if (index.equals("load")) {
				if (resultStr == null || "".equals(resultStr)) {
					Toast.makeText(ActionScanActivity.this, "扫描结果为空", 0).show();
				} else {
					// APP扫描上传名单二维码
					new Thread() {
						@SuppressWarnings({ "null", "unused" })
						public void run() {
							String loadParam = "{\"activityId\":\""
									+ activityId + "\",\"code\":\"" + resultStr
									+ "\"}";
							KeelLog.e(TAG, "loadParam::" + loadParam);
							String result = HttpRequestUtil.sendPost(
									Constant.appLoadPath, loadParam,
									ActionScanActivity.this);
							KeelLog.e(TAG, "result::" + result);
							ServerResultDao dao = Utils.getServerResult(result);
							KeelLog.e(TAG, "load  dao:" + dao.toString());
							if (dao == null) {
								Message msg = new Message();
								msg.what = 5;
								msg.obj = "app扫描上传名单二维码失败";
								handler.sendMessage(msg);
							} else {

								if (dao.getNotifyCode().equals("0001")) {
									boolean isLoad = signResult(dao
											.getResponseData());
									KeelLog.e(TAG, "isLoad：" + isLoad);
									if (isLoad) {
										KeelLog.e(TAG, "APP扫描上传名单二维码成功");
										handler.sendEmptyMessage(4);
									} else {
										Message msg = new Message();
										msg.what = 5;
										msg.obj = "app扫描上传名单二维码失败";
										handler.sendMessage(msg);
									}
								} else {
									Message msg = new Message();
									msg.what = 5;
									msg.obj = "app扫描上传名单二维码失败,失败原因："
											+ dao.getNotifyInfo();
									handler.sendMessage(msg);
								}
							}

						};

					}.start();
				}
			} else {

				if (index.equals("sign")) {
					if (!activityId.equals(rawResult + "")) {
						resultText.setText("扫码的活动不是当前活动");
						return;
					}
				}

				getSignInCode(activityId);
			}
		}
	}

	@Override
	protected void onPause() {
		KeelLog.e(TAG, "onPause()");
		super.onPause();
		scanView.stopCamera();
	}

	public void setupFormats() {
		KeelLog.e(TAG, "setupFormats()");
		List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
		formats.add(BarcodeFormat.QR_CODE);
		if (scanView != null) {
			scanView.setFormats(formats);
		}
	}

	@Override
	public Rect getDetectRect() {
		KeelLog.e(TAG, "getDetectRect()");
		View view = findViewById(R.id.scan_window);
		int top = ((View) view.getParent()).getTop() + view.getTop();
		int left = view.getLeft();
		int width = view.getWidth();
		int height = view.getHeight();
		Rect rect = null;
		if (width != 0 && height != 0) {
			rect = new Rect(left, top, left + width, top + height);
			addLineAnim(rect);
		}
		return rect;
	}

	private void addLineAnim(Rect rect) {
		KeelLog.e(TAG, "addLineAnim()");
		ImageView imageView = (ImageView) findViewById(R.id.scanner_line);
		imageView.setVisibility(View.VISIBLE);
		if (imageView.getAnimation() == null) {
			TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
					rect.height());
			anim.setDuration(1500);
			anim.setRepeatCount(Animation.INFINITE);
			imageView.startAnimation(anim);
		}
	}

	// 获取签到码
	public void getSignInCode(final String activityId) {
		new Thread() {
			public void run() {
				String param = "{\"activityId\":\"" + activityId + "\"}";
				KeelLog.e(TAG, "getSignInCode  param::" + param);
				String result = HttpRequestUtil.sendPost(
						Constant.getSignInCodePath, param, ActionScanActivity.this);
				KeelLog.e(TAG, "getSignInCode  result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao == null) {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = "获取签到码失败";
					handler.sendMessage(msg);
				} else {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())) {
							Map<String, String> tempMap = new HashMap<String, String>();
							tempMap = getSignCode(dao.getResponseData());
							KeelLog.e(TAG,
									"isSignIn::" + tempMap.get("isSignIn")
											+ ",signInCode::"
											+ tempMap.get("signInCode"));
							if (tempMap != null && tempMap.size() > 0) {
								if (tempMap.get("isSignIn").equals("1")) {// 已签到
									Message msg = new Message();
									msg.what = 0;
									msg.obj = "您已签到成功！";
									handler.sendMessage(msg);
								} else if (tempMap.get("isSignIn").equals("0")) {// 未签到
									Message msg = new Message();
									msg.what = 0;
									msg.obj = tempMap.get("signInCode");
									handler.sendMessage(msg);
								}
							} else {
								Message msg = new Message();
								msg.what = 1;
								msg.obj = "获取签到码失败";
								handler.sendMessage(msg);
							}
						}
					} else {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = "获取签到码失败,失败原因：" + dao.getNotifyInfo();
						handler.sendMessage(msg);
					}
				}
			};
		}.start();
	}

	//
	// 解析获取签到码数据
	public Map<String, String> getSignCode(String param) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject job = new JSONObject(param);
			String signInCode = job.getString("signInCode");
			String isSignIn = job.getString("isSignIn");
			if (signInCode != null && !"".equals(signInCode)) {
				map.put("signInCode", signInCode);
			}
			if (isSignIn != null && !"".equals(isSignIn)) {
				map.put("isSignIn", isSignIn);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

	public void signSuccess() {
		Intent intent = new Intent(ActionScanActivity.this, SignAcivity.class);
		intent.putExtra("activityId", activityId);
		intent.putExtra("index", index);
		intent.putExtra("role", role);
		startActivity(intent);
		finish();
	}

	// 签到
	public void sign(final String code) {
		new Thread() {
			public void run() {
				String param = "{\"activityId\":\"" + activityId
						+ "\",\"code\":\"" + code + "\"}";
				KeelLog.e(TAG, "onClick  param::" + param);
				String result = HttpRequestUtil.sendPost(Constant.signPath,
						param, ActionScanActivity.this);
				KeelLog.e(TAG, "onClick  result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao == null) {
					Message msg = new Message();
					msg.what = 3;
					msg.obj = "签到失败";
					handler.sendMessage(msg);
				} else {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())
								&& !"null".equals(dao.getResponseData())) {
							boolean isSigned = signResult(dao.getResponseData());
							if (isSigned) {
								handler.sendEmptyMessage(2);
							} else {
								Message msg = new Message();
								msg.what = 3;
								msg.obj = "签到失败";
								handler.sendMessage(msg);
							}
						} else {
							Message msg = new Message();
							msg.what = 3;
							msg.obj = "签到失败";
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = 3;
						msg.obj = "签到失败,失败原因:" + dao.getNotifyInfo();
						handler.sendMessage(msg);
					}
				}
			};
		}.start();
	}

	// 签到结果
	public boolean signResult(String result) {
		KeelLog.e(TAG, "signResult result::" + result);
		try {
			JSONObject job = new JSONObject(result);
			return job.getBoolean("succeed");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
}
