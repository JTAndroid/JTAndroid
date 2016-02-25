package com.tr.ui.demand;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.tr.model.demand.DemandAttFile;
import com.tr.model.demand.DemandFile;
import com.tr.model.demand.DemandPvFile;
import com.tr.model.demand.IntroduceData;
import com.tr.model.demand.NoteData;
import com.tr.model.demand.VoicePlayer;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.conference.common.LocalPlayer;
import com.tr.ui.conference.common.Recorder;
import com.tr.ui.conference.common.Recorder.VolumeChangedListener;
import com.tr.ui.demand.MyView.DemandHorizontalListView;
import com.tr.ui.demand.util.FileUploader;
import com.tr.ui.demand.util.FileUploader.OnFileUploadListenerEx;
import com.utils.common.TaskIDMaker;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.Util;

/**
 * @ClassName: IntroduceActivity.java
 * @author zcs
 * @Date 2015年3月16日 上午9:47:22
 * @Description: 介绍
 */
public class IntroduceActivity extends JBaseActivity implements OnClickListener, SpeechOnRecognizerResultListener, OnFileUploadListenerEx, IBindData {

	public static final int REQUEST_CODE_BROWERS_ACTIVITY = 1001; // 启动关联的回调
	public static final int REQUEST_CODE_ADD_ACTIVITY = 1002; // 启动关联的回调

	private Bitmap netBitmap;
	private Bitmap extractMiniThumb;
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
	// 存放勾选图片/视频的路径集合
	private ArrayList<JTFile> selectedPictureSDAndNet = new ArrayList<JTFile>();// 本地加网络
	// private ArrayList<JTFile> selectedPictureNet = new ArrayList<JTFile>();//
	// 网络
	private DemandHorizontalListView showBackPicVidHlv;

	/**
	 * 需要上传的集合
	 */
	private ArrayList<FileUploader> mListUploader = new ArrayList<FileUploader>(); // 文件上传类
	// private ArrayList<JTFile> mListJtFile = new ArrayList<JTFile>(); //
	// 所选文件（附件）
	private ArrayList<FileUploader> mListJtImgVideo = new ArrayList<FileUploader>(); // 图片和视频
	/**
	 * 附件
	 */
	private ArrayList<JTFile> fileListSD = new ArrayList<JTFile>();// 本地
	private List<JTFile> fileListSDAndNet = new ArrayList<JTFile>();// 本地加网络
	private List<JTFile> fileListNet = new ArrayList<JTFile>();// 加网络
	private ImageLoader loader;
	private DisplayImageOptions options;

	/**
	 * 录音效果
	 */

	private Speech speech;
	private EditText editTv;// 输入框
	private LocalPlayer mPlayer = new LocalPlayer(); // 播放器
	private Recorder mRecorder; // 录制对象
	// 录音对话框
	private TextView tv_cancel = null;
	private TextView tv_complete = null;
	private TextView tv_message = null;
	private Dialog recorderDialog = null;
	private ImageView volumeLevel;
	private String mVoicePath;
	private LinearLayout voiceLv;
	private ArrayList<VoicePlayer> voiceList = new ArrayList<VoicePlayer>();
	/** 计时器 */
	private long endTime;// 结束时间
	private ListView accessoryLv;
	private boolean tag = false; // 判断是否要在 listView里面最后一项加载添加按钮
	ImageView photoVideoIb;
	private TextView count_item;
	/** 附件adapter */
	private AccessoryLv accessoryAdapter;
	/** 视频/图片 */
	private PictureAdapter mPictureAdapter;
	private String mTaskId;

	private NoteData noteData;// 附件对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_introduce);
		// TextView tv = (TextView) findViewById(R.id.txtContentTv);//说点什么
		accessoryLv = (ListView) findViewById(R.id.accessoryLv);
		showBackPicVidHlv = (DemandHorizontalListView) findViewById(R.id.showBackPicVidHlv);// 图片
		// showBackPicVidHParentlv = (LinearLayout)
		// findViewById(R.id.showBackPicVidHParentlv);//图片集合的父级
		photoVideoIb = (ImageView) findViewById(R.id.photoVideoIb);
		photoVideoIb.setOnClickListener(this);// 图片/视频
		editTv = (EditText) findViewById(R.id.txtContentTv);// 说点什么
		findViewById(R.id.voiceCharIv).setOnClickListener(this);// 语音转文字
		findViewById(R.id.img_hint_tv).setVisibility(View.GONE);
		findViewById(R.id.accessory_layout).setVisibility(View.VISIBLE);
		voiceLv = (LinearLayout) findViewById(R.id.voiceRecordBLV);// 音频List
		findViewById(R.id.voiceRecord).setOnClickListener(this);// 音频点击
		findViewById(R.id.otherRl).setOnClickListener(this); // 附件
		count_item = (TextView) findViewById(R.id.count_item);
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		// 设置图片在加载中显示的图片
				.showImageOnLoading(R.drawable.demand_defaultimg)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageForEmptyUri(R.drawable.demand_defaultimg)
				// 设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.drawable.demand_defaultimg)
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
		mPictureAdapter = new PictureAdapter();
		accessoryAdapter = new AccessoryLv();
		accessoryLv.setAdapter(accessoryAdapter);
		showBackPicVidHlv.setAdapter(mPictureAdapter);
		showBackPicVidHlv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position < selectedPictureSDAndNet.size()) {// 浏览图片
					Intent intent = new Intent(IntroduceActivity.this, BrowesPhotoVideo.class);
					intent.putExtra("index", position);
					intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE, selectedPictureSDAndNet);
					intent.putExtra(ENavConsts.DEMAND_BROWER_DELETE, true);
					IntroduceActivity.this.startActivityForResult(intent, REQUEST_CODE_BROWERS_ACTIVITY);
				} else {// 选择图片
					boolean b = false;
					for (JTFile imageItem : selectedPictureSDAndNet) {
						if (imageItem.mType == 2) {
							b = true;
							break;
						}
					}
					ENavigate.startSelectPictureActivity(IntroduceActivity.this, REQUEST_CODE_ADD_ACTIVITY, selectedPictureSDAndNet, b);
				}
			}
		});
		
		Intent intent = getIntent();
		noteData = (NoteData) intent.getSerializableExtra(ENavConsts.DEMAND_NOTE_DATA);
		mTaskId = noteData != null ? noteData.taskId : "";// 上个界面传的mTaskId
		if (TextUtils.isEmpty(mTaskId)) {
			mTaskId = TaskIDMaker.getTaskId(App.getUserName());// 根据用户名生成TaskId
		} else {
			startGetData();
		}
		editTv.setText(noteData == null || noteData.value == null ? "" : noteData.value);// 文本内容
		createDialog();// 创建等待dialog
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.voiceCharIv:// 语音转文字
			if (speech == null) {
				speech = new Speech(IntroduceActivity.this);
				speech.init();
			}
			speech.startSpeech();
			break;
		case R.id.voiceRecord:// 音频点击
			startRecord();
			break;
		case R.id.photoVideoIb:// 选择图片/视频
			boolean b = false;
			for (JTFile imageItem : selectedPictureSDAndNet) {
				if (imageItem.mType == 2) {
					b = true;
					break;
				}
			}
			ENavigate.startSelectPictureActivity(IntroduceActivity.this, REQUEST_CODE_ADD_ACTIVITY, selectedPictureSDAndNet, b);
			break;
		case R.id.otherRl:// 附件
			ENavigate.startAccessoryActivity(IntroduceActivity.this, fileListSD, 255);
			break;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
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
		fileListSD.clear();// 清除本地集合
		fileListSDAndNet.clear();
		fileListSDAndNet.addAll(fileListNet);
		List<String> list = new ArrayList(selectList);
		for (String str : list) {
			JTFile jtFile = new JTFile();
			jtFile.mTaskId = mTaskId;// taskId
			jtFile.mModuleType = JTFile.TYPE_DEMAND;// 文件类型
			jtFile.mType = 3;// 附件就是3
			jtFile.mFileName = str;// 文件名
			jtFile.mLocalFilePath = str;// 文件路径
			jtFile.mCreateTime = System.currentTimeMillis(); // 创建时间，作为文件的识别码
			fileListSD.add(jtFile);// 添加到本地集合
		}
		fileListSDAndNet.addAll(fileListSD);
		if (accessoryAdapter != null) {
			accessoryAdapter.notifyDataSetChanged();
		}
		count_item.setText(fileListSDAndNet == null || fileListSDAndNet.size() <= 0 ? "" : fileListSDAndNet.size() + "");
	}

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
				convertView = View.inflate(IntroduceActivity.this, R.layout.demand_accessory_all_item, null);
			}
			// vh = (ViewHoulderItem) convertView.getTag();
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
				documentImgIv.setBackgroundResource(R.drawable.demand_excel);
			} else if (path.matches("^.*?\\.(ppt|pptx)$")) {
				index = 1;
				documentImgIv.setBackgroundResource(R.drawable.demand_ppt);
				
			} else if (path.matches("^.*?\\.(doc|doc x)$")) {
				documentImgIv.setBackgroundResource(R.drawable.demand_word);
				index = 2;
			} else if (path.matches("^.*?\\.pdf$")) {
				index = 3;
				documentImgIv.setBackgroundResource(R.drawable.demand_pdf);
			} else if (path.matches("^.*?\\.txt$")) {
				index = 4;
				documentImgIv.setBackgroundResource(R.drawable.demand_file);
			} else if (path.matches("^.*?\\.(zip|rar|7z)$")) {
				index = 5;
				documentImgIv.setBackgroundResource(R.drawable.demand_rar);
			}
//			documentImgIv.getDrawable().setLevel(index);// 设置显示的图片
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
						FileUploader fileUploader = mListUploader.get(j);
						int status = fileUploader.getStatus();
						// Log.i(TAG, MSG + "status = " + status );
						/*
						 * if (status == 1) { documentSizeTv.setText("准备上传"); }
						 * else if (status == 2) {
						 * documentSizeTv.setText("开始上传");
						 * uploadStateTv.setVisibility(View.GONE); } else
						 */if (status == 3) {
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

	private String fileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("0.00");
		String str = fileS < 1024 ? df.format((double) fileS) + "B" : fileS < 1048576 ? df.format((double) fileS / 1024) + "K" : fileS < 1073741824 ? df.format((double) fileS / 1048576) + "M" : df
				.format((double) fileS / 1073741824) + "G";
		return str;
	}

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
				convertView = View.inflate(IntroduceActivity.this, R.layout.demand_item_showback_local, null);// 带有播放按钮的图片view(播放按钮默认隐藏)
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
				/*
				 * if(!(new File(imageItem.path).isFile())){ String fullUrl =
				 * DemandReqUtil.getFullUrl(imageItem.path); imageItem.path =
				 * fullUrl; }
				 */

				if (!(jtFile.mType == 2)) {// 如果是图片
					if (!(new File(imgPath).isFile())) {
						// imageItem.path =
						// "http://192.168.101.22:81/web1/0032900961";
						loader.displayImage(imgPath, localPhotoVideoIv, options);
					} else {
						loader.displayImage("file://" + imgPath, localPhotoVideoIv, options);
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

	@Override
	public void initJabActionBar() {
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(R.layout.demand_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		TextView myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("备注");
		mCustomView.findViewById(R.id.titleIv).setVisibility(View.GONE);

	}

	@Override
	public void onRecognizerResultListener(String result) {
		if (!Util.isNull(result)) {
			editTv.append(result);
		}
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
			// String content = editTv.getText().toString();
			// if(TextUtils.isEmpty(content)){
			// Toast.makeText(getApplicationContext(), "内容不能为空", 0).show();
			// return super.onOptionsItemSelected(item);
			// }

			uploading();// 上传
			break;
		case android.R.id.home:// 返回
			// close();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 开始录音
	 */
	public void startRecord() {
		mRecorder = new Recorder();
		mVoicePath = Recorder.getVoiceFilePath();
		mRecorder.setOnVolumeChangedListener(new VolumeChangedListener() {
			@Override
			public void onVolumeChanged(double volume) {
				switch ((int) (volume + 5) / 10) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
					volumeLevel.setBackgroundResource(R.drawable.hy_meeting_volume_level_0);
					break;
				case 5:
				case 6:
					volumeLevel.setBackgroundResource(R.drawable.hy_meeting_volume_level_1);
					break;
				case 7:
				case 8:
					volumeLevel.setBackgroundResource(R.drawable.hy_meeting_volume_level_2);
					break;
				case 9:
				case 10:
					volumeLevel.setBackgroundResource(R.drawable.hy_meeting_volume_level_3);
					break;
				}
			}
		});
		RelativeLayout rl_rec = (RelativeLayout) View.inflate(this, R.layout.hy_meeting_dialog_road_show, null);
		tv_cancel = (TextView) rl_rec.findViewById(R.id.hy_note_btn_recode_cancel);
		tv_complete = (TextView) rl_rec.findViewById(R.id.hy_note_btn_recode_complete);
		volumeLevel = (ImageView) rl_rec.findViewById(R.id.hy_meeting_volume_level);
		tv_message = (TextView) rl_rec.findViewById(R.id.hy_meetion_voluem_text);
		tv_message.setText("请您开始说话");
		if (null != mRecorder && false == mVoicePath.isEmpty()) {
			recorderDialog = new Dialog(this, R.style.transmeeting_dialog);
			recorderDialog.setContentView(rl_rec);
			recorderDialog.setCancelable(false);
			recorderDialog.show();
			mRecorder.startRecord(mVoicePath);
			startTimerRecorder();
		}
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imterRecorder.cancel();
				if (null != mRecorder) {
					mRecorder.stopRecord();
					mRecorder = null;
				}
				recorderDialog.dismiss();
				File file = new File(mVoicePath);
				if (file != null && file.exists()) {
					file.delete();
				}
			}
		});
		tv_complete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imterRecorder.cancel();
				if (null != mRecorder) {
					mRecorder.stopRecord();
					endTime = recorderTimer;
					mRecorder = null;
				}
				recorderDialog.dismiss();
				File voiceFile = new File(mVoicePath);
				// 点击确定按钮
				VoicePlayer voPlayer = new VoicePlayer();
				voPlayer.file = voiceFile;
				voPlayer.time = endTime;
				addPlayView(voPlayer);
				// voiceList.add(voPlayer);
				// voiceAdapter.notifyDataSetInvalidated();
			}
		});
	}

	/**
	 * 开始计时统计
	 */
	private void startTimerRecorder() {
		timerRecorder = new Timer();
		recorderTimer = 0;
		imterRecorder = new TimerTask() {

			@Override
			public void run() {
				recorderTimer += 1000;// 加一
				if (recorderTimer >= recorderSizeTimer) {
					// 停止录音
					handler.sendEmptyMessage(2);
					imterRecorder.cancel();// 停止计时
				}
			}
		};
		timerRecorder.schedule(imterRecorder, 1000, 1000);// 一秒后开始计时，
	}

	/**
	 * 创建一个音频播放器对象到界面中
	 * 
	 * @param voice
	 */
	private void addPlayView(VoicePlayer play) {
		if (voiceList.add(play)) {
			View convertView = View.inflate(this, R.layout.demand_play_item, null);
			ImageView playIv = (ImageView) convertView.findViewById(R.id.play_start_iv); // 播放按钮
			TextView timeTv = (TextView) convertView.findViewById(R.id.play_time_tv);// 播放时间进度
			SeekBar seekBar = (SeekBar) convertView.findViewById(R.id.play_seekBar);// 播放时间长度
			ImageView deleteIv = (ImageView) convertView.findViewById(R.id.play_delete_iv);// 删除按钮
			TextView btnTv = (TextView) convertView.findViewById(R.id.play_btn_tv);//

			deleteIv.setTag(play);
			playIv.setTag(play);
			timeTv.setTag(play);
			seekBar.setTag(play);
			deleteIv.setTag(play);
			btnTv.setTag(play);

			deleteIv.setOnClickListener(deleteBtnListener); // 删除方法
			playIv.setOnClickListener(playBtnListener);
			seekBar.setOnSeekBarChangeListener(seekBarChange);
			// btnTv.setOnClickListener(threadBtn);
			play.id = System.currentTimeMillis();
			seekBar.setProgress(0);
			date.setTime(play.time);
			voiceView.put(play.id, convertView);
			timeTv.setText(simpleData.format(date));// 分秒
			voiceLv.addView(convertView);
		}
	}

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

	/**
	 * 删除 方法，停止正在播放的对象，并 删除界面效果
	 */
	private OnClickListener deleteBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			VoicePlayer player = (VoicePlayer) v.getTag();
			if (player != null) {
				if (indexPlay != null && indexPlay.id == player.id) {
					reductionView();
				}
				View view = voiceView.get(player.id);
				if (view != null) {
					voiceLv.removeView(view);
				}
				voiceList.remove(player);// 从数据中删除
			}
		}
	};
	/**
	 * 播放器控制按钮
	 */
	private OnClickListener playBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			VoicePlayer player = (VoicePlayer) v.getTag();
			if (indexPlay != null && player.id == indexPlay.id) {
				// 是自己在播放
				if (mPlayer.isPlay()) { // 暂停
					v.setBackgroundResource(R.drawable.recordplay);
					mPlayer.pause();
					mTimerTask.cancel();
				} else {// 播放
					v.setBackgroundResource(R.drawable.recordpause);
					mPlayer.start();
					startTime();
				}
			} else {
				if (mPlayer.isPlay())
					reductionView();
				indexPlay = player;
				// 自己开始进行播放 修改自己的状态
				v.setBackgroundResource(R.drawable.recordpause);
				mPlayer.startPlay(player.file.getPath(), onCompletion);
				getView(v);
				startTime();// 开始计时器
			}
		}
	};

	/**
	 * 播放器控制事件
	 */
	private OnCompletionListener onCompletion = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			reductionView();
		}
	};

	/**
	 * 播放器控制计时器
	 */
	private void startTime() {
		mTimer = new Timer();
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (null != seekBar && null != mPlayer) {
					seekBar.setProgress(mPlayer.getProgress());
					timer += 10;
					handler.sendEmptyMessage(1);
				}
			}
		};
		if (null != mPlayer && null != mTimer && null != mTimerTask) {
			mTimer.schedule(mTimerTask, 0, 10);
		}
	}

	private Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			if (msg.what == 1) {
				date.setTime(timer);
				time.setText(simpleData.format(date));
			} else if (msg.what == 2) {
				mRecorder.stopRecord();
				endTime = recorderTimer;
				mRecorder = null;
				tv_message.setText("录制时间已经超时，是否保存本次录音？");
			}
		};
	};

	/**
	 * 状态还原
	 */
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
	}

	/**
	 * 获取当前播放的对象 的控件信息
	 */
	private void getView(View childs) {
		View view = (View) childs.getParent();
		playIv = (ImageView) view.findViewById(R.id.play_start_iv);
		time = (TextView) view.findViewById(R.id.play_time_tv);
		seekBar = (SeekBar) view.findViewById(R.id.play_seekBar);
		play_btn_tv = (TextView) view.findViewById(R.id.play_btn_tv);
	}

	private Timer mTimer; // 记录进度条的
	private TimerTask mTimerTask;
	private Date date = new Date();
	private TextView play_btn_tv;// 按钮状态
	private TextView time;// 播放时间
	private SeekBar seekBar;// 进度条
	private ImageView playIv;// 正在播放的对象按钮
	private VoicePlayer indexPlay = null;// 正在播放的对象
	private Map<Long, View> voiceView = new HashMap<Long, View>();
	private SimpleDateFormat simpleData = new SimpleDateFormat("mm:ss");
	private long timer;
	private int recorderSizeTimer = 180000;// 最大录制时间 单位秒
	private int recorderTimer = 0;// 当前录制时间
	private Timer timerRecorder;// 录音时长监控
	private TimerTask imterRecorder;// 录音超时后的提示效果

	private boolean isExist(JTFile jtfile) {
		if (mListUploader == null || mListUploader.size() <= 0) {
			return false;
		} else {
			for (FileUploader fileUploader : mListUploader) {
				if (fileUploader.getJTFile().mLocalFilePath.equals(jtfile.getmLocalFilePath())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isNet() {
		for (JTFile jtFile : selectedPictureSDAndNet) {
			if (TextUtils.isEmpty(jtFile.getId())) {
				return false;
			}
		}
		return true;
	}

	public void uploading() {
		if ((fileListSD == null || fileListSD.size() <= 0) && isNet()) {
			isUploading = false;
			close();
			return;
		}
		isUploading = true;
		dialog.show();
		// 上传图片/视频
		mListJtImgVideo.clear();

		for (JTFile imgFile : selectedPictureSDAndNet) {
			if (!new File(imgFile.mFileName).isFile() || !TextUtils.isEmpty(imgFile.mTaskId)) {// 如果是本地的
				continue;
			}
			imgFile.mTaskId = mTaskId;
			imgFile.mModuleType = JTFile.TYPE_DEMAND;
			FileUploader uploader = new FileUploader(imgFile, this);
			uploader.WEB_URL = EAPIConsts.FILE_DEMAND_URL;
			mListJtImgVideo.add(uploader);
			uploader.start();
		}

		for (JTFile jtFile : fileListSD) {// 上传sd卡的文件
			FileUploader uploader = new FileUploader(jtFile, this);
			uploader.WEB_URL = EAPIConsts.FILE_DEMAND_URL;
			jtFile.mModuleType = JTFile.TYPE_DEMAND;
			jtFile.mTaskId = mTaskId;
			mListUploader.add(uploader);
			uploader.start();
		}
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
		DemandReqUtil.deleteDemandFile(IntroduceActivity.this, IntroduceActivity.this, id, null);

	}

	@Override
	public void onPrepared(long uid) {
		Message msg = new Message();
		msg.what = FileUploader.Status.Prepared;
		msg.obj = uid;
		mHandler.sendMessage(msg);

	}

	@Override
	public void onStarted(long uid) {
		Message msg = new Message();
		msg.what = FileUploader.Status.Started;
		msg.obj = uid;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onUpdate(long uid, int progress) {
		Message msg = new Message();
		msg.what = FileUploader.Status.Success;
		msg.arg1 = progress;
		msg.obj = uid;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onCanceled(long uid) {
		Message msg = new Message();
		msg.what = FileUploader.Status.Canceled;
		msg.obj = uid;
		mHandler.sendMessage(msg);

	}

	@Override
	public void onSuccess(long uid, String fileUrl) {
		Message msg = new Message();
		msg.what = FileUploader.Status.Success;
		msg.obj = uid;
		mHandler.sendMessage(msg);
		Log.v(TAG, "上传成功" + fileUrl);
	}

	@Override
	public void onError(long uid, int code, String message, String filePath) {
		Log.e(TAG, message);
		Message msg = new Message();
		msg.what = 6;
		msg.obj = uid;
		mHandler.sendMessage(msg);
		Log.v(TAG, "上传失败" + filePath);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		String MSG = "  handleMessage()";

		@Override
		public void handleMessage(Message msg) {
			// 准备和准备完成
			if (msg.what == FileUploader.Status.Prepared || msg.what == FileUploader.Status.Started) {
				return;
			}
			if (msg.what == FileUploader.Status.Success) {// mListJtImgVideo

			}
			boolean b = imgIsStop();
			if (isAllSuccess() && b) {// 如果附件上传成功，图片/视频停止上传
				isUploading = false;
				showToast("上传结束");
				close();
			} else if (isStopUpLoading() && b) {// 附件是否已停止上传,图片/视频停止上传
				isUploading = false;
				dialog.dismiss();
			}
			if (accessoryAdapter != null) {
				accessoryAdapter.notifyDataSetChanged();
			}
		}

		// 附是否已停止上传
		private boolean isStopUpLoading() {// false:未停止上传 ,true停止上传
			for (FileUploader uploader : mListUploader) {
				if (uploader.getStatus() != FileUploader.Status.Success && uploader.getStatus() != FileUploader.Status.Error && uploader.getStatus() != FileUploader.Status.Canceled) {
					return false;
				}
			}
			return true;
		}

		// 是否上传成功 true:上传完成
		private boolean isAllSuccess() {// true:全部上传成功
			if (mListUploader == null || mListUploader.size() <= 0) {
				return true;
			} else {
				for (FileUploader fileUploader : mListUploader) {
					if (fileUploader.getStatus() != FileUploader.Status.Success) {// 如有一个没有上传成功
						/*
						 * Log.e(MSG, "上传失败:" +
						 * fileUploader.getJTFile().mLocalFilePath);
						 */
						return false;
					}
				}
				return true;
			}
		}

	};

	private void close() {
		dialog.dismiss();
		isUploading = false;
		Intent intent = new Intent();
		noteData.taskId = mTaskId;
		noteData.value = editTv.getText().toString();
		if (noteData != null) {
			intent.putExtra(ENavConsts.DEMAND_NOTE_DATA, noteData);
		}
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	/**
	 * 图片是否true:停止
	 */
	private boolean imgIsStop() {
		// 删除上传成功的
		for (FileUploader uploader : mListJtImgVideo) {
			for (JTFile jtFile : selectedPictureSDAndNet) {
				if (uploader.getJTFile().mCreateTime == jtFile.mCreateTime) {
					selectedPictureSDAndNet.remove(jtFile);
					break;
				}
			}
		}
		for (FileUploader uploader : mListJtImgVideo) {
			if (uploader.getStatus() != FileUploader.Status.Canceled && uploader.getStatus() != FileUploader.Status.Error && uploader.getStatus() != FileUploader.Status.Success) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 创建等待dialog
	 */
	private void createDialog() {
		dialog = new Dialog(IntroduceActivity.this, R.style.dialog);
		View v = View.inflate(IntroduceActivity.this, R.layout.widget_loading, null);
		dialog.setCancelable(false);
		dialog.setContentView(v);
		ImageView rocketImage = (ImageView) v.findViewById(R.id.loading);
		AnimationDrawable rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
		rocketAnimation.start();
	}

	/**
	 * true:正在上传 false上传完成
	 */
	private boolean isUploading;

	private DemandAttFile attFile;

	private DemandPvFile pvFile;

	private Dialog dialog;

	// private LinearLayout showBackPicVidHParentlv;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 如果是返回
			close();
			/*
			 * if(!isUploading){//如果正在上传 showToast("正在上传..."); return false; }
			 */
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		if (TextUtils.isEmpty(mTaskId)) {
			return;
		}
		DemandReqUtil.getDemandFile(IntroduceActivity.this, IntroduceActivity.this, mTaskId, null);
	}

	@Override
	public void bindData(int tag, Object object) {
		if (object == null) {
			return;
		}
		if (tag == EAPIConsts.demandReqType.demand_findDemandFile) {// 查询需求介绍内容
			// selectedPictureNet.clear();
			IntroduceData data = (IntroduceData) object;
			attFile = data.attFile;// 附件
			pvFile = data.pvFile;// 视频/图片
			for (DemandFile demandFile : attFile.getFileList()) {// 附件
				JTFile jtFile = new JTFile();
				jtFile.setId(demandFile.id);
				jtFile.setmType(demandFile.filetype);
				jtFile.setmLocalFilePath(demandFile.filePath);
				jtFile.mFileName = demandFile.fileTitle;
				fileListNet.add(jtFile);
				fileListSDAndNet.add(jtFile);
			}
			selectedPictureSDAndNet.clear();
			for (DemandFile pvFiles : pvFile.getFileList()) {// jtFile.mType = 2
				JTFile jtFile = new JTFile();
				jtFile.mTaskId = mTaskId;
				jtFile.mScreenshotFilePath = pvFiles.thumbnailspath;
				jtFile.mUrl = pvFiles.filePath;
				jtFile.setId(pvFiles.id);
				jtFile.setmType(pvFiles.filetype);
				jtFile.setmLocalFilePath(pvFiles.filePath);
				jtFile.mFileName = pvFiles.fileTitle;
				selectedPictureSDAndNet.add(jtFile);
				/*
				 * ImageItem imageItem = new ImageItem(pvFiles.filePath,
				 * pvFiles.filetype == 2, pvFiles.fileTitle, pvFiles.id);
				 * imageItem.type = pvFiles.filetype;//
				 * http://192.168.101.22:81/web1/0032900961
				 * imageItem.thumbnailspath = pvFiles.thumbnailspath;
				 * Picture.add(imageItem);
				 */
			}
			// selectedPictureSDAndNet.addAll(Picture);
			// addImage(Picture);
			if (accessoryAdapter != null) {
				accessoryAdapter.notifyDataSetChanged();
				resetListView();
			}
			if (mPictureAdapter != null) {
				showImage();
				// mPictureAdapter.notifyDataSetChanged();
			}
		} else if (tag == EAPIConsts.demandReqType.demand_deleteFile) {// 删除一个附件
			if ((Boolean) object) {
				accessoryAdapter.notifyDataSetChanged();
				mPictureAdapter.notifyDataSetChanged();
				mPictureAdapter.notifyDataSetChanged();
			}
		}
	}


	@Override
	public void onSuccessForJTFile(JTFile jtFile) {
		// TODO Auto-generated method stub
		
	}

}
