package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.organization.model.government.AreaInfo;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.tr.ui.people.cread.view.MyitemView;
/**
 * 地区概况(子模块)展示状态
 * @author Wxh07151732
 *
 */
public class AreaSurveyActivity extends BaseActivity implements OnClickListener {
	private ListView org_area_survey_Lv;
	private MyitemView org_area_survey_Mv;
	private String [] area_surveys={"中文名称","别民","面积","人口","行政区域类别","上级行政区域","地区GDP","资源","机场","火车站","主要企业","主要高校","当代名人","地区简介","自定义"};
	private ArrayList<String> area_survey;
	private Intent intent;
	private ArrayList<String> list;
	private AreaInfo areaInfo;
	private AreaInfo area;
	private ArrayList<String> area_Survey_Input_Activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_area_survey);
		area_survey  = new ArrayList<String>();
		areaInfo = new AreaInfo();
		list =  new ArrayList<String>();
		area = (AreaInfo) this.getIntent().getSerializableExtra("Area_survey_Bean");
		if (area!=null) {
			areaInfo = area;
		}
		init();
		initData();
		
	}
	public void finish(View v){
			intent = new Intent();
			intent.putStringArrayListExtra("Area_survey_Activity", area_Survey_Input_Activity);
			Bundle bundle = new Bundle();
			bundle.putSerializable("Area_Survey_Input_Bean", areaInfo);
			intent.putExtras(bundle);
			setResult(7029, intent);
		finish();
	}
	private void initData() {
		for (int i = 0; i < area_surveys.length; i++) {
			String area = area_surveys[i];
			area_survey.add(area);
		}	
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  area_survey,org_area_survey_Mv.getMyitemview_Lv()),displayMetrics);
	}

	private void init() {
		org_area_survey_Mv = (MyitemView) findViewById(R.id.org_area_survey_Mv);
		RelativeLayout quit_area_survey_Rl = (RelativeLayout) findViewById(R.id.quit_area_survey_Rl);
		quit_area_survey_Rl.setOnClickListener(this);
		org_area_survey_Mv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_area_survey_Rl:
			finish();
			break;
		case R.id.org_area_survey_Mv:
			Intent intent = new Intent(context, AreaSurveyInput_Activity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("Area_Survey_Input_Bean", areaInfo);
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			switch (arg0) {
			case 0:
				area_Survey_Input_Activity = arg2.getStringArrayListExtra("Area_Survey_Input_Activity");
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  area_Survey_Input_Activity,org_area_survey_Mv.getMyitemview_Lv()),displayMetrics);
				areaInfo = (AreaInfo) arg2.getSerializableExtra("Area_Survey_Input_Bean");
				break;

			default:
				break;
			}
		}
	}
	
}
