package com.tr.ui.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
/**
 * 选择颜色
 * @author Administrator
 *
 */
public class WorkNewColorActivity extends JBaseActivity {
	//颜色对应的选择对勾的图片
	private ImageView ImageView1;
	private ImageView ImageView2;
	private ImageView ImageView3;

	private int mColor;//颜色 0 无色 1:红，2:黄,3:绿,4:蓝",
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_new_color_activity);
		
		mColor=getIntent().getIntExtra("Color", 0);
		initView();
		initData();

	}

	/**初始化控件*/
	public void initView() {
		ImageView1 = (ImageView) findViewById(R.id.ImageView1);
		ImageView2 = (ImageView) findViewById(R.id.ImageView2);
		ImageView3 = (ImageView) findViewById(R.id.ImageView3);
		
	}
	/**初始化数据*/
	public void initData() {
		resetViewData();
	}
	
	public void resetViewData()
	{
		ImageView1.setVisibility(View.GONE);
		ImageView2.setVisibility(View.GONE);
		ImageView3.setVisibility(View.GONE);
		
		if (mColor==0)
			ImageView1.setVisibility(View.VISIBLE);
		if (mColor==1 || mColor==2)
			ImageView3.setVisibility(View.VISIBLE);
		if (mColor==3 || mColor==4)
			ImageView2.setVisibility(View.VISIBLE);
		
	}

	@Override
	public void initJabActionBar() {//actionBar的设置
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "重要程度", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}
	
	
	public void onLinearTypeClick(View v) {
		String vstr=v.getTag().toString();
		int vIndex=Integer.parseInt(vstr);
		Intent intent=getIntent();  
        intent.putExtra("Color", vIndex); 
		setResult(1000,intent);
		finish();
	}
}
