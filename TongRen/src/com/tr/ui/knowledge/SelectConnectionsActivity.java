package com.tr.ui.knowledge;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.db.ConnectionsCacheData;
import com.tr.db.ConnectionsDBManager;
import com.tr.db.DBHelper;
import com.tr.model.obj.Connections;
import com.tr.model.user.OrganizationMini;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.image.LoadImage;
import com.utils.string.StringUtils;

/***
 * 
 * @author gushi
 * 
 */
public class SelectConnectionsActivity extends JBaseActivity {

	public static final String TAG = "SelectConnectionsActivity";

	private EditText searchNameEt;
	private TextView searchTv;
	private ExpandableListView contactElv;
	private ArrayList<Connections> connectionsList;
	private ConnectionsExpandableListAdapter connectionsExpandableListAdapter;

	private Context context;
	private ArrayList<String> groupList;
	private ArrayList<ArrayList<Connections>> childrenConnectionsList;
	private ArrayList<ArrayList<Connections>> childrenConnectionsListTemp = new ArrayList<ArrayList<Connections>>();
	private ArrayList<Connections> childrenPlatformBeanList;
	private ArrayList<Connections> connectionsData= new ArrayList<Connections>();
	public static ConnectionsDBManager connectionsDBManager = null;
	private ArrayList<Connections> mPeoPleConnections = new ArrayList<Connections>();
	private ArrayList<Connections> mOrganizationConnections = new ArrayList<Connections>();
	private ArrayList<Connections> childrenPlatformBeanList2;
	private ArrayList<Connections> checkedConnectionsList = new ArrayList<Connections>();

	private int friendPeoPleIndex = 0;
	private int friendPeoPleSize = 1000;
	private int friendPeoPleTotal;
	private int friendOrganizationIndex = 0;
	private int friendOrganizationSize = 1000;
	private int friendOrganizationTotal;

	private String keyWord;
	private ArrayList<String> ClickConnectionsIds = new ArrayList<String>();;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "选择",
				false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_select_contact);
		initComponent();
		// initSimulateData();
		initData();

	}

	private void initComponent() {
		searchNameEt = (EditText) findViewById(R.id.searchNameEt);
		searchNameEt.addTextChangedListener(mTextWatcher);
		searchTv = (TextView) findViewById(R.id.searchTv);
		contactElv = (ExpandableListView) findViewById(R.id.contactElv);
		connectionsExpandableListAdapter = new ConnectionsExpandableListAdapter(
				context, groupList, childrenConnectionsList);
		contactElv.setAdapter(connectionsExpandableListAdapter);
		contactElv.setOnChildClickListener(mChildClickListener);
	}

	private void initSimulateData() {
		String MSG = "initSimulateData()";

		groupList = new ArrayList<String>();
		groupList.add("金桐脑推荐");
		groupList.add("人");
		groupList.add("组织");

		childrenConnectionsList = new ArrayList<ArrayList<Connections>>();

		childrenPlatformBeanList2 = new ArrayList<Connections>();

		Connections connections1 = new Connections();
		connections1.type = Connections.type_org;
		connections1.setID(0 + "");
		connections1.setName("金桐脑");
		connections1.setmSourceFrom("智能大数据平台");
		childrenPlatformBeanList2.add(connections1);

		Connections connections2 = new Connections();
		connections2.type = Connections.type_org;
		connections2.setID(-1 + "");
		connections2.setName("全平台");
		connections2.setmSourceFrom("全金桐网");
		childrenPlatformBeanList2.add(connections2);
		childrenConnectionsList.add(childrenPlatformBeanList2);

		ArrayList<Connections> childrenPeopleBeanList = new ArrayList<Connections>();
		for (int i = 0; i < 5; i++) {
			Connections connections = new Connections();
			connections.type = Connections.type_persion;
			connections.setName("人名" + i);
			childrenPeopleBeanList.add(connections);
		}
		childrenConnectionsList.add(childrenPeopleBeanList);

		ArrayList<Connections> childrenOrgBeanList = new ArrayList<Connections>();
		for (int i = 0; i < 5; i++) {
			Connections connections = new Connections();
			connections.type = Connections.type_org;
			connections.setName("组织名" + i);
			childrenOrgBeanList.add(connections);
		}
		childrenConnectionsList.add(childrenOrgBeanList);

		Log.i(TAG, MSG + " childrenConnectionsList.size = "
				+ childrenConnectionsList.size());

		connectionsExpandableListAdapter.setGroupList(groupList);
		connectionsExpandableListAdapter
				.setChildrenConnectionsList(childrenConnectionsList);

		connectionsExpandableListAdapter.notifyDataSetChanged();
	}

	private void initData() {
		String MSG = "initData()";

		SharedPreferences sp = context.getSharedPreferences(
				EConsts.share_firstLoginGetConnections, context.MODE_PRIVATE);
		String tableName = sp.getString(EConsts.share_itemUserTableName, "");
		if (StringUtils.isEmpty(tableName)) {
			connectionsDBManager = new ConnectionsDBManager(context,
					DBHelper.TABLE_APP_CONNECTIONS);
		} else {
			if (tableName.equals(DBHelper.TABLE_APP_CONNECTIONS)) {
				connectionsDBManager = new ConnectionsDBManager(context,
						DBHelper.TABLE_APP_CONNECTIONS);
			} else if (tableName.equals(DBHelper.TABLE_APP_CONNECTIONS_BACK)) {
				connectionsDBManager = new ConnectionsDBManager(context,
						DBHelper.TABLE_APP_CONNECTIONS_BACK);
			} else {
				connectionsDBManager = new ConnectionsDBManager(context,
						DBHelper.TABLE_APP_CONNECTIONS);
			}
		}

		groupList = new ArrayList<String>();
		groupList.add("金桐脑推荐");
		groupList.add("人");
		groupList.add("组织"); // 产品脑袋有问题 这个功能设计了 后来不要了

		childrenConnectionsList = new ArrayList<ArrayList<Connections>>();

		childrenPlatformBeanList = new ArrayList<Connections>();

		Connections connections1 = new Connections();
		connections1.type = Connections.type_org;
		connections1.setID(0 + "");
		connections1.setName("金桐脑");
		connections1.setImage(EAPIConsts.AVATAR_GinTongNao_URL);
		connections1.setmSourceFrom("智能大数据平台");
		childrenPlatformBeanList.add(connections1);

		Connections connections2 = new Connections();
		connections2.type = Connections.type_org;
		connections2.setID(-1 + "");
		connections2.setName("全平台");
		connections2.setmSourceFrom("全金桐网");
		childrenPlatformBeanList.add(connections2);
		childrenConnectionsList.add(childrenPlatformBeanList);

		mPeoPleConnections = connectionsDBManager.query(keyWord,
				Connections.type_persion, true,
				OrganizationMini.type_fri_friend, friendPeoPleIndex,
				friendPeoPleSize);
		friendPeoPleTotal = connectionsDBManager.queryCount(keyWord,
				Connections.type_persion, true,
				OrganizationMini.type_fri_friend);
		childrenConnectionsList.add(mPeoPleConnections);

		// 产品脑袋有问题 这个功能设计了 后来不要了
		mOrganizationConnections = connectionsDBManager.query(keyWord,
				Connections.type_org, true, OrganizationMini.type_fri_friend,
				friendOrganizationIndex, friendOrganizationSize);
		friendOrganizationTotal = connectionsDBManager.queryCount(keyWord,
				Connections.type_org, true, OrganizationMini.type_fri_friend);
		childrenConnectionsList.add(mOrganizationConnections);

		connectionsExpandableListAdapter.setGroupList(groupList);
		connectionsExpandableListAdapter
				.setChildrenConnectionsList(childrenConnectionsList);
		
		for (Connections iterable_element : mPeoPleConnections) {
			connectionsData.add(iterable_element);
		}
		for (Connections iterable_element : mOrganizationConnections) {
			connectionsData.add(iterable_element);
		}
		for (Connections iterable_element : childrenPlatformBeanList) {
			connectionsData.add(iterable_element);
		}
		childrenConnectionsListTemp.add(connectionsData);
		
		connectionsExpandableListAdapter.notifyDataSetChanged();

		expandAllList();

		Intent intent = getIntent();
		checkedConnectionsList = (ArrayList<Connections>) intent
				.getSerializableExtra("listConnections");
		if (checkedConnectionsList.size() > 0) {
			for (Connections connections : checkedConnectionsList) {

				for (int i = 0; i < childrenPlatformBeanList.size(); i++) {
					Connections connections3 = childrenPlatformBeanList.get(i);
					if (connections3.getId().equals(connections.getId())) {
						connections3.setmIsFocuse(true);
						ClickConnectionsIds.add(connections3.getId());
					}
				}
				for (int i = 0; i < mPeoPleConnections.size(); i++) {
					Connections connections3 = mPeoPleConnections.get(i);
					if (connections3.getId().equals(connections.getId())) {
						connections3.setmIsFocuse(true);
						ClickConnectionsIds.add(connections3.getId());
					}
				}
				for (int i = 0; i < mOrganizationConnections.size(); i++) {
					Connections connections3 = mOrganizationConnections.get(i);
					if (connections3.getId().equals(connections.getId())) {
						connections3.setmIsFocuse(true);
						ClickConnectionsIds.add(connections3.getId());
					}
				}
			}
		}
		connectionsExpandableListAdapter.notifyDataSetChanged();
		// expandAllList();

	}

	ArrayList<Connections> mOrganizationConnectionsTemp = new ArrayList<Connections>();
	ArrayList<Connections> mPeoPleConnectionsTemp = new ArrayList<Connections>();

	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void afterTextChanged(Editable s) { // 关键字改变后更新列表
			keyWord = s.toString();
			// onRefresh();
			if (keyWord.contains("'")) {
				keyWord = keyWord.replaceAll("'", "");
			}

			mPeoPleConnectionsTemp.clear();
			for (int i = 0; i < mPeoPleConnections.size(); i++) {
				for (int j = 0; j < ClickConnectionsIds.size(); j++) {
					if (ClickConnectionsIds.get(j).equals(
							mPeoPleConnections.get(i).getId())
							&& !mPeoPleConnections.get(i).isFocuse()) {
						mPeoPleConnectionsTemp.add(mPeoPleConnections.get(i));
					}
				}
			}
			mPeoPleConnections.clear();

			mPeoPleConnections.addAll(connectionsDBManager.query(keyWord,
					Connections.type_persion, true,
					OrganizationMini.type_fri_friend, friendPeoPleIndex,
					friendPeoPleSize));
			if (mPeoPleConnections != null && !mPeoPleConnections.isEmpty()
					&& !ClickConnectionsIds.isEmpty()) {
				for (int i = 0; i < mPeoPleConnections.size(); i++) {
					for (int j = 0; j < ClickConnectionsIds.size(); j++) {
						if (ClickConnectionsIds.get(j).equals(
								mPeoPleConnections.get(i).getId())) {
							mPeoPleConnections.get(i).setmIsFocuse(true);
						}
						if (mPeoPleConnectionsTemp.contains(mPeoPleConnections
								.get(i))) {
							mPeoPleConnections.get(i).setmIsFocuse(true);
						}
					}
				}
			}
			friendPeoPleTotal = connectionsDBManager.queryCount(keyWord,
					Connections.type_persion, true,
					OrganizationMini.type_fri_friend);

			mOrganizationConnectionsTemp.clear();
			for (int i = 0; i < mOrganizationConnections.size(); i++) {
				for (int j = 0; j < ClickConnectionsIds.size(); j++) {
					if (ClickConnectionsIds.get(j).equals(
							mOrganizationConnections.get(i).getId())
							&& !mOrganizationConnections.get(i).isFocuse()) {
						mOrganizationConnectionsTemp
								.add(mOrganizationConnections.get(i));
					}
				}
			}
			mOrganizationConnections.clear();

			mOrganizationConnections.addAll(connectionsDBManager.query(keyWord,
					Connections.type_org, true,
					OrganizationMini.type_fri_friend, friendOrganizationIndex,
					friendOrganizationSize));
			if (mOrganizationConnections != null
					&& !mOrganizationConnections.isEmpty()
					&& !ClickConnectionsIds.isEmpty()) {
				for (int i = 0; i < mOrganizationConnections.size(); i++) {
					for (int j = 0; j < ClickConnectionsIds.size(); j++) {
						if (ClickConnectionsIds.get(j).equals(
								mOrganizationConnections.get(i).getId())) {
							mOrganizationConnections.get(i).setmIsFocuse(true);
						}
						if (mOrganizationConnectionsTemp
								.contains(mOrganizationConnections.get(i))) {
							mOrganizationConnections.get(i).setmIsFocuse(true);
						}
					}
				}
			}
			friendOrganizationTotal = connectionsDBManager.queryCount(keyWord,
					Connections.type_org, true,
					OrganizationMini.type_fri_friend);

			connectionsExpandableListAdapter.notifyDataSetChanged();

			// expandAllList();
		}
	};

	private void expandAllList() {
		String MSG = "expandAllList()";

		// int count = groupList.size();
		int count = contactElv.getCount();
		for (int i = 0; i < count; i++) {
			contactElv.expandGroup(i);
		}
	}

	class ConnectionsExpandableListAdapter extends BaseExpandableListAdapter {

		private Context context;
		private ArrayList<String> groupList;
		private ArrayList<ArrayList<Connections>> childrenConnectionsList;
		private String keyWord;
		private ArrayList<Connections> keyWordChildrenConnectionsList = new ArrayList<Connections>();

		public ConnectionsExpandableListAdapter(Context context,
				ArrayList<String> groupList,
				ArrayList<ArrayList<Connections>> childrenConnectionsList) {
			super();
			this.context = context;
			if (groupList != null) {
				this.groupList = groupList;
			} else {
				this.groupList = new ArrayList<String>();
			}
			if (childrenConnectionsList != null) {
				this.childrenConnectionsList = childrenConnectionsList;
			} else {
				this.childrenConnectionsList = new ArrayList<ArrayList<Connections>>();
			}

		}

		public Context getContext() {
			return context;
		}

		public void setContext(Context context) {
			this.context = context;
		}

		public ArrayList<String> getGroupList() {
			return groupList;
		}

		public void setGroupList(ArrayList<String> groupList) {
			this.groupList = groupList;
		}

		public ArrayList<ArrayList<Connections>> getChildrenConnectionsList() {
			return childrenConnectionsList;
		}

		public void setChildrenConnectionsList(
				ArrayList<ArrayList<Connections>> childrenConnectionsList) {
			this.childrenConnectionsList = childrenConnectionsList;

		}

		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// if (TextUtils.isEmpty(keyWord)) {
			return childrenConnectionsList.get(groupPosition).size();
			// }else{
			// for (int i = 0; i <
			// childrenConnectionsList.get(groupPosition).size(); i++) {
			// if
			// (childrenConnectionsList.get(groupPosition).get(i).getName().contains(keyWord))
			// {
			// keyWordChildrenConnectionsList
			// .add(childrenConnectionsList.get(groupPosition).get(i));
			// }
			// }
			//
			// return keyWordChildrenConnectionsList.size();
			// }

		}

		@Override
		public String getGroup(int groupPosition) {
			return groupList.get(groupPosition);
		}

		@Override
		public Connections getChild(int groupPosition, int childPosition) {
			// if (TextUtils.isEmpty(keyWord)) {
			return childrenConnectionsList.get(groupPosition)
					.get(childPosition);
			// }else{
			// for (int i = 0; i <
			// childrenConnectionsList.get(groupPosition).size(); i++) {
			// if
			// (childrenConnectionsList.get(groupPosition).get(i).getName().contains(keyWord))
			// {
			// keyWordChildrenConnectionsList
			// .add(childrenConnectionsList.get(groupPosition).get(i));
			// }
			// }
			//
			// return keyWordChildrenConnectionsList.get(childPosition);
			// }

		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String MSG = "getGroupView()";

			GroupViewHolder groupViewHolder = null;
			if (convertView == null) {
				groupViewHolder = new GroupViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.connections_expandableist_group_item, null);

				groupViewHolder.groupTitleTv = (TextView) convertView
						.findViewById(R.id.groupTitleTv);
				groupViewHolder.groupIndicatorIv = (ImageView) convertView
						.findViewById(R.id.groupIndicatorIv);

				convertView.setTag(groupViewHolder);

			} else {
				groupViewHolder = (GroupViewHolder) convertView.getTag();
			}

			String groupTitleStr = groupList.get(groupPosition);
			groupViewHolder.groupTitleTv.setText(groupTitleStr);
			if (isExpanded) {
				groupViewHolder.groupIndicatorIv
						.setBackgroundResource(R.drawable.arrow_down_grey);
			} else {
				groupViewHolder.groupIndicatorIv
						.setBackgroundResource(R.drawable.arrow_right_grey);
			}

			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder childViewHolder = null;
			int childSize = childrenConnectionsList.get(groupPosition).size();

			if (convertView == null) {
				childViewHolder = new ChildViewHolder();
				// if( childPosition < childSize ){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.connections_expandableist_chren_item, null);

				childViewHolder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkBox);
				childViewHolder.imageIv = (ImageView) convertView
						.findViewById(R.id.imageIv);
				childViewHolder.nameTv = (TextView) convertView
						.findViewById(R.id.nameTv);
				childViewHolder.descriptionTv = (TextView) convertView
						.findViewById(R.id.descriptionTv);

				convertView.setTag(childViewHolder);

				// }
				// else if ( childPosition == childSize ){
				// convertView = inflater.inflate(R.layout.pulldown_footer,
				// null);
				// childViewHolder.mvTextView = (TextView)
				// convertView.findViewById(R.id.pulldown_footer_text);
				// childViewHolder.mvProgressBar = (View)
				// convertView.findViewById(R.id.pulldown_footer_loading);
				//
				// convertView.setTag(childViewHolder);
				// }

			} else {
				childViewHolder = (ChildViewHolder) convertView.getTag();
			}

			Connections connections = getChild(groupPosition, childPosition);
			String nameStr = connections.getName();
			boolean isFocuse = connections.ismIsFocuse();

			if (groupPosition == 0) {
				String mSourceFrom = connections.getmSourceFrom();
				childViewHolder.descriptionTv.setVisibility(View.VISIBLE);
				childViewHolder.descriptionTv.setText(mSourceFrom);
			} else {
				childViewHolder.descriptionTv.setVisibility(View.GONE);
			}
			if (connections.getmSourceFrom().equals("全金桐网"))
				childViewHolder.imageIv
						.setImageResource(R.drawable.all_pltoform_bg);
			else
				ImageLoader.getInstance().displayImage(connections.getImage(),
						childViewHolder.imageIv, LoadImage.mDefaultHead);

			childViewHolder.nameTv.setText(nameStr);
			childViewHolder.checkBox.setChecked(isFocuse);

			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		class GroupViewHolder {
			TextView groupTitleTv;
			ImageView groupIndicatorIv;
		}

		class ChildViewHolder {
			CheckBox checkBox;
			ImageView imageIv;
			TextView nameTv;
			TextView descriptionTv;

			TextView mvTextView;
			View mvProgressBar;
		}

	}

	private OnChildClickListener mChildClickListener = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {

			ConnectionsExpandableListAdapter connectionsExpandableListAdapter = (ConnectionsExpandableListAdapter) (parent
					.getExpandableListAdapter());
			Connections connections = connectionsExpandableListAdapter
					.getChild(groupPosition, childPosition);
			if (connections.ismIsFocuse()) {
				connections.setmIsFocuse(false);
				if (!ClickConnectionsIds.isEmpty()
						&& ClickConnectionsIds.contains(connections.getId())) {
					ClickConnectionsIds.remove(connections.getId());
				}
			} else {
				connections.setmIsFocuse(true);
				if ("-1".equals(connections.getId())) {
					ClickConnectionsIds.clear();
				}else if(ClickConnectionsIds.contains("-1")){//其他人
					ClickConnectionsIds.remove("-1");
				}
				ClickConnectionsIds.add(connections.getId());
			}
			// 全平台与其他人互斥
			if ("-1".equals(connections.getId()) && connections.ismIsFocuse()) {
				setBooleanForConnection(false);
				connections.setmIsFocuse(true);
				if (!ClickConnectionsIds.isEmpty()
						&& ClickConnectionsIds.contains(connections.getId())) {
					ClickConnectionsIds.add(connections.getId());
				}
			} else if (connections.ismIsFocuse()) {
				for (Connections item : childrenPlatformBeanList) {
					if ("-1".equals(item.getId())) {
						item.setmIsFocuse(false);
					}
				}
			}
			connectionsExpandableListAdapter.notifyDataSetChanged();

			return true;
		}

		private void setBooleanForConnection(boolean bool) {
			for (Connections item : childrenPlatformBeanList) {
				item.setmIsFocuse(bool);
			}
			for (Connections item : mPeoPleConnections) {
				item.setmIsFocuse(bool);
			}
			for (Connections item : mOrganizationConnections) {
				item.setmIsFocuse(bool);
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.finish, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String MSG = "onOptionsItemSelected()";

		switch (item.getItemId()) {
		case R.id.create_ok:
			checkedConnectionsList.clear();
			
//			ChildrenConnectionsList: for (ArrayList<Connections> childConnectionsList : childrenConnectionsListTemp) {
				for (Connections connections : connectionsData) {
						if (ClickConnectionsIds.contains(connections
								.getJtContactMini().getId())) {
							checkedConnectionsList.add(connections);
						} else if (ClickConnectionsIds.contains(connections
								.getOrganizationMini().getId())) {
							checkedConnectionsList.add(connections);
						} else if (ClickConnectionsIds
								.contains(connections.getId())) {
							checkedConnectionsList.add(connections);
						}
//				}
			}
			Intent data = new Intent();

			data.putExtra("listConnections", checkedConnectionsList);
			setResult(Activity.RESULT_OK, data);

			finish();

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
