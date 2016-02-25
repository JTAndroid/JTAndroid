package com.tr.ui.conference.home;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.model.conference.MMeetingNoticeQuery;
import com.tr.model.conference.MMeetingNoticeRelation;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.home.MainActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.im.MeetListActivity;
import com.tr.ui.widgets.CircleImageView;
import com.utils.common.GlobalVariable;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;
import com.utils.string.StringUtils;

/**
 * 新版——会议通知
 * 
 * @author zhongshan
 */
public class MeetingNoticeActivity2 extends JBaseActivity implements IBindData, OnItemLongClickListener, OnClickListener, OnItemClickListener {

	private Context mContext;
	private XListView listView;
	private MeetingNoticeAdapter adapter;
	private DisplayImageOptions options;
	private boolean showChectBox = false;
	private boolean isChectAll = false;
	private AnimateFirstDisplayListener animateFirstDisplayListener;
	private ArrayList<MMeetingNoticeRelation> meetingNoticeRelationList;
	private TextView deleteButton;
	private MenuItem selectAll;
	private MenuItem cancleAll;
	/** 存放删除会议通知的id */
	private Map<Integer, Long> deleteNoticeMap;
	private RelativeLayout.LayoutParams lp;
	private String currentFlowFrgTitle = "会议通知";

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(MeetingNoticeActivity2.this, getActionBar(),currentFlowFrgTitle ,false,null,false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meeting_notice_activity_layout);
		this.mContext = this;
		listView = (XListView) findViewById(R.id.listView);
		lp = (android.widget.RelativeLayout.LayoutParams) listView.getLayoutParams();
		deleteButton = (TextView) findViewById(R.id.deleteButton);
		listView.setOnItemLongClickListener(this);
		listView.setOnItemClickListener(this);
		listView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				showLoadingDialog();
				ConferenceReqUtil.doGetMeetingNoticeList(MeetingNoticeActivity2.this, MeetingNoticeActivity2.this, App.getUserID(), null);
			}

			@Override
			public void onLoadMore() {

			}
		});
		deleteButton.setOnClickListener(this);
		initContent();
	}

	private void initContent() {
		meetingNoticeRelationList = new ArrayList<MMeetingNoticeRelation>();
		adapter = new MeetingNoticeAdapter();
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		startGetData();
	}
	/**
	 * 获取社交会议列表
	 */
	public void startGetData() {
		showLoadingDialog();
		ConferenceReqUtil.doGetMeetingNoticeList(this, this, App.getUserID(), null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_filter, menu);
		selectAll = menu.findItem(R.id.filter_select_all);
		cancleAll = menu.findItem(R.id.filter_reverse_selection);
		menu.findItem(R.id.filter_confirm_selection).setVisible(false);
		selectAll.setVisible(false);
		cancleAll.setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 取消和全选显隐
	 * 
	 * @param b
	 */
	private void showSelectOrCancleAllMenuItem(boolean b) {
		selectAll.setVisible(b);
		cancleAll.setVisible(!b);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/** 全选——》取消全选 */
		case R.id.filter_select_all:
			showSelectOrCancleAllMenuItem(false);
			isChectAll = true;
			int position = 0;
			deleteNoticeMap.clear();
			for (MMeetingNoticeRelation noticeRelation : meetingNoticeRelationList) {
				deleteNoticeMap.put(position, noticeRelation.getMeetingId());
				position++;
			}
			break;
		/** 取消全选——》全选 */
		case R.id.filter_reverse_selection:
			showSelectOrCancleAllMenuItem(true);
			isChectAll = false;
			deleteNoticeMap.clear();
			break;
		}
		adapter.notifyDataSetChanged();
		return super.onOptionsItemSelected(item);
	}

	private class MeetingNoticeAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return meetingNoticeRelationList != null ? meetingNoticeRelationList.size() : 0;
		}

		@Override
		public MMeetingNoticeRelation getItem(int position) {
			return meetingNoticeRelationList != null ? meetingNoticeRelationList.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return meetingNoticeRelationList != null && meetingNoticeRelationList.get(position) != null ? meetingNoticeRelationList.get(position).getMeetingId() : 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			try {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.hy_meeting_notice_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.meetingIv = (CircleImageView) convertView.findViewById(R.id.meetingIv);
				viewHolder.meetingTitleTv = (TextView) convertView.findViewById(R.id.meetingTitleTv);
				viewHolder.meetingOperateTv = (TextView) convertView.findViewById(R.id.meetingOperateTv);
				viewHolder.meetingTimeTv = (TextView) convertView.findViewById(R.id.meetingTimeTv);
				viewHolder.meetingNoticeCb = (CheckBox) convertView.findViewById(R.id.meetingNoticeCb);
				viewHolder.noticeDetialCountFL = (FrameLayout) convertView.findViewById(R.id.noticeDetialCountFL);
				viewHolder.countTv = (TextView) convertView.findViewById(R.id.countTv);
//				RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.image_gv_rl);
//				android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext, 54), DisplayUtil.dip2px(mContext, 54));
//				rl.setLayoutParams(layoutParams);
//				LayoutParams lp = new LayoutParams(DisplayUtil.dip2px(mContext, 50), DisplayUtil.dip2px(mContext, 50));
//				viewHolder.meetingIv.setLayoutParams(lp);
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			if (meetingNoticeRelationList != null && meetingNoticeRelationList.get(position) != null) {
				MMeetingNoticeRelation mMeetingNoticeRelation = meetingNoticeRelationList.get(position);
				final Long meetingId = mMeetingNoticeRelation.getMeetingId();
				if (options == null) {
					options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.meeting_notice_logo_a).showImageForEmptyUri(R.drawable.meeting_notice_logo_a)
							.showImageOnFail(R.drawable.meeting_notice_logo_a).cacheInMemory(true).cacheOnDisc(true).considerExifParams(false).build();
				}
				if (animateFirstDisplayListener == null) {
					animateFirstDisplayListener = new AnimateFirstDisplayListener();
				}
				TreeSet<MMeetingNoticeQuery> meetingNoticeQuery = mMeetingNoticeRelation.getMeetingNoticeQuery();
				int count = getNoticeCount(meetingNoticeQuery);
				/** 遍历meetingNoticeQuery，获取到isRead == 0(未读)的数量 */
				if (count > 0) {
					viewHolder.noticeDetialCountFL.setVisibility(View.VISIBLE);
					viewHolder.countTv.setText(count + "");
				} else {
					viewHolder.noticeDetialCountFL.setVisibility(View.GONE);
				}
		
					ImageLoader.getInstance().displayImage(mMeetingNoticeRelation.getMeetingPic(), viewHolder.meetingIv, options, animateFirstDisplayListener);


				viewHolder.meetingTitleTv.setTextColor(Color.BLACK);
				viewHolder.meetingTitleTv.setText(mMeetingNoticeRelation.getMeetingName());
				SpannableString meetingOperateStr = getMeetingOperateStr(mMeetingNoticeRelation.getMeetingNoticeQuery());
				viewHolder.meetingOperateTv.setText(meetingOperateStr);
				viewHolder.meetingTimeTv.setText(formartTime(mMeetingNoticeRelation.getUpdateTime()));
				if (showChectBox) {
					viewHolder.meetingNoticeCb.setVisibility(View.VISIBLE);
					if (deleteNoticeMap.containsValue(meetingId)) {
						viewHolder.meetingNoticeCb.setChecked(true);
					}else {
						viewHolder.meetingNoticeCb.setChecked(false);
					}
				} else {
					viewHolder.meetingNoticeCb.setVisibility(View.GONE);
				}
//				viewHolder.meetingNoticeCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//						if (isChecked) {
//							if (!deleteNoticeMap.containsValue(meetingId)) {
//								deleteNoticeMap.put(position, meetingId);
//							}
//						} else {
//							if (deleteNoticeMap.containsValue(meetingId)) {
//								deleteNoticeMap.remove(position);
//							}
//						}
//					}
//				});
				viewHolder.meetingNoticeCb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!deleteNoticeMap.containsValue(meetingId)) {
							deleteNoticeMap.put(position, meetingId);
						}else {
							deleteNoticeMap.remove(position);
						}
						notifyDataSetChanged();
					}
				});
			}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return convertView;
		}

	}

	class ViewHolder {
		CircleImageView meetingIv;
		TextView meetingTitleTv;
		TextView meetingOperateTv;
		TextView meetingTimeTv;
		CheckBox meetingNoticeCb;
		FrameLayout noticeDetialCountFL;
		TextView countTv;
	}

	/** 格式化日期 */
	private String formartTime(String str) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm");
		try {
			if (false == str.isEmpty()) {
				Date tmp = (fmt.parse(str));
				return dateFormat.format(tmp);
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return "";
	}

	/** 长按显示选择按钮 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if (position!=0) {
			if (showChectBox) {
				return false;
			}

			if (deleteNoticeMap == null) {
				deleteNoticeMap = new HashMap<Integer, Long>();
			} else {
				deleteNoticeMap.clear();
			}
			long meetingId = adapter.getItemId(position-1);
			if (meetingId != 0) {
				deleteNoticeMap.put(position-1, meetingId);
			}
			deleteButton.setVisibility(View.VISIBLE);
			lp.setMargins(0, 0, 0, DisplayUtil.dip2px(mContext, 50));
			listView.setLayoutParams(lp);
			showSelectOrCancleAllMenuItem(true);
			showChectBox = true;
			adapter.notifyDataSetChanged();
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position!=0) {

			MMeetingNoticeRelation mMeetingNoticeRelation = adapter.getItem(position-1);
			if (mMeetingNoticeRelation != null) {
				Intent intent = new Intent();
				intent.setClass(mContext, MeetingNoticeDetialsActivity.class);
				intent.putExtra("MMeetingNoticeRelation", mMeetingNoticeRelation);
				intent.putExtra("position", position-1);
				startActivityForResult(intent, 10000);
			}
		
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.deleteButton:
			// 删除
			StringBuffer sb = new StringBuffer();
			int i = 0;
			Iterator<Entry<Integer, Long>> iterator = deleteNoticeMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Long> entry = iterator.next();
				Long id = entry.getValue();
				if (id != 0) {
					if (i == deleteNoticeMap.size() - 1) {
						sb.append(id + "");
					} else {
						sb.append(id + ",");
					}
					i++;
				}
			}
			if (!StringUtils.isEmpty(sb.toString())) {
				// 删除
				showLoadingDialog();
				ConferenceReqUtil.doDeleteNotice(mContext, this, sb.toString(), 0, null);
				IMReqUtil.doclearUnreadMessageNumber(mContext, MeetingNoticeActivity2.this, Long.valueOf(App.getUserID()), 0, 0, 5, null, false);
			}
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (showChectBox) {
			selectAll.setVisible(false);
			cancleAll.setVisible(false);
			showChectBox = false;
			isChectAll = false;
			deleteButton.setVisibility(View.GONE);
			lp.setMargins(0, 0, 0, 0);
			listView.setLayoutParams(lp);
			adapter.notifyDataSetChanged();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		listView.stopRefresh();
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_NOYICE && null != object) {
			meetingNoticeRelationList = (ArrayList<MMeetingNoticeRelation>) object;
			if (null == meetingNoticeRelationList || meetingNoticeRelationList.size() <= 0) {
				return;
			}
			adapter.notifyDataSetChanged();
			MeetingNoticeActivity2.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					int numCount = 0 ;
					for (int i = 0; meetingNoticeRelationList != null && i < meetingNoticeRelationList.size(); i++) {
						TreeSet<MMeetingNoticeQuery> meetingNoticeQuery = meetingNoticeRelationList.get(i).getMeetingNoticeQuery();
						int count = getNoticeCount(meetingNoticeQuery);
						numCount += count;
					}
					//写入数
					SharedPreferences mySharedPreferences= getSharedPreferences(GlobalVariable.SHARED_PREFERENCES_NOTICE_NEW_COUNT, 
					MeetingNoticeActivity2.MODE_PRIVATE);
					SharedPreferences.Editor editor = mySharedPreferences.edit();
					editor.putInt(GlobalVariable.NOTICE_NEW_COUNT_KEY, numCount);
					editor.commit();
			}});
			
		} else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_NOTICE && null != object) {
			boolean b = (Boolean) object;
			if (b) {// 删除成功
				Iterator<Entry<Integer, Long>> iterator = deleteNoticeMap.entrySet().iterator();
				ArrayList<MMeetingNoticeRelation> temps = new ArrayList<MMeetingNoticeRelation>();
				while (iterator.hasNext()) {
					Entry<Integer, Long> entry = iterator.next();
					Integer position = entry.getKey();
					temps.add(meetingNoticeRelationList.get(position));
				}
				meetingNoticeRelationList.removeAll(temps);
				deleteNoticeMap.clear();
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			ArrayList<MMeetingNoticeRelation> list = (ArrayList<MMeetingNoticeRelation>) data.getSerializableExtra("MeetingNoticeRelationList");
			int position = data.getIntExtra("position", -1);
			if (list != null && list.size() > 0) {
				// 直接整个列表更新
				meetingNoticeRelationList.clear();
				meetingNoticeRelationList.addAll(list);
				adapter.notifyDataSetChanged();
			} else if (position != -1) {
				// 对应位置的通知的 "isRead":"是否已读取 0：未读，1：已读",标记为已读，再更新列表（只进行局部更新）
				MMeetingNoticeRelation mMeetingNoticeRelation = meetingNoticeRelationList.get(position);
				if (mMeetingNoticeRelation != null && mMeetingNoticeRelation.getMeetingNoticeQuery() != null) {
					TreeSet<MMeetingNoticeQuery> meetingNoticeQueryTreeSet = mMeetingNoticeRelation.getMeetingNoticeQuery();
					Iterator<MMeetingNoticeQuery> iteratorSet = meetingNoticeQueryTreeSet.iterator();
					while (iteratorSet.hasNext()) {
						MMeetingNoticeQuery meetingNoticeQuery = iteratorSet.next();
						if (meetingNoticeQuery != null) {
							meetingNoticeQuery.setIsRead(1);
						}
					}
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	/** 获取到未读通知总数 */
	public int getNoticeCount(TreeSet<MMeetingNoticeQuery> meetingNoticeQuery) {
		int count = 0;
		Iterator<MMeetingNoticeQuery> iteratorSet = meetingNoticeQuery.iterator();
		while (iteratorSet.hasNext()) {
			MMeetingNoticeQuery item = iteratorSet.next();
			if (item != null) {
				if (item.getIsRead() == 0) {
					count++;
				}
			}
		}
		return count;
	}

	/** 获取到ListView 子布局中对操作的描述项 */
	public SpannableString getMeetingOperateStr(TreeSet<MMeetingNoticeQuery> meetingNoticeQuery) {
		String meetingOperateStr = "";
		SpannableString spannableString = null;
		Iterator<MMeetingNoticeQuery> iteratorSet = meetingNoticeQuery.iterator();
		ArrayList<MMeetingNoticeQuery> mMeetingNoticeQueryList = new ArrayList<MMeetingNoticeQuery>();
		while (iteratorSet.hasNext()) {
			MMeetingNoticeQuery item = iteratorSet.next();
			if (item != null) {
				mMeetingNoticeQueryList.add(item);
			}
		}
		MMeetingNoticeQuery mMeetingNoticeQuery = mMeetingNoticeQueryList.get(0);
		if (mMeetingNoticeQuery != null) {
			String name = App.getUserID().equals(mMeetingNoticeQuery.getCreateId() + "") ? "您" : mMeetingNoticeQuery.getCreateName();
			Integer noticeType = mMeetingNoticeQuery.getNoticeType();
			switch (noticeType) {
			case 0:
				List<String> listMeetingField = mMeetingNoticeQuery.getListMeetingField();
				String meetingFiled = "";
				if (listMeetingField!=null&&listMeetingField.size()>0) {
					for (int i = 0; i < listMeetingField.size(); i++) {
						String string = listMeetingField.get(i);
						if (!StringUtils.isEmpty(string)) {
							if (i<listMeetingField.size()-1) {
								meetingFiled = meetingFiled+string+"、";
							}else {
								meetingFiled = meetingFiled+string;
							}
						}
					}
				}
				meetingOperateStr = name + "修改了会议" +"  " +meetingFiled;
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, name.length() + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spannableString.setSpan(new ForegroundColorSpan(Color.LTGRAY), name.length() + 5, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 1:
				meetingOperateStr = name + "申请报名";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 2:
				meetingOperateStr = name + "通过了您的报名";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 3:
				meetingOperateStr = name + "拒绝了您的报名";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 4:
				meetingOperateStr = name + "接受了您的邀请";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 5:
				meetingOperateStr = name + "拒绝了您的邀请";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 6:
				meetingOperateStr = name + "同意了" + "  " + mMeetingNoticeQuery.getAttendMeetingName() + "的报名";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, name.getBytes().length + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spannableString.setSpan(new ForegroundColorSpan(Color.LTGRAY), name.getBytes().length + 3, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 7:
				meetingOperateStr = name + "拒绝了" + "  " + mMeetingNoticeQuery.getNoticeContent() + "的报名";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, name.getBytes().length + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spannableString.setSpan(new ForegroundColorSpan(Color.LTGRAY), name.getBytes().length + 3, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 8:
				meetingOperateStr = name + "取消了参会";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 9:
				meetingOperateStr = name + "新增了议题";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 10:
				meetingOperateStr = name + "修改了议题";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 11:
				meetingOperateStr = name + "删除了议题";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 12:
				meetingOperateStr = name + "退出了会议";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 13:
				meetingOperateStr = name + "取消了报名";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			case 14:
				meetingOperateStr = name + "删除了会议";
				spannableString = new SpannableString(meetingOperateStr);
				spannableString.setSpan(new ForegroundColorSpan(Color.rgb(20, 155, 245)), 0, meetingOperateStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				break;
			}
		}
		return spannableString;
	}
}