package com.tr.ui.im;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import m.framework.utils.Utils;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.api.KnowledgeReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.db.ChatLocalFileDBManager;
import com.tr.db.VoiceFileDBManager;
import com.tr.image.ImageLoader;
import com.tr.model.api.DataBox;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.IMUtil;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.service.FileDownloadService;
import com.tr.service.FileDownloadService.MyBinder;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.FilePreviewActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.communities.home.CommunitiesActivity;
import com.tr.ui.communities.home.CommunitiesDetailsActivity;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.im.ChatBaseActivity.ChatBaseAdapter.ViewHolder;
import com.tr.ui.knowledge.CreateKnowledgeActivity.OperateType;
import com.tr.ui.widgets.ChatDialog;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyView;
import com.utils.common.ApolloUtils;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.FileDownloader;
import com.utils.common.FilePathResolver;
import com.utils.common.FileUploader;
import com.utils.common.GlobalVariable;
import com.utils.common.JTDateUtils;
import com.utils.common.OpenFiles;
import com.utils.common.TaskIDMaker;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;
import com.utils.picture.PictureManageUtil;
import com.utils.string.StringUtils;
import com.utils.time.Util;

/**
 * 私聊和群聊基类 注：语音下载使用DownloadManager
 * 
 * @author xuxinjian/leon
 */

public abstract class ChatBaseActivity extends JBaseFragmentActivity implements
		IBindData {

	public static final String TAG = ChatBaseActivity.class.getSimpleName();

	protected final int MAX_MESSAGE_SIZE = 20; // 单次最多加载的消息数目
	
	public final int sendshow = 0;
	public final int sendgone = 1;

	private final int SEND_TEXT = 0; // 发送文本
	private final int SEND_VOICE = 1; // 发送语音

	// 显示消息的View
	protected XListView listView;
	protected ChatBaseAdapter mAdapter;
	// 语音/文本发送
	// private LinearLayout controlLl; // 控制面板
	private ImageView switchIv; // 切换面板
	private TextView voiceTv; // 语音按钮
	private EditText textEt; // 文本按钮
	private ImageView expressionIv;// 表情
	private ImageView sendIv; // 发送/附件

	private LinearLayout viewPagerCon;
	private ViewPager viewPager;

	private List<SmileyView> listSmileyViews;
	private Boolean isShowface = false;
	protected Map<String, String> mHeadImages = new HashMap<String, String>();// 用户点行id和头像url映射表
	private GridView moreGrid = null;

	/* 初始化语音的保存路径 */
	public String recordPath = null;
	public String recordAudioPath = null;
	public String recordNamePrefix = null;
	protected Handler mNetHandler = new Handler();

	// 聊天数据
	protected ArrayList<IMBaseMessage> listMessage;//= new ArrayList<IMBaseMessage>();// 私聊信息列表
	protected ChatDetail chatDetail;// 对方的用户id
	protected String thatMucID;
	protected MUCDetail mucDetail;// 群聊详情
	protected String fromActivityName;// 从哪个activity进入

	protected LinearLayout inputLl;
	// 拍照图片临时文件
	protected File pictureFile;
	// 拍摄视频文件
	protected File videoFile;
	// 待分享的消息
	protected IMBaseMessage mShareMsg;
	protected ArrayList<IMBaseMessage> mShareMsgList; // 待分享的消息
	protected JTFile shareInfo; // 分享单条消息
	protected ArrayList<JTFile> listShareInfo = new ArrayList<JTFile>(); // 分享多条消息
	// 广播过滤器
	private IntentFilter mFilter;
	// 文件上传队列
	protected List<FileUploader> mListUploader = new ArrayList<FileUploader>();
	// 下载服务
	private FileDownloadService mDownloadService;

	// 录音相关
	private MediaRecorder mRecorder;
	private MediaPlayer mPlayer;
	private VoiceFileDBManager voiceFileManager;
	private DownloadManager downloadManager;

	// 列表中最后一项的位置
	private int listViewLastPosition = 0;

	// 控制是都支持头像的长按功能 默认true
	protected boolean isAvatarLongClickable = true;

	// 本地文件数据库管理对象
	protected ChatLocalFileDBManager clfManager = null;

	/** 选中的@的人 */
	protected List<ConnectionsMini> atConnectionsMinis;
	/** 选中的@的人的名称 */
	protected List<String> atConnectionsNames;
	/** 选中的@的人的ID */
	protected List<Integer> atConnectionsIds;

	/** 图片视频的缩放大小（数值按照dp的标准进行等比例放大或者缩小） */
	private static final int SCALESIZE = 200;

	private static final String LEFTSPECCHAR = ((char) 0X1B) + "";
	private static final String RIGHTSPECCHAR = ((char) 0X11) + "";

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

	// 项目更新索引（根据此所以来确定要更新的项目，避免使用遍历造成的计算浪费）
	protected HashMap<String, Integer> mDownloadQueueIndex = new HashMap<String, Integer>();
	protected HashMap<String, Integer> mUploadQueueIndex = new HashMap<String, Integer>();

	// 缓存的超链接
	protected String cacheUrl = "";
	// 缓存的用户消息
	protected String cacheMessage = "";
	// 需要定位的MessageId（查询前后消息，搜索用）
//	protected String searchMessageId = "";
	protected int searchMessagefromIndex = -1;
	protected int searchMessagePosition = -1;
	// 搜素关键字
	protected String searchKeyword = "";

	// 需要定位的消息的ID（通常是@的消息）
	protected String locateMessageId = "";
	protected int locateMessagePosition = -1;
 
	// 录音时间标尺
	private long recordTimestamp = 0;

	private LinearLayout moreGrid_Ll;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.im_chat_base);
		mListUploader.clear();
		initView();
		initVars();
		// 初始化消息列表
		listMessage = new ArrayList<IMBaseMessage>();

		atConnectionsMinis = new ArrayList<ConnectionsMini>();
		atConnectionsNames = new ArrayList<String>();
		atConnectionsIds = new ArrayList<Integer>();

		// 绑定服务对象
		Intent intent = new Intent(ChatBaseActivity.this,
				FileDownloadService.class);
		bindService(intent, mConn, BIND_AUTO_CREATE);

	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		// 解除绑定
		unbindService(mConn);
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

	// 初始化变量
	@SuppressWarnings("unchecked")
	protected void initVars() {
		// 消息对象
		if (getIntent().hasExtra(ENavConsts.EIMBaseMessage)) {
			mShareMsg = (IMBaseMessage) getIntent().getSerializableExtra(
					ENavConsts.EIMBaseMessage);
			if (mShareMsg != null) {
				mShareMsg.setType(mShareMsg.getJtFile()); // 设置消息类型
				shareInfo = mShareMsg.getJtFile();
			}
		} else if (getIntent().hasExtra(ENavConsts.EIMBaseMessageList)) {
			mShareMsgList = (ArrayList<IMBaseMessage>) getIntent()
					.getSerializableExtra(ENavConsts.EIMBaseMessageList);
			if (mShareMsgList != null) {
				for (IMBaseMessage msg : mShareMsgList) { // 设置消息类型
					msg.setType(msg.getJtFile());
					listShareInfo.add(msg.getJtFile());
				}
			}
		} else if (getIntent().hasExtra(ENavConsts.EShareParam)) {
			// 分享单条消息
			shareInfo = (JTFile) getIntent().getSerializableExtra(
					ENavConsts.EShareParam);
		} else if (getIntent().hasExtra(ENavConsts.EShareParamList)) {
			// 分享多条消息
			listShareInfo = (ArrayList<JTFile>) getIntent()
					.getSerializableExtra(ENavConsts.EShareParamList);
		}

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
		// 下载管理器
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		// 搜索结果
		searchMessagefromIndex = getIntent().getIntExtra(
				ENavConsts.ESearchFromIndex,-1);
		searchKeyword = getIntent().getStringExtra(ENavConsts.ESearchKeyword);
		// 定位的消息
		locateMessageId = getIntent().getStringExtra(
				ENavConsts.ELocateMessageID);
	}

	// 清空聊天记录列表
	public void cleanHistory() {
		listMessage = new ArrayList<IMBaseMessage>();
		setData(listMessage);
	}

	@Override
	public void initJabActionBar() {
		ActionBar actionbar = getActionBar();
//		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.im_mucdetail_menu, menu);
		MenuItem menuItem = menu.findItem(R.id.im_muc_detail_create_next);
		if(searchMessagefromIndex!=-1){
			menuItem.setVisible(false);
		}else{
			menuItem.setVisible(true);
		}

		if (chatDetail != null) { // 私聊
			menuItem.setIcon(getResources().getDrawable(R.drawable.chat_shezhi));
		} else { // 群聊
			menuItem.setIcon(getResources().getDrawable(
					R.drawable.hy_ic_action_relation_pressed));
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			if(chatDetail!=null){
				Intent data = new Intent();
				if(listMessage.size()>0){
					data.putExtra("imbasemsg", listMessage.get(listMessage.size()-1));
				}
				setResult(RESULT_OK, data);
				finish();
			}else if(mucDetail!=null){
				Intent data = new Intent();
				if(listMessage.size()>0){
					data.putExtra("imbasemsg", listMessage.get(listMessage.size()-1));
				}
				setResult(RESULT_OK, data);
			}
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {

		// 语音/键盘切换按钮
		switchIv = (ImageView) findViewById(R.id.switchIv);
		switchIv.setOnClickListener(listener);
		switchIv.setTag(SEND_TEXT);
		// 语音按钮
		voiceTv = (TextView) findViewById(R.id.voiceTv);
		voiceTv.setOnTouchListener(mTouchListener);
		voiceTv.setOnClickListener(listener);
		// 消息列表
		listView = (XListView) findViewById(R.id.chatRecordPlv);
		listView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				getMoreMessage(false); // 加载更多消息（查找旧记录
			}

			@Override
			public void onLoadMore() {
				getMoreMessage(false); // 加载更多消息（查找旧记录
			}
		});

		// 输入面板
		inputLl = (LinearLayout) findViewById(R.id.inputLl);
		if (getIntent().hasExtra(ENavConsts.EFromActivityName)
				&& getIntent().getStringExtra(ENavConsts.EFromActivityName)
						.equalsIgnoreCase(
								ChatRecordSearchActivity.class.getSimpleName())) {
			inputLl.setVisibility(View.GONE);
		}
		// 发送/附件按钮
		sendIv = (ImageView) findViewById(R.id.sendIv);
		sendIv.setOnClickListener(listener);
		sendIv.setTag(sendgone);
		// 文本输入框
		textEt = (EditText) findViewById(R.id.textEt);
		textEt.setOnClickListener(listener);
		textEt.addTextChangedListener(new MyTextWatcher());
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
		// 表情
		expressionIv = (ImageView) findViewById(R.id.expressionIv);
		expressionIv.setOnClickListener(listener);
		//
		viewPager = (ViewPager) this.findViewById(R.id.smileyPager);
		viewPagerCon = (LinearLayout) this
				.findViewById(R.id.smileyPagerContainer);
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
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {

				if (arg0 == 0) {
					((ImageView) findViewById(R.id.smileyPagerchange))
							.setImageResource(R.drawable.chat_biaoqing_1);
				} else if (arg0 == 1) {
					((ImageView) findViewById(R.id.smileyPagerchange))
							.setImageResource(R.drawable.chat_biaoqing_2);
				} else if (arg0 == 2) {
					((ImageView) findViewById(R.id.smileyPagerchange))
							.setImageResource(R.drawable.chat_biaoqing_3);
				} else if (arg0 == 3) {
					((ImageView) findViewById(R.id.smileyPagerchange))
							.setImageResource(R.drawable.chat_biaoqing_4);
				} else if (arg0 == 4) {
					((ImageView) findViewById(R.id.smileyPagerchange))
							.setImageResource(R.drawable.chat_biaoqing_5);
				} else if (arg0 == 5) {
					((ImageView) findViewById(R.id.smileyPagerchange))
							.setImageResource(R.drawable.chat_biaoqing_6);
				} else if (arg0 == 6) {
					((ImageView) findViewById(R.id.smileyPagerchange))
							.setImageResource(R.drawable.chat_biaoqing_7);
				} else if (arg0 == 7) {
					((ImageView) findViewById(R.id.smileyPagerchange))
							.setImageResource(R.drawable.chat_biaoqing_8);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

		});
	
		// 更多面板
		moreGrid = (GridView) this.findViewById(R.id.moreGrid);
		moreGrid.setAdapter(new MoreGridAdpter());
		moreGrid_Ll = (LinearLayout)this.findViewById(R.id.moreGrid_Ll);
		// 初始化适配器
		initAdpter();
	}

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
				mHandler.postDelayed(mStopPlayingRunnable, duration);
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
		mHandler.removeCallbacks(mStopPlayingRunnable);
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

	/** 选择多媒体消息后的返回 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case EConsts.ReqCode.SelectFromMyRequirement: { // 需求
			JTFile jtFile = (JTFile) intent
					.getSerializableExtra(EConsts.Key.JT_FILE);
			sendRichMessage(IMBaseMessage.TYPE_REQUIREMENT, jtFile);
		}
			break;
		case EConsts.ReqCode.SelectFromMyKnowledge: { // 知识
			JTFile jtFile = (JTFile) intent
					.getSerializableExtra(EConsts.Key.JT_FILE);
			sendRichMessage(IMBaseMessage.TYPE_KNOWLEDGE2, jtFile);
		}
			break;
		case EConsts.ReqCode.SelectFromMyConnectionAndOrg: { // 关系
			Object object = intent.getSerializableExtra(ENavConsts.redatas);
			if (object != null && object instanceof JTFile) {
				JTFile jtFile = (JTFile) object;
				if (jtFile.getmType() == JTFile.TYPE_JTCONTACT_OFFLINE
						|| jtFile.getmType() == JTFile.TYPE_ORG_OFFLINE
						|| jtFile.getmType() == JTFile.TYPE_CLIENT) { // 分享一个人脉或离线机构（创建一个新的后再分享）

					IMBaseMessage msg = new IMBaseMessage();
					msg.setJtFile(jtFile);
					msg.setType(jtFile);

					if (jtFile.getmType() == JTFile.TYPE_JTCONTACT_OFFLINE) {
						sendRichMessage(IMBaseMessage.TYPE_JTCONTACT_OFFLINE,
								jtFile);
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
						sendRichMessage(IMBaseMessage.TYPE_JTCONTACT_ONLINE,
								jtFile);
					} else if (jtFile.getmType() == JTFile.TYPE_ORG_ONLINE) {
						sendRichMessage(IMBaseMessage.TYPE_ORG_ONLINE, jtFile);
					} else if (jtFile.getmType() == JTFile.TYPE_ORGANIZATION) {
						sendRichMessage(IMBaseMessage.TYPE_ORGANIZATION, jtFile);
					}
				}
			}
		}
			break;
		case EConsts.ReqCode.SelectFromMyConnection_share: { // 新创建的人脉
			Object object = intent.getSerializableExtra(ENavConsts.redatas);
			if (object != null && object instanceof JTFile) {
				JTFile jtFile = (JTFile) object;
				sendRichMessage(
						IMBaseMessage
								.convertJTFileType2IMBaseMessageType(jtFile.mType),
						jtFile);
			}
		}
			break;
		case EConsts.REQ_CODE_PICK_PICTURE: // 选图
		case EConsts.REQ_CODE_TAKE_PICTURE: { // 拍照
			JTFile jtFile = null;
			if (requestCode == EConsts.REQ_CODE_PICK_PICTURE) {
				jtFile = EUtil.createJTFileFromLocalFile(FilePathResolver
						.getPath(this, intent.getData()));
			} else {
				if (pictureFile!=null) {
					jtFile = EUtil.createJTFileFromLocalFile(pictureFile
							.getAbsolutePath());
				}
			}
			if (jtFile != null && jtFile.mType == JTFile.TYPE_IMAGE) {
				sendRichMessage(IMBaseMessage.TYPE_IMAGE, jtFile);
			} else {
				showToast("图片地址解析错误或不支持的图片格式");
			}
			break;
		}
		case EConsts.REQ_CODE_PICK_FILE: { // 文件
			JTFile jtFile = EUtil.createJTFileFromLocalFile(FilePathResolver
					.getPath(this, intent.getData()));
			sendRichMessage(IMBaseMessage.TYPE_FILE, jtFile);
		}
			break;
		case EConsts.REQ_CODE_TAKE_VIDEO: // 拍视频
		case EConsts.REQ_CODE_PICK_VIDEO: { // 选视频
			JTFile jtFile = null;
			if (requestCode == EConsts.REQ_CODE_TAKE_VIDEO) {
				if (videoFile!=null) {
					jtFile = EUtil.createJTFileFromLocalFile(videoFile
							.getAbsolutePath());
				}
			} else {
				jtFile = EUtil.createJTFileFromLocalFile(FilePathResolver
						.getPath(this, intent.getData()));
			}
			if (jtFile != null && jtFile.mType == JTFile.TYPE_VIDEO) {
				sendRichMessage(IMBaseMessage.TYPE_VIDEO, jtFile);
			} else {
				showToast("视频地址解析错误或不支持的视频格式");
			}
		}
			break;
		case ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_AT_FRIENDS: { // 邀请好友
			ConnectionsMini connectionsMini = (ConnectionsMini) intent
					.getExtras().getSerializable(ENavConsts.EConnectionsMini);

			String before_text = textEt.getText().toString();
			String after_text = before_text.substring(0, mStart) + LEFTSPECCHAR +"@"+ connectionsMini.getName() + RIGHTSPECCHAR +" "+ before_text.substring(mStart+1, before_text.length());
			textEt.setText(after_text);
			textEt.setSelection(textEt.getText().length());
			atConnectionsMinis.add(connectionsMini);
		}
			break;

		}
	}

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
											cacheDir = EUtil.getChatImageCacheDir(ChatBaseActivity.this, getChatId());
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
											cacheDir = EUtil.getChatVideoCacheDir(ChatBaseActivity.this,getChatId());
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

	private String beforeText = "";
	private int mStart = 0;
	private class MyTextWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			String inputStr = s.toString();
			if (inputStr.length() == 0) { // StringUtils.isEmpty(inputStr)
											// 无法处理"null"的字符串
				sendIv.setImageResource(R.drawable.chat_more);
				sendIv.setTag(sendgone);
			} else {
				sendIv.setImageResource(R.drawable.chat_send);
				moreGrid.setVisibility(View.GONE);
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
					if(temp_after.equals("@") && mucDetail != null){
						IMChatMessageCache.getInstance().setMucDetailCache(
								mucDetail);
						ENavigate
								.startAtInformFriendsActivityForResult(
										ChatBaseActivity.this,
										ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_AT_FRIENDS);
					}
				}else if (start >= 1) {
					String temp_before = beforeText.charAt(start-1)+"";
					if(temp_after.equals("@") && (Util.checkIsChinses(temp_before) || !Util.checkIsEngANum(temp_before))){//输入字符是“@”,其之前的字符是中文或者不是英文和数字
						if (mucDetail != null) {
							IMChatMessageCache.getInstance().setMucDetailCache(
									mucDetail);
							ENavigate
									.startAtInformFriendsActivityForResult(
											ChatBaseActivity.this,
											ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_AT_FRIENDS);
	
						}
					}
				}
			}else if (s.length() < beforeText.length() && start >= 1) {
				StringBuilder builder = new StringBuilder();
				// 删除的左侧特殊字符 向后索引（暂不处理此种情况）
				if ((beforeText.charAt(start) + "").equals(LEFTSPECCHAR)) {
					/*
					 * for (int i = start; i < beforeText.length(); i++) { //
					 * 等于右侧字符 if ((beforeText.charAt(i) +
					 * "").equals(RIGHTSPECCHAR)) {
					 * builder.append(beforeText.subSequence(0,
					 * start)).append(beforeText.subSequence(i + 1,
					 * beforeText.length()));
					 * textEt.setText(builder.toString());
					 * textEt.setSelection(start); break; } }
					 */
				}
				// 如果删除的是@
				else if ((beforeText.charAt(start - 1) + "")
						.equals(LEFTSPECCHAR)
						&& (beforeText.charAt(start) + "").equals("@")) {
					for (int i = start - 1; i < beforeText.length(); i++) {
						// 等于右侧字符
						if ((beforeText.charAt(i) + "").equals(RIGHTSPECCHAR)) {
							builder.append(beforeText.subSequence(0, start - 1))
									.append(beforeText.subSequence(i + 1,
											beforeText.length()));
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
							builder.append(s.subSequence(0, i)).append(
									s.subSequence(start, s.length()));
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

	/**
	 * 添加颜色块
	 * 
	 * @param s
	 */
	public Spanned addAtBgBlock(String s) {
		return Html.fromHtml("<body bgcolor=\"#bde8fc\">" + s + "<body/>");
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
			/*
			 * if (position == 0) { ((ViewPager) container).addView(smileyView);
			 * return smileyView; } else if (position == 1) {
			 * smileyView2.setVisibility(View.VISIBLE); ((ViewPager)
			 * container).addView(smileyView2); return smileyView2; } return
			 * null;
			 */
			((ViewPager) container).addView(listSmileyViews.get(position));
			return listSmileyViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			/*
			 * if (position == 0) { container.removeView(smileyView); } else {
			 * container.removeView(smileyView2); }
			 */
			container.removeView(listSmileyViews.get(position));
		}
	}

	// 重发消息
	public void sendMessage(final IMBaseMessage msg) {
		// 重新设置发送状态
		msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
		// 刷新列表
		setData(listMessage);
//		new AsyncTask<Void, Void, Void>() {
//
//			@Override
//			protected Void doInBackground(Void... params) {
//				// 更新数据库状态
//				chatRecordManager.setMessageSendType(App.getUserID(),
//						getChatId(), msg.getMessageID(),
//						IMBaseMessage.SEND_TYPE_SENDING);
//				return null;
//			}
//		}.execute();

		if (msg.getType() == IMBaseMessage.TYPE_TEXT
				|| msg.getType() == IMBaseMessage.TYPE_REQUIREMENT
				|| msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE
				|| msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE
				|| msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE
				|| msg.getType() == IMBaseMessage.TYPE_ORG_OFFLINE
				|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE
				|| msg.getType() == IMBaseMessage.TYPE_ORGANIZATION) {
			IMReqUtil.sendMessage(this, this, strangerHandler, msg); // 直接重发
		} else { // 发送文件（上传失败还是消息发送失败）
			if (!TextUtils.isEmpty(msg.getJtFile().mUrl)) { // 文件已上传，直接重发
				IMReqUtil.sendMessage(this, this, mNetHandler, msg);
			} else {
				if (msg.getJtFile().mType == JTFile.TYPE_VIDEO
						&& TextUtils.isEmpty(msg.getJtFile().reserved2)) { // 视频截图未上传

					String screenshotPath = EUtil.getChatVideoScreenshot(this,
							getChatId(), msg.getJtFile().mLocalFilePath);
					if (TextUtils.isEmpty(screenshotPath)) {
						msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
//						new AsyncTask<Void, Void, Void>() {
//
//							@Override
//							protected Void doInBackground(Void... params) {
//								// 更新数据库状态
//								chatRecordManager.setMessageSendType(
//										App.getUserID(), getChatId(),
//										msg.getMessageID(),
//										IMBaseMessage.SEND_TYPE_FAIL);
//								return null;
//							}
//						}.execute();

						setData(listMessage);
						showToast("获取视频截图失败");
						return;
					}
					startNewUploadTask(msg);
				} else {
					startNewUploadTask(msg);
				}
			}
		}
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
						updateUploadItem(msg.getJtFile().mTaskId,
								FileUploader.Status.Started, 0);
					}

					@Override
					public void onUpdate(int value) {
						if (msg.getJtFile().mType == JTFile.TYPE_FILE) {
							updateUploadItem(msg.getJtFile().mTaskId,
									FileUploader.Status.Started, value);
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
						IMReqUtil.sendMessage(ChatBaseActivity.this,
								ChatBaseActivity.this, strangerHandler, msg);
						// 从上传列表中删除
						Iterator<FileUploader> iterator = mListUploader.iterator();
						while (iterator.hasNext()) {
							FileUploader fileUploader = iterator.next();
							if (fileUploader.getJTFile().mTaskId.equals(msg.getJtFile().mTaskId)) 
								mListUploader.remove(fileUploader);
						}
						// 更新列表
						updateUploadItem(msg.getJtFile().mTaskId,
								FileUploader.Status.Success);
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
						updateUploadItem(msg.getJtFile().mTaskId,
								FileUploader.Status.Error);
					}
				});
		mListUploader.add(uploader);
		uploader.start();
	}

	abstract public void cleanNotifyBox(String id); // 清空通知栏消息

	abstract public void sendMessage(String text); // 发送文本消息

	abstract public void sendRichMessage(int type, JTFile file); // 发送富媒体消息
	// abstract public void sendShareMessage(IMBaseMessage imMsg); // 发送分享的消息

	abstract public void getMoreMessage(boolean isBackward); // 获取更多聊天记录，true-查找新记录；false-查找旧记录
	abstract public void getMessageRecord(int fromIndex); // 查询聊天记录

	abstract public String getNickNameByMessage(IMBaseMessage msg); // 获取消息发送人的昵称

	abstract public String getImageByMessage(IMBaseMessage msg); // 获取消息发送人的头像

	abstract public int getSenderTypeByMessage(IMBaseMessage msg); // 获取消息发送人类型
	
	abstract public String getChatId();//获取聊天id

	public void initAdpter() {
		mAdapter = new ChatBaseAdapter(ChatBaseActivity.this);
		messageList = new ArrayList<IMBaseMessage>();

//		IMBaseMessage i = new ChatMessage("");
//		i.setSenderType(IMBaseMessage.MSG_OTHER_SEND);
//		messageList.add(i);
		listView.setAdapter(mAdapter);
		mAdapter.setData(messageList);
//		setData(messageList);
	}

	/**
	 * 设置列表适配器
	 * 
	 * @param messageList
	 */
	public void setData(List<IMBaseMessage> messageList) {
		if(mAdapter != null){
			mAdapter.setData(messageList);
		}
		refreshList();
	}

	private OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == sendIv.getId()) { // 发送按钮

				int inttag = ((Integer) sendIv.getTag()).intValue();
				if (inttag == sendshow) { // send按钮在显示
					moreGrid.setVisibility(View.GONE);
					moreGrid_Ll.setVisibility(View.GONE);
					sendIv.setTag(sendgone);
					// 显示发送
					isShowface = false;
					String txtSend = textEt.getText().toString().trim();

					// 生成@的人的Id的数组
					// atConnectionsNames
					// atConnectionsIds
					// atConnectionsMinis

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
									atConnectionsNames.add(txtSend.substring(
											beforeIndex + 2, j));
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
					if (txtSend.startsWith("http://")
							|| txtSend.startsWith("https://")) {
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
					// 隐藏软盘
					// InputMethodManager m = (InputMethodManager)
					// getSystemService(INPUT_METHOD_SERVICE);
					// m.hideSoftInputFromWindow(textEt.getApplicationWindowToken(),
					// 0);
					viewPagerCon.setVisibility(View.GONE);
					expressionIv.setImageResource(R.drawable.chat_biaoqing);
				} else {
					if (moreGrid.getVisibility() == View.GONE) {
						moreGrid.setVisibility(View.VISIBLE);
						moreGrid_Ll.setVisibility(View.VISIBLE);
						sendIv.setTag(sendgone);
						viewPagerCon.setVisibility(View.GONE);
						// 隐藏软盘，加了之后gridView起不来
						InputMethodManager m = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						m.hideSoftInputFromWindow(
								textEt.getApplicationWindowToken(), 0);
					} else {
						moreGrid.setVisibility(View.GONE);
						moreGrid_Ll.setVisibility(View.GONE);

						sendIv.setTag(sendgone);
						viewPagerCon.setVisibility(View.GONE);
						// 显示软盘
						InputMethodManager manager = (InputMethodManager) textEt
								.getContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						manager.toggleSoftInput(0,
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
			} else if (v.getId() == expressionIv.getId()) { // 笑脸按钮
				if (!isShowface) {
					// sendImageIv.setImageResource(R.drawable.chat_keyboard);
					viewPagerCon.setVisibility(View.VISIBLE);
					isShowface = true;
					moreGrid.setVisibility(View.GONE);
					moreGrid_Ll.setVisibility(View.GONE);

					// 隐藏软盘，加了之后gridView起不来
					InputMethodManager m = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					m.hideSoftInputFromWindow(
							textEt.getApplicationWindowToken(), 0);
					// 消息定位到最新
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							listView.setSelection(mAdapter.getData().size());
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
			} else if (v.getId() == textEt.getId()) {
				if (isShowface) {
					expressionIv.setImageResource(R.drawable.chat_biaoqing);
					viewPagerCon.setVisibility(View.GONE);
					isShowface = false;
					// 显示软盘
					textEt.setFocusableInTouchMode(true);
				}
				moreGrid.setVisibility(View.GONE);
				moreGrid_Ll.setVisibility(View.GONE);

			} else if (v.getId() == switchIv.getId()) { // 切换语音和文本
				viewPagerCon.setVisibility(View.GONE);
				isShowface = false;
				moreGrid.setVisibility(View.GONE);
				moreGrid_Ll.setVisibility(View.GONE);

				sendIv.setTag(sendgone);
				int tag = ((Integer) v.getTag()).intValue();
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
			}
		}
	};


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

	private OnTouchListener mTouchListener = new OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: // 按下（开始录音）

				// 设置界面状态
				voiceTv.setText("松开 发送");
				// 间隔时间是否足够
				if (recordTimestamp > 0
						&& System.currentTimeMillis() - recordTimestamp < 1200) {
					return false;
				}
				// 间隔时间度量衡
				recordTimestamp = System.currentTimeMillis();
				// 停止播放
				stopPlaying();
				// 文件名(保证唯一)
				mAudioName = generateRandomFileName()
						+ EConsts.DEFAULT_AUDIO_SUFFIX;
				Log.d(TAG, "********************语音文件名********************"
						+ mAudioName);
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
				if (TextUtils.isEmpty(mAudioPath)
						|| !new File(mAudioPath).exists()) {
					break;
				}
				// 延时500ms停止
				mHandler.postDelayed(new Runnable() {
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
							long duration = TimeUnit.MILLISECONDS
									.toSeconds(player.getDuration());
							if (duration < 1) { // 不足1s
								showToast("录音时间太短");
								return;
							} else { // 发送消息
								JTFile jtFile = new JTFile();
								jtFile.fileName = mAudioName;
								jtFile.mType = JTFile.TYPE_AUDIO;
								jtFile.mTaskId = TaskIDMaker.getTaskId(App
										.getUserID()); // 上传所需
								jtFile.mLocalFilePath = jtFile.reserved1 = mAudioPath; // 本地文件路径
								jtFile.reserved2 = duration + ""; // 时长(单位:s)
								jtFile.mFileSize = new File(mAudioPath)
										.length(); // 文件大小
								sendRichMessage(IMBaseMessage.TYPE_AUDIO,
										jtFile); // 发送消息
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}, 500);
				break;
			}
			return false;
		}
	};

	/**
	 * 随时调用检查
	 * 
	 * @return
	 */
	private File getVoiceFileDir() {
		return EUtil.getChatVoiceCacheDir(this, getChatId());
	}

	@Override
	public void onResume() {
		super.onResume();
		// 注册下载广播监听器
		registerReceiver(mReceiver, mFilter);
		// 用来启动更新
		// 隐藏分享框
		moreGrid.setVisibility(View.GONE);
		moreGrid_Ll.setVisibility(View.GONE);

		// 隐藏软键盘
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(textEt.getWindowToken(), 0);
	}

	@Override
	public void onPause() {
		super.onPause();
		stopGet();
		// 取消下载监听器
		unregisterReceiver(mReceiver);
		// 暂停所有文件上传
//		for (FileUploader uploader : mListUploader) {
//			uploader.cancel();
//		}
		// 回收录音和播放资源
		/*
		 * if (mRecorder != null) { mRecorder.release(); mRecorder = null; } if
		 * (mPlayer != null) { mPlayer.release(); mPlayer = null; }
		 */
		// 隐藏语音提示框
		/*
		 * if(chatDlg != null && chatDlg.isShowing()){ chatDlg.dismiss(); }
		 */
	}

	@Override
	public void onStop() {
		super.onStop();
		// 回收录音和播放资源
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
		stopPlayingAnim();
	}

	/*
	 * @Override public void onNewIntent(Intent intent){
	 * super.onNewIntent(intent); // 锁定到指定条目 String messageId =
	 * intent.getStringExtra(ENavConsts.EMessageID); for (int i = 0; i <
	 * mAdapter.getData().size(); i++) { if
	 * (mAdapter.getData().get(i).getMessageID().equals(messageId)) { final int
	 * pos = i; mHandler.post(new Runnable(){
	 * 
	 * @Override public void run() { listView.setSelection(pos); } }); break; }
	 * } }
	 */

	/** 刷新列表 */
	protected void refreshList() {
		String MSG = "refreshList()";
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	
		if (listView != null && mAdapter != null && mAdapter.getCount() > 0) {
			listView.setSelection(listView.getAdapter().getCount() - 1);
			Log.i(TAG, MSG + " listView.getAdapter().getCount() = " + listView.getAdapter().getCount());
		}
	}

	/** 定位到最新的消息 */
	protected void locateToLatestMessage() {
		if (listView != null && !listView.isRefreshing() && mAdapter != null
				&& mAdapter.getCount() > 0) {
			listView.setSelection(listView.getAdapter().getCount() - 1);
		}
	}

	@Override
	public void onBackPressed() {
		if (isShowface && viewPagerCon != null) {
			viewPagerCon.setVisibility(View.GONE);
			isShowface = false;
			if (expressionIv != null) {
				expressionIv.setImageResource(R.drawable.chat_biaoqing);
			}
			return;
		}
		if (moreGrid.getVisibility() == View.VISIBLE) {
			moreGrid.setVisibility(View.GONE);
			moreGrid_Ll.setVisibility(View.GONE);

			return;
		}
		if(chatDetail!=null){
			Intent data = new Intent();
			if(listMessage.size()>0){
				data.putExtra("imbasemsg", listMessage.get(listMessage.size()-1));
			}
			setResult(RESULT_OK, data);
			finish();
		}else if(mucDetail!=null){
			Intent data = new Intent();
			if(listMessage.size()>0){
				data.putExtra("imbasemsg", listMessage.get(listMessage.size()-1));
			}
			setResult(RESULT_OK, data);
		}
		super.onBackPressed();
	}

	public void startUserActivity(String userID) {
		KeelLog.d(TAG, "startUserActivity userID;" + userID);
		if (!TextUtils.isEmpty(userID)) {
			ENavigate.startUserDetailsActivity(this, userID, true, 0);
		}
	}

	/** 是否重新发送 */
	private void showSendMessageDialog(final IMBaseMessage msg) {
		new AlertDialog.Builder(ChatBaseActivity.this)
				.setTitle(R.string.str_again_send_title)
				.setMessage(R.string.str_again_send_hint)
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 执行重发操作
								msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
								sendMessage(msg);
							}
						}).setNegativeButton(R.string.str_cancel, null)
				.create().show();
	}

	protected class ChatBaseAdapter extends BaseAdapter {

		// 删除该条聊天记录
		private void deleteItem(IMBaseMessage msg, int position) {
			int type = 0;
			long mucId = 0;
			long senderId = 0;
			IMBaseMessage imbaseMsg = mAdapter.getData().get(position);
			if(imbaseMsg.getImtype() == IMBaseMessage.IM_TYPE_CHAT){
				type = 1;
				mucId = Long.valueOf(chatDetail.getThatID());
				senderId = Long.valueOf(imbaseMsg.getSenderID());
			}else if(imbaseMsg.getImtype() == IMBaseMessage.IM_TYPE_MUC){
				type = 2;
				mucId = Long.valueOf(mucDetail.getId());
				senderId = Long.valueOf(imbaseMsg.getSenderID());
			}
			
			IMReqUtil.clientDeleteMessage(ChatBaseActivity.this, ChatBaseActivity.this, Long.valueOf(App.getUserID()), imbaseMsg.getMessageID(), type, 0, mucId, senderId, 0, mHandler);
//			deleteCacheMessage(mAdapter.getData().get(position).getMessageID()); // 从数据库移除（设置为不可见）
			mAdapter.getData().remove(position); // 删除该节点
//			mAdapter.notifyDataSetChanged(); // 更新列表
//			showToast("记录已删除");
		}

		// 聊天项长按
		private void showItemLongClickDialog(final IMBaseMessage msg,
				final int position) {
			try {
			String[] listOperation = null;

			switch (msg.getType()) {
			case IMBaseMessage.TYPE_TEXT: // 文本
				listOperation = new String[] { "复制", "转发/分享", "删除" };
//				listOperation = new String[] { "复制", "转发/分享"};
				break;
			case IMBaseMessage.TYPE_IMAGE: // 图片
			case IMBaseMessage.TYPE_VIDEO: // 视频
			case IMBaseMessage.TYPE_FILE: // 文件
			case IMBaseMessage.TYPE_AUDIO: // 语音
				listOperation = new String[] { "转发", "删除", "保存" };
//				listOperation = new String[] { "转发", "保存" };
				break;
			case IMBaseMessage.TYPE_KNOWLEDGE: // 知识
			case IMBaseMessage.TYPE_KNOWLEDGE2: // 新知识
				listOperation = new String[] { "收藏", "转发/分享", "保存", "删除" };
//				listOperation = new String[] { "收藏", "转发/分享", "保存" };
				break;
			case IMBaseMessage.TYPE_REQUIREMENT: // 需求
//				listOperation = new String[] { "关注", "转发/分享", "删除" };
				listOperation = new String[] { "转发/分享", "删除" };//需求长按没有关注
//				listOperation = new String[] { "转发/分享" };//需求长按没有关注
				break;
			case IMBaseMessage.TYPE_JTCONTACT_OFFLINE: // 关系
			case IMBaseMessage.TYPE_JTCONTACT_ONLINE:
			case IMBaseMessage.TYPE_ORG_OFFLINE:
			case IMBaseMessage.TYPE_ORG_ONLINE:
			case IMBaseMessage.TYPE_ORGANIZATION:
			case IMBaseMessage.TYPE_CUSTOMER:
				listOperation = new String[] { "转发/分享", "删除" };
//				listOperation = new String[] { "转发/分享"};
				break;
			case IMBaseMessage.TYPE_CONFERENCE: // 会议
				listOperation = new String[] { "转发", "删除" };
//				listOperation = new String[] { "转发"};
				break;
			case IMBaseMessage.TYPE_COMMUNITY: // 社群
				listOperation = new String[] { "转发", "删除" };
				break;
			default:
				break;
			}
			// 显示弹出框
			new AlertDialog.Builder(mContext)
					.setItems(listOperation,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// 封装操作类型，0-复制文字；1-转发；2-转发/分享；3-收藏知识；4-关注需求；5-保存知识；6-删除；
									int operationType = -1;
									if (msg.getType() == IMBaseMessage.TYPE_TEXT) { // 对文本操作
										if (which == 0) {
											operationType = 0;
										} else if (which == 1) {
											operationType = 1;
										} else if (which == 2) {
											operationType = 6;
										}
									} else if (msg.getType() == IMBaseMessage.TYPE_IMAGE
											|| msg.getType() == IMBaseMessage.TYPE_VIDEO
											|| msg.getType() == IMBaseMessage.TYPE_FILE
											|| msg.getType() == IMBaseMessage.TYPE_AUDIO) { // 对图片、视频、音频、文件操作
										if (which == 0) {
											operationType = 1;
										} else if (which == 1) {
											operationType = 6;
										} else if (which == 2) {
											operationType = 7;
										}
									} else if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE
											|| msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 对知识操作
										if (which == 0) {
											operationType = 3;
										} else if (which == 1) {
											operationType = 2;
										} else if (which == 2) {
											operationType = 5;
										} else if (which == 3) {
											operationType = 6;
										}
									} else if (msg.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 对需求操作
										if (which == 0) {
											operationType = 2;
										} else if (which == 1) {
											operationType = 6;
										} 
									}else if (msg.getType() == IMBaseMessage.TYPE_COMMUNITY) { // 对社群操作
										if (which == 0) {
											operationType = 1;
										} else if (which == 1) {
											operationType = 6;
										} 
									} else if (msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE
											|| msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE
											|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE
											|| msg.getType() == IMBaseMessage.TYPE_ORGANIZATION
											|| msg.getType() == IMBaseMessage.TYPE_ORG_OFFLINE
											|| msg.getType() == IMBaseMessage.TYPE_CUSTOMER) { // 对关系操作
										if (which == 0) {
											operationType = 2;
										} else if (which == 1) {
											operationType = 6;
										}
									} else if (msg.getType() == IMBaseMessage.TYPE_CONFERENCE) { // 对会议操作
										if (which == 0) {
											operationType = 1;
										} else if (which == 1) {
											operationType = 6;
										}
									}

									if (operationType == 0) { // 复制文本
										((ClipboardManager) getSystemService(CLIPBOARD_SERVICE))
												.setText(msg.getContent());
										showToast("已复制");
									} else if (operationType == 1) { // 转发
										if (msg.getType() == IMBaseMessage.TYPE_TEXT) {
											if(msg.getJtFile()==null){
												msg.setJtFile(new JTFile());//发送失败的文本，jtfile为null
											}
											msg.getJtFile().mFileName = msg
													.getContent();
											msg.getJtFile().mType = JTFile.TYPE_TEXT;
										}
										ENavigate.startSocialShareActivity(
												ChatBaseActivity.this,
												msg.getJtFile());
									} else if (operationType == 2) { // 转发分享
										FrameWorkUtils.showSharePopupWindow2(
												ChatBaseActivity.this,
												msg.getJtFile());
									} else if (operationType == 3) { // 收藏知识
										if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 收藏旧知识
											showLoadingDialog();
											cacheUrl = "";
											CommonReqUtil
													.doFetchExternalKnowledgeUrl(
															ChatBaseActivity.this,
															ChatBaseActivity.this,
															msg.getJtFile().mUrl,
															true, null);
										} else if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 收藏新知识
											showLoadingDialog();
											KnowledgeReqUtil
													.doUpdateCollectKnowledge(
															ChatBaseActivity.this,
															ChatBaseActivity.this,
															Long.parseLong(msg
																	.getJtFile().mTaskId),
															Integer.parseInt(msg
																	.getJtFile().reserved1),
															"", null);
										}
									} else if (operationType == 4) { // 关注需求
										showLoadingDialog();
										UserReqUtil
												.doFocusRequirement(
														ChatBaseActivity.this,
														ChatBaseActivity.this,
														UserReqUtil
																.getDoFocusRequirementParams(
																		msg.getJtFile().mTaskId,
																		true),
														null);
									} else if (operationType == 5) { // 保存知识
										if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 保存旧知识
											ENavigate
													.startCreateKnowledgeActivity(
															ChatBaseActivity.this,
															true,
															msg.getJtFile().mUrl,false);
										} else if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 保存新知识
											showLoadingDialog();
											KnowledgeReqUtil
													.doGetKnoDetailsBySaveKno(
															ChatBaseActivity.this,
															ChatBaseActivity.this,
															Long.parseLong(msg
																	.getJtFile().mTaskId),
															Integer.parseInt(msg
																	.getJtFile().reserved1),
															null);
										}
									} else if (operationType == 6) { // 删除
										deleteItem(msg, position);
									} else if (operationType == 7) {// 对图片、视频、音频、文件操作
										if(messageList.get(position) != null && messageList.get(position)
												.getJtFile() != null){
											/*图片、视频、音频、文件 的idT*/
											try {
												fileIds = Util.getDownloadIdByUrl(messageList.get(position)
																.getJtFile().getmUrl());
											} catch (Exception ex) {
												ex.printStackTrace();
											}
										}
										/*跳转到二级目录，选择目录（保存目录ID*/
										ENavigate.startFileManagementActivity(ChatBaseActivity.this, fileIds);
									}
								}

							}).create().show();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		private Context mContext;
		private LayoutInflater inflater = null;
		private List<IMBaseMessage> messageList;
		private String currentNick;
		private SmileyParser parser;
		// private SmileyParser2 parser2;
		private int grayColor;
		private Drawable defaultHeadPic;
		private int chatMargin = 30;
		private String costPerPersonFormat;
		private Bitmap netImage;
		private Bitmap bitmap;
		private Bitmap bmpOriginal;
		private String filePath;
		private Cursor cursor;

		public ChatBaseAdapter(Context context) {
			this.mContext = context;
			inflater = LayoutInflater.from(context);
			parser = SmileyParser.getInstance(context);
			// parser2 = SmileyParser2.getInstance(context);
			final Resources res = context.getResources();
			grayColor = res.getColor(R.color.gray);
			defaultHeadPic = res.getDrawable(R.drawable.im_img_user);
			chatMargin = (int) getResources()
					.getDimension(R.dimen.chant_margin);
			costPerPersonFormat = "人均：%1$s元";
		}


		public void setData(List<IMBaseMessage> messageList) {
			this.messageList = messageList;
			this.notifyDataSetChanged();
		}

		/**
		 * 返回消息列表
		 * 
		 * @return
		 */
		public List<IMBaseMessage> getData() {
			return this.messageList;
		}

		@Override
		public int getCount() {
			return messageList != null ? messageList.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return messageList != null ? messageList.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			// final int nowPos = position;
			if (messageList != null && position < messageList.size()) {
				final IMBaseMessage item = (IMBaseMessage) messageList
						.get(position);

				final int f_position = position;
				if (convertView == null) {
					convertView = inflater.inflate(
							R.layout.im_chat_message_item, null);
					holder = new ViewHolder();
					holder.viewBG = (View) convertView
							.findViewById(R.id.im_message_bg);
					holder.leftLayout = (RelativeLayout) convertView
							.findViewById(R.id.left_letter_layout);
					holder.rightLayout = (RelativeLayout) convertView
							.findViewById(R.id.right_letter_layout);

					holder.leftContent = (TextView) convertView
							.findViewById(R.id.left_letter_content);
					holder.rightContent = (TextView) convertView
							.findViewById(R.id.right_letter_content);

					holder.leftHead = (CircleImageView) convertView
							.findViewById(R.id.head);
					holder.rightHead = (CircleImageView) convertView
							.findViewById(R.id.right_head);
					holder.leftName = (TextView) convertView
							.findViewById(R.id.leftuserName);
					holder.rightName = (TextView) convertView
							.findViewById(R.id.rightuserName);

					holder.letterDate = (TextView) convertView
							.findViewById(R.id.letter_date);
					holder.systemMessage = (TextView) convertView
							.findViewById(R.id.system_message);
					holder.sendMsgProgress = (ProgressBar) convertView
							.findViewById(R.id.send_message_progress);
					holder.sendMsgFail = (ImageView) convertView
							.findViewById(R.id.send_message_fail);

					// 语音部分
					holder.leftVoiceLl = (LinearLayout) convertView
							.findViewById(R.id.leftVoiceLl);
					holder.leftVoiceDurationLl = (LinearLayout) convertView
							.findViewById(R.id.leftVoiceDurationLl);
					holder.leftVoiceIv = (ImageView) convertView
							.findViewById(R.id.leftVoiceIv);
					holder.leftVoiceDurationTv = (TextView) convertView
							.findViewById(R.id.leftVoiceDurationTv);
					holder.leftVoiceLoadingPb = (ProgressBar) convertView
							.findViewById(R.id.leftVoiceLoadingPb);

					holder.rightVoiceLl = (LinearLayout) convertView
							.findViewById(R.id.rightVoiceLl);
					holder.rightVoiceDurationLl = (LinearLayout) convertView
							.findViewById(R.id.rightVoiceDurationLl);
					holder.rightVoiceLoadingPb = (ProgressBar) convertView
							.findViewById(R.id.rightVoiceLoadingPb);
					holder.rightVoiceDurationTv = (TextView) convertView
							.findViewById(R.id.rightVoiceDurationTv);
					holder.rightVoiceIv = (ImageView) convertView
							.findViewById(R.id.rightVoiceIv);

					// 图片部分
					holder.leftImageRl = (RelativeLayout) convertView
							.findViewById(R.id.leftImageRl);
					holder.leftImageIv = (ImageView) convertView
							.findViewById(R.id.leftImageIv);

					holder.rightImageRl = (RelativeLayout) convertView
							.findViewById(R.id.rightImageRl);
					holder.rightImageIv = (ImageView) convertView
							.findViewById(R.id.rightImageIv);

					// 视频部分
					holder.leftVideoRl = (RelativeLayout) convertView
							.findViewById(R.id.leftVideoRl);
					holder.leftVideoIv = (ImageView) convertView
							.findViewById(R.id.leftVideoIv);
					holder.leftPlayIv = (ImageView) convertView
							.findViewById(R.id.leftPlayIv);

					holder.rightVideoRl = (RelativeLayout) convertView
							.findViewById(R.id.rightVideoRl);
					holder.rightVideoIv = (ImageView) convertView
							.findViewById(R.id.rightVideoIv);
					holder.rightPlayIv = (ImageView) convertView
							.findViewById(R.id.rightPlayIv);

					// 分享部分
					holder.leftShareLl = (LinearLayout) convertView
							.findViewById(R.id.leftShareLl);
					holder.leftShareTopLl = (LinearLayout) convertView
							.findViewById(R.id.leftShareTopLl);
					holder.leftShareMsgTv = (TextView) convertView
							.findViewById(R.id.leftShareMsgTv);
					holder.leftShareBottomLl = (LinearLayout) convertView
							.findViewById(R.id.leftShareBottomLl);
					holder.leftShareImageIv = (ImageView) convertView
							.findViewById(R.id.leftShareImageIv);
					holder.leftShareTypeIv = (ImageView) convertView
							.findViewById(R.id.leftShareTypeIv);
					holder.leftShareTitleTv = (TextView) convertView
							.findViewById(R.id.leftShareTitleTv);
					holder.leftShareContentTv = (TextView) convertView
							.findViewById(R.id.leftShareContentTv);
					holder.leftShareLinkIv = (TextView) convertView
							.findViewById(R.id.leftShareLinkIv);

					holder.rightShareLl = (LinearLayout) convertView
							.findViewById(R.id.rightShareLl);
					holder.rightShareTopLl = (LinearLayout) convertView
							.findViewById(R.id.rightShareTopLl);
					holder.rightShareMsgTv = (TextView) convertView
							.findViewById(R.id.rightShareMsgTv);
					holder.rightShareBottomLl = (LinearLayout) convertView
							.findViewById(R.id.rightShareBottomLl);
					holder.rightShareImageIv = (ImageView) convertView
							.findViewById(R.id.rightShareImageIv);
					holder.rightShareTypeIv = (ImageView) convertView
							.findViewById(R.id.rightShareTypeIv);
					holder.rightShareTitleTv = (TextView) convertView
							.findViewById(R.id.rightShareTitleTv);
					holder.rightShareContentTv = (TextView) convertView
							.findViewById(R.id.rightShareContentTv);
					holder.rightShareLinkIv = (TextView) convertView
							.findViewById(R.id.rightShareLinkIv);

					// 分享文件部分
					holder.leftFileLl = (LinearLayout) convertView
							.findViewById(R.id.leftFileLl);
					holder.leftFileTypeIv = (ImageView) convertView
							.findViewById(R.id.leftFileTypeIv);
					holder.leftFileNameTv = (TextView) convertView
							.findViewById(R.id.leftFileNameTv);
					holder.leftFileSizeTv = (TextView) convertView
							.findViewById(R.id.leftFileSizeTv);
					holder.leftFileStatusTv = (TextView) convertView
							.findViewById(R.id.leftFileStatusTv);
					holder.leftFileProgressPb = (ProgressBar) convertView
							.findViewById(R.id.leftFileProgressPb);

					holder.rightFileLl = (LinearLayout) convertView
							.findViewById(R.id.rightFileLl);
					holder.rightFileTypeIv = (ImageView) convertView
							.findViewById(R.id.rightFileTypeIv);
					holder.rightFileNameTv = (TextView) convertView
							.findViewById(R.id.rightFileNameTv);
					holder.rightFileSizeTv = (TextView) convertView
							.findViewById(R.id.rightFileSizeTv);
					holder.rightFileStatusTv = (TextView) convertView
							.findViewById(R.id.rightFileStatusTv);
					holder.rightFileProgressPb = (ProgressBar) convertView
							.findViewById(R.id.rightFileProgressPb);

					holder.rightCnsRl = (RelativeLayout) convertView
							.findViewById(R.id.rightCnsRl);
					holder.rightCnsIcon = (ImageView) convertView
							.findViewById(R.id.rightCnsIcon);
					holder.rightCnsTitle = (TextView) convertView
							.findViewById(R.id.rightCnsTitle);
					holder.rightCnsName1 = (TextView) convertView
							.findViewById(R.id.rightCnsName1);
					holder.rightCnsName2 = (TextView) convertView
							.findViewById(R.id.rightCnsName2);
					holder.rightCnsName3 = (TextView) convertView
							.findViewById(R.id.rightCnsName3);

					holder.leftCnsRl = (RelativeLayout) convertView
							.findViewById(R.id.leftCnsRl);
					holder.leftCnsIcon = (ImageView) convertView
							.findViewById(R.id.leftCnsIcon);
					holder.leftCnsTitle = (TextView) convertView
							.findViewById(R.id.leftCnsTitle);
					holder.leftCnsName1 = (TextView) convertView
							.findViewById(R.id.leftCnsName1);
					holder.leftCnsName2 = (TextView) convertView
							.findViewById(R.id.leftCnsName2);
					holder.leftCnsName3 = (TextView) convertView
							.findViewById(R.id.leftCnsName3);

					holder.chat_blank_ll = (LinearLayout) convertView
							.findViewById(R.id.chat_blank_ll);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				if (position == (messageList.size() - 1)) {
					holder.chat_blank_ll.setVisibility(View.VISIBLE);
				} else {
					holder.chat_blank_ll.setVisibility(View.GONE);

				}
				if (item.isHide()) {
					// 如果该记录隐藏,则隐藏记录
					convertView.setVisibility(View.GONE);
					holder.viewBG.setVisibility(View.GONE);
					holder.leftLayout.setVisibility(View.GONE);
					holder.rightLayout.setVisibility(View.GONE);
					holder.systemMessage.setVisibility(View.GONE);
					holder.letterDate.setVisibility(View.GONE);
					return convertView;
				} else {
					// 如果没隐藏，则显示
					holder.viewBG.setVisibility(View.VISIBLE);
					convertView.setVisibility(View.VISIBLE);
				}

				// 长按事件（根据类别区分处理）
				OnLongClickListener longClickListener = new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {

						// 左侧头像
						if (isAvatarLongClickable == true) {
							if (v == holder.leftHead) {
								String leftName = holder.leftName.getText()
										.toString();
								if (!textEt
										.getText()
										.toString()
										.contains(
												LEFTSPECCHAR + "@" + leftName
														+ RIGHTSPECCHAR)) {
									textEt.setText(textEt.getText()
											+ LEFTSPECCHAR + "@" + leftName
											+ RIGHTSPECCHAR + " ");

									textEt.setSelection(textEt.getText()
											.length());

									String senderId = (String) holder.leftHead
											.getTag();
									ConnectionsMini mini = new ConnectionsMini();
									mini.setId(senderId);
									mini.setName(leftName);
									atConnectionsMinis.add(mini);
								}
								return true;
							}
						}
						showItemLongClickDialog(item, f_position);
						return true;
					}
				};

				// 点击事件
				final OnClickListener clickListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						int type = getSenderTypeByMessage(item);
						switch (v.getId()) {
						case R.id.head: // 左侧头像
							if ((type + 1) == ConnectionsMini.UT_ORG) {
								ENavigate.startOrgMyHomePageActivityByUseId(
										ChatBaseActivity.this,
										Long.parseLong(item.getSenderID()));
							} else {
								/*ENavigate.startRelationHomeActivity(
										ChatBaseActivity.this,
										item.getSenderID());*/
								ENavigate.startRelationHomeActivity(
										ChatBaseActivity.this,
										item.getSenderID(),true,1);
							}
							break;
						case R.id.right_head: // 右侧头像
							if (App.getUser().getmUserType() == ConnectionsMini.UT_ORG) {
								ENavigate.startOrgMyHomePageActivityByUseId(
										ChatBaseActivity.this,
										Long.parseLong(App.getUserID()));
							} else {
								ENavigate.startRelationHomeActivity(
										ChatBaseActivity.this, App.getUserID());
							}
							break;
						case R.id.leftShareLl: // 分享
						case R.id.rightShareLl:
						try {
							if (item.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 需求
								// ENavigate.startRequirementDetailActivity(ChatBaseActivity.this,
								// Integer.parseInt(item.getJtFile().mTaskId));
								ENavigate.startNeedDetailsActivity(
										ChatBaseActivity.this,
										item.getJtFile().mTaskId, 2);
							} else if (item.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 知识e
								//修改bug ANDROID-5319
								if(!TextUtils.isEmpty(item.getJtFile().mTaskId) && !TextUtils.isEmpty(item.getJtFile().getReserved1())){
									ENavigate.startKnowledgeOfDetailActivity(ChatBaseActivity.this, Long.parseLong(item.getJtFile().mTaskId),
											Integer.parseInt(item.getJtFile().getReserved1()));
								}else{
									//第三方分享到社交的知识
									ENavigate.startShareDetailActivity(ChatBaseActivity.this, item.getJtFile().toKnowledgeMini());
								}
								
							} else if (item.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 新知识
								ENavigate.startKnowledgeOfDetailActivity(ChatBaseActivity.this, Long.parseLong(item.getJtFile().mTaskId),
										Integer.parseInt(item.getJtFile().getReserved1()));
							} else if (item.getType() == IMBaseMessage.TYPE_CONFERENCE) { // 会议
								ENavigate.startSquareActivity(mContext, Long.parseLong(item.getJtFile().mTaskId),0);
							}else if (item.getType() == IMBaseMessage.TYPE_COMMUNITY) { // 社群
								ENavigate.startCommunityDetailsActivity(mContext, Long.valueOf(item.getJtFile().mTaskId), false);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
							break;
						case R.id.leftFileLl: { // 左侧文件
							Intent intent = new Intent(ChatBaseActivity.this,
									FilePreviewActivity.class);
							intent.putExtra(EConsts.Key.JT_FILE,
									item.getJtFile());
							mContext.startActivity(intent);
						}
							break;
						case R.id.rightFileLl: // 右侧文件
							if (item.getJtFile() != null
									&& !TextUtils
											.isEmpty(item.getJtFile().mUrl)) {
								Intent intent = new Intent(
										ChatBaseActivity.this,
										FilePreviewActivity.class);
								intent.putExtra(EConsts.Key.JT_FILE,
										item.getJtFile());
								mContext.startActivity(intent);
							}
							break;
						case R.id.leftImageRl: // 左侧图片
						case R.id.rightImageRl: // 右侧图片
//							ENavigate.startImageBrowserActivity(
//									ChatBaseActivity.this, getChatId(),
//									item.getMessageID());
							ENavigate.startImageBrowserActivity(ChatBaseActivity.this, getChatId(),item.getMessageID(), getMessageHasImg(listMessage));
							break;
						case R.id.leftVoiceLl: { // 左侧语音
							// 语音下载中
							/*
							 * if(holder.leftVoiceLoadingPb.getVisibility() ==
							 * View.VISIBLE){ return; }
							 */
							// 当前音频的url
							curVoiceUrl = item.getJtFile().mUrl;
							try {
								File file = new File(getVoiceFileDir(),
										item.getJtFile().mFileName);
								if (file.exists()
										&& file.length() == item.getJtFile().mFileSize) {
									// 判断语音时长
									if (StringUtils
											.isEmpty(item.getJtFile().reserved2)) {
										showToast("无法播放语音文件，文件可能已损坏");
										return;
									}
									// 播放音频
									startPlaying(
											file.getAbsolutePath(),
											holder.leftVoiceIv,
											Integer.parseInt(item.getJtFile().reserved2) * 1000);
								} else { // 是否已开始下载任务
									new AsyncTask<Void, Void, Long>() {

										@Override
										protected Long doInBackground(
												Void... params) {
											// 更新数据库状态
											long taskId = voiceFileManager
													.query(item.getJtFile().mUrl);
											return taskId;
										}

										@Override
										protected void onPostExecute(Long taskId) {
											super.onPostExecute(taskId);
											try {
												if (taskId < 0) { // 从未开始任务
													File dir = getVoiceFileDir();
													if (dir != null) {
														Request request = new Request(
																Uri.parse(item
																		.getJtFile().mUrl));
														request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
														request.setDestinationUri(Uri.fromFile(new File(
																dir,
																item.getJtFile().mFileName))); // 设置文件下载位置
														voiceFileManager
																.insert(item
																		.getJtFile().mUrl,
																		downloadManager
																				.enqueue(request));
														// 界面修改
														holder.leftVoiceLoadingPb
																.setVisibility(View.VISIBLE);
														holder.leftVoiceIv
																.setVisibility(View.GONE);
													} else {
														showToast("没有SD卡，无法下载语音文件");
													}
												} else { // 任务已经开始
													Query query = new Query()
															.setFilterById(taskId);
													Cursor cursor = null;
													try {
														cursor = downloadManager
																.query(query);
														int status = cursor
																.getInt(cursor
																		.getColumnIndex(DownloadManager.COLUMN_STATUS));
														switch (status) {
														case DownloadManager.STATUS_PENDING:
														case DownloadManager.STATUS_RUNNING:
															holder.leftVoiceLoadingPb
																	.setVisibility(View.VISIBLE);
															holder.leftVoiceIv
																	.setVisibility(View.GONE);
															break;
														default:
															// 删除旧任务
															downloadManager
																	.remove(taskId);
															voiceFileManager
																	.delete(taskId);
															try {
															// 开始新任务
															File dir = getVoiceFileDir();
															if (dir != null) {
																Request request = new Request(
																		Uri.parse(item
																				.getJtFile().mUrl));
																request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
																request.setDestinationUri(Uri
																		.fromFile(new File(
																				dir,
																				item.getJtFile().mFileName))); // 设置文件下载位置

																voiceFileManager
																		.insert(item
																				.getJtFile().mUrl,
																				downloadManager
																						.enqueue(request));
																// 界面修改
																holder.leftVoiceLoadingPb
																		.setVisibility(View.VISIBLE);
																holder.leftVoiceIv
																		.setVisibility(View.GONE);
															} else {
																showToast("没有SD卡，无法下载语音文件");
															}
															} catch (Exception ex) {
																ex.printStackTrace();
															}
															break;
														}
													} catch (Exception e) {
														if (cursor != null) {
															cursor.close();
														}
														holder.leftVoiceLoadingPb
																.setVisibility(View.GONE);
														holder.leftVoiceIv
																.setVisibility(View.VISIBLE);
													}
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}.execute();
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
							break;
						case R.id.rightVoiceLl: { // 右侧语音

							// 语音下载中
							/*
							 * if(holder.rightVoiceLoadingPb.getVisibility() ==
							 * View.VISIBLE){ return; }
							 */
							// 正在播放的音频的url
							curVoiceUrl = item.getJtFile().mUrl;
							try {
							File file = new File(getVoiceFileDir(),
									item.getJtFile().mFileName);
							if (file.exists()
									&& file.length() == item.getJtFile().mFileSize) {
								if (StringUtils
										.isEmpty(item.getJtFile().reserved2)) {
									showToast("无法播放语音文件，文件可能已损坏");
									return;
								}
								// 开始播放音频和动画
								startPlaying(
										file.getAbsolutePath(),
										holder.rightVoiceIv,
										Integer.parseInt(item.getJtFile().reserved2) * 1000);
							} else if (item.getJtFile().mUrl != null
									&& item.getJtFile().mUrl.length() > 0) { // 文件已上传
								new AsyncTask<Void, Void, Long>() {

									@Override
									protected Long doInBackground(
											Void... params) {
										long taskId = voiceFileManager
												.query(item.getJtFile().mUrl);
										return taskId;
									}

									@Override
									protected void onPostExecute(Long taskId) {
										super.onPostExecute(taskId);
										if (taskId < 0) { // 从未开始任务
											File dir = getVoiceFileDir();
											if (dir != null) {
												Request request = new Request(
														Uri.parse(item
																.getJtFile().mUrl));
												request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
												request.setDestinationUri(Uri.fromFile(new File(
														dir,
														item.getJtFile().mFileName))); // 设置文件下载位置
												voiceFileManager
														.insert(item
																.getJtFile().mUrl,
																downloadManager
																		.enqueue(request));
												// 界面修改
												holder.rightVoiceLoadingPb
														.setVisibility(View.VISIBLE);
												holder.rightVoiceIv
														.setVisibility(View.GONE);
											} else {
												showToast("没有SD卡，无法下载语音文件");
											}
										} else {
											Query query = new Query()
													.setFilterById(taskId);
											Cursor cursor = null;
											try {
												cursor = downloadManager
														.query(query);
												if (cursor != null) {
													int status = cursor
															.getInt(cursor
																	.getColumnIndex(DownloadManager.COLUMN_STATUS));
													switch (status) {
													case DownloadManager.STATUS_PENDING:
													case DownloadManager.STATUS_RUNNING:
														holder.rightVoiceLoadingPb
																.setVisibility(View.VISIBLE);
														holder.rightVoiceIv
																.setVisibility(View.GONE);
														break;
													default:
														// 删除旧任务
														downloadManager
																.remove(taskId);
														voiceFileManager
																.delete(taskId);
														// 开始新任务
														File dir = getVoiceFileDir();
														if (dir != null) {
															Request request = new Request(
																	Uri.parse(item
																			.getJtFile().mUrl));
															request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
															request.setDestinationUri(Uri
																	.fromFile(new File(
																			dir,
																			item.getJtFile().mFileName))); // 设置文件下载位置
															voiceFileManager
																	.insert(item
																			.getJtFile().mUrl,
																			downloadManager
																					.enqueue(request));
															// 界面修改
															holder.rightVoiceLoadingPb
																	.setVisibility(View.VISIBLE);
															holder.rightVoiceIv
																	.setVisibility(View.GONE);
														} else {
															showToast("没有SD卡，无法下载语音文件");
														}
														break;
													}
													if (cursor!=null) {
														cursor.close();
													}
													
												}
											} catch (Exception e) {
												if (cursor != null) {
													cursor.close();
												}
												holder.rightVoiceLoadingPb
														.setVisibility(View.GONE);
												holder.rightVoiceIv
														.setVisibility(View.VISIBLE);
											}
										}
									}
								}.execute();

							} else {
								file.delete(); // 删除不完整文件
								showToast("语音文件不存在");
							}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
							break;
						case R.id.leftPlayIv: // 左侧视频
							try {
							File file = new File(
									EUtil.getAppCacheFileDir(mContext),
									item.getJtFile().mFileName);
							if (file.exists()
									&& file.length() == item.getJtFile().mFileSize) { // 文件已经下载到本地
								OpenFiles
										.open(mContext, file.getAbsolutePath());
							} else {
								Intent intent = new Intent(
										ChatBaseActivity.this,
										FilePreviewActivity.class);
								intent.putExtra(EConsts.Key.JT_FILE,
										item.getJtFile());
								startActivity(intent);
							}
							
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							break;
						case R.id.rightPlayIv: // 右侧视频
							break;
						case R.id.leftCnsRl: // 左侧关系
						case R.id.rightCnsRl: // 右侧关系
							int mtype = item.getJtFile().getmType()!=0?item.getJtFile().getmType():item.getType();
							if (!StringUtils.isEmpty(item.getJtFile().mTaskId)) {
								if (item.getJtFile() != null) {
									if (JTFile.TYPE_JTCONTACT_ONLINE == mtype) { // 线上用户
										/*ENavigate.startRelationHomeActivity(
												ChatBaseActivity.this,
												item.getJtFile().mTaskId);*/
										ENavigate.startRelationHomeActivity(
												ChatBaseActivity.this,
												item.getJtFile().mTaskId,true,1);
									}
									else if (JTFile.TYPE_JTCONTACT_OFFLINE == mtype) {//人脉
										if(v.getId() == R.id.leftCnsRl ){//人脉详情
											ENavigate.startRelationHomeActivity(
													ChatBaseActivity.this,
													item.getJtFile().mTaskId,false,2,false);	
										}else {//我的人脉
											ENavigate.startRelationHomeActivity(
													ChatBaseActivity.this,
													item.getJtFile().mTaskId,false,2);	
										}
									}
									// 组织
									else if (JTFile.TYPE_ORG_ONLINE == mtype) {
										ENavigate
										.startOrgMyHomePageActivityByUseId(
												ChatBaseActivity.this,
												Long.parseLong(item
														.getJtFile().mTaskId)
												);	
									} else if (JTFile.TYPE_ORG_OFFLINE == mtype) {
										ENavigate
												.startOrgMyHomePageActivity(
														ChatBaseActivity.this,
														Long.parseLong(item
																.getJtFile().mTaskId),
														Long.parseLong(item
																.getJtFile().reserved2),
														true,
														ENavConsts.type_details_org);
									} else if (JTFile.TYPE_JTCONTACT_OFFLINE == mtype) {
										ENavigate
												.startContactsDetailsActivity(
														ChatBaseActivity.this,2,
														Long.parseLong(item
																.getJtFile().mTaskId),1456, 0
														);
									} else if (JTFile.TYPE_ORGANIZATION == mtype) {
										ENavigate
												.startOrgMyHomePageActivityByUseId(
														ChatBaseActivity.this,
														Long.parseLong(item
																.getJtFile().mTaskId)
														);	
									} else if (JTFile.TYPE_CLIENT == mtype) {
										ENavigate
												.startClientDedailsActivity(
														ChatBaseActivity.this,
														Long.parseLong(item
																.getJtFile().mTaskId),
														1, 6);
									}else if(IMBaseMessage.TYPE_JTCONTACT_OFFLINE ==item.getType() ){
										ENavigate
										.startContactsDetailsActivity(
												ChatBaseActivity.this,2,
												Long.parseLong(item
														.getJtFile().mTaskId),0,0
												);
									}
								}
							}
							break;
						}
					}
				};

				// 点击头像
				holder.leftHead.setOnClickListener(clickListener);
				holder.rightHead.setOnClickListener(clickListener);
				holder.leftHead.setTag(item.getSenderID());

				// 长按头像
				holder.leftHead.setOnLongClickListener(longClickListener);
				holder.rightHead.setOnLongClickListener(longClickListener);

				// 点击知识消息
				holder.leftShareLl.setOnClickListener(clickListener);
				holder.rightShareLl.setOnClickListener(clickListener);

				// 点击文件消息
				holder.leftFileLl.setOnClickListener(clickListener);
				holder.rightFileLl.setOnClickListener(clickListener);

				if (item != null && item.getContent() != null) {
					final String body = item.getContent().trim();
					if (KeelLog.DEBUG) {
						KeelLog.e("发布内容 " + body);
					}
					if (item.getSenderType() == IMBaseMessage.MSG_SYS_SEND) {// 发送系统消息
						holder.leftLayout.setVisibility(View.GONE);
						holder.rightLayout.setVisibility(View.GONE);
						holder.systemMessage.setVisibility(View.VISIBLE);
						holder.systemMessage.setText(body);
					} else if (item.getSenderType() == IMBaseMessage.MSG_MY_SEND) { // 发送显示右边

						// 自己发送的消息
						holder.leftLayout.setVisibility(View.GONE);
						holder.systemMessage.setVisibility(View.GONE);
						holder.rightLayout.setVisibility(View.VISIBLE);

						if(TextUtils.isEmpty(getImageByMessage(item))){
							if(getSenderTypeByMessage(item)+1 == ConnectionsMini.UT_ORG){
								holder.rightHead.setImageResource(R.drawable.default_portrait116);
							}else{
								holder.rightHead.setImageResource(R.drawable.ic_default_avatar);
							}
						}else{
							if(getSenderTypeByMessage(item)+1 == ConnectionsMini.UT_ORG){
								com.utils.common.Util.initAvatarImage(mContext, holder.rightHead, item.getSenderName(), getImageByMessage(item), 1, 2);
							}else{
								com.utils.common.Util.initAvatarImage(mContext, holder.rightHead, item.getSenderName(), getImageByMessage(item), 1, 1);
							}
						}
						holder.rightName.setText(item.getSenderName());

						if (IMBaseMessage.TYPE_IMAGE == item.getType()) { // 右侧图片

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
								if(!TextUtils.isEmpty(item.getJtFile().mUrl)){ // 图片已上传
									ImageLoader.load(holder.rightImageIv, 130,item.getJtFile().mUrl, R.drawable.hy_chat_right_pic);
									}
								else{ // 图片不存在
									holder.rightImageIv.setImageResource(R.drawable.hy_chat_right_pic);
								}
							}else{
								holder.rightImageIv.setImageResource(R.drawable.hy_chat_right_pic);
							}
							// 长按事件
							holder.rightImageRl
									.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightImageRl
									.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_AUDIO == item.getType()) { // 右侧语音
							try {
							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.sendMsgProgress.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {

								// 显示时长
								if (TextUtils
										.isEmpty(item.getJtFile().reserved2)) {
									holder.rightVoiceDurationTv.setText("未知\"");
								} else {
									holder.rightVoiceDurationTv.setText(item
											.getJtFile().reserved2 + "\"");
								}
								// 控件长度
								// int paramsWidth = 50 +
								// (StringUtils.isEmpty(item.getJtFile().reserved2)
								// ? 0 :
								// Integer.parseInt(item.getJtFile().reserved2)
								// * 5);
								// 最大不超过200dp
								// paramsWidth = Math.min(paramsWidth, 200);
								// 设置长度
								// LinearLayout.LayoutParams layoutParams =
								// (LayoutParams)
								// holder.rightVoiceDurationLl.getLayoutParams();
								// layoutParams.width =
								// EUtil.convertDpToPx(paramsWidth);
								// holder.rightVoiceDurationLl.setLayoutParams(layoutParams);
								// 语音文件
								final File file = new File(getVoiceFileDir(),
										item.getJtFile().mFileName);
								// 设置状态
								if (file.exists()
										&& file.length() == item.getJtFile().mFileSize) {
									holder.rightVoiceLoadingPb
											.setVisibility(View.GONE);
									holder.rightVoiceIv
											.setVisibility(View.VISIBLE);
								} else if (!StringUtils.isEmpty(item
										.getJtFile().mUrl)) {
									new AsyncTask<Void, Void, Long>() {

										@Override
										protected Long doInBackground(
												Void... params) {
											long taskId = voiceFileManager
													.query(item.getJtFile().mUrl);

											Query query = new Query()
													.setFilterById(taskId);
											Cursor cursor = null;
											try {
												cursor = downloadManager
														.query(query);
												if (cursor != null) {
													int status = cursor
															.getInt(cursor
																	.getColumnIndex(DownloadManager.COLUMN_STATUS));
													/*switch (status) {
													case DownloadManager.STATUS_PENDING:
													case DownloadManager.STATUS_RUNNING:
														holder.rightVoiceLoadingPb
																.setVisibility(View.VISIBLE);
														holder.rightVoiceIv
																.setVisibility(View.GONE);
														break;
													default:
														holder.rightVoiceLoadingPb
																.setVisibility(View.GONE);
														holder.rightVoiceIv
																.setVisibility(View.VISIBLE);
														break;
													}*/
												}
											} catch (Exception e) {
												if (cursor != null) {
													cursor.close();
												}
												return null;
											}
											return taskId;

										}

										@Override
										protected void onPostExecute(Long result) {
											super.onPostExecute(result);
											try {
												if (result >= 0) {
													holder.rightVoiceLoadingPb
															.setVisibility(View.GONE);
													holder.rightVoiceIv
															.setVisibility(View.VISIBLE);
												} else {
													holder.rightVoiceLoadingPb
															.setVisibility(View.GONE);
													holder.rightVoiceIv
															.setVisibility(View.VISIBLE);
												}
											} catch (Exception ex) {
												ex.printStackTrace();
											}

										}
									}.execute();

								} else {
									holder.rightVoiceLoadingPb
											.setVisibility(View.GONE);
									holder.rightVoiceIv
											.setVisibility(View.VISIBLE);
								}
								// 点击事件
								holder.rightVoiceLl
										.setOnClickListener(clickListener);
								// 长按事件
								holder.rightVoiceLl
										.setOnLongClickListener(longClickListener);
							}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else if (IMBaseMessage.TYPE_KNOWLEDGE == item
								.getType()) { // 右侧知识

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {

								holder.rightShareTopLl
										.setVisibility(View.VISIBLE);
								holder.rightShareBottomLl
										.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
								holder.rightShareMsgTv.setText("分享了[知识]");
								// 截图
								holder.rightShareImageIv
										.setVisibility(View.GONE);
								// 类型
								holder.rightShareTypeIv
										.setImageResource(R.drawable.hy_chat_type_knowledge);
								// 标题
								holder.rightShareTitleTv
										.setVisibility(View.GONE);
								// 内容
								int httpIndex = item.getJtFile().mSuffixName
										.indexOf("http");
								if (!StringUtils
										.isEmpty(item.getJtFile().mSuffixName)) {
									if (httpIndex < 0) {
										holder.rightShareContentTv.setText(!TextUtils.isEmpty(item
												.getJtFile().mFileName)?item
														.getJtFile().mFileName:item
														.getJtFile().mSuffixName);
									} else if (httpIndex == 0) {
										holder.rightShareContentTv.setText("");
									} else {
										holder.rightShareContentTv.setText(!TextUtils.isEmpty(item
												.getJtFile().mFileName)?item
														.getJtFile().mFileName:item
														.getJtFile().mSuffixName);
									}
									holder.rightShareContentTv
											.setVisibility(View.VISIBLE);
								} else {
									holder.rightShareContentTv
											.setVisibility(View.GONE);
								}
								// 链接
								if (!StringUtils.isEmpty(item.getJtFile().mUrl)) {
									if (item.getJtFile().mUrl
											.startsWith("http://mp.weixin.qq.com/")) { // 微信分享
										// holder.rightShareLinkIv.setImageResource(R.drawable.chat_link_weixin);
										holder.rightShareLinkIv.setText("来自微信");
									} else {
										// holder.rightShareLinkIv.setImageResource(R.drawable.chat_link_normal);
										holder.rightShareLinkIv.setText("来自网页");
									}
									holder.rightShareLinkIv
											.setVisibility(View.VISIBLE);
								} else {
									holder.rightShareLinkIv
											.setVisibility(View.GONE);
								}
							}
							// 长按
							holder.rightShareLl
									.setOnLongClickListener(longClickListener);
						} else if (IMBaseMessage.TYPE_KNOWLEDGE2 == item
								.getType()) { // 右侧新知识

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
								holder.rightShareMsgTv.setText("分享了[知识]");
								holder.rightShareTopLl
										.setVisibility(View.VISIBLE);
								holder.rightShareBottomLl
										.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
								// 图片
								holder.rightShareImageIv.setVisibility(View.VISIBLE);
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									ImageLoader.load(holder.rightShareImageIv, item.getJtFile().mUrl, R.drawable.hy_chat_share_img);
								} else {
									 holder.rightShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
								}
								// 类型
								holder.rightShareTypeIv
										.setImageResource(R.drawable.hy_chat_type_knowledge);
								// 标题
								holder.rightShareTitleTv.setText("\u3000\u3000"
										+ item.getJtFile().reserved2);
								holder.rightShareTitleTv
										.setVisibility(View.VISIBLE);
								// 内容
								if (!StringUtils
										.isEmpty(item.getJtFile().mSuffixName)||!StringUtils
										.isEmpty(item.getJtFile().mFileName)) {
									holder.rightShareContentTv.setText(!TextUtils.isEmpty(item
											.getJtFile().mFileName)?item
													.getJtFile().mFileName:item
													.getJtFile().mSuffixName);
									holder.rightShareContentTv
											.setVisibility(View.VISIBLE);
								} else {
									holder.rightShareContentTv
											.setVisibility(View.GONE);

								}
								// 链接(判断来源)
								if (TextUtils
										.isEmpty(item.getJtFile().reserved3)) {
									holder.rightShareLinkIv
											.setVisibility(View.GONE);
								} else {
									if (item.getJtFile().reserved3
											.startsWith("http://mp.weixin.qq.com/")) {
										// holder.rightShareLinkIv.setImageResource(R.drawable.chat_link_weixin);
										holder.rightShareLinkIv.setText("来自微信");
									} else {
										// holder.rightShareLinkIv.setImageResource(R.drawable.chat_link_normal);
										holder.rightShareLinkIv.setText("来自网页");
									}
									holder.rightShareLinkIv
											.setVisibility(View.VISIBLE);
								}
							}
							// 长按
							holder.rightShareLl
									.setOnLongClickListener(longClickListener);
						} else if (IMBaseMessage.TYPE_COMMUNITY == item
								.getType()) { // 右侧社群

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
								holder.rightShareMsgTv.setText("分享了[社群]");
								holder.rightShareTopLl
										.setVisibility(View.VISIBLE);
								holder.rightShareBottomLl
										.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
								// 图片
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									ImageLoader.load(holder.rightShareImageIv, item.getJtFile().mUrl, R.drawable.hy_chat_share_img);
								} else {
									 holder.rightShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
								}
//								// 类型
								holder.rightShareTypeIv.setVisibility(View.GONE);
								// 标题
								if (!StringUtils.isEmpty(item.getJtFile().mFileName)) {
									holder.rightShareTitleTv.setText(item.getJtFile().mFileName);
									holder.rightShareTitleTv.setVisibility(View.VISIBLE);
								}else{
									holder.rightShareTitleTv.setVisibility(View.GONE);
								}
								// 内容
								if (!StringUtils.isEmpty(item.getJtFile().mSuffixName)) {
									holder.rightShareContentTv.setText(item.getJtFile().mSuffixName);
									holder.rightShareContentTv.setVisibility(View.VISIBLE);
								} else {
									holder.rightShareContentTv
											.setVisibility(View.GONE);

								}
								holder.rightShareLinkIv.setVisibility(View.GONE);
							}
							// 长按
							holder.rightShareLl
									.setOnLongClickListener(longClickListener);
						} else if (IMBaseMessage.TYPE_CONFERENCE == item
								.getType()) { // 右侧会议

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
								holder.rightShareMsgTv.setText("分享了[会议]");
								holder.rightShareTopLl
										.setVisibility(View.VISIBLE);
								holder.rightShareBottomLl
										.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
								// 图片
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									ImageLoader.load(holder.rightShareImageIv, item.getJtFile().mUrl, R.drawable.hy_chat_share_img);
								}else{
									holder.rightShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
								}
								// 类型
								holder.rightShareTypeIv
										.setImageResource(R.drawable.hy_chat_type_conference);
								// 标题
								holder.rightShareTitleTv.setText("\u3000\u3000"
										+ item.getJtFile().mFileName);
								holder.rightShareTitleTv
										.setVisibility(View.VISIBLE);
								// 内容
								if (!StringUtils
										.isEmpty(item.getJtFile().reserved1)) {
									holder.rightShareContentTv.setText(item
											.getJtFile().reserved1);
									holder.rightShareContentTv
											.setVisibility(View.VISIBLE);
								} else {

									holder.rightShareContentTv
											.setVisibility(View.GONE);
								}
								// 链接
								holder.rightShareLinkIv
										.setVisibility(View.GONE);
							}
							// 长按
							holder.rightShareLl
									.setOnLongClickListener(longClickListener);
							// 点击
							holder.rightShareLl
									.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item
								.getType()
								|| // 右侧关系
								IMBaseMessage.TYPE_JTCONTACT_ONLINE == item
										.getType()
								|| IMBaseMessage.TYPE_ORG_OFFLINE == item
										.getType()
								|| IMBaseMessage.TYPE_ORG_ONLINE == item
										.getType()
								|| IMBaseMessage.TYPE_ORGANIZATION == item
										.getType()
								|| IMBaseMessage.TYPE_CUSTOMER == item
										.getType()) {

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.GONE);
							holder.rightCnsName1.setVisibility(View.GONE);
							holder.rightCnsName2.setVisibility(View.GONE);
							holder.rightCnsName3.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.VISIBLE);
							holder.rightCnsRl.setTag(item.getJtFile());

							if (item.getJtFile() != null) {
								final JTFile jtFile = item.getJtFile();
								// 用户
								if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item
										.getType()
										|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item
												.getType()) {
									if (!StringUtils.isEmpty(jtFile.mFileName)) {
										// holder.rightCnsTitle.setText(jtFile.mFileName);
										holder.rightCnsName1
												.setText(jtFile.mFileName);
										holder.rightCnsName1
												.setVisibility(View.VISIBLE);
									}
								}
								// 组织
								else {
									holder.rightCnsName1
											.setText(!TextUtils.isEmpty(item
													.getJtFile().mFileName)?item
															.getJtFile().mFileName:item
															.getJtFile().mSuffixName);
									holder.rightCnsName1
											.setVisibility(View.VISIBLE);
								}
								// 用户
								if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item
										.getType()
										|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item
												.getType()) {
									if (!StringUtils
											.isEmpty(jtFile.mSuffixName)) {
										holder.rightCnsName2
												.setText(jtFile.mSuffixName);
										holder.rightCnsName2
												.setVisibility(View.VISIBLE);
									}
									if (!StringUtils.isEmpty(jtFile.reserved1)) {
										holder.rightCnsName3
												.setText(jtFile.reserved1);
										holder.rightCnsName3
												.setVisibility(View.VISIBLE);
									}
								}
								// 组织
								else {
									if (!StringUtils.isEmpty(jtFile.reserved1)) {
										holder.rightCnsName2
												.setText(jtFile.reserved1);
										holder.rightCnsName2
												.setVisibility(View.VISIBLE);
									}
								}
								// if(!StringUtils.isEmpty(jtFile.mUrl)){
								if (jtFile.mUrl == null) {
									jtFile.mUrl = "";
								}

								if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item
										.getType()
										|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item
												.getType()) {
									if(!TextUtils.isEmpty(jtFile.mUrl)){
										ImageLoader.load(holder.rightCnsIcon, jtFile.mUrl, R.drawable.ic_default_avatar);
									}else{
										holder.rightCnsIcon.setImageResource(R.drawable.ic_default_avatar);
									}
								} else {
									if(!TextUtils.isEmpty(jtFile.mUrl)){
										ImageLoader.load(holder.rightCnsIcon, jtFile.mUrl, R.drawable.companyfriend);
									}else{
										holder.rightCnsIcon.setImageResource(R.drawable.companyfriend);
									}
								}

							}

							// 点击事件
							holder.rightCnsRl.setOnClickListener(clickListener);
							// 长按事件
							holder.rightCnsRl
									.setOnLongClickListener(longClickListener);
						} else if (IMBaseMessage.TYPE_REQUIREMENT == item
								.getType()) { // 右侧需求

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.VISIBLE);

							// 初始化数据
							if (item.getJtFile() != null) {

								holder.rightShareTopLl
										.setVisibility(View.VISIBLE);
								holder.rightShareBottomLl
										.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
								holder.rightShareMsgTv.setText("分享了[需求]");
								// 图片
								holder.rightShareImageIv
										.setVisibility(View.GONE);
								// 类型
								holder.rightShareTypeIv
										.setImageResource(R.drawable.hy_chat_type_requirement);
								// 标题
								holder.rightShareTitleTv.setText("\u3000\u3000"
										+ item.getJtFile().mFileName);
								holder.rightShareTitleTv
										.setVisibility(View.VISIBLE);
								// 内容
								if (!StringUtils
										.isEmpty(item.getJtFile().reserved1)) {
									holder.rightShareContentTv.setText(item
											.getJtFile().reserved1);
									holder.rightShareContentTv
											.setVisibility(View.VISIBLE);
								} else {
									holder.rightShareContentTv
											.setVisibility(View.GONE);

								}
								// 链接
								holder.rightShareLinkIv
										.setVisibility(View.GONE);
							}
							// 长按
							holder.rightShareLl
									.setOnLongClickListener(longClickListener);
						} else if (IMBaseMessage.TYPE_VIDEO == item.getType()) { // 右侧视频
							try {

							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightVideoRl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) { // 视频已上传
									File localFile = null;
									boolean fileExist = false;
									// 本地文件是否存在
									if (!StringUtils.isEmpty(item.getJtFile().reserved1)) {
										localFile = new File(item.getJtFile().reserved1);
										fileExist = localFile.exists();
									}
									if(!fileExist){
										File localDir = EUtil.getAppCacheFileDir(mContext);
										if (localDir != null && !StringUtils.isEmpty(item.getJtFile().mFileName)) {
											localFile = new File(localDir,
											item.getJtFile().mFileName); // 本地缓存文件路径
											fileExist = localFile.exists();
										}
									}
									filePath = null;
									if (localFile!=null) {
										filePath= localFile != null ? 
												localFile.getAbsolutePath() : "";
									}
								
									if (fileExist
											&& localFile.length() == item
													.getJtFile().mFileSize) {
										BmpAsyncTask bAsyncTask = new BmpAsyncTask(holder.rightVideoIv);
										bAsyncTask.execute(filePath);
										holder.rightPlayIv
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) { // 打开文件
														OpenFiles.open(
																mContext,
																filePath);
													}
												});
									} else { // 本地文件不存在（显示截图）
										holder.rightVideoIv
												.setImageResource(R.drawable.hy_chat_right_pic);
										holder.rightPlayIv
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														Intent intent = new Intent(
																ChatBaseActivity.this,
																FilePreviewActivity.class);
														intent.putExtra(
																EConsts.Key.JT_FILE,
																item.getJtFile());
														startActivity(intent);
													}
												});
									}
								} else { // 视频文件未上传或上传中
									if (!StringUtils.isEmpty(item.getJtFile().mLocalFilePath)) {
										BmpAsyncTask bAsyncTask = new BmpAsyncTask(holder.rightVideoIv);
										bAsyncTask.execute(item.getJtFile().mLocalFilePath);
									} else {
										holder.rightVideoIv
												.setImageResource(R.drawable.hy_chat_right_pic);
									}
									holder.rightPlayIv
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) { // 打开文件
													if (!StringUtils.isEmpty(item
															.getJtFile().mLocalFilePath)) {
														OpenFiles.open(
																mContext,
																item.getJtFile().mLocalFilePath);
													} else {
														Toast.makeText(
																ChatBaseActivity.this,
																"视频源未找到", 0)
																.show();
													}
												}
											});
								}
							}
							// 长按事件
							holder.rightVideoIv
									.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightVideoIv
									.setOnClickListener(clickListener);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else if (IMBaseMessage.TYPE_FILE == item.getType()) { // 右侧文件

							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {

								// 根据不同的后缀名设置不同的文件图片
								setFileIconAccoredSuffixName(
										holder.rightFileTypeIv, item
												.getJtFile().getmSuffixName());

								holder.rightFileNameTv
										.setText(item.getJtFile().mFileName); // 文件名
								holder.rightFileSizeTv
										.setText(EUtil.formatFileSize(item
												.getJtFile().mFileSize)); // 文件大小
								if (item.getJtFile().mUrl == null
										|| StringUtils
												.isEmpty(item.getJtFile().mUrl)) { // 文件正在上传
									holder.rightFileStatusTv.setText("");
									for (FileUploader uploader : mListUploader) {
										if (uploader.getJTFile().mTaskId
												.equals(item.getJtFile().mTaskId)) {
											holder.rightFileProgressPb
													.setProgress(uploader
															.getProgress());
											break;
										}
									}
									holder.rightFileProgressPb
											.setVisibility(View.VISIBLE);
								} else { // 文件已上传
									boolean taskExist = false;
									if (mDownloadService != null) {
										for (FileDownloader downloader : mDownloadService
												.getListDownloader()) {
											if (downloader.getJTFile().mUrl
													.equals(item.getJtFile().mUrl)) {
												holder.rightFileStatusTv
														.setText("");
												holder.rightFileProgressPb
														.setProgress(downloader
																.getProgress());
												holder.rightFileProgressPb
														.setVisibility(View.VISIBLE);
												taskExist = true;
												break;
											}
										}
									}
									if (!taskExist) {
										File localFile = null;
										boolean fileExist = false;
										if (!StringUtils.isEmpty(item
												.getJtFile().reserved1)) {
											localFile = new File(
													item.getJtFile().reserved1);
											fileExist = localFile.exists();
										} else {
											File localDir = EUtil
													.getAppCacheFileDir(mContext);
											if (localDir != null
													&& !StringUtils
															.isEmpty(item
																	.getJtFile().mFileName)) {
												localFile = new File(
														localDir,
														item.getJtFile().mFileName);
												fileExist = localFile.exists();
											}
										}
										if (fileExist) {
											holder.rightFileStatusTv
													.setText("打开");
											holder.rightFileProgressPb
													.setVisibility(View.GONE);
										} else {
											holder.rightFileStatusTv
													.setText("未下载");
											holder.rightFileProgressPb
													.setVisibility(View.GONE);
										}
									}
								}
							}
							// 长按
							holder.rightFileLl
									.setOnLongClickListener(longClickListener);
						} else { // 右侧文本

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.VISIBLE);
							CharSequence dd = parser.addSmileySpans(body);
							// CharSequence dd1 = parser2.addSmileySpans(dd);
							holder.rightContent.setText(dd);
							if (item.getContent().startsWith("http://")
									|| item.getContent().startsWith("https://")) {
								Linkify.addLinks(holder.rightContent,
										Linkify.WEB_URLS);
							}
							// 长按
							holder.rightContent
									.setOnLongClickListener(longClickListener);
						}
						// 发送失败的红色按钮处理
						if (item.getSendType() == IMBaseMessage.SEND_TYPE_FAIL) {
							// 失败
							holder.sendMsgProgress.setVisibility(View.GONE);
							holder.sendMsgFail.setVisibility(View.VISIBLE);
							holder.sendMsgFail
									.setOnClickListener(new ImageView.OnClickListener() {
										@Override
										public void onClick(View v) {
											showSendMessageDialog(item);
										}
									});
							// 处理文件发送失败时的情况
							/*
							 * if(item.getType() == IMBaseMessage.TYPE_FILE){
							 * holder
							 * .rightFileProgressPb.setVisibility(View.GONE); }
							 */
						} else if (item.getSendType() == IMBaseMessage.SEND_TYPE_SENDING) {
							// 发送中
							holder.sendMsgFail.setVisibility(View.GONE);
							holder.sendMsgProgress.setVisibility(View.VISIBLE);
						} else {
							// 成功
							holder.sendMsgFail.setVisibility(View.GONE);
							holder.sendMsgProgress.setVisibility(View.GONE);
						}
					} else {

						// 对方发送的内容
						holder.leftLayout.setVisibility(View.VISIBLE);
						holder.rightLayout.setVisibility(View.GONE);
						holder.systemMessage.setVisibility(View.GONE);
						if(TextUtils.isEmpty(getImageByMessage(item))){
							if(getSenderTypeByMessage(item)+1 == ConnectionsMini.UT_ORG){
								holder.leftHead.setImageResource(R.drawable.default_portrait116);
							}else{
								holder.leftHead.setImageResource(R.drawable.ic_default_avatar);
							}
						}else{
							if(getSenderTypeByMessage(item)+1 == ConnectionsMini.UT_ORG){
								com.utils.common.Util.initAvatarImage(mContext, holder.leftHead, item.getSenderName(), getImageByMessage(item), 1, 2);
							}else{
								com.utils.common.Util.initAvatarImage(mContext, holder.leftHead, item.getSenderName(), getImageByMessage(item), 1, 1);
							}
						}
						holder.leftName.setText(item.getSenderName());

						if (IMBaseMessage.TYPE_TEXT == item.getType()) { // 左侧文本

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.VISIBLE);
							holder.leftContent
									.setOnLongClickListener(longClickListener);
							CharSequence dd = parser.addSmileySpans(body);
							// CharSequence dd1 = parser2.addSmileySpans(dd);
							holder.leftContent.setText(dd);

							if (item.getContent().startsWith("http://")
									|| item.getContent().startsWith("https://")) {
								Linkify.addLinks(holder.leftContent,
										Linkify.WEB_URLS);
							}
						} else if (IMBaseMessage.TYPE_IMAGE == item.getType()) { // 左侧图片

							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftImageRl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									ImageLoader.load(holder.leftImageIv, 130, item.getJtFile().mUrl, R.drawable.hy_chat_right_pic);
								} else {
									holder.leftImageIv.setImageResource(R.drawable.hy_chat_right_pic);
								}
							} else {
								holder.leftImageIv.setImageResource(R.drawable.hy_chat_right_pic);
							}
							// 长按事件
							holder.leftImageRl
									.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftImageRl
									.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_KNOWLEDGE == item
								.getType()) { // 左侧知识

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);

							holder.leftShareMsgTv.setText("分享了[知识]");
							holder.leftShareTopLl
									.setVisibility(View.VISIBLE);
							holder.leftShareBottomLl
									.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
							// 图片
							holder.leftShareImageIv.setVisibility(View.GONE);
							// 类型
							holder.leftShareTypeIv
									.setImageResource(R.drawable.hy_chat_type_knowledge);
							// 标题
							holder.leftShareTitleTv.setVisibility(View.GONE);
							// 内容
							int httpIndex = item.getJtFile().mSuffixName
									.indexOf("http");
							if (!StringUtils
									.isEmpty(item.getJtFile().mSuffixName)||!StringUtils
									.isEmpty(item.getJtFile().mFileName)) {
								holder.leftShareContentTv
										.setVisibility(View.VISIBLE);
								if (httpIndex <= 0) {
									holder.leftShareContentTv.setText(!TextUtils.isEmpty(item
											.getJtFile().mFileName)?item
													.getJtFile().mFileName:item
													.getJtFile().mSuffixName);
								} else {
									holder.leftShareContentTv.setText(!TextUtils.isEmpty(item
											.getJtFile().mFileName)?item
													.getJtFile().mFileName:item
													.getJtFile().mSuffixName);
								}
							} else {
								holder.leftShareContentTv
										.setVisibility(View.GONE);
							}
							// 链接
							if (!StringUtils.isEmpty(item.getJtFile().mUrl)) {
								if (item.getJtFile().mUrl
										.startsWith("http://mp.weixin.qq.com/")) { // 微信分享
									// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_weixin);
									holder.leftShareLinkIv.setText("来自微信");
								} else {
									// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_normal);
									holder.leftShareLinkIv.setText("来自网页");
								}
								holder.leftShareLinkIv
										.setVisibility(View.VISIBLE);
							} else {
								holder.leftShareLinkIv.setVisibility(View.GONE);
							}
							// 长按
							holder.leftShareLl
									.setOnLongClickListener(longClickListener);
						} else if (IMBaseMessage.TYPE_KNOWLEDGE2 == item
								.getType()) { // 左侧新知识

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);

							// 知识
							holder.leftShareMsgTv.setText("分享了[知识]");
							holder.leftShareTopLl
									.setVisibility(View.VISIBLE);
							holder.leftShareBottomLl
									.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
							// 图片
							holder.leftShareImageIv.setVisibility(View.VISIBLE);
							if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
								ImageLoader.load(holder.leftShareImageIv,item.getJtFile().mUrl, R.drawable.hy_chat_share_img);
							} else {
								 holder.leftShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
							}
							// 类型
							holder.leftShareTypeIv
									.setImageResource(R.drawable.hy_chat_type_knowledge);
							// 标题
							holder.leftShareTitleTv.setText("\u3000\u3000"
									+ item.getJtFile().reserved2);
							holder.leftShareTitleTv.setVisibility(View.VISIBLE);
							// 内容
							if (!StringUtils
									.isEmpty(item.getJtFile().mSuffixName)||!StringUtils
									.isEmpty(item.getJtFile().mFileName)) {
								holder.leftShareContentTv.setText(!TextUtils.isEmpty(item
										.getJtFile().mFileName)?item
												.getJtFile().mFileName:item
												.getJtFile().mSuffixName);
								holder.leftShareContentTv
										.setVisibility(View.VISIBLE);
							} else {
								holder.leftShareContentTv
										.setVisibility(View.GONE);
							}
							// 链接
							holder.leftShareLinkIv.setVisibility(View.GONE);
							if (TextUtils.isEmpty(item.getJtFile().reserved3)) {
								holder.leftShareLl
										.setOnLongClickListener(longClickListener);
							} else {
								if (item.getJtFile().reserved3
										.startsWith("http://mp.weixin.qq.com/")) { // 微信分享
									// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_weixin);
									holder.leftShareLinkIv.setText("来自微信");
								} else {
									// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_normal);
									// // 其它分享
									holder.leftShareLinkIv.setText("来自网页");
								}
								holder.leftShareLinkIv
										.setVisibility(View.VISIBLE);
							}
							// 长按
							holder.leftShareLl
									.setOnLongClickListener(longClickListener);
						} else if (IMBaseMessage.TYPE_COMMUNITY == item
								.getType()) { // 左侧社群

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);

							// 社群
							holder.leftShareMsgTv.setText("分享了[社群]");
							holder.leftShareTopLl
									.setVisibility(View.VISIBLE);
							holder.leftShareBottomLl
									.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
							// 图片
							if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
								ImageLoader.load(holder.leftShareImageIv,item.getJtFile().mUrl, R.drawable.hy_chat_share_img);
							} else {
								 holder.leftShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
							}
//							// 类型
							holder.leftShareTypeIv.setVisibility(View.GONE);
							// 标题
							if (!StringUtils.isEmpty(item.getJtFile().mFileName)) {
								holder.leftShareTitleTv.setText(item.getJtFile().mFileName);
								holder.leftShareTitleTv.setVisibility(View.VISIBLE);
							}else{
								holder.leftShareTitleTv.setVisibility(View.GONE);
							}
							// 内容
							if (!StringUtils.isEmpty(item.getJtFile().mSuffixName)) {
								holder.leftShareContentTv.setText(item.getJtFile().mSuffixName);
								holder.leftShareContentTv.setVisibility(View.VISIBLE);
							} else {
								holder.leftShareContentTv
										.setVisibility(View.GONE);
							}
							// 链接
							holder.leftShareLinkIv.setVisibility(View.GONE);
							
							// 长按
							holder.leftShareLl
									.setOnLongClickListener(longClickListener);
						}else if (IMBaseMessage.TYPE_CONFERENCE == item
								.getType()) { // 左侧会议

							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
								holder.leftShareMsgTv.setText("分享了[会议]");
								holder.leftShareTopLl
										.setVisibility(View.VISIBLE);
								holder.leftShareBottomLl
										.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
								// 图片
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									ImageLoader.load(holder.leftShareImageIv, item.getJtFile().mUrl, R.drawable.hy_chat_share_img);
								} else {
									 holder.leftShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
								}
								// 类型
								holder.leftShareTypeIv
										.setImageResource(R.drawable.hy_chat_type_conference);
								// 标题
								holder.leftShareTitleTv.setText("\u3000\u3000"
										+ item.getJtFile().mFileName);
								holder.leftShareTitleTv
										.setVisibility(View.VISIBLE);
								// 内容
								if (!StringUtils
										.isEmpty(item.getJtFile().reserved1)) {
									holder.leftShareContentTv.setText(item
											.getJtFile().reserved1);
									holder.leftShareContentTv
											.setVisibility(View.VISIBLE);
								} else {
									holder.leftShareContentTv
											.setVisibility(View.GONE);
								}
								// holder.leftShareContentTv.setText(item.getJtFile().reserved1);
								// holder.leftShareContentTv.setVisibility(View.VISIBLE);
								// 链接
								holder.leftShareLinkIv.setVisibility(View.GONE);
							}
							// 长按
							holder.leftShareLl
									.setOnLongClickListener(longClickListener);
							// 点击
							holder.leftShareLl
									.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_REQUIREMENT == item
								.getType()) { // 左侧需求

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);
							holder.leftShareLl.setTag(item.getJtFile());

							holder.leftShareMsgTv
									.setText("分享了[需求]");
							holder.leftShareTopLl
									.setVisibility(View.VISIBLE);
							holder.leftShareBottomLl
									.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
							// 图片
							holder.leftShareImageIv.setVisibility(View.GONE);
							// 类型
							holder.leftShareTypeIv
									.setImageResource(R.drawable.hy_chat_type_requirement);
							// 标题
							holder.leftShareTitleTv.setText("\u3000\u3000"
									+ item.getJtFile().mFileName);
							holder.leftShareTitleTv.setVisibility(View.VISIBLE);
							// 内容
							if (!StringUtils
									.isEmpty(item.getJtFile().reserved1)) {
								holder.leftShareContentTv.setText(item
										.getJtFile().reserved1);
								holder.leftShareContentTv
										.setVisibility(View.VISIBLE);
							} else {
								holder.leftShareContentTv
										.setVisibility(View.GONE);
							}
							// holder.leftShareContentTv.setText(item.getJtFile().reserved1);
							// holder.leftShareContentTv.setVisibility(View.VISIBLE);
							// 链接
							holder.leftShareLinkIv.setVisibility(View.GONE);
							// 长按
							holder.leftShareLl
									.setOnLongClickListener(longClickListener);
						} else if (IMBaseMessage.TYPE_VIDEO == item.getType()) { // 左侧视频

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
								File file = new File(
										EUtil.getAppCacheFileDir(mContext),
										item.getJtFile().mFileName);
								if (file.exists()
										&& file.length() == item.getJtFile().mFileSize) { // 文件已经下载到本地
									BmpAsyncTask bAsyncTask = new BmpAsyncTask(holder.leftVideoIv);
									bAsyncTask.execute(file.getAbsolutePath());
								} else { // 文件尚未下载
									holder.leftVideoIv
											.setImageResource(R.drawable.hy_chat_right_pic);
								}
							}
							// 长按事件
							holder.leftVideoIv
									.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftPlayIv.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item
								.getType()
								|| // 左侧关系
								IMBaseMessage.TYPE_JTCONTACT_ONLINE == item
										.getType()
								|| IMBaseMessage.TYPE_ORG_OFFLINE == item
										.getType()
								|| IMBaseMessage.TYPE_ORG_ONLINE == item
										.getType()
								|| IMBaseMessage.TYPE_ORGANIZATION == item
										.getType()
								|| IMBaseMessage.TYPE_CUSTOMER == item
										.getType()) { // 关系

							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.VISIBLE);

							holder.leftCnsName1.setVisibility(View.GONE);
							holder.leftCnsName2.setVisibility(View.GONE);
							holder.leftCnsName3.setVisibility(View.GONE);
							if (item.getJtFile() != null) {
								final JTFile jtFile = item.getJtFile();
								// 用户
								if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item
										.getType()
										|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item
												.getType()) {
									if (!StringUtils.isEmpty(jtFile.mFileName)) {
										// holder.leftCnsTitle.setText(jtFile.mFileName);
										holder.leftCnsName1
												.setText(jtFile.mFileName);
										holder.leftCnsName1
												.setVisibility(View.VISIBLE);
									}
								}
								// 组织
								else {
									holder.leftCnsName1
											.setText(!TextUtils.isEmpty(item
													.getJtFile().mFileName)?item
															.getJtFile().mFileName:item
															.getJtFile().mSuffixName);
									holder.leftCnsName1
											.setVisibility(View.VISIBLE);

								}
								// 用户
								if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item
										.getType()
										|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item
												.getType()) {
									if (!StringUtils
											.isEmpty(jtFile.mSuffixName)) {
										holder.leftCnsName2
												.setText(jtFile.mSuffixName);
										holder.leftCnsName2
												.setVisibility(View.VISIBLE);
									}
									if (!StringUtils.isEmpty(jtFile.reserved1)) {
										holder.leftCnsName3
												.setText(jtFile.reserved1);
										holder.leftCnsName3
												.setVisibility(View.VISIBLE);
									}
								}
								// 组织
								else {
									holder.leftCnsName2
											.setText(jtFile.reserved1);
									holder.leftCnsName2
											.setVisibility(View.VISIBLE);

								}
								// if(!StringUtils.isEmpty(jtFile.mUrl)){
								if (jtFile.mUrl == null) {
									jtFile.mUrl = "";
								}

								
								if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item
										.getType()
										|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item
												.getType()) {
									if(!TextUtils.isEmpty(jtFile.mUrl)){
										ImageLoader.load(holder.leftCnsIcon, jtFile.mUrl, R.drawable.ic_default_avatar);
									}else{
										holder.leftCnsIcon.setImageResource(R.drawable.ic_default_avatar);
									}
								} else {
									if(!TextUtils.isEmpty(jtFile.mUrl)){
										ImageLoader.load(holder.leftCnsIcon, jtFile.mUrl, R.drawable.org_default_orgnization);
									}else{
										holder.leftCnsIcon.setImageResource(R.drawable.org_default_orgnization);
									}
								}

								// 点击事件
								holder.leftCnsRl
										.setOnClickListener(clickListener);
								// 长按事件
								holder.leftCnsRl
										.setOnLongClickListener(longClickListener);
							}
						} else if (IMBaseMessage.TYPE_FILE == item.getType()) { // 左侧文件

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {

								// 根据不同的后缀名设置不同的文件图片
								setFileIconAccoredSuffixName(
										holder.leftFileTypeIv, item.getJtFile()
												.getmSuffixName());

								holder.leftFileNameTv
										.setText(item.getJtFile().mFileName); // 文件名
								holder.leftFileSizeTv
										.setText(EUtil.formatFileSize(item
												.getJtFile().mFileSize)); // 文件大小
								// 是否正在下载
								boolean taskExist = false;
								if (mDownloadService != null) {
									for (FileDownloader downloader : mDownloadService
											.getListDownloader()) {
										if (downloader.getJTFile().mUrl
												.equals(item.getJtFile().mUrl)) {
											taskExist = true;
											holder.leftFileStatusTv.setText("");
											holder.leftFileProgressPb
													.setProgress(downloader
															.getProgress());
											holder.leftFileProgressPb
													.setVisibility(View.VISIBLE);
											break;
										}
									}
								}
								if (!taskExist) {
									if (new File(
											EUtil.getAppCacheFileDir(mContext),
											item.getJtFile().mFileName)
											.exists()) {
										holder.leftFileStatusTv.setText("打开");
										holder.leftFileProgressPb
												.setVisibility(View.GONE);
									} else {
										holder.leftFileStatusTv.setText("未下载");
										holder.leftFileProgressPb
												.setVisibility(View.GONE);
									}
								}
							}
							// 长按事件
							holder.leftFileLl
									.setOnLongClickListener(longClickListener);
						} else if (IMBaseMessage.TYPE_AUDIO == item.getType()) { // 左侧语音

							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {

								// 显示时长
								if (StringUtils
										.isEmpty(item.getJtFile().reserved2)) {
									holder.leftVoiceDurationTv.setText("未知\"");
								} else {
									holder.leftVoiceDurationTv.setText(item
											.getJtFile().reserved2 + "\"");
								}
								// 控件长度
								int paramsWidth = 50 + (StringUtils
										.isEmpty(item.getJtFile().reserved2) ? 0
										: Integer
												.parseInt(item.getJtFile().reserved2) * 5);
								// 最大不超过200dp
								paramsWidth = Math.min(paramsWidth, 200);
								// 设置长度
								LinearLayout.LayoutParams layoutParams = (LayoutParams) holder.leftVoiceDurationLl
										.getLayoutParams();
								layoutParams.width = EUtil
										.convertDpToPx(paramsWidth);
								holder.leftVoiceDurationLl
										.setLayoutParams(layoutParams);
								// 语音文件
								File file = new File(getVoiceFileDir(),
										item.getJtFile().mFileName);
								// 当前状态
								if (file.exists()
										&& file.length() == item.getJtFile().mFileSize) {
									holder.leftVoiceLoadingPb
											.setVisibility(View.GONE);
									holder.leftVoiceIv
											.setVisibility(View.VISIBLE);
								} else {
									long taskId = voiceFileManager.query(item
											.getJtFile().mUrl);
									if (taskId >= 0) {
										final Query query = new Query()
												.setFilterById(taskId);
										cursor = null;
										try {
											new AsyncTask<Void, Void, Integer>(){

												@Override
												protected Integer doInBackground(
														Void... params) {
													cursor = downloadManager
															.query(query);
													int status = cursor
															.getInt(cursor
																	.getColumnIndex(DownloadManager.COLUMN_STATUS));
													cursor.close();
													return status;
												}
												@Override
												protected void onPostExecute(
														Integer status) {
													super.onPostExecute(status);
													switch (status) {
													case DownloadManager.STATUS_PENDING:
													case DownloadManager.STATUS_RUNNING:
														holder.leftVoiceLoadingPb
																.setVisibility(View.VISIBLE);
														holder.leftVoiceIv
																.setVisibility(View.GONE);
														break;
													default:
														holder.leftVoiceLoadingPb
																.setVisibility(View.GONE);
														holder.leftVoiceIv
																.setVisibility(View.VISIBLE);
														break;
													}
												}
											};
											
											
										} catch (Exception e) {
											if (cursor != null) {
												cursor.close();
											}
											holder.leftVoiceLoadingPb
													.setVisibility(View.GONE);
											holder.leftVoiceIv
													.setVisibility(View.VISIBLE);
										}
									} else {
										holder.leftVoiceLoadingPb
												.setVisibility(View.GONE);
										holder.leftVoiceIv
												.setVisibility(View.VISIBLE);
									}
								}
								// 点击事件
								holder.leftVoiceLl
										.setOnClickListener(clickListener);
								// 长按事件
								holder.leftVoiceLl
										.setOnLongClickListener(longClickListener);
							}
						}
					}
				}
				// 发布时间
				String createDate = item.getTime();
				if (!TextUtils.isEmpty(createDate)) {
					createDate = JTDateUtils.getIMTimeDisplay(createDate,
							App.getApplicationConxt(), false);
				} else {
					createDate = "";
				}

				if (KeelLog.DEBUG) {
					KeelLog.i("发布时间 " + createDate);
				}

				if (TextUtils.isEmpty(createDate)) {
					holder.letterDate.setVisibility(View.GONE);
				} else {
					if (position > 0) {
						long interval = JTDateUtils.getIntervalMoreTime(
								messageList.get(position - 1).getTime(),
								item.getTime());
						// 聊天间隔小于5分钟， 不显示时间
						if (interval < 60) {
							holder.letterDate.setVisibility(View.GONE);
						} else {
							holder.letterDate.setVisibility(View.VISIBLE);
							holder.letterDate.setText(createDate);
						}
					} else {
						holder.letterDate.setVisibility(View.VISIBLE);
						holder.letterDate.setText(createDate);
					}
				}
			}

			return convertView;
		}

		class ViewHolder {

			public View viewBG;
			public RelativeLayout leftLayout;
			public RelativeLayout rightLayout;
			public TextView leftContent;
			public TextView rightContent;
			public CircleImageView leftHead;
			public CircleImageView rightHead;
			public TextView leftName;
			public TextView rightName;
			public ImageView sendMsgFail;
			public TextView letterDate;
			public TextView systemMessage;
			public ProgressBar sendMsgProgress;

			// 语音部分
			public LinearLayout leftVoiceLl;
			public LinearLayout leftVoiceDurationLl;
			public ImageView leftVoiceIv;
			public TextView leftVoiceDurationTv;
			public ProgressBar leftVoiceLoadingPb;

			public LinearLayout rightVoiceLl;
			public LinearLayout rightVoiceDurationLl;
			public ImageView rightVoiceIv;
			public TextView rightVoiceDurationTv;
			public ProgressBar rightVoiceLoadingPb;

			// 图片部分
			public RelativeLayout rightImageRl;
			public ImageView rightImageIv;

			public RelativeLayout leftImageRl;
			public ImageView leftImageIv;

			// 分享部分
			public LinearLayout leftShareLl;
			public TextView leftShareMsgTv;
			public LinearLayout leftShareTopLl;
			public ImageView leftShareImageIv;
			public LinearLayout leftShareBottomLl;
			public ImageView leftShareTypeIv;
			public TextView leftShareTitleTv;
			public TextView leftShareContentTv;
			public TextView leftShareLinkIv;

			public LinearLayout rightShareLl;
			public TextView rightShareMsgTv;
			public LinearLayout rightShareTopLl;
			public LinearLayout rightShareBottomLl;
			public ImageView rightShareImageIv;
			public ImageView rightShareTypeIv;
			public TextView rightShareTitleTv;
			public TextView rightShareContentTv;
			public TextView rightShareLinkIv;

			// 视频部分
			public RelativeLayout leftVideoRl;
			public ImageView leftVideoIv;
			public ImageView leftPlayIv;

			public RelativeLayout rightVideoRl;
			public ImageView rightVideoIv;
			public ImageView rightPlayIv;

			// 文件部分
			public LinearLayout leftFileLl;
			public ImageView leftFileTypeIv;
			public TextView leftFileNameTv;
			public TextView leftFileSizeTv;
			public TextView leftFileStatusTv;
			public ProgressBar leftFileProgressPb;

			public LinearLayout rightFileLl;
			public ImageView rightFileTypeIv;
			public TextView rightFileNameTv;
			public TextView rightFileSizeTv;
			public TextView rightFileStatusTv;
			public ProgressBar rightFileProgressPb;

			// 关系部分
			public RelativeLayout rightCnsRl;
			public TextView rightCnsTitle;
			public ImageView rightCnsIcon;
			public TextView rightCnsName1;
			public TextView rightCnsName2;
			public TextView rightCnsName3;

			public RelativeLayout leftCnsRl;
			public TextView leftCnsTitle;
			public ImageView leftCnsIcon;
			public TextView leftCnsName1;
			public TextView leftCnsName2;
			public TextView leftCnsName3;

			// 最后一条消息空白
			public LinearLayout chat_blank_ll;
		}
	}

	// 更新下载列表项
	public void updateDownloadItem(String url, int... args) {

		List<IMBaseMessage> listMsg = mAdapter.getData();
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
			View v = listView.getChildAt(index
					- listView.getFirstVisiblePosition());
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

	// 更新上传列表项
	public void updateUploadItem(String taskId, int... args) {

		List<IMBaseMessage> listMsg = mAdapter.getData();
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

			View v = listView.getChildAt(index
					- listView.getFirstVisiblePosition());
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
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								holder.rightFileProgressPb
										.setProgress(progress);
							}
						});
						break;
					case FileUploader.Status.Success:
					case FileUploader.Status.Canceled:
					case FileUploader.Status.Error:
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								holder.rightFileProgressPb
										.setVisibility(View.GONE);
							}
						});
						break;
					}
				}
			}
		}
	}

	// 更新语音项目
	public void updateVoiceItem(String url) {

		List<IMBaseMessage> listMsg = mAdapter.getData();
		if (listMsg == null || listMsg.size() < 0) {
			return;
		}
		for (int i = 0; i < listMsg.size(); i++) {
			IMBaseMessage msg = listMsg.get(i);
			if (msg.getJtFile() != null
					&& !StringUtils.isEmpty(msg.getJtFile().mUrl)
					&& msg.getJtFile().mUrl.equals(url)) {

				View v = listView.getChildAt(i
						- listView.getFirstVisiblePosition() + 1);
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
												this, getChatId()), msg
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
												this, getChatId()), msg
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

	/** 接口回调 */
	@Override
	public void bindData(int tag, Object object) {

		if (hasDestroy()) {
			return;
		}
		// 获取知识详情
		if (tag == EAPIConsts.KnoReqType.GetKnoDetails) {
			// 获取知识详情列表
			Map<String, Object> dataHm = (Map<String, Object>) object;
			if (dataHm == null) {
				return;
			}
			Knowledge2 knowledge2 = (Knowledge2) dataHm.get("knowledge2");
			if (knowledge2 != null) {
				ENavigate.startKnowledgeOfDetailActivity(ChatBaseActivity.this,
						knowledge2);
			}

		} else if (tag == EAPIConsts.KnoReqType.doGetKnoDetailsBySaveKno) {
			dismissLoadingDialog();
			// 长按保存知识
			Map<String, Object> dataHm = (Map<String, Object>) object;
			if (dataHm == null) {
				return;
			}
			Knowledge2 knowledge2 = (Knowledge2) dataHm.get("knowledge2");
			if (knowledge2 != null) {
				ENavigate.startCreateKnowledgeActivity(ChatBaseActivity.this,
						true, EConsts.ReqCode.CreateKnowledgeForResult,
						knowledge2, OperateType.Save);
			}
		} else if (tag == EAPIConsts.ReqType.FOCUS_REQUIREMENT) { // 关注需求
			dismissLoadingDialog();
			DataBox dataBox = (DataBox) object;
			if (dataBox.mIsSuccess) {
				showToast("关注成功");
			} else {
				showToast("关注失败");
			}
		} else if (tag == EAPIConsts.KnoReqType.UpdateCollectKnowledge) { // 收藏知识
			dismissLoadingDialog();
			if (object != null) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> dataBox = (HashMap<String, Object>) object;
				if (dataBox.containsKey("succeed")) {
					boolean succeed = (Boolean) dataBox.get("succeed");
					if (succeed) {
						showToast("收藏成功");
					} else {
						showToast("收藏失败");
					}
				} else {
					showToast("收藏失败");
				}
			}
		} else if (tag == EAPIConsts.CommonReqType.FetchExternalKnowledgeUrl) { // 解析url
			if (object != null) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> dataBox = (HashMap<String, Object>) object;
				if (dataBox.containsKey("knowledge2")) {
					Knowledge2 knowledge2 = (Knowledge2) dataBox
							.get("knowledge2");
					if (TextUtils.isEmpty(cacheUrl)) {
						KnowledgeReqUtil.doUpdateCollectKnowledge(this, this,
								knowledge2.getId(), knowledge2.getType(), "",
								null);
					} else {
						JTFile jtFile = knowledge2.toJTFile();
						jtFile.mFileName = cacheMessage; // 添加用户消息
						sendRichMessage(IMBaseMessage.TYPE_KNOWLEDGE2, jtFile);
					}
				} else {
					if (TextUtils.isEmpty(cacheUrl)) {
						dismissLoadingDialog();
						showToast("收藏失败");
					} else {
						sendMessage(cacheUrl);
					}
				}
			} else {
				if (TextUtils.isEmpty(cacheUrl)) {
					dismissLoadingDialog();
					showToast("收藏失败");
				} else {
					sendMessage(cacheUrl);
				}
			}
		} else if (tag == EAPIConsts.KnoReqType.GetKnoDetails) { // 获取知识详情
			dismissLoadingDialog();
			if (object != null) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				if (dataMap.containsKey("knowledge2")) {
					Knowledge2 knowledge2 = (Knowledge2) dataMap
							.get("knowledge2");
					ENavigate.startCreateKnowledgeActivity(this, true,
							knowledge2);
				} else {
					showToast("获取知识详情失败，无法保存，请重试");
				}
			} else {
				showToast("获取知识详情失败，无法保存，请重试");
			}
		} else if (tag == EAPIConsts.IMReqType.IM_REQ_CLIENTDELETEMESSAGE){
			if (object!=null) {
				int responseCode = (Integer) object;
				//删除失败
				if (responseCode== -1) {
					showToast("删除失败");
				}else{
					mAdapter.notifyDataSetChanged(); // 更新列表
					showToast("记录已删除");
				}
			}
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

	/**
	 * 获取最早一条消息的索引
	 * 
	 * @return
	 */
	public int getEarliestMessageIndex() {
		int index = -1;
		for (int i = 0; i < listMessage.size(); i++) {
			if (listMessage.size() > i) {
				IMBaseMessage msg = listMessage.get(i);
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

	/**
	 * 设置畅聊标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), title, false, null, false, true);
//		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@SuppressLint("HandlerLeak")
	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (TextUtils.isEmpty(fromActivityName)
					|| !fromActivityName.equals(ChatRecordSearchActivity.class
							.getSimpleName())) { // 搜索聊天记录时不再更新
				getMoreMessage(true); // 获取最新的数据
			}
		}
	};

	/** 获取聊天信息 */
	protected Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(0);
		}
	};

	public void notifyGetMessage() {
		mHandler.sendEmptyMessage(0);
	}

	/** 请求更新 */
	private void startGet() {
		mHandler.postDelayed(mRunnable, 100);
	}

	/** 移除回调 */
	private void stopGet() {
		mHandler.removeCallbacks(mRunnable);
	}

	// 重置获取聊天记录的时间
	/*
	 * protected void resetGetTime(){ this.mLatestGetTime = new Date(); }
	 */

	/**
	 * 启动合并返回的消息列表并保存到文件
	 * 
	 * @param context
	 * @param oldList
	 *            当前列表
	 * @param newList
	 *            服务器返回的新列表
	 * @type 
	 *       0-merge推送的消息，1-正常merge，2-仅保存oldList中的数据，3-读取数据出来，并在post中返回，4-加载更多，5-
	 *       新消息，6-搜索，7-定位
	 */

	public final static int TASK_MERGE_PUSH = 0;
	public final static int TASK_MERGE = 1;
	public final static int TASK_SAVE = 2;
	public final static int TASK_READ = 3;
	public final static int TASK_MORE = 4;
	public final static int TASK_NEW = 5;
	public final static int TASK_SEARCH = 6;

	// public final static int TASK_LOCATE = 7; // 定位消息

	public void startMergeAndSaveMessage(Context context,
			List<IMBaseMessage> oldList, List<IMBaseMessage> newList,
			Integer type) {
		ApolloUtils.execute(false, new ChatBaseTask(), context, oldList,
				newList, type);
	}

	public class ChatBaseTask extends AsyncTask<Object, Integer, Object> {

		private Context context;
		private int taskType = TASK_MERGE;
		private long currentTime;
		private long endTime;

		public ChatBaseTask() {

		}

		@Override
		protected Object doInBackground(Object... params) {
			currentTime = System.currentTimeMillis();
			try {
				synchronized (ChatBaseTask.class) {
					// 上下文对象
					if (params[0] instanceof Context) {
						context = (Context) params[0];
					} else {
						return null;
					}
					// 旧列表（增加了封装，尽量避免出现unnotification的错误）
					@SuppressWarnings("unchecked")
					ArrayList<IMBaseMessage> oldList = null;
					if (params[1] != null) {
						oldList = (ArrayList<IMBaseMessage>) params[1];
					}
					// 新列表
					@SuppressWarnings("unchecked")
					ArrayList<IMBaseMessage> newList = (ArrayList<IMBaseMessage>) params[2];
					// 任务类型
					taskType = (Integer) params[3];
					switch (taskType) {
					case TASK_MERGE_PUSH:
					case TASK_MERGE: { // 合并新旧消息列表
						List<IMBaseMessage> mergeList = new ArrayList<IMBaseMessage>();
						mergeList = IMUtil.mergeListMessage(
								ChatBaseActivity.this, oldList, newList);
//						saveCacheMessage(mergeList);
						// 被定位消息Id
						if (!TextUtils.isEmpty(locateMessageId)) {
							if (locateMessagePosition < 0) {
								for (int i = 0; i < mergeList.size(); i++) {
									if (mergeList.get(i).getMessageID()
											.equalsIgnoreCase(locateMessageId)) {
										locateMessagePosition = i;
										break;
									}
								}
							}
						}
						return mergeList;
					}
					case TASK_READ: { // 读取缓存消息
						List<IMBaseMessage> listMessage = new ArrayList<IMBaseMessage>();
//						List<IMBaseMessage> listMessage = loadCacheMessage();
//						if (listMessage == null) {
//							listMessage = new ArrayList<IMBaseMessage>();
//						}
//						// 将之前发送状态为发送中或者推送的消息，设置为发送完毕
//						for (int i = 0; i < listMessage.size(); i++) {
//							IMBaseMessage msg = listMessage.get(i);
//							if (/*msg.getSendType() == IMBaseMessage.SEND_TYPE_SENDING
//									|| */msg.getSenderType() == IMBaseMessage.SEND_TYPE_PUSH) {
//								msg.setSendType(IMBaseMessage.SEND_TYPE_SENT);
//							}
//							if (!TextUtils.isEmpty(locateMessageId)) { // 是否需要定位消息
//								if (locateMessageId.equalsIgnoreCase(msg
//										.getMessageID())) {
//									locateMessagePosition = i;
//								}
//							}
//						}
						return listMessage;
					}
					case TASK_SAVE: { // 保存到本地数据库
//						saveCacheMessage(oldList);
						return null;
					}
					case TASK_MORE: { // 直接将旧数据添加到列表最前部
						oldList.addAll(0, newList);
//						saveCacheMessage(oldList);
						listViewLastPosition = newList.size();
						return oldList;
					}
					case TASK_NEW: { // 读取数据库中最新的消息
						for (int i = 0; i < newList.size(); i++) {
							IMBaseMessage msg = newList.get(i);
							if (!msg.isHide()
									&& (msg.getSendType() == IMBaseMessage.SEND_TYPE_PUSH || msg
											.getSendType() == IMBaseMessage.SEND_TYPE_SENT)) { // 数据库中的新消息
								oldList.add(msg);
							}
						}
						return oldList;
					}
					case TASK_SEARCH: { // 搜索聊天记录
//						Pair<Integer, ArrayList<IMBaseMessage>> pair = loadCacheMessage(
//								searchMessageId, 100);
//						if (pair != null) {
//							searchMessagePosition = pair.first;
//							return pair.second;
//						}
					}
					default:
						return null;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			if (result != null) {
				// 更新列表
				listMessage = (ArrayList<IMBaseMessage>) result;
				setData(listMessage);
				dismissLoadingDialog();
				// 处理不同类型的任务
				if (taskType == TASK_READ) { // 处理第一次请求
					// 分享单条消息
					if (shareInfo != null) {
						sendRichMessage(
								IMBaseMessage
										.convertJTFileType2IMBaseMessageType(shareInfo.mType),
								shareInfo);
					}
					// 分享多条消息
					if (listShareInfo != null) {
						for (JTFile jtFile : listShareInfo) {
							sendRichMessage(
									IMBaseMessage
											.convertJTFileType2IMBaseMessageType(jtFile.mType),
									jtFile);
						}
					}
					// 定位消息位置
					if (!TextUtils.isEmpty(locateMessageId)) {
						if (locateMessagePosition > 0) {
							locateMessageId = null;
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									listView.setSelection(locateMessagePosition);
								}
							});
							// 指定时间间隔（5秒）之后开始重新定位到最新的消息
							mHandler.postDelayed(mRunnable, 5000);
						}
						return;
					}
					// 定位到最新的消息
					locateToLatestMessage();
					// 第一次读取缓存后，启动获取聊天记录
					startGet();
				} else if (taskType == TASK_MERGE_PUSH) { // 推送整合消息
					if (!TextUtils.isEmpty(locateMessageId)) {
						if (locateMessagePosition > 0) {
							locateMessageId = null;
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									listView.setSelection(locateMessagePosition);
								}
							});
						}
						return;
					}
					locateToLatestMessage(); // 定位到最新的消息
				} else if (taskType == TASK_MERGE) { // 整合消息
					if (!TextUtils.isEmpty(locateMessageId)) {
						locateMessageId = null;
						if (locateMessagePosition > 0) {
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									listView.setSelection(locateMessagePosition);
								}
							});
						}
						return;
					}
					locateToLatestMessage(); // 定位到最新的消息
				} else if (taskType == TASK_MORE) { // 加载更多
					listView.stopRefresh(); // 停止刷新列表
					listView.setSelection(listViewLastPosition); // 定位到之前的消息位置
				} else if (taskType == TASK_NEW) { // 读取最新消息
					locateToLatestMessage(); // 定位到最新的消息
				} else if (taskType == TASK_SEARCH) { // 读取搜索消息
					if (searchMessagePosition > 0) {

						mHandler.post(new Runnable() {
							@Override
							public void run() {
								listView.setSelection(searchMessagePosition);
							}
						});
					}
				}
				/*
				 * else if (taskType == TASK_LOCATE) { // 定位到指定消息 if
				 * (locateMessagePosition > 0) { // 有效的消息位置 // 直接定位到新消息
				 * mHandler.post(new Runnable() {
				 * 
				 * @Override public void run() {
				 * listView.setSelection(locateMessagePosition); } }); } //
				 * 指定时间间隔（5秒）之后开始重新定位到最新的消息 mHandler.postDelayed(mRunnable,
				 * 5000); }
				 */
			}
			endTime = System.currentTimeMillis();
			Log.i("ChatBaseActivity", (endTime - currentTime) + "");
		}
	}

	/**
	 * 获取私聊信息
	 * 
	 * @return
	 */
	public ChatDetail getChatDetail() {
		return chatDetail;
	}

	/**
	 * 获取群聊ID
	 * 
	 * @return
	 */
	public String getThatMucID() {
		return thatMucID;
	}

	/** 非好友消息提示器 */
	protected Handler strangerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			showToast(msg.getData().getString(EAPIConsts.Header.ERRORMESSAGE));
		}
	};

	public void setFileIconAccoredSuffixName(ImageView iv, String suffixName) {
		// 根据文件的类型 给文件设置不同的图标
		if (suffixName.equalsIgnoreCase("jpg")
				|| suffixName.equalsIgnoreCase("jpeg")
				|| suffixName.equalsIgnoreCase("png")
				|| suffixName.equalsIgnoreCase("bmp")) {
			iv.setImageResource(R.drawable.chat_ui_pic);
		} else if (suffixName.equalsIgnoreCase("mp4")
				|| suffixName.equalsIgnoreCase("rmvb")
				|| suffixName.equalsIgnoreCase("avi")
				|| suffixName.equalsIgnoreCase("mpeg")
				|| suffixName.equalsIgnoreCase("mkv")
				|| suffixName.equalsIgnoreCase("flv")) {
			iv.setImageResource(R.drawable.chat_ui_video);
		} else if (suffixName.equalsIgnoreCase("pdf")) {
			iv.setImageResource(R.drawable.chat_ui_pdf);
		} else if (suffixName.equalsIgnoreCase("txt")) {
			iv.setImageResource(R.drawable.chat_ui_txt);
		} else if (suffixName.equalsIgnoreCase("pptx")
				|| suffixName.equalsIgnoreCase("ppt")
				|| suffixName.equalsIgnoreCase("pptm")) {
			iv.setImageResource(R.drawable.chat_ui_ppt);
		} else if (suffixName.equalsIgnoreCase("doc")
				|| suffixName.equalsIgnoreCase("docx")
				|| suffixName.equalsIgnoreCase("docm")) {
			iv.setImageResource(R.drawable.chat_ui_word);
		} else if (suffixName.equalsIgnoreCase("doc")
				|| suffixName.equalsIgnoreCase("docx")) {
			iv.setImageResource(R.drawable.chat_ui_word);
		} else if (suffixName.equalsIgnoreCase("xlsx")
				|| suffixName.equalsIgnoreCase("xls")) {
			iv.setImageResource(R.drawable.chat_ui_excel);
		} else {
			iv.setImageResource(R.drawable.chat_file);
		}
	}

	/** 表情点击事件 */
	private SmileyView.OnItemClickListener smileyViewClickListener = new SmileyView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg) {
			final SmileyParser parser = SmileyParser
					.getInstance(ChatBaseActivity.this);
			if (position == SmileyView.MaxSmileyNumber) { // 删除表情
				String text = textEt.getText().toString();

				if (text.length() > 0) {

					if (text.lastIndexOf(RIGHTSPECCHAR) == text.length() - 1) {
						text = text
								.substring(0, text.lastIndexOf(LEFTSPECCHAR));
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

	private List<IMBaseMessage> messageList;

	private String fileIds;


	class BmpAsyncTask extends AsyncTask<String, Void, Bitmap>{

		private final WeakReference<ImageView> imageViewReference;
		private String data = "";
		
		public BmpAsyncTask(ImageView imageView){
			imageViewReference=new WeakReference<ImageView>(imageView);
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			data=params[0];
			MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
			mediaMetadataRetriever.setDataSource(data);
			Bitmap bmpOriginal = mediaMetadataRetriever
					.getFrameAtTime(0);
			Bitmap netImage = PictureManageUtil
					.resizeBitmap(
							bmpOriginal,
							Utils.dipToPx(
									ChatBaseActivity.this,
									SCALESIZE),
							Utils.dipToPx(
									ChatBaseActivity.this,
									SCALESIZE));
			
			
			return netImage;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}
	
	private ArrayList<IMBaseMessage> getMessageHasImg(ArrayList<IMBaseMessage> listMessage){
		ArrayList<IMBaseMessage> listImgMessage = new ArrayList<IMBaseMessage>();
		for(IMBaseMessage imbm : listMessage){
			if(imbm.getType() == 2){
				listImgMessage.add(imbm);
			}
		}
		return listImgMessage;
	}
}
