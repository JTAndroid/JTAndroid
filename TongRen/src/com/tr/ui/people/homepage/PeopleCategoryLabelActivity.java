package com.tr.ui.people.homepage;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.api.PeopleReqUtil;
import com.tr.model.obj.Connections;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.home.frg.FrgConnections2.ContactAdapter;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.model.MyContacts;
import com.tr.ui.people.model.PersonSimple;
import com.tr.ui.people.model.PersonSimpleList;
import com.tr.ui.widgets.ConnsListDelDialog;
import com.tr.ui.widgets.ConnsListDelDialog.OnSelectListener;
import com.tr.ui.widgets.KnoCategoryAlertDialog.OperType;
import com.tr.ui.widgets.SideBar;
import com.utils.common.EConsts;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;

/**
 * 人脉目录标签展示页面
 */
public class PeopleCategoryLabelActivity extends JBaseActivity implements
		OnItemClickListener, IBindData {

	private XListView lvContact;// 内容list
	private RelativeLayout loadConnectionsWaitView;
	public static ContactAdapter contactAdapter = null;
	// private TextView mDialogText;// 中间提示的text

	// private SwipeRefreshLayout swipeLayout;
	/** 联系人操作对话框 */
	private ConnsListDelDialog connsListDelDialog;
	// ******************* 控件 end ******************* /

	private long cid;
//	private String categoryName;
	private long tid;

	private PersonSimpleList personSimpleList;
	
	private MyAdapter adapter;
	private ActionBar actionBar;

	@Override
	public void initJabActionBar() {
		actionBar = jabGetActionBar();
		
//		actionBar.setTitle("人脉");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_frg_connections_list3);
		Intent intent = getIntent();
		if (intent != null) {
			cid = intent.getLongExtra(EConsts.Key.CATEGORY_KEY, 0);
			if(intent.hasExtra(EConsts.Key.CATEGORY_NAME)){
				actionBar.setTitle(intent.getStringExtra(EConsts.Key.CATEGORY_NAME));
				HomeCommonUtils.initLeftCustomActionBar(this, actionBar, intent.getStringExtra(EConsts.Key.CATEGORY_NAME), false, null, true, true);
			}
			tid = intent.getLongExtra(EConsts.Key.LABEL_KEY, 0);
		}

		findView();
		
		
		requestJson(cid, tid);
	}

	private void requestJson(long cid, long tid) {
		MyContacts myContacts = new MyContacts();
		myContacts.cid = cid;
		myContacts.tid = tid;
		PeopleReqUtil.doRequestWebAPI(this, this, myContacts, null,
				PeopleRequestType.PEOPLE_REQ_PEOPLELIST);
	}

	private void findView() {
		loadConnectionsWaitView = (RelativeLayout) 
				findViewById(R.id.waitview);
		lvContact = (XListView) findViewById(R.id.lvContact);
		setXlistViewConfig(); //add by wenxiaohua 不需要下拉刷新

		connsListDelDialog = new ConnsListDelDialog(PeopleCategoryLabelActivity.this);
		connsListDelDialog.setOnSelectListener(new OnSelectListener() {

			@Override
			public void onSelect(OperType operType,
					Connections mAttachConnections) {

			}
		});

		adapter = new MyAdapter(this);
		lvContact.setAdapter(adapter);
		lvContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Adapter myAdapter =  parent.getAdapter();
				if (myAdapter!=null) {
					PersonSimple connections = (PersonSimple) myAdapter.getItem(position);
					ENavigate.startContactsDetailsActivity(PeopleCategoryLabelActivity.this, 2, Long.valueOf(connections.personid), 0);
				}
				
			}
		});
	}

	/** 设置XListView的参数 */
	private void setXlistViewConfig() {
		lvContact.showFooterView(false);
		// 设置xlistview可以加载、刷新
		lvContact.setPullRefreshEnable(false);
		lvContact.setPullLoadEnable(false);

		
	}

	class MyAdapter extends BaseAdapter implements OnClickListener {
		private Context context;
		public List<PersonSimple> dataSource;
		public MyAdapter(Context context) {
			this.context = context;
			dataSource = new ArrayList<PersonSimple>();
		}

		@Override
		public int getCount() {
			return dataSource.size();
		}

		@Override
		public Object getItem(int position) {
			return dataSource.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder =  new ViewHolder();
			if (convertView == null) {
				convertView = View.inflate(context,
						R.layout.im_relationcontactmain_item, null);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.contactAvatarIv);// 头像Logo
				holder.name = (TextView) convertView.findViewById(R.id.contactNameTv);// 姓名
				holder.work = (TextView) convertView.findViewById(R.id.contactCompanyOfferTv);// 职业
				holder.iv_message = (ImageView) convertView
						.findViewById(R.id.sendSmsIv);// 短信图标
				holder.iv_dial = (ImageView) convertView
						.findViewById(R.id.callIv);// 电话图标
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
							}
			PersonSimple personSimple = dataSource.get(position);
			if (personSimple != null) {
				Drawable drawable = null;
				if (personSimple.persontype == 1) {
					drawable = getResources().getDrawable(R.drawable.contactpeopletag);
				} else {
					drawable = getResources().getDrawable(R.drawable.contactpeopletag);
				}
				if (drawable != null) {
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
					if (holder.name != null) {
						holder.name.setCompoundDrawables(drawable,null, null, null);
						holder.name.setCompoundDrawablePadding(DisplayUtil.dip2px(PeopleCategoryLabelActivity.this, 10));
					}
				}
				if (personSimple.pinyin == null) {
					holder.tv_word.setText("A");
				}
				ImageLoader.getInstance().displayImage(
						personSimple.picpath, holder.imageview);
				holder.name
						.setText(personSimple.name1 + personSimple.name2);
				holder.work.setText(personSimple.company + " "
						+ personSimple.position);
				holder.iv_message.setVisibility(View.GONE);
				holder.iv_dial.setVisibility(View.GONE);
			}
			return convertView;
		}

		class ViewHolder {
			RelativeLayout contacts_RL;// 首字母所在的布局
			TextView tv_word;// 开头首字母
			ImageView imageview;// 头像Logo
			TextView name;// 姓名
			TextView work;// 职业
			ImageView iv_message;// 短信图标
			ImageView iv_dial;// 电话图标
			ImageView person;// 用户 人脉标识
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case EAPIConsts.PeopleRequestType.PEOPLE_REQ_PEOPLELIST:
			if (object == null) {
				return;
			}
			personSimpleList = (PersonSimpleList) object;
			if (personSimpleList != null && personSimpleList.list != null) {
				adapter.dataSource = personSimpleList.list;
				adapter.notifyDataSetChanged();
			}
			break;
		}
	}

	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
