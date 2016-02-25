package com.tr.ui.home.frg;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.db.ConnectionsCacheData;
import com.tr.db.ConnectionsDBManager;
import com.tr.db.DBHelper;
import com.tr.image.ImageLoader;
import com.tr.model.im.ChatDetail;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTContactMini;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.home.MainActivity;
import com.tr.ui.relationship.GroupListActivity;
import com.tr.ui.relationship.MyFriendAndPeopleListActivity;
import com.tr.ui.relationship.NewConnectionActivity;
import com.tr.ui.widgets.SideBar;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.LoadImage;
import com.utils.string.StringUtils;
import com.utils.time.Util;

/**
 * @Description:   我的好友(人脉)  已弃用
 * @author         hanqi
 */
public class FrgMyFriendAndPeople extends JBaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnScrollListener{
	public static final String TAG = "FrgConnections2";
	
	//新关系消息和组
	private com.tr.ui.widgets.BasicListView lvContact;//内容list
	private RelativeLayout loadConnectionsWaitView;
	public static ContactAdapter contactAdapter=null;
	private SideBar indexBar;//拖动的字母bar
	private WindowManager mWindowManager;
	private TextView mDialogText;//中间提示的text
	/** all datas*/
	public static ConnectionsDBManager connectionsDBManager=null;
	public static ConnectionsCacheData cnsCacheData;
	ArrayList<Connections> mOrgConnections=new ArrayList<Connections>();
	/** 新关系 群组列表按钮 */
	LinearLayout im_new_contactlist;
	LinearLayout im_groupslist;
	public TextView cnsSizeTagNew;
	public TextView cnsSizeTagGroup;
	private SwipeRefreshLayout swipeLayout;
	
	private String source ;
	
	/** 父activity */
	private MyFriendAndPeopleListActivity mActivity;
	
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String MSG = "onCreateView()";
		Log.i(TAG, MSG);
		View view = inflater.inflate(R.layout.home_frg_connections_list2, container, false);
		mWindowManager = (WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE);
		/** 加载刷新控件*/
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.mystart_frg_requirement_swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
		
		return view;
	}

	
	boolean isRefreshing = false;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity  = (MyFriendAndPeopleListActivity) getActivity();
		
		connectionsDBManager = ConnectionsDBManager.buildConnectionsDBManager(getActivity());
		cnsCacheData=new ConnectionsCacheData(connectionsDBManager);
		// 这里设置联系人的类型
		cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_FRIEND_ALL);
		
		findView(this.getView());
	}
	
	boolean isFirst=true;
	@Override
	public void onResume(){
		super.onResume();
		String MSG = "onResume()";
		
		 SharedPreferences sp = getActivity().getSharedPreferences(EConsts.share_firstLoginGetConnections, getActivity().MODE_PRIVATE);
		 int firstLogin=sp.getInt(EConsts.share_itemFirstLogin, 0);
		 
		if(firstLogin==0){//第一次登陆
			loadConnectionsWaitView.setVisibility(View.VISIBLE);
			//swipeLayout.setVisibility(View.GONE);
			if(!isRefreshing){
				isRefreshing=true;
				startGetConnections();
			}
			isFirst=false;
			Editor editor = sp.edit();
			editor.putInt(EConsts.share_itemFirstLogin,1);
			editor.commit();
		}
		else if(isFirst){
			isFirst=false;
//			if(cnsCacheData.size()<50){
				if(!isRefreshing){
					isRefreshing=true;
					startGetConnections();
				}
//			}
		}
	}
	
	
	private  MyFriendAndPeopleListActivity getContent() {
		return mActivity;
	}
	
	private void startGetConnections(){
		 JSONObject jb=new JSONObject();
		try {
			jb.put("type", "0");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ConnectionsReqUtil.doGetConnectionsList(FrgMyFriendAndPeople.this.getActivity(), iBindData, jb, null);
	}
	
	IBindData iBindData=new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			if(!isResumed()){
				return;
			}
			// 邀请加入金铜网
			if(tag==EAPIConsts.concReqType.im_inviteJoinGinTong){
				if(getActivity()!=null){
					((MyFriendAndPeopleListActivity)getActivity()).dismissLoadingDialog();
				}
				if(object!=null){
					String sur=(String)object;
					if(sur.equals("true")){
						//addfriend.setVisibility(View.INVISIBLE);
						showToast("邀请邮件已发送");
						return;
					}
				}
				showToast("邀请邮件以发送失败");
				
			}
			// 请求加好友
			else if(tag==EAPIConsts.concReqType.im_addFriend){
				if(getActivity()!=null){
					((MyFriendAndPeopleListActivity)getActivity()).dismissLoadingDialog();
				}
				if(object!=null){
					String sur=(String)object;
					if(sur.equals("true")){
						//addfriend.setVisibility(View.INVISIBLE);
						showToast("请求对方为好友，发送成功");
						return;
					}
				}
				showToast("请求对方为好友，发送失败");
				
			}
			// 获取联系人列表
			else if(tag==EAPIConsts.concReqType.CONNECTIONSLIST){
				
				if (object != null) {
					ArrayList<Connections> connArr = (ArrayList<Connections>) object;

					synchronized (connectionsDBManager) {
						// 确定tablename
						String tableName;
						Context context = connectionsDBManager.getContext();
						ConnectionsDBManager tempConnectionsDBManager = null;
						if (connectionsDBManager.getTableName().equals(DBHelper.TABLE_APP_CONNECTIONS)) {
							tempConnectionsDBManager = new ConnectionsDBManager(connectionsDBManager.getContext(), DBHelper.TABLE_APP_CONNECTIONS_BACK);
							tableName = DBHelper.TABLE_APP_CONNECTIONS_BACK;
						}
						else {
							tempConnectionsDBManager = new ConnectionsDBManager(context, DBHelper.TABLE_APP_CONNECTIONS);
							tableName = DBHelper.TABLE_APP_CONNECTIONS;
						}
						ConnectionsCacheData tempConnectionsCacheData = new ConnectionsCacheData(tempConnectionsDBManager);
						tempConnectionsCacheData.clearData();
						ArrayList<Connections> insertData = new ArrayList<Connections>();
						for (int i = 0; i < connArr.size(); i++) {
							insertData.add(connArr.get(i));
							if (insertData.size() >= 100) {
								tempConnectionsCacheData.insert(insertData);
								insertData.clear();
							}
						}
						if (insertData.size() != 0) {
							tempConnectionsCacheData.insert(insertData);
							insertData.clear();
						}

						cnsCacheData.setTableName(tableName);
						// FrgConnections.contactAdapter.dataChange();
						SharedPreferences sp = context.getSharedPreferences(EConsts.share_firstLoginGetConnections, Activity.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putString(EConsts.share_itemUserTableName, tableName);
						editor.commit();
					}

					loadConnectionsWaitView.setVisibility(View.GONE);
					swipeLayout.setRefreshing(false);
					if (getContent() != null) {
						getContent().dismissLoadingDialog();
					}

					if (object != null) {
						contactAdapter.dataChange();
						isRefreshing = false;
					}
					else {
						isRefreshing = false;
					}
					isRefreshing = false;
				}
				
			}
		}
	};

	
	@SuppressLint("HandlerLeak")
	private Handler mFilterHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:	{
				contactAdapter.dataChange();
			}
			default:
				break;
			}
		}
	};
	
	Runnable showComparator=new Runnable() {
		@Override
		public void run() {

    		mFilterHandler.sendEmptyMessage(2);
		}
	};
	
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			
		}else{
			
		}
	}

    private void findView(View view){
    	loadConnectionsWaitView=(RelativeLayout)view.findViewById(R.id.waitview);
    	lvContact = (com.tr.ui.widgets.BasicListView)view.findViewById(R.id.lvContact);
    	indexBar = (SideBar) view.findViewById(R.id.sideBar);  
//        indexBar.setListView(lvContact,cnsCacheData); 
        contactAdapter=new ContactAdapter(getActivity(), cnsCacheData);
        
        contactAdapter.dataChange();
        lvContact.setAdapter(contactAdapter);
		lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 != 0) {
					if (Util.user_simulation) {
						Connections connections = (Connections) arg1.getTag();
						if (connections.type == Connections.type_persion) {
							ENavigate.startUserDetailsActivity(
									FrgMyFriendAndPeople.this.getActivity(),
									connections.getId(),
									connections.isOnline(), 0);
						} else {
						}

					} else {
						Connections connections = (Connections) arg1.getTag();
						if (connections.type == Connections.type_persion) {
							// 发起聊天
							ChatDetail chatDetail = new ChatDetail();
							chatDetail.setThatID(connections.getId());
							chatDetail.setThatImage(connections.getImage());
							chatDetail.setThatName(connections.getName());
							ENavigate.startIMActivity(FrgMyFriendAndPeople.this.getActivity(), chatDetail);
						} 
						else {
						}
					}
				}
			}
		});
        mDialogText = (TextView) LayoutInflater.from(this.getActivity()).inflate(R.layout.im_relationcontactlist_position, null);
        mDialogText.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        indexBar.setTextView(mDialogText);
    }
    
    
     class GourpJoinAdapter extends BaseAdapter{
    	 ArrayList<Connections> mOrgConnections;
    	 public GourpJoinAdapter(ArrayList<Connections> mOrgConnections){
    		 this.mOrgConnections=mOrgConnections;
    	 }
		@Override
		public int getCount() {
			return mOrgConnections.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {	 
			arg1 = LayoutInflater.from(FrgMyFriendAndPeople.this.getActivity()).inflate(R.layout.im_relationcontactmain_item, null);
			Connections connections=mOrgConnections.get(arg0);
			 ((TextView)arg1.findViewById(R.id.contactitem_catalog)).setVisibility(View.GONE);
			 ((TextView)arg1.findViewById(R.id.imcontactname)).setText(connections.getName());
			 ((TextView)arg1.findViewById(R.id.imcontactcomefrom)).setText(connections.sourceFrom);
			 arg1.findViewById(R.id.yaoqoingcon).setVisibility(View.GONE);
			 ImageView icon = (ImageView)arg1.findViewById(R.id.contactitem_avatar_iv);
			 ImageLoader.load(icon, connections.getImage(), R.drawable.ic_default_avatar);
			 arg1.setTag(connections);
			return arg1;
		}
    	
    }
    public class ContactAdapter extends BaseAdapter  {  
    	private Context mContext;
    	private ConnectionsCacheData connectionsCacheData;
    	@SuppressWarnings("unchecked")
		public ContactAdapter(Context mContext,ConnectionsCacheData cnsCacheData){
    		this.mContext = mContext;
    		connectionsCacheData=cnsCacheData;
    		
    	}
    	
    	public void dataChange(){
    		//indexBar.init();
    		mOrgConnections=connectionsDBManager.queryJoinOrg();
    		if(mOrgConnections==null){
    			mOrgConnections=new ArrayList<Connections>();
    		}
    		connectionsCacheData.init();
    		notifyDataSetChanged();
    	}
    	
		@Override
		public int getCount() {
			return connectionsCacheData.size()+1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}
		
		public ConnectionsCacheData getConnectionsCacheData(){
			return connectionsCacheData;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Connections connections=null;
			String mNicks=null;
			if(position!=0){
				connections=connectionsCacheData.get(position-1);
				mNicks=connections.getName();
			}
			
			TextView tag;//标头
			TextView imcontactName;//名字
			TextView imcontactComeFrom;//来自
			ImageView icon;//头像
			ImageView icontag;//头像
			
			if(convertView==null||convertView.getId()==R.id.im_relationcontactmain_itemfirst){
				convertView=null;
			}
			if(position==0){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.im_relationcontactmain_itemfirst, null);
				cnsSizeTagNew=(TextView)convertView.findViewById(R.id.cnsSizeTagNew);
				cnsSizeTagGroup=(TextView)convertView.findViewById(R.id.cnsSizeTagGroup);
				
//				MyFriendAllActivity myFriendAllActivity=(MyFriendAllActivity)getActivity();
//				if(cnsSizeTagGroup!=null){
//					if(myFriendAllActivity.newGroupCount==0){
//						cnsSizeTagGroup.setVisibility(View.INVISIBLE);
//					}
//					else {
//						cnsSizeTagGroup.setVisibility(View.VISIBLE);
//						cnsSizeTagGroup.setText(myFriendAllActivity.newGroupCount+"");
//					}
//				}
				
//				if(cnsSizeTagNew!=null){
//					if(myFriendAllActivity.newConnectionsCount==0){
//						cnsSizeTagNew.setVisibility(View.INVISIBLE);
//					}
//					else {
//						cnsSizeTagNew.setVisibility(View.VISIBLE);
//						cnsSizeTagNew.setText(myFriendAllActivity.newConnectionsCount+"");
//					}
//				}
				
				if(mOrgConnections!=null&&mOrgConnections.size()>0){
					ListView dd=(ListView)convertView.findViewById(R.id.groupjoin);
					dd.setAdapter(new GourpJoinAdapter(mOrgConnections));
					dd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
//							Connections connections=(Connections)arg1.getTag();
////							Intent intent=new Intent(FrgConnections.this.getActivity(),ChatActivity.class);
////							FrgConnections.this.startActivity(intent);
//							ENavigate.startOrgDetailsActivity(FrgConnections.this.getActivity(),connections.mID,
//									 connections.isOnline(),0);
						}
					});
					convertView.findViewById(R.id.joinorgtitle).setVisibility(View.VISIBLE);
					EUtil.setListViewHeightBasedOnChildren(dd);	
				}else{
					convertView.findViewById(R.id.joinorgtitle).setVisibility(View.GONE);	
				}
					im_new_contactlist=(LinearLayout)convertView.findViewById(R.id.im_new_contactlist);
					im_new_contactlist.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
//							((MainActivity)getActivity()).changeCnsSizeTag(MainActivity.ITEM_NEWConnections);
							Intent intent =new Intent(getActivity(),NewConnectionActivity.class);
					    	getActivity().startActivity(intent);
						}
					});
					im_groupslist=(LinearLayout)convertView.findViewById(R.id.im_groupslist);
					im_groupslist.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
//							((MainActivity)getActivity()).changeCnsSizeTag(MainActivity.ITEM_GROUP);
							Intent intent =new Intent(getActivity(),GroupListActivity.class);
					    	getActivity().startActivity(intent);
						}
					});
			}else{
				if(convertView==null){
					convertView= LayoutInflater.from(mContext).inflate(R.layout.im_relationcontactmain_item, null);
				}
				tag = (TextView)convertView.findViewById(R.id.contactitem_catalog);
				imcontactName = (TextView)convertView.findViewById(R.id.imcontactname);
				imcontactComeFrom = (TextView)convertView.findViewById(R.id.imcontactcomefrom);
				icon = (ImageView)convertView.findViewById(R.id.contactitem_avatar_iv);
				icontag = (ImageView)convertView.findViewById(R.id.contactitem_icontag);
				LinearLayout yaoqingll=(LinearLayout)convertView.findViewById(R.id.yaoqoingcon);
//				Log.v("232","getName"+connections.getName() );
//				Log.v("232","isFriendState"+connections.isFriendState()+"" );
//				Log.v("232","isOnline"+connections.isOnline()+"");
				yaoqingll.setVisibility(View.VISIBLE);
				if(connections.isFriendState()!=JTContactMini.type_friend){
					if(connections.isOnline()){
						((ImageView)convertView.findViewById(R.id.yaoqingicon)).setImageResource(R.drawable.guanxi_jiahaoyou);
						((TextView)convertView.findViewById(R.id.yaoqingtxt)).setText("加为好友");
					}else{
						((ImageView)convertView.findViewById(R.id.yaoqingicon)).setImageResource(R.drawable.guanxi_yaoqing);
						((TextView)convertView.findViewById(R.id.yaoqingtxt)).setText("邀请加入");
					}
				}else{
					yaoqingll.setVisibility(View.GONE);
				}
				final Connections yaoqingConnections=connections;
				yaoqingll.setOnClickListener(new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
						if(yaoqingConnections.isOnline()){
							JSONObject jb=ConnectionsReqUtil.getReqNewFriend(yaoqingConnections.getId(), yaoqingConnections.type);
							ConnectionsReqUtil.doReqNewFriend(getActivity(), iBindData,jb , null);
							if(getActivity()!=null){
								((MainActivity)getActivity()).showLoadingDialog("正在联网");
							}
						}else{
							String mobile=yaoqingConnections.getMobilePhone();
							if(!StringUtils.isEmpty(mobile)){
								sendSMS(mobile);
							}else{
								String email=yaoqingConnections.getEmail();
								if(!StringUtils.isEmpty(email)){
									JSONObject jo=new JSONObject();
									JSONArray ja=new JSONArray();
									ja.put(email);
									try {
										jo.put("listEmail",ja);
									} catch (JSONException e) {
										e.printStackTrace();
									}
									if(jo!=null){
										ConnectionsReqUtil.getInviteJoinGinTong(getActivity(), iBindData, jo, null);
									}
								}else{
									if(yaoqingConnections.getmType()==Connections.type_org){
										showToast("该客户没有邮箱和电话，请完善客户资料");
									}else{
										showToast("该人脉没有邮箱和电话，请完善人脉资料");
									}
									
								}
							}
						}
						
					}
				});
				
				
				String lastCatalog=null;
				if(position!=1&&!StringUtils.isEmpty(mNicks)){
					String catalog = "";
				//	catalog=converterToFirstSpell(mNicks).substring(0, 1);
					catalog=connections.getCharName()+"";
					//Log.v("11", catalog);
					//Log.v("11", mConnections.get(position-2).getName());
					

						lastCatalog = connectionsCacheData.get(position-2).getCharName()+"";
					
					if(catalog.equals(lastCatalog)){
						tag.setVisibility(View.GONE);
					}else{
						tag.setVisibility(View.VISIBLE);
						tag.setText(catalog);
					}
				}else{
					tag.setVisibility(View.GONE);
				}
				
				imcontactName.setText(connections.getName());
				imcontactComeFrom.setText(connections.sourceFrom);
				
				if(!connections.isOnline()){
					icontag.setVisibility(View.VISIBLE);
				}else{
					icontag.setVisibility(View.GONE);
				}
//				ImageLoader.getInstance().cancelDisplayTask(icon);
//				ImageLoader.getInstance().displayImage(connections.getImage(), icon);
				
				if(connections.type==Connections.type_persion){
					com.utils.common.Util.initAvatarImage(mContext, icon, "", connections.getImage(), 0, 1);
				}else if(connections.type==Connections.type_org){
					com.utils.common.Util.initAvatarImage(mContext, icon, "", connections.getImage(), 0, 2);
				}
				// Log.d(TAG, connections.getImage());
			}
			convertView.setTag(connections);
			return convertView;
}
    	
		

 
    }
    

     /*
      * 发邀请短信
      */
     private void sendSMS(String smsnumber ) {  
		String str = App.getApp().getAppData().mInviteJoinGinTongInfo;
		Uri smsToUri = Uri.parse("smsto:" + smsnumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", str);
		startActivity(intent); 
	}



	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stubif(!isRefreshing){
		if(!isRefreshing){
			isRefreshing=true;
			startGetConnections();
		}
	}  
     
     
     
}
