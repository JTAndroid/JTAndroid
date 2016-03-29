package com.tr.ui.home.frg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tongmeng.alliance.activity.MysettingActivity;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.ConferenceReqUtil;
import com.tr.db.ConnectionsCacheData;
import com.tr.model.CommunityStateResult;
import com.tr.model.home.MHomePageNumInfo;
import com.tr.model.home.MHomePageNumInfos;
import com.tr.model.home.MainPageList;
import com.tr.model.user.JTMember;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.communities.home.CommunitiesActivity;
import com.tr.ui.communities.home.MyCommunitiesActivity;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.home.HomePageActivity;
import com.tr.ui.home.MainActivity;
import com.tr.ui.knowledge.MyKnowledgeActivity;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

/**
 * @author Sunjianan
 * @description 我的
 */
public class FrgMyHomePage extends JBaseFragment implements OnClickListener,
		IBindData {
	private File cameraPhotoFile;
	@ViewInject(R.id.avatar)
	private ImageView avatar;
	@ViewInject(R.id.my_name)
	private TextView myName;
	@ViewInject(R.id.my_counter)
	private TextView myCounter;
	@ViewInject(R.id.avatarText_MyHomePage)
	private TextView avatarText_MyHomePage;
	@ViewInject(R.id.my_base_info)
	private RelativeLayout myBaseInfoRl;

	@ViewInject(R.id.friend_and_relation)
	private RelativeLayout friendsRl;

	@ViewInject(R.id.requirment)
	private RelativeLayout requirmentRl;

	@ViewInject(R.id.knowledge)
	private RelativeLayout knowledgesRl;

	@ViewInject(R.id.conference)
	private RelativeLayout conferenceRl;
	@ViewInject(R.id.file_management)
	private RelativeLayout fileManagementRl;
	@ViewInject(R.id.share_gintong)
	private RelativeLayout shareGintongRl;
	@ViewInject(R.id.my_community)
	private RelativeLayout my_community;

	private PopupWindow popupWindow;

	@ViewInject(R.id.friend_num_tv)
	private TextView friend_num_tv;
	@ViewInject(R.id.file_management_num_tv)
	private TextView file_management_num_tv;
	@ViewInject(R.id.requirment_num_tv)
	private TextView requirment_num_tv;
	@ViewInject(R.id.knowledge_num_tv)
	private TextView knowledge_num_tv;
	@ViewInject(R.id.conference_num_tv)
	private TextView conference_num_tv;
	@ViewInject(R.id.my_community_num_tv)
	private TextView my_community_num_tv;
	@ViewInject(R.id.organization)
	private RelativeLayout organization;
	@ViewInject(R.id.organization_num_tv)
	private TextView organizationNumTv;
	@ViewInject(R.id.img_my_community_red_fl)
	private FrameLayout img_my_community_red_fl;

	public static final String IMAGE_UNSPECIFIED = "image/*";
	private final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/gintong/photo");
	private List<MHomePageNumInfo> list;
	private String qrUrl;
	private boolean mIsVisibleToUser = false;
	private int SCALESIZE = 100;
	private int num;
	private boolean initOVer = false;
	private ArrayList<CommunityStateResult> mCommunityStateResultList = new ArrayList<CommunityStateResult>(); // 返回数据

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_my_home_page, null);
		ViewUtils.inject(this, view);
		setHasOptionsMenu(true);
		setListener();
		initData();
		initOVer = true;
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (initOVer)
				initData();
			CommonReqUtil.doGetMyCountList(getActivity(), this, 0, null);
			ConferenceReqUtil.getCommunityState(getActivity(), this, null,
					App.getUserID());
		}
	}

	// 初始化数据
	private void initData() {
		if (App.getApp().getAppData().getUser().getmUserType() == JTMember.UT_PERSON) {
			String people_name;
			if (!StringUtils.isEmpty(App.getUser().getmNick())) {
				people_name = App.getUser().getmNick();
			} else if (!StringUtils.isEmpty(App.getUser().getmUserName())) {
				people_name = App.getUser().getmUserName();
			} else {
				people_name = App.getUser().getmNick();
			}
			Util.initAvatarImage(getActivity(), avatar, people_name, App
					.getUser().getImage(), 0, 1);
			// 个人用户
			if (!StringUtils.isEmpty(App.getUser().getmNick())) {
				myName.setText(App.getUser().getmNick());
			} else if (!StringUtils.isEmpty(App.getUser().getmUserName())) {
				myName.setText(App.getUser().getmUserName());
			} else {
				myName.setText("");
			}

		}
		// 机构用户
		else {
			String org_name = TextUtils.isEmpty(App.getUser().mNick) ? App
					.getUser().getmOrganizationInfo().mFullName
					: App.getUser().mNick;
			Util.initAvatarImage(getActivity(), avatar, org_name, App.getUser()
					.getImage(), 0, 2);
			if (!StringUtils.isEmpty(App.getUser().mNick)) {
				myName.setText(App.getUser().mNick);
			} else if (!StringUtils.isEmpty(App.getUser()
					.getmOrganizationInfo().mFullName)) {
				myName.setText(App.getUser().getmOrganizationInfo().mFullName);
			} else {
				myName.setText("");
			}
		}

		// 显示公司名
		myCounter.setText(App.getUser().getJTContact().mcompany + " "/*
																	 * +App.getUser
																	 * ().
																	 * getJTContact
																	 * (
																	 * ).userJob
																	 */);
	}

	/** 添加点击事件 */
	private void setListener() {
		myCounter.setOnClickListener(this);
		myBaseInfoRl.setOnClickListener(this);
		friendsRl.setOnClickListener(this);
		requirmentRl.setOnClickListener(this);
		knowledgesRl.setOnClickListener(this);
		conferenceRl.setOnClickListener(this);
		fileManagementRl.setOnClickListener(this);
		shareGintongRl.setOnClickListener(this);
		organization.setOnClickListener(this);
		my_community.setOnClickListener(this);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.avatar:
			/*
			 * <<<<<<< HEAD case R.id.my_base_info: case R.id.my_counter: if
			 * (App.getUser().getmUserType() == JTMember.UT_PERSON) {
			 * ENavigate.startRelationHomeActivity(getActivity(),
			 * App.getApp().getAppData().getUser().mID, true,
			 * ENavConsts.type_details_member); } else if
			 * (App.getUser().getmUserType() == JTMember.UT_ORGANIZATION) { if
			 * (!App.getApp().getAppData().getUser().getmOrganizationInfo().
			 * mLegalPersonIDCardImage.equals("null")) {
			 * ENavigate.startOrgMyHomePageActivity(getActivity(),
			 * Long.valueOf(App
			 * .getApp().getAppData().getUser().getmOrganizationInfo
			 * ().mLegalPersonIDCardImage), Long.valueOf(App.getUserID()), true,
			 * ENavConsts.type_details_org); } else {
			 * Toast.makeText(getActivity(), "数据加载失败",
			 * Toast.LENGTH_SHORT).show(); }
			 * 
			 * }
			 * 
			 * break;
			 * 
			 * case R.id.friend_and_relation://人脉 TimeUtil.isFastDoubleClick();
			 * ENavigate.startMyFriendAll(getActivity(),
			 * ConnectionsCacheData.FILTER_PEOPLE_ALL,num);
			 * 
			 * break; case R.id.requirment://需求 =======
			 */
			break;
		case R.id.my_base_info:
			// ENavigate.startRelationHomeActivity(getActivity(),
			// App.getApp().getAppData().getUser().mID, true,
			// ENavConsts.type_details_member);
			startActivity(new Intent(getActivity(), HomePageActivity.class));
			break;
		case R.id.friend_and_relation:
			TimeUtil.isFastDoubleClick();
			ENavigate.startMyFriendAll(getActivity(),
					ConnectionsCacheData.FILTER_PEOPLE_ALL, num);
			break;
		case R.id.requirment:// 我的需求
			ENavigate.startMeNeedActivity(getActivity());
			break;
		case R.id.knowledge:// 知识
			startActivity(new Intent(getActivity(), MyKnowledgeActivity.class));
			break;
		case R.id.conference:// 会议
//			ENavigate.startMyMeetingActivity(getActivity());
			Intent settingIntent = new Intent(getActivity(),MysettingActivity.class);
			startActivity(settingIntent);
			break;
		case R.id.file_management:// 文件
			ENavigate.startFileManagementActivity(getActivity(), null, null);
			break;
		case R.id.share_gintong:
			FrameWorkUtils.showSharePopupWindow(getActivity(), qrUrl);
			break;
		case R.id.organization:
			ENavigate.startOrganizationActivity(getActivity());
			break;
		case R.id.my_community:
			Intent intent = new Intent(getActivity(),
					MyCommunitiesActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("mCommunityStateResultList",
					mCommunityStateResultList);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}

	/** 点击头像后，弹出popupwindow */
	private void showAvatarPopWindow() {
		View view = View.inflate(getActivity(), R.layout.popup_window_avatar,
				null);
		TextView tvCapture = (TextView) view.findViewById(R.id.take_a_photo);
		TextView tvFromAlbum = (TextView) view
				.findViewById(R.id.get_from_album);
		TextView tvCancel = (TextView) view.findViewById(R.id.cancel);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		popupWindow.setAnimationStyle(R.style.ConnsDialogAnim);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		tvCapture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cameraPhotoFile = new File(PHOTO_DIR, String.valueOf(System
						.currentTimeMillis()) + ".jpg");
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) {
					// 用户点击了从照相机获取
					try {
						// 创建照片的存储目录
						if (!PHOTO_DIR.exists()) {
							PHOTO_DIR.mkdirs();
						}
						cameraPhotoFile = new File(PHOTO_DIR, String
								.valueOf(System.currentTimeMillis()) + ".jpg");// UUID
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE, null);
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(cameraPhotoFile));
						getActivity().startActivityForResult(intent,
								MainActivity.REQUEST_CODE_PHOTO_GRAPH);
						popupWindow.dismiss();
					} catch (ActivityNotFoundException e) {
						Toast.makeText(getActivity(), "找不到相机",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), "没有SD卡", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		tvFromAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_UNSPECIFIED);
				getActivity().startActivityForResult(intent,
						MainActivity.REQUEST_CODE_PHOTO_ALBUM);
				popupWindow.dismiss();
			}
		});
		tvCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.myhomepage_menu, menu);
		menu.findItem(R.id.home_new_menu_more).setVisible(false);
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		// menu.findItem(R.id.aff_create).setVisible(false);
		// menu.findItem(R.id.affairs_new_menu_list).setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		//
		case R.id.my_home_page_set:
			// Toast.makeText(getActivity(), "第二个图标", 0).show();
			ENavigate.startSettingActivity(getActivity());
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MainActivity.REQUEST_CODE_PHOTO_GRAPH) {
			Uri originalUri = Uri.fromFile(cameraPhotoFile);
			startPhotoZoom(originalUri);
		}
		if (requestCode == MainActivity.REQUEST_CODE_PHOTO_ALBUM) {
			// TODO 相册返回数据
			Uri originalUri = data.getData();
			startPhotoZoom(originalUri);
		}
		if (requestCode == MainActivity.REQUEST_CODE_PHOTO_TRIM) {
			// TODO 图片裁剪
			Bitmap bitmap = data.getExtras().getParcelable("data");
			avatar.setImageBitmap(bitmap);
			// Toast.makeText(getActivity(), "dsfsdfsdfsdf", 0).show();
		}
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		getActivity().startActivityForResult(intent,
				MainActivity.REQUEST_CODE_PHOTO_TRIM);
	}

	@Override
	public void onResume() {
		super.onResume();
		FrameWorkUtils.dismissLoadingDialog();
		initData();
	}

	@Override
	public void bindData(int tag, Object object) {
		// dismissLoadingDialog();
		try {
			App.getUserID();
			if (tag == EAPIConsts.CommonReqType.GetMyCountList
					&& object != null) {
				MHomePageNumInfos infos = (MHomePageNumInfos) object;
				list = infos.getList();
				init();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (tag == EAPIConsts.CloudDiskType.categoryDocumentSum
				&& object != null) {
			HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
			String sum = (String) dataMap.get("sum");
			// file_management_num_tv.setText("(" +
			// FileManagementActivity.FormetFileSize(Long.parseLong(sum)) +
			// ")");
		}
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_COMMUNITY_STATE
				&& object != null) {
			mCommunityStateResultList = (ArrayList<CommunityStateResult>) object;
			if (mCommunityStateResultList != null
					&& mCommunityStateResultList.size() > 0) {
				my_community_num_tv.setText("("
						+ mCommunityStateResultList.size() + ")");
				for (CommunityStateResult iterable_element : mCommunityStateResultList) {
					if (iterable_element.getNewMessageRemind() == 0
							&& iterable_element.getNewCount() > 0) {
						// 显示红点
						img_my_community_red_fl.setVisibility(View.VISIBLE);
						break;
					} else {
						// 隐藏
						img_my_community_red_fl.setVisibility(View.GONE);
					}
				}
			} else {
				img_my_community_red_fl.setVisibility(View.GONE);
			}
		}
	}

	private void init() {
		initData();
		for (int i = 0; i < list.size(); i++) {
			switch (list.get(i).getType()) {
			// 好友人脉
			case 1:
				friend_num_tv.setText("(" + list.get(i).getNum() + ")");
				num = list.get(i).getNum();
				break;
			// 2-组织（客户
			case 2:
				organizationNumTv.setText("(" + list.get(i).getNum() + ")");
				break;
			// 3-需求
			case 3:
				requirment_num_tv.setText("(" + list.get(i).getNum() + ")");
				break;
			// 知识
			case 4:
				knowledge_num_tv.setText("(" + list.get(i).getNum() + ")");
				break;
			// 会议
			case 5:
				conference_num_tv.setText("(" + list.get(i).getNum() + ")");
				break;
			}
		}
	}
}
