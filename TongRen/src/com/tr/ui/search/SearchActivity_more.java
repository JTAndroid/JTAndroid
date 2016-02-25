package com.tr.ui.search;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;

public class SearchActivity_more extends JBaseFragmentActivity{

	private EditText mInputText;
	private TextView searchButton;
	private ImageView home_search_iv;
	private FrgSearch frgSearch;
	
	@Override
	public void initJabActionBar() {
		// 将下拉列表添加到actionbar中
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowTitleEnabled(false);

		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.search_actionbar_edit);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.search_actionbar_edit, null);
		searchButton = (TextView) v.findViewById(R.id.home_search_tv);
		mInputText = (EditText) v.findViewById(R.id.home_search_edit);
		mInputText.setText(getIntent().getStringExtra("keyword"));
		home_search_iv = (ImageView) v.findViewById(R.id.home_search_iv);
		home_search_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mInputText.setText("");
				home_search_iv.setVisibility(View.GONE);
			}
		});
		mInputText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				home_search_iv.setVisibility(View.VISIBLE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mCurKeyword = mInputText.getText().toString();
				frgSearch.mCurKeyword = mCurKeyword;
				frgSearch.startGetData(0,true);
			}
		});
		actionbar.setCustomView(v);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_more);
		
		frgSearch = new FrgSearch();
		frgSearch.setType(getIntent().getIntExtra("type", 0));
		frgSearch.mCurKeyword = getIntent().getStringExtra("keyword");
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.fragment_conainer, frgSearch, getIntent().getIntExtra("type", 0)+"");
		transaction.commitAllowingStateLoss();
	}
}
