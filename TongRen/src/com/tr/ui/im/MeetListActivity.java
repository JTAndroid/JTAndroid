package com.tr.ui.im;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.image.ImageLoader;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MListSociality;
import com.tr.model.conference.MMeetingDetail;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MSociality;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.conference.initiatorhy.InitiatorHYActivity;
import com.tr.ui.people.cread.utils.Utils;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.EditOrDeletePopupWindow;
import com.tr.ui.widgets.EditOrDeletePopupWindow.OnMeetingOptItemClickListener;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

public class MeetListActivity extends JBaseFragmentActivity implements OnClickListener,IBindData{
	private ImageView meet_iv,more_iv,search_iv;
	private XListView mListView;
	private boolean isFirstStart = true;
	private MListSociality conference;
	private MeetAdapter adapter;
	private List<MSociality> listSocialMeet,golble_meet;
	private PopupWindow popupWindow;
	
	private EditOrDeletePopupWindow itemLongClickPopupWindow;
	

	private View deleteView;
	private TextView deleteTv;
	private TextView save;
	private int measuredWidth;
	private int linmitHeight;
	private int measuredHeigh;
	private PopupWindow window;
	private int deleteClickPosition = -1;
	private RelativeLayout title_RL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.social_meetlist);
		title_RL = (RelativeLayout) findViewById(R.id.title_RL);
		meet_iv = (ImageView) findViewById(R.id.meet_iv);
		more_iv = (ImageView) findViewById(R.id.more_iv);
		search_iv = (ImageView) findViewById(R.id.search_iv);
		meet_iv.setOnClickListener(this);
		more_iv.setOnClickListener(this);
		search_iv.setOnClickListener(this);
		
		mListView = (XListView) findViewById(R.id.meet_listview);
		
		deleteView = View.inflate(MeetListActivity.this, R.layout.layout_sociality_delete, null);
		deleteTv = (TextView) deleteView.findViewById(R.id.delete);
		save = (TextView) deleteView.findViewById(R.id.save);
		setXlistViewConfig();
//		startGetData();
	}
	
	@Override
	public void initJabActionBar() {
		getActionBar().hide();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startGetData();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.meet_iv:
			finish();
			break;
		case R.id.more_iv:
			popupWindow = new PopupWindow(MeetListActivity.this);
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
			ColorDrawable dw = new ColorDrawable(0000000);
            popupWindow.setBackgroundDrawable(dw);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);  
			View convertView = View.inflate(MeetListActivity.this,
					R.layout.meet_popupwindow, null);
			final TextView meet_all = (TextView) convertView.findViewById(R.id.meet_all);
			final TextView meet_nostart = (TextView) convertView.findViewById(R.id.meet_nostart);
			final TextView meet_start = (TextView) convertView.findViewById(R.id.meet_start);
			final TextView meet_end = (TextView) convertView.findViewById(R.id.meet_end);
			meet_all.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(adapter!=null && golble_meet!=null){
						adapter.setData(golble_meet);
						adapter.notifyDataSetChanged();
					}
					popupWindow.dismiss();
				}
			});
			meet_nostart.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(adapter!=null && golble_meet!=null){
						adapter.setData(getList(golble_meet, 4));
						adapter.notifyDataSetChanged();
					}
					popupWindow.dismiss();
				}
			});
			meet_start.setOnClickListener(new OnClickListener() {
		
				@Override
				public void onClick(View v) {
					if(adapter!=null && golble_meet!=null){
						adapter.setData(getList(golble_meet, 3));
						adapter.notifyDataSetChanged();
					}
					popupWindow.dismiss();
				}
			});
			meet_end.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(adapter!=null && golble_meet!=null){
						adapter.setData(getList(golble_meet, 5));
						adapter.notifyDataSetChanged();
					}
					popupWindow.dismiss();
				}                            
			});
			popupWindow.setContentView(convertView);
			
			popupWindow.showAsDropDown(more_iv, (int)Utils.convertDpToPixel(10), 0);
			break;
		case R.id.search_iv:
			ENavigate.startSearchActivity(this,1);
			break;
		}
	}
	
	/** 设置XListView的参数 */
	private void setXlistViewConfig() {
		mListView.showFooterView(false);
		// 设置xlistview可以加载、刷新
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);

		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				startGetData();
			}

			@Override
			public void onLoadMore() {
				startGetData();
			}
		});
	}
	
	/**
	 * 获取社交会议列表
	 */
	public void startGetData() {
		showLoadingDialog();
		ConferenceReqUtil.doGetSocialMeetList(MeetListActivity.this, MeetListActivity.this, null);
	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETMEETLIST && object != null) {
			conference = (MListSociality) object;
			golble_meet = conference.getListSocial();
			listSocialMeet = conference.getListSocial();
			if(isFirstStart){
				isFirstStart = false;
				init();
			}else{
				adapter.notifyDataSetChanged();
			}
			mListView.stopRefresh();
			dismissLoadingDialog();
			int numCount = 0 ;
			for (int i = 0; i < listSocialMeet.size(); i++) {
				int num = listSocialMeet.get(i).getNewCount();
				numCount += num;
			}
			//写入数
			SharedPreferences mySharedPreferences= getSharedPreferences(GlobalVariable.SHARED_PREFERENCES_MEETING_NEW_COUNT, 
			MeetListActivity.MODE_PRIVATE);
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putInt(GlobalVariable.MEETING_NEW_COUNT_KEY, numCount);
			editor.commit();
			
		}
		if (tag == EAPIConsts.IMReqType.IM_REQ_CLEAR_UNREAD_MESSAGENUMBER) {
			if (object!=null) {
				int responseCode = (Integer) object;
				//删除失败
				if (responseCode== -1) {
					showToast("删除失败");
				}
				//删除成功
				else if (responseCode==0) {
					if (deleteClickPosition!=-1) {
						listSocialMeet.remove(deleteClickPosition -1);
						adapter.notifyDataSetChanged();
					}
				}
			}
			deleteClickPosition = -1;
		}
		if(tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_SOCIAL){
			if(object!=null){
				boolean is_remove = (Boolean) object;
				if(!is_remove){
					showToast("删除失败");
				}
			}
		}
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CHANGEMYMEMBERMEETSTATUS && object != null) {
			SimpleResult result = (SimpleResult) object;
			if (result.isSucceed()) {
				Toast.makeText(MeetListActivity.this, "保存会议成功", 0).show();
			} else {
				Toast.makeText(MeetListActivity.this, "保存会议失败", 0).show();
			}
		}
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_DETAIL && object != null) {
			MMeetingDetail meetingDetail = (MMeetingDetail) object;
			MMeetingQuery meetingData = meetingDetail.getMeeting();
			if (meetingData != null) {
				Intent intent = new Intent(this, InitiatorHYActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("meetingData", meetingData);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
		
	}
	
	private void dismissPopWindow() {
		if (itemLongClickPopupWindow != null && itemLongClickPopupWindow.isShowing()) {
			itemLongClickPopupWindow.dismiss();
		}
	}
	private void init(){
		adapter = new MeetAdapter();
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			private Intent meetingIntent;
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				Holder holder = (Holder) view.getTag();
				switch(listSocialMeet.get(position-1).getType()){
				// 会议  进行中的会议
				case 3:
//					holder.conference_push_data_num_control.setVisibility(View.GONE);

					meetingIntent = new Intent();
					if (listSocialMeet.get(position - 1).getMeetingType() == 1) { // 有议题
						ENavigate.startMeetingMasterActivityForResult(MeetListActivity.this, listSocialMeet.get(position - 1).getId(),-1);
					} else if (listSocialMeet.get(position - 1).getMeetingType() == 0) { // 无议题
//						Intent intent = new Intent(MeetListActivity.this, MeetingChatActivity.class);
//						intent.putExtra(ENavConsts.EMeetingId, listSocialMeet.get(position - 1).getId()+"");
//						intent.putExtra(ENavConsts.ETopicId, listSocialMeet.get(position - 1).getListMeetingTopic().get(0).getId()+"");
//						startActivity(intent);
						ENavigate.startMeetingChatActivity(MeetListActivity.this, listSocialMeet.get(position - 1).getId()+"", listSocialMeet.get(position - 1).getListMeetingTopic().get(0).getId()+"");
					}
					break;
					
				//	未开始 的 会议
				case 4:
//					holder.conference_push_data_num_control.setVisibility(View.GONE);

					meetingIntent = new Intent();
					if (listSocialMeet.get(position - 1).getMeetingType() == 1) { // 有议题
						ENavigate.startMeetingMasterActivityForResult(MeetListActivity.this, listSocialMeet.get(position - 1).getId(),-1);
					} else if (listSocialMeet.get(position - 1).getMeetingType() == 0) { // 无议题
//						meetingIntent.setClass(MeetListActivity.this, MeetingMasterNonGuestActivity.class);
//						meetingIntent.putExtra("meeting_id",
//								Integer.parseInt(listSocialMeet.get(position - 1).getId() + ""));
//						startActivity(meetingIntent);
						ENavigate.startMeetingChatActivity(MeetListActivity.this, listSocialMeet.get(position - 1).getId()+"", listSocialMeet.get(position - 1).getListMeetingTopic().get(0).getId()+"");
					}
					break;
					
				//	已结束的会议
				case 5:
//					holder.conference_push_data_num_control.setVisibility(View.GONE);
					meetingIntent = new Intent();

					if (listSocialMeet.get(position - 1).getMeetingType() == 1) { // 有议题
						ENavigate.startMeetingMasterActivityForResult(MeetListActivity.this, listSocialMeet.get(position - 1).getId(),-1);
					} else if (listSocialMeet.get(position - 1).getMeetingType() == 0) { // 无议题
//						meetingIntent.setClass(MeetListActivity.this, MeetingMasterNonGuestActivity.class);
//						meetingIntent.putExtra("meeting_id",
//								Integer.parseInt(listSocialMeet.get(position - 1).getId() + ""));
//						startActivity(meetingIntent);
						ENavigate.startMeetingChatActivity(MeetListActivity.this, listSocialMeet.get(position - 1).getId()+"", listSocialMeet.get(position - 1).getListMeetingTopic().get(0).getId()+"");
					}

					break;
				
				}
			}
		});
		
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				dismissPopWindow();
				if (position!=0) {
					final MSociality mSociality = listSocialMeet.get(position - 1);
					if (itemLongClickPopupWindow == null) {
						itemLongClickPopupWindow = new EditOrDeletePopupWindow(MeetListActivity.this);
					}
					dismissPopWindow();
					
					if (App.getNick().equals(mSociality.getCompereName())) {//发起人是自己//显示编辑和删除
						itemLongClickPopupWindow.showAllButton("编辑","删除");
					}else{
						itemLongClickPopupWindow.showAllButton("保存","删除");
					}
					itemLongClickPopupWindow.setOnItemClickListener(new OnMeetingOptItemClickListener() {
						
						@Override
						public void edit(String editStr) {
							if ("编辑".equals(editStr)) {
								ConferenceReqUtil.doGetMeetingDetail(MeetListActivity.this, MeetListActivity.this, mSociality.getId(), App.getUserID(), null);
							}else if ("保存".equals(editStr)) {
								ConferenceReqUtil.doChangeMyMemberMeetStatus(MeetListActivity.this, MeetListActivity.this, listSocialMeet.get(position - 1).getId(), Long.valueOf(App.getUserID()), 0, null);
							}
							dismissPopWindow();
						}
						
						@Override
						public void delete(String deleteStr) {
							deleteClickPosition = position;
							int type = listSocialMeet.get(position - 1).getType();
							// 1单聊 2畅聊 3会议 4邀请函 5通知 :
							// 1-私聊，2-群聊，3-进行中的会议，4-未开始，5-已结束的会议，6-通知，7-邀请函
							int parametersType = 0;
							long userId2 = 0;
							long mucId = 0;
							if (type == 1) {
								userId2 = listSocialMeet.get(position - 1).getId();
								parametersType = 1;
							} else if (type == 2 || type == 3 || type == 4 || type == 5) {
								mucId = listSocialMeet.get(position - 1).getId();
								parametersType = (type == 3 || type == 4 || type == 5) ? 3 : 2;
							} else if (type == 6) {
								parametersType = 4;
							} else if (type == 7) {
								parametersType = 5;
							}
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							// 调删除社交列表中数据的接口
							ConferenceReqUtil.doRemoveSocial(MeetListActivity.this, MeetListActivity.this, listSocialMeet.get(position - 1).getId(), -1, type, Long.valueOf(App.getUserID()),
									format.format(new Date()), null);
							IMReqUtil.doclearUnreadMessageNumber(MeetListActivity.this, MeetListActivity.this, Long.valueOf(App.getUserID()), userId2, mucId, parametersType, null, false);
						}
					});
					if (title_RL != null) {
						itemLongClickPopupWindow.showAsDropDown(title_RL);
					}
				}
				return true;
			}
			
		});
	}

	private class MeetAdapter extends BaseAdapter {
		
		public void setData(List<MSociality> list){
			listSocialMeet = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listSocialMeet.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listSocialMeet.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;

			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(MeetListActivity.this, R.layout.list_item_sociality_conference, null);
				holder.image_rl = (RelativeLayout) convertView.findViewById(R.id.image_rl);
				holder.conference_sociality_iv = (CircleImageView) convertView.findViewById(R.id.conference_sociality_iv);
				holder.conference_status = (TextView) convertView.findViewById(R.id.conference_status);
				holder.conference_name = (TextView) convertView.findViewById(R.id.conference_name);
				holder.conference_time = (TextView) convertView.findViewById(R.id.conference_time);
				holder.the_last_time_of_conference = (TextView) convertView
						.findViewById(R.id.the_last_time_of_conference);
				holder.conference_push_data_num = (TextView) convertView.findViewById(R.id.conference_push_data_num);
				holder.conference_push_data_num_control = (FrameLayout) convertView
						.findViewById(R.id.conference_push_data_num_control);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			
			// 3-进行中的会议
			if (listSocialMeet.get(position).getType() == 3) {
				holder.conference_status.setBackgroundResource(R.drawable.hy_status_doing);
			}
			// 4-未开始
			if (listSocialMeet.get(position).getType() == 4) {
				holder.conference_status.setBackgroundResource(R.drawable.hy_status_not_start);
			}
			// 5-已结束的会议
			if (listSocialMeet.get(position).getType() == 5) {
				holder.conference_status.setBackgroundResource(R.drawable.hy_status_done);
			}
			if (listSocialMeet.get(position).getSocialDetail().getListImageUrl() != null
					&& !StringUtils.isEmpty(listSocialMeet.get(position).getSocialDetail().getListImageUrl().get(0))) {
					ImageLoader.load(holder.conference_sociality_iv, listSocialMeet.get(position).getSocialDetail().getListImageUrl().get(0));
			} else {
				holder.conference_sociality_iv.setImageResource(R.drawable.meeting_logo_a);
			}
			if (!StringUtils.isEmpty(listSocialMeet.get(position).getTitle())) {
				holder.conference_name.setText(listSocialMeet.get(position).getTitle());
			} else {
				holder.conference_name.setText("");
			}
			if(!StringUtils.isEmpty(listSocialMeet.get(position).getCompereName())){
				holder.conference_time.setText("发起人："+listSocialMeet.get(position).getCompereName());
			}else{
				holder.conference_time.setText("暂无内容");
			}
			if (!StringUtils.isEmpty(listSocialMeet.get(position).getTime())) {
				holder.the_last_time_of_conference.setText(TimeUtil.TimeFormat(listSocialMeet.get(position).getTime()));
			} else {
				holder.the_last_time_of_conference.setText("");
			}
			if (listSocialMeet.get(position).getNewCount() > 0) {
				holder.conference_push_data_num_control.setVisibility(View.VISIBLE);
				holder.conference_push_data_num.setText((listSocialMeet.get(position).getNewCount() > 99 ? 99
						: listSocialMeet.get(position).getNewCount()) + "");
			} else {
				holder.conference_push_data_num_control.setVisibility(View.GONE);
			}
			return convertView;
		}
		
	}
	private class Holder {
		private RelativeLayout image_rl;
		private CircleImageView conference_sociality_iv;
		private TextView conference_status;
		private TextView conference_name;
		private TextView conference_time;
		private TextView the_last_time_of_conference;
		private TextView conference_push_data_num;
		private FrameLayout conference_push_data_num_control;
	}
	
	private List<MSociality> getList(List<MSociality> listdata, int type){
		List<MSociality> list = new ArrayList<MSociality>();
		for(MSociality social : listdata){
			if(social.getType() == type){
				list.add(social);
			}
		}
		return list;
	}
}
