package com.tr.ui.organization.orgdetails;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.navigate.ENavConsts;
import com.tr.ui.organization.model.OrganizationReport;
import com.tr.ui.people.cread.BaseActivity;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

public class OrganizationReportActivity extends BaseActivity implements IBindData{
	private EditText editSms;
	private Button commit;
	private TextView num_Tv;
	
	 int MAX_LENGTH = 100;                   //最大输入字符数100  
	    int Rest_Length = MAX_LENGTH;
		private RadioGroup report_Rg;
		private String report_text;  //举报类型
		
		public boolean canCommit  = false;
		private long org_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organization_report_activity);
		
		org_id = this.getIntent().getLongExtra(ENavConsts.Org_Report, 0);
		
		init();
		initData();
	}

	private void initData() {
		commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(report_text)&&canCommit) {//当提交按钮可用的时候，进行提交操作
				
					OrganizationReport report = new OrganizationReport();
					if (org_id!=0) {
						report.customerId = org_id;
						report.content = report_text;
						report.reason = editSms.getText().toString();
						OrganizationReqUtil.doRequestWebAPI(context,OrganizationReportActivity.this, report,null,OrganizationReqType.CUSTOMER_INFORM_SAVE);
					}
				
				}
				
			}
		});
		editSms.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				 if(Rest_Length > 0){  
	                    Rest_Length = MAX_LENGTH - editSms.getText().length();  
	                }  
				 num_Tv.setText(Rest_Length+"");
					if (Rest_Length<=0) {
						ToastUtil.showToast(context, "不得超过100字!!!");
					}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				 if(Rest_Length > 0){  
	                    Rest_Length = MAX_LENGTH - editSms.getText().length();  
	                }  
			}
			@Override
			public void afterTextChanged(Editable s) {
				 if(Rest_Length > 0){  
	                    Rest_Length = MAX_LENGTH - editSms.getText().length();  
	                }  
				
			}
		});
	}
	private void init() {
		RelativeLayout quit_report_Rl = (RelativeLayout) findViewById(R.id.org_quit_report_Rl);
		editSms = (EditText) findViewById(R.id.org_editSms);
		commit = (Button) findViewById(R.id.org_commit);
		num_Tv = (TextView) findViewById(R.id.org_num_Tv);
		report_Rg = (RadioGroup) findViewById(R.id.org_report_Rg);
//		report_Rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				selectRadioBtn(checkedId);
//			}
//
//			
//		});
		final RadioButton report_seqing = (RadioButton) findViewById(R.id.org_report_seqing);
		final RadioButton report_saorao = (RadioButton) findViewById(R.id.org_report_saorao);
		final RadioButton report_guanggao = (RadioButton) findViewById(R.id.org_report_guanggao);
		final RadioButton report_fandong = (RadioButton) findViewById(R.id.org_report_fandong);
		final RadioButton org_report_eyi = (RadioButton) findViewById(R.id.org_report_eyi);
		final RadioButton org_report_maoming = (RadioButton) findViewById(R.id.org_report_maoming);
		
		
	
		
		org_report_maoming.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				report_text =org_report_maoming .getText().toString();
				report_saorao.setChecked(false);
				report_guanggao.setChecked(false);
				report_fandong.setChecked(false);
				org_report_eyi.setChecked(false);
				report_seqing.setChecked(false);
				canCommit = true;
				commit.setBackgroundResource(R.drawable.sign_in);
			}
		});
		
		report_seqing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				report_text =report_seqing .getText().toString();
				report_saorao.setChecked(false);
				report_guanggao.setChecked(false);
				report_fandong.setChecked(false);
				org_report_eyi.setChecked(false);
				org_report_maoming.setChecked(false);
				canCommit = true;
				commit.setBackgroundResource(R.drawable.sign_in);
			}
		});
		
		report_saorao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				report_text =report_saorao .getText().toString();
				report_seqing.setChecked(false);
				report_guanggao.setChecked(false);
				report_fandong.setChecked(false);
				org_report_eyi.setChecked(false);
				org_report_maoming.setChecked(false);
				canCommit = true;
				commit.setBackgroundResource(R.drawable.sign_in);
			}
		});
		
		report_guanggao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				report_text =report_guanggao .getText().toString();
				report_seqing.setChecked(false);
				report_saorao.setChecked(false);
				report_fandong.setChecked(false);
				org_report_eyi.setChecked(false);
				org_report_maoming.setChecked(false);
				canCommit = true;
				commit.setBackgroundResource(R.drawable.sign_in);
			}
		});
		report_fandong.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				report_text =report_fandong .getText().toString();
				report_seqing.setChecked(false);
				report_saorao.setChecked(false);
				report_guanggao.setChecked(false);
				org_report_eyi.setChecked(false);
				org_report_maoming.setChecked(false);
				canCommit = true;
				commit.setBackgroundResource(R.drawable.sign_in);
			}
		});
		org_report_eyi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				report_text =org_report_eyi .getText().toString();
				report_seqing.setChecked(false);
				report_saorao.setChecked(false);
				report_guanggao.setChecked(false);
				report_fandong.setChecked(false);
				org_report_maoming.setChecked(false);
				canCommit = true;
				commit.setBackgroundResource(R.drawable.sign_in);
			}
		});
		
		quit_report_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private void selectRadioBtn(int checkedId) {
		report_Rg.clearCheck();
		RadioButton button = (RadioButton) findViewById(report_Rg.getCheckedRadioButtonId());		
		report_text = button.getText().toString();
		canCommit = true;
		commit.setBackgroundResource(R.drawable.sign_in);
	}
	@Override
	public void bindData(int tag, Object object) {
		if (object!=null) {
			Boolean success = (Boolean) object;
			if (success) {
				ToastUtil.showToast(context, "举报成功");
				finish();
			}else{
				ToastUtil.showToast(context, "举报失败");
			}
		}
	}
}
