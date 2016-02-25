package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.TemplateData;
import com.tr.model.demand.TemplateList;
import com.tr.model.demand.TemplateListItem;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.demand.util.DemandAction;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 选择模版界面
 * 
 * @author Administrator
 *
 */
public class TemplateActivity extends JBaseActivity implements IBindData {
	private XListView demandLv;
	private JTPage jtpage;// 分页加载对象
	private List<TemplateData> listData;
	private TemplateAdapter demandAdapter;
	private TextView titileTv;
	private int typeId;// 类型id 告诉我来自哪个模块的模版
	private Dialog dialog; // 删除对话框
	private boolean isForResult= false;//是否能回调

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_template);
		getParam();
		initView();
		startGetData();
		initBroadcast();

	}

	public void initBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(DemandAction.DEMAND_TEMPLATE_ACYTION);
		this.registerReceiver(temapleBroad, filter);
	}

	/**
	 *刷新列表的
	 */
	private BroadcastReceiver temapleBroad = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (DemandAction.DEMAND_TEMPLATE_ACYTION.equals(intent.getAction())) {
				jtpage= null;
				startGetData();
			}
		}
	};

	private void getParam() {
		Intent intent = this.getIntent();
		typeId = intent.getIntExtra(ENavConsts.DEMAND_TYPE,
				ChooseDataUtil.CHOOSE_TYPE_DEFAULT);
		isForResult = intent.getBooleanExtra(ENavConsts.DEMAND_FOR_RESULT, false);
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
		titileTv.setText("选择模板");
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

						DemandReqUtil.deleteTemplate(TemplateActivity.this,
								TemplateActivity.this, id, null);

					}

				});
		((TextView) upView.findViewById(R.id.neetNameTv)).setText("删除");
		dialog = new Dialog(this, R.style.MyDialog);
		dialog.setContentView(upView);
		dialog.show();

	}

	private void initView() {
		listData = new ArrayList<TemplateData>();
		// 信息的id ，信息的名称， 信息来自上一级的id
		demandAdapter = new TemplateAdapter(this, listData);
		demandLv = (XListView) this.findViewById(R.id.demandLv);
		demandLv.showFooterView(true);
		// 设置xlistview可以加载、刷新
		demandLv.setPullLoadEnable(true);
		demandLv.setPullRefreshEnable(true);
		demandLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TemplateData bean = (TemplateData) demandAdapter
						.getItem(position -1);
				if(isForResult){
					ENavigate.startDemandNewActivityForResult(TemplateActivity.this,ENavConsts.ActivityReqCode.REQUEST_DEMAND_ACTIVITY, bean,
							typeId);
				}else{
				ENavigate.startDemandNewActivity(TemplateActivity.this, bean,
						typeId);
				}
			}
		});
		demandLv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				TemplateData bean = (TemplateData) demandAdapter
						.getItem(arg2 - 1);
				if (arg2 != 1) {
					showDialog(bean.id);
				}
				return true;
			}
		});
		
		demandLv.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				DemandReqUtil.getTemplatelist(TemplateActivity.this,
						TemplateActivity.this, 0, 20, typeId, null);
			}

			@Override
			public void onLoadMore() {
				startGetData();
			}
		});
		demandLv.setAdapter(demandAdapter);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode==ENavConsts.ActivityReqCode.REQUEST_DEMAND_ACTIVITY){
			setResult(RESULT_OK);
			this.finish();
		}
	}
	/**
	 * 获取页数据
	 */
	public void startGetData() {
		int nowIndex = 0;
		if (jtpage != null) {
			nowIndex = jtpage.getIndex() + 1;
		} else {
			showLoadingDialog();
		}
		DemandReqUtil.getTemplatelist(TemplateActivity.this,
				TemplateActivity.this, nowIndex, 20, typeId, null);
	}

	/**
	 * 列表数据适配器
	 * 
	 * @author Administrator
	 *
	 */
	class TemplateAdapter extends BaseAdapter {
		private Context context;
		private List<TemplateData> listData;

		public TemplateAdapter(Context context, List<TemplateData> listData) {
			this.context = context;
			this.listData = listData;
		}

		public void setData(List<TemplateData> mData) {
			this.listData = mData;
		}

		@Override
		public int getCount() {
			return listData != null ? listData.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return listData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_demand_template, null);
			}
			TextView nameTv = ViewHolder.get(convertView, R.id.nameTv);
			TemplateData bean = listData.get(position);
			nameTv.setText(bean.name);
			return convertView;
		}
	}

	/**
	 * 处理网络请求返回的数据
	 */
	@Override
	public void bindData(int tag, Object object) {
		try {
		demandLv.stopLoadMore();
		demandLv.stopRefresh();
		demandLv.showFooterView(false);
		dismissLoadingDialog();
		if (tag == EAPIConsts.demandReqType.demand_gettemplatelist) {
			TemplateList mGetDynamic = (TemplateList) object;
			if (mGetDynamic != null) {
				listData.clear();
				if (typeId==2) {
					listData.addAll(mGetDynamic.rzTemplateList);
				}else if(typeId==1){
					listData.addAll(mGetDynamic.tzTemplateList);
				}
				demandAdapter.setData(listData);
				demandAdapter.notifyDataSetChanged();
			}
		} else if (tag == EAPIConsts.demandReqType.demand_deletedtemplate) {
			demandLv.startRefresh();
			if (null != dialog) {
				dialog.dismiss();
			}
		}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		if (temapleBroad != null) {
			this.unregisterReceiver(temapleBroad);
		}
		super.onDestroy();
	}
}
