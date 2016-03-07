package com.tr.ui.search;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiEnterpriseConfig.Eap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.model.home.MGetSearchList;
import com.tr.model.obj.SearchResult;
import com.tr.model.obj.SearchResultList;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.communities.home.CommunitiesDetailsActivity;
import com.tr.ui.home.InviteFriendByQRCodeActivity;
import com.tr.ui.widgets.NoScrollListview;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

public class SearchActivity_new extends JBaseFragmentActivity implements IBindData, OnClickListener{

	private LinearLayout result_first,result_second,resultll,personll,customll,knowledgell,conferencell,requirell;
	private TextView resultTv,personMore,customMore,knowledgeMore,conferenceMore,requireMore;
	private NoScrollListview perosnLv,customLv,knowledgeLv,conferenceLv,requireLv;
	private EditText mInputText;
	private TextView searchButton;
	private ImageView home_search_iv;
	private MyAdapter person_adapter,custom_adapter,knowledge_adapter,conference_adapter,require_adapter;
	
	public final static int TYPE_MEMBER = 4;// 人脉
	public final static int TYPE_KNOWLEDGE = 8;// 知识
	public final static int TYPE_METTING = 9;// 会议
	public final static int TYPE_DEMAND= 5;//需求
	public final static int TYPE_ORGANDCUSTOMER= 6;//组织和客户
	private int req_count = 0;
	
	@Override
	public void initJabActionBar() {
		// 将下拉列表添加到actionbar中
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowTitleEnabled(false);

		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.search_actionbar_edit);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.search_actionbar_edit, null);
		searchButton = (TextView) v.findViewById(R.id.home_search_tv);
		mInputText = (EditText) v.findViewById(R.id.home_search_edit);
		home_search_iv = (ImageView) v.findViewById(R.id.home_search_iv);
		home_search_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mInputText.setText("");
				home_search_iv.setVisibility(View.GONE);
			}
		});
		mInputText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				home_search_iv.setVisibility(View.VISIBLE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getData(mInputText.getText().toString());
			}
		});
		actionbar.setCustomView(v);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_new);
		initView();
		initData();
	}

	private void getData(String keyword) {
		req_count = 0;
		result_first.setVisibility(View.GONE);
		if(TextUtils.isEmpty(keyword)){
			personll.setVisibility(View.GONE);
			customll.setVisibility(View.GONE);
			knowledgell.setVisibility(View.GONE);
			conferencell.setVisibility(View.GONE);
			requirell.setVisibility(View.GONE);
			result_second.setVisibility(View.VISIBLE);
		}else{
			showLoadingDialog();
			HomeReqUtil.getSearchIndexList(this, this, null, keyword, 0, 3);
//			HomeReqUtil.getSearchList(this, this, null, keyword, TYPE_MEMBER, 0, 3);
//			HomeReqUtil.getSearchList(this, this, null, keyword, TYPE_ORGANDCUSTOMER, 0, 3);
//			HomeReqUtil.getSearchList(this, this, null, keyword, TYPE_KNOWLEDGE, 0, 3);
//			HomeReqUtil.getSearchList(this, this, null, keyword, TYPE_METTING, 0, 3);
//			HomeReqUtil.getSearchList(this, this, null, keyword, TYPE_DEMAND, 0, 3);
		}
	}

	private void initView() {
		result_first = (LinearLayout) findViewById(R.id.result_first);
		result_second = (LinearLayout) findViewById(R.id.result_second);
		resultll = (LinearLayout) findViewById(R.id.resultll);
		personll = (LinearLayout) findViewById(R.id.personll);
		customll = (LinearLayout) findViewById(R.id.customll);
		knowledgell = (LinearLayout) findViewById(R.id.knowledgell);
		conferencell = (LinearLayout) findViewById(R.id.conferencell);
		requirell = (LinearLayout) findViewById(R.id.requirell);
		
		resultTv = (TextView) findViewById(R.id.resultTv);
		personMore = (TextView) findViewById(R.id.personMore);
		customMore = (TextView) findViewById(R.id.customMore);
		knowledgeMore = (TextView) findViewById(R.id.knowledgeMore);
		conferenceMore = (TextView) findViewById(R.id.conferenceMore);
		requireMore = (TextView) findViewById(R.id.requireMore);
		
		personMore.setOnClickListener(this);
		customMore.setOnClickListener(this);
		knowledgeMore.setOnClickListener(this);
		conferenceMore.setOnClickListener(this);
		requireMore.setOnClickListener(this);
		
		perosnLv = (NoScrollListview) findViewById(R.id.perosnLv);
		customLv = (NoScrollListview) findViewById(R.id.customLv);
		knowledgeLv = (NoScrollListview) findViewById(R.id.knowledgeLv);
		conferenceLv = (NoScrollListview) findViewById(R.id.conferenceLv);
		requireLv = (NoScrollListview) findViewById(R.id.requireLv);
	}

	private void initData() {
		person_adapter = new MyAdapter(this, TYPE_MEMBER);
		custom_adapter = new MyAdapter(this, TYPE_ORGANDCUSTOMER);
		knowledge_adapter = new MyAdapter(this, TYPE_KNOWLEDGE);
		conference_adapter = new MyAdapter(this, TYPE_METTING);
		require_adapter = new MyAdapter(this, TYPE_DEMAND);
		
		perosnLv.setAdapter(person_adapter);
		customLv.setAdapter(custom_adapter);
		knowledgeLv.setAdapter(knowledge_adapter);
		conferenceLv.setAdapter(conference_adapter);
		requireLv.setAdapter(require_adapter);
	}

	class MyAdapter extends BaseAdapter{
		
		private Context mContext;
		private int mType;
		private List<SearchResult> data = new ArrayList<SearchResult>();
		
		public MyAdapter(Context context, int type) {
			this.mType = type;
			this.mContext = context;
		}

		public void setData(List<SearchResult> list) {
			this.data = list;
		}
		
		@Override
		public int getCount() {
			return data!=null?data.size():0;
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ItemHolder holder;
			final SearchResult dataHolder =data!=null? data.get(position):new SearchResult();
			final String imageUrl = dataHolder.getImage();
			if (convertView == null) {
				holder = new ItemHolder();
				if(mType == TYPE_MEMBER || mType == TYPE_ORGANDCUSTOMER){
					convertView = View.inflate(mContext, R.layout.home_search_relation_item_new, null);
					holder.imageIv = (ImageView) convertView.findViewById(R.id.searchAvatarIv);
					holder.mTime = (TextView) convertView.findViewById(R.id.contactTimeTv);
					holder.mTitle = (TextView) convertView.findViewById(R.id.contactNameTv);
				}else if (mType == TYPE_METTING || mType == TYPE_DEMAND) {
					convertView = View.inflate(mContext, R.layout.home_search_relation_item_new, null);
					holder.mTitle = (TextView) convertView.findViewById(R.id.contactNameTv);
					holder.contentTv = (TextView) convertView.findViewById(R.id.contactTimeTv);
					holder.imageIv = (ImageView) convertView.findViewById(R.id.searchAvatarIv);
				}else{
					convertView = LayoutInflater.from(mContext).inflate(R.layout.search_frg_listview_item, parent, false);
					holder.mTitle = (TextView) convertView.findViewById(R.id.txtsearch_listitem_title);
					holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
					holder.mTime = (TextView) convertView.findViewById(R.id.txtsearchPublishTime);
				}
				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}
			if (TextUtils.isEmpty(dataHolder.getTitle())|dataHolder.getTitle().contains("null"))
				holder.mTitle.setText("");
			else
				holder.mTitle.setText(dataHolder.getTitle());
			/** 好友/人脉 */
			if(mType == TYPE_MEMBER){
				//设置标签图片
				Drawable drawable = null;
				if (dataHolder != null && dataHolder.getType() == 1) {
					drawable = getResources().getDrawable(R.drawable.contactusertag);
				} else {
					drawable = getResources().getDrawable(R.drawable.contactpeopletag);
				}
				if (drawable != null) {
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
					if (holder.mTitle != null) {
						holder.mTitle.setCompoundDrawables(drawable,null, null, null);
						holder.mTitle.setCompoundDrawablePadding(10);
					}
				}
				
				Util.initAvatarImage(mContext, holder.imageIv, "", imageUrl, 0, 1);
				holder.mTime.setText(dataHolder.getCompany());
			}else if(mType == TYPE_ORGANDCUSTOMER){
				//设置标签图片
				Drawable drawable = null;
				if (dataHolder != null && dataHolder.getType() == 0) {
					//客户标签
					drawable = getResources().getDrawable(R.drawable.contactclienttag);
				} else {
					//组织标签
					drawable = getResources().getDrawable(R.drawable.contactorganizationtag);
				}
				if (drawable != null) {
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
					if (holder.mTitle != null) {
						holder.mTitle.setCompoundDrawables(drawable,null, null, null);
						holder.mTitle.setCompoundDrawablePadding(10);
					}
				}
				Util.initAvatarImage(mContext, holder.imageIv, "", imageUrl, 0, 2);
				holder.mTime.setText(dataHolder.getIndustrys());
			}else if (TYPE_METTING == mType) {
				holder.mTitle.setCompoundDrawables(null, null, null, null);
				holder.imageIv.setImageResource( R.drawable.meeting_logo_a);
//				com.tr.image.ImageLoader.load(holder.imageIv, imageUrl, R.drawable.meeting_logo_a);
				holder.contentTv.setVisibility(View.VISIBLE);
				holder.contentTv.setText(dataHolder.getContent());
			}else if (TYPE_KNOWLEDGE == mType) {
				if (StringUtils.isEmpty(dataHolder.getContent())) {
					holder.contentTv.setVisibility(View.GONE);
				}else {
					holder.contentTv.setVisibility(View.VISIBLE);
					holder.contentTv.setText(dataHolder.getContent());
				}
			}else if(mType == TYPE_DEMAND){
				//设置标签图片
//				Drawable drawable = null;
//				if (dataHolder != null && dataHolder.getType() == 0) {
//					//客户标签
//					drawable = getResources().getDrawable(R.drawable.contactclienttag);
//				} else {
//					//组织标签
//					drawable = getResources().getDrawable(R.drawable.contactorganizationtag);
//				}
//				if (drawable != null) {
//					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
//					if (holder.mTitle != null) {
//						holder.mTitle.setCompoundDrawables(drawable,null, null, null);
//						holder.mTitle.setCompoundDrawablePadding(10);
//					}
//				}
				if (dataHolder.getType() == 2) {// 融资事件
					initMyimage(holder.imageIv, "融资", 2);
//					people_type.setImageResource(R.drawable.demand_me_need02);
				}else{// 投资事件
					initMyimage(holder.imageIv, "投资", 0);
//					people_type.setImageResource(R.drawable.demand_me_need01);
				}
				holder.mTitle.setCompoundDrawables(null, null, null, null);
//				Util.initAvatarImage(mContext, holder.imageIv, "", imageUrl, 0, 2);
				if (TextUtils.isEmpty(dataHolder.getIndustrys())|dataHolder.getIndustrys().contains("null"))
					holder.contentTv.setText("");
				else
					holder.contentTv.setText(dataHolder.getIndustrys());
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (mType) {
					case TYPE_MEMBER:
						Long idLong = 0L;
						if(!TextUtils.isEmpty(dataHolder.getId())){
							idLong = Long.valueOf(dataHolder.getId());
						}
						if(2 == dataHolder.getType()){//人脉
							ENavigate.startRelationHomeActivity(mContext, String.valueOf(idLong),false,ENavConsts.TYPE_CONNECTIONS_HOME_PAGE);
//							ENavigate.startContactsDetailsActivity(mContext, 2, idLong, 0x123);
						}else{
//							ENavigate.startRelationHomeActivity(mContext, String.valueOf(idLong), true, ENavConsts.type_details_other);
							Intent intent = new Intent(mContext, InviteFriendByQRCodeActivity.class);
							intent.putExtra("friendId", dataHolder.getSource());
							mContext.startActivity(intent);
						}
						break;
					case TYPE_ORGANDCUSTOMER:
						//组织和客户跳转  ：0客户　1组织  2 大数据推的组织"
						if(dataHolder.getType() == 1||dataHolder.getType() == 2){
							Intent intent = new Intent(mContext, InviteFriendByQRCodeActivity.class);
							intent.putExtra("type", 1);
							intent.putExtra("OrgId", dataHolder.getId());
							mContext.startActivity(intent);
						}else if(dataHolder.getType() == 0 ){
							ENavigate.startClientDedailsActivity(mContext,Long.valueOf(dataHolder.getId()));
						}
						break;
					case TYPE_KNOWLEDGE:
						ENavigate.startKnowledgeOfDetailActivity(SearchActivity_new.this,
								Integer.valueOf(dataHolder.getId().trim()), dataHolder.getType(),false);
						break;
					case TYPE_METTING:
						ENavigate.startSquareActivity(mContext,
								Long.valueOf(dataHolder.getId()),0);
						break;
					default:
						ENavigate.startNeedDetailsActivity(mContext,dataHolder.getId(), 1);
//						showToast("此功能尚未开放,敬请等待");
						break;
					}
				}
			});
			return convertView;
		}
		
		private class ItemHolder {
			public TextView mTitle;
			public TextView contentTv;
			public TextView mTime;
			public ImageView imageIv;
		}
	}
	private void initMyimage(ImageView avatarIv, String name, int type) {
		Bitmap bm = null;
		int resid = 0;
		if (type == 2) {// 融资
			resid = R.drawable.no_avatar_but_gender;// 粉色
		} else {// 投资
			resid = R.drawable.no_avatar_client_organization;// 浅蓝色
		}
		bm = Util.createBGBItmap(this, resid, R.color.avatar_text_color, R.dimen.avatar_text_size, name);
		avatarIv.setImageBitmap(bm);
	}
	@Override
	public void bindData(int tag, Object object) {
		req_count++;
		if(object != null){
//			MGetSearchList searchList = (MGetSearchList) object;
//			List<SearchResult> list = new ArrayList<SearchResult>();
//			JTPage mPage = searchList.getJtPage();
//			if ((mPage != null) && (mPage.getLists() != null)) {
//				for (int i = 0; i < mPage.getLists().size(); i++) {
//					list.add((SearchResult) mPage.getLists().get(i));
//				}
//			}else{
//				return;
//			}
			switch(tag){
//			case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_PERSON:
//				if(list.size()>0){
//					personll.setVisibility(View.VISIBLE);
//					person_adapter.setData(list);
//					person_adapter.notifyDataSetChanged();
//				}else{
//					personll.setVisibility(View.GONE);
//				}
//				break;
//			case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_ORG:
//				if(list.size()>0){
//					customll.setVisibility(View.VISIBLE);
//					custom_adapter.setData(list);
//					custom_adapter.notifyDataSetChanged();
//				}else{
//					customll.setVisibility(View.GONE);
//				}
//				break;
//			case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_KNOWLEDGE:
//				if(list.size()>0){
//					knowledgell.setVisibility(View.VISIBLE);
//					knowledge_adapter.setData(list);
//					knowledge_adapter.notifyDataSetChanged();
//				}else{
//					knowledgell.setVisibility(View.GONE);
//				}
//				break;
//			case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_MEET:
//				if(list.size()>0){
//					conferencell.setVisibility(View.VISIBLE);
//					conference_adapter.setData(list);
//					conference_adapter.notifyDataSetChanged();
//				}else{
//					conferencell.setVisibility(View.GONE);
//				}
//				break;
//			case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_DEMEND:
//				if(list.size()>0){
//					requirell.setVisibility(View.VISIBLE);
//					require_adapter.setData(list);
//					require_adapter.notifyDataSetChanged();
//				}else{
//					requirell.setVisibility(View.GONE);
//				}
//				break;
			case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_INDEX_LIST:
				dismissLoadingDialog();
				if(object!=null){
					SearchResultList srl = (SearchResultList) object;
					if(srl.getPersonList().size()>0){
						personll.setVisibility(View.VISIBLE);
						person_adapter.setData(srl.getPersonList());
						person_adapter.notifyDataSetChanged();
					}else{
						personll.setVisibility(View.GONE);
					}
					if(srl.getOrganList().size()>0){
						customll.setVisibility(View.VISIBLE);
						custom_adapter.setData(srl.getOrganList());
						custom_adapter.notifyDataSetChanged();
					}else{
						customll.setVisibility(View.GONE);
					}
					if(srl.getKnowledgeList().size()>0){
						knowledgell.setVisibility(View.VISIBLE);
						knowledge_adapter.setData(srl.getKnowledgeList());
						knowledge_adapter.notifyDataSetChanged();
					}else{
						knowledgell.setVisibility(View.GONE);
					}
					if(srl.getMeetingList().size()>0){
						conferencell.setVisibility(View.VISIBLE);
						conference_adapter.setData(srl.getMeetingList());
						conference_adapter.notifyDataSetChanged();
					}else{
						conferencell.setVisibility(View.GONE);
					}
					if(srl.getDemandList().size()>0){
						requirell.setVisibility(View.VISIBLE);
						require_adapter.setData(srl.getDemandList());
						require_adapter.notifyDataSetChanged();
					}else{
						requirell.setVisibility(View.GONE);
					}
					
					if(srl.getPersonList().size()==0 && srl.getOrganList().size()==0 && srl.getKnowledgeList().size()==0 &&
							srl.getMeetingList().size()==0 && srl.getDemandList().size()==0){
						result_second.setVisibility(View.VISIBLE);
					}else
						result_second.setVisibility(View.GONE);
						
				}
				break;
			}
		}
//		if(req_count == 5){
//			dismissLoadingDialog();
//			if(personll.getVisibility() == View.GONE && 
//					customll.getVisibility() == View.GONE &&
//					knowledgell.getVisibility() == View.GONE && 
//					conferencell.getVisibility() == View.GONE && 
//					requirell.getVisibility() == View.GONE){
//				result_second.setVisibility(View.VISIBLE);
//			}else{
//				result_second.setVisibility(View.GONE);
//			}
//		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.personMore:
			ENavigate.startSearchMoreActivity(this, TYPE_MEMBER, mInputText.getText().toString());
			break;
		case R.id.customMore:
			ENavigate.startSearchMoreActivity(this, TYPE_ORGANDCUSTOMER ,mInputText.getText().toString());
			break;
		case R.id.knowledgeMore:
			ENavigate.startSearchMoreActivity(this, TYPE_KNOWLEDGE ,mInputText.getText().toString());
			break;
		case R.id.conferenceMore:
			ENavigate.startSearchMoreActivity(this, TYPE_METTING ,mInputText.getText().toString());
			break;
		case R.id.requireMore:
			ENavigate.startSearchMoreActivity(this, TYPE_DEMAND ,mInputText.getText().toString());
			break;
		}
		
	}
}
