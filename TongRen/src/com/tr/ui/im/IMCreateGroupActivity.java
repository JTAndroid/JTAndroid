package com.tr.ui.im;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.IMReqUtil;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.widgets.FileUploaderLinearLayout;
import com.tr.ui.widgets.time.SelectBirthday;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 创建会议时的第一个页面，设置会议主题、会议文件，时间等内容 从群聊修改会议主题，也进入该页面
 * 
 * @ClassName: IMCreateGroupActivity.java
 * @Description: TODO(用一句话描述该文件做什么)
 * @version V1.0
 * @Date 2014-4-23 下午3:58:47
 */
public class IMCreateGroupActivity extends JBaseFragmentActivity implements
		IBindData {
	/** Called when the activity is first created. */

	private LinearLayout uploadList;
	private TextView mViewTime;// 会议时间
	private View mViewTimeBG;// 会议时间背景
	private EditText mEtSubject;// 会议主题
	private View mViewFile;// 文件管理部分
	private EditText mEtContent;// 会议内容

	private FileUploaderLinearLayout uploaderLl; // 文件上传控件

	private MUCDetail mucDetail;
	private String fromActivityName;
	
	private boolean mChangeSubject = false;
	private boolean mChangeContent = false;
	private boolean mChangeOrderTime = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getParam();
		setContentView(R.layout.im_creategroup_list);

		// 文件上传控件
		uploaderLl = (FileUploaderLinearLayout) findViewById(R.id.im_conference_detai_uploaderLl);

		mViewTime = (TextView) this
				.findViewById(R.id.im_conference_detail_time);
		mViewTimeBG = this.findViewById(R.id.conference_detail_time_bg);
		final SelectBirthday birth = new SelectBirthday(
				IMCreateGroupActivity.this,
				new SelectBirthday.SelectBirthdayListener() {

					@Override
					public void onSelectBirthdayListener(String time) {
						// TODO Auto-generated method stub
						mViewTime.setText(time);
					}
				},SelectBirthday.type_all);
		birth.initTime();
		mViewTimeBG.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				birth.showTime(mViewTime.getText().toString());
				birth.showAtLocation(IMCreateGroupActivity.this
						.findViewById(R.id.im_conference_detail_root),
						Gravity.CENTER, 0, 0);

			}
		});

		mEtSubject = (EditText) this.findViewById(R.id.im_detail_txt_subject);
		mEtContent = (EditText) this
				.findViewById(R.id.im_conference_detail_et_content);

		updateData();
	}

	// 读取传入的参数
	public void getParam() {
		Intent intent = getIntent();
		fromActivityName = intent.getStringExtra(ENavConsts.EFromActivityName);
		if (fromActivityName.equalsIgnoreCase(ENavConsts.EMainActivity)) {
			// 从首页创建群聊过来
			mucDetail = new MUCDetail();
			mucDetail.setSubject("");
			mucDetail.setTitle(mucDetail.getSubject());
			mucDetail.setConference(true);
			mucDetail.setContent("");
			mucDetail.setOrderTime(EUtil.getFormatFromDate(new Date()));
			// List<JTFile> listfile = new ArrayList<JTFile>();
			// for (int i = 0; i < 3; i++) {
			// JTFile file = new JTFile();
			// file.setmType(JTFile.TYPE_IMAGE);
			// file.setmUrl("http://192.168.101.131/02.jpg");
			// file.setmSuffixName("jpg");
			// listfile.add(file);
			// }
		} else if (fromActivityName
				.equalsIgnoreCase(ENavConsts.EIMEditMemberActivity)) {
			// 从群聊设置页过来
			mucDetail = (MUCDetail) intent
					.getSerializableExtra(ENavConsts.EMucDetail);
		} else {
			// 从其他地方过来
			mucDetail = (MUCDetail) intent
					.getSerializableExtra(ENavConsts.EMucDetail);
		}
	}

	/**
	 * actionbar 中菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.conference_create_next:{
			String strSubject = mEtSubject.getText().toString();
			String strTime = mViewTime.getText().toString();
			String strContent = mEtContent.getText().toString();
			if(uploaderLl.isFileUploading()){
				//
				showToast("请等待文件上传完毕再提交");
				return true;
			}
			
			if(TextUtils.isEmpty(strSubject)){
				showToast("请填写会议主题");
				return true;
			}
			
			if(TextUtils.isEmpty(strTime)){
				showToast("请填写会议开始时间");
				return true;
			}
			
			if(TextUtils.isEmpty(strContent)){
				showToast("请填写会议内容");
				return true;
			}
			if(!mucDetail.getSubject().equals(strSubject)){
				mChangeSubject = true;
			}
			mucDetail.setSubject(strSubject);
			if(!mucDetail.getContent().equals(strContent)){
				mChangeContent = true;
			}
			mucDetail.setContent(strContent);
			if(!mucDetail.getOrderTime().equals(strTime)){
				mChangeOrderTime = true;
			}
			mucDetail.setOrderTime(strTime);
			
			List<JTFile> listJTFile = uploaderLl.getListJTFile();
			mucDetail.setListJTFile(listJTFile);
			
			if (fromActivityName.equalsIgnoreCase(ENavConsts.EMainActivity)) {
				// 首页-创建会议流程
				ENavigate.startIMRelationSelectActivity(this, mucDetail, null,ENavigate.REQUSET_CODE,null,null);
			}
			else if (fromActivityName.equalsIgnoreCase(ENavConsts.EIMEditMemberActivity)) {
				// 群聊设置页进来修改会议主题,修改完后,返回群聊设置页
				modifyConference();
				// ENavigate.startIMRelationSelectActivity(this, detail, null,
				// ENavigate.REQUSET_CODE);
			} 
			else {
				finish();
			}
		}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.im_chatmenu, menu);
		return true;
	}

	@Override
	public void initJabActionBar() {
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setTitle("创建会议");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 可以根据多个请求代码来作相应的操作
		if ((ENavigate.REQUSET_CODE == requestCode)
				&& (resultCode == RESULT_OK)) {
			// 创建会成功后返回
			MUCDetail nowDetail = (MUCDetail) data
					.getSerializableExtra(ENavConsts.EMucDetail);
			if (nowDetail != null) {
				finish();
				ENavigate.startIMGroupActivity(this, nowDetail);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void updateData() {
		if (!TextUtils.isEmpty(mucDetail.getSubject())) {
			this.mEtSubject.setText(mucDetail.getSubject());
		}

		if (!TextUtils.isEmpty(mucDetail.getOrderTime())) {
			this.mViewTime.setText(mucDetail.getOrderTime());
		}

		if (!TextUtils.isEmpty(mucDetail.getContent())) {
			this.mEtContent.setText(mucDetail.getContent());
		}
	}

	// 请求网络数据相关=========================================
	@Override
	public void bindData(int tag, Object object) {
		if(hasDestroy()){
			return;
		}
		dismissLoadingDialog();

		switch (tag) {
		case EAPIConsts.IMReqType.IM_REQ_MODIFY_CONFERENCE: {
			// 踢人，修改参数，成功后更新界面
			if (object != null) {
				mucDetail = (MUCDetail) object;
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.EMucDetail, mucDetail);
				setResult(RESULT_OK, intent);
				finish();
			}else{
				showToast("修改会议失败");
			}
		}
			break;

		default:
			break;
		}
	}

	/**
	 * 修改会议资料
	 */
	public void modifyConference() {
		
		String strSubject = mEtSubject.getText().toString();
		String strTime = mViewTime.getText().toString();
		String strContent = mEtContent.getText().toString();
		List<JTFile> listFile = uploaderLl.getListJTFile();

		try {
			JSONObject obj = new JSONObject();
			obj.put("id", mucDetail.getId());
			if(mChangeSubject){
				obj.put("subject", strSubject);
			}
			if(mChangeOrderTime){
				obj.put("startTime", strTime);
			}
			if(mChangeContent){
				obj.put("content", strContent);
			}
			JSONArray arrFile = new JSONArray();
			for (int i = 0; i < listFile.size(); i++) {
				arrFile.put(i, listFile.get(i).toJson());
			}
			obj.put("listAddJTFile", arrFile);
			IMReqUtil.modifyConference(App.getApp().getApplicationContext(), this, null, obj);
			showLoadingDialog();
		} 
		catch (Exception e) {
			return;
		}
	}
}