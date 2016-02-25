package com.tr.ui.connections.revision20150122;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.string;
import android.content.Context;
import android.os.Bundle;
import android.os.MessageQueue.IdleHandler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.obj.Connections;
import com.tr.model.obj.Connections;
import com.tr.model.obj.Connections;
import com.tr.model.obj.Connections;
import com.tr.ui.base.JBaseFragment;
import com.utils.common.ApolloUtils;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

public class PushPeopleFragment extends JBaseFragment implements IBindData {
	
	public static final String TAG = "PushPeopleFragment";
	
	
	/** 邀请 状态 */
	public static final int type_invite = 1;
	/** 添加 状态 */
	public static final int type_add = 2;
	

	private int type = -1;
	private ListView mainLv;
	private WantConnectionsAdapter wantConnectionsAdapter;
	private Context context;
	private ArrayList<Connections> mConnectionsList;

	private CheckBox allcheckBox;
	private TextView addBtn;
	private TextView titleTv;
	/** 选中的用户列表 */
	private ArrayList<Connections> selectedConnectionsList;
	private boolean IsCheckAll;

	public PushPeopleFragment(int type, ArrayList<Connections> connectionsList) {
		super();
		this.type = type;
		mConnectionsList = connectionsList;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_push_people, container, false);

		titleTv = (TextView) view.findViewById(R.id.titleTv);
		if( type_invite == type ){
			titleTv.setText("他们还没有加入，马上邀请！");
		}
		else if ( type_add == type ){
			titleTv.setText("他们也在金桐，赶紧加好友吧！");
		}
		
		allcheckBox = (CheckBox) view.findViewById(R.id.allcheckBox);
		allcheckBox.setOnClickListener(mOnClickListener);

		addBtn = (TextView) view.findViewById(R.id.addBtn);
		addBtn.setOnClickListener(mOnClickListener);
		if( type_invite == type ){
			addBtn.setText(" 邀请");
		}
		else if ( type_add == type ){
			addBtn.setText("+好友");
		}

		mainLv = (ListView) view.findViewById(R.id.mainLv);
		wantConnectionsAdapter = new WantConnectionsAdapter(context, mConnectionsList);
		mainLv.setAdapter(wantConnectionsAdapter);
		mainLv.setOnItemClickListener(mOnItemClickListener);
		return view;
	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (allcheckBox == v) {
				if (mConnectionsList != null & mConnectionsList.size() > 0) {
					for (Connections connections : mConnectionsList) {
						connections.setSelected(!IsCheckAll);
					}
					IsCheckAll = !IsCheckAll;
					wantConnectionsAdapter.notifyDataSetChanged();
				}
			}
			else if (addBtn == v) {
				selectedConnectionsList = getSelectedConnectionsList();
				if( type_invite == type ){
					ArrayList<String> listMobile = new ArrayList<String>();
					for (Connections connections : selectedConnectionsList) {
						listMobile.add(connections.getMobilePhone());
					}
					ConnectionsReqUtil.doSendSMS(getActivity(), PushPeopleFragment.this, listMobile, null);
				}
				else if ( type_add == type ){
					ArrayList<String> listuserId = new ArrayList<String>();
					for (Connections connections : selectedConnectionsList) {
						listuserId.add(connections.getId());
					}
					ConnectionsReqUtil.doAddFriends(getActivity(), PushPeopleFragment.this, listuserId, null);
				}
				showLoadingDialog();
			}
		}
	};

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			WantConnectionsAdapter wantConnectionsAdapter = (WantConnectionsAdapter) parent.getAdapter();
			Connections connections = wantConnectionsAdapter.getItem(position);
			boolean selected = !connections.isSelected();
			connections.setSelected(selected);
			wantConnectionsAdapter.notifyDataSetChanged();
			if (selected) {
				if (checkIsAllSelected()) {
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

	private boolean checkIsAllSelected() {
		for (Connections connections : mConnectionsList) {
			if (!connections.isSelected()) {
				return false;
			}
		}
		return true;
	}

	private ArrayList<Connections> getSelectedConnectionsList() {
		ArrayList<Connections> selectedConnectionsList = new ArrayList<Connections>();
		for (Connections connections : mConnectionsList) {
			if (connections.isSelected()) {
				selectedConnectionsList.add(connections);
			}
		}
		return selectedConnectionsList;
	}

	/** 推荐联系人adapter */
	class WantConnectionsAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<Connections> mConnectionsList;

		public WantConnectionsAdapter() {
			super();
		}

		public WantConnectionsAdapter(Context context, ArrayList<Connections> connectionsList) {
			super();
			this.mContext = context;
			if (connectionsList == null) {
				this.mConnectionsList = new ArrayList<Connections>();
			}
			else {
				this.mConnectionsList = connectionsList;
			}
		}

		public ArrayList<Connections> getConnectionsList() {
			return mConnectionsList;
		}

		public void setConnectionsList(ArrayList<Connections> connectionsList) {
			this.mConnectionsList = connectionsList;
		}

		@Override
		public int getCount() {
			return mConnectionsList.size();
		}

		@Override
		public Connections getItem(int position) {
			return mConnectionsList.get(position);
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

			Connections connections = mConnectionsList.get(position);

			holder.checkBox.setChecked(connections.isSelected());

			if (!StringUtils.isEmpty(connections.getImage())) {
				try {
					ApolloUtils.getImageFetcher(context).loadHomeImage(connections.getImage(), holder.imageIv);
				}
				catch (Exception e) {
					Log.e(TAG, MSG + " e = " + e);
				}
			}
			String peopleNameString = connections.getLastName() + connections.getName();
			if(!TextUtils.isEmpty(peopleNameString)){
				peopleNameString = peopleNameString.replace("null", "");
			}
			holder.peopleNameTv.setText(peopleNameString);
			String companyString = connections.getCompany();
			if(!TextUtils.isEmpty(companyString)){
				companyString = companyString.replace("null", "");
			}
			holder.companyName.setText(companyString);
			holder.companyJob.setText(connections.getCareer());
			holder.friendStateTv.setText(1 == connections.getFriendState() ? "等待确认" : "");

			return convertView;
		}

		public final class ViewHolder {
			public CheckBox checkBox;
			public ImageView imageIv;
			public TextView peopleNameTv;
			public TextView companyName;
			public TextView companyJob;
			public TextView friendStateTv;
			public TextView addFriendBtn;
		}
	}

	@Override
	public void bindData(int tag, Object object) {

		if (tag == EAPIConsts.concReqType.sendSMS) {
			String MSG = "tag == EAPIConsts.concReqType.sendSMS";
			
			dismissLoadingDialog();
			if (object != null) {
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				boolean success = (Boolean) dataMap.get("success");
				String msg = (String) dataMap.get("msg");
				Log.i(TAG, MSG + " success = " + success);
				if (success) {
					selectedConnectionsListChangeFriendState();
				}
				else {
				}
				
				if(msg != null){
					Toast.makeText(getActivity(), msg, 0).show();
				}
			}
		}
		else if (tag == EAPIConsts.concReqType.addFriends) {
			dismissLoadingDialog();
			if(object!=null){
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				boolean success = (Boolean) dataMap.get("success");
				String msg = (String) dataMap.get("msg");
				if (success) {
					selectedConnectionsListChangeFriendState();
				}
				else {
				}
				
				if(msg != null){
					Toast.makeText(getActivity(), msg, 0).show();
				}
				
			}
			else {
				
			}
			
		}
	}

	private void selectedConnectionsListChangeFriendState() {
//		for (Connections connections : selectedConnectionsList) {
//			connections.setFriendState(1);
//			connections.setSelected(false);
//		}
		for (int i = 0; i < selectedConnectionsList.size(); i++) {
			Connections connections = selectedConnectionsList.get(i);
			connections.setFriendState(1);
			connections.setSelected(false);
		}
		wantConnectionsAdapter.notifyDataSetChanged();
	}

}
