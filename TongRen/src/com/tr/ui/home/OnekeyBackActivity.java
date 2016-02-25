package com.tr.ui.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.home.MSuggestion;
import com.tr.model.home.MSuggestionType;
import com.tr.model.knowledge.Knowledge2;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.knowledge.business.KonwledgeSquareReadedKnowledgeSharedPreferencesBusiness;
import com.tr.ui.widgets.ProductSuggestionMenuPopupWindow;
import com.tr.ui.widgets.ProductSuggestionMenuPopupWindow.OnMyHomeMenuItemClickListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class OnekeyBackActivity extends JBaseActivity implements OnClickListener , IBindData{
	
	private String suggestion_style_string = "无";
	private String suggestion_content_string;
	private String style_no = "无";
	private String style_product = "产品建议";
	
	private String must_in = "至少10个字符...";
	private String no_must_in = "说点什么吧...";
	

	private EditText suggestion_contact;//反馈内容
	private Button commit_bt;//提交按钮
	private RelativeLayout suggestion_style;//反馈类型
	/* 显示菜单 */
	private View suggestion_headerVi;
	private ProductSuggestionMenuPopupWindow productSuggestionMenuPopupWindow;//建议类型菜单
	private TextView suggestion_style_content;
	private EditText suggestion_content;//建议内容
	private MSuggestion suggestion; //建议对象
	private ArrayList<String> suggestionTypeValueList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onekeyback);
		initView();
		initParameter();
	}
	
	
	
	private void initParameter() {
		suggestion = new MSuggestion();
		suggestion.setDicId("0");
		suggestion.setSource("1");
	}



	private void initView() {
		//联系方式
		suggestion_contact = (EditText) findViewById(R.id.suggestion_contact);
		//建议内容
		suggestion_content = (EditText) findViewById(R.id.suggestion_content);
		
		suggestion_style = (RelativeLayout) findViewById(R.id.suggestion_style);
		suggestion_style_content = (TextView) findViewById(R.id.suggestion_style_content);
		suggestion_headerVi = findViewById(R.id.suggestion_headerVi);
		suggestion_style.setOnClickListener(this);
		
		commit_bt = (Button) findViewById(R.id.commit_bt);
		commit_bt.setOnClickListener(this);
		commit_bt.setClickable(false);
		commit_bt.setBackgroundResource(R.drawable.sign_in_normal);
		suggestion_style_content.setText(style_no);
		suggestion_content.setHint(must_in);
		
		
		
		suggestion_content.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (suggestion_style_string.equals(style_no) && (s.length()>=10) || (suggestion_style_string.equals(style_product) && s.length()>=10)) {
					commit_bt.setClickable(true);
					commit_bt.setBackgroundResource(R.drawable.sign_in);
 				}
 				else if (!(suggestion_style_string.equals(style_no)) && !(suggestion_style_string.equals(style_product))) {
 					commit_bt.setClickable(true);
					commit_bt.setBackgroundResource(R.drawable.sign_in);
				}
 				else{
 					commit_bt.setClickable(false);
 					commit_bt.setBackgroundResource(R.drawable.sign_in_normal);
 				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit_bt:
			if (suggestion.getDicName() == null) {
				suggestion.setDicName("无");
			}
			suggestion.setProblemContent(suggestion_content.getText().toString());
			suggestion.setContact(suggestion_contact.getText().toString());
			HomeReqUtil.addSuggestion(OnekeyBackActivity.this, this, null, suggestion);
			showLoadingDialog();
			break;
		case R.id.suggestion_style:
			if (productSuggestionMenuPopupWindow == null) {
				productSuggestionMenuPopupWindow = new ProductSuggestionMenuPopupWindow(OnekeyBackActivity.this, suggestionTypeValueList);
			}

			productSuggestionMenuPopupWindow.setFocusable(true);
			productSuggestionMenuPopupWindow.showAsDropDown(suggestion_headerVi);
			productSuggestionMenuPopupWindow.setOnItemClickListener(new OnMyHomeMenuItemClickListener() {

				@Override
				public void setSuggestionTypeValue() {
					suggestion.setDicName(suggestion_style_content.getText().toString());
					suggestion_style_string = suggestion_style_content.getText().toString();
					if (suggestion_style_string.equals(style_no) || (suggestion_style_string.equals(style_product)))
					{
						suggestion_content.setHint(must_in);
						commit_bt.setClickable(false);
						commit_bt.setBackgroundResource(R.drawable.sign_in_normal);
					}else {
						suggestion_content.setHint(no_must_in);
						commit_bt.setClickable(true);
						commit_bt.setBackgroundResource(R.drawable.sign_in);
					}
				}
				
			});
			break;

		default:
			break;
		}
	}
	
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "意见反馈", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}


	

	@Override
	public void onResume() {
		HomeReqUtil.getSuggestionType(this, this, null);
		super.onResume();
	}



	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (object == null) {
			return;
		}
		List<MSuggestionType> suggestionTypeLists = new ArrayList<MSuggestionType>();
		if (tag == EAPIConsts.HomeReqType.HOME_REQ_GET_SUGGESTION_TYPE) {
			Map<String, Object> dataHm = (Map<String, Object>) object;
			if (dataHm != null) {
				suggestionTypeLists = (List<MSuggestionType>) dataHm.get("list");
			}
			for (MSuggestionType suggestionType : suggestionTypeLists) {
				suggestionTypeValueList.add(suggestionType.getName());
			}
		}
		if (tag == EAPIConsts.HomeReqType.HOME_REQ_ADD_SUGGESTION) {
			if (object!=null) {
				String succeed = (String) object;
				if ("true".equals(succeed)) {
					Toast.makeText(OnekeyBackActivity.this, "反馈建议成功，非常感谢", 1).show();
					finish();
				}else {
					Toast.makeText(OnekeyBackActivity.this, "提交失败，请再次提交", 1).show();
				}
			}
		}
	}
	
	
}
