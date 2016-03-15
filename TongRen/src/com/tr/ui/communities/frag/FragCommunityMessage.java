package com.tr.ui.communities.frag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import m.framework.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.image.ImageLoader;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.communities.im.CommunityChatActivity;
import com.tr.ui.communities.model.CommunityNotify;
import com.tr.ui.communities.model.CommunitySocial;
import com.tr.ui.communities.model.CommunitySocialList;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

public class FragCommunityMessage extends JBaseFragment implements IBindData{
	
	private ArrayList<CommunitySocial> communitySociallist = new ArrayList<CommunitySocial>();
	private CommumitieSocialAdapter adapter;
	private XListView commumitiesnotificationLv;
	private TextView NoContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_commumitiesnotification, null);
		initView(view);
		initData();
		return view;

	}
	
	@Override
	public void onResume() {
		super.onResume();
		CommunityReqUtil.doGetCommunityList(getActivity(), FragCommunityMessage.this, App.getUserID(), null);
	}
	
	private void initView(View view) {
		NoContent = (TextView) view.findViewById(R.id.NoContent);
		commumitiesnotificationLv = (XListView) view.findViewById(R.id.commumitiesnotificationLv);
		commumitiesnotificationLv.setPullLoadEnable(false);
		commumitiesnotificationLv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				CommunityReqUtil.doGetCommunityList(getActivity(), FragCommunityMessage.this, App.getUserID(), null);
			}
			
			@Override
			public void onLoadMore() {
				
			}
		});
	}
	
	private void initData() {
		adapter = new CommumitieSocialAdapter(getActivity());
		adapter.setData(communitySociallist);
		commumitiesnotificationLv.setAdapter(adapter);
		
		commumitiesnotificationLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				CommunitySocial communitySocial = adapter.getItem(position-1);
				communitySocial.setNewCount(0);
				adapter.notifyDataSetChanged();
				Intent intent = new Intent(getActivity(), CommunityChatActivity.class);
				intent.putExtra("communityId", Long.valueOf(communitySocial.getId()));
				startActivityForResult(intent, 0);
			}
		});
	}
	
	@Override
	public void bindData(int tag, Object object) {
		commumitiesnotificationLv.stopRefresh();
		switch(tag){
		case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_LIST:
			if (object != null) {
				HashMap<String, Object> dataBox = (HashMap<String, Object>) object;
				CommunitySocialList csl = (CommunitySocialList) dataBox.get("CommunitySocialList");
				communitySociallist = (ArrayList<CommunitySocial>) csl.getListCommunity();
				if(!communitySociallist.isEmpty()){
					NoContent.setVisibility(View.GONE);
					adapter.setData(communitySociallist);
					adapter.notifyDataSetChanged();
				}else{
					NoContent.setVisibility(View.VISIBLE);
				}
			}else{
				NoContent.setVisibility(View.VISIBLE);
			}
			break;
		}
		
	}
	
	class CommumitieSocialAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<CommunitySocial> communitySociallist = new ArrayList<CommunitySocial>();

		public CommumitieSocialAdapter(Context context) {
			this.mContext = context;
		}
		
		public void setData(ArrayList<CommunitySocial> communitySociallist){
			this.communitySociallist = communitySociallist;
		}

		@Override
		public int getCount() {
			return communitySociallist.size();
		}

		@Override
		public CommunitySocial getItem(int position) {
			return communitySociallist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final CommunitySocial communitySocial = getItem(position);
			if (convertView == null)
				convertView = View.inflate(mContext, R.layout.list_item_community_chat, null);
			
			GridView sociality_gv = ViewHolder.get(convertView, R.id.sociality_gv);// 头像
			FrameLayout chat_push_data_num_gv_control = ViewHolder.get(convertView, R.id.chat_push_data_num_gv_control);
			TextView chat_push_data_num_gv = ViewHolder.get(convertView, R.id.chat_push_data_num_gv);
			TextView chat_name = ViewHolder.get(convertView, R.id.chat_name); 
			TextView the_last_time_of_chat = ViewHolder.get(convertView, R.id.the_last_time_of_chat); 
			TextView chat_content = ViewHolder.get(convertView, R.id.chat_content); 
			
			sociality_gv.setAdapter(new GridViewAdapter(communitySocial.getSocialDetail().getListImageUrl()));
			if(communitySocial.getNewCount()==0){
				chat_push_data_num_gv_control.setVisibility(View.GONE);
			}else{
				chat_push_data_num_gv_control.setVisibility(View.VISIBLE);
				chat_push_data_num_gv.setText(communitySocial.getNewCount()+"");
			}
			chat_name.setText(communitySocial.getTitle());
			chat_content.setText(communitySocial.getSocialDetail().getContent());
			if(!TextUtils.isEmpty(communitySocial.getTime())){
				the_last_time_of_chat.setText(TimeUtil.TimeFormat(communitySocial.getTime()));
			}else{
				the_last_time_of_chat.setText("");
			}
			
			return convertView;
		}
		
		class GridViewAdapter extends BaseAdapter {
			List<String> imgs = null;

			private GridViewAdapter(List<String> imgs) {
				this.imgs = imgs;
			}

			@Override
			public int getCount() {
				return imgs.size() > 4 ? 4 : imgs.size();
			}

			@Override
			public Object getItem(int position) {
				return imgs.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				CircleImageView view = new CircleImageView(mContext);
				ListView.LayoutParams params = new ListView.LayoutParams(Utils.dipToPx(mContext, 19), Utils.dipToPx(mContext, 19));
				view.setLayoutParams(params);
				try {
					if (!StringUtils.isEmpty(imgs.get(position))) {
						// ImageLoader.getInstance().displayImage(imgs.get(position),
						// view, LoadImage.mDefaultHead);
						// ImageLoader.load(view, imgs.get(position),
						// R.drawable.ic_default_avatar);
						ImageLoader.setContext(mContext);
						ImageLoader.load(view, ImageLoader.MUCCHAT_BITMAP, imgs.get(position), R.drawable.chat_im_img_user);
					} else {
						view.setImageResource(R.drawable.chat_im_img_user);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return view;
			}
		}
	}

}
