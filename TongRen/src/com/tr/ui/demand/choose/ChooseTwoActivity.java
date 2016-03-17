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
 * @ClassName: ChooseTowActivity.java
 * @author fxtx
 * @Date 2015年3月9日 上午11:48:41
 * @Description: 三级选择界面第二页
 */
public class ChooseTwoActivity extends JBaseActivity {
	private TextView titleTv;
	private XListView chooseXListView;
	private List<Metadata> listData = new ArrayList<Metadata>();// 数据
	private ChooseAdapter chooseAdapter;
	private Metadata meta; // 当前项数据
	private int tempIndex;
	private boolean isMultiSelect;
	private MenuItem saveItem;
	private DemandDBManager manager;
	private int type;
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
				Metadata data = chooseAdapter.getItem(index);
				boolean isSelect = data.isSelect;
				if (index == 0 && isMultiSelect) { // 选择了 第一项
					// 点击全部
					saveItem.setTitle("保存");
					meta.childs.clear();
					data.isSelect = !isSelect;
					initAllSelect(data.isSelect);
					chooseAdapter.notifyDataSetChanged();
					/*if (!isMultiSelect) {
						// 单选模式情况下
						// 调用保存并关闭
						ChooseTwoActivity.this.meta.selectNum = ChooseDataUtil.METADATA_SELECT_ALL;
						ChooseTwoActivity.this.meta.childs.clear();
						closeActivity();
					}*/
				} else {
					listData.get(0).isSelect = isAllSelect();
					tempIndex = index; // 获取当前选择的id
					Intent intent = new Intent(ChooseTwoActivity.this,
							ChooseThreeActivity.class);
					intent.putExtra(ENavConsts.PEOPLE_ID, people);
					intent.putExtra(ENavConsts.DEMAND_CHOOSE_DATA, data);
					intent.putExtra(ENavConsts.DEMAND_CHOOSE_MULTI,
							isMultiSelect);
					intent.putExtra(ENavConsts.DEMAND_CHOOSE_TYEP, type);
					
					startActivityForResult(intent,
							ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT);
					overridePendingTransition(R.anim.demand_in_from_right,
							R.anim.demand_out_from_left);
				}
			}
		});
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
			if (data.selectNum != ChooseDataUtil.METADATA_SELECT_ALL) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取上一界面传递过来的信息
	 */
	private void getParams() {
		Intent intent = getIntent();
		meta = (Metadata) intent
				.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA);
		isMultiSelect = intent.getBooleanExtra(ENavConsts.DEMAND_CHOOSE_MULTI,
				true); // 默认为多选
		type = intent.getIntExtra(ENavConsts.DEMAND_CHOOSE_TYEP,
				ChooseDataUtil.CHOOSE_TYPE_DEFAULT);
		if (titleTv != null) {
			if(type == ChooseDataUtil.CHOOSE_type_InInvestType||type == ChooseDataUtil.CHOOSE_type_OutInvestType){
				titleTv.setText(meta.name);
			}else{
				if ("国外".equals(meta.name)) {
					titleTv.setText("选择洲");
				}else{
					titleTv.setText("选择城市");
				}
			}
			
		}
		getDBData(type, meta.id);
		boolean isAll = false;
		if (meta.selectNum == ChooseDataUtil.METADATA_SELECT_ALL) {// 判断上一级
																	// 是否全选择
			isAll = true;
			initAllSelect(true);
		} else if (meta.selectNum == ChooseDataUtil.METADATA_SELECT_MULTI) { // 多选情况
			for (Metadata data : meta.childs) { // 当前选中的二级列表
				for (Metadata data1 : listData) { // 当前展示的二级列表
					if (data1.id.equals(data.id)) { // 两个id 相等
						data1.childs = data.childs;// 当前子级状态
						data1.selectNum = data.selectNum;// 选中状态
					}
				}
			}
		}
		if (isMultiSelect) {
			listData.add(0, new Metadata("全部", isAll)); // 二级情况的时候 多选时 显示 全部
		} 
		people = intent.getStringExtra(ENavConsts.PEOPLE_ID);
	}

	private void initAllSelect(boolean isAll) {
		if (isAll) { // 全部选择
			if (isMultiSelect) {
				meta.selectNum = ChooseDataUtil.METADATA_SELECT_ALL;
				for (Metadata data : listData) {
					data.selectNum = ChooseDataUtil.METADATA_SELECT_ALL; // 全部选择
					if (data.id != null)
						meta.childs.add(data);
				}
			}
		} else {
			// 全部不选择
			if (isMultiSelect) {
				meta.childs.clear();
				meta.selectNum = ChooseDataUtil.METADATA_SELECT_NO;
				for (Metadata data : listData) {
					data.selectNum = ChooseDataUtil.METADATA_SELECT_NO; // 全部选择

				}
			}
		}

	}

	/**
	 * 获取当前要显示的数据
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
			ImageView rightIv = ViewHolder.get(convertView, R.id.rightIv);
			ImageView rightCb = ViewHolder.get(convertView, R.id.rightCb);
			Metadata meta = listData.get(position);
			nameTv.setText(meta.name);
			if (position == 0) {
				rightIv.setVisibility(View.GONE);
				contextTv.setText("");
				if (isMultiSelect) {// 多选
					rightCb.setVisibility(View.VISIBLE);
					if (meta.isSelect) { // 选中
						rightCb.setImageResource(R.drawable.demand_me_need_checkbox_activated);
					} else { // 没有选中
						rightCb.setImageResource(R.drawable.demand_me_need_checkbox_default);
					}
				} else {// 单选
					if (meta.isSelect) {
						contextTv.setText(meta.name);
					}
					rightIv.setVisibility(View.VISIBLE);
					rightCb.setVisibility(View.GONE);
				}
			} else {
				contextTv.setText(ChooseDataUtil.dataToMetadata(meta));
				rightCb.setVisibility(View.GONE);
				rightIv.setVisibility(View.VISIBLE);
			}
			return convertView;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT
				&& resultCode == RESULT_OK) {
			Metadata meta = (Metadata) data
					.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA);
			if (isMultiSelect) { // 多选效果
				listData.set(tempIndex, meta);// 这是当前三级的选择效果 将三级的效果刷新到整个信息中来
				listData.get(0).isSelect = isAllSelect();
				setMetaChilds(meta);
				chooseAdapter.notifyDataSetChanged();
				saveItem.setTitle("保存");
				// 多
			} else {
				// 单选
				this.meta.selectNum = ChooseDataUtil.METADATA_SELECT_MULTI;
				this.meta.addChildsOnly(meta);
				closeActivity();
			}
		}
	}

	private void setMetaChilds(Metadata data) {
		// 将数据全部清除了
		Metadata deleteMeta = null;
		for (Metadata data1 : this.meta.childs) { // 当前二级信息
			if (data1.id.equals(data.id)) { // 两个id 相等
				if (data.selectNum != ChooseDataUtil.METADATA_SELECT_NO) {
					data1.childs = data.childs;// 当前子级状态
					data1.selectNum = data.selectNum;// 选中状态
					return;
				} else {
					deleteMeta = data1;
				}
			}
		}
		if (data.selectNum != ChooseDataUtil.METADATA_SELECT_NO) {
			this.meta.childs.add(data);// 没有选择过 就将他进行添加到已经选择中
		} else {
			this.meta.childs.remove(deleteMeta);// 删除
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
		mActionBar.setTitle("所选的类型名称");
		titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);
	}

	/**
	 * 保存选择的信息 并关闭Activity
	 */
	private void closeActivity() {
		Intent intent = new Intent();
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_DATA, meta);
		intent.putExtra(ENavConsts.PEOPLE_ID, people);
		ChooseTwoActivity.this.setResult(RESULT_OK, intent);
		ChooseTwoActivity.this.finish();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // 按下ActionBar上的返回键回到上一页
			this.finish();
			overridePendingTransition(R.anim.demand_in_from_left,
					R.anim.demand_out_from_right);
			break;
		case R.id.chooseSave:
			// 保存
			if (saveItem.getTitle().length() > 0) {
				listData.remove(0);
				isItemSelect();
				closeActivity();
				overridePendingTransition(R.anim.demand_in_from_left,
						R.anim.demand_out_from_right);
			}
			break;
		}
		return true;
	}

	/**
	 * 作用，判断三级是否全部选择。 二级是否多选和全选。决定一级的对象 的选择状态 全选，多选。单选
	 * 
	 * @return
	 */
	public void isItemSelect() {
		if (listData.size() == 0) {
			return;
		}
		if (meta.childs.size() == listData.size()) {
			// 全部二级都有选择
			meta.selectNum = ChooseDataUtil.METADATA_SELECT_ALL;
			// 便利当前二级对象 是否有未全选的状态值
			for (Metadata metad : meta.childs) {
				if (metad.selectNum == ChooseDataUtil.METADATA_SELECT_MULTI) {
					meta.selectNum = ChooseDataUtil.METADATA_SELECT_MULTI;
					return;// 多选
				}
			}
			meta.childs.clear();
		} else if (meta.childs.size() > 0) {
			meta.selectNum = ChooseDataUtil.METADATA_SELECT_MULTI;
		} else {
			meta.selectNum = ChooseDataUtil.METADATA_SELECT_NO;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_choose, menu);
		saveItem = menu.findItem(R.id.chooseSave);
		return true;
	}
}
