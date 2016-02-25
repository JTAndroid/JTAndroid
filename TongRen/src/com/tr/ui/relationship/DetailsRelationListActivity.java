package com.tr.ui.relationship;
/**
 * 选择联系人界面
 */

import java.util.ArrayList;
import org.json.JSONObject;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.obj.ConnectionsMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.common.EConsts;
import com.utils.http.IBindData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DetailsRelationListActivity extends  JBaseFragmentActivity   {
/** Called when the activity is first created. */
	
//	 "type":"1-需求；2-业务需求；3-任务；4-项目；5-用户",
//     "id":"需求id"
		public final static int type_requirement=1;
		public final static int type_businessrequirement=2;
		public final static int type_task=3;
		public final static int type_project=4;
		public final static int type_user=5;
		
		
		private ListView lvContact;//内容list
		private WindowManager mWindowManager;
		private TextView mDialogText;//中间提示的text
		/** all datas*/
		ArrayList<ConnectionsMini> mConnections=null;
		//新关系 群组列表按钮
		LinearLayout im_new_contactlist;
		LinearLayout im_groupslist;
		String fromActivityName;
		int  id=0;
		private int type=0;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			type=getIntent().getIntExtra(EConsts.Key.TYPE, 0);
			id=getIntent().getIntExtra(EConsts.Key.ID, 0);
			super.onCreate(savedInstanceState);
			this.setContentView(R.layout.detailsrelationlist);
			mWindowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
			lvContact = (ListView)this.findViewById(R.id.lvContact);
	        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					ConnectionsMini connections=(ConnectionsMini)arg1.getTag();
					String tempId=connections.getId();
					if(connections.getType()==ConnectionsMini.UT_PERSON){
						ENavigate.startUserDetailsActivity(DetailsRelationListActivity.this,tempId,
								 true,0);
					}else{
					}
				}
			});
	      //  initData();
	        
//	       ArrayList obj=(ArrayList)this.getIntent().getSerializableExtra(ENavConsts.datas);
//	       mConnections=(ArrayList<ConnectionsMini>)obj;
	       if(mConnections==null){
	    	   mConnections =new ArrayList<ConnectionsMini>();
	       }
//	       if(Util.user_simulation){
//	    	   ConnectionsMini connectionsMini=new ConnectionsMini();
//		       connectionsMini.setType(ConnectionsMini.UT_PERSON);
//		       connectionsMini.setName("3434");
//		       mConnections.add(connectionsMini);
//	       }
	       lvContact.setAdapter(new ContactAdapter(this,mConnections));
	       
	       JSONObject jb=ConnectionsReqUtil.getMatchConnectionsMiniJson( id+"",type);
	       ConnectionsReqUtil.getMatchConnectionsMini(this,ib,jb,null); 
	       showLoadingDialog();
		}
		IBindData ib=new IBindData() {
			@Override
			public void bindData(int tag, Object object) {
				
				if ( !isLoadingDialogShowing()) {
					return;
				} else {
					dismissLoadingDialog();
				}
				// TODO Auto-generated method stub
				if(object==null){
					showToast("读取错误");
				}else{
					mConnections=(ArrayList<ConnectionsMini>) object;
					if(mConnections.size()==0){
						showToast("数据为空");
					}else{
						((ContactAdapter)lvContact.getAdapter()).setData(mConnections);
						((ContactAdapter)lvContact.getAdapter()).notifyDataSetChanged();
					}
				}
			}
		};
		
		 public void getParam(){
		    	Intent intent=getIntent();  
		    	fromActivityName = intent.getStringExtra(ENavConsts.EFromActivityName);  
		 }


	     class ContactAdapter extends BaseAdapter   {  
	    	private Context mContext;
	    	ArrayList<ConnectionsMini> mConnections;
	    	@SuppressWarnings("unchecked")
			public ContactAdapter(Context mContext,ArrayList<ConnectionsMini> connections){
	    		this.mContext = mContext;
	    		mConnections=connections;
	    	}
	    	public void setData(ArrayList<ConnectionsMini> connections){
	    		this.mConnections=connections;
	    	}
	    	
			@Override
			public int getCount() {
				return mConnections.size();
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ConnectionsMini connections=null;
				String mNicks=null;
				connections=mConnections.get(position);
				mNicks=connections.getName();
				
				TextView tag;//标头
				TextView imcontactName;//名字
				TextView imcontactComeFrom;//来自
				ImageView icon;//头像
				ImageView icontag;//头像
				
				if(convertView==null){
					convertView= LayoutInflater.from(mContext).inflate(R.layout.im_relationcontactmain_item1, null);
				}
					tag = (TextView)convertView.findViewById(R.id.contactitem_catalog);
					imcontactName = (TextView)convertView.findViewById(R.id.imcontactname);
					imcontactComeFrom = (TextView)convertView.findViewById(R.id.imcontactcomefrom);
					icon = (ImageView)convertView.findViewById(R.id.contactitem_avatar_iv);
					icontag = (ImageView)convertView.findViewById(R.id.contactitem_icontag);
					
					
					tag.setVisibility(View.GONE);
					imcontactName.setText(connections.getName());
					imcontactComeFrom.setText("");
					
//					if(!connections.isOnline()){
//						icontag.setVisibility(View.VISIBLE);
//					}else{
						icontag.setVisibility(View.GONE);
//					}
						convertView.setTag(connections);
//					ImageLoader.getInstance().cancelDisplayTask(icon);
//					ImageLoader.getInstance().displayImage("http://img0.bdstatic.com/static/common/widget/search_box_search/logo/logo_3b6de4c.png",
//					icon,LoadImage.getDisplayOptions(LoadImage.TYPE_IMAGE_ICON));
//					convertView.setTag(connections);
					return convertView;
			}		
	}

		@Override
		public void initJabActionBar() {
			// TODO Auto-generated method stub
			
		}    
		
		
		
}