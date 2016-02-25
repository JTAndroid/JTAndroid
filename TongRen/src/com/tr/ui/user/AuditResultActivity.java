package com.tr.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.tr.App;
import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;

/**
 * 审核结果
 * @author leon
 *
 */
public class AuditResultActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	// 常量
	private final String TIP_AUDIT_FAILED_HEADER = "对不起，审核未通过，原因如下：";
	private final String TIP_AUDIT_FAILED_BOTTOM = "请点击下面的按钮重新提交审核资料";
	private final String TIP_AUDIT_SUCCESS_HEADER = "您提交的认证信息正在审核中..."; // 下次登录时显示此信息
	// private final String TIP_AUDIT_SUCCESS_HEADER = "您的机构详细信息提交成功，请等待审核。"; // 提交完成后显示此信息
	private final String TIP_AUDIT_SUCCESS_MIDDLE = "感谢您注册金桐网机构会员\n审核将在3个工作日内完成。";
	private final String TIP_AUDIT_SUCCESS_BOTTOM = "请保持联系人的手机畅通\n我们将会通过短信方式通知审核结果。";
	
	// 控件
	private TextView tip1Tv;
	private TextView tip2Tv;
	private TextView tip3Tv;
	private TextView editTv;
	
	// 变量
	private int mState; // 审核状态。提交成功待审核-0;待审核-1;审核失败-2
	private App mMainApp;
	
	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("审核结果");
		jabGetActionBar().setDisplayShowTitleEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_act_audit_result);
		initVars();
		initControls();
	}
	
	private void initVars(){
		mMainApp = App.getApp();
		Log.d(TAG,mMainApp.toString());
	}
	
	private void initControls(){
		tip1Tv = (TextView) findViewById(R.id.tip1Tv);
		tip2Tv = (TextView) findViewById(R.id.tip2Tv);
		tip3Tv = (TextView) findViewById(R.id.tip3Tv);
		editTv = (TextView) findViewById(R.id.editTv);
		editTv.setOnClickListener(mClickListener);
		initControlsWithData();
	}
	
	private void initControlsWithData(){
		switch(mMainApp.getAppData().getUser().mOrganizationInfo.mState){
		case -1: // -1 未进行第一次认证（不会出现此种情况）
			break;
		case 0: // 第一次认证进行中
			tip1Tv.setText(TIP_AUDIT_SUCCESS_HEADER);
			tip2Tv.setText(TIP_AUDIT_SUCCESS_MIDDLE);
			tip2Tv.setTextColor(getResources().getColor(R.color.commen_text_color_5));
			tip3Tv.setText(TIP_AUDIT_SUCCESS_BOTTOM);
			editTv.setVisibility(View.GONE);
			break;
		case 1: // 第一次认证失败
			tip1Tv.setText(TIP_AUDIT_FAILED_HEADER);
			tip2Tv.setText(mMainApp.getAppData().getUser().mOrganizationInfo.mFailInfo);
			tip2Tv.setTextColor(getResources().getColor(R.color.commen_text_color_6));
			tip3Tv.setText(TIP_AUDIT_FAILED_BOTTOM);
			editTv.setVisibility(View.VISIBLE);
			break;
		case 2: // 第一次认证成功（不会出现此种情况）
			break;
		}
	}
	
	private OnClickListener mClickListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			// 跳转到机构联系人页面
			startActivity(new Intent(AuditResultActivity.this,
					RegisterOrganizationContactActivity.class));
		}
	};
}

