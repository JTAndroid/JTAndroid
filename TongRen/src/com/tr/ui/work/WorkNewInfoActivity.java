package com.tr.ui.work;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
/**
 * 创建事务--备注条目
 * 
 * @author Administrator
 */
public class WorkNewInfoActivity extends JBaseActivity {
	private EditText EditTextInfo;//备注输入框
	

	private String mInfoText;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_new_info_activity);
		
		mInfoText=getIntent().getStringExtra("Info");
		initView();
		initData();

	}


	public void initView() {
		EditTextInfo = (EditText) findViewById(R.id.EditTextInfo);
		
	}

	public void initData() {
		EditTextInfo.setText(mInfoText);
	}
	


	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "备注", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(0, 101, 0, "完成");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if( 101 == item.getItemId() )
		{
			if (EditTextInfo!=null && (!EditTextInfo.getText().toString().equals("")))
			{
				Intent intent=getIntent();  
	            intent.putExtra("Info", EditTextInfo.getText().toString()); 
				setResult(1000,intent);
				finish();
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
