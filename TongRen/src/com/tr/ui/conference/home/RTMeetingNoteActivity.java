/*
 * Copyright (C) 2015 Emanuel Moecklin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tr.ui.conference.home;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MMeetingNoteDetail;
import com.tr.model.conference.MMeetingNoteObj;
import com.tr.model.conference.MMeetingNoteQuery;
import com.tr.model.obj.JTFile;
import com.tr.ui.conference.common.ImageUtils;
import com.tr.ui.demand.util.FileUploader;
import com.tr.ui.demand.util.FileUploader.OnFileUploadListener;
import com.tr.ui.widgets.EProgressDialog;
import com.utils.common.EUtil;
import com.utils.common.FileDownloader;
import com.utils.common.FileDownloader.OnFileDownloadListener;
import com.utils.common.TaskIDMaker;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.note.json.Attachment;
import com.utils.note.json.Attributes;
import com.utils.note.json.HtmlToJson;
import com.utils.note.json.JsonText;
import com.utils.note.json.JsonToHtml;
import com.utils.note.json.Runs;
import com.utils.note.rteditor.RTEditText;
import com.utils.note.rteditor.RTManager;
import com.utils.note.rteditor.api.RTApi;
import com.utils.note.rteditor.api.RTMediaFactoryImpl;
import com.utils.note.rteditor.api.RTProxyImpl;
import com.utils.note.rteditor.api.format.RTFormat;
import com.utils.note.rteditor.utils.Constants;
import com.utils.note.rteditor.utils.Helper;
import com.utils.note.view.AudioImage;
import com.utils.note.view.AudioPlayView;
import com.utils.note.view.FileConstans;
import com.utils.note.view.FontPopupView;
import com.utils.note.view.RichTextEditor;
import com.utils.note.view.SineWave;
import com.utils.note.view.SoundMeter;
import com.utils.note.view.Utils;
import com.utils.string.StringUtils;

@SuppressWarnings("unused")
public class RTMeetingNoteActivity extends Activity implements View.OnClickListener, View.OnLayoutChangeListener, IBindData {
	private static final String TAG = "RTMeetingNoteActivity";
	public static final int REQUEST_CODE_GET_IMG = 606;
	public static final int REQUEST_CODE_GET_IMAGE_VIA_CAMERA = 607;
	public static final int UPLOAD_SUCCESS = 11;
	public static final int UPLOAD_FAILED = 12;
	public static final int UPLOAD_CANCELED = 13;
	public static final int DOWNLOAD_SUCCESS = 14;
	public static final int DOWNLOAD_FAILED = 15;
	public static final int DOWNLOAD_CANCELED = 16;
	protected Uri _protoUri;
	/**音频缓存目录*/
	private String _audioDir;
	/**图片缓存目录*/
	private String _imageDir;
	/**json缓存目录*/
	private String _jsonDir;

	private RTManager mRTManager;
	/**富文本编辑控件*/
	private RTEditText mRTMessageField;
	private LinearLayout _rte_titleview;
	private FrameLayout _layout_top;
	
	/**编辑工具栏view*/
	private View _layout_toolbar;
	private PopupWindow _popupWindow;
	private int _popTop;
	private int _popLeft;
	/**工具栏字体按钮*/
	private ImageButton _btn_tool_font;
	/**工具栏录音按钮*/
	private ImageButton _btn_tool_audio;
	/**录音布局*/
	private RelativeLayout _audioRecodeView;
	private AudioPlayView _audioPlayView;

	/**会议笔记名称和内容的scrollview*/
	private ScrollView _rte_content;

	public static final int WHAT_STOP_RECODE = 3;
	public static final int WHAT_STOP_PLAY = 4;

	private View activityRootView;
	private int keyHeight = 0;
	private boolean _isKeyBoardShow;

	private boolean _isRecoding;
	SineWave _sw = null;
	private String _voiceName;
	private SoundMeter _sensor;
	private String _totalTime;
	private static final int POLL_INTERVAL = 1000 / 200;

	private Chronometer _timedown;
	/**笔记标题*/
	private EditText _rtet_subject;
	private int _meeting_id;
	/**会议笔记列表*/
	private MMeetingNoteQuery mMeetingNoteQuery = null;
	protected EProgressDialog progressDialog;
	private String _createTime;
	/**json缓存路径名*/
	private String _preJsonPath;
	int cy = 0;
	boolean isAdd = true;
	private Button _btn_save;
	private List<FileUploader> _uploaderList = new ArrayList<FileUploader>();
	private List<FileDownloader> _downloaderList = new ArrayList<FileDownloader>();
	private int _attachmentCount;
	private JsonText _jsonText;

	private Handler _handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constants.START_AUDIO_RECODE:
				_rte_titleview.setVisibility(View.GONE);
				_audioRecodeView.setVisibility(View.VISIBLE);
				if (_audioPlayView.getView().getVisibility() == View.VISIBLE) {
					_audioPlayView.getView().setVisibility(View.GONE);
					_audioPlayView.realse();
				}
				startRecode();
				break;
			case Constants.START_AUDIO_PLAYER:
				if (_audioPlayView.getView().getVisibility() == View.VISIBLE) {
					_audioPlayView.realse();
				}
				_rte_titleview.setVisibility(View.GONE);
				_audioPlayView.getView().setVisibility(View.VISIBLE);
				if (_audioRecodeView.getVisibility() == View.VISIBLE) {
					stopAudioRecode(true);
				}
				String audio = (String) msg.obj;
				playAudio(audio);
				break;
			case WHAT_STOP_RECODE:
				_audioRecodeView.setVisibility(View.GONE);
				if (_audioPlayView.getView().getVisibility() != View.VISIBLE) {
					_rte_titleview.setVisibility(View.VISIBLE);
				}
				_btn_tool_audio.setImageResource(R.drawable.selector_ic_toolbar_audio);
				AudioImage image = (AudioImage) msg.obj;
				// String path = "file:///" + image.getPath();
				String path = image.getPath();
				String audioPath = image.getAudioPath();
				mRTManager.insertAudio(path, audioPath);
				break;
			case WHAT_STOP_PLAY:
				_audioPlayView.getView().setVisibility(View.GONE);
				_rte_titleview.setVisibility(View.VISIBLE);
				break;
			case UPLOAD_SUCCESS:
				createMeetingNote();
				break;
			case UPLOAD_FAILED:
				dismissLoadingDialog();
				showFailDialog("会议笔记保存失败");
				break;
			case UPLOAD_CANCELED:
				dismissLoadingDialog();
				break;
			case DOWNLOAD_SUCCESS:
				loadTextFromJson();
				break;
			case DOWNLOAD_FAILED:
				dismissLoadingDialog();
				showFailDialog("会议笔记下载失败");
				break;
			case DOWNLOAD_CANCELED:
				dismissLoadingDialog();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// read extras
		String message = null;
		super.onCreate(savedInstanceState);

		setContentView(R.layout.rte_layout_main);

		activityRootView = findViewById(R.id.root_layout);
		int screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		keyHeight = screenHeight / 3;

		FrameLayout _layout_top = (FrameLayout) findViewById(R.id.layout_top);
		_rte_titleview = (LinearLayout) findViewById(R.id.rte_titleview);
		_audioRecodeView = (RelativeLayout) findViewById(R.id.rte_audiorecoder);
		_audioPlayView = new AudioPlayView(this, _handler);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		_layout_top.addView(_audioPlayView.getView());
		_audioRecodeView.setVisibility(View.GONE);
		_audioPlayView.getView().setVisibility(View.GONE);

		_btn_save = (Button) _rte_titleview.findViewById(R.id.btn_save);
		LinearLayout btn_back = (LinearLayout) _rte_titleview.findViewById(R.id.rte_backBtn);
		_btn_save.setOnClickListener(this);
		btn_back.setOnClickListener(this);

		_sw = new SineWave(this);
		_timedown = (Chronometer) _audioRecodeView.findViewById(R.id.timedown);
		_timedown.setFormat("%s");
		_sensor = new SoundMeter();
		Button btn_stop = (Button) _audioRecodeView.findViewById(R.id.btn_stop);
		btn_stop.setOnClickListener(this);

		_rte_content = (ScrollView) findViewById(R.id.rte_content);
		// initialize rich text manager
		RTApi rtApi = new RTApi(this, new RTProxyImpl(this), new RTMediaFactoryImpl(this, true));
		//初始化编辑器
		mRTManager = new RTManager(rtApi, savedInstanceState);
		mRTManager.setActivityHandler(_handler);

		_layout_toolbar = findViewById(R.id.layout_toolbar);
		_layout_toolbar.setVisibility(View.GONE);
		ImageButton btn_tool_keybord = (ImageButton) _layout_toolbar.findViewById(R.id.btn_tool_keybord);
		ImageButton btn_tool_camer = (ImageButton) _layout_toolbar.findViewById(R.id.btn_tool_camer);
		ImageButton btn_tool_image = (ImageButton) _layout_toolbar.findViewById(R.id.btn_tool_image);
		_btn_tool_audio = (ImageButton) _layout_toolbar.findViewById(R.id.btn_tool_audio);
		_btn_tool_font = (ImageButton) _layout_toolbar.findViewById(R.id.btn_tool_font);
		btn_tool_keybord.setOnClickListener(this);
		btn_tool_camer.setOnClickListener(this);
		btn_tool_image.setOnClickListener(this);
		_btn_tool_audio.setOnClickListener(this);
		_btn_tool_font.setOnClickListener(this);

		_rtet_subject = (EditText) findViewById(R.id.rtet_subject);
		_rtet_subject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && _isKeyBoardShow) {
					if (_layout_toolbar.getVisibility() == View.VISIBLE) {
						_layout_toolbar.setVisibility(View.GONE);
					}
				}
			}
		});
		mRTMessageField = (RTEditText) findViewById(R.id.rtEditText_1);
		//注册编辑器view
		mRTManager.registerEditor(mRTMessageField, true);
		if (message != null) {
			mRTMessageField.setRichTextEditing(true, message);
		}
		//设置焦点改变的监听
		mRTMessageField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && _isKeyBoardShow) {
					_layout_toolbar.setVisibility(View.VISIBLE);
					scrollToBottom();
				}
			}
		});
		TextWatcher tw = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				_btn_save.setEnabled(true);
				// _btn_save.setTextColor(Color.WHITE);
				_btn_save.setTextColor(App.getApplicationConxt().getResources().getColor(R.color.text_black));
				if (TextUtils.isEmpty(_rtet_subject.getText())) {
					_btn_save.setEnabled(false);
					_btn_save.setTextColor(Color.GRAY);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		};
		mRTMessageField.addMyTextChangeListener(tw);
		_rtet_subject.addTextChangedListener(tw);
		initFileDirs();
		loadData();
	}
	/**初始化缓存文件*/
	private void initFileDirs() {
		String cacheDir = EUtil.getAppCacheFileDir(this).getAbsolutePath();
		_audioDir = cacheDir + FileConstans.DIR_AUDIO_SAVE;
		_imageDir = cacheDir + FileConstans.DIR_IMAGE_SAVE;
		_jsonDir = cacheDir + FileConstans.DIR_JSON_SAVE;
		System.out.println("audio:" + _audioDir);
		System.out.println("image:" + _imageDir);
		System.out.println("json:" + _jsonDir);
		File audio = new File(_audioDir);
		if (!audio.exists()) {
			audio.mkdirs();
		}
		File image = new File(_imageDir);
		if (!image.exists()) {
			image.mkdirs();
		}
		File json = new File(_jsonDir);
		if (!json.exists()) {
			json.mkdirs();
		}
	}

	public String getAudioDir() {
		return _audioDir;
	}

	public String getImageDir() {
		return _imageDir;
	}

	public String getJsonDir() {
		return _jsonDir;
	}
	/**请求数据*/
	private void loadData() {
		Intent intent = getIntent();
		if (intent != null) {
			_meeting_id = intent.getIntExtra("meeting_id", 0);
			if (0 != _meeting_id) {
				showLoadingDialog();
				ConferenceReqUtil.doGetNoteByMeetingId(this, this, _meeting_id, null);
			} else {
				Toast.makeText(this, "参数错误，没有会议编号", 0).show();
				finish();
			}
		}
	}
	/**填充数据*/
	private void fillData() {
		//笔记名称
		String meetingNoteTitle = mMeetingNoteQuery.getMeetingNoteTitle();
		int mId = mMeetingNoteQuery.getMeetingId();
		/**创建时间*/
		String time = mMeetingNoteQuery.getCreateTime();
		if (!TextUtils.isEmpty(meetingNoteTitle)) {
			_rtet_subject.setText(meetingNoteTitle);
		}
		/**会议笔记详情列表*/
		List<MMeetingNoteDetail> listMeetingNoteDetail = mMeetingNoteQuery.getListMeetingNoteDetail();
		if (listMeetingNoteDetail != null && listMeetingNoteDetail.size() > 0) {
			MMeetingNoteDetail mMeetingNoteDetail = listMeetingNoteDetail.get(0);
			if (mMeetingNoteDetail != null) {
				_preJsonPath = _jsonDir + time.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "") + "GT" + mId + ".json";
				File file = new File(_preJsonPath);
				System.out.println("cache:" + _preJsonPath);
				//json缓存存在，读取缓存
				if (file.exists()) {
					System.out.println("get cache:" + _preJsonPath);
					String json = Utils.file2String(file, "UTF-8");
					Gson gson = new Gson();
					_jsonText = gson.fromJson(json, JsonText.class);
					loadTextFromJson();
				} 
				//json缓存不存在
				else {
					String formatedContent = mMeetingNoteDetail.getFormatedContent();
					System.out.println(formatedContent);
					if (!StringUtils.isEmpty(formatedContent)) {
						downLoadAttachment(formatedContent);
					} else if (!StringUtils.isEmpty(mMeetingNoteDetail.getMeetingNoteContent())) {
						mRTMessageField.setText(mMeetingNoteDetail.getMeetingNoteContent());
						dismissLoadingDialog();
					}
				}
			} else {
				dismissLoadingDialog();
			}
		} else {
			dismissLoadingDialog();
		}
	}

	public void showLoadingDialog() {
		if (null == progressDialog) {
			progressDialog = new EProgressDialog(this);
		}
		progressDialog.setMessage("");
		progressDialog.show();
	}

	public void dismissLoadingDialog() {
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		activityRootView.addOnLayoutChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (_audioPlayView != null) {
			_audioPlayView.pausePlay();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mRTManager != null) {
			mRTManager.onDestroy(true);
		}
		if (_audioPlayView != null) {
			_audioPlayView.realse();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mRTManager.onSaveInstanceState(outState);
	}

	private void playAudio(String audio) {
		_audioPlayView.startPlay(audio);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_tool_keybord) {
			hideKeyboard();
		} else if (id == R.id.btn_tool_camer) {
			changeToImage(true);
		} else if (id == R.id.btn_tool_image) {
			changeToImage(false);
		} else if (id == R.id.btn_stop) {
			stopAudioRecode(true);
		} else if (id == R.id.btn_save) {
			saveTextToJson();
		} else if (id == R.id.btn_tool_audio) {
			_btn_tool_audio.setImageResource(R.drawable.ic_toolbar_audio_play);
			mRTManager.onStartAudioRecode();
		} else if (id == R.id.btn_tool_font) {
			if (_popupWindow != null && _popupWindow.isShowing()) {
				_popupWindow.dismiss();
				_btn_tool_font.setImageResource(R.drawable.selector_ic_toolbar_font);
			} else {
				showFontPop(_btn_tool_font);
				_btn_tool_font.setImageResource(R.drawable.ic_toolbar_font_press);
			}
		} else if (id == R.id.rte_backBtn) {
			finish();
		}
	}

	private void saveTextToJson() {
		new AsyncTask<Void, Void, JsonText>() {
			private String text;
			private String html;

			protected void onPreExecute() {
				showLoadingDialog();
				_attachmentCount = 0;
				_uploaderList.clear();
				_jsonText = null;
				Calendar c = Calendar.getInstance();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				_createTime = dateFormat.format(new Date(c.getTimeInMillis()));
				text = mRTMessageField.getText().toString();
				html = mRTMessageField.getText(RTFormat.JSON);
			};

			protected JsonText doInBackground(Void... params) {
				HtmlToJson htj = new HtmlToJson(text);
				JsonText jsonText = htj.convertHtmlToJson(html);
				_attachmentCount = htj.getAttachmentCount();
				if (!TextUtils.isEmpty(_preJsonPath)) {
					File file = new File(_preJsonPath);
					if (file.exists()) {
						file.delete();
					}
				}
				String jsonPath = _jsonDir + _createTime.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "") + "GT" + _meeting_id + ".json";
				_preJsonPath = jsonPath;
				System.out.println("save json path:" + jsonPath);
				File file = new File(jsonPath);
				if (file.exists()) {
					file.delete();
				}
				try {
					file.createNewFile();
					Gson gson = new Gson();
					String res = gson.toJson(jsonText);
					Utils.string2File(res, jsonPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return jsonText;
			};

			protected void onPostExecute(JsonText result) {
				if (result != null) {
					_jsonText = result;
					List<Runs> runs = result.getRuns();
					if (_attachmentCount > 0) {
						for (Runs r : runs) {
							Attributes attributes = r.getAttributes();
							Attachment attachment = attributes.getAttachment();
							if (attachment != null) {
								String suffix = attachment.getSuffix();
								String type = attachment.getType();
								String url = attachment.getUrl() + suffix;
								OnFileUploadListener uploadListener = new MyUploadListener(attachment);
								FileUploader uploader = new FileUploader(url, uploadListener);
								_uploaderList.add(uploader);
							}
						}
						if (_uploaderList.size() != _attachmentCount) {
							dismissLoadingDialog();
							// Toast.makeText(getApplicationContext(), "上传附件失败",
							// Toast.LENGTH_SHORT).show();
							showFailDialog("会议笔记保存失败");
						} else {
							_uploaderList.get(_attachmentCount - 1).start();
						}
					} else {
						createMeetingNote();
					}
				} else {
					dismissLoadingDialog();
					showFailDialog("会议笔记保存失败");
				}
			};
		}.execute();
	}

	class MyUploadListener implements OnFileUploadListener {
		private Attachment _attachment;

		public MyUploadListener(Attachment attachment) {
			_attachment = attachment;
		}

		@Override
		public void onPrepared() {
		}

		@Override
		public void onStarted() {
		}

		@Override
		public void onUpdate(int value) {
		}

		@Override
		public void onCanceled() {
			Message msg = Message.obtain();
			msg.what = UPLOAD_CANCELED;
			_handler.sendMessage(msg);
		}

		@Override
		public void onSuccess(String fileUrl) {
			System.out.println("success:" + fileUrl);
			_attachmentCount--;
			_attachment.setUrl(fileUrl);
			if (_attachmentCount == 0) {
				Message msg = Message.obtain();
				msg.what = UPLOAD_SUCCESS;
				_handler.sendMessage(msg);
			} else {
				_uploaderList.get(_attachmentCount - 1).start();
			}
		}

		@Override
		public void onError(int code, String message) {
			Message msg = Message.obtain();
			msg.what = UPLOAD_FAILED;
			_handler.sendMessage(msg);
		}
	}

	private void createMeetingNote() {
		if (_jsonText == null) {
			// Toast.makeText(this, "保存会议笔记失败", 0).show();
			showFailDialog("会议笔记保存失败");
			dismissLoadingDialog();
			return;
		}
		Gson gson = new Gson();
		String json = gson.toJson(_jsonText);
		String text = _jsonText.getString();

		if (null == mMeetingNoteQuery) {
			mMeetingNoteQuery = new MMeetingNoteQuery();
		}
		String title = _rtet_subject.getText().toString().trim();
		mMeetingNoteQuery.setCreater(App.getUserID());

		mMeetingNoteQuery.setCreateTime(_createTime);
		if (title != null) {
			mMeetingNoteQuery.setMeetingNoteTitle(title);
		} else {
			mMeetingNoteQuery.setMeetingNoteTitle("");
		}
		mMeetingNoteQuery.setMeetingId(_meeting_id);
		List<MMeetingNoteDetail> aList = new ArrayList<MMeetingNoteDetail>();
		MMeetingNoteDetail aNote = new MMeetingNoteDetail();
		aNote.setMeetingNoteContent(text);
		aNote.setFormatedContent(json);
		aNote.setTaskId(TaskIDMaker.getTaskId(App.getUser().getmNick()));
		aList.add(aNote);
		mMeetingNoteQuery.setListMeetingNoteDetail(aList);
		MMeetingNoteObj aObj = new MMeetingNoteObj();
		aObj.setMeetingNoteQuery(mMeetingNoteQuery);
		ConferenceReqUtil.doSaveMeetingNote(this, this, aObj, null);
	}

	private void downLoadAttachment(String json) {
		_downloaderList.clear();
		_attachmentCount = 0;
		_jsonText = null;
		Gson gson = new Gson();
		_jsonText = gson.fromJson(json, JsonText.class);
		if (_jsonText != null) {
			List<Runs> runs = _jsonText.getRuns();
			for (Runs r : runs) {
				Attributes attributes = r.getAttributes();
				Attachment attachment = attributes.getAttachment();
				if (attachment != null) {
					String url = attachment.getUrl();
					String suffix = attachment.getSuffix();
					if (!suffix.startsWith(".")) {
						suffix = "." + suffix;
					}
					attachment.setSuffix(suffix);
					JTFile jtFile = new JTFile();
					jtFile.mFileName = url.substring(url.lastIndexOf(File.separator) + 1) + suffix;
					jtFile.setmSuffixName(suffix);
					jtFile.setmUrl(url);
					jtFile.setmType(JTFile.TYPE_FILE);
					OnFileDownloadListener downListener = new MyOnDownloadListener(attachment);
					FileDownloader downloader = new FileDownloader(getApplicationContext(), jtFile, downListener);
					_downloaderList.add(downloader);
				}
			}
		}
		_attachmentCount = _downloaderList.size();
		if (_attachmentCount > 0) {
			_downloaderList.get(_attachmentCount - 1).start();
		} else {
			loadTextFromJson();
		}
	}

	private void loadTextFromJson() {
		new AsyncTask<Void, Void, String>() {
			protected void onPreExecute() {

			};

			protected String doInBackground(Void... params) {
				if (_jsonText != null) {
					String html = new JsonToHtml().convertJsonTextToHtml(_jsonText);
					return html;
				} else {
					return null;
				}
			};

			protected void onPostExecute(String result) {
				if (result == null) {
					dismissLoadingDialog();
					showFailDialog("会议笔记下载失败");
					return;
				}
				mRTMessageField.setRichTextEditing(true, result);
				mRTMessageField.setSelection(mRTMessageField.length());
				mRTMessageField.append("\n");
				_btn_save.setEnabled(false);
				_btn_save.setTextColor(Color.GRAY);
				dismissLoadingDialog();
			};
		}.execute();
	}

	private class MyOnDownloadListener implements OnFileDownloadListener {
		private Attachment _attachment;

		public MyOnDownloadListener(Attachment attachment) {
			_attachment = attachment;
		}

		@Override
		public void onPrepared(String url) {
		}

		@Override
		public void onStarted(String url) {

		}

		@Override
		public void onSuccess(String url, JTFile jtFile) {
			System.out.println("down success:" + jtFile.mLocalFilePath);
			String path = jtFile.mLocalFilePath;
			_attachment.setUrl(path.substring(0, path.lastIndexOf(".")));
			_attachmentCount--;
			if (_attachmentCount == 0) {
				Gson gson = new Gson();
				String json = gson.toJson(_jsonText);
				int mId = mMeetingNoteQuery.getMeetingId();
				String time = mMeetingNoteQuery.getCreateTime();
				File file = new File(_preJsonPath);
				if (file.exists()) {
					file.delete();
				}
				Utils.string2File(json, _preJsonPath);
				Message msg = Message.obtain();
				msg.what = DOWNLOAD_SUCCESS;
				_handler.sendMessage(msg);
			} else {
				_downloaderList.get(_attachmentCount - 1).start();
			}
		}

		@Override
		public void onError(String url, int code, String errMsg) {
			System.out.println("down error:" + errMsg);
			Message msg = Message.obtain();
			msg.what = DOWNLOAD_FAILED;
			_handler.sendMessage(msg);
		}

		@Override
		public void onUpdate(String url, int progress) {
		}

		@Override
		public void onCanceled(String url) {
			Message msg = Message.obtain();
			msg.what = DOWNLOAD_CANCELED;
			_handler.sendMessage(msg);
		}

	}

	private void showAlertDialog(DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(this).setTitle("提示").setMessage("音频操作正在进行中，是否退出音频操作？").setPositiveButton("确定", listener).setNegativeButton("取消", null).show();
	}

	private void showFailDialog(String message) {
		new AlertDialog.Builder(this).setTitle("提示").setMessage(message).setPositiveButton("确定", null).show();
	}

	private void changeToImage(final boolean isPhoto) {
		if (_audioRecodeView.getVisibility() == View.VISIBLE) {
			showAlertDialog(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					stopAudioRecode(false);
					gotoImage(isPhoto);
				}
			});
		} else if (_audioPlayView.getView().getVisibility() == View.VISIBLE) {
			if (_audioPlayView.getIsPlay()) {
				showAlertDialog(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						_audioPlayView.getView().setVisibility(View.GONE);
						_rte_titleview.setVisibility(View.VISIBLE);
						_audioPlayView.realse();
						gotoImage(isPhoto);
					}
				});
			} else {
				_audioPlayView.getView().setVisibility(View.GONE);
				_rte_titleview.setVisibility(View.VISIBLE);
				_audioPlayView.realse();
				gotoImage(isPhoto);
			}
		} else {
			gotoImage(isPhoto);
		}
	}

	private void gotoImage(boolean isPhoto) {
		if (isPhoto) {
			openTakePhoto();
			// mRTManager.onCaptureImage();

		} else {
			// mRTManager.onPickImage();
			openImgSelect();
		}
		// hideKeyboard();
	}

	private void openTakePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String filename = timeStampFormat.format(new Date());
		String path = _imageDir + filename + ".jpg";
		File file = new File(path);
		_protoUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, _protoUri);
		startActivityForResult(intent, REQUEST_CODE_GET_IMAGE_VIA_CAMERA);
	}

	private void openImgSelect() {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_CODE_GET_IMG);
	}

	private void onSelectImg(String path) {
		if (!TextUtils.isEmpty(path) && mRTManager != null) {
			mRTManager.insertImage(path);
		}
	}

	private void onSelectImg(Uri selectedImage) {
		if (selectedImage != null) {
			String src = Utils.getImageAbsolutePath(this, selectedImage);
			String path = _imageDir + SystemClock.currentThreadTimeMillis() + ".jpg";
			Bitmap bMapRotate = null;
			Bitmap bitmap = null;
			int width = 400;

			int orientation = ImageUtils.readPictureDegree(src);
			if (orientation == 90 || orientation == 270) {
				Point pt = ImageUtils.getBitmapWidthAndHeight(src);
				if (pt != null && pt.x > 0 && pt.y > 0) {
					width = width * pt.x / pt.y;
				}
			}
			bitmap = ImageUtils.getBitmapFromFileEx(src, width);
			bMapRotate = ImageUtils.fixSnapshotOrder(bitmap, src);
			ImageUtils.saveBitmapToFile(bMapRotate, path, 80);

			ImageUtils.recycleBitmap(bitmap);
			ImageUtils.recycleBitmap(bMapRotate);
			mRTManager.insertImage(path);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_GET_IMG) {
			if (resultCode == RESULT_OK) {
				Uri selectedImage = null;
				if (data != null) {
					selectedImage = data.getData();
				}
				onSelectImg(selectedImage);
			} else {
				onSelectImg("");
			}
		} else if (requestCode == REQUEST_CODE_GET_IMAGE_VIA_CAMERA) {
			if (resultCode == RESULT_OK) {
				Uri selectedImage = null;
				if (data != null) {
					selectedImage = data.getData();
				}
				if (selectedImage == null) {
					selectedImage = _protoUri;
					onSelectImg(selectedImage);
				} else {
					onSelectImg(selectedImage);
				}
			}

		}
	}

	private void stopAudioRecode(boolean isInsertAudio) {
		_audioRecodeView.setVisibility(View.GONE);
		_timedown.setVisibility(View.GONE);
		if (isInsertAudio) {
			stopRecoder();
			hideRecoderView();
		} else {
			_rte_titleview.setVisibility(View.VISIBLE);
			stopRecoder();
		}
		_isRecoding = false;
		_btn_tool_audio.setImageResource(R.drawable.selector_ic_toolbar_audio);
	}

	private void showFontPop(View view) {
		System.out.println(mRTMessageField.getText(RTFormat.HTML));
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int[] screenSize = Helper.getScreenSize();
		if (_popupWindow == null) {
			FontPopupView fontView = new FontPopupView(this, mRTManager);
			_popupWindow = new PopupWindow(fontView.getView(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			// _popupWindow.setFocusable(true);
			_popupWindow.setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));
			fontView.getView().measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			_popTop = location[1] - fontView.getView().getMeasuredHeight();
			_popLeft = (screenSize[0] - fontView.getView().getMeasuredWidth()) / 2;
			_popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, _popLeft, _popTop);
		} else {
			_popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, _popLeft, _popTop);
		}
	}

	private void hideKeyboard() {
		mRTMessageField.clearFocus();
		mRTMessageField.setFocusable(false);
		_rtet_subject.clearFocus();
		_rtet_subject.setFocusable(false);
		_layout_toolbar.setVisibility(View.GONE);
		hideSoftKeybord();
		mRTMessageField.setFocusable(true);
		mRTMessageField.setFocusableInTouchMode(true);
		_rtet_subject.setFocusable(true);
		_rtet_subject.setFocusableInTouchMode(true);
	}

	public void hideSoftKeybord() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mRTMessageField.getWindowToken(), 0);
	}

	public void showSoftKeybord() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mRTMessageField, InputMethodManager.SHOW_FORCED);
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
			_isKeyBoardShow = true;
			if (!_rtet_subject.isFocused()) {
				_layout_toolbar.setVisibility(View.VISIBLE);
			} else {
				_layout_toolbar.setVisibility(View.GONE);
			}
		} else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
			if (_popupWindow != null && _popupWindow.isShowing()) {
				_popupWindow.dismiss();
				_btn_tool_font.setImageResource(R.drawable.selector_ic_toolbar_font);
			}
			_isKeyBoardShow = false;
			_layout_toolbar.setVisibility(View.GONE);
		}
	}

	private void scrollToBottom() {
		_handler.post(new Runnable() {
			@Override
			public void run() {
				_rte_content.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}

	public void startRecode() {
		if (!_isRecoding) {
			_timedown.setVisibility(View.VISIBLE);
			_timedown.setBase(SystemClock.elapsedRealtime());
			_timedown.start();
			_voiceName = System.currentTimeMillis() + ".mp4";
			startRecoder(_audioDir + _voiceName);
			_isRecoding = true;
		}
	}

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stopRecoder();
		}
	};
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = _sensor.getAmplitude();
			updateDisplay(amp);
			_handler.postDelayed(mPollTask, POLL_INTERVAL);
		}
	};

	private void startRecoder(String path) {
		_sensor.start(path);
		_handler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	public void stopRecoder() {
		if (_timedown != null) {
			_totalTime = _timedown.getText().toString();
			_timedown.setBase(SystemClock.elapsedRealtime());
			_timedown.stop();
		}
		_handler.removeCallbacks(mSleepTask);
		_handler.removeCallbacks(mPollTask);
		_sensor.stop();
	}

	public void hideRecoderView() {
		Message msg = Message.obtain();
		msg.what = RichTextEditor.WHAT_STOP_RECODE;
		msg.obj = new AudioImage(this, _voiceName, _totalTime);
		_handler.sendMessage(msg);
	}

	private void updateDisplay(double signalEMA) {
		int y = (int) signalEMA;
		if (y == 0) {
			if (isAdd) {
				cy++;
				isAdd = cy < 10;
			} else {
				cy--;
				isAdd = cy > 0;
			}
			_sw.Set(cy, 1, 0);
		} else {
			_sw.Set(y * 2 + 20, 1, 0);
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		/**会议笔记查询结果*/
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETNOTEBYMEETINGID && null != object) {
			MMeetingNoteObj aMeetingNote = (MMeetingNoteObj) object;
			if (null != aMeetingNote) {
				MMeetingNoteQuery aQuery = aMeetingNote.getMeetingNoteQuery();
				if (null != aQuery) {
					mMeetingNoteQuery = aQuery;
					fillData();
				} else {
					dismissLoadingDialog();
				}
			} else {
				dismissLoadingDialog();
				_btn_save.setEnabled(false);
				_btn_save.setTextColor(Color.GRAY);
			}
		} 
		/**保存*/
		else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SAVEMEETINGNOTE && null != object) {
			dismissLoadingDialog();
			SimpleResult flag = (SimpleResult) object;
			if (null != flag) {
				if (flag.isSucceed()) {
					Toast.makeText(this, "保存会议笔记成功", 0).show();
					_btn_save.setEnabled(false);
					_btn_save.setTextColor(Color.GRAY);
					// finish();
				} else {
					// Toast.makeText(this, "保存会议笔记失败", 0).show();
					showFailDialog("会议笔记保存失败");
				}
			}
		}
	}
}