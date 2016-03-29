package com.tongmeng.alliance.activity;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongmeng.alliance.util.LoadUtil;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class ActionPriceActivity extends JBaseActivity implements OnClickListener{

	private TextView myTitle;
	private TextView create_Tv;
	private ImageView search;

	EditText edittext;
	TextView checkText, clauseText;
	
	boolean isChecked = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_action_price);

		edittext = (EditText) findViewById(R.id.activity_action_price_edittext);
		checkText = (TextView) findViewById(R.id.activity_action_price_checkbox);
		clauseText = (TextView) findViewById(R.id.activity_action_price_textview);
		checkText.setOnClickListener(this);
		clauseText.setOnClickListener(this);

		setPricePoint(edittext);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_action_price_textview:
			Intent intent1 = new Intent(this, TMProtocolActivity.class);
			startActivity(intent1);
			break;

		default:
			break;
		}
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("费用");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("确定");
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isChecked) {
					Intent intent = new Intent();
					intent.putExtra("price", edittext.getText().toString());
					Log.e("ActionPriceActivity::", "price:"
							+ edittext.getText().toString());
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(ActionPriceActivity.this, "请阅读并同意“桐盟条款”", 0).show();
				}
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}

	public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
 
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
 
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }
 
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
 
            }
 
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                 
            }
 
        });
 
    }
}
