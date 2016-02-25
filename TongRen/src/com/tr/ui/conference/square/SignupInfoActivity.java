package com.tr.ui.conference.square;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.baidu.navisdk.util.common.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.model.conference.MMeetingRequiredSignupInfo;
import com.tr.model.conference.MMeetingSignLabelDataQuery;
import com.tr.ui.conference.common.BaseActivity;
import com.utils.string.StringUtils;

public class SignupInfoActivity extends BaseActivity implements OnClickListener {

	private MMeetingRequiredSignupInfo signInfo;

	@ViewInject(R.id.hy_meeting_title_signup_info)
	private FrameLayout meetingSignupTitle;
	private LinearLayout backBtn;
	private TextView signupBtn;
	private TextView hyLayoutTitle;

	@ViewInject(R.id.hy_meeting_signup_info_lv)
	private LinearLayout signInfoLv;

	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_meeting_signup_info);
		ViewUtils.inject(this);
		// hy_meeting_regist_detail_item.xml
		Intent intent = getIntent();
		signInfo = (MMeetingRequiredSignupInfo) intent.getSerializableExtra("MMeetingRequiredSignupInfo");

		backBtn = (LinearLayout) meetingSignupTitle.findViewById(R.id.hy_layoutTitle_backBtn);
		hyLayoutTitle = ((TextView) meetingSignupTitle.findViewById(R.id.hy_layoutTitle_title));
		hyLayoutTitle.setText(R.string.hy_layout_title);
		signupBtn = (TextView) meetingSignupTitle.findViewById(R.id.hy_layoutTitle_rightTextBtn);
		signupBtn.setText("报名");

		if (null != signInfo) {
			List<MMeetingSignLabelDataQuery> dql = signInfo.getListMeetingSignLabelDataQuery();
			if (null != dql) {
				int size = dql.size();
				if (size > 0) {
					for (int i = 0; i < size; i++) {
						MMeetingSignLabelDataQuery dq = dql.get(i);
						if (null != dq) {
							View convertView = View.inflate(SignupInfoActivity.this, R.layout.hy_list_item_meeting_sign_up, null);
							TextView labelName = (TextView) convertView.findViewById(R.id.hy_meeting_label_name_tv);
							EditText labelContent = (EditText) convertView.findViewById(R.id.hy_meeting_label_content_tv);
							labelName.setText(dq.getMslabelName().trim() + "：");
							if(null!=dq.getLabelContent()){
								if(false == dq.getLabelContent().isEmpty()){
									labelContent.setText(dq.getLabelContent());
									labelContent.setEnabled(false);
								}
							}
							
							if("姓名".equals(dq.getMslabelName()) && hyLayoutTitle.getText().equals(getResources().getString(R.string.hy_layout_title))){
								labelContent.setText(App.getNick());
								labelContent.setEnabled(false);
								signInfo.getListMeetingSignLabelDataQuery().get(i).setLabelContent(App.getNick());
							}
							final int position = i;
							labelContent.addTextChangedListener(new TextWatcher() {

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
								}

								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {
								}

								@Override
								public void afterTextChanged(Editable s) {
									signInfo.getListMeetingSignLabelDataQuery().get(position).setLabelContent(s.toString());
								}
							});
							signInfoLv.addView(convertView);

						}
					}
				}
			}

		}
	}

	@Override
	public void initData() {
		setLitener();
	}

	private void setLitener() {
		backBtn.setOnClickListener(this);
		signupBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 回退
		case R.id.hy_layoutTitle_backBtn:
			finish();
			break;
		case R.id.hy_layoutTitle_rightTextBtn:
			// TODO 通过接口返回数据
			for (MMeetingSignLabelDataQuery signData : signInfo.getListMeetingSignLabelDataQuery()) {
				if (StringUtils.isEmpty(signData.getLabelContent())) {
				AlertDialog.Builder dialog=new Builder(this);
				dialog.setMessage("需要填写全部信息哦，请继续完善！");
				
				dialog.setNegativeButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
				});
				dialog.show();
					//Toast.makeText(this, "请完善报名信息", 0).show();
					return;
				}
			}
			Intent resultIntent = new Intent();
			resultIntent.putExtra("signInfoResult", signInfo);
			setResult(0, resultIntent);
			finish();
			break;
		}
	}

}
