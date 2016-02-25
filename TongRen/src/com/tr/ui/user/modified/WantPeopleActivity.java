package com.tr.ui.user.modified;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ConnectionsMini;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.relationship.SIMContactActivity;
import com.utils.common.ApolloUtils;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/** 手机人脉&推荐人脉
 * 
 * @author gushi */
public class WantPeopleActivity extends JBaseFragmentActivity implements IBindData {

	private Button uploadingCellphoneAddressBookBtn;
	private ListView mainLv;
	private WantConnectionsAdapter wantConnectionsAdapter;
	private CheckBox allcheckBox;
	private TextView addFriendBtn;
	private Context context;
	private ArrayList<ConnectionsMini> connectionsMiniList;
	/** 选中的用户列表 */
	private ArrayList<ConnectionsMini> selectedConnectionsMiniList;
	private boolean IsCheckAll;
	private MenuItem  skipMenuItem;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "金桐", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_want_people);
		context = this;

		uploadingCellphoneAddressBookBtn = (Button) findViewById(R.id.uploadingCellphoneAddressBookBtn);
		uploadingCellphoneAddressBookBtn.setOnClickListener(mOnClickListener);

		mainLv = (ListView) findViewById(R.id.mainLv);
		wantConnectionsAdapter = new WantConnectionsAdapter(getContext(), connectionsMiniList);
		mainLv.setAdapter(wantConnectionsAdapter);
		mainLv.setOnItemClickListener(mOnItemClickListener);

		allcheckBox = (CheckBox) findViewById(R.id.allcheckBox);
		allcheckBox.setOnClickListener(mOnClickListener);

		addFriendBtn = (TextView) findViewById(R.id.addFriendBtn);
		addFriendBtn.setOnClickListener(mOnClickListener);

		
		ConnectionsReqUtil.doPushPeopleList(getContext(), this, null);
		showLoadingDialog();
	}

	public Context getContext() {
		return context;
	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (uploadingCellphoneAddressBookBtn == v) {
				ENavigate.startSIMContactActivity(getContext(), SIMContactActivity.TYPE_INVITE);
			}
			else if (allcheckBox == v) {
				if(connectionsMiniList != null & connectionsMiniList.size() > 0){
					for (ConnectionsMini  connectionsMini : connectionsMiniList ){
						connectionsMini.setSelected(!IsCheckAll);
					}
					IsCheckAll = !IsCheckAll;
					wantConnectionsAdapter.notifyDataSetChanged();
				}

			}
			else if (addFriendBtn == v) {
				selectedConnectionsMiniList = getSelectedConnectionsMiniList();
				ArrayList<String> listuserId = new ArrayList<String>();
				for(ConnectionsMini connectionsMini : selectedConnectionsMiniList) {
					listuserId.add(connectionsMini.getId());
				}
				if(null!=listuserId&&listuserId.size()>0){
					ConnectionsReqUtil.doAddFriends(context, WantPeopleActivity.this, listuserId, null);
					showLoadingDialog();
				}
			}
		}
	};
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			WantConnectionsAdapter  wantConnectionsAdapter = (WantConnectionsAdapter)parent.getAdapter();
			ConnectionsMini  connectionsMini = wantConnectionsAdapter.getItem(position);
			boolean selected = !connectionsMini.isSelected();
			connectionsMini.setSelected(selected);
			wantConnectionsAdapter.notifyDataSetChanged();
			if(selected){
				if(checkIsAllSelected()){
					IsCheckAll = true;
					allcheckBox.setChecked(true);
				}
			}
			else {
				IsCheckAll = false;
				allcheckBox.setChecked(false);
			}
		}
	};
	
	private boolean checkIsAllSelected(){
		for (ConnectionsMini  connectionsMini2 : connectionsMiniList ){
			if (!connectionsMini2.isSelected()){
				return false;
			}
		}
		return true;
	}
	
	private ArrayList<ConnectionsMini> getSelectedConnectionsMiniList(){
		ArrayList<ConnectionsMini> selectedConnectionsMiniList = new ArrayList<ConnectionsMini>();
		for (ConnectionsMini  connectionsMini2 : connectionsMiniList ){
			if (connectionsMini2.isSelected()){
				selectedConnectionsMiniList.add(connectionsMini2);
			}
		}
		return selectedConnectionsMiniList;
	}
	
	

	/** 推荐联系人adapter */
	class WantConnectionsAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<ConnectionsMini> mConnectionsMiniList;

		public WantConnectionsAdapter() {
			super();
		}

		public WantConnectionsAdapter(Context context, ArrayList<ConnectionsMini> connectionsMiniList) {
			super();
			this.mContext = context;
			if (mConnectionsMiniList == null) {
				this.mConnectionsMiniList = new ArrayList<ConnectionsMini>();
			}
			else {
				this.mConnectionsMiniList = connectionsMiniList;
			}
		}

		public ArrayList<ConnectionsMini> getConnectionsMiniList() {
			return mConnectionsMiniList;
		}

		public void setmConnectionsMiniList(ArrayList<ConnectionsMini> connectionsMiniList) {
			this.mConnectionsMiniList = connectionsMiniList;
		}

		@Override
		public int getCount() {
			return mConnectionsMiniList.size();
		}

		@Override
		public ConnectionsMini getItem(int position) {
			return mConnectionsMiniList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String MSG = "getView()";

			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();

				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_item_want_people, null);

				holder.organizationNameTV = (TextView) convertView.findViewById(R.id.organizationNameTV);
				holder.peopleNameTv = (TextView) convertView.findViewById(R.id.peopleNameTv);
				holder.companyName = (TextView) convertView.findViewById(R.id.companyName);
				holder.companyJob = (TextView) convertView.findViewById(R.id.companyJob);
				holder.imageIv = (ImageView) convertView.findViewById(R.id.imageIv);
				holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
				holder.friendStateTv = (TextView) convertView.findViewById(R.id.friendStateTv);
				holder.addFriendBtn = (TextView) convertView.findViewById(R.id.addFriendBtn);
				holder.addFriendBtn.setVisibility(View.GONE);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			 ConnectionsMini connectionsMini =  mConnectionsMiniList.get(position);
			 
			 holder.checkBox.setChecked(connectionsMini.isSelected());
			 
			if (!StringUtils.isEmpty(connectionsMini.getImage())) {
				try {
					ApolloUtils.getImageFetcher(context).loadHomeImage(connectionsMini.getImage(), holder.imageIv);
				}
				catch (Exception e) {
					Log.e(TAG, MSG + " e = " + e);
				}
			}
			if(connectionsMini.getType()==1){//type=1人脉 type=2组织
				holder.peopleNameTv.setVisibility(View.VISIBLE);
				holder.companyJob.setVisibility(View.VISIBLE);
				holder.organizationNameTV.setVisibility(View.GONE);
			}else{
				holder.peopleNameTv.setVisibility(View.GONE);
				holder.companyJob.setVisibility(View.GONE);
				holder.organizationNameTV.setVisibility(View.VISIBLE);
				
			}
			String nameString= connectionsMini.getName();
			if(!TextUtils.isEmpty(nameString)){
				nameString = nameString.replace("null", "");
			}
			holder.organizationNameTV.setText(nameString);
			holder.peopleNameTv.setText(nameString);
			String companyNameString = connectionsMini.getCompanyName();
			if(!TextUtils.isEmpty(companyNameString)){
				companyNameString = companyNameString.replace("null", "");
			}
			holder.companyName.setText(companyNameString);
			String companyJobString = connectionsMini.getCompanyJob();
			if(!TextUtils.isEmpty(companyJobString)){
				companyJobString = companyJobString.replace("null", "");
			}
			holder.companyJob.setText(companyJobString);
			holder.friendStateTv.setText(1 == connectionsMini.getFriendState() ? "等待确认" : "");

			return convertView;
		}

		public final class ViewHolder {
			public CheckBox checkBox;
			public ImageView imageIv;
			public TextView organizationNameTV;//组织名称
			public TextView peopleNameTv;
			public TextView companyName;
			public TextView companyJob;
			public TextView friendStateTv;
			public TextView addFriendBtn;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuItem  = menu.add(0, MENU_ITEM_ID_SKIP, 0, "跳过");
		skipMenuItem = menu.add(0, Menu.NONE, 0, "跳过");
		skipMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// 这里要写跳转到框架 然后结束自己.
		if(skipMenuItem == item){
			ENavigate.startMainActivity((Activity)getContext());
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void bindData(int tag, Object object) {
		
		if (tag == EAPIConsts.concReqType.pushPeopleList) {
			dismissLoadingDialog();
			if(object!=null){
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				ArrayList<ConnectionsMini> userConnectionsList = (ArrayList<ConnectionsMini>) dataMap.get("listUser");
				
				if(userConnectionsList != null ){
					connectionsMiniList = userConnectionsList;
				}
				wantConnectionsAdapter.setmConnectionsMiniList(connectionsMiniList);
				wantConnectionsAdapter.notifyDataSetChanged();
			}
			else {
				
			}
		
		}
		else if (tag == EAPIConsts.concReqType.addFriends) {
			dismissLoadingDialog();
			if(object!=null){
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				boolean success = (Boolean) dataMap.get("success");
				if (success) {
					Toast.makeText(context, "加好友申请已发出,等待对方通过", 0).show();
					for (ConnectionsMini connectionsMini2 : selectedConnectionsMiniList) {
						connectionsMini2.setFriendState(1);
					}
					wantConnectionsAdapter.notifyDataSetChanged();
				}
			}
			else {
				
			}
			
		}
		
	}

}
