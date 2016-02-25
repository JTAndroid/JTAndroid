package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.ReportData;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.demand.util.TextStrUtil;
import com.utils.common.EUtil;
import com.utils.http.IBindData;

/**
 * 用户举报
 * 
 * @author Administrator
 *
 */

public class ReportMessageActivity extends JBaseActivity implements
		OnClickListener,  IBindData {

	private EditText reasonEt;
	private EditText phoneEt;
	private TextView charCountTv;
	private Button submitBtn;
	private Boolean isOther = false;
	private MyAdapter labelAdapter;
	
	private List<ReportData> reportListData = new ArrayList<ReportData>();//所有的 id
	private List<String> reportListId = new ArrayList<String>();//已选中的id

	private String demandid;
	private ReportData reportData5; //其他對象
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_demand_report_message);
		Intent intent = getIntent();
		demandid = intent.getStringExtra(ENavConsts.DEMAND_DETAILS_ID);
		GridView InfoGv = (GridView) findViewById(R.id.InfoGv);
		labelAdapter = new MyAdapter();
		InfoGv.setAdapter(labelAdapter);
		InfoGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ReportData data = reportListData.get(arg2);
				if(arg2==labelAdapter.getCount()-1){
					//點擊了其他
					reportListId.clear();
					reportListId.add(data.id);
				}else{
					//點擊
					 if(reportListId.contains(data.id)){
						 if(reportListId.size()!=1){
							reportListId.remove(data.id);
						 }
					}else{
						if(reportListId.contains(reportData5.id)){
							reportListId.remove(reportData5.id);
						}
						reportListId.add(data.id);
					}
				}
				labelAdapter.notifyDataSetChanged();
			}
		});
		reasonEt = (EditText) findViewById(R.id.ReasonEt);
		phoneEt = (EditText) findViewById(R.id.phoneEt);
		charCountTv = (TextView) findViewById(R.id.charCountTv);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(this);
		submitBtn.setEnabled(false);
		
		ReportData reportData = new ReportData();
		reportData.id="联系电话虚假";
		reportData.value="联系电话虚假";
		
		ReportData reportData2 = new ReportData();
		reportData2.id="虚假，违法信息";
		reportData2.value="虚假，违法信息";
		
		ReportData reportData3 = new ReportData();
		reportData3.id="价格虚假";
		reportData3.value="价格虚假";
		
		ReportData reportData4 = new ReportData();
		reportData4.id="要求汇款";
		reportData4.value="要求汇款";
		
		
		reportData5= new ReportData();
		reportData5.id="其他";
		reportData5.value="其他";
		
		reportListData.add(reportData);
		reportListData.add(reportData2);
		reportListData.add(reportData3);
		reportListData.add(reportData4);
		reportListData.add(reportData5);
		
		phoneEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String keyWordStr = phoneEt.getText().toString();
				if (TextUtils.isEmpty(keyWordStr)) {//输入和未输入
					submitBtn.setEnabled(false);
					submitBtn.setBackgroundResource(R.drawable.demand_report_submit_bn_bg_default);
				} else {
					submitBtn.setEnabled(true);
					submitBtn.setBackgroundResource(R.drawable.demand_report_submit_bn_bg);
				}
			}
		});
		reasonEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s != null) {// 显示剩余字符数
					charCountTv.setText(String.valueOf(100 - s.length()));
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return reportListData==null?0:reportListData.size();
		}

		@Override
		public Object getItem(int position) {
			
			return reportListData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(ReportMessageActivity.this,
						R.layout.demand_report_item_textview, null);
			TextView cb = (TextView) convertView;
			ReportData data = reportListData.get(position);
			cb.setText(data.value);
			if(reportListId.contains(data.id)){
				convertView.setBackgroundResource(R.drawable.demand_report_clicked);
			}else{
				convertView.setBackgroundResource(R.drawable.demand_report_default);
				}
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitBtn:// 提交
			String content = reasonEt.getText().toString();
			if (reportListId == null || reportListId.size() <= 0) {
				showToast("请选择投诉理由");
				return;
			}
			if(reportListId.size()==1 && reportListId.get(0).equals(reportData5.id) && TextUtils.isEmpty(content)){
				showToast("请输入投诉内容");
				return;
			}
			String phone = phoneEt.getText().toString();
			submitReportMessage(content, phone, TextStrUtil.getStringAppend(reportListId, ","));
			break;
		}

	}

	/**
	 * 提交投诉信息
	 * 
	 * @param string
	 */
	private void submitReportMessage(String string, String phone, String reason) {
		DemandReqUtil.getReportMessageList(ReportMessageActivity.this,
				ReportMessageActivity.this, string, phone, reason, demandid,
				mHandler);

	}


	@Override
	public void initJabActionBar() {
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		TextView myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("用户举报");
		mCustomView.findViewById(R.id.titleIv).setVisibility(View.GONE);

	}

	@Override
	public void bindData(int tag, Object object) {
		if(object instanceof Boolean){
			showToast("举报成功");
			finish();
		}
		if(object instanceof String){
			showToast(object.toString());
		}

	}

}
