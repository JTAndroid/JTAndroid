package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.CreateDemandBean;
import com.tr.model.demand.TemplateData;
import com.tr.model.demand.TemplateList;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.demand.TemplateActivity.TemplateAdapter;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 创建需求界面： 选择投融资类型
 * 
 * @author Administrator
 * 
 */
public class CreateDemandActivity extends JBaseActivity implements IBindData {
	private ExpandableListView demand_Lv;
	TextView titileTv;
	private boolean isForResult = false;// 是否能回调
	private Dialog dialog;

	private ArrayList<TemplateData> rzchildArray = new ArrayList<TemplateData>();
	private ArrayList<TemplateData> tzchildArray = new ArrayList<TemplateData>();
	private ArrayList<CreateDemandBean> groupArray = new ArrayList<CreateDemandBean>();
	private DemandExpandableAdapter demandExpandableAdapter;
	private static final int GROUP_POSITION = 0;
	private static final int CHILD_POSITION = 1;
	private static final int GROUP = 2;
	public HashMap<String, ArrayList<TemplateData>> map = new HashMap<String, ArrayList<TemplateData>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_create);
		initView();
		startGetData();
		isForResult = getIntent().getBooleanExtra(ENavConsts.DEMAND_FOR_RESULT,
				false);
	}

	private void initView() {
		
		titileTv.setText("创建需求");
		demand_Lv = (ExpandableListView) this.findViewById(R.id.demand_Lv);
		demandExpandableAdapter = new DemandExpandableAdapter(this);
		demand_Lv.setAdapter(demandExpandableAdapter);
		groupArray.add(new CreateDemandBean("我要融资",
				R.drawable.demand_financing,
				ChooseDataUtil.CHOOSE_type_InInvestType));
		groupArray.add(new CreateDemandBean("我要投资",
				R.drawable.demand_investment,
				ChooseDataUtil.CHOOSE_type_OutInvestType));
		for(int i = 0; i < demandExpandableAdapter.getGroupCount(); i++){  
            
			demand_Lv.expandGroup(i);  
			                          
			}  
		demand_Lv.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				TemplateData bean = (TemplateData) demandExpandableAdapter
						.getChild(groupPosition, childPosition);
				if (isForResult) {
					if (groupPosition==0) {
						ENavigate.startDemandNewActivityForResult(
								CreateDemandActivity.this,
								ENavConsts.ActivityReqCode.REQUEST_DEMAND_ACTIVITY,
								bean, ChooseDataUtil.CHOOSE_type_InInvestType);
					}else if(groupPosition==1) {
						ENavigate.startDemandNewActivityForResult(
								CreateDemandActivity.this,
								ENavConsts.ActivityReqCode.REQUEST_DEMAND_ACTIVITY,
								bean, ChooseDataUtil.CHOOSE_type_OutInvestType);
					}
					
				} else {
					if (groupPosition==0) {
						ENavigate.startDemandNewActivity(
								CreateDemandActivity.this,
								bean, ChooseDataUtil.CHOOSE_type_InInvestType);
					}else if(groupPosition==1) {
						ENavigate.startDemandNewActivity(
								CreateDemandActivity.this,
								bean, ChooseDataUtil.CHOOSE_type_OutInvestType);
					}
				}
				return false;
			}

		});
		demand_Lv.setOnGroupClickListener(new OnGroupClickListener() {
			
			private boolean isExpanded;

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				ViewHolder.get(v, R.id.rightIv).clearAnimation();
				final RotateAnimation animation;
				if (!isExpanded) {
					animation = new RotateAnimation(0f, -90f,
							Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
				} else {
					animation = new RotateAnimation(-90f, 0f,
							Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
				}

				animation.setDuration(500);// 设置动画持续时间
				animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
				ViewHolder.get(v, R.id.rightIv).setAnimation(animation);
				/** 开始动画 */
				animation.startNow();
				isExpanded =!isExpanded;
				return false;
			}
		});

		demand_Lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Integer group = (Integer) view.getTag(R.id.iconIv); //当group为null时，说明点击是子条目
				if (group == null) {
						TemplateData bean = (TemplateData)  view.getTag(R.id.child_left_Iv);
						if (Integer.parseInt(bean.id)!=-1&&Integer.parseInt(bean.id)!=-2) {//如果是默认模板，不做删除
							showDialog(bean.id);
						}
				}
				return true;
			}
		});
		
	}

	/**
	 * 
	 * @param message
	 */
	private void showDialog(final String id) {
		View upView = View.inflate(this, R.layout.demand_item_dialog, null);
		upView.findViewById(R.id.neetNameTv).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						DemandReqUtil.deleteTemplate(CreateDemandActivity.this,
								CreateDemandActivity.this, id, null);

					}

				});
		((TextView) upView.findViewById(R.id.neetNameTv)).setText("删除");
		dialog = new Dialog(this, R.style.MyDialog);
		dialog.setContentView(upView);
		dialog.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK
				&& requestCode == ENavConsts.ActivityReqCode.REQUEST_DEMAND_ACTIVITY) {
			setResult(RESULT_OK);
			this.finish();
		}
	}
	
	@Override
	public void initJabActionBar() {
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);// 不显示应用图标
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		titileTv = (TextView) mCustomView.findViewById(R.id.titleTv);
	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		int nowIndex = 0;
		DemandReqUtil.getTemplatelist(CreateDemandActivity.this,
				CreateDemandActivity.this, nowIndex, 20, 0, null);
	}

	// ExpandableListView的Adapter
	public class DemandExpandableAdapter extends BaseExpandableListAdapter {

		Activity activity;

		public DemandExpandableAdapter(Activity a) {
			activity = a;
		}

		public Object getChild(int groupPosition, int childPosition) {
			CreateDemandBean  group = groupArray.get(groupPosition);
			ArrayList<TemplateData> childArray = map.get(group.title);
			return childArray.get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			CreateDemandBean  group = groupArray.get(groupPosition);
			ArrayList<TemplateData> childArray = map.get(group.title);
			if (childArray!=null&&!childArray.isEmpty()) {
				int size = childArray.size();
				return size;
			}else{
				return 0;
			}
			
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(activity).inflate(
						R.layout.list_item_demand_template, null);
			}
			TextView nameTv = ViewHolder.get(convertView, R.id.child_nameTv);
			ImageView child_left_Iv =  ViewHolder.get(convertView, R.id.child_left_Iv);
			TemplateData bean = (TemplateData) getChild(groupPosition,childPosition);
			nameTv.setText(bean.name);
			convertView.setTag(R.id.child_nameTv, childPosition);
			convertView.setTag(R.id.child_left_Iv, bean);
			return convertView;
		}

		// group method stub
		public Object getGroup(int groupPosition) {
			return groupArray.get(groupPosition);
		}

		public int getGroupCount() {
			return groupArray.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, final boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(activity).inflate(
						R.layout.list_item_demand_create, null);
			}
			final ImageView iconIv = ViewHolder.get(convertView, R.id.iconIv);
			LinearLayout demand_group_Ll = ViewHolder.get(convertView, R.id.demand_group_Ll);
			final ImageView rightIv = ViewHolder.get(convertView, R.id.rightIv);
			TextView nameTv = ViewHolder.get(convertView, R.id.nameTv);
			CreateDemandBean bean = groupArray.get(groupPosition);
			iconIv.setImageResource(bean.icon);
			nameTv.setText(bean.title);

			
			convertView.setTag(R.id.rightIv, groupPosition);
			convertView.setTag(R.id.iconIv, -1); // 区分长按的是group
			return convertView;
		}

		public boolean hasStableIds() {
			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	/**
	 * 处理网络请求返回的数据
	 */
	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (tag == EAPIConsts.demandReqType.demand_gettemplatelist) {
			TemplateList mGetDynamic = (TemplateList) object;
			if (mGetDynamic != null) {
				map.clear();
				rzchildArray.clear();
				tzchildArray.clear();
				if (mGetDynamic.rzTemplateList!=null&&!mGetDynamic.rzTemplateList.isEmpty()) {
					rzchildArray.addAll(mGetDynamic.rzTemplateList);
					map.put("我要融资", rzchildArray);
				}
				if (mGetDynamic.tzTemplateList!=null&&!mGetDynamic.tzTemplateList.isEmpty()) {
					tzchildArray.addAll(mGetDynamic.tzTemplateList);
					map.put("我要投资", tzchildArray);
				}
				
				demandExpandableAdapter.notifyDataSetChanged();
			}
		} else if (tag == EAPIConsts.demandReqType.demand_deletedtemplate) {
			if (null != dialog) {
				dialog.dismiss();
			}
			demandExpandableAdapter.notifyDataSetChanged();
			for(int i = 0; i < demandExpandableAdapter.getGroupCount(); i++){  
				demand_Lv.expandGroup(i);  
			}  
			startGetData();
		}
	}

}
