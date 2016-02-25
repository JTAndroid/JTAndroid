package com.tr.ui.organization.firstpage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.model.demand.ImageItem;
import com.tr.model.demand.Metadata;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.demand.BrowesPhotoVideo;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.organization.model.BasicInfo;
import com.tr.ui.organization.model.Customer;
import com.tr.ui.organization.model.CustomerProfileVo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.utils.common.OrganizationPictureUploader;
import com.utils.common.OrganizationPictureUploader.OnOrganizationPictureUploadListener;
import com.utils.http.EAPIConsts.OrganizationReqType;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditBasicInfomationActivity extends BaseActivity implements
		OnClickListener, OnOrganizationPictureUploadListener {
	private ImageView edit_basic_iv, edit_avatar_iv;
	private TextView edit_basic_tv;
	private MyEditTextView edit_basic_custom;
	private MyEditTextView edit_basic_introduction;
	private MyEditTextView edit_basic_email;
	private MyEditTextView edit_basic_phone;
	// private BasicInfo basicInfo;
	private EditText edit_basicinfomation_edittext;
	private BasicData basicData;
	private LinearLayout LLL_main, LL_main;

	private boolean isNull;
	private MyEditTextView edit_basic_industry;
	private ArrayList<MyEditTextView> editTextViews;
	private ImageView edit_basic_rl_iv;
	private ArrayList<ImageItem> selectedPictureSD = new ArrayList<ImageItem>();// 本地
	private ArrayList<JTFile> picture;
	private JTFile jtFile;
	private String avatar_urlComplete;
	private String avatar_urlToSql;
	public static final int REQUEST_CODE_BROWERS_ACTIVITY = 1001; // 启动关联的回调

	private CustomerProfileVo customer;
	private ArrayList<CustomerPersonalLine> propertyList;
	private LinearLayout org_information_Ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_basicinfomation);
		editTextViews = new ArrayList<MyEditTextView>();
		customer = (CustomerProfileVo) getIntent().getSerializableExtra("edit");
		init();
		initListener();
		initData();
	}

	private void initData() {

		// System.out.println("EditBasicInfomationActivity="+getIntent().getExtras().getParcelable("key"));
		if (!TextUtils.isEmpty(customer.picLogo)) {
			ImageLoader.getInstance().displayImage(customer.picLogo,edit_basic_rl_iv);
		} else {
			edit_basic_rl_iv.setVisibility(View.VISIBLE);
		}

		// 简称
		if (customer.shotName != null) {
			edit_basicinfomation_edittext.setText(customer.shotName);
		} else if (customer.name != null) {
			edit_basicinfomation_edittext.setText(customer.name);
		}

		// 行业
		List<String> lists = customer.industrys;
		String str = "";
		if (lists != null && lists.size() > 0) {
			for (int i = 0; i < lists.size(); i++) {
				if (i == lists.size() - 1) {
					str += lists.get(i);
				} else {
					str += lists.get(i) + "";
				}
			}
		}
		edit_basic_industry.setText(str);

		// 邮箱
		if (customer.linkEmail != null) {
			edit_basic_email.setText(customer.linkEmail);
		} else {
			edit_basic_email.setText("");
		}
		// 联系电话
		if (customer.linkMobile != null) {
			edit_basic_phone.setText(customer.linkMobile);
		} else {
			edit_basic_phone.setText("");
		}
		// 组织简介
		if (customer.discribe != null) {
			edit_basic_introduction.setText(customer.discribe);
		} else {
			edit_basic_introduction.setText("");
		}

		propertyList = customer.propertyList;
		if (propertyList != null && propertyList.size() > 0) {
			for (int i = 0; i < propertyList.size(); i++) {
				CustomerPersonalLine line = propertyList.get(i);
				if ("1".equals(line.type)) {
					if (!line.name.equals("")) {
						final MyEditTextView myEditTextView_Text = new MyEditTextView(
								context);
						myEditTextView_Text.setText(line.content);
						myEditTextView_Text.setTextLabel(line.name);
						myEditTextView_Text.setDelete(true);
						
						org_information_Ll.addView(myEditTextView_Text,org_information_Ll.indexOfChild(edit_basic_custom)-1);
						editTextViews.add(myEditTextView_Text);
						myEditTextView_Text.getAddMore_Iv().setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								org_information_Ll.removeView(myEditTextView_Text);
								editTextViews.remove(myEditTextView_Text);
							}
						});
					}
				}
			}
		}
		
	}

	private void initListener() {
		edit_basic_iv.setOnClickListener(this);
		edit_basic_tv.setOnClickListener(this);
		edit_basic_custom.setOnClickListener(this);
		edit_basic_introduction.setOnClickListener(this);
		edit_basic_industry.setOnClickListener(this);
	}

	private void init() {
		edit_basic_iv = (ImageView) findViewById(R.id.edit_basic_iv);
		edit_basic_tv = (TextView) findViewById(R.id.edit_basic_tv);
		edit_basic_custom = (MyEditTextView) findViewById(R.id.edit_basic_custom);
		edit_basic_introduction = (MyEditTextView) findViewById(R.id.edit_basic_introduction);
		edit_basicinfomation_edittext = (EditText) findViewById(R.id.edit_basicinfomation_edittext);
		edit_basic_email = (MyEditTextView) findViewById(R.id.edit_basic_email);
		edit_basic_phone = (MyEditTextView) findViewById(R.id.edit_basic_phone);
		edit_basic_industry = (MyEditTextView) findViewById(R.id.edit_basic_industry);
		LL_main = (LinearLayout) findViewById(R.id.LL_main);
		edit_basic_rl_iv = (ImageView) findViewById(R.id.edit_basic_rl_iv);
		edit_avatar_iv = (ImageView) findViewById(R.id.edit_basic_rl_iv);
		edit_avatar_iv.setOnClickListener(this);
		org_information_Ll = (LinearLayout) findViewById(R.id.org_information_Ll);
		edit_basic_phone.setNumEdttext_inputtype();
	}
	
	
	private void clickFinishBtn() {
		
		customer.picLogo = avatar_urlComplete;
		App.getUser().setImage(avatar_urlComplete);
		customer.shotName = edit_basicinfomation_edittext.getText().toString().trim();
		App.getUser().setNick(edit_basicinfomation_edittext.getText().toString().trim());
		String industryString = edit_basic_industry.getText().toString().trim();
		if (industryString != null) {// 添加行业
			String[] arr = industryString.split(",");
			List<String> lists = new ArrayList<String>();
			for (String str : arr) {
				lists.add(str);
			}
			
			if (!lists.isEmpty()) {
				customer.industrys = lists;
			}
			
		}
		
		customer.linkEmail = edit_basic_email.getText().toString().trim();
		customer.linkMobile = edit_basic_phone.getText().toString().trim();
		customer.discribe = edit_basic_introduction.getText().toString().trim();
		
		if (!editTextViews.isEmpty()) {
			for (int i = 0; i < editTextViews.size(); i++) {
				CustomerPersonalLine customerPersonalLine = new CustomerPersonalLine();
				MyEditTextView myEditTextView = editTextViews.get(i);
				customerPersonalLine .content = myEditTextView.getText();
				customerPersonalLine .name = myEditTextView.getTextLabel();
				customerPersonalLine.type ="1";
				propertyList.add(customerPersonalLine);
			}
			customer.propertyList = propertyList;	
		}
		
		
		Intent intent_finish = new Intent();
		intent_finish.putExtra("customer", customer);
		setResult(333, intent_finish);
		finish();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_basic_iv:// 点击后退
			finish();
			break;
		case R.id.edit_basic_tv:// 点击完成,将本页面的数据带到基本资料页面
//			BasicData basicData = new BasicData();
//			basicData.setName(edit_basicinfomation_edittext.getText()
//					.toString().trim());
//			basicData.setIndustry(edit_basic_industry.getText().toString()
//					.trim());
//			basicData.setEmail(edit_basic_email.getText());
//			basicData.setDial(edit_basic_phone.getText());
//			basicData.setIntrodution(edit_basic_introduction.getText());
//			basicData.setAvatar_urlComplete(avatar_urlComplete);
//			basicData.setUrlToSql(avatar_urlToSql);
			clickFinishBtn();
		
			break;
		case R.id.edit_basic_industry:// 点击行业，进行筛选
			ENavigate.startChooseActivityForResult(this, false, "行业",
					ChooseDataUtil.CHOOSE_type_Trade, null);
			break;
		case R.id.edit_basic_custom:// 点击自定义文本
//			Intent intent_custom = new Intent(this, Custom_Activity.class);
//			intent_custom.putExtra("fengxing", true);
//			if (propertyList != null) {
//				Bundle b = new Bundle();
//				b.putSerializable("Customer_Bean", propertyList);
//				intent_custom.putExtras(b);
//			}
//			startActivityForResult(intent_custom, 0);
			final MyEditTextView editTextView = new MyEditTextView(context);
			editTextView.setDelete(true);
			editTextView.setCustom(true);
			org_information_Ll.addView(editTextView,
					org_information_Ll.indexOfChild(edit_basic_custom) - 1);

			editTextView.getAddMore_Iv().setBackgroundResource(
					R.drawable.people_column_delete);
			editTextView.getAddMore_Iv().setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							org_information_Ll.removeView(editTextView);
							editTextViews.remove(editTextView);
						}
					});
			editTextViews.add(editTextView);
			break;
		case R.id.edit_basic_introduction:// 点击组织简介，跳到组织简介页面
			Intent intent = new Intent(this,
					OrganizationIntroductionActivity.class);
			intent.putExtra("intro", edit_basic_introduction.getText());
			startActivityForResult(intent, 999);
			break;
		case R.id.edit_basic_rl_iv:// 上传头像
			ENavigate.startSelectPictureActivityforSingleSelection(this,
					REQUEST_CODE_BROWERS_ACTIVITY, picture, true);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			ArrayList<Metadata> backList = (ArrayList<Metadata>) data
					.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA);
			if (backList != null && backList.size() > 0) {
				List<Metadata> list = ChooseDataUtil.getSelectList(backList);
				String backData = list.get(0).name;
				edit_basic_industry.setText(backData);
			}
			if (resultCode == 100) {
				String text = (String) data
						.getSerializableExtra("introduction");
				edit_basic_introduction.setText(text);
			}
			if (resultCode == 999) {
				isNull = data.getBooleanExtra("isNull", false);
				LL_main.removeAllViews();
				propertyList = custom2(data, edit_basic_custom, LL_main, isNull,
						editTextViews);
				
			}
			if (resultCode == RESULT_OK) {
				// 获取选中图片的集合
				if (REQUEST_CODE_BROWERS_ACTIVITY == requestCode) {
					picture = (ArrayList<JTFile>) data
							.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
					selectedPictureSD.clear();
					if (picture != null && !picture.isEmpty()
							&& picture.size() > 0) {
						if (picture.get(0) != null) {

							// 调上传头像接口
							OrganizationPictureUploader uploader = new OrganizationPictureUploader(
									this);
							JTFile jtFile = new JTFile();
							jtFile.setId(String.valueOf(picture.get(0).mCreateTime));
							jtFile.mLocalFilePath = picture.get(0).mLocalFilePath;
							jtFile.mType = 4;
							uploader.startNewUploadTask(jtFile);

							edit_avatar_iv
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											Intent intent = new Intent(
													EditBasicInfomationActivity.this,
													BrowesPhotoVideo.class);
											intent.putExtra("index", 0);
											intent.putExtra(
													ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
													picture);
											startActivity(intent);
										}
									});
						}

					}

				}
			}

		}

	}

	@Override
	public void onPrepared(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStarted(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate(String id, int progress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCanceled(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(String id, Map<String, String> result) {
		final String url = result.get("url"); //
		final String urlToSql = result.get("urlToSql");

		avatar_urlComplete = url;
		avatar_urlToSql = urlToSql;

		if (!TextUtils.isEmpty(url)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ImageLoader.getInstance().displayImage(url, edit_avatar_iv);
					System.out.println("----url---" + url);
					System.out.println("----urlToSql---" + urlToSql);
				}
			});
		}

	}

	@Override
	public void onError(String id, int code, String message) {

	}
}
