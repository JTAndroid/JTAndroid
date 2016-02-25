package com.tr.ui.conference.square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MMeetingData;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.conference.common.BaseActivity;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 
 * @author sunjianan
 * 
 */
public class RequirmentDataActivity extends BaseActivity implements OnClickListener, IBindData {

	@ViewInject(R.id.hy_meeting_data_requirment_titlebar)
	private FrameLayout layoutTitle;
	// 标题返回键
	private LinearLayout backBtn;
	// 标题题目
	private TextView titleTv;

	private ViewHolder holder;

	// --------------------需求操作栏 start
	@ViewInject(R.id.hy_meeting_data_requirment_toolbar)
	private RelativeLayout requirmentToolbar;

	private TextView editBtn;
	private TextView cancelBtn;
	private TextView selectBtn;
	private ImageView transmitBtn;
	private ImageView enshrienBtn;

	// --------------------需求操作栏 end

	@ViewInject(R.id.hy_meeting_data_requirment__lv)
	private ListView relationLv;

	private MMeetingQuery meeting;

	private ListMeetingDataRequirmentAdapter adapter;

	// 当前的编辑状态
	private boolean isEdit = false;

	// 已经选中的 需求条目的 状态的集合
	/**
	 * Long --> meetingId Boolean --> 当前会议的选中状态
	 */
	private Map<Long, MMeetingData> selectedItems = new HashMap<Long, MMeetingData>();
	private boolean isSecrecy = false;

	private MMeetingMember member = null;

	// 初始化布局
	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_meeting_requirment_data);
		ViewUtils.inject(this);

		Intent relationshipIntent = getIntent();
		meeting = (MMeetingQuery) relationshipIntent.getSerializableExtra(ENavConsts.EMeetingDetail);

		for (MMeetingMember mem : meeting.getListMeetingMember()) {
			if (App.getUserID().equals("" + (mem.getMemberId()))) {
				member = mem;
				break;
			}
		}

		backBtn = (LinearLayout) layoutTitle.findViewById(R.id.hy_layoutTitle_backBtn);
		titleTv = (TextView) layoutTitle.findViewById(R.id.hy_layoutTitle_title);

		// --------------------需求操作栏
		// 初始化hy_list_item_meeting_regist_data_requirment_toolbar
		editBtn = (TextView) requirmentToolbar.findViewById(R.id.hy_meeting_data_knowledge_edit_normal);
		cancelBtn = (TextView) requirmentToolbar.findViewById(R.id.hy_meeting_data_cancel);
		selectBtn = (TextView) requirmentToolbar.findViewById(R.id.hy_meeting_data_select);
		transmitBtn = (ImageView) requirmentToolbar.findViewById(R.id.hy_meeting_data_transmit);
		enshrienBtn = (ImageView) requirmentToolbar.findViewById(R.id.hy_meeting_data_enshrien);

		// 获取当前保密状态
		isSecrecy = meeting.getIsSecrecy();
		// 更改当前工具栏
		changeToolBar(isEdit);

		adapter = new ListMeetingDataRequirmentAdapter();

		if (meeting != null && meeting.getListMeetingRequirement() != null) {
			relationLv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					// 判断当前选中的状态
					// //判断当前条目的状态:1.未选中-->加入集合-->设置为选中 2.选中-->移出集合-->设置为非选中状态
					if (isEdit) {
						// 判断当前条目的状态:1.未选中-->加入集合 2.选中-->移出集合
						if (selectedItems.containsKey(meeting.getListMeetingRequirement().get(position).getDataId())) {
							// 1
							selectedItems.remove(meeting.getListMeetingRequirement().get(position).getDataId());
						} else {
							// 2
							// selectedItems.clear();
							selectedItems.put(meeting.getListMeetingRequirement().get(position).getDataId(),
									meeting.getListMeetingRequirement().get(position));
						}

						adapter.notifyDataSetChanged();
					} else {
						// 非编辑状态跳转到对应的详情页面
					}
				}
			});
		}
		relationLv.setAdapter(adapter);
		setListener();
	}

	private void setListener() {
		// 后退按钮
		backBtn.setOnClickListener(this);
		// 编辑按钮
		editBtn.setOnClickListener(this);
		// 取消编辑
		cancelBtn.setOnClickListener(this);
		// 选择
		selectBtn.setOnClickListener(this);
		// 转发
		transmitBtn.setOnClickListener(this);
		// 收藏
		enshrienBtn.setOnClickListener(this);
	}

	@Override
	public void initData() {
		titleTv.setText("需求");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 后退键
		case R.id.hy_layoutTitle_backBtn:
			finish();
			break;
		// 编辑
		case R.id.hy_meeting_data_knowledge_edit_normal:
			isEdit = true;
			changeToolBar(isEdit);
			adapter.notifyDataSetChanged();
			break;
		// 取消编辑
		case R.id.hy_meeting_data_cancel:
			isEdit = false;
			changeToolBar(isEdit);
			adapter.notifyDataSetChanged();
			break;
		case R.id.hy_meeting_data_select:
			if (meeting != null && meeting.getListMeetingRequirement() != null) {
				for (MMeetingData data : meeting.getListMeetingRequirement()) {
					selectedItems.put(data.getDataId(), data);
				}
				adapter.notifyDataSetChanged();
			}
			break;
		// 转发
		case R.id.hy_meeting_data_transmit:
			if (selectedItems.size() == 0) {
				Toast.makeText(this, "请至少选中一个需求", 0).show();
				break;
			}
			showTransmitDialog();
			break;
		// 关注
		case R.id.hy_meeting_data_enshrien:
			// TODO
			Iterator<Entry<Long, MMeetingData>> iterator = selectedItems.entrySet().iterator();
			total = selectedItems.size();
			while (iterator.hasNext()) {
				Map.Entry<Long, MMeetingData> entry = iterator.next();
				MMeetingData data = entry.getValue();
				ConferenceReqUtil.doFocusRequirement(this, this, data.getDataId(), null);
			}
			if (selectedItems.size() == 0) {
				Toast.makeText(this, "请至少选中一个需求", 0).show();
				break;
			}
			break;
		}
	}

	int total = 0;
	int successed = 0;
	int failure = 0;

	/**
	 * 改变工具栏的显示
	 * 
	 * @param isEdit
	 */
	private void changeToolBar(boolean isEdit) {
		if (isEdit) {
			// 编辑状态
			editBtn.setVisibility(View.GONE);
			cancelBtn.setVisibility(View.VISIBLE);
			selectBtn.setVisibility(View.VISIBLE);
			transmitBtn.setVisibility(View.VISIBLE);
			enshrienBtn.setVisibility(View.VISIBLE);
		} else {
			// 正常
			editBtn.setVisibility(View.VISIBLE);
			cancelBtn.setVisibility(View.GONE);
			selectBtn.setVisibility(View.GONE);
			transmitBtn.setVisibility(View.GONE);
			enshrienBtn.setVisibility(View.GONE);
		}
		// 保密状态
		if (isSecrecy) {
			editBtn.setVisibility(View.GONE);
			cancelBtn.setVisibility(View.GONE);
			selectBtn.setVisibility(View.GONE);
			transmitBtn.setVisibility(View.GONE);
			enshrienBtn.setVisibility(View.GONE);
			return;
		}
		// 没有参会 没有接受邀请 不是发起人 gone掉
		if (member == null
				|| !(App.getUserID().equals("" + meeting.getCreateId()) || (member.getAttendMeetType() == 0 && member.getAttendMeetStatus() == 1) || (member
						.getAttendMeetType() == 1 && member.getAttendMeetStatus() == 4 && member.getExcuteMeetSign() == 1))) {
			editBtn.setVisibility(View.GONE);
			return;
		}

		if (App.getUserID().equals("" + meeting.getCreateId())) {
			enshrienBtn.setVisibility(View.GONE);
		}
	}

	/** --------adapter--------- */

	private class ListMeetingDataRequirmentAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (meeting != null && meeting.getListMeetingRequirement() != null) {
				return meeting.getListMeetingRequirement().size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return meeting.getListMeetingRequirement().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			holder = null;
			if (convertView == null) {
				convertView = View.inflate(RequirmentDataActivity.this, R.layout.hy_list_item_meeting_regist_data_requirment, null);
				holder = new ViewHolder();
				holder.unselectedImg = (ImageView) convertView.findViewById(R.id.hy_meeting_data_checkbox_unselected);
				holder.selectedImg = (ImageView) convertView.findViewById(R.id.hy_meeting_data_checkbox_selected);
				holder.financingImg = (ImageView) convertView.findViewById(R.id.hy_meeting_data_financing);

				holder.dataName = (TextView) convertView.findViewById(R.id.hy_meeting_data_name);
				holder.dataTime = (TextView) convertView.findViewById(R.id.hy_meeting_data_time);
				holder.fileSize = (TextView) convertView.findViewById(R.id.hy_meeting_data_file_size);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// ------------------------------------------------------------------------------------------------
			// 1.判断当前的状态 如果是编辑状态,点击条目-->加入链表-->当前选中状态设置为选中
			// 如果不是编辑状态,点击条目-->跳转到详情界面

			// 1.1获取需求Id
			final long requirmentId = meeting.getListMeetingRequirement().get(position).getDataId();
			// 1.2创建final holder
			if (isEdit) {
				// 判断当前条目的状态:1.集合中存在-->设置为选中 2.集中不存在-->设置为非选中状态
				if (selectedItems.containsKey(requirmentId)) {
					// 已选-->更改编辑状态为未选中
					holder.unselectedImg.setVisibility(View.GONE);
					holder.selectedImg.setVisibility(View.VISIBLE);
				} else {
					// 未选中-->更改编辑状态为选中
					holder.unselectedImg.setVisibility(View.VISIBLE);
					holder.selectedImg.setVisibility(View.GONE);
				}
			} else {
				// 如果不是编辑状态,全部隐藏
				holder.unselectedImg.setVisibility(View.GONE);
				holder.selectedImg.setVisibility(View.GONE);
			}
			// ----------------------------------------------------------------------------------------------------------

			if (meeting.getListMeetingData().get(position).getDataReqType() == 12) {
				holder.financingImg.setBackgroundResource(R.drawable.hy_icon_meeting_detail_invest);
			} else if (meeting.getListMeetingData().get(position).getDataReqType() == 13) {
				holder.financingImg.setBackgroundResource(R.drawable.hy_icon_meeting_detail_finacing);
			}
			holder.dataName.setText(meeting.getListMeetingRequirement().get(position).getDataName());
			holder.dataTime.setText(meeting.getListMeetingRequirement().get(position).getCreateTime().split(" ")[0]);
			// holder.fileSize.setText(meeting.getListMeetingRequirement().get(position).get)

			return convertView;
		}

	}

	class ViewHolder {
		ImageView unselectedImg;
		ImageView selectedImg;
		ImageView financingImg;
		TextView dataName;
		TextView dataTime;
		TextView fileSize;
	}

	/**
	 * 转发到
	 */
	private void showTransmitDialog() {
		final Dialog dialog = new Dialog(this, R.style.transmeeting_dialog);
		View dialogBg = View.inflate(this, R.layout.hy_dialog_meeting_regist, null);

		dialog.setContentView(dialogBg);

		// 转发到会议
		((TextView) dialogBg.findViewById(R.id.transmit_to_meeting_tv)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(SquareActivity.this, "转发到会议", 0).show();
				// Intent trans2Meeting = new
				// Intent(RequirmentDataActivity.this,
				// TansmitMeetingListActivity.class);
				// startActivity(trans2Meeting);
				ArrayList<JTFile> jtFiles = new ArrayList<JTFile>();
				Iterator<Entry<Long, MMeetingData>> iterator = selectedItems.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<Long, MMeetingData> entry = iterator.next();
					MMeetingData requirmentData = entry.getValue();
					JTFile jtFile = new JTFile();
					jtFile.mFileName = requirmentData.getDataName();
					// 需求内容
					// jtFile.mSuffixName = knowledgeData.getDataName();
					jtFile.mType = 11;
					jtFile.mTaskId = String.valueOf(requirmentData.getDataId());
					jtFile.reserved1 = requirmentData.getCreateTime();
					jtFiles.add(jtFile);
				}
				ENavigate.startTransmitMeetingList(RequirmentDataActivity.this, jtFiles, meeting.getId());
				dialog.dismiss();
			}
		});

		// 转发到畅聊
		((TextView) dialogBg.findViewById(R.id.transmit_to_chat_tv)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<JTFile> jtFiles = new ArrayList<JTFile>();
				Iterator<Entry<Long, MMeetingData>> iterator = selectedItems.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<Long, MMeetingData> entry = iterator.next();
					MMeetingData requirmentData = entry.getValue();
					JTFile jtFile = new JTFile();
					jtFile.mFileName = requirmentData.getDataName();
					jtFile.mSuffixName = "";
					jtFile.mType = JTFile.TYPE_REQUIREMENT;
					jtFile.mTaskId = String.valueOf(requirmentData.getDataId());
					jtFile.reserved1 = requirmentData.getCreateTime();
					jtFiles.add(jtFile);
				}
				ENavigate.startShareActivity(RequirmentDataActivity.this, 11, jtFiles);
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_FOCUSREQUIREMENT && object != null) {
			SimpleResult result = (SimpleResult) object;
			if (result.isSucceed()) {
				successed++;
			} else {
				failure++;
			}
			// "共上传"+total+ "条" + ": " + successed + "成功-" +
			if (successed == total) {
				Toast.makeText(RequirmentDataActivity.this, "关注成功", 1).show();
			} else if (successed + failure == total) {
				Toast.makeText(RequirmentDataActivity.this, failure + "条未关注成功", 1).show();
			}
		}

	}
}
