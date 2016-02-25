package com.tr.ui.people.homepage.frg;

import java.util.List;

import org.xclcharts.chart.PieData;
import org.xclcharts.common.MathHelper;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.event.click.ChartArcListener;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.home.MHomePageNumInfo;
import com.tr.model.home.MHomePageNumInfos;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.conference.home.MeetingPiazzaActivity;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class FrgChartView extends JBaseFragment implements IBindData{

	private FrameLayout mFrameLayout;
	private List<MHomePageNumInfo> list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonReqUtil.doGetMyCountList(getActivity(), this, 0, null);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_chart_view, container, false);
		mFrameLayout = (FrameLayout) view.findViewById(R.id.chartviewFl);
		return view;
	}
	
	private void addChartView(ChartView chartView){
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	
		// 居中显示
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		// 图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
		RelativeLayout chartLayout = new RelativeLayout(getActivity());
		chartLayout.addView(chartView, layoutParams);
		chartLayout.setPadding(100, 100, 100, 100);
		// 增加控件
		mFrameLayout.addView(chartLayout);
		
		chartView.setOnPlotClickListener(new ChartArcListener() {
			@Override
			public void onClick(PointF point, ArcPosition positionRecord) {
				if (null == positionRecord)
					return;
				switch(positionRecord.getDataID()){
				case 0://人脉
					ENavigate.startFindPeopleActivity(getActivity(),1);
					break;
				case 1://组织
					ENavigate.startOrganizationFirstPageActivity(getActivity());
					break;
				case 2://需求
					ENavigate.startFindDemandActivity(getActivity(),5);
					break;
				case 3://知识
					ENavigate.startKnowledgeSquareActivity(getActivity());
					break;
				case 4://会议
					startActivity(new Intent(getActivity(), MeetingPiazzaActivity.class));
					break;
				}
			}
		});
	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.CommonReqType.GetMyCountList && object != null) {
			MHomePageNumInfos infos = (MHomePageNumInfos) object;
			list = infos.getList();
			init();
		}
	}
	
	private void init() {
		ChartView chartView = new ChartView(getActivity());
		int allPercent = 0;
		for (int i = 0; i < list.size(); i++) {
			allPercent+=list.get(i).getNum();
		}
		PieData pieData = null;
		for (int i = 0; i < list.size(); i++) {
			switch (list.get(i).getType()) {
			// 好友人脉
			case 1:
				pieData = new PieData("人脉","人脉:"+list.get(i).getNum(), MathHelper.getInstance().div(list.get(i).getNum(), allPercent)*100,Color.rgb(77, 83, 97));
				chartView.addData(pieData);
				break;
			// 2-组织（客户
			case 2:
				pieData = new PieData("客户","客户:"+list.get(i).getNum(),MathHelper.getInstance().div(list.get(i).getNum(), allPercent)*100,Color.rgb(75, 132, 1));
				chartView.addData(pieData);
				break;
			// 3-需求
			case 3:
				pieData = new PieData("需求","需求:"+list.get(i).getNum(),MathHelper.getInstance().div(list.get(i).getNum(), allPercent)*100,Color.rgb(180, 205, 230));
				chartView.addData(pieData);
				break;
			// 知识
			case 4:
				pieData = new PieData("知识","知识:"+list.get(i).getNum(),MathHelper.getInstance().div(list.get(i).getNum(), allPercent)*100,Color.rgb(148, 159, 181));
				chartView.addData(pieData);
				break;
			// 会议
			case 5:
				pieData = new PieData("会议","会议:"+list.get(i).getNum(),MathHelper.getInstance().div(list.get(i).getNum(), allPercent)*100,Color.rgb(253, 180, 90),true);
				chartView.addData(pieData);
				break;
			}
		}
		chartView.initView();
		addChartView(chartView);
	}

}
