package com.tr.ui.people.cread;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * @author Wxh07151732 备注(子模块)
 */
public class RemarkActivity extends BaseActivity implements IBindData {
	private EditText remark_Et;
	private TextView finish_remark_Tv;
	private RelativeLayout quit_remark_Rl;
	private TextView remark_Tv;
	private String client_remark;
	private long communityId = -1;// 创建成功返回的社群id；
	private Object String;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activtiy_remark);
		init();
	}

	/**
	 * 修改基本信息
	 * 
	 * @param “key”:社群的属性:头像，名称，公告，社群介绍
	 * @param "value":属性值,
	 */
	private void doModifyCommunityInfo(String key, String value) {
		showLoadingDialogHy();
		CommunityReqUtil.doModifyCommunityInfo(this, this, communityId, key, value, null);
	}

	private void init() {
		communityId = getIntent().getLongExtra(GlobalVariable.COMMUNITY_ID, -1);
		remark_Et = (EditText) findViewById(R.id.remark_Et);
		finish_remark_Tv = (TextView) findViewById(R.id.finish_remark_Tv);
		quit_remark_Rl = (RelativeLayout) findViewById(R.id.quit_remark_Rl);
		remark_Tv = (TextView) findViewById(R.id.remark_Tv);
		finish_remark_Tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!((java.lang.String) String).valueOf(communityId).contains("-1")) {
					if (client_remark.contains("社群名称")) {
						if (!TextUtils.isEmpty(remark_Et.getText().toString()))
							doModifyCommunityInfo("title", remark_Et.getText().toString());
						else
							Toast.makeText(RemarkActivity.this, "社群名称不能为空", Toast.LENGTH_SHORT).show();
					} else if (client_remark.contains("社群介绍")) {
						doModifyCommunityInfo("subject", remark_Et.getText().toString());
					} else if (client_remark.contains("社群公告")) {
						doModifyCommunityInfo("content", remark_Et.getText().toString());
					}
				} else {
					doFinish();

				}
			}

		});
		quit_remark_Rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		String remark = this.getIntent().getStringExtra("Remark_Activity");
		if (remark != null) {
			remark_Et.setText(remark);
		}
		client_remark = this.getIntent().getStringExtra("remark");
		if (!TextUtils.isEmpty(client_remark)) {
			remark_Tv.setText(client_remark);
		}
		if (client_remark.contains("社群名称"))
			remark_Et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });// 社群名称
	}

	@Override
	public void bindData(int tag, Object object) {
		HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
		switch (tag) {
		case EAPIConsts.CommunityReqType.TYPE_MODIFY_COMMUNITYINFO:
			if (null != dataMap){
				String notifCode =  (String) dataMap.get("notifCode");
				if(notifCode.contains("1"))
					doFinish();
			}else{
				Toast.makeText(RemarkActivity.this, "修改信息失败", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
		dismissLoadingDialogHy();
	}

	private void doFinish() {
		Intent intent = new Intent();
		intent.putExtra("Remark_Activity", remark_Et.getText().toString());
		setResult(RESULT_OK, intent);
		finish();
	}
}
