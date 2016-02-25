package com.tr.ui.conference.initiatorhy;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.string.StringUtils;

public class EditTextContentActivity extends JBaseActivity {

	private ActionBar actionBar;
	private EditText infoEt;
	private String editTextInfo;

	@Override
	public void initJabActionBar() {
		actionBar = getActionBar();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edittext_content_layout);
		infoEt = (EditText) findViewById(R.id.EditTextInfo);
		Intent intent = getIntent();
		editTextInfo = intent.getStringExtra("editTextInfo");
		HomeCommonUtils.initLeftCustomActionBar(EditTextContentActivity.this, actionBar, "议题名称", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
		if (!StringUtils.isEmpty(editTextInfo)) {
			infoEt.setText(editTextInfo);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(0, 101, 0, "完成");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (101 == item.getItemId()) {
			if (infoEt != null && (!infoEt.getText().toString().equals(""))) {
				Intent intent = getIntent();
				intent.putExtra("editTextInfo", infoEt.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
			}
		}
		return super.onOptionsItemSelected(item);
	}

}
