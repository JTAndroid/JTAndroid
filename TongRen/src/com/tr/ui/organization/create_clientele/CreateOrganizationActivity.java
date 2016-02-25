package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tr.App;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.demand.ImageItem;
import com.tr.model.home.MListCountry;
import com.tr.model.home.McountryCode;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.cloudDisk.FileManagementActivity;
import com.tr.ui.organization.model.OrgInfoVo;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.utils.CommonUtils;
import com.tr.ui.organization.widgets.OrganizationInfoAlertDialog;
import com.tr.ui.organization.widgets.ShowOrganizationFirstFaileLoginAlertDialog;
import com.tr.ui.organization.widgets.OrganizationInfoAlertDialog.OnDialogClickListener;
import com.tr.ui.organization.widgets.ShowOrganizationFirstFaileLoginAlertDialog.OnShowFaileDialogClickListener;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.widgets.ShowBitmapAlertDialog;
import com.tr.ui.widgets.ShowBitmapAlertDialog.OnEditDialogClickListener;
import com.tr.ui.widgets.ShowOrganizationAndLoginPromptAlertDialog;
import com.tr.ui.widgets.ShowOrganizationAndLoginPromptAlertDialog.OnShowPromptDialogClickListener;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.OrganizationPictureUploader;
import com.utils.common.OrganizationPictureUploader.OnOrganizationPictureUploadListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;
import com.utils.string.StringUtils;
//import com.baidu.navisdk.util.common.StringUtils;

/**
 * 创建组织
 * 
 * @author hzy
 * 
 */
public class CreateOrganizationActivity extends BaseActivity implements
		OnClickListener, OnOrganizationPictureUploadListener {
	public static final int REQUEST_CODE_BROWERS_ACTIVITY = 1001; // 启动关联的回调
	// 存放勾选图片/视频的路径集合
	private ArrayList<ImageItem> selectedPictureSD = new ArrayList<ImageItem>();// 本地
	private ArrayList<ImageItem> selectedPictureSDAndNet = new ArrayList<ImageItem>();// 本地加网络
	private ArrayList<ImageItem> selectedPictureNet = new ArrayList<ImageItem>();// 网络
	private static MyEditTextView org_tcreate_type_Etv;
//	private static MyEditTextView org_message_Etv;
	private CheckBox isMarket;
	private MyEditTextView org_name_Etv;
	private MyEditTextView org_ename_Etv;
	private ImageView iv_select_p_pic;
	private ImageView select_identification_face;//身份证正面选择按钮
	private ImageView orgnization_logo;//上传组织logo
	private ImageView iv_select_pic;
	private MyEditTextView people_name_Etv;
	private MyEditTextView phone_number_Etv;
	private TextView tv_getnumber;
	private EditText input_num;
	private Intent intent;
	private LinearLayout ll_org_main;
	private RelativeLayout isMarket_ll;
	//弹出框问题提示
	private final String HAS_MAIL = "        您好,感谢您申请组织，如未验证邮箱请立即登录验证，验证成功后我们会在第一时间审核您的资料，审核结果会通过短信和邮件的形式发送给您，请注意查收。";
	private final String HAS_NOT_MAIL = "        您好,感谢您申请组织，如未验证邮箱请立即登录验证，验证成功后我们会在第一时间审核您的资料，审核结果会通过短信和邮件的形式发送给您，请注意查收。";
	// 文字提示
	private final String TIP_REGET_VERIFY_CODE = "重获验证码";
	private final String TIP_GET_VERIFY_CODE = "获取验证码";
	private final String TIP_WRONG_VERIFY_CODE = "验证码错误";
	private final String TIP_GET_VERIFY_CODE_SUCCESS = "验证码已发送到您的手机，请注意查收";
	private final String TIP_ILLEGAL_ACCOUNT = "请输入正确的手机号码或邮箱";
	private final String TIP_EMPTY_VCODE = "请输入验证码";
	private ArrayList<ImageItem> browesPhotoVideo = new ArrayList<ImageItem>();
	// 常量
	private final int EXPIRED_TIME = 60; // 验证码超时时间,60s
	private final int COUNTDOWN_INTERVAL = 1000; // 倒计时时间间隔

	// 变脸相关
	private int mCountdownLeft; // 倒计时剩余时间
	private Timer mTimer; // 计时任务
	private String mVerifyCode; // 验证码
	
	private String LocalFilePath;

	// 消息类型
	private final int MSG_BASE = 100;
	private final int MSG_COUNT_DOWN = MSG_BASE + 1; // 倒计时的消息标识

	// 变量
	private String mMobile; // 用户输入的手机号
	private TextView region_number;
	private TextView business_hint;
	private TextView isMarket_text;
	private TextView identification_back_hint;
	private TextView identification_face_hint;

	private String lienceUrlToSql="", shenfenbackUrlToSql="",shenfenfaceUrlToSql="", logoUrlToSql="";
	private int Type = 2;
	private static String type3;
	MListCountry mListCountry;
	private ImageView pictrue_shenfen;
	private ImageView identification_face_thumbnail;
	//警示线
	private View organization_short_name_line;
	private View organization_type_line;
	private View yingye_picture_line;
	private View identification_face_line;
	private View identification_back_line;
	private View isMarket_line;
	private View people_name_line;
	private View phone_line;
	private View number_line;
	private TextView organization_all_name_tv;//组织全称
	private boolean hasNumber = true;
	private boolean hasPhone = true;
	private boolean hasPeopleName = true;
	private boolean hasShenFenBack = true;
	private boolean hasShenFenFace = true;
	private boolean hasLicensePic = true;
	private boolean hasStockNum = true;
	private boolean hasType = true;
	private boolean hasShortName = true;
	private static MyEditTextView stockNum_add;
	
	private	DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.org_default_orgnization).showImageForEmptyUri(R.drawable.org_default_orgnization)
			.showImageOnFail(R.drawable.org_default_orgnization).displayer(new RoundedBitmapDisplayer(5)).cacheInMemory(true).cacheOnDisc(true).considerExifParams(false).build();
	
	public static Handler handler = new Handler() {
		

		public void handleMessage(Message msg) {
			if (msg.what == 111111) {
				switch (msg.arg1) {
				case 1:
					yinyeTmpBitmap = (Bitmap)msg.obj;
					break;
				case 2:
					identification_face_TmpBitmap = (Bitmap)msg.obj;
					break;
				case 3:
					identification_back_TmpBitmap = (Bitmap)msg.obj;
					break;
				default:
					break;
				}
			} else {
				type3 = (String) msg.obj;
				org_tcreate_type_Etv.setText(type3);
				
//			org_message_Etv.setVisibility(View.VISIBLE);
				yinye_Rl.setVisibility(View.VISIBLE);
				stockNum_add.setVisibility(View.VISIBLE);
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_create_organizaton);

		jtFile = new JTFile();
		jtFile2 = new JTFile();
		init();
//		org_tcreate_type_Etv.setText("请选择组织类型");
//		org_message_Etv.setText("请选择上市信息");
		
		quit_research_report_Rl.setOnClickListener(this);
		org_tcreate_type_Etv.setOnClickListener(this);
//		org_message_Etv.setOnClickListener(this);
		iv_select_pic.setOnClickListener(this);
		finish_createorg_Tv.setOnClickListener(this);
		iv_select_p_pic.setOnClickListener(this);
		select_identification_face.setOnClickListener(this);
		orgnization_logo.setOnClickListener(this);
		tv_getnumber.setOnClickListener(this);
		initData();
		initVars();
	}

	// 初始化变量
	private void initVars() {
		mTimer = new Timer();
		mCountdownLeft = EXPIRED_TIME; // 60s倒计时时间
		mVerifyCode = ""; // 验证码
	}

	private void initData() {
		OrgInfoVo orgInfoVo = (OrgInfoVo) getIntent().getSerializableExtra(EConsts.Key.ORGINFOVO);
		String faileText = getIntent().getStringExtra(EConsts.Key.FAILETEXT);
		boolean isShowDialog = getIntent().getBooleanExtra(EConsts.Key.ISSHOWDIALOG, false);
		if (orgInfoVo != null) {
			//设置组织logo
			if (!TextUtils.isEmpty(orgInfoVo.picLogo)) {
				ImageLoader.getInstance().displayImage(orgInfoVo.picLogo, orgnization_logo,options);
				logoUrlToSql = CommonUtils.alterImageUrl(orgInfoVo.picLogo);
			}
			//设置组织全称
			if (!TextUtils.isEmpty(orgInfoVo.name)) {
				organization_all_name_tv.setText(orgInfoVo.name);
			}
			//设置组织简称
			if (!TextUtils.isEmpty(orgInfoVo.shotName)) {
				org_ename_Etv.setText(orgInfoVo.shotName);
			}
			//设置组织类型
			if (!TextUtils.isEmpty(orgInfoVo.orgType)) {
				if (orgInfoVo.orgType.equals("1")) {
					org_tcreate_type_Etv.setText("金融机构");
				}
				if (orgInfoVo.orgType.equals("2")) {
					org_tcreate_type_Etv.setText("一般企业");
				}
				if (orgInfoVo.orgType.equals("3")) {
					org_tcreate_type_Etv.setText("政府组织");
				}
				if (orgInfoVo.orgType.equals("4")) {
					org_tcreate_type_Etv.setText("中介机构");
				}
				if (orgInfoVo.orgType.equals("5")) {
					org_tcreate_type_Etv.setText("专业媒体");
				}
				if (orgInfoVo.orgType.equals("6")) {
					org_tcreate_type_Etv.setText("期刊报纸");
				}
				if (orgInfoVo.orgType.equals("7")) {
					org_tcreate_type_Etv.setText("研究机构");
				}
				if (orgInfoVo.orgType.equals("8")) {
					org_tcreate_type_Etv.setText("电视广播");
				}
				if (orgInfoVo.orgType.equals("9")) {
					org_tcreate_type_Etv.setText("互联网媒体");
				}
			}
			
			//设置政府组织信息
			if ("政府组织".equals(org_tcreate_type_Etv.getText().toString())) {
				isMarket_ll.setVisibility(View.GONE);
				yinye_Rl.setVisibility(View.GONE);
				stockNum_add.setVisibility(View.GONE);
			}else {
				//设置上市信息
				if (!TextUtils.isEmpty(orgInfoVo.isListing)) {
					if (orgInfoVo.isListing.equals("0")) {
						isMarket.setChecked(false);
						isMarket_text.setText("非上市公司");
					} else {
						isMarket.setChecked(true);
						isMarket_text.setText("上市公司");
						stockNum_add.setText(orgInfoVo.stockNum);
					}
					if ("非上市公司".equals(isMarket_text.getText().toString())) {
						stockNum_add.setVisibility(View.GONE);
					} else {
						stockNum_add.setVisibility(View.VISIBLE);
					}
				}
				//设置营业执照
				if (!TextUtils.isEmpty(orgInfoVo.licensePic)) {
					lienceUrlToSql = CommonUtils.alterImageUrl(orgInfoVo.licensePic);
					business_hint.setVisibility(View.GONE);
					pictrue_yinye.setVisibility(View.VISIBLE);
					CommonUtils.initRotateBitmap(orgInfoVo.licensePic,pictrue_yinye,1);
				}
			}
			
			
			
			//设置身份证正面照片
			if (!TextUtils.isEmpty(orgInfoVo.linkIdPic)) {
				shenfenfaceUrlToSql = CommonUtils.alterImageUrl(orgInfoVo.linkIdPic);
				identification_face_hint.setVisibility(View.GONE);
				identification_face_thumbnail.setVisibility(View.VISIBLE);
				CommonUtils.initRotateBitmap(orgInfoVo.linkIdPic,identification_face_thumbnail,2);
			}
			//设置身份证反面照片
			if (!TextUtils.isEmpty(orgInfoVo.linkIdPicReverse)) {
				shenfenbackUrlToSql = CommonUtils.alterImageUrl(orgInfoVo.linkIdPicReverse);
				identification_back_hint.setVisibility(View.GONE);
				pictrue_shenfen.setVisibility(View.VISIBLE);
				CommonUtils.initRotateBitmap(orgInfoVo.linkIdPicReverse,pictrue_shenfen,3);
			}
			//设置姓名
			if (orgInfoVo.linkName != null) {
				people_name_Etv.setText(orgInfoVo.linkName.relation);
			}
			//设置电话
			if (!TextUtils.isEmpty(orgInfoVo.linkMobile)) {
				et_input.setText(orgInfoVo.linkMobile);
			}
			
			if (isShowDialog) {
				final ShowOrganizationFirstFaileLoginAlertDialog showOrganizationFirstFaileLoginAlertDialog = new ShowOrganizationFirstFaileLoginAlertDialog(CreateOrganizationActivity.this);
				showOrganizationFirstFaileLoginAlertDialog.setCancelable(false);//点击外部不消失
				showOrganizationFirstFaileLoginAlertDialog.setCanceledOnTouchOutside(false);//点击外部不消失
				showOrganizationFirstFaileLoginAlertDialog.setFaileText("原因:"+faileText);
				showOrganizationFirstFaileLoginAlertDialog.setOnShowFaileDialogClickListener(new OnShowFaileDialogClickListener() {
					
					@Override
					public void startLogin() {
						showOrganizationFirstFaileLoginAlertDialog.dismiss();
					}
				});
				showOrganizationFirstFaileLoginAlertDialog.show();
			}
			
		}


	}

	private void init() {
		
		organization_short_name_line = findViewById(R.id.organization_short_name_line);
		organization_type_line = findViewById(R.id.organization_type_line);
		yingye_picture_line = findViewById(R.id.yingye_picture_line);
		identification_face_line = findViewById(R.id.identification_face_line);
		identification_back_line = findViewById(R.id.identification_back_line);
		people_name_line = findViewById(R.id.people_name_line);
		isMarket_line = findViewById(R.id.isMarket_line);
		phone_line = findViewById(R.id.phone_line);
		number_line = findViewById(R.id.number_line);
		
		
		organization_all_name_tv = (TextView) findViewById(R.id.organization_all_name_tv);
		finish_createorg_Tv = (TextView) findViewById(R.id.finish_createorg_Tv);
		isMarket_text = (TextView) findViewById(R.id.isMarket_text);
		ll_org_main = (LinearLayout) findViewById(R.id.ll_org_main);
		quit_research_report_Rl = (RelativeLayout) findViewById(R.id.quit_research_report_Rl);
		isMarket_ll = (RelativeLayout) findViewById(R.id.isMarket_ll);
		org_tcreate_type_Etv = (MyEditTextView) findViewById(R.id.org_create_type_Etv);
		stockNum_add = (MyEditTextView) findViewById(R.id.stockNum_add);
		stockNum_add.setVisibility(View.GONE);
		stockNum_add.setNumEdttext_inputtype();
		isMarket = (CheckBox) findViewById(R.id.isMarket);
		isMarket.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					stockNum_add.setVisibility(View.VISIBLE);
					isMarket_text.setText("上市公司");
				} else {
					stockNum_add.setVisibility(View.GONE);
					isMarket_text.setText("非上市公司");
				}
			}
		});
		org_name_Etv = (MyEditTextView) findViewById(R.id.org_name_Etv);
		org_ename_Etv = (MyEditTextView) findViewById(R.id.org_ename_Etv);
		people_name_Etv = (MyEditTextView) findViewById(R.id.people_name_Etv);
		region_number = (TextView) findViewById(R.id.tv_region_number);
		business_hint = (TextView) findViewById(R.id.business_hint);
		identification_back_hint = (TextView) findViewById(R.id.identification_back_hint);
		identification_face_hint = (TextView) findViewById(R.id.identification_face_hint);
		et_input = (EditText) findViewById(R.id.et_input);
		iv_select_p_pic = (ImageView) findViewById(R.id.iv_select_p_pic);
		select_identification_face = (ImageView) findViewById(R.id.select_identification_face);
		orgnization_logo = (ImageView) findViewById(R.id.orgnization_logo);
		iv_select_pic = (ImageView) findViewById(R.id.iv_select_pic);
		tv_count_backwards = (TextView) findViewById(R.id.tv_count_backwards);
		yinye = (TextView) findViewById(R.id.yinye);
		pictrue_yinye = (ImageView) findViewById(R.id.pictrue_yinye);
		pictrue_yinye.setOnClickListener(this);
		pictrue_shenfen = (ImageView) findViewById(R.id.pictrue_shenfen);
		pictrue_shenfen.setOnClickListener(this);
		identification_face_thumbnail = (ImageView) findViewById(R.id.identification_face_thumbnail);
		identification_face_thumbnail.setOnClickListener(this);
		yinye_Rl = (LinearLayout) findViewById(R.id.yinye_Rl);
		shenfen_Rl = (LinearLayout) findViewById(R.id.shenfen_Rl);

		// 获取验证码
		tv_getnumber = (TextView) findViewById(R.id.tv_getnumber);
		tv_getnumber.setText(TIP_GET_VERIFY_CODE);
		tv_getnumber.setOnClickListener(this);
		region_number.setOnClickListener(this);

		// 输入验证码
		input_num = (EditText) findViewById(R.id.input_num);
	}

	@Override
	public void onClick(View v) {
		try {
			
		switch (v.getId()) {
		case R.id.org_create_type_Etv:
			intent = new Intent(CreateOrganizationActivity.this,
					OrgTypeActivity.class);
			intent.putExtra("organ", true);
			intent.putExtra("Type", Type);
			startActivityForResult(intent, 0);

			break;
			
			
		case R.id.pictrue_yinye:
			showBitmapAlertDialog = new ShowBitmapAlertDialog(CreateOrganizationActivity.this);
			showBitmapAlertDialog.setOnDialogClickListener(new OnEditDialogClickListener() {
				
				@Override
				public void onshow(ImageView showImageView) {
					showImageView.setImageBitmap(yinyeTmpBitmap);
				}
			});
			showBitmapAlertDialog.show();
			
			break;
		case R.id.pictrue_shenfen:
			showBitmapAlertDialog = new ShowBitmapAlertDialog(CreateOrganizationActivity.this);
				showBitmapAlertDialog.setOnDialogClickListener(new OnEditDialogClickListener() {
					
					@Override
					public void onshow(ImageView showImageView) {
//						showImageView.setBackground(new BitmapDrawable(yinyeTmpBitmap));
						showImageView.setImageBitmap(identification_back_TmpBitmap);
					}
				});
				showBitmapAlertDialog.show();
		
			
			break;
		case R.id.identification_face_thumbnail:
			showBitmapAlertDialog = new ShowBitmapAlertDialog(CreateOrganizationActivity.this);
			showBitmapAlertDialog.setOnDialogClickListener(new OnEditDialogClickListener() {
				
				@Override
				public void onshow(ImageView showImageView) {
//					showImageView.setBackground(new BitmapDrawable(identification_face_TmpBitmap));
					showImageView.setImageBitmap(identification_face_TmpBitmap);
				}
			});
			showBitmapAlertDialog.show();
		
			
			break;
//		case R.id.iv_back:
//
//			finish();
//
//			break;
		case R.id.finish_createorg_Tv:
			//初始化横线和标记
			organization_short_name_line.setVisibility(View.GONE);
			organization_type_line.setVisibility(View.GONE);
			yingye_picture_line.setVisibility(View.GONE);
			identification_face_line.setVisibility(View.GONE);
			identification_back_line.setVisibility(View.GONE);
			people_name_line.setVisibility(View.GONE);
			isMarket_line.setVisibility(View.GONE);
			phone_line.setVisibility(View.GONE);
			number_line.setVisibility(View.GONE);
			hasNumber = true;
			hasPhone = true;
			hasPeopleName = true;
			hasShenFenBack = true;
			hasShenFenFace = true;
			hasLicensePic = true;
			hasStockNum = true;
			hasType = true;
			hasShortName = true;
			
			
			orgInfoVo = new OrgInfoVo();
			//组织类型
			if (org_tcreate_type_Etv.getText().equals("金融机构")) {
				orgInfoVo.orgType = "1";
			} else if (org_tcreate_type_Etv.getText().equals("一般企业")) {
				orgInfoVo.orgType = "2";
			} else if (org_tcreate_type_Etv.getText().equals("政府组织")) {
				orgInfoVo.orgType = "3";
			} else if (org_tcreate_type_Etv.getText().equals("中介机构")) {
				orgInfoVo.orgType = "4";
			} else if (org_tcreate_type_Etv.getText().equals("专业媒体")) {
				orgInfoVo.orgType = "5";
			} else if (org_tcreate_type_Etv.getText().equals("期刊报纸")) {
				orgInfoVo.orgType = "6";
			} else if (org_tcreate_type_Etv.getText().equals("研究机构")) {
				orgInfoVo.orgType = "7";
			} else if (org_tcreate_type_Etv.getText().equals("电视广播")) {
				orgInfoVo.orgType = "8";
			} else if (org_tcreate_type_Etv.getText().equals("互联网媒体")) {
				orgInfoVo.orgType = "9";
			} else {
				orgInfoVo.orgType = "10";
			}
			
			//组织logo地址
			if (logoUrlToSql != null) {
				orgInfoVo.picLogo = logoUrlToSql;
			}
			//营业执照地址
			if (lienceUrlToSql != null) {
				orgInfoVo.licensePic = lienceUrlToSql;
			}
			//身份证证明照片
			if (shenfenfaceUrlToSql != null) {
				orgInfoVo.linkIdPic = shenfenfaceUrlToSql;
			}
			//身份证反面照片
			if (shenfenbackUrlToSql != null) {
				orgInfoVo.linkIdPicReverse = shenfenbackUrlToSql;
			}

			//组织全称
			orgInfoVo.name = organization_all_name_tv.getText().toString();
			
			//是否是上市公司
			if ("非上市公司".equals(isMarket_text.getText().toString())) {
				orgInfoVo.isListing = "0";
			} else {
				orgInfoVo.isListing = "1";
			}
			
			//组织简称非空判断
			if (TextUtils.isEmpty(org_ename_Etv.getText().toString())) {
				organization_short_name_line.setVisibility(View.VISIBLE);
				hasShortName = false;
			}else {
				//组织简称
				orgInfoVo.shotName = org_ename_Etv.getText();
			}
			//判断组织类型
			if (TextUtils.isEmpty(org_tcreate_type_Etv.getText().toString())) {
				organization_type_line.setVisibility(View.VISIBLE);
				hasType = false;
			}
			
			if ("政府组织".equals(type2) && jtFile2.mLocalFilePath == "") {
				isMarket_ll.setVisibility(View.GONE);
				yinye_Rl.setVisibility(View.GONE);
				stockNum_add.setVisibility(View.GONE);
			}
			
			//判断证券号，在不是政府组织的情况下
			if ("政府组织".equals(org_tcreate_type_Etv.getText().toString())) {
				orgInfoVo.isListing = "";
				orgInfoVo.stockNum = "";
				orgInfoVo.licensePic = "";
				hasStockNum = true;
				hasLicensePic = true;
			}else {
				if ("非上市公司".equals(isMarket_text.getText())) {
					orgInfoVo.stockNum = "";
					hasStockNum = true;
				} else {
					if (TextUtils.isEmpty(stockNum_add.getText().toString())) {
						isMarket_line.setVisibility(View.VISIBLE);
						hasStockNum = false;
					}else {
						orgInfoVo.stockNum = stockNum_add.getText().toString();
					}
				}
				if (TextUtils.isEmpty(lienceUrlToSql)) {
					yingye_picture_line.setVisibility(View.VISIBLE);
					hasLicensePic = false;
				}
			}
			
			//身份证正面
			if (TextUtils.isEmpty(shenfenfaceUrlToSql)) {
				identification_face_line.setVisibility(View.VISIBLE);
				hasShenFenFace = false;
			}
			//身份证反面
			if (TextUtils.isEmpty(shenfenbackUrlToSql)) {
				identification_back_line.setVisibility(View.VISIBLE);
				hasShenFenBack = false;
			}
			//姓名
			if (TextUtils.isEmpty(people_name_Etv.getText().toString())) {
				people_name_line.setVisibility(View.VISIBLE);
				hasPeopleName = false;
			}else {
				Relation linkName = new Relation();
				linkName.relation = people_name_Etv.getText().toString();
				orgInfoVo.linkName = linkName;
			}
			
			//手机号
			if (TextUtils.isEmpty(et_input.getText().toString())) {
				phone_line.setVisibility(View.VISIBLE);
				hasPhone = false;
			}else {
				orgInfoVo.linkMobile = et_input.getText().toString().trim();
			}
			//验证码
			if (TextUtils.isEmpty(input_num.getText().toString())) {
				number_line.setVisibility(View.VISIBLE);
				hasNumber = false;
			}else {
				orgInfoVo.mobileCode = input_num.getText().toString().trim();
			}
			
			if (hasNumber && hasPhone && hasPeopleName && hasShenFenBack && hasShenFenFace && hasLicensePic && hasStockNum && hasType && hasShortName) {
			if (orgInfoVo != null) {
				CommonUtils.showLoadingDialog("", CreateOrganizationActivity.this);
//				if (!StringUtils.isEmpty(stockNum_add.getText().toString())) {
//					if (CommonUtils.JudgeOnlyNumber(stockNum_add.getText().toString())) {
//					} else {
//						CommonUtils.dismissLoadingDialog();
//						Toast.makeText(this, "证券号只能包含数字", Toast.LENGTH_SHORT).show();
//						return;
//					}
//				}
				// 保存组织
				OrganizationReqUtil.doSaveOrganization(CreateOrganizationActivity.this, mBindData, orgInfoVo, null);
				
			}
			}else {
				Toast.makeText(this, "请填写下面是黄色横线的必填项", Toast.LENGTH_SHORT).show();
			}
			

			
			
			break;
//		case R.id.org_message_Etv:
//			intent = new Intent(this, MarketActivity.class);
//			startActivityForResult(intent, 0);
//			break;
		case R.id.tv_getnumber:
			System.out.println("message");
			// 获取验证码 监听点击事件 开始倒计时
			if (tv_getnumber.getText().equals(TIP_GET_VERIFY_CODE)) {
				// 验证手机号是否正确
				if (EUtil.isMobileNO(region_number.getText().toString().trim(),
						et_input.getText().toString())) {
					// 发送获取验证码请求：
					// mParentActivity.showLoadingDialog();
					UserReqUtil.doGetVerifyCode(CreateOrganizationActivity.this, mBindData,UserReqUtil.getDoGetVerifyCodeParams(2,region_number.getText().toString().trim(),et_input.getText().toString()), null);
				} else {
					if (StringUtils.isEmpty(et_input.getText().toString())) {
						Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG)
								.show();
					} else {
						Toast.makeText(this, "您填写的号码格式不正确", Toast.LENGTH_LONG)
								.show();
					}
				}
			} else if (tv_getnumber.getText().equals(TIP_REGET_VERIFY_CODE)) {// 重获验证码
				// 发送获取验证码请求
				UserReqUtil.doGetVerifyCode(CreateOrganizationActivity.this,
						mBindData, UserReqUtil.getDoGetVerifyCodeParams(2,
								region_number.getText().toString().trim(),
								et_input.getText().toString()), null);
			}
			break;

		case R.id.tv_region_number:
			ENavigate.startCountryCodeActivity(this, mListCountry, 2057);// +86
																			// 选地区
			Log.i("TAG", "选地区+86");
			break;

		case R.id.iv_select_pic:
			ENavigate.startSelectPictureActivity(this,REQUEST_CODE_BROWERS_ACTIVITY, selectedPictureSD);
			break;
		case R.id.iv_select_p_pic:
			ENavigate.startSelectPictureActivity(this, 1002, selectedPictureSD);
			break;
		case R.id.select_identification_face:
			ENavigate.startSelectPictureActivity(this, 1003, selectedPictureSD);
			break;
		case R.id.orgnization_logo:
			orgnization_logo.setBackgroundResource(R.drawable.org_default_orgnization);
			ENavigate.startSelectPictureActivity(this, 1004, selectedPictureSD);
			break;
		case R.id.quit_research_report_Rl:
			showDialog();
			break;
		default:
			break;
		}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int type, Object object) {
			CommonUtils.dismissLoadingDialog();
			if (object==null) {
				return;
			}
			// 获取验证码
			if (type == EAPIConsts.ReqType.GET_VERIFY_CODE) {
				// 是否获取成功
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					mVerifyCode = dataBox.mVerifyCode;
					// 获取验证码成功，开始倒计时
					if (dataBox.mIsSuccess) {
						// 重置倒计时Timer
						if (mTimer != null) {
							mTimer.cancel();
							mTimer = null;
						}
						mTimer = new Timer();
						mTimer.schedule(new TimerTask() {

							@Override
							public void run() {
								mCountdownLeft--;
								mHandler.sendEmptyMessage(MSG_COUNT_DOWN);
							}

						}, 0, COUNTDOWN_INTERVAL);
						// 设置倒计时时间
						mCountdownLeft = EXPIRED_TIME;
						// 设置验证按钮状态
						tv_getnumber.setEnabled(false);
						// 显示消息
						Toast.makeText(App.getApp(),
								TIP_GET_VERIFY_CODE_SUCCESS, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
			if (type == EAPIConsts.OrganizationReqType.SAVE_ORGANIZATION) {

				Map<String, Boolean> map = (Map<String, Boolean>) object;
				boolean success = (boolean) map.get("success");
					if (success) {
						CommonUtils.dismissLoadingDialog();
						ShowOrganizationAndLoginPromptAlertDialog showOrganizationAndLoginPromptAlertDialog = new ShowOrganizationAndLoginPromptAlertDialog(CreateOrganizationActivity.this);
						showOrganizationAndLoginPromptAlertDialog.setCancelable(false);//点击外部不消失
						showOrganizationAndLoginPromptAlertDialog.setCanceledOnTouchOutside(false);//点击外部不消失
						showOrganizationAndLoginPromptAlertDialog.setOnDialogClickListener(new OnShowPromptDialogClickListener() {
							
							@Override
							public void startLogin() {
								ENavigate.startLoginActivity(CreateOrganizationActivity.this, null);
								CreateOrganizationActivity.this.finish();
							}
						});
//						if(App.getApp().getAppData().getUser().mOrganizationInfo.mState == 1){
//							showOrganizationAndLoginPromptAlertDialog.setMailText(HAS_NOT_MAIL);
//						}
						showOrganizationAndLoginPromptAlertDialog.show();
					} else {
						Toast.makeText(CreateOrganizationActivity.this, "完善信息失败，请稍候重试", 0).show();
						return;
					}
				}
			
			}
	};
	private boolean istrue = true;

	 // 检查信息的完整性（是否显示下一步按钮）
	 private boolean infoIntegrityCheck() {
	
	 if (EUtil.isMobileNO(region_number.getText().toString().trim(),
	 et_input.getText().toString())) { // 手机号和验证码----//后端决定此处由后台验证
	 // 验证码的正确性
	 if (input_num.getText().toString().length() <= 0) {
	 Toast.makeText(App.getApp(), TIP_EMPTY_VCODE, Toast.LENGTH_SHORT).show();
	 return false;
	 }
	
	 }
	 else if (EUtil.isEmail(et_input.getText().toString())) {
	 return true;
	 }
	 else {
	 Toast.makeText(this, TIP_ILLEGAL_ACCOUNT, 1).show();
	 return false;
	 }
	 return true;
	 }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (resultCode) {

//			case 20:
//				String market = data.getStringExtra("market");
//				org_message_Etv.setText(market);
//				if ("上市公司".equals(market)) {
//					editTextView = new MyEditTextView(this);
//					editTextView.setTextLabel("证券号");
//					editTextView.setTextHintLabel("请填写证券号码");
//					ll_org_main.addView(editTextView,ll_org_main.indexOfChild(org_message_Etv) + 1);
//				} else {
//					if (editTextView != null) {
//						editTextView.setVisibility(View.GONE);
//					}
//				}
//				break;
			case 21:
				type2 = data.getStringExtra("type");
				org_tcreate_type_Etv.setText(type2);
				if ("政府组织".equals(type2)) {
					isMarket_ll.setVisibility(View.GONE);
					yinye_Rl.setVisibility(View.GONE);
					stockNum_add.setVisibility(View.GONE);
				} else{
					isMarket_ll.setVisibility(View.VISIBLE);
					yinye_Rl.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
			switch (requestCode) {
			case 1001:
				if (resultCode == RESULT_OK) {
					// 获取选中图片的集合
					if (REQUEST_CODE_BROWERS_ACTIVITY == requestCode) {
						picture = (ArrayList<JTFile>) data.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
						selectedPictureSD.clear();
						selectedPictureSDAndNet.clear();
						if (picture != null && !picture.isEmpty()) {
							System.out.println("picture---营业执照--"+ picture.get(0).mLocalFilePath);
//							yinye_Rl.setPadding(0,(int) Utils.convertDpToPixel(14), 0,(int) Utils.convertDpToPixel(14));
							pictrue_yinye.setVisibility(View.VISIBLE);
							business_hint.setVisibility(View.GONE);
							// 调上传头像接口
							OrganizationPictureUploader uploader = new OrganizationPictureUploader(
									this);
							// JTFile jtFile = new JTFile();
							jtFile.setId(101 + "");
							jtFile.mLocalFilePath = picture.get(0).mLocalFilePath;
							LocalFilePath = picture.get(0).mLocalFilePath;
							sourcebitmap = BitmapFactory.decodeFile(LocalFilePath);
							jtFile.mType = 1;
							CommonUtils.showLoadingDialog("", CreateOrganizationActivity.this);
							uploader.startNewUploadTask(jtFile);

						}

					}
				}

				break;
			//身份证反面照
			case 1002:
				if (resultCode == RESULT_OK) {
					// 获取选中图片的集合
					if (1002 == requestCode) {
						identification_back_picture_video_lists = (ArrayList<JTFile>) data.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
						selectedPictureSD.clear();
						selectedPictureSDAndNet.clear();
						if (identification_back_picture_video_lists != null && !identification_back_picture_video_lists.isEmpty()) {
							pictrue_shenfen.setVisibility(View.VISIBLE);
							identification_back_hint.setVisibility(View.GONE);
//							shenfen_Rl.setPadding(0,(int) Utils.convertDpToPixel(14), 0,(int) Utils.convertDpToPixel(14));
							if (identification_back_picture_video_lists.get(0) != null) {
								// 调上传头像接口
								OrganizationPictureUploader uploader = new OrganizationPictureUploader(this);
								
								jtFile2.setId(102 + "");
								jtFile2.mLocalFilePath = identification_back_picture_video_lists.get(0).mLocalFilePath;
								LocalFilePath = identification_back_picture_video_lists.get(0).mLocalFilePath;
								sourcebitmap = BitmapFactory.decodeFile(LocalFilePath);
								jtFile2.mType = 2;
								CommonUtils.showLoadingDialog("", CreateOrganizationActivity.this);
								uploader.startNewUploadTask(jtFile2);
								
							}

						}

					}
				}
				break;
			//身份正正面照
			case 1003:
				if (resultCode == RESULT_OK) {
					// 获取选中图片的集合
					if (1003 == requestCode) {
						identification_face_picture_video_lists = (ArrayList<JTFile>) data.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
						selectedPictureSD.clear();
						selectedPictureSDAndNet.clear();
						if (identification_face_picture_video_lists != null && !identification_face_picture_video_lists.isEmpty()) {
							identification_face_thumbnail.setVisibility(View.VISIBLE);
							identification_face_hint.setVisibility(View.GONE);
//							shenfen_Rl.setPadding(0,(int) Utils.convertDpToPixel(14), 0,(int) Utils.convertDpToPixel(14));
							if (identification_face_picture_video_lists.get(0) != null) {
								// 调上传头像接口
								OrganizationPictureUploader uploader = new OrganizationPictureUploader(this);
								
								// JTFile jtFile2 = new JTFile();
								jtFile2.setId(103 + "");
								jtFile2.mLocalFilePath = identification_face_picture_video_lists.get(0).mLocalFilePath;
								LocalFilePath = identification_face_picture_video_lists.get(0).mLocalFilePath;
								sourcebitmap = BitmapFactory.decodeFile(LocalFilePath);
								jtFile2.mType = 2;
								CommonUtils.showLoadingDialog("", CreateOrganizationActivity.this);
								uploader.startNewUploadTask(jtFile2);
							}
							
						}
						
					}
				}
				break;
			//组织logo上传
			case 1004:
				if (resultCode == RESULT_OK) {
					// 获取选中图片的集合
					if (1004 == requestCode) {
						orgnization_logo_lists = (ArrayList<JTFile>) data.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
						selectedPictureSD.clear();
						selectedPictureSDAndNet.clear();
						if (orgnization_logo_lists != null && !orgnization_logo_lists.isEmpty()) {
//							shenfen_Rl.setPadding(0,(int) Utils.convertDpToPixel(14), 0,(int) Utils.convertDpToPixel(14));
							if (orgnization_logo_lists.get(0) != null) {
								// 调上传头像接口
								OrganizationPictureUploader uploader = new OrganizationPictureUploader(this);
								
								jtFile2.setId(104 + "");
								jtFile2.mLocalFilePath = orgnization_logo_lists.get(0).mLocalFilePath;
								LocalFilePath = orgnization_logo_lists.get(0).mLocalFilePath;
								sourcebitmap = BitmapFactory.decodeFile(LocalFilePath);
								jtFile2.mType = 4;
								CommonUtils.showLoadingDialog("", CreateOrganizationActivity.this);
								uploader.startNewUploadTask(jtFile2);
							}
							
						}
						
					}
				}
				break;
			case 2057:
					McountryCode mcountryCode = (McountryCode) data.getExtras().get(ENavConsts.ECountry);
					region_number.setText("+" + mcountryCode.getCode());
				break;

			default:
				break;
			}
			
		}
	}
	
	
	// 消息处理器
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_COUNT_DOWN: // 倒计时消息
				if (mCountdownLeft <= 0) { // 倒计时结束
					// 取消倒计时任务
					if (mTimer != null) {
						mTimer.cancel();
						mTimer = null;
					}
					// 更改态验证码按钮状态和文字
					tv_getnumber.setText(TIP_REGET_VERIFY_CODE);
					tv_count_backwards.setVisibility(View.GONE);
					tv_getnumber.setEnabled(true);
				} else { // 倒计时仍在进行
					tv_count_backwards.setVisibility(View.VISIBLE);
					tv_getnumber.setVisibility(View.VISIBLE);
					tv_count_backwards.setText("" + mCountdownLeft);
					tv_getnumber.setText("秒后可重发");
				}
				break;
			}
		}
	};
	private TextView finish_createorg_Tv;
	private EditText et_input;
	private TextView tv_count_backwards;
	private ImageView pictrue_yinye;
	private static LinearLayout yinye_Rl;
	private LinearLayout shenfen_Rl;
	private ArrayList<JTFile> picture;
	private ArrayList<JTFile> identification_back_picture_video_lists;
	private ArrayList<JTFile> identification_face_picture_video_lists;
	private ArrayList<JTFile> orgnization_logo_lists;
	private RelativeLayout quit_research_report_Rl;
	private TextView yinye;
	private JTFile jtFile;
	private JTFile jtFile2;
	private OrgInfoVo orgInfoVo;
	private String types;
	private static String type2;
	private Bitmap sourcebitmap;
	private static Bitmap yinyeTmpBitmap;//营业执照临时bitmap
	private static Bitmap identification_face_TmpBitmap;//身份证正面临时bitmap
	private static Bitmap identification_back_TmpBitmap;//身份证反面临时bitmap
	private ShowBitmapAlertDialog showBitmapAlertDialog ;

	@Override
	public void onPrepared(String id) {

	}

	@Override
	public void onStarted(String id) {

	}

	@Override
	public void onUpdate(String id, int progress) {

	}

	@Override
	public void onCanceled(String id) {

	}

	@Override
	public void onSuccess(final String id, Map<String, String> result) {
		final String url = result.get("url"); //
		final String urlToSql = result.get("urlToSql");

		if (id.equals("101")) {//营业执照上传

			lienceUrlToSql = urlToSql;// 传回后台的相对路径

			if (!TextUtils.isEmpty(url)) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
//						ImageLoader.getInstance().displayImage(url,pictrue_yinye);
						if (sourcebitmap.getWidth() < sourcebitmap.getHeight()) {
							yinyeTmpBitmap = CommonUtils.rotateBitmap(sourcebitmap,-90);
							yinyeTmpBitmap.setHeight(sourcebitmap.getWidth());
							yinyeTmpBitmap.setWidth(sourcebitmap.getHeight());
						}else {
							yinyeTmpBitmap = sourcebitmap;
						}
//						pictrue_yinye.setBackground(new BitmapDrawable(yinyeTmpBitmap));
						pictrue_yinye.setImageBitmap(yinyeTmpBitmap);
						CommonUtils.dismissLoadingDialog();
						
					}
				});
			}
		} else if (id.equals("102")) {//身份证反面上传
			shenfenbackUrlToSql = urlToSql;

			if (!TextUtils.isEmpty(url)) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (sourcebitmap.getWidth() < sourcebitmap.getHeight()) {
							identification_back_TmpBitmap = CommonUtils.rotateBitmap(sourcebitmap,-90);
							identification_back_TmpBitmap.setHeight(sourcebitmap.getWidth());
							identification_back_TmpBitmap.setWidth(sourcebitmap.getHeight());
						}else {
							identification_back_TmpBitmap = sourcebitmap;
						}
//						pictrue_shenfen.setBackground(new BitmapDrawable(identification_back_TmpBitmap));
						pictrue_shenfen.setImageBitmap(identification_back_TmpBitmap);
						CommonUtils.dismissLoadingDialog();
					}
				});
			}
		}else if (id.equals("103")) {//身份证正面上传
			shenfenfaceUrlToSql = urlToSql;
			if (!TextUtils.isEmpty(url)) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (sourcebitmap.getWidth() < sourcebitmap.getHeight()) {
							identification_face_TmpBitmap = CommonUtils.rotateBitmap(sourcebitmap,-90);
							identification_face_TmpBitmap.setHeight(sourcebitmap.getWidth());
							identification_face_TmpBitmap.setWidth(sourcebitmap.getHeight());
						}else {
							identification_face_TmpBitmap = sourcebitmap;
						}
						identification_face_thumbnail.setImageBitmap(identification_face_TmpBitmap);
						CommonUtils.dismissLoadingDialog();
					}
				});
			}
		}else if (id.equals("104")) {//组织logo上传
			logoUrlToSql = urlToSql;
			if (!TextUtils.isEmpty(url)) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ImageLoader.getInstance().displayImage(url, orgnization_logo,options);
						CommonUtils.dismissLoadingDialog();
					}
				});
			}
		}

	}

	@Override
	public void onError(String id, int code, String message) {
		KeelLog.d("===>>OnError", message);
		System.out.println("返回信息失败--message：" + message);

	}

	private void showDialog() {
		final OrganizationInfoAlertDialog infoAlertDialog = new OrganizationInfoAlertDialog(CreateOrganizationActivity.this);
		infoAlertDialog.setTipTv("退出后所填信息将被删除，您确定要退出吗？");
		infoAlertDialog.setOnDialogClickListener(new OnDialogClickListener() {
			
			@Override
			public void okTv() {
				infoAlertDialog.dismiss();
			    finish();
			}
			
			@Override
			public void cancelTv() {
				infoAlertDialog.dismiss();
			}
		});
		infoAlertDialog.show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home ) {
			showDialog();
			return false;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
