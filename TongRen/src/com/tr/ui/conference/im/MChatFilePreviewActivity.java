package com.tr.ui.conference.im;

import java.io.File;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.tr.App;
import com.tr.R;
import com.tr.db.ChatLocalFileDBManager;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.service.HyChatFileDownloadService;
import com.tr.service.HyChatFileDownloadService.ServiceBinder;
import com.tr.ui.base.JBaseActivity;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.OpenFiles;

/**
 * 会议畅聊文件下载预览
 * @author leon
 */
public class MChatFilePreviewActivity extends JBaseActivity {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private LinearLayout backLl;
	private TextView titleTv;
	private ImageView typeIv;
	private TextView nameTv;
	private TextView sizeTv;
	private TextView controlTv;
	private LinearLayout statusLl;
	private TextView progressTv;
	private ImageView cancelIv;
	private ProgressBar progressPb;
	
	// 已下载文件管理器
	private IntentFilter broadcastFilter;
	private JTFile jtFile;
	private String messageId; // 消息id
	private long meetingId; // 会议id
	private long topicId; // 议题id
	private HyChatFileDownloadService downloadService;
	
	@Override
	public void initJabActionBar() {
		jabGetActionBar().hide();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_activity_chat_file_preview);
		initVars();
		initControls();
		// 绑定下载服务
		bindService(new Intent(this, HyChatFileDownloadService.class) , serviceConnection, BIND_AUTO_CREATE);
	}
	
	private void initVars(){
		jtFile = (JTFile) getIntent().getSerializableExtra(ENavConsts.EJTFile);
		messageId = getIntent().getStringExtra(ENavConsts.EMessageID);
		meetingId = getIntent().getLongExtra(ENavConsts.EMeetingId, 0);
		topicId = getIntent().getLongExtra(ENavConsts.ETopicId, 0);
		broadcastFilter =  new IntentFilter();
		broadcastFilter.addAction(EConsts.Action.DOWNLOAD_START);
		broadcastFilter.addAction(EConsts.Action.DOWNLOAD_UPDATE);
		broadcastFilter.addAction(EConsts.Action.DOWNLOAD_SUCCESS);
		broadcastFilter.addAction(EConsts.Action.DOWNLOAD_FAILED);
		broadcastFilter.addAction(EConsts.Action.DOWNLOAD_CANCELED);
	}

	private void initControls(){
		
		// 返回
		backLl = (LinearLayout) findViewById(R.id.hy_layoutTitle_backBtn);
		backLl.setOnClickListener(mClickListener);
		// 标题
		titleTv = (TextView) findViewById(R.id.hy_layoutTitle_title);
		titleTv.setText("文件预览");
		// 文件类型
		typeIv = (ImageView) findViewById(R.id.typeIv);
		// 文件名
		nameTv = (TextView) findViewById(R.id.nameTv);
		nameTv.setText(jtFile.mFileName);
		// 文件大小
		sizeTv = (TextView) findViewById(R.id.sizeTv);
		sizeTv.setText(EUtil.formatFileSize(jtFile.mFileSize));
		// 下载控制
		controlTv = (TextView) findViewById(R.id.controlTv);
		controlTv.setOnClickListener(mClickListener);
		// 下载状态
		statusLl = (LinearLayout) findViewById(R.id.statusLl);
		// 下载进度文本显示
		progressTv = (TextView) findViewById(R.id.progressTv);
		// 取消下载
		cancelIv = (ImageView) findViewById(R.id.cancelIv);
		cancelIv.setOnClickListener(mClickListener);
		// 下载进度条
		progressPb = (ProgressBar) findViewById(R.id.progressPb);
	}
	
	private OnClickListener mClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.hy_layoutTitle_backBtn: // 返回
				finish();
				break;
			case R.id.controlTv: // 控制按钮
				if(controlTv.getText().equals("打开")){ // 打开文件
					File dir = EUtil.getMeetingChatFileDir(MChatFilePreviewActivity.this, jtFile.mType, meetingId, topicId);
					if(dir != null){
						File file = new File(dir, jtFile.mFileName);
						OpenFiles.open(MChatFilePreviewActivity.this, file.getAbsolutePath());
					}
					else{
						showToast("无法访问文件存储路径");
					}
				}
				else{
					if(downloadService != null){
						downloadService.startNewTask(messageId, jtFile, meetingId, topicId);
					}
					else{
						showToast("未初始化绑定的下载服务");
					}
				}
				break;
			case R.id.cancelIv: // 取消下载
				if(downloadService != null){
					downloadService.cancelTask(messageId);
				}
				else{
					showToast("未初始化绑定的下载服务");
				}
			}
		}
	};
	
	@Override
	public void onResume(){
		super.onResume();
		registerReceiver(broadcastReceiver, broadcastFilter);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unbindService(serviceConnection);
	}
	
	// 服务连接器
	private ServiceConnection serviceConnection = new ServiceConnection(){
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder) {
			// 获取下载服务对象
			downloadService = ((ServiceBinder) binder).getService();
			if(downloadService.isTaskExist(messageId)){ // 正在下载
				int progress = downloadService.getTaskProgress(messageId);
				controlTv.setVisibility(View.GONE);
				statusLl.setVisibility(View.VISIBLE);
				progressTv.setText(progress + "%");
				progressPb.setProgress(progress);
			}
			else{
				// 原始文件是否存在
				ChatLocalFileDBManager localFileDBManager = new ChatLocalFileDBManager(MChatFilePreviewActivity.this);
				String localPath = localFileDBManager.query(App.getUserID(), messageId);
				if(!TextUtils.isEmpty(localPath)
						&& new File(localPath).exists()){
					controlTv.setText("打开");
					controlTv.setVisibility(View.VISIBLE);
					statusLl.setVisibility(View.GONE);
					return;
				}
				// 下载的文件状态
				File dir = EUtil.getMeetingChatFileDir(MChatFilePreviewActivity.this, jtFile.mType, meetingId, topicId);
				if(dir != null){
					File localFile = new File(dir, jtFile.mFileName);
					if(localFile.exists()){
						if(localFile.length() < jtFile.mFileSize){
							controlTv.setText("继续下载");
							controlTv.setVisibility(View.VISIBLE);
							statusLl.setVisibility(View.GONE);
						}
						else if(localFile.length() == jtFile.mFileSize){
							controlTv.setText("打开");
							controlTv.setVisibility(View.VISIBLE);
							statusLl.setVisibility(View.GONE);
						}
						else{
							controlTv.setText("开始下载");
							controlTv.setVisibility(View.VISIBLE);
							statusLl.setVisibility(View.GONE);
						}
					}
					else{
						controlTv.setText("开始下载");
						controlTv.setVisibility(View.VISIBLE);
						statusLl.setVisibility(View.GONE);
					}
				}
				else{
					controlTv.setText("开始下载");
					controlTv.setVisibility(View.VISIBLE);
					statusLl.setVisibility(View.GONE);
				}
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			
		}
	};
	
	// 下载监听器
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context content, Intent intent) {
			
			if(intent == null || TextUtils.isEmpty(intent.getAction())){
				return;
			}
			String msgId = intent.getStringExtra(EConsts.Key.MESSAGE_ID);
			if(TextUtils.isEmpty(msgId) || !msgId.equals(messageId)){
				return;
			}
			String action = intent.getAction();
			if(action.equals(EConsts.Action.DOWNLOAD_START)){
				
				controlTv.setVisibility(View.GONE);
				statusLl.setVisibility(View.VISIBLE);
				progressTv.setText("0%");
				progressPb.setProgress(0);
			}
			else if(action.equals(EConsts.Action.DOWNLOAD_UPDATE)){
				
				int progress = intent.getIntExtra(EConsts.Key.PROGRESS_UPDATE, 0);
				controlTv.setVisibility(View.GONE);
				statusLl.setVisibility(View.VISIBLE);
				progressTv.setText(progress + "%");
				progressPb.setProgress(progress);
			}
			else if(action.equals(EConsts.Action.DOWNLOAD_SUCCESS)){
				
				jtFile = (JTFile) intent.getSerializableExtra(EConsts.Key.JT_FILE);
				
				controlTv.setText("打开");
				controlTv.setVisibility(View.VISIBLE);
				statusLl.setVisibility(View.GONE);
				progressTv.setText("0%");
				progressPb.setProgress(0);
			}
			else if(action.equals(EConsts.Action.DOWNLOAD_FAILED)){
				
				controlTv.setText("继续下载");
				controlTv.setVisibility(View.VISIBLE);
				statusLl.setVisibility(View.GONE);
				progressTv.setText("0%");
				progressPb.setProgress(0);
			}
			else if(action.equals(EConsts.Action.DOWNLOAD_CANCELED)){
				
				controlTv.setText("继续下载");
				controlTv.setVisibility(View.VISIBLE);
				statusLl.setVisibility(View.GONE);
				progressTv.setText("0%");
				progressPb.setProgress(0);
			}
		}
	};

}
