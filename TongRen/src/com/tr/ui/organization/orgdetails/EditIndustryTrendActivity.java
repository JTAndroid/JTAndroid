package com.tr.ui.organization.orgdetails;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.tr.R;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.utils.Utils;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.WordWrapView;
/**
 * 行业动态 编辑页面
 * @author User
 *
 */
public class EditIndustryTrendActivity extends BaseActivity {
	
	private RelativeLayout rl_industry_label;
	private LinearLayout ll_industry_trend;
	private MyEditTextView eidt_industry_Etv;
	private RelativeLayout quit_industry_trends_Rl;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_edit_industry_trend);
    	rl_industry_label = (RelativeLayout) findViewById(R.id.rl_industry_label);
    	ll_industry_trend = (LinearLayout) findViewById(R.id.ll_industry_trend);
    	eidt_industry_Etv = (MyEditTextView) findViewById(R.id.eidt_industry_Etv);
    	quit_industry_trends_Rl = (RelativeLayout) findViewById(R.id.quit_industry_trends_Rl);
    	quit_industry_trends_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    	initData();
    }
     public void save(View v){
    	 finish();
     }
	private void initData() {
		

		
		final WordWrapView wordWrapView = new WordWrapView(this);
		 ImageView imageView = new ImageView(this);
		 imageView.setBackgroundResource(R.drawable.org_wholeaddlabel);
		 LayoutParams layoutParams =new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 layoutParams.leftMargin=(int)Utils.convertDpToPixel(20);
		 layoutParams.bottomMargin=(int)Utils.convertDpToPixel(10);
		 layoutParams.topMargin =(int) Utils.convertDpToPixel(10);
		 imageView.setOnClickListener(new OnClickListener() {
			
			private AlertDialog create;
			private TextView alertdialog_Tv;
			private TextView alertdialog_Yes;
			private TextView alertdialog_No;
			private EditText alertdialog_Et;

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(EditIndustryTrendActivity.this);
				View view2 = View.inflate(EditIndustryTrendActivity.this, R.layout.people_alertdialog_module, null);
				builder.setView(view2);
				create = builder.create();
				alertdialog_Tv = (TextView) view2.findViewById(R.id.alertdialog_module_Tv);
				alertdialog_Yes = (TextView) view2.findViewById(R.id.alertdialog_module_Yes);
				alertdialog_No = (TextView) view2.findViewById(R.id.alertdialog_module_No);
				alertdialog_Et = (EditText) view2.findViewById(R.id.alertdialog_module_Et);
				alertdialog_Tv.setText("添加新的标签");	
				alertdialog_Yes.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String text = alertdialog_Et.getText().toString().trim();
						TextView view = new TextView(EditIndustryTrendActivity.this);
						view.setText(text);
						wordWrapView.addView(view);
						create.dismiss();
					}
				});
				alertdialog_No.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						create.dismiss();
					}
				});
				create.show();
			}
		});
		 ll_industry_trend.addView(wordWrapView,ll_industry_trend.indexOfChild(rl_industry_label)+1);
		 ll_industry_trend.addView(imageView,ll_industry_trend.indexOfChild(wordWrapView)+1,layoutParams);
	}
}
