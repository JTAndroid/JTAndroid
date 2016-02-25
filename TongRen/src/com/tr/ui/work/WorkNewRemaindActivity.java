package com.tr.ui.work;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
 * 事务提醒
 * 
 * @param RemindType
 * 			提醒类型 m:分钟，h：小时，d：天，其他为不提醒"
 * 
 * @author Administrator
 *
 */
public class WorkNewRemaindActivity extends JBaseActivity {
	
	/**选中分钟的对勾显示*/
	private ImageView ImageViewMinute;
	/**选中小时的对勾显示*/
	private ImageView ImageViewHouse;
	/**选中天的对勾显示*/
	private ImageView ImageViewDay;
	/**选中不提醒的对勾显示*/
	private ImageView ImageViewNo;
	/**提前的输入框*/
	private EditText EditTextValue;
	//获取创建、编辑传过来的值
	private int mRemindValue;
	private String mRemindType;//获取创建、编辑传过来的类型
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_new_remind_activity);
		
		mRemindType="o";
		mRemindValue=getIntent().getIntExtra("RemindValue",0);
		mRemindType=getIntent().getStringExtra("RemindType");
		initView();
		initData();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(0, 101, 0, "完成");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public void initView() {
		ImageViewMinute = (ImageView) findViewById(R.id.ImageViewMinute);
		ImageViewHouse = (ImageView) findViewById(R.id.ImageViewHouse);
		ImageViewDay = (ImageView) findViewById(R.id.ImageViewDay);
		ImageViewNo = (ImageView) findViewById(R.id.ImageViewNo);
		EditTextValue = (EditText) findViewById(R.id.EditTextValue);
		EditTextValue.addTextChangedListener(mTextWatcher);
	}
	/**
	 * 初始化数据
	 */
	public void initData() {
		resetViewData();
	}
	
	public void resetViewData()
	{
		if (mRemindType.equals("o"))
		{//不提醒
			mRemindValue=0;
			EditTextValue.setEnabled(false);
		}
		else
		{
			EditTextValue.setEnabled(true);
		}
		
		if (mRemindValue>0)
			EditTextValue.setText(mRemindValue+"");
		else
			EditTextValue.setText("");
		
		ImageViewMinute.setVisibility(View.GONE);
		ImageViewHouse.setVisibility(View.GONE);
		ImageViewDay.setVisibility(View.GONE);
		ImageViewNo.setVisibility(View.GONE);
		if (mRemindType.equals("m"))
			ImageViewMinute.setVisibility(View.VISIBLE);
		if (mRemindType.equals("h"))
			ImageViewHouse.setVisibility(View.VISIBLE);
		if (mRemindType.equals("d"))
			ImageViewDay.setVisibility(View.VISIBLE);
		if (mRemindType.equals("o"))
			ImageViewNo.setVisibility(View.VISIBLE);
		
	}
	
	TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (!(EditTextValue.getText()==null) && !(EditTextValue.getText().toString().equals("")))
			{
				mRemindValue=Integer.parseInt(EditTextValue.getText().toString());
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void initJabActionBar() {//ActionBar的设置
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "提醒", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String MSG = "onOptionsItemSelected()";
		boolean vCanReturn=true;
		int vValue=0;
		if( 101 == item.getItemId() )
		{
			if (!mRemindType.equals("o"))
			{
				if (!(EditTextValue.getText()==null) && !(EditTextValue.getText().toString().equals("")))
				{
					vValue=Integer.parseInt(EditTextValue.getText().toString());
					if (mRemindType.equals("m"))
					{
						if (vValue<0 || vValue>60)
						{
							Toast.makeText(this, "请输入0-60中间的数字", 0).show();
							vCanReturn=false;
						}
					}
					if (mRemindType.equals("h"))
					{
						if (vValue<0 || vValue>24)
						{
							Toast.makeText(this, "请输入0-24中间的数字", 0).show();
							vCanReturn=false;
						}
					}
					if (mRemindType.equals("d"))
					{
						if (vValue<0 || vValue>30)
						{
							Toast.makeText(this, "请输入0-30中间的数字", 0).show();
							vCanReturn=false;
						}
					}
				}
				else
				{
					Toast.makeText(this, "请输入提前时间", 0).show();
					vCanReturn=false;
				}
			}

			if (vCanReturn)
			{
				mRemindValue= vValue;
				Log.d("xmx","type:"+mRemindType+",value:"+mRemindValue);
				Intent intent=getIntent();  
	            intent.putExtra("RemindValue", mRemindValue); 
	            intent.putExtra("RemindType", mRemindType); 
				setResult(1000,intent);
				finish();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void onLinearTypeClick(View v) {
		mRemindType=v.getTag().toString();
		
		resetViewData();
	}

	
}
