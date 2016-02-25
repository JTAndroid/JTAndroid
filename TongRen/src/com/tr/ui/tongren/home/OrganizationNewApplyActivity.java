package com.tr.ui.tongren.home;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.model.review.Apply;
import com.tr.ui.tongren.model.review.Process;
import com.tr.ui.tongren.model.review.ProcessType;
import com.tr.ui.work.WorkDateTimePickerDialog;
import com.tr.ui.work.WorkDateTimePickerDialog.OnDayChangeListener;
import com.tr.ui.work.WorkNewActivity;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.TimeUtil;

public class OrganizationNewApplyActivity extends JBaseFragmentActivity
		implements IBindData, OnClickListener {

	private String oid;
	private ArrayList<Process> listProcess;
	private RelativeLayout applyName_rl, applyType_rl, startTime_rl, endTime_rl;
	private TextView applyName, applyType, startTimeTv, endTimeTv, content;
	private final int ORG_APPLYNAME = 100;
	private final int ORG_APPLYTYPE = 101;
	private String reviewProcessId;// 流程id
	private String reviewGenreId;// 类型id
	private String applyRereason;// 申请原因
	private String startTime;// 开始时间
	private String endTime;// 结束时间
	
	private String click_btn = "";

	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "新建申请",
				false, null, true, true);
		oid = getIntent().getStringExtra("oid");
		getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPROCESSBYORGID);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tongren_org_new_apply_activity);

		applyName_rl = (RelativeLayout) findViewById(R.id.applyName_rl);
		applyType_rl = (RelativeLayout) findViewById(R.id.applyType_rl);
		applyName = (TextView) findViewById(R.id.applyName);
		applyType = (TextView) findViewById(R.id.applyType);
		
		startTime_rl = (RelativeLayout) findViewById(R.id.startTime_rl);
		endTime_rl = (RelativeLayout) findViewById(R.id.endTime_rl);
		startTimeTv = (TextView) findViewById(R.id.startTime);
		endTimeTv = (TextView) findViewById(R.id.endTime);
		content = (TextView) findViewById(R.id.content);
		

		applyName_rl.setOnClickListener(this);
		applyType_rl.setOnClickListener(this);
		startTime_rl.setOnClickListener(mBeginTimeClick);
		endTime_rl.setOnClickListener(mBeginTimeClick);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tongren_apply, menu);
		menu.findItem(R.id.menu_apply).setTitle("完成");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_apply:
			startTime = startTimeTv.getText().toString();
			endTime = endTimeTv.getText().toString();
			applyRereason = content.getText().toString();
			if(TextUtils.isEmpty(applyName.getText().toString())){
				showToast("请选择申请名称");
				return true;
			}
			if(TextUtils.isEmpty(applyType.getText().toString())){
				showToast("请选择申请类型");
				return true;
			}
			if(TextUtils.isEmpty(startTime)){
				showToast("请选择开始时间");
				return true;
			}
			if(TextUtils.isEmpty(endTime)){
				showToast("请选择结束时间");
				return true;
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				Date vDateBegin = df.parse(startTime);
				Date vDateEnd = df.parse(endTime);
				if (vDateEnd.getTime() <= vDateBegin.getTime()) {
					showToast("申请开始时间不能大于或等于结束时间");
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(TextUtils.isEmpty(applyRereason)){
				showToast("请输入申请内容");
				return true;
			}
			getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_CREATEAPPLICATION);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (object == null) {
			return;
		}
		switch (tag) {
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPROCESSBYORGID:
			listProcess = (ArrayList<Process>) object;
			if(click_btn.equals("applyName_rl")){
				Intent intent = new Intent(this,
						OrganizationApplyNameAndTypeActivity.class);
				intent.putExtra("listProcess", listProcess);
				intent.putExtra("applyName", applyName.getText().toString());
				startActivityForResult(intent, ORG_APPLYNAME);
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_CREATEAPPLICATION:
			Apply apply = (Apply) object;
			Intent intent = new Intent();
			intent.putExtra("apply", apply);
			setResult(0, intent);
			finish();
			break;
		}
		dismissLoadingDialog();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EAPIConsts.handler.show_err:
				showToast("请在web端添加审批流程");
				break;
			}
		}
	};

	private void getDataFromServer(int requestType) {
		showLoadingDialog();
		JSONObject jsonObj = new JSONObject();
		try {
			switch (requestType) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPROCESSBYORGID:// 组织的审批流程列表
				jsonObj.put("oid", oid);
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_CREATEAPPLICATION:// 新建申请
				jsonObj.put("organizationId", oid);
				jsonObj.put("reviewProcessId", reviewProcessId);
				jsonObj.put("reviewGenreId", reviewGenreId);
				jsonObj.put("applyRereason", applyRereason);
				jsonObj.put("startTime", startTime);
				jsonObj.put("endTime", endTime);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		TongRenReqUtils.doRequestOrg(this, this, jsonObj, handler, requestType);
	}

	@Override
	public void onClick(View v) {
		if (listProcess == null) {
			if(v.getId() == R.id.applyName_rl){
				click_btn = "applyName_rl";
			}else{
				click_btn = "";
			}
			getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPROCESSBYORGID);
			return;
		}
		Intent intent = new Intent(this,
				OrganizationApplyNameAndTypeActivity.class);
		intent.putExtra("listProcess", listProcess);
		switch (v.getId()) {
		case R.id.applyName_rl:
			intent.putExtra("applyName", applyName.getText().toString());
			startActivityForResult(intent, ORG_APPLYNAME);
			break;
		case R.id.applyType_rl:
			if (TextUtils.isEmpty(applyName.getText().toString())) {
				showToast("请先选择申请名称");
				return;
			}
			intent.putExtra("applyType", applyType.getText().toString());
			intent.putExtra("reviewProcessId", reviewProcessId);
			startActivityForResult(intent, ORG_APPLYTYPE);
			break;
		}
	}
	
	private OnClickListener mBeginTimeClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			WorkDateTimePickerDialog datePicKDialog = new WorkDateTimePickerDialog(OrganizationNewApplyActivity.this, TimeUtil.getDate(new Date(), "yyyyMMdd HH:mm:ss"));
			datePicKDialog.dateTimePicKDialog(0);
			datePicKDialog.setDayChangeListener(new OnDayChangeListener() {
				
				@Override
				public void onDayChagne(String outDay) {
					try {
						SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
						switch (v.getId()) {
						case R.id.startTime_rl:
							startTimeTv.setText(TimeUtil.getDate(df.parse(outDay), "yyyy-MM-dd HH:mm"));
							break;
						case R.id.endTime_rl:
							endTimeTv.setText(TimeUtil.getDate(df.parse(outDay), "yyyy-MM-dd HH:mm"));
							break;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			});
		}
		
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (intent == null) {
			return;
		}
		switch (requestCode) {
		case ORG_APPLYNAME:
			Process process = (Process) intent.getSerializableExtra("Process");
			if (process != null) {
				applyName.setText(process.getReviewName());
				reviewProcessId = process.getId();
			}
			break;
		case ORG_APPLYTYPE:
			ProcessType pt = (ProcessType) intent.getSerializableExtra("ProcessType");
			if (pt != null) {
				applyType.setText(pt.getName());
				reviewGenreId = pt.getId();
			}
			break;
		}
	}
}
