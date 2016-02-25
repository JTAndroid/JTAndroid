package com.tr.ui.user.modified;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.home.MIndustry;
import com.tr.model.home.MPageIndustrys;
import com.tr.model.home.MUserProfile;
import com.tr.model.user.JTMember;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.Util.DensityUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class MarkTagActivity extends JBaseActivity implements IBindData {

	private List<MIndustry> data;
	private TextView goToMain;
	private FrameLayout mFlowLayout;
	private MUserProfile mUserProfile;
	private String nick, image, company, hangye;
	private int sex;

	private App mMainApp;
	
	private List<String> ids = new ArrayList<String>();
	private List<String> industrys = new ArrayList<String>();
	
	private int width;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_mark_tag);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;//宽度height = dm.heightPixels ;//高度
		// 全局对象
		mMainApp = App.getApp();
		nick = getIntent().getStringExtra("nick");
		image = getIntent().getStringExtra("image");
		sex = getIntent().getIntExtra("sex", 0);
		company = getIntent().getStringExtra("company");
		hangye = getIntent().getStringExtra("hangye");
		
		goToMain = (TextView) findViewById(R.id.goToMain);
		mFlowLayout = (FrameLayout) findViewById(R.id.flowlayout);
		CommonReqUtil.doGetInterestIndustry(this, this, 1, 10000, null);
		mUserProfile = new MUserProfile();
		mUserProfile.setShortName(nick);
		mUserProfile.setImage(image);
		mUserProfile.setGender(sex);
		mUserProfile.setCompanyName(company);
		mUserProfile.setIndustry(hangye);
		
		goToMain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mUserProfile.setListCareIndustryIds(ids);
				mUserProfile.setListCareIndustryNames(industrys);
				
				CommonReqUtil.doUploadUserProfile(MarkTagActivity.this, MarkTagActivity.this, mUserProfile, null);
			}
		});
		goToMain.setClickable(false);
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "偏好标签选择", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}


	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (tag == EAPIConsts.CommonReqType.GetInterestIndustry && object != null) {
			MPageIndustrys mProfessions = (MPageIndustrys) object;
			data = mProfessions.getPage().getListIndustry();
			init();
		}else if (tag == EAPIConsts.CommonReqType.UploadUserProfile) {
			if (object != null) {
				DataBox dataBox = (DataBox) object;
				if (dataBox.mJTMember == null) {
					showToast("完善资料失败，请重试");
					return;
				}
				mMainApp.getAppData().setUser(dataBox.mJTMember);
				String nick = App.getNick();
				String image = App.getUser().getImage();
				JTMember member = mMainApp.getAppData().getUser();
				Log.d("", nick + image + member);
//				showToast("注册成功");
				// ENavigate.startMainActivity(RegisterPersonalDetailActivity.this);
				EMChatManager.getInstance().login(mMainApp.getAppData().getUserID(),
						mMainApp.getAppData().getUserID(), new EMCallBack() {// 回调
							@Override
							public void onSuccess() {
								runOnUiThread(new Runnable() {
									public void run() {
										EMGroupManager.getInstance().loadAllGroups();
										EMChatManager.getInstance().loadAllConversations();
									}
								});
								
							}

							@Override
							public void onProgress(int progress, String status) {
							}

							@Override
							public void onError(int code, String message) {
								Log.d("main", "登陆聊天服务器失败！");
							}
						});
				ENavigate.startWantPeopleActivity(MarkTagActivity.this);
			} else {
				showToast("完善资料失败，请重试");
			}
		}
		
	}

	private void init() {
		for(int i=0;i<data.size();i++){
			mFlowLayout.addView(getBT(data.get(i), i));
		}
	}

	private Button getBT(final MIndustry mid, int index) {
		final Button cb = new Button(this);
		cb.setText(mid.getName());
		cb.setTextColor(getResources().getColor(R.color.text_flow_content));
		cb.setBackgroundResource(R.drawable.cb_bg);
		cb.setGravity(Gravity.CENTER);
		
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		switch(index){
		case 0:
			lp.leftMargin = Math.round(46*width/720.0f*2);
			lp.topMargin = Math.round(40*width/720.0f*2);
			lp.width = Math.round(width*2.0f/9);
			lp.height = Math.round(width*2.0f/9);
			break;
		case 1:
			lp.leftMargin = Math.round(134*width/720.0f*2);
			lp.topMargin = Math.round(24*width/720.0f*2);
			lp.width = Math.round(width*5.0f/18);
			lp.height = Math.round(width*5.0f/18);
			break;
		case 2:
			lp.leftMargin = Math.round(244*width/720.0f*2);
			lp.topMargin = Math.round(34*width/720.0f*2);
			lp.width = Math.round(width*2.0f/9);
			lp.height = Math.round(width*2.0f/9);
			break;
		case 3:
			lp.leftMargin = Math.round(21*width/720.0f*2);
			lp.topMargin = Math.round(128*width/720.0f*2);
			lp.width = Math.round(width*5.0f/18);
			lp.height = Math.round(width*5.0f/18);
			break;
		case 4:
			lp.leftMargin = Math.round(124*width/720.0f*2);
			lp.topMargin = Math.round(125*width/720.0f*2);
			lp.width = Math.round(width*2.0f/9);
			lp.height = Math.round(width*2.0f/9);
			break;
		case 5:
			lp.leftMargin = Math.round(207*width/720.0f*2);
			lp.topMargin = Math.round(111*width/720.0f*2);
			lp.width = Math.round(width*2.0f/9);
			lp.height = Math.round(width*2.0f/9);
			break;
		case 6:
			lp.leftMargin = Math.round(82*width/720.0f*2);
			lp.topMargin = Math.round(215*width/720.0f*2);
			lp.width = Math.round(width*5.0f/18);
			lp.height = Math.round(width*5.0f/18);
			break;
		case 7:
			lp.leftMargin = Math.round(183*width/720.0f*2);
			lp.topMargin = Math.round(204*width/720.0f*2);
			lp.width = Math.round(width*2.0f/9);
			lp.height = Math.round(width*2.0f/9);
			break;
		case 8:
			lp.leftMargin = Math.round(261*width/720.0f*2);
			lp.topMargin = Math.round(174*width/720.0f*2);
			lp.width = Math.round(width*2.0f/9);
			lp.height = Math.round(width*2.0f/9);
			break;
		case 9:
			lp.leftMargin = Math.round(24*width/720.0f*2);
			lp.topMargin = Math.round(292*width/720.0f*2);
			lp.width = Math.round(width*5.0f/18);
			lp.height = Math.round(width*5.0f/18);
			break;
		case 10:
			lp.leftMargin = Math.round(144*width/720.0f*2);
			lp.topMargin = Math.round(299*width/720.0f*2);
			lp.width = Math.round(width*5.0f/18);
			lp.height = Math.round(width*5.0f/18);
			break;
		case 11:
			lp.leftMargin = Math.round(243*width/720.0f*2);
			lp.topMargin = Math.round(262*width/720.0f*2);
			lp.width = Math.round(width*5.0f/18);
			lp.height = Math.round(width*5.0f/18);
			break;
		}
		cb.setLayoutParams(lp);

		final Animation scale_out = AnimationUtils.loadAnimation(this,
				R.anim.scale_out);
		final Animation scale_in = AnimationUtils.loadAnimation(this,
				R.anim.scale_in);
		
		cb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!ids.contains(mid.getId()) && !industrys.contains(mid.getName())){
					ids.add(mid.getId());
					industrys.add(mid.getName());
					cb.setBackgroundResource(R.drawable.cb_bg_selected);
					cb.setTextColor(getResources().getColor(R.color.text_white));
					scale_out.setFillAfter(true);
					cb.startAnimation(scale_out);
				}else{
					ids.remove(mid.getId());
					industrys.remove(mid.getName());
					cb.setBackgroundResource(R.drawable.cb_bg);
					cb.setTextColor(getResources().getColor(R.color.text_flow_content));
					scale_in.setFillAfter(true);
					cb.startAnimation(scale_in);
				}
				//开启金桐之旅是否可点击
				isChecked();
			}
		});
		
		return cb;
	}
	
	private void isChecked(){
		if(ids.size()==0){
			goToMain.setBackgroundResource(R.drawable.button_circle_noclick);
			goToMain.setClickable(false);
		}else{
			goToMain.setBackgroundResource(R.drawable.button_circle_click);
			goToMain.setClickable(true);
		}
	}
}
