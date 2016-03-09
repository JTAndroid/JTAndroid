package com.tr.ui.home.frg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.PeopleReqUtil;
import com.tr.model.demand.Metadata;
import com.tr.model.home.MIndustry;
import com.tr.model.home.MIndustrys;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.organization.model.Area;
import com.tr.ui.people.cread.GenderActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.model.BaseResult;
import com.tr.ui.people.model.Basic;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.PeopleRequest;
import com.tr.ui.people.model.PermIds;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonTagRelation;
import com.utils.common.EConsts;
import com.utils.common.OrganizationPictureUploader;
import com.utils.common.OrganizationPictureUploader.OnOrganizationPictureUploadListener;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;
import com.utils.string.StringUtils;

public class EditBusinessCardFrag extends JBaseFragment implements OnClickListener, IBindData, OnOrganizationPictureUploadListener {
	private Context mContext;
	@ViewInject(R.id.circle_avatar_edit)
	private ImageView circle_avatar_edit;// 头像

	@ViewInject(R.id.text_full_name)
	private MyEditTextView text_full_name;// 姓名
	@ViewInject(R.id.text_gender)
	private MyEditTextView text_gender;// 性别
	@ViewInject(R.id.text_classify)
	private MyEditTextView text_classify;// 职能即：行业/方向
	@ViewInject(R.id.text_company_name)
	private MyEditTextView text_company;// 公司
	@ViewInject(R.id.text_post)
	private MyEditTextView text_post;// 职位
	@ViewInject(R.id.text_area)
	private MyEditTextView text_area;// 地区

	@ViewInject(R.id.text_phone_num)
	private MyEditTextView text_phone_num;// 手机号
	@ViewInject(R.id.text_email)
	private MyEditTextView text_email;// 邮箱号
	@ViewInject(R.id.text_weixin)
	private MyEditTextView text_weixin;// 微信号
	@ViewInject(R.id.text_qq)
	private MyEditTextView text_qq;// QQ号
	@ViewInject(R.id.text_weibo)
	private MyEditTextView text_weibo;// 微博号
	private App mMainApp;

	private PeopleDetails people_details = new PeopleDetails();// 人脉详情对象
	/** 向后台提交的人脉对象 */
	private PeopleRequest people_request;
	private ArrayList<Long> categoryList; // 目录
	private ArrayList<Long> tid; // 标签
	private PermIds permIds; // 权限
	private String avatarUrl = null;// 头像URL
	private String gender = "保密";
	private String qq = "";
	private String weixin = "";
	private String weibo = "";
	/* 行业对象集合 */
	private List<MIndustry> mIndustryList;
	private MIndustrys mIndustrys;
	private StringBuilder strBuilder;

	private ArrayList<Metadata> metadataArea; // 区域
	private Area area_result; // 所在地区对象
	private ArrayList<JTFile> picture; // 选择相片返回的值
	private Person person = new Person();
	private boolean is_self_bool;
	/** 人脉主对象 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = getActivity();
		getBundle();
		mMainApp = App.getApp();
	}

	private void getBundle() {
		people_details = (PeopleDetails) this.getArguments().getSerializable(ENavConsts.datas);
		is_self_bool = this.getArguments().getBoolean(ENavConsts.IS_SELF_BOOL);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_edit_business_card, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initView(view);
		initData();
	}

	private String getGender(int num) {
		switch (num) {
		case 1:// 男
			gender = "男";
			break;
		case 2:// 女
			gender = "女";
			break;
		case 3:// 保密
			gender = "保密";
			break;

		default:
			break;
		}
		return gender;
	}

	private void getBasicInfo(Basic basic) {
		if (null != Integer.valueOf(basic.subtype)) {
			switch (Integer.valueOf(basic.subtype)) {
			case 1:// QQ
				if (!TextUtils.isEmpty(basic.content))
					qq = basic.content;
				break;
			case 2:// 微信
				if (!TextUtils.isEmpty(basic.content))
					weixin = basic.content;
				break;
			case 3:// 微博
				if (!TextUtils.isEmpty(basic.content))
					weibo = basic.content;
				break;

			default:
				break;
			}
		}

	}

	private String getArea(Person person) {
		String area ;
		String locationCountry = TextUtils.isEmpty(person.locationCountry) ? "" : person.locationCountry;
		String locationCity = TextUtils.isEmpty(person.locationCity) ? "" : person.locationCity;
		String locationCounty = TextUtils.isEmpty(person.locationCounty) ? "" : person.locationCounty;
		if (locationCountry.equals(locationCity)) {
			area = locationCountry  + locationCounty;
		}else{
			 area = locationCountry + locationCity + locationCounty;
		}
		
		return area;
	}

	private void initData() {
		if (people_details != null) {// 编辑
			person = people_details.people;
			ImageLoader.getInstance().displayImage(person.portrait, circle_avatar_edit);
			// 个人用户
//			if (!StringUtils.isEmpty(App.getUser().getmNick())) {
//				text_full_name.setText(App.getUser().getmNick());
//			} else if (!StringUtils.isEmpty(App.getUser().getmUserName())) {
//				text_full_name.setText(App.getUser().getmUserName());
//			} 
			if (person.getPeopleNameList().isEmpty()) {//防止旧数据索引越界
				text_full_name.setText("");
			}else{
				text_full_name.setText(person.getPeopleNameList().get(0).lastname);
			}
			text_gender.setText(getGender(person.gender));
			text_company.setText(person.company);
			text_post.setText(person.position);
			text_area.setText(getArea(person));

			for (int i = 0; i < person.contactInformationList.size(); i++) {
				if (person.contactInformationList.get(i).type.equalsIgnoreCase("1")) {
						text_phone_num.setText(person.contactInformationList.get(i).content);
				}
				if (person.contactInformationList.get(i).type.equalsIgnoreCase("4")) {
//					if (person.contactInformationList.get(i).subtype.equalsIgnoreCase("1")) {
						text_email.setText(person.contactInformationList.get(i).content);
//					}
				}
			}
			List<Basic> bList = person.getContactInformationList();
			if (bList != null) {
				for (int i = 0; i < bList.size(); i++) {
					Basic basic = bList.get(i);
					if (null != basic.type && Integer.valueOf(basic.type) == 6) {
						getBasicInfo(basic);
					}
				}
			}
			if (!TextUtils.isEmpty(qq))
				text_qq.setText(qq);
			if (!TextUtils.isEmpty(weixin))
				text_weixin.setText(weixin);
			if (!TextUtils.isEmpty(weibo))
				text_weibo.setText(weibo);
		}
		mIndustrys = new MIndustrys();
		mIndustrys.setListIndustry(mMainApp.getAppData().getUser().getListIndustry());
		showSettingIndustry();
	}

	private void initView(View view) {
		circle_avatar_edit.setOnClickListener(this);// 头像
		text_gender.setOnClickListener(this);
		text_classify.setOnClickListener(this);
		text_area.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.circle_avatar_edit:// 头像
			ENavigate.startSelectPictureActivityforSingleSelection(getActivity(), 9, picture, true);
			break;
		case R.id.text_gender:// 性别
			Intent intent = new Intent(mContext, GenderActivity.class);
			startActivityForResult(intent, ENavConsts.ActivityReqCode.REQUEST_CODE_GET_GENDER);

			break;
		case R.id.text_classify:// 职能即行业
			ENavigate.startChooseProfessionActivityForResult(getActivity(), ENavConsts.ActivityReqCode.REQUEST_CODE_SETTING_INDUSTRY_ACTIVITY, 0, mIndustrys);

			break;
		case R.id.text_area:// 地区
			ENavigate.startChooseActivityForResult(getActivity(), false, "区域", ChooseDataUtil.CHOOSE_type_Area, null);
			break;

		default:
			break;
		}

	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case PeopleRequestType.PEOPLE_REQ_CREATE:
			dismissLoadingDialog();
			if (object != null) {
				// 数据是使用Intent返回
				Intent intent = new Intent();
				BaseResult result = (BaseResult) object;
				if (result.success){
					String  userIDString = people_details.getPeople().getCreateUserId()+"";
					if(App.getUserID().equals(userIDString) && is_self_bool){
						if (url != null) {
							App.getUser().setImage(url);
							intent.putExtra("url", url);
						}
						App.getUser().setNick(text_full_name.getText());
						App.getUser().setmUserName(text_full_name.getText());
					}
					
					// 把返回数据存入Intent
					
					intent.putExtra("people", people_request.people);
					getActivity().setResult(getActivity().RESULT_OK, intent);
					// 关闭Activity
					finishParentActivity();
				}
				else
					Toast.makeText(mContext, "保存失败！", 0).show();
			} else {
				Toast.makeText(mContext, "保存失败！", 0).show();

			}
			break;

		default:
			break;
		}
	}

	public void handerRequsetCode(int requestCode, Intent intent) {
		switch (requestCode) {
		case ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT://地区
			// 多级选择回调界面
			setChooseText((ArrayList<Metadata>) intent.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA));
			break;
		case ENavConsts.ActivityReqCode.REQUEST_CODE_SETTING_INDUSTRY_ACTIVITY:// 职能即行业
			mIndustrys = (MIndustrys) intent.getExtras().getSerializable(EConsts.Key.INDUSTRYS);
			handler.sendEmptyMessageDelayed(0, 100);
			break;
		case 9:// 头像

			picture = (ArrayList<JTFile>) intent.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
			System.out.println("上传头像---picture:--" + picture);
			if (picture != null && !picture.isEmpty() && picture.size() > 0) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(picture.get(0).mLocalFilePath, options);

				// Calculate inSampleSize
				options.inSampleSize = calculateInSampleSize(options, 480, 800);

				// Decode bitmap with inSampleSize set
				options.inJustDecodeBounds = false;

				Bitmap bm = BitmapFactory.decodeFile(picture.get(0).mLocalFilePath, options);
				circle_avatar_edit.setImageBitmap(bm);
				// 调上传头像接口
				OrganizationPictureUploader uploader = new OrganizationPictureUploader(this);
				JTFile jtFile = new JTFile();
				jtFile.setId(String.valueOf(picture.get(0).mCreateTime));
				jtFile.mLocalFilePath = picture.get(0).mLocalFilePath;
				jtFile.mType = 4;
				uploader.startNewUploadTask(jtFile);
			}

			break;
		default:
			break;
		}
	}

	public void handerResultCode(int resultCode, Intent intent) {
		switch (resultCode) {
		case 22:// 性别
			String gender = intent.getStringExtra("gender");
			text_gender.setText(gender);
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// 各种activity界面回调方法
		if (null != intent) {
			handerRequsetCode(requestCode, intent);
			handerResultCode(resultCode, intent);
		}
		// super.onActivityResult(requestCode, resultCode, intent);
	}

	public void setChooseText(ArrayList<Metadata> data) {
		// 地区
		if (metadataArea != null) {
			metadataArea.clear();
		}
		metadataArea = data;
		area_result = ChooseDataUtil.getMetadataName(metadataArea);
		// area_Etv.setText((TextUtils.isEmpty(area_result.province) ? ""
		// : area_result.province)
		// + (TextUtils.isEmpty(area_result.city) ? "" : area_result.city)
		// + (TextUtils.isEmpty(area_result.county) ? ""
		// : area_result.county));
		// 去除直辖市名字重叠的问题
		text_area.setText(getAreaStr(area_result));
		// 将这些值赋值给创建人脉的对象，避免值的丢失
		person.locationCountry = area_result.province;
		person.locationCounty = area_result.county;
		person.locationCity = area_result.city;
		if (metadataArea != null) {
			if (!metadataArea.isEmpty()) {
				for (int i = 0; i < metadataArea.size(); i++) {
					person.regionId = Long.parseLong(metadataArea.get(i).id);

					for (Metadata metadata : metadataArea) {
						if (!metadata.childs.isEmpty()) {

							// 有二级
							for (Metadata data2 : metadata.childs) {
								// 有三级
								if (!data2.childs.isEmpty()) {

									for (Metadata data3 : data2.childs) {
										if (!data3.childs.isEmpty()) {
											person.regionId = Long.parseLong(data3.id);
										}
									}
									person.regionId = Long.parseLong(data2.id);
								}
							}
						}
						person.regionId = Long.parseLong(metadata.id);
					}

				}

			}
		}
	}

	/**
	 * 获取地区对象
	 * 
	 * @param area_result
	 * @return
	 */
	public String getAreaStr(Area area_result) {
		String area = "";
		if (area_result != null) {
			String province = TextUtils.isEmpty(area_result.province) ? "" : area_result.province;
			String city = TextUtils.isEmpty(area_result.city) ? "" : area_result.city;
			String county = TextUtils.isEmpty(area_result.county) ? "" : area_result.county;
			if (city.equalsIgnoreCase(province)) {
				area = province + county;
			} else {
				area = province + city + county;
			}

		}
		return area;
	}

	/**
	 * 计算裁剪尺寸
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			showSettingIndustry();
		};
	};
	private String url;  //上传图片的全路劲

	private void showSettingIndustry() {
		if (null != mIndustrys && null != mIndustrys.getListIndustry() && mIndustrys.getListIndustry().size() > 0) {
			mIndustryList = mIndustrys.getListIndustry();
			strBuilder = new StringBuilder("");
			for (int i = 0; i < mIndustryList.size(); i++) {
				if (mIndustryList.size() - 1 == i) {
					strBuilder.append(mIndustryList.get(i).getName());
				} else {
					strBuilder.append(mIndustryList.get(i).getName() + "、");
				}
			}
			mMainApp.getAppData().setmIndustrys(mIndustrys);
			mMainApp.getAppData().getUser().setListIndustry(mIndustryList);
			text_classify.setText(strBuilder.toString().trim());
		} else {
			text_classify.setText("还未设置您感兴趣的行业");
		}
	}

	public void onSave() {
		showLoadingDialog();
		if (StringUtils.isEmpty(text_full_name.getText())) {
			Toast.makeText(mContext, "请输入您的姓名", 1).show();
			dismissLoadingDialog();
			return;
		}
		people_request = new PeopleRequest(); // 向后台传入的对象
		people_request.opType = "5";// 编辑个人资料
		if (avatarUrl != null) {
			person.portrait = avatarUrl;// 表示重新上传了一张头像
		}
		if ("男".equals(text_gender.getText())) {
			person.gender = 1;
		} else if ("女".equals(text_gender.getText())) {
			person.gender = 2;
		} else {
			person.gender = 3;
		}
		person.company = text_company.getText();
		person.position = text_post.getText();
		App.getUser().getJTContact().userJob =  text_post.getText();
		person.getPeopleNameList().get(0).lastname=text_full_name.getText().toString().trim();
		person.getPeopleNameList().get(0).firstname="";
		person.email = text_email.getText();
		
		person.telephone = text_phone_num.getText();
		List<Basic> bList = person.getContactInformationList();
		if (null != bList) {
			Iterator<Basic> sListIterator = bList.iterator();
			while (sListIterator.hasNext()) {
				Basic basic = sListIterator.next();
				if (!TextUtils.isEmpty(basic.subtype)) {
					if (Integer.valueOf(basic.subtype) == 1) {
						sListIterator.remove();
					} else if (Integer.valueOf(basic.subtype) == 2) {
						sListIterator.remove();
					} else if (Integer.valueOf(basic.subtype) == 3) {
						sListIterator.remove();
					}
				}
			}
		} else {
			bList = new ArrayList<Basic>();
		}
		Basic telePhone = new Basic("手机号", text_phone_num.getText(), "1", "1");
		Basic email = new Basic("邮箱", text_email.getText(), "4", "1");
		Basic qq = new Basic("QQ", text_qq.getText(), "6", "1");
		Basic weixin = new Basic("微信", text_weixin.getText(), "6", "2");
		Basic weibo = new Basic("微博", text_weibo.getText(), "6", "3");
		bList.add(telePhone);
		bList.add(email);
		bList.add(qq);
		bList.add(weixin);
		bList.add(weibo);
		person.setContactInformationList(bList);
		people_request.people = person;
		for (int i = 0; people_details.tid != null && i < people_details.tid.size(); i++) {
			PersonTagRelation personTagRelation = people_details.tid.get(i);
			tid.add(personTagRelation.tagId);
		}
		people_request.tid = tid;

		for (int i = 0; people_details.categoryList != null && i < people_details.categoryList.size(); i++) {
			Long categoryId = people_details.categoryList.get(i).id;
			categoryList.add(categoryId);
		}
		people_request.categoryList = categoryList;
		if (people_details.people.permIds != null) {
			permIds = people_details.people.permIds;
		}
		people_request.permIds = permIds;
		people_request.asso = people_details.asso;

		PeopleReqUtil.doRequestWebAPI(mContext, this, people_request, null, PeopleRequestType.PEOPLE_REQ_CREATE);
	}

	// /********上传头像回调方法***********///
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
		url = result.get("url");
		KeelLog.d("===>>onSuccess", url);

		if (!TextUtils.isEmpty(url)) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(mContext, "上传成功", 0).show();
				}
			});

		}
		avatarUrl = result.get("urlToSql"); //
		KeelLog.d("===>>onSuccess", url);
	}

	@Override
	public void onError(String id, int code, final String message) {
		KeelLog.d("===>>OnError", message);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
	// /********上传头像回调方法***********///
}
