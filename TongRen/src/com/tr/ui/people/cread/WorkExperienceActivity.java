package com.tr.ui.people.cread;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.model.demand.Metadata;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavConsts.PeopleModuleReqCode;
import com.tr.navigate.ENavigate;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;
import com.tr.ui.people.model.Basic;
import com.tr.ui.people.model.WorkExperience;
import com.utils.log.ToastUtil;

/**
 * 工作经历(子模块)
 * @param <V>
 * @since 2015-01-15
 * @author Wxh07151732
 * 
 */
public class WorkExperienceActivity extends BaseActivity implements
		OnClickListener {
	private TextView finish_work_Tv;//完成
	private MyEditTextView data_work_Etv;//工作时间
	private MyEditTextView unit_work_Etv;//单位名称
	private MyEditTextView industry_work_Etv;//单位行业
	private MyEditTextView phone_work_Etv;//证明人电话
	private MyEditTextView certifier_work_Etv;//证明人
	private MyEditTextView section_work_Etv;//部门
	private MyEditTextView duty_work_Etv;//职位
	private MyEditTextView colleague_work_Etv;//同事
	private MyEditTextView custom_work_Etv;//自定义
	private CheckBox work_experience_Cb;//工作经历选择框
	private ArrayList<String> title_content;//原始标题和内容
	private Intent intent;
	private AlertDialog create;//自定义弹窗
	private TextView alertdialog_Tv;//添加新模块
	private TextView alertdialog_Yes;//确定
	private TextView alertdialog_No;//取消
	private EditText alertdialog_Et;//dialog输入内容框
	private List<Basic> customTagList;//自定义集合
	private ArrayList<CustomerPersonalLine> propertyList; // 自定义
	private ArrayList<Metadata> metadataIndustry;// 行业
	private ArrayList<String> industry = new ArrayList<String>();//行业的集合
	private List<String> industryIds = new ArrayList<String>();// 行业id集合；
	private LinearLayout continueadd_Ll;//新增工作经历的布局条目
	private LinearLayout delete_Ll;//删除布局条目
	private LinearLayout work_Ll;//时间、单位名称、行业。。布局
	private RelativeLayout work_experience_Rl;//将本工作经历当做为当前身份
	private RelativeLayout quit_work_Rl;//返回布局
	private ArrayList<String> addTitle;//新增标题的集合
	private ArrayList<MyEditTextView> addItem;//新增的条目
	private ArrayList<MyLineraLayout> layouts;//存放新增的布局
	private int count;
	private LinearLayout work_main_Ll;//工作总布局
	private boolean isNull;
	private ArrayList<MyEditTextView> editTextViews;//添加自定义每个条目
	private LinearLayout custom_main1_Ll;//添加自定条目的布局
	private WorkExperience workExperience;//工作经历的  对象
	private ArrayList<WorkExperience> workExperience_list; //对象的集合
	
	private String startTime;//开始时间
	private String endTime;//结束时间
	private ArrayList<Basic> colleagueList;//同事的集合
	private ArrayList<WorkExperience> workExperience_addlist;//新增对象的集合
	private ArrayList<Basic> customTagaddList;//新增自定义的集合
	private ArrayList<Basic> colleagueaddList;//新增同事的集合
	private String addstartTime;//新增的开始时间
	private String addendTime;//新增结束时间
	private MyEditTextView time_Etv;//新增的工作时间
	private MyEditTextView company_Etv;//新增工作单位
	private MyEditTextView companyIndustry_Etv;//新增行业
	private MyEditTextView certifier_Etv;//新增证明人
	private MyEditTextView certifierPhone_Etv;//新增证明人电话
	private MyEditTextView department_Etv;//新增部门
	private MyEditTextView position_Etv;//新增职位
	private MyEditTextView colleague_Etv;//新增同事
	private MyEditTextView custom_Etv;//新增自定义
	private ArrayList<MyEditTextView> addeditTextViews;
	private ArrayList<CheckBox> cbList;//工作经历身份的集合
	private CheckBox work_identity_Cb;//新增的工作经历身份
	private ArrayList<WorkExperience> workExperience_bean;
	private int checkbox_count;
	private WorkExperience addexperience;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_work_experience);
		//获取从创建人脉返回来的数据
		workExperience_bean = (ArrayList<WorkExperience>) this.getIntent().getSerializableExtra("WorkExperience_Bean");
		//工作经历对象
		workExperience = new WorkExperience();
		if (editTextViews == null) {
			editTextViews = new ArrayList<MyEditTextView>();//添加自定义条目
		}
		addeditTextViews = new ArrayList<MyEditTextView>();//新增添加自定义条目
		title_content = new ArrayList<String>();//原始标题和内容
		addTitle = new ArrayList<String>();//存新增的标题
		addItem = new ArrayList<MyEditTextView>();//存新增的条目
		layouts = new ArrayList<MyLineraLayout>();//存放新增的布局
		propertyList = new ArrayList<CustomerPersonalLine>(); // 自定义值的集合
		customTagList = new ArrayList<Basic>();//自定义集合
		colleagueList = new ArrayList<Basic>();//同事集合
		customTagaddList = new ArrayList<Basic>();//新增自定义集合
		colleagueaddList = new ArrayList<Basic>();//新增同事集合
		workExperience_list = new ArrayList<WorkExperience>();//对象的集合
		//选经历是否为当前身份的集合
		if (cbList==null) {
			cbList = new ArrayList<CheckBox>();
		}
		init();
	}

	private void init() {
		quit_work_Rl = (RelativeLayout) findViewById(R.id.quit_work_Rl);//返回布局
		finish_work_Tv = (TextView) findViewById(R.id.finish_work_Tv);//完成
		data_work_Etv = (MyEditTextView) findViewById(R.id.data_work_Etv);//工作时间
		unit_work_Etv = (MyEditTextView) findViewById(R.id.unit_work_Etv);//单位名称
		industry_work_Etv = (MyEditTextView) findViewById(R.id.industry_work_Etv);//行业
		certifier_work_Etv = (MyEditTextView) findViewById(R.id.certifier_work_Etv);//证明人
		phone_work_Etv = (MyEditTextView) findViewById(R.id.phone_work_Etv);//证明人电话
		section_work_Etv = (MyEditTextView) findViewById(R.id.section_work_Etv);//部门
		duty_work_Etv = (MyEditTextView) findViewById(R.id.duty_work_Etv);//职位
		colleague_work_Etv = (MyEditTextView) findViewById(R.id.colleague_work_Etv);//同事
		custom_main1_Ll = (LinearLayout) findViewById(R.id.custom_main1_Ll);//添加自定条目的布局
		custom_work_Etv = (MyEditTextView) findViewById(R.id.custom_work_Etv);// 自定义
		work_experience_Cb = (CheckBox) findViewById(R.id.work_experience_Cb);//当做工作经历的选择框
		continueadd_Ll = (LinearLayout) findViewById(R.id.continueadd_Ll);//新增工作经历的总布局
		work_Ll = (LinearLayout) findViewById(R.id.work_Ll);//时间、单位名称、行业。。布局
		work_main_Ll = (LinearLayout) findViewById(R.id.work_main_Ll);//工作总布局
		delete_Ll = (LinearLayout) findViewById(R.id.delete_Ll);//删除
		delete_Ll.setVisibility(View.GONE);
		work_experience_Rl = (RelativeLayout) findViewById(R.id.work_experience_Rl);//将本工作经历当做为当前身份
		TextView continueadd_Tv = (TextView) findViewById(R.id.continueadd_Tv);//新增工作经历  标题
		continueadd_Tv.setText("新增工作经历");
		phone_work_Etv.setNumEdttext_inputtype();//设置输入类型为数字
		work_experience_Rl.setOnClickListener(this);
		quit_work_Rl.setOnClickListener(this);
		delete_Ll.setOnClickListener(this);
		continueadd_Ll.setOnClickListener(this);

		finish_work_Tv.setOnClickListener(this);
		data_work_Etv.setOnClickListener(this);
		industry_work_Etv.setOnClickListener(this);
		custom_work_Etv.setOnClickListener(this);
		
		
		
		//如果有值的进来显示
		if (workExperience_bean != null) {
			for (int i = 0; i < workExperience_bean.size(); i++) {
				WorkExperience experience = workExperience_bean.get(i);//获取对象进行赋值
				if (i==0) {//第一条没有删除
					if (experience.stime==null&&experience.stime==null) {
						data_work_Etv.setText("");
					}else{
						data_work_Etv.setText(experience.stime+"-"+experience.etime);//时间
					}
					unit_work_Etv.setText(experience.company);//工作单位
					industry_work_Etv.setText(experience.companyIndustry);//行业
					certifier_work_Etv.setText(experience.certifier);//证明人
					phone_work_Etv.setText(experience.certifierPhone);//证明人电话
					section_work_Etv.setText(experience.department);//部门
					duty_work_Etv.setText(experience.position);//职位
					if (experience.colleagueRelationshipList != null
							&& !experience.colleagueRelationshipList
									.isEmpty()) { //同事
						for (int x = 0; x < experience.colleagueRelationshipList
								.size(); x++) {
							colleague_work_Etv
									.setText(experience.colleagueRelationshipList
											.get(x).content);
						}
					}
					if (experience.custom != null) {// 自定义
						if (!experience.custom.isEmpty()) {
							for (int y = 0; y < experience.custom.size(); y++) {
								Basic basic = experience.custom.get(y);
								final MyEditTextView editTextView; editTextView = new MyEditTextView(context);
								editTextView.setCustom(true);
								editTextView.setDelete(true);
								editTextView.setTextLabel(basic.name);
								editTextView.setText(basic.content);
								work_Ll.addView(editTextView,
										work_Ll.indexOfChild(custom_work_Etv) - 1);
								if (editTextView!=null&&editTextViews!=null) {
									editTextViews.add(editTextView);
								}
									editTextView.getAddMore_Iv()
											.setOnClickListener(
													new OnClickListener() {

														@Override
														public void onClick(View v) {
															editTextViews
																	.remove(editTextView);
															work_Ll.removeView(editTextView);
														}
													});
								}
							}
						}
					//工作经历当做身份赋值
					work_experience_Cb.setChecked(experience.currentStatus);
				}
				if (i>=1) {//不断添加的
					addTitle = new ArrayList<String>();//存新增的标题
					addItem = new ArrayList<MyEditTextView>();//存新增的条目
					layouts = new ArrayList<MyLineraLayout>();//存放新增的布局
					
						addexperience();//新增工作经历
						if (cbList!=null&&!cbList.isEmpty()) {
							for (int j = 0; j < cbList.size(); j++) {//遍历新增     工作经历当做身份的集合
								CheckBox checkBox = cbList.get(j);
								if (checkBox.isChecked()&&checkBox.getId()==j) {
									checkBox.setChecked(true);
									work_experience_Cb.setChecked(false);
								}else {
									checkBox.setChecked(false);
								}
								
								
								
								/*final RadioButton radioButton = cbList.get(j);//获取每一个新增的工作身份的条目
								checkbox_count = j;//将工作身份的条数赋值
								 radioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {//设置一个选择监听
									@Override
									public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
										if (isChecked) {
											int isTrue = cbList.indexOf(radioButton);//匹配
											if (checkbox_count!=isTrue) {//不匹配为true,匹配为false
												radioButton.setChecked(false);
											}else{
												radioButton.setChecked(true);
												work_experience_Cb.setChecked(false);
											}
											
										}else{
											radioButton.setChecked(true);
										}
									}
								});*/
							}
						}
						initAddView(experience);//初始化新增控件
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finish_work_Tv://点击完成
			if (!TextUtils.isEmpty(data_work_Etv.getText())) {//时间
				title_content.add(data_work_Etv.getTextLabel() + "_"
						+ data_work_Etv.getText());
				/*String target ="-";
				String startTime = getString(data_work_Etv.getText(), target);*/
				String[] split = data_work_Etv.getText().split("-");//对时间进行分割----得到开始时间和结束时间
				startTime = split[0];
				endTime = split[1];
				
				
			}
			if (!TextUtils.isEmpty(unit_work_Etv.getText())) {//工作单位
				title_content.add(unit_work_Etv.getTextLabel() + "_"
						+ unit_work_Etv.getText());

			}
			if (!TextUtils.isEmpty(industry_work_Etv.getText())) {//行业
				title_content.add(industry_work_Etv.getTextLabel() + "_"
						+ industry_work_Etv.getText());

			}
			if (!TextUtils.isEmpty(certifier_work_Etv.getText())) {//证明人
				title_content.add(certifier_work_Etv.getTextLabel() + "_"
						+ certifier_work_Etv.getText());

			}
			if (!TextUtils.isEmpty(phone_work_Etv.getText())) {//证明人电话
				title_content.add(phone_work_Etv.getTextLabel() + "_"
						+ phone_work_Etv.getText());

			}

			if (!TextUtils.isEmpty(section_work_Etv.getText())) {//部门
				title_content.add(section_work_Etv.getTextLabel() + "_"
						+ section_work_Etv.getText());

			}
			if (!TextUtils.isEmpty(duty_work_Etv.getText())) {//职位
				title_content.add(duty_work_Etv.getTextLabel() + "_"
						+ duty_work_Etv.getText());

			}

			if (!TextUtils.isEmpty(colleague_work_Etv.getText())) {//同事
				title_content.add(colleague_work_Etv.getTextLabel() + "_"
						+ colleague_work_Etv.getText());

			}
			if (!TextUtils.isEmpty(colleague_work_Etv.getText())) {//同事
				Basic basic = new Basic();
				basic.content = colleague_work_Etv.getText();
				colleagueList.add(basic);
				title_content.add(colleague_work_Etv.getTextLabel() + "_"
						+ colleague_work_Etv.getText());
			}	
			if (editTextViews != null && !editTextViews.isEmpty()) {//自定义
				for (int i = 0; i < editTextViews.size(); i++) {
					MyEditTextView myEditTextView = editTextViews.get(i);
					Basic basic = new Basic();
					basic.content = myEditTextView.getText();
					basic.name = myEditTextView.getTextLabel();
					basic.type = "N";
					customTagList.add(basic);
					title_content.add(myEditTextView.getTextLabel() + "_"
							+ myEditTextView.getText());
				}

			}
			//对输入内容的进行保存
			workExperience.stime=startTime;
			workExperience.etime=endTime;
			workExperience.company = unit_work_Etv.getText();
			workExperience.position = duty_work_Etv.getText();
			workExperience.colleagueRelationshipList=colleagueList;
			workExperience.custom = customTagList;
			workExperience.department = section_work_Etv.getText();
			workExperience.certifierPhone = phone_work_Etv.getText();
			workExperience.certifier = certifier_work_Etv.getText();
			workExperience.companyIndustry = industry_work_Etv.getText();
			if (work_experience_Cb.isChecked()) {//判断是否为选中，选中则传过去
				workExperience.currentStatus = true;
			}
			workExperience_list.add(workExperience);
			
			if (addItem != null && !addItem.isEmpty()) {//新增的条目的集合
				for (int i = 0; i < addItem.size(); i++) {
					if (!TextUtils.isEmpty(addItem.get(i).getText())) {
						title_content.add(addItem.get(i).getTextLabel() + "_"
								+ addItem.get(i).getText());
					}
				}
			}
			
			
			if (!layouts.isEmpty()) {//新增工作经历存放的集合
				initAddView(null);//初始化新增控件
			}
			
			if (!"[]".equals(title_content.toString())) {//将内容传到创建人脉
				Intent intent = new Intent(context,
						NewConnectionsActivity.class);
				intent.putStringArrayListExtra(ENavConsts.WORK_EXPERIENCE, title_content);
				//intent.putParcelableArrayListExtra("WorkExperience_Bean",workExperience_list);
				Bundle bundle = new Bundle();
				bundle.putSerializable("WorkExperience_Bean", workExperience_list);
				intent.putExtras(bundle);
				setResult(PeopleModuleReqCode.WORK_EXPERIENCE_REQ_CODE, intent);
			}
			finish();
			break;
		case R.id.data_work_Etv://点击时间
			intent = new Intent(this, WorkDataActivity.class);
			if (intent != null) {
				startActivityForResult(intent, 0);
			}
			break;
		case R.id.industry_work_Etv://点击行业
			ENavigate.startChooseActivityForResult(this, true, "行业",
					ChooseDataUtil.CHOOSE_type_Trade, null);
			break;
		case R.id.custom_work_Etv://点击自定义
			/*
			 * intent = new Intent(this, Custom_Activity.class);
			 * startActivityForResult(intent, 0);
			 */
			AlertDialog.Builder builder = new Builder(WorkExperienceActivity.this);//弹窗
			View view2 = View.inflate(WorkExperienceActivity.this,
					R.layout.people_alertdialog_module, null);//弹窗布局
			builder.setView(view2);
			create = builder.create();
			alertdialog_Tv = (TextView) view2
					.findViewById(R.id.alertdialog_module_Tv);
			alertdialog_Yes = (TextView) view2
					.findViewById(R.id.alertdialog_module_Yes);
			alertdialog_No = (TextView) view2
					.findViewById(R.id.alertdialog_module_No);
			alertdialog_Et = (EditText) view2
					.findViewById(R.id.alertdialog_module_Et);
			alertdialog_Et.addTextChangedListener(new TextWatcher() {//对输入的内容进行一个监听

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (s.length() >= 6) {
						Toast.makeText(context, "不得超过12个字符", 0).show();
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable arg0) {

				}
			});
			alertdialog_Tv.setText("添加自定义字段");
			alertdialog_Yes.setOnClickListener(new OnClickListener() {//点击确定的一些操作
				@Override
				public void onClick(View v) {
					String text = alertdialog_Et.getText().toString().trim();//获取输入的你内容
					final MyEditTextView editTextView = new MyEditTextView(
							context);
					editTextView.setDelete(true);//设置添加的自定义为可删除的
					work_Ll.addView(editTextView,work_Ll.indexOfChild(custom_work_Etv) - 1);//在自定义上加一个条目
					editTextView.getAddMore_Iv().setOnClickListener(
							new OnClickListener() {
								private AlertDialog delete_dialog;

								@Override
								public void onClick(View v) {
									if (!TextUtils.isEmpty(editTextView
											.getText())) {
										AlertDialog.Builder delete = new Builder(
												WorkExperienceActivity.this);
										View delete_view = View
												.inflate(
														WorkExperienceActivity.this,
														R.layout.people_delete_alertdialog,
														null);
										delete.setView(delete_view);
										delete_dialog = delete.create();
										delete_dialog.show();
										alertdialog_Tv = (TextView) delete_view
												.findViewById(R.id.alertdialog_delete_Tv);
										alertdialog_Yes = (TextView) delete_view
												.findViewById(R.id.alertdialog_delete_Yes);
										alertdialog_No = (TextView) delete_view
												.findViewById(R.id.alertdialog_delete_No);
										alertdialog_Yes
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														work_Ll.removeView(editTextView);
														editTextViews
																.remove(editTextView);
														delete_dialog.dismiss();
													}
												});
										alertdialog_No
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														delete_dialog.dismiss();
													}
												});
									} else {
										work_Ll.removeView(editTextView);
										editTextViews.remove(editTextView);
									}

								}
							});
					editTextViews.add(editTextView);

					if (!TextUtils.isEmpty(text)) {
						editTextView.setTextLabel(text);
					} else {
						editTextView.setTextLabel("自定义");
					}
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
			break;
		case R.id.quit_work_Rl://点击返回
			finish();
			break;
		case R.id.delete_Ll://点击删除
			if (!(count == 0)) {
				work_Ll.removeAllViews();
			} else {
				finish();
			}
			count--;
			break;
		case R.id.continueadd_Ll://点击新增工作经历
			addexperience();
			if (cbList!=null&&!cbList.isEmpty()) {
				
				for (int j = 0; j < cbList.size(); j++) {
					final CheckBox checkBox = cbList.get(j);
					 checkbox_count = j;
					 //对checkBox进行事件监听
					 checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									//进行匹配
									int isTrue = cbList.indexOf(checkBox);
									if (checkbox_count!=isTrue) {
										checkBox.setChecked(true);
									}
									work_experience_Cb.setChecked(false);
								}else{
									checkBox.setChecked(false);
								}
							}
						});
				}
			}
			break;
		case R.id.work_experience_Rl://点击是否当作工作经历
			if (work_experience_Cb.isChecked()) {
				work_experience_Cb.setChecked(false);
			} else {
				work_experience_Cb.setChecked(true);
				work_identity_Cb.setChecked(false);
			}
			break;
		default:
			break;
		}

	}
	/*初始化新增控件*/
	private void initAddView(WorkExperience experience) {
		if (layouts!=null&&!layouts.isEmpty()) {
			
		for (int i = 0; i < layouts.size(); i++) {
				
			time_Etv = (MyEditTextView) layouts.get(i).getChildAt(1);
			company_Etv = (MyEditTextView) layouts.get(i).getChildAt(2);
			companyIndustry_Etv = (MyEditTextView) layouts.get(i).getChildAt(3);
			certifier_Etv = (MyEditTextView) layouts.get(i).getChildAt(4);
			certifierPhone_Etv = (MyEditTextView) layouts.get(i).getChildAt(5);
			department_Etv = (MyEditTextView) layouts.get(i).getChildAt(6);
			position_Etv = (MyEditTextView) layouts.get(i).getChildAt(7);
			colleague_Etv = (MyEditTextView) layouts.get(i).getChildAt(8);
			
			if (experience!=null) { //当初始化控件时走这里,设置值
				if (experience.stime==null&&experience.stime==null) {
					time_Etv.setText("");
				}else{
					time_Etv.setText(experience.stime+"-"+experience.etime);//新增的时间
				}
				company_Etv.setText(experience.company);//新增的单位名称
				companyIndustry_Etv.setText(experience.companyIndustry);//新增的单位行业
				certifier_Etv.setText(experience.certifier);//新增的证明人
				certifierPhone_Etv.setText(experience.certifierPhone);//新增的证明人电话
				department_Etv.setText(experience.department);//新增的部门显示
				position_Etv.setText(experience.position);//新增的职位显示
				//同事
				if (experience.colleagueRelationshipList != null&& !experience.colleagueRelationshipList.isEmpty()) { 
					for (int x = 0; x < experience.colleagueRelationshipList
							.size(); x++) {
						colleague_Etv
								.setText(experience.colleagueRelationshipList
										.get(x).content);
					}
				}
				//自定义
				if (experience.custom != null) {// 自定义
					if (!experience.custom.isEmpty()) {
						for (int y = 0; y < experience.custom.size(); y++) {
							final MyLineraLayout layout = new MyLineraLayout(context);
							Basic basic = experience.custom.get(y);
							 final MyEditTextView editTextView; editTextView = new MyEditTextView(context);
							editTextView.setCustom(true);
							editTextView.setDelete(true);
							editTextView.setTextLabel(basic.name);
							editTextView.setText(basic.content);
							layout.addView(editTextView,
									layout.indexOfChild(colleague_Etv) + 1);
							if (editTextView!=null&&addeditTextViews!=null) {
								addeditTextViews.add(editTextView);
							}
								editTextView.getAddMore_Iv()
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(View v) {
														addeditTextViews.remove(editTextView);
														layout.removeView(editTextView);
													}
												});
							}
						}
					}
				//工作经历当做当前身份
				CheckBox checkBox = cbList.get(i);
				checkBox.setChecked(experience.currentStatus);
			}else{ //当完成时走这里，获取值
				if (time_Etv!=null&&!TextUtils.isEmpty(time_Etv.getText())) {
					String[] split = time_Etv.getText().split("-");
					addstartTime = split[0];
					addendTime = split[1];
				}
					addexperience = new WorkExperience();
					addexperience.stime=addstartTime;//新增开始时间
					addexperience.etime=addendTime;//新增结束时间
					//新增的单位
					addexperience.company=company_Etv.getText();
					
					//新增单位行业
					addexperience.companyIndustry=companyIndustry_Etv.getText();			
					
					//新增的证明人
					addexperience.certifier=certifier_Etv.getText();
					
					//新增的证明人电话
					addexperience.certifierPhone=certifierPhone_Etv.getText();
					
					//新增的部门
					addexperience.department=department_Etv.getText();
					
					//新增的职位
					addexperience.position=position_Etv.getText();
					
					//新增的同事
					Basic basic = new Basic();
					basic.content = colleague_Etv.getText();
					colleagueaddList.add(basic);
					addexperience.colleagueRelationshipList=colleagueaddList;
					
					//自定义
					if (addeditTextViews != null && !addeditTextViews.isEmpty()) {//自定义
						for (int i1 = 0; i1 < addeditTextViews.size(); i1++) {
							MyEditTextView myEditTextView = addeditTextViews.get(i1);
							Basic basic1 = new Basic();
							basic1.content = myEditTextView.getText();
							basic1.name = myEditTextView.getTextLabel();
							basic1.type = "N";
							customTagaddList.add(basic1);
							addexperience.custom=customTagaddList;
						}
					}
					//工作经历做当前身份
					if (cbList != null&& !cbList.isEmpty()) {
						for (int j = 0; j < cbList.size(); j++) {
							CheckBox cb = cbList.get(j);
							if (cb.isChecked()) {
								addexperience.currentStatus = true;
							}
						}
							
					}
					workExperience_list.add(addexperience);//添加工作对象
				}
			}
		}
	}
	//新增一个工作经历
	private void addexperience() {
		cbList =  new ArrayList<CheckBox>();
		final MyLineraLayout layout = new MyLineraLayout(context);
		MyDeleteView deleteView = new MyDeleteView(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		if (data_work_Etv.getTextLabel()!=null) {
			addTitle.add(data_work_Etv.getTextLabel());
		}
		if (unit_work_Etv.getTextLabel()!=null) {
			addTitle.add(unit_work_Etv.getTextLabel());
		}
		if (industry_work_Etv.getTextLabel()!=null) {
			addTitle.add(industry_work_Etv.getTextLabel());
		}
		if (certifier_work_Etv.getTextLabel()!=null) {
			addTitle.add(certifier_work_Etv.getTextLabel());
		}
		if (phone_work_Etv.getTextLabel()!=null) {
			addTitle.add(phone_work_Etv.getTextLabel());
		}
		if (section_work_Etv.getTextLabel()!=null) {
			addTitle.add(section_work_Etv.getTextLabel());
		}
		if (duty_work_Etv.getTextLabel()!=null) {
			addTitle.add(duty_work_Etv.getTextLabel());
		}
		if (colleague_work_Etv.getTextLabel()!=null) {
			addTitle.add(colleague_work_Etv.getTextLabel());
		}
		if (custom_work_Etv.getTextLabel()!=null) {
			addTitle.add(custom_work_Etv.getTextLabel());
		}
		
		for (int i = 0; i < addTitle.size(); i++) {
			final MyEditTextView MyEditTextView = new MyEditTextView(
					context);
			String text = addTitle.get(i);
			if (i == 0) {//新增的工作时间
				MyEditTextView.setChoose(true);
				MyEditTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mLineraDictionary.put(
								MyEditTextView.getMyEdit_Id(), layout);
						mDictionary.put(MyEditTextView.getMyEdit_Id(),
								MyEditTextView);
						intent = new Intent(WorkExperienceActivity.this,
								WorkDataActivity.class);
						intent.putExtra("Work_Data_ID",
								MyEditTextView.getMyEdit_Id());
						startActivityForResult(intent, 1);
					}
				});

			} 
			
				if (i == 2) {//单位行业
				MyEditTextView.setChoose(true);
				MyEditTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mLineraDictionary.put(
								MyEditTextView.getMyEdit_Id(), layout);
						mDictionary.put(MyEditTextView.getMyEdit_Id(),
								MyEditTextView);
						ENavigate.startChooseActivityForResult(
								WorkExperienceActivity.this, true, "行业",
								ChooseDataUtil.CHOOSE_type_Trade, null,
								MyEditTextView.getMyEdit_Id());
					}
				});
				
			} 
				
				if (i == 4) {//证明人电话
				MyEditTextView.setNumEdttext_inputtype();
				
			}
				
			if (i == addTitle.size() - 1) {//自定义
				MyEditTextView.setAddMore(true);
				MyEditTextView.setReadOnly(true);
				MyEditTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new Builder(
								WorkExperienceActivity.this);
						View view2 = View.inflate(
								WorkExperienceActivity.this,
								R.layout.people_alertdialog_module, null);
						builder.setView(view2);
						create = builder.create();
						alertdialog_Tv = (TextView) view2
								.findViewById(R.id.alertdialog_module_Tv);
						alertdialog_Yes = (TextView) view2
								.findViewById(R.id.alertdialog_module_Yes);
						alertdialog_No = (TextView) view2
								.findViewById(R.id.alertdialog_module_No);
						alertdialog_Et = (EditText) view2
								.findViewById(R.id.alertdialog_module_Et);
						alertdialog_Et
								.addTextChangedListener(new TextWatcher() {

									@Override
									public void onTextChanged(
											CharSequence s, int start,
											int before, int count) {
										if (s.length() >= 6) {
											ToastUtil.showToast(context,
													"不得超过12个字符");
										}
									}

									@Override
									public void beforeTextChanged(
											CharSequence s, int start,
											int count, int after) {

									}

									@Override
									public void afterTextChanged(
											Editable arg0) {

									}
								});
						alertdialog_Tv.setText("添加自定义字段");
						alertdialog_Yes
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										String text = alertdialog_Et
												.getText().toString()
												.trim();
										final MyEditTextView editTextView = new MyEditTextView(
												context);
										editTextView.setDelete(true);
										layout.addView(
												editTextView,
												layout.indexOfChild(MyEditTextView));
										editTextView
												.getAddMore_Iv()
												.setOnClickListener(
														new OnClickListener() {
															private AlertDialog delete_dialog;

															@Override
															public void onClick(
																	View v) {
																if (!TextUtils
																		.isEmpty(editTextView
																				.getText())) {
																	AlertDialog.Builder delete = new Builder(
																			WorkExperienceActivity.this);
																	View delete_view = View
																			.inflate(
																					WorkExperienceActivity.this,
																					R.layout.people_delete_alertdialog,
																					null);
																	delete.setView(delete_view);
																	delete_dialog = delete
																			.create();
																	delete_dialog
																			.show();
																	alertdialog_Tv = (TextView) delete_view
																			.findViewById(R.id.alertdialog_delete_Tv);
																	alertdialog_Yes = (TextView) delete_view
																			.findViewById(R.id.alertdialog_delete_Yes);
																	alertdialog_No = (TextView) delete_view
																			.findViewById(R.id.alertdialog_delete_No);
																	alertdialog_Yes
																			.setOnClickListener(new OnClickListener() {

																				@Override
																				public void onClick(
																						View v) {
																					layout.removeView(editTextView);
																					addeditTextViews
																							.remove(editTextView);
																					delete_dialog
																							.dismiss();
																				}
																			});
																	alertdialog_No
																			.setOnClickListener(new OnClickListener() {

																				@Override
																				public void onClick(
																						View v) {
																					delete_dialog
																							.dismiss();
																				}
																			});
																} else {
																	layout.removeView(editTextView);
																	addeditTextViews
																			.remove(editTextView);
																}

															}
														});
										addeditTextViews.add(editTextView);

										if (!TextUtils.isEmpty(text)) {
											editTextView.setTextLabel(text);
										} else {
											editTextView
													.setTextLabel("自定义");
										}
										create.dismiss();
									}
								});
						alertdialog_No
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										create.dismiss();
									}
								});
						create.show();

					}
				});

			}
			
			MyEditTextView.setTextLabel(text);
			addItem.add(MyEditTextView);
			layout.addView(MyEditTextView, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		}
		//工作经历当做身份  的布局引入
		View identityView = View.inflate(context, R.layout.people_include_identity, null);
		work_identity_Cb = (CheckBox)identityView.findViewById(R.id.work_identity_Cb);
		cbList.add(work_identity_Cb);
		RelativeLayout work_identity_Rl = (RelativeLayout) identityView.findViewById(R.id.work_identity_Rl);
		/*work_identity_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (work_identity_Cb.isChecked()) {
					work_identity_Cb.setChecked(false);
				} else {
					work_identity_Cb.setChecked(true);
					work_experience_Cb.setChecked(false);
				}
			}
		});*/
		
		//点击删除的条目
		deleteView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				work_main_Ll.removeView(layout);
				layouts.remove(layout);
				count--;
			}
		});
		//将布局引入
		layout.addView(identityView);
		layout.addView(deleteView);
		
		layouts.add(layout);
		work_main_Ll.addView(layout,2, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addTitle.removeAll(addTitle);
		count++;
	}

	

	public void setChooseText(ArrayList<Metadata> data,
			MyEditTextView industry_view) {
		// 行业
		if (metadataIndustry != null) {
			metadataIndustry.clear();
			industry.clear();
			industryIds.clear();
		}
		metadataIndustry = data;
		industry_view.setText(ChooseDataUtil.getMetadataName(metadataIndustry,
				9));
		// industry.add(org_industry_Etv.getText().toString());// 客户行业

		if (metadataIndustry != null) {
			if (!metadataIndustry.isEmpty()) {
				for (Metadata Industrydata : metadataIndustry) {

					// 有二级
					for (Metadata data2 : Industrydata.childs) {
						// 有三级
						for (Metadata data3 : data2.childs) {
							industry.add(data3.name);
						}
						industry.add(data2.name);
					}
					industry.add(Industrydata.name);
				}
			}

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT) {

				// 多级选择回调界面
				setChooseText(
						(ArrayList<Metadata>) data
								.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA),
						industry_work_Etv);

			}
			if (requestCode == 0) {

				switch (resultCode) {
				case 999:
					isNull = data.getBooleanExtra("isNull", false);
					propertyList = custom2(data, custom_work_Etv,
							custom_main1_Ll, isNull, editTextViews);
				case 3:
					String Work_Data = data.getStringExtra("Work_Data");
					if (!TextUtils.isEmpty(Work_Data)) {
						data_work_Etv.setText(Work_Data);
					}
					break;

				default:
					break;
				}
			}
			if (requestCode == 1) {
				String work_Data_ID = data.getStringExtra("work_Data_ID");
				String Work_Custom_ID = data.getStringExtra("Work_Custom_ID");
				String industry_id = data.getStringExtra(ENavConsts.PEOPLE_ID);
				// 多级选择回调界面
				if (!TextUtils.isEmpty(industry_id)) {
					setChooseText(
							(ArrayList<Metadata>) data
									.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA),
							mDictionary.get(industry_id));
				}
				switch (resultCode) {
				case 999:
					custom2(data, mDictionary.get(Work_Custom_ID),
							mLineraDictionary.get(Work_Custom_ID), isNull,
							addeditTextViews);
					break;
				case 3:
					String Work_Data = data.getStringExtra("Work_Data");
					if (!TextUtils.isEmpty(Work_Data)) {
						if (work_Data_ID != null) {
							MyEditTextView myEditTextView = mDictionary
									.get(work_Data_ID);
							myEditTextView.setText(Work_Data);
						}
					}
					break;
				default:
					break;
				}
			}
		}
	}
}
