package com.tr.ui.conference.im;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
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

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.ConferenceReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.api.KnowledgeReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.db.ChatLocalFileDBManager;
import com.tr.db.MeetingRecordDBManager;
import com.tr.db.VoiceFileDBManager;
import com.tr.model.api.DataBox;
import com.tr.model.conference.MMeetingDetail;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.im.IMUtil.IMTimeComparator;
import com.tr.model.im.MNotifyMessageBox;
import com.tr.model.im.MSendMessage;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MeetingMessage;
import com.tr.model.obj.RequirementMini;
import com.tr.model.user.JTMember;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.service.HyChatFileDownloadService;
import com.tr.service.HyChatFileDownloadService.ServiceBinder;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.FilePreviewActivity;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.tr.ui.conference.square.MeetingBranchFragment;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.im.ChatBaseActivity;
import com.tr.ui.widgets.BasicListView;
import com.tr.ui.widgets.ChatDialog;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyParser2;
import com.tr.ui.widgets.SmileyView;
import com.utils.common.ApolloUtils;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.FilePathResolver;
import com.utils.common.GlobalVariable;
import com.utils.common.HyChatFileDownloader;
import com.utils.common.HyChatFileUploader;
import com.utils.common.HyChatFileUploader.OnFileUploadListener;
import com.utils.common.JTDateUtils;
import com.utils.common.OpenFiles;
import com.utils.common.TaskIDMaker;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.LoadImage;
import com.utils.log.KeelLog;
import com.utils.log.ToastUtil;
import com.utils.picture.PictureManageUtil;
import com.utils.string.StringUtils;
import com.utils.time.Util;

/**
 * 封装caht和Muc共性操作 注：语音的下载使用DownloadManager
 * 
 * @author leon
 */
public abstract class MChatBaseActivity extends JBaseFragmentActivity implements IBindData, OnFileUploadListener {

	public final String TAG = getClass().getSimpleName();
	// 录音时间标尺
	private long recordTimestamp = 0;
	public final int sendshow = 0;
	public final int sendgone = 1;

	private final int SEND_TEXT = 0; // 发送文本
	private final int SEND_VOICE = 1; // 发送语音
	private boolean meetingTopicDataChanged;

	// 显示消息的View
	protected BasicListView listView;
	protected ChatBaseAdapter mAdapter;
	// 语音/文本发送
	private ImageView switchIv; // 切换面板
	private TextView voiceTv; // 语音按钮
	private EditText textEt; // 文本按钮
	private ImageView expressionIv;// 表情
	private ImageView sendIv; // 发送/附件

	private LinearLayout viewPagerCon;
	private ViewPager viewPager;
	private Boolean isShowface = false;
	private GridView moreGrid = null;

	// 初始化语音的保存路径
	public String recordPath = null;
	public String recordAudioPath = null;
	public String recordNamePrefix = null;
	protected Handler mNetHandler = new Handler();

	// 聊天数据
	protected ArrayList<MeetingMessage> listMessage = new ArrayList<MeetingMessage>();// 聊天记录列表
	protected long meetingId; // 主会场id
	protected long topicId; // 分会场id
	protected MMeetingQuery meetingDetail; // 会议详情
	protected MMeetingTopicQuery topicDetail; // 分会场详情
	protected String fromActivityName; // 跳转来源
	protected String searchMessageId; // 搜索的记录id

	// 上次获取消息记录的时间
	private Date mLatestGetTime = new Date();

	// 拍照图片文件
	protected File mPictureFile;
	// 视频文件
	protected File mVideoFile;
	// 待分享的消息
	protected JTFile shareInfo;
	protected ArrayList<JTFile> shareInfoList;
	// 广播过滤器
	private IntentFilter broadcastFilter;
	// 文件上传队列
	protected List<HyChatFileUploader> listUploader = new ArrayList<HyChatFileUploader>();
	// 输入
	private LinearLayout inputLl;
	// 录音相关
	private MediaRecorder mRecorder;
	private MediaPlayer mPlayer;
	private VoiceFileDBManager voiceFileManager;
	private DownloadManager downloadManager;
	private String filePath;
	// 下载服务
	private HyChatFileDownloadService downloadService;

	DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_default_avatar).showImageForEmptyUri(R.drawable.ic_default_avatar)
			.showImageOnFail(R.drawable.ic_default_avatar).cacheInMemory(true).cacheOnDisc(true).build();

	private List<SmileyView> listSmileyViews;
	private static final String LEFTSPECCHAR = ((char) 0X1B) + "";
	private static final String RIGHTSPECCHAR = ((char) 0X11) + "";

	// 下载服务连接对象
	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			downloadService = ((ServiceBinder) binder).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};

	// 下载状态广播监听器
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				String action = intent.getAction();
				if (TextUtils.isEmpty(action)) {
					return;
				}
				String messageId = intent.getStringExtra(EConsts.Key.MESSAGE_ID);
				if (action.equals(EConsts.Action.DOWNLOAD_SUCCESS)) {
					updateDownloadItem(messageId, HyChatFileDownloader.Status.Success);
				} else if (action.equals(EConsts.Action.DOWNLOAD_FAILED)) {
					updateDownloadItem(messageId, HyChatFileDownloader.Status.Error);
				} else if (action.equals(EConsts.Action.DOWNLOAD_CANCELED)) {
					updateDownloadItem(messageId, HyChatFileDownloader.Status.Canceled);
				} else if (action.equals(EConsts.Action.DOWNLOAD_UPDATE)) {
					int progress = intent.getIntExtra(EConsts.Key.PROGRESS_UPDATE, 0);
					updateDownloadItem(messageId, HyChatFileDownloader.Status.Started, progress);
				} else if (action.equals(EConsts.Action.DOWNLOAD_START)) {
					updateDownloadItem(messageId, HyChatFileDownloader.Status.Prepared);
				} else if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) { // 语音文件完成下载
					long taskId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
					if (taskId >= 0) {
						String url = voiceFileManager.query(taskId);
						voiceFileManager.delete(taskId); // 删除数据表信息
						updateVoiceItem(url); // 更新界面
					}
				}
			}
		}
	};

	// 聊天记录数据库管理器
	protected MeetingRecordDBManager meetingRecordDBManager;
	// 上传的本地文件地址数据库管理器
	protected ChatLocalFileDBManager localFileDBManager;
	// 缓存的超链接
	protected String cacheUrl = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_im_chat_base);
		initView();
		initVars();
		cleanNotifyBox();
		listMessage = new ArrayList<MeetingMessage>();
		setData(listMessage);
		// 绑定服务
		bindService(new Intent(this, HyChatFileDownloadService.class), serviceConnection, BIND_AUTO_CREATE);
	}

	// 进行一些初始化工作
	protected void doInit() {

		// 显示输入框
		if (meetingDetail.getMeetingStatus() == 3 || topicDetail.getIsFinished() == 1) { // 会议或分会场已结束，不能聊天
			inputLl.setVisibility(View.GONE);
		} else if (!TextUtils.isEmpty(fromActivityName) && fromActivityName.equalsIgnoreCase(MChatRecordSearchActivity.class.getSimpleName())) { // 查找聊天记录
			inputLl.setVisibility(View.GONE);
		} else {
			inputLl.setVisibility(View.VISIBLE);
		}
		// 加载标题栏
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		fragment = new MeetingBranchFragment(this, meetingDetail, topicDetail);
		fragment.setMeetingTopicDataChanged(meetingTopicDataChanged);
		transaction.add(R.id.speakerFl, fragment);
		transaction.commitAllowingStateLoss();
		// 聊天记录数据库管理对象
		if (meetingRecordDBManager == null) {
			meetingRecordDBManager = new MeetingRecordDBManager(this);
		}
		// 本地文件
		if (localFileDBManager == null) {
			localFileDBManager = new ChatLocalFileDBManager(this);
		}
		// 是否只是查看聊天记录

		// 读取聊天记录缓存数据
		startMergeAndSaveMessage(this, null, null, TASK_READ);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 解除绑定
		unbindService(serviceConnection);
		// 停止上传
		stopUploadTask();
	}

	// 初始化变量
	@SuppressWarnings("unchecked")
	protected void initVars() {
		// 分享的消息对象
		shareInfo = (JTFile) getIntent().getSerializableExtra(ENavConsts.EJTFile);
		shareInfoList = (ArrayList<JTFile>) getIntent().getSerializableExtra(ENavConsts.EListJTFile);
		// 广播过滤器
		broadcastFilter = new IntentFilter();
		broadcastFilter.addAction(EConsts.Action.DOWNLOAD_START);
		broadcastFilter.addAction(EConsts.Action.DOWNLOAD_UPDATE);
		broadcastFilter.addAction(EConsts.Action.DOWNLOAD_SUCCESS);
		broadcastFilter.addAction(EConsts.Action.DOWNLOAD_FAILED);
		broadcastFilter.addAction(EConsts.Action.DOWNLOAD_CANCELED);
		broadcastFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE); // 语音专用
		// 语音文件管理
		if (voiceFileManager == null) {
			voiceFileManager = new VoiceFileDBManager(this);
		}
		// 下载管理器
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
	}

	// 初始化ImageLoader参数配置
	protected void initImageLoaderConfiguration() {

		// 设置图片缓存路径
		File cacheDir = EUtil.getMeetingChatFileDir(this, JTFile.TYPE_IMAGE, meetingId, topicId);
		if (cacheDir != null) {
			ImageLoader.getInstance().destroy();
			ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext()).discCache(new UnlimitedDiscCache(cacheDir))
					.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null).defaultDisplayImageOptions(EUtil.getDefaultImageLoaderDisplayOptions()).build();
			ImageLoader.getInstance().init(configuration);
		}
	}

	// 恢复默认设置
	protected void recoverImageLoaderConfiguration() {
		try {
			ImageLoader.getInstance().destroy();
			ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(EUtil.getDefaultImageLoaderDisplayOptions()).build();
			ImageLoader.getInstance().init(configuration);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 清空消息盒子里的内容
	public void cleanNotifyBox() {
		MNotifyMessageBox.clearConferenceMessage();
	}

	// 清空聊天记录列表
	public void cleanHistory() {
		listMessage = new ArrayList<MeetingMessage>();
		setData(listMessage);
	}

	@Override
	public void initJabActionBar() {
		getActionBar().hide();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.im_mucdetail_menu, menu);
		MenuItem menuItem = menu.findItem(R.id.im_muc_detail_create_next);
		menuItem.setVisible(true);
		menuItem.setIcon(getResources().getDrawable(R.drawable.chat_huiyishezhi));
		return true;
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
		// 聊天列表
		listView = (BasicListView) findViewById(R.id.personal_letter_item_ltw);
		// 输入面板
		inputLl = (LinearLayout) findViewById(R.id.inputLl);
		// 发送/附件按钮
		sendIv = (ImageView) findViewById(R.id.sendIv);
		sendIv.setOnClickListener(listener);
		sendIv.setTag(sendgone);
		// 文本输入框
		textEt = (EditText) findViewById(R.id.textEt);
		textEt.setOnClickListener(listener);
		textEt.addTextChangedListener(new Watcher());
		// 表情
		expressionIv = (ImageView) findViewById(R.id.expressionIv);
		expressionIv.setOnClickListener(listener);
		//
		viewPager = (ViewPager) this.findViewById(R.id.smileyPager);
		viewPagerCon = (LinearLayout) this.findViewById(R.id.smileyPagerContainer);
		listSmileyViews = new ArrayList<SmileyView>();
		int totalPage = (int) Math.ceil(SmileyParser.mEnhancedIconIds.length * 1.0 / SmileyView.MaxSmileyNumber);
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

		// 更多
		moreGrid = (GridView) this.findViewById(R.id.moreGrid);
		moreGrid.setAdapter(new MoreGridAdpter());
		initAdpter();
	}

	/**
	 * 播放录音
	 */
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
			}
		}
	}

	/**
	 * 停止播放录音
	 */
	private void stopPlaying() {
		// 停止播放
		if (mPlayer != null) {
			mPlayer.reset();
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

	/**
	 * 停止录音Runnable
	 */
	private Runnable mStopPlayingRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
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
//				voiceIv.setBackgroundResource(R.drawable.chatto_voice_playing);
				voiceIv.setBackgroundResource(R.drawable.chat_right_voice_playing);
			} else { // 对方
				voiceIv.setBackgroundResource(R.drawable.chatfrom_voice_playing);
			}
		}
	}

	/**
	 * 开始录音
	 */
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

	/**
	 * 停止录音
	 */
	private void stopRecording() throws Exception{
		if (mRecorder != null) {
			mRecorder.setOnErrorListener(null);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case EConsts.ReqCode.SelectFromMyRequirement: { // 需求
			Iterator<Entry<Integer, RequirementMini>> iter = InitiatorDataCache.getInstance().shareDemandSelectedMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				RequirementMini item = (RequirementMini) entry.getValue();
				JTFile jtFile = JTFile.createFromRequirementMini(item);
				if (jtFile != null) {
					sendFile(jtFile);
				}
			}
			// 清空缓存
			InitiatorDataCache.getInstance().shareDemandSelectedMap.clear();
		}
			break;
		case EConsts.ReqCode.SelectFromMyKnowledge: { // 知识
			Iterator<Entry<Long, KnowledgeMini2>> iter = InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				KnowledgeMini2 item = (KnowledgeMini2) entry.getValue();
				JTFile jtFile = JTFile.createFromKnowledgeMini2(item);
				if (jtFile != null) {
					sendFile(jtFile);
				}
			}
			// 清空缓存
			InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.clear();
		}
			break;
		case EConsts.ReqCode.SelectFromMyConnection_share: { // 人脉或线下机构
			JTFile jtFile = (JTFile) intent.getSerializableExtra(ENavConsts.redatas);
			if (jtFile != null) {
				IMBaseMessage im = new IMBaseMessage();
				im.setType(jtFile);
				im.setJtFile(jtFile);
				sendFile(jtFile);
			}
		}
			break;
		case EConsts.ReqCode.SelectFromMyConnection: { // 关系
			JTFile jtFile = (JTFile) intent.getSerializableExtra(ENavConsts.redatas);
			if (jtFile != null) {

				IMBaseMessage im = new IMBaseMessage();
				im.setType(jtFile);
				im.setJtFile(jtFile);

				if (jtFile.mType == JTFile.TYPE_JTCONTACT_OFFLINE || jtFile.mType == JTFile.TYPE_ORG_OFFLINE) { // 人脉或线下机构
					// 跳转到人脉可见性选择页面
					ENavigate.startIMCreateMeetingCategoryActivity1Result(this, meetingDetail, im, EConsts.ReqCode.SelectFromMyConnection_share);
				} else if (jtFile.mType == JTFile.TYPE_JTCONTACT_ONLINE || jtFile.mType == JTFile.TYPE_ORG_ONLINE) { // 用户或线上机构
					// 发送消息
					sendFile(jtFile);
				}
			}
		}
			break;
		case EConsts.REQ_CODE_PICK_PICTURE: // 选照
		case EConsts.REQ_CODE_TAKE_PICTURE: { // 拍照
			JTFile jtFile = null;
			if (requestCode == EConsts.REQ_CODE_PICK_PICTURE) {
				Uri uri = intent.getData();
				jtFile = EUtil.createJTFileFromLocalFile(FilePathResolver.getPath(this, uri));
			} else {
				jtFile = EUtil.createJTFileFromLocalFile(mPictureFile.getAbsolutePath());
			}
			if (jtFile != null) {
				if (jtFile.mFileSize > 1024 * 1024) { // 图片大小超过1M进行压缩处理
					JTFile thumbJtFile = EUtil.createJTFileFromLocalFile(getImageThumbCachePath(jtFile.mLocalFilePath, getUserAndTimeBasedFormatName(".jpg"), meetingId, topicId));
					if (thumbJtFile != null) {
						sendFile(thumbJtFile); // 发送缩略图
					} else {
						sendFile(jtFile); // 处理失败发送原图
					}
				} else {
					sendFile(jtFile); // 图片小于1M以直接发送
				}
			} else {
				showToast("地址解析错误或不支持的图片格式!");
			}
		}
			break;
		case EConsts.REQ_CODE_PICK_FILE: { // 文件
			Uri uri = intent.getData();
			JTFile jtFile = EUtil.createJTFileFromLocalFile(FilePathResolver.getPath(this, uri));
			if (jtFile != null) {
				sendFile(jtFile);
			} else {
				showToast("地址解析错误!");
			}
		}
			break;
		case EConsts.REQ_CODE_PICK_VIDEO: // 选视频
		case EConsts.REQ_CODE_TAKE_VIDEO: { // 拍视频
			JTFile jtFile = null;
			if (requestCode == EConsts.REQ_CODE_TAKE_VIDEO) {
//				Uri uri = intent.getData();
//				jtFile = EUtil.createJTFileFromLocalFile(FilePathResolver.getPath(this, uri));
				if (mVideoFile!=null) {
					jtFile = EUtil.createJTFileFromLocalFile(mVideoFile
							.getAbsolutePath());
				}
			
			} else {
				jtFile = EUtil.createJTFileFromLocalFile(FilePathResolver
						.getPath(this, intent.getData()));
			}
			if (jtFile != null && jtFile.mType == JTFile.TYPE_VIDEO) {
				sendFile(jtFile);
			} else {
				showToast("地址解析错误或不支持的视频格式!");
			}
		}
			break;
		case EConsts.CALL_MEETING_TOPIC_EDIT: {
			if (resultCode == RESULT_OK) {
				topicDetail = (MMeetingTopicQuery) intent.getSerializableExtra("MeetingTopicQuery");
				int meetingStatus = (int) intent.getIntExtra("meetingStatus", -1);
				if (meetingStatus != -1 && meetingStatus != meetingDetail.getMeetingStatus()) {
					meetingTopicDataChanged = true;
					meetingDetail.setMeetingStatus(meetingStatus);
				}
				if (topicDetail != null) {
					meetingTopicDataChanged = true;
					doInit();
				}
				// showLoadingDialog();
				// ConferenceReqUtil.doGetMeetingDetail(this, this, meetingId,
				// App.getApp().getUserID(), null);
			}
		}
			break;

		case EConsts.CALL_MEETING_ATTEND_INFOMATION: {
			Intent data2 = new Intent();
			setResult(RESULT_OK, data2);
			finish();
		}
			break;
		case EConsts.ReqCode.SelectFromMyConnectionAndOrg: {
			// 关系
			Object object = intent.getSerializableExtra(ENavConsts.redatas);
			if (object != null && object instanceof JTFile) {
				JTFile jtFile = (JTFile) object;
				IMBaseMessage msg = new IMBaseMessage();
				msg.setJtFile(jtFile);
				msg.setType(jtFile);

				sendFile(jtFile);
			}

		}
			break;
		}
	}

	class MoreGridAdpter extends BaseAdapter implements OnClickListener {

		private String[] names = new String[] { "需求", "知识", "关系", "图片", "文件", "拍摄", "视频" };
//		private int[] images = new int[] { R.drawable.hy_chat_req_bg, R.drawable.hy_chat_kno_bg, R.drawable.hy_chat_conns_bg, R.drawable.hy_chat_pic_bg, R.drawable.hy_chat_file_bg,
//				R.drawable.hy_chat_cam_bg, R.drawable.hy_chat_vid_bg };
		private int[] images = new int[] { R.drawable.chat_share_req,
				R.drawable.chat_share_kno, R.drawable.chat_share_conns,
				R.drawable.chat_share_pic, R.drawable.chat_share_file,
				R.drawable.chat_share_camera, R.drawable.chat_share_video };
		public MoreGridAdpter() {

		}

		@Override
		public int getCount() {
			return names.length;
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
				convertView = inflater.inflate(R.layout.im_chatmoregriditem, parent, false);
			}
			TextView typeTv = (TextView) convertView.findViewById(R.id.text);
			ImageView typeIv = (ImageView) convertView.findViewById(R.id.image);
			typeIv.setOnClickListener(this);
			typeIv.setTag(position);
			typeTv.setText(names[position]);
			typeIv.setImageResource(images[position]);
			return convertView;
		}

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			switch (position) {
			case 0: // 需求
				InitiatorDataCache.getInstance().releaseAll(); // 清除旧数据
				ENavigate.startHyShareActivity(MChatBaseActivity.this, 1, EConsts.ReqCode.SelectFromMyRequirement);
				break;
			case 1: // 知识
				InitiatorDataCache.getInstance().releaseAll(); // 清除旧数据
				ENavigate.startHyShareActivity(MChatBaseActivity.this, 2, EConsts.ReqCode.SelectFromMyKnowledge);
				break;
			case 2: // 关系
				InitiatorDataCache.getInstance().releaseAll(); // 清除旧数据
				// ENavigate.startHyShareActivity(MChatBaseActivity.this, 3,
				// EConsts.ReqCode.SelectFromMyConnection);
				ENavigate.startIMRelationSelectActivityEx(MChatBaseActivity.this, ENavConsts.EShareCnsSelectActivity, EConsts.ReqCode.SelectFromMyConnectionAndOrg);
				break;
			case 3: // 图片
				EUtil.dispatchPickPictureIntent(MChatBaseActivity.this);
				break;
			case 4: // 文件
				EUtil.dispatchPickFileIntent(MChatBaseActivity.this);
				break;
			case 5: // 拍摄
				new AlertDialog.Builder(MChatBaseActivity.this).setItems(new String[] { "拍照", "拍视频" }, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							File cacheDir = EUtil.getMeetingChatFileDir(MChatBaseActivity.this, JTFile.TYPE_IMAGE, meetingId, topicId);
							if (cacheDir == null) {
								showToast("无法获取图片缓存目录,请重试");
								return;
							}
							mPictureFile = new File(cacheDir, getUserAndTimeBasedFormatName(".jpg"));
							if (mPictureFile.exists()) {
								mPictureFile.delete();
							}
							EUtil.dispatchTakePictureIntent(MChatBaseActivity.this, Uri.fromFile(mPictureFile));
						} else {
							File cacheDir = EUtil.getMeetingChatFileDir(MChatBaseActivity.this, JTFile.TYPE_VIDEO, meetingDetail.getId(), topicDetail.getId());
							if (cacheDir == null) {
								showToast("无法获取视频缓存目录,请重试");
								return;
							}
							mVideoFile = new File(cacheDir, getUserAndTimeBasedFormatName(".mp4"));
							if (mVideoFile.exists()) {
								mVideoFile.delete();
							}
							EUtil.dispatchTakeVideoIntent(MChatBaseActivity.this, Uri.fromFile(mVideoFile));
						}
					}
				}).create().show();
				break;
			case 6: // 视频
				EUtil.dispatchPickVideoIntent(MChatBaseActivity.this);
				break;
			}
		}
	}

	public class Watcher implements TextWatcher {
		@Override
		public void afterTextChanged(Editable s) {
			String inputStr = s.toString();
			if (inputStr.length() == 0) { // StringUtils.isEmpty(inputStr)
											// 无法处理"null"的字符串
				sendIv.setImageResource(R.drawable.chat_more);
				sendIv.setTag(sendgone);
			} else {
				sendIv.setImageResource(R.drawable.hy_chat_send);
				moreGrid.setVisibility(View.GONE);
				sendIv.setTag(sendshow);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

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

	// 开始上传任务
	protected void startNewUploadTask(IMBaseMessage msg) {
		HyChatFileUploader uploader = new HyChatFileUploader(this, msg.getJtFile(), msg.getMessageID());
		uploader.setOnFileUploadListener(this);
		listUploader.add(uploader);
		uploader.start();
	}

	// 取消全部上传任务
	protected void stopUploadTask() {
		for (HyChatFileUploader uploader : listUploader) {
			uploader.cancel();
		}
		listUploader.clear();
	}

	// 取消指定上传任务
	protected void stopUploadTask(String messageId) {
		for (HyChatFileUploader uploader : listUploader) {
			if (uploader.getMessageId().equals(messageId)) {
				uploader.cancel();
				listUploader.remove(uploader);
				break;
			}
		}
	}

	// 移除指定上传任务
	protected void removeUploadTask(String messageId) {
		for (HyChatFileUploader uploader : listUploader) {
			if (uploader.getMessageId().equals(messageId)) {
				listUploader.remove(uploader);
				break;
			}
		}
	}

	// 上传任务是否存在
	protected boolean isUploadTaskExist(String messageId) {
		for (HyChatFileUploader uploader : listUploader) {
			if (uploader.getMessageId().equals(messageId)) {
				return true;
			}
		}
		return false;
	}

	// 获取上传任务进度
	protected int getUploadTaskProgress(String messageId) {
		if (isUploadTaskExist(messageId)) {
			for (HyChatFileUploader uploader : listUploader) {
				if (uploader.getMessageId().equals(messageId)) {
					return uploader.getProgress();
				}
			}
			return 0;
		} else {
			return 0;
		}
	}

	// 获取上传任务状态
	protected int getUploadTaskStatus(String messageId) {
		if (isUploadTaskExist(messageId)) {
			for (HyChatFileUploader uploader : listUploader) {
				if (uploader.getMessageId().equals(messageId)) {
					return uploader.getStatus();
				}
			}
			return 0;
		} else {
			return 0;
		}
	}

	// 发送普通文本消息
	abstract public void sendMessage(String text);

	// 重发消息
	abstract public void resendMessage(MeetingMessage msg);

	// 发送多媒体消息
	abstract public void sendFile(JTFile jtFile);

	// 获取更多消息
	abstract public void getMoreMessage();

	// 加载缓存的消息记录
	abstract public List<MeetingMessage> loadCacheMessage();

	// 加载缓存的消息记录（搜索记录，指定消息前后最多50条）
	abstract public List<MeetingMessage> loadCacheMessage(String messageId, String keyword, int span);

	// 保存一组记录
	abstract public void saveCacheMessage(List<MeetingMessage> list);

	// 更新单条数据
	abstract public void updateCacheMessage(MeetingMessage msg);

	// 删除单条记录
	abstract public void deleteCacheMessage(String msgId);

	// 返回指定消息发送人的昵称
	abstract public String getNickNameByMessage(MeetingMessage msg);

	// 返回指定消息发送人的头像
	abstract public String getImageByMessage(MeetingMessage msg);

	// 初始化适配器
	public void initAdpter() {
		mAdapter = new ChatBaseAdapter(MChatBaseActivity.this);
		List<MeetingMessage> messageList = new ArrayList<MeetingMessage>();
		mAdapter.setData(messageList);
		listView.setAdapter(mAdapter);
		setData(messageList);
	}

	public void setData(List<MeetingMessage> messageList) {
		mAdapter.setData(messageList);
		refreshList();
	}

	private OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == sendIv.getId()) { // 发送按钮
				if (fragment != null) {
					fragment.setNotFold();
				}
				int inttag = ((Integer) sendIv.getTag()).intValue();
				if (inttag == sendshow) { // send按钮在显示
					moreGrid.setVisibility(View.GONE);
					sendIv.setTag(sendgone);
					// 显示发送
					isShowface = false;
					String txtSend = textEt.getText().toString();
					// 发送消息
					cacheUrl = "";
					if (txtSend.startsWith("http://") || txtSend.startsWith("https://")) {
						cacheUrl = txtSend;
						// UserReqUtil
						// .doGetTreatedHtml(MChatBaseActivity.this,
						// MChatBaseActivity.this,
						// UserReqUtil.getTreatedHtmlParams(cacheUrl), null);
						CommonReqUtil.doFetchExternalKnowledgeUrl(MChatBaseActivity.this, MChatBaseActivity.this, cacheUrl, true, null);
					} else if (txtSend.trim().isEmpty()) {
						showToast("消息内容不能为空");
					} else {
						sendMessage(txtSend);
					}
					textEt.setText("");
					sendIv.setImageResource(R.drawable.hy_chat_more_bg);
					// 隐藏软盘
					InputMethodManager m = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					m.hideSoftInputFromWindow(textEt.getApplicationWindowToken(), 0);
					viewPagerCon.setVisibility(View.GONE);
					expressionIv.setImageResource(R.drawable.hy_chat_exp_bg);
				} else {
					if (moreGrid.getVisibility() == View.GONE) {
						moreGrid.setVisibility(View.VISIBLE);
						sendIv.setTag(sendgone);
						viewPagerCon.setVisibility(View.GONE);
						// 隐藏软盘，加了之后gridView起不来
						InputMethodManager m = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						m.hideSoftInputFromWindow(textEt.getApplicationWindowToken(), 0);
					} else {
						moreGrid.setVisibility(View.GONE);
						sendIv.setTag(sendgone);
						viewPagerCon.setVisibility(View.GONE);
						// 显示软盘
						InputMethodManager manager = (InputMethodManager) textEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
			} else if (v.getId() == expressionIv.getId()) { // 笑脸按钮
				if (!isShowface) {
					if (fragment != null) {
						fragment.setNotFold();
					}
					// sendImageIv.setImageResource(R.drawable.chat_keyboard);
					viewPagerCon.setVisibility(View.VISIBLE);
					isShowface = true;
					moreGrid.setVisibility(View.GONE);
					// 隐藏软盘，加了之后gridView起不来
					InputMethodManager m = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					m.hideSoftInputFromWindow(textEt.getApplicationWindowToken(), 0);
					// 消息定位到最新
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							listView.setSelection(mAdapter.getData().size());
						}
					});
				} else {
					expressionIv.setImageResource(R.drawable.hy_chat_exp_bg);
					viewPagerCon.setVisibility(View.GONE);
					isShowface = false;
					moreGrid.setVisibility(View.GONE);
					// 显示软盘
					InputMethodManager manager = (InputMethodManager) textEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			} else if (v.getId() == textEt.getId()) {
				if (fragment != null) {
					fragment.setNotFold();
				}
				if (isShowface) {
					expressionIv.setImageResource(R.drawable.hy_chat_exp_bg);
					viewPagerCon.setVisibility(View.GONE);
					isShowface = false;
					// 显示软盘
					textEt.setFocusableInTouchMode(true);
				}
				moreGrid.setVisibility(View.GONE);
			} else if (v.getId() == switchIv.getId()) { // 切换语音和文本
				viewPagerCon.setVisibility(View.GONE);
				isShowface = false;
				moreGrid.setVisibility(View.GONE);
				sendIv.setTag(sendgone);
				int tag = ((Integer) v.getTag()).intValue();
				if (tag == SEND_TEXT) { // 发送语音
					switchIv.setTag(SEND_VOICE);
					switchIv.setImageResource(R.drawable.hy_chat_keyboard_bg);
					textEt.setVisibility(View.GONE);
					voiceTv.setVisibility(View.VISIBLE);
					expressionIv.setVisibility(View.GONE);
					sendIv.setImageResource(R.drawable.hy_chat_more_bg);
					sendIv.setTag(sendgone);
				} else { // 发送文本
					switchIv.setTag(SEND_TEXT);
					switchIv.setImageResource(R.drawable.hy_chat_microphone_bg);
					voiceTv.setVisibility(View.GONE);
					textEt.setVisibility(View.VISIBLE);
					expressionIv.setVisibility(View.VISIBLE);
					sendIv.setImageResource(R.drawable.hy_chat_more_bg);
					sendIv.setTag(sendgone);
				}
			}else if (v.getId() == voiceTv.getId()) {
				long time = System.currentTimeMillis();   
				long lastClickTime = 0;
		        if ( time - lastClickTime < 500) {     
		            return;     
		        }     
		        lastClickTime = time;     
			}
		}
	};

	public void hideSoftInput() {
		InputMethodManager m = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		m.hideSoftInputFromWindow(textEt.getApplicationWindowToken(), 0);
	}

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

		@SuppressWarnings("static-access")
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
					// 间隔时间度量衡
					return false;
				}
				// 间隔时间度量衡
				recordTimestamp = System.currentTimeMillis();
				// 停止播放
				stopPlaying();
				
				// 文件名(防止不同用户发送的文件名重复)
				mAudioName = getUserAndTimeBasedFormatName(".amr");
				
				// 音频文件存储路径
				File dir = EUtil.getMeetingChatFileDir(MChatBaseActivity.this, JTFile.TYPE_AUDIO, meetingId, topicId);
				if (dir != null) {
					// 语音文件完整路径
					mAudioPath = new File(dir, mAudioName).getAbsolutePath();
					// 显示对话框
					if (chatDlg == null) {
						chatDlg = new ChatDialog(MChatBaseActivity.this);
					}
					chatDlg.show();
					// 开始录音
					
					startRecording(mAudioPath);
					
				} else {
					// 语音文件路径置空
					mAudioPath = "";
					// 弹出提示消息
					ToastUtil.showToast(MChatBaseActivity.this, "没有SD卡，无法生成语音文件");
//					showToast("没有SD卡，无法生成语音文件");
					return false;
				}
				break;
			case MotionEvent.ACTION_UP: // 松开（停止录音）
				// 文字状态
				voiceTv.setText("按住 说话");
				// 隐藏对话框
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
					ToastUtil.showToast(MChatBaseActivity.this, "录音时间太短");
//					showToast("录音时间太短");
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
						try {
							// 停止录音
							MChatBaseActivity.this.stopRecording();
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
								ToastUtil.showToast(MChatBaseActivity.this, "录音时间太短");
//								showToast("录音时间太短");
								return;
							} else { // 发送消息
								JTFile jtFile = new JTFile();
								jtFile.mFileName = mAudioName;
								jtFile.fileName = mAudioName;
								jtFile.mType = JTFile.TYPE_AUDIO;
								jtFile.mTaskId = TaskIDMaker.getTaskId(App.getUserID()); // 上传所需
								jtFile.mLocalFilePath = mAudioPath; // 本地文件路径
								jtFile.reserved2 = duration + ""; // 时长(单位:s)
								jtFile.mFileSize = new File(mAudioPath).length(); // 文件大小
								sendFile(jtFile); // 发送消息
							}
						} catch (IOException e) {
							Log.d(TAG, e.getMessage());
						}
					}
				}, 500);
				
				break;
			}
			return false;
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		// 注册下载广播监听器
		registerReceiver(broadcastReceiver, broadcastFilter);
		// 用来启动更新
		mAdapter.notifyDataSetChanged();
		// 隐藏分享框
		moreGrid.setVisibility(View.GONE);
		// 隐藏软键盘
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(textEt.getWindowToken(), 0);
		// 清空上传队列
		listUploader.clear();
		// 设置ImageLoader配置
		initImageLoaderConfiguration();
	}

	@Override
	public void onPause() {
		super.onPause();
		stopGet();
		// 取消下载监听器
		unregisterReceiver(broadcastReceiver);
		// 暂停所有文件上传
		stopUploadTask();
		// 回收录音和播放资源
		/*
		 * if (mRecorder != null) { mRecorder.release(); mRecorder = null; } if
		 * (mPlayer != null) { mPlayer.release(); mPlayer = null; }
		 */
		// 恢复ImageLoader默认配置
		recoverImageLoaderConfiguration();
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

	public void refreshList() {
		String MSG = "refreshList()";
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
		if (listView != null && mAdapter != null && mAdapter.getCount() > 0) {
			listView.setSelection(listView.getAdapter().getCount() - 1);
			Log.i(TAG, MSG + " listView.getAdapter().getCount() = " + listView.getAdapter().getCount());
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
			return;
		}
		super.onBackPressed();
	}

	public void startUserActivity(String userID) {
		KeelLog.d(TAG, "startUserActivity userID;" + userID);
		if (!TextUtils.isEmpty(userID)) {
			ENavigate.startUserDetailsActivity(this, userID, true, 0);
		}
	}

	// 获取JTFile的字符串类型
	protected String getJTFileTypeString(JTFile jtFile) {
		String typeStr = "未知";
		if (jtFile == null) {
			return typeStr;
		}
		switch (jtFile.mType) {
		case JTFile.TYPE_IMAGE:
			typeStr = "图片";
			break;
		case JTFile.TYPE_VIDEO:
			typeStr = "视频";
			break;
		case JTFile.TYPE_FILE:
			typeStr = "文件";
			break;
		case JTFile.TYPE_AUDIO:
			typeStr = "语音";
			break;
		default:
			typeStr = "文件";
			break;
		}
		return typeStr;
	}

	class ChatBaseAdapter extends BaseAdapter {

		// 删除该条聊天记录
		private void deleteItem(MeetingMessage msg, int position) {
			/*
			 * if (mAdapter.getData().size() > (position + 1)) {
			 * deleteCacheMessage
			 * (mAdapter.getData().get(position).getMessageID()); // 从数据库移除
			 * mAdapter.getData().remove(position); // 如果非最后一个，删除该节点 } else {
			 * msg.setHide(true); // 如果是最后一个,设置为不可见 updateCacheMessage(msg); //
			 * 更新数据库的数据 }
			 */
			int type = 0;
			switch (meetingDetail.getMeetingStatus()) {
			case 1:
				type = 4;
				break;
			case 2:
				type = 3;
				break;
			case 3:
				type = 5;
				break;
			}
			IMReqUtil.clientDeleteMessage(MChatBaseActivity.this, MChatBaseActivity.this, Long.valueOf(App.getUserID()), msg.getMessageID(), type, topicId, meetingId, Long.valueOf(msg.getSenderID()),
					0, mHandler);

			msg.setHide(true); // 设置为不可见
			updateCacheMessage(msg); // 更新数据库
			removeHideMessage(listMessage); // 移除不可见数据
			// mAdapter.notifyDataSetChanged(); // 更新列表
			// showToast("记录已删除");
		}

		private void showItemLongClickDialog(final MeetingMessage msg, final int position) {

			String[] listOper = null;

			// 处理长按事件
			switch (msg.getType()) {
			case IMBaseMessage.TYPE_TEXT: // 　文本
				listOper = new String[] { "复制", "转发/分享", "删除" };
				break;
			case IMBaseMessage.TYPE_IMAGE: // 图片
			case IMBaseMessage.TYPE_VIDEO: // 视频
			case IMBaseMessage.TYPE_FILE: // 文件
			case IMBaseMessage.TYPE_AUDIO: // 语音
				listOper = new String[] { "转发", "删除", "保存" };
				break;

			case IMBaseMessage.TYPE_KNOWLEDGE: // 知识
			case IMBaseMessage.TYPE_KNOWLEDGE2: // 新知识
				listOper = new String[] { "收藏", "转发/分享", "保存", "删除" };
				break;
			case IMBaseMessage.TYPE_REQUIREMENT: // 需求
				listOper = new String[] { "关注", "转发/分享", "删除" };
				break;
			case IMBaseMessage.TYPE_JTCONTACT_OFFLINE: // 关系
			case IMBaseMessage.TYPE_JTCONTACT_ONLINE:
			case IMBaseMessage.TYPE_ORG_OFFLINE:
			case IMBaseMessage.TYPE_ORG_ONLINE:
			case IMBaseMessage.TYPE_CUSTOMER:
				listOper = new String[] { "转发/分享", "删除" };
				break;
			case IMBaseMessage.TYPE_CONFERENCE: // 会议
				listOper = new String[] { "转发", "删除" };
			case IMBaseMessage.TYPE_COMMUNITY: //社群
				listOper = new String[] { "转发", "删除" };
				break;
			}
			// 显示弹出框
			new AlertDialog.Builder(mContext).setItems(listOper, new DialogInterface.OnClickListener() {

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 封装操作类型，0-复制文字；1-转发；2-转发/分享；3-收藏知识；4-关注需求；5-保存知识；6-删除；7-对图片、视频、音频、文件操作
					int operationType = msg.getMeetingMessageOperationTypeByDialogPosition(which);
					switch (operationType) {
					case 0:
						((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setText(msg.getContent());
						showToast("已复制");
						break;
					case 1:
						if (msg.getType() == IMBaseMessage.TYPE_TEXT) {
							msg.getJtFile().mFileName = msg.getContent();
							msg.getJtFile().mType = JTFile.TYPE_TEXT;
						}
						ENavigate.startSocialShareActivity(MChatBaseActivity.this, msg.getJtFile());
						break;
					case 2:
						FrameWorkUtils.showSharePopupWindow2(MChatBaseActivity.this, msg.getJtFile());
						break;
					case 3:
						// 收藏知识
						if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 收藏旧知识
							showLoadingDialog();
							cacheUrl = "";
							CommonReqUtil.doFetchExternalKnowledgeUrl(MChatBaseActivity.this, MChatBaseActivity.this, msg.getJtFile().mUrl, true, null);
						} else if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 收藏新知识
							showLoadingDialog();
							KnowledgeReqUtil.doUpdateCollectKnowledge(MChatBaseActivity.this, MChatBaseActivity.this, Long.parseLong(msg.getJtFile().mTaskId),
									Integer.parseInt(msg.getJtFile().reserved1), "", null);
						}
						break;
					case 4:
						// 关注需求
						showLoadingDialog();
						UserReqUtil.doFocusRequirement(MChatBaseActivity.this, MChatBaseActivity.this, UserReqUtil.getDoFocusRequirementParams(msg.getJtFile().mTaskId, true), null);

						break;
					case 5:
						// 保存知识
						if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 保存旧知识
							ENavigate.startCreateKnowledgeActivity(MChatBaseActivity.this, true, msg.getJtFile().mUrl,false);
						} else if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 保存新知识
							showLoadingDialog();
							KnowledgeReqUtil.doGetKnoDetailsBySaveKno(MChatBaseActivity.this, MChatBaseActivity.this, Long.parseLong(msg.getJtFile().mTaskId),
									Integer.parseInt(msg.getJtFile().reserved1), null);
						}
						break;
					case 6:
						// 删除
						deleteItem(msg, position);
						break;
					case 7:
						try {
							String fileIds;
							fileIds = Util.getDownloadIdByUrl(messageList.get(position).getJtFile().getmUrl());
							ENavigate.startFileManagementActivity(MChatBaseActivity.this, fileIds);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}).create().show();
		}

		private Context mContext;
		private LayoutInflater inflater = null;
		private List<MeetingMessage> messageList;
		private SmileyParser parser;
		private SmileyParser2 parser2;
		private Bitmap netImage;
		private Bitmap bitmap;
		private Bitmap bmpOriginal;
		private ImageLoader imageLoader = ImageLoader.getInstance();
		/** 图片视频的缩放大小（数值按照dp的标准进行等比例放大或者缩小） */
		private static final int SCALESIZE = 200;

		public ChatBaseAdapter(Context context) {
			this.mContext = context;
			inflater = LayoutInflater.from(context);
			parser = SmileyParser.getInstance(context);
			parser2 = SmileyParser2.getInstance(context);
		}

		public void setData(List<MeetingMessage> messageList) {
			this.messageList = messageList;
			this.notifyDataSetChanged();
		}

		/**
		 * 返回消息列表
		 * 
		 * @return
		 */
		public List<MeetingMessage> getData() {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (messageList != null && position < messageList.size()) {
				final MeetingMessage item = messageList.get(position);
				final int f_position = position;
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.hy_im_chat_message_item, parent, false);
					holder = new ViewHolder();
					holder.viewBG = (View) convertView.findViewById(R.id.im_message_bg);
					holder.leftLayout = (LinearLayout) convertView.findViewById(R.id.left_letter_layout);
					holder.rightLayout = (RelativeLayout) convertView.findViewById(R.id.right_letter_layout);

					holder.leftContent = (TextView) convertView.findViewById(R.id.left_letter_content);
					holder.rightContent = (TextView) convertView.findViewById(R.id.right_letter_content);

					holder.leftHead = (CircleImageView) convertView.findViewById(R.id.head);
					holder.rightHead = (CircleImageView) convertView.findViewById(R.id.right_head);
					holder.leftName = (TextView) convertView.findViewById(R.id.leftuserName);
					holder.rightName = (TextView) convertView.findViewById(R.id.rightuserName);

					holder.letterDate = (TextView) convertView.findViewById(R.id.letter_date);
					holder.systemMessage = (TextView) convertView.findViewById(R.id.system_message);
					holder.sendMsgProgress = (ProgressBar) convertView.findViewById(R.id.send_message_progress);
					holder.sendMsgFail = (ImageView) convertView.findViewById(R.id.send_message_fail);

					// 语音部分
					holder.leftVoiceLl = (LinearLayout) convertView.findViewById(R.id.leftVoiceLl);
					holder.leftVoiceDurationLl = (LinearLayout) convertView.findViewById(R.id.leftVoiceDurationLl);
					holder.leftVoiceIv = (ImageView) convertView.findViewById(R.id.leftVoiceIv);
					holder.leftVoiceDurationTv = (TextView) convertView.findViewById(R.id.leftVoiceDurationTv);
					holder.leftVoiceLoadingPb = (ProgressBar) convertView.findViewById(R.id.leftVoiceLoadingPb);

					holder.rightVoiceLl = (LinearLayout) convertView.findViewById(R.id.rightVoiceLl);
					holder.rightVoiceDurationLl = (LinearLayout) convertView.findViewById(R.id.rightVoiceDurationLl);
					holder.rightVoiceLoadingPb = (ProgressBar) convertView.findViewById(R.id.rightVoiceLoadingPb);
					holder.rightVoiceDurationTv = (TextView) convertView.findViewById(R.id.rightVoiceDurationTv);
					holder.rightVoiceIv = (ImageView) convertView.findViewById(R.id.rightVoiceIv);

					// 图片部分
					holder.leftImageRl = (RelativeLayout) convertView.findViewById(R.id.leftImageRl);
					holder.leftImageIv = (ImageView) convertView.findViewById(R.id.leftImageIv);

					holder.rightImageRl = (RelativeLayout) convertView.findViewById(R.id.rightImageRl);
					holder.rightImageIv = (ImageView) convertView.findViewById(R.id.rightImageIv);

					// 视频部分
					holder.leftVideoRl = (RelativeLayout) convertView.findViewById(R.id.leftVideoRl);
					holder.leftVideoIv = (ImageView) convertView.findViewById(R.id.leftVideoIv);
					holder.leftPlayIv = (ImageView) convertView.findViewById(R.id.leftPlayIv);

					holder.rightVideoRl = (RelativeLayout) convertView.findViewById(R.id.rightVideoRl);
					holder.rightVideoIv = (ImageView) convertView.findViewById(R.id.rightVideoIv);
					holder.rightPlayIv = (ImageView) convertView.findViewById(R.id.rightPlayIv);

					// 分享知识、需求
					holder.leftShareLl = (LinearLayout) convertView.findViewById(R.id.leftShareLl);
					holder.leftShareLinkIv = (TextView) convertView.findViewById(R.id.leftShareLinkIv);
					holder.leftShareTopLl = (LinearLayout) convertView.findViewById(R.id.leftShareTopLl);
					holder.leftShareMsgTv = (TextView) convertView.findViewById(R.id.leftShareMsgTv);
					holder.leftShareBottomLl = (LinearLayout) convertView.findViewById(R.id.leftShareBottomLl);
					holder.leftShareImageIv = (ImageView) convertView.findViewById(R.id.leftShareImageIv);
					holder.leftShareTypeIv = (ImageView) convertView.findViewById(R.id.leftShareTypeIv);
					holder.leftShareTitleTv = (TextView) convertView.findViewById(R.id.leftShareTitleTv);
					holder.leftShareContentTv = (TextView) convertView.findViewById(R.id.leftShareContentTv);

					holder.rightShareMsgTv = (TextView) convertView.findViewById(R.id.rightShareMsgTv);
					holder.rightShareBottomLl = (LinearLayout) convertView.findViewById(R.id.rightShareBottomLl);
					holder.rightShareTopLl = (LinearLayout) convertView.findViewById(R.id.rightShareTopLl);
					holder.rightShareLl = (LinearLayout) convertView.findViewById(R.id.rightShareLl);
					holder.rightShareImageIv = (ImageView) convertView.findViewById(R.id.rightShareImageIv);
					holder.rightShareTypeIv = (ImageView) convertView.findViewById(R.id.rightShareTypeIv);
					holder.rightShareTitleTv = (TextView) convertView.findViewById(R.id.rightShareTitleTv);
					holder.rightShareContentTv = (TextView) convertView.findViewById(R.id.rightShareContentTv);
					holder.rightShareLinkIv = (TextView) convertView.findViewById(R.id.rightShareLinkIv);

					// 分享文件部分
					holder.leftFileLl = (LinearLayout) convertView.findViewById(R.id.leftFileLl);
					holder.leftFileTypeIv = (ImageView) convertView.findViewById(R.id.leftFileTypeIv);
					holder.leftFileNameTv = (TextView) convertView.findViewById(R.id.leftFileNameTv);
					holder.leftFileSizeTv = (TextView) convertView.findViewById(R.id.leftFileSizeTv);
					holder.leftFileStatusTv = (TextView) convertView.findViewById(R.id.leftFileStatusTv);
					holder.leftFileProgressPb = (ProgressBar) convertView.findViewById(R.id.leftFileProgressPb);

					holder.rightFileLl = (LinearLayout) convertView.findViewById(R.id.rightFileLl);
					holder.rightFileTypeIv = (ImageView) convertView.findViewById(R.id.rightFileTypeIv);
					holder.rightFileNameTv = (TextView) convertView.findViewById(R.id.rightFileNameTv);
					holder.rightFileSizeTv = (TextView) convertView.findViewById(R.id.rightFileSizeTv);
					holder.rightFileStatusTv = (TextView) convertView.findViewById(R.id.rightFileStatusTv);
					holder.rightFileProgressPb = (ProgressBar) convertView.findViewById(R.id.rightFileProgressPb);

					holder.rightCnsRl = (RelativeLayout) convertView.findViewById(R.id.rightCnsRl);
					holder.rightCnsIcon = (ImageView) convertView.findViewById(R.id.rightCnsIcon);
					holder.rightCnsTitle = (TextView) convertView.findViewById(R.id.rightCnsTitle);
					holder.rightCnsName1 = (TextView) convertView.findViewById(R.id.rightCnsName1);
					holder.rightCnsName2 = (TextView) convertView.findViewById(R.id.rightCnsName2);
					holder.rightCnsName3 = (TextView) convertView.findViewById(R.id.rightCnsName3);

					holder.leftCnsRl = (RelativeLayout) convertView.findViewById(R.id.leftCnsRl);
					holder.leftCnsIcon = (ImageView) convertView.findViewById(R.id.leftCnsIcon);
					holder.leftCnsTitle = (TextView) convertView.findViewById(R.id.leftCnsTitle);
					holder.leftCnsName1 = (TextView) convertView.findViewById(R.id.leftCnsName1);
					holder.leftCnsName2 = (TextView) convertView.findViewById(R.id.leftCnsName2);
					holder.leftCnsName3 = (TextView) convertView.findViewById(R.id.leftCnsName3);

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
				// 记录是否隐藏
				if (item.isHide()) { // 隐藏
					convertView.setVisibility(View.GONE);
					holder.viewBG.setVisibility(View.GONE);
					holder.leftLayout.setVisibility(View.GONE);
					holder.rightLayout.setVisibility(View.GONE);
					holder.systemMessage.setVisibility(View.GONE);
					holder.letterDate.setVisibility(View.GONE);
					return convertView;
				} else { // 显示
					holder.viewBG.setVisibility(View.VISIBLE);
					convertView.setVisibility(View.VISIBLE);
				}

				// 长按事件
				OnLongClickListener longClickListener = new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						showItemLongClickDialog(item, f_position);
						return true;
					}
				};

				// 点击事件
				OnClickListener clickListener = new OnClickListener() {

					private boolean isFormIOS = false;

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.head: // 详情
						case R.id.right_head:
							// startUserActivity(item.getSenderID());
							ENavigate.startRelationHomeActivity(MChatBaseActivity.this, item.getSenderID(), true, ENavConsts.type_details_other);
							break;
						case R.id.send_message_fail: // 重发
							new AlertDialog.Builder(MChatBaseActivity.this).setTitle(R.string.str_again_send_title).setMessage(R.string.str_again_send_hint)
									.setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											item.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
											resendMessage(item);
										}
									}).setNegativeButton(R.string.str_cancel, null).create().show();
							break;
						case R.id.leftShareLl: // 左侧需求、左侧知识、左侧会议

						case R.id.rightShareLl:// 右侧需求、右侧知识、右侧会议
							if (item.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 需求
								// ENavigate.startRequirementDetailActivity(MChatBaseActivity.this,
								// Integer.parseInt(item.getJtFile().mTaskId));
								ENavigate.startNeedDetailsActivity(MChatBaseActivity.this, item.getJtFile().mTaskId, 2);
							} else if (item.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 知识
								if(!TextUtils.isEmpty(item.getJtFile().mTaskId) && !TextUtils.isEmpty(item.getJtFile().getReserved1())){
									ENavigate.startKnowledgeOfDetailActivity(MChatBaseActivity.this, Long.parseLong(item.getJtFile().mTaskId),
											Integer.parseInt(item.getJtFile().getReserved1()));
								}else{
									//第三方分享到社交的知识
									ENavigate.startShareDetailActivity(MChatBaseActivity.this, item.getJtFile().toKnowledgeMini());
								}
							} else if (item.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 新知识
								ENavigate.startKnowledgeOfDetailActivity(MChatBaseActivity.this, Long.parseLong(item.getJtFile().mTaskId), Integer.parseInt(item.getJtFile().getReserved1()));
							} else if (item.getType() == IMBaseMessage.TYPE_CONFERENCE) { // 会议
								ENavigate.startSquareActivity(mContext, Long.parseLong(item.getJtFile().mTaskId), 0);
							}else if (item.getType() == IMBaseMessage.TYPE_COMMUNITY) { // 社群
								ENavigate.startCommunityDetailsActivity(mContext, Long.valueOf(item.getJtFile().mTaskId), false);
							}
							break;
						case R.id.leftPlayIv: // 左侧视频
							if (v.getTag() == null) {
								ENavigate.startMChatFilePreviewActivity(mContext, item.getJtFile(), item.getMessageID(), meetingId, topicId);
							} else {
								OpenFiles.open(mContext, v.getTag() + ""); // 打开视频
							}
							break;
						case R.id.rightPlayIv: // 右侧视频
							break;
						case R.id.leftFileLl: // 左侧文件
						case R.id.rightFileLl: // 右侧文件
							if (v.getTag() == null) {
								ENavigate.startMChatFilePreviewActivity(mContext, item.getJtFile(), item.getMessageID(), meetingId, topicId);
							} else {
								OpenFiles.open(mContext, v.getTag() + ""); // 打开文件
							}
							break;
						case R.id.leftCnsRl: // 左侧关系
						case R.id.rightCnsRl: // 右侧关系
							boolean isOnline = false;
							String id = item.getJtFile().mTaskId;
							boolean isOrg = false;
							if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType()) { // 人脉
								isOrg = false;
								isOnline = false;
							} else if (IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) { // 用户
								isOrg = false;
								isOnline = true;
							} else if (IMBaseMessage.TYPE_ORG_OFFLINE == item.getType() || IMBaseMessage.TYPE_CUSTOMER == item.getType()) {
								isOrg = true;
								isOnline = false;
							} else if (IMBaseMessage.TYPE_ORG_ONLINE == item.getType()) {
								isOrg = true;
								isOnline = true;
							} else if (IMBaseMessage.TYPE_ORGANIZATION == item.getType()) {
								isOrg = true;
								isOnline = true; // 从IOS过来的是useid
							}
							if (isOrg) {
								if (isOnline) { // 组织
									ENavigate.startOrgMyHomePageActivityByUseId(MChatBaseActivity.this, Long.parseLong(id));
								} else if (isFormIOS) {
									ENavigate.startOrgMyHomePageActivity(MChatBaseActivity.this, Long.parseLong(id), 0, true, 1);
								} else { // 客户
									ENavigate.startClientDedailsActivity(MChatBaseActivity.this, Long.parseLong(id), 2, 6);
								}
							} else {
								if (isOnline) { // 用户
									ENavigate.startRelationHomeActivity(MChatBaseActivity.this, id, isOnline, ENavConsts.type_details_other);
								} else { // 人脉
									ENavigate.startRelationHomeActivity(MChatBaseActivity.this, id, isOnline, ENavConsts.TYPE_CONNECTIONS_HOME_PAGE);
//									ENavigate.startContactsDetailsActivity(MChatBaseActivity.this, 2, Long.valueOf(id), 0, 1);
								}
							}
							break;
						case R.id.leftImageRl: // 左侧图片
						case R.id.rightImageRl: // 右侧图片
							ENavigate.startHyImageBrowserActivity(MChatBaseActivity.this, meetingId, topicId, item.getMessageID());
							break;
						}
					}
				};

				// 头像点击
				holder.leftHead.setOnClickListener(clickListener);
				holder.rightHead.setOnClickListener(clickListener);

				// 初始化列表内容
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

						holder.leftLayout.setVisibility(View.GONE);
						holder.systemMessage.setVisibility(View.GONE);
						holder.rightLayout.setVisibility(View.VISIBLE);
						
//						String lastchar = "";
//						if(!TextUtils.isEmpty(item.getSenderName())){
//							lastchar = item.getSenderName().substring(item.getSenderName().length()-1);
//						}

						com.utils.common.Util.initAvatarImage(mContext, holder.rightHead, item.getSenderName(), App.getUser().getImage(), 0, App.getUser().mUserType);
						
						/*String picPath = App.getUser().getImage();
						if (!StringUtils.isEmpty(picPath)&&!picPath.endsWith(GlobalVariable.PERSON_DEFAULT_AVATAR)&&!picPath.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR)) {
							DisplayImageOptions mDefaultHeadOption =  LoadImage.mDefaultHead;
							if (App.getUser().mUserType == JTMember.UT_ORGANIZATION) {
								mDefaultHeadOption = LoadImage.mOrganizationDefaultHead;
							}
							imageLoader.displayImage(App.getUser().getImage(), holder.rightHead, mDefaultHeadOption);
						}else {
							Bitmap bm = null;
							int resid = R.drawable.ic_person_default_avatar_gray;
							if (App.getUser().getmUserType() == JTMember.UT_ORGANIZATION) {
								resid = R.drawable.no_avatar_client_organization;
							}
							bm = com.utils.common.Util.createBGBItmap(mContext, resid, R.color.avatar_text_color, R.dimen.avatar_text_size, lastchar);
							holder.rightHead.setImageBitmap(bm);
						}*/
						
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

								String originalFilePath = localFileDBManager.query(App.getUserID(), item.getMessageID());
								File originalFile = null;
								if (!TextUtils.isEmpty(originalFilePath)) {
									originalFile = new File(originalFilePath);
								}
								if (originalFile != null && originalFile.exists()) { // 本地图片存在
									imageLoader.displayImage(Uri.fromFile(originalFile).toString(), holder.rightImageIv);
								} else if (!TextUtils.isEmpty(item.getJtFile().mUrl)) { // 图片已上传
									imageLoader.displayImage(item.getJtFile().mUrl, holder.rightImageIv);
								} else { // 图片不存在
									holder.rightImageIv.setImageResource(R.drawable.hy_chat_right_pic);
								}
							}
							// 长按事件
							holder.rightImageRl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightImageRl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_AUDIO == item.getType()) { // 右侧语音

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

								// 语音时长
								holder.rightVoiceDurationTv.setText(item.getJtFile().reserved2 + "\"");
								// 控件长度
								int paramsWidth = 50 + (StringUtils.isEmpty(item.getJtFile().reserved2) ? 0 : Integer.parseInt(item.getJtFile().reserved2) * 5);
								// 最大不超过200dp
								paramsWidth = Math.min(paramsWidth, 200);
								// 设置长度
								LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.rightVoiceDurationLl.getLayoutParams();
								layoutParams.width = EUtil.convertDpToPx(paramsWidth);
								holder.rightVoiceDurationLl.setLayoutParams(layoutParams);
								// 语音文件
								final File file = new File(EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_AUDIO, meetingId, topicId), item.getJtFile().mFileName);
								// 设置状态
								if (file.exists() && file.length() == item.getJtFile().mFileSize) {
									holder.rightVoiceLoadingPb.setVisibility(View.GONE);
									holder.rightVoiceIv.setVisibility(View.VISIBLE);
								} else if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									long taskId = voiceFileManager.query(item.getJtFile().mUrl);
									if (taskId >= 0) {
										Query query = new Query().setFilterById(taskId);
										Cursor cursor = null;
										try {
											cursor = downloadManager.query(query);
											if (cursor != null) {
												int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
												switch (status) {
												case DownloadManager.STATUS_PENDING:
												case DownloadManager.STATUS_RUNNING:
													holder.rightVoiceLoadingPb.setVisibility(View.VISIBLE);
													holder.rightVoiceIv.setVisibility(View.GONE);
													break;
												default:
													holder.rightVoiceLoadingPb.setVisibility(View.GONE);
													holder.rightVoiceIv.setVisibility(View.VISIBLE);
													break;
												}
											}
										} catch (Exception e) {
											if (cursor != null) {
												cursor.close();
											}
											holder.rightVoiceLoadingPb.setVisibility(View.GONE);
											holder.rightVoiceIv.setVisibility(View.VISIBLE);
										}
									} else {
										holder.rightVoiceLoadingPb.setVisibility(View.GONE);
										holder.rightVoiceIv.setVisibility(View.VISIBLE);
									}
								} else {
									holder.rightVoiceLoadingPb.setVisibility(View.GONE);
									holder.rightVoiceIv.setVisibility(View.VISIBLE);
								}
								// 点击事件
								holder.rightVoiceLl.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {

										// 正在播放音频的url
										curVoiceUrl = item.getJtFile().mUrl;

										if (file.exists() && file.length() == item.getJtFile().mFileSize) {
											// 开始播放音频和动画
											startPlaying(file.getAbsolutePath(), holder.rightVoiceIv, Integer.parseInt(item.getJtFile().reserved2) * 1000);
										} else if (item.getJtFile().mUrl != null && item.getJtFile().mUrl.length() > 0) { // 文件已上传

											long taskId = voiceFileManager.query(item.getJtFile().mUrl);
											if (taskId < 0) { // 从未开始任务

												File dir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_AUDIO, meetingId, topicId);
												if (dir != null) {
													Request request = new Request(Uri.parse(item.getJtFile().mUrl));
													request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
													request.setDestinationUri(Uri.fromFile(new File(dir, item.getJtFile().mFileName))); // 设置文件下载位置
													voiceFileManager.insert(item.getJtFile().mUrl, downloadManager.enqueue(request));
													// 界面修改
													holder.rightVoiceLoadingPb.setVisibility(View.VISIBLE);
													holder.rightVoiceIv.setVisibility(View.GONE);
												} else {
													showToast("没有SD卡，无法下载语音文件");
												}
											} else {
												Query query = new Query().setFilterById(taskId);
												Cursor cursor = null;
												try {
													cursor = downloadManager.query(query);
													if (cursor != null) {
														int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
														switch (status) {
														case DownloadManager.STATUS_PENDING:
														case DownloadManager.STATUS_RUNNING:
															holder.rightVoiceLoadingPb.setVisibility(View.VISIBLE);
															holder.rightVoiceIv.setVisibility(View.GONE);
															break;
														default:
															// 删除旧任务
															downloadManager.remove(taskId);
															voiceFileManager.delete(taskId);
															// 开始新任务
															File dir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_AUDIO, meetingDetail.getId(), topicDetail.getId());
															if (dir != null) {
																Request request = new Request(Uri.parse(item.getJtFile().mUrl));
																request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
																request.setDestinationUri(Uri.fromFile(new File(dir, item.getJtFile().mFileName))); // 设置文件下载位置
																voiceFileManager.insert(item.getJtFile().mUrl, downloadManager.enqueue(request));
																// 界面修改
																holder.rightVoiceLoadingPb.setVisibility(View.VISIBLE);
																holder.rightVoiceIv.setVisibility(View.GONE);
															} else {
																showToast("没有SD卡，无法下载语音文件");
															}
															break;
														}
													}
												} catch (Exception e) {
													if (cursor != null) {
														cursor.close();
													}
													holder.rightVoiceLoadingPb.setVisibility(View.GONE);
													holder.rightVoiceIv.setVisibility(View.VISIBLE);
												}
											}
										} else {
											file.delete(); // 删除不完整文件
											showToast("语音文件不存在");
										}
									}
								});
								// 长按事件
								holder.rightVoiceLl.setOnLongClickListener(longClickListener);
							}
						} else if (IMBaseMessage.TYPE_KNOWLEDGE == item.getType()) { // 右侧知识

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {

//								String typeStr = "[知识]";
//								if (item.getContent().equals(typeStr) || StringUtils.isEmpty(item.getContent())) {
//									holder.rightShareTopLl.setVisibility(View.GONE);
//									holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_content_white);
//								} else {
									holder.rightShareTopLl.setVisibility(View.VISIBLE);
									holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
									// 消息内容
									holder.rightShareMsgTv.setText("分享了[知识]");
//								}
								// 截图
								holder.rightShareImageIv.setVisibility(View.GONE);
								// 类型
								holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);
								// 标题
								holder.rightShareTitleTv.setVisibility(View.GONE);
								// 内容
								int httpIndex = item.getJtFile().mSuffixName.indexOf("http");
								if (!StringUtils.isEmpty(item.getJtFile().mSuffixName) || !StringUtils.isEmpty(item.getJtFile().mFileName)) {
									if (httpIndex < 0) {
										holder.rightShareContentTv.setText(!TextUtils.isEmpty(item.getJtFile().mFileName) ? item.getJtFile().mFileName : item.getJtFile().mSuffixName);
									} else if (httpIndex == 0) {
										holder.rightShareContentTv.setText("");
									} else {
										holder.rightShareContentTv.setText(!TextUtils.isEmpty(item.getJtFile().mFileName) ? item.getJtFile().mFileName : item.getJtFile().mSuffixName);
									}
									holder.rightShareContentTv.setVisibility(View.VISIBLE);
								} else {
									holder.rightShareContentTv.setVisibility(View.GONE);
								}
								// 链接
								if (!StringUtils.isEmpty(item.getJtFile().mUrl)) {
									if (item.getJtFile().mUrl.startsWith("http://mp.weixin.qq.com/")) { // 微信分享
										// holder.rightShareLinkIv.setImageResource(R.drawable.chat_link_weixin);
										holder.rightShareLinkIv.setText("来自微信");
									} else {
										// holder.rightShareLinkIv.setImageResource(R.drawable.chat_link_normal);
										holder.rightShareLinkIv.setText("来自网页");
									}
									holder.rightShareLinkIv.setVisibility(View.VISIBLE);
								} else {
									holder.rightShareLinkIv.setVisibility(View.GONE);
								}
							}
							// 长按事件
							holder.rightShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightShareLl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_KNOWLEDGE2 == item.getType()) { // 右侧新知识

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
//								String typeStr = "[知识]";
//								if (TextUtils.isEmpty(item.getContent()) || item.getContent().equals(typeStr)) {
//									holder.rightShareTopLl.setVisibility(View.GONE);
//									holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_content_white);
//								} else {
									holder.rightShareMsgTv.setText("分享了[知识]");
									holder.rightShareTopLl.setVisibility(View.VISIBLE);
									holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
//								}
								// 图片
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									imageLoader.displayImage(item.getJtFile().mUrl, holder.rightShareImageIv, LoadImage.mKnowledgeDefaultImage);

								} else {
									holder.rightShareImageIv.setVisibility(View.GONE);
								}
								// 类型
								holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);
								// 标题
								holder.rightShareTitleTv.setText("\u3000\u3000" + item.getJtFile().reserved2);
								holder.rightShareTitleTv.setVisibility(View.VISIBLE);
								// 内容
								if (!StringUtils.isEmpty(item.getJtFile().mSuffixName) || !StringUtils.isEmpty(item.getJtFile().mFileName)) {
									holder.rightShareContentTv.setText(!TextUtils.isEmpty(item.getJtFile().mFileName) ? item.getJtFile().mFileName : item.getJtFile().mSuffixName);
									holder.rightShareContentTv.setVisibility(View.VISIBLE);
								} else {
									holder.rightShareContentTv.setVisibility(View.GONE);

								}
								// 链接(判断来源)
								if (TextUtils.isEmpty(item.getJtFile().reserved3)) {
									holder.rightShareLinkIv.setVisibility(View.GONE);
								} else {
									if (item.getJtFile().reserved3.startsWith("http://mp.weixin.qq.com/")) {
										holder.rightShareLinkIv.setText("来自微信");
									} else {
										holder.rightShareLinkIv.setText("来自网页");
									}
									holder.rightShareLinkIv.setVisibility(View.VISIBLE);
								}
							}
							// 长按事件
							holder.rightShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightShareLl.setOnClickListener(clickListener);
						}  else if (IMBaseMessage.TYPE_COMMUNITY == item.getType()) { // 右侧社群

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
//								String typeStr = "[社群]";
//								if (TextUtils.isEmpty(item.getContent()) || item.getContent().equals(typeStr)) {
//									holder.rightShareTopLl.setVisibility(View.GONE);
//									holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_content_white);
//								} else {
									holder.rightShareMsgTv.setText("分享了[社群]");
									holder.rightShareTopLl.setVisibility(View.VISIBLE);
									holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
//								}
								// 图片
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									imageLoader.displayImage(item.getJtFile().mUrl, holder.rightShareImageIv, LoadImage.mKnowledgeDefaultImage);
								} else {
									holder.rightShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
								}
								// 类型
								holder.rightShareTypeIv.setVisibility(View.GONE);//setImageResource(R.drawable.hy_chat_type_knowledge);
								// 标题
								holder.rightShareTitleTv.setText(item.getJtFile().mFileName);
								holder.rightShareTitleTv.setVisibility(View.VISIBLE);
								// 内容
								if (!StringUtils.isEmpty(item.getJtFile().mSuffixName) ) {
									holder.rightShareContentTv.setText(item.getJtFile().mSuffixName);
									holder.rightShareContentTv.setVisibility(View.VISIBLE);
								} else {
									holder.rightShareContentTv.setVisibility(View.GONE);

								}
							}
							// 链接(判断来源)
							if (TextUtils.isEmpty(item.getJtFile().reserved3)) 
								holder.rightShareLinkIv.setVisibility(View.GONE);
							// 长按事件
							holder.rightShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightShareLl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_CONFERENCE == item.getType()) { // 右侧会议

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
//								String typeStr = "[会议]";
//								if (item.getContent().equals(typeStr) || TextUtils.isEmpty(item.getContent())) {
//									holder.rightShareTopLl.setVisibility(View.GONE);
//									holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_content_white);
//								} else {
									holder.rightShareMsgTv.setText("分享了[会议]");
									holder.rightShareTopLl.setVisibility(View.VISIBLE);
									holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
//								}
								// 图片
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									imageLoader.displayImage(item.getJtFile().mUrl, holder.rightShareImageIv);
								}else{
									holder.rightShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
								}
								holder.rightShareImageIv.setVisibility(View.VISIBLE);
								// 类型
								holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_conference);
								// 标题
								holder.rightShareTitleTv.setText("\u3000\u3000" + item.getJtFile().mFileName);
								holder.rightShareTitleTv.setVisibility(View.VISIBLE);
								// 内容
								if (!StringUtils.isEmpty(item.getJtFile().reserved1)) {
									holder.rightShareContentTv.setText(item.getJtFile().reserved1);
									holder.rightShareContentTv.setVisibility(View.VISIBLE);
								} else {

									holder.rightShareContentTv.setVisibility(View.GONE);
								}
								// 链接
								holder.rightShareLinkIv.setVisibility(View.GONE);
							}
							// 长按事件
							holder.rightShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightShareLl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType() || IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType() || IMBaseMessage.TYPE_ORG_OFFLINE == item.getType()
								|| IMBaseMessage.TYPE_ORG_ONLINE == item.getType() || IMBaseMessage.TYPE_CUSTOMER == item.getType() || item.getType() == IMBaseMessage.TYPE_ORGANIZATION) { // 右侧关系

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
								if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType() || IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
									if (!StringUtils.isEmpty(jtFile.mFileName)) {
										// holder.rightCnsTitle.setText(jtFile.mFileName);
										holder.rightCnsName1.setText(jtFile.mFileName);
										holder.rightCnsName1.setVisibility(View.VISIBLE);
									}
									if (!StringUtils.isEmpty(jtFile.mSuffixName)) {
										holder.rightCnsName2.setText(jtFile.mSuffixName);
										holder.rightCnsName2.setVisibility(View.VISIBLE);
									}
									if (!StringUtils.isEmpty(jtFile.reserved1)) {
										holder.rightCnsName3.setText(jtFile.reserved1);
										holder.rightCnsName3.setVisibility(View.VISIBLE);
									}
									imageLoader.displayImage(jtFile.mUrl, holder.rightCnsIcon, LoadImage.TYPE_IMAGE_connection80obj);
								}
								// 组织
								else {
									holder.rightCnsName1.setText(!TextUtils.isEmpty(item.getJtFile().mFileName) ? item.getJtFile().mFileName : item.getJtFile().mSuffixName);
									holder.rightCnsName1.setVisibility(View.VISIBLE);
									if (!StringUtils.isEmpty(jtFile.reserved1)) {
										holder.rightCnsName2.setText(jtFile.reserved1);
										holder.rightCnsName2.setVisibility(View.VISIBLE);
									}
									imageLoader.displayImage(jtFile.mUrl, holder.rightCnsIcon, LoadImage.TYPE_IMAGE_companyfriend80Obj);

								}
								if (jtFile.mUrl == null) {
									jtFile.mUrl = "";
								}
							}
							// 长按事件
							holder.rightCnsRl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightCnsRl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_REQUIREMENT == item.getType()) { // 右侧需求

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.VISIBLE);

							// 初始化数据
							if (item.getJtFile() != null) {

//								String typeStr = "[需求]";
//								if (item.getContent().equals(typeStr.trim()) || StringUtils.isEmpty(item.getContent())) {
//									holder.rightShareTopLl.setVisibility(View.GONE);
//									holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_content_white);
//								} else {
									holder.rightShareTopLl.setVisibility(View.VISIBLE);
									holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
									holder.rightShareMsgTv.setText("分享了[需求]");
//								}
								// 图片
								holder.rightShareImageIv.setVisibility(View.GONE);
								// 类型
								holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_requirement);
								// 标题
								holder.rightShareTitleTv.setText("\u3000\u3000" + item.getJtFile().mFileName);
								holder.rightShareTitleTv.setVisibility(View.VISIBLE);
								// 内容
								if (!StringUtils.isEmpty(item.getJtFile().reserved1)) {
									holder.rightShareContentTv.setText(item.getJtFile().reserved1);
									holder.rightShareContentTv.setVisibility(View.VISIBLE);
								} else {
									holder.rightShareContentTv.setVisibility(View.GONE);

								}
								// 链接
								holder.rightShareLinkIv.setVisibility(View.GONE);
							}
							// 长按事件
							holder.rightShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightShareLl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_VIDEO == item.getType()) { // 右侧视频

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
																MChatBaseActivity.this,
																FilePreviewActivity.class);
														intent.putExtra(
																EConsts.Key.JT_FILE,
																item.getJtFile());
														startActivity(intent);
													}
												});
									}
								}else {
									 // 视频文件未上传或上传中
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
																MChatBaseActivity.this,
																"视频源未找到", 0)
																.show();
													}
												}
											});
								
								}
//								// 首先检查原文件是否存在，其次是下载的文件
//								String originalFilePath = localFileDBManager.query(App.getUserID(), item.getMessageID());
//								File originalFile = null;
//								if (!TextUtils.isEmpty(originalFilePath)) {
//									originalFile = new File(originalFilePath);
//								}
//								if (originalFile == null || !originalFile.exists()) { // 原文件已不存在
//									if (!TextUtils.isEmpty(item.getJtFile().mUrl)) { // 文件已上传
//										File fileDir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_VIDEO, meetingId, topicId);
//										if (fileDir != null) {
//											File downloadFile = new File(fileDir, item.getJtFile().mFileName);
//											if (downloadFile.length() == item.getJtFile().mFileSize) { // 文件已下载完成，点击直接打开
//												holder.rightPlayIv.setTag(downloadFile.getAbsolutePath());
//											} else { // 文件尚未下载完，点击跳转到预览页
//												holder.rightPlayIv.setTag(null);
//											}
//										} else { // 文件尚未下载，点击跳转到预览页
//											holder.rightPlayIv.setTag(null);
//										}
//										// 显示缩略图
//										holder.rightVideoIv.setImageResource(R.drawable.hy_chat_right_pic);
//										imageLoader.displayImage(item.getJtFile().reserved2, holder.rightVideoIv);
//									} else { // 文件未上传
//										holder.rightVideoIv.setImageResource(R.drawable.hy_chat_right_pic);
//										holder.rightPlayIv.setTag(""); // 提示文件不存在
//									}
//								} else {}
								// 长按事件
								holder.rightVideoIv.setOnLongClickListener(longClickListener);
								// 点击事件
								holder.rightPlayIv.setOnClickListener(clickListener);
							}
							
							// 长按事件
							holder.rightVideoIv
									.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightVideoIv
									.setOnClickListener(clickListener);
							
						} else if (IMBaseMessage.TYPE_FILE == item.getType()) { // 右侧文件

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
								// 根据不同的后缀名设置不同的文件图片
								HomeCommonUtils.setFileIconAccoredSuffixName(holder.leftFileTypeIv, item.getJtFile().getmSuffixName());
								holder.rightFileNameTv.setText(item.getJtFile().mFileName); // 文件名
								holder.rightFileSizeTv.setText(EUtil.formatFileSize(item.getJtFile().mFileSize)); // 文件大小
								String fileStatus = ""; // 文件状态
								if (TextUtils.isEmpty(item.getJtFile().mUrl)) { // 文件未上传或正在上传

									if (isUploadTaskExist(item.getMessageID())
											&& (getUploadTaskStatus(item.getMessageID()) == HyChatFileUploader.Status.Prepared || getUploadTaskStatus(item.getMessageID()) == HyChatFileUploader.Status.Started)) {
										holder.rightFileProgressPb.setProgress(getUploadTaskProgress(item.getMessageID()));
										holder.rightFileProgressPb.setVisibility(View.VISIBLE);
									} else { // 文件不在上传队列
										holder.rightFileProgressPb.setVisibility(View.GONE);
									}
									fileStatus = "打开";
									holder.rightFileStatusTv.setText(fileStatus);
									String filePath = localFileDBManager.query(App.getUserID(), item.getMessageID());
									if (!TextUtils.isEmpty(filePath)) {
										holder.rightFileLl.setTag(filePath);
									} else {
										holder.rightFileLl.setTag("");
									}
								} else { // 文件已上传

									// 首先判断原文件，接着判断下载文件，接着判断下载任务
									String originalFilePath = localFileDBManager.query(App.getUserID(), item.getMessageID());
									File originalFile = null;
									if (!TextUtils.isEmpty(originalFilePath)) {
										originalFile = new File(originalFilePath);
									}
									if (originalFile == null || !originalFile.exists()) { // 原文件已不存在
										// 是否在下载队列中
										if (downloadService != null && downloadService.isTaskExist(item.getMessageID())) {
											holder.rightFileProgressPb.setProgress(downloadService.getTaskProgress(item.getMessageID()));
											holder.rightFileProgressPb.setVisibility(View.VISIBLE);
											int status = downloadService.getTaskStatus(item.getMessageID());
											if (status == HyChatFileDownloader.Status.Prepared || status == HyChatFileDownloader.Status.Started) { // 下载中
												fileStatus = "下载中";
											} else { // 已停止
												fileStatus = "继续下载";
											}
											holder.rightFileLl.setTag(null);
										} else { // 未下载
											File fileDir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_FILE, meetingId, topicId);
											if (fileDir != null) {
												File downloadFile = new File(fileDir, item.getJtFile().mFileName);
												if (downloadFile.length() == item.getJtFile().mFileSize) { // 文件已下载完成，点击直接打开
													fileStatus = "打开";
													holder.rightFileProgressPb.setVisibility(View.GONE);
													holder.rightFileLl.setTag(downloadFile.getAbsoluteFile());
												} else if (downloadFile.length() > 0) { // 文件尚未下载完，点击跳转到预览页
													holder.rightFileProgressPb.setProgress((int) (downloadFile.length() * 1.0 / item.getJtFile().mFileSize * 100));
													holder.rightFileProgressPb.setVisibility(View.VISIBLE);
													fileStatus = "继续下载";
													holder.rightFileLl.setTag(null);
												} else {
													fileStatus = "未下载";
													holder.rightFileProgressPb.setVisibility(View.GONE);
													holder.rightFileLl.setTag(null);
												}
											} else { // 文件尚未下载，点击跳转到预览页
												fileStatus = "未下载";
												holder.rightFileProgressPb.setVisibility(View.GONE);
												holder.rightFileLl.setTag(null);
											}
										}
									} else { // 原文件存在
										fileStatus = "打开";
										holder.rightFileProgressPb.setVisibility(View.GONE);
										holder.rightFileLl.setTag(originalFile.getAbsoluteFile());
									}
									// 设置文件状态
									holder.rightFileStatusTv.setText(fileStatus);
								}
							}
							// 长按事件
							holder.rightFileLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightFileLl.setOnClickListener(clickListener);
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
							if (item.getContent().startsWith("http://") || item.getContent().startsWith("https://")) {
								Linkify.addLinks(holder.rightContent, Linkify.WEB_URLS);
							}
							// 长按
							holder.rightContent.setOnLongClickListener(longClickListener);
						}
						// 发送失败的红色按钮处理
						if (item.getSendType() == IMBaseMessage.SEND_TYPE_FAIL) { // 发送失败
							holder.sendMsgProgress.setVisibility(View.GONE);
							holder.sendMsgFail.setVisibility(View.VISIBLE);
							holder.sendMsgFail.setOnClickListener(clickListener);
						} else if (item.getSendType() == IMBaseMessage.SEND_TYPE_SENDING) { // 发送中
							holder.sendMsgFail.setVisibility(View.GONE);
							holder.sendMsgProgress.setVisibility(View.VISIBLE);
						} else { // 发送成功
							holder.sendMsgFail.setVisibility(View.GONE);
							holder.sendMsgProgress.setVisibility(View.GONE);
						}
					} else { // 对方发送的内容

						holder.leftLayout.setVisibility(View.VISIBLE);
						holder.rightLayout.setVisibility(View.GONE);
						holder.systemMessage.setVisibility(View.GONE);
						
						
						com.utils.common.Util.initAvatarImage(mContext, holder.leftHead, item.getSenderName(), getImageByMessage(item), 0, 1);
						
						/*String lastchar = "";
						if(!TextUtils.isEmpty(item.getSenderName())){
							lastchar = item.getSenderName().substring(item.getSenderName().length()-1);
						}

						String picPath = getImageByMessage(item);
						if (!StringUtils.isEmpty(picPath)&&!picPath.endsWith(GlobalVariable.PERSON_DEFAULT_AVATAR)&&!picPath.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR)) {
							DisplayImageOptions mDefaultHeadOption =  LoadImage.mDefaultHead;
//							if (App.getUser().mUserType == JTMember.UT_ORGANIZATION) {
//								mDefaultHeadOption = LoadImage.mOrganizationDefaultHead;
//							}
							imageLoader.displayImage(picPath, holder.leftHead, mDefaultHeadOption);
						}else {
							Bitmap bm = null;
							int resid = R.drawable.ic_person_default_avatar_gray;
//							if (App.getUser().getmUserType() == JTMember.UT_ORGANIZATION) {
//								resid = R.drawable.no_avatar_client_organization;
//							}
							bm = com.utils.common.Util.createBGBItmap(mContext, resid, R.color.avatar_text_color, R.dimen.avatar_text_size, lastchar);
							holder.leftHead.setImageBitmap(bm);
						}*/
						
						holder.leftName.setText(getNickNameByMessage(item));

						if (IMBaseMessage.TYPE_TEXT == item.getType()) { // 左侧文本

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.VISIBLE);
							holder.leftContent.setOnLongClickListener(longClickListener);
							CharSequence dd = parser.addSmileySpans(body);
							// CharSequence dd1 = parser2.addSmileySpans(dd);
							holder.leftContent.setText(dd);

							if (item.getContent().startsWith("http://") || item.getContent().startsWith("https://")) {
								Linkify.addLinks(holder.leftContent, Linkify.WEB_URLS);
							}
						} else if (IMBaseMessage.TYPE_IMAGE == item.getType()) { // 左侧图片

							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftImageRl.setVisibility(View.VISIBLE);

							// 显示图片
							if (item.getJtFile() != null && !TextUtils.isEmpty(item.getJtFile().mUrl)) {
								imageLoader.displayImage(item.getJtFile().mUrl, holder.leftImageIv);
							} else {
								holder.leftImageIv.setImageResource(R.drawable.hy_chat_right_pic);
							}
							// 长按事件
							holder.leftImageRl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftImageRl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_KNOWLEDGE == item.getType()) { // 左侧知识

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);
							// 知识
//							String typeStr = "[知识]";
//							if (item.getContent().equals(typeStr) || StringUtils.isEmpty(item.getContent())) {
//								holder.leftShareTopLl.setVisibility(View.GONE);
//								holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang);
//							} else {
								holder.leftShareMsgTv.setText("分享了[知识]");
								holder.leftShareTopLl.setVisibility(View.VISIBLE);
								holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
//							}
							// 缩略图
							holder.leftShareImageIv.setVisibility(View.GONE);
							// 类型
							holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);
							// 标题
							holder.leftShareTitleTv.setVisibility(View.GONE);
							// 内容
							int httpIndex = item.getJtFile().mSuffixName.indexOf("http");
							if (!StringUtils.isEmpty(item.getJtFile().mSuffixName) || !StringUtils.isEmpty(item.getJtFile().mFileName)) {
								holder.leftShareContentTv.setVisibility(View.VISIBLE);
								if (httpIndex <= 0) {
									holder.leftShareContentTv.setText(!TextUtils.isEmpty(item.getJtFile().mFileName) ? item.getJtFile().mFileName : item.getJtFile().mSuffixName);
								} else {
									holder.leftShareContentTv.setText(!TextUtils.isEmpty(item.getJtFile().mFileName) ? item.getJtFile().mFileName : item.getJtFile().mSuffixName);
								}
							} else {
								holder.leftShareContentTv.setVisibility(View.GONE);
							}
							// 链接
							if (!StringUtils.isEmpty(item.getJtFile().mUrl)) {
								if (item.getJtFile().mUrl.startsWith("http://mp.weixin.qq.com/")) { // 微信分享
									// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_weixin);
									holder.leftShareLinkIv.setText("来自微信");
								} else {
									// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_normal);
									holder.leftShareLinkIv.setText("来自网页");
								}
								holder.leftShareLinkIv.setVisibility(View.VISIBLE);
							} else {
								holder.leftShareLinkIv.setVisibility(View.GONE);
							}
							// 内容
							holder.leftShareContentTv.setText(item.getJtFile().mUrl);
							// 长按事件
							holder.leftShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftShareLl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_KNOWLEDGE2 == item.getType()) { // 左侧新知识

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);
							// 知识
//							String typeStr = "[知识]";
//							if (TextUtils.isEmpty(item.getContent()) || item.getContent().equals(typeStr)) {
//								holder.leftShareTopLl.setVisibility(View.GONE);
//								holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang);
//							} else {
								holder.leftShareMsgTv.setText("分享了[知识]");
								holder.leftShareTopLl.setVisibility(View.VISIBLE);
								holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
//							}
							// 图片
							if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
								imageLoader.displayImage(item.getJtFile().mUrl, holder.leftShareImageIv, LoadImage.mKnowledgeDefaultImage);
							} else {
								// holder.leftShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
								holder.leftShareImageIv.setVisibility(View.GONE);
							}
							// holder.leftShareImageIv.setVisibility(View.VISIBLE);
							// 类型
							holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);
							// 标题
							holder.leftShareTitleTv.setText("\u3000\u3000" + item.getJtFile().reserved2);
							holder.leftShareTitleTv.setVisibility(View.VISIBLE);
							// 内容
							if (!StringUtils.isEmpty(item.getJtFile().mSuffixName) || !StringUtils.isEmpty(item.getJtFile().mFileName)) {
								holder.leftShareContentTv.setText(!TextUtils.isEmpty(item.getJtFile().mFileName) ? item.getJtFile().mFileName : item.getJtFile().mSuffixName);
								holder.leftShareContentTv.setVisibility(View.VISIBLE);
							} else {
								holder.leftShareContentTv.setVisibility(View.GONE);
							}
							// 链接
							holder.leftShareLinkIv.setVisibility(View.GONE);
							if (TextUtils.isEmpty(item.getJtFile().reserved3)) {
								holder.leftShareLl.setOnLongClickListener(longClickListener);
							} else {
								if (item.getJtFile().reserved3.startsWith("http://mp.weixin.qq.com/")) { // 微信分享
									// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_weixin);
									holder.leftShareLinkIv.setText("来自微信");
								} else {
									// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_normal);
									// // 其它分享
									holder.leftShareLinkIv.setText("来自网页");
								}
								holder.leftShareLinkIv.setVisibility(View.VISIBLE);
							}
							// 长按事件
							holder.leftShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftShareLl.setOnClickListener(clickListener);
						}else if (IMBaseMessage.TYPE_COMMUNITY == item.getType()) { // 左侧社群

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);
							// 知识
//							String typeStr = "[社群]";
//							if (TextUtils.isEmpty(item.getContent()) || item.getContent().equals(typeStr)) {
//								holder.leftShareTopLl.setVisibility(View.GONE);
//								holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang);
//							} else {
								holder.leftShareMsgTv.setText("分享了[社群]");
								holder.leftShareTopLl.setVisibility(View.VISIBLE);
								holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
//							}
							// 图片
							if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
								imageLoader.displayImage(item.getJtFile().mUrl, holder.leftShareImageIv, LoadImage.mKnowledgeDefaultImage);
							} else {
								 holder.leftShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
//								holder.leftShareImageIv.setVisibility(View.GONE);
							}
							// holder.leftShareImageIv.setVisibility(View.VISIBLE);
							// 类型
							holder.leftShareTypeIv.setVisibility(View.GONE);//setImageResource(R.drawable.hy_chat_type_knowledge);
							// 标题
							holder.leftShareTitleTv.setText(item.getJtFile().mFileName);
							holder.leftShareTitleTv.setVisibility(View.VISIBLE);
							// 内容
							if (!StringUtils.isEmpty(item.getJtFile().mSuffixName)) {
								holder.leftShareContentTv.setText(item.getJtFile().mSuffixName);
								holder.leftShareContentTv.setVisibility(View.VISIBLE);
							} else {
								holder.leftShareContentTv.setVisibility(View.GONE);
							}
							// 链接(判断来源)
							if (TextUtils.isEmpty(item.getJtFile().reserved3)) 
								holder.rightShareLinkIv.setVisibility(View.GONE);
							// 长按事件
							holder.leftShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftShareLl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_REQUIREMENT == item.getType()) { // 左侧需求

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);
							holder.leftShareLl.setTag(item.getJtFile());

//							String typeStr = "[需求]";
//							if (item.getContent().equals(typeStr) || TextUtils.isEmpty(item.getContent())) {
//								holder.leftShareTopLl.setVisibility(View.GONE);
//								holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang);
//							} else {
								holder.leftShareMsgTv.setText("分享了[需求]");
								holder.leftShareTopLl.setVisibility(View.VISIBLE);
								holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
//							}
							// 图片
							holder.leftShareImageIv.setVisibility(View.GONE);
							// 类型
							holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_requirement);
							// 标题
							holder.leftShareTitleTv.setText("\u3000\u3000" + item.getJtFile().mFileName);
							holder.leftShareTitleTv.setVisibility(View.VISIBLE);
							// 内容
							if (!StringUtils.isEmpty(item.getJtFile().reserved1)) {
								holder.leftShareContentTv.setText(item.getJtFile().reserved1);
								holder.leftShareContentTv.setVisibility(View.VISIBLE);
							} else {
								holder.leftShareContentTv.setVisibility(View.GONE);
							}
							// 链接
							holder.leftShareLinkIv.setVisibility(View.GONE);
							// 长按事件
							holder.leftShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftShareLl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_CONFERENCE == item.getType()) { // 左侧会议

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
//								String typeStr = "[会议]";
//								if (item.getContent().equals(typeStr) || TextUtils.isEmpty(item.getContent())) {
//									holder.leftShareTopLl.setVisibility(View.GONE);
//									holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang);
//								} else {
									holder.leftShareMsgTv.setText("分享了[会议]");
									holder.leftShareTopLl.setVisibility(View.VISIBLE);
									holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
//								}
								// 图片
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									imageLoader.displayImage(item.getJtFile().mUrl, holder.leftShareImageIv);
								}else{
									holder.leftShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
								}
								holder.leftShareImageIv.setVisibility(View.VISIBLE);
								// 类型
								holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_conference);
								// 标题
								holder.leftShareTitleTv.setText("\u3000\u3000" + item.getJtFile().mFileName);
								holder.leftShareTitleTv.setVisibility(View.VISIBLE);
								// 内容
								if (!StringUtils.isEmpty(item.getJtFile().reserved1)) {
									holder.leftShareContentTv.setText(item.getJtFile().reserved1);
									holder.leftShareContentTv.setVisibility(View.VISIBLE);
								} else {
									holder.leftShareContentTv.setVisibility(View.GONE);
								}
								// holder.leftShareContentTv.setText(item.getJtFile().reserved1);
								// holder.leftShareContentTv.setVisibility(View.VISIBLE);
								// 链接
								holder.leftShareLinkIv.setVisibility(View.GONE);
							}
							// 长按事件
							holder.leftShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftShareLl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_VIDEO == item.getType()) { // 左侧视频

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {
								File file = new File(EUtil.getAppCacheFileDir(mContext), item.getJtFile().mFileName);
								if (file.exists() && file.length() == item.getJtFile().mFileSize) { // 文件已经下载到本地
									BmpAsyncTask bAsyncTask = new BmpAsyncTask(holder.leftVideoIv);
									bAsyncTask.execute(file.getAbsolutePath());
								} else { // 文件尚未下载
									holder.leftVideoIv.setImageResource(R.drawable.hy_chat_right_pic);
								}
							}
							// 长按事件
							holder.leftVideoIv.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftPlayIv.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType() || IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType() || IMBaseMessage.TYPE_ORG_OFFLINE == item.getType()
								|| IMBaseMessage.TYPE_ORG_ONLINE == item.getType() || IMBaseMessage.TYPE_CUSTOMER == item.getType() || item.getType() == IMBaseMessage.TYPE_ORGANIZATION) { // 左侧关系

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
								if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType() || IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
									if (!StringUtils.isEmpty(jtFile.mFileName)) {
										// holder.leftCnsTitle.setText(jtFile.mFileName);
										holder.leftCnsName1.setText(jtFile.mFileName);
										holder.leftCnsName1.setVisibility(View.VISIBLE);
									}

									if (!StringUtils.isEmpty(jtFile.mSuffixName)) {
										holder.leftCnsName2.setText(jtFile.mSuffixName);
										holder.leftCnsName2.setVisibility(View.VISIBLE);
									}
									if (!StringUtils.isEmpty(jtFile.reserved1)) {
										holder.leftCnsName3.setText(jtFile.reserved1);
										holder.leftCnsName3.setVisibility(View.VISIBLE);
									}

								}// 组织
								else {
									holder.leftCnsName1.setText(!TextUtils.isEmpty(item.getJtFile().mFileName) ? item.getJtFile().mFileName : item.getJtFile().mSuffixName);
									holder.leftCnsName1.setVisibility(View.VISIBLE);

									holder.leftCnsName2.setText(jtFile.reserved1);
									holder.leftCnsName2.setVisibility(View.VISIBLE);
								}
								if (jtFile.mUrl == null) {
									jtFile.mUrl = "";
								}

								if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType() || IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
									imageLoader.displayImage(jtFile.mUrl, holder.leftCnsIcon, LoadImage.TYPE_IMAGE_connection80obj);
								} else {
									imageLoader.displayImage(jtFile.mUrl, holder.leftCnsIcon, LoadImage.TYPE_IMAGE_companyfriend80Obj);
								}
								// 长按事件
								holder.leftCnsRl.setOnLongClickListener(longClickListener);
								// 点击事件
								holder.leftCnsRl.setOnClickListener(clickListener);
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
								HomeCommonUtils.setFileIconAccoredSuffixName(holder.leftFileTypeIv, item.getJtFile().getmSuffixName());
								holder.leftFileNameTv.setText(item.getJtFile().mFileName); // 文件名
								holder.leftFileSizeTv.setText(EUtil.formatFileSize(item.getJtFile().mFileSize)); // 文件大小
								String fileStatus = ""; // 文件状态
								if (downloadService != null && downloadService.isTaskExist(item.getMessageID())) { // 下载中

									holder.leftFileProgressPb.setProgress(downloadService.getTaskProgress(item.getMessageID()));
									holder.leftFileProgressPb.setVisibility(View.VISIBLE);
									int status = downloadService.getTaskStatus(item.getMessageID());
									if (status == HyChatFileDownloader.Status.Prepared || status == HyChatFileDownloader.Status.Started) { // 下载中
										fileStatus = "下载中";
									} else { // 已停止
										fileStatus = "继续下载";
									}
									holder.leftFileStatusTv.setText(fileStatus);
								} else { // 已下载、未下载或下载未完成

									File fileDir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_FILE, meetingId, topicId);
									if (fileDir != null) {
										File localFile = new File(fileDir, item.getJtFile().mFileName);
										if (localFile.length() == item.getJtFile().mFileSize) { // 文件已下载完成，点击直接打开
											holder.leftFileProgressPb.setVisibility(View.GONE);
											fileStatus = "打开";
										} else if (localFile.length() > 0) { // 文件尚未下载完，点击跳转到预览页
											holder.leftFileProgressPb.setProgress((int) (localFile.length() * 1.0 / item.getJtFile().mFileSize * 100));
											holder.leftFileProgressPb.setVisibility(View.VISIBLE);
											fileStatus = "继续下载";
										} else { // 文件尚未下载
											holder.leftFileProgressPb.setVisibility(View.GONE);
											fileStatus = "未下载";
										}
									} else {
										holder.leftFileProgressPb.setVisibility(View.GONE);
										fileStatus = "未下载";
									}
									holder.leftFileStatusTv.setText(fileStatus);
								}
							}
							// 长按事件
							holder.leftFileLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftFileLl.setOnClickListener(clickListener);
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
								holder.leftVoiceDurationTv.setText(item.getJtFile().reserved2 + "\"");
								// 控件长度
								int paramsWidth = 50 + (StringUtils.isEmpty(item.getJtFile().reserved2) ? 0 : Integer.parseInt(item.getJtFile().reserved2) * 5);
								// 最大不超过200dp
								paramsWidth = Math.min(paramsWidth, 200);
								// 设置长度
								LinearLayout.LayoutParams layoutParams = (LayoutParams) holder.leftVoiceDurationLl.getLayoutParams();
								layoutParams.width = EUtil.convertDpToPx(paramsWidth);
								holder.leftVoiceDurationLl.setLayoutParams(layoutParams);
								// 语音文件
								final File file = new File(EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_AUDIO, meetingDetail.getId(), topicDetail.getId()), item.getJtFile().mFileName);
								// 当前状态
								if (file.exists() && file.length() == item.getJtFile().mFileSize) { // 已下载
									holder.leftVoiceLoadingPb.setVisibility(View.GONE);
									holder.leftVoiceIv.setVisibility(View.VISIBLE);
								} else { // 其它
									long taskId = voiceFileManager.query(item.getJtFile().mUrl);
									if (taskId >= 0) { // 下载中
										Query query = new Query().setFilterById(taskId);
										Cursor cursor = null;
										try {
											cursor = downloadManager.query(query);
											int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
											switch (status) {
											case DownloadManager.STATUS_PENDING:
											case DownloadManager.STATUS_RUNNING:
												holder.leftVoiceLoadingPb.setVisibility(View.VISIBLE);
												holder.leftVoiceIv.setVisibility(View.GONE);
												break;
											default:
												holder.leftVoiceLoadingPb.setVisibility(View.GONE);
												holder.leftVoiceIv.setVisibility(View.VISIBLE);
												break;
											}
										} catch (Exception e) {
											if (cursor != null) {
												cursor.close();
											}
											holder.leftVoiceLoadingPb.setVisibility(View.GONE);
											holder.leftVoiceIv.setVisibility(View.VISIBLE);
											Log.d(TAG, e.getMessage());
										}
									} else { // 未下载
										holder.leftVoiceLoadingPb.setVisibility(View.GONE);
										holder.leftVoiceIv.setVisibility(View.VISIBLE);
									}
								}
								// 点击事件
								holder.leftVoiceLl.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {

										// 当前音频url
										curVoiceUrl = item.getJtFile().mUrl;

										if (file.exists() && file.length() == item.getJtFile().mFileSize) {
											// 开始播放音频和动画
											startPlaying(file.getAbsolutePath(), holder.leftVoiceIv, Integer.parseInt(item.getJtFile().reserved2) * 1000);
										} else {
											// 是否已开始下载任务
											long taskId = voiceFileManager.query(item.getJtFile().mUrl);
											if (taskId < 0) { // 从未开始任务

												File dir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_AUDIO, meetingId, topicId);
												if (dir != null) {
													Request request = new Request(Uri.parse(item.getJtFile().mUrl));
													request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
													request.setDestinationUri(Uri.fromFile(new File(dir, item.getJtFile().mFileName))); // 设置文件下载位置
													voiceFileManager.insert(item.getJtFile().mUrl, downloadManager.enqueue(request));
													// 界面修改
													holder.leftVoiceLoadingPb.setVisibility(View.VISIBLE);
													holder.leftVoiceIv.setVisibility(View.GONE);
												} else {
													showToast("没有SD卡，无法下载语音文件");
												}
											} else { // 任务已经开始

												Query query = new Query().setFilterById(taskId);
												Cursor cursor = null;
												try {
													cursor = downloadManager.query(query);
													int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
													switch (status) {
													case DownloadManager.STATUS_PENDING:
													case DownloadManager.STATUS_RUNNING:
														holder.leftVoiceLoadingPb.setVisibility(View.VISIBLE);
														holder.leftVoiceIv.setVisibility(View.GONE);
														break;
													default:
														// 删除旧任务
														downloadManager.remove(taskId);
														voiceFileManager.delete(taskId);
														// 开始新任务
														File dir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_AUDIO, meetingDetail.getId(), topicDetail.getId());
														if (dir != null) {
															Request request = new Request(Uri.parse(item.getJtFile().mUrl));
															request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
															request.setDestinationUri(Uri.fromFile(new File(dir, item.getJtFile().mFileName))); // 设置文件下载位置
															voiceFileManager.insert(item.getJtFile().mUrl, downloadManager.enqueue(request));
															// 界面修改
															holder.leftVoiceLoadingPb.setVisibility(View.VISIBLE);
															holder.leftVoiceIv.setVisibility(View.GONE);
														} else {
															showToast("没有SD卡，无法下载语音文件");
														}
														break;
													}
												} catch (Exception e) {
													if (cursor != null) {
														cursor.close();
													}
													holder.leftVoiceLoadingPb.setVisibility(View.GONE);
													holder.leftVoiceIv.setVisibility(View.VISIBLE);
												}
											}
										}
									}
								});
								// 长按事件
								holder.leftVoiceLl.setOnLongClickListener(longClickListener);
							}
						}
					}
				}
				// 发布时间
				String createDate = item.getTime();
				if (!TextUtils.isEmpty(createDate)) {
					createDate = JTDateUtils.getIMTimeDisplay(createDate, App.getApplicationConxt(), false);
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
						long interval = JTDateUtils.getIntervalMoreTime(messageList.get(position - 1).getTime(), item.getTime());
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
	}

	// 更新下载列表项
	public void updateDownloadItem(String messageId, int... args) {

		if (TextUtils.isEmpty(messageId)) {
			return;
		}

		List<MeetingMessage> listMsg = mAdapter.getData();
		if (listMsg == null || listMsg.size() < 0) {
			return;
		}

		int index = -1;
		for (int i = listMsg.size() - 1; i >= 0; i--) {
			if (messageId.equals(listMsg.get(i).getMessageID())) {
				index = i;
				break;
			}
		}

		if (index >= listMsg.size() || index < 0) {
			return;
		}

		IMBaseMessage msg = listMsg.get(index);
		View v = listView.getChildAt(index - listView.getFirstVisiblePosition());
		if (v == null) {
			return;
		}
		ViewHolder holder = (ViewHolder) v.getTag();
		if (holder != null) {

			if (msg.getType() == IMBaseMessage.TYPE_FILE) { // 文件

				switch (args[0]) {
				case HyChatFileDownloader.Status.Prepared: // 准备就绪
					if (msg.getSenderID().equals(App.getUserID())) {
						holder.rightFileProgressPb.setVisibility(View.VISIBLE);
						holder.rightFileStatusTv.setText("开始下载");
					} else {
						holder.leftFileProgressPb.setVisibility(View.VISIBLE);
						holder.leftFileStatusTv.setText("开始下载");
					}
					break;
				case HyChatFileDownloader.Status.Started: // 正在下载
					int progress = 0;
					if (args.length > 1) {
						progress = args[1];
					}
					if (msg.getSenderID().equals(App.getUserID())) {
						holder.rightFileProgressPb.setProgress(progress);
						holder.rightFileStatusTv.setText("下载中");
					} else { // 对方
						holder.leftFileProgressPb.setProgress(args[1]);
						holder.leftFileStatusTv.setText("下载中");
					}
					break;
				case HyChatFileDownloader.Status.Success: // 下载成功
					if (msg.getSenderID().equals(App.getUserID())) {
						holder.rightFileProgressPb.setVisibility(View.GONE);
						holder.rightFileStatusTv.setText("打开");
					} else {
						holder.leftFileProgressPb.setVisibility(View.GONE);
						holder.leftFileStatusTv.setText("打开");
					}
					break;
				case HyChatFileDownloader.Status.Canceled: // 下载取消
					if (msg.getSenderID().equals(App.getUserID())) {
						holder.rightFileProgressPb.setVisibility(View.GONE);
						holder.rightFileStatusTv.setText("未下载");
					} else {
						holder.leftFileProgressPb.setVisibility(View.GONE);
						holder.leftFileStatusTv.setText("未下载");
					}
					break;
				case HyChatFileDownloader.Status.Error: // 下载出错
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

	class ViewHolder {

		public View viewBG;
		public LinearLayout leftLayout;
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
		public LinearLayout leftShareTopLl;
		public LinearLayout leftShareBottomLl;
		public TextView leftShareMsgTv;
		public ImageView leftShareImageIv;
		public ImageView leftShareTypeIv;
		public TextView leftShareTitleTv;
		public TextView leftShareContentTv;
		public TextView leftShareLinkIv;

		public TextView rightShareMsgTv;
		public LinearLayout rightShareBottomLl;
		public LinearLayout rightShareTopLl;
		public TextView rightShareLinkIv;
		public LinearLayout rightShareLl;
		public ImageView rightShareImageIv;
		public ImageView rightShareTypeIv;
		public TextView rightShareTitleTv;
		public TextView rightShareContentTv;

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

	// 更新列表中的上传项状态
	public void updateUploadItem(String messageId, int... args) {

		if (TextUtils.isEmpty(messageId)) {
			return;
		}

		List<MeetingMessage> listMsg = mAdapter.getData();
		if (listMsg == null || listMsg.size() < 0) {
			return;
		}

		int index = getMessageIndexById(messageId);
		if (index < 0 || index >= listMessage.size()) {
			return;
		}

		IMBaseMessage msg = listMsg.get(index);

		View v = listView.getChildAt(index - listView.getFirstVisiblePosition());
		if (v == null) {
			return;
		}
		final ViewHolder holder = (ViewHolder) v.getTag();
		if (holder != null && msg != null) {
			if (msg.getType() == IMBaseMessage.TYPE_FILE) { // 上传文件
				switch (args[0]) {
				case HyChatFileUploader.Status.Started: // 开始上传
					if (args.length < 2) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								holder.rightFileProgressPb.setVisibility(View.VISIBLE);
							}
						});
						break;
					}
					final int progress = args[1]; // 上传进度
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							holder.rightFileProgressPb.setProgress(progress);
						}
					});
					break;
				case HyChatFileUploader.Status.Success:
				case HyChatFileUploader.Status.Canceled:
				case HyChatFileUploader.Status.Error:
					mHandler.post(new Runnable() {
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

	// 更新语音项目
	public void updateVoiceItem(String url) {

		List<MeetingMessage> listMsg = mAdapter.getData();
		if (listMsg == null || listMsg.size() < 0) {
			return;
		}

		for (int i = 0; i < listMsg.size(); i++) {
			IMBaseMessage msg = listMsg.get(i);
			if (msg.getJtFile() != null && !StringUtils.isEmpty(msg.getJtFile().mUrl) && msg.getJtFile().mUrl.equals(url)) {

				View v = listView.getChildAt(i - listView.getFirstVisiblePosition());
				if (v != null) {
					ViewHolder holder = (ViewHolder) v.getTag();
					if (holder != null) {
						if (msg.getSenderID().equals(App.getUserID())) { // 自己
							holder.rightVoiceLoadingPb.setVisibility(View.GONE);
							holder.rightVoiceIv.setVisibility(View.VISIBLE);
							// 播放音频
							if (curVoiceUrl.equals(msg.getJtFile().mUrl)) {
								startPlaying(new File(EUtil.getMeetingChatFileDir(this, JTFile.TYPE_AUDIO, meetingId, topicId), msg.getJtFile().mFileName).getAbsolutePath(), holder.rightVoiceIv,
										Integer.parseInt(msg.getJtFile().reserved2) * 1000);
							}
						} else { // 对方
							holder.leftVoiceLoadingPb.setVisibility(View.GONE);
							holder.leftVoiceIv.setVisibility(View.VISIBLE);
							// 播放音频
							if (curVoiceUrl.equals(msg.getJtFile().mUrl)) {
								startPlaying(new File(EUtil.getMeetingChatFileDir(this, JTFile.TYPE_AUDIO, meetingId, topicId), msg.getJtFile().mFileName).getAbsolutePath(), holder.leftVoiceIv,
										Integer.parseInt(msg.getJtFile().reserved2) * 1000);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 数据部分
	 */
	@Override
	public void bindData(int tag, Object object) {
		if (hasDestroy()) {
			return;
		}
		if (object != null) {
			if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SEND_MEETING_CHAT) { // 发送消息返回
				MSendMessage sm = (MSendMessage) object;
				if (!sm.isSucceed()) { // 消息发送失败,设置该条消息状态
					setMessageSendFail(listMessage, sm.getMessageID());
					setData(listMessage);
					return;
				} else { // 消息发送成功,发送成功把刚发的消息状态改为成功,刷新列表
					setMessageSent(listMessage, sm.getMessageID());
					refreshList();
					// 然后再合并消息,视觉上感觉快了.提高用户体验.
					List<IMBaseMessage> newList = sm.getListMessage();
					// 转换为MeetingMessage数组
					List<MeetingMessage> newMetList = convertBaseMsgList2MeetingMsgList(newList);
					if (newMetList != null && newMetList.size() > 0) {
						// 更新数据库
						startMergeAndSaveMessage(this, listMessage, newMetList, TASK_MERGE);
					}
				}
			} else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GET_MEETING_MESSAGE) { // 获取会议信息

				MSendMessage sm = (MSendMessage) object;
				if (sm.isSucceed()) {
					List<IMBaseMessage> newList = sm.getListMessage();
					List<MeetingMessage> newMetList = convertBaseMsgList2MeetingMsgList(newList);
					if (newMetList != null && newMetList.size() > 0) {
						// 更新数据库记录
						startMergeAndSaveMessage(this, listMessage, newMetList, TASK_MERGE);
					}
				}
			} else if (tag == EAPIConsts.ReqType.FOCUS_REQUIREMENT) { // 关注需求

				dismissLoadingDialog();
				DataBox dataBox = (DataBox) object;
				if (dataBox.mIsSuccess) {
					showToast("关注成功");
				} else {
					showToast("关注失败");
				}
			}
		}
		// if (tag == EAPIConsts.ReqType.GET_TREATED_HTML) { //
		// 获取大数据提取的内容被抛弃，发送超连接同畅聊一样
		//
		// if (object != null) {
		// DataBox dataBox = (DataBox) object;
		// if (dataBox.mTreatedHtml != null) {
		// JTFile jtFile = new JTFile();
		// jtFile.mSuffixName = dataBox.mTreatedHtml.getTitle();
		// jtFile.mUrl = dataBox.mTreatedHtml.getUrl();
		// jtFile.mType = JTFile.TYPE_KNOWLEDGE;
		// sendFile(jtFile);
		// } else {
		// sendMessage(cacheUrl);
		// }
		// } else {
		// sendMessage(cacheUrl);
		// }
		// }
		if (tag == EAPIConsts.CommonReqType.FetchExternalKnowledgeUrl) { // 解析url//发送超连接同畅聊一样,抛弃原来EAPIConsts.ReqType.GET_TREATED_HTML
			if (object != null) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> dataBox = (HashMap<String, Object>) object;
				if (dataBox.containsKey("knowledge2")) {
					Knowledge2 knowledge2 = (Knowledge2) dataBox.get("knowledge2");
					if (TextUtils.isEmpty(cacheUrl)) {
						KnowledgeReqUtil.doUpdateCollectKnowledge(this, this, knowledge2.getId(), knowledge2.getType(), "", null);
					} else {
						JTFile jtFile = knowledge2.toJTFile();
						sendFile(jtFile);
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
		} else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_ADD_NOTE_DETAIL_BY_CHAT) { // 会议畅聊添加到会议笔记

			dismissLoadingDialog();
			if (object != null) {
				boolean success = (Boolean) object;
				if (success) {
					// 跳转到会议笔记页面
					ENavigate.startMeetingNoteActivity(this, (int) meetingId, true);
				} else {
					showToast("操作失败");
				}
			} else {
				showToast("操作失败");
			}
		} else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_DETAIL) { // 获取会议详情
			dismissLoadingDialog();
			MMeetingDetail aDetail = (MMeetingDetail) object;
			if (null != aDetail) {
				meetingDetail = aDetail.getMeeting();
				if (null != meetingDetail) {
					for (MMeetingTopicQuery topicDetail : meetingDetail.getListMeetingTopicQuery()) {
						if (topicDetail.getId() == topicId) {
							this.topicDetail = topicDetail;
							break;
						}
					}
					if (null != topicDetail) {
						doInit();
					}
					return;
				}
			}
			finish();
		} else if (tag == EAPIConsts.KnoReqType.UpdateCollectKnowledge) {
			dismissLoadingDialog();
			Map<String, Object> dataBox = (Map<String, Object>) object;
			if (dataBox != null) {
				boolean success = false;
				if (dataBox.containsKey("succeed")) {
					success = (Boolean) dataBox.get("succeed");
				}
				if (success) {
					showToast("收藏成功");
				} else {
					showToast("收藏失败");
				}
			}
		} else if (tag == EAPIConsts.IMReqType.IM_REQ_CLIENTDELETEMESSAGE) {
			if (object != null) {
				int responseCode = (Integer) object;
				// 删除失败
				if (responseCode == -1) {
					showToast("删除失败");
				} else {
					mAdapter.notifyDataSetChanged(); // 更新列表
					showToast("记录已删除");
				}
			}
		}
	}

	public String getLatestTime() {
		String time = null;
		for (int i = listMessage.size() - 1; i >= 0; i--) {
			if (listMessage.size() > i) {
				IMBaseMessage cm = listMessage.get(i);
				if (cm.getSendType() == IMBaseMessage.SEND_TYPE_SENT) {
					time = cm.getTime();
					break;
				} else {
					continue;
				}
			}
		}
		return time;
	}

	public void setTitle(String title) {
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setTitle(title);
	}

	@SuppressLint("HandlerLeak")
	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 请求数据
			getMoreMessage();
			mHandler.postDelayed(mRunnable, 15000);
		}
	};

	protected Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			// 获取聊天信息
			mHandler.sendEmptyMessage(0);
		}
	};

	/**
	 * 更新列表
	 */
	public void notifyGetMessage() {
		mHandler.sendEmptyMessage(0);
	}

	/**
	 * 更新列表
	 * 
	 * @param msg
	 */
	public void notifyGetMessage(MeetingMessage msg) {

		// 然后再合并消息,视觉上感觉快了.提高用户体验.
		List<MeetingMessage> newList = new ArrayList<MeetingMessage>();
		newList.add(msg);
		// 更新数据库
		startMergeAndSaveMessage(this, listMessage, newList, TASK_MERGE);
		// 设置消息状态
		setMessageSent(listMessage, msg.getMessageID());
		refreshList();
	}

	private void startGet() {
		mHandler.postDelayed(mRunnable, 100);
	}

	private void stopGet() {
		mHandler.removeCallbacks(mRunnable);
	}

	// 重置获取聊天记录的时间
	protected void resetGetTime() {
		this.mLatestGetTime = new Date();
	}

	/**
	 * 启动合并返回的消息列表并保存到文件
	 * 
	 * @param context
	 * @param oldList
	 *            当前列表
	 * @param newList
	 *            服务器返回的新列表
	 * @type 1-正常merge, 2-仅保存oldList中的数据, 3-读取数据出来,并在post中返回
	 */
	public final static int TASK_MERGE = 1;
	public final static int TASK_SAVE = 2;
	public final static int TASK_READ = 3;

	private MeetingBranchFragment fragment;

	public void startMergeAndSaveMessage(Context context, List<MeetingMessage> oldList, List<MeetingMessage> newList, Integer type) {
		ApolloUtils.execute(false, new ChatBaseTask(), context, oldList, newList, type);
	}

	public class ChatBaseTask extends AsyncTask<Object, Integer, Object> {

		private String TAG = getClass().getSimpleName();

		private Context context;
		private int operateType = TASK_MERGE;

		public ChatBaseTask() {

		}

		@Override
		protected Object doInBackground(Object... params) {
			try {

				synchronized (TAG) {
					if (params[0] instanceof Context) {
						context = (Context) params[0];
					} else {
						return null;
					}
					@SuppressWarnings("unchecked")
					List<MeetingMessage> oldList = (List<MeetingMessage>) params[1];
					@SuppressWarnings("unchecked")
					List<MeetingMessage> newList = (List<MeetingMessage>) params[2];
					operateType = (Integer) params[3];
					switch (operateType) {
					case TASK_MERGE: {
						List<MeetingMessage> mergeList = new ArrayList<MeetingMessage>();
						// 合并两个list，并生成一个新的list
						mergeList = mergeListMessage(oldList, newList);
						// 移除隐藏记录
						removeHideMessage(mergeList);
						// 保存到数据库
						saveCacheMessage(mergeList);
						return mergeList;
					}
					case TASK_READ: { // 读取缓存数据
						List<MeetingMessage> retList = loadCacheMessage();
						if (retList == null) {
							retList = new ArrayList<MeetingMessage>();
						}
						// 将之前发送状态为发送中或者推送的消息，设置为发送完毕
						for (IMBaseMessage obj : retList) {
							if (obj.getSendType() == IMBaseMessage.SEND_TYPE_SENDING) {
								obj.setSendType(IMBaseMessage.SEND_TYPE_SENT);
							}
						}
						return retList;
					}
					case TASK_SAVE:
						saveCacheMessage(oldList);
						return null;
					default:
						return null;
					}
				}
			} catch (Exception e) {
				Log.d(TAG, e.getMessage() + "");
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			if (result != null) {
				// 更新新列表到列表页
				listMessage = (ArrayList<MeetingMessage>) result;
				setData(listMessage);
				if (operateType == TASK_READ) {
					// 发送分享的消息
					if (shareInfo != null) { // 单条
						if (shareInfo.mType == JTFile.TYPE_TEXT) {
							sendFile(shareInfo);
						}
					}
					if (shareInfoList != null) { // 多条
						for (JTFile jtFile : shareInfoList) {
							sendFile(jtFile);
						}
					}
					// 第一次读取缓存后，启动获取聊天记录
					startGet();
				}
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}

	// 设置消息发送状态为失败
	public void setMessageSendFail(List<MeetingMessage> listMsg, String msgId) {
		if (listMsg == null || listMsg.size() <= 0) {
			return;
		}
		if (TextUtils.isEmpty(msgId)) {
			return;
		}
		for (int i = listMsg.size() - 1; i >= 0; i--) {
			MeetingMessage msg = listMsg.get(i);
			if (0 == msg.getMessageID().compareToIgnoreCase(msgId)) {
				msg.setSendType(IMBaseMessage.SEND_TYPE_FAIL);
				// 更新数据库中的状态
				meetingRecordDBManager.update(App.getUserID(), msg);
				// 改变界面状态
				final int index = i;
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						View v = listView.getChildAt(index - listView.getFirstVisiblePosition());
						if (v != null) {
							ViewHolder holder = (ViewHolder) v.getTag();
							if (holder != null) {
								holder.sendMsgProgress.setVisibility(View.GONE);
								holder.sendMsgFail.setVisibility(View.VISIBLE);
							}
						}
					}
				});
				break;
			}
		}
	}

	// 设置消息发送状态为已发送
	public void setMessageSent(List<MeetingMessage> listMsg, String msgId) {
		if (listMsg == null || listMsg.size() <= 0) {
			return;
		}
		if (TextUtils.isEmpty(msgId)) {
			return;
		}
		for (int i = listMsg.size() - 1; i >= 0; i--) {
			MeetingMessage msg = listMsg.get(i);
			if (msg.getMessageID().equalsIgnoreCase(msgId)) {
				msg.setSendType(IMBaseMessage.SEND_TYPE_SENT);
				// 更新数据库中的状态
				meetingRecordDBManager.update(App.getUserID(), msg);
				break;
			}
		}
	}

	// 将List<IMBaseMesssage> 转换为 List<MeetingMessage>
	public List<MeetingMessage> convertBaseMsgList2MeetingMsgList(List<IMBaseMessage> listMsg) {
		List<MeetingMessage> listMetMsg = null;
		if (listMsg == null || listMsg.size() < 0) {
			return listMetMsg;
		}
		for (IMBaseMessage msg : listMsg) {
			if (msg instanceof MeetingMessage) {
				if (listMetMsg == null) {
					listMetMsg = new ArrayList<MeetingMessage>();
				}
				listMetMsg.add((MeetingMessage) msg);
			}
		}
		return listMetMsg;
	}

	// 移除隐藏的数据
	public void removeHideMessage(List<MeetingMessage> listMsg) {
		if (listMsg == null || listMsg.size() <= 0) {
			return;
		}
		Iterator<MeetingMessage> iterator = listMsg.iterator();
		while (iterator.hasNext()) {
			MeetingMessage msg = iterator.next();
			if (!meetingRecordDBManager.queryVisibility(App.getUserID(), msg.getMessageID())) {
				iterator.remove();
			}
		}
		/*
		 * 不能用此种方法删除 for(int i = 0; i < listMsg.size(); i++){
		 * if(!meetingRecordDBManager.queryVisibility(App.getUserID(),
		 * listMsg.get(i).getMessageID())){ listMsg.remove(i); i--; } }
		 */
	}

	// 合并聊天记录数组
	@SuppressWarnings("unchecked")
	public List<MeetingMessage> mergeListMessage(List<MeetingMessage> oldList, List<MeetingMessage> newList) {
		if (oldList.size() == 0) {
			return newList;
		}
		if (newList.size() == 0) {
			return oldList;
		}
		// 合并，排重
		for (int i = 0; i < newList.size(); i++) {

			MeetingMessage newItem = newList.get(i);
			/*
			 * if (newItem.getSenderID().equalsIgnoreCase("0")) { //
			 * 系统消息，直接添加到队尾 oldList.add(newItem); continue; }
			 */
			int oldSize = oldList.size();
			boolean blnFindInOld = false;
			for (int j = 0; j < oldSize; j++) {
				MeetingMessage oldItem = oldList.get(j);
				if (oldItem.getMessageID().equalsIgnoreCase(newItem.getMessageID())) { // 如果在老的中间找到了新消息相同的messageID，则用新的取代老的
					oldList.set(j, newItem);
					blnFindInOld = true;
					break;
				}
			}
			if (blnFindInOld == false) {
				oldList.add(newItem);
			}
		}
		// 排序
		Comparator comparator = new IMTimeComparator();
		Collections.sort(oldList, comparator);
		return oldList;
	}

	// 获取议题ID
	public String getTopicID() {
		return topicDetail.getId() + "";
	}

	@Override
	public void onPrepared(String messageId) {

	}

	@Override
	public void onStarted(String messageId) {
		updateUploadItem(messageId, HyChatFileUploader.Status.Started);
	}

	@Override
	public void onUpdate(String messageId, int value) {
		updateUploadItem(messageId, HyChatFileUploader.Status.Started, value);
	}

	@Override
	public void onCanceled(String messageId) {
		// 设置消息的状态
		setMessageSendFail(listMessage, messageId);
		// 从上传列表中取消指定任务
		removeUploadTask(messageId);
		// 更新显示
		updateUploadItem(messageId, HyChatFileUploader.Status.Canceled);
	}

	@Override
	public void onSuccess(String messageId, JTFile jtFile) {
		// 从上传队列中移除任务
		removeUploadTask(messageId);
		// 上传成功则发送消息
		ConferenceReqUtil.doSendMeetingChat(MChatBaseActivity.this, MChatBaseActivity.this, getMessageById(messageId), mNetHandler);
		// 更新列表
		updateUploadItem(messageId, HyChatFileUploader.Status.Success);
	}

	@Override
	public void onError(String messageId, int errCode, String errMsg) {
		// 设置失败状态
		setMessageSendFail(listMessage, messageId);
		// 从上传队列中移除任务
		removeUploadTask(messageId);
		// 更新列表
		updateUploadItem(messageId, HyChatFileUploader.Status.Error);
	}

	/**
	 * 获取图片上传的缩略图地址
	 * 
	 * @param imgPath
	 *            图片路径
	 * @param name
	 *            缩略图名称
	 * @param meetingId
	 *            // 会议id
	 * @param topicId
	 *            // 分会场id
	 * @return
	 */
	protected String getImageThumbCachePath(String srcImgPath, String desImgName, long meetingId, long topicId) {
		String path = null;
		if (!new File(srcImgPath).exists()) {
			return null;
		}
		if (!EUtil.isSDCardExist()) {
			return null;
		}
		File cacheDir = EUtil.getMeetingChatFileDir(this, JTFile.TYPE_IMAGE, meetingId, topicId);
		if (cacheDir != null) {
			Bitmap bitmap = EUtil.getImageThumb(this, srcImgPath, 800);
			if (bitmap != null) {
				path = EUtil.saveImage(this, bitmap, desImgName, cacheDir);
			}
		}
		return path;
	}

	/**
	 * 获取视频缩略图地址
	 * 
	 * @param srcVideoPath
	 *            视频原始地址
	 * @param desVideoName
	 *            缩略图文件名
	 * @param meetingId
	 * @param topicId
	 * @return
	 */
	protected String getVideoScreenshotCachePath(String srcVideoPath, String desVideoName, long meetingId, long topicId) {
		String path = null;
		if (!new File(srcVideoPath).exists()) {
			return path;
		}
		if (!EUtil.isSDCardExist()) {
			return path;
		}
		File cacheDir = EUtil.getMeetingChatFileDir(this, JTFile.TYPE_IMAGE, meetingId, topicId);
		if (cacheDir != null) {
			Bitmap originalBitmap = EUtil.getVideoScreenshotBitmap(this, srcVideoPath);
			Bitmap compressedBitmap = EUtil.compressImage(originalBitmap); // 压缩图片
			if (compressedBitmap != null) { // 获取压缩Bitmap
				path = EUtil.saveImage(this, compressedBitmap, desVideoName, cacheDir); // 保存截图
			} else { // 获取压缩Bitmap失败
				path = EUtil.saveImage(this, originalBitmap, desVideoName, cacheDir); // 保存截图
			}
		}
		return path;
	}

	/**
	 * 获取基于时间的格式化的文件名
	 * 
	 * @param suffix
	 * @return
	 */
	protected String getUserAndTimeBasedFormatName(String suffix) {
		return App.getUserID() + "_" + DateFormat.format("yyyyMMddkkmmss", System.currentTimeMillis()) + suffix;
	}

	/**
	 * 根据指定id返回消息对象
	 * 
	 * @param msgId
	 * @return
	 */
	protected MeetingMessage getMessageById(String messageId) {
		MeetingMessage message = null;
		if (TextUtils.isEmpty(messageId)) {
			return message;
		}
		for (int i = listMessage.size() - 1; i >= 0; i--) {
			if (messageId.equals(listMessage.get(i).getMessageID())) {
				message = listMessage.get(i);
				break;
			}
		}
		return message;
	}

	/**
	 * 根据上传任务id返回对应消息的位置
	 * 
	 * @param messageId
	 * @return
	 */
	protected int getMessageIndexById(String messageId) {
		int index = -1;
		if (!TextUtils.isEmpty(messageId)) {
			for (int i = listMessage.size() - 1; i >= 0; i--) {
				if (messageId.equals(listMessage.get(i).getMessageID())) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	/** 表情点击事件 */
	private SmileyView.OnItemClickListener smileyViewClickListener = new SmileyView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg) {
			final SmileyParser parser = SmileyParser.getInstance(MChatBaseActivity.this);
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

			final String text = textEt.getText().toString() + LEFTSPECCHAR + parser.getmSmileyTexts()[(int) arg] + RIGHTSPECCHAR;
			textEt.setText(text);
			textEt.setSelection(text.length());
		}
	};

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (meetingTopicDataChanged) {
				setResult(RESULT_OK);
			}
		}
		return super.onKeyDown(keyCode, event);
	};

	/** 图片视频的缩放大小（数值按照dp的标准进行等比例放大或者缩小） */
	private static final int SCALESIZE = 200;

	class BmpAsyncTask extends AsyncTask<String, Void, Bitmap> {

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
									MChatBaseActivity.this,
									SCALESIZE),
							Utils.dipToPx(
									MChatBaseActivity.this,
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
}
