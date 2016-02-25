package com.tr.ui.conference.square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import android.widget.Button;
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
public class KnowLedgeDataActivity extends BaseActivity implements OnClickListener, IBindData {

	@ViewInject(R.id.hy_meeting_data_knowledge_titlebar)
	private FrameLayout layoutTitle;
	// 标题返回键
	private LinearLayout backBtn;
	// 标题题目
	private TextView titleTv;

	private ViewHolder holder;

	// --------------------需求操作栏 start
	@ViewInject(R.id.hy_meeting_data_knowledge_toolbar)
	private RelativeLayout knowledgeToolbar;

	private TextView editBtn;
	private Button cancelBtn;
	private Button selectBtn;
	private ImageView transmitBtn;
	private ImageView focusBtn;

	// --------------------需求操作栏 end

	@ViewInject(R.id.hy_meeting_data_knowledge__lv)
	private ListView relationLv;

	private MMeetingQuery meeting;

	private ListMeetingDataknowledgeAdapter adapter;

	private MMeetingMember member = null;

	// 当前的编辑状态
	private boolean isEdit = false;

	// 已经选中的 需求条目的 状态的集合
	/**
	 * Long --> meetingId Boolean --> 当前会议的选中状态
	 */
	private Map<Long, MMeetingData> selectedItems = new HashMap<Long, MMeetingData>();
	private Boolean isSecrecy = false;

	// 初始化布局
	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_meeting_knowledge_data);
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

		// --------------------需求操作栏 初始化
		editBtn = (TextView) knowledgeToolbar.findViewById(R.id.hy_meeting_data_knowledge_edit_normal);
		cancelBtn = (Button) knowledgeToolbar.findViewById(R.id.hy_meeting_data_cancel);
		selectBtn = (Button) knowledgeToolbar.findViewById(R.id.hy_meeting_data_select);
		transmitBtn = (ImageView) knowledgeToolbar.findViewById(R.id.hy_meeting_data_transmit);
		focusBtn = (ImageView) knowledgeToolbar.findViewById(R.id.hy_meeting_data_focus);

		// 获取当前保密状态
		isSecrecy = meeting.getIsSecrecy();
		// 更改当前工具栏
		changeToolBar(isEdit);

		adapter = new ListMeetingDataknowledgeAdapter();

		if (meeting != null && meeting.getListMeetingKnowledge() != null) {
			relationLv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					// 判断当前选中的状态
					// //判断当前条目的状态:1.未选中-->加入集合-->设置为选中 2.选中-->移出集合-->设置为非选中状态
					if (isEdit) {
						// 判断当前条目的状态:1.未选中-->加入集合 2.选中-->移出集合
						if (selectedItems.containsKey(meeting.getListMeetingKnowledge().get(position).getDataId())) {
							// 1
							selectedItems.remove(meeting.getListMeetingKnowledge().get(position).getDataId());
						} else {
							// 2
							// selectedItems.clear();
							selectedItems.put(meeting.getListMeetingKnowledge().get(position).getDataId(),
									meeting.getListMeetingKnowledge().get(position));
						}
						adapter.notifyDataSetChanged();
					} else {
						// 非编辑状态跳转到对应的详情页面
						// ENavigate.startKnowledgeDetailActivity(KnowLedgeDataActivity.this,
						// String.valueOf(meeting.getListMeetingKnowledge().get(position).getDataId()));
						ENavigate.startKnowledgeOfDetailActivity(KnowLedgeDataActivity.this, meeting.getListMeetingKnowledge().get(position)
								.getDataId(), meeting.getListMeetingKnowledge().get(position).getDataReqType());

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
		focusBtn.setOnClickListener(this);
	}

	@Override
	public void initData() {
		titleTv.setText("知识");
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
			if (meeting != null && meeting.getListMeetingKnowledge() != null) {
				for (MMeetingData data : meeting.getListMeetingKnowledge()) {
					selectedItems.put(data.getDataId(), data);
				}
				adapter.notifyDataSetChanged();
			}
			break;
		// 转发
		case R.id.hy_meeting_data_transmit:
			if (selectedItems.size() == 0) {
				Toast.makeText(this, "请至少选中一个知识", 0).show();
				break;
			}
			showTransmitDialog();
			break;
		// 收藏
		case R.id.hy_meeting_data_focus:
			// TODO 收藏到哪?
			Iterator<Entry<Long, MMeetingData>> iterator = selectedItems.entrySet().iterator();
			total = selectedItems.size();
			while (iterator.hasNext()) {
				Map.Entry<Long, MMeetingData> entry = iterator.next();
				MMeetingData data = entry.getValue();
				ConferenceReqUtil.doCollectKnowledge(this, this, data.getDataId(), data.getDataType(), false, null);
			}
			if (selectedItems.size() == 0) {
				Toast.makeText(this, "请至少选中一个知识", 0).show();
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
			focusBtn.setVisibility(View.VISIBLE);
		} else {
			// 正常
			editBtn.setVisibility(View.VISIBLE);
			cancelBtn.setVisibility(View.GONE);
			selectBtn.setVisibility(View.GONE);
			transmitBtn.setVisibility(View.GONE);
			focusBtn.setVisibility(View.GONE);
		}
		// 保密状态
		if (isSecrecy) {
			editBtn.setVisibility(View.GONE);
			cancelBtn.setVisibility(View.GONE);
			selectBtn.setVisibility(View.GONE);
			transmitBtn.setVisibility(View.GONE);
			focusBtn.setVisibility(View.GONE);
			return;
		}

		if (member == null
				|| !(App.getUserID().equals("" + meeting.getCreateId()) || (member.getAttendMeetType() == 0 && member.getAttendMeetStatus() == 1) || (member
						.getAttendMeetType() == 1 && member.getAttendMeetStatus() == 4 && member.getExcuteMeetSign() == 1))) {
			editBtn.setVisibility(View.GONE);
		}

		if (App.getUserID().equals("" + meeting.getCreateId())) {
			focusBtn.setVisibility(View.INVISIBLE);
		}
	}

	/** --------adapter--------- */

	private class ListMeetingDataknowledgeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (meeting != null && meeting.getListMeetingKnowledge() != null) {
				return meeting.getListMeetingKnowledge().size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return meeting.getListMeetingKnowledge().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			holder = null;
			if (convertView == null) {
				convertView = View.inflate(KnowLedgeDataActivity.this, R.layout.hy_list_item_meeting_regist_data_knowledge, null);
				holder = new ViewHolder();
				holder.unselectedImg = (ImageView) convertView.findViewById(R.id.hy_meeting_data_checkbox_unselected);
				holder.selectedImg = (ImageView) convertView.findViewById(R.id.hy_meeting_data_checkbox_selected);
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
			final long knowledgeId = meeting.getListMeetingKnowledge().get(position).getDataId();
			// 1.2创建final holder

			if (isEdit) {
				// 判断当前条目的状态:1.集合中存在-->设置为选中 2.集中不存在-->设置为非选中状态
				if (selectedItems.containsKey(knowledgeId)) {
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

			holder.dataName.setText(meeting.getListMeetingKnowledge().get(position).getDataName());
			holder.dataTime.setText(meeting.getListMeetingKnowledge().get(position).getCreateTime().split(" ")[0]);
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
				// Intent trans2Meeting = new Intent(KnowLedgeDataActivity.this,
				// TansmitMeetingListActivity.class);
				// startActivity(trans2Meeting);
				ArrayList<JTFile> jtFiles = new ArrayList<JTFile>();
				Iterator<Entry<Long, MMeetingData>> iterator = selectedItems.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<Long, MMeetingData> entry = iterator.next();
					MMeetingData knowLedgeData = entry.getValue();
					JTFile jtFile = new JTFile();
					jtFile.mUrl = knowLedgeData.getDataUrl();
					// jtFile.mSuffixName = knowLedgeData.getDataName();
					jtFile.mType = JTFile.TYPE_KNOWLEDGE2;
					jtFile.mTaskId = String.valueOf(knowLedgeData.getDataId());
					jtFile.reserved1 = String.valueOf(knowLedgeData.getDataReqType());
					jtFile.reserved2 = knowLedgeData.getDataName();
					jtFiles.add(jtFile);
				}
				ENavigate.startTransmitMeetingList(KnowLedgeDataActivity.this, jtFiles, meeting.getId());
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
					MMeetingData knowLedgeData = entry.getValue();
					JTFile jtFile = new JTFile();
					// jtFile.mFileName = knowLedgeData.getDataName();
					jtFile.mUrl = knowLedgeData.getDataUrl();
					// jtFile.mSuffixName = knowLedgeData.getDataName();
					jtFile.mType = JTFile.TYPE_KNOWLEDGE2;
					jtFile.mTaskId = String.valueOf(knowLedgeData.getDataId());
					jtFile.reserved1 = String.valueOf(knowLedgeData.getDataReqType());
					jtFile.reserved2 = knowLedgeData.getDataName();
					jtFiles.add(jtFile);
				}
				// TODO
				ENavigate.startShareActivity(KnowLedgeDataActivity.this, 13, jtFiles);
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_COLLECTKNOWLEDGE && object != null) {
			SimpleResult result = (SimpleResult) object;
			if (result.isSucceed()) {
				successed++;
			} else {
				failure++;
			}
			// "共上传" + total + "条" + ": " + successed + "成功-" +
			if (successed == total) {
				Toast.makeText(KnowLedgeDataActivity.this, "收藏成功", 1).show();
			} else if (successed + failure == total) {
				Toast.makeText(KnowLedgeDataActivity.this, failure + "条未收藏成功", 1).show();
			}
		}
	}
}
