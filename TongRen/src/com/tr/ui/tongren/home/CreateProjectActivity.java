package com.tr.ui.tongren.home;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.tr.App;
import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.api.TongRenReqUtils;
import com.tr.model.demand.Metadata;
import com.tr.model.obj.DynamicNews;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.flow.CreateFlowActivtiy;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.organization.model.Area;
import com.tr.ui.people.cread.EducationDataActivity;
import com.tr.ui.people.cread.RemarkActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.tongren.model.project.Project;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.TongRenRequestUrl;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

public class CreateProjectActivity extends JBaseActivity implements OnClickListener, IBindData{

	private MyEditTextView projectNameEtv;
	private MyEditTextView projectDescribeEtv;
	private MyEditTextView projectAveragesEtv;
	private MyEditTextView projectTimeEtv;
	private MyEditTextView projectPeriodEtv;
	private MyEditTextView projectAreaEtv;
	private MyEditTextView projectIndustryEtv;
//	private MyEditTextView projectDocumentEtv;
	private ArrayList<Metadata> metadataIndustry = new ArrayList<Metadata>();
	private int dataIndex;
	private ArrayList<String> industryIds = new ArrayList<String>();
	private ArrayList<String> industry = new ArrayList<String>();
	private ArrayList<Metadata> metadataArea = new ArrayList<Metadata>();
	private Area area_result;
	private String remark;
	Project project = new Project();
	private String projectTime; 
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "创建项目", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createproject);
		initView();
		initData();
	}
	private void initData() {
		
	}
	private void initView() {
		projectNameEtv = (MyEditTextView) findViewById(R.id.projectNameEtv);
		projectDescribeEtv = (MyEditTextView) findViewById(R.id.projectDescribeEtv);
		projectAveragesEtv = (MyEditTextView) findViewById(R.id.projectAveragesEtv);
		projectTimeEtv = (MyEditTextView) findViewById(R.id.projectTimeEtv);
		projectPeriodEtv = (MyEditTextView) findViewById(R.id.projectPeriodEtv);
		projectAreaEtv = (MyEditTextView) findViewById(R.id.projectAreaEtv);
		projectIndustryEtv = (MyEditTextView) findViewById(R.id.projectIndustryEtv);
//		projectDocumentEtv = (MyEditTextView) findViewById(R.id.projectDocumentEtv);
		projectPeriodEtv.setNumEdttext_inputtype();
		projectAveragesEtv.setNumEdttext_inputtype();
		projectDescribeEtv.setOnClickListener(this);
		projectTimeEtv.setOnClickListener(this);
		projectAreaEtv.setOnClickListener(this);
		projectIndustryEtv.setOnClickListener(this);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_createflow, menu);
		menu.findItem(R.id.flow_create).setTitle("完成");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.flow_create:
			project.setName(projectNameEtv.getText());
			project.setIntroduction(projectDescribeEtv.getText());

			if (!TextUtils.isEmpty(projectPeriodEtv.getText())) {
				project.setCycle(Integer.parseInt(projectPeriodEtv.getText()));
			}
			if (!TextUtils.isEmpty(App.getUserID())) {
				project.setCreaterId(Long.parseLong(App.getUserID()));
			}
			if (!TextUtils.isEmpty(projectAveragesEtv.getText())) {
				project.setRemuneration(Long.parseLong(projectAveragesEtv.getText()));
			}
			
			project.setArea(projectAreaEtv.getText());
			project.setIndustry(projectIndustryEtv.getText());
			DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
//			if (!TextUtils.isEmpty(projectTime)) {
//				String[] split = projectTime.split("-");
//				Date startDate = new Date();
//				Date endDate = new Date();
//				try {
//					startDate = sdf.parse(split[0]);
//					endDate =  sdf.parse(split[1]);
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				
//				Timestamp validityStartTime = Timestamp.valueOf(sdf.format(startDate));
//				Timestamp validityEndTime = Timestamp.valueOf(sdf.format(endDate));
//				project.setValidityStartTime(validityStartTime);
//				project.setValidityEndTime(validityEndTime);
//			}
//			Timestamp ts = new Timestamp(System.currentTimeMillis());
//			project.setCreateTime(ts);
//			
		
		
			
			project.setStatus(1);
			TongRenReqUtils.doRequestWebAPI(this, this, project, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_CREATE_PROJECT);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.projectTimeEtv:
			Intent Timeintent = new Intent(this, EducationDataActivity.class);
			startActivityForResult(Timeintent, 1001);
			break;
		case R.id.projectIndustryEtv:
			dataIndex = 2;
			ENavigate.startChooseActivityForResult(this, true, "行业",
					ChooseDataUtil.CHOOSE_type_Trade, null);
			break;
		case R.id.projectAreaEtv:
			dataIndex = 3;
			ENavigate.startChooseActivityForResult(this, false, "区域",
					ChooseDataUtil.CHOOSE_type_Area, null);
			break;
		case R.id.projectDescribeEtv:
			Intent intent = new Intent(this, RemarkActivity.class);
			if (!TextUtils.isEmpty(remark)) {
				intent.putExtra("Remark_Activity", remark);
			}
			intent.putExtra("remark", "项目描述");
			startActivityForResult(intent, 1006);	
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data!=null) {
			switch (requestCode) {
			case ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT:
				// 多级选择回调界面
				setChooseText((ArrayList<Metadata>) data
						.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA));
				break;
			case 1006://项目介绍
				remark = data.getStringExtra("Remark_Activity");
				projectDescribeEtv.setText(remark);
				break;
			case 1001: //需求时间
				projectTime = data.getStringExtra("Education_Data");
				ToastUtil.showToast(this, projectTime);
				projectTimeEtv.setText(projectTime);
			break;
			default:
				break;
			}
		}
	}
	public void setChooseText(ArrayList<Metadata> data) {
		switch (dataIndex) {
		case 1:
			// // 类型
			// if (metadataType != null) {
			// metadataType.clear();
			// }
			// metadataType = data;
			// demandType
			// .setText(ChooseDataUtil.getMetadataName(metadataType, 9));
			break;
		case 2:
			// 行业
			if (metadataIndustry != null) {
				metadataIndustry.clear();
				industry.clear();
				industryIds.clear();
			}
			metadataIndustry  = data;
			projectIndustryEtv.setText(ChooseDataUtil.getMetadataName(
					metadataIndustry, 9));
//			industry.add(org_industry_Etv.getText().toString());// 客户行业

			if (metadataIndustry != null) {
				if (!metadataIndustry.isEmpty()) {
					for (Metadata Industrydata : metadataIndustry) {

						// 有二级
						for (Metadata data2 : Industrydata.childs) {
							// 有三级
							for (Metadata data3 : data2.childs) {
								industryIds.add(data3.id);
								industry.add(data3.name);
							}
							industryIds.add(data2.id);
							industry.add(data2.name);
						}
						industryIds.add(Industrydata.id);
						industry.add(Industrydata.name);
					}
				}

			}
			break;
		case 3:
			// 地区
			if (metadataArea != null) {
				metadataArea.clear();
			}
			metadataArea  = data;

			area_result = ChooseDataUtil.getMetadataName(metadataArea);
			projectAreaEtv.setText(getAreaStr(area_result));
			break;
		}

	}

	public String getAreaStr(Area area_result) {
		String area = "";
		if (area_result != null) {
			String province = TextUtils.isEmpty(area_result.province) ? ""
					: area_result.province;
			String city = TextUtils.isEmpty(area_result.city) ? ""
					: area_result.city;
			String county = TextUtils.isEmpty(area_result.county) ? ""
					: area_result.county;
			if (city.equalsIgnoreCase(province)) {
				area = province + county;
			} else {
				area = province + city + county;
			}

			// KeelLog.e("==>>province" ,province);
			// KeelLog.e("==>>city" ,city);
			// KeelLog.e("==>>county" ,county);
			// KeelLog.e("==>>area" ,area);
		}
		return area;
	}
	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_CREATE_PROJECT:
			if (object!=null) {
				ToastUtil.showToast(this, "创建完成");
				finish();
			}
			break;

		default:
			break;
		}
	}
}
