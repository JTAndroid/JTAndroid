package com.tr.ui.conference.home;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.baidumapsdk.BaiduLoc;
import com.tr.model.conference.MeetingOfMineListQuery;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.conference.initiatorhy.InitiatorHYActivity;
import com.tr.ui.conference.myhy.utils.Utils;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.MeetingPiazzaPopupWindow;
import com.tr.ui.widgets.MeetingPiazzaPopupWindow.OnListViewItemSelectClickListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;
import com.utils.string.StringUtils;

/**
 * 新 会议广场
 * 
 * @author zhongshan
 * 
 */
public class MeetingPiazzaActivity extends JBaseActivity implements OnCheckedChangeListener, OnListViewItemSelectClickListener, OnDismissListener, IBindData, IXListViewListener, OnItemClickListener {

	private Context mContext;
	private int popTimeSelectPosition;
	private int popLocationSelectPosition;
	private int popIndrustrySelectPosition;
	private String time = "all";
	private String location = "";
	private String industry = "";
	private String keyWord = "";
	private int index;
	/** 会议列表 */
	private ArrayList<MeetingOfMineListQuery> meetinglist = new ArrayList<MeetingOfMineListQuery>();

	private AnimateFirstDisplayListener animateFirstDisplayListener;
	private DisplayImageOptions option;

	/** 时间选择按钮 */
	private RadioButton timeRB;
	/** 地点选择按钮 */
	private RadioButton addressRB;
	/** 行业选择按钮 */
	private RadioButton industryRB;
	private XListView listView;
	private RadioButton checkRB;
	private MeetingPiazzaPopupWindow popupWindow;
	private View anchor;
	/**请求中 position参数*/
	private String aParm;
	private MyMeetingAdapter adapter;
	private String city;

	public enum TypeTitle {
		time, address, industry
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "活动", false, null,true, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_piazza_layout);
		initView();
		initListener();
		initContent();
	}

	private void initView() {
		timeRB = (RadioButton) findViewById(R.id.timeRB);
		addressRB = (RadioButton) findViewById(R.id.addressRB);
		industryRB = (RadioButton) findViewById(R.id.industryRB);
		anchor = findViewById(R.id.anchor);
		listView = (XListView) findViewById(R.id.listview);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
	}

	private void initListener() {
		timeRB.setOnCheckedChangeListener(this);
		addressRB.setOnCheckedChangeListener(this);
		industryRB.setOnCheckedChangeListener(this);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
	}

	private void initContent() {
		BaiduLoc bdLoacation = App.getApp().getBaiduLoc();
		if (null != bdLoacation && bdLoacation.isLocationValid(bdLoacation.getLongitude(), bdLoacation.getLatitude())) {
			aParm = String.valueOf(bdLoacation.getLongitude()) + "," + String.valueOf(bdLoacation.getLatitude());
			BDLocation location2 = bdLoacation.getLocation();
			city = location2.getCity();
		}
		showLoadingDialog();
		ConferenceReqUtil.doGetMeetingPlazaList(mContext, this, App.getUserID(), time,location , industry, "" ,keyWord, 0, 10, null);
		adapter = new MyMeetingAdapter();
		listView.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_search, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.search_home) {
			ENavigate.startNewSearchActivity(this);
		}else if(item.getItemId() == R.id.create){
			 Intent intent = new Intent(MeetingPiazzaActivity.this, InitiatorHYActivity.class);
				startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	private class MyMeetingAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return meetinglist == null ? 0 : meetinglist.size();
		}

		@Override
		public Object getItem(int position) {
			return meetinglist == null ? null : meetinglist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.hy_mymeeting_frg_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.meetingIv = (ImageView) convertView.findViewById(R.id.meetingIv);
				viewHolder.meetingTitleTv = (TextView) convertView.findViewById(R.id.meetingTitleTv);
				viewHolder.meetingTimeTv = (TextView) convertView.findViewById(R.id.meetingTimeTv);
				viewHolder.meetingDesTv = (TextView) convertView.findViewById(R.id.meetingDesTv);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			MeetingOfMineListQuery meeting = meetinglist.get(position);
			if (meeting != null) {
				/** 会议状态 0：草稿，1：发起，2：会议进行中，3：会议结束 */
				int meetingStatus = meeting.getMeetingStatus();
				Drawable drawable = null;
				switch (meetingStatus) {
				case 0:
					drawable = getResources().getDrawable(R.drawable.hy_status_draft);
					break;
				case 1:
					drawable = getResources().getDrawable(R.drawable.hy_status_not_start);
					break;
				case 2:
					drawable = getResources().getDrawable(R.drawable.hy_status_doing);
					break;
				case 3:
					drawable = getResources().getDrawable(R.drawable.hy_status_done);
					break;
				}
				if (drawable != null) {
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					viewHolder.meetingTitleTv.setCompoundDrawablePadding(20);
					viewHolder.meetingTitleTv.setCompoundDrawables(drawable, null, null, null);
				}
				viewHolder.meetingTitleTv.setEllipsize(TruncateAt.END);
				viewHolder.meetingTitleTv.setTextColor(Color.BLACK);
				viewHolder.meetingTitleTv.setText(meeting.getMeetingName());
				viewHolder.meetingTimeTv.setText(Utils.parseTime(meeting.getStartTime()));
				if (StringUtils.isEmpty(meeting.getMeetingDesc())) {
					viewHolder.meetingDesTv.setVisibility(View.GONE);
				}else {
					viewHolder.meetingDesTv.setVisibility(View.VISIBLE);
					viewHolder.meetingDesTv.setText(meeting.getMeetingDesc());
				}
				if (option == null) {
					option = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.meeting_logo_a).showImageForEmptyUri(R.drawable.meeting_logo_a)
							.showImageOnFail(R.drawable.meeting_logo_a)/*.displayer(new RoundedBitmapDisplayer(5))*/.cacheInMemory(true).cacheOnDisc(true).considerExifParams(false).build();
				}
				if (animateFirstDisplayListener == null)
					animateFirstDisplayListener = new AnimateFirstDisplayListener();
				ImageLoader.getInstance().displayImage(meeting.getPath(), viewHolder.meetingIv, option, animateFirstDisplayListener);
			}
			return convertView;
		}

	}

	class ViewHolder {
		/** 会议封面图片 */
		ImageView meetingIv;
		/** 会议主题 */
		TextView meetingTitleTv;
		/** 会议时间 */
		TextView meetingTimeTv;
		/** 会议介绍 */
		TextView meetingDesTv;
	}

	/**
	 * RadioButton Check
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			checkRB = (RadioButton) buttonView;
			checkRB.setSelected(isChecked);
			switch (buttonView.getId()) {
			/** 时间 */
			case R.id.timeRB:
				showPropu(TypeTitle.time, popTimeSelectPosition);
				break;
			/** 地点 */
			case R.id.addressRB:
				showPropu(TypeTitle.address, popLocationSelectPosition);
				break;
			/** 行业 */
			case R.id.industryRB:
				showPropu(TypeTitle.industry, popIndrustrySelectPosition);
				break;
			}

		}
	}

	private void showPropu(final TypeTitle typeTitle, int position) {
		popupWindow = new MeetingPiazzaPopupWindow(mContext, typeTitle, position);
		popupWindow.showAsDropDown(anchor);
		popupWindow.setOnItemClickListener(this);
		popupWindow.setOnDismissListener(this);
	}

	/**
	 * popUpWindow 选择 回调
	 * 
	 * @param value
	 *            选择的值
	 * @param position
	 *            在pop中listView的位置
	 */
	@Override
	public void selectResult(TypeTitle typeTitle, String key ,String value, int position) {
		checkRB.setText(key);
		switch (typeTitle) {
		case time:
			time = value;
			popTimeSelectPosition = position;
			break;
		case address:
			location = value;
			popLocationSelectPosition = position;
			break;
		case industry:
			industry = value;
			popIndrustrySelectPosition = position;
			break;
		}
		String Currentposition = "";
		// 刷新数据
		if ("city".equals(location)||"other".equals(location)) {
			Currentposition = city;
		}else {
			location="";
			Currentposition = aParm ;
		}
		showLoadingDialog();
		ConferenceReqUtil.doGetMeetingPlazaList(mContext, this, App.getUserID(), time, location , industry, Currentposition ,keyWord, 0, 10, null);
		listView.setSelection(0);
//		showLongToast(value);
	}

	@Override
	public void onDismiss() {
		checkRB.setChecked(false);
		checkRB.setSelected(false);
	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		listView.stopLoadMore();
		listView.stopRefresh();
		if (object != null && tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_PLAZA) {
			Map<String, Object> dataMap = (Map<String, Object>) object;
			if (dataMap != null) {
				int total = (Integer) dataMap.get("total");
				index = (Integer) dataMap.get("index");
				controlXListBottom(total, index);
				ArrayList<MeetingOfMineListQuery> list = (ArrayList<MeetingOfMineListQuery>) dataMap.get("listMeetingMemberListQuery");
				if (index == 0) {
					meetinglist.clear();
				}
				if (list != null && list.size() > 0) {
					meetinglist.addAll(list);
					listView.setPullLoadEnable(true);
					if (index == 0&&list.size()<10) {
						listView.setPullLoadEnable(false);
					}
				}else {
					showToast("没有更多会议");
					listView.setPullLoadEnable(false);
				}
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onRefresh() {
		showLoadingDialog();
		ConferenceReqUtil.doGetMeetingPlazaList(mContext, this, App.getUserID(), time,location , industry, aParm ,keyWord, 0, 10, null);
	}

	@Override
	public void onLoadMore() {
		startGetData();
	}
	/**
	 * 获取页数据
	 */
	public void startGetData() {
		int nowIndex = 0;
		nowIndex = index + 1;
		showLoadingDialog();
		ConferenceReqUtil.doGetMeetingPlazaList(mContext, this, App.getUserID(), time, location, industry, aParm, keyWord, nowIndex, 10, null);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position!=0) {
			final MeetingOfMineListQuery meeting = meetinglist.get(position - 1);
			if (meeting.getMeetingStatus() == 0) {
				/** 获取详情，进入编辑页面 */
				ConferenceReqUtil.doGetMeetingDetail(mContext, this, meeting.getId(), App.getUserID(), null);
				showLoadingDialog();
			} else {
				/** 跳转详情页面 */
				ENavigate.startSquareActivity(mContext, meeting.getId(),0);
			}		
		}
	}
	
	/**
	 * 控制XListViewButtom
	 */
	private void controlXListBottom(int total,int index) {
		int totalPages;
		if ((total % 20) == 0) {
			totalPages = total / 20;
		} else {
			totalPages = total / 20 + 1;
		}
		if ((totalPages == 0) || (index >= totalPages)) {
			listView.setPullLoadEnable(false);
		} else {
			listView.setPullLoadEnable(true);
		}
	}
	
}
