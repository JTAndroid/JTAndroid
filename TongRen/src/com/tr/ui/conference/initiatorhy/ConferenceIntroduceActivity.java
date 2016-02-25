package com.tr.ui.conference.initiatorhy;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tr.App;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.iflytek.Speech;
import com.tr.iflytek.Speech.SpeechOnRecognizerResultListener;
import com.tr.model.conference.JTFile2ForHY;
import com.tr.model.conference.MMeetingPic;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.demand.BrowesPhotoVideo;
import com.tr.ui.demand.MyView.DemandHorizontalListView;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.UpLoadingDialog;
import com.utils.common.FileUploader;
import com.utils.common.TaskIDMaker;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.Util;

/**
 * 页面传入数据：传音视频文件集合，图片文件集合
 * 
 * @author zhongshan
 * 
 */
public class ConferenceIntroduceActivity extends JBaseActivity implements IBindData, SpeechOnRecognizerResultListener, OnClickListener/*, OnFileDownloadListener */{

	public static final int REQUEST_CODE_BROWERS_ACTIVITY = 1001; // 启动关联的回调
	public static final int REQUEST_CODE_ADD_ACTIVITY = 1002; // 启动关联的回调

	/** 语音转文字内容 */
	private EditText txtContentTv;
	private Speech speech;
	/** 录音列表 */
//	private LinearLayout voiceRecordBLV;
	/** 添加图片视频 */
	private ImageView photoVideoIb;
	/** 将图片视频选择后展示 */
	private DemandHorizontalListView showBackPicVidHlv;
//	/** 附件 */
	private ListView accessoryLv;
//	/** 附件数量 */
	private TextView count_item;
	private String alterHyId;
	private String taskId;

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
	// 设置图片在加载中显示的图片
			.showImageOnLoading(R.drawable.hy_pic_loading)
			// 设置图片Uri为空或是错误的时候显示的图片
			.showImageForEmptyUri(R.drawable.hy_pic_loading)
			// 设置图片加载/解码过程中错误时候显示的图片
			.showImageOnFail(R.drawable.hy_pic_loading)
			// 设置下载的图片是否缓存在内存中
			.cacheInMemory(true)
			// 设置下载的图片是否缓存在SD卡中
			// .cacheOnDisk(true)
			// 保留Exif信息
			.considerExifParams(false)
			// 设置图片以如何的编码方式显示
			.imageScaleType(ImageScaleType.EXACTLY)
			// 设置图片的解码类型
			.bitmapConfig(Bitmap.Config.RGB_565).build();

	private PictureAdapter mPictureAdapter;
	private AccessoryLv accessoryAdapter;
	/*
	*//** 播放器 *//*
	private LocalPlayer mPlayer = new LocalPlayer();
	*//***//*
	private ArrayList<VoicePlayer> voiceList = new ArrayList<VoicePlayer>();
	private RelativeLayout rl_rec;
	private TextView tv_cancel;
	private TextView tv_complete;
	private ImageView volumeLevel;
	private Dialog recorderDialog;
	private TextView tv_message;
	private TimerTask mTimerTask;
	private Date date = new Date();
	*//** 播放时间 *//*
	private TextView time;
	*//** 进度条 *//*
	private SeekBar seekBar;
	*//** 正在播放的对象按钮 *//*
	private ImageView playIv;
	*//** 正在播放的对象 *//*
	private VoicePlayer indexPlay = null;
	private SimpleDateFormat simpleData = new SimpleDateFormat("mm:ss");
	*//** 录音对象 *//*
	private Recorder mRecorder;
	*//** 录音路径 *//*
	private String mVoicePath;
	*//** 记录进度条的 *//*
	private Timer mTimer;
	*//** 按钮状态 *//*
	private TextView play_btn_tv;
	private Map<Long, View> voiceView = new HashMap<Long, View>();
	private long timer;
	*//** 最大录制时间 单位秒 *//*
	private int recorderSizeTimer = 180000;
	*//** 当前录制时间 *//*
	private int recorderTimer = 0;
	*//** 录音时长监控 *//*
	private Timer timerRecorder;
	*//** 录音超时后的提示效果 *//*
	private TimerTask imterRecorder;
	*//** 计时器 结束时间 *//*
	private long endTime;*/

	/** 存放勾选图片/视频的路径集合 */
	private ArrayList<JTFile> selectedPictureSDAndNet = new ArrayList<JTFile>();// 本地加网络
	/** 判断是否要在 listView里面最后一项加载添加按钮 */
	private boolean tag = false;

	private Bitmap netBitmap;
	private Bitmap extractMiniThumb;

	// 本次上传的taskId;如果已经有过 上传的文件那么taskId=mTaskId,否则taskId =
	// TaskIDMaker.getTaskId(App.getUserName());

	/** 需要上传的集合 */
	// private ArrayList<FileUploader> mListJtImgVideo = new
	// ArrayList<FileUploader>(); // 图片

	// 传递来的图集
	private ArrayList<MMeetingPic> mMeetingPicList = new ArrayList<MMeetingPic>();
	// 传递来的文件集
	private ArrayList<JTFile2ForHY> mMeetingFileList = new ArrayList<JTFile2ForHY>();

	// 返回给前页的图集
	private ArrayList<MMeetingPic> mMeetingPicForList = new ArrayList<MMeetingPic>();
	// 返回给前页的文件集
	private ArrayList<JTFile2ForHY> mMeetingFileForList = new ArrayList<JTFile2ForHY>();

	/** 介绍中 附件 上传集合类 */
	private ArrayList<FileUploader> mListUploader = new ArrayList<FileUploader>();
	/** 介绍中 音频和视频 上传 集合类 */
	private ArrayList<FileUploader> mListVoiceVideoFileUpLoader = new ArrayList<FileUploader>();
	/** 介绍中 图片上传 集合类 */
	private ArrayList<FileUploader> mListPicUpLoader = new ArrayList<FileUploader>();

	/** 上传：图片文件集合 */
	private ArrayList<JTFile> fileImageList = new ArrayList<JTFile>();
	/** 上传：视频文件集合 */
	private ArrayList<JTFile> fileVideoList = new ArrayList<JTFile>();
	/** 上传：音频文件集合 */
	private ArrayList<JTFile> fileVoiceList = new ArrayList<JTFile>();
	/** 上传 sd卡 中文件集合（附件中的文件都是文档或压缩文件） */
	private ArrayList<JTFile> fileListSD = new ArrayList<JTFile>();// 本地
	/** 视频音频 文件集合 */
	private ArrayList<JTFile> fileVoiceVideoList = new ArrayList<JTFile>();

	private List<JTFile> fileListSDAndNet = new ArrayList<JTFile>();// 本地加网络
	private List<JTFile> fileListNet = new ArrayList<JTFile>();// 加网络

	/**
	 * true:正在上传 false上传完成
	 */
	private boolean isUploading;

	// 上传总数
	private int totalCount;
	// 图片上传成功总数
	private int picSuccessCount;
	// 音视频上传成功总数
	private int voiceVideoSuccessCount;
	// 附件上传成功总数
	private int fileSuccessCount;

	private Handler handlerVideo = new Handler() {
		public void handleMessage(Message msg) {
			((ImageView) msg.obj).setImageBitmap(extractMiniThumb);
		};
	};
	private Handler handlerNetVideo = new Handler() {
		public void handleMessage(Message msg) {
			((ImageView) msg.obj).setImageBitmap(netBitmap);
		};
	};

//	/**音频文件handler*/
//	private Handler handler = new Handler() {
//		public void dispatchMessage(android.os.Message msg) {
//			if (msg.what == 1) {
//				date.setTime(timer);
//				time.setText(simpleData.format(date));
//			} else if (msg.what == 2) {
//				mRecorder.stopRecord();
//				endTime = recorderTimer;
//				mRecorder = null;
//				tv_message.setText("录制时间已经超时，是否保存本次录音？");
//			}else if (msg.what == 3) {
//				// 音频下载成功
//				JTFile jtFile = (JTFile) msg.obj;
//				// 点击确定按钮
//				VoicePlayer voPlayer = new VoicePlayer();
//				File voiceFile = new File(jtFile.mLocalFilePath);
//				voPlayer.file = voiceFile;
//				MediaPlayer mediaPlayer = new MediaPlayer();
//				try {
//					mediaPlayer.setDataSource(jtFile.getmUrl());
//					mediaPlayer.prepare();
//					int duration = mediaPlayer.getDuration();
//					voPlayer.time = duration;
//					mediaPlayer.release();
//					mediaPlayer = null;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
////				addPlayView(voPlayer, jtFile);
//			}
//		};
//	};

	/** 介绍类型：会议介绍，主讲人议题介绍 */
	private enum IntroduceType {
		meetingTopicIntroduce, speakerTopicIntroduce
	}

	private IntroduceType introduceType;
	/** 议题设置 */
	private MMeetingTopicQuery mMeetingTopicQuery;

	@Override
	public void initJabActionBar() {
		ActionBar actionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, actionBar, "介绍", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_create_label_ok, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_ok:
			uploading();// 上传
			break;
		case android.R.id.home:// 返回
			// close();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_introduce);
		initView();
		getParamer();
		initControl();

	}

	private void initView() {
		//2015-08-11主讲人设置>介绍，1标题错误‘金桐’变更为‘介绍’；2取消上传附件功能；3音频功能取消；与IOS一致
		
		findViewById(R.id.img_hint_tv).setVisibility(View.VISIBLE);
		findViewById(R.id.accessory_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.voiceCharIv).setOnClickListener(this);
//		findViewById(R.id.voiceRecord).setOnClickListener(this);
		findViewById(R.id.introduce_voice).setVisibility(View.GONE);
		photoVideoIb = (ImageView) findViewById(R.id.photoVideoIb);
		findViewById(R.id.otherLl).setOnClickListener(this);
		photoVideoIb.setOnClickListener(this);

		txtContentTv = (EditText) findViewById(R.id.txtContentTv);
//		voiceRecordBLV = (LinearLayout) findViewById(R.id.voiceRecordBLV);
		showBackPicVidHlv = (DemandHorizontalListView) findViewById(R.id.showBackPicVidHlv);
		accessoryLv = (ListView) findViewById(R.id.accessoryLv);
		count_item = (TextView) findViewById(R.id.count_item);
	}

	private void getParamer() {
		alterHyId = InitiatorDataCache.getInstance().isAlterHyId == 0 ? "" : InitiatorDataCache.getInstance().isAlterHyId + "";
		Intent intent = getIntent();
		if (intent != null) {
			if (intent.hasExtra("mMeetingPicList")) {
				mMeetingPicList = (ArrayList<MMeetingPic>) intent.getSerializableExtra("mMeetingPicList");
				introduceType = IntroduceType.meetingTopicIntroduce;
			}
			if (intent.hasExtra("mMeetingFileList")) {
				mMeetingFileList = (ArrayList<JTFile2ForHY>) intent.getSerializableExtra("mMeetingFileList");
				introduceType = IntroduceType.meetingTopicIntroduce;
			}

			if (intent.hasExtra("mMeetingTopicQuery")) {
				mMeetingTopicQuery = (MMeetingTopicQuery) intent.getSerializableExtra("mMeetingTopicQuery");
				if (mMeetingTopicQuery == null) {
					mMeetingTopicQuery = new MMeetingTopicQuery();
				}
				// 议题附件任务id 统一
				taskId = mMeetingTopicQuery.getTaskId();
				if (StringUtils.isEmpty(taskId)) {
					taskId = TaskIDMaker.getTaskId(App.getUserName());
				}
				mMeetingPicList = (ArrayList<MMeetingPic>) mMeetingTopicQuery.getListMeetingPic();
				mMeetingFileList = (ArrayList<JTFile2ForHY>) mMeetingTopicQuery.getListMeetingFile();
				introduceType = IntroduceType.speakerTopicIntroduce;
			}

		}
		if (mMeetingPicList == null) {
			mMeetingPicList = new ArrayList<MMeetingPic>();
		}
		if (mMeetingFileList == null) {
			mMeetingFileList = new ArrayList<JTFile2ForHY>();
		}
		for (MMeetingPic item : mMeetingPicList) {
			JTFile jtFile = item.toJTFile();
			fileImageList.add(jtFile);// 加入图片集待显示
		}
		for (JTFile2ForHY item : mMeetingFileList) {
			JTFile jtfile = item.toJtfile();
			if (jtfile.mType == 2) {// 视频
				fileVideoList.add(jtfile);
			} else if (jtfile.mType == 4) {// 音频
//				fileVoiceList.add(jtfile);
//				FileDownloader fileDownloader = new FileDownloader(this, jtfile, this);
//				fileDownloader.start();
			} else if (jtfile.mType == 3) {// 附件
				fileListSD.add(jtfile);
				fileListSDAndNet.add(jtfile);
			}
		}
		// 会议附件的任务id统一
		/** 如果全局中taskId为空说明是创建会议或者修改会议没有上传过 */
		if (StringUtils.isEmpty(InitiatorDataCache.getInstance().taskId)) {
			InitiatorDataCache.getInstance().taskId = TaskIDMaker.getTaskId(App.getUserName());
		}
		if (StringUtils.isEmpty(taskId)) {
			taskId = InitiatorDataCache.getInstance().taskId;
		}
		// 至此将议题和会议taskId统一到界面的 taskId参数。
		switch (introduceType) {
		case meetingTopicIntroduce:
			txtContentTv.setText(InitiatorDataCache.getInstance().introduce.contentText);
			break;
		case speakerTopicIntroduce:
			txtContentTv.setText(mMeetingTopicQuery.getTopicDesc());
			break;
		}
		// 初始化显示图片和视频的集合
		selectedPictureSDAndNet.addAll(fileImageList);
		selectedPictureSDAndNet.addAll(fileVideoList);
	}

	private void initControl() {
		mPictureAdapter = new PictureAdapter();
		accessoryAdapter = new AccessoryLv();
		accessoryLv.setAdapter(accessoryAdapter);
		showBackPicVidHlv.setAdapter(mPictureAdapter);
		showBackPicVidHlv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position < selectedPictureSDAndNet.size()) {// 浏览图片
					Intent intent = new Intent(ConferenceIntroduceActivity.this, BrowesPhotoVideo.class);
					intent.putExtra("index", position);
					intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE, selectedPictureSDAndNet);
					intent.putExtra(ENavConsts.DEMAND_BROWER_DELETE, true);
					ConferenceIntroduceActivity.this.startActivityForResult(intent, REQUEST_CODE_BROWERS_ACTIVITY);
				} else {// 选择图片
					boolean b = false;
					for (JTFile imageItem : selectedPictureSDAndNet) {
						if (imageItem.mType == 2) {
							b = true;
							break;
						}
					}
					ENavigate.startSelectPictureActivity(ConferenceIntroduceActivity.this, REQUEST_CODE_ADD_ACTIVITY, selectedPictureSDAndNet, b);
				}
			}
		});
		upLoadingDialog = new UpLoadingDialog(this);
		showImage();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 语音转文字
		case R.id.voiceCharIv:
			if (speech == null) {
				speech = new Speech(ConferenceIntroduceActivity.this);
				speech.init();
			}
			speech.startSpeech();
			break;
//		// 录音
//		case R.id.voiceRecord:
//			startRecord();
//			break;
		// 选择图片
		case R.id.photoVideoIb:
			boolean b = false;
			for (JTFile imageItem : selectedPictureSDAndNet) {
				if (imageItem.mType == 2) {
					b = true;
					break;
				}
			}
			ENavigate.startSelectPictureActivity(ConferenceIntroduceActivity.this, REQUEST_CODE_ADD_ACTIVITY, selectedPictureSDAndNet, b);
			break;
		// 选择附件
		case R.id.otherLl:
			ENavigate.startAccessoryActivity(this, fileListSD, 255);
			break;
		}
	}

	/**
	 * 开始录音
	 */
//	private void startRecord() {
//		rl_rec = (RelativeLayout) View.inflate(this, R.layout.hy_meeting_dialog_road_show, null);
//		tv_cancel = (TextView) rl_rec.findViewById(R.id.hy_note_btn_recode_cancel);
//		tv_complete = (TextView) rl_rec.findViewById(R.id.hy_note_btn_recode_complete);
//		volumeLevel = (ImageView) rl_rec.findViewById(R.id.hy_meeting_volume_level);
//		tv_message = (TextView) rl_rec.findViewById(R.id.hy_meetion_voluem_text);
//
//		mRecorder = new Recorder();
//		mVoicePath = Recorder.getVoiceFilePath();
//		mRecorder.setOnVolumeChangedListener(new VolumeChangedListener() {
//			@Override
//			public void onVolumeChanged(double volume) {
//				switch ((int) (volume + 5) / 10) {
//				case 0:
//				case 1:
//				case 2:
//				case 3:
//				case 4:
//					volumeLevel.setBackgroundResource(R.drawable.hy_meeting_volume_level_0);
//					break;
//				case 5:
//				case 6:
//					volumeLevel.setBackgroundResource(R.drawable.hy_meeting_volume_level_1);
//					break;
//				case 7:
//				case 8:
//					volumeLevel.setBackgroundResource(R.drawable.hy_meeting_volume_level_2);
//					break;
//				case 9:
//				case 10:
//					volumeLevel.setBackgroundResource(R.drawable.hy_meeting_volume_level_3);
//					break;
//				}
//			}
//		});
//		tv_message.setText("请您开始说话");
//		if (null != mRecorder && false == mVoicePath.isEmpty()) {
//			recorderDialog = new Dialog(this, R.style.transmeeting_dialog);
//			recorderDialog.setContentView(rl_rec);
//			recorderDialog.setCancelable(false);
//			recorderDialog.show();
//			mRecorder.startRecord(mVoicePath);
//			startTimerRecorder();
//		}
//		tv_cancel.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				imterRecorder.cancel();
//				if (null != mRecorder) {
//					mRecorder.stopRecord();
//					mRecorder = null;
//				}
//				recorderDialog.dismiss();
//				File file = new File(mVoicePath);
//				if (file != null && file.exists()) {
//					file.delete();
//				}
//			}
//		});
//		tv_complete.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				imterRecorder.cancel();
//				if (null != mRecorder) {
//					mRecorder.stopRecord();
//					endTime = recorderTimer;
//					mRecorder = null;
//				}
//				// ：创建一个音频JTfile
//				JTFile jtFile = new JTFile();
//				jtFile.mTaskId = taskId;
//				jtFile.mModuleType = -1;
//				jtFile.setReserved3(alterHyId);// 如果当前为修改会议草稿 那么放入这个会议的id
//				jtFile.mLocalFilePath = mVoicePath;
//				recorderDialog.dismiss();
//				File voiceFile = new File(mVoicePath);
//				// 点击确定按钮
//				VoicePlayer voPlayer = new VoicePlayer();
//				voPlayer.file = voiceFile;
//				voPlayer.time = endTime;
//				fileVoiceList.add(jtFile);
//				addPlayView(voPlayer, jtFile);
//				// voiceList.add(voPlayer);
//				// voiceAdapter.notifyDataSetInvalidated();
//			}
//		});
//	}

//	/**
//	 * 开始计时统计
//	 */
//	private void startTimerRecorder() {
//		timerRecorder = new Timer();
//		recorderTimer = 0;
//		imterRecorder = new TimerTask() {
//
//			@Override
//			public void run() {
//				recorderTimer += 1000;// 加一
//				if (recorderTimer >= recorderSizeTimer) {
//					// 停止录音
//					handler.sendEmptyMessage(2);
//					imterRecorder.cancel();// 停止计时
//				}
//			}
//		};
//		timerRecorder.schedule(imterRecorder, 1000, 1000);// 一秒后开始计时，
//	}

	/**
	 * 创建一个音频播放器对象到界面中
	 * 
	 * @param voice
	 */
//	private void addPlayView(VoicePlayer play, JTFile jtFile) {
//		if (voiceList.add(play)) {
//			View convertView = View.inflate(this, R.layout.demand_play_item, null);
//			ImageView playIv = (ImageView) convertView.findViewById(R.id.play_start_iv); // 播放按钮
//			TextView timeTv = (TextView) convertView.findViewById(R.id.play_time_tv);// 播放时间进度
//			SeekBar seekBar = (SeekBar) convertView.findViewById(R.id.play_seekBar);// 播放时间长度
//			ImageView deleteIv = (ImageView) convertView.findViewById(R.id.play_delete_iv);// 删除按钮
//			TextView btnTv = (TextView) convertView.findViewById(R.id.play_btn_tv);//
//
//			deleteIv.setTag(play);
//			deleteIv.setTag(deleteIv.getId(), jtFile);
//			playIv.setTag(play);
//			timeTv.setTag(play);
//			seekBar.setTag(play);
//			deleteIv.setTag(play);
//			btnTv.setTag(play);
//
//			deleteIv.setOnClickListener(deleteBtnListener); // 删除方法
//			playIv.setOnClickListener(playBtnListener);
//			seekBar.setOnSeekBarChangeListener(seekBarChange);
//			// btnTv.setOnClickListener(threadBtn);
//			play.id = System.currentTimeMillis();
//			seekBar.setProgress(0);
//			date.setTime(play.time);
//			voiceView.put(play.id, convertView);
//			timeTv.setText(simpleData.format(date));// 分秒
////			voiceRecordBLV.addView(convertView);
//		}
//	}

/*	*//**
	 * 删除 方法，停止正在播放的对象，并 删除界面效果
	 *//*
	private OnClickListener deleteBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			VoicePlayer player = (VoicePlayer) v.getTag();
			JTFile jtFile = (JTFile) v.getTag(v.getId());
			if (player != null) {
				if (indexPlay != null && indexPlay.id == player.id) {
					reductionView();
				}
				View view = voiceView.get(player.id);
				if (view != null) {
//					voiceRecordBLV.removeView(view);
				}
				voiceList.remove(player);// 从数据中删除
				if (jtFile != null) {
					fileVoiceList.remove(jtFile);
				}
			}
		}
	};*/

	/**
	 * 状态还原
	 */
	/*
	private void reductionView() {
		if (mPlayer != null) {
			mPlayer.stopPlay();// 停止播放
		}
		if (indexPlay != null) {
			timer = 0;
			seekBar.setProgress(0);
			mTimerTask.cancel();
			date.setTime(indexPlay.time);
			time.setText(simpleData.format(date));
			playIv.setBackgroundResource(R.drawable.recordplay);
			indexPlay = null;
		}
	}*/

//	/**
//	 * 播放器控制按钮
//	 */
//	private OnClickListener playBtnListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			VoicePlayer player = (VoicePlayer) v.getTag();
//			if (indexPlay != null && player.id == indexPlay.id) {
//				// 是自己在播放
//				if (mPlayer.isPlay()) { // 暂停
//					v.setBackgroundResource(R.drawable.recordplay);
//					mPlayer.pause();
//					mTimerTask.cancel();
//				} else {// 播放
//					v.setBackgroundResource(R.drawable.recordpause);
//					mPlayer.start();
//					startTime();
//				}
//			} else {
//				if (mPlayer.isPlay())
//					reductionView();
//				indexPlay = player;
//				// 自己开始进行播放 修改自己的状态
//				v.setBackgroundResource(R.drawable.recordpause);
//				mPlayer.startPlay(player.file.getPath(), onCompletion);
//				getView(v);
//				startTime();// 开始计时器
//			}
//		}
//	};

	/**
	 * 获取当前播放的对象 的控件信息
	 */
	/*private void getView(View childs) {
		View view = (View) childs.getParent();
		playIv = (ImageView) view.findViewById(R.id.play_start_iv);
		time = (TextView) view.findViewById(R.id.play_time_tv);
		seekBar = (SeekBar) view.findViewById(R.id.play_seekBar);
		play_btn_tv = (TextView) view.findViewById(R.id.play_btn_tv);
	}*/

//	/**
//	 * 播放器控制事件
//	 */
//	private OnCompletionListener onCompletion = new OnCompletionListener() {
//
//		@Override
//		public void onCompletion(MediaPlayer mp) {
//			reductionView();
//		}
//	};

//	/**
//	 * 播放器控制计时器
//	 */
//	private void startTime() {
//		mTimer = new Timer();
//		mTimerTask = new TimerTask() {
//			@Override
//			public void run() {
//				if (null != seekBar && null != mPlayer) {
//					seekBar.setProgress(mPlayer.getProgress());
//					timer += 10;
//					handler.sendEmptyMessage(1);
//				}
//			}
//		};
//		if (null != mPlayer && null != mTimer && null != mTimerTask) {
//			mTimer.schedule(mTimerTask, 0, 10);
//		}
//	}

	private OnSeekBarChangeListener seekBarChange = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// 停止
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// 按下
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			// 滑动
		}
	};

	public class PictureAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return tag ? selectedPictureSDAndNet.size() + 1 : selectedPictureSDAndNet.size();
		}

		@Override
		public JTFile getItem(int position) {
			return selectedPictureSDAndNet.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(ConferenceIntroduceActivity.this, R.layout.demand_item_showback_local, null);// 带有播放按钮的图片view(播放按钮默认隐藏)
			}
			ImageView localPhotoVideoIv = ViewHolder.get(convertView, R.id.localPhotoVideoIv);// 显示图片和视频的缩略图

			ImageView localVideoPlayIv = ViewHolder.get(convertView, R.id.localVideoPlayIv);// 视频的播放按钮默认隐藏

			LinearLayout localPhotoVideoIv2 = ViewHolder.get(convertView, R.id.localPhotoVideoIv2);// 添加图片按钮

			if (position < selectedPictureSDAndNet.size()) {
				JTFile jtFile = selectedPictureSDAndNet.get(position);
				localPhotoVideoIv.setVisibility(View.VISIBLE);
				localPhotoVideoIv2.setVisibility(View.GONE);

				String imgPath = jtFile.mScreenshotFilePath;
				if (TextUtils.isEmpty(imgPath)) {// 缩略图为空
					imgPath = jtFile.mUrl;
				}
				if (TextUtils.isEmpty(imgPath)) {// 网络地址为空
					imgPath = jtFile.mLocalFilePath;
				}
				if (!(jtFile.mType == 2)) {// 如果是图片
					if (!(new File(imgPath).isFile())) {
						ImageLoader.getInstance().displayImage(imgPath, localPhotoVideoIv, options);
					} else {
						ImageLoader.getInstance().displayImage("file://" + imgPath, localPhotoVideoIv, options);
					}

					// 隐藏播放按钮
					localVideoPlayIv.setVisibility(View.GONE);
				} else {
					if (!(new File(imgPath).isFile())) {
						// 异步获取网络视频的第一帧
						videoNetThread(localPhotoVideoIv, imgPath, 150, 150);
					} else {
						// 异步获取本地视频的第一帧
						videoThread(localPhotoVideoIv, imgPath);
					}
					localVideoPlayIv.setVisibility(View.VISIBLE);
				}
			} else {
				localPhotoVideoIv2.setVisibility(View.VISIBLE);
				localPhotoVideoIv.setVisibility(View.GONE);
				localVideoPlayIv.setVisibility(View.GONE);
			}

			return convertView;
		}

		private void videoNetThread(final ImageView localPhotoVideoIv, final String imgPath, final int width, final int height) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					netBitmap = null;
					MediaMetadataRetriever retriever = new MediaMetadataRetriever();
					int kind = MediaStore.Video.Thumbnails.MINI_KIND;
					try {
						if (Build.VERSION.SDK_INT >= 14) {
							retriever.setDataSource(imgPath, new HashMap<String, String>());
						} else {
							retriever.setDataSource(imgPath);
						}
						netBitmap = retriever.getFrameAtTime();
					} catch (IllegalArgumentException ex) {
					} catch (RuntimeException ex) {
					} finally {
						try {
							retriever.release();
						} catch (RuntimeException ex) {
						}
					}
					if (kind == Images.Thumbnails.MICRO_KIND && netBitmap != null) {
						netBitmap = ThumbnailUtils.extractThumbnail(netBitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
					}
					Message msg = handlerNetVideo.obtainMessage();
					msg.obj = localPhotoVideoIv;
					handlerNetVideo.sendMessage(msg);

				}
			}).start();
		}

		private void videoThread(final ImageView image, final String imagePath) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Bitmap bm = ThumbnailUtils.createVideoThumbnail(imagePath, Thumbnails.MINI_KIND);
					extractMiniThumb = ThumbnailUtils.extractThumbnail(bm, 200, 200);
					Message msg = handlerVideo.obtainMessage();
					msg.obj = image;
					handlerVideo.sendMessage(msg);
				}
			}).start();
		}

		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		private Bitmap createVideoThumbnail(String url, int width, int height) {
			Bitmap bitmap = null;
			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			int kind = MediaStore.Video.Thumbnails.MINI_KIND;
			try {
				if (Build.VERSION.SDK_INT >= 14) {
					retriever.setDataSource(url, new HashMap<String, String>());
				} else {
					retriever.setDataSource(url);
				}
				bitmap = retriever.getFrameAtTime();
			} catch (IllegalArgumentException ex) {
				// Assume this is a corrupt video file
			} catch (RuntimeException ex) {
				// Assume this is a corrupt video file.
			} finally {
				try {
					retriever.release();
				} catch (RuntimeException ex) {
					// Ignore failures while cleaning up.
				}
			}
			if (kind == Images.Thumbnails.MICRO_KIND && bitmap != null) {
				bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			}
			return bitmap;
		}

	}

	/**
	 * 附件适配器
	 * 
	 * @param fileS
	 * @return
	 */
	class AccessoryLv extends BaseAdapter {
		@Override
		public int getCount() {
			return fileListSDAndNet == null ? 0 : fileListSDAndNet.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int finalPosition = position;
			if (convertView == null) {
				convertView = View.inflate(ConferenceIntroduceActivity.this, R.layout.demand_accessory_all_item, null);
			}
			ImageView documentImgIv = ViewHolder.get(convertView, R.id.documentImgIv);
			TextView documentNameTv = ViewHolder.get(convertView, R.id.documentNameTv);
			TextView documentSizeTv = ViewHolder.get(convertView, R.id.documentSizeTv);
			TextView uploadStateTv = ViewHolder.get(convertView, R.id.uploadStateTv);// 上传状态
			ViewHolder.get(convertView, R.id.rightCb).setVisibility(View.INVISIBLE);
			ViewHolder.get(convertView, R.id.lineV).setVisibility(View.INVISIBLE);
			View deleteIv = ViewHolder.get(convertView, R.id.deleteIv);
			deleteIv.setVisibility(View.VISIBLE);
			JTFile jtFile = fileListSDAndNet.get(position);
			String path = jtFile.mFileName;
			int index = 4;
			if (path.matches("^.*?\\.(xls|xlsx)$")) {
				index = 0;
			} else if (path.matches("^.*?\\.(ppt|pptx)$")) {
				index = 1;
				documentImgIv.getBackground().setLevel(1);// 设置显示的图片
			} else if (path.matches("^.*?\\.(doc|doc x)$")) {
				index = 2;
			} else if (path.matches("^.*?\\.pdf$")) {
				index = 3;
			} else if (path.matches("^.*?\\.txt$")) {
				index = 4;
			} else if (path.matches("^.*?\\.(zip|rar|7z)$")) {
				index = 5;
			}
			documentImgIv.getBackground().setLevel(index);// 设置显示的图片
			documentNameTv.setText(path.substring(path.lastIndexOf('/') + 1));
			if (new File(path).isFile()) {
				documentSizeTv.setVisibility(View.VISIBLE);
				long size = new File(path).length();
				documentSizeTv.setText(fileSize(size));// 文件大小
				documentSizeTv.setTag(size);
			} else {
				documentSizeTv.setText("网络文件");
				// documentSizeTv.setVisibility(View.INVISIBLE);;
			}
			deleteIv.setTag(fileListSDAndNet.get(position));// 保存文件
			// 删除文件
			deleteIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					JTFile file = (JTFile) v.getTag();
					deleteFileByFile(file);// 删除文件
					resetListView();
				}
			});
			uploadStateTv.setVisibility(View.GONE);
			// 如果本地文件不等于空，需要上传的文件也不等于空
			if (fileListSD != null && position < fileListSD.size() && mListUploader != null) {
				JTFile jTFile = fileListSD.get(position);

				// uploadStateTv.setText("取消");
				for (int j = 0; j < mListUploader.size(); j++) {
					if (jTFile.mCreateTime == mListUploader.get(j).getJTFile().mCreateTime) {
						com.utils.common.FileUploader fileUploader = mListUploader.get(j);
						int status = fileUploader.getStatus();
						if (status == 3) {
							uploadStateTv.setVisibility(View.GONE);
							documentSizeTv.setVisibility(View.VISIBLE);
							documentSizeTv.setText("上传成功");
							if (!fileListNet.contains(jTFile)) {
								fileListNet.add(jTFile);
							}
							fileListSD.remove(jTFile);

						} else if (status == 4) {
							uploadStateTv.setVisibility(View.VISIBLE);
							uploadStateTv.setText("上传失败重新上传");
						} else if (status == 5) {
							uploadStateTv.setVisibility(View.VISIBLE);
							uploadStateTv.setText("上传失败重新上传");
						} else {
							uploadStateTv.setVisibility(View.GONE);
						}
					}
				}
				// 处理图片重传事件
				uploadStateTv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						for (FileUploader uploader : mListUploader) {// 通过创建时间判断是否上传成功
							if (uploader.getJTFile().mCreateTime == fileListSD.get(finalPosition).mCreateTime) {
								if (uploader.getStatus() == FileUploader.Status.Error || uploader.getStatus() == FileUploader.Status.Canceled) {
									// 重新上传
									uploader.start();
									v.setVisibility(View.GONE);
								}
								break;
							}
						}
					}
				});

			}
			return convertView;
		}

	}

	private String fileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("0.00");
		String str = fileS < 1024 ? df.format((double) fileS) + "B" : fileS < 1048576 ? df.format((double) fileS / 1024) + "K" : fileS < 1073741824 ? df.format((double) fileS / 1048576) + "M" : df
				.format((double) fileS / 1073741824) + "G";
		return str;
	}

	/**
	 * 删除文件
	 */
	private void deleteFileByFile(JTFile file) {// 判断是服务器还是本地
		// 如果是附件
		if (file == null) {
			return;
		}
		// fileListSDAndNet.remove(f);
		if (TextUtils.isEmpty(file.getId())) {// 本地
			for (JTFile f : fileListSD) {
				if (f.mLocalFilePath.equals(file.mLocalFilePath)) {
					fileListSD.remove(f);
					break;
				}
			}
		} else {// 网络文件
			for (JTFile f : fileListNet) {
				if (f.getId().equals(file.getId())) {
					fileListNet.remove(f);
					break;
				}
			}
			deleteFileById(file.getId());
		}
		fileListSDAndNet.clear();
		fileListSDAndNet.addAll(fileListSD);
		fileListSDAndNet.addAll(fileListNet);
		count_item.setText((fileListSDAndNet == null || fileListSDAndNet.size() == 0) ? "" : fileListSDAndNet.size() + "");// 选择的条数
		accessoryAdapter.notifyDataSetChanged();
	}

	private void deleteFileById(String id) {
		if (TextUtils.isEmpty(id)) {
			return;
		}
		DemandReqUtil.deleteDemandFile(ConferenceIntroduceActivity.this, ConferenceIntroduceActivity.this, id, null);

	}

	/**
	 * 解决只显示一条的问题
	 */
	private void resetListView() {
		ListAdapter listAdapter = accessoryLv.getAdapter();
		count_item.setText((fileListSDAndNet == null || fileListSDAndNet.size() == 0) ? "" : fileListSDAndNet.size() + "");// 选择的条数
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, accessoryLv);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = accessoryLv.getLayoutParams();
		params.height = totalHeight + (accessoryLv.getDividerHeight() * (listAdapter.getCount() - 1));
		accessoryLv.setLayoutParams(params);
	}

	private void showImage() {//
		if (selectedPictureSDAndNet != null && selectedPictureSDAndNet.size() > 0) {
			showBackPicVidHlv.setVisibility(View.VISIBLE);
		} else {
			showBackPicVidHlv.setVisibility(View.GONE);
		}

		if (selectedPictureSDAndNet.size() < 4 && selectedPictureSDAndNet.size() != 0) {
			photoVideoIb.setVisibility(View.GONE);
			tag = true;
		} else {
			tag = false;
			photoVideoIb.setVisibility(View.VISIBLE);
		}
		if (mPictureAdapter != null) {
			showBackPicVidHlv.setAdapter(mPictureAdapter);
			// mPictureAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 显示所有附件
	 * 
	 * @param selectList
	 */
	private void showAccessory(HashSet<String> selectList) {

		if (selectList == null || selectList.size() < -0) {
			if (accessoryAdapter != null) {
				accessoryAdapter.notifyDataSetChanged();
			}
			return;
		}
//		fileListSD.clear();// 清除本地集合
		fileListSDAndNet.clear();
		fileListSDAndNet.addAll(fileListNet);
		List<String> list = new ArrayList(selectList);
		for (String str : list) {
			boolean hasFile = false;
			JTFile jtFile = new JTFile();
			jtFile.mModuleType = -1;// 文件类型
			jtFile.mType = 3;// 附件就是3
			jtFile.mFileName = str;// 文件名
			jtFile.mLocalFilePath = str;// 文件路径
			jtFile.mCreateTime = System.currentTimeMillis(); // 创建时间，作为文件的识别码
			for (int i = 0; i < fileListSD.size(); i++) {
				JTFile jf = fileListSD.get(i);
				//已经上传的网络文件
				if (!StringUtils.isEmpty(jf.reserved2)&&str.equals(jf.mUrl)) {
					jtFile.reserved2 = jf.reserved2;
					hasFile = true;
				}
			}
			if (!hasFile) {
				fileListSD.add(jtFile);// 添加到本地集合
			}
		}
		
		fileListSDAndNet.addAll(fileListSD);
		if (accessoryAdapter != null) {
			accessoryAdapter.notifyDataSetChanged();
		}
		count_item.setText(fileListSDAndNet == null || fileListSDAndNet.size() <= 0 ? "" : fileListSDAndNet.size() + "");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == REQUEST_CODE_BROWERS_ACTIVITY) {// 查看图片
			selectedPictureSDAndNet.clear();
			selectedPictureSDAndNet = (ArrayList<JTFile>) data.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
			showImage();

		} else if (requestCode == REQUEST_CODE_ADD_ACTIVITY) {// 添加图片
			// 获取选中图片的集合
			selectedPictureSDAndNet.clear();
			selectedPictureSDAndNet = (ArrayList<JTFile>) data.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
			showImage();
		}
		// 选择附件返回结果
		else if (requestCode == 255) {
			HashSet<String> selectList = (HashSet<String>) data.getSerializableExtra("select_list");
			showAccessory(selectList);
		}
	}

	private Handler upLoadHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 上传成功
			case 1:
				switch (msg.arg1) {
				// 图片上传
				case 0:
					if (msg.obj != null) {
						MMeetingPic fileImg = (MMeetingPic) msg.obj;
						if (!StringUtils.isEmpty(fileImg.getId() + "")) {
							fileImg.setId(null);
						}
						mMeetingPicForList.add(fileImg);
						mMeetingPicList.add(fileImg);
					}
					if (fileImageList.size() == picSuccessCount) {
						showToast("图片上传成功");
						if (fileVoiceVideoList.size() == voiceVideoSuccessCount && fileListSD.size() == fileSuccessCount) {
							// 全部上传成功
							close();
						}
					}
					break;
				// 音视频上传
				case 1:
					if (msg.obj != null) {
						JTFile2ForHY jtfile = (JTFile2ForHY) msg.obj;
						mMeetingFileForList.add(jtfile);
						mMeetingFileList.add(jtfile);
					}
					if (fileVoiceVideoList.size() == voiceVideoSuccessCount) {
						showToast("音频视频上传成功");
						if (fileImageList.size() == picSuccessCount && fileListSD.size() == fileSuccessCount) {
							// 全部成功
							close();
						}
					}
					break;
				// 附件上传
				case 2:
					if (msg.obj != null) {
						JTFile2ForHY jtfile = (JTFile2ForHY) msg.obj;
						mMeetingFileForList.add(jtfile);
						mMeetingFileList.add(jtfile);
					}
					if (fileListSD.size() == fileSuccessCount) {
						showToast("附件上传成功");
						if (fileImageList.size() == picSuccessCount && fileVoiceVideoList.size() == voiceVideoSuccessCount) {
							// 全成功
							close();
						}
					}
					break;

				}
				break;
			// 上传失败
			case 2:
				if (upLoadingDialog != null) {
					upLoadingDialog.dismiss();
				}
				isUploading = false;
				switch (msg.arg1) {
				// 图片上传
				case 0:
					showToast("图片上传失败,请检查网络连接");
					for (FileUploader uploader : mListPicUpLoader) {
						uploader.cancel();
					}
					break;
				// 音视频上传
				case 1:
					showToast("音视频上传失败,请检查网络连接");
					for (FileUploader uploader : mListVoiceVideoFileUpLoader) {
						uploader.cancel();
					}
					break;
				// 附件上传
				case 2:
					showToast("附件上传失败,请检查网络连接");
					for (FileUploader uploader : mListUploader) {
						uploader.cancel();
					}
					break;
				}
				break;
			default:
				break;
			}
		};
	};
	private UpLoadingDialog upLoadingDialog;

	public void uploading() {
		// 没有上传文件
		if (selectedPictureSDAndNet != null && selectedPictureSDAndNet.size() < 1 && fileVoiceList != null && fileVoiceList.size() < 1 && fileListSD != null && fileListSD.size() < 1) {
			isUploading = false;
			close();
			return;
		}

		mMeetingPicForList.clear();
		mMeetingFileForList.clear();
		fileVoiceVideoList.clear();
		totalCount = 0;
		picSuccessCount = 0;
		fileSuccessCount = 0;
		voiceVideoSuccessCount = 0;
		isUploading = true;
		upLoadingDialog.show();

		fileImageList.clear();
		fileVideoList.clear();
		/** 将选择的图片和视频分到fileImageList图片和fileVideoList视频 集合中 */
		for (JTFile jtFile : selectedPictureSDAndNet) {
			jtFile.mTaskId = "";
			if (!(jtFile.mType == JTFile.TYPE_FILE)) {// 图片
				fileImageList.add(jtFile);// 图片集合添加
			} else {// 视频
				fileVideoList.add(jtFile);// 视频集合添加
			}
		}

		/** 将音频文件加到fileVoiceList中 即fileVoiceList */
		fileVoiceVideoList.addAll(fileVideoList);
		fileVoiceVideoList.addAll(fileVoiceList);
		// 不需要上传的数量
		int noNeedUpLoadCount = 0;

		/** 开始上传 图片 */
		for (JTFile jtFile : fileImageList) {
			// reserved2放fileIndexId，当作已经上传成功跳过
			if (!StringUtils.isEmpty(jtFile.reserved2)) {
				picSuccessCount++;
				for (MMeetingPic meetingPic : mMeetingPicList) {
					if ((meetingPic.getFileIndexId() + "").equals(jtFile.reserved2)) {// 文件索引相同，则为同一文件
						mMeetingPicForList.add(meetingPic);
						noNeedUpLoadCount++;
						break;
					}
				}
				// 跳过
				continue;
			}

			// 图片未上传过
			jtFile.mTaskId = taskId;
			jtFile.mModuleType = -1;
			jtFile.reserved3 = alterHyId;
			FileUploader uploader = new FileUploader(jtFile, new FileUploader.OnMeetingPicUploadListener() {
				@Override
				public void onUpdate(int value) {
				}

				@Override
				public void onSuccess(String url, MMeetingPic fileImg) {

					Message msg = upLoadHandler.obtainMessage();
					picSuccessCount++;
					msg.what = 1; // 代表上传成功
					msg.arg1 = 0; // 图片上传；
					msg.obj = fileImg;
					upLoadHandler.sendMessage(msg);
					// 上传图片成功
					// mMeetingPicForList.add(fileImg);
					// mMeetingPicList.add(fileImg);
					// successCount++;
					// if (totalCount == successCount) {
					// close();
					// }
				}

				@Override
				public void onStarted() {
				}

				@Override
				public void onPrepared() {
				}

				@Override
				public void onError(int code, String message) {
					Message msg = upLoadHandler.obtainMessage();
					msg.what = 2; // 代表上传失败
					msg.arg1 = 0; // 图片上传；
					upLoadHandler.sendMessage(msg);
				}

				@Override
				public void onCanceled() {

				}
			});
			uploader.WEB_URL = EAPIConsts.MEETING_PICTURE_UPLOADER_URL;
			mListPicUpLoader.add(uploader);
			uploader.start();

		}

		int noNeedUpLoadCount2 = 0;
		/** 开始上传 音频视频 */
		for (JTFile jtFile : fileVoiceVideoList) {
			// 文件已经上传，当作已经上传成功跳过
			if (!StringUtils.isEmpty(jtFile.reserved2)) {
				voiceVideoSuccessCount++;
				for (JTFile2ForHY jtFile2ForHY : mMeetingFileList) {
					if ((jtFile2ForHY.fileIndexId + "").equals(jtFile.reserved2)) {// 将这个文件加入返回的文件集
						mMeetingFileForList.add(jtFile2ForHY);
						noNeedUpLoadCount2++;
						break;
					}
				}
				continue;
			}
			// 文件未上传过
			jtFile.mTaskId = taskId;
			jtFile.mModuleType = -1;
			jtFile.setReserved3(alterHyId);// 如果当前为修改会议草稿 那么放入这个会议的id
			FileUploader uploader = new FileUploader(jtFile, new FileUploader.OnMeetingFileUploadListener() {
				@Override
				public void onUpdate(int value) {
				}

				@Override
				public void onSuccess(String id, JTFile2ForHY jtfile) {
					voiceVideoSuccessCount++;
					Message msg = upLoadHandler.obtainMessage();
					msg.what = 1; // 代表上传成功
					msg.arg1 = 1; // 音频视频上传；
					msg.obj = jtfile;
					upLoadHandler.sendMessage(msg);
					// mMeetingFileForList.add(jtfile);
					// successCount++;
					// if (totalCount == successCount) {
					// close();
					// }
				}

				@Override
				public void onStarted() {
				}

				@Override
				public void onPrepared() {
				}

				@Override
				public void onError(int code, String message) {
					Message msg = upLoadHandler.obtainMessage();
					msg.what = 2; // 代表上传失败
					msg.arg1 = 1; // 音频视频上传；
					upLoadHandler.sendMessage(msg);
				}

				@Override
				public void onCanceled() {
				}
			});
			uploader.WEB_URL = EAPIConsts.MEETING_FILE_UPLOADER_URL;
			mListVoiceVideoFileUpLoader.add(uploader);
			uploader.start();
		}

		int noNeedUpLoadCount3 = 0;
		for (JTFile jtFile : fileListSD) {// 上传sd卡的文件

			// 文件已经上传，当作已经上传成功跳过
			if (!StringUtils.isEmpty(jtFile.reserved2)) {
				fileSuccessCount++;
				for (JTFile2ForHY jtFile2ForHY : mMeetingFileList) {
					if ((jtFile2ForHY.fileIndexId + "").equals(jtFile.reserved2)) {// 将这个文件加入返回的文件集
						mMeetingFileForList.add(jtFile2ForHY);
						noNeedUpLoadCount3++;
						break;
					}
				}
				continue;
			}

			// 文件未上传过
			jtFile.mTaskId = taskId;
			jtFile.mModuleType = -1;
			jtFile.setReserved3(alterHyId);// 如果当前为修改会议草稿 那么放入这个会议的id
			FileUploader uploader = new FileUploader(jtFile, new FileUploader.OnMeetingFileUploadListener() {

				@Override
				public void onUpdate(int value) {
				}

				@Override
				public void onSuccess(String id, JTFile2ForHY jtfile) {
					fileSuccessCount++;
					Message msg = upLoadHandler.obtainMessage();
					msg.what = 1; // 代表上传成功
					msg.arg1 = 2; // 附件上传；
					msg.obj = jtfile;
					upLoadHandler.sendMessage(msg);

					// mMeetingFileForList.add(jtfile);
					// successCount++;
					// if (totalCount == successCount) {
					// close();
					// }
				}

				@Override
				public void onStarted() {
				}

				@Override
				public void onPrepared() {

				}

				@Override
				public void onError(int code, String message) {
					Message msg = upLoadHandler.obtainMessage();
					msg.what = 2; // 代表上传失败
					msg.arg1 = 2; // 附件上传；
					upLoadHandler.sendMessage(msg);
				}

				@Override
				public void onCanceled() {
				}
			});
			uploader.WEB_URL = EAPIConsts.MEETING_FILE_UPLOADER_URL;
			mListUploader.add(uploader);
			uploader.start();
		}

		if (fileImageList.size() == noNeedUpLoadCount && fileVoiceVideoList.size() == noNeedUpLoadCount2 && fileListSD.size() == noNeedUpLoadCount3) {
			close();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 如果是返回
			// close();
			if (upLoadingDialog != null) {
				upLoadingDialog.dismiss();
			}
			for (FileUploader item : mListPicUpLoader) {
				item.cancel();
			}
			for (FileUploader item : mListUploader) {
				item.cancel();
			}
			for (FileUploader item : mListVoiceVideoFileUpLoader) {
				item.cancel();
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void close() {
		if (isUploading == true) {
			showToast("上传完成");
			isUploading = false;
			if (upLoadingDialog != null) {
				upLoadingDialog.dismiss();
			}
		}
		Intent intent = new Intent();
		switch (introduceType) {
		case meetingTopicIntroduce:
			InitiatorDataCache.getInstance().introduce.contentText = txtContentTv.getText().toString().trim();
			intent.putExtra("mMeetingFileForList", mMeetingFileForList);
			intent.putExtra("mMeetingPicForList", mMeetingPicForList);
			intent.putExtra("taskId", taskId);
			break;
		case speakerTopicIntroduce:
			mMeetingTopicQuery.setCreateId(Long.valueOf(App.getUserID()));
			mMeetingTopicQuery.setTopicDesc(txtContentTv.getText().toString().trim());
			if (StringUtils.isEmpty(mMeetingTopicQuery.getTaskId())) {
				mMeetingTopicQuery.setTaskId(taskId);
			}
			mMeetingTopicQuery.setListMeetingFile(mMeetingFileForList);
			mMeetingTopicQuery.setListMeetingPic(mMeetingPicForList);
			intent.putExtra("mMeetingTopicQuery", mMeetingTopicQuery);
			break;
		}
		setResult(RESULT_OK, intent);
		finish();
	}

	private boolean isNet() {
		for (JTFile jtFile : selectedPictureSDAndNet) {
			if (TextUtils.isEmpty(jtFile.getId())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onRecognizerResultListener(String result) {
		if (!Util.isNull(result)) {
			txtContentTv.append(result);
		}
	}

	@Override
	public void bindData(int tag, Object object) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		handler.removeCallbacksAndMessages(null);
		handlerNetVideo.removeCallbacksAndMessages(null);
		handlerVideo.removeCallbacksAndMessages(null);
		upLoadHandler.removeCallbacksAndMessages(null);

	}

//	@Override
//	public void onPrepared(String url) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onStarted(String url) {
//		// TODO Auto-generated method stub
//		
//	}
//	/**音频下载成功*/
//	@Override
//	public void onSuccess(String url, JTFile jtFile) {
//		Message msg = handler.obtainMessage();
//		msg.what = 3;
//		msg.obj = jtFile;
//		handler.sendMessage(msg);
//	}
//	/**音频下载失败*/
//	@Override
//	public void onError(String url, int code, String errMsg) {
//	}
//
//	@Override
//	public void onUpdate(String url, int progress) {
//	}
//
//	@Override
//	public void onCanceled(String url) {
//		// TODO Auto-generated method stub
//	}
}
