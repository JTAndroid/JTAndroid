package com.tr.ui.communities.im;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.CommunityReqUtil;
import com.tr.db.ChatLocalFileDBManager;
import com.tr.db.VoiceFileDBManager;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.service.FileDownloadService;
import com.tr.service.FileDownloadService.MyBinder;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.im.ChatRecordSearchActivity;
import com.tr.ui.widgets.ChatDialog;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyView;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.FileDownloader;
import com.utils.common.FilePathResolver;
import com.utils.common.FileUploader;
import com.utils.common.TaskIDMaker;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.Util;
import com.tr.ui.communities.adapter.ChatAdapter;
import com.tr.ui.communities.home.CommunityChatSettingActivity;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.communities.adapter.ChatAdapter.ViewHolder;

public abstract class ChatBaseActivity extends JBaseFragmentActivity implements IBindData, OnClickListener {

	protected XListView chatXlv;
	protected LinearLayout inputLl, smileyPagerContainer;
	protected ImageView switchIv, expressionIv, sendIv, smileyPagerchange;
	protected EditText textEt;
	protected TextView voiceTv;
	protected ViewPager viewPager;
	private LinearLayout moreGrid_Ll;
	protected GridView moreGrid;
	protected ChatAdapter adapter;
	private LinearLayout viewPagerCon;
	protected ArrayList<IMBaseMessage> listMessage;
	/** 选中的@的人 */
	protected List<ConnectionsMini> atConnectionsMinis;
	/** 选中的@的人的名称 */
	protected List<String> atConnectionsNames;
	/** 选中的@的人的ID */
	protected List<Integer> atConnectionsIds;
	
	public final int sendshow = 0;
	public final int sendgone = 1;

	private final int SEND_TEXT = 0; // 发送文本
	private final int SEND_VOICE = 1; // 发送语音
	/**
	 * 社群的社群实体
	 */
	protected ImMucinfo community;
	
	protected int PAGE_SIZE = 20;
	protected int fromIndex = -1;
	public boolean isBackward = false;
	
	private static final String LEFTSPECCHAR = ((char) 0X1B) + "";
	private static final String RIGHTSPECCHAR = ((char) 0X11) + "";
	private static final int COMMUNITYCHATSETTINGACTIVITY = 1000;
	
	private Boolean isShowface = false;
	// 缓存的超链接
	protected String cacheUrl = "";
	private List<SmileyView> listSmileyViews;
	protected MUCDetail mucDetail;// 群聊详情
	// 下载服务
	private FileDownloadService mDownloadService;
	// 本地文件数据库管理对象
	protected ChatLocalFileDBManager clfManager = null;
	private VoiceFileDBManager voiceFileManager;
	// 文件上传队列
	protected List<FileUploader> mListUploader = new ArrayList<FileUploader>();
	// 项目更新索引（根据此所以来确定要更新的项目，避免使用遍历造成的计算浪费）
	protected HashMap<String, Integer> mDownloadQueueIndex = new HashMap<String, Integer>();
	protected HashMap<String, Integer> mUploadQueueIndex = new HashMap<String, Integer>();
	// 广播过滤器
	private IntentFilter mFilter;
	protected String fromActivityName="";// 从哪个activity进入
	protected int searchMessagefromIndex = -1;
	protected String thatMucID;
	
	// 服务连接对象
	private ServiceConnection mConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mDownloadService = ((MyBinder) service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};
	
	// 下载状态广播监听器
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && intent.getAction() != null) {
				String url = intent.getStringExtra(EConsts.Key.WEB_FILE_URL);
				if (intent.getAction().equals(EConsts.Action.DOWNLOAD_SUCCESS)) {
					updateDownloadItem(url, FileDownloader.Status.Success);
				} else if (intent.getAction().equals(
						EConsts.Action.DOWNLOAD_FAILED)) {
					updateDownloadItem(url, FileDownloader.Status.Error);
				} else if (intent.getAction().equals(
						EConsts.Action.DOWNLOAD_CANCELED)) {
					updateDownloadItem(url, FileDownloader.Status.Canceled);
				} else if (intent.getAction().equals(
						EConsts.Action.DOWNLOAD_UPDATE)) {
					int progress = intent.getIntExtra(
							EConsts.Key.PROGRESS_UPDATE, 0);
					updateDownloadItem(url, FileDownloader.Status.Started,
							progress);
				} else if (intent.getAction().equals(
						EConsts.Action.DOWNLOAD_START)) {
					updateDownloadItem(url, FileDownloader.Status.Prepared);
				} else if (intent.getAction().equals(
						DownloadManager.ACTION_DOWNLOAD_COMPLETE)) { // 语音文件完成下载
					long taskId = intent.getLongExtra(
							DownloadManager.EXTRA_DOWNLOAD_ID, -1);
					if (taskId >= 0) {
						url = voiceFileManager.query(taskId);
						voiceFileManager.delete(taskId); // 删除数据表信息
						updateVoiceItem(url); // 更新界面
					}
				}
			}
		}
	};
	


	// 更新语音项目
	public void updateVoiceItem(String url) {

		List<IMBaseMessage> listMsg = adapter.getData();
		if (listMsg == null || listMsg.size() < 0) {
			return;
		}
		for (int i = 0; i < listMsg.size(); i++) {
			IMBaseMessage msg = listMsg.get(i);
			if (msg.getJtFile() != null
					&& !StringUtils.isEmpty(msg.getJtFile().mUrl)
					&& msg.getJtFile().mUrl.equals(url)) {

				View v = chatXlv.getChildAt(i - chatXlv.getFirstVisiblePosition() + 1);
				if (v != null) {
					ViewHolder holder = (ViewHolder) v.getTag();
					if (holder != null) {
						if (msg.getSenderID().equals(App.getUserID())) { // 自己
							holder.rightVoiceLoadingPb.setVisibility(View.GONE);
							holder.rightVoiceIv.setVisibility(View.VISIBLE);
							// 播放音频
							if (curVoiceUrl.equals(msg.getJtFile().mUrl)) {
								if (StringUtils
										.isEmpty(msg.getJtFile().reserved2)) { // 语音时长不正确
									showToast("无法播放语音文件，文件可能已损坏");
									return;
								}
								startPlaying(
										new File(EUtil.getChatVoiceCacheDir(
												this, mucDetail.getId()+""), msg
												.getJtFile().mFileName)
												.getAbsolutePath(),
										holder.rightVoiceIv,
										Integer.parseInt(msg.getJtFile().reserved2) * 1000);
							}
						} else { // 对方
							holder.leftVoiceLoadingPb.setVisibility(View.GONE);
							holder.leftVoiceIv.setVisibility(View.VISIBLE);
							// 播放音频
							if (curVoiceUrl.equals(msg.getJtFile().mUrl)) {
								if (StringUtils
										.isEmpty(msg.getJtFile().reserved2)) { // 语音时长不正确
									showToast("无法播放语音文件，文件可能已损坏");
									return;
								}
								startPlaying(
										new File(EUtil.getChatVoiceCacheDir(
												this, mucDetail.getId()+""), msg
												.getJtFile().mFileName)
												.getAbsolutePath(),
										holder.leftVoiceIv,
										Integer.parseInt(msg.getJtFile().reserved2) * 1000);
							}
						}
					}
				}
			}
		}
	}
	
	// 更新下载列表项
		public void updateDownloadItem(String url, int... args) {

			List<IMBaseMessage> listMsg = adapter.getData();
			if (listMsg == null || listMsg.size() < 0) {
				return;
			}
			// 通过索引查找要更新的项目

			// 检查索引的有效性
			if (mDownloadQueueIndex.containsKey(url)) {
				int index = mDownloadQueueIndex.get(url).intValue();
				if (index >= listMsg.size()) {
					mDownloadQueueIndex.remove(url); // 删除索引
				} else {
					IMBaseMessage msg = listMsg.get(index);
					if (msg.getType() != IMBaseMessage.TYPE_FILE
							|| msg.getJtFile() == null
							|| msg.getJtFile().mUrl == null
							|| !msg.getJtFile().mUrl.equals(url)) {
						mDownloadQueueIndex.remove(url); // 删除索引
					}
				}
			}

			// 重新计算索引
			if (!mDownloadQueueIndex.containsKey(url)) {
				for (int i = 0; i < listMsg.size(); i++) {
					IMBaseMessage msg = listMsg.get(i);
					if (msg.getType() == IMBaseMessage.TYPE_FILE
							&& msg.getJtFile() != null
							&& msg.getJtFile().mUrl != null
							&& msg.getJtFile().mUrl.equals(url)) {
						mDownloadQueueIndex.put(url, Integer.valueOf(i)); // 插入索引
						break;
					}
				}
			}

			if (mDownloadQueueIndex.containsKey(url)) {

				int index = mDownloadQueueIndex.get(url).intValue();
				if (index >= listMsg.size()) {
					return;
				}
				IMBaseMessage msg = listMsg.get(index);
				View v = chatXlv.getChildAt(index - chatXlv.getFirstVisiblePosition());
				if (v == null) {
					return;
				}
				ViewHolder holder = (ViewHolder) v.getTag();
				if (holder != null) {

					if (msg.getType() == IMBaseMessage.TYPE_FILE) { // 文件

						switch (args[0]) {
						case FileDownloader.Status.Prepared: // 准备就绪
							if (msg.getSenderID().equals(App.getUserID())) {
								holder.rightFileProgressPb
										.setVisibility(View.VISIBLE);
							} else {
								holder.leftFileProgressPb
										.setVisibility(View.VISIBLE);
							}
							break;
						case FileDownloader.Status.Started: // 正在下载
							if (args.length <= 1) {
								return;
							}
							if (msg.getSenderID().equals(App.getUserID())) {
								holder.rightFileProgressPb.setProgress(args[1]);
							} else { // 对方
								holder.leftFileProgressPb.setProgress(args[1]);
							}
							break;
						case FileDownloader.Status.Success: // 下载成功
							if (msg.getSenderID().equals(App.getUserID())) {
								holder.rightFileProgressPb.setVisibility(View.GONE);
								holder.rightFileStatusTv.setText("打开");
							} else {
								holder.leftFileProgressPb.setVisibility(View.GONE);
								holder.leftFileStatusTv.setText("打开");
							}
							break;
						case FileDownloader.Status.Canceled: // 下载取消
							if (msg.getSenderID().equals(App.getUserID())) {
								holder.rightFileProgressPb.setVisibility(View.GONE);
								holder.rightFileStatusTv.setText("未下载");
							} else {
								holder.leftFileProgressPb.setVisibility(View.GONE);
								holder.leftFileStatusTv.setText("未下载");
							}
							break;
						case FileDownloader.Status.Error: // 下载出错
							if (msg.getSenderID().equals(App.getUserID())) {
								holder.rightFileProgressPb.setVisibility(View.GONE);
								holder.rightFileStatusTv.setText("未下载");
							} else {
								holder.leftFileProgressPb.setVisibility(View.GONE);
								holder.leftFileStatusTv.setText("未下载");
							}
							break;
						}
					}
				}
			}
		}
	
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "社群聊天名称",false, null, true, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_chat_base);
		
		// 搜索结果
		fromActivityName = getIntent().getStringExtra(ENavConsts.EFromActivityName);
		searchMessagefromIndex = getIntent().getIntExtra(ENavConsts.ESearchFromIndex,-1);
		mucDetail = (MUCDetail) getIntent().getSerializableExtra(ENavConsts.EMucDetail);
		
		listMessage = new ArrayList<IMBaseMessage>();
		atConnectionsMinis = new ArrayList<ConnectionsMini>();
		atConnectionsNames = new ArrayList<String>();
		atConnectionsIds = new ArrayList<Integer>();

		// 广播过滤器
		mFilter = new IntentFilter();
		mFilter.addAction(EConsts.Action.DOWNLOAD_START);
		mFilter.addAction(EConsts.Action.DOWNLOAD_UPDATE);
		mFilter.addAction(EConsts.Action.DOWNLOAD_SUCCESS);
		mFilter.addAction(EConsts.Action.DOWNLOAD_FAILED);
		mFilter.addAction(EConsts.Action.DOWNLOAD_CANCELED);
		mFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE); // 语音专用
		// 语音文件管理
		if (voiceFileManager == null) {
			voiceFileManager = new VoiceFileDBManager(this);
		}
		
		// 本地文件数据库管理对象
		if (clfManager == null) {
			clfManager = new ChatLocalFileDBManager(this);
		}
		
		initView();
		setListeners();
		initData();
		
		// 绑定服务对象
		Intent intent = new Intent(ChatBaseActivity.this,
				FileDownloadService.class);
		bindService(intent, mConn, BIND_AUTO_CREATE);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// 注册下载广播监听器
		registerReceiver(mReceiver, mFilter);
	}
	
	private void initView(){
		chatXlv = (XListView) findViewById(R.id.chatXlv);
		inputLl = (LinearLayout) findViewById(R.id.inputLl);
		smileyPagerContainer = (LinearLayout) findViewById(R.id.smileyPagerContainer);
		switchIv = (ImageView) findViewById(R.id.switchIv);
		expressionIv = (ImageView) findViewById(R.id.expressionIv);
		sendIv = (ImageView) findViewById(R.id.sendIv);
		smileyPagerchange = (ImageView) findViewById(R.id.smileyPagerchange);
		textEt = (EditText) findViewById(R.id.textEt);
		voiceTv = (TextView) findViewById(R.id.voiceTv);
		viewPager = (ViewPager) findViewById(R.id.smileyPager);
		moreGrid_Ll = (LinearLayout)this.findViewById(R.id.moreGrid_Ll);
		moreGrid = (GridView) findViewById(R.id.moreGrid);
		//
		viewPagerCon = (LinearLayout) findViewById(R.id.smileyPagerContainer);
		
		if (getIntent().hasExtra(ENavConsts.EFromActivityName)
				&& getIntent().getStringExtra(ENavConsts.EFromActivityName)
						.equalsIgnoreCase(ChatRecordSearchActivity.class.getSimpleName())) {
			inputLl.setVisibility(View.GONE);
		}
	}
	
	private void setListeners(){
		switchIv.setOnClickListener(this);
		switchIv.setTag(SEND_TEXT);
		expressionIv.setOnClickListener(this);
		sendIv.setOnClickListener(this);
		sendIv.setTag(sendgone);
		voiceTv.setOnTouchListener(mTouchListener);
		textEt.addTextChangedListener(new MyTextWatcher());
		textEt.setOnClickListener(this);
		textEt.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motion) {
				viewPagerCon.setVisibility(View.GONE);
				isShowface = false;// 显示软盘
				if (motion.getAction() == MotionEvent.ACTION_UP) { // 按下操作
					// int position = ((EditText) view).getSelectionEnd();
					// Log.d("按钮触发**********", position + "");
				}
				return false;
			}
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {

				if (arg0 == 0) {
					((ImageView) findViewById(R.id.smileyPagerchange)).setImageResource(R.drawable.chat_biaoqing_1);
				} else if (arg0 == 1) {
					((ImageView) findViewById(R.id.smileyPagerchange)).setImageResource(R.drawable.chat_biaoqing_2);
				} else if (arg0 == 2) {
					((ImageView) findViewById(R.id.smileyPagerchange)).setImageResource(R.drawable.chat_biaoqing_3);
				} else if (arg0 == 3) {
					((ImageView) findViewById(R.id.smileyPagerchange)).setImageResource(R.drawable.chat_biaoqing_4);
				} else if (arg0 == 4) {
					((ImageView) findViewById(R.id.smileyPagerchange)).setImageResource(R.drawable.chat_biaoqing_5);
				} else if (arg0 == 5) {
					((ImageView) findViewById(R.id.smileyPagerchange)).setImageResource(R.drawable.chat_biaoqing_6);
				} else if (arg0 == 6) {
					((ImageView) findViewById(R.id.smileyPagerchange)).setImageResource(R.drawable.chat_biaoqing_7);
				} else if (arg0 == 7) {
					((ImageView) findViewById(R.id.smileyPagerchange)).setImageResource(R.drawable.chat_biaoqing_8);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

		});
		

		chatXlv.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				getMoreMessage(false); // 加载更多消息（查找旧记录
			}

			@Override
			public void onLoadMore() {
				getMoreMessage(true); // 加载更多消息（查找旧记录
			}
		});
	}
	
	private void initData(){
		adapter = new ChatAdapter(this, this);
		adapter.setList(listMessage);
		adapter.setFDService(mDownloadService);
		adapter.setHandler(handler);
		adapter.setView(textEt);
		adapter.setConn(atConnectionsMinis);
		chatXlv.setAdapter(adapter);
		moreGrid.setAdapter(new MoreGridAdpter());
		
		// 表情view
		listSmileyViews = new ArrayList<SmileyView>();
		int totalPage = (int) Math.ceil(SmileyParser.mEnhancedIconIds.length
				* 1.0 / SmileyView.MaxSmileyNumber);
		for (int i = 0; i < totalPage; i++) {
			SmileyView sv = new SmileyView(this, i);
			sv.setTag(i);
			sv.setOnItemClickListener(smileyViewClickListener);
			listSmileyViews.add(sv);
		}
		viewPager.setAdapter(new PageViewAdpter());
	}
	
	private String beforeText = "";
	private int mStart = 0;
	class MyTextWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			String inputStr = s.toString();
			if (inputStr.length() == 0) {
				sendIv.setImageResource(R.drawable.chat_more);
				sendIv.setTag(sendgone);
			} else {
				sendIv.setImageResource(R.drawable.chat_send);
				moreGrid_Ll.setVisibility(View.GONE);
				sendIv.setTag(sendshow);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			beforeText = s.toString();
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mStart = start;
			if(s.length() > beforeText.length()){
				String temp_after = s.charAt(start)+"";
				if(start == 0){
					if(temp_after.equals("@")){
						/*ENavigate.startAtInformFriendsActivityForResult(
										ChatBaseActivity.this,
										ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_AT_FRIENDS);*/
					}
				}else if (start >= 1) {
					String temp_before = beforeText.charAt(start-1)+"";
					if(temp_after.equals("@") && (Util.checkIsChinses(temp_before) || !Util.checkIsEngANum(temp_before))){//输入字符是“@”,其之前的字符是中文或者不是英文和数字
							/*ENavigate.startAtInformFriendsActivityForResult(
											ChatBaseActivity.this,
											ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_AT_FRIENDS);*/
	
						}
					}
			}else if (s.length() < beforeText.length() && start >= 1) {
				StringBuilder builder = new StringBuilder();
				// 删除的左侧特殊字符 向后索引（暂不处理此种情况）
				if ((beforeText.charAt(start) + "").equals(LEFTSPECCHAR)) {
				}
				// 如果删除的是@
				else if ((beforeText.charAt(start - 1) + "")
						.equals(LEFTSPECCHAR)
						&& (beforeText.charAt(start) + "").equals("@")) {
					for (int i = start - 1; i < beforeText.length(); i++) {
						// 等于右侧字符
						if ((beforeText.charAt(i) + "").equals(RIGHTSPECCHAR)) {
							builder.append(beforeText.subSequence(0, start - 1))
								   .append(beforeText.subSequence(i + 1, beforeText.length()));
							textEt.setText(builder.toString());
							textEt.setSelection(start);
							break;
						}
					}
				}
				// 删除的右侧的特殊字符 向前索引
				else if ((beforeText.charAt(start) + "").equals(RIGHTSPECCHAR)) {
					for (int i = start - 1; i >= 0; i--) {
						// 等于右侧或左侧字符
						if ((s.charAt(i) + "").equals(LEFTSPECCHAR)) {
							builder.append(s.subSequence(0, i))
							       .append(s.subSequence(start, s.length()));
							textEt.setText(builder.toString());
							textEt.setSelection(i);
							break;
						} else if ((s.charAt(i) + "").equals(RIGHTSPECCHAR)) {
							builder.append(s.subSequence(0, i))
								   .append(RIGHTSPECCHAR)
								   .append(s.subSequence(start, s.length()));
							textEt.setText(builder.toString());
							textEt.setSelection(i + 1);
							break;
						}
					}
				}
			}
		}
		
	}
	
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EAPIConsts.handler.show_err:
				showToast(msg.getData().getString(EAPIConsts.Header.ERRORMESSAGE));
				break;
			case 0:
				getMoreMessage(true); // 获取最新的数据
				break;
			}
		}
	};


	public void notifyGetMessage() {
		handler.sendEmptyMessage(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.im_mucdetail_menu, menu);
		MenuItem menuItem = menu.findItem(R.id.im_muc_detail_create_next);
		menuItem.setIcon(getResources().getDrawable(R.drawable.frame_setting));// 设置
		if(searchMessagefromIndex == -1){
			menuItem.setVisible(true);
		}else{
			menuItem.setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.sendIv: // 发送按钮
			int sendtag = ((Integer) sendIv.getTag()).intValue();
			if(sendtag == sendshow){// send按钮显示
				moreGrid_Ll.setVisibility(View.GONE);
				sendIv.setTag(sendgone);
				// 显示发送
				isShowface = false;
				String txtSend = textEt.getText().toString().trim();
				atConnectionsIds.clear();
				atConnectionsNames.clear();
				
				int beforeIndex = 0;
				for (int i = 0; i < txtSend.length(); i++) {
					char ch = txtSend.charAt(i);
					if ((ch + "").equals(LEFTSPECCHAR)) {
						beforeIndex = i;
						for (int j = beforeIndex; j < txtSend.length(); j++) {
							char ch2 = txtSend.charAt(j);
							if ((ch2 + "").equals(RIGHTSPECCHAR)) {
								atConnectionsNames.add(txtSend.substring(beforeIndex + 2, j));
								break;
							}
						}
					}

				}
				// 循环遍历出所有需要@的人的id
				for (String name : atConnectionsNames) {
					for (ConnectionsMini mini : atConnectionsMinis) {
						if (mini.getName().equals(name)) {
							atConnectionsIds.add(Integer.parseInt(mini
									.getId()));
							break;
						}
					}
				}
				atConnectionsMinis.clear();
				// 发送消息
				cacheUrl = "";
				if (txtSend.startsWith("http://") || txtSend.startsWith("https://")) {
					cacheUrl = txtSend;
					CommonReqUtil.doFetchExternalKnowledgeUrl(
							ChatBaseActivity.this, ChatBaseActivity.this,
							cacheUrl, true, null);
				} else if (txtSend.trim().isEmpty()) {
					showToast("消息内容不能为空");
				} else {
					sendMessage(txtSend);
				}
				textEt.setText("");
				sendIv.setImageResource(R.drawable.chat_more);

				viewPagerCon.setVisibility(View.GONE);
				expressionIv.setImageResource(R.drawable.chat_biaoqing);
			}else{
				if (moreGrid.getVisibility() == View.GONE) {
					moreGrid.setVisibility(View.VISIBLE);
					moreGrid_Ll.setVisibility(View.VISIBLE);
					sendIv.setTag(sendgone);
					viewPagerCon.setVisibility(View.GONE);
					// 隐藏软盘，加了之后gridView起不来
					InputMethodManager m = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					m.hideSoftInputFromWindow(textEt.getApplicationWindowToken(), 0);
				} else {
					moreGrid.setVisibility(View.GONE);
					moreGrid_Ll.setVisibility(View.GONE);

					sendIv.setTag(sendgone);
					viewPagerCon.setVisibility(View.GONE);
					// 显示软盘
					InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
			break;
		case R.id.expressionIv: // 笑脸按钮
			if (!isShowface) {
				// sendImageIv.setImageResource(R.drawable.chat_keyboard);
				viewPagerCon.setVisibility(View.VISIBLE);
				isShowface = true;
				moreGrid.setVisibility(View.GONE);
				moreGrid_Ll.setVisibility(View.GONE);

				// 隐藏软盘，加了之后gridView起不来
				InputMethodManager m = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				m.hideSoftInputFromWindow(textEt.getApplicationWindowToken(), 0);
				// 消息定位到最新
				handler.post(new Runnable() {
					@Override
					public void run() {
						chatXlv.setSelection(adapter.getData().size());
					}
				});
			} else {
				expressionIv.setImageResource(R.drawable.chat_biaoqing);
				viewPagerCon.setVisibility(View.GONE);
				isShowface = false;
				moreGrid.setVisibility(View.GONE);
				moreGrid_Ll.setVisibility(View.GONE);

				// 显示软盘
				InputMethodManager manager = (InputMethodManager) textEt
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				manager.toggleSoftInput(0,
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		case R.id.switchIv: // 切换语音和文本
			viewPagerCon.setVisibility(View.GONE);
			isShowface = false;
			moreGrid.setVisibility(View.GONE);
			moreGrid_Ll.setVisibility(View.GONE);

			sendIv.setTag(sendgone);
			int tag = ((Integer) switchIv.getTag()).intValue();
			if (tag == SEND_TEXT) { // 发送语音
				switchIv.setTag(SEND_VOICE);
				switchIv.setImageResource(R.drawable.chat_keyboard);
				textEt.setVisibility(View.GONE);
				voiceTv.setVisibility(View.VISIBLE);
				expressionIv.setVisibility(View.GONE);
				sendIv.setImageResource(R.drawable.chat_more);
				sendIv.setTag(sendgone);
			} else { // 发送文本
				switchIv.setTag(SEND_TEXT);
				switchIv.setImageResource(R.drawable.chat_microphone);
				voiceTv.setVisibility(View.GONE);
				textEt.setVisibility(View.VISIBLE);
				expressionIv.setVisibility(View.VISIBLE);
				sendIv.setImageResource(R.drawable.chat_more);
				sendIv.setTag(sendgone);
			}
			break;
		case R.id.textEt:
			if (isShowface) {
				expressionIv.setImageResource(R.drawable.chat_biaoqing);
				viewPagerCon.setVisibility(View.GONE);
				isShowface = false;
				// 显示软盘
				textEt.setFocusableInTouchMode(true);
			}
			moreGrid.setVisibility(View.GONE);
			moreGrid_Ll.setVisibility(View.GONE);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode != RESULT_OK) {
			return;
		}
		JTFile jtFile = null;
		switch (requestCode) {
		case ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_AT_FRIENDS: // 邀请好友
			ConnectionsMini connectionsMini = (ConnectionsMini) intent
					.getExtras().getSerializable(ENavConsts.EConnectionsMini);

			String before_text = textEt.getText().toString();
			String after_text = before_text.substring(0, mStart) + LEFTSPECCHAR +"@"+ connectionsMini.getName() + RIGHTSPECCHAR +" "+ before_text.substring(mStart+1, before_text.length());
			textEt.setText(after_text);
			textEt.setSelection(textEt.getText().length());
			atConnectionsMinis.add(connectionsMini);
			break;
		case EConsts.ReqCode.SelectFromMyRequirement:  // 需求
			jtFile = (JTFile) intent.getSerializableExtra(EConsts.Key.JT_FILE);
			sendRichMessage(IMBaseMessage.TYPE_REQUIREMENT, jtFile);
			break;
		case EConsts.ReqCode.SelectFromMyKnowledge: // 知识
			jtFile = (JTFile) intent.getSerializableExtra(EConsts.Key.JT_FILE);
			sendRichMessage(IMBaseMessage.TYPE_KNOWLEDGE2, jtFile);
			break;
		case EConsts.ReqCode.SelectFromMyConnectionAndOrg:  // 关系
			Object object = intent.getSerializableExtra(ENavConsts.redatas);
			if (object != null && object instanceof JTFile) {
				jtFile = (JTFile) object;
				if (jtFile.getmType() == JTFile.TYPE_JTCONTACT_OFFLINE
						|| jtFile.getmType() == JTFile.TYPE_ORG_OFFLINE
						|| jtFile.getmType() == JTFile.TYPE_CLIENT) { // 分享一个人脉或离线机构（创建一个新的后再分享）

					IMBaseMessage msg = new IMBaseMessage();
					msg.setJtFile(jtFile);
					msg.setType(jtFile);

					if (jtFile.getmType() == JTFile.TYPE_JTCONTACT_OFFLINE) {
						sendRichMessage(IMBaseMessage.TYPE_JTCONTACT_OFFLINE, jtFile);
					}
					if (jtFile.getmType() == JTFile.TYPE_ORG_OFFLINE) {
						sendRichMessage(IMBaseMessage.TYPE_ORG_OFFLINE, jtFile);
					}
					if (jtFile.getmType() == JTFile.TYPE_CLIENT) {
						sendRichMessage(IMBaseMessage.TYPE_CUSTOMER, jtFile);
					}
				} else if (jtFile.getmType() == JTFile.TYPE_JTCONTACT_ONLINE
						|| jtFile.getmType() == JTFile.TYPE_ORG_ONLINE
						|| jtFile.getmType() == JTFile.TYPE_ORGANIZATION) { // 分享一个用户或在线机构
					if (jtFile.getmType() == JTFile.TYPE_JTCONTACT_ONLINE) {
						sendRichMessage(IMBaseMessage.TYPE_JTCONTACT_ONLINE, jtFile);
					} else if (jtFile.getmType() == JTFile.TYPE_ORG_ONLINE) {
						sendRichMessage(IMBaseMessage.TYPE_ORG_ONLINE, jtFile);
					} else if (jtFile.getmType() == JTFile.TYPE_ORGANIZATION) {
						sendRichMessage(IMBaseMessage.TYPE_ORGANIZATION, jtFile);
					}
				}
			}
			break;
		case EConsts.ReqCode.SelectFromMyConnection_share: // 新创建的人脉
			Object obj = intent.getSerializableExtra(ENavConsts.redatas);
			if (obj != null && obj instanceof JTFile) {
				jtFile = (JTFile) obj;
				sendRichMessage(IMBaseMessage.convertJTFileType2IMBaseMessageType(jtFile.mType),jtFile);
			}
			break;
		case EConsts.REQ_CODE_PICK_PICTURE: // 选图
		case EConsts.REQ_CODE_TAKE_PICTURE:  // 拍照
			if (requestCode == EConsts.REQ_CODE_PICK_PICTURE) {
				jtFile = EUtil.createJTFileFromLocalFile(FilePathResolver.getPath(this, intent.getData()));
			} else {
				if (pictureFile!=null) {
					jtFile = EUtil.createJTFileFromLocalFile(pictureFile.getAbsolutePath());
				}
			}
			if (jtFile != null && jtFile.mType == JTFile.TYPE_IMAGE) {
				sendRichMessage(IMBaseMessage.TYPE_IMAGE, jtFile);
			} else {
				showToast("图片地址解析错误或不支持的图片格式");
			}
			break;
		case EConsts.REQ_CODE_PICK_FILE:  // 文件
			jtFile = EUtil.createJTFileFromLocalFile(FilePathResolver
					.getPath(this, intent.getData()));
			sendRichMessage(IMBaseMessage.TYPE_FILE, jtFile);
			break;
		case EConsts.REQ_CODE_TAKE_VIDEO: // 拍视频
		case EConsts.REQ_CODE_PICK_VIDEO:  // 选视频
			if (requestCode == EConsts.REQ_CODE_TAKE_VIDEO) {
				if (videoFile!=null) {
					jtFile = EUtil.createJTFileFromLocalFile(videoFile.getAbsolutePath());
				}
			} else {
				jtFile = EUtil.createJTFileFromLocalFile(FilePathResolver.getPath(this, intent.getData()));
			}
			if (jtFile != null && jtFile.mType == JTFile.TYPE_VIDEO) {
				sendRichMessage(IMBaseMessage.TYPE_VIDEO, jtFile);
			} else {
				showToast("视频地址解析错误或不支持的视频格式");
			}
			break;
		}
	}
	
	/**
	 * 获取最后一条消息的发送时间
	 * 
	 * @return
	 */
	public String getLatestTime() {
		String time = null;
		for (int i = listMessage.size() - 1; i >= 0; i--) {
			if (listMessage.size() > i) {
				IMBaseMessage msg = listMessage.get(i);
				if (msg.getSendType() == IMBaseMessage.SEND_TYPE_SENT) {
					time = msg.getTime();
					break;
				} else {
					continue;
				}
			}
		}
		return time;
	}
	
	/**
	 * 获取最新一条消息的索引
	 * 
	 * @return
	 */
	public int getLatestMessageIndex() {
		int index = -1;
		for (int i = listMessage.size() - 1; i >= 0; i--) {
			if (listMessage.size() > 0) {
				IMBaseMessage msg = listMessage.get(listMessage.size()-1);
				if (msg.getSendType() == IMBaseMessage.SEND_TYPE_SENT) {
					index = msg.getIndex();
					break;
				} else {
					continue;
				}
			}
		}
		return index; 
	}
	
	/** 定位到最新的消息 */
	protected void locateToLatestMessage() {
		if (chatXlv != null && !chatXlv.isRefreshing() && adapter != null
				&& adapter.getCount() > 0) {
			chatXlv.setSelection(chatXlv.getAdapter().getCount() - 1);
		}
	}

	/** 刷新列表 */
	protected void refreshList() {
		String MSG = "refreshList()";
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	
		if (chatXlv != null && adapter != null && adapter.getCount() > 0) {
			chatXlv.setSelection(adapter.getCount() - 1);
			Log.i(TAG, MSG + " listView.getAdapter().getCount() = " + adapter.getCount());
		}
	}
	

	// 拍照图片临时文件
	protected File pictureFile;
	// 拍摄视频文件
	protected File videoFile;

	/**
	 * 生成随机且唯一的文件名
	 * 
	 * @return
	 */
	protected String generateRandomFileName() {
		String fileName = null;
		try {
			Date now = new Date();
			String userId = App.getUserID();
			Random ran = new Random(System.currentTimeMillis());
			fileName = StringUtils.getMD5Str(userId + now + ran.nextLong());
		} catch (Exception e) {
			fileName = DateFormat.format("yyyyMMddkkmmss",
					System.currentTimeMillis())
					+ "_" + App.getUserID();
		}
		return fileName;
	}
	
	/**
	 * 发送更多适配器类
	 * 
	 * @author leon
	 */
	class MoreGridAdpter extends BaseAdapter implements OnClickListener {

		// 项目标签
		private final String[] labels = new String[] { "需求", "知识", "关系", "图片",
				"文件", "拍摄", "视频" };
		// 项目图标
		private int[] images = new int[] { R.drawable.chat_share_req,
				R.drawable.chat_share_kno, R.drawable.chat_share_conns,
				R.drawable.chat_share_pic, R.drawable.chat_share_file,
				R.drawable.chat_share_camera, R.drawable.chat_share_video };

		public MoreGridAdpter() {

		}

		@Override
		public int getCount() {
			return labels.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.im_chatmoregriditem,
						parent, false);
			}
			// 类型（文字）
			TextView typeTv = (TextView) convertView.findViewById(R.id.text);
			typeTv.setText(labels[position]);
			// 类型（图标）
			ImageView typeIv = (ImageView) convertView.findViewById(R.id.image);
			typeIv.setImageResource(images[position]);
			typeIv.setTag(position);
			typeIv.setOnClickListener(this);
			return convertView;
		}

		@Override
		public void onClick(View v) {
			final int position = (Integer) v.getTag();
			switch (position) {
			case 0: // 需求
				EUtil.dispatchShareRequirementIntent(ChatBaseActivity.this);
				break;
			case 1: // 知识
				EUtil.dispatchShareKnowledgeIntent(ChatBaseActivity.this);
				break;
			case 2: // 关系
				ENavigate.startIMRelationSelectActivityEx(
						ChatBaseActivity.this,
						ENavConsts.EShareCnsSelectActivity,
						EConsts.ReqCode.SelectFromMyConnectionAndOrg);
				break;
			case 3: // 图片
				EUtil.dispatchPickPictureIntent(ChatBaseActivity.this);
				break;
			case 4: // 文件
				EUtil.dispatchPickFileIntent(ChatBaseActivity.this);
				break;
			case 5: // 拍摄
				new AlertDialog.Builder(ChatBaseActivity.this)
						.setItems(new String[] { "拍照", "拍视频" },
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										File cacheDir = null;
										if (which == 0) {
											cacheDir = EUtil.getChatImageCacheDir(ChatBaseActivity.this, mucDetail.getId()+"");
											if (cacheDir == null) {
												showToast("无法访问文件系统");
												return;
											}
											pictureFile = new File(cacheDir, generateRandomFileName() + EConsts.DEFAULT_PIC_SUFFIX);
											if (pictureFile.exists()) {
												pictureFile.delete();
											}
											EUtil.dispatchTakePictureIntent(ChatBaseActivity.this, Uri.fromFile(pictureFile));
										} else {
											cacheDir = EUtil.getChatVideoCacheDir(ChatBaseActivity.this, mucDetail.getId()+"");
											if (cacheDir == null) {
												showToast("无法访问文件系统");
												return;
											}
											videoFile = new File(cacheDir, generateRandomFileName() + EConsts.DEFAULT_VIDEO_SUFFIX);
											if (videoFile.exists()) {
												videoFile.delete();
											}
											EUtil.dispatchTakeVideoIntent(ChatBaseActivity.this, Uri.fromFile(videoFile));
										}
									}
								}).create().show();
				break;
			case 6: // 视频
				EUtil.dispatchPickVideoIntent(ChatBaseActivity.this);
				break;
			}
		}
	}
	
	class PageViewAdpter extends PagerAdapter {

		@Override
		public int getCount() {
			return listSmileyViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(listSmileyViews.get(position));
			return listSmileyViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(listSmileyViews.get(position));
		}
	}
	
	/** 表情点击事件 */
	private SmileyView.OnItemClickListener smileyViewClickListener = new SmileyView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg) {
			final SmileyParser parser = SmileyParser.getInstance(ChatBaseActivity.this);
			if (position == SmileyView.MaxSmileyNumber) { // 删除表情
				String text = textEt.getText().toString();

				if (text.length() > 0) {

					if (text.lastIndexOf(RIGHTSPECCHAR) == text.length() - 1) {
						text = text.substring(0, text.lastIndexOf(LEFTSPECCHAR));
					} else {
						text = text.substring(0, text.length() - 1);
					}
					textEt.setText(text);
					textEt.setSelection(text.length());
				}
				return;
			}

			final String text = textEt.getText().toString() + LEFTSPECCHAR
					+ parser.getmSmileyTexts()[(int) arg] + RIGHTSPECCHAR;
			textEt.setText(text);
			textEt.setSelection(text.length());
		}
	};
	
	// 录音时间标尺
	private long recordTimestamp = 0;
	// 播放语音相关
	private AnimationDrawable mVoiceAnim = null; // 当前语音动画
	private ImageView voiceIv = null; // 当前语音控件
	private String mVoicePath = ""; // 当前播放的语音文件路径
	private boolean mPlaying = false; // 是否在播放
	private String curVoiceUrl = ""; // 当前播放的音频文件的url

	// 录音相关
	private String mAudioName = ""; // 临时语音文件名
	private String mAudioPath = ""; // 临时语音文件全路径
	private ChatDialog chatDlg; // 聊天对话框
	// 录音相关
	private MediaRecorder mRecorder;
	private MediaPlayer mPlayer;
	
	private OnTouchListener mTouchListener = new OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: // 按下（开始录音）

				// 设置界面状态
				voiceTv.setText("松开 发送");
				// 间隔时间是否足够
				if (recordTimestamp > 0 && System.currentTimeMillis() - recordTimestamp < 1200) {
					return true;
				}
				// 间隔时间度量衡
				recordTimestamp = System.currentTimeMillis();
				// 停止播放
				stopPlaying();
				// 文件名(保证唯一)
				mAudioName = generateRandomFileName() + EConsts.DEFAULT_AUDIO_SUFFIX;
				Log.d(TAG, "********************语音文件名********************" + mAudioName);
				// 音频文件存储路径
				File dir = getVoiceFileDir();
				if (dir != null) {
					// 语音文件完整路径
					mAudioPath = new File(dir, mAudioName).getAbsolutePath();
					// 显示对话框
					if (chatDlg == null) {
						chatDlg = new ChatDialog(ChatBaseActivity.this);
					}
					chatDlg.show();
					// 开始录音
					startRecording(mAudioPath);
				} else {
					// 语音文件路径置空
					mAudioPath = "";
					// 弹出提示消息
					showToast("没有SD卡，无法生成语音文件");
				}
				break;
			case MotionEvent.ACTION_UP: // 松开（停止录音）

				// 设置界面状态
				voiceTv.setText("按住 说话");
				// 是否弹出了对话框
				if (chatDlg != null && chatDlg.isShowing()) {
					chatDlg.dismiss();
				}
				// 间隔时间是否足够
				if (System.currentTimeMillis() - recordTimestamp < 1200) {
					// add by zhongshan 回收MidiaRecoder资源
					try {
						stopRecording();
					} catch (Exception e) {
						e.printStackTrace();
					}
					showToast("录音时间太短");
					return false;
				}
				// 间隔时间度量衡
				recordTimestamp = System.currentTimeMillis();
				// 录音不成功
				if (TextUtils.isEmpty(mAudioPath) || !new File(mAudioPath).exists()) {
					break;
				}
				// 延时500ms停止
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// 停止录音
						try {
							ChatBaseActivity.this.stopRecording();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						// 检查音频文件合法性
						try {
							MediaPlayer player = new MediaPlayer();
							player.setDataSource(mAudioPath);
							player.prepare();
							long duration = TimeUnit.MILLISECONDS.toSeconds(player.getDuration());
							if (duration < 1) { // 不足1s
								showToast("录音时间太短");
								return;
							} else { // 发送消息
								JTFile jtFile = new JTFile();
								jtFile.fileName = mAudioName;
								jtFile.mType = JTFile.TYPE_AUDIO;
								jtFile.mTaskId = TaskIDMaker.getTaskId(App.getUserID()); // 上传所需
								jtFile.mLocalFilePath = jtFile.reserved1 = mAudioPath; // 本地文件路径
								jtFile.reserved2 = duration + ""; // 时长(单位:s)
								jtFile.mFileSize = new File(mAudioPath).length(); // 文件大小
								sendRichMessage(IMBaseMessage.TYPE_AUDIO, jtFile); // 发送消息
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}, 500);
				break;
			}
			return true;
		}
	};
	
	/** 播放录音 */
	private void startPlaying(String voicePath, ImageView view, long duration) {

		if (mPlaying && voicePath.equals(mVoicePath)) { // 语音正在播放
			// 停止播放
			stopPlaying();
		} else {
			// 停止播放
			stopPlaying();
			// 开始播放新的
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(voicePath);
				mPlayer.prepare();
				mPlayer.start();
				mVoicePath = voicePath;
				voiceIv = view;
				mPlaying = true;
				startPlayingAnim();
				handler.postDelayed(mStopPlayingRunnable, duration);
			} catch (IOException e) {
				stopPlaying();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	/** 停止播放录音 */
	private void stopPlaying() {
		// 停止播放
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
		// 取消回调
		handler.removeCallbacks(mStopPlayingRunnable);
		// 停止动画
		stopPlayingAnim();
		// 设置状态为
		mPlaying = false;
	}

	/** 停止录音Runnable */
	private Runnable mStopPlayingRunnable = new Runnable() {

		@Override
		public void run() {
			stopPlaying();
		}
	};

	/**
	 * 开始播放动画
	 * 
	 * @param voiceIv
	 */
	private void startPlayingAnim() {
		if (voiceIv != null) {
			int tag = Integer.parseInt(voiceIv.getTag().toString());
			if (tag == 0) { // 本方
				voiceIv.setBackgroundResource(R.drawable.chat_to_anim);
			} else { // 对方
				voiceIv.setBackgroundResource(R.drawable.chat_from_anim);
			}
			mVoiceAnim = (AnimationDrawable) voiceIv.getBackground();
			mVoiceAnim.start();
		}
	}

	/**
	 * 停止播放动画
	 * 
	 * @param voiceIv
	 */
	private void stopPlayingAnim() {

		// 停止动画
		if (mVoiceAnim != null) {
			mVoiceAnim.stop();
		}
		// 设置背景
		if (voiceIv != null) {
			int tag = Integer.parseInt(voiceIv.getTag().toString());
			if (tag == 0) { // 本方
				voiceIv.setBackgroundResource(R.drawable.chat_right_voice_playing);
			} else { // 对方
				voiceIv.setBackgroundResource(R.drawable.chatfrom_voice_playing);
			}
		}
	}

	// 开始录音
	private void startRecording(String filePath) {
		mRecorder = new MediaRecorder();
		try {
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setOutputFile(filePath);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.prepare();
			mRecorder.start();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	// 停止录音
	private void stopRecording() throws Exception{
		if (mRecorder != null) {
			try {
				mRecorder.stop();
				// add by zhongshan 回收MediaRecoder资源
				mRecorder.reset();
				mRecorder.release();
			} catch (IllegalStateException e) {
				mRecorder.release();
				e.printStackTrace();
			}
			mRecorder = null;
		}
	}
	
	/**
	 * 随时调用检查
	 * 
	 * @return
	 */
	private File getVoiceFileDir() {
		return EUtil.getChatVoiceCacheDir(this, mucDetail.getId()+"");
	}
	
	// 开始上传任务
	protected void startNewUploadTask(final IMBaseMessage msg) {

		// 上传文件
		final FileUploader uploader = new FileUploader(msg.getJtFile(),
				new FileUploader.OnFileUploadListener() {

					@Override
					public void onPrepared() {

					}

					@Override
					public void onStarted() {
						updateUploadItem(msg.getJtFile().mTaskId, FileUploader.Status.Started, 0);
					}

					@Override
					public void onUpdate(int value) {
						if (msg.getJtFile().mType == JTFile.TYPE_FILE) {
							updateUploadItem(msg.getJtFile().mTaskId, FileUploader.Status.Started, value);
						}
					}

					@Override
					public void onCanceled() {
						// 消息发送失败
						msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
						// 从上传列表中删除
						Iterator<FileUploader> iterator = mListUploader.iterator();
						while (iterator.hasNext()) {
							FileUploader fileUploader = iterator.next();
							if (fileUploader.getJTFile().mTaskId.equals(msg.getJtFile().mTaskId)) 
								mListUploader.remove(fileUploader);
						}
						// 更新显示
						updateUploadItem(msg.getJtFile().mTaskId,
								FileUploader.Status.Canceled);
					}

					@Override
					public void onSuccess(String fileUrl) {
						// 设置消息发送标识
						msg.setTime(getLatestTime());
						msg.setIndex(getLatestMessageIndex());
						// msg.getJtFile().mUrl = fileUrl; // 赋值文件地址
						// 发送消息
						CommunityReqUtil.sendMessage(ChatBaseActivity.this, ChatBaseActivity.this, handler, msg);
						// 从上传列表中删除
						Iterator<FileUploader> iterator = mListUploader.iterator();
						while (iterator.hasNext()) {
							FileUploader fileUploader = iterator.next();
							if (fileUploader.getJTFile().mTaskId.equals(msg.getJtFile().mTaskId)) 
								mListUploader.remove(fileUploader);
						}
						// 更新列表
						updateUploadItem(msg.getJtFile().mTaskId, FileUploader.Status.Success);
					}

					@Override
					public void onError(int code, String message) {
						// 消息发送失败，设置失败状态
						msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
						Iterator<FileUploader> iterator = mListUploader.iterator();
						// 从上传列表中删除
						while (iterator.hasNext()) {
							FileUploader fileUploader = iterator.next();
							if (fileUploader.getJTFile().mTaskId.equals(msg.getJtFile().mTaskId)) 
								mListUploader.remove(fileUploader);
						}
						// 更新列表
						updateUploadItem(msg.getJtFile().mTaskId, FileUploader.Status.Error);
					}
				});
		mListUploader.add(uploader);
		uploader.start();
	}
	
	// 更新上传列表项
	public void updateUploadItem(String taskId, int... args) {

		List<IMBaseMessage> listMsg = adapter.getData();
		if (listMsg == null || listMsg.size() < 0) {
			return;
		}
		// 通过索引查找要更新的项目

		// 检查索引的有效性
		if (mUploadQueueIndex.containsKey(taskId)) {
			int index = mUploadQueueIndex.get(taskId).intValue();
			if (index >= listMsg.size()) {
				mUploadQueueIndex.remove(taskId); // 删除索引
			} else {
				IMBaseMessage msg = listMsg.get(index);
				if (msg.getType() != IMBaseMessage.TYPE_FILE
						|| msg.getJtFile() == null
						|| msg.getJtFile().mTaskId == null
						|| !msg.getJtFile().mTaskId.equals(taskId)) {
					mUploadQueueIndex.remove(taskId); // 删除索引
				}
			}
		}

		// 重新计算索引
		if (!mUploadQueueIndex.containsKey(taskId)) {
			for (int i = 0; i < listMsg.size(); i++) {
				IMBaseMessage msg = listMsg.get(i);
				if (msg.getType() == IMBaseMessage.TYPE_FILE
						&& msg.getJtFile() != null
						&& msg.getJtFile().mTaskId != null
						&& msg.getJtFile().mTaskId.equals(taskId)) {
					mUploadQueueIndex.put(taskId, Integer.valueOf(i)); // 插入索引
					break;
				}
			}
		}

		if (mUploadQueueIndex.containsKey(taskId)) {

			int index = mUploadQueueIndex.get(taskId).intValue();
			if (index >= listMsg.size()) {
				return;
			}

			IMBaseMessage msg = listMsg.get(index);

			View v = chatXlv.getChildAt(index - chatXlv.getFirstVisiblePosition());
			if (v == null) {
				return;
			}
			final ViewHolder holder = (ViewHolder) v.getTag();
			if (holder != null && msg != null) {
				if (msg.getType() == IMBaseMessage.TYPE_FILE) { // 上传文件
					switch (args[0]) {
					case FileUploader.Status.Started:
						if (args.length <= 1) {
							return;
						}
						// 更新上传进度
						final int progress = args[1];
						handler.post(new Runnable() {
							@Override
							public void run() {
								holder.rightFileProgressPb.setProgress(progress);
							}
						});
						break;
					case FileUploader.Status.Success:
					case FileUploader.Status.Canceled:
					case FileUploader.Status.Error:
						handler.post(new Runnable() {
							@Override
							public void run() {
								holder.rightFileProgressPb.setVisibility(View.GONE);
							}
						});
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 解除绑定
		unbindService(mConn);
		unregisterReceiver(mReceiver);
		// 停止上传
//		for (FileUploader uploader : mListUploader) {
//			uploader.cancel();
//		}

		//清空数据
		if(listMessage!=null){
			listMessage.clear();
			listMessage = null;
		}
	}
	/**
	 * 获取群聊ID
	 * 
	 * @return
	 */
	public String getThatMucID() {
		return thatMucID;
	}
	
	abstract public void sendMessage(String text);// 发送普通文本消息
	abstract public void sendMessage(IMBaseMessage msg);// 重发消息
	abstract public void sendRichMessage(int type, JTFile file);// 发送富文本消息
	abstract public void getMoreMessage(boolean isBackward); // 获取更多聊天记录，true-查找新记录；false-查找旧记录
	
}
