package com.tr.ui.conference.im;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.db.MeetingRecordDBManager;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.MeetingMessage;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.image.LoadImage;

/**
 * 分会场聊天记录搜索
 * @author leon
 */
public class MChatRecordSearchActivity extends JBaseFragmentActivity implements OnClickListener, OnItemClickListener{

	private final String TAG = getClass().getSimpleName();
	
	private ListView recordLv; // 聊天记录列表
	private TextView tipTv; // 提示
	private EditText keywordEt; // 关键字
	private ImageView backIv; // 返回键
	
	private MeetingRecordDBManager dbManager;
	private MMeetingQuery meetingDetail; // 会议对象
	private MMeetingTopicQuery topicDetail; // 分会场对象
	private List<MeetingMessage> listMessage;
	private ChatRecordAdapter listAdapter;
	
	@Override
	public void initJabActionBar() {
		getActionBar().hide();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mchat_record_search);
		initVars();
		initControls();
	}
	
	// 初始化变量
	private void initVars(){
		meetingDetail = (MMeetingQuery) getIntent().getSerializableExtra(ENavConsts.EMeetingDetail);
		topicDetail = (MMeetingTopicQuery) getIntent().getSerializableExtra(ENavConsts.EMeetingTopicDetail);
		listMessage = new ArrayList<MeetingMessage>();
		dbManager = new MeetingRecordDBManager(this);
		listAdapter = new ChatRecordAdapter(this);
	}
	
	// 初始化控件
	private void initControls(){
		
		// 返回键
		backIv = (ImageView) findViewById(R.id.backIv);
		backIv.setOnClickListener(this);
		// 提示框
		tipTv = (TextView) findViewById(R.id.tipTv);
		// 记录列表
		recordLv = (ListView) findViewById(R.id.recordLv);
		recordLv.setAdapter(listAdapter);
		recordLv.setOnItemClickListener(this);
		// 搜索框
		keywordEt = (EditText) findViewById(R.id.keywordEt);
		keywordEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				if (!TextUtils.isEmpty(arg0.toString())) {
					listMessage = dbManager.query(App.getUserID(), meetingDetail.getId() + "", topicDetail.getId() + "", arg0.toString());
					listAdapter.notifyDataSetChanged();
					if (listMessage.size() > 0) {
						tipTv.setVisibility(View.GONE);
					} 
					else {
						tipTv.setVisibility(View.VISIBLE);
					}
				} 
				else {
					listMessage.clear();
					listAdapter.notifyDataSetChanged();
					tipTv.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
		});
	}
	
	// 查找消息发送人昵称
	private String getNickNameByMessage(MeetingMessage message) {
		String nick = "";
		MMeetingMember member = meetingDetail.getMeetingMemberByUserId(message.getSenderID());
		if (member != null) {
			nick = member.getMemberName();
		}
		return nick;
	}

	// 查找消息发送人头像
	private String getImageByMessage(MeetingMessage message) {
		String avatar = "";
		MMeetingMember member = meetingDetail.getMeetingMemberByUserId(message.getSenderID());
		if (member != null) {
			avatar = member.getMemberPhoto();
		}
		return avatar;
	}
	
	/**
	 * 聊天记录列表适配器
	 * @author leon
	 */
	class ChatRecordAdapter extends BaseAdapter{

		private Context context;
		
		public ChatRecordAdapter(Context context){
			this.context = context;
		}
		
		@Override
		public int getCount() {
			return listMessage.size();
		}

		@Override
		public Object getItem(int position) {
			return listMessage.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MeetingMessage message = listMessage.get(position);
			ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.list_item_chat_record, parent, false);
				holder = new ViewHolder();
				holder.avatarIv = (ImageView) convertView.findViewById(R.id.avatarIv);
				holder.senderTv = (TextView) convertView.findViewById(R.id.senderTv);
				holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
				holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			// 发送人姓名
			holder.senderTv.setText(getNickNameByMessage(message));
			// 消息内容
			holder.contentTv.setText(message.getContent());
			// 发送时间
			holder.timeTv.setText(message.getTime());
			// 发送人头像
			if(message.getSenderType() == IMBaseMessage.MSG_MY_SEND){ // 我发送的
				ImageLoader.getInstance().displayImage(App.getUser().getImage(), holder.avatarIv, LoadImage.mHyDefaultHead);
			}
			else if(message.getSenderType() == IMBaseMessage.MSG_OTHER_SEND){ // 其它人发送的
				ImageLoader.getInstance().displayImage(getImageByMessage(message), holder.avatarIv, LoadImage.mHyDefaultHead);
			}
			return convertView;
		}
		
		class ViewHolder{
			ImageView avatarIv;
			TextView senderTv;
			TextView contentTv;
			TextView timeTv;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent == recordLv){
			Intent intent = new Intent(this, MChatRecordBrowserActivity.class);
			intent.putExtra(ENavConsts.EMeetingDetail, meetingDetail);
			intent.putExtra(ENavConsts.EMeetingTopicDetail, topicDetail);
			intent.putExtra(ENavConsts.EMessageID, listMessage.get(position).getMessageID()); // 目标消息ID
			intent.putExtra(ENavConsts.EFromActivityName, TAG);
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		if(v == backIv){ // 返回
			finish();
		}
	};
}
