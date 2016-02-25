package com.tr.ui.conference.im;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.AnimationDrawable;
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
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.api.KnowledgeReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.db.ChatLocalFileDBManager;
import com.tr.db.MeetingRecordDBManager;
import com.tr.db.VoiceFileDBManager;
import com.tr.model.api.DataBox;
import com.tr.model.conference.MMeetingDetail;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.im.IMUtil.IMTimeComparator;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MeetingMessage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.service.HyChatFileDownloadService;
import com.tr.service.HyChatFileDownloadService.ServiceBinder;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.conference.im.MChatRecordBrowserActivity.ChatBaseAdapter.ViewHolder;
import com.tr.ui.conference.square.MeetingBranchFragment;
import com.tr.ui.widgets.BasicListView;
import com.tr.ui.widgets.ChatDialog;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyParser2;
import com.utils.common.ApolloUtils;
import com.utils.common.EUtil;
import com.utils.common.HyChatFileDownloader;
import com.utils.common.HyChatFileUploader;
import com.utils.common.JTDateUtils;
import com.utils.common.OpenFiles;
import com.utils.common.TaskIDMaker;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.LoadImage;
import com.utils.log.KeelLog;
import com.utils.string.StringUtils;

/**
 * 浏览会议聊天记录
 * @author leon
 */
public class MChatRecordBrowserActivity extends JBaseFragmentActivity implements IBindData{

	public final String TAG = getClass().getSimpleName();

	// 显示消息的View
	protected BasicListView listView;
	protected ChatBaseAdapter mAdapter;

	// 初始化语音的保存路径
	public String recordPath = null;
	public String recordAudioPath = null;
	public String recordNamePrefix = null;

	// 聊天数据
	protected ArrayList<MeetingMessage> listMessage = new ArrayList<MeetingMessage>(); // 聊天记录列表
	protected long meetingId; // 主会场id
	protected long topicId; // 分会场id
	protected MMeetingQuery meetingDetail; // 会议详情
	protected MMeetingTopicQuery topicDetail; // 分会场详情
	protected String fromActivityName; // 跳转来源
	protected String searchMessageId; // 搜索的记录id

	// 录音相关
	private MediaRecorder mRecorder;
	private MediaPlayer mPlayer;
	private VoiceFileDBManager voiceFileManager;
	private DownloadManager downloadManager;

	// 聊天记录数据库管理器
	protected MeetingRecordDBManager meetingRecordDBManager;
	// 上传的本地文件地址数据库管理器
	protected ChatLocalFileDBManager localFileDBManager;
	// 下载服务
	private HyChatFileDownloadService downloadService;
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
	// 消息处理器
	private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_im_chat_record);
		initView();
		initVars();
		// 绑定服务
		bindService(new Intent(this, HyChatFileDownloadService.class), serviceConnection, BIND_AUTO_CREATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// 初始化变量
	protected void initVars() {
		// 查看记录id
		searchMessageId = getIntent().getStringExtra(ENavConsts.EMessageID);
		// 语音文件管理
		voiceFileManager = new VoiceFileDBManager(this);
		// 聊天记录数据库管理对象
		meetingRecordDBManager = new MeetingRecordDBManager(this);
		// 下载管理器
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		// 消息列表
		listMessage = new ArrayList<MeetingMessage>();
		setData(listMessage);
		// 读取聊天记录缓存数据
		startMergeAndSaveMessage(this, null, null, TASK_READ);
	}

	// 初始化ImageLoader参数配置
	protected void initImageLoaderConfiguration() {

		// 设置图片缓存路径
		File cacheDir = EUtil.getMeetingChatFileDir(this, JTFile.TYPE_IMAGE, meetingId, topicId);
		if (cacheDir != null) {
			ImageLoader.getInstance().destroy();
			ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
					.discCache(new UnlimitedDiscCache(cacheDir))
					.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
					.defaultDisplayImageOptions(EUtil.getDefaultImageLoaderDisplayOptions()).build();
			ImageLoader.getInstance().init(configuration);
		}
	}

	// 恢复默认设置
	protected void recoverImageLoaderConfiguration() {
		ImageLoader.getInstance().destroy();
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(
				EUtil.getDefaultImageLoaderDisplayOptions()).build();
		ImageLoader.getInstance().init(configuration);
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
		// 聊天列表
		listView = (BasicListView) findViewById(R.id.personal_letter_item_ltw);
		initAdpter();
		// 初始化信息
		meetingDetail = (MMeetingQuery) getIntent().getSerializableExtra(ENavConsts.EMeetingDetail);
		topicDetail = (MMeetingTopicQuery) getIntent().getSerializableExtra(ENavConsts.EMeetingTopicDetail);
		meetingId = meetingDetail.getId();
		topicId = topicDetail.getId();
		// 加载标题栏
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		MeetingBranchFragment fragment = new MeetingBranchFragment(this,
				meetingDetail, topicDetail);
		transaction.add(R.id.speakerFl, fragment);
		transaction.commit();
	}

	/**
	 * 播放录音
	 */
	private void startPlaying(String voicePath, ImageView view, long duration) {

		if (mPlaying && voicePath.equals(mVoicePath)) { // 语音正在播放
			// 停止播放
			stopPlaying();
		} 
		else {
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
			} 
			catch (IOException e) {
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

	/**
	 * 停止录音Runnable
	 */
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
				voiceIv.setBackgroundResource(R.drawable.chatto_voice_playing);
			} else { // 对方
				voiceIv.setBackgroundResource(R.drawable.chatfrom_voice_playing);
			}
		}
	}
	
	// 查找消息发送人昵称
	public String getNickNameByMessage(MeetingMessage msg) {
		String nick = "";
		MMeetingMember member = meetingDetail.getMeetingMemberByUserId(msg
				.getSenderID());
		if (member != null) {
			nick = member.getMemberName();
		}
		return nick;
	}

	// 查找消息发送人头像
	public String getImageByMessage(MeetingMessage msg) {
		String avatar = "";
		MMeetingMember member = meetingDetail.getMeetingMemberByUserId(msg
				.getSenderID());
		if (member != null) {
			avatar = member.getMemberPhoto();
		}
		return avatar;
	}

	// 初始化适配器
	public void initAdpter() {
		mAdapter = new ChatBaseAdapter(this);
		List<MeetingMessage> messageList = new ArrayList<MeetingMessage>();
		mAdapter.setData(messageList);
		listView.setAdapter(mAdapter);
		setData(messageList);
	}

	public void setData(List<MeetingMessage> messageList) {
		mAdapter.setData(messageList);
		refreshList();
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

	
	@Override
	public void onResume() {
		super.onResume();
		// 设置ImageLoader配置
		initImageLoaderConfiguration();
	}

	@Override
	public void onPause() {
		super.onPause();
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
		super.onBackPressed();
	}

	public void startUserActivity(String userID) {
		KeelLog.d(TAG, "startUserActivity userID;" + userID);
		if (!TextUtils.isEmpty(userID)){
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

	protected class ChatBaseAdapter extends BaseAdapter {

		// 删除该条聊天记录
		private void deleteItem(MeetingMessage msg, int position) {
			msg.setHide(true); // 设置为不可见
			meetingRecordDBManager.update(App.getUserID(), msg); // 更新数据
			listMessage.remove(position); // 移除不可见数据
			mAdapter.notifyDataSetChanged(); // 更新列表
			showToast("记录已删除"); // 弹出提示消息
		}

		private void showItemLongClickDialog(final MeetingMessage msg, final int position) {

			String[] listOper = null;

			// 处理长按事件
			switch (msg.getType()) {
			case IMBaseMessage.TYPE_TEXT: // 　文本
				listOper = new String[] { "复制", "转发到会议", "转发到畅聊", "删除", "会议笔记" };
				break;
			case IMBaseMessage.TYPE_IMAGE: // 图片
			case IMBaseMessage.TYPE_AUDIO: // 语音
				listOper = new String[] { "保存", "转发到会议", "转发到畅聊", "删除", "会议笔记" };
				break;
			case IMBaseMessage.TYPE_VIDEO: // 视频
			case IMBaseMessage.TYPE_FILE: // 文件
				listOper = new String[] { "保存", "转发到会议", "转发到畅聊", "删除"};
				break;
			case IMBaseMessage.TYPE_KNOWLEDGE: // 知识
			case IMBaseMessage.TYPE_KNOWLEDGE2: // 新知识
				listOper = new String[] { "收藏", "转发到会议", "转发到畅聊", "删除", "分享" };
				break;
			case IMBaseMessage.TYPE_REQUIREMENT: // 需求
				listOper = new String[] { "关注", "转发到会议", "转发到畅聊", "删除", "分享" };
				break;
			case IMBaseMessage.TYPE_JTCONTACT_OFFLINE: // 关系
			case IMBaseMessage.TYPE_JTCONTACT_ONLINE:
			case IMBaseMessage.TYPE_ORG_OFFLINE:
			case IMBaseMessage.TYPE_ORG_ONLINE:
				listOper = new String[] { "转发到会议", "转发到畅聊", "删除", "分享" };
				break;
			case IMBaseMessage.TYPE_CONFERENCE: // 会议
				listOper = new String[] { "转发到会议", "转发到畅聊", "删除" };
				break;
			}
			// 显示弹出框
			new AlertDialog.Builder(mContext).setItems(listOper, new DialogInterface.OnClickListener() {

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0: /******************************/
						if (msg.getType() == IMBaseMessage.TYPE_TEXT) { // 复制文本
							ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
							cmb.setText(msg.getContent());
							showToast("已复制");
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_IMAGE 
								|| msg.getType() == IMBaseMessage.TYPE_VIDEO
								|| msg.getType() == IMBaseMessage.TYPE_FILE 
								|| msg.getType() == IMBaseMessage.TYPE_AUDIO) { // 保存图片、视频、文件、语音

							// 首先查看原文件是否存在
							String originalPath = localFileDBManager.query(App.getUserID(), msg.getMessageID());
							if (!TextUtils.isEmpty(originalPath)) {
								File originalFile = new File(originalPath);
								if (originalFile.exists()) {
									if (EUtil.saveImageToUserDir(mContext, originalFile)) {
										showToast("保存成功");
										return;
									} 
									else {
										showToast("保存失败");
										return;
									}
								}
							}
							// 其次查看文件是否下载完成
							File dir = EUtil.getMeetingChatFileDir(mContext, msg.getJtFile().mType, meetingId, topicId);
							String typeStr = getJTFileTypeString(msg.getJtFile());
							if (dir != null) {
								File downloadFile = null;
								if(msg.getType() == IMBaseMessage.TYPE_IMAGE){
									downloadFile = ImageLoader.getInstance().getDiscCache().get(msg.getJtFile().mUrl);
									if(downloadFile.exists()){ // 图片不需要比较，如果存在即可保存
										if (EUtil.saveImageToUserDir(mContext, downloadFile)) {
											showToast("保存成功");
										} 
										else {
											showToast("保存失败");
										}
									}
									else{
										showToast(typeStr + "尚未下载完成，请稍后再试");
									}
								}
								else{
									downloadFile = new File(dir, msg.getJtFile().mFileName);
									if (downloadFile.length() == msg.getJtFile().mFileSize) {
										if (EUtil.saveImageToUserDir(mContext, downloadFile)) {
											showToast("保存成功");
										} 
										else {
											showToast("保存失败");
										}
									} 
									else if (downloadFile.length() > 0) {
										showToast(typeStr + "尚未下载完成，请稍后再试");
									} 
									else {
										showToast(typeStr + "尚未下载完成，请稍后再试");
									}
								}
							} 
							else {
								showToast("无法访问" + typeStr + "缓存目录");
							}
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 收藏知识
							showToast("暂不支持此功能");
						} 
						else if(msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2){ // 收藏新知识
							showLoadingDialog();
							KnowledgeReqUtil.doUpdateCollectKnowledge(mContext, MChatRecordBrowserActivity.this, 
									Long.parseLong(msg.getJtFile().mTaskId), Integer.parseInt(msg.getJtFile().reserved1), "", null);
						}
						else if (msg.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 关注需求
							showLoadingDialog();
							UserReqUtil.doFocusRequirement(MChatRecordBrowserActivity.this, MChatRecordBrowserActivity.this,
									UserReqUtil.getDoFocusRequirementParams(msg.getJtFile().mTaskId, true), null);
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE || msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE
								|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE || msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE) { // 人脉转发到会议
							ArrayList<JTFile> jtFiles = new ArrayList<JTFile>();
							jtFiles.add(msg.getJtFile());
							ENavigate.startTransmitMeetingList(mContext, jtFiles, meetingDetail.getId());
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_CONFERENCE) { // 会议转发到会议
							ArrayList<JTFile> jtFiles = new ArrayList<JTFile>();
							jtFiles.add(msg.getJtFile());
							ENavigate.startTransmitMeetingList(mContext, jtFiles, meetingDetail.getId());
						}
						break;
					case 1: /******************************/
						if (msg.getType() == IMBaseMessage.TYPE_TEXT) { // 文本转发到会议
							JTFile jtFile = new JTFile();
							jtFile.mType = JTFile.TYPE_TEXT;
							jtFile.mFileName = msg.getContent();
							ArrayList<JTFile> jtFiles = new ArrayList<JTFile>();
							jtFiles.add(jtFile);
							ENavigate.startTransmitMeetingList(mContext, jtFiles, meetingDetail.getId());
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_IMAGE || msg.getType() == IMBaseMessage.TYPE_VIDEO
								|| msg.getType() == IMBaseMessage.TYPE_FILE || msg.getType() == IMBaseMessage.TYPE_AUDIO) { // 图片、视频、文件、语音转发到会议

							// 如果文件已上传，直接转发
							if (!TextUtils.isEmpty(msg.getJtFile().mUrl)) {
								ArrayList<JTFile> jtFiles = new ArrayList<JTFile>();
								jtFiles.add(msg.getJtFile());
								ENavigate.startTransmitMeetingList(mContext, jtFiles, meetingDetail.getId());
								return;
							}
							// 首先查看原文件是否存在
							String originalFilePath = localFileDBManager.query(App.getUserID(), msg.getMessageID());
							if (!TextUtils.isEmpty(originalFilePath)) {
								File originalFile = new File(originalFilePath);
								if (originalFile.exists()) {
									ArrayList<JTFile> jtFiles = new ArrayList<JTFile>();
									msg.getJtFile().mLocalFilePath = originalFile.getAbsolutePath();
									jtFiles.add(msg.getJtFile());
									ENavigate.startTransmitMeetingList(mContext, jtFiles, meetingDetail.getId());
									return;
								}
							}
							// 其次查看文件是否下载完成
							File dir = EUtil.getMeetingChatFileDir(mContext, msg.getJtFile().mType, meetingId, topicId);
							String typeStr = getJTFileTypeString(msg.getJtFile());
							if (dir != null) {
								File downloadFile = new File(dir, msg.getJtFile().mFileName);
								if (downloadFile.length() == msg.getJtFile().mFileSize) {
									ArrayList<JTFile> jtFiles = new ArrayList<JTFile>();
									msg.getJtFile().mLocalFilePath = downloadFile.getAbsolutePath();
									jtFiles.add(msg.getJtFile());
									ENavigate.startTransmitMeetingList(mContext, jtFiles, meetingDetail.getId());
									return;
								} else {
									showToast(typeStr + "尚未下载完成，请稍后再试");
								}
							} else {
								showToast("无法访问" + typeStr + "缓存目录");
							}
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE 
								|| msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 知识、新知识转发到会议
							ArrayList<JTFile> jtFiles = new ArrayList<JTFile>();
							jtFiles.add(msg.getJtFile());
							ENavigate.startTransmitMeetingList(mContext, jtFiles, meetingDetail.getId());
						}
						else if (msg.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 需求转发到会议
							ArrayList<JTFile> jtFiles = new ArrayList<JTFile>();
							jtFiles.add(msg.getJtFile());
							ENavigate.startTransmitMeetingList(mContext, jtFiles, meetingDetail.getId());
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE 
								|| msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE
								|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE 
								|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE) { // 人脉转发到畅聊
							ENavigate.startShareActivity(MChatRecordBrowserActivity.this, msg.getJtFile());
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_CONFERENCE) { // 会议转发到畅聊
							ENavigate.startShareActivity(MChatRecordBrowserActivity.this, msg.getJtFile());
						}
						break;
					case 2:
						/******************************/
						if (msg.getType() == IMBaseMessage.TYPE_TEXT) { // 文本转发到畅聊

							JTFile jtFile = new JTFile();
							jtFile.setmType(JTFile.TYPE_TEXT);
							jtFile.mFileName = msg.getContent();
							ENavigate.startShareActivity(MChatRecordBrowserActivity.this, jtFile);
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_IMAGE 
								|| msg.getType() == IMBaseMessage.TYPE_VIDEO
								|| msg.getType() == IMBaseMessage.TYPE_FILE 
								|| msg.getType() == IMBaseMessage.TYPE_AUDIO) { // 图片、视频、文件、语音转发到畅聊

							// 文件是否已上传
							if (!TextUtils.isEmpty(msg.getJtFile().mUrl)) { // 直接转发
								ENavigate.startShareActivity(MChatRecordBrowserActivity.this, msg.getJtFile());
								return;
							}
							// 原文件是否存在
							String originalFilePath = localFileDBManager.query(App.getUserID(), msg.getMessageID());
							if (!TextUtils.isEmpty(originalFilePath)) {
								File originalFile = new File(originalFilePath);
								if (originalFile.exists()) {
									msg.getJtFile().mLocalFilePath = originalFile.getAbsolutePath();
									ENavigate.startShareActivity(MChatRecordBrowserActivity.this, msg.getJtFile());
									return;
								}
							}
							// 其次查看文件是否下载完成
							File dir = EUtil.getMeetingChatFileDir(mContext, msg.getJtFile().mType, meetingId, topicId);
							String typeStr = getJTFileTypeString(msg.getJtFile());
							if (dir != null) {
								File downloadFile = new File(dir, msg.getJtFile().mFileName);
								if (downloadFile.length() == msg.getJtFile().mFileSize) {
									msg.getJtFile().mLocalFilePath = downloadFile.getAbsolutePath();
									ENavigate.startShareActivity(MChatRecordBrowserActivity.this, msg.getJtFile());
									return;
								} else {
									showToast(typeStr + "尚未下载完成，请稍后再试");
								}
							} else {
								showToast("无法访问" + typeStr + "缓存目录");
							}
						} 
						else if (msg.getType() == JTFile.TYPE_KNOWLEDGE 
								|| msg.getType() == JTFile.TYPE_KNOWLEDGE2) { // 知识、新知识转发到畅聊
							ENavigate.startShareActivity(MChatRecordBrowserActivity.this, msg.getJtFile());
						} 
						else if (msg.getType() == JTFile.TYPE_REQUIREMENT) { // 需求转发到畅聊
							ENavigate.startShareActivity(MChatRecordBrowserActivity.this, msg.getJtFile());
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE 
								|| msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE
								|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE 
								|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE) { // 删除人脉
							deleteItem(msg, position);
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_CONFERENCE) { // 删除会议
							deleteItem(msg, position);
						}
						break;
					case 3:
						/******************************/
						if (msg.getType() == IMBaseMessage.TYPE_TEXT 
							|| msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE
							|| msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2 
							|| msg.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 删除文本、知识、新知识、需求
							deleteItem(msg, position);
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_IMAGE || msg.getType() == IMBaseMessage.TYPE_VIDEO
								|| msg.getType() == IMBaseMessage.TYPE_FILE || msg.getType() == IMBaseMessage.TYPE_AUDIO) { // 删除图片、视频、文件、语音

							// 是否在下载队列
							if (downloadService != null) {
								if (downloadService.isTaskExist(msg.getMessageID())) {
									downloadService.removeTask(msg.getMessageID());
								}
							}
							// 删除条目
							deleteItem(msg, position);
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE || msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE
								|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE || msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE) { // 分享人脉

							Intent intent = new Intent(Intent.ACTION_SEND);
							intent.setType("text/plain");
							intent.putExtra(Intent.EXTRA_TEXT, "金桐网，您的一站式投融资平台。http://www.gintong.com/");
							startActivity(Intent.createChooser(intent, "分享至"));
						}
						break;
					case 4: /******************************/
						if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE 
							|| msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 分享知识、新知识
							Intent intent = new Intent(Intent.ACTION_SEND);
							intent.setType("text/plain");
							intent.putExtra(Intent.EXTRA_TEXT, msg.getJtFile().mFileName + msg.getJtFile().mUrl);
							startActivity(Intent.createChooser(intent, "分享至"));
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 分享需求
							Intent intent = new Intent(Intent.ACTION_SEND);
							intent.setType("text/plain");
							intent.putExtra(Intent.EXTRA_TEXT, "金桐网，您的一站式投融资平台。http://www.gintong.com/");
							startActivity(Intent.createChooser(intent, "分享至"));
						} 
						else if (msg.getType() == IMBaseMessage.TYPE_TEXT
								|| msg.getType() == IMBaseMessage.TYPE_IMAGE
								|| msg.getType() == IMBaseMessage.TYPE_VIDEO) { // 文本、图片、语音保存到会议笔记
							showLoadingDialog();
							ConferenceReqUtil.doAddNoteDetailByChat(mContext, MChatRecordBrowserActivity.this, meetingId, msg.getMessageID(),
									TaskIDMaker.getTaskId(App.getUserName()), null);
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

		public ChatBaseAdapter(Context context) {
			this.mContext = context;
			inflater = LayoutInflater.from(context);
			parser = SmileyParser.getInstance(context);
			parser2 = SmileyParser2.getInstance(context);
			;
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
					holder.rightLayout = (LinearLayout) convertView.findViewById(R.id.right_letter_layout);

					holder.leftContent = (TextView) convertView.findViewById(R.id.left_letter_content);
					holder.rightContent = (TextView) convertView.findViewById(R.id.right_letter_content);

					holder.leftHead = (ImageView) convertView.findViewById(R.id.head);
					holder.rightHead = (ImageView) convertView.findViewById(R.id.right_head);
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
					holder.leftShareImageIv = (ImageView) convertView.findViewById(R.id.leftShareImageIv);
					holder.leftShareTypeIv = (ImageView) convertView.findViewById(R.id.leftShareTypeIv);
					holder.leftShareTitleTv = (TextView) convertView.findViewById(R.id.leftShareTitleTv);
					holder.leftShareContentTv = (TextView) convertView.findViewById(R.id.leftShareContentTv);

					holder.rightShareLl = (LinearLayout) convertView.findViewById(R.id.rightShareLl);
					holder.rightShareImageIv = (ImageView) convertView.findViewById(R.id.rightShareImageIv);
					holder.rightShareTypeIv = (ImageView) convertView.findViewById(R.id.rightShareTypeIv);
					holder.rightShareTitleTv = (TextView) convertView.findViewById(R.id.rightShareTitleTv);
					holder.rightShareContentTv = (TextView) convertView.findViewById(R.id.rightShareContentTv);

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

					holder.leftCnsRl = (RelativeLayout) convertView.findViewById(R.id.leftCnsRl);
					holder.leftCnsIcon = (ImageView) convertView.findViewById(R.id.leftCnsIcon);
					holder.leftCnsTitle = (TextView) convertView.findViewById(R.id.leftCnsTitle);
					holder.leftCnsName1 = (TextView) convertView.findViewById(R.id.leftCnsName1);
					holder.leftCnsName2 = (TextView) convertView.findViewById(R.id.leftCnsName2);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
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

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.head: // 详情
						case R.id.right_head:
							startUserActivity(item.getSenderID());
							break;
						case R.id.leftShareLl: // 左侧需求、左侧知识、左侧会议
						case R.id.rightShareLl:// 右侧需求、右侧知识、右侧会议
							if (item.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 需求
							} else if (item.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 知识
								ENavigate.startShareDetailActivity(MChatRecordBrowserActivity.this, item.getJtFile().toKnowledgeMini());
							} else if (item.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 新知识
								ENavigate.startKnowledgeOfDetailActivity(MChatRecordBrowserActivity.this, Long.parseLong(item.getJtFile().mTaskId),
										Integer.parseInt(item.getJtFile().getReserved1()));
							} else if (item.getType() == IMBaseMessage.TYPE_CONFERENCE) { // 会议
								ENavigate.startSquareActivity(mContext, Long.parseLong(item.getJtFile().mTaskId),0);
							}
							break;
						case R.id.leftPlayIv: // 左侧视频
						case R.id.rightPlayIv: // 右侧视频
							if (v.getTag() == null) {
								ENavigate.startMChatFilePreviewActivity(mContext, item.getJtFile(), item.getMessageID(), meetingId, topicId);
							} else {
								OpenFiles.open(mContext, v.getTag() + ""); // 打开视频
							}
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
							if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType()) {
								isOrg = false;
								isOnline = false;
							} else if (IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
								isOrg = false;
								isOnline = true;
							} else if (IMBaseMessage.TYPE_ORG_OFFLINE == item.getType()) {
								isOrg = true;
								isOnline = false;
							} else if (IMBaseMessage.TYPE_ORG_ONLINE == item.getType()) {
								isOrg = true;
								isOnline = true;
							}
							if (isOrg) {
							} 
							else {
								ENavigate.startUserDetailsActivity(MChatRecordBrowserActivity.this, id, isOnline, isOnline ? ENavConsts.type_details_other
										: ENavConsts.type_details_share);
							}
							break;
						case R.id.leftImageRl: // 左侧图片
						case R.id.rightImageRl: // 右侧图片
							ENavigate.startHyImageBrowserActivity(MChatRecordBrowserActivity.this, meetingId, topicId, item.getMessageID());
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
						ImageLoader.getInstance().displayImage(App.getUser().getImage(), holder.rightHead, LoadImage.mHyDefaultHead);
						holder.rightName.setText(App.getNick());

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
									ImageLoader.getInstance().displayImage(Uri.fromFile(originalFile).toString(), holder.rightImageIv);
								} 
								else if(!TextUtils.isEmpty(item.getJtFile().mUrl)){ // 图片已上传
									ImageLoader.getInstance().displayImage(item.getJtFile().mUrl, holder.rightImageIv);
								}
								else{ // 图片不存在
									holder.rightImageIv.setImageResource(R.drawable.hy_chat_right_pic);
								}
							}
							// 长按事件
							holder.rightImageRl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightImageRl.setOnClickListener(clickListener);
						} 
						else if (IMBaseMessage.TYPE_AUDIO == item.getType()) { // 右侧语音

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
								int paramsWidth = 50 + (StringUtils.isEmpty(item.getJtFile().reserved2) ? 0 : Integer
										.parseInt(item.getJtFile().reserved2) * 5);
								// 最大不超过200dp
								paramsWidth = Math.min(paramsWidth, 200);
								// 设置长度
								LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.rightVoiceDurationLl.getLayoutParams();
								layoutParams.width = EUtil.convertDpToPx(paramsWidth);
								holder.rightVoiceDurationLl.setLayoutParams(layoutParams);
								// 语音文件
								final File file = new File(EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_AUDIO, meetingId, topicId),
										item.getJtFile().mFileName);
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
											startPlaying(file.getAbsolutePath(), holder.rightVoiceIv,
													Integer.parseInt(item.getJtFile().reserved2) * 1000);
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
															File dir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_AUDIO, meetingDetail.getId(),
																	topicDetail.getId());
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

								// 缩略图
								holder.rightShareImageIv.setVisibility(View.GONE);
								// 类型
								holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);
								// 标题
								int httpIndex = item.getJtFile().mSuffixName.indexOf("http");
								if (httpIndex < 0) {
									holder.rightShareTitleTv.setText(item.getJtFile().mSuffixName);
								} else if (httpIndex == 0) {
									holder.rightShareTitleTv.setText("");
								} else {
									holder.rightShareTitleTv.setText(item.getJtFile().mSuffixName.substring(0, httpIndex));
								}
								// 内容
								holder.rightShareContentTv.setText(item.getJtFile().mUrl);
							}
							// 长按事件
							holder.rightShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightShareLl.setOnClickListener(clickListener);
						} 
						else if (IMBaseMessage.TYPE_KNOWLEDGE2 == item.getType()) { // 右侧新知识

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {

								// 缩略图
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									ImageLoader.getInstance().displayImage(item.getJtFile().mUrl, holder.rightShareImageIv);
								}
								holder.rightShareImageIv.setVisibility(View.VISIBLE);
								// 类型
								holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);
								// 标题
								holder.rightShareTitleTv.setText(item.getJtFile().reserved2);
								holder.rightShareTitleTv.setVisibility(View.VISIBLE);
								// 内容
								holder.rightShareContentTv.setText(item.getJtFile().mSuffixName);
								holder.rightShareContentTv.setVisibility(View.VISIBLE);
							}
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

								// 缩略图
								holder.rightShareImageIv.setVisibility(View.VISIBLE);
								if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
									ImageLoader.getInstance().displayImage(item.getJtFile().mUrl, holder.rightShareImageIv);
								}
								// 类型
								holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_conference);
								// 会议标题
								holder.rightShareTitleTv.setText(item.getJtFile().mSuffixName);
								// 会议内容
								holder.rightShareContentTv.setText(item.getJtFile().reserved1);
							}
							// 长按事件
							holder.rightShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.rightShareLl.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType() || IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()
								|| IMBaseMessage.TYPE_ORG_OFFLINE == item.getType() || IMBaseMessage.TYPE_ORG_ONLINE == item.getType()) { // 右侧关系

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.GONE);
							holder.rightCnsName1.setVisibility(View.GONE);
							holder.rightCnsName2.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.VISIBLE);
							holder.rightCnsRl.setTag(item.getJtFile());

							if (item.getJtFile() != null) {
								final JTFile jtFile = item.getJtFile();
								if (!StringUtils.isEmpty(jtFile.mFileName)) {
									holder.rightCnsTitle.setText(jtFile.mFileName);
								}
								if (!StringUtils.isEmpty(jtFile.mSuffixName)) {
									holder.rightCnsName1.setText(jtFile.mSuffixName);
									holder.rightCnsName1.setVisibility(View.VISIBLE);
								}
								if (!StringUtils.isEmpty(jtFile.reserved1)) {
									holder.rightCnsName2.setText(jtFile.reserved1);
									holder.rightCnsName2.setVisibility(View.VISIBLE);
								}
								if (jtFile.mUrl == null) {
									jtFile.mUrl = "";
								}

								if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType() || IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
									ImageLoader.getInstance().displayImage(jtFile.mUrl, holder.rightCnsIcon, LoadImage.TYPE_IMAGE_connection80obj);
								} else {
									ImageLoader.getInstance().displayImage(jtFile.mUrl, holder.rightCnsIcon, LoadImage.TYPE_IMAGE_companyfriend80Obj);
								}

								holder.rightCnsRl.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View arg0) {

									}
								});
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

								// 缩略图
								holder.rightShareImageIv.setVisibility(View.GONE);
								// 类型
								holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_requirement);
								// 标题
								holder.rightShareTitleTv.setText(item.getJtFile().mFileName);
								// 内容
								holder.rightShareContentTv.setText(item.getJtFile().reserved1);
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

								// 首先检查原文件是否存在，其次是下载的文件
								String originalFilePath = localFileDBManager.query(App.getUserID(), item.getMessageID());
								File originalFile = null;
								if (!TextUtils.isEmpty(originalFilePath)) {
									originalFile = new File(originalFilePath);
								}
								if (originalFile == null || !originalFile.exists()) { // 原文件已不存在
									if (!TextUtils.isEmpty(item.getJtFile().mUrl)) { // 文件已上传
										File fileDir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_VIDEO, meetingId, topicId);
										if (fileDir != null) {
											File downloadFile = new File(fileDir, item.getJtFile().mFileName);
											if (downloadFile.length() == item.getJtFile().mFileSize) { // 文件已下载完成，点击直接打开
												holder.rightPlayIv.setTag(downloadFile.getAbsolutePath());
											} else { // 文件尚未下载完，点击跳转到预览页
												holder.rightPlayIv.setTag(null);
											}
										} else { // 文件尚未下载，点击跳转到预览页
											holder.rightPlayIv.setTag(null);
										}
										// 显示缩略图
										holder.rightVideoIv.setImageResource(R.drawable.hy_chat_right_pic);
										ImageLoader.getInstance().displayImage(item.getJtFile().reserved2, holder.rightVideoIv);
									} else { // 文件未上传
										holder.rightVideoIv.setImageResource(R.drawable.hy_chat_right_pic);
										holder.rightPlayIv.setTag(""); // 提示文件不存在
									}
								} else { // 本地文件存在，点击直接打开
											// 设置文件本地路径
									holder.rightPlayIv.setTag(originalFile.getAbsolutePath());
									// 显示缩略图
									Bitmap screenshotBitmap = EUtil.getVideoCompressedScreenshotBitmap(mContext, originalFile.getAbsolutePath());
									// 设置缩略图
									if (screenshotBitmap != null) {
										holder.rightVideoIv.setImageBitmap(screenshotBitmap);
									} else {
										holder.rightVideoIv.setImageResource(R.drawable.hy_chat_right_pic);
									}
								}
								// 长按事件
								holder.rightVideoIv.setOnLongClickListener(longClickListener);
								// 点击事件
								holder.rightPlayIv.setOnClickListener(clickListener);
							}
						} else if (IMBaseMessage.TYPE_FILE == item.getType()) { // 右侧文件

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.VISIBLE);

							if (item.getJtFile() != null) {

								holder.rightFileNameTv.setText(item.getJtFile().mFileName); // 文件名
								holder.rightFileSizeTv.setText(EUtil.formatFileSize(item.getJtFile().mFileSize)); // 文件大小
								String fileStatus = ""; // 文件状态
								if (TextUtils.isEmpty(item.getJtFile().mUrl)) { // 文件未上传或正在上传

									 // 文件不在上传队列
									holder.rightFileProgressPb.setVisibility(View.GONE);
									fileStatus = "打开";
									holder.rightFileStatusTv.setText(fileStatus);
									String filePath = localFileDBManager.query(App.getUserID(), item.getMessageID());
									if (!TextUtils.isEmpty(filePath)) {
										holder.rightFileLl.setTag(filePath);
									} 
									else {
										holder.rightFileLl.setTag("");
									}
								} 
								else { // 文件已上传

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
											} 
											else { // 已停止
												fileStatus = "继续下载";
											}
											holder.rightFileLl.setTag(null);
										} 
										else { // 未下载
											File fileDir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_FILE, meetingId, topicId);
											if (fileDir != null) {
												File downloadFile = new File(fileDir, item.getJtFile().mFileName);
												if (downloadFile.length() == item.getJtFile().mFileSize) { // 文件已下载完成，点击直接打开
													fileStatus = "打开";
													holder.rightFileProgressPb.setVisibility(View.GONE);
													holder.rightFileLl.setTag(downloadFile.getAbsoluteFile());
												} 
												else if (downloadFile.length() > 0) { // 文件尚未下载完，点击跳转到预览页
													holder.rightFileProgressPb.setProgress((int) (downloadFile.length() * 1.0
															/ item.getJtFile().mFileSize * 100));
													holder.rightFileProgressPb.setVisibility(View.VISIBLE);
													fileStatus = "继续下载";
													holder.rightFileLl.setTag(null);
												} 
												else {
													fileStatus = "未下载";
													holder.rightFileProgressPb.setVisibility(View.GONE);
													holder.rightFileLl.setTag(null);
												}
											} 
											else { // 文件尚未下载，点击跳转到预览页
												fileStatus = "未下载";
												holder.rightFileProgressPb.setVisibility(View.GONE);
												holder.rightFileLl.setTag(null);
											}
										}
									} 
									else { // 原文件存在
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
						} 
						else { // 右侧文本

							holder.rightVideoRl.setVisibility(View.GONE);
							holder.rightShareLl.setVisibility(View.GONE);
							holder.rightImageRl.setVisibility(View.GONE);
							holder.rightFileLl.setVisibility(View.GONE);
							holder.rightCnsRl.setVisibility(View.GONE);
							holder.rightVoiceLl.setVisibility(View.GONE);
							holder.rightContent.setVisibility(View.VISIBLE);
							CharSequence dd = parser.addSmileySpans(body);
							CharSequence dd1 = parser2.addSmileySpans(dd);
							holder.rightContent.setText(dd1);
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
						ImageLoader.getInstance().displayImage(getImageByMessage(item), holder.leftHead, LoadImage.mHyDefaultHead);
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
							CharSequence dd1 = parser2.addSmileySpans(dd);
							holder.leftContent.setText(dd1);

							if (item.getContent().startsWith("http://") || item.getContent().startsWith("https://")) {
								Linkify.addLinks(holder.leftContent, Linkify.WEB_URLS);
							}
						} 
						else if (IMBaseMessage.TYPE_IMAGE == item.getType()) { // 左侧图片

							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftImageRl.setVisibility(View.VISIBLE);

							// 显示图片
							if (item.getJtFile() != null 
									&& !TextUtils.isEmpty(item.getJtFile().mUrl)) {
								ImageLoader.getInstance().displayImage(item.getJtFile().mUrl, holder.leftImageIv);
							} 
							else {
								holder.leftImageIv.setImageResource(R.drawable.hy_chat_right_pic);
							}
							// 长按事件
							holder.leftImageRl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftImageRl.setOnClickListener(clickListener);
						} 
						else if (IMBaseMessage.TYPE_KNOWLEDGE == item.getType()) { // 左侧知识

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);

							// 缩略图
							holder.leftShareImageIv.setVisibility(View.GONE);
							// 类型
							holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);
							// 标题
							int httpIndex = item.getJtFile().mSuffixName.indexOf("http");
							if (httpIndex < 0) {
								holder.leftShareTitleTv.setText(item.getJtFile().mSuffixName);
							} 
							else if (httpIndex == 0) {
								holder.leftShareTitleTv.setText("");
							} 
							else {
								holder.leftShareTitleTv.setText(item.getJtFile().mSuffixName.substring(0, httpIndex));
							}
							// 内容
							holder.leftShareContentTv.setText(item.getJtFile().mUrl);
							// 长按事件
							holder.leftShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftShareLl.setOnClickListener(clickListener);
						} 
						else if (IMBaseMessage.TYPE_KNOWLEDGE2 == item.getType()) { // 左侧新知识

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);

							// 缩略图
							if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
								ImageLoader.getInstance().displayImage(item.getJtFile().mUrl, holder.leftShareImageIv);
							}
							holder.leftShareImageIv.setVisibility(View.VISIBLE);
							// 类型
							holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);
							// 标题
							holder.leftShareTitleTv.setText(item.getJtFile().reserved2);
							holder.leftShareTitleTv.setVisibility(View.VISIBLE);
							// 内容
							holder.leftShareContentTv.setText(item.getJtFile().mSuffixName);
							holder.leftShareContentTv.setVisibility(View.VISIBLE);
							// 长按事件
							holder.leftShareLl.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftShareLl.setOnClickListener(clickListener);
						} 
						else if (IMBaseMessage.TYPE_REQUIREMENT == item.getType()) { // 左侧需求

							holder.leftImageRl.setVisibility(View.GONE);
							holder.leftContent.setVisibility(View.GONE);
							holder.leftVoiceLl.setVisibility(View.GONE);
							holder.leftFileLl.setVisibility(View.GONE);
							holder.leftVideoRl.setVisibility(View.GONE);
							holder.leftCnsRl.setVisibility(View.GONE);
							holder.leftShareLl.setVisibility(View.VISIBLE);
							holder.leftShareLl.setTag(item.getJtFile());

							// 缩略图
							holder.leftShareImageIv.setVisibility(View.GONE);
							// 类型
							holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_requirement);
							// 标题
							holder.leftShareTitleTv.setText(item.getJtFile().mFileName);
							// 内容
							holder.leftShareContentTv.setText(item.getJtFile().reserved1);

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

							// 缩略图
							holder.leftShareImageIv.setVisibility(View.VISIBLE);
							if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
								ImageLoader.getInstance().displayImage(item.getJtFile().mUrl, holder.leftShareImageIv);
							}
							// 类型
							holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_conference);
							// 会议标题
							holder.leftShareTitleTv.setText(item.getJtFile().mSuffixName);
							// 会议介绍
							holder.leftShareContentTv.setText(item.getJtFile().reserved1);
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

								// 设置默认图片
								holder.leftVideoIv.setImageResource(R.drawable.hy_chat_right_pic);
								// 本地文件是否存在
								File fileDir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_VIDEO, meetingId, topicId);
								if (fileDir != null) {
									File localFile = new File(fileDir, item.getJtFile().mFileName);
									if (localFile.length() == item.getJtFile().mFileSize) { // 下载完成
										// 设置文件本地路径
										holder.leftPlayIv.setTag(localFile.getAbsoluteFile());
										// 显示缩略图
										Bitmap screenshotBitmap = EUtil.getVideoCompressedScreenshotBitmap(mContext, localFile.getAbsolutePath());
										if (screenshotBitmap != null) {
											holder.leftVideoIv.setImageBitmap(screenshotBitmap);
										} else {
											ImageLoader.getInstance().displayImage(item.getJtFile().reserved2, holder.leftVideoIv);
										}
									} else {
										holder.leftPlayIv.setTag(null);
										ImageLoader.getInstance().displayImage(item.getJtFile().reserved2, holder.leftVideoIv);
									}
								} else {
									holder.leftPlayIv.setTag(null);
									ImageLoader.getInstance().displayImage(item.getJtFile().reserved2, holder.leftVideoIv);
								}
							}
							// 长按事件
							holder.leftVideoIv.setOnLongClickListener(longClickListener);
							// 点击事件
							holder.leftPlayIv.setOnClickListener(clickListener);
						} else if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType() || IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()
								|| IMBaseMessage.TYPE_ORG_OFFLINE == item.getType() || IMBaseMessage.TYPE_ORG_ONLINE == item.getType()) { // 左侧关系

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

							if (item.getJtFile() != null) {
								final JTFile jtFile = item.getJtFile();
								if (!StringUtils.isEmpty(jtFile.mFileName)) {
									holder.leftCnsTitle.setText(jtFile.mFileName);
								}
								if (!StringUtils.isEmpty(jtFile.mSuffixName)) {
									holder.leftCnsName1.setText(jtFile.mSuffixName);
									holder.leftCnsName1.setVisibility(View.VISIBLE);
								}
								if (!StringUtils.isEmpty(jtFile.reserved1)) {
									holder.leftCnsName2.setText(jtFile.reserved1);
									holder.leftCnsName2.setVisibility(View.VISIBLE);
								}

								if (jtFile.mUrl == null) {
									jtFile.mUrl = "";
								}

								if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType() || IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
									ImageLoader.getInstance().displayImage(jtFile.mUrl, holder.leftCnsIcon, LoadImage.TYPE_IMAGE_connection80obj);
								} else {
									ImageLoader.getInstance().displayImage(jtFile.mUrl, holder.leftCnsIcon, LoadImage.TYPE_IMAGE_companyfriend80Obj);
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
											holder.leftFileProgressPb
													.setProgress((int) (localFile.length() * 1.0 / item.getJtFile().mFileSize * 100));
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
						} 
						else if (IMBaseMessage.TYPE_AUDIO == item.getType()) { // 左侧语音

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
								int paramsWidth = 50 + (StringUtils.isEmpty(item.getJtFile().reserved2) ? 0 : Integer
										.parseInt(item.getJtFile().reserved2) * 5);
								// 最大不超过200dp
								paramsWidth = Math.min(paramsWidth, 200);
								// 设置长度
								LinearLayout.LayoutParams layoutParams = (LayoutParams) holder.leftVoiceDurationLl.getLayoutParams();
								layoutParams.width = EUtil.convertDpToPx(paramsWidth);
								holder.leftVoiceDurationLl.setLayoutParams(layoutParams);
								// 语音文件
								final File file = new File(EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_AUDIO, meetingDetail.getId(), topicDetail.getId()),
										item.getJtFile().mFileName);
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
											startPlaying(file.getAbsolutePath(), holder.leftVoiceIv,
													Integer.parseInt(item.getJtFile().reserved2) * 1000);
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
														File dir = EUtil.getMeetingChatFileDir(mContext, JTFile.TYPE_AUDIO, meetingDetail.getId(),
																topicDetail.getId());
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

		class ViewHolder {

			public View viewBG;
			public LinearLayout leftLayout;
			public LinearLayout rightLayout;
			public TextView leftContent;
			public TextView rightContent;
			public ImageView leftHead;
			public ImageView rightHead;
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
			public ImageView leftShareImageIv;
			public ImageView leftShareTypeIv;
			public TextView leftShareTitleTv;
			public TextView leftShareContentTv;

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

			public RelativeLayout leftCnsRl;
			public TextView leftCnsTitle;
			public ImageView leftCnsIcon;
			public TextView leftCnsName1;
			public TextView leftCnsName2;
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
	

	/**
	 * 数据部分
	 */
	@Override
	public void bindData(int tag, Object object) {
		if (hasDestroy()) {
			return;
		}
		if (tag == EAPIConsts.ReqType.FOCUS_REQUIREMENT) { // 关注需求
			dismissLoadingDialog();
			if(object != null){
				DataBox dataBox = (DataBox) object;
				if (dataBox.mIsSuccess) {
					showToast("关注成功");
				} 
				else {
					showToast("关注失败");
				}
			}
			else{
				showToast("关注失败");
			}
		}
		else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_ADD_NOTE_DETAIL_BY_CHAT) { // 会议畅聊添加到会议笔记

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
		} 
		else if(tag == EAPIConsts.KnoReqType.UpdateCollectKnowledge){ // 收藏知识
			dismissLoadingDialog();
			Map<String, Object> dataBox = (Map<String, Object>) object;
			if(dataBox != null){
				boolean success = false;
				if(dataBox.containsKey("succeed")){
					success = (Boolean) dataBox.get("succeed");
				}
				if(success){
					showToast("收藏成功");
				}
				else{
					showToast("收藏失败");
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
				} 
				else {
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
					} 
					else {
						return null;
					}
					operateType = (Integer) params[3];
					switch (operateType) {
					case TASK_READ: { // 读取缓存数据
						Pair<Integer, List<MeetingMessage>> pair = meetingRecordDBManager.queryContext(App.getUserID(), meetingId + "", topicId + "", searchMessageId, 50);
						if (pair != null) {
							return pair;
						}
						break;
					}
					case TASK_MERGE: 
						break;
					case TASK_SAVE: 
						break;
					default:
						break;
					}
				}
			} 
			catch (Exception e) {
				Log.d(TAG, e.getMessage() + "");
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			if (result != null) {
				// 更新新列表到列表页
				Pair<Integer, ArrayList<MeetingMessage>> pair = (Pair<Integer, ArrayList<MeetingMessage>>) result;
				listMessage = pair.second;
				setData(listMessage);
				final int position = pair.first;
				// 定位到指定消息位置
				handler.post(new Runnable() {
					@Override
					public void run() {
						listView.setSelection(position);
					}
				});
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
	public void removeHideMessage(List<MeetingMessage> listMsg){
		if(listMsg == null || listMsg.size() <= 0){
			return;
		}
		Iterator<MeetingMessage> iterator = listMsg.iterator();
		
		for(int i = 0; i < listMsg.size(); i++){  
	        if(!meetingRecordDBManager.queryVisibility(App.getUserID(), listMsg.get(i).getMessageID())){
				listMsg.remove(i);
				i--;
			}
		}
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
			if (newItem.getSenderID().equalsIgnoreCase("0")) {
				oldList.add(newItem);// 系统消息，直接添加到队尾
				break;
			}

			int oldSize = oldList.size();
			boolean blnFindInOld = false;
			for (int j = 0; j < oldSize; j++) {
				MeetingMessage oldItem = oldList.get(j);
				if (oldItem.getMessageID().equalsIgnoreCase(newItem.getMessageID())) {
					// 如果在老的中间找到了新消息相同的messageID,则用新的取代老的
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
}
