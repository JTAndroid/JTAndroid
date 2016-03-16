package com.tr.ui.home.frg;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.joint.JointResource;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.NewJointResourceFragment.ResourceType_new;
import com.tr.ui.widgets.CircleProgressView;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class JoinFrg_chart extends JBaseFragment implements OnClickListener, IBindData {

	private PieChart pie_chart;
	private CircleProgressView org_circle, know_circle, people_circle, demand_circle;
	private TextView orgTv, knowTv, peopleTv, demandTv;
	private int peopleSize, orgSize, knowSize, demandSize, meetSize, total;
	private String keyword;
	
	public final static int TYPE_MEMBER = 4;// 人脉
	public final static int TYPE_KNOWLEDGE = 8;// 知识
	public final static int TYPE_METTING = 9;// 会议
	public final static int TYPE_DEMAND= 10;//需求
	public final static int TYPE_ORGANDCUSTOMER= 6;//组织和客户
	private int org_size, know_size, people_size, demand_size, count=0;
	private int org_size_my, know_size_my, people_size_my, demand_size_my;
	private int org_size_gt, know_size_gt, people_size_gt, demand_size_gt;
	private int org_size_friend, know_size_friend, people_size_friend, demand_size_friend;
	private String targetId;
	private int targetType;
	private FrameLayout mCommonTitle;
	private ImageView demandHomeBackIv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		getData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_new_search_chart, null);
		initView(view);
		inivListener();
		return view;
	}
	
	private void initView(View view){
		mCommonTitle = (FrameLayout) view.findViewById(R.id.mCommonTitle);
		demandHomeBackIv = (ImageView) view.findViewById(R.id.demandHomeBackIv);
		org_circle = (CircleProgressView) view.findViewById(R.id.org_circle);
		know_circle = (CircleProgressView) view.findViewById(R.id.know_circle);
		people_circle = (CircleProgressView) view.findViewById(R.id.people_circle);
		demand_circle = (CircleProgressView) view.findViewById(R.id.demand_circle);
		
		orgTv = (TextView) view.findViewById(R.id.orgTv);
		knowTv = (TextView) view.findViewById(R.id.knowTv);
		peopleTv = (TextView) view.findViewById(R.id.peopleTv);
		demandTv = (TextView) view.findViewById(R.id.demandTv);
		
		pie_chart = (PieChart) view.findViewById(R.id.pie_chart);
		
		
	}
	
	private void inivListener(){
		org_circle.setOnClickListener(this);
		know_circle.setOnClickListener(this);
		people_circle.setOnClickListener(this);
		demand_circle.setOnClickListener(this);
		demandHomeBackIv.setOnClickListener(this);
	}
	
	public void getData(){
		showLoadingDialog();
		targetId = getArguments().getString(EConsts.Key.ID);
		targetType = getArguments().getInt(EConsts.Key.TYPE);
		if(targetType == 1){
			mCommonTitle.setVisibility(View.VISIBLE);
		}
		//我的
		CommonReqUtil.doGetJointResource_new(getActivity(), this, targetId, targetType, 1, 20, 0, null);
		//好友的
		CommonReqUtil.doGetJointResource_new(getActivity(), this, targetId, targetType, 2, 20, 0, null);
		//金桐脑的
		CommonReqUtil.doGetJointResource_new(getActivity(), this, targetId, targetType, 3, 20, 0, null);
	}
	
	private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);    
    
        pieChart.setHoleRadius(40f);  //半径    
        pieChart.setTransparentCircleRadius(40f); // 半透明圈    
        //pieChart.setHoleRadius(0)  //实心圆    
        
        pieChart.setDescription("");    
    
        // mChart.setDrawYValues(true);    
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字    
    
        pieChart.setDrawHoleEnabled(true);    
    
        pieChart.setRotationAngle(180); // 初始旋转角度    
    
        // draws the corresponding description value into the slice    
        // mChart.setDrawXValues(true);    
    
        // enable rotation of the chart by touch    
        pieChart.setRotationEnabled(true); // 可以手动旋转    
    
        // display percentage values    
        pieChart.setUsePercentValues(false);  //显示成百分比    
    
        pieChart.setHighlightPerTapEnabled(true);
        // mChart.setUnit(" €");    
        // mChart.setDrawUnitsInChart(true);    
    
        // add a selection listener    
//      mChart.setOnChartValueSelectedListener(this);    
        // mChart.setTouchEnabled(false);    
    
//      mChart.setOnAnimationListener(this);    
    
        pieChart.setCenterText("");  //饼状图中间的文字    
    
        //设置数据    
        pieChart.setData(pieData);     
            
        // undo all highlights    
//      pieChart.highlightValues(null);    
//      pieChart.invalidate();    
    
        Legend mLegend = pieChart.getLegend();  //设置比例图    
        mLegend.setPosition(LegendPosition.RIGHT_OF_CHART);  //最右边显示    
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形    
        mLegend.setXEntrySpace(7f);    
        mLegend.setYEntrySpace(5f);   
        mLegend.setEnabled(false);
            
        pieChart.animateXY(1000, 1000);  //设置动画    
        // mChart.spin(2000, 0, 360);    
    }
	
	private PieData getPieData() {    
        
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容    
        xValues.add("25%");  //饼块上显示
        xValues.add("25%");
        xValues.add("25%");
        xValues.add("25%");
    
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据    
    
        // 饼图数据    
        /**  
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38  
         * 所以 14代表的百分比就是14%   
         */    
        float quarterKnow = 25;
        float quarterOrg = 25;
        float quarterPeople = 25;
        float quarterDemand = 25;
        if(total != 0){
            quarterKnow = getNum(knowSize, total);    
            quarterOrg = getNum(orgSize, total);  
            quarterPeople = getNum(peopleSize, total);    
            quarterDemand = getNum(demandSize, total);  
            xValues.clear();
            xValues.add(quarterOrg+"%"); 
            xValues.add(quarterKnow+"%"); 
            xValues.add(quarterPeople+"%"); 
            xValues.add(quarterDemand+"%"); 
        }
        
        yValues.add(new Entry(quarterOrg, 0));    
        yValues.add(new Entry(quarterKnow, 1));    
        yValues.add(new Entry(quarterPeople, 2));    
        yValues.add(new Entry(quarterDemand, 3)); 
    
        //y轴的集合    
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);    
        pieDataSet.setSliceSpace(1f); //设置个饼状图之间的距离    
        pieDataSet.setDrawValues(false);
        
        ArrayList<Integer> colors = new ArrayList<Integer>();    
    
        // 饼图颜色    
        colors.add(0xff957DEF);    
        colors.add(0xff66C4EF);    
        colors.add(0xff4DF049);    
        colors.add(0xffF5BF00);    
    
        pieDataSet.setColors(colors);    
    
        DisplayMetrics metrics = getResources().getDisplayMetrics();    
        float px = 4 * (metrics.densityDpi / 160f);    
        pieDataSet.setSelectionShift(px); // 选中态多出的长度    
    
        PieData pieData = new PieData(xValues, pieDataSet);    
        pieData.setValueTextSize(20);
        pieData.setValueTextColor(getResources().getColor(R.color.white));
        return pieData;    
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.org_circle:
		case R.id.know_circle:
		case R.id.people_circle:
		case R.id.demand_circle:
			if ((org_size_my + know_size_my + people_size_my + demand_size_my) != 0) {
				ENavigate.startJointResourceActivity(getActivity(), targetId,
						ResourceType_new.People, 0);
			} else if ((org_size_friend + know_size_friend + people_size_friend + demand_size_friend) != 0) {
				ENavigate.startJointResourceActivity(getActivity(), targetId,
						ResourceType_new.People, 1);
			} else {
				ENavigate.startJointResourceActivity(getActivity(), targetId,
						ResourceType_new.People, 2);
			}
			break;
		case R.id.demandHomeBackIv:
			getActivity().finish();
			break;
		}
	}
	
	public void setData(int peopleSize, int orgSize, int demandSize, int knowledgeSize){
		this.peopleSize = peopleSize;
		this.orgSize = orgSize;
		this.knowSize = knowledgeSize;
		this.demandSize = demandSize;
		this.total = peopleSize+orgSize+demandSize+knowledgeSize;
		
		people_circle.setProgress(getNum(peopleSize, total));
		peopleTv.setText(peopleSize+"");

		org_circle.setProgress(getNum(orgSize, total));
		orgTv.setText(orgSize+"");
		
		know_circle.setProgress(getNum(knowledgeSize, total));
		knowTv.setText(knowledgeSize+"");

		demand_circle.setProgress(getNum(demandSize, total));
		demandTv.setText(demandSize+"");
		
		pie_chart.setVisibility(View.VISIBLE);
        PieData mPieData = getPieData();
        showChart(pie_chart, mPieData);
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

	
	public void setKeyword(String keyword){
		this.keyword = keyword;
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
			org_size = org_size_my + org_size_friend + org_size_gt;
			know_size = know_size_my + know_size_friend + know_size_gt;
			people_size = people_size_my + people_size_friend + people_size_gt;
			demand_size = demand_size_my + demand_size_friend + demand_size_gt;
			dismissLoadingDialog();
			count=0;
			if(getActivity()!=null){
				setData(people_size, org_size, demand_size, know_size);
			}
		}
	}
	
}
