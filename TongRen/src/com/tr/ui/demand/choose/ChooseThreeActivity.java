package com.tr.ui.demand.choose;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.db.DemandDBManager;
import com.tr.model.demand.Metadata;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.utils.common.ViewHolder;

/**
 * @ClassName: ChooseThreeActivity.java
 * @author fxtx
 * @Date 2015年3月9日 上午11:48:41
 * @Description: 三级选择界面第三页
 */
public class ChooseThreeActivity extends JBaseActivity {
	private TextView titleTv;
	private XListView chooseXListView;
	private List<Metadata> listData = new ArrayList<Metadata>();// 数据
	private ChooseAdapter chooseAdapter;
	private Metadata meta; // 当前项数据
	private boolean isMultiSelect;
	private MenuItem saveItem;
	private int type;
	private DemandDBManager manager;
	private String people;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frg_demand_choose);
		manager = new DemandDBManager(this);
		getParams();
		initView();
	}

	private void initView() {
		/** 加载刷新控件 */
		chooseXListView = (XListView) this.findViewById(R.id.chooseXlv);
		/**
		 * 禁止刷新滑动效果
		 */
		chooseXListView.setPullLoadEnable(false);
		chooseXListView.setPullRefreshEnable(false);
		chooseAdapter = new ChooseAdapter(this);
		chooseXListView.setAdapter(chooseAdapter);
		chooseXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = position - 1;
				Metadata meta = listData.get(index);
				boolean isSelect = meta.isSelect;
				if (index == 0 && isMultiSelect) {
					// 点击全部
					saveItem.setTitle("保存");
					ChooseThreeActivity.this.meta.childs.clear();// 清除三级选中字段
					meta.isSelect = (!isSelect);
					initAllSelect(meta.isSelect);
					chooseAdapter.notifyDataSetChanged();

					/*if (!isMultiSelect) {
						// 单选模式情况下
						// 调用保存并关闭
						ChooseThreeActivity.this.meta
								.selectNum = ChooseDataUtil.METADATA_SELECT_ALL;
						ChooseThreeActivity.this.meta.childs.clear();
						closeActivity();
					} else {
						chooseAdapter.notifyDataSetChanged();
					}*/
				} else {
					if (isMultiSelect) {// 多选情况
						saveItem.setTitle("保存");
						meta.isSelect=!isSelect;
						listData.get(0).isSelect=isAllSelect();
						// 取消自己的选中效果
						chooseAdapter.notifyDataSetChanged();
					} else {// 单选情况
						meta.isSelect = true;
						ChooseThreeActivity.this.meta
								.selectNum = ChooseDataUtil.METADATA_SELECT_MULTI;
						ChooseThreeActivity.this.meta.addChildsOnly(meta);
						closeActivity();
					}
				}
			}
		});
	}

	private void initAllSelect(boolean isAll) {
		if (isAll) { // 全部选择
			if (isMultiSelect) {
				for (Metadata data : listData) {
					meta.selectNum = ChooseDataUtil.METADATA_SELECT_ALL;
					data.isSelect = true;
					if (data.id != null) // 添加
						meta.childs.add(data);
				}
			}
		} else {
			// 全部不选择
			if (isMultiSelect) {
				for (Metadata data : listData) {
					data.isSelect=false;
					meta.selectNum = ChooseDataUtil.METADATA_SELECT_NO;
				}
			}
		}

	}

	private void getParams() {
		Intent intent = getIntent();
		meta = (Metadata) intent
				.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA);
		isMultiSelect = intent.getBooleanExtra(ENavConsts.DEMAND_CHOOSE_MULTI,
				true);
		type = intent.getIntExtra(ENavConsts.DEMAND_CHOOSE_TYEP, -1);
		if (titleTv != null) {
			if(type == ChooseDataUtil.CHOOSE_type_Trade){
				titleTv.setText(meta.name);
			}else{
				if (meta.name.contains("洲")) {
					titleTv.setText("选择国家");
				}else{
					titleTv.setText("选择地区");
				}
			}
			
		}
		listData = meta.childs;
		if (meta.selectNum == 2) { // 全部为全选效果
			for (Metadata data : listData) {
				data.isSelect =true;
			}
		}
		getDBData(type, meta.id);
		boolean isAll = false;
		if (meta.selectNum == ChooseDataUtil.METADATA_SELECT_ALL) {// 判断上一级
																		// 是否全选择
			isAll = true;
			if (isMultiSelect) {
				for (Metadata data : listData) {
					data.isSelect = true;
				}
			}
		} else if (meta.selectNum  == ChooseDataUtil.METADATA_SELECT_MULTI) { // 多选情况
			for (Metadata data : meta.childs) { // 当前选中的二级列表
				for (Metadata data1 : listData) { // 当前展示的二级列表
					if (data1.id.equals(data.id)) { // 两个id 相等
						data1.isSelect=true;// 选中状态
					}
				}
			}
		}
		if (isMultiSelect) {
			listData.add(0, new Metadata("全部", isAll)); // 二级情况的时候 多选时 显示 全部
		} 
		people = intent.getStringExtra(ENavConsts.PEOPLE_ID);
	}

	/**
	 * 查询数据库获取数据
	 * 
	 * @param type
	 * @param id
	 */
	private void getDBData(int type, String id) {
		switch (type) {
		case ChooseDataUtil.CHOOSE_type_OutInvestType:
			listData = manager.queryInvestType(id);
			break;
		case ChooseDataUtil.CHOOSE_type_InInvestType:
			listData = manager.queryInvestType(id);
			break;
		case ChooseDataUtil.CHOOSE_type_Area:
			listData = manager.queryArea(id);
			break;
		case ChooseDataUtil.CHOOSE_type_Trade: // 行业
			listData = manager.queryInvestType(id);
			break;
		}

	}

	class ChooseAdapter extends BaseAdapter {
		private Context context;
		private boolean isCheck; // 是否能进行编辑多选

		public ChooseAdapter(Context context) {
			this.context = context;
		}

		public boolean isCheck() {
			return isCheck;
		}

		public void setCheck(boolean isCheck) {
			this.isCheck = isCheck;
		}

		@Override
		public int getCount() {
			return listData == null ? 0 : listData.size();
		}

		@Override
		public Metadata getItem(int position) {
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
						R.layout.demand_choose_item, null);
			}
			
			TextView nameTv = ViewHolder.get(convertView, R.id.nameTv);
			TextView contextTv = ViewHolder.get(convertView, R.id.contextTv);
			ImageView rightCb = ViewHolder.get(convertView, R.id.rightCb);
			ImageView rightIv = ViewHolder.get(convertView, R.id.rightIv);
			rightIv.setVisibility(View.GONE);
			Metadata meta = listData.get(position);
			rightCb.setVisibility(View.VISIBLE);
			nameTv.setText(meta.name);
			rightCb.setVisibility(View.VISIBLE);
			if (isMultiSelect) {// 多选
				contextTv.setText("");
				if (meta.isSelect) { // 选中
					rightCb.setImageResource(R.drawable.demand_me_need_checkbox_activated);
				} else { // 没有选中
					rightCb.setImageResource(R.drawable.demand_me_need_checkbox_default);
				}
			} else {// 单选
				rightCb.setVisibility(View.GONE);
				if (meta.isSelect) {
					contextTv.setText(meta.name);
				}
			}
			return convertView;
		}
	}

	private void closeActivity() {
		Intent intent = new Intent();
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_DATA, meta);
		intent.putExtra(ENavConsts.PEOPLE_ID, people);
		ChooseThreeActivity.this.setResult(RESULT_OK, intent);
		ChooseThreeActivity.this.finish();
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
		titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // 按下ActionBar上的返回键回到上一页
			this.finish();
			overridePendingTransition(R.anim.demand_in_from_left,R.anim.demand_out_from_right);
			break;
		case R.id.chooseSave:
			// 完成
			if(saveItem.getTitle().length()>0){
			listData.remove(0);// 移除第一项选择的
			isItemSelect();
			closeActivity();
			overridePendingTransition(R.anim.demand_in_from_left,R.anim.demand_out_from_right);
			}
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_choose, menu);
		saveItem = menu.findItem(R.id.chooseSave);
		saveItem.setTitle("");
		return true;
	}

	/**
	 * 判断当前列表是否全部选中
	 * 
	 * @return
	 */
	public boolean isAllSelect() {
		Metadata data;
		for (int i = 1; i < listData.size(); ++i) {
			data = listData.get(i);
			if (data.isSelect!= true) {
				return false;
			}
		}
		return true;
	}

	public void isItemSelect() {
		meta.childs.clear();// 清除三级选中字段
		if (listData.size() == 0) {
			return;
		}
		int size = 0;
		boolean isSelect = false;
		List<Metadata> temp = new ArrayList<Metadata>();
		for (Metadata data : listData) {
			if (data.isSelect == true) {
				size++;
				temp.add(data);
				isSelect = true;
			}
		}
		if (size == listData.size()) { // 全选效果
			meta.selectNum = ChooseDataUtil.METADATA_SELECT_ALL;// 全部选择
		} else if (isSelect == true) {
			meta.selectNum =ChooseDataUtil.METADATA_SELECT_MULTI;
			meta.childs =temp;// 当前选择的第二项
		} else {
			meta.selectNum=ChooseDataUtil.METADATA_SELECT_NO;
		}
	}
}
