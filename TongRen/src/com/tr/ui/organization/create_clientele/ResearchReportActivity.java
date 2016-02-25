package com.tr.ui.organization.create_clientele;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tr.R;
import com.tr.ui.organization.orgdetails.EditResearchReportActivity;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
/**
 * 研究报告
 * @author Wxh07151732
 *
 */
public class ResearchReportActivity extends BaseActivity {
	private ImageView edit_reseaech_Tv;
	private MyEditTextView eidt_research_home_Etv;
	private MyEditTextView eidt_research_name_Etv;
	private MyEditTextView eidt_research_Etv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_research_report);
		edit_reseaech_Tv = (ImageView) findViewById(R.id.edit_reseaech_Tv);
		eidt_research_home_Etv = (MyEditTextView) findViewById(R.id.eidt_research_home_Etv);
		eidt_research_name_Etv = (MyEditTextView) findViewById(R.id.eidt_research_name_Etv);
		eidt_research_Etv = (MyEditTextView) findViewById(R.id.eidt_research_Etv);
	}
	
	public void edit(View v){
		 Intent intent = new Intent(this, CustomActivity.class);
		 intent.putExtra("Custom", "研究报告");
		 startActivity(intent);
	}
}
