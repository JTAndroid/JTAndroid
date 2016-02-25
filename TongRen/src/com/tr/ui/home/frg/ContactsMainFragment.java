package com.tr.ui.home.frg;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.db.ConnectionsCacheData;
import com.tr.db.ConnectionsDBManager;
import com.tr.model.obj.Connections;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.home.frg.FrgConnections.ContactAdapter;

/**
 * 主页——通讯录
 * 
 * @author zhongshan
 * 
 */
public class ContactsMainFragment extends JBaseFragment implements OnScrollListener, OnClickListener {
	private Context mContext;
	private ListView contactsLv;
	private EditText searchEt;
	private RelativeLayout contactsHeaderLayout;
	private RelativeLayout contactsFooterLayout;
	private ArrayList<Connections> listConnections = new ArrayList<Connections>();
	private ConnectionsDBManager connectionsDBManager;
	private ConnectionsCacheData cnsCacheData;
	private boolean initListDataOver;
	private ContactsAdapter contactAdapter;
	private boolean isLoadMore;
	private int total;
	private TextView newConectionsItemTv;
	private TextView   companyConnectionItemTv;
	private TextView attentionCompanyItemTv;
	private TextView directoryItemTv;
	private TextView lableItemTv;
	private TextView conectionsCountTv;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View contactsFragmentLayout = inflater.inflate(R.layout.fragment_contactsmain_layout, null);
		searchEt = (EditText) contactsFragmentLayout.findViewById(R.id.EditTextSearch);
		contactsLv = (ListView) contactsFragmentLayout.findViewById(R.id.contactsLv);
		contactsHeaderLayout = (RelativeLayout) View.inflate(mContext, R.layout.contacts_header_layout,null);
		contactsFooterLayout = (RelativeLayout) View.inflate(mContext, R.layout.contacts_footer_layout,null);
		conectionsCountTv = (TextView) contactsFooterLayout.findViewById(R.id.conectionsCountTv);
		
		newConectionsItemTv = (TextView) contactsHeaderLayout.findViewById(R.id.newConectionsItemTv);
		companyConnectionItemTv = (TextView) contactsHeaderLayout.findViewById(R.id.companyConnectionItemTv);
		attentionCompanyItemTv = (TextView) contactsHeaderLayout.findViewById(R.id.attentionCompanyItemTv);
		directoryItemTv = (TextView) contactsHeaderLayout.findViewById(R.id.directoryItemTv);
		lableItemTv = (TextView) contactsHeaderLayout.findViewById(R.id.lableItemTv);
		
		contactsLv.addHeaderView(contactsHeaderLayout);
		contactsLv.addFooterView(contactsFooterLayout);
		return contactsFragmentLayout;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		setLisenter();
		initData();
	}

	private void setLisenter() {
		newConectionsItemTv.setOnClickListener(this);
		companyConnectionItemTv.setOnClickListener(this);
		attentionCompanyItemTv.setOnClickListener(this);
		directoryItemTv.setOnClickListener(this);
		lableItemTv.setOnClickListener(this);
		contactsLv.setOnScrollListener(this);
	}
	private void initData() {
		connectionsDBManager = ConnectionsDBManager.buildConnectionsDBManager(getActivity());
		cnsCacheData = new ConnectionsCacheData(connectionsDBManager);
		cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_FRIEND_PEOPLE);
		// 设置适配器的操作
				// 如果数据库写入操作完成，那么就初始化列表数据，否则，就在广播接收到的时候初始化
				if (App.connectionDataBaseWriteOver && !initListDataOver) {
					// 将页面已经进行初始化，置为true;
					initListDataOver = true;
					initListViewData();
				}
		
	};
	
	private void initListViewData() {
		listConnections.clear();
		ArrayList<Connections> connections = cnsCacheData.getDate(0, 20);
		total = connections.size();
		listConnections.addAll(connections);
		contactAdapter = new ContactsAdapter(mContext, listConnections);
		contactAdapter.setListConnections(listConnections);
		contactsLv.setAdapter(contactAdapter);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (view.getLastVisiblePosition() == view.getCount() - 1) {
			loadMoreData();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		if (totalItemCount!=0) {
//			if (firstVisibleItem+visibleItemCount == totalItemCount&&total!=totalItemCount) {//到底部
//				loadMoreData();
//			}
//		}
	}
	/**
	 * 加载更多
	 */
	private void loadMoreData() {
		synchronized (new Object()) {
			if (!isLoadMore) {
				isLoadMore = true;
				// 起始位置
				int startIndex = 0;
				startIndex = total;
				if (total == 0) {
					startIndex = 0;
					listConnections.clear();
				}
				ArrayList<Connections> connections = cnsCacheData.getDate(
						startIndex, 20);
				if (connections.size()==0) {
					isLoadMore = false;
					conectionsCountTv.setText(listConnections.size()+"位联系人");
					return;
				}
				// 当前查询到的总数
				total += connections.size();
				listConnections.addAll(connections);
				contactAdapter.setListConnections(listConnections);
				isLoadMore = false;
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newConectionsItemTv:
			showToast("newConectionsItemTv");
			
			break;
		case R.id.companyConnectionItemTv:
			showToast("companyConnectionItemTv");
			
			break;
		case R.id.attentionCompanyItemTv:
			showToast("attentionCompanyItemTv");
			
			break;
		case R.id.directoryItemTv:
			showToast("directoryItemTv");
			
			break;
		case R.id.lableItemTv:
			showToast("lableItemTv");
			break;
		}
	}
}
