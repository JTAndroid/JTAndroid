package com.tr.ui.home;

import java.util.HashMap;

import com.tr.App;
import com.tr.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tr.api.CommonReqUtil;
import com.tr.model.joint.JointResource;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.NewJointResourceFragment.ResourceType_new;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.CircleProgressView;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class JointResourceActivity extends JBaseFragmentActivity implements IBindData, OnClickListener{

	private CircleProgressView org_circle, know_circle, people_circle, demand_circle;
	private TextView orgTv, knowTv, peopleTv, demandTv, back;
	private int org_size, know_size, people_size, demand_size, count=0;
	private int org_size_my, know_size_my, people_size_my, demand_size_my;
	private int org_size_gt, know_size_gt, people_size_gt, demand_size_gt;
	private int org_size_friend, know_size_friend, people_size_friend, demand_size_friend;
	
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "对接图谱", false, null, false, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joint_resource_chart);
		
		org_circle = (CircleProgressView) findViewById(R.id.org_circle);
		know_circle = (CircleProgressView) findViewById(R.id.know_circle);
		people_circle = (CircleProgressView) findViewById(R.id.people_circle);
		demand_circle = (CircleProgressView) findViewById(R.id.demand_circle);
		back = (TextView) findViewById(R.id.back);
		
		orgTv = (TextView) findViewById(R.id.orgTv);
		knowTv = (TextView) findViewById(R.id.knowTv);
		peopleTv = (TextView) findViewById(R.id.peopleTv);
		demandTv = (TextView) findViewById(R.id.demandTv);
		
		getData();
		
		org_circle.setOnClickListener(this);
		know_circle.setOnClickListener(this);
		people_circle.setOnClickListener(this);
		demand_circle.setOnClickListener(this);
		back.setOnClickListener(this);
	}
	
	private void getData(){
		showLoadingDialog();
		/*"targetId":对接的物料id,
        "targetType":[
                1  知识
                2  需求
                3  组织
                4  人脉
                5  会议],
        "page":页数,
        "rows":条数,
        "scope":[范围
                1  我的
                2  好友的
                3  金桐脑的]*/
		//我的
		CommonReqUtil.doGetJointResource_new(this, this,App.getUserID(), 4, 1, 20, 1, null);
		//好友的
		CommonReqUtil.doGetJointResource_new(this, this,App.getUserID(), 4, 2, 20, 1, null);
		//金桐脑的
		CommonReqUtil.doGetJointResource_new(this, this,App.getUserID(), 4, 3, 20, 1, null);
	}

	@Override
	public void bindData(int tag, Object object) {
		switch(tag){
		case EAPIConsts.CommonReqType.GetJointResource_MY:
			if(object != null){
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				JointResource jr = (JointResource) dataMap.get("JointResource");
				org_size_my = jr.getOrgs().size();
				know_size_my = jr.getKnos().size();
				people_size_my = jr.getPeoples().size();
				demand_size_my = jr.getReqs().size();
			}
			break;
		case EAPIConsts.CommonReqType.GetJointResource_FRIEND:
			if(object != null){
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				JointResource jr = (JointResource) dataMap.get("JointResource");
				org_size_friend = jr.getOrgs().size();
				know_size_friend = jr.getKnos().size();
				people_size_friend = jr.getPeoples().size();
				demand_size_friend = jr.getReqs().size();
			}
			break;
		case EAPIConsts.CommonReqType.GetJointResource_GT:
			if(object != null){
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				JointResource jr = (JointResource) dataMap.get("JointResource");
				org_size_gt = jr.getOrgs().size();
				know_size_gt = jr.getKnos().size();
				people_size_gt = jr.getPeoples().size();
				demand_size_gt = jr.getReqs().size();
			}
			break;
		}
		count++;
		if(count == 3){
			dismissLoadingDialog();
			org_size = org_size_my + org_size_friend + org_size_gt;
			know_size = know_size_my + know_size_friend + know_size_gt;
			people_size = people_size_my + people_size_friend + people_size_gt;
			demand_size = demand_size_my + demand_size_friend + demand_size_gt;
			
			int total = org_size + know_size + people_size + demand_size;
			
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");   
			org_circle.setProgress(getNum(org_size, total));
			orgTv.setText(org_size+"");
			
			know_circle.setProgress(getNum(know_size, total));
			knowTv.setText(know_size+"");
			
			people_circle.setProgress(getNum(people_size, total));
			peopleTv.setText(people_size+"");
			
			demand_circle.setProgress(getNum(demand_size, total));
			demandTv.setText(demand_size+"");
		}
	}
	
	private float getNum(int size, int total){
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.###");
		float num = size*100.f/total;
		if(num<1){
			num = Float.valueOf(df.format(size*100.f/total));
		}else{
			num = (float) Math.floor(size*100f/total);
		}
		return num;
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.org_circle:
		case R.id.know_circle:
		case R.id.people_circle:
		case R.id.demand_circle:
			if ((org_size_my + know_size_my + people_size_my + demand_size_my) != 0) {
				ENavigate.startJointResourceActivity(this, App.getUserID(),
						ResourceType_new.People, 0);
			} else if ((org_size_friend + know_size_friend + people_size_friend + demand_size_friend) != 0) {
				ENavigate.startJointResourceActivity(this, App.getUserID(),
						ResourceType_new.People, 1);
			} else {
				ENavigate.startJointResourceActivity(this, App.getUserID(),
						ResourceType_new.People, 2);
			}
			break;
		case R.id.back:
			finish();
			break;
		}
	}
	
}
