package com.tongmeng.alliance.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.tr.App;
import com.tr.R;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.RequirementMini;
import com.tr.model.user.OrganizationMini;
import com.tr.navigate.ENavigate;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.tr.ui.conference.initiatorhy.InitiatorHYActivity;
import com.tr.ui.conference.initiatorhy.IniviteUtil;
import com.tr.ui.knowledge.MyKnowledgeActivity;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongmeng.alliance.dao.Meeting;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tongmeng.alliance.view.SimpleBottomDialog;
import com.utils.common.EConsts;
import com.utils.log.KeelLog;
import com.utils.time.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 发布活动
 * */
public class ReleaseActivity extends Activity implements OnClickListener {
	public String TAG = "----ReleaseActivity----";

	LinearLayout tagLayout, stimeLayout, etimeLayout, gridLayout, tagLayout1,
			locationLayout, priceLayout, gridviewLayout, setaccountLayout,
			relevanceLayout, treeLayout, labelLayout, moduleLayout,
			module_catalogLayout, module_tagLayout;
	// 人脉中好友和合作方的layout
	RelativeLayout friendLayout, partnerLayout;
	// 四大组件中的删除ImageView
	ImageView friendIv, partnerIv, organizeIv, knowledgeIv, affairIv;
	// 四大组件及“目录”、“标签”中的textView
	TextView friendTv, partnerTv, organizeTv, knowledgeTv, affairTv,
			tree_numTv, tree_contentTv, tag_contentTv;

	Button releaseBtn, changeImgBtn;
	ImageView chooseImg, priceChooseImg, backImg;
	TextView setAccountText, priceNoText, infomationText, textview,
			startTimeEdit, endTimeEdit, locationEdit;
	// introduceEdit,
	EditText themeEdit, personNoEdit;
	GridView gridview;
	// Switch mySwitch;
	InfoAdapter adapter;
	Button openBtn;
	Bitmap bitmap;
	LinearLayout acticvity_linearlayout;
	String type, Imgid = "0", positionX, positionY, province, city, detailStr,
			addressName;
	ArrayList<String> tagList = new ArrayList<String>(),// list为活动标签list
			infoList = new ArrayList<String>();// 报名信息list
	int[] images = { R.drawable.bg_0001, R.drawable.bg_0002,
			R.drawable.bg_0003, R.drawable.bg_0004, R.drawable.bg_0005,
			R.drawable.bg_0006 };
	String activity_profile;
	Map<String, String> map = new HashMap<String, String>();
	WebView myWebView_show;
	String activityDes;

	String applyLabelListStr;

	boolean contentIndex = false;

	/** 对接资源所用到的关键字 */
	private String keyWord;
	private String peopleRelatedkeyWord = "";
	private String orgRelatedkeyWord = "";
	private String knowledgeRelatedkeyWord = "";
	private String requarementRelatedkeyWord = "";

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 获取数据失败或者发布失败
				Toast.makeText(ReleaseActivity.this, msg.obj + "", 0).show();
				break;
			case 1:// 编辑活动时，获取数据成功，加载数据
				initValue();
				break;
			case 2:// 发布活动成功
				Toast.makeText(ReleaseActivity.this, "" + msg.obj, 0).show();
				finish();
				break;
			case 3:// 加载html数据
				activityDes = msg.obj + "";
				String path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/myhtml/actiondetail.html";
				File file = new File(path);
				try {
					boolean isCreate = Utils.createFile(file);
					String html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body>"
							+ msg.obj + "</body></html>";

					OutputStreamWriter out = new OutputStreamWriter(
							new FileOutputStream(file), "UTF-8");
					out.write(html.toCharArray());
					out.flush();
					out.close();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				myWebView_show.loadUrl("file:///" + path);
				break;
			default:
				break;
			}
		};
	};

	String activityId;
	Meeting meeting;

	public boolean isOpen = true;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_action_start);

		activityId = getIntent().getIntExtra("activityId", 0) + "";

		initView();

		// activityId不等于0，则为编辑活动，获取活动详情
		if (!"0".equals(activityId)) {
			new Thread() {
				public void run() {
					String idParam = "{\"id\":\"" + activityId
							+ "\",\"source\":\"\"}";
					KeelLog.e("", "idParam::" + idParam);
					String result = HttpRequestUtil.sendPost(
							Constant.actionDetailPath, idParam,
							ReleaseActivity.this);
					KeelLog.e(TAG, "action result：：" + result);
					ServerResultDao dao = Utils.getServerResult(result);
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() == null
								|| "".equals(dao.getResponseData())) {
							Message msg = new Message();
							msg.what = 0;
							msg.obj = "获取活动详情失败";
							handler.sendMessage(msg);
						} else {
							meeting = getMeetingDetail(dao.getResponseData());
							KeelLog.e(TAG, meeting.toString());
							if (meeting == null) {
								Message msg = new Message();
								msg.what = 0;
								msg.obj = "获取活动详情失败";
								handler.sendMessage(msg);
							} else {
								handler.sendEmptyMessage(1);
							}
						}
					} else {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = "获取活动详情失败,失败原因：" + dao.getNotifyInfo();
						handler.sendMessage(msg);
					}
				};
			}.start();
		}

	}

	public void initView() {
		tagLayout = (LinearLayout) findViewById(R.id.activity_action_start_tagLayout);
		tagLayout1 = (LinearLayout) findViewById(R.id.activity_action_start_tagLayout1);
		releaseBtn = (Button) findViewById(R.id.activity_action_start_actionreleaseBtn);
		changeImgBtn = (Button) findViewById(R.id.activity_action_start_chooseimageBtn);
		chooseImg = (ImageView) findViewById(R.id.activity_action_actionImg);
		setAccountText = (TextView) findViewById(R.id.activity_action_start_setaccountText);
		themeEdit = (EditText) findViewById(R.id.activity_action_start_themeEdit);
		stimeLayout = (LinearLayout) findViewById(R.id.activity_action_start_timeLayout);
		startTimeEdit = (TextView) findViewById(R.id.activity_action_start_timeEdit);
		etimeLayout = (LinearLayout) findViewById(R.id.activity_action_end_timeLayout);
		endTimeEdit = (TextView) findViewById(R.id.activity_action_end_timeEdit);
		locationEdit = (TextView) findViewById(R.id.activity_action_start_locationEdit);
		personNoEdit = (EditText) findViewById(R.id.activity_action_start_personNoEdit);
		priceNoText = (TextView) findViewById(R.id.activity_action_start_priceNoText);
		priceChooseImg = (ImageView) findViewById(R.id.activity_action_start_pricechooseImg);
		infomationText = (TextView) findViewById(R.id.activity_action_start_infoNoText);
		gridview = (GridView) findViewById(R.id.activity_action_start_gridview);
		openBtn = (Button) findViewById(R.id.activity_action_startSwitch);
		textview = (TextView) findViewById(R.id.activity_action_start_typeintroduceText);
		gridLayout = (LinearLayout) findViewById(R.id.activity_action_start_gridLayout);
		backImg = (ImageView) findViewById(R.id.activity_action_start_backImg);
		locationLayout = (LinearLayout) findViewById(R.id.activity_action_start_locationLayout);
		priceLayout = (LinearLayout) findViewById(R.id.activity_action_start_paiceLayout);
		setaccountLayout = (LinearLayout) findViewById(R.id.activity_action_start_setaccountLayout);
		gridviewLayout = (LinearLayout) findViewById(R.id.activity_action_start_gridviewLayout);
		acticvity_linearlayout = (LinearLayout) findViewById(R.id.acticvity_linearlayout);
		relevanceLayout = (LinearLayout) findViewById(R.id.activity_action_relevanceLayout);
		treeLayout = (LinearLayout) findViewById(R.id.activity_action_treeLayout);
		labelLayout = (LinearLayout) findViewById(R.id.activity_action_labelLayout);
		relevanceLayout.setOnClickListener(this);
		treeLayout.setOnClickListener(this);
		labelLayout.setOnClickListener(this);

		myWebView_show = (WebView) findViewById(R.id.myWebView_show);
		locationLayout.setOnClickListener(this);
		priceLayout.setOnClickListener(this);
		openBtn.setOnClickListener(this);
		changeImgBtn.setOnClickListener(this);
		stimeLayout.setOnClickListener(this);
		etimeLayout.setOnClickListener(this);
		priceChooseImg.setOnClickListener(this);
		tagLayout.setOnClickListener(this);
		gridLayout.setOnClickListener(this);
		releaseBtn.setOnClickListener(this);
		backImg.setOnClickListener(this);
		setAccountText.setOnClickListener(this);
		acticvity_linearlayout.setOnClickListener(this);

		myWebView_show.getSettings().setJavaScriptEnabled(true);
		myWebView_show.getSettings().setBuiltInZoomControls(true);
		myWebView_show.getSettings().setSavePassword(false);
		// 支持多种分辨率，需要js网页支持
		myWebView_show.getSettings().setUserAgentString("mac os");
		myWebView_show.getSettings().setDefaultTextEncodingName("utf-8");
		// myWebView_show.addJavascriptInterface(new JavaScriptinterface(this),
		// "android");
		// myWebView_show.loadUrl(Constant.htmlPath);

		// 四大组件
		moduleLayout = (LinearLayout) findViewById(R.id.activity_action_moduleLayout);

		friendLayout = (RelativeLayout) findViewById(R.id.activity_action_relevance_friendLayout);
		friendIv = (ImageView) findViewById(R.id.activity_action_relevance_friendTagIv);
		friendTv = (TextView) findViewById(R.id.activity_action_releFriendTv);
		friendIv.setOnClickListener(this);

		partnerLayout = (RelativeLayout) findViewById(R.id.activity_action_relevance_partnerLayout);
		partnerIv = (ImageView) findViewById(R.id.activity_action_relevance_partnerTagIv);
		partnerTv = (TextView) findViewById(R.id.activity_action_relevance_partnerTv);
		partnerIv.setOnClickListener(this);

		organizeIv = (ImageView) findViewById(R.id.activity_action_organize_tagIv);
		organizeTv = (TextView) findViewById(R.id.activity_action_organize_Nametv);
		organizeIv.setOnClickListener(this);

		knowledgeIv = (ImageView) findViewById(R.id.activity_action_knowledge_tagIv);
		knowledgeTv = (TextView) findViewById(R.id.activity_action_knowledge_Nametv);
		knowledgeIv.setOnClickListener(this);

		affairIv = (ImageView) findViewById(R.id.activity_action_affair_tagIv);
		affairTv = (TextView) findViewById(R.id.activity_action_affair_Nametv);
		affairIv.setOnClickListener(this);

		module_catalogLayout = (LinearLayout) findViewById(R.id.activity_action_module_catalogLayout);
		module_catalogLayout.setOnClickListener(this);
		tree_numTv = (TextView) findViewById(R.id.activity_action_tree_numTv);
		tree_contentTv = (TextView) findViewById(R.id.activity_action_tree_contentTv);

		module_tagLayout = (LinearLayout) findViewById(R.id.activity_action_module_tagLayout);
		module_tagLayout.setOnClickListener(this);
		tag_contentTv = (TextView) findViewById(R.id.activity_action_tag_contentTv);
	}

	public void initValue() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_picture_liebiao) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_picture_liebiao) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_picture_liebiao) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();

		ImageLoader.getInstance().displayImage(meeting.getPicPath(), chooseImg,
				options);

		themeEdit.setText(meeting.getTitle());
		startTimeEdit.setText(meeting.getStartTime());
		endTimeEdit.setText(meeting.getEndTime());
		locationEdit.setText(meeting.getProvince() + meeting.getCity()
				+ meeting.getAddress());
		if (meeting.getPeopleNumber() == 0) {
			personNoEdit.setText("不限");
		} else {
			personNoEdit.setText(meeting.getPeopleNumber() + "");
		}
		if (Integer.parseInt(meeting.getFee()) == 0) {
			setaccountLayout.setVisibility(View.GONE);
			priceNoText.setText("免费");
		} else {
			priceNoText.setText(Integer.parseInt(meeting.getFee()) + "元");
		}

		tagList = meeting.getActivityLabelList();
		if (tagList != null) {
			for (int i = 0; i < tagList.size(); i++) {
				View view = LayoutInflater.from(ReleaseActivity.this).inflate(
						R.layout.release_tag_text, null);
				TextView tv = (TextView) view
						.findViewById(R.id.release_tag_text_textview);
				tv.setText(tagList.get(i));
				tagLayout1.addView(view);
			}
		}

		infoList = meeting.getApplyLabelList();
		if (infoList != null) {
			gridviewLayout.setVisibility(View.VISIBLE);
			adapter = new InfoAdapter();
			gridview.setAdapter(adapter);
			infomationText.setText(infoList.size() + "");
		}

		activityDes = meeting.getActivityDesc();

		new Thread() {
			@SuppressWarnings("deprecation")
			public void run() {
				contentIndex = true;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = 3;
				if (!isNull(meeting.getActivityDesc())) {
					msg.obj = meeting.getActivityDesc();
					;
				} else {
					msg.obj = "";
				}
				handler.sendMessage(msg);
			};
		}.start();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_action_start_backImg:
			finish();
			break;
		case R.id.activity_action_startSwitch:
			if (isOpen) {
				isOpen = false;
				openBtn.setBackgroundResource(R.drawable.icon_private);
				textview.setText("只有收到邀请才能参会");
			} else {
				isOpen = true;
				openBtn.setBackgroundResource(R.drawable.icon_public);
				textview.setText("允许所有人报名参会");
			}
			break;
		case R.id.activity_action_start_locationLayout:// 地点
			locationEdit.clearFocus();
			Intent locaionIntent = new Intent(ReleaseActivity.this,
					ActionMapActivity.class);
			startActivityForResult(locaionIntent, 0);
			break;
		case R.id.activity_action_start_chooseimageBtn:// 图片
			Intent changeImgIntent = new Intent(ReleaseActivity.this,
					ChangeImgActivity.class);
			changeImgIntent.putExtra("imageId", Integer.parseInt(Imgid));
			startActivityForResult(changeImgIntent, 1);
			break;
		case R.id.activity_action_start_timeLayout:// 开始时间
			SimpleBottomDialog startDialog = new SimpleBottomDialog(this,
					startTimeEdit);
			startDialog.show();
			break;
		case R.id.activity_action_end_timeLayout:// 结束时间
			SimpleBottomDialog endDialog = new SimpleBottomDialog(this,
					endTimeEdit);
			endDialog.show();
			break;
		case R.id.activity_action_start_paiceLayout:// 价格
			Intent actionPriceIntent = new Intent(ReleaseActivity.this,
					ActionPriceActivity.class);
			startActivityForResult(actionPriceIntent, 4);
			break;
		case R.id.activity_action_start_tagLayout:// 标签
			Intent tagIntent = new Intent(ReleaseActivity.this,
					TagChooseActivity.class);
			if (tagList != null && tagList.size() > 0) {
				tagIntent.putStringArrayListExtra("tagList", tagList);
			}
			startActivityForResult(tagIntent, 5);
			break;
		case R.id.activity_action_start_gridLayout:// 报名信息
			Intent infoIntent = new Intent(ReleaseActivity.this,
					InfoChooesActivity.class);
			if (infoList != null && infoList.size() > 0) {
				infoIntent.putStringArrayListExtra("infoList", infoList);
			}
			startActivityForResult(infoIntent, 6);
			break;
		case R.id.activity_action_start_setaccountText:// 设置用户
			Intent accountIntent = new Intent(ReleaseActivity.this,
					AccountUserSetActivity.class);
			startActivity(accountIntent);
			finish();
			break;
		case R.id.acticvity_linearlayout:// 互动简介
			Intent intent = new Intent(ReleaseActivity.this,
					ActivitiesProfileActivity.class);
			if (activityDes != null && !"".equals(activityId)) {
				intent.putExtra("activitydes", activityDes);
			}
			startActivityForResult(intent, 7);
			myWebView_show.loadUrl("javascript:getHtml()");
			break;
		case R.id.activity_action_relevanceLayout:// 关联
			// Intent releIntent = new
			// Intent(this,ActionRelevanceActivity.class);
			// startActivityForResult(releIntent, 8);
			// 将sharePeopleHubSelectedMap数据转换成connectionNode，以对接关联资源
			ConnectionNode connectionNode = null;
			if (InitiatorDataCache.getInstance().sharePeopleHubSelectedMap != null) {
				connectionNode = new ConnectionNode();
				// listConnections
				ArrayList<Connections> listConnections = new ArrayList<Connections>();
				// 封装ResourceNode
				Iterator<Entry<String, JTContactMini>> peoplehubSleMap = InitiatorDataCache
						.getInstance().sharePeopleHubSelectedMap.entrySet()
						.iterator();
				while (peoplehubSleMap.hasNext()) {
					Map.Entry entry = (Map.Entry) peoplehubSleMap.next();
					JTContactMini item = (JTContactMini) entry.getValue();
					// 封装connection
					Connections connections = new Connections(item);
					listConnections.add(connections);
				}
				connectionNode.setListConnections(listConnections);
				connectionNode.setMemo(keyWord);
			}
			ENavigate.startRelatedResourceActivityForResult(
					ReleaseActivity.this, 8, "", ResourceType.People,
					connectionNode);
			break;
		case R.id.activity_action_treeLayout:// 目录
		case R.id.activity_action_module_catalogLayout:
			Intent treeIntent = new Intent(this, ActionTreeActivity.class);
			startActivityForResult(treeIntent, 9);
			break;
		case R.id.activity_action_labelLayout:// 标签
		case R.id.activity_action_module_tagLayout:
			Intent labelIntent = new Intent(this, ActionlabelActivity.class);
			startActivityForResult(labelIntent, 10);
			break;
		case R.id.activity_action_start_actionreleaseBtn:// 发布

			String personNo = personNoEdit.getText().toString();
			String priceStr = priceNoText.getText().toString();
			Log.e("", "personNo::" + personNo);
			Log.e("", "priceStr::" + priceStr);

			if (isNull(themeEdit.getText().toString())) {
				Toast.makeText(this, "请填写您的活动主题名称", 0).show();
				break;
			}

			if (isNull(startTimeEdit.getText().toString())) {
				Toast.makeText(this, "请选择您的活动开始时间", 0).show();
				break;
			}
			if (isNull(endTimeEdit.getText().toString())) {
				Toast.makeText(this, "请选择您的活动结束时间", 0).show();
				break;
			}
			if (Utils.compare_date(startTimeEdit.getText().toString(),
					endTimeEdit.getText().toString()) >= 0) {
				Toast.makeText(this, "开始时间必须在结束时间之前", 0).show();
				break;
			}
			if (tagList.size() == 0) {
				Toast.makeText(this, "您尚未设置活动标签", 0).show();
				break;
			}
			if (infoList.size() == 0) {
				Toast.makeText(this, "您尚未设置报名信息", 0).show();
				break;
			}
			if (personNo == null || "".equals(personNo) || "0".equals(personNo)) {
				personNo = "不限";
			}

			if (activityDes == null || "".equals(activityDes)) {
				Toast.makeText(this, "请输入您的活动简介", 0).show();
				break;
			}

			if ("0".equals(activityId)) {// 发布活动
				if (!priceStr.equals("免费") && !priceStr.equals("0元")) {
					if (!getAccount()) {
						Toast.makeText(this, "您发布的活动为收费活动，请设置您的账户", 0).show();
						createDialog();
					} else {
						priceStr = priceStr.substring(0, priceStr.indexOf("元"));
						sendData(priceStr, personNo);
					}
				} else if (priceStr.equals("免费")) {
					priceStr = "0";
					setAccountText.setVisibility(View.GONE);
					sendData(priceStr, personNo);
				}

			} else {
				if (priceStr.endsWith("免费")) {
					priceStr = 0 + "";
				} else {
					priceStr.substring(0, priceStr.indexOf("元"));
				}

				Map<String, String> myMap = new HashMap<String, String>();
				myMap.put("title", themeEdit.getText().toString());
				myMap.put("startTime", startTimeEdit.getText().toString());
				myMap.put("endTime", endTimeEdit.getText().toString());
				myMap.put("picFileId", meeting.getPicFileId() + "");
				myMap.put("province", province);
				myMap.put("city", city);
				myMap.put("address", addressName);
				myMap.put("peopleNumber", personNoEdit.getText().toString());
				myMap.put("fee", priceStr);
				myMap.put("activityDesc", html(activityDes));
				new Thread() {
					@SuppressWarnings("unused")
					public void run() {

						String param = Utils.simpleMapToJsonStr(map);
						String temParam = param.substring(0, 1)
								+ "\"activityLabelList\":"
								+ Utils.ListToJson(tagList)
								+ ",\"applyLabelList\":"
								+ Utils.ListToJson(infoList) + ","
								+ param.substring(1, param.length());

						KeelLog.e("ReleaseActivity::", "temParam::" + temParam);
						String result = HttpRequestUtil.sendPost(
								Constant.editPath, temParam,
								ReleaseActivity.this);
						ServerResultDao dao = Utils.getServerResult(result);
						KeelLog.e(TAG, "编辑dao：：" + dao.toString());
						if (dao != null) {
							if (dao.getNotifyCode().equals("0001")) {
								if (dao.getResponseData() != null
										&& !"".equals(dao.getResponseData())
										&& !"null"
												.equals(dao.getResponseData())) {
									String id = getCreateActionResult(dao
											.getResponseData());
									if (id != null && !"".equals(id)) {
										Message msg = new Message();
										msg.what = 2;
										msg.obj = "编辑活动成功，活动id是：" + id;
										handler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.what = 0;
										msg.obj = "编辑活动失败";
										handler.sendMessage(msg);
									}
								} else {
									Message msg = new Message();
									msg.what = 0;
									msg.obj = "编辑活动失败！";
									handler.sendMessage(msg);
								}
							} else {
								Message msg = new Message();
								msg.what = 0;
								msg.obj = "编辑活动失败,失败原因：" + dao.getNotifyInfo();
								handler.sendMessage(msg);
							}
						} else {
							Message msg = new Message();
							msg.what = 0;
							msg.obj = "编辑活动失败";
							handler.sendMessage(msg);
						}
					};
				}.start();
			}
			break;

		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	public void sendData(String priceStr, String personNo) {
		// TODO Auto-generated method stub

		map.put("title", themeEdit.getText().toString());
		map.put("startTime", startTimeEdit.getText().toString());
		map.put("endTime", endTimeEdit.getText().toString());
		if (type == null || type.equals("")) {
			map.put("picFileId", 1 + "");
		} else if (type.equals("defalt")) {
			map.put("picFileId", Integer.parseInt(Imgid) + 1 + "");
		} else if (type.equals("defind")) {
			// map.put("picFileId", ChangeImgActivity.imageId);
		}
		map.put("province", province);
		map.put("city", city);
		map.put("address", addressName);
		map.put("peopleNumber", personNo);
		map.put("fee", priceStr + "");
		// String s = (String) SharedPreferencesUtils.getParam(
		// ReleaseActivity.this, "activitiesProfile", "");
		map.put("activityDesc", html(activityDes));
		map.put("positionX", positionX);
		map.put("positionY", positionY);
		map.put("isOpen", isOpen ? 1 + "" : 0 + "");

		new Thread() {
			@SuppressWarnings("unused")
			public void run() {
				String param = Utils.simpleMapToJsonStr(map);
				String temParam = param.substring(0, 1)
						+ "\"activityLabelList\":" + Utils.ListToJson(tagList)
						+ ",\"applyLabelList\":" + Utils.ListToJson(infoList)
						+ "," + param.substring(1, param.length());

				KeelLog.e("", "temParam::" + temParam);
				String result = HttpRequestUtil.sendPost(Constant.createPath,
						temParam, ReleaseActivity.this);
				KeelLog.e(TAG, "发布活动result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				KeelLog.e(TAG, "发布dao：：" + dao.toString());
				if (dao != null) {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())
								&& !"null".equals(dao.getResponseData())) {
							String id = getCreateActionResult(dao
									.getResponseData());
							if (id != null && !"".equals(id)) {
								Message msg = new Message();
								msg.what = 2;
								msg.obj = "发布活动成功，活动id是：" + id;
								handler.sendMessage(msg);
							} else {
								Message msg = new Message();
								msg.what = 0;
								msg.obj = "发布活动失败";
								handler.sendMessage(msg);
							}
						} else {
							Message msg = new Message();
							msg.what = 0;
							msg.obj = "发布活动失败！";
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = "发布活动失败,失败原因：" + dao.getNotifyInfo();
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = "发布活动失败";
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		Calendar calendar = Calendar.getInstance();
		String currentTime = null;
		Log.e("ReleaseActivity::", "requestCode:" + requestCode);
		Log.e("ReleaseActivity::", "resultCode:" + resultCode);
		Log.e("ReleaseActivity::", "data:" + data);
		if (requestCode == 0 && resultCode == RESULT_OK) {// 地图

			addressName = data.getStringExtra("addressName");
			province = data.getStringExtra("province");
			city = data.getStringExtra("city");
			detailStr = data.getStringExtra("detailStr");
			positionX = data.getStringExtra("positionX");
			positionY = data.getStringExtra("positionY");
			locationEdit.setText(addressName);

		}
		if (requestCode == 1 && resultCode == RESULT_OK) {// 图片
			type = data.getStringExtra("type");
			Log.e("", "type::" + type);
			if (type.equals("defalt")) {
				Imgid = data.getStringExtra("imageID") + "";
				KeelLog.e("", "Imgid::" + Imgid);
				Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),
						images[Integer.parseInt(Imgid)]);
				int weight = this.getWindowManager().getDefaultDisplay()
						.getWidth();
				int height = chooseImg.getHeight();
				Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, weight,
						height, true);
				chooseImg.setImageBitmap(bitmap);
			} else if (type.equals("defind")) {
				final String imagePath = data.getStringExtra("path");
				try {
					FileInputStream fis = new FileInputStream(imagePath);
					Bitmap bitmap1 = BitmapFactory.decodeStream(fis);
					int weight = this.getWindowManager().getDefaultDisplay()
							.getWidth();
					int height = chooseImg.getHeight();
					Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, weight,
							height, true);
					chooseImg.setImageBitmap(bitmap);

				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		if (requestCode == 4 && resultCode == RESULT_OK) {// 价格
			String price = data.getStringExtra("price");
			if (price.equals("") || price == null) {
				priceNoText.setText("免费");
				setaccountLayout.setVisibility(View.GONE);
			} else if (Float.parseFloat(price) == 0) {
				priceNoText.setText("免费");
				setaccountLayout.setVisibility(View.GONE);
			} else if (Float.parseFloat(price) > 0) {
				if (!infoList.contains("姓名")) {
					infoList.add("姓名");
				}
				if (!infoList.contains("手机")) {
					infoList.add("手机");
				}

				if (getAccount()) {
				} else {
					setaccountLayout.setVisibility(View.VISIBLE);
				}
				gridviewLayout.setVisibility(View.VISIBLE);
				adapter = new InfoAdapter();
				gridview.setAdapter(adapter);
				infomationText.setText(infoList.size() + "");
				priceNoText.setText(price + "元");
			} else if (Float.parseFloat(price) < 0) {
				Toast.makeText(this, "当前金额输入有误，请重新输入", 0).show();
			}
		}

		if (requestCode == 5 && resultCode == RESULT_OK) {// 标签
			tagList = data.getStringArrayListExtra("tagList");
			if (tagList == null || tagList.size() == 0) {
				tagLayout1.removeAllViews();
			} else {
				tagLayout1.removeAllViews();
				for (int i = 0; i < tagList.size(); i++) {
					View view = LayoutInflater.from(ReleaseActivity.this)
							.inflate(R.layout.release_tag_text, null);
					TextView tv = (TextView) view
							.findViewById(R.id.release_tag_text_textview);
					tv.setText(tagList.get(i));
					tagLayout1.addView(view);
				}
			}

		}
		if (requestCode == 6 && resultCode == RESULT_OK) {// 报名信息
			infoList = data.getStringArrayListExtra("infoList");
			if (infoList == null && infoList.size() == 0) {
				gridviewLayout.setVisibility(View.GONE);
			} else {
				gridviewLayout.setVisibility(View.VISIBLE);
			}
			adapter = new InfoAdapter();
			gridview.setAdapter(adapter);
			infomationText.setText(infoList.size() + "");
		}

		if (requestCode == 7 && resultCode == RESULT_OK) {// 活动简介
			activityDes = data.getStringExtra("activitydes");
			KeelLog.e("", "活动详情返回数据：：：：" + activityDes);
			if (!contentIndex) {
				contentIndex = true;
			}
			Message msg = new Message();
			msg.what = 3;
			msg.obj = data.getStringExtra("activitydes");
			handler.sendMessage(msg);
		}
		if (requestCode == 8 && resultCode == RESULT_OK) {
			if (resultCode == Activity.RESULT_OK) {
				// 关联人脉：
				if (data.hasExtra(EConsts.Key.RELATED_PEOPLE_NODE)) {
					ConnectionNode connectionNode = (ConnectionNode) data
							.getSerializableExtra(EConsts.Key.RELATED_PEOPLE_NODE);
					peopleRelatedkeyWord = connectionNode.getMemo();
					if (connectionNode != null
							&& connectionNode.getListConnections() != null
							&& connectionNode.getListConnections().size() > 0) {
						ArrayList<Connections> listConnections = connectionNode
								.getListConnections();
						InitiatorDataCache.getInstance().sharePeopleHubSelectedMap
								.clear();
						for (int i = 0; i < listConnections.size(); i++) {
							Connections connections = listConnections.get(i);
							KeelLog.e(TAG,
									"人脉connections::" + connections.toString());
							if (connections != null
									&& connections.getJtContactMini() != null) {
								JTContactMini jtContactMini = connections
										.getJtContactMini();
								KeelLog.e(TAG, "人脉jtContactMini::"
										+ jtContactMini.toString());
								// // 把关联的数据塞进sharePeopleHubSelectedMap
								// InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.put(jtContactMini.getId(),
								// jtContactMini);
								if(moduleLayout.getVisibility() == View.GONE){
									moduleLayout.setVisibility(View.VISIBLE);
								}
								if(jtContactMini.isOnline){
									partnerLayout.setVisibility(View.GONE);
									if(friendTv.getText().toString() != null && !"".equals(friendTv.getText().toString())){
										friendTv.setText(friendTv.getText().toString()+"、"+jtContactMini.getLastName()+jtContactMini.getName());
									}else{
										friendTv.setText(jtContactMini.getLastName()+jtContactMini.getName());
									}
								}else{
									friendLayout.setVisibility(View.GONE);
									if(partnerTv.getText().toString() != null && !"".equals(partnerTv.getText().toString())){
										partnerTv.setText(partnerTv.getText().toString()+"、"+jtContactMini.getLastName()+jtContactMini.getName());
									}else{
										partnerTv.setText(jtContactMini.getLastName()+jtContactMini.getName());
									}
								}
							}
						}
						// // 将选择显示在界面上
						// if
						// (!Util.isNull(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap))
						// {
						// String sharePeoplehubStr =
						// IniviteUtil.format(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap,
						// "，");
						// peopleHubText.setText(sharePeoplehubStr);
						// }
					}
				}

				// 关联组织
				if (data.hasExtra(EConsts.Key.RELATED_ORGANIZATION_NODE)) {
					ConnectionNode connectionNode = (ConnectionNode) data
							.getSerializableExtra(EConsts.Key.RELATED_ORGANIZATION_NODE);
					orgRelatedkeyWord = connectionNode.getMemo();
					if (connectionNode != null
							&& connectionNode.getListConnections() != null
							&& connectionNode.getListConnections().size() > 0) {
						ArrayList<Connections> listConnections = connectionNode
								.getListConnections();
						InitiatorDataCache.getInstance().shareOrgHubSelectedMap
								.clear();
						for (int i = 0; i < listConnections.size(); i++) {
							Connections connections = listConnections.get(i);
							KeelLog.e(TAG,
									"组织connections::" + connections.toString());
							if (connections != null
									&& connections.getOrganizationMini() != null) {
								OrganizationMini organizationMini = connections
										.getOrganizationMini();
								KeelLog.e(TAG, "组织organizationMini::"
										+ organizationMini.toString());
								if(moduleLayout.getVisibility() == View.GONE){
									moduleLayout.setVisibility(View.VISIBLE);
								}
								organizeTv.setText(organizationMini.getFullName());
								// // 把关联的数据塞进shareOrgHubSelectedMap
								// InitiatorDataCache.getInstance().shareOrgHubSelectedMap.put(organizationMini.getId(),
								// organizationMini);
							}
						}
						// // 将选择显示在界面上
						// if
						// (!Util.isNull(InitiatorDataCache.getInstance().shareOrgHubSelectedMap))
						// {
						// String shareOrghubStr =
						// IniviteUtil.formatOrg(InitiatorDataCache.getInstance().shareOrgHubSelectedMap,
						// "，");
						// orgTextTv.setText(shareOrghubStr);
						// }
					}
				}
				// 关联知识
				if (data.hasExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE)) {
					KnowledgeNode knowledgeNode = (KnowledgeNode) data
							.getSerializableExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE);
					knowledgeRelatedkeyWord = knowledgeNode.getMemo();
					if (knowledgeNode != null
							&& knowledgeNode.getListKnowledgeMini2() != null
							&& knowledgeNode.getListKnowledgeMini2().size() > 0) {
						ArrayList<KnowledgeMini2> listKnowledgeMini2 = knowledgeNode
								.getListKnowledgeMini2();
						InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap
								.clear();
						for (int i = 0; i < listKnowledgeMini2.size(); i++) {
							KnowledgeMini2 knowledgeMini2 = listKnowledgeMini2
									.get(i);
							KeelLog.e(TAG, "知识knowledgeMini2::"
									+ knowledgeMini2.toString());
							if (knowledgeMini2 != null) {
								// InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.put(knowledgeMini2.id,
								// knowledgeMini2);
								if(moduleLayout.getVisibility() == View.GONE){
									moduleLayout.setVisibility(View.VISIBLE);
								}
								knowledgeTv.setText(knowledgeMini2.getTitle());
							}
						}
						// if
						// (!Util.isNull(InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap))
						// {
						// int index = 0;
						// knowleadgeText1.setVisibility(View.INVISIBLE);
						// knowleadgeText2.setVisibility(View.GONE);
						// knowleadgeText3.setVisibility(View.GONE);
						// Iterator<Entry<Long, KnowledgeMini2>> iter =
						// InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.entrySet().iterator();
						// while (iter.hasNext()) {
						// Map.Entry<Long, KnowledgeMini2> entry = iter.next();
						// KnowledgeMini2 item = entry.getValue();
						// if (index == 0) {
						// knowleadgeText1.setVisibility(View.VISIBLE);
						// knowleadgeText1.setText(item.title);
						// } else if (index == 1) {
						// knowleadgeText2.setVisibility(View.VISIBLE);
						// knowleadgeText2.setText(item.title);
						// } else if (index == 2) {
						// knowleadgeText3.setVisibility(View.VISIBLE);
						// knowleadgeText3.setText(item.title);
						// break;
						// } else {
						// break;
						// }
						// index++;
						// }
						// }
					}
				}
				// 关联需求
				if (data.hasExtra(EConsts.Key.RELATED_AFFAIR_NODE)) {
					AffairNode affairNode = (AffairNode) data
							.getSerializableExtra(EConsts.Key.RELATED_AFFAIR_NODE);
					requarementRelatedkeyWord = affairNode.getMemo();
					if (affairNode != null
							&& affairNode.getListAffairMini() != null
							&& affairNode.getListAffairMini().size() > 0) {
						ArrayList<AffairsMini> listAffairMini = affairNode
								.getListAffairMini();
						InitiatorDataCache.getInstance().shareDemandSelectedMap
								.clear();
						for (int i = 0; i < listAffairMini.size(); i++) {
							AffairsMini affairsMini = listAffairMini.get(i);
							RequirementMini requirementMini = affairsMini
									.toRequirementMini();
							KeelLog.e(TAG, "需求requirementMini::"
									+ requirementMini);
							if (requirementMini != null) {
								// InitiatorDataCache.getInstance().shareDemandSelectedMap.put(requirementMini.getmID(),
								// requirementMini);
								if(moduleLayout.getVisibility() == View.GONE){
									moduleLayout.setVisibility(View.VISIBLE);
								}
								affairTv.setText(requirementMini.getmTitle());
							}
						}

					}
					// if
					// (!Util.isNull(InitiatorDataCache.getInstance().shareDemandSelectedMap))
					// {
					// int index = 0;
					// demandText1.setVisibility(View.INVISIBLE);
					// demandText2.setVisibility(View.GONE);
					// demandText3.setVisibility(View.GONE);
					// Iterator<Entry<Integer, RequirementMini>> iter =
					// InitiatorDataCache.getInstance().shareDemandSelectedMap.entrySet().iterator();
					// while (iter.hasNext()) {
					// Map.Entry entry = (Map.Entry) iter.next();
					// RequirementMini item = (RequirementMini)
					// entry.getValue();
					// if (index == 0) {
					// demandText1.setVisibility(View.VISIBLE);
					// demandText1.setText(item.mTitle);
					// } else if (index == 1) {
					// demandText2.setVisibility(View.VISIBLE);
					// demandText2.setText(item.mTitle);
					// } else if (index == 2) {
					// demandText3.setVisibility(View.VISIBLE);
					// demandText3.setText(item.mTitle);
					// break;
					// } else {
					// break;
					// }
					// index++;
					// }
					// }
				}

			}
		}
		if (requestCode == 9 && resultCode == RESULT_OK) {//目录

			String catalogStr = data.getStringExtra("catalogStr");
			int catalogNum = data.getIntExtra("catalogNum", 0);
			if(module_catalogLayout.getVisibility() == View.GONE){
				module_catalogLayout.setVisibility(View.VISIBLE);
			}
			if (catalogStr != null && !"".equals(catalogStr)) {
				if (moduleLayout.getVisibility() == View.GONE) {
					moduleLayout.setVisibility(View.VISIBLE);
				}
				tree_contentTv.setText(catalogStr);
				tree_numTv.setText(catalogNum+"个目录");
			}
		}
		if (requestCode == 9 && resultCode == RESULT_OK) {//标签
			String labelStr = data.getStringExtra("labelStr");
			if(labelStr != null && !"".equals(labelStr)){
				tag_contentTv.setText(labelStr);
			}
		}
	}

	private class InfoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infoList.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(ReleaseActivity.this)
						.inflate(R.layout.activity_info_choose_item2, null);
				TextView tv = (TextView) convertView
						.findViewById(R.id.activity_info_choose_item2_text);
				tv.setText(infoList.get(position));
			}
			return convertView;
		}

	}

	public void createDialog() {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(
				ReleaseActivity.this).inflate(R.layout.setaccountdialog, null);
		final AlertDialog dialog = new AlertDialog.Builder(ReleaseActivity.this)
				.create();
		dialog.show();
		dialog.setContentView(layout);
		Button btn = (Button) dialog.findViewById(R.id.setaccount_dialog_btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReleaseActivity.this,
						AccountUserSetActivity.class);
				startActivity(intent);
				dialog.dismiss();
			}
		});
	}

	public boolean getAccount() {
		String userAccount = App.getApp().getAppData().getUserID();
		if (userAccount == null || "".equals(userAccount)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isNull(String s) {
		if (TextUtils.equals(s, "") || TextUtils.equals(s, null)) {
			return true;
		} else {
			return false;
		}
	}

	class JavaScriptinterface {

		private Context mContext;

		/** Instantiate the interface and set the context */
		public JavaScriptinterface(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public void jsMethod(String result) {
			System.out.print("返回值" + result);
			// activityDes = result;
			KeelLog.e(TAG, "activityDes::" + activityDes);
			Intent intent = new Intent(ReleaseActivity.this,
					ActivitiesProfileActivity.class);
			if (result != null && !"".equals(result)) {
				if (contentIndex) {
					intent.putExtra("activitydes", result);
				} else {
					intent.putExtra("activitydes", "");
				}
			}
			intent.putExtra("activitydes", result);
			startActivityForResult(intent, 7);
		}

	}

	public String getCreateActionResult(String responseData) {
		try {
			JSONObject job = new JSONObject(responseData);
			return job.getString("activityId");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	// 获取活动详情
	public Meeting getMeetingDetail(String responseData) {
		try {
			JSONObject job = new JSONObject(responseData);
			Gson gson = new Gson();
			Meeting meeting = gson.fromJson(job.getString("activity"),
					Meeting.class);
			applyLabelListStr = new JSONObject(job.getString("activity"))
					.getString("applyLabelList");
			KeelLog.e(TAG, "applyLabelListStr::" + applyLabelListStr);
			return meeting;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static String html(String content) {
		if (content == null)
			return "";
		String html = content;

		html = html.replace("'", "&apos;");
		html = html.replaceAll("&", "&amp;");
		html = html.replace("\"", "&quot;"); // "
		html = html.replace("\t", "&nbsp;&nbsp;");// 替换跳格
		html = html.replace(" ", "&nbsp;");// 替换空格
		html = html.replace("<", "&lt;");

		html = html.replaceAll(">", "&gt;");

		return html;
	}
}
