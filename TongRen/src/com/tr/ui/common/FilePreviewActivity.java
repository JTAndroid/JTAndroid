package com.tr.ui.common;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavigate;
import com.tr.service.FileDownloadService;
import com.tr.service.FileDownloadService.MyBinder;
import com.tr.ui.base.JBaseActivity;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.FileDownloader;
import com.utils.common.OpenFiles;
import com.utils.time.Util;

/**
 * 文件预览
 * @author leon
 */
public class FilePreviewActivity extends JBaseActivity {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private ImageView typeIv;
	private TextView nameTv;
	private TextView sizeTv;
	private TextView controlTv;
	private TextView saveTv;
	private LinearLayout statusLl;
	private TextView progressTv;
	private ImageView cancelIv;
	private ProgressBar progressPb;
	private boolean isShowSaveButton;
	
	// 已下载文件管理器
	private IntentFilter mFilter;
	private JTFile mJTFile;
	
	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("文件详情");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_preview);
		initVars();
		initControls();
		doInit();
		initTypeIv();
	}
	
	private void initTypeIv()
	{
		String suffixName = mJTFile.getmSuffixName();
		int fileSourceId = 0;// 资源文件ID
		if (EUtil.PIC_FILE_STR.contains(suffixName)) {// 图片
			fileSourceId = R.drawable.picture_fang;
		} else if (EUtil.VIDEO_FILE_STR.contains(suffixName)) {// 视频
			fileSourceId = R.drawable.video_fang;
		} else if ("PDF".contains(suffixName)) {//pdf文件
			fileSourceId = R.drawable.pdf_fang;
		} else if ("PPT".contains(suffixName)
				|| "pptx".contains(suffixName)) {//ppt文件
			fileSourceId = R.drawable.ppt_fang;
		} else if ("XLS".contains(suffixName)
				|| "XLSX".equals(suffixName)) {//excel文件
			fileSourceId = R.drawable.file_excel_fang;
		} else if (EUtil.DOC_FILE_STR.contains(suffixName)) {//word文档
			fileSourceId = R.drawable.word_fang;
		} else if (EUtil.AUDIO_FILE_STR.contains(suffixName)) {//音频文件
			fileSourceId = R.drawable.file_audio_fang;
		} else if ("ZIP".contains(suffixName)
				|| "RAR".contains(suffixName)) {//压缩文件
			fileSourceId = R.drawable.file_zip;
		} else {
			fileSourceId = R.drawable.file_other;
		}
		typeIv.setBackgroundResource(fileSourceId);
	}
	
	
	private void initVars(){
		isShowSaveButton = getIntent().getBooleanExtra("isShowSaveButton", true);
		mJTFile = (JTFile) getIntent().getSerializableExtra(EConsts.Key.JT_FILE);
		mJTFile.setmType(JTFile.TYPE_FILE); // 统一按文件类型处理
		mFilter =  new IntentFilter();
		mFilter.addAction(EConsts.Action.DOWNLOAD_START);
		mFilter.addAction(EConsts.Action.DOWNLOAD_UPDATE);
		mFilter.addAction(EConsts.Action.DOWNLOAD_SUCCESS);
		mFilter.addAction(EConsts.Action.DOWNLOAD_FAILED);
		mFilter.addAction(EConsts.Action.DOWNLOAD_CANCELED);
	}

	private void initControls(){
		typeIv = (ImageView) findViewById(R.id.typeIv);
		nameTv = (TextView) findViewById(R.id.nameTv);
		nameTv.setText(mJTFile.mFileName);
		sizeTv = (TextView) findViewById(R.id.sizeTv);
		sizeTv.setText(EUtil.formatFileSize(mJTFile.mFileSize));
		controlTv = (TextView) findViewById(R.id.controlTv);
		controlTv.setOnClickListener(mClickListener);
		saveTv = (TextView)findViewById(R.id.saveTv);
		if (!isShowSaveButton) {
			saveTv.setVisibility(View.GONE);
		}
		saveTv.setOnClickListener(mClickListener);
		statusLl = (LinearLayout) findViewById(R.id.statusLl);
		progressTv = (TextView) findViewById(R.id.progressTv);
		cancelIv = (ImageView) findViewById(R.id.cancelIv);
		cancelIv.setOnClickListener(mClickListener);
		progressPb = (ProgressBar) findViewById(R.id.progressPb);
	}
	
	private void doInit(){
		
		Intent intent = new Intent(this,FileDownloadService.class);
		bindService(intent, mConn, BIND_AUTO_CREATE);
	}
	
	private OnClickListener mClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.controlTv: // 控制按钮
				if(controlTv.getText().equals("打开")){
					OpenFiles.open(FilePreviewActivity.this, EUtil.getAppCacheFileDir(FilePreviewActivity.this).getAbsolutePath() + File.separator + mJTFile.mFileName);
				}
				else{
					Intent intent = new Intent(EConsts.Action.DOWNLOAD_START);
					intent.setClass(FilePreviewActivity.this, FileDownloadService.class);
					intent.putExtra(EConsts.Key.JT_FILE, mJTFile);
					startService(intent);
				}
				break;
			case R.id.cancelIv: // 取消按钮
				Intent intent = new Intent(EConsts.Action.DOWNLOAD_CANCELED);
				intent.setClass(FilePreviewActivity.this, FileDownloadService.class);
				intent.putExtra(EConsts.Key.WEB_FILE_URL, mJTFile.mUrl);
				startService(intent);
				break;
			case R.id.saveTv://保存目录
				String fileIds;
				try {
					fileIds = Util.getDownloadIdByUrl(mJTFile.getmUrl());
					ENavigate.startFileManagementActivity(FilePreviewActivity.this,fileIds);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	
	@Override
	public void onResume(){
		super.onResume();
		registerReceiver(mReceiver, mFilter);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		unregisterReceiver(mReceiver);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unbindService(mConn);
	}
	
	private ServiceConnection mConn = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			FileDownloadService service = ((MyBinder) arg1).getService();
			boolean taskExist = false;
			for (FileDownloader downloader : service.getListDownloader()) {
				if (downloader.getJTFile().mUrl.equals(mJTFile.mUrl)) {
					taskExist = true;
					controlTv.setVisibility(View.GONE);
					statusLl.setVisibility(View.VISIBLE);
					progressTv.setText(downloader.getProgress() + "%");
					progressPb.setProgress(downloader.getProgress());
					break;
				}
			}
			if (!taskExist) {
				if (EUtil.isFileExist(FilePreviewActivity.this, mJTFile)) {
					controlTv.setText("打开");
					controlTv.setVisibility(View.VISIBLE);
					statusLl.setVisibility(View.GONE);
				} 
				else if(EUtil.getFileDownloadInfo(FilePreviewActivity.this, mJTFile.mUrl) != null){
					controlTv.setText("恢复下载");
					controlTv.setVisibility(View.VISIBLE);
					statusLl.setVisibility(View.GONE);
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
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context content, Intent intent) {
			if(intent == null || intent.getAction() == null){
				return;
			}
			String url = intent.getStringExtra(EConsts.Key.WEB_FILE_URL);
			if(url == null || !url.equals(mJTFile.mUrl)){
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
				
				JTFile jtfile = (JTFile) intent.getSerializableExtra(EConsts.Key.JT_FILE);
				mJTFile = jtfile;
				
				controlTv.setText("打开");
				controlTv.setVisibility(View.VISIBLE);
				statusLl.setVisibility(View.GONE);
				progressTv.setText("0%");
				progressPb.setProgress(0);
			}
			else if(action.equals(EConsts.Action.DOWNLOAD_FAILED)){
				
				controlTv.setText("恢复下载");
				controlTv.setVisibility(View.VISIBLE);
				statusLl.setVisibility(View.GONE);
				progressTv.setText("0%");
				progressPb.setProgress(0);
			}
			else if(action.equals(EConsts.Action.DOWNLOAD_CANCELED)){
				
				controlTv.setText("恢复下载");
				controlTv.setVisibility(View.VISIBLE);
				statusLl.setVisibility(View.GONE);
				progressTv.setText("0%");
				progressPb.setProgress(0);
			}
		}
	};
}
