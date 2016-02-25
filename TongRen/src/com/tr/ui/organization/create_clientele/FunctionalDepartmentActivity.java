package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.organization.model.government.DepartMents;
import com.tr.ui.organization.model.government.DepartMentsInfo;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.tr.ui.people.cread.view.MyitemView;
/**
 * 主要技能部门
 * @author Wxh07151732
 *
 */
public class FunctionalDepartmentActivity extends BaseActivity implements OnClickListener {
	private static ArrayList<String> Department ;
	private RelativeLayout quit_function_Rl;
	private MyitemView org_function_finance_Mv;
	private MyitemView org_function_NDRC_Mv;
	private MyitemView org_function_SASAC_Mv;
	private MyitemView org_function_FB_Mv;
	private MyitemView org_function_PB_Mv;
	private MyitemView org_function_CONUS_Mv;
	private MyitemView org_function_BB_Mv;
	private MyitemView org_function_BI_Mv;
	private MyitemView org_function_FIG_Mv;
	private MyitemView org_function_JB_Mv;
	private Intent intent;
	private ArrayList<String> list;
	private String[] Departments={"地址","网址","电话","传真","主要领导"};
	private DepartMentsInfo department2;
	private Bundle bundle;
	private DepartMents ments;
	private DepartMentsInfo department1;
	private DepartMentsInfo department3;
	private DepartMentsInfo department4;
	private DepartMentsInfo department5;
	private DepartMentsInfo department6;
	private DepartMentsInfo department7;
	private DepartMentsInfo department8;
	private DepartMentsInfo department9;
	private DepartMentsInfo department10;
	private static boolean isProvince;
	public static Handler handler = new Handler(){
		

		public void handleMessage(android.os.Message msg) {
			String area = (String) msg.obj;
			if ("1".equals(area)) {
				isProvince = true;
			}
		};
	};
	private ArrayList<String> department_Activity;
	private DepartMents ments1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_functional_department);
		Department = new ArrayList<String>();
		ments = new DepartMents();
		department1 = new DepartMentsInfo();
		department2 = new DepartMentsInfo();
		department3 = new DepartMentsInfo();
		department4 = new DepartMentsInfo();
		department5 = new DepartMentsInfo();
		department6 = new DepartMentsInfo();
		department7 = new DepartMentsInfo();
		department8 = new DepartMentsInfo();
		department9 = new DepartMentsInfo();
		department10 = new DepartMentsInfo();
		list = new ArrayList<String>();
		bundle = new Bundle();
		ments1 = (DepartMents) this.getIntent().getSerializableExtra("Functional_department_Bean"); 
		if (ments1!=null) {
			department1=ments1.cztDepart ;
			department2=ments1.fgwDepart;
			department3=ments1.gzwDepart ;
			department4=ments1.sjrbDepart;
			department5=ments1.ghjDepart ;
			 department6=ments1.gtzytDepart;
			 department7=ments1.swtDepart;
			 department8=ments1.gxwDepart;
			department9=ments1.houseDepart ;
			  department10=ments1.sftDepart;
		}
		init();
		initData();
		if (isProvince) {
			org_function_finance_Mv.setText_label("财政厅");
			org_function_NDRC_Mv.setText_label("发改委");
			org_function_SASAC_Mv.setText_label("国资委");
			org_function_FB_Mv.setText_label("省金融办");
			org_function_PB_Mv.setText_label("规划局");
			org_function_CONUS_Mv.setText_label("国土资源局");
			org_function_BB_Mv.setText_label("商务厅");
			org_function_BI_Mv.setText_label("工信委");
			org_function_FIG_Mv.setText_label("住房和城乡建设厅");
			org_function_JB_Mv.setText_label("司法厅");
		}
	}


	private void init() {
		quit_function_Rl = (RelativeLayout) findViewById(R.id.quit_function_Rl);
		org_function_finance_Mv = (MyitemView) findViewById(R.id.org_function_finance_Mv);
		org_function_NDRC_Mv = (MyitemView) findViewById(R.id.org_function_NDRC_Mv);
		org_function_SASAC_Mv = (MyitemView) findViewById(R.id.org_function_SASAC_Mv);
		org_function_FB_Mv = (MyitemView) findViewById(R.id.org_function_FB_Mv);
		org_function_PB_Mv = (MyitemView) findViewById(R.id.org_function_PB_Mv);
		org_function_CONUS_Mv = (MyitemView) findViewById(R.id.org_function_CONUS_Mv);
		org_function_BB_Mv = (MyitemView) findViewById(R.id.org_function_BB_Mv);
		org_function_BI_Mv = (MyitemView) findViewById(R.id.org_function_BI_Mv);
		org_function_FIG_Mv = (MyitemView) findViewById(R.id.org_function_FIG_Mv);
		org_function_JB_Mv = (MyitemView) findViewById(R.id.org_function_JB_Mv);
	}

	private void initData() {
		for (int i = 0; i < Departments.length; i++) {
			Department.add(Departments[i]);
		}
		quit_function_Rl.setOnClickListener(this);
		org_function_finance_Mv.setOnClickListener(this);
		org_function_NDRC_Mv.setOnClickListener(this);
		org_function_SASAC_Mv.setOnClickListener(this);
		org_function_FB_Mv.setOnClickListener(this);
		org_function_PB_Mv.setOnClickListener(this);
		org_function_CONUS_Mv.setOnClickListener(this);
		org_function_BB_Mv.setOnClickListener(this);
		org_function_BI_Mv.setOnClickListener(this);
		org_function_FIG_Mv.setOnClickListener(this);
		org_function_JB_Mv.setOnClickListener(this);
		
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  Department,org_function_finance_Mv.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  Department,org_function_NDRC_Mv.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  Department,org_function_SASAC_Mv.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  Department,org_function_FB_Mv.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  Department,org_function_PB_Mv.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  Department,org_function_CONUS_Mv.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  Department,org_function_BB_Mv.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  Department,org_function_BI_Mv.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  Department,org_function_FIG_Mv.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  Department,org_function_JB_Mv.getMyitemview_Lv()),displayMetrics);
	}
	public void finish(View v){
		
		ments.cztDepart = department1;
		ments.fgwDepart = department2;
		ments.gzwDepart = department3;
		ments.sjrbDepart = department4;
		ments.ghjDepart = department5;
		ments.gtzytDepart = department6;
		ments.swtDepart = department7;
		ments.gxwDepart = department8;
		ments.houseDepart = department9;
		ments.sftDepart = department10;
		if (ments!=null) {
			intent = new Intent();
			intent.putStringArrayListExtra("Functional_department_Activity", department_Activity);
			Bundle bundle = new Bundle();
			bundle.putSerializable("Functional_department_Bean", ments);
			intent.putExtras(bundle);
			setResult(7030, intent);
		}
		finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_function_Rl:
			finish();
			break;
		case R.id.org_function_finance_Mv:
			intent = new  Intent(this, DepartmentActivity.class);
			intent.putExtra("Department_Activity",org_function_finance_Mv.getText_label());
			bundle.putSerializable("Department_Bean", department1);
			intent.putExtras(bundle);
			startActivityForResult(intent, 9000);
			break;
		case R.id.org_function_NDRC_Mv:
			intent = new  Intent(this, DepartmentActivity.class);
			intent.putExtra("Department_Activity", org_function_NDRC_Mv.getText_label());
			bundle.putSerializable("Department_Bean", department2);
			intent.putExtras(bundle);
			startActivityForResult(intent, 9001);
			break;
		case R.id.org_function_SASAC_Mv:
			intent = new  Intent(this, DepartmentActivity.class);
			intent.putExtra("Department_Activity",org_function_SASAC_Mv.getText_label());
			bundle.putSerializable("Department_Bean", department3);
			intent.putExtras(bundle);
			startActivityForResult(intent, 9002);
			break;
		case R.id.org_function_FB_Mv:
			intent = new  Intent(this, DepartmentActivity.class);
			intent.putExtra("Department_Activity", org_function_FB_Mv.getText_label());
			bundle.putSerializable("Department_Bean", department4);
			intent.putExtras(bundle);
			startActivityForResult(intent, 9003);
			break;
		case R.id.org_function_PB_Mv:
			intent = new  Intent(this, DepartmentActivity.class);
			intent.putExtra("Department_Activity", org_function_PB_Mv.getText_label());
			bundle.putSerializable("Department_Bean", department5);
			intent.putExtras(bundle);
			startActivityForResult(intent, 9004);
			break;
		case R.id.org_function_CONUS_Mv:
			intent = new  Intent(this, DepartmentActivity.class);
			intent.putExtra("Department_Activity",org_function_CONUS_Mv.getText_label());
			bundle.putSerializable("Department_Bean", department6);
			intent.putExtras(bundle);
			startActivityForResult(intent, 9005);
			break;
			
		case R.id.org_function_BB_Mv:
			intent = new  Intent(this, DepartmentActivity.class);
			intent.putExtra("Department_Activity",org_function_BB_Mv.getText_label());
			bundle.putSerializable("Department_Bean", department7);
			intent.putExtras(bundle);
			startActivityForResult(intent, 9006);
			break;
		case R.id.org_function_BI_Mv:
			intent = new  Intent(this, DepartmentActivity.class);
			intent.putExtra("Department_Activity",org_function_BI_Mv.getText_label());
			bundle.putSerializable("Department_Bean", department8);
			intent.putExtras(bundle);
			startActivityForResult(intent, 9007);
			break;
		case R.id.org_function_FIG_Mv:
			intent = new  Intent(this, DepartmentActivity.class);
			intent.putExtra("Department_Activity",org_function_FIG_Mv.getText_label());
			bundle.putSerializable("Department_Bean", department9);
			intent.putExtras(bundle);
			startActivityForResult(intent, 9008);
			break;
		case R.id.org_function_JB_Mv:
			intent = new  Intent(this, DepartmentActivity.class);
			intent.putExtra("Department_Activity",org_function_JB_Mv.getText_label());
			bundle.putSerializable("Department_Bean", department10);
			intent.putExtras(bundle);
			startActivityForResult(intent, 9009);
			break;

		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			department_Activity = arg2.getStringArrayListExtra("Department_Activity");
			switch (arg0) {
			case 9000:
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  department_Activity,org_function_finance_Mv.getMyitemview_Lv()),displayMetrics);
				department1 = (DepartMentsInfo) arg2.getSerializableExtra("Department_Bean");
				break;
			case 9001:
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  department_Activity,org_function_NDRC_Mv.getMyitemview_Lv()),displayMetrics);
				department2 = (DepartMentsInfo) arg2.getSerializableExtra("Department_Bean");
				break;
			case 9002:
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  department_Activity,org_function_SASAC_Mv.getMyitemview_Lv()),displayMetrics);
				department3 = (DepartMentsInfo) arg2.getSerializableExtra("Department_Bean");
				break;
			case 9003:
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  department_Activity,org_function_FB_Mv.getMyitemview_Lv()),displayMetrics);
				department4 = (DepartMentsInfo) arg2.getSerializableExtra("Department_Bean");
				break;
			case 9004:
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  department_Activity,org_function_PB_Mv.getMyitemview_Lv()),displayMetrics);
				department5 = (DepartMentsInfo) arg2.getSerializableExtra("Department_Bean");
				break;
			case 9005:
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  department_Activity,org_function_CONUS_Mv.getMyitemview_Lv()),displayMetrics);
				department6 = (DepartMentsInfo) arg2.getSerializableExtra("Department_Bean");
				break;
			case 9006:
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  department_Activity,org_function_BB_Mv.getMyitemview_Lv()),displayMetrics);
				department7 = (DepartMentsInfo) arg2.getSerializableExtra("Department_Bean");
				break;
			case 9007:
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  department_Activity,org_function_BI_Mv.getMyitemview_Lv()),displayMetrics);
				department8 = (DepartMentsInfo) arg2.getSerializableExtra("Department_Bean");
				break;
			case 9008:
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  department_Activity,org_function_FIG_Mv.getMyitemview_Lv()),displayMetrics);
				department9 = (DepartMentsInfo) arg2.getSerializableExtra("Department_Bean");
				break;
			case 9009:
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  department_Activity,org_function_JB_Mv.getMyitemview_Lv()),displayMetrics);
				department10 = (DepartMentsInfo) arg2.getSerializableExtra("Department_Bean");
				break;
			default:
				break;
			}
		}
	}
}
