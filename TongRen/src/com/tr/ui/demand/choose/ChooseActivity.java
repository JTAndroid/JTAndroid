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
 * @ClassName: ChooseActivity.java
 * @author fxtx
 * @Date 2015年3月9日 上午11:48:41
 * @Description: 三级选择界面第一页 默认为多选效果
 */
public class ChooseActivity extends JBaseActivity {
	private TextView titleTv;
	private XListView chooseXListView;
	private List<Metadata> listData = new ArrayList<Metadata>();// 数据
	private ChooseAdapter chooseAdapter;
	private int tempIds = -1;
	private int tempIndex;
	private boolean isMultiSelect;// 多选控制
	private MenuItem saveItem;
	private DemandDBManager manager;
	private int type;// 具体参数 参考 ChooseDataUtil.CHOOSE_type;


	private ArrayList<Metadata> tempMetadata; // 当前选择的数据
	private String people_id;


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
				int index = position - 1; // XListView 的缺陷 下标获取必须减少一个
				Metadata data = chooseAdapter.getItem(index);
				tempIndex = index;
				Intent intent = new Intent(ChooseActivity.this,
						ChooseTwoActivity.class);
				
				intent.putExtra(ENavConsts.DEMAND_CHOOSE_DATA, data); // 将当前选择的对象传递过去
				intent.putExtra(ENavConsts.DEMAND_CHOOSE_MULTI, isMultiSelect); // 单选
				intent.putExtra(ENavConsts.PEOPLE_ID, people_id);							// 还是多选
				intent.putExtra(ENavConsts.DEMAND_CHOOSE_TYEP, type);
				startActivityForResult(intent,
						ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT);
				overridePendingTransition(R.anim.demand_in_from_right,
						R.anim.demand_out_from_left);
			}
		});
	}

	private void getParams() {
		Intent intent = getIntent();

		isMultiSelect = intent.getBooleanExtra(ENavConsts.DEMAND_CHOOSE_MULTI,
				true); // 默认为多选
		type = intent.getIntExtra(ENavConsts.DEMAND_CHOOSE_TYEP,
				ChooseDataUtil.CHOOSE_TYPE_DEFAULT);// 默认
		if (titleTv != null) {
			titleTv.setText(intent
					.getStringExtra(ENavConsts.DEMAND_CHOOSE_TITLE)); // 标题
		}
		tempMetadata = (ArrayList<Metadata>) intent
				.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA);
		if (tempMetadata == null) {
			tempMetadata = new ArrayList<Metadata>();
		}
		// 获取当前信息数据
		getDBData(type);
		// 判断 当前是否有显示的数据
		for (Metadata data : listData) {
			Metadata b = select(data);
			if (b != null) {
				data.childs = b.childs;
				data.selectNum = b.selectNum;
			}
		}
		people_id = intent.getStringExtra(ENavConsts.PEOPLE_ID);
	}

	private Metadata select(Metadata dd) {
		tempIds = -1;
		for (Metadata b : tempMetadata) {
			tempIds++;
			if (b.id.equals(dd.id)) {
				// 两个相等
				return b;
			}
		}
		return null;
	}

	/**
	 * 查询数据库中的第一级信息 投资信息为1 融资信息为8 行业信息为15
	 * 
	 * @param type
	 *            :区分是查询行业，还是地区，还是类型
	 */
	private void getDBData(int type) {
		switch (type) {
		case ChooseDataUtil.CHOOSE_type_OutInvestType:
			listData = manager.queryInvestType("1");
			break;
		case ChooseDataUtil.CHOOSE_type_InInvestType:
			listData = manager.queryInvestType("8");
			break;
		case ChooseDataUtil.CHOOSE_type_Area:
			listData = manager.queryArea();
			break;
		case ChooseDataUtil.CHOOSE_type_Trade: // 行业
			listData = manager.queryInvestType("15");
			break;
		default:
			listData = new ArrayList<Metadata>();
			break;
		}
	}

	

	/**
	 * 数据适配器
	 * 
	 * @author Administrator
	 *
	 */
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
			Metadata meta = listData.get(position);
			nameTv.setText(meta.name);
			// 判断 当前这一项是否有选择过
			// 将显示的状态显示出来
			contextTv.setText(ChooseDataUtil.dataToMetadata(meta)); // 遍历信息
			return convertView;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT
				&& resultCode == RESULT_OK) {
			Metadata meta = (Metadata) data
					.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA);
			if (isMultiSelect) {
				Metadata temp = select(meta);
				if (meta.selectNum == ChooseDataUtil.METADATA_SELECT_NO) {
					if (temp != null)
						tempMetadata.remove(temp);// 删除对象
				} else {
					if (temp != null)
						tempMetadata.set(tempIds, meta);
					else
						tempMetadata.add(meta);
				}
				listData.set(tempIndex, meta);
				chooseAdapter.notifyDataSetChanged();
				saveItem.setTitle("保存");
			} else {
				tempMetadata.clear();// 清除整个数据
				tempMetadata.add(meta);
				closeActivity();
			}
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
		titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // 按下ActionBar上的返回键回到上一页
			this.finish();
			break;
		case R.id.chooseSave:
			// 完成
			if (item.getTitle().length() > 0) {
				closeActivity();
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

	private void closeActivity() {
		Intent intent = new Intent();
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_DATA, tempMetadata);
		intent.putExtra(ENavConsts.PEOPLE_ID, people_id);	
		ChooseActivity.this.setResult(RESULT_OK, intent);
		ChooseActivity.this.finish();

	}
}
