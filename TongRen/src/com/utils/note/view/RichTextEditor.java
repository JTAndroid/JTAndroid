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

package com.utils.note.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.tr.R;
import com.utils.note.json.JsonToHtml;
import com.utils.note.json.ParagraphStyle;
import com.utils.note.rteditor.RTEditText;
import com.utils.note.rteditor.RTManager;
import com.utils.note.rteditor.api.RTApi;
import com.utils.note.rteditor.api.RTMediaFactoryImpl;
import com.utils.note.rteditor.api.RTProxyImpl;
import com.utils.note.rteditor.api.format.RTFormat;
import com.utils.note.rteditor.effects.Effects;
import com.utils.note.rteditor.utils.Constants;
import com.utils.note.rteditor.utils.Helper;

@SuppressWarnings("unused")
public class RichTextEditor extends Activity implements View.OnClickListener,
		View.OnLayoutChangeListener {
	public static final int REQUEST_CODE_GET_IMG = 606;
	public static final int REQUEST_CODE_GET_IMAGE_VIA_CAMERA = 607;
	protected Uri _protoUri;

	private RTManager mRTManager;
	private RTEditText mRTMessageField;
	private LinearLayout _rte_titleview;
	private FrameLayout _layout_top;
	private View _layout_toolbar;
	private PopupWindow _popupWindow;
	private int _popTop;
	private int _popLeft;
	private ImageButton _btn_tool_font;
	private ImageButton _btn_tool_audio;

	private RelativeLayout _audioRecodeView;
	private AudioPlayView _audioPlayView;

	private ScrollView _rte_content;

	public static final int WHAT_STOP_RECODE = 3;
	public static final int WHAT_STOP_PLAY = 4;

	private View activityRootView;
	private int keyHeight = 0;
	private boolean _isKeyBoardShow;

	private boolean _isRecoding;
	SineWave sw = null;
	private String _voiceName;
	private SoundMeter _sensor;
	private String _totalTime;
	private static final int POLL_INTERVAL = 1000 / 200;

	private Chronometer _timedown;

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
				_btn_tool_audio
						.setImageResource(R.drawable.selector_ic_toolbar_audio);
				AudioImage image = (AudioImage) msg.obj;
				String path = "file:///" + image.getPath();
				String audioPath = image.getAudioPath();
				mRTManager.insertAudio(path, audioPath);
				break;
			case WHAT_STOP_PLAY:
				_audioPlayView.getView().setVisibility(View.GONE);
				_rte_titleview.setVisibility(View.VISIBLE);
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
		if (savedInstanceState == null) {
			Intent intent = getIntent();
			message = getStringExtra(intent, "message");
		}

		super.onCreate(savedInstanceState);

		setContentView(R.layout.rte_layout_main);

		activityRootView = findViewById(R.id.root_layout);
		int screenHeight = this.getWindowManager().getDefaultDisplay()
				.getHeight();
		keyHeight = screenHeight / 3;

		FrameLayout _layout_top = (FrameLayout) findViewById(R.id.layout_top);
		_rte_titleview = (LinearLayout) findViewById(R.id.rte_titleview);
		_audioRecodeView = (RelativeLayout) findViewById(R.id.rte_audiorecoder);

		_audioPlayView = new AudioPlayView(this, _handler);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		_layout_top.addView(_audioPlayView.getView());
		_audioRecodeView.setVisibility(View.GONE);
		_audioPlayView.getView().setVisibility(View.GONE);
		Button btn_save = (Button) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);

		sw = new SineWave(this);
		_timedown = (Chronometer) _audioRecodeView.findViewById(R.id.timedown);
		_timedown.setFormat("%s");
		Button btn_stop = (Button) _audioRecodeView.findViewById(R.id.btn_stop);
		_sensor = new SoundMeter();
		btn_stop.setOnClickListener(this);

		_rte_content = (ScrollView) findViewById(R.id.rte_content);

		// initialize rich text manager
		RTApi rtApi = new RTApi(this, new RTProxyImpl(this),
				new RTMediaFactoryImpl(this, true));
		mRTManager = new RTManager(rtApi, savedInstanceState);
		mRTManager.setActivityHandler(_handler);

		_layout_toolbar = findViewById(R.id.layout_toolbar);
		_layout_toolbar.setVisibility(View.GONE);
		ImageButton btn_tool_keybord = (ImageButton) _layout_toolbar
				.findViewById(R.id.btn_tool_keybord);
		ImageButton btn_tool_camer = (ImageButton) _layout_toolbar
				.findViewById(R.id.btn_tool_camer);
		ImageButton btn_tool_image = (ImageButton) _layout_toolbar
				.findViewById(R.id.btn_tool_image);
		_btn_tool_audio = (ImageButton) _layout_toolbar
				.findViewById(R.id.btn_tool_audio);
		_btn_tool_font = (ImageButton) _layout_toolbar
				.findViewById(R.id.btn_tool_font);
		btn_tool_keybord.setOnClickListener(this);
		btn_tool_camer.setOnClickListener(this);
		btn_tool_image.setOnClickListener(this);
		_btn_tool_audio.setOnClickListener(this);
		_btn_tool_font.setOnClickListener(this);

		// register message editor
		mRTMessageField = (RTEditText) findViewById(R.id.rtEditText_1);
		mRTManager.registerEditor(mRTMessageField, true);
		if (message != null) {
			mRTMessageField.setRichTextEditing(true, message);
		}
		mRTMessageField
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus && _isKeyBoardShow) {
							scrollToBottom();
						}
					}
				});
		mRTMessageField.requestFocus();
	}

	private String getStringExtra(Intent intent, String key) {
		String s = intent.getStringExtra(key);
		return s == null ? "" : s;
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

	private void restart() {
		String message = mRTMessageField.getText(RTFormat.HTML);
		Intent intent = new Intent(this, getClass()).putExtra("message",
				message);
		startActivity(intent);
		finish();
	}

	private void playAudio(String audio) {
		_audioPlayView.startPlay(audio);
		// _audioPlayView.startPlay(Environment.getExternalStorageDirectory() +
		// Constants.DIR_AUDIO + "000.m4a");
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
			// System.out.println(mRTMessageField.getText(RTFormat.JSON));
			// System.out.println(mRTMessageField.getTextSize() + "");
			saveTextToJson();
			// String json =
			// "{\"runs\":[{\"attributes\":{\"font\":{\"fontName\":\".Helvetica Neue Interface\",\"fontWeight\":\"normal\",\"fontStyle\":\"normal\",\"fontSize\":14},\"foregroundColor\":\"rgb(0,0,0)\",\"paragraphStyle\":{\"alignment\":0,\"firstLineHeadIndent\":0,\"headIndent\":0,\"lineBreakMode\":0,\"lineHeightMultiple\":0,\"lineSpacing\":9,\"paragraphSpacing\":0,\"paragraphSpacingBefore\":0,\"tailIndent\":0},\"strikethrough\":0,\"underline\":0},\"range\":[0,14]}],\"string\":\"健康快乐\\n\\n吞吐量\\n\\n零距离\"}";
			// loadTextFromJson(json);
		} else if (id == R.id.btn_tool_audio) {
			_btn_tool_audio.setImageResource(R.drawable.ic_toolbar_audio_play);
			mRTManager.onStartAudioRecode();
		} else if (id == R.id.btn_tool_font) {
			if (_popupWindow != null && _popupWindow.isShowing()) {
				_popupWindow.dismiss();
				_btn_tool_font
						.setImageResource(R.drawable.selector_ic_toolbar_font);
			} else {
				showFontPop(_btn_tool_font);
				_btn_tool_font
						.setImageResource(R.drawable.ic_toolbar_font_press);
			}
		}
	}

	private void saveTextToJson() {
		int alignment = ParagraphStyle.ALIGNMENT_NORMAL;
		if (mRTMessageField.getIsJustify()) {
			alignment = ParagraphStyle.ALIGNMENT_FULL;
		} else {
			if (mRTMessageField.getLayout() != null) {
				Layout.Alignment a = mRTMessageField.getLayout().getAlignment();
				if (a == Layout.Alignment.ALIGN_CENTER) {
					alignment = ParagraphStyle.ALIGNMENT_CENTER;
				} else if (a == Layout.Alignment.ALIGN_OPPOSITE) {
					alignment = ParagraphStyle.ALIGNMENT_RIGHT;
				}
			}
		}
//		System.out.println(mRTMessageField.getText(RTFormat.JSON));
//		System.out.println(mRTMessageField.getText(RTFormat.HTML));
//		String json = new HtmlToJson(alignment)
//				.convertHtmlToJson(mRTMessageField.getText(RTFormat.JSON));
//		System.out.println(json);
	}

	private void loadTextFromJson(String json) {
		String html0 = new JsonToHtml().convertJsonToHtml(json);
		int alignment = Integer.parseInt(html0.substring(html0.length() - 1));
		String html = html0.substring(0, html0.length() - 1);
		mRTMessageField.setRichTextEditing(true, html);
		mRTMessageField.setSelection(mRTMessageField.length());
		mRTMessageField.append("\n");
		switch (alignment) {
		case ParagraphStyle.ALIGNMENT_NORMAL:
		case ParagraphStyle.ALIGNMENT_LEFT:
			mRTManager.onEffectSelected(Effects.ALIGNMENT,
					Layout.Alignment.ALIGN_NORMAL);
			break;
		case ParagraphStyle.ALIGNMENT_RIGHT:
			mRTManager.onEffectSelected(Effects.ALIGNMENT,
					Layout.Alignment.ALIGN_OPPOSITE);
			break;
		case ParagraphStyle.ALIGNMENT_CENTER:
			mRTManager.onEffectSelected(Effects.ALIGNMENT,
					Layout.Alignment.ALIGN_CENTER);
			break;
		case ParagraphStyle.ALIGNMENT_FULL:
			mRTManager.setJustify(true);
			break;
		default:
			break;
		}
	}

	private void hideKeyboard() {
		mRTMessageField.clearFocus();
		mRTMessageField.setFocusable(false);
		_layout_toolbar.setVisibility(View.GONE);
		hideSoftKeybord();
		mRTMessageField.setFocusable(true);
		mRTMessageField.setFocusableInTouchMode(true);
	}

	private void showAlertDialog(DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("音频操作正在进行中，是否退出音频操作？")
				.setPositiveButton("确定", listener)
				.setNegativeButton("取消", null).show();
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
		hideKeyboard();
	}

	private void openTakePhoto() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		SimpleDateFormat timeStampFormat = new SimpleDateFormat(
				"yyyy_MM_dd_HH_mm_ss");
		String filename = timeStampFormat.format(new Date());
		ContentValues values = new ContentValues();
		values.put(MediaStore.Video.Media.TITLE, filename);
		_protoUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, _protoUri);
		startActivityForResult(intent, REQUEST_CODE_GET_IMAGE_VIA_CAMERA);
	}

	private void openImgSelect() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_CODE_GET_IMG);
	}

	private void onSelectImg(String path) {
		if (!TextUtils.isEmpty(path)) {
			mRTManager.insertImage(path);
		}
	}

	private void onSelectImg(Uri selectedImage) {
		if (selectedImage != null) {
			String path = Utils.getImageAbsolutePath(this, selectedImage);
			mRTManager.insertImage(path);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
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
			_popupWindow = new PopupWindow(fontView.getView(),
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			// _popupWindow.setFocusable(true);
			_popupWindow.setBackgroundDrawable(new PaintDrawable(
					Color.TRANSPARENT));
			fontView.getView().measure(
					View.MeasureSpec.makeMeasureSpec(0,
							View.MeasureSpec.UNSPECIFIED),
					View.MeasureSpec.makeMeasureSpec(0,
							View.MeasureSpec.UNSPECIFIED));
			_popTop = location[1] - fontView.getView().getMeasuredHeight();
			_popLeft = (screenSize[0] - fontView.getView().getMeasuredWidth()) / 2;
			_popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, _popLeft,
					_popTop);
		} else {
			_popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, _popLeft,
					_popTop);
		}
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
	public void onLayoutChange(View v, int left, int top, int right,
			int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
			_isKeyBoardShow = true;
			_layout_toolbar.setVisibility(View.VISIBLE);
		} else if (oldBottom != 0 && bottom != 0
				&& (bottom - oldBottom > keyHeight)) {
			if (_popupWindow != null && _popupWindow.isShowing()) {
				_popupWindow.dismiss();
				_btn_tool_font
						.setImageResource(R.drawable.selector_ic_toolbar_font);
				_isKeyBoardShow = false;
			}
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
			// _handler.postDelayed(new Runnable() {
			// public void run() {
			// }
			// }, 300);
			_voiceName = System.currentTimeMillis() + ".3gp";
			startRecoder(_voiceName);
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

	private void startRecoder(String name) {
		_sensor.start(name);
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

	int cy = 0;
	boolean isAdd = true;

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
			sw.Set(cy, 1, 0);
		} else {
			sw.Set(y * 2 + 20, 1, 0);
		}
	}
}