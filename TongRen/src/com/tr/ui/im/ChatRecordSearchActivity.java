package com.tr.ui.im;

import java.util.ArrayList;
import java.util.List;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.db.ChatRecordDBManager;
import com.tr.image.ImageLoader;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.MGetChatMessage;
import com.tr.model.im.MGetMUCMessage;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class ChatRecordSearchActivity extends JBaseFragmentActivity implements IBindData{

	private final String TAG = getClass().getSimpleName();
	
	private ListView recordLv;
	private TextView tipTv;
	private TextView searchTv;
	private EditText keywordEt;
	private ChatRecordDBManager dbManager;
	private String mucId;
	private ChatDetail chatDetail; // 私聊
	private MUCDetail mucDetail; // 群聊
	private String fromActivity;
	private List<IMBaseMessage> listMsg;
	private ChatRecordAdapter adapter;
	private Handler mHandler = new Handler();
	
	@Override
	public void initJabActionBar() {
	
		ActionBar actionbar = getActionBar();  
	    actionbar.setDisplayHomeAsUpEnabled(true);
	    actionbar.setDisplayShowTitleEnabled(false);
	    actionbar.setDisplayShowCustomEnabled(true); 
	    actionbar.setTitle("搜索记录");
	    actionbar.setDisplayShowCustomEnabled(true); 
	    
	    View parent = LayoutInflater.from(this).inflate(R.layout.search_actionbar_edit, null);
	    keywordEt = (EditText) parent.findViewById(R.id.home_search_edit);
	    searchTv = (TextView) parent.findViewById(R.id.home_search_tv);
	    searchTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String keyWord = keywordEt.getText().toString();
				if(!TextUtils.isEmpty(keyWord)){
					if(mucDetail!=null){
						if(fromActivity.equals("CommunityChatSettingActivity")){
							CommunityReqUtil.fetchHistoryMessages(ChatRecordSearchActivity.this, ChatRecordSearchActivity.this, keyWord, mucDetail.getId(), IMReqUtil.MSG_MUC, mHandler);
						}else{
							IMReqUtil.fetchHistoryMessages(ChatRecordSearchActivity.this, ChatRecordSearchActivity.this, keyWord, mucDetail.getId(), IMReqUtil.MSG_MUC, mHandler);
						}
					}else if(chatDetail!=null){
						IMReqUtil.fetchHistoryMessages(ChatRecordSearchActivity.this, ChatRecordSearchActivity.this, keyWord, Long.valueOf(chatDetail.getThatID()), IMReqUtil.MSG_CHAT, mHandler);
					}
				}
			}
		});
	    keywordEt.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				if(TextUtils.isEmpty(arg0.toString())){
					listMsg.clear();
					adapter.notifyDataSetChanged();
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
	    actionbar.setCustomView(parent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_record_search);
		initVars();
		initControls();
	}
	
	private void initVars(){
		mucId = getIntent().getStringExtra(ENavConsts.EMucID);
		chatDetail = (ChatDetail) getIntent().getSerializableExtra(ENavConsts.EChatDetail);
		mucDetail = (MUCDetail) getIntent().getSerializableExtra(ENavConsts.EMucDetail);
		fromActivity = getIntent().getStringExtra(ENavConsts.EFromActivityName);
		
		listMsg = new ArrayList<IMBaseMessage>();
		dbManager = new ChatRecordDBManager(this);
		adapter = new ChatRecordAdapter(this);
	}
	
	private void initControls(){
		tipTv = (TextView) findViewById(R.id.tipTv);
		recordLv = (ListView) findViewById(R.id.recordLv);
		recordLv.setAdapter(adapter);
		recordLv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if(chatDetail != null){
					ENavigate.startIMActivity(ChatRecordSearchActivity.this, chatDetail, listMsg.get(position).getIndex(), keywordEt.getText().toString());
				}
				else if(mucDetail != null){
					if(fromActivity.equals("CommunityChatSettingActivity")){
						ENavigate.startCommunityChartActivity(ChatRecordSearchActivity.this, mucDetail, listMsg.get(position).getIndex(), keywordEt.getText().toString());
					}else{
						ENavigate.startIMGroupActivity(ChatRecordSearchActivity.this, mucDetail, listMsg.get(position).getIndex(), keywordEt.getText().toString());
					}
				}
				// 隐藏软键盘
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(keywordEt.getWindowToken(), 0);
			}
		});
	}
	
	// 获取发送人头像
	public String getImageByMessage(IMBaseMessage msg) {
		
		String avatar = "";
		try{
			if(chatDetail != null){
				avatar = chatDetail.getThatImage();
			}
			else if(mucDetail != null){
				ConnectionsMini mini = mucDetail.getConnectionsMiniByUserId(msg.getSenderID());
				avatar = mini.getImage();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return avatar;
	}
	
	public int getSenderTypeByMessage(IMBaseMessage msg) {
		try {
			if(mucDetail != null){
				ConnectionsMini connsMini = mucDetail.getConnectionsMiniByUserId(msg.getSenderID());
				return connsMini.getType();
			}else if(chatDetail != null){
				if (msg.getSenderID().equalsIgnoreCase(App.getUserID())) {
					return App.getUser().getmUserType() - 1;
				} else {
					return chatDetail.getType() - 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	class ChatRecordAdapter extends BaseAdapter{

		private Context context;
		
		public ChatRecordAdapter(Context context){
			this.context = context;
		}
		
		@Override
		public int getCount() {
			return listMsg.size();
		}

		@Override
		public Object getItem(int position) {
			return listMsg.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			IMBaseMessage msg = listMsg.get(position);
			ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.list_item_chat_record, parent, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.initWithData(msg);
			return convertView;
		}
		
		class ViewHolder{
			
			ImageView avatarIv;
			TextView senderTv;
			TextView contentTv;
			TextView timeTv;
			
			public ViewHolder(View convertView){
				avatarIv = (ImageView) convertView.findViewById(R.id.avatarIv);
				senderTv = (TextView) convertView.findViewById(R.id.senderTv);
				contentTv = (TextView) convertView.findViewById(R.id.contentTv);
				timeTv = (TextView) convertView.findViewById(R.id.timeTv);
			}
			
			public void initWithData(IMBaseMessage msg){
				senderTv.setText(msg.getSenderName());
				contentTv.setText(msg.getContent());
				timeTv.setText(msg.getTime());
				if(msg.getSenderType() == IMBaseMessage.MSG_MY_SEND){ // 我发送的
					ImageLoader.load(avatarIv, App.getUser().getImage(), R.drawable.default_people_avatar);
				}
				else{ // 其它人发送的
					if(getSenderTypeByMessage(msg)+1 == ConnectionsMini.UT_ORG){
						Util.initAvatarImage(context, avatarIv, msg.getSenderName(), getImageByMessage(msg), 0, 2);
					}else{
						Util.initAvatarImage(context, avatarIv, msg.getSenderName(), getImageByMessage(msg), 0, 1);
					}
				}
			}
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		if(object == null){
			return;
		}
		switch(tag){
		case EAPIConsts.IMReqType.IM_REQ_FETCHHISTORYMESSAGE_CHAT:
			MGetChatMessage chatmessage = (MGetChatMessage) object;
			listMsg = chatmessage.getListMessage();
			adapter.notifyDataSetChanged();
			if(listMsg.size() > 0){
			tipTv.setVisibility(View.GONE);
			}
			else{
				tipTv.setVisibility(View.VISIBLE);
			}
			break;
		case EAPIConsts.IMReqType.IM_REQ_FETCHHISTORYMESSAGE_MUC:
			MGetMUCMessage mucMessage = (MGetMUCMessage) object;
			listMsg = mucMessage.getListMessage();
			adapter.notifyDataSetChanged();
			if(listMsg.size() > 0){
			tipTv.setVisibility(View.GONE);
			}
			else{
				tipTv.setVisibility(View.VISIBLE);
			}
			break;
		}
		
	};
}
