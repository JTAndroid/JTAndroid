package com.tr.ui.people.contactsdetails;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.PeopleReqUtil;
import com.tr.api.PeopleRespFactory;
import com.tr.navigate.ENavConsts;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.Report;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

/**
 * 人脉举报
 * @author John
 *
 */
public class ReportActivity extends BaseActivity implements IBindData{
	private EditText editSms;
	private Button commit;
	private TextView num_Tv;
	
	 int MAX_LENGTH = 100;                   //最大输入字符数100  
	    int Rest_Length = MAX_LENGTH;
		private Person person;
		private RadioGroup report_Rg;
		private String report_text;  //举报类型
		
		public boolean canCommit  = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_report_activity);
		
		person = (Person) this.getIntent().getSerializableExtra(ENavConsts.Report);
		
		init();
		initData();
	}

	private void initData() {
		commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(report_text)&&canCommit) {//当提交按钮可用的时候，进行提交操作
				
					Report report = new Report();
					if (person!=null) {
						report.personId = person.id;
						report.userId = person.createUserId;
						report.content = report_text;
						report.reason = editSms.getText().toString();
						PeopleReqUtil.doRequestWebAPI(context,ReportActivity.this, report,null,PeopleRequestType.REPORT_SAVE);
					}
				
				}
				
			}
		});
		editSms.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
	                    Rest_Length = MAX_LENGTH - editSms.getText().length();  
				 num_Tv.setText(Rest_Length+"");
					if (Rest_Length<=0) {
						ToastUtil.showToast(context, "不得超过100字");
					}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
	                    Rest_Length = MAX_LENGTH - editSms.getText().length();  
			}
			@Override
			public void afterTextChanged(Editable s) {
	                    Rest_Length = MAX_LENGTH - editSms.getText().length();  
				
			}
		});
	}
	private void init() {
		RelativeLayout quit_report_Rl = (RelativeLayout) findViewById(R.id.quit_report_Rl);
		editSms = (EditText) findViewById(R.id.editSms);
		commit = (Button) findViewById(R.id.commit);
		num_Tv = (TextView) findViewById(R.id.num_Tv);
		report_Rg = (RadioGroup) findViewById(R.id.report_Rg);
//		report_Rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				selectRadioBtn(checkedId);
//			}
//
//			
//		});
		final RadioButton report_seqing = (RadioButton) findViewById(R.id.report_seqing);
		final RadioButton report_saorao = (RadioButton) findViewById(R.id.report_saorao);
		final RadioButton report_guanggao = (RadioButton) findViewById(R.id.report_guanggao);
		final RadioButton report_fandong = (RadioButton) findViewById(R.id.report_fandong);
		final RadioButton report_weifa = (RadioButton) findViewById(R.id.report_weifa);
		
		
		report_seqing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				report_seqing.setTextColor(Color.WHITE);
				report_saorao.setTextColor(Color.BLACK);
				report_guanggao.setTextColor(Color.BLACK);
				report_fandong.setTextColor(Color.BLACK);
				report_weifa.setTextColor(Color.BLACK);
				report_text =report_seqing .getText().toString();
				report_saorao.setChecked(false);
				report_guanggao.setChecked(false);
				report_fandong.setChecked(false);
				report_weifa.setChecked(false);
				
				canCommit = true;
				commit.setBackgroundResource(R.drawable.sign_in);
			}
		});
		
		report_saorao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				report_seqing.setTextColor(Color.BLACK);
				report_saorao.setTextColor(Color.WHITE);
				report_guanggao.setTextColor(Color.BLACK);
				report_fandong.setTextColor(Color.BLACK);
				report_weifa.setTextColor(Color.BLACK);
				report_text =report_saorao .getText().toString();
				report_seqing.setChecked(false);
				report_guanggao.setChecked(false);
				report_fandong.setChecked(false);
				report_weifa.setChecked(false);
				canCommit = true;
				commit.setBackgroundResource(R.drawable.sign_in);
			}
		});
		
		report_guanggao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				report_seqing.setTextColor(Color.BLACK);
				report_saorao.setTextColor(Color.BLACK);
				report_guanggao.setTextColor(Color.WHITE);
				report_fandong.setTextColor(Color.BLACK);
				report_weifa.setTextColor(Color.BLACK);
				report_text =report_guanggao .getText().toString();
				report_seqing.setChecked(false);
				report_saorao.setChecked(false);
				report_fandong.setChecked(false);
				report_weifa.setChecked(false);
				canCommit = true;
				commit.setBackgroundResource(R.drawable.sign_in);
			}
		});
		report_fandong.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				report_fandong.setTextColor(Color.WHITE);
				report_seqing.setTextColor(Color.BLACK);
				report_saorao.setTextColor(Color.BLACK);
				report_guanggao.setTextColor(Color.BLACK);
				report_weifa.setTextColor(Color.BLACK);
				report_text =report_fandong .getText().toString();
				report_seqing.setChecked(false);
				report_saorao.setChecked(false);
				report_guanggao.setChecked(false);
				report_weifa.setChecked(false);
				canCommit = true;
				commit.setBackgroundResource(R.drawable.sign_in);
			}
		});
		report_weifa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				report_weifa.setTextColor(Color.WHITE);
				report_fandong.setTextColor(Color.BLACK);
				report_seqing.setTextColor(Color.BLACK);
				report_saorao.setTextColor(Color.BLACK);
				report_guanggao.setTextColor(Color.BLACK);
				//report_guanggao.setTextColor(Color.BLACK);
				report_text =report_weifa .getText().toString();
				report_seqing.setChecked(false);
				report_saorao.setChecked(false);
				report_guanggao.setChecked(false);
				report_fandong.setChecked(false);
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
				Toast.makeText(context, "举报成功", 1).show();
				finish();
			}else{
				Toast.makeText(context, "举报失败", 1).show();
			}
		}
	}
}
